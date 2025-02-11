package com.telus.cmb.jws;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.cmb.jws.mapping.account_information_20.BillingPropertyMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.BusinessCreditInformationMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.PersonalCreditInformationMapper;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.BillingPropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BusinessCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation;

@WebService(portName = "WirelessAccountMgmtServicePort", serviceName = "WirelessAccountMgmtService_v1_0",
			targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/WirelessAccountMgmtService_1",
			wsdlLocation = "/wsdls/WirelessAccountMgmtService_v1_0.wsdl",
			endpointInterface="com.telus.cmb.jws.WirelessAccountMgmtServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
public class WirelessAccountManagementService_10 extends BaseService implements WirelessAccountMgmtServicePort {

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


}
