package com.telus.cmb.tool.logServices.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.telus.cmb.tool.services.log.service.IdentityEncryptionService;

@Test
@ContextConfiguration("classpath:application-context-test.xml")
public class IdentityEncryptionServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	IdentityEncryptionService identityEncryptionService;
	
	private static final String CLEARTEXT = "telus123";
	private static final String ENCRYPTED = "-7b03d1986b1f3eced7e209d3152353b94f9ee781b099e7e26091f7e890a40329b4eed18ca1c11cd6aa892d53fe9a85f8e84bcca9132716f7ae010f807aa275afda402052d062997f1749cf81f58851063135121b114041232e82adf22df4abd844a1994e05e495c8335eac4e91ca7d38d57f8fb451063fcbd836d25a1881ed4a";
	
	@Test
	public void test_encrypt() throws Exception {
		String encrypted = identityEncryptionService.encrypt(CLEARTEXT);
		String decrypted = identityEncryptionService.decrypt(encrypted);
		Assert.assertEquals(decrypted, CLEARTEXT);
	}

	@Test
	public void test_decrypt() throws Exception {	
		String decrypted = identityEncryptionService.decrypt(ENCRYPTED);
		Assert.assertEquals(decrypted, CLEARTEXT);
	}
	
}
