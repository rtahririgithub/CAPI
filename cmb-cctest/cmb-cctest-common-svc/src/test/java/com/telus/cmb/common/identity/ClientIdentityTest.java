package com.telus.cmb.common.identity;

import org.junit.Test;

public class ClientIdentityTest {

	@Test
	public void testDecrypt() throws Throwable {
		ClientIdentityUtil ciUtil = new ClientIdentityUtil();
		ClientIdentity ci = ciUtil.decrypt("c6qQhpyvYwJJ68Mk3iEMlY7u2qTX8NsELVi+Le+/NXQ=");
		System.out.println(ci);
	}

	@Test
	public void testEncrypt() throws Throwable {
		ClientIdentityUtil ciUtil = new ClientIdentityUtil();
		ClientIdentity ci = new ClientIdentity("22297", "test", "cpms");
		String sessionId = ciUtil.encrypt(ci);
		System.out.println(sessionId);
	}
}
