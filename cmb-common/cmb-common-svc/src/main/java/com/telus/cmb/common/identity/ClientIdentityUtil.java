package com.telus.cmb.common.identity;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ClientIdentityUtil {
	
	private static final byte[] key = "ClientAPI2012EJB".getBytes();
	
	private static final String DELIMITER = ";;";
	
	private Cipher enCipher = null; 
	private Cipher deCipher = null; 
	
	public ClientIdentityUtil() throws Exception {
		
		//initiate two cipher instances: one for encryption one for decryption, both use same key.
		enCipher = Cipher.getInstance("AES");			
		enCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES") );

		deCipher = Cipher.getInstance("AES");			
		deCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES") );
	}
	
	public String encrypt( ClientIdentity clientIdentity ) throws Throwable {
		
		String clearText  = clientIdentity.getPrincipal() + DELIMITER 
			+ clientIdentity.getCredential() +DELIMITER + clientIdentity.getApplication();
		
		byte[] encryptedValue = null; 
		synchronized ( enCipher ) {
			encryptedValue = enCipher.doFinal(clearText.getBytes());
		}
		return new org.apache.commons.codec.binary.Base64().encodeToString(encryptedValue);
	}
	
	public ClientIdentity decrypt (String encryptedText) throws Throwable {
		
		byte[] encryptedValue = new org.apache.commons.codec.binary.Base64().decode(encryptedText);
		byte[] decryptedValue = null;

		synchronized ( deCipher ) {
			decryptedValue = deCipher.doFinal(encryptedValue);
		}
		String decryptedText = new String( decryptedValue, "UTF-8");
		
		return parseClientIdentity(decryptedText);
		
	}

	public ClientIdentity parseClientIdentity(String decryptedText) {

		ClientIdentity clientIdentity = new ClientIdentity();
		String[] parts = decryptedText.split( DELIMITER );
		
		if ( parts.length!= 3 ) {
			throw new RuntimeException( "Unable to parse ClientIdentity from [" + decryptedText + "]"   );
		}
		
		clientIdentity.setPrincipal(parts[0]);
		clientIdentity.setCredential(parts[1]);
		clientIdentity.setApplication(parts[2]);
		
		return clientIdentity;
	}
}
