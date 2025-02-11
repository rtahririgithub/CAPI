/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.bo;

import com.telus.api.reference.BusinessRole;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSummary;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServicePolicyInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class ServiceBo extends ReferenceBo<ServiceInfo> {

	private static final long serialVersionUID = 1L;

	public ServiceBo(ServiceInfo service) {
		super(service);
	}
	
	public ServicePolicyInfo getServicePolicy(String businessRoleCode, String privilegeCode, ReferenceDataFacade referenceFacade) throws TelusException  {

		ServiceInfo service = getDelegate();
		
		ServicePolicyInfo servicePolicy = referenceFacade.getServicePolicy(service.getCode().trim() + businessRoleCode.trim() + privilegeCode.trim());
		if (servicePolicy == null) {
			servicePolicy = referenceFacade.getServicePolicy(service.getCode().trim() + BusinessRole.BUSINESS_ROLE_ALL + privilegeCode.trim());
		}

		if (servicePolicy == null) {
			
			servicePolicy = new ServicePolicyInfo();
			
			servicePolicy.setServiceCode(service.getCode());
			servicePolicy.setBusinessRoleCode(businessRoleCode);
			servicePolicy.setPrivilegeCode(privilegeCode);

			if (Service.PRIVILEGE_AUTOADD.equals(privilegeCode)) {
				servicePolicy.setAvailable(false);
			} else {
				
				if ((businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_AGENT)) || (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_AGENT_LNR))
						|| (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_AGENT_ACTIVATIONS))
						|| (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_CORPORATE_STORE))
						|| (privilegeCode.equals(ServiceSummary.PRIVILEGE_SERVICE_DISPLAY))
						|| (privilegeCode.equals(ServiceSummary.PRIVILEGE_SERVICE_REMOVE))
						|| (privilegeCode.equals(ServiceSummary.PRIVILEGE_SERVICE_RETAIN_ON_PRICE_PLAN_CHANGE))
						|| (privilegeCode.equals(ServiceSummary.PRIVILEGE_SERVICE_RETAIN_ON_RENEWAL))) {
					
					servicePolicy.setAvailable(true);
					
				} else if (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_CLIENT)
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_CLIENT_ACTIVATIONS)
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_IVR_CLIENT)) {
					
					servicePolicy.setAvailable(service.isClientActivation());
					
				} else if (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_DEALER) 
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_DEALER_ACTIVATIONS)
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_CONTRACT_RENEWAL)
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_CONTRACT_RENEWAL_CORP)
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_RETAIL_STORE)) {
					
					servicePolicy.setAvailable(service.isDealerActivation());
					
				} else {
					servicePolicy.setAvailable(true);
				}
			}
		}
		
		return servicePolicy;
	}	

}
