package com.telus.eas.portability.info;

import com.telus.api.portability.LocalServiceProvider;
import com.telus.eas.framework.info.Info;

public class LocalServiceProviderInfo extends Info implements LocalServiceProvider {

	private static final long serialVersionUID = 1L;

	private String localServiceProviderType;
	private String localServiceProviderId;
	private String locationRoutingNumber;
	private boolean portableIndicator;

	public String getLocalServiceProviderType() {
		return localServiceProviderType;
	}

	public void setLocalServiceProviderType(String localServiceProviderType) {
		this.localServiceProviderType = localServiceProviderType;
	}

	public String getLocalServiceProviderId() {
		return localServiceProviderId;
	}

	public void setLocalServiceProviderId(String localServiceProviderId) {
		this.localServiceProviderId = localServiceProviderId;
	}

	public String getLocationRoutingNumber() {
		return locationRoutingNumber;
	}

	public void setLocationRoutingNumber(String locationRoutingNumber) {
		this.locationRoutingNumber = locationRoutingNumber;
	}

	public boolean isPortableIndicator() {
		return portableIndicator;
	}

	public void setPortableIndicator(boolean portableIndicator) {
		this.portableIndicator = portableIndicator;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("LocalServiceProviderInfo:{\n");
		s.append("    localServiceProviderType=[").append(localServiceProviderType).append("]\n");
		s.append("    localServiceProviderId=[").append(localServiceProviderId).append("]\n");
		s.append("    locationRoutingNumber=[").append(locationRoutingNumber).append("]\n");
		s.append("    portableIndicator=[").append(portableIndicator).append("]\n");
		s.append("}");

		return s.toString();
	}

	
}
