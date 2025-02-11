package com.telus.cmb.tool.services.log.service;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IdentityEncryptionServiceImpl implements IdentityEncryptionService {

	private static String IDENTITY_KEY_ALIAS = "figaro";
	private static String IDENTITY_KEY_PASS = "mobility";
	private static Logger logger = Logger.getLogger(IdentityEncryptionServiceImpl.class);
	
	@Value("${keystorefile.path}")
	private String keystorePath;
	
	@Value("${keystore.pass}")
	private String keystorePassword;
	
	@Override
	public String encrypt(String input) throws Exception {

		String encryptedString = null;		
		if (input != null) {
			try {
				KeyStore ks = KeyStore.getInstance("JKS");
				ks.load(new FileInputStream(getKeystorePath()), keystorePassword.toCharArray());
				X509Certificate cert = (X509Certificate) ks.getCertificate(IDENTITY_KEY_ALIAS);
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());
				byte[] encryptedBytes = cipher.doFinal(input.getBytes());
				encryptedString = new BigInteger(encryptedBytes).toString(16);	
			} catch (Exception e) {
				logger.error("Exception caught: " + e.getMessage());
			}
		} else {
			logger.info("[encrypt] - input parameter cannot be null");
		}

		return encryptedString;
	}

	@Override
	public String decrypt(String input) throws Exception {

		String decryptedString = null;
		if (input != null) {
			try {
				KeyStore ks = KeyStore.getInstance("JKS");
				ks.load(new FileInputStream(getKeystorePath()), keystorePassword.toCharArray());
				PrivateKey privateKey = (PrivateKey) ks.getKey(IDENTITY_KEY_ALIAS, IDENTITY_KEY_PASS.toCharArray());
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
				byte[] decryptedBytes = cipher.doFinal(new BigInteger(input, 16).toByteArray());
				decryptedString = new String(decryptedBytes, "UTF-8");
			} catch (NumberFormatException nfe) {
				logger.error("Input is not a HEX string: " + nfe.getMessage());
				throw new IllegalArgumentException("[decrypt] - input parameter must be a HEX string");
			} catch (Exception e) {
				logger.error("Exception caught: " + e.getMessage());
			}
		} else {
			logger.info("[decrypt] - input parameter cannot be null");			
		}

		return decryptedString;
	}
	
	private String getKeystorePath() {
		return getClass().getClassLoader().getResource(keystorePath).getPath().replace("%20", " ");
	}

}