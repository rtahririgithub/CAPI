package com.telus.cmb.utility.utilities;

import com.telus.cmb.common.util.BaseAppConfiguration;

public class AppConfiguration extends BaseAppConfiguration {
	
	public static String getSrpsEASUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.SRPS_EAS_URL);
	}
	
}
