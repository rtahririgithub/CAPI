package com.telus.cmb.jws.utilities;

import com.telus.cmb.jws.util.WSBaseAppConfiguration;

public class AppConfiguration extends WSBaseAppConfiguration {

	public static String getResourceOrderReferenceFacadeUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.RESOURCE_ORDER_REFERENCE_FACADE_URL);
	}

}
