/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.account.lifecyclefacade.dao.impl;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telus.api.account.AuditHeader;

/**
 * @author Pavel Simonovsky
 *
 */
public class AvalonSoapHandler implements SOAPHandler<SOAPMessageContext>{
	
	private static final Logger logger = LoggerFactory.getLogger(AvalonSoapHandler.class);

	private static final String AVALON_NS = "http://schemas.telus.com/avalon/common/v1_0";
	private static final String AVALON_PREFIX = "acvl";
	
	private ThreadLocal<AuditHeader> auditHeaderHolder = new ThreadLocal<AuditHeader>();

	
	public void setAuditHeader(AuditHeader auditHeader) {
		auditHeaderHolder.set(auditHeader);
	}
	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		
		AuditHeader auditHeader = auditHeaderHolder.get();
		
		logger.debug("Audit Header: {}", auditHeader);
		
		if ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
			try {
				
				SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
				SOAPFactory factory = SOAPFactory.newInstance();
				
				SOAPHeader header = envelope.getHeader();
				if (header == null) {
					header = envelope.addHeader();
				}
				
				SOAPHeaderElement userElement = header.addHeaderElement(factory.createName("UserHeader", AVALON_PREFIX, AVALON_NS));
				
				if (StringUtils.isNotEmpty(auditHeader.getCustomerId())) {
					userElement.addChildElement("custId", AVALON_PREFIX).addTextNode(auditHeader.getCustomerId());
				}
				
				if (StringUtils.isNotEmpty(auditHeader.getUserIPAddress())) {
					userElement.addChildElement("ipAddress", AVALON_PREFIX).addTextNode(auditHeader.getUserIPAddress());
				}
				
				if (auditHeader.getAppInfos() != null) {
					for (AuditHeader.AppInfo info : auditHeader.getAppInfos()) {
						SOAPElement appInfoElement = userElement.addChildElement("appInfo", AVALON_PREFIX);
						appInfoElement.addChildElement("userId", AVALON_PREFIX).addTextNode(info.getUserId());
						appInfoElement.addChildElement("applicationId", AVALON_PREFIX).addTextNode(String.valueOf(info.getApplicationId()));
						appInfoElement.addChildElement("ipAddress", AVALON_PREFIX).addTextNode(info.getIPAddress());
					}
				}
				
			} catch (Exception e) {
				logger.error("Avalon handler error: {}", e.getLocalizedMessage(), e);
			}
		}
		
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

}
