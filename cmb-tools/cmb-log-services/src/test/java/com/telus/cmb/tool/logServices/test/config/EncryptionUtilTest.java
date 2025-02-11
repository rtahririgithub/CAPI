package com.telus.cmb.tool.logServices.test.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.telus.cmb.tool.services.log.utils.EncryptionUtil;

public class EncryptionUtilTest {

	@Test
	public void test_encrypt_decrypt() {
		String user = "x1234567";
		String password = "Passw0rd";
		String encrypted = EncryptionUtil.encryptPassword(user, password);
		String decrypted = EncryptionUtil.decryptPassword(user, encrypted);
		System.out.println("username=" + user + "\npassword=" + password + "\nencrypted=" + encrypted + "\ndecrypted=" + decrypted);
		Assert.assertEquals(password, decrypted);
		Assert.assertNotEquals(password, encrypted);
	}

	@Test
	public void test_date() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("2019-07-30Z");
		System.out.println(sdf.format(date));
	}
	
}
