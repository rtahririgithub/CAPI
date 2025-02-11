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

import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.RemoteException;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.CreditCard;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.ServiceFailureException;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.framework.exception.PolicyFaultInfo;
import com.telus.eas.framework.exception.TelusCreditCardException;
import com.telus.eas.framework.exception.WpsPolicyException;

/**
 * @author Anitha Duraisamy
 *
 */
public class ProviderCreditCardExceptionTranslator implements TelusExceptionTranslator {

	/* (non-Javadoc)
	 * @see com.telus.api.util.TelusExceptionTranslator#translateException(java.lang.Throwable)
	 * 
	 */
	private CreditCard creditCard;
	
	public ProviderCreditCardExceptionTranslator(CreditCard creditCard) {
		this.creditCard=creditCard;
	}
	public TelusAPIException translateException(Throwable throwable) {
		
		if (throwable instanceof WpsPolicyException) {
			return handleWpsPolicyException((WpsPolicyException) throwable);
		} else if (throwable instanceof TelusCreditCardException) {
			return  handleTelusCreditCardException((TelusCreditCardException) throwable);
		} else if (throwable instanceof ApplicationException) {
			return handleApplicationException((ApplicationException) throwable);
		} 
		
		else if (throwable instanceof UndeclaredThrowableException) {
			return handleUndeclaredThrowableException((UndeclaredThrowableException) throwable);
		}		
		
		return null;
	}
	
	private TelusAPIException handleUndeclaredThrowableException (
			UndeclaredThrowableException throwable) {
		if (throwable.getMessage() != null && throwable.getMessage().length() > 0) {
			return new TelusAPIException(throwable.getMessage(), throwable);
		} else if (throwable.getUndeclaredThrowable() instanceof RemoteException) {
			RemoteException remoteEx = (RemoteException) throwable.getUndeclaredThrowable();
			if (remoteEx.getCause() instanceof SystemException) {
				SystemException sysEx = (SystemException) remoteEx.getCause();
				return new TelusAPIException(sysEx.getErrorMessage(), throwable);							
			}
			return new TelusAPIException(remoteEx.getMessage(), throwable);			
		}
		return null;
	}	
	
	private TelusAPIException handleWpsPolicyException(WpsPolicyException exception) {
		return new InvalidCreditCardException(exception, exception.id, creditCard, 
				exception.getCCardMessageEN(), exception.getCCardMessageFR(), exception.getWpsPolicyFaultInfo());
	}
	
	private TelusAPIException handleTelusCreditCardException(TelusCreditCardException exception) {
		TelusAPIException result =getExceptionForErrorId(exception.id, exception);
		return result == null ? 
			new InvalidCreditCardException(exception, exception.id, creditCard, exception.getCCardMessageEN(),exception.getCCardMessageFR())
		    : result;
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
		}
		return null;
	}

	private TelusAPIException handleApplicationException(ApplicationException ae) {
		
		if(ae.getNestedException()!=null){
			if(ae.getNestedExceptionBySystemCode(SystemCodes.WPS)!=null){
				PolicyFaultInfo policyFaultInfo = new PolicyFaultInfo(ae.getNestedException().getSystemCode(),
						ae.getNestedException().getErrorCode(), ae.getNestedException().getErrorMessage(), null);
				return  new InvalidCreditCardException(ae, ae.getErrorCode(), creditCard, ae.getErrorMessage(),
						ae.getErrorMessageFr(), policyFaultInfo);
			}else if (ae.getNestedExceptionBySystemCode(SystemCodes.WPS_GP)!=null){
				TelusAPIException result =getExceptionForErrorId(ae.getErrorCode(), ae);
				return result == null ? new InvalidCreditCardException(ae, ae.getErrorCode(), creditCard, 
						ae.getErrorMessage(),ae.getErrorMessageFr()): result;

			}
		}

		return null;
	}
	
}
