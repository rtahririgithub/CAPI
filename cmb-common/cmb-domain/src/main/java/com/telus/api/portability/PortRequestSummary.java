package com.telus.api.portability;

import java.util.Date;

public interface PortRequestSummary {
	
	public static final String TYPE_PORT_IN = "I";
	public static final String TYPE_PORT_OUT = "O";
	public static final String TYPE_PORT_WITHIN = "W";
	public static final String TYPE_PORT_SNAP_BACK = "X";

	public static final String STATUS_CATEGORY_IN_PROGRESS = "P";
	public static final String STATUS_CATEGORY_COMPLETED = "C";
	public static final String STATUS_CATEGORY_NOT_EXIST = "N";
	public static final String STATUS_CATEGORY_CANCELLED = "D";

	String getPhoneNumber();
	int getBanId();
	String getPortRequestId();
	String getType();
	String getStatusCategory();
	String getStatusCode();
	String getStatusReasonCode();
	Date getCreationDate();
	boolean canBeActivated();  
	boolean canBeSubmitted();
	boolean canBeCanceled();
	boolean canBeModified();
	int getIncomingBrandId();
	int getOutgoingBrandId();
	
	/**
	 * Sets the platform identifier to indicate the system responsible for handling the
	 * port.  Current values are 1 for TELUS managed brands and 2 for the RedKnee
	 * Mobile Virtual Network Enabler (MVNE)
	 */
	public void setPlatformId(int platformId);

	/**
	 * Gets the platform identifier to indicate the system responsible for handling the
	 * port.  Current values are 1 for TELUS managed brands and 2 for the RedKnee
	 * Mobile Virtual Network Enabler (MVNE)
	 */
	public int getPlatformId();
	
	/**
	 * This method is a convenience method that returns true if the platformId is not TELUS
	 * (platformId != 1) otherwise it returns false
	 */
	public boolean isPortInFromMVNE();
}
