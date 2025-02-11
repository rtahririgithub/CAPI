package com.telus.cmb.common.identity;


public interface ClientIdentityCorporateLdapDao {
	
	public ClientIdentity getClientIdentityByTID(String tid);
	
	public ClientIdentity getClientIdentityByUserDn(String userDn);

}
