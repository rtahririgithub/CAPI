package com.telus.cmb.account.payment;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.log4j.Logger;

import com.telus.api.account.AuditHeader;

/**
 * This class maps AuditHeader to Avalon's UserHeader in the SOAPHeader
 */
public class AuditInfoSOAPHeaderBuilder extends GenericHandler {

	private static final Logger LOGGER = Logger.getLogger(AuditInfoSOAPHeaderBuilder.class);
	private static final String AVALON_COMMON_NS_URI = "http://schemas.telus.com/avalon/common/v1_0";
	private static final String AVALON_NS_PREFIX = "acv1";
	private static ThreadLocal<AuditHeader> auditHeaderHolder = new ThreadLocal<AuditHeader>();

	/**
	 * Return AuditHeader that being associated to this thread, and clear out the place holder.
	 * @return
	 */
	private AuditHeader getAuditHeader() {
		AuditHeader auditHeader = (AuditHeader) auditHeaderHolder.get();
		auditHeaderHolder.set(null);
		return auditHeader;
	}

	/**
	 * associate the given AuditHeader instance to current Thread, so that this SOAP handler can map it to SOAPHeader 
	 * @param auditHeader
	 */
	public static void setAuditHeader(AuditHeader auditHeader) {
		auditHeaderHolder.set(auditHeader);
	}

	public QName[] getHeaders() {
		return null;
	}

	public boolean handleRequest(MessageContext context) {
		try {

			SOAPMessageContext soapCtx = (SOAPMessageContext) context;
			SOAPMessage soapMessage = soapCtx.getMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();
			SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

			populateAvalonUserHeader(soapEnvelope, getAuditHeader());
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(),e);
		}

		return true;
	}
	
	private void populateAvalonUserHeader(SOAPEnvelope soapEnv, AuditHeader auditHeader) {

		try {
			if (auditHeader==null ) return ;
			
			//javax.xml.soap.Name name = soapEnv.createName( "UserHeader");
			javax.xml.soap.Name name = soapEnv.createName("UserHeader", AVALON_NS_PREFIX, AVALON_COMMON_NS_URI);
			SOAPHeaderElement userHeaderElement = soapEnv.getHeader().addHeaderElement(name);
			
			if (auditHeader.getCustomerId()!=null) 
				userHeaderElement.addChildElement("custId", AVALON_NS_PREFIX).addTextNode(auditHeader.getCustomerId());
			if( auditHeader.getUserIPAddress()!=null) 
				userHeaderElement.addChildElement("ipAddress", AVALON_NS_PREFIX).addTextNode( formatIP( auditHeader.getUserIPAddress() ));
			
			if (auditHeader.getAppInfos() != null) {
				AuditHeader.AppInfo[] appInfos = auditHeader.getAppInfos(); 
				for (int i = 0; i < appInfos.length; i++) {
					AuditHeader.AppInfo appInfo = appInfos[i];
					SOAPElement appInfoElement = userHeaderElement.addChildElement("appInfo",AVALON_NS_PREFIX);
					
					appInfoElement.addChildElement("userId", AVALON_NS_PREFIX).addTextNode(appInfo.getUserId());
					appInfoElement.addChildElement("applicationId", AVALON_NS_PREFIX).addTextNode( String.valueOf(appInfo.getApplicationId()));
					appInfoElement.addChildElement("ipAddress", AVALON_NS_PREFIX).addTextNode( formatIP(appInfo.getIPAddress()) );
				}
			}
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	
	private String formatIP( String ipAddress ) {
		//TODO do we need to make sure the IP address follows pattern xxx.xxx.xxx.xxx as being specified in AvalonCommonSchema_v1_0.xsd?
		return ipAddress;
	}
}