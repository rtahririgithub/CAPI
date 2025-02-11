package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.BrandPortInfo;
import amdocs.APILink.datatypes.NameInfo;
import amdocs.APILink.datatypes.ProductActivityInfo;
import amdocs.APILink.datatypes.ProductAdditionalInfo;
import amdocs.APILink.datatypes.ProductDepositInfo;
import amdocs.APILink.datatypes.ProductServicesInfo;
import amdocs.APILink.datatypes.ProductServicesValidationInfo;
import amdocs.APILink.datatypes.SMBResourceInfo;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.NewCellularConv;
import amdocs.APILink.sessions.interfaces.NewProductConv;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;
import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.ServicesValidation;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.resource.Resource;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclemanager.dao.impl.amdocs.AmdocsConvBeanWrapper;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public abstract class NewSubscriberDaoImpl extends SubscriberDaoImpl {

	private final Logger LOGGER = Logger.getLogger(NewSubscriberDaoImpl.class);


	protected String[] retrieveAvailablePhoneNumbers(final Class<? extends NewProductConv> newConv, final int ban,
			final PhoneNumberReservationInfo phoneNumberReservation, final int maxNumbers,
			String sessionId) throws ApplicationException {
		String[] phoneNumbers = super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<String[]>() {

			@Override
			public String[] doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {

				NewProductConv newProductConv = transactionContext.createBean(newConv);

				newProductConv.setProductPK(ban);

				return retrievePhoneNumbers(newProductConv, ban, phoneNumberReservation, maxNumbers);
			}
		});
		return super.returnAvailablePhoneNumbers(phoneNumbers);
	}

	protected void createSubscriber(final Class<? extends NewProductConv> newConv
			, final SubscriberInfo subscriberInfo
			, final SubscriberContractInfo subscriberContractInfo
			, final boolean activate
			, final boolean dealerHasDeposit
			, final boolean portedIn
			, final ServicesValidation srvValidation
			, final String portProcessType
			, final int oldBanId
			, final String oldSubscriberId
			, final String sessionId) throws ApplicationException {

		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				boolean subcriberShouldBeReleased = false;
				
				NewProductConv newProductConv = transactionContext.createBean(newConv);
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);

				setProductPk(newProductConv, subscriberInfo, portedIn);

				// populate ProductActivityInfo
				ProductActivityInfo productActivityInfo = new ProductActivityInfo();
				productActivityInfo.dealerCode = subscriberInfo.getDealerCode();
				productActivityInfo.salesCode = subscriberInfo.getSalesRepId();	
				if (subscriberInfo.getStartServiceDate() != null && activate){
					LOGGER.info("Future Dating to: " + subscriberInfo.getStartServiceDate());
					productActivityInfo.activityDate = subscriberInfo.getStartServiceDate();
				}
				if (subscriberInfo.getActivityReasonCode() != null && !subscriberInfo.getActivityReasonCode().equals("") && activate){
					LOGGER.info("Setting activity reason to: " + subscriberInfo.getActivityReasonCode());
					productActivityInfo.activityReason = subscriberInfo.getActivityReasonCode();
				}	
				setProductActivityInfo(productActivityInfo, subscriberInfo, activate);
				newProductConv.setProductActivityInfo(productActivityInfo);
				LOGGER.debug("ProductActivityInfo set");

				boolean portInMSISDN = performIdenSpecificWork(updateBanConv, newProductConv, portedIn, subscriberInfo, sessionId);
				
				subcriberShouldBeReleased = true;

				try {
					// populate ProductAdditionalInfo
					ProductAdditionalInfo productAdditionalInfo = new ProductAdditionalInfo();
					productAdditionalInfo.emailAddress = AttributeTranslator.emptyFromNull(subscriberInfo.getEmailAddress());
					productAdditionalInfo.subFtrLang = AttributeTranslator.emptyFromNull(subscriberInfo.getLanguage());
					productAdditionalInfo.subLangPref = AttributeTranslator.emptyFromNull(subscriberInfo.getLanguage());
					productAdditionalInfo.seatGroup = AttributeTranslator.emptyFromNull(subscriberInfo.getSeatData()!= null ? subscriberInfo.getSeatData().getSeatGroup(): null);
					
					Resource[] resources=  subscriberInfo.getSeatData()!=null ? subscriberInfo.getSeatData().getResources() : null;
					if (newProductConv instanceof NewCellularConv && resources!=null && resources.length >0) {
						SMBResourceInfo[] paramArrayOfSMBResourceInfo = new SMBResourceInfo[resources.length];
						for (int i = 0; i < resources.length; i++) {
							SMBResourceInfo smbResourceInfo =  new SMBResourceInfo();
							smbResourceInfo.resourceNum = AttributeTranslator.emptyFromNull(resources[i].getResourceNumber());
							smbResourceInfo.resourceType= AttributeTranslator.byteFromString (resources[i].getResourceType());
							paramArrayOfSMBResourceInfo[i] = smbResourceInfo;
						}
						((NewCellularConv) newProductConv).setSMBResourceInfo(paramArrayOfSMBResourceInfo);
						
					}
					if (subscriberInfo.getAddress() != null) {
						newProductConv.setAddressInfo(DaoSupport.mapTelusAddressInfoToAmdocsAddressInfoForActivation((AddressInfo)subscriberInfo.getAddress()));
					}
					setSubscriberAlias(subscriberInfo, productAdditionalInfo);
					newProductConv.setProductAdditionalInfo(productAdditionalInfo);
					LOGGER.debug("ProductAdditionalInfo set");

					// Populate product Equipment Info
					setProductEquipmentInfo(newProductConv, subscriberInfo);

					// populate NameInfo
					NameInfo nameInfo = new NameInfo();
					nameInfo.firstName = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getFirstName());
					nameInfo.lastBusinessName = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getLastName());
					nameInfo.middleInitial = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getMiddleInitial());
					nameInfo.nameTitle = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getTitle());
					nameInfo.nameSuffix = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getGeneration());
					nameInfo.additionalTitle = AttributeTranslator.emptyFromNull(subscriberInfo.getConsumerName().getAdditionalLine());
					newProductConv.setNameInfo(nameInfo, AttributeTranslator.byteFromString(subscriberInfo.getConsumerName().getNameFormat()));
					LOGGER.debug("NameInfo set");

					// populate ProductDepositInfo
					ProductDepositInfo productDepositInfo = new ProductDepositInfo();
					productDepositInfo.depositKept = dealerHasDeposit;
					newProductConv.setProductDepositInfo(productDepositInfo);
					LOGGER.debug("ProductDepositInfo set");

					// populate ProductCommitmentInfo
					newProductConv.setProductCommitmentInfo((short) subscriberContractInfo.getCommitmentMonths());
					LOGGER.info("ProductDepositInfo set to [" + subscriberContractInfo.getCommitmentMonths() + "]");

					// populate ProductServicesInfo
					ProductServicesInfo productServicesInfo = new ProductServicesInfo();
					productServicesInfo = DaoSupport.mapTelusContractToAmdocsProductServicesForActivation(newProductConv, subscriberInfo, subscriberContractInfo, true, true, portInMSISDN);

					ProductServicesValidationInfo validationInfo = new ProductServicesValidationInfo();
					validationInfo.ppSocGrouping = srvValidation.validatePricePlanServiceGrouping();
					validationInfo.provinceMatch = srvValidation.validateProvinceServiceMatch();
					setEquipmentTypeMatch(subscriberInfo, validationInfo, srvValidation);
					newProductConv.setProductServices(productServicesInfo, validationInfo);
					
					// save or activate new subscriber
					if (activate) {
						activateSubscriber(newProductConv, portProcessType, oldBanId, oldSubscriberId);
						subcriberShouldBeReleased = false; // don't release subscriber even if something fails after this point
						LOGGER.debug("activateSubscriber Successful");
					} else {
						newProductConv.saveResSubscriber();
						LOGGER.debug("saveResSubscriber Successful");
					}

					subcriberShouldBeReleased = false;
					return null;
				} finally {
					if (subcriberShouldBeReleased) {
						if (!portedIn) {
							releaseSubscriber(newConv, subscriberInfo, sessionId);
						} else {
							releasePortedInSubscriber(subscriberInfo, sessionId);
						}
					}
				}
			}
		});		
	}

	protected boolean performIdenSpecificWork(UpdateBanConv amdocsUpdateBanConv, NewProductConv newProductConv, boolean portedIn, SubscriberInfo subscriberInfo, String sessionId) throws RemoteException, ValidateException, TelusException, ApplicationException {
		return false;
	}

	protected void setProductActivityInfo(
			ProductActivityInfo productActivityInfo,
			SubscriberInfo subscriberInfo, boolean activate) {	
		// DO Nothing
	}

	protected void setProductPk(NewProductConv newProductConv,
			SubscriberInfo subscriberInfo, boolean portedIn) throws ApplicationException, RemoteException, ValidateException {
		// PortedIn is mainly usd for IDEN.

		// Check that subscriber id is passed in
		if (subscriberInfo.getSubscriberId() == null || subscriberInfo.getSubscriberId().trim().equals("")) {		
			throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_ID_REQUIRED, "Subscriber Id is mandatory.","");
		}

		// Set ProductPK (which also retrieves the BAN)
		newProductConv.setProductPK(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());
		LOGGER.debug("ProductPK set");

	}

	protected void setEquipmentTypeMatch(
			SubscriberInfo subscriberInfo,
			ProductServicesValidationInfo validationInfo,
			ServicesValidation srvValidation) {
		validationInfo.equipmentTypeMatch = srvValidation.validateEquipmentServiceMatch();		
	}

	protected void setSubscriberAlias(
			SubscriberInfo subscriberInfo, ProductAdditionalInfo productAdditionalInfo) {
		// DO nothing.		
	}

	protected abstract void setProductEquipmentInfo(NewProductConv newProductConv
			, SubscriberInfo subscriberInfo) throws RemoteException, ValidateException;

	protected void activateSubscriber(NewProductConv newProductConv, String portProcessType
			, int oldBanId, String oldSubscriberId) throws RemoteException, ValidateException {
		if (PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT.equals( portProcessType ) ) {
	          BrandPortInfo brandPortInfo = new BrandPortInfo();
	          brandPortInfo.isBrandPortActivity=true;
	          brandPortInfo.previousBan = oldBanId;
	          brandPortInfo.previousSubscriber = oldSubscriberId;
	          newProductConv.activateSubscriber(brandPortInfo);
		} else {
	    	newProductConv.activateSubscriber();
	    }
	}

	protected void releaseSubscriber(final Class<? extends NewProductConv> newConv
			, final SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException {
		super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				NewProductConv newProductConv = transactionContext.createBean(newConv);

				newProductConv.setProductPK(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());

				newProductConv.releaseSubscriber();

				return null;
			}
		});
	}

	protected SubscriberInfo reserveLikePhoneNumber(final Class<? extends NewProductConv> newConv, final SubscriberInfo subscriberInfo
			, final PhoneNumberReservationInfo phoneNumberReservation, String sessionId) throws ApplicationException {

		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<SubscriberInfo>() {

			@Override
			public SubscriberInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				NewProductConv newProductConv = transactionContext.createBean(newConv);			    



				String[] phoneNumbers = null;
				String[] numberPattern = getPhoneNumberPattern(phoneNumberReservation.getPhoneNumberPattern());

				// Set ProductPK (which also retrieves the BAN)
				newProductConv.setProductPK(subscriberInfo.getBanId());

				// populate ProductActivityInfo
				ProductActivityInfo productActivityInfo = new ProductActivityInfo();
				productActivityInfo.numberLocation = phoneNumberReservation.getNumberGroup().getNumberLocation();
				productActivityInfo.dealerCode = subscriberInfo.getDealerCode();
				productActivityInfo.salesCode = subscriberInfo.getSalesRepId();
				newProductConv.setProductActivityInfo(productActivityInfo);


				// loop thru number patterns and find list of available phone numbers
				for (int i = 0; i < numberPattern.length; i++) {
					phoneNumbers = retrieveAvailableSubscriberList(newProductConv, phoneNumberReservation, numberPattern[i]);

					// exit if a number was found
					if (phoneNumbers != null && phoneNumbers.length > 0){
						break;
					}
				}

				// reserve phone number
				if (phoneNumbers != null && phoneNumbers.length > 0){			    	
					AmdocsConvBeanWrapper.reserveSubscriber(newProductConv, phoneNumbers[0],false);
					LOGGER.debug("Reserved Number: " + phoneNumbers[0]);
				} else {
					//no numbers found.
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS
							,genNoAvailablePhoneNumberMessage(phoneNumberReservation), "");
				}

				// set subscriberId and phoneNumber
				subscriberInfo.setSubscriberId(phoneNumbers[0]);
				subscriberInfo.setPhoneNumber(phoneNumbers[0]);

				return subscriberInfo;
			}

		});
	}
	protected String[] retrieveAvailableSubscriberList(
			NewProductConv newProductConv,
			PhoneNumberReservationInfo phoneNumberReservation,
			String numberPattern) throws Exception{
		return AmdocsConvBeanWrapper.getAvailableSubscriberList(newProductConv, phoneNumberReservation.getNumberGroup().getCode(),
				"0",
				numberPattern,
				phoneNumberReservation.isAsian());
	}

	protected String[] getPhoneNumberPattern(String phoneNumberPattern) throws ApplicationException {
		String likePhoneNumber = phoneNumberPattern.replace((char)'*',(char)' ');
		likePhoneNumber = likePhoneNumber.trim();

		if (likePhoneNumber.length() < 10) {
			throw new ApplicationException(SystemCodes.CMB_ALM_DAO, ErrorCodes.LIKE_NUMBER_MUST_BE_10_DIGITS, "Like Number must be 10 numbers in length", "");
		}

		String[] numberPattern = new String[15];
		numberPattern[0] = likePhoneNumber.substring(0,9) + "*";
		numberPattern[1] = likePhoneNumber.substring(0,6) + "*" + likePhoneNumber.substring(7);
		numberPattern[2] = likePhoneNumber.substring(0,8) + "*" + likePhoneNumber.substring(9);
		numberPattern[3] = likePhoneNumber.substring(0,7) + "*" + likePhoneNumber.substring(8);
		numberPattern[4] = likePhoneNumber.substring(0,8) + "**";
		numberPattern[5] = likePhoneNumber.substring(0,6) + "**" + likePhoneNumber.substring(8);
		numberPattern[6] = likePhoneNumber.substring(0,7) + "**" + likePhoneNumber.substring(9);
		numberPattern[7] = likePhoneNumber.substring(0,6) + "*" + likePhoneNumber.substring(7,9) + "*";
		numberPattern[8] = likePhoneNumber.substring(0,6) + "*" + likePhoneNumber.substring(7,8) + "*" + likePhoneNumber.substring(9);
		numberPattern[9] = likePhoneNumber.substring(0,7) + "*" + likePhoneNumber.substring(8,9) + "*";
		numberPattern[10] = likePhoneNumber.substring(0,7) + "***";
		numberPattern[11] = likePhoneNumber.substring(0,6) + "***" + likePhoneNumber.substring(9);
		numberPattern[12] = likePhoneNumber.substring(0,6) + "*" + likePhoneNumber.substring(7,8) + "**";
		numberPattern[13] = likePhoneNumber.substring(0,6) + "**" + likePhoneNumber.substring(8,9) + "*";
		numberPattern[14] = likePhoneNumber.substring(0,6) + "****";

		return numberPattern;
	}

	protected SubscriberInfo reservePhoneNumber(final Class<? extends NewProductConv> newConv, final SubscriberInfo subscriberInfo
			, final PhoneNumberReservationInfo phoneNumberReservation,final boolean isOfflineReservation, String sessionId)
	throws ApplicationException {
		return super.getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<SubscriberInfo>() {

			@Override
			public SubscriberInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				String wildCards = "**********";
				String[] phoneNumbers = null;
				
				NewProductConv newProductConv = transactionContext.createBean(newConv);		

				// Set ProductPK (which also retrieves the BAN)
				newProductConv.setProductPK(subscriberInfo.getBanId());

				// populate ProductActivityInfo
				ProductActivityInfo productActivityInfo = new ProductActivityInfo();
				productActivityInfo.numberLocation = phoneNumberReservation.getNumberGroup().getNumberLocation();
				productActivityInfo.dealerCode = subscriberInfo.getDealerCode();
				productActivityInfo.salesCode = subscriberInfo.getSalesRepId();
				newProductConv.setProductActivityInfo(productActivityInfo);
				if (isOfflineReservation) {
					phoneNumbers =  new String[1];
					phoneNumbers[0]=phoneNumberReservation.getPhoneNumberPattern();		
				} else {
				// get list of available phone numbers
				String wildCard = phoneNumberReservation.getPhoneNumberPattern();

				if (wildCard.length() < 10)
					wildCard += wildCards.substring(0, 10 - wildCard.length());

				 phoneNumbers = retrieveAvailableSubscriberList(newProductConv
						, phoneNumberReservation, wildCard);
				}
				// reserve phoneNumber
		if (phoneNumbers != null && phoneNumbers.length > 0) {
					AmdocsConvBeanWrapper.reserveSubscriber(newProductConv, phoneNumbers[0], isOfflineReservation);
					LOGGER.debug("Reserved Number: " + phoneNumbers[0]);
				} else {
					//no numbers found.
					throw new ApplicationException(SystemCodes.CMB_SLM_DAO, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS
							,genNoAvailablePhoneNumberMessage(phoneNumberReservation), "");
				}

				// set subscriberId and phoneNumber
				subscriberInfo.setSubscriberId(phoneNumbers[0]);
				subscriberInfo.setPhoneNumber(phoneNumbers[0]);

				return subscriberInfo;

			}

		});
	}
	
	public void releasePortedInSubscriber(final SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException {
		//do nothing. to be overriden;
	}
}
