package com.telus.cmb.jws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.FinancialHistory;
import com.telus.api.account.InvoiceHistory;
import com.telus.api.account.Subscriber;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper.AuditMapper;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper.BaseWirelessAccountMapper;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper.BusinessCreditIdentityMapper;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper.CreatePostpaidAccountResponseMapper;
import com.telus.cmb.jws.mapping.billing_inquiry.AuditHeaderMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.CreditCardMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AirtimeUsageChargeInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.PostpaidAccountCreationResponseInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.config.info.AccountStatusChangeInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.cmb.jws.CreatePostpaidAccount;
import com.telus.cmb.jws.CreatePostpaidAccountResponse;
import com.telus.cmb.jws.CreatePrepaidAccount;
import com.telus.cmb.jws.CreatePrepaidAccountResponse;
import com.telus.cmb.jws.RestoreAccountFromPayment;
import com.telus.cmb.jws.RestoreAccountFromPaymentResponse;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.AccountStatus;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BusinessCreditIdentity;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.ResponseMessage;



@WebService(
		portName = "WirelessAccountLifecycleMgmtServicePort", 
		serviceName = "WirelessAccountLifecycleMgmtService_v1_1", 
		targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/WirelessAccountLifecycleMgmtService_1", 
		wsdlLocation = "/wsdls/WirelessAccountLifecycleMgmtService_v1_1.wsdl", 
		endpointInterface = "com.telus.cmb.jws.WirelessAccountLifecycleMgmtServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
public class WirelessAccountLifecycleMgmtService_11 extends BaseService implements WirelessAccountLifecycleMgmtServicePort{

	private static final String CLM_suspendReasonCode = "CLMS";
	private static final String CLM_restoreAccountReasonCode = "CLMR";
	private RestoreAccountFromPaymentResponse notEligibleForRestoral; 
	@Override
	@ServiceBusinessOperation(errorCode="CMB_WALM_0001", errorMessage="Create Postpaid Account")
	public CreatePostpaidAccountResponse createPostpaidAccount(
			final CreatePostpaidAccount parameters,
			final OriginatingUserType createPostpaidAccountInSoapHdr)
			throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<CreatePostpaidAccountResponse>() {
		
		@Override
		public CreatePostpaidAccountResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
		
			if (createPostpaidAccountInSoapHdr == null) {
				throw new ServicePolicyException (ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
			}
			
	    	AuditHeaderMapper auditHeaderMapper = new AuditHeaderMapper();
			AuditHeaderInfo auditHeaderInfo = auditHeaderMapper.mapToDomain(createPostpaidAccountInSoapHdr);
			  
			AccountInfo accountInfo = getBaseWirelessAccountMapper().mapToDomain(parameters.getBaseAccountRequest().getBaseAccount());
			
			BusinessCreditIdentityInfo[] creditIdentityInfos=null;
			if(parameters.getBaseAccountRequest().getBusinessCreditIdentityList()!=null &&
					parameters.getBaseAccountRequest().getBusinessCreditIdentityList().getItem().size()>0){
				List<BusinessCreditIdentityInfo> businessCreditIdentityList=  new ArrayList<BusinessCreditIdentityInfo>(); 
				
				List<BusinessCreditIdentity> sourceList =  parameters.getBaseAccountRequest().getBusinessCreditIdentityList().getItem();
				if (sourceList != null && sourceList.size() > 0) {
					for (int i = 0; i < sourceList.size(); i++) {
						businessCreditIdentityList.add(getBusinessCreditIdentityMapper().mapToDomain(sourceList.get(i)));
					}
				}
				 creditIdentityInfos=	businessCreditIdentityList.toArray(new BusinessCreditIdentityInfo[businessCreditIdentityList.size()]);
			}	
			BusinessCreditIdentityInfo selectedBusinessCreditIdentity =getBusinessCreditIdentityMapper().mapToDomain(parameters.getBaseAccountRequest().getSelectedBusinessCreditIdentity());
			AuditInfo auditInfo= getAuditMapper().mapToDomain(parameters.getBaseAccountRequest().getAuditInfo());
			
			setTelusTidToMessageContext(auditInfo);
			
			String customerId=null;
			if( parameters.getBaseAccountRequest().getCompassCustomerId()!=null)
				customerId= parameters.getBaseAccountRequest().getCompassCustomerId().toString();
			
			 PostpaidAccountCreationResponseInfo postpaidAccountCreationResponse  =
				 getAccountLifecycleFacade(context).createPostpaidAccount(accountInfo, 
						 customerId, creditIdentityInfos, selectedBusinessCreditIdentity, auditHeaderInfo, auditInfo, 
					context.getAccountLifeCycleFacadeSessionId());
			
			 return getCreatePostpaidAccountResponseMapper().mapToSchema(postpaidAccountCreationResponse);
			
		}

	});
	}

	private void setTelusTidToMessageContext(AuditInfo auditInfo) {
		//grab the telusTID put into message context
		if ( auditInfo!=null
			&& "1".equals( auditInfo.getUserTypeCode()) 
			) {
			super.setTelusTidToMessageContext( auditInfo.getUserId() );
		}
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_WALM_0002", errorMessage="Create Prepaid Account")
	public CreatePrepaidAccountResponse createPrepaidAccount(
			final CreatePrepaidAccount parameters,
			final OriginatingUserType createPrepaidAccountInSoapHdr)
			throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<CreatePrepaidAccountResponse>() {
		
			@Override
			public CreatePrepaidAccountResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				if (createPrepaidAccountInSoapHdr == null) {
					throw new ServicePolicyException (ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
				}
				AuditHeaderMapper auditHeaderMapper = new AuditHeaderMapper();
				AuditHeaderInfo auditHeaderInfo = auditHeaderMapper.mapToDomain(createPrepaidAccountInSoapHdr);
				
				AccountInfo accountInfo = getBaseWirelessAccountMapper().mapToDomain(parameters.getPrepaidAccounttRequest().getBaseAccount());
				if(parameters.getPrepaidAccounttRequest().getCompassCustomerId()!=null)
				  ((PrepaidConsumerAccountInfo)accountInfo).setCustomerId(parameters.getPrepaidAccounttRequest().getCompassCustomerId().intValue());
				
				((PrepaidConsumerAccountInfo)accountInfo).setSerialNumber(parameters.getPrepaidAccounttRequest().getSerialNumber());
				
				if(parameters.getPrepaidAccounttRequest().getHandsetIMEI()!=null)
				  ((PrepaidConsumerAccountInfo)accountInfo).setAssociatedHandsetIMEI(parameters.getPrepaidAccounttRequest().getHandsetIMEI());
				
				if(parameters.getPrepaidAccounttRequest().getActivationType()!=null)
					((PrepaidConsumerAccountInfo)accountInfo).setActivationType(Integer.valueOf(parameters.getPrepaidAccounttRequest().getActivationType()));
				
				((PrepaidConsumerAccountInfo)accountInfo).setActivationCode(parameters.getPrepaidAccounttRequest().getActivationCode());
				
				if(parameters.getPrepaidAccounttRequest().getActivationAmount()!=null)
					((PrepaidConsumerAccountInfo)accountInfo).setActivationCreditAmount(parameters.getPrepaidAccounttRequest().getActivationAmount());
				
				if(parameters.getPrepaidAccounttRequest().getBirthDate()!=null)
					((PrepaidConsumerAccountInfo)accountInfo).setBirthDate(parameters.getPrepaidAccounttRequest().getBirthDate());
				
				CreditCardInfo creditCardInfo = new CreditCardMapper().mapToDomain(parameters.getPrepaidAccounttRequest().getActivationCreditCard());
				if (creditCardInfo != null) 
					((PrepaidConsumerAccountInfo) accountInfo).getActivationCreditCard0().copyFrom(creditCardInfo);
				
				CreditCardInfo topUpCreditCardInfo = new CreditCardMapper().mapToDomain(parameters.getPrepaidAccounttRequest().getTopUpCreditCard());
				if (topUpCreditCardInfo != null)
					((PrepaidConsumerAccountInfo) accountInfo).getTopUpCreditCard0().copyFrom(topUpCreditCardInfo);

				AuditInfo auditInfo= getAuditMapper().mapToDomain(parameters.getPrepaidAccounttRequest().getAuditInfo());

				setTelusTidToMessageContext(auditInfo);
				
				String customerId=null;
				if( parameters.getPrepaidAccounttRequest().getCompassCustomerId()!=null)
					customerId= parameters.getPrepaidAccounttRequest().getCompassCustomerId().toString();
				
				String businessRole=null;
				if(parameters.getPrepaidAccounttRequest().getBusinessRole()!=null)
					businessRole=parameters.getPrepaidAccounttRequest().getBusinessRole().value();
				
				int ban  =	 getAccountLifecycleFacade(context).createPrepaidAccount(accountInfo, 
							 customerId,businessRole, auditHeaderInfo, auditInfo, context.getAccountLifeCycleFacadeSessionId());
				 
				CreatePrepaidAccountResponse createPrepaidAccountResponse=new CreatePrepaidAccountResponse();
				createPrepaidAccountResponse.setBillingAccountNumber(String.valueOf(ban));
				return createPrepaidAccountResponse;
			   }
	  });
	}
	
	private BaseWirelessAccountMapper getBaseWirelessAccountMapper() {
		return WirelessAccountLifecycleMgmtMapper.BaseWirelessAccountMapper();
	}
	private BusinessCreditIdentityMapper getBusinessCreditIdentityMapper() {
		return WirelessAccountLifecycleMgmtMapper.BusinessCreditIdentityMapper();
	}
	private AuditMapper getAuditMapper() {
		return WirelessAccountLifecycleMgmtMapper.AuditMapper();
	}
	private CreatePostpaidAccountResponseMapper getCreatePostpaidAccountResponseMapper() {
		return WirelessAccountLifecycleMgmtMapper.CreatePostpaidAccountResponseMapper();
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_WALM_0003", errorMessage="Restore Account from Payment")
	public RestoreAccountFromPaymentResponse restoreAccountFromPayment(
			final RestoreAccountFromPayment parameters,
			final OriginatingUserType restoreAccountFromPaymentInSoapHdr)
			throws PolicyException, ServiceException 
	{
		return execute(new ServiceInvocationCallback<RestoreAccountFromPaymentResponse>() 
		{
			
			@Override
			public RestoreAccountFromPaymentResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable 
			{
				
				try {
					RestoreAccountFromPaymentResponse ret  = new RestoreAccountFromPaymentResponse();
					/*
					responseCode returns codes that indicates reasons why account could't not been restored, description will be populated in response message as follows:
					WALM_RA_000 Account restored successfully.
					WALM_RA_001 Account is not suspended.
					WALM_RA_002 Account is suspended but can’t be restored. 
					WALM_RA_003 Account is delinquent and can't be restored.
					WALM_RA_004 Account suspended due to CLM but paid amount is smaller than minimal payment. Account can't be restored.
					WALM_RA_005 Account suspended due to nonpayment, but amount paid is smaller than past due balance. Account can't be restored.
					WALM_RA_006 Account is not consumer postpaid and can't be restored.
					
					 */
					
					if (restoreAccountFromPaymentInSoapHdr == null) {
						throw new ServicePolicyException (ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
					}
					
					//Code from TMAccount.java line 1013; 
					 
					AccountInfo accountInfo = getAccountInformationHelper(context).retrieveAccountByBan(Integer.parseInt(parameters.getBillingAccountNumber()));
					//AirtimeUsageChargeInfo retrieveUnpaidAirtimeUsageChargeInfo(int ban)
					// restoreSuspendedAccount should not be called for Prepaid Account
					if (accountInfo.getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER
							&& accountInfo.getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID) {
						ret.setResponseCode("WALM_RA_006");								        	            
				        ret.getMessageList().setMessage("Account is not consumer postpaid and can't be restored.");
				        return ret; 
					}
					
					// get past due balance
					double pastDueBalance = accountInfo.getFinancialHistory().getDebtSummary().getPastDue();
					double amount = Double.parseDouble(parameters.getPaymentAmount()); 
					 
					//removed  payment amount check as per the new requirement for IVR phase 3
				//	if(isSuspendedDueToCLM()  && ((PostpaidConsumerAccount)this).getCLMSummary().getRequiredMinimumPayment() <= payment){
					//if(isSuspendedDueToCLM(accountInfo) && (accountInfo.getRequiredPaymentForRestoral() <= amount )) {
					if (accountInfo.getStatus() !=  AccountSummary.STATUS_SUSPENDED)
					{
						ret.setResponseCode("WALM_RA_001");	
						Message m = new Message(); 
						ret.setMessageList(m);
				        ret.getMessageList().setMessage("Account is not suspended.");
				        return ret;
					}
					
					if (accountInfo.getFinancialHistory().isDelinquent())
					{
						ret.setResponseCode("WALM_RA_003");
						Message m = new Message(); 
						ret.setMessageList(m);
				        ret.getMessageList().setMessage("Account is delinquent and can't be restored.");
				        return ret;
					}
					
						
					if (isSuspendedDueToCLM(accountInfo))
					{
						if (calculateRequiredPaymentForRestoral(accountInfo, context) <= amount)							
						{
							getAccountLifecycleFacade(context).restoreSuspendedAccount(accountInfo.getBanId(),
									new Date(), CLM_restoreAccountReasonCode, "Minimum payment was payed, Account is restored by CLM Rule " + context.getClientApplication(), false, null,null,context.getAccountLifeCycleManagerSessionId());					
						}
						else
						{
							//WALM_RA_004 Account suspended due to CLM but paid amount is smaller than minimal payment. Account can't be restored.
							ret.setResponseCode("WALM_RA_004");	
							Message m = new Message(); 
							ret.setMessageList(m);
					        ret.getMessageList().setMessage("Account suspended due to CLM, but paid amount is smaller than minimal payment. Account can't be restored.");
					        return ret;
						}						
					}
					
					boolean suspendedDueToNonPayment = (accountInfo.getStatus() == AccountSummary.STATUS_SUSPENDED)  &&
							("SNP".equals(accountInfo.getStatusActivityReasonCode().trim()) || "SNP1".equals(accountInfo.getStatusActivityReasonCode()));
					if (suspendedDueToNonPayment) 
					 {
						if (amount >= pastDueBalance) {
							   getAccountLifecycleFacade(context).restoreSuspendedAccount(accountInfo.getBanId(),
									new Date(), "PY", "Balance Paid using " + context.getClientApplication(), 
									true ,null,null, 
									context.getAccountLifeCycleFacadeSessionId());
						   }
						   else
						   {
							    ret.setResponseCode("WALM_RA_005");
								Message m = new Message(); 
								ret.setMessageList(m);
						        ret.getMessageList().setMessage("Account suspended due to nonpayment, but amount paid is smaller than past due balance. Account can't be restored.");
						        return ret;
						   }			
					} 
					else
					{
						ret.setResponseCode("WALM_RA_007");
						Message m = new Message(); 
						ret.setMessageList(m);
				        ret.getMessageList().setMessage("Account is suspended from different reason then nonpayment. Account can't be restored.");
				        return ret;
					}
						   
					boolean oldHotlined = accountInfo.getFinancialHistory().isHotlined(); 
					char oldStatus = accountInfo.getStatus();
						
					if (accountInfo.getFinancialHistory().isHotlined()) {
						accountInfo.setHotlined(false);
						context.getAccountLifecycleManager().updateHotlineInd(accountInfo.getBanId(), 
								false, 
								context.getAccountLifeCycleFacadeSessionId());
						
					} 
					
					boolean statusChanged = oldHotlined != accountInfo.isHotlined() || oldStatus != accountInfo.getStatus();

					if (statusChanged) {
						AccountStatusChangeInfo accountStatusChangeInfo = new AccountStatusChangeInfo();
						if (oldHotlined)
						{
							accountStatusChangeInfo.setOldHotlinedInd('N');
						}
						else
						{
							accountStatusChangeInfo.setOldHotlinedInd('Y');
						}
							accountStatusChangeInfo.setOldStatus(oldStatus);
								context.getSubscriberLifecycleFacade().asyncLogAccountStatusChange(accountStatusChangeInfo);	  
					}
					//WALM_RA_000 Account restored successfully. 
					ret.setResponseCode("WALM_RA_000");
					Message m = new Message(); 
					ret.setMessageList(m);
			        ret.getMessageList().setMessage("Account restored successfully");
			        return ret; 
				} catch (PolicyException pe)
				{
					RestoreAccountFromPaymentResponse ret  = new RestoreAccountFromPaymentResponse();
					Message m = new Message(); 
					ret.setMessageList(m);
			        ret.getMessageList().setMessage(pe.getMessage()); 
			        return ret; 
				} catch (Exception e)
				{
					throw new ServiceNestedException(e);
				}			
			}
		}); 
	}

	private boolean isSuspendedDueToCLM(AccountInfo ai) {
		final String CLM_suspendReasonCode = "CLMS";

		if ( ai.getStatus()  == AccountSummary.STATUS_SUSPENDED && (ai.getCreditCheckResult().getCreditClass().equals("X") || ai.getCreditCheckResult().getCreditClass().equals("L"))) {
			if (CLM_suspendReasonCode.equals(ai.getStatusActivityReasonCode().trim())) {
				return true;
			}
		}

		return false;
	}
	
	//coming from TMPostpaidConsumerAccount.java method getCLMSummary()
		private double calculateRequiredPaymentForRestoral(AccountInfo accountInfo, ServiceInvocationContext context) throws TelusAPIException{
			Date defaultDate = new Date(100, 0, 1); //2000-01-01
			double reqMinPayment = 0.0;
			
			
			//double unpaidVoiceUsage = accountInfo.getUnpaidAirtimeTotal();
			double unpaidVoiceUsage = 0.0; //auci.getTotalChargeAmount(); 
			double unpaidDataUsage=0.0;
			
			try {
				AirtimeUsageChargeInfo auci = getAccountInformationHelper(context).retrieveUnpaidAirtimeUsageChargeInfo(accountInfo.getBanId());  //retrieveUnpaidAirTimeTotal( getBanId() );
				unpaidVoiceUsage  = auci.getTotalChargeAmount();
				List <InvoiceHistory> invoiceHistories = getAccountInformationHelper(context).retrieveInvoiceHistory(accountInfo.getBanId(), defaultDate, new Date()); 
				
				Date invoiceDate = (invoiceHistories.size() > 0)? invoiceHistories.get(0).getDate(): defaultDate;
				if (invoiceHistories.size() >0) {
					invoiceDate = invoiceHistories.get(0).getDate();
					Calendar cal = Calendar.getInstance();
					cal.setTime(invoiceDate);
					//advance last invoice date by one month, so we get current bill cycle
					cal.add(Calendar.MONTH, 1 );
					
					int billCycleYear = cal.get(Calendar.YEAR );
					int billCycleMonth = cal.get(Calendar.MONTH )+1; 

					unpaidDataUsage = context.getAccountLifecycleFacade().getTotalUnbilledDataAmount( accountInfo.getBanId(), billCycleYear, billCycleMonth, accountInfo.getBillCycle() );
				} else {
					unpaidDataUsage = context.getAccountLifecycleFacade().getTotalDataOutstandingAmount(accountInfo.getBanId(), defaultDate );
				}
				
				double billedCharges = accountInfo.getFinancialHistory().getDebtSummary().getCurrentDue() + accountInfo.getFinancialHistory().getDebtSummary().getPastDue();
			    double unpaidUnBilledAmount = calculateUnpaidUnBilledAmount(accountInfo, context);
				reqMinPayment = (unpaidVoiceUsage + unpaidDataUsage + unpaidUnBilledAmount) - (accountInfo.getCreditCheckResult().getLimit() / 2);
				if (reqMinPayment < 0.0) {
					reqMinPayment = 0.0;
				}
				reqMinPayment = reqMinPayment + billedCharges;
				if (reqMinPayment < 0.0) {
					reqMinPayment = 0.0;
				}
				
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
			} 
			return 0.0; 
		}
		
		private double calculateUnpaidUnBilledAmount(AccountInfo accountInfo, ServiceInvocationContext context) {
			double totalAmount = 0;
			try {
				FinancialHistory financialHistory = accountInfo.getFinancialHistory();
				
				//totalAmount = accountInfo.getLastPaymentActivity().getPaymentActivities()
				//		+ financialHistory.getDebtSummary().getPastDue()
					
				Date billedDueDate = financialHistory.getDebtSummary().getBillDueDate();
				if(billedDueDate == null){
					//Subscriber[] sub = accountInfo.getSubscribers(1);
					//List <Subscriber> sub = getAccountInformationHelper(context).retrieveSubscriberIdsByStatus(accountInfo.getBanId(), AccountSummary.STATUS_SUSPENDED, 1); 
					Collection <SubscriberInfo> subscriberInfoList = getSubscriberLifecycleHelper(context).retrieveSubscriberListByBAN(Integer.valueOf(accountInfo.getBanId()), 1);
					if (subscriberInfoList.size() > 0)
					{
						for (SubscriberInfo subscriberInfo : subscriberInfoList) 
						{
							billedDueDate = subscriberInfo.getStartServiceDate();
							break;
						}
					}
				}
				if (billedDueDate.before(new Date())) {
					
					List <InvoiceHistory> invoiceHistories = getAccountInformationHelper(context).retrieveInvoiceHistory(accountInfo.getBanId(), billedDueDate, new Date()); 
					for (int i = 0; i < invoiceHistories.size(); i++) {
						totalAmount += invoiceHistories.get(i).getAmountDue() - invoiceHistories.get(i).getPastDue();
					}
				}

			} catch (Throwable e) {
			
			}
			return totalAmount;
		}
		
}
