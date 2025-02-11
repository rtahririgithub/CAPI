package com.telus.cmb.tool.services.log.service;

public interface IdentityEncryptionService {

	public String encrypt(String input) throws Exception;
	
	public String decrypt(String input) throws Exception;
	
}
