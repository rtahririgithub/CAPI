/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.identity;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;

import javax.crypto.Cipher;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.config.Configuration;
import com.telus.api.config.ConfigurationManager;
import com.telus.api.config.UnknownConfigurationException;
import com.telus.cmb.common.cache.DataEntryCache;

/**
 * @author Pavel Simonovsky
 *
 */
public class ClientIdentityManagerLdapImpl implements ClientIdentityManager {

	private static final Log logger = LogFactory.getLog(ClientIdentityManagerLdapImpl.class);
	
    private static String KEYSTORE_RESOURCE = "/identityKeystore.jks";

    private static String KEYSTORE_PASSWORD = "weblogic";

    private static String IDENTITY_KEY_ALIAS = "figaro";
    
    private static String IDENTITY_KEY_PASSWORD = "weblogic";
	
	private static final String CACHE_CONFIG_RESOURCE = "/identityCacheManager.xml";
	
	private static final String[] LDAP_IDENTITY_ROOT = {"Infra", "ClientAgent"};
	
	private Cipher cipher;
	
	private DataEntryCache<ClientIdentity> cache;
	
	private CacheManager cacheManager;
	
	private ClientIdentityCorporateLdapDao corpLdapDao;
	private DataEntryCache<ClientIdentity> tidCache;
	
	public ClientIdentityManagerLdapImpl() {
		this ( null );
	}
	
	public ClientIdentityManagerLdapImpl(ClientIdentityCorporateLdapDao dao ) {
		
		// cache initialization
		
		URL url = getClass().getResource(CACHE_CONFIG_RESOURCE);
		cacheManager = CacheManager.create(url);
		cache = new DataEntryCache<ClientIdentity>("clientIdentities", cacheManager);
		
		if ( dao !=null ) {
			tidCache =  new DataEntryCache<ClientIdentity>("tidMapping", cacheManager);
			this.corpLdapDao = dao;
		}
		
		
		// cryptographic cipher initialization
		
		try {

			KeyStore keystore = KeyStore.getInstance("JKS");
			InputStream stream = getClass().getResourceAsStream(KEYSTORE_RESOURCE);
			keystore.load(stream, KEYSTORE_PASSWORD.toCharArray());
			PrivateKey privateKey = (PrivateKey) keystore.getKey(IDENTITY_KEY_ALIAS, IDENTITY_KEY_PASSWORD.toCharArray());

			logger.debug("Private key in use: [" + privateKey + "]");
			
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			
		} catch (Exception e) {
			throw new RuntimeException("Error configuring client identity manager: " + e.getMessage(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.common.identity.ClientIdentityManager#getClientIdentity(java.lang.String)
	 */
	@Override
	public ClientIdentity getClientIdentity(String applicationCode) {
		
		// use only last segment of application code
		
		int lastIndex = applicationCode.lastIndexOf('\\');
		if (lastIndex != -1) {
			applicationCode = applicationCode.substring(lastIndex + 1);
		}
		
		return cache.get(applicationCode, new CacheEntryFactory() {
			
			@Override
			public Object createEntry(Object key) throws Exception {
				
				logger.debug("Loading client identity for application code [" + key + "]");
				
				String[] identityPath = new String[3];
				
				identityPath[0] = LDAP_IDENTITY_ROOT[0];
				identityPath[1] = LDAP_IDENTITY_ROOT[1];
				identityPath[2] = (String) key;
				
				ConfigurationManager configurationManager = null;
				try {
					configurationManager = ConfigurationManager.getInstance();
					Configuration config = configurationManager.lookup(identityPath);

					ClientIdentity identity = new ClientIdentity();
					
					identity.setPrincipal(config.getPropertyAsString("principal").trim());
					identity.setCredential(decrypt(config.getPropertyAsString("credential").trim()));
					identity.setApplication(config.getPropertyAsString("applicationCode").trim());

					return identity;
					
				} catch (UnknownConfigurationException configurationException) {
					logger.debug("Configuration not found for code [" + key + "]");
				} finally {
					if (configurationManager != null) {
						configurationManager.destroy();
					}
				}

				return null;
			}
		});
	}
	
	private synchronized String decrypt(String src) throws Exception {
		byte[] decryptedBytes = cipher.doFinal(new BigInteger(src, 16).toByteArray());
		return new String(decryptedBytes, "UTF-8");
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.common.identity.ClientIdentityManager#getClientIdentity(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ClientIdentity getClientIdentity(String principal, String credential, String applicatonCode) {
		return new ClientIdentity(principal, credential, applicatonCode);
	}


	/* (non-Javadoc)
	 * @see com.telus.cmb.common.identity.ClientIdentityManager#getClientIdentityByTid(java.lang.String)
	 */
	@Override
	public ClientIdentity getClientIdentityByTID(String telusTID) {
		
		if ( corpLdapDao==null ) throw new UnsupportedOperationException( "Corporate LDAP DAO is null." );
		
		ClientIdentity identityInfo =  tidCache.get(telusTID, new CacheEntryFactory() {
			
			@Override
			public Object createEntry(Object key) throws Exception {
				
				logger.debug("Loading client identity for telusTID [" + key + "]");
				return corpLdapDao.getClientIdentityByTID((String)key);
			}
		});
		
		return identityInfo;
	}
}
