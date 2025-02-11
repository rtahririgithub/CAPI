package com.telus.api.account;

import com.telus.api.resource.Resource;
public interface SeatData {

	/**
	 * Set seatType, type of seat used for Business Connect seat activation.(should be either
	 * START,MOBL,OFFC,PROF),use CAPI @link SeatType interface seat type
	 * constant values to set seat type
	 * 
	 * @param seatType String
	 */
	void setSeatType(String seatType);
	String getSeatType();

	/**
	 * Set SeatGroup, used to create to mandatory starter seat and other optional seats under the Ban.
	 * 
	 * @param Group  String
	 */
	void setSeatGroup(String seatGroup);
	String getSeatGroup();
	
	/**
	 * Set resource type and resourceNumber, used for BF seat activation. use CAPI @link Subscriber interface resource type constant values to set resource type 
	 * (should be either  I,V,L,O)
	 * @param resourcetype String
	 * @param resourceNumber String         
	 */
	
	void addSeatResource( String resourceType, String resourceNumber );
	
	Resource[] getResources();
	
	/**
	 * remove resource, used to remove the resource from existing/passed resource list.
	 * @param Resource resource
	 */
	
	Resource[] removeResource(Resource resource);
}
