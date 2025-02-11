package com.telus.cmb.support.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.crypto.Cipher;

public class IdentityPasswordEncryptor {

	private static File PROPERTIES_FILE = new File("src/main/resources/keystorefile.properties");
	
	private static String KEYSTORE_PATH;
	private static String KEYSTORE_PASSWORD;
	private static String CREDENTIALS_TEXT;
	private static String IDENTITY_KEY_ALIAS = "figaro";
	private static String IDENTITY_KEY_PASS = "mobility";
	private static String PWD = "-7b03d1986b1f3eced7e209d3152353b94f9ee781b099e7e26091f7e890a40329b4eed18ca1c11cd6aa892d53fe9a85f8e84bcca9132716f7ae010f807aa275afda402052d062997f1749cf81f58851063135121b114041232e82adf22df4abd844a1994e05e495c8335eac4e91ca7d38d57f8fb451063fcbd836d25a1881ed4a";
	
	public IdentityPasswordEncryptor() throws Exception {
		readPropertiesFile();
	}

	public String encrypt(String input) {
		
		if (input == null) {
			throw new NullPointerException("[encrypt] - input parameter cannot be null");
		}

		String encryptedString = null;
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(KEYSTORE_PATH), KEYSTORE_PASSWORD.toCharArray());
			X509Certificate cert = (X509Certificate) ks.getCertificate(IDENTITY_KEY_ALIAS);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());
			byte[] encryptedBytes = cipher.doFinal(input.getBytes());
			encryptedString = new BigInteger(encryptedBytes).toString(16);
			
		} catch (Exception e) {
			System.out.println("Exception caught: " + e.getMessage());
		}

		return encryptedString;
	}

	public String decrypt(String input) {

		if (input == null) {
			throw new NullPointerException("[decrypt] - input parameter cannot be null");
		}

		String decryptedString = null;
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(KEYSTORE_PATH), KEYSTORE_PASSWORD.toCharArray());
			PrivateKey privateKey = (PrivateKey) ks.getKey(IDENTITY_KEY_ALIAS, IDENTITY_KEY_PASS.toCharArray());
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decryptedBytes = cipher.doFinal(new BigInteger(input, 16).toByteArray());
			decryptedString = new String(decryptedBytes, "UTF-8");
			
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("[decrypt] - input parameter must be a HEX string");
		} catch (Exception e) {
			System.out.println("Exception caught: " + e.getMessage());
		}

		return decryptedString;
	}
	
	private static void readPropertiesFile() throws IOException {
		
		System.out.println("Reading properties file at: [" + PROPERTIES_FILE.getAbsolutePath() + "]");
		if (PROPERTIES_FILE.exists()) {
			Properties properties = new Properties();
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(PROPERTIES_FILE);
				properties.load(fileInputStream);
				System.out.println("Properties key set: " + properties.keySet());
				KEYSTORE_PATH = properties.getProperty("keystorefile.path");
				KEYSTORE_PASSWORD = properties.getProperty("keystore.pass");
				CREDENTIALS_TEXT = properties.getProperty("credentials.text");
				System.out.println("Finished reading properties file at: [" + PROPERTIES_FILE.getAbsolutePath() + "]");
				
			} finally {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			}
		} else {
			System.out.println("Properties file not found at specified location: [" + PROPERTIES_FILE.getAbsolutePath() + "]");
		}
	}
	
	// Static main method for standalone execution - this relies on the properties file for the credentials text
	public static void main(String[] args) {
		try {
			IdentityPasswordEncryptor ipe = new IdentityPasswordEncryptor();
			String clearText = "vNaG4mEb";
			String encryptedText = ipe.encrypt(clearText);
			//System.out.println("Before encryption: [" + clearText + "]");
			//System.out.println("After encryption: [" + encryptedText + "]");
			//String decryptedText = ipe.decrypt(encryptedText);
			
			
			
			System.out.println("Before decryption: [" + PWD + "]");
			String decryptedText = ipe.decrypt(PWD);
			System.out.println("After decryption: [" + decryptedText + "]");
			
		} catch (Exception e) {
			System.out.println("Exception caught: " + e.getMessage());
		}
	}
	
}