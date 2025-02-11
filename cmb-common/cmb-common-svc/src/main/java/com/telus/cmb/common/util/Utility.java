package com.telus.cmb.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
	
	public static int ERROR_UNKNOWN=0;
	public static int TUXEDO_ERROR_BAN_CHANGED=1;
	public static int TUXEDO_ERROR_BAN_IN_USE=2;
	private static Pattern TUXEDO_BAN_IN_USE_PATTERN = Pattern.compile("BAN\\s*\\((\\d*)\\) in use. Please try accessing account again later", Pattern.CASE_INSENSITIVE);
	private static Pattern TUXEDO_BAN_CHANGED_PATTERN =Pattern.compile("BAN\\s*\\((\\d*)\\) has been changed since last retrieved", Pattern.CASE_INSENSITIVE);

	
	 public static int compare(Object o1, Object o2)
	    {
	        if(o1 == o2)
	        {
	            return 0;
	        }
	        else if(o1 == null)
	        {
	            return -1;
	        }
	        else if(o2 == null)
	        {
	            return 1;
	        }
	        else
	        {
	            return o1.toString().compareToIgnoreCase(o2.toString());
	        }
	    }
	 
	 
		public static boolean isConcurrentUpdateError(  Throwable exception, int expectedBan ) {
			boolean result = false;
			int errorType = parseException( exception , expectedBan );
			if ( errorType==TUXEDO_ERROR_BAN_IN_USE || errorType == TUXEDO_ERROR_BAN_CHANGED ) 
				result= true;
			return result;
		}

		/**
		 * @param exception The exception to be parsed
		 * @param expectedBan The ban to be checked in the Exception's message
		 * @return 
		 * 		<tt>ERROR_UNKNOWN</tt> - unknown error
		 * 		<tt>TUXEDO_ERROR_BAN_CHANGED</tt> - Exception indicate a Tuxedo error occured with Ban changed message
		 * 		<tt>TUXEDO_ERROR_BAN_IN_USE</tt> - Exception indicate a Tuxedo error occured with Ban in message
		 * 			
		 */
		public static int parseException( Throwable exception, int expectedBan ) {
			return parseErrorMessage( exception.getMessage(), expectedBan);
		}

		/**
		 * @param errorMessage The error message to be parsed
		 * @param expectedBan The ban to be checked in the Exception's message
		 * @return
		 * 		<tt>ERROR_UNKNOWN</tt> - unknown error
		 * 		<tt>TUXEDO_ERROR_BAN_CHANGED</tt> - error message indicate a Tuxedo error occured with Ban changed message
		 * 		<tt>TUXEDO_ERROR_BAN_IN_USE</tt> - error message indicate a Tuxedo error occured with Ban in message
		 */
		public static int parseErrorMessage( String errorMessage, int expectedBan ) {
			int result = ERROR_UNKNOWN;
			
			Matcher matcher = TUXEDO_BAN_CHANGED_PATTERN.matcher(errorMessage);
			if ( matcher.find() ) {
				String ban = matcher.group(1);
				if ( Integer.parseInt(ban)==expectedBan) {
					result = TUXEDO_ERROR_BAN_CHANGED;
				}
			} else {
				matcher = TUXEDO_BAN_IN_USE_PATTERN.matcher( errorMessage );
				if ( matcher.find() ) {
					String ban = matcher.group(1);
					if ( Integer.parseInt(ban)==expectedBan) {
						result = TUXEDO_ERROR_BAN_IN_USE;
					}
				}
			}
			
			return result;
		}


}
