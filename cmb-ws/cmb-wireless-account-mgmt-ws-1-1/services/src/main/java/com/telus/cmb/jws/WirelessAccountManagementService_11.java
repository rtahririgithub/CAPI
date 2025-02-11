package com.telus.cmb.jws;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.cmb.jws.mapping.account_information_20.BillingPropertyMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.BusinessCreditInformationMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.PersonalCreditInformationMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.BusinessCreditIdentityMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.CreditCheckResultMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.cmb.jws.SaveBusinessCreditCheckResult.BusinessCreditIdentityList;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.BillingPropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BusinessCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BusinessCreditIdentity;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.CreditCheckResult;
/**
 * @author Inbaselvan Gandhi
 *
 */
@WebService(portName = "WirelessAccountMgmtServicePort", serviceName = "WirelessAccountMgmtService_v1_1",
		targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/WirelessAccountMgmtService_1",
		wsdlLocation = "/wsdls/WirelessAccountMgmtService_v1_1.wsdl",
		endpointInterface="com.telus.cmb.jws.WirelessAccountMgmtServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
public class WirelessAccountManagementService_11 extends BaseService implements WirelessAccountMgmtServicePort {

	private static final String DEPOSIT_CHANGE_REASON_CODE = "103";
	private static final CreditCheckResultMapper ccrMapper = new CreditCheckResultMapper();
	private static final BusinessCreditIdentityMapper bciMapper = new BusinessCreditIdentityMapper();
	private static final com.telus.cmb.jws.mapping.customer_management_common_40.PersonalCreditInformationMapper pciMapper = new com.telus.cmb.jws.mapping.customer_management_common_40.PersonalCreditInformationMapper();
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_WAM_0001", errorMessage="Update Account Password error")
	public void updateAccountPassword(final String billingAccountNumber, final String newPassword) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
		        getAccountLifecycleFacade(context).updateAccountPassword(Integer.valueOf(billingAccountNumber), newPassword, context.getAccountLifeCycleFacadeSessionId());
				return null;
			}
			
		});

	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_WAM_0002", errorMessage="Update Billing Information error")
	public void updateBillingInformation(final String billingAccountNumber, final BillingPropertyListType billingPropertyListType) throws PolicyException, ServiceException {
		execute (new ServiceInvocationCallback<Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				BillingPropertyInfo billingPropertyInfo = BillingPropertyMapper.getInstance().mapToDomain(billingPropertyListType);
				getAccountLifecycleFacade(context).updateBillingInformation(Integer.valueOf(billingAccountNumber), billingPropertyInfo, context.getAccountLifeCycleFacadeSessionId());
				return null;
			}
		});

	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_WAM_0003", errorMessage="Update Bill Cycle error")
	public void updateBillCycle(final String billingAccountNumber, final int billCycle) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
		       	getAccountLifecycleFacade(context).updateBillCycle(Integer.valueOf(billingAccountNumber), (short)billCycle, context.getAccountLifeCycleFacadeSessionId());
				return null;
			}
			
		});

	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_WAM_0004", errorMessage="Update Personal Credit Information")
	public void updatePersonalCreditInformation(final String billingAccountNumber,
			final PersonalCreditInformation personalCreditInformation)
			throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				PersonalCreditInfo personalCreditInfo = PersonalCreditInformationMapper.getInstance().mapToDomain(personalCreditInformation);
		       	getAccountLifecycleManager(context).updatePersonalCreditInformation(Integer.valueOf(billingAccountNumber), personalCreditInfo, context.getAccountLifeCycleManagerSessionId());
				return null;
			}
			
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_WAM_0005", errorMessage="Update Business Credit Information")
	public void updateBusinessCreditInformation(final String billingAccountNumber,
			final BusinessCreditInformation businessCreditInformation)
			throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				BusinessCreditInfo businessCreditInfo = BusinessCreditInformationMapper.getInstance().mapToDomain(businessCreditInformation);
		       	getAccountLifecycleManager(context).updateBusinessCreditInformation(Integer.valueOf(billingAccountNumber), businessCreditInfo, context.getAccountLifeCycleManagerSessionId());
				return null;
			}
			
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_WAM_0006", errorMessage="Update Credit Check Result")
	public void updateCreditCheckResult(final String billingAccountNumber,
			final Boolean idenInd, final CreditCheckResult currentCreditCheckResult,
			final CreditCheckResult newCreditCheckResult, final Boolean asyncInd)
			throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				if (currentCreditCheckResult == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Missing currentCreditCheckResult");
				}else if (newCreditCheckResult == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Missing newCreditCheckResult");
				}

				NumberFormat formatter = new DecimalFormat("0.00");
				int banNumber = Integer.parseInt(billingAccountNumber);
				CreditCheckResultInfo oldCreditCheckResultInfo = ccrMapper.mapToDomain(currentCreditCheckResult);
				CreditCheckResultInfo newCreditCheckResultInfo = ccrMapper.mapToDomain(newCreditCheckResult);
				
				String currentCreditClass = oldCreditCheckResultInfo.getCreditClass();
				String newCreditClass = newCreditCheckResultInfo.getCreditClass();
				
				double currentLimit = oldCreditCheckResultInfo.getLimit();
		        double newLimit = newCreditCheckResultInfo.getLimit();
		        
		        CreditCheckResultDepositInfo[] currentDeposits = (CreditCheckResultDepositInfo[]) oldCreditCheckResultInfo.getDeposits();
		        CreditCheckResultDepositInfo[] newDeposits = (CreditCheckResultDepositInfo[]) newCreditCheckResultInfo.getDeposits();
		        
		        boolean isClassChanged = !newCreditClass.equals(currentCreditClass);
		        boolean isDepositChanged = isDepositResultChanged(currentDeposits, newDeposits);
		        boolean isLimitChanged = (newLimit != currentLimit);
		        
		        if (isLimitChanged) {
		        	StringBuffer msgText = new StringBuffer();
		        	msgText.append("Subscriber Eligibility Change: ");
		        	msgText.append("Credit Limit from ");
		        	msgText.append(formatter.format(currentLimit));
		        	msgText.append(" to ");
		        	msgText.append(formatter.format(newLimit));
		        	
		        	if(asyncInd){
		        		getAccountLifecycleFacade(context).asyncUpdateCreditProfile(banNumber, newCreditClass, newLimit, msgText.toString(), context.getAccountLifeCycleFacadeSessionId());
		        	}else {
		        		getAccountLifecycleManager(context).updateCreditProfile(banNumber,newCreditClass,newLimit,msgText.toString(), context.getAccountLifeCycleManagerSessionId());
		        	}
		        }
		        
		        if (isClassChanged || isDepositChanged) {

		        	StringBuffer messageText = new StringBuffer();
		        	messageText.append("Subscriber Eligibility Change: ");

		        	if (isClassChanged) {
		        		messageText.append("Credit Class from ");
		        		messageText.append(currentCreditClass);
		        		messageText.append(" to ");
		        		messageText.append(newCreditClass);
		        		messageText.append(", ");
		        	}

		        	if (isDepositChanged) {
		        		boolean bIsIDEN = false;
		        		if (idenInd != null) {
		        			bIsIDEN = idenInd.booleanValue();
		        		}
		        		
		        		Iterator<DepositChangeMessage> messages = depositChangeMessage(currentDeposits, newDeposits, bIsIDEN).iterator();
		        		DepositChangeMessage message = null;

		        		while (messages.hasNext()) {

		        			message = messages.next();

		        			messageText.append("Deposit Amount for Product Type ");
		        			messageText.append(message.productType);
		        			messageText.append(" from ");
		        			messageText.append(formatter.format(message.oldDepositAmount));
		        			messageText.append(" to ");
		        			messageText.append(formatter.format(message.newDepositAmount));
		        			messageText.append(", ");

		        		}
		        	}

		        	if(asyncInd){
		        		getAccountLifecycleFacade(context).asyncUpdateCreditCheckResult(banNumber, 
								   newCreditClass, 
								   mapCreditCheckDeposits(newDeposits),
								   DEPOSIT_CHANGE_REASON_CODE, 
								   messageText.toString(),
								   context.getAccountLifeCycleFacadeSessionId());
		        	}else {
		        		getAccountLifecycleManager(context).updateCreditCheckResult(banNumber, 
								   newCreditClass, 
								   mapCreditCheckDeposits(newDeposits),
								   DEPOSIT_CHANGE_REASON_CODE, 
								   messageText.toString(),
								   context.getAccountLifeCycleManagerSessionId());	
		        	}
		      }
				return null;
			}
			
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_WAM_0007", errorMessage="Save Credit Check Result")
	public void saveCreditCheckResult(final String billingAccountNumber,
			final CreditCheckResult creditCheckResult, 
			final com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.PersonalCreditInformation personalCreditInformation,
			final Boolean asyncInd)
			throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				if (creditCheckResult == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Missing creditCheckResult");
				}
				
				int ban = Integer.parseInt(billingAccountNumber);
				CreditCheckResultInfo creditCheckResultInfo = ccrMapper.mapToDomain(creditCheckResult);
				PersonalCreditInfo personalCreditInfo = pciMapper.mapToDomain(personalCreditInformation) ;

				AccountInfo accountInfo;
				
					accountInfo = getAccountInformationHelper(context).retrieveAccountByBan(ban);
				
					if (asyncInd) {
			        	getAccountLifecycleFacade(context).asyncSaveCreditCheckInfo(accountInfo.getBanId(), personalCreditInfo, creditCheckResultInfo, "I", context.getAccountLifeCycleFacadeSessionId());
					} else {
						getAccountLifecycleManager(context).saveCreditCheckInfo(accountInfo, creditCheckResultInfo, "I", context.getAccountLifeCycleManagerSessionId());
					}
					
				return null;
			}
			
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_WAM_0008", errorMessage="Save Business Credit Check Result")
	public void saveBusinessCreditCheckResult(
			final String billingAccountNumber,
			final CreditCheckResult creditCheckResult,
			final BusinessCreditIdentityList businessCreditIdentityList,
			final BusinessCreditIdentity selectedBusinessCreditIdentity,
			com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BusinessCreditInformation businessCreditInforamtion,
			final boolean asyncInd) throws PolicyException, ServiceException {

		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				if (creditCheckResult == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Missing creditCheckResult");
				}
				
				int ban = Integer.parseInt(billingAccountNumber);
				CreditCheckResultInfo creditCheckResultInfo = ccrMapper.mapToDomain(creditCheckResult);

				AccountInfo accountInfo;
				
					accountInfo = getAccountInformationHelper(context).retrieveAccountByBan(ban);

					if (asyncInd) {
						BusinessCreditInfo businessCreditInfo = new BusinessCreditInfo();
			        	businessCreditInfo.setIncorporationDate(creditCheckResultInfo.getLastCreditCheckIncorporationDate());
			        	businessCreditInfo.setIncorporationNumber(creditCheckResultInfo.getLastCreditCheckIncorporationNumber());
			        	List<BusinessCreditIdentityInfo> bcList = bciMapper.mapToDomain(businessCreditIdentityList.getItem());
			        	BusinessCreditIdentityInfo[] bciiList = bcList.toArray(new BusinessCreditIdentityInfo[bcList.size()]);
			        	BusinessCreditIdentityInfo bciInfo = bciMapper.mapToDomain(selectedBusinessCreditIdentity) ;
			        	getAccountLifecycleFacade(context).asyncSaveCreditCheckInfoForBusiness(accountInfo.getBanId(), businessCreditInfo, bciiList, bciInfo,creditCheckResultInfo, "I", context.getAccountLifeCycleFacadeSessionId());
					} else {
						getAccountLifecycleManager(context).saveCreditCheckInfoForBusiness(accountInfo, null, null, creditCheckResultInfo, "I", context.getAccountLifeCycleManagerSessionId());
					}
				return null;
			}
			
		});
	
	}

	private boolean isDepositResultChanged(CreditCheckResultDepositInfo[] currentDeposits, CreditCheckResultDepositInfo[] newDeposits) {
		if ((currentDeposits == null || currentDeposits.length == 0) && 
			(newDeposits == null || newDeposits.length == 0)) {
			return false;
		}

		if ((currentDeposits == null || currentDeposits.length == 0) && 
			!(newDeposits == null || newDeposits.length == 0)) {
			return true;
		}

		if (!(currentDeposits == null || currentDeposits.length == 0) && 
			(newDeposits == null || newDeposits.length == 0)) {
			return true;
		}

		for (CreditCheckResultDepositInfo newDeposit : newDeposits) {
			for (CreditCheckResultDepositInfo currentDeposit : currentDeposits) {
				if (newDeposit.getProductType().equals(currentDeposit.getProductType())) {
					if (newDeposit.getDeposit() != currentDeposit.getDeposit()) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	private class DepositChangeMessage {
		String productType;
		double oldDepositAmount;
		double newDepositAmount;
	}
	
	private List<DepositChangeMessage> depositChangeMessage(CreditCheckResultDepositInfo[] currentDeposits, 
			CreditCheckResultDepositInfo[] newDeposits,
			boolean isIDEN) {

		List<DepositChangeMessage> messages = new ArrayList<DepositChangeMessage>();
		DepositChangeMessage message = null;

		if (currentDeposits != null && newDeposits != null) {
			for (CreditCheckResultDepositInfo currentDeposit : currentDeposits) {
				for (CreditCheckResultDepositInfo newDeposit : newDeposits) {
					if (currentDeposit.getProductType().equals(newDeposit.getProductType())) {
						if (currentDeposit.getDeposit() != newDeposit.getDeposit()) {

							message = new DepositChangeMessage();
							String productType = SubscriberInfo.PRODUCT_TYPE_PCS;
							if (isIDEN) {
								productType = SubscriberInfo.PRODUCT_TYPE_IDEN;
							}
							message.productType = productType;
							message.oldDepositAmount = currentDeposit.getDeposit();
							message.newDepositAmount = newDeposit.getDeposit();

							messages.add(message);
						}
					}
				}
			}
		}

		return messages;
	}

	private CreditCheckResultDepositInfo[] mapCreditCheckDeposits(CreditCheckResultDepositInfo[] deposits){
		CreditCheckResultDepositInfo[] depositInfos = null;
		if (deposits != null && deposits.length > 0){
			depositInfos = new CreditCheckResultDepositInfo[deposits.length];
			for (int i=0; i<deposits.length; i++ ){
				CreditCheckResultDepositInfo depositInfo = new CreditCheckResultDepositInfo();
				depositInfo.setDeposit(deposits[i].getDeposit());
				depositInfo.setProductType(deposits[i].getProductType());
				depositInfos[i] = depositInfo;
			}
		}

		return depositInfos;
	}


}
