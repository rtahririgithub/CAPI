package com.telus.cmb.framework.endpoint;

/**
 * This class is created to support web services which have response types 
 * which are not wrapped (in a separate complex type).  It will override the
 * respond() method to directly set the response.
 * @author Wilson Cheong
 *
 */
public class EndpointProviderV2 extends EndpointProvider {

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T respond(Object response) {
		getOperationContext().setResponse(response);
		return (T) response;
	}
}
