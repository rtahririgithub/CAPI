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
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.Holder;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.api.ApplicationException;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.AccountMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.AccountMemoMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.BusinessCreditInformationMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.ContactPropertyMapper;
import com.telus.cmb.jws.mapper.AccountInformationServiceMapper.PersonalCreditInformationMapper;
import com.telus.cmb.jws.mapping.account_information_20.BillingPropertyMapper;
import com.telus.cmb.jws.mapping.account_information_20.InvoicePropertiesMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ContactDetailInfo;
import com.telus.eas.account.info.ContactPropertyInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.Account;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.AccountMemo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.BillingPropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.ContactPropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.InternationalServiceEligibilityCheckResult;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.InvoicePropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BusinessCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Memo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation;

/**
 * @author Tsz Chung Tong
 *
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)

@WebService(
		portName = "AccountInformationServicePort", 
		serviceName = "AccountInformationService_v3_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/AccountInformationService_3", 
		wsdlLocation = "/wsdls/AccountInformationService_v3_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.AccountInformationServicePortType")

@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class AccountInformationService_30 extends BaseService implements AccountInformationServicePort {
	private static final int RETURN_MAXIMUM = 1000;
	
	public AccountInformationService_30() {
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
	public void updateEmailAddress(final String ban, final String emailAddress)
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
	public void updateInvoicePropertyList(final String billingAccountNumber, final InvoicePropertyListType invoicePropertyList)
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
	public void updateContactInformation(final String billingAccountNumber, final ContactPropertyListType contactPropertyList) throws PolicyException, ServiceException {
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
	public void getBillCyclePropertyList(Holder<String> billingAccountNumber) throws PolicyException, ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
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
	public InternationalServiceEligibilityCheckResult checkInternationalServiceEligibility(String billingAccountNumber) throws PolicyException, ServiceException {
		throw new ServicePolicyException (ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is not yet implemented.");
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

}
