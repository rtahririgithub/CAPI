package com.telus.api.reference;




/**
 * This represents the data sharing group information specific to a service. It indicates
 * if the service is access or contributing to the data sharing group
 */
public interface ServiceDataSharingGroup {
	
	public String getDataSharingGroupCode();	
	
	/**
	 * True if the service is a contributing SOC to the data sharing group.  If this
	 * is false then the service is only an access.  If true, the service is both
	 * access and contributing.
	 */
	 public boolean isContributing();
}


