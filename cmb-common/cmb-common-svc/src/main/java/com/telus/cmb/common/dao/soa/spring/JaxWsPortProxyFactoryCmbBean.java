package com.telus.cmb.common.dao.soa.spring;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;

import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.util.AppConfiguration;

public class JaxWsPortProxyFactoryCmbBean extends JaxWsPortProxyFactoryBean implements InitializingBean {
	
	private static final Log LOGGER = LogFactory.getLog(JaxWsPortProxyFactoryCmbBean.class);
	
	private String ldapEndpointKey;
	private String ldapUserNameKey = "ClientAPIEJBUserName";			// Default to ClientAPI_EJB
	private String ldapUserPasswordKey = "ClientAPIEJBUserPassword";	// Default to ClientAPI_EJB

	public void afterPropertiesSet() {
		/*
		 * For setting the endpoint, a property "ldapEndpointKey" must be provided in the bean definition of JaxWsPortProxyFactoryCmbBean
		 * An entry with the value of ldapEndpointKey must be provided in the following file (e.g. "PrepaidWirelessCustomerOrderServiceUrl"):
		 * 		- ldap-common.xml (ldap entry)
		 */
		super.afterPropertiesSet();
		LOGGER.debug("afterPropertiesSet for JaxWsPortProxyFactoryCmbBean");
		setEndpoint();
		setWSCredential();
	}
	
	private void setEndpoint() {
		String endpoint = AppConfiguration.getStringByKey(ldapEndpointKey);
		LOGGER.info("Setting endpoint for key " + ldapEndpointKey + ": " + endpoint);
		if (StringUtils.isEmpty(ldapEndpointKey))
			throw new SystemException(SystemCodes.SOA_SPRING, "For serviceName " + getServiceName() + ": ldapEndpointKey is empty", "");
		if (StringUtils.isEmpty(endpoint))
			throw new SystemException(SystemCodes.SOA_SPRING, "For serviceName " + getServiceName() + ": endpoint is empty for key " + ldapEndpointKey, "");
		setEndpointAddress(endpoint);
	}
	
	private void setWSCredential() {
		setWSUserName();
		setWSUserPassword();
	}
	
	private void setWSUserName() {
		String ldapUserName = AppConfiguration.getStringByKey(ldapUserNameKey);
		LOGGER.info("Setting ldap user name for key " + ldapUserNameKey + ": " + ldapUserName);
		if (StringUtils.isEmpty(ldapUserNameKey))
			throw new SystemException(SystemCodes.SOA_SPRING, "For serviceName " + getServiceName() + ": ldapUserNameKey is empty", "");
		if (StringUtils.isEmpty(ldapUserName))
			throw new SystemException(SystemCodes.SOA_SPRING, "For serviceName " + getServiceName() + ": ldapUserName is empty for key " + ldapUserNameKey, "");
		setUsername(ldapUserName);
	}

	private void setWSUserPassword() {
		String ldapUserPassword = AppConfiguration.getStringByKey(ldapUserPasswordKey);
		LOGGER.debug("Setting ldap user password for key " + ldapUserPasswordKey + ": " + ldapUserPassword);
		if (StringUtils.isEmpty(ldapUserPasswordKey))
			throw new SystemException(SystemCodes.SOA_SPRING, "For serviceName " + getServiceName() + ": ldapUserPasswordKey is empty", "");
		if (StringUtils.isEmpty(ldapUserPassword))
			throw new SystemException(SystemCodes.SOA_SPRING, "For serviceName " + getServiceName() + ": ldapUserPassword is empty for key " + ldapUserPasswordKey, "");
		setPassword(ldapUserPassword);
	}

	
	public String getLdapEndpointyKey() {
		return ldapEndpointKey;
	}

	public void setLdapEndpointKey(String ldapEndpointKey) {
		this.ldapEndpointKey = ldapEndpointKey;
	}

	public String getLdapUserNameKey() {
		return ldapUserNameKey;
	}

	public void setLdapUserNameKey(String ldapUserNameKey) {
		this.ldapUserNameKey = ldapUserNameKey;
	}

	public String getLdapUserPasswordKey() {
		return ldapUserPasswordKey;
	}

	public void setLdapUserPasswordKey(String ldapUserPasswordKey) {
		this.ldapUserPasswordKey = ldapUserPasswordKey;
	}


}
