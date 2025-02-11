package com.telus.cmb.account.utilities;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.telus.api.ClientAPI;
import com.telus.api.account.AuditHeader;
import com.telus.eas.account.info.AuditHeaderInfo;

public class AuditHeaderUtil {

	private static final String ECA_SIGN = "@ClientAPI-ECA";

	public static AuditHeader appendToAuditHeader(AuditHeader header, String applicationId, String userId) {
		
		boolean isLastAppInfoECA = false;
		AuditHeader.AppInfo[] apps = header.getAppInfos();
		// check last app info if it is ECA
		if (apps.length > 0) {
			String userid = apps[apps.length - 1].getUserId();
			if (userid != null && userid.indexOf(ECA_SIGN) != -1) {
				isLastAppInfoECA = true;
			}
		}
		if (isLastAppInfoECA) { // if so, no need to append header again
			return header;
		} else {
			// the AuditHeader does not contain ECA info, so make a clone and append it
			AuditHeaderInfo auditHeader = new AuditHeaderInfo(header);
			try {
				auditHeader.appendAppInfo("kbUser=" + userId + ", appCode=" + applicationId + ECA_SIGN, ClientAPI.CMDB_ID, InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException e) {
				// if can't get local IP, continue the transaction
			}
			
			return auditHeader;
		}
	}

}