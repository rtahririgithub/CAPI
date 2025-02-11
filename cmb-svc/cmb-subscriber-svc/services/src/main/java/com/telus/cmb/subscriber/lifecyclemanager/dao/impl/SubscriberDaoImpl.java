package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.rmi.RemoteException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;

import amdocs.APILink.datatypes.ActivityInfo;
import amdocs.APILink.datatypes.AppliedDiscountInfo;
import amdocs.APILink.datatypes.BrandPortInfo;
import amdocs.APILink.datatypes.CancelInfo;
import amdocs.APILink.datatypes.FleetSecuredInfo;
import amdocs.APILink.datatypes.MigrateM2PInfo;
import amdocs.APILink.datatypes.MigrateReserveInfo;
import amdocs.APILink.datatypes.NameInfo;
import amdocs.APILink.datatypes.ProductActivityInfo;
import amdocs.APILink.datatypes.ProductAdditionalInfo;
import amdocs.APILink.datatypes.ProductCommitmentInfo;
import amdocs.APILink.datatypes.ProductDepositInfo;
import amdocs.APILink.datatypes.ProductServicesInfo;
import amdocs.APILink.datatypes.ProductServicesInfoSys;
import amdocs.APILink.datatypes.ProductServicesValidationInfo;
import amdocs.APILink.datatypes.SearchSubscriber;
import amdocs.APILink.datatypes.ServiceFeatureInfo;
import amdocs.APILink.datatypes.ServiceVoiceAllocationInfo;
import amdocs.APILink.datatypes.SocInfo;
import amdocs.APILink.datatypes.UFMIInfo;
import amdocs.APILink.datatypes.UpdateFleetInfo;
import amdocs.APILink.datatypes.UpdateProductActivityInfo;
import amdocs.APILink.datatypes.UpdateProductAdditionalInfo;
import amdocs.APILink.datatypes.VoiceAllocationInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.NewProductConv;
import amdocs.APILink.sessions.interfaces.SearchServices;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;
import amdocs.APILink.sessions.interfaces.UpdateProductConv;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.ServicesValidation;
import com.telus.api.fleet.Fleet;
import com.telus.api.portability.PortInEligibility;
import com.telus.cmb.common.aspects.LogInvocation;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.common.dao.util.VoiceAirTimeMapping;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.common.util.Utility;
import com.telus.cmb.subscriber.lifecyclemanager.dao.SubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.amdocs.AmdocsConvBeanWrapper;
import com.telus.cmb.subscriber.utilities.AmdocsConvBeanClassFactory;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.framework.exception.TelusApplicationException;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.ExceptionInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;

public class SubscriberDaoImpl extends AmdocsDaoSupport implements SubscriberDao{

	protected static String phoneNumberWildCards = "**********";
	protected boolean portInMSISDN = false;
	
	private final Logger LOGGER = Logger.getLogger(SubscriberDaoImpl.class);

	private MultipleJdbcDaoTemplateSupport jdbcTemplateSupport;

	public void setJdbcTemplateSupport(
			MultipleJdbcDaoTemplateSupport jdbcTemplateSupport) {
		this.jdbcTemplateSupport = jdbcTemplateSupport;
	}

	@Override
	public void saveSubscriptionPreference(
			final SubscriptionPreferenceInfo preferenceInfo, final String user) {

		String sqlUpdatePreference="{call subscriber_pref_pkg.updatesubscriberpreference (?,?,?,?,?,?)}";
		String sqlSavePreference="{call subscriber_pref_pkg.savesubscriberpreference (?,?,?,?,?)}";
		String sql="";

		final boolean hasPrefernceId=preferenceInfo.getSubscriberPreferenceId()>0?true:false;
		sql=hasPrefernceId?sqlUpdatePreference:sqlSavePreference;

		jdbcTemplateSupport.getCodsJdbcTemplate().execute(sql, new CallableStatementCallback<Object>() {

			@Override
			public Object doInCallableStatement(CallableStatement callstmt)
			throws SQLException, DataAccessException {

				int i=1;
				if(hasPrefernceId){
					callstmt.setLong(i,  preferenceInfo.getSubscriberPreferenceId());
					i++;
				}
				callstmt.setLong(i,  preferenceInfo.getSubscriptionId() ); i++;
				callstmt.setInt(i, preferenceInfo.getPreferenceTopicId()); i++;
				callstmt.setInt(i, preferenceInfo.getSubscrPrefChoiceSeqNum() ); i++;
				callstmt.setString(i, preferenceInfo.getPreferenceValueTxt() ); i++;
				callstmt.setString(i, user );
				callstmt.execute();
				return true;
			}
		});
	}

	@Override
	public DiscountInfo[] retrieveDiscounts(final int ban, final String subscriberId, final String productType, String sessionId) throws ApplicationException {		
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<DiscountInfo[]>() {

			@Override
			public DiscountInfo[] doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				DiscountInfo[] discounts = new DiscountInfo[0];
				AppliedDiscountInfo[] amdocsAppliedDiscountInfoArray = null;				

				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));

				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateProductConv.setProductPK(ban, subscriberId);

				amdocsAppliedDiscountInfoArray = amdocsUpdateProductConv.getAppliedDiscounts();

				// map amdocs info class to telus info class
				if (amdocsAppliedDiscountInfoArray != null) {
					discounts = new com.telus.eas.framework.info.DiscountInfo[amdocsAppliedDiscountInfoArray.length];
					for (int i=0; i < amdocsAppliedDiscountInfoArray.length; i++) {
						discounts[i] = new com.telus.eas.framework.info.DiscountInfo();
						discounts[i].setBan(ban);
						discounts[i].setSubscriberId(subscriberId);
						discounts[i].setProductType(amdocsUpdateProductConv.getProductType());
						discounts[i].setDiscountCode(amdocsAppliedDiscountInfoArray[i].discountKey.code);
						discounts[i].setEffectiveDate(amdocsAppliedDiscountInfoArray[i].discountKey.effectiveDate);
						discounts[i].setExpiryDate(amdocsAppliedDiscountInfoArray[i].discountKey.expirationDate);
						discounts[i].setDiscountSequenceNo(amdocsAppliedDiscountInfoArray[i].discountKey.sequenceNumber);
						discounts[i].setDiscountByUserId(String.valueOf(amdocsAppliedDiscountInfoArray[i].discountByUserId) );

					}
				}
				return discounts;
			}

		});	
	}

	@Override
	public CancellationPenaltyInfo retrieveCancellationPenalty(final int ban,
			final String subscriberId, final String productType, String sessionId)
	throws ApplicationException {
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CancellationPenaltyInfo>() {

			@Override
			public CancellationPenaltyInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				CancellationPenaltyInfo cancellationPenaltyInfo = new CancellationPenaltyInfo();

				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));

				amdocsUpdateProductConv.setProductPK(ban, subscriberId);

				// retrieve discounts
				amdocs.APILink.datatypes.CancellationPenaltyInfo amdocsCancellationPenaltyInfo = amdocsUpdateProductConv.getCancellationPenaltyInfo();

				// map amdocs info class to telus info class
				cancellationPenaltyInfo.setDepositAmount(amdocsCancellationPenaltyInfo.totalDepositAmount);
				cancellationPenaltyInfo.setDepositInterest(amdocsCancellationPenaltyInfo.totalDepositInterest);
				cancellationPenaltyInfo.setPenalty(amdocsCancellationPenaltyInfo.totalPenalty);
				cancellationPenaltyInfo.setSubscriberNumber(amdocsCancellationPenaltyInfo.subscriberNumber);

				return cancellationPenaltyInfo ;
			}

		});
	}

	protected String getWildCard(String phoneNumberPattern) {
		String wildCard = phoneNumberPattern;  

		if (wildCard.length() < 10)
			wildCard.concat(phoneNumberWildCards.substring(0,10 - wildCard.length()));

		return wildCard;
	}

	protected String getWildCardAmdocs(String phoneNumberPattern) {
		String wildCard = getWildCard(phoneNumberPattern);

		// Replace the wildcard character (*) with percentage signs (%) before making the call to Amdocs
		String wildCardAmdocs = wildCard.replace('*', '%');

		return wildCardAmdocs;
	}

	protected String[] returnAvailablePhoneNumbers(String[] phoneNumbers) throws ApplicationException {
		if (phoneNumbers.length == 0) {
			throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS, "No available phone numbers found.","");
		}

		return phoneNumbers;
	}

	protected String[] retrievePhoneNumbers(Object updateConv, final int ban, 
			final PhoneNumberReservation phoneNumberReservation, final int maxNumbers) throws RemoteException, ValidateException {
		String startFromPhoneNumberInternal = "0";
		Collection<String> phoneNumbersFound = new ArrayList<String>();
		boolean morePhoneNumbersAvailable = true;		

		// loop until the requested number of phone numbers is found OR no more numbers are
		// available
		while (phoneNumbersFound.size() < maxNumbers && morePhoneNumbersAvailable) {					
			String[] phoneNumbers = AmdocsConvBeanWrapper.getAvailablePhoneNumbers(updateConv
					, phoneNumberReservation 
					, getWildCardAmdocs(phoneNumberReservation.getPhoneNumberPattern())
					, startFromPhoneNumberInternal);

			if (phoneNumbers == null || phoneNumbers.length  < 10)
				morePhoneNumbersAvailable = false;

			if (phoneNumbers != null && phoneNumbers.length > 0) {
				for (int i=0; i < phoneNumbers.length; i++) {
					phoneNumbersFound.add(phoneNumbers[i]);
					if (phoneNumbersFound.size() == maxNumbers)
						break;
				}
				startFromPhoneNumberInternal = phoneNumbers[phoneNumbers.length-1];
			}							
		}

		return phoneNumbersFound.toArray(new String[phoneNumbersFound.size()]);
	}	

	protected StringBuffer appendSubscriberInfo( StringBuffer sb, SubscriberInfo subInfo )
	{
		if ( subInfo!=null ) {
			sb.append( "  Subscriber[ban:").append(subInfo.getBanId());
			if ( subInfo.getSubscriberId()!=null && subInfo.getSubscriberId().trim().length()>0)
				sb.append(", subId:").append( subInfo.getSubscriberId() );
			sb.append( ", phone:").append( subInfo.getPhoneNumber() )
			.append("]");
		}
		return sb;
	}

	protected final String genNoAvailablePhoneNumberMessage( PhoneNumberReservation phoneNumberReservation) {
		StringBuffer sb  = new StringBuffer("No phone numbers available matching criteria: ")
		.append( "NL[").append( phoneNumberReservation.getNumberGroup().getNumberLocation()).append("]")
		.append( " NGP[").append( phoneNumberReservation.getNumberGroup().getCode()).append("]")
		.append(" pattern[").append( phoneNumberReservation.getPhoneNumberPattern()).append("]")
		.append(" asian[").append( phoneNumberReservation.isAsian() ).append("]");
		return sb.toString();
	}

	protected MigrateReserveInfo newMigrateReserveInfo(SubscriberInfo subscriberInfo, MigrationRequestInfo migrationRequestInfo) {
		MigrateReserveInfo reserveInfo = new MigrateReserveInfo();
		reserveInfo.dealerOfRecord = migrationRequestInfo.getDealerCode();
		reserveInfo.salesPersonOfRecord = migrationRequestInfo.getSalesRepCode();
		reserveInfo.migrationType = migrationRequestInfo.getMigrationType().getCode();
		reserveInfo.phoneNumber = subscriberInfo.getPhoneNumber();
		reserveInfo.previousBan = subscriberInfo.getBanId();
		reserveInfo.previousSubscriber = subscriberInfo.getSubscriberId();
		return reserveInfo;
	}

	protected ProductActivityInfo newProductActivityInfo(Date activityDate, MigrationRequestInfo migrationRequestInfo) {
		ProductActivityInfo productActivityInfo = new ProductActivityInfo();
		productActivityInfo.dealerCode = migrationRequestInfo.getDealerCode();
		productActivityInfo.salesCode = migrationRequestInfo.getSalesRepCode();
		productActivityInfo.activityDate = activityDate;
		productActivityInfo.activityReason = migrationRequestInfo.getMigrationReasonCode();
		return productActivityInfo;
	}



	protected MigrateM2PInfo newMigrateM2PInfo(Date activityDate, MigrationRequestInfo migrationRequestInfo) {
		final char COL_DEPOSIT_METHOD_RECEIVED = 'S';
		final char COL_DEPOSIT_METHOD_HELD_BY_DEALER = 'D';
		final char DEPOSIT_RET_METHOD_REFUND_ALL = 'R';

		MigrateM2PInfo migrateInfo = new MigrateM2PInfo();

		CancelInfo cancelInfo = new CancelInfo();
		cancelInfo.activityDate = activityDate;
		cancelInfo.activityReason = migrationRequestInfo.getMigrationReasonCode();
		cancelInfo.userText = migrationRequestInfo.getUserMemoText();
		//CancelInfo.depositReturnMethod: This field can have three values, and should be passed as a byte value.
		//  -Refund the entire amount of the deposit, pass 'R'
		//  -Refund the excess amount of the deposit, pass 'E'
		//  -Apply the deposit amount to cover open debts, pass 'O'
		cancelInfo.depositReturnMethod = DEPOSIT_RET_METHOD_REFUND_ALL;

		migrateInfo.migrateCancelInfo = cancelInfo;

		migrateInfo.migrateDepositInd =  migrationRequestInfo.isDepositTransferInd();

		if (migrateInfo.migrateDepositInd ==false ) {
			ProductDepositInfo depositInfo = new ProductDepositInfo();
			migrateInfo.nonTransferDepositInfo = depositInfo;
			depositInfo.depositKept = migrationRequestInfo.isDealerAccepteddeposit();

			//ProductDepositInfo.collectDepositMethod: This field can also have three values, and should be passed as a byte value.
			//  -Most common is 'S', indicating that payment has been received for the deposit.
			//  -If the amount is to be applied against the customer's bill, then a value of 'B' would be passed.
			//  -If the deposit has been held/kept by the dealer, then this would be 'D'.
			if ( depositInfo.depositKept)
				depositInfo.collectDepositMethod=COL_DEPOSIT_METHOD_HELD_BY_DEALER;
			else
				depositInfo.collectDepositMethod=COL_DEPOSIT_METHOD_RECEIVED;
		}
		return migrateInfo;
	}

	protected void increaseExpectedNumberOfSubscribers(UpdateBanConv pUpdateBanConv
			, int pBan, int pUrbanId, int pFleetId) throws ValidateException, RemoteException, TelusException {

		UpdateFleetInfo amdocsUpdateFleetInfo = new UpdateFleetInfo();

		// Set BanPK (which also retrieves the BAN)
		pUpdateBanConv.setBanPK(pBan);

		// increase the number of subscribers by 1
		// - get list of fleets associated with this ban
		// - find fleet in question
		// - increase expectedSubscriberNumber by 1
		// (NOTE: future improvment would be to get a method that returns a specific fleet)
		amdocs.APILink.datatypes.FleetInfo[] associatedFleets = pUpdateBanConv.getFleetList();
		int fleetFound = -1;
		if (associatedFleets != null) {
			for (int i=0; i < associatedFleets.length; i++) {
				if (associatedFleets[i].urbanId == pUrbanId &&
						associatedFleets[i].fleetId == pFleetId) {
					fleetFound = i;
					break;
				}
			}
		}
		if (fleetFound < 0)
			throw new TelusApplicationException(new ExceptionInfo("VAL10011","Fleet not associated to BAN!"));

		// Populated UpdateFleetInfo
		amdocsUpdateFleetInfo.urbanId = associatedFleets[fleetFound].urbanId;
		amdocsUpdateFleetInfo.fleetId = associatedFleets[fleetFound].fleetId;
		amdocsUpdateFleetInfo.expectedSubscriberNumber = associatedFleets[fleetFound].expectedSubscriberNumber + 1;
		amdocsUpdateFleetInfo.scchInd = (byte)'S';
		LOGGER.debug("Excecuting modifyFleet() - start...");
		if (associatedFleets[fleetFound].fleetType == Fleet.TYPE_PUBLIC) {
			pUpdateBanConv.modifyFleet(amdocsUpdateFleetInfo);
		} else {
			FleetSecuredInfo amdocsFleetSecuredInfo = new FleetSecuredInfo();
			pUpdateBanConv.modifyFleet(amdocsUpdateFleetInfo,amdocsFleetSecuredInfo);
		}

		LOGGER.debug("Excecuting modifyFleet() - end...");
		LOGGER.debug("Leaving...");

	}

	@Override
	public void suspendSubscriber(final SubscriberInfo subscriberInfo,
			final Date activityDate, final String activityReasonCode, final String userMemoText,
			String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(
						AmdocsConvBeanClassFactory.getUpdateProductConvBean(subscriberInfo.getProductType()));

				ActivityInfo activityInfo = new ActivityInfo();

				// Set ProductPK
				amdocsUpdateProductConv.setProductPK(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());

				// Populate ActivityInfo
				activityInfo.activityDate = activityDate == null ? new Date() : activityDate;
				activityInfo.activityReason = activityReasonCode;
				activityInfo.userText = userMemoText == null ? "" : userMemoText;

				// store
				amdocsUpdateProductConv.suspendSubscriber(activityInfo);

				return null;

			}
		});

	}	
	
	@Override
	public void updatePortRestriction(final int ban, final String subscriberNo,
			final boolean restrictPort, final String userID) throws ApplicationException {
		String sql = "{call eas_utility_pkg.UpdatePortRestriction (?,?,?,?)}";
		jdbcTemplateSupport.getCodsJdbcTemplate().execute(sql, new CallableStatementCallback<Object>() {

			@Override
			public Object doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {

				callable.setInt(1,  ban);
				callable.setString(2, subscriberNo);
				callable.setString(3, restrictPort ? "Y" : "N");
				callable.setInt(4, Integer.parseInt(userID));

				callable.execute();

				return null;
			}
		});

	}

	@Override
	public SubscriberInfo updateSubscriber(final SubscriberInfo subscriberInfo, String sessionId)
	throws ApplicationException {
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<SubscriberInfo>() {

			@Override
			public SubscriberInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(
						subscriberInfo.getProductType()));
				
				ProductAdditionalInfo amdocsProductAdditionalInfo = new ProductAdditionalInfo();
				ProductActivityInfo amdocsProductActivityInfo = new ProductActivityInfo();
				UpdateProductAdditionalInfo amdocsUpdateProductAdditionalInfo = new UpdateProductAdditionalInfo();
				UpdateProductActivityInfo amdocsUpdateProductActivityInfo = new UpdateProductActivityInfo();
				NameInfo amdocsNameInfo = new NameInfo();
				boolean updateNecessary = false;


				// Set ProductPK (which also retrieves the BAN)
				amdocsUpdateProductConv.setProductPK(subscriberInfo.getBanId(),subscriberInfo.getSubscriberId());

				// Change miscellaneous subscriber attributes
				// - email address
				// - subscriber alias
				// - subscriber language preference
				/* amdocsProductAdditionalInfo = amdocsUpdateProductConv.getProductAdditionalInfo();

			      if (Utility.compare(amdocsProductAdditionalInfo.emailAddress,pSubscriberInfo.getEmailAddress()) != 0  ) {
			        updateNecessary = true;
			        amdocsUpdateProductAdditionalInfo.emailAddress = AttributeTranslator.emptyFromNull(pSubscriberInfo.getEmailAddress());
			      }
			      if (Utility.compare(amdocsProductAdditionalInfo.subLangPref,pSubscriberInfo.getLanguage()) != 0  ) {
			        updateNecessary = true;
			        amdocsUpdateProductAdditionalInfo.subLangPref = AttributeTranslator.emptyFromNull(pSubscriberInfo.getLanguage());
			      }
			      if (Utility.compare(AttributeTranslator.stringFrombyte(amdocsProductAdditionalInfo.sortOrder),pSubscriberInfo.getInvoiceCallSortOrderCode()) != 0  ) {
			        updateNecessary = true;
			        amdocsUpdateProductAdditionalInfo.sortOrder = AttributeTranslator.byteFromString(pSubscriberInfo.getInvoiceCallSortOrderCode());
			        LOGGER.debug("after update:amdocsUpdateProductAdditionalInfo.sortOrder:"+ amdocsUpdateProductAdditionalInfo.sortOrder);
			      }
			      if (pSubscriberInfo.isIDEN()) {
			        IDENSubscriberInfo idenSubscriberInfo = (IDENSubscriberInfo)pSubscriberInfo;
			        if (Utility.compare(amdocsProductAdditionalInfo.subscriberAlias,idenSubscriberInfo.getSubscriberAlias()) != 0 ) {
			          updateNecessary = true;
			          amdocsUpdateProductAdditionalInfo.subscriberAlias = AttributeTranslator.emptyFromNull(idenSubscriberInfo.getSubscriberAlias());
			        }
			      }
			      if (updateNecessary) {
			        LOGGER.debug("Excecuting changeSubscriber(UpdateProductAdditionalInfo) - start...");
			        amdocsUpdateProductConv.changeSubscriber(amdocsUpdateProductAdditionalInfo);
			        LOGGER.debug("Excecuting changeSubscriber(UpdateProductAdditionalInfo) - end...");
			      }*/

				// Change dealer / sales rep
				amdocsProductActivityInfo = amdocsUpdateProductConv.getProductActivityInfo();
				if (Utility.compare(amdocsProductActivityInfo.dealerCode,subscriberInfo.getDealerCode()) != 0  ) {
					updateNecessary = true;
					amdocsUpdateProductActivityInfo.dealerCode = AttributeTranslator.emptyFromNull(subscriberInfo.getDealerCode());
				}
				if (Utility.compare(amdocsProductActivityInfo.salesCode,subscriberInfo.getSalesRepId()) != 0  ) {
					updateNecessary = true;
					amdocsUpdateProductActivityInfo.salesCode = AttributeTranslator.emptyFromNull(subscriberInfo.getSalesRepId());
				}
				if (updateNecessary) {
					amdocsUpdateProductConv.changeSubscriber(amdocsUpdateProductActivityInfo);
					updateNecessary = false;
				}

				// Change miscellaneous subscriber attributes
				// - email address
				// - subscriber alias
				// - subscriber language preference
				amdocsProductAdditionalInfo = amdocsUpdateProductConv.getProductAdditionalInfo();

				if (Utility.compare(amdocsProductAdditionalInfo.emailAddress,subscriberInfo.getEmailAddress()) != 0  ) {
					updateNecessary = true;
					amdocsUpdateProductAdditionalInfo.emailAddress = AttributeTranslator.emptyFromNull(subscriberInfo.getEmailAddress());
				}
				if (Utility.compare(amdocsProductAdditionalInfo.subLangPref,subscriberInfo.getLanguage()) != 0  ) {
					updateNecessary = true;
					amdocsUpdateProductAdditionalInfo.subLangPref = AttributeTranslator.emptyFromNull(subscriberInfo.getLanguage());
				}
				if (subscriberInfo.getInvoiceCallSortOrderCode() != null &&
						Utility.compare(AttributeTranslator.stringFrombyte(amdocsProductAdditionalInfo.sortOrder),subscriberInfo.getInvoiceCallSortOrderCode()) != 0  ) {
					updateNecessary = true;
					amdocsUpdateProductAdditionalInfo.sortOrder = AttributeTranslator.byteFromString(subscriberInfo.getInvoiceCallSortOrderCode());

				}
				if (subscriberInfo.isIDEN()) {
					IDENSubscriberInfo idenSubscriberInfo = (IDENSubscriberInfo)subscriberInfo;
					if (Utility.compare(amdocsProductAdditionalInfo.subscriberAlias,idenSubscriberInfo.getSubscriberAlias()) != 0 ) {
						updateNecessary = true;
						amdocsUpdateProductAdditionalInfo.subscriberAlias = AttributeTranslator.emptyFromNull(idenSubscriberInfo.getSubscriberAlias());
					}
				}

				if (updateNecessary) {
					LOGGER.debug("Excecuting changeSubscriber(UpdateProductAdditionalInfo) - start...");
					amdocsUpdateProductConv.changeSubscriber(amdocsUpdateProductAdditionalInfo);
					LOGGER.debug("Excecuting changeSubscriber(UpdateProductAdditionalInfo) - end...");
				}

				// Change user name
				amdocsNameInfo = amdocsUpdateProductConv.getNameInfo();
				if (Utility.compare(amdocsNameInfo.firstName, subscriberInfo.getConsumerName().getFirstName()) != 0 ||
						Utility.compare(amdocsNameInfo.middleInitial, subscriberInfo.getConsumerName().getMiddleInitial()) != 0 ||
						Utility.compare(amdocsNameInfo.lastBusinessName, subscriberInfo.getConsumerName().getLastName()) != 0 ||
						Utility.compare(amdocsNameInfo.nameTitle, subscriberInfo.getConsumerName().getTitle()) != 0 ||
						Utility.compare(amdocsNameInfo.nameSuffix, subscriberInfo.getConsumerName().getGeneration()) != 0 ||
						Utility.compare(amdocsNameInfo.additionalTitle, subscriberInfo.getConsumerName().getAdditionalLine()) != 0) {

					amdocsNameInfo.firstName = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getFirstName());
					amdocsNameInfo.middleInitial = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getMiddleInitial());
					amdocsNameInfo.lastBusinessName = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getLastName());
					amdocsNameInfo.nameTitle = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getTitle());
					amdocsNameInfo.nameSuffix = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getGeneration());
					amdocsNameInfo.additionalTitle = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getAdditionalLine());

					LOGGER.debug("Excecuting changeUserName - start...");        
					amdocsUpdateProductConv.changeUserName(amdocsNameInfo, AttributeTranslator.byteFromString(subscriberInfo.getConsumerName().getNameFormat()));
					LOGGER.debug("Excecuting changeUserName - end...");
				}

				LOGGER.debug("Leaving...");
				return subscriberInfo;
			}
		});		
	}

	@Override
	public void updateSubscriptionRole(final int ban, final String subscriberNo,
			final String subscriptionCode, final String dealerCode, final String salesRepCode,
			final String csrId) throws ApplicationException {
		String myInsertString = "begin " +
        "update STAGED_SUBSCRIBER_ROLE srs "  +
        "SET srs.subscription_role_code=?, " +
        "srs.assigned_at_outlet_code=?, " +
        "srs.assigned_by_sales_rep_id=?, " +
        "srs.assigned_by_csr_id=?, " +
        "srs.refresh_ts=SYSDATE, " +
        "srs.effective_timestamp=SYSDATE " +
        "where srs.ban=? AND " +
        "subscriber_no=?; " +
        "if SQL%ROWCOUNT <> 1 then " +
        "insert into  STAGED_SUBSCRIBER_ROLE srs " +
        "(ban, " +
        "subscriber_no, " +
        "subscription_role_code, " +
        "assigned_at_outlet_code, " +
        "assigned_by_sales_rep_id, " +
        "assigned_by_csr_id, " +
        "refresh_ts, " +
        "effective_timestamp) " +
        "values (" +
        "?,?,?,?,?,?,SYSDATE,SYSDATE);" +
        "end if;" +
        "end;";
		jdbcTemplateSupport.getCodsJdbcTemplate().execute(myInsertString, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement pstmt)
					throws SQLException, DataAccessException {
				//Set the statements for the update attempt.
		        pstmt.setString(1, subscriptionCode);
		        pstmt.setString(2, dealerCode);
		        pstmt.setString(3, salesRepCode);
		        pstmt.setString(4, csrId);
		        pstmt.setInt(5, ban);
		        pstmt.setString(6, subscriberNo);

		        //Set the statements for the insert attempt
		        pstmt.setInt(7, ban);
		        pstmt.setString(8, subscriberNo);
		        pstmt.setString(9, subscriptionCode);
		        pstmt.setString(10, dealerCode);
		        pstmt.setString(11, salesRepCode);
		        pstmt.setString(12, csrId);


		        //Because of the if/else begin/end statement, we don't get a
		        //number of rows updated. Therefore, pay no attention to the result from
		        //the execute update.
		        pstmt.executeUpdate();
		        
		        return null;
			}
		});
		
	}

	@Override
	@LogInvocation
	public void activateReservedSubscriber(final int pBan, final String pSubscriberId, final Date pStartServiceDate, final String pActivityReasonCode,
			final String pDealerCode,final String pSalesCode,	final ServicesValidation srvValidation, final String portProcessType,
			final int oldBanId, final  String oldSubscriberId, final String productType,String sessionId) throws ApplicationException {
		
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
				
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
			
				ProductActivityInfo productActivityInfo = new ProductActivityInfo();
			    ProductServicesInfo productServicesInfo = new ProductServicesInfo();
			    ProductServicesInfoSys productServicesInfoSys = new ProductServicesInfoSys();
			    ProductCommitmentInfo productCommitmentInfo = new ProductCommitmentInfo();
			    
			    String methodName = "activateReservedSubscriber";
			    LOGGER.debug("("+getClass().getName()+"."+methodName+") Entering... pBan=[" + pBan + "] pSubscriberId=[" + pSubscriberId + "] pStartServiceDate=[" + pStartServiceDate + "] pActivityReasonCode=[" + pActivityReasonCode + "]");
				
			    NewProductConv amdocsNewProductConv = transactionContext.createBean
				 (AmdocsConvBeanClassFactory.getNewProductConvBean(productType));
					
				amdocsNewProductConv.setProductPK(pBan,pSubscriberId);

			      // populate ProductServicesInfo
			      // - if the activation start date has been changed, adjust the servie agreement effective dates
			      // - for all features except features on auto-expiry socs: if any features were expired as part of the initial reserve step, the features need to be marked
			      //   as 'delete' once again
			      productServicesInfo = amdocsNewProductConv.getProductServices();
			      productServicesInfoSys = amdocsNewProductConv.getProductServicesSys();

			      if (pStartServiceDate == null ||
			          productServicesInfo.pricePlan.soc.effDate.compareTo(pStartServiceDate) != 0) {
			    	  LOGGER.debug("("+getClass().getName()+"."+methodName+") Changing effective date from=[" + productServicesInfo.pricePlan.soc.effDate + "] to [" + pStartServiceDate + "]");
			      	productServicesInfo.pricePlan.soc.effDate = pStartServiceDate;
			      	productServicesInfo.pricePlan.soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;
			      	for (int j=0; j < productServicesInfoSys.pricePlan.feature.length; j++) {
			      		if (productServicesInfoSys.pricePlan.feature[j].ftrExpDate != null) {
			      			productServicesInfo.pricePlan.feature[j].transactionType = ServiceFeatureInfo.FTR_TRANSACTION_TYPE_DELETE;
			      		}
			      	}

			      	for (int i=0; i < productServicesInfo.addtnlSrvs.length; i++) {
			      		if (amdocsNewProductConv.getPromotionalSoc(productServicesInfo.addtnlSrvs[i].soc.soc) == null) {
			      			productServicesInfo.addtnlSrvs[i].soc.effDate = pStartServiceDate;
				          	if (productServicesInfoSys.addtnlSrvs[i].soc.serviceType != (byte)'T' &&
				          		productServicesInfoSys.addtnlSrvs[i].soc.serviceType != (byte)'G') {
				      			for (int j=0; j < productServicesInfoSys.addtnlSrvs[i].feature.length; j++) {
					          		if (productServicesInfoSys.addtnlSrvs[i].feature[j].ftrExpDate != null) {
					          			productServicesInfo.addtnlSrvs[i].feature[j].transactionType = ServiceFeatureInfo.FTR_TRANSACTION_TYPE_DELETE;
					          		}
					          	}
				          	}
				      		productServicesInfo.addtnlSrvs[i].soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_UPDATE;
				      	} else {
			      			productServicesInfo.addtnlSrvs[i].soc.effDate = pStartServiceDate;
				      		productServicesInfo.addtnlSrvs[i].soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_INSERT;
						}
			      	}
			      	for (int i=0; i < productServicesInfo.promPricePlan.length; i++) {
			      		productServicesInfo.promPricePlan[i].soc.effDate = pStartServiceDate;
			      		productServicesInfo.promPricePlan[i].soc.transactionType = SocInfo.SOC_TRANSACTION_TYPE_DELETE;
			      	}
			      	
			     // print ProductServices
			        // - Price Plan
			      	LOGGER.debug("("+getClass().getName()+"."+methodName+") ProductServices being set to for Subscriber " + pSubscriberId + ":");
			      	LOGGER.debug("("+getClass().getName()+"."+methodName+") PricePlan SOC    : [" + productServicesInfo.pricePlan.soc.soc + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.pricePlan.soc.transactionType) + " effDate:" + productServicesInfo.pricePlan.soc.effDate + " expDate:" + productServicesInfo.pricePlan.soc.expDate);
			        for (int i=0; i < productServicesInfo.pricePlan.feature.length; i++) {
			        	LOGGER.debug("("+getClass().getName()+"."+methodName+")           Feature: [" + productServicesInfo.pricePlan.feature[i].featureCode + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.pricePlan.feature[i].transactionType) + " ftrParam:" + productServicesInfo.pricePlan.feature[i].ftrParam + " msisdn:" + productServicesInfo.pricePlan.feature[i].msisdn);
			        }
			        // - Additional services
			        for (int i=0; i < productServicesInfo.addtnlSrvs.length; i++) {
			        	LOGGER.debug("("+getClass().getName()+"."+methodName+") Additional SOC    : [" + productServicesInfo.addtnlSrvs[i].soc.soc + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.addtnlSrvs[i].soc.transactionType) + " effDate:" + productServicesInfo.addtnlSrvs[i].soc.effDate + " expDate:" + productServicesInfo.addtnlSrvs[i].soc.expDate);
			          for (int j=0; j < productServicesInfo.addtnlSrvs[i].feature.length; j++) {
			        	  LOGGER.debug("("+getClass().getName()+"."+methodName+")            Feature: [" + productServicesInfo.addtnlSrvs[i].feature[j].featureCode + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.addtnlSrvs[i].feature[j].transactionType) + " ftrParam:" + productServicesInfo.addtnlSrvs[i].feature[j].ftrParam + " msisdn:" + productServicesInfo.addtnlSrvs[i].feature[j].msisdn);
			          }
			        }
			        // - Promotional services
			        for (int i=0; i < productServicesInfo.promPricePlan.length; i++) {
			        	LOGGER.debug("("+getClass().getName()+"."+methodName+") Promotional SOC    : [" + productServicesInfo.promPricePlan[i].soc.soc + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.promPricePlan[i].soc.transactionType) + " effDate:" + productServicesInfo.promPricePlan[i].soc.effDate + " expDate:" + productServicesInfo.promPricePlan[i].soc.expDate);
			          for (int j=0; j < productServicesInfo.promPricePlan[i].feature.length; j++) {
			        	  LOGGER.debug("("+getClass().getName()+"."+methodName+")             Feature: [" + productServicesInfo.promPricePlan[i].feature[j].featureCode + "] transactionType:" + AttributeTranslator.stringFrombyte(productServicesInfo.promPricePlan[i].feature[j].transactionType) + " ftrParam:" + productServicesInfo.promPricePlan[i].feature[j].ftrParam + " msisdn:" + productServicesInfo.promPricePlan[i].feature[j].msisdn);
			          }
			        }
			        // Product Services Info
			        ProductServicesValidationInfo validationInfo = new ProductServicesValidationInfo ();
			        validationInfo.ppSocGrouping = srvValidation.validatePricePlanServiceGrouping();
			        validationInfo.provinceMatch = srvValidation.validateProvinceServiceMatch();
			        validationInfo.equipmentTypeMatch = srvValidation.validateEquipmentServiceMatch();        
			        amdocsNewProductConv.setProductServices(productServicesInfo, validationInfo);
			        LOGGER.debug("("+getClass().getName()+"."+methodName+") setProductServicesInfo done!");
			      }

			      // populate ProductActivityInfo
			      if (pStartServiceDate != null){
			    	  LOGGER.debug("("+getClass().getName()+"."+methodName+") Future Dating to: " + pStartServiceDate);
			    	  productActivityInfo.activityDate = pStartServiceDate;
			      }
			      if (pActivityReasonCode != null && !pActivityReasonCode.equals("")){
			    	  LOGGER.debug("("+getClass().getName()+"."+methodName+") Setting activity reason to: " + pActivityReasonCode);
			    	  productActivityInfo.activityReason = pActivityReasonCode;
			      }
			      productActivityInfo.dealerCode = pDealerCode;
			      productActivityInfo.salesCode = pSalesCode;
			      amdocsNewProductConv.setProductActivityInfo(productActivityInfo);
			      LOGGER.debug("("+getClass().getName()+"."+methodName+") ProductActivityInfo set");

			      // populate ProductCommitmentInfo
			      productCommitmentInfo = amdocsNewProductConv.getProductCommitmentInfo();
			      amdocsNewProductConv.setProductCommitmentInfo(productCommitmentInfo.commitmentMonths);

			      // Activate Subscriber
			      if (PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT.equals( portProcessType ) ) {
			          BrandPortInfo brandPortInfo = new BrandPortInfo();
			          brandPortInfo.isBrandPortActivity=true;
			          brandPortInfo.previousBan = oldBanId;
			          brandPortInfo.previousSubscriber = oldSubscriberId;
			          amdocsNewProductConv.activateSubscriber(brandPortInfo);
			       }
			      else {
			        amdocsNewProductConv.activateSubscriber();
			      }
					
					return null;
				}
			});
		
	}

	@Override
	public void cancelSubscriber(final SubscriberInfo pSubscriberInfo,
			final Date pActivityDate, final String pActivityReasonCode,
			final String pDepositReturnMethod, final String pWaiveReason,
			final String pUserMemoText, final boolean isPortActivity, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				 UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean
				 (AmdocsConvBeanClassFactory.getUpdateProductConvBean(pSubscriberInfo.getProductType()));
				 CancelInfo cancelInfo = new CancelInfo();
				
			      amdocsUpdateProductConv.setProductPK(pSubscriberInfo.getBanId(), pSubscriberInfo.getSubscriberId());
			    
			      cancelInfo.activityDate = pActivityDate == null ? new Date() : pActivityDate;
			      cancelInfo.activityReason = pActivityReasonCode;
			      cancelInfo.userText = pUserMemoText == null ? "" : pUserMemoText;
			      cancelInfo.depositReturnMethod = AttributeTranslator.byteFromString(pDepositReturnMethod);
			      cancelInfo.waiveReason = pWaiveReason;
			      cancelInfo.isPortActivity=isPortActivity;
			      amdocsUpdateProductConv.cancelSubscriber(cancelInfo);
			    return null;
			}
			});	
	}

	@Override
	public void refreshSwitch(final int pBan, final String pSubscriberId,
			final String pProductType, String sessionId) throws ApplicationException {
		
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean
				 (AmdocsConvBeanClassFactory.getUpdateProductConvBean(pProductType));
				
			      amdocsUpdateProductConv.setProductPK(pBan,pSubscriberId);
			      amdocsUpdateProductConv.refreshSwitch();
				 return null;
			}
			});
	}

	@Override
	public void restoreSuspendedSubscriber(final SubscriberInfo pSubscriberInfo,
			final Date pActivityDate, final String pActivityReasonCode,
			final String pUserMemoText, final boolean portIn, String sessionId)
			throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean
				 (AmdocsConvBeanClassFactory.getUpdateProductConvBean(pSubscriberInfo.getProductType()));

				  ActivityInfo activityInfo = new ActivityInfo();

			      amdocsUpdateProductConv.setProductPK(pSubscriberInfo.getBanId(), pSubscriberInfo.getSubscriberId());

			      activityInfo.activityDate = pActivityDate == null ? new Date() : pActivityDate;
			      activityInfo.activityReason = pActivityReasonCode;
			      activityInfo.userText = pUserMemoText == null ? "" : pUserMemoText;
			      activityInfo.isPortActivity = portIn;
			      
			      amdocsUpdateProductConv.restoreSuspendedSubscriber(activityInfo);
				 return null;
			}
			});
		
	}

	@Override
	public void resumeCancelledSubscriber(final SubscriberInfo pSubscriberInfo,
			final String pActivityReasonCode, final String pUserMemoText, final boolean portIn,
			final String portProcessType, final int oldBanId, final String oldSubscriberId,
			String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean
				 (AmdocsConvBeanClassFactory.getUpdateProductConvBean(pSubscriberInfo.getProductType()));
				
				amdocsUpdateProductConv.setProductPK(pSubscriberInfo.getBanId(), pSubscriberInfo.getSubscriberId());
			    
				ActivityInfo activityInfo = new ActivityInfo();
			    activityInfo.activityReason = pActivityReasonCode;
			    activityInfo.userText = pUserMemoText == null ? "" : pUserMemoText;
			    activityInfo.isPortActivity = portIn;

			    if (PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT.equals( portProcessType ) 
			    		|| PortInEligibility.PORT_PROCESS_ROLLBACK.equals(portProcessType)) {
			          BrandPortInfo brandPortInfo = new BrandPortInfo();
			          brandPortInfo.isBrandPortActivity=true;
			          brandPortInfo.previousBan = oldBanId;
			          brandPortInfo.previousSubscriber = oldSubscriberId;
			          amdocsUpdateProductConv.resumeCancelSubscriber(activityInfo,brandPortInfo);
			    } else {
			        amdocsUpdateProductConv.resumeCancelSubscriber(activityInfo);
			    }
				return null;
			}
			});
	}

	@Override
	public SubscriberInfo[] retrieveSubscribersByMemberIdentity(final int urbanId,
			final int fleetId, final int memberId, String sessionId)
			throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<SubscriberInfo[]>() {
			
			@Override
			public SubscriberInfo[] doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				  SearchServices amdocsSearchServices = transactionContext.createBean(SearchServices.class);
				  SubscriberInfo[] subscribers = null;
				 // set up the input value object
		          UFMIInfo ufmiInfo = new UFMIInfo();
		          ufmiInfo.urbanId = urbanId;
		          ufmiInfo.fleetId = fleetId;
		          ufmiInfo.memberId = memberId;

		          // retrieve subscribers
		          SearchSubscriber[] searchSubscribers = amdocsSearchServices.searchByUFMI(ufmiInfo);

		          int searchSubscribersSize = searchSubscribers != null ? searchSubscribers.length : 0;

		          subscribers = new SubscriberInfo[searchSubscribersSize];

		          for (int i = 0; i < searchSubscribersSize; i++) {
		              subscribers[i] = new SubscriberInfo();

		              subscribers[i].setBanId(searchSubscribers[i].ban);
		              subscribers[i].setSubscriberId(searchSubscribers[i].subscriberNo);
		          }
		
		          return subscribers;
			}
		});
	}

	@Override
	public void updateEmailAddress(final int ban, 
								   final String subscriberNumber,
								   final String emailAddress, 
								   final String productType, 
								   String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				UpdateProductAdditionalInfo updateProductAdditionalInfo = new UpdateProductAdditionalInfo();
				updateProductAdditionalInfo.emailAddress = emailAddress;
				
				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));
				amdocsUpdateProductConv.setProductPK(ban, subscriberNumber);
				amdocsUpdateProductConv.changeSubscriber(updateProductAdditionalInfo);
				return null;
			}
		});
	}

		
	@Override
	public ServiceAirTimeAllocationInfo[] retrieveVoiceAllocation(final int billingAccountNumber, final String subscriberId, 
			final String productType, String sessionId)	throws ApplicationException {
	
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<ServiceAirTimeAllocationInfo[]>() {
			@Override
			public ServiceAirTimeAllocationInfo[] doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));
				amdocsUpdateProductConv.setProductPK(billingAccountNumber, subscriberId);
				VoiceAllocationInfo  voiceAllocationInfo =amdocsUpdateProductConv.getVoiceAllocationInfo(null);
				ServiceVoiceAllocationInfo[] serviceVoiceAllocationInfos=voiceAllocationInfo.serviceVoiceAllocationInfo;
				return mapServiceVoiceAllocation(serviceVoiceAllocationInfos);
			}
		});
	}
	
	private ServiceAirTimeAllocationInfo[] mapServiceVoiceAllocation( ServiceVoiceAllocationInfo[] serviceVoiceAllocationInfos ) {
		List<ServiceAirTimeAllocationInfo> serviceAirTimeAllocationInfoList= new ArrayList<ServiceAirTimeAllocationInfo>();
		
		for(ServiceVoiceAllocationInfo amdocsInfo : serviceVoiceAllocationInfos) {
			serviceAirTimeAllocationInfoList.add(VoiceAirTimeMapping.mapServiceVoiceAllocation(amdocsInfo));
		}
		return serviceAirTimeAllocationInfoList.toArray(new ServiceAirTimeAllocationInfo[serviceAirTimeAllocationInfoList.size()]);
	}

}