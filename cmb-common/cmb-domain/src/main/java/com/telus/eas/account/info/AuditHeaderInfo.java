package com.telus.eas.account.info;

import java.util.ArrayList;
import java.util.List;

import com.telus.api.account.AuditHeader;

public class AuditHeaderInfo implements AuditHeader {
	
	private static final long serialVersionUID = 1L;
	
	private String customerId;
	private String userIPAddress;
	private List appInfos = new ArrayList();
	
	public AuditHeaderInfo() {}
	
	//create a instance and copy everything from the give AuditHeader
	public AuditHeaderInfo(AuditHeader aHeader ) {
		this();

		//stick the AuditHeader interface,do not make assumption of the implementation class
		this.customerId = aHeader.getCustomerId();
		this.userIPAddress = aHeader.getUserIPAddress();
		
		AuditHeader.AppInfo[] appArray = aHeader.getAppInfos();
		for ( int i=0; i<appArray.length; i++ ) {
			appInfos.add( appArray[i] );
		}
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getUserIPAddress() {
		return userIPAddress;
	}

	public void setUserIPAddress(String userIPAddress) {
		this.userIPAddress = userIPAddress;
	}

	public void appendAppInfo( String userId, int applicationId, String appServerIP ) {
		AppInfo aInfo = new AppInfo( userId, applicationId, appServerIP );
		appInfos.add( aInfo );
	}
	
	public AuditHeader.AppInfo[] getAppInfos() {
		
		return (AuditHeader.AppInfo[]) appInfos.toArray( new AuditHeader.AppInfo[appInfos.size()] );
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "AduitHeaderInfo@").append( Integer.toHexString(hashCode() )).append("[\n")
			.append("  customerId=").append(customerId).append("\n")
			.append("  userIpAddress=").append(userIPAddress).append( "\n");
		sb.append("  appInfos@").append( Integer.toHexString(hashCode() )).append("\n");
		for( int i=0; i<appInfos.size(); i++ ) {
			sb.append( "  [").append(i).append("]=[").append( appInfos.get(i) ).append("  ]\n");
		}
		sb.append( "]");
		return sb.toString();
		
	}
	
	static class AppInfo implements AuditHeader.AppInfo {
		private static final long serialVersionUID = 1L;
		private String userId;
		private int applicationId;
		private String ipAddress;

		AppInfo(String userId, int applicationId, String appServerIP) {
			this.userId = userId;
			this.applicationId = applicationId;
			this.ipAddress = appServerIP;
		}
		
		public String getUserId() {
			return userId;
		}

		public int getApplicationId() {
			return applicationId;
		}

		public String getIPAddress() {
			return ipAddress;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("AppInfo@").append(Integer.toHexString(hashCode())).append( "[\n")
			.append("      userId=").append(userId).append("\n")
			.append("      applicationId=").append(applicationId).append( "\n")
			.append("      ipAddress=").append(ipAddress).append( "\n");
			sb.append("      ]\n");
			return sb.toString();
		}
		
	}
	
	public void copyFrom(AuditHeaderInfo copy) {
		this.customerId = copy.customerId;
		this.userIPAddress = copy.userIPAddress;
		this.appInfos = copy.appInfos;
	}

}
