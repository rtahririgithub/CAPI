package com.telus.cmb.tool.services.log.utils;

import javax.servlet.http.HttpSession;

import com.telus.cmb.tool.services.log.domain.LogServerInfo;
import com.telus.cmb.tool.services.log.service.LogFileService;

public class SessionUtil {

	private static final String SESSION_LOGIN = "login_id";
	private static final String SESSION_LDAP_CRED = "login_ldap_credential";
	private static final String SESSION_UNIX_CRED = "login_unix_credential";
	
	public static boolean isPassValid(LogFileService logFileService, String user, String pass, LogServerInfo logServer, HttpSession session) {
		if (logFileService.isCredentialValid(logServer.getHost(), user, pass)) {
			setCredentialsToSession(user, pass, logServer.usesUnixLogin(), session);
			return true;
		} else {
			return false;
		}
	}
	
	private static void setCredentialsToSession(String user, String pass, boolean isUnix, HttpSession session) {
		session.setAttribute(SESSION_LOGIN, user);		
		if (isUnix) {
			session.setAttribute(SESSION_UNIX_CRED, EncryptionUtil.encryptPassword(user, pass));				
		} else {
			session.setAttribute(SESSION_LDAP_CRED, EncryptionUtil.encryptPassword(user, pass));			
		}	
	}
	
	public static boolean checkCredentials(HttpSession session, boolean isUnix) {
		Object credential = session.getAttribute(SESSION_LDAP_CRED);
		if (isUnix) {
			credential = session.getAttribute(SESSION_UNIX_CRED);
		}
		if (credential == null) {
			return false;			
		}
		return true;
	}
	
	public static String getUserFromSession(HttpSession session) {
		return session.getAttribute(SESSION_LOGIN).toString();
	}
	
	public static String getPassFromSession(HttpSession session, boolean isUnix) {
		String encryptedPass = isUnix ? session.getAttribute(SESSION_UNIX_CRED).toString() : session.getAttribute(SESSION_LDAP_CRED).toString();
		return EncryptionUtil.decryptPassword(getUserFromSession(session), encryptedPass);
	}
	
}
