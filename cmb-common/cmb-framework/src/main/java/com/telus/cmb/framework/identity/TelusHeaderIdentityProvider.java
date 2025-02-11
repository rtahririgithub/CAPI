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

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.developer.JAXWSProperties;
import com.telus.cmb.framework.util.SystemRuntimeException;

/**
 * @author Pavel Simonovsky
 *
 */
public class TelusHeaderIdentityProvider extends TelusSamlIdentityProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(TelusHeaderIdentityProvider.class);
	
	private static final String TAG_APPLICATION_CODE = "AppID";
	
	private static final String ERROR_CODE = "ERROR_003";

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.identity.ClientIdentityProvider#getClientIdentity(javax.xml.ws.WebServiceContext, com.telus.cmb.common.identity.ClientIdentityManager)
	 */
	@Override
	public ClientIdentity getClientIdentity(WebServiceContext context) {
		
		IdentityContext identityContext = extractIdentityContext(context);
		
		logger.debug(identityContext.toString());
		
		//now try to get application identity
		ClientIdentity appIdentity =  null;
		
		if (identityContext.getAppId()!=null ) {
			
			//use appId from JAAS subject first
			
			appIdentity = getIdentityManager().getApplicationClientIdentity(identityContext.getAppId());
			if (appIdentity == null) {
				throw new SystemRuntimeException(ERROR_CODE, String.format("Unable to create identity for application ID [%s]", identityContext.getAppId()));
			} 
		} else if (identityContext.getTelusHeaderAppId()!=null ) {
			
			//if JAAS subject does not have appId, try telusHeader
			
			appIdentity = getIdentityManager().getApplicationClientIdentity( identityContext.getTelusHeaderAppId() );
			if (appIdentity==null) {
				throw new SystemRuntimeException(ERROR_CODE, String.format("Unable to create identity for application ID [%s]", identityContext.getTelusHeaderAppId()));
			} 
		}  else {
			throw new SystemRuntimeException(ERROR_CODE, "No application identity info found from message context");
		}
		
		ClientIdentity userIdentity = null;
		if (!Boolean.TRUE.equals(context.getMessageContext().get(USE_APP_IDENTITY))) {
			userIdentity = getClientIdentityByTID(context);
		} 
		
		if ( userIdentity!=null ) {
			//the identity retrieved by TID, does not contain application info, and it is from cache, 
			//we shall clone it and set the application code.
			userIdentity = (ClientIdentity) userIdentity.clone();
			userIdentity.setApplication(appIdentity.getApplication());
		} else {
			userIdentity = appIdentity;
		}
		
		return userIdentity;
	}
	
	public ClientIdentity getClientIdentityByTID(WebServiceContext context) {
		
		ClientIdentity identity = null;

		String tid = (String) context.getMessageContext().get(TELUS_TID); 
		
		logger.debug("TID in messageContext: {}", tid);

		if (StringUtils.isNotEmpty(tid)) {
			identity = getIdentityManager().getUserClientIdentity(tid);
			if (identity == null) {
				// if unable to find KB credential mapping, error out.
				throw new SystemRuntimeException(ERROR_CODE, String.format("Unable to create identity for T-ID [%s]", tid));
			}
		}
		return identity;
	}
	
	public IdentityContext extractIdentityContext( WebServiceContext context ) {
		
		IdentityContext	identityContext = super.extractIdentityContext(context);
		
		if ( identityContext.getAppId()==null) {
			//if we already have appId from JAAS subject, then no need to extract appId from TelusHeader, 
			//this will eliminate the unnecessary overhead associates to TelusHeader parsing process.  
			try {
				identityContext.setTelusHeaderAppId( extractAppCodeFromTelusHeader(context) );
			} catch (Exception e) {
				logger.warn(e.getMessage(), e );
			}
		}

		return identityContext;
	}
	
	private String extractAppCodeFromTelusHeader( WebServiceContext context ) throws Exception {
		
		MessageContext messageContext = context.getMessageContext();
		HeaderList headers = (HeaderList) messageContext.get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
		Header header = headers.get("http://schemas.telus.com/integration/telusheader_v1", "telusHeader", true);
		IdentityHandler identityHandler = new IdentityHandler();			
		String errorMessage = null;
		
		if (header != null) {
			header.writeTo(identityHandler, identityHandler);
			String applicationCode = identityHandler.getApplicationCode();
			if (applicationCode != null) {
				return applicationCode;
			} else {
				errorMessage = "AppId node does not exist";
			}
		} else {
			errorMessage = "TelusHeader does not exist";
		}
		
		if (StringUtils.isNotEmpty(errorMessage)) {
			logger.debug(errorMessage);
		}

		return null;
	}
	
	/**
	 * SAX handler implementation is used to extract application code
	 * from Telus security header in order to make code independent from
	 * XMLStreamReader implementation.
	 *
	 */
	private class IdentityHandler extends DefaultHandler {
		
		private StringBuffer buffer = null;
		
		private String applicationCode = null;
		
		public String getApplicationCode() {
			return applicationCode;
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (localName.equals(TAG_APPLICATION_CODE)) {
				buffer = new StringBuffer();
			}
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (buffer != null) {
				buffer.append(ch, start, length);
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (localName.equals(TAG_APPLICATION_CODE)) {
				applicationCode = buffer.toString().trim();
				buffer = null;
			}
		}
	}
}
