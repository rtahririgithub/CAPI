package com.telus.cmb.tool.services.log.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.telus.cmb.tool.services.log.controllers.LogFileReadController;

public class EncryptionUtil {
	
	private static Logger logger = Logger.getLogger(LogFileReadController.class);
	
	public static String encryptPassword(String user, String pass) {
		try {
			Key aesKey = new SecretKeySpec(getBytesFromHexString(user), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
	        byte[] encrypted = cipher.doFinal(pass.getBytes());       
	        return Base64.encodeBase64String(encrypted);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return pass;
		}
	}
	
	public static String decryptPassword(String user, String encrypted) {
		try {
			Key aesKey = new SecretKeySpec(getBytesFromHexString(user), "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.DECRYPT_MODE, aesKey);
	        return new String(cipher.doFinal(Base64.decodeBase64(encrypted.getBytes())));
		} catch (Exception e) {
			logger.info(e.getMessage());
			return encrypted;
		}
	}
	
	public static byte[] getBytesFromHexString(String s) {
		
		String padded = StringUtils.leftPad(s, 32);
	    int len = padded.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(padded.charAt(i), 16) << 4)
	                             + Character.digit(padded.charAt(i+1), 16));
	    }
	    
	    return data;
	}
	
}
