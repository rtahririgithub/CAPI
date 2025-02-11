/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.service;

import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.telus.cmsc.domain.artifact.Environment;
import com.telus.cmsc.domain.identity.ApplicationIdentity;
import com.telus.cmsc.domain.identity.ApplicationIdentityEntry;
import com.telus.cmsc.domain.identity.ConfigDocument;
import com.telus.cmsc.domain.identity.ConfigDocumentReader;
import com.telus.cmsc.domain.identity.ConfigDocumentWriter;
import com.telus.cmsc.domain.identity.ConfigEntry;

/**
 * @author Pavel Simonovsky	
 *
 */

public class ApplicationIdentityManagementServiceImpl implements ApplicationIdentityManagementService {

	private Resource keystoreResource;
	
	private String keyStorePassword;

	private static String IDENTITY_KEY_ALIAS = "figaro";
	private static String IDENTITY_KEY_PASS = "mobility";

	@Autowired
	private EnvironmentService environmentService;
	
	private KeyStore keystore;

	public void setKeystore(Resource keystoreResource) {
		this.keystoreResource = keystoreResource;
	}
	
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	
	@PostConstruct
	public void init() {
		try {

			keystore = KeyStore.getInstance("JKS");
			keystore.load(keystoreResource.getInputStream(), keyStorePassword.toCharArray());
			
		} catch (Exception e) {
			throw new RuntimeException("Error loading keystore: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.modules.identity.service.ApplicationIdentityManagementService#encrypt(com.telus.cmbsc.modules.identity.domain.ApplicationIdentity)
	 */
	@Override
	public String encrypt(ApplicationIdentity identity) {
		
		ConfigEntry rootEntry = new ConfigEntry(identity.getApplicationKey());
		rootEntry.setDescription(identity.getDescription());
		
		ConfigEntry appCodeEntry = new ConfigEntry("applicationCode");
		appCodeEntry.setValue(identity.getApplicationCode());
		
		ConfigEntry principalEntry = new ConfigEntry("principal");
		ConfigEntry credentialEntry = new ConfigEntry("credential");
		
		for (ApplicationIdentityEntry identityEntry : identity.getEntries()) {
			
			String principal = identityEntry.getPrincipal();
			String environmentCode = identityEntry.getEnvironment().getConfigCode();
			
			if (StringUtils.isNotEmpty(principal)) {
				principalEntry.addValue(environmentCode, principal);
				credentialEntry.addValue(environmentCode, encryptValue(identityEntry.getDecryptedCredentials()));
			}
		}
		
		rootEntry.addChild(appCodeEntry);
		rootEntry.addChild(principalEntry);
		rootEntry.addChild(credentialEntry);
		
		ConfigDocument document = new ConfigDocument();
		document.setRootEntry(rootEntry);
		
		StringWriter writer = new StringWriter();
		ConfigDocumentWriter documentWriter = new ConfigDocumentWriter();
		documentWriter.write(document, writer);
		
		return writer.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.modules.identity.service.ApplicationIdentityManagementService#decrypt(java.lang.String)
	 */
	@Override
	public ApplicationIdentity decrypt(String source) {
		
		ConfigDocumentReader reader = new ConfigDocumentReader();
		ConfigDocument document = reader.read(source);
		
		ConfigEntry rootEntry = document.getRootEntry();
		
		ApplicationIdentity identity = new ApplicationIdentity();
		
		identity.setApplicationKey(rootEntry.getName());
		identity.setDescription(rootEntry.getDescription());
		
		ConfigEntry appCodeEntry = rootEntry.getChild("applicationCode");
		if (appCodeEntry != null) {
			identity.setApplicationCode(appCodeEntry.getValue());
		}
		
		ConfigEntry principalEntry = rootEntry.getChild("principal");
		ConfigEntry credentialEntry = rootEntry.getChild("credential");
		
		for (Environment environment : environmentService.getEnvironments()) {
			if (!environment.isFlipperMember()) {
				
				String configCode = environment.getConfigCode();
				
				ApplicationIdentityEntry identityEntry = new ApplicationIdentityEntry();
				identityEntry.setEnvironment(environment);
				
				if (principalEntry != null) {
					identityEntry.setPrincipal(principalEntry.getValue(configCode));
				}
				
				if (credentialEntry != null) {
					String value = credentialEntry.getValue(configCode);
					identityEntry.setDecryptedCredentials(decryptValue(value));
				}
				
				identity.addEntry(identityEntry);
			}
		}
		
		return identity;
	}	
	
	private String decryptValue(String source) {
		if (StringUtils.isNotEmpty(source)) {
			try {
				
				PrivateKey privateKey = (PrivateKey) keystore.getKey(IDENTITY_KEY_ALIAS, IDENTITY_KEY_PASS.toCharArray());
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
				byte[] decryptedBytes = cipher.doFinal(new BigInteger(source, 16).toByteArray());
				return new String(decryptedBytes, "UTF-8");
				
			} catch (Exception e) {
				throw new RuntimeException("Error decrypting content: " + e.getMessage(), e);
			}
		}
		return source;
	}
	
	public String encryptValue(String source) {
		try {
			
			X509Certificate cert = (X509Certificate) keystore.getCertificate(IDENTITY_KEY_ALIAS);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());
			byte[] encryptedBytes = cipher.doFinal(source.getBytes());
			return new BigInteger(encryptedBytes).toString(16);
			
		} catch (Exception e) {
			throw new RuntimeException("Error encrypting content: " + e.getMessage(), e);
		}
	}
}
