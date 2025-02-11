package com.telus.cmb.jws.identity;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.identity.ClientIdentityCorporateLdapDao;

public class ClientIdentityCorporateLdapDaoImpl implements ClientIdentityCorporateLdapDao {
	
	private static final String SEARCH_BASE ="ou=people,ou=teammembers,ou=internal,o=telus";  
	private static final String ATTR_NAME_TID = "uid";
	private static final String ATTR_NAME_KB_USER = "telusKnowbilityUserName";
	private static final String ATTR_NAME_KB_PWD = "telusKnowbilityPassword";

	private LdapTemplate ldapTemplate;
	
	private AttributesMapper kbCredentialMapper = new KBCredientalAttributeMapper();
	
	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
	
	
	public ClientIdentity getClientIdentityByUserDn(String userDn) {
		
		return (ClientIdentity) ldapTemplate.lookup( userDn,kbCredentialMapper);
	}
	
	public ClientIdentity getClientIdentityByTID(String tid) {
		
		String filter = "("+ATTR_NAME_TID+"=" + tid +")"; 
		
		@SuppressWarnings("rawtypes")
		List list = ldapTemplate.search( SEARCH_BASE , 
				filter, 
				kbCredentialMapper 
				);
		
		ClientIdentity clientIdentity = null;
		if ( list.isEmpty()==false ) {
			clientIdentity = (ClientIdentity) list.get(0);
		}
		return clientIdentity;
	}
	
	private class KBCredientalAttributeMapper implements AttributesMapper {

		@Override
		public Object mapFromAttributes(Attributes attributes) throws NamingException {
			ClientIdentity clientIdentity = new ClientIdentity();
			clientIdentity.setPrincipal( (String) attributes.get(ATTR_NAME_KB_USER).get() );
			clientIdentity.setCredential( (String) attributes.get(ATTR_NAME_KB_PWD).get() );
			return clientIdentity;
		}
	}
}
