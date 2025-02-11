/*
 *  Copyright (c) 2010 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebService;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingType;
import javax.xml.ws.Holder;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.api.ApplicationException;
import com.telus.api.ClientAPI;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.AccountMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.AccountMemoMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.BusinessCreditInformationMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.ContactPropertyMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.CustomerNotificationPreferenceMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.CustomerNotificationPreferenceUpdateMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.FollowUpRequestMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.PersonalCreditInformationMapper;
import com.telus.cmb.jws.mapping.account_information_20.BillingPropertyMapper;
import com.telus.cmb.jws.mapping.account_information_20.InvoicePropertiesMapper;
import com.telus.cmb.jws.mapping.billing_inquiry.AuditHeaderMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.CreditCardMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.PaymentMethodAuthorizationTypeMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.PaymentMethodMapper;
import com.telus.cmb.jws.mapping.enterprisecommontypes_v8.AuditInfoMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ContactDetailInfo;
import com.telus.eas.account.info.ContactPropertyInfo;
import com.telus.eas.account.info.CreditCardHolderInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.CustomerNotificationPreferenceInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.eligibility.info.InternationalServiceEligibilityCheckResultInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.cmb.jws.CreditCardValidationType;
import com.telus.cmb.jws.CustomerNotificationPreferenceType;
import com.telus.cmb.jws.CustomerNotificationPreferenceUpdateType;
import com.telus.cmb.jws.FollowUpRequest;
import com.telus.cmb.jws.RegisterTopUpCreditCard;
import com.telus.cmb.jws.RegisterTopUpCreditCardResponse;
import com.telus.cmb.jws.UpdatePaymentMethod;
import com.telus.cmb.jws.UpdatePaymentMethodResponse;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.Account;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.AccountMemo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.BillingPropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.ContactPropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.InternationalServiceEligibilityCheckResult;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.InvoicePropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BusinessCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CreditCard;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Memo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PaymentMethod;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.ResponseMessage;


/**
 * @author Sam Ye 
 *
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(portName = "AccountInformationServicePort", serviceName = "AccountInformationService_v3_3", 
		targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/AccountInformationService_3", 
		wsdlLocation = "/wsdls/AccountInformationService_v3_3.wsdl", 
		endpointInterface = "com.telus.cmb.jws.AccountInformationServicePort")
		@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class AccountInformationService_33 extends BaseService implements AccountInformationServicePort {
	private static final int RETURN_MAXIMUM = 1000;
	private static final CreditCardMapper ccMapper = new CreditCardMapper();

	public AccountInformationService_33() {
		super( new AccountInformationExceptionTranslator());
	}



	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0001", errorMessage="Account retrieval error")
	public Account getAccountByAccountNumber(final String ban) throws PolicyException, ServiceException {

		return execute( new ServiceInvocationCallback<Account>() {

			@Override
			public Account doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				int banNumber = Integer.valueOf(ban);
				AccountInfo	accountInfo = getAccountInformationHelper(context).retrieveAccountByBan(banNumber);
				String sessionId = null;
				try {
					sessionId = context.getAccountLifeCycleManagerSessionId();
				} catch (Throwable t) {
					getLogger().debug("Fail to get sessionId for ban: " + ban + ". Exception message: " + t.getMessage());
				}
				if (sessionId != null) {
					CreditCheckResultInfo creditCheckResultInfo = getAccountLifecycleManager(context).retrieveAmdocsCreditCheckResultByBan(banNumber, sessionId);
					accountInfo.getCreditCheckResult0().setDeposits(creditCheckResultInfo.getDeposits());
				}
				accountInfo.setProductSubscriberLists(getAccountInformationHelper(context).retrieveProductSubscriberLists(banNumber));
				return getAccountMapper().mapToSchema(accountInfo);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0002", errorMessage="Account retrieval error")
	public Account getAccountByPhoneNumber(final String phoneNumber) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Account>() {

			@Override
			public Account doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				AccountInfo	accountInfo = getAccountInformationHelper(context).retrieveAccountByPhoneNumber(phoneNumber);
				accountInfo.setProductSubscriberLists(getAccountInformationHelper(context).retrieveProductSubscriberLists(accountInfo.getBanId()));
				return getAccountMapper().mapToSchema(accountInfo);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_AIS_0003", errorMessage = "Account retrieval error")
	public List<Account> getAccountListByAccountNumbers(final List<String> banNumbers) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Account>>() {

			@Override
			public List<Account> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				List<Account> accountList = new ArrayList<Account>();
				if (banNumbers != null && banNumbers.size() > 0) {
					for (int i = 0; i < (banNumbers.size() <= RETURN_MAXIMUM ? banNumbers.size() : RETURN_MAXIMUM); i++) {
						int ban = Integer.valueOf(banNumbers.get(i));

						try {
							AccountInfo accountInfo = getAccountInformationHelper(context).retrieveAccountByBan(ban);
							accountInfo.setProductSubscriberLists(getAccountInformationHelper(context).retrieveProductSubscriberLists(ban));
							Account account = getAccountMapper().mapToSchema(accountInfo);
							accountList.add(account);
						} catch (TelusException e) {
							if (!e.id.equals("APP10004")) { // Ban not found error will not be thrown
								throw e;
							}
						} catch (ApplicationException e) {
							if (!e.getErrorCode().equals("APP10004")) {// Ban not found error will not be thrown
								throw e;
							}
						}
					}
				}
				return accountList.size() == 0 ? null : accountList;
			}
		});
	}

	//	@Override
	//	@ServiceBusinessOperation(errorCode="CMB_AIS_0004", errorMessage="Account password update error")
	//	public void updateAccountPassword(final String ban, final String newPassword) throws PolicyException, ServiceException {
	//		execute(new ServiceInvocationCallback <Object>() {
	//
	//			@Override
	//			public Object doInInvocationCallback(ServiceInvocationContext context) throws Exception {
	//		        int banNumber = Integer.valueOf(ban);
	//		        if (banNumber != 0) {
	//						getAccountLifecycleManager(context).updateAccountPassword(banNumber, newPassword, context.getAccountLifeCycleManagerSessionId());
	//				}
	//				return null;
	//			}
	//			
	//		});
	//	}


	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0006", errorMessage="Get Email Address error")
	public String getEmailAddress(final String ban) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<String>() {

			@Override
			public String doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				int accountNumber = Integer.valueOf(ban);
				ContactDetailInfo contactDetailInfo = getAccountInformationHelper(context).getCustomerContactInfo(accountNumber);
				if (contactDetailInfo == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_INVALID_BAN, ServiceErrorCodes.ERROR_DESC_NO_DATA_FOR_BAN);
				}

				return contactDetailInfo.getEmailAddress();
			}

		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0007", errorMessage="Email Address Update error")
	public void updateEmailAddress(final String ban, final String emailAddress,Boolean notificationSuppressionInd, AuditInfo auditInfo)
	throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				int accountNumber = Integer.valueOf(ban);
				getAccountLifecycleManager(context).updateEmailAddress(accountNumber, emailAddress, context.getAccountLifeCycleManagerSessionId());
				return null;
			}
		});

	}


	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0008", errorMessage="Get Invoice Properties Error")
	public InvoicePropertyListType getInvoicePropertyList(final String ban)
	throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<InvoicePropertyListType>() {

			@Override
			public InvoicePropertyListType doInInvocationCallback(
					ServiceInvocationContext context) throws Throwable {
				InvoicePropertiesInfo	invoiceProperties = getAccountInformationHelper(context).getInvoiceProperties(Integer.valueOf(ban));

				if (invoiceProperties == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_INVALID_BAN, ServiceErrorCodes.ERROR_DESC_NO_DATA_FOR_BAN);
				}
				return new InvoicePropertiesMapper().mapToSchema(invoiceProperties);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0009", errorMessage="Invoice Properties Update Error")
	public void updateInvoicePropertyList(final String billingAccountNumber, final InvoicePropertyListType invoicePropertyList,Boolean notificationSuppressionInd, AuditInfo auditInfo)
	throws PolicyException, ServiceException {

		execute(new ServiceInvocationCallback<Object>() {

			@Override
			public Object doInInvocationCallback(
					ServiceInvocationContext context) throws Throwable {
				InvoicePropertiesInfo invProperties = new InvoicePropertiesMapper().mapToDomain(invoicePropertyList);
				getAccountLifecycleManager(context).updateInvoiceProperties(Integer.valueOf(billingAccountNumber), invProperties, context.getAccountLifeCycleManagerSessionId());
				return null;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0010", errorMessage="Get Account List By IMSI Error")
	public List<com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.Account> getAccountListByIMSI(final String imsi) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.Account>>() {

			@Override
			public List<Account> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				AccountInfo accountInfo = getAccountInformationHelper(context).retrieveAccountByImsi(imsi);
				List<Account> accountList = new ArrayList<Account>();
				if (accountInfo != null) {
					accountList.add(getAccountMapper().mapToSchema(accountInfo));
				}
				return accountList;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0011", errorMessage="Get Billing Information Error")
	public BillingPropertyListType getBillingInformation(final String billingAccountNumber) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<BillingPropertyListType>() {

			@Override
			public BillingPropertyListType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				BillingPropertyInfo billingProperty = getAccountInformationHelper(context).retrieveBillingInformation(Integer.valueOf(billingAccountNumber));

				return BillingPropertyMapper.getInstance().mapToSchema(billingProperty);
			}

		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0012", errorMessage="Update Contact Information Error")
	public void updateContactInformation(final String billingAccountNumber, final ContactPropertyListType contactPropertyList,Boolean notificationSuppressionInd, AuditInfo auditInfo) throws PolicyException, ServiceException {
		execute (new ServiceInvocationCallback<Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				ContactPropertyInfo contactProperty = getContactPropertyMapper().mapToDomain(contactPropertyList);
				getAccountLifecycleManager(context).updateContactInformation(Integer.valueOf(billingAccountNumber), contactProperty, context.getAccountLifeCycleManagerSessionId());
				return null;
			}
		});

	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0013", errorMessage="Get Contact Information Error")
	public ContactPropertyListType getContactInformation(final String billingAccountNumber) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<ContactPropertyListType>() {

			@Override
			public ContactPropertyListType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				ContactPropertyInfo contactProperty = getAccountInformationHelper(context).retrieveContactInformation(Integer.valueOf(billingAccountNumber));
				return getContactPropertyMapper().mapToSchema(contactProperty);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0014", errorMessage="Get BillCycle PropertyList error")
	public void getBillCyclePropertyList(Holder<String> billingAccountNumber) throws PolicyException, ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0015", errorMessage="Get Last Memo error")
	public Memo getLastMemo(final String billingAccountNumber, final String memoType) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Memo>() {

			@Override
			public Memo doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				MemoInfo info = getAccountInformationHelper(context).retrieveLastMemo(Integer.parseInt(billingAccountNumber), memoType);
				return getAccountMemoMapper().mapToSchema(info);
			}
		} );
	}
	  
	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.AccountInformationServicePortType#createMemo(java.lang.String, com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.AccountMemo)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0016", errorMessage="Create Memo error")
	public void createMemo(final String billingAccountNumber, final AccountMemo memo) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback<Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				MemoInfo memoInfo = getAccountMemoMapper().mapToDomain(memo);
				memoInfo.setBanId(Integer.parseInt(billingAccountNumber));
				if ( Boolean.TRUE.equals( memo.isAsyncInd()) ) {
					getAccountLifecycleFacade(context).asyncCreateMemo(memoInfo, context.getAccountLifeCycleFacadeSessionId() );
				} else {
					getAccountLifecycleManager(context).createMemo(memoInfo, context.getAccountLifeCycleManagerSessionId());
				}
				return null;
			}
		});
	}

	private AccountMemoMapper getAccountMemoMapper() {
		return AccountInformationServiceMapper.AccountMemoMapper();
	}
	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0017", errorMessage="Check International Service Eligibility error")
	public InternationalServiceEligibilityCheckResult checkInternationalServiceEligibility(final String billingAccountNumber) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<InternationalServiceEligibilityCheckResult>() {

			@Override
			public InternationalServiceEligibilityCheckResult doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				InternationalServiceEligibilityCheckResultInfo internationalServiceEligibilityCheckResultInfo = getAccountInformationHelper(context).checkInternationalServiceEligibility(Integer.valueOf(billingAccountNumber));
				InternationalServiceEligibilityCheckResult response = new InternationalServiceEligibilityCheckResult();
				response.setDepositAmount(internationalServiceEligibilityCheckResultInfo.getDepositAmount());
				response.setEligibleForInternationalDialingInd(internationalServiceEligibilityCheckResultInfo.isEligibleForInternationalDialing());
				response.setEligibleForInternationalRoamingInd(internationalServiceEligibilityCheckResultInfo.isEligibleForInternationalRoaming());
				return response;
			}
		});
	}

	private AccountMapper getAccountMapper() {
		return AccountInformationServiceMapper.AccountMapper();
	}

	private ContactPropertyMapper getContactPropertyMapper() {
		return AccountInformationServiceMapper.ContactPropertyMapper();
	}

	private PersonalCreditInformationMapper getPersonalCreditMapper() {
		return AccountInformationServiceMapper.PersonalCreditMapper();
	}

	private BusinessCreditInformationMapper getBusinessCreditMapper() {
		return AccountInformationServiceMapper.BusinessCreditMapper();
	}

	private FollowUpRequestMapper getFollowUpRequestMapper() {
		return AccountInformationServiceMapper.FollowUpRequestMapper();
	}

	private CustomerNotificationPreferenceMapper getCustomerNotificationPreferenceMapper() {
		return AccountInformationServiceMapper.CustomerNotificationPreferenceMapper();
	}

	private CustomerNotificationPreferenceUpdateMapper getCustomerNotificationPreferenceUpdateMapper() {
		return AccountInformationServiceMapper.CustomerNotificationPreferenceUpdateMapper();
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0018", errorMessage="Get Personal Credit Information Error")
	public PersonalCreditInformation getPersonalCreditInformation(
			final String billingAccountNumber) throws PolicyException,
			ServiceException {	
		return execute(new ServiceInvocationCallback<PersonalCreditInformation>() {
			@Override
			public PersonalCreditInformation doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				PersonalCreditInfo personalCreditInfo= getAccountInformationHelper(context).retrievePersonalCreditInformation(Integer.parseInt(billingAccountNumber));
				return getPersonalCreditMapper().mapToSchema(personalCreditInfo);
			}
		} );
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0019", errorMessage="Get Business Credit Information Error")
	public BusinessCreditInformation getBusinessCreditInformation(
			final String billingAccountNumber) throws PolicyException,
			ServiceException {
		return execute(new ServiceInvocationCallback<BusinessCreditInformation>() {
			@Override
			public BusinessCreditInformation doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				BusinessCreditInfo businessCreditInfo= getAccountInformationHelper(context).retrieveBusinessCreditInformation(Integer.parseInt(billingAccountNumber));
				return getBusinessCreditMapper().mapToSchema(businessCreditInfo);
			}
		} );
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0020", errorMessage="Update Payment Method error")
	public UpdatePaymentMethodResponse updatePaymentMethod(
			final UpdatePaymentMethod parameters,
			final OriginatingUserType updatePaymentMethodInSoapHdr)
	throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback <UpdatePaymentMethodResponse>() {

			@Override
			public UpdatePaymentMethodResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				String billingAccountNumber=parameters.getBillingAccountNumber();
				PaymentMethod paymentMethod = parameters.getPaymentMethod();
				CreditCardValidationType creditCardValidationType = parameters.getPaymentMethodValidation();
				CreditCardResponseInfo creditCardResponseInfo=new CreditCardResponseInfo();

				if (creditCardValidationType.isValidateCreditCardInd() && updatePaymentMethodInSoapHdr == null) {
					throw new ServicePolicyException (ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
				}

				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(paymentMethod);
				com.telus.eas.transaction.info.AuditInfo auditInfo = null;
				if (parameters.getAuditInfo() != null) {
					auditInfo = new AuditInfoMapper().mapToDomain(parameters.getAuditInfo());
				} 		
				
				 boolean notificationSuppressionInd =parameters.isNotificationSuppressionInd()== null ? false :parameters.isNotificationSuppressionInd();

				if (creditCardValidationType.isValidateCreditCardInd())
				{
					CreditCardTransactionInfo creditCardTransactionInfo = createCreditCardTransactionInfo(updatePaymentMethodInSoapHdr, paymentMethodInfo.getCreditCard0(), creditCardValidationType.getBusinessRole().toString());
					creditCardResponseInfo = getAccountLifecycleFacade(context).validateCreditCard(Integer.parseInt(billingAccountNumber), creditCardTransactionInfo, context.getAccountLifeCycleFacadeSessionId());
				}	
               /*Note:This portion has been commented as we will miss sending the notification mail, for removal transaction case.There will be sustainment task for the same.
				if (paymentMethodInfo.getPaymentMethod().equals(PaymentMethodInfo.PAYMENT_METHOD_REGULAR)) {
					getAccountLifecycleManager(context).changePaymentMethodToRegular(Integer.parseInt(billingAccountNumber), context.getAccountLifeCycleManagerSessionId());
				}else{
					getAccountLifecycleFacade(context).updatePaymentMethod(Integer.parseInt(billingAccountNumber), null, paymentMethodInfo, false, null, context.getAccountLifeCycleFacadeSessionId());
				}	
				*/ 
					getAccountLifecycleFacade(context).updatePaymentMethod(Integer.parseInt(billingAccountNumber), null,paymentMethodInfo, notificationSuppressionInd ,auditInfo,context.getAccountLifeCycleFacadeSessionId());
				
				UpdatePaymentMethodResponse response = new UpdatePaymentMethodResponse();
				response.setAuthorization(new PaymentMethodAuthorizationTypeMapper().mapToSchema(creditCardResponseInfo));
				return response;
			}
		});
	}



	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0021", errorMessage="Register TopUp CreditCard error")
	public RegisterTopUpCreditCardResponse registerTopUpCreditCard(
			final RegisterTopUpCreditCard parameters,
			final OriginatingUserType registerTopUpCreditCardInSoapHdr)
	throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback <RegisterTopUpCreditCardResponse>() {

			@Override
			public RegisterTopUpCreditCardResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				String billingAccountNumber=parameters.getBillingAccountNumber();
				CreditCard creditCard = parameters.getCreditCard();
				CreditCardValidationType creditCardValidationType = parameters.getCreditCardValidation();
				CreditCardInfo creditCardInfo = ccMapper.mapToDomain(creditCard);
				CreditCardResponseInfo creditCardResponseInfo=new CreditCardResponseInfo();

				if (creditCardValidationType.isValidateCreditCardInd() && registerTopUpCreditCardInSoapHdr == null) {
					throw new ServicePolicyException (ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
				}

				if (creditCardValidationType.isValidateCreditCardInd())
				{
					CreditCardTransactionInfo creditCardTransactionInfo = createCreditCardTransactionInfo(registerTopUpCreditCardInSoapHdr, creditCardInfo, creditCardValidationType.getBusinessRole().toString());		
					creditCardResponseInfo = getAccountLifecycleFacade(context).validateCreditCard(Integer.parseInt(billingAccountNumber), creditCardTransactionInfo, context.getAccountLifeCycleFacadeSessionId());
				}

				if (creditCardInfo == null || creditCardInfo.hasToken() == false) {
					throw new ServicePolicyException (ServiceErrorCodes.ERROR_MISSING_CREDIT_CARD_TOKEN, ServiceErrorCodes.ERROR_DESC_CC_TOKEN_MISSING);
				}
				PaymentMethodInfo paymentMethodInfo = new PaymentMethodInfo();
				paymentMethodInfo.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_REGULAR);
				paymentMethodInfo.setCreditCard0(creditCardInfo);
				paymentMethodInfo.setStatus(PaymentMethodInfo.DIRECT_DEBIT_STATUS_HOLD);
				paymentMethodInfo.setStatusReason(PaymentMethodInfo.DIRECT_DEBIT_REASON_CHANGE_BILL_DATA);

				getAccountLifecycleFacade(context).registerTopUpCreditCard(Integer.parseInt(billingAccountNumber), creditCardInfo, context.getAccountLifeCycleFacadeSessionId());

				RegisterTopUpCreditCardResponse response = new RegisterTopUpCreditCardResponse();
				response.setAuthorization(new PaymentMethodAuthorizationTypeMapper().mapToSchema(creditCardResponseInfo));

				return response;

			}
		});
	}



	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0022", errorMessage="Remove TopUp CreditCard error")
	public void removeTopUpCreditCard(String billingAccountNumber)
	throws PolicyException, ServiceException {
		// TODO Implementation at a later date
		throw new ServicePolicyException (ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is not yet implemented.");

	}



	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0023", errorMessage="Create FollowUp error")
	public void createFollowUp(final String billingAccountNumber,
			final FollowUpRequest followUp, final boolean asyncInd) throws PolicyException,
			ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				FollowUpInfo followUpInfo = getFollowUpRequestMapper().mapToDomain(followUp);
				followUpInfo.setBan(Integer.parseInt(billingAccountNumber));
				followUpInfo.setBanId(Integer.parseInt(billingAccountNumber));
				if(asyncInd){					
					getAccountLifecycleFacade(context).asyncCreateFollowUp(followUpInfo, context.getAccountLifeCycleFacadeSessionId());
				}else{
					getAccountLifecycleManager(context).createFollowUp(followUpInfo, context.getAccountLifeCycleManagerSessionId());
				}
				return null;
			}

		});


	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0024", errorMessage="Get Subscriber Eligibility Supporting Information error")
	public void getSubscriberEligibilitySupportingInfo(
			String billingAccountNumber, List<String> memoTypeList,
			Date dateFrom, Date dateTo, Holder<List<Memo>> memoList,
			Holder<String> totalPaymentAmount, Holder<String> totalDepositHeld)
	throws PolicyException, ServiceException {
		throw new ServicePolicyException (ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is not yet implemented.");

	}

	private CreditCardTransactionInfo createCreditCardTransactionInfo(OriginatingUserType updatePaymentMethodInSoapHdr ,CreditCardInfo creditCardInfo, String businessRole)
	{
		CreditCardTransactionInfo creditCardTransactionInfo = new CreditCardTransactionInfo();
		AuditHeaderMapper auditHeaderMapper = new AuditHeaderMapper();
		CreditCardHolderInfo creditCardHolderInfo = new CreditCardHolderInfo();

		AuditHeaderInfo auditHeaderInfo = auditHeaderMapper.mapToDomain(updatePaymentMethodInSoapHdr);
		auditHeaderInfo.appendAppInfo(this.getClass().getSimpleName(), ClientAPI.CMDB_ID, getHostIpAddress());

		creditCardTransactionInfo.setAuditHeader(auditHeaderInfo);
		creditCardTransactionInfo.setCreditCardInfo(creditCardInfo);

		creditCardHolderInfo.setBusinessRole(businessRole);
		creditCardTransactionInfo.setCreditCardHolderInfo(creditCardHolderInfo);

		return creditCardTransactionInfo;
	}

	
    
	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0025", errorMessage="Get Customer Notification Preference List error")
	public List<CustomerNotificationPreferenceType> getCustomerNotificationPreferenceList(
        final String billingAccountNumber)
        throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<CustomerNotificationPreferenceType>>() {

			@Override
			public List<CustomerNotificationPreferenceType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				List<CustomerNotificationPreferenceType> customerNotificationPreferences = new ArrayList<CustomerNotificationPreferenceType>();
				if (billingAccountNumber != null && billingAccountNumber.length() > 0) {
					int ban = Integer.valueOf(billingAccountNumber).intValue(); 
					List<CustomerNotificationPreferenceInfo> preferenceList = getAccountLifecycleManager(context).getCustomerNotificationPreferenceList(
							ban, context.getAccountLifeCycleManagerSessionId());
					for (CustomerNotificationPreferenceInfo customerNotificationPreferenceInfo:preferenceList) {
						customerNotificationPreferences.add(getCustomerNotificationPreferenceMapper().mapToSchema(customerNotificationPreferenceInfo));
					}
					
				}
				
				return customerNotificationPreferences;
			}
		});
		
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0026", errorMessage="Update Customer Notification Preference List error")
    public ResponseMessage updateCustomerNotificationPreferenceList(
            final String billingAccountNumber, 
            final List<CustomerNotificationPreferenceUpdateType> notificationPreferenceList)
            throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {

			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ResponseMessage result = new ResponseMessage();
				result.setDateTimeStamp(new Date());
				updateCustomerNotificationPreferenceList(billingAccountNumber, notificationPreferenceList, context);
				
				return result;
			}
		});
	
	}
	
	private void updateCustomerNotificationPreferenceList(String billingAccountNumber, 
			List<CustomerNotificationPreferenceUpdateType> notificationPreferenceList, 
			ServiceInvocationContext context) throws Throwable {
		List<CustomerNotificationPreferenceInfo> customerNotificationPreferenceInfos = new ArrayList<CustomerNotificationPreferenceInfo>();
		if (billingAccountNumber != null 
				&& billingAccountNumber.length() > 0 
				&& notificationPreferenceList != null 
				&& notificationPreferenceList.size() > 0) {
			
			for (CustomerNotificationPreferenceUpdateType notificationPreference:notificationPreferenceList) {
				customerNotificationPreferenceInfos.add(getCustomerNotificationPreferenceUpdateMapper().mapToDomain(notificationPreference));
			}
			getAccountLifecycleManager(context).updateCustomerNotificationPreferenceList(
					Integer.valueOf(billingAccountNumber).intValue(), 
					customerNotificationPreferenceInfos,
					context.getAccountLifeCycleManagerSessionId());
		}
	}

}
