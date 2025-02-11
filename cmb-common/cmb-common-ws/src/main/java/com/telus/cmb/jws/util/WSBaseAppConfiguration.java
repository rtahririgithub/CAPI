package com.telus.cmb.jws.util;

import com.telus.cmb.common.util.BaseAppConfiguration;

public abstract class WSBaseAppConfiguration extends BaseAppConfiguration {

	public static String getServiceOrderReferenceFacadeUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.SERVICE_ORDER_REFERENCE_FACADE_URL);
	}

	public static String getCustomerInformationReferenceFacadeUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.CUSTOMER_INFORMATION_REFERENCE_FACADE_URL);
	}

	public static String getCustomerOrderReferenceFacadeUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.CUSTOMER_ORDER_REFERENCE_FACADE_URL);
	}

	public static String getEnterpriseReferenceFacadeUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.ENTERPRISE_REFERENCE_FACADE_URL);
	}
	
	public static String BillingInquiryReferenceFacadeUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.BILLING_INQUIRY_REFERENCE_FACADE_URL);
	}
	
}
