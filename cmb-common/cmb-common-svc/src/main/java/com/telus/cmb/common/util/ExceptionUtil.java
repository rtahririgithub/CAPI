package com.telus.cmb.common.util;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.eas.framework.exception.TelusException;

public class ExceptionUtil {
	
	private final static int ERROR_ORA_04068=4068;
	
	/**
	 * This is method iterates through the exception hierarchy to determine if the root cause is a 
	 * SQLException with the matching SQL error code
	 *  
	 * @param t
	 * @param sqlErrorCode
	 * @return
	 */
	public static boolean isSqlError(Throwable t, int sqlErrorCode) {
		if (t instanceof java.sql.SQLException ) {
			return (((java.sql.SQLException ) t).getErrorCode()==sqlErrorCode);
		} else if( t.getCause()!=null ) {
			return isSqlError ( t.getCause(), sqlErrorCode );
		} else if (t instanceof SystemException && ((SystemException) t).getErrorCode() != null) {
			if (sqlErrorCode == ERROR_ORA_04068) {
				return ((SystemException) t).getErrorCode().equals("ORA-0"+sqlErrorCode);
			}else {
				return ((SystemException) t).getErrorCode().equals("ORA-"+sqlErrorCode);
			}
		} else {
			return false;
		}
	}
		
	/**
	 * This is method iterates through the exception hierarchy to determine if the root cause is because of ORA-04068 
	 * @param t
	 * @return return true if 
	 */
	public static boolean isRootCauseAsORA04068(Throwable t) {
		return isSqlError( t, ERROR_ORA_04068 );
	}

	/**
	 * This method returns, 
	 *  1) id from TelusException
	 *  2) errorCode from ApplicationException or SystemException
	 *  3) EMPTY from other
	 *  
	 * @param t
	 * @return
	 */
	public static String getErrorCode(Throwable t) {
		if (t instanceof TelusException) {
			return ((TelusException)t).id;
		} else if (t instanceof ApplicationException) {
			return ((ApplicationException)t).getErrorCode();
		} else if (t instanceof SystemException) {
			return ((SystemException)t).getErrorCode();
		} else {
			return "";
		}
	}
}
