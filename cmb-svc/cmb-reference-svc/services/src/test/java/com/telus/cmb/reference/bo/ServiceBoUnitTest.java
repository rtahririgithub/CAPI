package com.telus.cmb.reference.bo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.telus.api.reference.BusinessRole;
import com.telus.api.reference.ServiceSummary;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServicePolicyInfo;
import com.telus.cmb.reference.bo.ServiceBo;

/**
 * @author Brandon Wen
 * Mar 26, 2015
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceBoUnitTest {
	private ServiceBo serviceBo1;
	private ServiceBo serviceBo2;
	
	@Mock private ServiceInfo serviceInfo1; 
	@Mock private ServiceInfo serviceInfo2; 
	@Mock private ReferenceDataFacade referenceFacade; 
	@Mock private ServicePolicyInfo servicePolicy1;
	@Mock private ServicePolicyInfo servicePolicy2;
	
	@Before
	public void startup() throws TelusException {
		when(referenceFacade.getServicePolicy(anyString())).thenReturn(null);
		when(serviceInfo1.getCode()).thenReturn("code1");
		when(serviceInfo2.getCode()).thenReturn("code2");
		when(serviceInfo1.isClientActivation()).thenReturn(false);
		when(serviceInfo1.isDealerActivation()).thenReturn(true);
		
		serviceBo1 = new ServiceBo(serviceInfo1);
		serviceBo2 = new ServiceBo(serviceInfo2);
	}
	
	@Test
	public void constructor() {
		assertNotNull(new ServiceBo(serviceInfo1));
	}
	
	@Test
	public void getDelegate() {
		ServiceBo serviceBo1 = new ServiceBo(serviceInfo1);
		assertThat(serviceBo1.getDelegate(), equalTo(serviceInfo1));
		assertThat(serviceBo1.getDelegate().getCode(), equalTo("code1"));
	}
	
	@Test
	public void getServicePolicy_from_referencefacade_case1() throws TelusException {
		when(referenceFacade.getServicePolicy("code1AGENTADD")).thenReturn(servicePolicy1);
		when(referenceFacade.getServicePolicy("code2ALLADD")).thenReturn(servicePolicy2);
		
		ServicePolicyInfo servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_AGENT, ServiceSummary.PRIVILEGE_SERVICE_ADD, referenceFacade);
		assertThat(servicePolicy, equalTo(servicePolicy1));
		assertThat(servicePolicy, not(servicePolicy2));
	}

	@Test
	public void getServicePolicy_from_referencefacade_case2() throws TelusException {
		when(referenceFacade.getServicePolicy("code1AGENTADD")).thenReturn(servicePolicy1);
		when(referenceFacade.getServicePolicy("code2ALLADD")).thenReturn(servicePolicy2);
		
		ServicePolicyInfo servicePolicy = serviceBo2.getServicePolicy(BusinessRole.BUSINESS_ROLE_AGENT, ServiceSummary.PRIVILEGE_SERVICE_ADD, referenceFacade);
		assertThat(servicePolicy, not(servicePolicy1));
		assertThat(servicePolicy, equalTo(servicePolicy2));
	}
	
	@Test(expected=TelusException.class)
	public void getServicePolicy_from_referencefacade_exception() throws Exception {
		when(referenceFacade.getServicePolicy(anyString())).thenThrow(new TelusException());
		serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_AGENT, ServiceSummary.PRIVILEGE_SERVICE_ADD, referenceFacade);
	}
	
	@Test
	public void getServicePolicy_set_in_method_case1() throws TelusException {
		ServicePolicyInfo servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_AGENT, ServiceSummary.PRIVILEGE_AUTOADD, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_AGENT));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_AUTOADD));
		assertThat(servicePolicy.isAvailable(), equalTo(false));
	}
	
	@Test
	public void getServicePolicy_set_in_method_case2() throws TelusException {
		ServicePolicyInfo servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_AGENT, ServiceSummary.PRIVILEGE_SERVICE_DISPLAY, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_AGENT));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_DISPLAY));
		assertThat(servicePolicy.isAvailable(), equalTo(true));
		
		servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_AGENT_LNR, ServiceSummary.PRIVILEGE_SERVICE_DISPLAY, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_AGENT_LNR));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_DISPLAY));
		assertThat(servicePolicy.isAvailable(), equalTo(true));

		
		servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_AGENT_ACTIVATIONS, ServiceSummary.PRIVILEGE_SERVICE_DISPLAY, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_AGENT_ACTIVATIONS));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_DISPLAY));
		assertThat(servicePolicy.isAvailable(), equalTo(true));
		
		servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_CORPORATE_STORE, ServiceSummary.PRIVILEGE_SERVICE_DISPLAY, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_CORPORATE_STORE));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_DISPLAY));
		assertThat(servicePolicy.isAvailable(), equalTo(true));
		
		servicePolicy = serviceBo2.getServicePolicy(BusinessRole.BUSINESS_ROLE_DEALER, ServiceSummary.PRIVILEGE_SERVICE_DISPLAY, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code2"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_DEALER));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_DISPLAY));
		assertThat(servicePolicy.isAvailable(), equalTo(true));
		
		servicePolicy = serviceBo2.getServicePolicy(BusinessRole.BUSINESS_ROLE_DEALER, ServiceSummary.PRIVILEGE_SERVICE_REMOVE, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code2"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_DEALER));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_REMOVE));
		assertThat(servicePolicy.isAvailable(), equalTo(true));
		
		servicePolicy = serviceBo2.getServicePolicy(BusinessRole.BUSINESS_ROLE_DEALER, ServiceSummary.PRIVILEGE_SERVICE_RETAIN_ON_PRICE_PLAN_CHANGE, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code2"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_DEALER));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_RETAIN_ON_PRICE_PLAN_CHANGE));
		assertThat(servicePolicy.isAvailable(), equalTo(true));
		
		servicePolicy = serviceBo2.getServicePolicy(BusinessRole.BUSINESS_ROLE_DEALER, ServiceSummary.PRIVILEGE_SERVICE_RETAIN_ON_RENEWAL, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code2"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_DEALER));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_RETAIN_ON_RENEWAL));
		assertThat(servicePolicy.isAvailable(), equalTo(true));

	}
	
	@Test
	public void getServicePolicy_set_in_method_case3() throws TelusException {
		ServicePolicyInfo servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_CLIENT, ServiceSummary.PRIVILEGE_SERVICE_ADD, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_CLIENT));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_ADD));
		assertThat(servicePolicy.isAvailable(), equalTo(serviceInfo1.isClientActivation()));
		
		servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_CLIENT_ACTIVATIONS, ServiceSummary.PRIVILEGE_SERVICE_ADD, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_CLIENT_ACTIVATIONS));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_ADD));
		assertThat(servicePolicy.isAvailable(), equalTo(serviceInfo1.isClientActivation()));
		
		servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_IVR_CLIENT, ServiceSummary.PRIVILEGE_SERVICE_ADD, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_IVR_CLIENT));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_ADD));
		assertThat(servicePolicy.isAvailable(), equalTo(serviceInfo1.isClientActivation()));
		
	}	

	@Test
	public void getServicePolicy_set_in_method_case4() throws TelusException {
		ServicePolicyInfo servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_DEALER, ServiceSummary.PRIVILEGE_SERVICE_ADD, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_DEALER));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_ADD));
		assertThat(servicePolicy.isAvailable(), equalTo(serviceInfo1.isDealerActivation()));
		
		servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_DEALER_ACTIVATIONS, ServiceSummary.PRIVILEGE_SERVICE_ADD, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_DEALER_ACTIVATIONS));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_ADD));
		assertThat(servicePolicy.isAvailable(), equalTo(serviceInfo1.isDealerActivation()));
		
		servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_CONTRACT_RENEWAL, ServiceSummary.PRIVILEGE_SERVICE_ADD, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_CONTRACT_RENEWAL));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_ADD));
		assertThat(servicePolicy.isAvailable(), equalTo(serviceInfo1.isDealerActivation()));
		
		servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_CONTRACT_RENEWAL_CORP, ServiceSummary.PRIVILEGE_SERVICE_ADD, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_CONTRACT_RENEWAL_CORP));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_ADD));
		assertThat(servicePolicy.isAvailable(), equalTo(serviceInfo1.isDealerActivation()));
		
		servicePolicy = serviceBo1.getServicePolicy(BusinessRole.BUSINESS_ROLE_RETAIL_STORE, ServiceSummary.PRIVILEGE_SERVICE_ADD, referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo(BusinessRole.BUSINESS_ROLE_RETAIL_STORE));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo(ServiceSummary.PRIVILEGE_SERVICE_ADD));
		assertThat(servicePolicy.isAvailable(), equalTo(serviceInfo1.isDealerActivation()));
		
	}	

	@Test
	public void getServicePolicy_set_in_method_case5() throws TelusException {
		ServicePolicyInfo servicePolicy = serviceBo1.getServicePolicy("OtherRoleCode", "OtherPrivilegeCode", referenceFacade);
		assertThat(servicePolicy.getServiceCode(), equalTo("code1"));
		assertThat(servicePolicy.getBusinessRoleCode(), equalTo("OtherRoleCode"));
		assertThat(servicePolicy.getPrivilegeCode(), equalTo("OtherPrivilegeCode"));
		assertThat(servicePolicy.isAvailable(), equalTo(true));
	}
}
