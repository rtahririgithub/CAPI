/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.redknee.util.crmapi.soap.common.exception.xsd._2008._08.CRMException;
import com.telus.api.ApplicationException;
import com.telus.api.portability.PortRequestException;
import com.telus.cmb.framework.endpoint.EndpointClient;
import com.telus.cmb.framework.resource.ResourceAccessContext;
import com.telus.cmb.framework.resource.ResourceAccessException;

/**
 * @author Pavel Simonovsky
 *
 */
public class WnpLegacyClient extends EndpointClient {

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.framework.resource.ResourceAccessTemplate#translate(com.telus.cmb.framework.resource.ResourceAccessContext, java.lang.Exception)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ApplicationException translate(ResourceAccessContext context, Exception e) {
		
		Throwable cause = e;

		String systemCode = context.getTargetSystemCode() + ":" + context.getTargetComponentCode();
		
		if (e instanceof ResourceAccessException) {
			cause = e.getCause();
		}
		
		String className = cause.getClass().getSimpleName();

		try {
			
			StringBuffer errorMessageBuffer = new StringBuffer();
			String errorCode = "Unknown";

			if (StringUtils.equals(className, "FaultDetails") || StringUtils.equals(className, "PRMServiceException")) {
				
				List<Object> faults = (List<Object>) PropertyUtils.getProperty(cause, "faultInfo.PRMFault");
				if (!faults.isEmpty()) {
					errorCode = (String) PropertyUtils.getProperty(faults.get(0), "code");
					for (Object fault : faults) {
						errorMessageBuffer.append("[").append(PropertyUtils.getProperty(fault, "reason")).append("]");
					}
				}
				return new ApplicationException(systemCode, errorCode, errorMessageBuffer.toString(), errorMessageBuffer.toString(), cause);
			} else if (StringUtils.equals(className, "CRMExceptionFault")) {
				CRMException crmException = (CRMException) PropertyUtils.getProperty(cause, "faultInfo");
				if (crmException != null) {
					errorCode = String.valueOf(crmException.getCode());
					errorMessageBuffer.append("[").append(crmException.getMessage()).append("]");
				}
				return new ApplicationException(systemCode, errorCode,errorMessageBuffer.toString(),errorMessageBuffer.toString(), cause);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ApplicationException(context.getComponentCode(), context.getErrorCode(), ex.getMessage(), ex.getMessage(), ex);
		}
		
		if (e instanceof PortRequestException) {
			return new ApplicationException(context.getComponentCode(), context.getErrorCode(), cause.getMessage(), cause.getMessage(), cause);
		}
		
		return super.translate(context, e);
	}

}
