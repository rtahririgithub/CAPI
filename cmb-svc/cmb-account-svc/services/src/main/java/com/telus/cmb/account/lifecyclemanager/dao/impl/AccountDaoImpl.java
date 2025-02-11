package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.AccountTypeInfo;
import amdocs.APILink.datatypes.ActivityInfo;
import amdocs.APILink.datatypes.AddressMatchInfo;
import amdocs.APILink.datatypes.BillDetailsInfo;
import amdocs.APILink.datatypes.BillParamsInfo;
import amdocs.APILink.datatypes.BusinessInfo;
import amdocs.APILink.datatypes.CPUIInfo;
import amdocs.APILink.datatypes.CancelInfo;
import amdocs.APILink.datatypes.CheckDetailsInfo;
import amdocs.APILink.datatypes.ContactInfo;
import amdocs.APILink.datatypes.ContractInfo;
import amdocs.APILink.datatypes.CreditCardDetailsInfo;
import amdocs.APILink.datatypes.CreditCardInfo;
import amdocs.APILink.datatypes.CycleInfo;
import amdocs.APILink.datatypes.FutureTransactionDetailsInfo;
import amdocs.APILink.datatypes.FutureTransactionInfo;
import amdocs.APILink.datatypes.IdentificationInfo;
import amdocs.APILink.datatypes.NameInfo;
import amdocs.APILink.datatypes.NationalGrowthInfo;
import amdocs.APILink.datatypes.NewBanAdditionalInfo;
import amdocs.APILink.datatypes.SearchBan;
import amdocs.APILink.datatypes.SpecialBillDetailsInfo;
import amdocs.APILink.datatypes.TaxExemptionInfo;
import amdocs.APILink.sessions.interfaces.AddressMatchServices;
import amdocs.APILink.sessions.interfaces.NewBanConv;
import amdocs.APILink.sessions.interfaces.SearchServices;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;

import com.telus.api.ApplicationException;
import com.telus.api.account.PaymentMethod;
import com.telus.api.reference.AccountType;
import com.telus.cmb.account.lifecyclemanager.dao.AccountDao;
import com.telus.cmb.account.utilities.AccountLifecycleUtilities;
import com.telus.cmb.account.utilities.StringUtils;
import com.telus.cmb.account.utilities.TelusToAmdocsAccountMapper;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTelusMapping;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationMessagesInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.BillParametersInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CustomerNotificationPreferenceInfo;
import com.telus.eas.account.info.FutureStatusChangeRequestInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.Info;

public class AccountDaoImpl extends AmdocsDaoSupport implements AccountDao {
	private static final Logger LOGGER = Logger.getLogger(AccountDaoImpl.class);

	
	public static final byte INSERT = (byte) 'I';
	public static final byte DELETE = (byte) 'D';
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.account.lifecyclefacade.dao.AccountModificationDao#updateEmailAddress(int, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateEmailAddress(final int ban, final String emailAddress, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(ban);

				IdentificationInfo amdocsIdentificationInfo = updateBanConv.getIdentificationInfo();
				amdocsIdentificationInfo.emailAddress = emailAddress;				
				updateBanConv.setIdentificationInfo(amdocsIdentificationInfo);

				updateBanConv.saveBan();

				return null;
			}
		});
	}

	@Override
	public void updateBillCycle(final int ban, final short billCycle, String sessionId)	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)	throws Exception {
				CycleInfo amdocsCycleInfo = new CycleInfo();

				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(ban);

				amdocsCycleInfo = updateBanConv.getCycleInfo();
				amdocsCycleInfo.cycleCode = billCycle;

				updateBanConv.changeCycleInfo(amdocsCycleInfo);

				return null;
			}
		});
	}

	@Override
	public int createAccount(final AccountInfo pAccountInfo, String sessionId) throws ApplicationException {

		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Integer>() {

			@Override
			public Integer doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				int ban = 0;

				// populate Amdocs Info classes
				HashMap <Object, Object>amdocsClasses = TelusToAmdocsAccountMapper.mapAccountInfoToAmdocs(pAccountInfo,null);

				// get amdocs classes in hash map
				AccountTypeInfo amdocsAccountTypeInfo = (AccountTypeInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new AccountTypeInfo().getClass()));
				NameInfo amdocsBillingNameInfo = (NameInfo)amdocsClasses.get("Billing" +  AttributeTranslator.getBaseName(new NameInfo().getClass()));
				NameInfo amdocsContactNameInfo = (NameInfo)amdocsClasses.get("Contact" +  AttributeTranslator.getBaseName(new NameInfo().getClass()));
				amdocs.APILink.datatypes.AddressInfo amdocsBillingAddressInfo = (amdocs.APILink.datatypes.AddressInfo)amdocsClasses.get("Billing" +  AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.AddressInfo().getClass()));
				amdocs.APILink.datatypes.AddressInfo amdocsCreditCheckAddressInfo = (amdocs.APILink.datatypes.AddressInfo)amdocsClasses.get("CreditCheck" +  AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.AddressInfo().getClass()));
				IdentificationInfo amdocsIdentificationInfo = (IdentificationInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new IdentificationInfo().getClass()));
				ContractInfo amdocsContractInfo = (ContractInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new ContractInfo().getClass()));
				ContactInfo amdocsContactInfo = (ContactInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new ContactInfo().getClass()));
				amdocs.APILink.datatypes.CreditCardInfo amdocsCreditCardInfo = (amdocs.APILink.datatypes.CreditCardInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.CreditCardInfo().getClass()));
				CreditCardDetailsInfo amdocsCreditCardDetailsInfo = (CreditCardDetailsInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new CreditCardDetailsInfo().getClass()));
				CheckDetailsInfo amdocsCheckDetailsInfo = (CheckDetailsInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new CheckDetailsInfo().getClass()));
				CycleInfo amdocsCycleInfo = (CycleInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new CycleInfo().getClass()));
				BusinessInfo amdocsBusinessInfo = (BusinessInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new BusinessInfo().getClass()));
				SpecialBillDetailsInfo amdocsSpecialBillDetailsInfo = (SpecialBillDetailsInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new SpecialBillDetailsInfo().getClass()));
				NationalGrowthInfo amdocsNationalGrowthInfo = (NationalGrowthInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new NationalGrowthInfo().getClass()));
				TaxExemptionInfo amdocsTaxExemptionInfo = (TaxExemptionInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new TaxExemptionInfo().getClass()));

				// set attributes on Amdocs's Conversation bean
				NewBanConv newBanConv = transactionContext.createBean(NewBanConv.class);	

				// Account Type
				newBanConv.setAccountTypeInfo(amdocsAccountTypeInfo);
				// Billing Name
				newBanConv.setBillingName(amdocsBillingNameInfo);
				// Credit Name
				newBanConv.setCreditName(amdocsBillingNameInfo);
				// Contact Name
				newBanConv.setContactName(amdocsContactNameInfo);
				// Pareto changes begin: setting GL_SEGMENT and GL_SUBSEGMENT for all account creation
				amdocsBusinessInfo.glSegment = pAccountInfo.getBanSegment();
				amdocsBusinessInfo.glSubSegment = pAccountInfo.getBanSubSegment();
				newBanConv.setBusinessInfo(amdocsBusinessInfo);
				// Pareto changes end
				// Business Info (only for business/regular)
				if (pAccountInfo instanceof PostpaidBusinessRegularAccountInfo) {
					newBanConv.setContactName(amdocsContactNameInfo);
					// temporary!!!! populate work telephone from contact telephone
					amdocsContactInfo.workTelNo = amdocsContactInfo.contactTelNo;
					amdocsContactInfo.workTelExtNo = amdocsContactInfo.contactTelExtNo;
				}
				// Identification Info
				newBanConv.setIdentificationInfo(resetDOB(amdocsIdentificationInfo));
				newBanConv.setContactInfo(amdocsContactInfo);
				// Address - Billing
				amdocsBillingAddressInfo.addressMatchInd = !(amdocsBillingAddressInfo.primaryLine.equals(""));
				newBanConv.setBillingAddress(amdocsBillingAddressInfo);
				// Address - CreditCheck
				if (!amdocsCreditCheckAddressInfo.city.trim().equals("")) {
					amdocsCreditCheckAddressInfo.addressMatchInd = !(amdocsCreditCheckAddressInfo.primaryLine.equals(""));
					newBanConv.setCreditAddress(amdocsCreditCheckAddressInfo);
				} else {
					newBanConv.setCreditAddress(amdocsBillingAddressInfo);
				}
				// Dealer
				newBanConv.setContractInfo(amdocsContractInfo);
				// Bill Cycle
				newBanConv.setCycleInfo(amdocsCycleInfo);
				// Guarantee Credit Card
				if (!amdocsCreditCardInfo.creditCardNum.trim().equals(""))
					newBanConv.setCreditCardInfo(amdocsCreditCardInfo);
				// Payment Method
				String paymentMethod = "";
				if (amdocsCreditCardDetailsInfo.creditCardNo == null || amdocsCreditCardDetailsInfo.creditCardNo.trim().equals(""))
					if (amdocsCheckDetailsInfo.bankAccountNo == null || amdocsCheckDetailsInfo.bankAccountNo.trim().equals(""))
						paymentMethod = PaymentMethod.PAYMENT_METHOD_REGULAR;
					else
						paymentMethod = PaymentMethod.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT;
				else
					paymentMethod = PaymentMethod.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD;

				if (paymentMethod.equals(PaymentMethod.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD)) {
					LOGGER.debug("calling setPaymentInfo() for CC..." );
					newBanConv.setPaymentInfo(amdocsCreditCardDetailsInfo);
				}
				if (paymentMethod.equals(PaymentMethod.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT)) {
					LOGGER.debug("calling setPaymentInfo() for CheckDetailsInfo..." );
					newBanConv.setPaymentInfo(amdocsCheckDetailsInfo);
				}
				// Set bill on-hold for QuebecTel Prepaid
				if (pAccountInfo.isQuebectelPrepaidConsumer()) {
					amdocsSpecialBillDetailsInfo.blManHndlReqOpid = 1;  // Hold Bill - Do not print
					amdocsSpecialBillDetailsInfo.blManHndlEffDate = new java.util.GregorianCalendar().getTime();
					amdocsSpecialBillDetailsInfo.blManHndlExpDate = AttributeTranslator.dateFromString("20991231","yyyyMMdd");
				}
				// Set e-bill for Ampd accounts
				if (pAccountInfo.isAmpd()) {
					amdocsSpecialBillDetailsInfo.invSuppressionInd = (byte) '9'; // Full Invoice Suppression
					amdocsSpecialBillDetailsInfo.blManHndlReqOpid = 1;  // Hold Bill - Do not print
					amdocsSpecialBillDetailsInfo.blManHndlEffDate = new java.util.GregorianCalendar().getTime();
					amdocsSpecialBillDetailsInfo.blManHndlExpDate = AttributeTranslator.dateFromString("20991231","yyyyMMdd");
				}
				// Adding special billing details handling for Hedwig Project 2018
				if (pAccountInfo.isForceZeroBalanceInd()) {
					amdocsSpecialBillDetailsInfo.blZeroBalancInd = (byte) 'Y';
				}

				// Tax Exemption Info
				if (amdocsTaxExemptionInfo.gstCertificateNumber == null) {
					amdocsTaxExemptionInfo.gstCertificateNumber = ""; //need to set this to empty string as it cannot be null (see defect # 101183)
				}				
				newBanConv.setTaxExemptionInfo(amdocsTaxExemptionInfo);
	
				// BillDetails
				newBanConv.setSpecialBillDetailsInfo(amdocsSpecialBillDetailsInfo);
				
				// NationalGrowth
				newBanConv.setNationalGrowthInfo(amdocsNationalGrowthInfo);
				
				// Set convRunNo for Migrated Fido Accounts
				if (pAccountInfo.isFidoConversion()) {
					NewBanAdditionalInfo amdocsNewBanAdditionalInfo = new NewBanAdditionalInfo();
					amdocsNewBanAdditionalInfo.convRunNo = AccountInfo.ACCOUNT_CONV_RUN_NO_FIDO;
					LOGGER.debug("Calling setNewBanAdditionalInfo..." );
					newBanConv.setNewBanAdditionalInfo(amdocsNewBanAdditionalInfo);
				}
				// store BAN
				LOGGER.debug("Excecuting saveBan() - start..." );
				newBanConv.saveBan();
				LOGGER.debug("Excecuting saveBan() - end..." );
				ban = newBanConv.getBanNumber();
				return new Integer(ban);
			}
		});
	}

	@Override
	public void updateBillParamsInfo(final int banId,final BillParametersInfo billParametersInfo, String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)	throws Exception {
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(banId);

				BillParamsInfo billParamsInfo = new BillParamsInfo();
				billParamsInfo.bpNoOfCopies = billParametersInfo.getNoOfInvoice();
				billParamsInfo.bpFormat = billParametersInfo.getBillFormat();
				billParamsInfo.bpMedia = billParametersInfo.getMediaCategory();
				updateBanConv.changeBillParamsInfo(billParamsInfo );
				return null;
			}
		});
	}

	@Override
	public void updateAccountPassword(final int pBan, final String pAccountPassword,final String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				IdentificationInfo amdocsIdentificationInfo = new IdentificationInfo();
				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(pBan);

				// Set account password
				amdocsIdentificationInfo = updateBanConv.getIdentificationInfo();
				amdocsIdentificationInfo.accPassword = pAccountPassword;
				updateBanConv.setIdentificationInfo(amdocsIdentificationInfo);
				// store
				LOGGER.debug("Excecuting saveBan() - start..." );
				updateBanConv.saveBan();
				LOGGER.debug("Excecuting saveBan() - end..." );

				return null;
			}
		} );
	}

	@Override
	public CancellationPenaltyInfo retrieveCancellationPenalty(final int pBan,
			String sessionId) throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CancellationPenaltyInfo>() {

			@Override
			public CancellationPenaltyInfo doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				CancellationPenaltyInfo cancellationPenaltyInfo = new CancellationPenaltyInfo();
				amdocs.APILink.datatypes.CancellationPenaltyInfo amdocsCancellationPenaltyInfo = null;
				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(pBan);

				// retrieve discounts
				amdocsCancellationPenaltyInfo = updateBanConv.getCancellationPenaltyInfo();

				// map amdocs info class to telus info class
				cancellationPenaltyInfo.setDepositAmount(amdocsCancellationPenaltyInfo.totalDepositAmount);
				cancellationPenaltyInfo.setDepositInterest(amdocsCancellationPenaltyInfo.totalDepositInterest);
				cancellationPenaltyInfo.setPenalty(amdocsCancellationPenaltyInfo.totalPenalty);
				return cancellationPenaltyInfo;
			}
		});
	}

	@Override
	public CancellationPenaltyInfo[] retrieveCancellationPenaltyList(final int banId,
			final String[] subscriberId, String sessionId) throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CancellationPenaltyInfo[]>() {

			@Override
			public CancellationPenaltyInfo[] doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				List<CancellationPenaltyInfo> cancellationPenalties = new ArrayList<CancellationPenaltyInfo>();
				com.telus.eas.account.info.CancellationPenaltyInfo cancellationPenaltyInfo = null;

				// set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(banId);
				// retrieve amdocs CancellationPenaltyInfo[]
				amdocs.APILink.datatypes.CancellationPenaltyInfo[] amdocsInfos = updateBanConv.getCancellationPenaltyInfoList(subscriberId);

				// map amdocs info class to telus info class
				if (amdocsInfos != null) {
					for(int i=0; i<amdocsInfos.length; i++) {
						cancellationPenaltyInfo = new com.telus.eas.account.info.CancellationPenaltyInfo();
						cancellationPenaltyInfo.setDepositAmount(amdocsInfos[i].depositAmt);
						cancellationPenaltyInfo.setDepositInterest(amdocsInfos[i].totalDepositInterest);
						cancellationPenaltyInfo.setPenalty(amdocsInfos[i].penaltyAmt);
						cancellationPenaltyInfo.setSubscriberNumber(amdocsInfos[i].subscriberNumber);
						cancellationPenalties.add(cancellationPenaltyInfo);
					}
				}
				return (com.telus.eas.account.info.CancellationPenaltyInfo[])cancellationPenalties.toArray(
						new com.telus.eas.account.info.CancellationPenaltyInfo[cancellationPenalties.size()]);
			}
		});
	}

	@Override
	public void updateNationalGrowth(final int ban, final String nationalGrowthIndicator,
			final String homeProvince, String sessionId) throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

				NationalGrowthInfo amdocsNationalGrowthInfo = new NationalGrowthInfo();
				NationalGrowthInfo originalAmdocsNationalGrowthInfo = new NationalGrowthInfo();

				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);	
				updateBanConv.setBanPK(ban);
				originalAmdocsNationalGrowthInfo = updateBanConv.getNationalGrowthInfo();//originalAmdocsNationalGrowthInfo.cloneMe();
				amdocsNationalGrowthInfo = updateBanConv.getNationalGrowthInfo();

				if (nationalGrowthIndicator != null)
					amdocsNationalGrowthInfo.nationalAccount = AttributeTranslator
					.byteFromString(nationalGrowthIndicator);
				if (homeProvince != null)
					amdocsNationalGrowthInfo.homeProvince = homeProvince
					.equals("QC") ? "PQ"
							: homeProvince.equals("NL") ? "NF" : homeProvince;

				// update national growth information (if something has changed)
				if (StringUtils.compare(originalAmdocsNationalGrowthInfo.homeProvince,
						amdocsNationalGrowthInfo.homeProvince) != 0
						|| originalAmdocsNationalGrowthInfo.nationalAccount != amdocsNationalGrowthInfo.nationalAccount) {

					// set default national account (ban category) (if necessary) to
					// prevent validation errors
					if (amdocsNationalGrowthInfo.nationalAccount != (byte) 'N'
							&& amdocsNationalGrowthInfo.nationalAccount != (byte) 'R'){
						amdocsNationalGrowthInfo.nationalAccount = 
							(originalAmdocsNationalGrowthInfo.nationalAccount != (byte) 'N' && 
									originalAmdocsNationalGrowthInfo.nationalAccount != (byte) 'R') ? 
											(byte) 'R': originalAmdocsNationalGrowthInfo.nationalAccount;
					}
					updateBanConv.changeNationalGrowthInfo(amdocsNationalGrowthInfo);
					updateBanConv.saveBan();

				} 
				return null;
			}
		});
	}

	@Override
	public void restoreSuspendedAccount(final int ban, final Date restoreDate,
			final String restoreReasonCode, final String restoreComment,
			final boolean collectionSuspensionsOnly, String sessionId)
	throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

				ActivityInfo amdocsActivityInfo = new ActivityInfo();

				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);	
				updateBanConv.setBanPK(ban);

				// Populate ActivityInfo
				amdocsActivityInfo.activityDate = restoreDate == null ? new Date()
				: restoreDate;
				amdocsActivityInfo.activityReason = restoreReasonCode;
				amdocsActivityInfo.userText = restoreComment == null ? new String(
				"") : restoreComment;

				updateBanConv.restoreSuspendedBan(amdocsActivityInfo,collectionSuspensionsOnly);

				return null;
			}
		});

	}

	@Override
	public void updateFutureStatusChangeRequest(final int ban,
			final FutureStatusChangeRequestInfo futureStatusChangeRequestInfo,
			String sessionId) throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);	
				updateBanConv.setBanPK(ban);

				// populate FutureTransactionInfo
				FutureTransactionInfo[] futureTransactionInfo = new FutureTransactionInfo[1];
				futureTransactionInfo[0] = new amdocs.APILink.datatypes.FutureTransactionInfo();
				futureTransactionInfo[0].cfrDate = futureStatusChangeRequestInfo
				.getEffectiveDate();
				futureTransactionInfo[0].cfrSequenceNumber = futureStatusChangeRequestInfo
				.getSequenceNumber();
				futureTransactionInfo[0].updateFlag = 
					AttributeTranslator.byteFromString(futureStatusChangeRequestInfo.getUpdateFlag()); // 'U' or 'D'

				for (int i = 0; i < futureTransactionInfo.length; i++) {
					LOGGER.debug(i+ " : cfrSequenceNumber=["
							+ futureTransactionInfo[i].cfrSequenceNumber + "],"
							+ " updateFlag=[" + futureTransactionInfo[i].updateFlag
							+ "]" + " cfrDate=[" + futureTransactionInfo[i].cfrDate
							+ "]");
				}

				updateBanConv.updateFutureTransaction(futureTransactionInfo);

				return null;
			}
		});

	}


	@Override
	public void suspendAccount(final int ban, final Date activityDate,
			final String activityReasonCode, final String userMemoText,final String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				ActivityInfo amdocsActivityInfo = new ActivityInfo();

				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(ban);

				// Populate ActivityInfo
				amdocsActivityInfo.activityDate = activityDate == null ? new Date()
				: activityDate;
				amdocsActivityInfo.activityReason = activityReasonCode;
				amdocsActivityInfo.userText = userMemoText == null ? new String("")
				: userMemoText;

				// store
				LOGGER.debug("Excecuting suspendBan() - start..." );
				updateBanConv.suspendBan(amdocsActivityInfo);
				LOGGER.debug("Excecuting suspendBan() - end..." );

				return null;   
			}
		});
	}

	@Override
	public List<FutureStatusChangeRequestInfo> retrieveFutureStatusChangeRequests(
			final int ban,String sessionId) throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<List<FutureStatusChangeRequestInfo>>() {

			@Override
			public List<FutureStatusChangeRequestInfo> doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {

				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(ban);

				FutureTransactionDetailsInfo[] amdocsFutureTransactions = new FutureTransactionDetailsInfo[0];
				List<FutureStatusChangeRequestInfo> futureStatusChangeRequestsList = new ArrayList<FutureStatusChangeRequestInfo>();

				amdocsFutureTransactions = updateBanConv.getFutureTransactionList();

				if (amdocsFutureTransactions != null) {
					FutureStatusChangeRequestInfo futureStatusChangeRequests = null;
					for (int i = 0; i < amdocsFutureTransactions.length; i++) {

						futureStatusChangeRequests = new FutureStatusChangeRequestInfo();
						futureStatusChangeRequests.setActivityCode(amdocsFutureTransactions[i].cfrActivityCode);
						futureStatusChangeRequests.setActivityReasonCode(amdocsFutureTransactions[i].cfrReasonCode);
						futureStatusChangeRequests.setBan(ban);
						futureStatusChangeRequests.setCreateDate(amdocsFutureTransactions[i].cfrCreateDate);
						futureStatusChangeRequests.setEffectiveDate(amdocsFutureTransactions[i].cfrDate);
						futureStatusChangeRequests.setPhoneNumber(amdocsFutureTransactions[i].cfrSubscriberNumber);
						futureStatusChangeRequests.setProductType(AttributeTranslator.stringFrombyte(amdocsFutureTransactions[i].cfrProductType));
						futureStatusChangeRequests.setSequenceNumber((long)amdocsFutureTransactions[i].cfrSequenceNumber);
						futureStatusChangeRequests.setSubscriberId(amdocsFutureTransactions[i].cfrSubscriberNumber);
						futureStatusChangeRequests.setUpdateFlag(null);
						futureStatusChangeRequestsList.add(futureStatusChangeRequests);

					}
				}
				return futureStatusChangeRequestsList;

			}
		});

	}

	@Override
	public void cancelAccount(final int ban, final Date activityDate, final String activityReasonCode, final String depositReturnMethod, 
			final String waiveReason, final String userMemoText, final boolean isPortActivity, final String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {

				CancelInfo amdocsCancelInfo = new CancelInfo();

				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(ban);

				// Populate CancelInfo
				amdocsCancelInfo.activityDate = activityDate == null ? new Date()
				: activityDate;
				amdocsCancelInfo.activityReason = activityReasonCode;
				amdocsCancelInfo.userText = userMemoText == null ? new String("")
				: userMemoText;
				amdocsCancelInfo.depositReturnMethod = AttributeTranslator
				.byteFromString(depositReturnMethod);
				amdocsCancelInfo.waiveReason = AttributeTranslator
				.emptyFromNull(waiveReason);
				amdocsCancelInfo.isPortActivity =isPortActivity;

				// store
				LOGGER.debug("Excecuting cancelBan() - start..." );
				updateBanConv.cancelBan(amdocsCancelInfo);
				LOGGER.debug("Excecuting cancelBan() - end..." );

				return null;   
			}
		});
	}

	@Override
	public void updateAuthorizationNames(final int ban,
			final ConsumerNameInfo[] authorizationNames, String sessionId)
	throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {

				NameInfo amdocsNameInfo = new NameInfo();

				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(ban);

				// update first authorization name
				if (authorizationNames.length > 0
						&& authorizationNames[0] != null) {
					amdocsNameInfo = (NameInfo) AmdocsTelusMapping.mapTelusToAmdocs(authorizationNames[0], amdocsNameInfo);
					updateBanConv.setFirstAuthName(amdocsNameInfo);
				} else {
					updateBanConv.deleteFirstAuthName();
				}

				// update second authorization name
				if (authorizationNames.length > 1
						&& authorizationNames[1] != null) {
					amdocsNameInfo = (NameInfo) AmdocsTelusMapping.mapTelusToAmdocs(authorizationNames[1], amdocsNameInfo);
					updateBanConv.setSecondAuthName(amdocsNameInfo);
				} else {
					updateBanConv.deleteSecondAuthName();
				}

				updateBanConv.saveBan();


				return null;   
			}
		});


	}

	@Override
	public void updateAutoTreatment(final int ban, final boolean holdAutoTreatment,
			String sessionId) throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {

				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(ban);

				updateBanConv.setAutoTreatment(holdAutoTreatment);

				updateBanConv.saveBan();

				return null;   
			}
		});


	}

	@Override
	public void updateBrand(final int ban, final int brandId, final String memoText,
			String sessionId) throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {

				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(ban);

				AccountTypeInfo accountTypeInfo = updateBanConv.getAccountTypeInfo();
				accountTypeInfo.brandId = brandId;
				//set memo text
				updateBanConv.setAccountTypeInfo(accountTypeInfo, memoText); //TO DO: Add memo text parameter
				updateBanConv.saveBan();

				return null;   
			}
		});

	}

	@Override
	public void updateSpecialInstructions(final int ban, final String specialInstructions,
			String sessionId) throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {

				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(ban);

				// update special instructions
				if (updateBanConv.getSpecialInstructions() == null) {
					if (!Info.isEmpty(specialInstructions)) {
						updateBanConv.setSpecialInstructions(specialInstructions);
					}
				} else {
					LOGGER.debug("Excecuting changeSpecialInstructions() from ["
							+ updateBanConv.getSpecialInstructions().userText
							+ "] to [" + specialInstructions
							+ "] - start...");
					updateBanConv.changeSpecialInstructions("");
					if (!Info.isEmpty(specialInstructions)) {
						updateBanConv
						.setSpecialInstructions(specialInstructions);
					}
				}

				return null;   
			}
		});

	}

	@Override
	public int[] retrieveAccountsByTalkGroup(final int urbanId, final int fleetId,
			final int talkGroupId, String sessionId) throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<int[]>() {

			@Override
			public int[] doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				SearchServices amdocsSearchServices = transactionContext.createBean(SearchServices.class);	

				int[] accounts = null;

				// call Amdocs EJB to retrieve accounts
				SearchBan[] searchAccounts = amdocsSearchServices.searchByFleetTalkGroup(urbanId, fleetId, (short) talkGroupId);

				int searchAccountsSize = searchAccounts != null ? searchAccounts.length : 0;
				LOGGER.debug("searchAccountsSize = " + searchAccountsSize);

				accounts = new int[searchAccountsSize];

				for (int i = 0; i < searchAccountsSize; i++) {
					accounts[i] = searchAccounts[i].ban;
				}

				return accounts;
			}			
		});
	}

	@Override
	public AddressValidationResultInfo validateAddress(final AddressInfo addressInfo,
			String sessionId) throws ApplicationException {
		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<AddressValidationResultInfo>() {

			@Override
			public AddressValidationResultInfo doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				AddressMatchServices amdocsAddressMatchServices = transactionContext.createBean(AddressMatchServices.class);
				// Telus Info classes
				AddressValidationResultInfo addressValidationResultInfo = new AddressValidationResultInfo();

				// Amdocs API Info classes
				amdocs.APILink.datatypes.AddressInfo amdocsAddressInfo = new amdocs.APILink.datatypes.AddressInfo();
				AddressMatchInfo amdocsAddressMatchInfo = new AddressMatchInfo();


				// populate Amdocs Info classes
				amdocsAddressInfo = (amdocs.APILink.datatypes.AddressInfo)AmdocsTelusMapping.mapTelusToAmdocs(addressInfo,amdocsAddressInfo);

				// print address that is input to address validation
				LOGGER.debug("Address Validation Input:");
				printAddress(amdocsAddressInfo);

				// perform address validation
				LOGGER.debug("Excecuting matchAddress() - start...");
				amdocsAddressMatchInfo = amdocsAddressMatchServices.matchAddress(amdocsAddressInfo);
				LOGGER.debug("Excecuting matchAddress() - end...");

				// print address that is input to address validation
				LOGGER.debug("Address Validation Output:");
				printAddress(amdocsAddressMatchInfo.addressInfo);

				// map from amdocs to our info class
				addressValidationResultInfo = mapAddressMatchInfo(amdocsAddressMatchInfo);

				return addressValidationResultInfo;
			}

		});
	}

	/**
	 * Print Address
	 *
	 * @param   String        method name
	 * @param   Adress        address
	 *
	 */
	private void printAddress(amdocs.APILink.datatypes.AddressInfo amdocsAddressInfo) {

		LOGGER.debug("  type: " + amdocsAddressInfo.type);
		LOGGER.debug("  attention: " + amdocsAddressInfo.attention);
		LOGGER.debug("  primaryLine: " + amdocsAddressInfo.primaryLine);
		LOGGER.debug("  secondaryLine: " + amdocsAddressInfo.secondaryLine);
		LOGGER.debug("  city: " + amdocsAddressInfo.city);
		LOGGER.debug("  province: " + amdocsAddressInfo.province);
		LOGGER.debug("  postalCode: " + amdocsAddressInfo.postalCode);
		LOGGER.debug("  civicNo: " + amdocsAddressInfo.civicNo);
		LOGGER.debug("  civicNoSuffix: " + amdocsAddressInfo.civicNoSuffix);
		LOGGER.debug("  streetDirection: " + amdocsAddressInfo.streetDirection);
		LOGGER.debug("  streetName: " + amdocsAddressInfo.streetName);
		LOGGER.debug("  streetType: " + amdocsAddressInfo.streetType);
		LOGGER.debug("  unitDesignator: " + amdocsAddressInfo.unitDesignator);
		LOGGER.debug("  unitIdentifier: " + amdocsAddressInfo.unitIdentifier);
		LOGGER.debug("  rrAreaNumber: " + amdocsAddressInfo.rrAreaNumber);
		LOGGER.debug("  rrBox: " + amdocsAddressInfo.rrBox);
		LOGGER.debug("  rrCompartment: " + amdocsAddressInfo.rrCompartment);
		LOGGER.debug("  rrDeliveryType: " + amdocsAddressInfo.rrDeliveryType);
		LOGGER.debug("  rrDesignator: " + amdocsAddressInfo.rrDesignator);
		LOGGER.debug("  rrGroup: " + amdocsAddressInfo.rrGroup);
		LOGGER.debug("  rrIdentifier: " + amdocsAddressInfo.rrIdentifier);
		LOGGER.debug("  rrQualifier: " + amdocsAddressInfo.rrQualifier);
		LOGGER.debug("  rrSite: " + amdocsAddressInfo.rrSite);
		LOGGER.debug("  country: " + amdocsAddressInfo.country);
		LOGGER.debug("  zipGeoCode: " + amdocsAddressInfo.zipGeoCode);
		LOGGER.debug("  foreignState: " + amdocsAddressInfo.foreignState);
	}

	/**
	 * Maps Amdocs AddressMatchInfo structure to Telus Mobility datastructures
	 *
	 * @param   AdressMatch                   Amdocs Info class
	 * @return  AddressValidationResultInfo   Telus Info class
	 * @throws ApplicationException 
	 *
	 * @see     AddressValidationResultInfo
	 */
	private AddressValidationResultInfo mapAddressMatchInfo(AddressMatchInfo pAmdocsAddressMatchInfo) throws ApplicationException {

		AddressValidationResultInfo addressValidationResultInfo = new AddressValidationResultInfo();
		String nullString = null;


		addressValidationResultInfo.setVerifiedAddress((com.telus.eas.account.info.AddressInfo) AmdocsTelusMapping.mapAmdocsToTelus(pAmdocsAddressMatchInfo.addressInfo,addressValidationResultInfo.getVerifiedAddress()));

		if (addressValidationResultInfo.getVerifiedAddress().getCivicNo() != null &&
				addressValidationResultInfo.getVerifiedAddress().getCivicNo().equals("0"))
			addressValidationResultInfo.getVerifiedAddress().setCivicNo(nullString);

		int countValidationMessages = pAmdocsAddressMatchInfo.warnings.length;
		int countInformationalMessages = 0;
		int countWarningMessages = 0;
		int countErrorMessages = 0;
		int countDBErrorMessages = 0;
		int countDBWarningMessages = 0;

		addressValidationResultInfo.setCountValidationMessages(countValidationMessages);

		LOGGER.debug("countValidationMessages:" + countValidationMessages);

		if (countValidationMessages > 0) {
			AddressValidationMessagesInfo validationMessages[] = new AddressValidationMessagesInfo[countValidationMessages];
			for (int i2=0; i2 < pAmdocsAddressMatchInfo.warnings.length; i2++) {

				validationMessages[i2] = new AddressValidationMessagesInfo();
				validationMessages[i2].setCode(pAmdocsAddressMatchInfo.warnings[i2].msgcode);
				validationMessages[i2].setMessage(pAmdocsAddressMatchInfo.warnings[i2].message);
				validationMessages[i2].setSeverity(AttributeTranslator.stringFrombyte(pAmdocsAddressMatchInfo.warnings[i2].severity));
				if (validationMessages[i2].getSeverity().equals("R")) countDBErrorMessages++;
				if (validationMessages[i2].getSeverity().equals("D")) countDBWarningMessages++;
				if (validationMessages[i2].getSeverity().equals("E")) countErrorMessages++;
				if (validationMessages[i2].getSeverity().equals("I")) countInformationalMessages++;
				if (validationMessages[i2].getSeverity().equals("W")) countWarningMessages++;
			}
			addressValidationResultInfo.setValidationMessages(validationMessages);
			addressValidationResultInfo.setCountInformationalMessages(countInformationalMessages);
			addressValidationResultInfo.setCountWarningMessages(countWarningMessages);
			addressValidationResultInfo.setCountErrorMessages(countErrorMessages);
			addressValidationResultInfo.setCountDBErrorMessages(countDBErrorMessages);
			addressValidationResultInfo.setCountDBWarningMessages(countDBWarningMessages);
			addressValidationResultInfo.setCountOtherMessages(countValidationMessages -
					countInformationalMessages -
					countWarningMessages -
					countErrorMessages -
					countDBErrorMessages -
					countDBWarningMessages);
		}

		return addressValidationResultInfo;


	}

	@Override
	public void changePostpaidConsumerToPrepaidConsumer(final int ban, final short prepaidBillCycle,
			String sessionId) throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);	
				updateBanConv.setBanPK(ban);

				AccountTypeInfo amdocsAccountTypeInfo = new AccountTypeInfo();
				CycleInfo amdocsCycleInfo = new CycleInfo();

					// Set/validate account sub type
					amdocsAccountTypeInfo = updateBanConv.getAccountTypeInfo();
					amdocsAccountTypeInfo.accountSubType = ((byte) AccountInfo.ACCOUNT_SUBTYPE_PCS_PREPAID);
					updateBanConv.setAccountTypeInfo(amdocsAccountTypeInfo);

					// save BAN
					updateBanConv.saveBan();

					// change bill cycle
					amdocsCycleInfo = updateBanConv.getCycleInfo();
					amdocsCycleInfo.cycleCode = prepaidBillCycle;

					// update bill cycle
					updateBanConv.changeCycleInfo(amdocsCycleInfo);

				return null;

			}

		});
		
	}

	@Override
	public List<DiscountInfo> retrieveDiscounts(final int ban, String sessionId)
	throws ApplicationException {

		return getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<List<DiscountInfo>>() {

			@Override
			public List<DiscountInfo> doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				DiscountInfo discountInfo = new DiscountInfo();
				List<DiscountInfo> discountInfoList=new ArrayList<DiscountInfo>();
				amdocs.APILink.datatypes.AppliedDiscountInfo[] amdocsAppliedDiscountInfoArray = null;

				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);	
				updateBanConv.setBanPK(ban);

				// retrieve discounts
				amdocsAppliedDiscountInfoArray = updateBanConv.getAppliedDiscounts();

				// map amdocs info class to telus info class
				if (amdocsAppliedDiscountInfoArray != null) {

					for (int i = 0; i < amdocsAppliedDiscountInfoArray.length; i++) {
						discountInfo = new DiscountInfo();
						discountInfo.setBan(ban);
						discountInfo.setDiscountCode(amdocsAppliedDiscountInfoArray[i].discountKey.code);
						discountInfo.setEffectiveDate(amdocsAppliedDiscountInfoArray[i].discountKey.effectiveDate);
						discountInfo.setExpiryDate(amdocsAppliedDiscountInfoArray[i].discountKey.expirationDate);
						discountInfo.setDiscountSequenceNo(amdocsAppliedDiscountInfoArray[i].discountKey.sequenceNumber);
						discountInfo.setDiscountByUserId("" + amdocsAppliedDiscountInfoArray[i].discountByUserId + "");
						discountInfoList.add(discountInfo);
					}
				}

				return discountInfoList;

			}

		});

	}

	@Override
	public void cancelAccountForPortOut(final int ban, final String activityReasonCode,
			final Date activityDate, final boolean portOutInd, final boolean isBrandPort,
			String sessionId) throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);	
				updateBanConv.setBanPK(ban);

				CancelInfo cancelInfo = new CancelInfo();

				// Populate CancelInfo
				cancelInfo.activityDate = activityDate == null ? new Date() : activityDate;
				cancelInfo.activityReason = activityReasonCode;
				cancelInfo.userText = "";
				cancelInfo.depositReturnMethod = 'O';
				cancelInfo.waiveReason = "";
				cancelInfo.isPortActivity = portOutInd;
				cancelInfo.isBrandPortActivity=isBrandPort;

				updateBanConv.cancelBan(cancelInfo);

				return null;
			}

		});

	}
	
	
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.account.lifecyclemanager.dao.AccountDao#updateAccount(com.telus.eas.account.info.AccountInfo, java.lang.String)
	 */
	@Override
	public void updateAccount(AccountInfo accountInfo, String sessionId) throws ApplicationException {
		updateAccount (accountInfo, false, sessionId);
	}

	/**
	 * Update an account - includes validation of all information except address
	 * and duplicate account check. It is assumed that the address that is being
	 * passed in is the address that needs to be stored, therefore the address
	 * apply type is set to 'I'. (It is up to the client to perform address
	 * validation if required.)
	 *
	 * @param AccountInfo
	 *            all general attributes of the account (i.e. account type, sub
	 *            type etc)
	 *
	 * @param blockDirectUpdate rather it should allow/disallow update on certain fields. if true, certain fields/objects will not be
	 *                          stored even it's set.
	 *
	 * @param sessionId   the EJB sessionId
	 *
	 * @see AccountInfo
	 *
	 * @excpetion ApplicationException
	 */
	
	@Override
	public void updateAccount(final AccountInfo accountInfo, final boolean blockDirectUpdate, String sessionId) throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {
				String methodName = "updateAccount"; 
				HashMap<Object,Object> amdocsClasses = null;
				boolean saveBanNecessary = false;
				String oldPaymentMethod = "";
				String newPaymentMethod = "";
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") Incoming AccountInfo object for BAN["+accountInfo.getBanId()+"]..."+accountInfo);
				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(accountInfo.getBanId());
				
				 amdocsClasses = new HashMap<Object,Object>();
			      amdocsClasses.put(AttributeTranslator.getBaseName(new AccountTypeInfo().getClass()),updateBanConv.getAccountTypeInfo());
			      amdocsClasses.put("Billing" + AttributeTranslator.getBaseName(new NameInfo().getClass()),updateBanConv.getBillingName());
			      amdocsClasses.put("Contact" + AttributeTranslator.getBaseName(new NameInfo().getClass()),updateBanConv.getContactName());
			      amdocsClasses.put("Billing" + AttributeTranslator.getBaseName(new AddressInfo().getClass()),updateBanConv.getBillingAddress());
			      amdocsClasses.put("CreditCheck" + AttributeTranslator.getBaseName(new AddressInfo().getClass()),updateBanConv.getCreditAddress());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new IdentificationInfo().getClass()),updateBanConv.getIdentificationInfo());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new ContractInfo().getClass()),updateBanConv.getContractInfo());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new ContactInfo().getClass()),updateBanConv.getContactInfo());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.CreditCardInfo().getClass()),updateBanConv.getCreditCardInfo());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new CreditCardDetailsInfo().getClass()),new CreditCardDetailsInfo());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new CheckDetailsInfo().getClass()),new CheckDetailsInfo());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new CycleInfo().getClass()),updateBanConv.getCycleInfo());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new BusinessInfo().getClass()),updateBanConv.getBusinessInfo());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new BillDetailsInfo().getClass()),updateBanConv.getBillDetailsInfo());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new NationalGrowthInfo().getClass()),updateBanConv.getNationalGrowthInfo());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new CPUIInfo().getClass()),updateBanConv.getBanCPUIInfo());
			      amdocsClasses.put(AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.TaxExemptionInfo().getClass()), updateBanConv.getTaxExemptionInfo());
			      

			      // populate Amdocs Info classes
			      amdocsClasses = TelusToAmdocsAccountMapper.mapAccountInfoToAmdocs(accountInfo, null);
			     
			   // get amdocs classes in hash map
			      AccountTypeInfo amdocsAccountTypeInfo = (AccountTypeInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new AccountTypeInfo().getClass()));
			      NameInfo amdocsBillingNameInfo = (NameInfo)amdocsClasses.get("Billing" + AttributeTranslator.getBaseName(new NameInfo().getClass()));
			      NameInfo amdocsContactNameInfo = (NameInfo)amdocsClasses.get("Contact" + AttributeTranslator.getBaseName(new NameInfo().getClass()));
			      amdocs.APILink.datatypes.AddressInfo amdocsBillingAddressInfo = (amdocs.APILink.datatypes.AddressInfo)amdocsClasses.get("Billing" + AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.AddressInfo().getClass()));
			      amdocs.APILink.datatypes.AddressInfo amdocsCreditCheckAddressInfo = (amdocs.APILink.datatypes.AddressInfo)amdocsClasses.get("CreditCheck" + AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.AddressInfo().getClass()));
			      IdentificationInfo amdocsIdentificationInfo = (IdentificationInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new IdentificationInfo().getClass()));
			      ContractInfo amdocsContractInfo = (ContractInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new ContractInfo().getClass()));
			      ContactInfo amdocsContactInfo = (ContactInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new ContactInfo().getClass()));
			      amdocs.APILink.datatypes.CreditCardInfo amdocsCreditCardInfo = (amdocs.APILink.datatypes.CreditCardInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.CreditCardInfo().getClass()));
			      CreditCardDetailsInfo amdocsCreditCardDetailsInfo = (CreditCardDetailsInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new CreditCardDetailsInfo().getClass()));
			      CheckDetailsInfo amdocsCheckDetailsInfo = (CheckDetailsInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new CheckDetailsInfo().getClass()));
			      CycleInfo amdocsCycleInfo = (CycleInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new CycleInfo().getClass()));
			      BusinessInfo amdocsBusinessInfo = (BusinessInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new BusinessInfo().getClass()));
			      BillDetailsInfo amdocsBillDetailsInfo = (BillDetailsInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new BillDetailsInfo().getClass()));
			      NationalGrowthInfo amdocsNationalGrowthInfo = (NationalGrowthInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new NationalGrowthInfo().getClass()));
			      CPUIInfo[] amdocsCPUIInfo = (CPUIInfo[])amdocsClasses.get(AttributeTranslator.getBaseName(new CPUIInfo().getClass()));
			      amdocs.APILink.datatypes.TaxExemptionInfo amdocsTaxExemptionInfo = (amdocs.APILink.datatypes.TaxExemptionInfo)amdocsClasses.get(AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.TaxExemptionInfo().getClass()));
			      /** preserve segmentations for business BANs */
			      if (updateBanConv.getBusinessInfo() != null) {
			    	  amdocsBusinessInfo.glSegment = updateBanConv.getBusinessInfo().glSegment;
			      	amdocsBusinessInfo.glSubSegment = updateBanConv.getBusinessInfo().glSubSegment;
			      }
			      // Account Type
			      AccountTypeInfo originalAmdocsAccountTypeInfo = updateBanConv.getAccountTypeInfo();
			      if (amdocsAccountTypeInfo.accountSubType != originalAmdocsAccountTypeInfo.accountSubType  ||
			          amdocsAccountTypeInfo.accountType != originalAmdocsAccountTypeInfo.accountType  ||
			          amdocsAccountTypeInfo.corporationType != originalAmdocsAccountTypeInfo.corporationType  ||
			          amdocsAccountTypeInfo.brandId != originalAmdocsAccountTypeInfo.brandId ||
			          AttributeTranslator.compare(amdocsAccountTypeInfo.businessType,originalAmdocsAccountTypeInfo.businessType) != 0 ) {
			        saveBanNecessary = true;
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setAccountTypeInfo()...");
			        updateBanConv.setAccountTypeInfo(amdocsAccountTypeInfo);
			      }
			      
			   // Billing Name
			      NameInfo originalAmdocsBillingNameInfo = updateBanConv.getBillingName();
			      // Temporary workaround to handle the special case for Corporate Individual and Corporate Employee account types.
			      // First name has to be populated for Amdocs' name objects where the account type name format = 'P'.
			      // TODO Implement long-term solution to handle these account types more gracefully - R. Fong, 2005.11.02.
			      if (accountInfo.isCorporate() && (accountInfo.getBillingNameFormat() == AccountType.BILLING_NAME_FORMAT_PERSONAL)) {
			      	// Assumption is that the original name is not empty - otherwise an Amdocs name validation error will be thrown.
			      	amdocsBillingNameInfo.firstName = originalAmdocsBillingNameInfo.firstName;
			      }
			      if (!blockDirectUpdate && 
			    	  (AttributeTranslator.compare(amdocsBillingNameInfo.additionalTitle, originalAmdocsBillingNameInfo.additionalTitle) != 0 ||
			    	  AttributeTranslator.compare(amdocsBillingNameInfo.firstName, originalAmdocsBillingNameInfo.firstName) != 0 ||
			    	  AttributeTranslator.compare(amdocsBillingNameInfo.lastBusinessName, originalAmdocsBillingNameInfo.lastBusinessName) != 0 ||
			    	  AttributeTranslator.compare(amdocsBillingNameInfo.middleInitial, originalAmdocsBillingNameInfo.middleInitial) != 0 ||
			    	  AttributeTranslator.compare(amdocsBillingNameInfo.nameSuffix, originalAmdocsBillingNameInfo.nameSuffix) != 0 ||
			    	  AttributeTranslator.compare(amdocsBillingNameInfo.nameTitle, originalAmdocsBillingNameInfo.nameTitle) != 0)) {
			        saveBanNecessary = true;
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setBillingName()...");
			        updateBanConv.setBillingName(amdocsBillingNameInfo);
			      }
			      
			   // Contact Name
			      NameInfo originalAmdocsContactNameInfo = updateBanConv.getContactName();
			      if (!amdocsContactNameInfo.lastBusinessName.trim().equals("") &&
			          (AttributeTranslator.compare(amdocsContactNameInfo.additionalTitle, originalAmdocsContactNameInfo.additionalTitle) != 0 ||
			           AttributeTranslator.compare(amdocsContactNameInfo.firstName, originalAmdocsContactNameInfo.firstName) != 0 ||
			           AttributeTranslator.compare(amdocsContactNameInfo.lastBusinessName, originalAmdocsContactNameInfo.lastBusinessName) != 0 ||
			           AttributeTranslator.compare(amdocsContactNameInfo.middleInitial, originalAmdocsContactNameInfo.middleInitial) != 0 ||
			           AttributeTranslator.compare(amdocsContactNameInfo.nameSuffix, originalAmdocsContactNameInfo.nameSuffix) != 0 ||
			           AttributeTranslator.compare(amdocsContactNameInfo.nameTitle, originalAmdocsContactNameInfo.nameTitle) != 0)) {
			        saveBanNecessary = true;
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setContactName()...");
			        updateBanConv.setContactName(amdocsContactNameInfo);
			      }
			      
			   // Business Info (only for business regular)
			      BusinessInfo originalAmdocsBusinessInfo = updateBanConv.getBusinessInfo();
			      if (accountInfo instanceof PostpaidBusinessRegularAccountInfo && amdocsBusinessInfo != null && originalAmdocsBusinessInfo != null) {
			          if (amdocsBusinessInfo.sicCode != originalAmdocsBusinessInfo.sicCode ||
			             (amdocsBusinessInfo.incorporationDate != null && !amdocsBusinessInfo.incorporationDate.equals(originalAmdocsBusinessInfo.incorporationDate)) ||
			             AttributeTranslator.compare(amdocsBusinessInfo.businessNature, originalAmdocsBusinessInfo.businessNature) != 0 ||
			             AttributeTranslator.compare(amdocsBusinessInfo.incorporationNo, originalAmdocsBusinessInfo.incorporationNo) != 0 ||
			             AttributeTranslator.compare(amdocsBusinessInfo.director, originalAmdocsBusinessInfo.director) != 0 ||
			             AttributeTranslator.compare(amdocsBusinessInfo.applicantName, originalAmdocsBusinessInfo.applicantName) != 0 ) {
			               saveBanNecessary = true;
			               LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setBusinessInfo()...");
			               updateBanConv.setBusinessInfo(amdocsBusinessInfo);
			           }
			      }
			      
			   // Identification Info
			      IdentificationInfo originalAmdocsIdentificationInfo = updateBanConv.getIdentificationInfo();
			      // Depending on the account type, we do not support DL information but the account
			      // might have that information - the code below will set the DL attributes to the attributes
			      // currently in the database. Otherwise, the DL-number and DL-province will be erased but
			      // the DL-expiry date remains which will result in a Amdocs exception on a subsequent update
			      if (!blockDirectUpdate && amdocsIdentificationInfo.drivrLicnsExpDt == null &&
			          (amdocsIdentificationInfo.drivrLicnsNo == null || amdocsIdentificationInfo.drivrLicnsNo.trim().equals("")) &&
			          (amdocsIdentificationInfo.drivrLicnsProvince == null || amdocsIdentificationInfo.drivrLicnsProvince.trim().equals(""))) {
			        amdocsIdentificationInfo.drivrLicnsExpDt = originalAmdocsIdentificationInfo.drivrLicnsExpDt;
			        amdocsIdentificationInfo.drivrLicnsNo = originalAmdocsIdentificationInfo.drivrLicnsNo;
			        amdocsIdentificationInfo.drivrLicnsProvince = originalAmdocsIdentificationInfo.drivrLicnsProvince;
			      }
			      if (!blockDirectUpdate && (amdocsIdentificationInfo.birthDate != null && originalAmdocsIdentificationInfo.birthDate == null ||
			          (amdocsIdentificationInfo.birthDate != null && originalAmdocsIdentificationInfo.birthDate != null && amdocsIdentificationInfo.birthDate.compareTo(originalAmdocsIdentificationInfo.birthDate) != 0)  ||
			          amdocsIdentificationInfo.customerSsn != originalAmdocsIdentificationInfo.customerSsn ||
			          (amdocsIdentificationInfo.drivrLicnsExpDt != null && originalAmdocsIdentificationInfo.drivrLicnsExpDt != null && amdocsIdentificationInfo.drivrLicnsExpDt.compareTo(originalAmdocsIdentificationInfo.drivrLicnsExpDt) != 0) ||
			          AttributeTranslator.compare(amdocsIdentificationInfo.drivrLicnsNo, originalAmdocsIdentificationInfo.drivrLicnsNo) != 0  ||
			          AttributeTranslator.compare(amdocsIdentificationInfo.drivrLicnsProvince, originalAmdocsIdentificationInfo.drivrLicnsProvince) != 0  ||
			          AttributeTranslator.compare(amdocsIdentificationInfo.emailAddress, originalAmdocsIdentificationInfo.emailAddress) != 0  ||
			          AttributeTranslator.compare(amdocsIdentificationInfo.langPref, originalAmdocsIdentificationInfo.langPref) != 0  ||
			          AttributeTranslator.compare(amdocsIdentificationInfo.accPassword, originalAmdocsIdentificationInfo.accPassword) != 0 ||
			          amdocsIdentificationInfo.verifiedDate != null && originalAmdocsIdentificationInfo.verifiedDate == null ||
			           (amdocsIdentificationInfo.verifiedDate != null && originalAmdocsIdentificationInfo.verifiedDate != null && amdocsIdentificationInfo.verifiedDate.compareTo(originalAmdocsIdentificationInfo.verifiedDate) != 0) )) {
			        saveBanNecessary = true;
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setIdentificationInfo()...");
			        updateBanConv.setIdentificationInfo(resetDOB(amdocsIdentificationInfo));
			      }else if (blockDirectUpdate) {
			    	  boolean updateIdentificationRequired = false;
			    	  
			    	  if (AttributeTranslator.compare(amdocsIdentificationInfo.emailAddress, originalAmdocsIdentificationInfo.emailAddress) != 0) {
			    		  originalAmdocsIdentificationInfo.emailAddress = amdocsIdentificationInfo.emailAddress;
			    		  updateIdentificationRequired = true;
			    	  }
			    	  
					  if (AttributeTranslator.compare(amdocsIdentificationInfo.langPref, originalAmdocsIdentificationInfo.langPref) != 0) {
						  originalAmdocsIdentificationInfo.langPref = amdocsIdentificationInfo.langPref;
						  updateIdentificationRequired = true;
					  }
					  
					  if (amdocsIdentificationInfo.verifiedDate != null && originalAmdocsIdentificationInfo.verifiedDate == null ||
					           (amdocsIdentificationInfo.verifiedDate != null && originalAmdocsIdentificationInfo.verifiedDate != null && amdocsIdentificationInfo.verifiedDate.compareTo(originalAmdocsIdentificationInfo.verifiedDate) != 0)) {
						  originalAmdocsIdentificationInfo.verifiedDate = amdocsIdentificationInfo.verifiedDate;
						  updateIdentificationRequired = true;
					  }
					  
					  if (updateIdentificationRequired) {
						  LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setIdentificationInfo() on originalAmdocsIdentificationInfo...");
						  saveBanNecessary = true;
						  updateBanConv.setIdentificationInfo(originalAmdocsIdentificationInfo);
					  }
			      }
			      
			   // Contact Info
			      ContactInfo originalAmdocsContactInfo = updateBanConv.getContactInfo();
			      // Temporary workaround to populate work or home phone number for Business Regular account types.
			      // Either work or home phone number has to be populated for Amdocs' contact objects.
			      // TODO This workaround should no longer be necessary since we are now handling work and home numbers
			      // for Business Regular accounts; however, this will need to be tested extensively! - R. Fong, 2005.11.25.
//			      if (pAccountInfo instanceof PostpaidBusinessRegularAccountInfo) {
//			    	  // Check the work phone number - if it's empty, populate with the contact number
//			    	  if (amdocsContactInfo.workTelNo.equals("")) {
//			    		  amdocsContactInfo.workTelNo = originalAmdocsContactInfo.workTelNo.equals("") ? amdocsContactInfo.contactTelNo : originalAmdocsContactInfo.workTelNo;
//			    		  amdocsContactInfo.workTelExtNo = originalAmdocsContactInfo.workTelExtNo.equals("") ? amdocsContactInfo.contactTelExtNo : originalAmdocsContactInfo.workTelExtNo;
//			    	  }
//			    	  // If work number is empty, check the home phone number
//			    	  if (amdocsContactInfo.workTelNo.equals("") && amdocsContactInfo.homeTelNo.equals("")) {
//			    		  amdocsContactInfo.homeTelNo = originalAmdocsContactInfo.homeTelNo;
//			    	  }
//			      }
			      if (AttributeTranslator.compare(amdocsContactInfo.contactTelExtNo, originalAmdocsContactInfo.contactTelExtNo) != 0  ||
			    	  AttributeTranslator.compare(amdocsContactInfo.contactTelNo, originalAmdocsContactInfo.contactTelNo) != 0  ||
			    	  AttributeTranslator.compare(amdocsContactInfo.homeTelNo, originalAmdocsContactInfo.homeTelNo) != 0  ||
			    	  AttributeTranslator.compare(amdocsContactInfo.otherTelExtNo, originalAmdocsContactInfo.otherTelExtNo) != 0  ||
			    	  AttributeTranslator.compare(amdocsContactInfo.otherTelNo, originalAmdocsContactInfo.otherTelNo) != 0  ||
			    	  AttributeTranslator.compare(amdocsContactInfo.otherTelType, originalAmdocsContactInfo.otherTelType) != 0  ||
			    	  AttributeTranslator.compare(amdocsContactInfo.workTelExtNo, originalAmdocsContactInfo.workTelExtNo) != 0  ||
			    	  AttributeTranslator.compare(amdocsContactInfo.workTelNo, originalAmdocsContactInfo.workTelNo) != 0 ) {
			        saveBanNecessary = true;
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setContactInfo()...");
			        updateBanConv.setContactInfo(amdocsContactInfo);
			      }

			      // Address - Billing
			      amdocs.APILink.datatypes.AddressInfo originalAmdocsBillingAddressInfo = updateBanConv.getBillingAddress();
			      if (!blockDirectUpdate && AccountLifecycleUtilities.addressHasChanged(originalAmdocsBillingAddressInfo, amdocsBillingAddressInfo)) {
			        saveBanNecessary = true;
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setBillingAddress()...");
			        updateBanConv.setBillingAddress(amdocsBillingAddressInfo);
			      }
			      
			   // Address - CreditCheck
			      amdocs.APILink.datatypes.AddressInfo originalAmdocsCreditCheckAddressInfo = updateBanConv.getCreditAddress();
			      if (!amdocsCreditCheckAddressInfo.city.trim().equals("") &&
			          (amdocsCreditCheckAddressInfo.type != originalAmdocsCreditCheckAddressInfo.type  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.attention,originalAmdocsCreditCheckAddressInfo.attention) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.primaryLine,originalAmdocsCreditCheckAddressInfo.primaryLine) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.secondaryLine,originalAmdocsCreditCheckAddressInfo.secondaryLine) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.city,originalAmdocsCreditCheckAddressInfo.city) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.province,originalAmdocsCreditCheckAddressInfo.province) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.postalCode,originalAmdocsCreditCheckAddressInfo.postalCode) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.civicNo,originalAmdocsCreditCheckAddressInfo.civicNo) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.civicNoSuffix,originalAmdocsCreditCheckAddressInfo.civicNoSuffix) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.streetDirection,originalAmdocsCreditCheckAddressInfo.streetDirection) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.streetName,originalAmdocsCreditCheckAddressInfo.streetName) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.streetType,originalAmdocsCreditCheckAddressInfo.streetType) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.unitDesignator,originalAmdocsCreditCheckAddressInfo.unitDesignator) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.unitIdentifier,originalAmdocsCreditCheckAddressInfo.unitIdentifier) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrAreaNumber,originalAmdocsCreditCheckAddressInfo.rrAreaNumber) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrBox,originalAmdocsCreditCheckAddressInfo.rrBox) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrCompartment,originalAmdocsCreditCheckAddressInfo.rrCompartment) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrDeliveryType,originalAmdocsCreditCheckAddressInfo.rrDeliveryType) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrDesignator,originalAmdocsCreditCheckAddressInfo.rrDesignator) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrGroup,originalAmdocsCreditCheckAddressInfo.rrGroup) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrIdentifier,originalAmdocsCreditCheckAddressInfo.rrIdentifier) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrQualifier,originalAmdocsCreditCheckAddressInfo.rrQualifier) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.rrSite,originalAmdocsCreditCheckAddressInfo.rrSite) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.country,originalAmdocsCreditCheckAddressInfo.country) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.zipGeoCode,originalAmdocsCreditCheckAddressInfo.zipGeoCode) != 0  ||
			           AttributeTranslator.compare(amdocsCreditCheckAddressInfo.foreignState,originalAmdocsCreditCheckAddressInfo.foreignState) != 0 )) {
			        saveBanNecessary = true;
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setCreditAddress()...");
			        updateBanConv.setCreditAddress(amdocsCreditCheckAddressInfo);
			      }

			      // Dealer
			      ContractInfo originalAmdocsContractInfo = updateBanConv.getContractInfo();
			      originalAmdocsContractInfo.salesCode = originalAmdocsContractInfo.salesCode == null ? "0000" : originalAmdocsContractInfo.salesCode;

			      if (AttributeTranslator.compare(amdocsContractInfo.dealerCode,originalAmdocsContractInfo.dealerCode) != 0  ||
			    	  AttributeTranslator.compare(amdocsContractInfo.salesCode,originalAmdocsContractInfo.salesCode) != 0 ) {
			        saveBanNecessary = true;
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setContractInfo()...");
			        updateBanConv.setContractInfo(amdocsContractInfo);
			      }
			      
			   // Credit Card
			      amdocs.APILink.datatypes.CreditCardInfo originalAmdocsCreditCardInfo = updateBanConv.getCreditCardInfo();

			      String originalExpiryDateInMMYYYY = AttributeTranslator.emptyFromNull(originalAmdocsCreditCardInfo.creditCardExpDt);

			      if (!originalExpiryDateInMMYYYY.trim().equals(""))
			        originalExpiryDateInMMYYYY = originalAmdocsCreditCardInfo.creditCardExpDt.substring(4,6) + "/" + originalAmdocsCreditCardInfo.creditCardExpDt.substring(0,4);
			      if (!blockDirectUpdate && (AttributeTranslator.compare(amdocsCreditCardInfo.creditCardExpDt,originalExpiryDateInMMYYYY) != 0  ||
			    	  AttributeTranslator.compare(amdocsCreditCardInfo.creditCardNum,originalAmdocsCreditCardInfo.creditCardNum) != 0)) {
			        saveBanNecessary = true;
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setCreditCardInfo()...");
			        updateBanConv.setCreditCardInfo(amdocsCreditCardInfo);
			      }

			      // SpecialBillDetails Info
			      BillDetailsInfo originalBillDetailsInfo = updateBanConv.getBillDetailsInfo();

			      // Vladimir. Changed for def #41333 - start
			      // since we never retrieve csDefIxcCode just comment out the condition
			      //if (AttributeTranslator.compare(amdocsBillDetailsInfo.csDefIxcCode, originalBillDetailsInfo.csDefIxcCode) != 0
			      //  || ((amdocsBillDetailsInfo.invSuppressionInd != originalBillDetailsInfo.invSuppressionInd) && !(amdocsBillDetailsInfo.invSuppressionInd == 0 && originalBillDetailsInfo.invSuppressionInd == 32))
			      // Vladimir. Changed for def #41333 - end
			      if (((amdocsBillDetailsInfo.invSuppressionInd != originalBillDetailsInfo.invSuppressionInd) && !(amdocsBillDetailsInfo.invSuppressionInd == 0 && originalBillDetailsInfo.invSuppressionInd == 32))
			        || amdocsBillDetailsInfo.blManHndlReqOpid != originalBillDetailsInfo.blManHndlReqOpid
			        || ((amdocsBillDetailsInfo.blManHndlEffDate == null && originalBillDetailsInfo.blManHndlEffDate != null)
			            || (amdocsBillDetailsInfo.blManHndlEffDate != null && (originalBillDetailsInfo.blManHndlEffDate == null || amdocsBillDetailsInfo.blManHndlEffDate.compareTo(originalBillDetailsInfo.blManHndlEffDate) != 0))
			           )
			        || ((amdocsBillDetailsInfo.blManHndlExpDate == null && originalBillDetailsInfo.blManHndlExpDate != null)
			            || (amdocsBillDetailsInfo.blManHndlExpDate != null && (originalBillDetailsInfo.blManHndlExpDate == null || amdocsBillDetailsInfo.blManHndlExpDate.compareTo(originalBillDetailsInfo.blManHndlExpDate) != 0))
			           )) {
			        saveBanNecessary = true;
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setBillDetailsInfo()...");
			        SpecialBillDetailsInfo specialBillDetailsInfo = new SpecialBillDetailsInfo();

			        specialBillDetailsInfo.blManHndlReqOpid = amdocsBillDetailsInfo.blManHndlReqOpid;
			        specialBillDetailsInfo.blManHndlEffDate = amdocsBillDetailsInfo.blManHndlEffDate;
			        specialBillDetailsInfo.blManHndlExpDate = amdocsBillDetailsInfo.blManHndlExpDate;
			        specialBillDetailsInfo.invSuppressionInd = amdocsBillDetailsInfo.invSuppressionInd;
			        specialBillDetailsInfo.blManHndlByOpid = amdocsBillDetailsInfo.blManHndlByOpid;
			        specialBillDetailsInfo.blBalHandleInd = amdocsBillDetailsInfo.blBalHandleInd;
			        specialBillDetailsInfo.blBillProdInd = amdocsBillDetailsInfo.blBillProdInd;
			        specialBillDetailsInfo.blOlpImpactImm = amdocsBillDetailsInfo.blOlpImpactImm;
			        specialBillDetailsInfo.blZeroBalancInd = amdocsBillDetailsInfo.blZeroBalancInd;
			        specialBillDetailsInfo.csBanTrxOffInd = amdocsBillDetailsInfo.csBanTrxOffInd;
			        specialBillDetailsInfo.csCpniCode = amdocsBillDetailsInfo.csCpniCode;
			        specialBillDetailsInfo.csDefIxcCode = amdocsBillDetailsInfo.csDefIxcCode;
			        specialBillDetailsInfo.csHandleByCtnInd = amdocsBillDetailsInfo.csHandleByCtnInd;
			        specialBillDetailsInfo.csLockMechanism = amdocsBillDetailsInfo.csLockMechanism;
			        specialBillDetailsInfo.csRetEnvlpInd = amdocsBillDetailsInfo.csRetEnvlpInd;
			        specialBillDetailsInfo.csVipClass = amdocsBillDetailsInfo.csVipClass;
			        specialBillDetailsInfo.studySection = amdocsBillDetailsInfo.studySection;

			        updateBanConv.setBillDetailsInfo(specialBillDetailsInfo);
			      }

			      // NationalGrowth Info
			      NationalGrowthInfo originalNationalGrowthInfo = updateBanConv.getNationalGrowthInfo();
			      if (AttributeTranslator.compare(amdocsNationalGrowthInfo.homeProvince,originalNationalGrowthInfo.homeProvince) != 0  ||
			        amdocsNationalGrowthInfo.nationalAccount != originalNationalGrowthInfo.nationalAccount) {
			        saveBanNecessary = true;
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setNationalGrowthInfo()...");
			        updateBanConv.changeNationalGrowthInfo(amdocsNationalGrowthInfo);
			      }
			      
			   // CPUI Info
			      LOGGER.debug("("+getClass().getName()+"."+ methodName+") amdocsCPUIInfo.length..." + amdocsCPUIInfo.length);

			      if (amdocsCPUIInfo.length != 0 && amdocsCPUIInfo[0].cpuiCd != null ) {
			      	CPUIInfo[] originalCPUIInfo = updateBanConv.getBanCPUIInfo();
			      	/* for (int i=0;i< originalCPUIInfo.length;i++)
			         {
			      	 	print(getClass().getName(),methodName,"ORIGINAL code..." + originalCPUIInfo[i].cpuiCd);
			      	 	print(getClass().getName(),methodName,"ORIGINAL Mode..." + originalCPUIInfo[i].cpuiMode);
			         }
			      	 */
			      	 for (int i=0;i< amdocsCPUIInfo.length;i++)
			         {
			      	 	//print(getClass().getName(),methodName,"NEW code..." + amdocsCPUIInfo[i].cpuiCd);
			      	 	//print(getClass().getName(),methodName,"NEW Mode..." + amdocsCPUIInfo[i].cpuiMode);
			      	 	amdocsCPUIInfo[i].cpuiCd = amdocsCPUIInfo[i].cpuiCd.trim();
			         }
			        boolean difCPUIInfo = compare(originalCPUIInfo,amdocsCPUIInfo);

			        // print(getClass().getName(),methodName,"difCPUIInfo..." + difCPUIInfo);
			        if ( difCPUIInfo ) {
			        	// saveBanNecessary = true;
			        	LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setCPUIInfo()...");

			        	// remove current cpui codes
			        	boolean oldValueExist = false;
			        	for (int i=0;i< originalCPUIInfo.length;i++)
			        	{
			        		oldValueExist = true;
			        		LOGGER.debug("("+getClass().getName()+"."+ methodName+") REMOVE ORIGINAL VALUES:  " );
			        		LOGGER.debug("("+getClass().getName()+"."+ methodName+") orig CPUICode:" + originalCPUIInfo[i].cpuiCd);
			        		originalCPUIInfo[i].cpuiMode = DELETE;
			        	}
			        	if (oldValueExist)
			        		updateBanConv.setBanCPUIInfo(originalCPUIInfo,"");

			        	boolean newValueExist = false;
			        	for (int i=0;i< amdocsCPUIInfo.length;i++)
			        	{
			        		newValueExist = true;
			        		LOGGER.debug("("+getClass().getName()+"."+ methodName+") INSERT NEW VALUES:  " );
			        		LOGGER.debug("("+getClass().getName()+"."+ methodName+") new CPUICode:" + amdocsCPUIInfo[i].cpuiCd);
			        		amdocsCPUIInfo[i].cpuiMode = INSERT;

			        	}
			        	if (newValueExist)
			        		updateBanConv.setBanCPUIInfo(amdocsCPUIInfo,"");

			        } //difCPUIInfo
			      }
			      
			   // Tax Exemption Info
			      amdocs.APILink.datatypes.TaxExemptionInfo originalAmdocsTaxExemptionInfo = updateBanConv.getTaxExemptionInfo();
			      boolean applyTaxExemption = false;      
			      if (originalAmdocsTaxExemptionInfo != null) {
			    	  if (amdocsTaxExemptionInfo.gstCertificateNumber == null)
			    		  amdocsTaxExemptionInfo.gstCertificateNumber = "";  //need to set this to empty string as it cannot be null (see defect # 101183)
			    	  
			    	  if (AttributeTranslator.compare(amdocsTaxExemptionInfo.gstCertificateNumber.trim(), originalAmdocsTaxExemptionInfo.gstCertificateNumber.trim()) != 0) {
			    		  applyTaxExemption = true;
			    	  }
			    	  
			    	  if (!applyTaxExemption && (AttributeTranslator.compare((new Byte(amdocsTaxExemptionInfo.gstExemptionInd)).toString(), (new Byte(originalAmdocsTaxExemptionInfo.gstExemptionInd)).toString()) != 0 ||
			    			  AttributeTranslator.compare((new Byte(amdocsTaxExemptionInfo.pstExemptionInd)).toString(), (new Byte(originalAmdocsTaxExemptionInfo.pstExemptionInd)).toString()) != 0 ||
			    	          AttributeTranslator.compare((new Byte(amdocsTaxExemptionInfo.hstExemptionInd)).toString(), (new Byte(originalAmdocsTaxExemptionInfo.hstExemptionInd)).toString()) != 0 ||
			    	          AttributeTranslator.compare(amdocsTaxExemptionInfo.gstEffDate, originalAmdocsTaxExemptionInfo.gstEffDate) != 0 ||
			    	          AttributeTranslator.compare(amdocsTaxExemptionInfo.pstEffDate, originalAmdocsTaxExemptionInfo.pstEffDate) != 0 ||
			    	          AttributeTranslator.compare(amdocsTaxExemptionInfo.hstEffDate, originalAmdocsTaxExemptionInfo.hstEffDate) != 0 ||
			    	          AttributeTranslator.compare(amdocsTaxExemptionInfo.gstExpDate, originalAmdocsTaxExemptionInfo.gstExpDate) != 0 ||
			    	          AttributeTranslator.compare(amdocsTaxExemptionInfo.pstExpDate, originalAmdocsTaxExemptionInfo.pstExpDate) != 0 ||
			    	          AttributeTranslator.compare(amdocsTaxExemptionInfo.hstExpDate, originalAmdocsTaxExemptionInfo.hstExpDate) != 0))    				  
			    		  applyTaxExemption = true;      
			      }
			    	  
			      if (applyTaxExemption) {
			    	  saveBanNecessary = true;
			          
			    	  LOGGER.debug("("+getClass().getName()+"."+ methodName+") Calling setContactName()...");
			    	  updateBanConv.setTaxExemptionInfo(amdocsTaxExemptionInfo);
			      }      
			      

			      // store BAN
			      if (saveBanNecessary) {
			    	  LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting saveBan() - start...");
			    	  updateBanConv.saveBan();
			    	  LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting saveBan() - end...");
			      }

			      // For Postpaid only: Update Payment Method (if changed)
			      if (accountInfo.isPostpaid()) {
			        // determine old/new payment method
			        oldPaymentMethod = AttributeTranslator.stringFrombyte(
			        		updateBanConv.getBillDetailsInfo().autoGenPymType);
			        if (accountInfo.isPostpaidConsumer() || accountInfo.isPostpaidEmployee() ||
			        		accountInfo.isPostpaidBoxedConsumer()) {
			          newPaymentMethod = ((PostpaidConsumerAccountInfo) accountInfo).getPaymentMethod0().getPaymentMethod();
			        } else if (accountInfo.isPostpaidBusinessPersonal() || accountInfo.isPostpaidCorporatePersonal()) {
			          newPaymentMethod = ((PostpaidBusinessPersonalAccountInfo)
			        		  accountInfo).getPaymentMethod0().getPaymentMethod();
			        } else {
			          newPaymentMethod = ((PostpaidBusinessRegularAccountInfo)
			        		  accountInfo).getPaymentMethod0().getPaymentMethod();
			        }

			        // new method is 'Regular'
			        // - change if old one was not regular
					// - set status to 'cancelled'
			        if (newPaymentMethod.equals(PaymentMethod.PAYMENT_METHOD_REGULAR) &&
			            !newPaymentMethod.equals(oldPaymentMethod)) {
						if (oldPaymentMethod.equals(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT)) {
							amdocsCheckDetailsInfo.directDebitStatus = PaymentMethodInfo.DIRECT_DEBIT_STATUS_CANCELED;
							LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting changePaymentInfo() to reset to 'Regular'(Check) - start...");
							updateBanConv.changePaymentInfo(amdocsCheckDetailsInfo);
							LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting changePaymentInfo() - end...");

						} else {
							amdocsCreditCardDetailsInfo.directDebitStatus = PaymentMethodInfo.DIRECT_DEBIT_STATUS_CANCELED;
							LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting changePaymentInfo() to reset to 'Regular'(CC) - start...");
							updateBanConv.changePaymentInfo(amdocsCreditCardDetailsInfo);
							LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting changePaymentInfo() - end...");
						}
			        }
			        // new method is 'Credit Card'
			        // - change credit card info if it has changed
			        // - change status to 'Active' and update credit card info if old one
					// was not 'Credit Card'
			        if (newPaymentMethod.equals(PaymentMethod.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD) &&
			            (!newPaymentMethod.equals(oldPaymentMethod)
			             ||
			             !updateBanConv.getPaymentInfo().creditCardDetailsInfo.
			             creditCardNo.equals(amdocsCreditCardDetailsInfo.creditCardNo)
			             ||
			             !AttributeTranslator.stringFromDate(updateBanConv.getPaymentInfo().creditCardDetailsInfo.
			             creditCardExpDate,"yyyyMM").equals(AttributeTranslator.stringFromDate(amdocsCreditCardDetailsInfo.creditCardExpDate,"yyyyMM"))
			             ||
			             !updateBanConv.getPaymentInfo().creditCardDetailsInfo.
			             creditCardType.
			             equals(amdocsCreditCardDetailsInfo.creditCardType)
			             ||
			             !updateBanConv.getPaymentInfo().creditCardDetailsInfo.
			             directDebitStatus.
			             equals(amdocsCreditCardDetailsInfo.directDebitStatus))) {

			          if (!newPaymentMethod.equals(oldPaymentMethod))
			            amdocsCreditCardDetailsInfo.directDebitStatus = PaymentMethodInfo.DIRECT_DEBIT_STATUS_ACTIVE;

			          LOGGER.debug("("+getClass().getName()+"."+ methodName+") calling changePaymentInfo() for CC...");
			          updateBanConv.changePaymentInfo(amdocsCreditCardDetailsInfo);
			        }
			        // new method is 'Direct Debit'
			        // - change bank info if it has changed
			        // - change status to 'Active' and update bank info if old one was not
					// 'Direct Debit'
			        if (newPaymentMethod.equals(PaymentMethod.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT) &&
			            (!newPaymentMethod.equals(oldPaymentMethod)
			             ||
			             !updateBanConv.getPaymentInfo().checkDetailsInfo.
			             bankAccountNo.equals(amdocsCheckDetailsInfo.bankAccountNo)
			             ||
			             !updateBanConv.getPaymentInfo().checkDetailsInfo.bankBranchNumber.
			             equals(amdocsCheckDetailsInfo.bankBranchNumber)
			             ||
			             !updateBanConv.getPaymentInfo().checkDetailsInfo.bankCode.
			             equals(amdocsCheckDetailsInfo.bankCode)
			             ||
			             updateBanConv.getPaymentInfo().checkDetailsInfo.bankAccountType !=
			             amdocsCheckDetailsInfo.bankAccountType
			             ||
			             !updateBanConv.getPaymentInfo().checkDetailsInfo.directDebitStatus.
			             equals(amdocsCheckDetailsInfo.directDebitStatus))) {

			          if (!newPaymentMethod.equals(oldPaymentMethod))
			            amdocsCreditCardDetailsInfo.directDebitStatus = PaymentMethodInfo.DIRECT_DEBIT_STATUS_ACTIVE;

			          LOGGER.debug("("+getClass().getName()+"."+ methodName+") calling changePaymentInfo() for Debit...");
			          updateBanConv.changePaymentInfo(amdocsCheckDetailsInfo);
			        }
			      }

			      // Update Bill Cycle (if changed)
			      if (!blockDirectUpdate && accountInfo.getBillCycle() != 0 && accountInfo.getBillCycle() != updateBanConv.getCycleInfo().billCycle) {

			    	LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting changeBillCycle() - start...");
			        updateBanConv.changeCycleInfo(amdocsCycleInfo);
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting changeBillCycle() - end...");
			      }

			      // Update Hot Line Indicator (if changed)
			      if ((accountInfo.getFinancialHistory().isHotlined() && updateBanConv.getBanHeaderInfo().hotlineInd != 'Y') ||
			         (!accountInfo.getFinancialHistory().isHotlined() && updateBanConv.getBanHeaderInfo().hotlineInd == 'Y')){

			    	LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting hotLineBan() - start...");
			        updateBanConv.hotLineBan(accountInfo.getFinancialHistory().isHotlined());
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting hotLineBan() - end...");
			      }
			      
			      if ( accountInfo.isNoOfInvoiceChanged() ) {
						BillParamsInfo billParamsInfo = new BillParamsInfo();
						billParamsInfo.bpNoOfCopies = (short) accountInfo.getBillParamsInfo().getNoOfInvoice();
						billParamsInfo.bpFormat = accountInfo.getBillParamsInfo().getBillFormat();
						billParamsInfo.bpMedia = accountInfo.getBillParamsInfo().getMediaCategory();
						updateBanConv.changeBillParamsInfo(billParamsInfo );
			      }

			      LOGGER.debug("("+getClass().getName()+"."+ methodName+") Leaving...");
				
				return null;   
			}
		});

	}

	private boolean compare(CPUIInfo[] pArray1, CPUIInfo[] pArray2) {


		boolean difValue = false;
		if (pArray1.length != pArray2.length) {
			difValue = true;
		} else {
			Arrays.sort(pArray1, new CPUIComparator());
			Arrays.sort(pArray2, new CPUIComparator());

			for (int i = 0; i < pArray1.length; i++) {
				if (AttributeTranslator.compare(pArray1[i].cpuiCd, pArray2[i].cpuiCd) != 0)
					difValue = true;
			}
		}
		return difValue;
	}
	
	public class CPUIComparator implements Comparator<Object> {

		public CPUIComparator() {}

		@SuppressWarnings("unused")
		public int compare(Object o1, Object o2) {
			CPUIInfo cpui1 = (CPUIInfo) o1;
			CPUIInfo cpui2 = (CPUIInfo) o2;

			if ( (cpui1.cpuiCd).equals(cpui2.cpuiCd )){ 
				return 0;
			}else if (cpui1 == null){ 
				return -1;
			}else if (cpui2 == null){ 
				return 1;
			}else{ 
				return ((cpui1.cpuiCd).compareToIgnoreCase(cpui2.cpuiCd));
			}
		}

		public boolean equals(Object o) {
			return this == o;
		}
	}

	@Override
	public void suspendAccountForPortOut(final int ban, final String activityReasonCode,
			final Date activityDate, final String portOutInd, String sessionId)
			throws ApplicationException {

		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				ActivityInfo activityInfo = new ActivityInfo();
				
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);	
				updateBanConv.setBanPK(ban);
				
				 // Populate ActivityInfo
		          activityInfo.activityDate = activityDate == null ? new Date() : activityDate;
		          activityInfo.activityReason = activityReasonCode;
		          activityInfo.userText = "";
		          activityInfo.isPortActivity = portOutInd.equals("Y") ? true : false;

		          // store
				updateBanConv.suspendBan(activityInfo);;

				return null;
			}

		});
		
	}

	@Override
	public void updatePersonalCreditInformation(final int ban,
			final PersonalCreditInfo personalCreditInfo, String sessionId)
			throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {

				String methodName="updatePersonalCreditInformation";
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") Incoming PersonalCreditInfo Object for BAN["+ban+"]..."+personalCreditInfo);
				
				boolean isIdentificationChanged = false;
				boolean isCreditCardChanged = false;
				IdentificationInfo newIdentificationInfo = new IdentificationInfo();
				CreditCardInfo newCreditCardInfo = new CreditCardInfo();
				IdentificationInfo amdocsIdentificationInfo = null;
				CreditCardInfo amdocsCreditCardInfo = null;
				
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);	
				updateBanConv.setBanPK(ban);				
				
				newIdentificationInfo = (IdentificationInfo)AmdocsTelusMapping.mapTelusToAmdocs(personalCreditInfo, newIdentificationInfo);
				newCreditCardInfo = (CreditCardInfo)AmdocsTelusMapping.mapTelusToAmdocs(personalCreditInfo, newCreditCardInfo);
				amdocsIdentificationInfo = updateBanConv.getIdentificationInfo();
				amdocsCreditCardInfo = updateBanConv.getCreditCardInfo();
				
				/*using amdocsIdentificationInfo object instead of newIdentificationInfo to retain the 
				values of fields that are not in schema but in amdocs object. But for creditcard changes we still use 
				newCreditCardInfo as there are no field values to be retained. - Inba.*/
				
				if(AttributeTranslator.compare(newIdentificationInfo.birthDate,amdocsIdentificationInfo.birthDate)!= 0  ){
					amdocsIdentificationInfo.birthDate = newIdentificationInfo.birthDate;
					isIdentificationChanged = true;
				}
				
				if(AttributeTranslator.compare(newIdentificationInfo.customerSsn,amdocsIdentificationInfo.customerSsn)!= 0  ){
					amdocsIdentificationInfo.customerSsn = newIdentificationInfo.customerSsn;
					isIdentificationChanged = true;
				}
				
				if(AttributeTranslator.compare(newIdentificationInfo.drivrLicnsExpDt,amdocsIdentificationInfo.drivrLicnsExpDt)!= 0 ){
					amdocsIdentificationInfo.drivrLicnsExpDt = newIdentificationInfo.drivrLicnsExpDt;
					isIdentificationChanged = true;
				}
				
				if(AttributeTranslator.compare(newIdentificationInfo.drivrLicnsNo,amdocsIdentificationInfo.drivrLicnsNo)!= 0){
					amdocsIdentificationInfo.drivrLicnsNo = newIdentificationInfo.drivrLicnsNo;
					isIdentificationChanged = true;
				}
				
				if(AttributeTranslator.compare(newIdentificationInfo.drivrLicnsProvince,amdocsIdentificationInfo.drivrLicnsProvince)!= 0 ){
					amdocsIdentificationInfo.drivrLicnsProvince = newIdentificationInfo.drivrLicnsProvince;
					isIdentificationChanged = true;
				}
				
				if(AttributeTranslator.compare(newCreditCardInfo.ccFirstSixDigits,amdocsCreditCardInfo.ccFirstSixDigits)!= 0 ||
						AttributeTranslator.compare(newCreditCardInfo.ccLastFourDigits,amdocsCreditCardInfo.ccLastFourDigits)!= 0 ||		
						AttributeTranslator.compare(AttributeTranslator.stringFromDate(personalCreditInfo.getCreditCard0().getExpiryDate(),"yyyyMMdd"),amdocsCreditCardInfo.creditCardExpDt)!= 0 ||
						AttributeTranslator.compare(newCreditCardInfo.creditCardNum,amdocsCreditCardInfo.creditCardNum)!= 0 ){
					isCreditCardChanged = true;
				}
				
				// save
				if(isIdentificationChanged || isCreditCardChanged){
					if(isCreditCardChanged){
						LOGGER.debug("("+getClass().getName()+"."+ methodName+") ...has Credit Card changes");
						updateBanConv.setCreditCardInfo(newCreditCardInfo);
					}
					if(isIdentificationChanged){
						LOGGER.debug("("+getClass().getName()+"."+ methodName+") ...has Identification changes");
						updateBanConv.setIdentificationInfo(resetDOB(amdocsIdentificationInfo));
					}
					updateBanConv.saveBan();
					LOGGER.debug("("+getClass().getName()+"."+ methodName+") The changes are updated");
				}else{
					LOGGER.debug("("+getClass().getName()+"."+ methodName+") There are no new change to be updated");
				}
				return null;
			}

		});
		
	}

	@Override
	public void updateBusinessCreditInformation(final int ban,
			final BusinessCreditInfo businessCreditInfo, String sessionId)
			throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {
				
				String methodName="updateBusinessCreditInformation";
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") Incoming BusinessCreditInfo Object for BAN["+ban+"]..."+businessCreditInfo);

				boolean isChanged = false;
				BusinessInfo newBusinessInfo = new BusinessInfo();
				BusinessInfo amdocsBusinessInfo = null;
				
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);	
				updateBanConv.setBanPK(ban);
				
				newBusinessInfo = (BusinessInfo)AmdocsTelusMapping.mapTelusToAmdocs(businessCreditInfo, newBusinessInfo);
				amdocsBusinessInfo = updateBanConv.getBusinessInfo();
				
				/*using amdocsBusinessInfo object instead of newBusinessInfo to retain the 
				values of fields that are not in schema but in amdocs object.  - Inba.*/
				
				if(AttributeTranslator.compare(newBusinessInfo.incorporationDate,amdocsBusinessInfo.incorporationDate)!= 0) {
					amdocsBusinessInfo.incorporationDate = newBusinessInfo.incorporationDate;
					isChanged = true;
				}
				
				if(AttributeTranslator.compare(newBusinessInfo.incorporationNo,amdocsBusinessInfo.incorporationNo)!= 0 ){
					amdocsBusinessInfo.incorporationNo = newBusinessInfo.incorporationNo;
					isChanged = true;
				}
				
				// store 
				if(isChanged){
	        	  	updateBanConv.setBusinessInfo(amdocsBusinessInfo);
					updateBanConv.saveBan();
					LOGGER.debug("("+getClass().getName()+"."+ methodName+") The changes are updated");
				}else{
					LOGGER.debug("("+getClass().getName()+"."+ methodName+") There are no new change to be updated");
				}
				return null;
			}

		});
		
	}

	@Override
	public List<CustomerNotificationPreferenceInfo> getCustomerNotificationPreferenceList(final int ban, 
			String sessionId) 
			throws ApplicationException {
		
		String methodName="getCustomerNotificationPreferenceList";
		List<CustomerNotificationPreferenceInfo> result = new ArrayList<CustomerNotificationPreferenceInfo>();
		
		CPUIInfo[] amdocsCPUIInfo = getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<CPUIInfo[]>() {
			@Override
			public CPUIInfo[] doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				String methodName="getCustomerNotificationPreferenceList";
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") Incoming BAN["+ban+"]");

				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(ban);
				CPUIInfo[] amdocsCPUIInfo = updateBanConv.getBanCPUIInfo();
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") CPUI information retrieved for BAN " + ban);

				return amdocsCPUIInfo;
			}
		});

		if (amdocsCPUIInfo !=null && amdocsCPUIInfo.length>0) {
			for (int i=0;i< amdocsCPUIInfo.length;i++) {
				CustomerNotificationPreferenceInfo customerNotificationPreferenceInfo = new CustomerNotificationPreferenceInfo();
				customerNotificationPreferenceInfo.setCode(amdocsCPUIInfo[i].cpuiCd);
				result.add(customerNotificationPreferenceInfo);
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") Adding CustomerNotificationPreferenceInfo" 
						+ " Code:" + customerNotificationPreferenceInfo.getCode());
			}
		} else {
			LOGGER.info("("+getClass().getName()+"."+ methodName+") amdocsCPUIInfo are null or empty for " + ban);
		}
		
		return result;
	}

	@Override
	public void updateCustomerNotificationPreferenceList(final int ban, 
			final List<CustomerNotificationPreferenceInfo> notificationPreferenceList,
			String sessionId) 
			throws ApplicationException {

		String methodName="updateCustomerNotificationPreferenceList";
		if (notificationPreferenceList != null && notificationPreferenceList.size()>0) {
			final CPUIInfo[] cpuiInfos = new CPUIInfo[notificationPreferenceList.size()];
			for (int i=0; i<notificationPreferenceList.size(); i++) {
				CustomerNotificationPreferenceInfo customerNotificationPreferenceInfo = notificationPreferenceList.get(i);
				CPUIInfo cpuiInfo = new CPUIInfo();
				if (customerNotificationPreferenceInfo != null && customerNotificationPreferenceInfo.getUpdateModeCode() != null) {
					cpuiInfo.cpuiCd = customerNotificationPreferenceInfo.getCode().trim();
					byte mode = 0;
					if (customerNotificationPreferenceInfo.getUpdateModeCode().trim().equalsIgnoreCase("D")) {
						mode = DELETE;
					} else if (customerNotificationPreferenceInfo.getUpdateModeCode().trim().equalsIgnoreCase("I")) {
						mode = INSERT;
					} else {
						LOGGER.debug("("+getClass().getName()+"."+ methodName+") customerNotificationPreferenceInfo are null or empty");
					}
					if (mode != 0) 
						cpuiInfo.cpuiMode = mode;
				}
				cpuiInfos[i] = cpuiInfo;
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") Adding CPUI with code:" + cpuiInfo.cpuiCd + " mode:" + cpuiInfo.cpuiMode);
			}
			
			if (cpuiInfos != null && cpuiInfos.length > 0) {
				getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
					@Override
					public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
						String methodName="updateCustomerNotificationPreferenceList";
						LOGGER.debug("("+getClass().getName()+"."+ methodName+") Incoming BAN["+ban+"]");
						
						UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
						updateBanConv.setBanPK(ban);
						updateBanConv.setBanCPUIInfo(cpuiInfos, "");
						LOGGER.debug("("+getClass().getName()+"."+ methodName+") CPUI updated for BAN " + ban);
						return null;
					}
				});
			} else {
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") cpuiInfos is empty for BAN " + ban);
			}
			
		} else {
			LOGGER.info("("+getClass().getName()+"."+ methodName+") notificationPreferenceList is empty for BAN " + ban);
		}
	}

	@Override
	public void updateHotlineInd(final int ban, final boolean hotLineInd, String sessionId)
			throws ApplicationException {
		
			final String methodName = "updateHotlineInd"; 
			getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
				
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)throws Exception {

				
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);				
				updateBanConv.setBanPK(ban);

				// Update Hot Line Indicator (if changed)
				if ((hotLineInd && updateBanConv.getBanHeaderInfo().hotlineInd != 'Y') ||
			         (!hotLineInd && updateBanConv.getBanHeaderInfo().hotlineInd == 'Y')){

			    	LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting hotLineBan() - start...");
			        updateBanConv.hotLineBan(hotLineInd);
			        LOGGER.debug("("+getClass().getName()+"."+ methodName+") Excecuting hotLineBan() - end...");
			        updateBanConv.saveBan();
			    }

				
				return null;   
			}
		});

		
		
	}

	// Workaround solution for defect 30419 as DOB issue found in createAccount and updateAccount
	// Long term solution is assigned to KB as new defect 31778. 
	private IdentificationInfo resetDOB(IdentificationInfo amdocsIdentificationInfo) {
		if (amdocsIdentificationInfo.birthDate != null) {
			GregorianCalendar c1 = new GregorianCalendar();
			c1.setTimeInMillis(amdocsIdentificationInfo.birthDate.getTime());
			GregorianCalendar c2 = new GregorianCalendar(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DATE), 6, 0, 0);
			amdocsIdentificationInfo.birthDate = c2.getTime();
		}
		return amdocsIdentificationInfo;
	}

}
