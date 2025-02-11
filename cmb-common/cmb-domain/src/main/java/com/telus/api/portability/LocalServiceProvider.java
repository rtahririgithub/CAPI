package com.telus.api.portability;


/**
 * @author Anitha Duraisamy
 *
 */
public interface LocalServiceProvider {
	
	String getLocalServiceProviderType();
	String getLocalServiceProviderId();
	String getLocationRoutingNumber();

	boolean isPortableIndicator();

	}
