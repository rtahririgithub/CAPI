package com.telus.api.resource;

public interface Resource {
	
	/**
	 * Resource type. Use CAPI Subscriber interface resource constant values to set resource type (should be either I,V,L,O).
	 * 
	 * @param resourceNumbers String
	 */
	void setResourceType(String resourceType);
	String getResourceType();
	
	/**
	 * Resource number activated for seat, interface applications get these resources from RC/CRIS and passed ClientAPI to activate/change.
	 * 
	 * @param resourceNumbers String
	 */
	void setResourceNumber(String resourceNumber);
	String getResourceNumber();
//	
public String getResourceStatus();
//	public void setResourceStatus(String resourceStatus);
	
}