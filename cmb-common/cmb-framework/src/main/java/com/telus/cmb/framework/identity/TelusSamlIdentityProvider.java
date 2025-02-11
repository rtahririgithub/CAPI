/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.identity;

import java.security.Principal;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weblogic.security.SimplePrincipal;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.developer.JAXWSProperties;

/**
 * @author Pavel Simonovsky
 *
 */
public class TelusSamlIdentityProvider implements ClientIdentityProvider {

	private static final Log logger = LogFactory.getLog(TelusSamlIdentityProvider.class);

	private static final String DOMAIN_SOA_APPLICATION_IDENTITY = "SOA Application Identity";
	private static final String DOMAIN_BT_USER_DOMAIN = "BT User Domain";
	
	private ClientIdentityManager identityManager;
	
	public void setIdentityManager(ClientIdentityManager identityManager) {
		this.identityManager = identityManager;
	}
	
	protected ClientIdentityManager getIdentityManager() {
		return identityManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.identity.ClientIdentityProvider#getClientIdentity(javax.xml.ws.WebServiceContext, com.telus.cmb.common.identity.ClientIdentityManager)
	 */
	@Override
	public ClientIdentity getClientIdentity(WebServiceContext context) {
		ClientIdentity identity = null;
		
		Principal principal  = context.getUserPrincipal();

		logger.info("Principal = [" + principal + "]");
		
		if (principal != null) {
			String principalName = principal.getName();
			identity = identityManager.getApplicationClientIdentity(principalName);
		}
		return identity;
	}

	@Override
	public IdentityContext extractIdentityContext( WebServiceContext context ) {
		
		IdentityContext	identityContext = new IdentityContext();

		// the saml header is used for development tests only to simulate SOA principal injection
		String samlHeaderPrincipal = getHeaderAttribute("http://schemas.telus.com/integration/samlheader_v1", "samlHeader", "principal", context);

		Principal principal = null;
		
		if (StringUtils.isNotEmpty(samlHeaderPrincipal)) {
			principal = new SimplePrincipal(samlHeaderPrincipal);
		} else {
			principal  = context.getUserPrincipal();
		}
		
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
	
	/**
	 * Returns a value of the header attribute from the given web service context
	 * 
	 * @param namespaceUri
	 * @param localName
	 * @param attributeName
	 * @param context
	 * @return
	 */
	protected String getHeaderAttribute(String namespaceUri, String localName, String attributeName, WebServiceContext context) {
		HeaderList headers = (HeaderList) context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
		if (headers != null) {
			Header header = headers.get(namespaceUri, localName, true);
			if (header != null) {
				return header.getAttribute("", attributeName);
			}
		}
		return null;
	}
	
}
