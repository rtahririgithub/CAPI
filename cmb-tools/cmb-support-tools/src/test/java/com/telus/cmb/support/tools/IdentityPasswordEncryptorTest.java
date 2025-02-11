package com.telus.cmb.support.tools;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author R. Fong
 *
 */
public class IdentityPasswordEncryptorTest {
	
	private IdentityPasswordEncryptor ipe;
	
	@BeforeTest
	public void initialize() throws Exception {
		ipe = new IdentityPasswordEncryptor();
	}

	@Test
	public void testEncrypt() throws Exception {		
		System.out.println("Running testEncrypt test case...");
		String input = "test123";
		System.out.println("Input text: [" + input + "]");
		String encryptedText = ipe.encrypt(input);
		System.out.println("Encrypted text: [" + encryptedText + "]");
	}
	
	@Test
	public void testDecrypt() throws Exception {		
		System.out.println("Running testDecrypt test case...");
		String input = "5a994a69d648f3cdf2c2953f4056de28690e63dba5779d10789ebb6fd37181d9e267f816d3677a77df046c82e78d951bf8ad323476da4bcafb9a1e05d73da943855139f9537fcd889e7fb3d727cb95ddc8b3f0ccd42fff2c1739bd6011b64dd3b8d503dfd8eb16cb5bd3692b9d7fd3b0e2049257ef9a7b3366597bf6018bf057";
		System.out.println("Input text: [" + input + "]");
		String decryptedText = ipe.decrypt(input);
		System.out.println("Decrypted text: [" + decryptedText + "]");
	}
	
}