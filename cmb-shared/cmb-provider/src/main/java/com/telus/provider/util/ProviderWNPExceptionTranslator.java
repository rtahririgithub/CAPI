package com.telus.provider.util;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.portability.InterBrandPortRequestException;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.provider.TMProvider;

public class ProviderWNPExceptionTranslator implements TelusExceptionTranslator {
	TMProvider provider;

	public ProviderWNPExceptionTranslator(TMProvider provider) {
		this.provider = provider;
	}

//	@Override
	public TelusAPIException translateException(Throwable throwable) {
		
		TelusAPIException exception = null;
		if(throwable instanceof InterBrandPortRequestException){
			return (InterBrandPortRequestException)throwable;
		}
		else if (throwable instanceof ApplicationException) {
			exception = handleApplicationException((ApplicationException) throwable);
		} else {
			exception = new TelusAPIException(throwable);
		}

		return exception;

	}

	private TelusAPIException handleApplicationException(ApplicationException ae) {
		
		PortRequestException prEx = null;
		
		if (!ae.getErrorCode().equals("")) {
			if( ae.getErrorCode().equals("PRM_FALSE")){
				prEx = new PortRequestException(null, provider.getApplicationMessage("10005", ae.getErrorMessage(), ae.getErrorMessage()));
				return prEx;
			}	
			if (provider.getContextBrandId() == 0) {
				 prEx= new PortRequestException(ae,provider.getApplicationMessage(ApplicationSummary.APP_PRM, ae.getErrorCode(),ae));
				 return prEx;
			} else {
				prEx= new PortRequestException(ae,provider.getApplicationMessage(ApplicationSummary.APP_PRM, ae.getErrorCode(),provider.getContextBrandId(),ae));
				return prEx;
			}
		}
		return new TelusAPIException(ae);
	}

}