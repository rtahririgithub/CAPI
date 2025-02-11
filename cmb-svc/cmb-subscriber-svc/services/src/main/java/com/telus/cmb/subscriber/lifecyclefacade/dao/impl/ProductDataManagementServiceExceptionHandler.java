package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.RemoteAccessException;
import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.dao.soa.spring.SoaDefaultErrorHandler;
import com.telus.cmb.common.dao.soa.spring.SoaExceptionContext;
import com.telus.cmb.wsclient.PolicyException_v1;
import com.telus.cmb.wsclient.ServiceException_v1;
import com.telus.cmb.wsclient.ServiceException_v3;

public class ProductDataManagementServiceExceptionHandler extends SoaDefaultErrorHandler{

	private static final Log LOGGER = LogFactory.getLog(ProductDataManagementServiceExceptionHandler.class);

	/**
	 * We have added the below method to treat cods RuntimeException error message
	 * "java.lang.RuntimeException getCatalogueItemIdByExternalId() method failed. "
	 *  as application exceptions so that these errors wont be processed again through queue and ignored after first try.
	 */
	
	
	@Override
	public void handleException(Throwable cause, SoaExceptionContext context) throws ApplicationException {
		/*
		 * TODO: context is not in used yet
		 */
		if (cause instanceof PolicyException_v1) {
			ApplicationException ae = getApplicationException((PolicyException_v1)cause);
			LOGGER.debug(getErrorMsg(ae), ae);
			throw ae;
		} else if (cause instanceof ServiceException_v1) {
			
			if (getErrorMsg((ServiceException_v1) cause).contains("getCatalogueItemIdByExternalId() method failed.")) {
				LOGGER.error("ConsumerProductDataManagementService getCatalogueItemIdByExternalId() method error..");
				ApplicationException ae = getApplicationException((ServiceException_v1) cause);
				LOGGER.error(getErrorMsg(ae), ae);
				throw ae;
			}
			SystemException se = getSystemException((ServiceException_v1) cause);
			LOGGER.error(getErrorMsg(se), se);
			throw se;
		} else if (cause instanceof ServiceException_v3) {
			SystemException se = getSystemException((ServiceException_v3)cause);
			LOGGER.error(getErrorMsg(se), se);
			throw se;
		} else if (cause instanceof SystemException) {
			LOGGER.error(getErrorMsg((SystemException)cause), cause);
			throw (SystemException)cause;
		} else if (cause instanceof ApplicationException) {
			LOGGER.debug(getErrorMsg((ApplicationException)cause), cause);
			throw (ApplicationException)cause;
		} else if (cause instanceof RemoteAccessException) {
			SystemException se = getSystemException(cause);
			LOGGER.error(getErrorMsg(se), se);
			throw se;
		} else {
			LOGGER.error(cause.getMessage(), cause);
			SystemException se = getUnknownException(cause);
			throw se;
		}
	}
	
	private ApplicationException getApplicationException(ServiceException_v1 cause) {
		return new ApplicationException(SystemCodes.SOA_SPRING,getErrorCode(cause), cause.getClass().getCanonicalName() + " "+
		getErrorMsgId(cause) + " " + getErrorMsg(cause),StringUtils.EMPTY, cause);

	}
}
