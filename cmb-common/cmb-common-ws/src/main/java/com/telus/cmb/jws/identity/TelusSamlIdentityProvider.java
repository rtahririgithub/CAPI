/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.identity;

import java.security.Principal;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.identity.ClientIdentityManager;
import com.telus.cmb.jws.PolicyException;
import com.telus.cmb.jws.ServiceException;

/**
 * @author Pavel Simonovsky
 *
 */
public class TelusSamlIdentityProvider implements ClientIdentityProvider {

	private static final Log logger = LogFactory.getLog(TelusSamlIdentityProvider.class);

	private static final String DOMAIN_SOA_APPLICATION_IDENTITY = "SOA Application Identity";
	private static final String DOMAIN_BT_USER_DOMAIN = "BT User Domain";
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.identity.ClientIdentityProvider#getClientIdentity(javax.xml.ws.WebServiceContext, com.telus.cmb.common.identity.ClientIdentityManager)
	 */
	@Override
	public ClientIdentity getClientIdentity(WebServiceContext context, ClientIdentityManager clientIdentityManager)  throws ServiceException, PolicyException {
		ClientIdentity identity = null;
		
		Principal principal  = context.getUserPrincipal();

		logger.info("Principal = [" + principal + "]");
		
		if (principal != null) {
			String principalName = principal.getName();
			identity = clientIdentityManager.getClientIdentity(principalName);
		}
		return identity;
	}

	@Override
	public IdentityContext extractIdentityContext( WebServiceContext context ) {
		
		IdentityContext	identityContext = new IdentityContext();
		
		Principal principal  = context.getUserPrincipal();
		if ( principal!=null) { 
			String principalName = principal.getName();
			identityContext.setPrincipal(principalName);
			
			int i=principalName.lastIndexOf("\\");
			if ( i!=-1) {
				String identityDomain = principalName.substring(0, i);
				if ( DOMAIN_BT_USER_DOMAIN.equalsIgnoreCase(identityDomain) ) {
					
					identityContext.setTelusTID( principalName.substring(i+1) );
					
					context.getMessageContext().put( TELUS_TID, identityContext.getTelusTID() );
					
				} else if (DOMAIN_SOA_APPLICATION_IDENTITY.equalsIgnoreCase(identityDomain) ) {
					identityContext.setAppId( principalName );
				}
			}
		}
		return identityContext;

	}
}
