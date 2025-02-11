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

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.developer.JAXWSProperties;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.identity.ClientIdentityManager;
import com.telus.cmb.jws.PolicyException;
import com.telus.cmb.jws.ServiceException;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyFaultInfo;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceFaultInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class TelusHeaderIdentityProvider extends TelusSamlIdentityProvider {
	
	private static final Log logger = LogFactory.getLog(TelusHeaderIdentityProvider.class);
	
	private static final String TAG_APPLICATION_CODE = "AppID";


	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.identity.ClientIdentityProvider#getClientIdentity(javax.xml.ws.WebServiceContext, com.telus.cmb.common.identity.ClientIdentityManager)
	 */
	@Override
	public ClientIdentity getClientIdentity(WebServiceContext context, ClientIdentityManager clientIdentityManager)  throws ServiceException, PolicyException {
		
		IdentityContext identityContext = extractIdentityContext(context);
		
		if (logger.isDebugEnabled() ) logger.debug(identityContext);
		
		//now try to get application identity
		ClientIdentity appIdentity =  null;
		
		if (identityContext.getAppId()!=null ) {
			//use appId from JAAS subject first
			appIdentity = clientIdentityManager.getClientIdentity(identityContext.getAppId());
			if (appIdentity==null) {
				PolicyFaultInfo faultInfo = new PolicyFaultInfo();
				faultInfo.setErrorCode("ERROR_003");
				faultInfo.setErrorMessage("Application identity exception");
				PolicyException policyException = new PolicyException("Unable to create identity for application ID [" + identityContext.getAppId() + "]", faultInfo);
				
				throw policyException;
			} 
		} else if (identityContext.getTelusHeaderAppId()!=null ) {
			//if JAAS subject does not have appId, try telusHeader
			
			appIdentity = clientIdentityManager.getClientIdentity( identityContext.getTelusHeaderAppId() );
			if (appIdentity==null) {
				PolicyFaultInfo faultInfo = new PolicyFaultInfo();
				faultInfo.setErrorCode("ERROR_003");
				faultInfo.setErrorMessage("Application identity exception");
				PolicyException policyException = new PolicyException("Unable to create identity for application ID [" + identityContext.getTelusHeaderAppId() + "]", faultInfo);
				
				throw policyException;
			} 
		}  else {
			//
			PolicyFaultInfo faultInfo = new PolicyFaultInfo();
			faultInfo.setErrorCode("ERROR_003");
			faultInfo.setErrorMessage("Application identity exception");
			PolicyException policyException = new PolicyException("No application identity info found from message context", faultInfo);
			
			throw policyException;
		}
		
		ClientIdentity userIdentity = null;
		if (Boolean.TRUE.equals(context.getMessageContext().get(USE_APP_IDENTITY))==false) {
			userIdentity = getClientIdentityByTID( context, clientIdentityManager );
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
	
	public ClientIdentity getClientIdentityByTID(WebServiceContext context, ClientIdentityManager clientIdentityManager)  throws ServiceException, PolicyException {
		ClientIdentity identity = null;
		String telusTID = (String) context.getMessageContext().get( TELUS_TID ); 
		
		if(logger.isDebugEnabled() ) logger.debug( "telusTID in messageContext: " + telusTID );
		
		if ( telusTID!=null) {
			try {
				identity = clientIdentityManager.getClientIdentityByTID( telusTID );
				if (identity==null) {
					//if unable to find KB credential mapping, error out. 
					String errorMessage = "Unable to create identity for T-ID [" + telusTID + "]";
					PolicyFaultInfo faultInfo = new PolicyFaultInfo();
					faultInfo.setErrorCode("ERROR_003");
					faultInfo.setErrorMessage( errorMessage );
					throw new PolicyException( errorMessage, faultInfo);
				}
			} catch( PolicyException pe ) {
				throw pe;
			} catch (Exception e ) {
				logger.error("Error retrieving clientIdentity for T-ID[: " + telusTID + "]: " + e.getMessage(), e);
				
				ServiceFaultInfo faultInfo = new ServiceFaultInfo();
				faultInfo.setErrorCode("ERROR_004");
				faultInfo.setErrorMessage("Client identity exception");
				throw new ServiceException( e.toString(), faultInfo);
			}
		}
		return identity;
	}
	
/*	ClientIdentity getClientIdentityByAppCode(WebServiceContext context, ClientIdentityManager clientIdentityManager)  throws ServiceException, PolicyException {
		ClientIdentity identity = null;
		try {
			MessageContext messageContext = context.getMessageContext();
			HeaderList headers = (HeaderList) messageContext.get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
			Header header = headers.get("http://schemas.telus.com/integration/telusheader_v1", "telusHeader", true);
			IdentityHandler identityHandler = new IdentityHandler();			
			String errorMessage = null;
			
			if (header != null) {
				header.writeTo(identityHandler, identityHandler);
				String applicationCode = identityHandler.getApplicationCode();
				if (applicationCode != null) {
					identity = clientIdentityManager.getClientIdentity(applicationCode);
					if (identity == null) {
						errorMessage = "Unable to create identity for application ID [" + applicationCode + "]";
					}
				} else {
					errorMessage = "AppId node does not exist";
				}
			} else {
				errorMessage = "TelusHeader does not exist";
			}
			
			if (errorMessage != null) {
				
				PolicyFaultInfo faultInfo = new PolicyFaultInfo();
				faultInfo.setErrorCode("ERROR_003");
				faultInfo.setErrorMessage("Application identity exception");
				PolicyException policyException = new PolicyException(errorMessage, faultInfo);
				
				throw policyException;
			}
		} catch (PolicyException policyException) {
			
			throw policyException;
			
		} catch (Exception e) {
			
			ServiceFaultInfo faultInfo = new ServiceFaultInfo();
			faultInfo.setErrorCode("ERROR_004");
			faultInfo.setErrorMessage("Application identity exception");
			
			logger.error("Error retrieving client identity: " + e.getMessage(), e);
			
			throw new ServiceException("Client identity system error", faultInfo);
		}
		
		return identity;
	}
*/
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
	
	private String extractAppCodeFromTelusHeader( WebServiceContext context ) throws PolicyException, SAXException {
		
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
		
		if (errorMessage != null) {
			
			PolicyFaultInfo faultInfo = new PolicyFaultInfo();
			faultInfo.setErrorCode("ERROR_003");
			faultInfo.setErrorMessage("Application identity exception");
			PolicyException policyException = new PolicyException(errorMessage, faultInfo);
			
			throw policyException;
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
