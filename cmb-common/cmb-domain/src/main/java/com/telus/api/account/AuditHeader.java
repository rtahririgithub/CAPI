package com.telus.api.account;

import java.io.Serializable;


/**
 * AuditHeader represent cumulative audit trail of all applications and users involved in a transaction. This Class maps to 
 * Avalon Services's UserHeader (OriginatingUserType in AvalonCommonSchema_v1_0.xsd). All applications involved in a transaction, 
 * for which being processing by Avalon services, need to capture the information about the application( userId, applicaitonId,
 * server IPAddress) and append to the AuditHeader.
 * 
 */
public interface AuditHeader extends Serializable {
	
	/**
	 * Set customer Id to this AuditHeader object.
	 * @param customerId - The customerId could be populated with the subscriber number or the User ID which can identify the end-user. 
	 * This field shall only be populated by system initiating the the request.
	 * 
	 */
	void setCustomerId(String customerId );
	/**
	 * @return CustomerId
	 */
	String getCustomerId();
	/**
	 * Set the end-user's IP address.
	 * @param userIpAddress - The IP Address will be the IP address of the end-user. This field shall only be populated by system 
	 * initiating the request 
	 */
	void setUserIPAddress(String userIpAddress);
	/**
	 * @return User IP address
	 */
	String getUserIPAddress();
	
	/**
	 * Append the information of an application to this AuditHeader. All applications involved in a transaction, for which being processing by 
	 * Avalon services, need to append itself to the AuditHeader.
	 * 
	 * @param userId - is the unique identifier of the person or system who access the application
	 * @param applicationId - the applciation's CMDB id
	 * @param appServerIPAddress -IP Address of the server instance processing the current request
	 */
	void appendAppInfo( String userId, int applicationId, String appServerIPAddress );
	
	/**
	 * Return all AppInfo in this AuditHeader. Can be empty array, but will never be null.
	 * @return array of AppInfo of this AuditHeader
	 */
	AppInfo[] getAppInfos ();
	
	
	/**
	 * This class contain information of an Application
	 *
	 */
	interface AppInfo extends Serializable {
		/**
		 * Return User Id.
		 * @return String
		 */
		String getUserId();
		/**
		 * @return application's CMDB Id.
		 */
		int getApplicationId();
		/**
		 * @return IP Address of the server instance.
		 */
		String getIPAddress();
	}
}
