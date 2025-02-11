package com.telus.cmb.common.ejb;

import java.util.Date;

import com.telus.cmb.framework.config.ConfigurationManager;
import com.telus.cmb.framework.config.ConfigurationManagerFactory;

public abstract class AbstractLifecycleFacade implements LdapTestPoint {

	protected ConfigurationManager configurationManager = ConfigurationManagerFactory.getInstance(); 
	
	@Override
	public void overrideLdapValue(String key, Object value, Date effectiveDate, Date expiryDate) {
		configurationManager.overrideValue(key, value, effectiveDate, expiryDate);
	}

	@Override
	public void clearLdapOverride(String key) {
		configurationManager.resetValue(key);
	}

	@Override
	public void refresh() {
		configurationManager.refresh();
	}

	@Override
	public String getStringValue(String key) {
		return configurationManager.getStringValue(key);
	}
	
}
