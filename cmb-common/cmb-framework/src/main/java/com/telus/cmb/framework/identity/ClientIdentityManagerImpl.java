/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.identity;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.List;

import javax.crypto.Cipher;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextExecutor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapUtils;

/**
 * @author Pavel Simonovsky
 *
 */
public class ClientIdentityManagerImpl implements ClientIdentityManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientIdentityManagerImpl.class);

	private static final String VALUE_ATTR = "telusconfigstringvalue";

    private static String IDENTITY_KEYSTORE_RESOURCE = "/identity-keystore.jks";

    private static String IDENTITY_KEYSTORE_PASSWORD = "weblogic";

    private static String IDENTITY_KEY_ALIAS = "figaro";
    
    private static String IDENTITY_KEY_PASSWORD = "weblogic";
	
	private LdapTemplate ssoLdapTemplate;
	
	private LdapTemplate configLdapTemplate;
	
	private String configLdapBaseDn = "cn=ClientAgent,cn=Infra";
	
	private String ssoLdapBase = "ou=people,ou=teammembers,ou=internal,o=telus";
	
	private Cipher cipher;
	
	public void setSsoLdapTemplate(LdapTemplate ldapTemplate) {
		this.ssoLdapTemplate = ldapTemplate;
	}

	public void setConfigLdapTemplate(LdapTemplate ldapTemplate) {
		this.configLdapTemplate = ldapTemplate;
	}
	
	/**
	 * 
	 */
	public ClientIdentityManagerImpl() {

		// cryptographic cipher initialization
		
		try {

			KeyStore keystore = KeyStore.getInstance("JKS");
			InputStream stream = getClass().getResourceAsStream(IDENTITY_KEYSTORE_RESOURCE);
			keystore.load(stream, IDENTITY_KEYSTORE_PASSWORD.toCharArray());
			PrivateKey privateKey = (PrivateKey) keystore.getKey(IDENTITY_KEY_ALIAS, IDENTITY_KEY_PASSWORD.toCharArray());

			logger.debug("Private key in use: [{}]", privateKey);
			
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			
		} catch (Exception e) {
			throw new RuntimeException("Error configuring client identity manager: " + e.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.identity.ClientIdentityManager#getApplicationClientIdentity(java.lang.String)
	 */
	@Override
	@Cacheable("clientIdentities")
	public ClientIdentity getApplicationClientIdentity(final String applicationCode) {
		
		return configLdapTemplate.executeReadOnly( new ContextExecutor<ClientIdentity>() {
			
			@Override
			public ClientIdentity executeWithContext(DirContext ctx) throws NamingException {
				
				String code = applicationCode;
				
				int lastIndex = applicationCode.lastIndexOf('\\');
				if (lastIndex != -1) {
					code = applicationCode.substring(lastIndex + 1);
				}

				Name base = new LdapName(configLdapBaseDn).add( new Rdn("cn", code));
				
				try {

					ClientIdentity identity = new ClientIdentity();
					
					identity.setApplication(getChildNodeValue(base, "applicationCode", ctx));
					identity.setPrincipal(getChildNodeValue(base, "principal", ctx));
					identity.setCredential(decrypt(getChildNodeValue(base, "credential", ctx)));
					
					return identity;
					
				} catch (Exception e) {
					logger.warn("Unable to retrive client identity for application code [{}]: {}", applicationCode, e.getMessage());
				}
				return null;
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.identity.ClientIdentityManager#getUserClientIdentity(java.lang.String)
	 */
	@Override
	@Cacheable("clientIdentities")
	public ClientIdentity getUserClientIdentity(String userTid) {
		
		LdapQuery query = LdapQueryBuilder.query().base(ssoLdapBase).where("uid").is(userTid);
		
		List<ClientIdentity> identities = ssoLdapTemplate.search(query, new AttributesMapper<ClientIdentity>() {
			
			@Override
			public ClientIdentity mapFromAttributes(Attributes attrs) throws NamingException {

				ClientIdentity identity = new ClientIdentity();
				
				identity.setPrincipal(getAttributeValue(attrs, "telusKnowbilityUserName"));
				identity.setCredential(getAttributeValue(attrs, "telusKnowbilityPassword"));
				 
				return identity;
			}
		});
		
		return identities.isEmpty() ? null : identities.get(0);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.identity.ClientIdentityManager#getClientIdentity(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ClientIdentity getClientIdentity(String principal, String credential, String applicationCode) {
		return new ClientIdentity(principal, credential, applicationCode);
	}

	private String getChildNodeValue(Name base, String cn, DirContext ctx) throws NamingException {
		Attributes attributes = ctx.getAttributes(LdapUtils.newLdapName(base).add( new Rdn("cn", cn)), new String[] {VALUE_ATTR});
		return getAttributeValue(attributes, VALUE_ATTR);
	}
	
	private String getAttributeValue(Attributes attributes, String name) throws NamingException {
		Attribute attribute = attributes.get(name);
		return attribute == null ? null : (String) attribute.get();
	}

	private synchronized String decrypt(String src) throws Exception {
		if (StringUtils.isNotEmpty(src)) {
			byte[] decryptedBytes = cipher.doFinal(new BigInteger(src, 16).toByteArray());
			return new String(decryptedBytes, "UTF-8");
		}
		return src;
	}
	
}
