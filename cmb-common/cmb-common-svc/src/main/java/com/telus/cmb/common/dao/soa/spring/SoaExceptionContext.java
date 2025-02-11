/*
 *  Copyright (c) 2013 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.dao.soa.spring;


public class SoaExceptionContext {

	private String errorCode;
	
	private String errorMessage;
	
	private String serviceName;
	
	private String operationName;
	
	private Object[] parameterValues;
	
	private String[] parameterNames;
	
	private String[] fieldMask;

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the parameterValues
	 */
	public Object[] getParameterValues() {
		return parameterValues;
	}

	/**
	 * @param parameterValues the parameterValues to set
	 */
	public void setParameterValues(Object[] parameterValues) {
		this.parameterValues = parameterValues;
	}

	/**
	 * @return the parameterNames
	 */
	public String[] getParameterNames() {
		return parameterNames;
	}

	/**
	 * @param parameterNames the parameterNames to set
	 */
	public void setParameterNames(String[] parameterNames) {
		this.parameterNames = parameterNames;
	}

	/**
	 * @return the fieldMask
	 */
	public String[] getFieldMask() {
		return fieldMask;
	}

	/**
	 * @param fieldMask the fieldMask to set
	 */
	public void setFieldMask(String[] fieldMask) {
		this.fieldMask = fieldMask;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the operationName
	 */
	public String getOperationName() {
		return operationName;
	}

	/**
	 * @param operationName the operationName to set
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
}
