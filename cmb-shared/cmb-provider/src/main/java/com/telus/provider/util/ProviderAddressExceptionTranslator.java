/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.provider.util;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.InvalidAddressException;
import com.telus.api.account.ServiceFailureException;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.framework.exception.TelusException;
import com.telus.provider.TMProvider;
import com.telus.provider.account.TMAddress;

/**
 * @author Anitha Duraisamy
 *
 */
public class ProviderAddressExceptionTranslator implements TelusExceptionTranslator {

	/* (non-Javadoc)
	 * @see com.telus.api.util.TelusExceptionTranslator#translateException(java.lang.Throwable)
	 * 
	 */
	private TMAddress address;
	
	public ProviderAddressExceptionTranslator(TMAddress address) {
		this.address=address;
	}
	public TelusAPIException translateException(Throwable throwable) {
		TelusAPIException exception = null;
		
		if (throwable instanceof ApplicationException) {
			exception = handleApplicationException((ApplicationException) throwable);
		} else if (throwable instanceof TelusException) {
			exception = handleTelusException((TelusException) throwable);
		} 
							
		return exception;
	}
	
	private TelusAPIException handleApplicationException(ApplicationException exception) {
		TelusAPIException result = getExceptionForErrorId(exception.getErrorCode(), exception);
		return result == null ? getExceptionForErrorMessage(exception) : result;
		
	}
	private TelusAPIException handleTelusException(TelusException exception) {
		TelusAPIException result = getExceptionForErrorId(exception.id, exception);
		return result == null ? getExceptionForErrorMessage(exception) : result;
	}
	
	private TelusAPIException getExceptionForErrorMessage(Throwable e) {
		
		if (TMProvider.messageContains(e, "Invalid Unit Designator")) {
			return new InvalidAddressException("Invalid Unit Type", address, null);
		 }else if (TMProvider.messageContains(e, "Missing required field:  Province")) {
			 return new InvalidAddressException("Province Field is Missing", address, null);
		 }else if (TMProvider.messageContains(e, "Invalid Address Province")) {
			 return new InvalidAddressException("Province Field is Invalid", address, null);
		 }else if (TMProvider.messageContains(e, "Invalid Street Type")) {
			 return new InvalidAddressException("Street Type is Invalid", address, null);
		 }else if (TMProvider.messageContains(e, "id=1110560") || TMProvider.messageContains(e, "id=1113000") ||
					  TMProvider.messageContains(e, "id=1110680")) {
			 StringBuffer sb  = new StringBuffer();
			 sb.append( " [caused by: ").append( e.getMessage() ).append("]");
			 return new InvalidAddressException("Address is Invalid" + sb.toString(), address, null);
		 }else {
			 return null;
		 }
		
	}
	
	private TelusAPIException getExceptionForErrorId(String errorId, Throwable e) {
		if (errorId.equals("APP10007")) {
			return  new ServiceFailureException(e, "Knowbility", ServiceFailureException.SERVICE_KNOWBILITY);
		} else if (errorId.equals("SYS00009")) {
			return new ServiceFailureException(e, "PrepaidAccountManager", ServiceFailureException.	SERVICE_PREPAID_ACCOUNT_MANAGER);
		} else if (errorId.equals("SYS00013")) {
			return new ServiceFailureException(e, "credit check applicationCode (CRISK)",ServiceFailureException.SERVICE_CRISK);
		} else if (errorId.equals("SYS00014") || errorId.equals("APP10008")) {
			return new ServiceFailureException(e, "credit card transaction applicationCode (BIH)", ServiceFailureException.SERVICE_BIH);
//		} else if (errorId.equals("1110560") || errorId.equals("1113000")|| errorId.equals("1110680")) {
//			 StringBuffer sb  = new StringBuffer();
//			 sb.append( " [caused by: ").append( e.getMessage() ).append("]");
//			 return new InvalidAddressException("Address is Invalid" + sb.toString(), address, null);
		}
		return null;
	}

	
}
