/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.util;

import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * AttributeTranslator<p>
 * The AttributeTranslator class provides conversion methods for different attribute types<p>
 * 
 * @author Peter Frei
 */
public class AttributeTranslator {
	/**
	 * Returns boolean from String
	 * 
	 * @param String
	 *            String to Translate
	 * @return boolean boolean value of String
	 * 
	 */
	public static boolean booleanFromString(String pString) {

		if (pString == null)
			return false;

		boolean isTrue = true;
		if ((pString.trim().equals("F")) || (pString.trim().equals("N")))
			isTrue = false;

		return isTrue;
	}

	/**
	 * Returns String from Date
	 * 
	 * @param Date
	 *            String to Translate
	 * @return String String value of Date (using default format)
	 * 
	 */
	public static String stringFromDate(Date dateDateArg) {

		if (dateDateArg == null)
			return "";

		// Use default format '"yyyyMMdd HH:mm:ss"'
		return stringFromDate(dateDateArg, "yyyyMMdd HH:mm:ss");
	}

	/**
	 * Returns String from Date
	 * 
	 * @param Date
	 *            String to Translate
	 * @param String
	 *            Format of resulting String
	 * @return String String value of Date
	 * 
	 */
	public static String stringFromDate(Date dateDateArg, String dateFormatArg) {

		if (dateDateArg == null || dateFormatArg == null)
			return "";

		// Format the date passed in
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormatArg);
		String dateString = formatter.format(dateDateArg);

		return dateString;
	}

	/**
	 * Returns String from Double
	 * 
	 * @param Double
	 *            String to Translate
	 * @param String
	 *            Format of resulting String
	 * @return String String value of Double
	 * 
	 */
	public static String stringFromDouble(Double doubleArg,
			String doubleFormatArg) {

		if (doubleArg == null || doubleFormatArg == null)
			return "";

		// Format the date passed in
		DecimalFormat formatter = new DecimalFormat(doubleFormatArg);
		String doubleString = formatter.format(doubleArg);

		return doubleString;
	}

	/**
	 * Returns Date from String
	 * 
	 * @param String
	 *            String value of Date
	 * @return Date String to Translate
	 * 
	 */
	public static Date dateFromString(String dateStringArg, String dateFormatArg) {

		// Validations
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern(dateFormatArg);
		dateFormat.setLenient(false);
		Date dt = null;

		// try and parse the date, it is valid if no exception is raised
		try {
			dt = dateFormat.parse(dateStringArg.trim());
		} catch (Exception e) {
			return null;
		}
		return dt;
	}

	/**
	 * Returns byte value of first character of String
	 * 
	 * @param String
	 *            text to extract first byte from
	 * @return byte 1st character of String as byte value
	 * 
	 */
	public static byte byteFromString(String pString) {
		if (pString == null)
			return (byte) ' ';
		byte[] byteArray = pString.getBytes();
		if (byteArray.length > 0)
			return byteArray[0];
		else
			return (byte) ' ';
	}

	/**
	 * Returns byte value as String
	 * 
	 * @param byte byte to convert
	 * @return String String representation of byte value
	 * 
	 */
	public static String stringFrombyte(byte pByte) {
		byte[] byteArray = new byte[1];
		byteArray[0] = pByte;

		return new String(byteArray);
	}

	/**
	 * Returns empty String if input is null
	 * 
	 */
	public static String emptyFromNull(String pString) {
		if ((pString == null) || (pString.toUpperCase().equals("NULL")))
			return "";
		return pString;
	}

	/**
	 * Returns empty String if input is null, otherwise the prefix + the string.
	 * 
	 */
	public static String emptyFromNull(String prefix, String pString) {
		if ((pString == null) || (pString.toUpperCase().equals("NULL")))
			return "";
		return prefix + pString;
	}

	/**
	 * This method will replace a string within another string
	 * 
	 * @param String
	 *            inputString
	 * @param String
	 *            token
	 * @param String
	 *            replaceToken
	 * 
	 * @return String
	 */
	public static String replaceString(String inputString, String token,
			String replaceToken) {
		int startIndex = 0;
		String stringToExamine = inputString;
		StringBuffer returnString = new StringBuffer("");

		try {
			startIndex = stringToExamine.indexOf(token);

			while (startIndex >= 0) {

				returnString.append(stringToExamine.substring(0, startIndex));
				returnString.append(replaceToken);
				returnString.append(stringToExamine.substring(startIndex
						+ token.length()));
				stringToExamine = returnString.toString();
				startIndex = stringToExamine.indexOf(token);
				returnString = new StringBuffer("");
			}
			return stringToExamine;
		} catch (Throwable t) {
			return inputString;
		}
	}

	/**
	 * This method will convert a string into a string array
	 * 
	 * @param String
	 *            inputString
	 * @param String
	 *            lengt of string element
	 * 
	 * @return String[]
	 */
	public static String[] stringArraryFromString(String inputString, int elementLength) {
		StringReader stringToRead = new StringReader(inputString);
		Vector<String> strings = new Vector<String>();
		String[] returnStringArray = null;
		char[] charArray = new char[elementLength];
		int returnValue = elementLength;

		while (returnValue > 0 && returnValue == elementLength) {
			try {
				returnValue = stringToRead.read(charArray, 0, elementLength);
				strings.add(new String(charArray));
				charArray = new char[elementLength];
			} catch (IOException ioe) {
			}
		}
		// convert vector to array
		returnStringArray = strings.toArray(new String[strings.size()]);

		return returnStringArray;
	}

	public static final int parseInt(String value) {

		if (value == null) {
			return 0;
		}

		StringBuffer s = new StringBuffer(value.length());
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (Character.isDigit(c)) {
				s.append(c);
			}
		}

		value = s.toString();		

		if (value.length() == 0) {
			return 0;
		}

		return Integer.parseInt(value);
	}
	
	public static String trimString(String s) {
		if (s == null)
			return "";

		return s.trim();
	}

	public static String  getBaseName( Class<?> pClass ) {
		String fullName = pClass.getName();
		int  idx = fullName.lastIndexOf( "." );
		
		return fullName.substring( idx + 1 );
	}
	
	
    public static int compare(Object o1, Object o2)   {
        if(o1 == o2) {
            return 0;
        } else if(o1 == null) {
            return -1;
        } else if(o2 == null) {
            return 1;
        }  else  {
            return o1.toString().compareToIgnoreCase(o2.toString());
        }
    }	
    
    // This method is used to round currency (to 2 decimal digits) before passing value
    // into the 'format' method of the java.text.NumberFormat object
    public static double roundCurrency(double amt) {
    	double dblX = amt * 100;
    	long lngX = Math.round(dblX);
    	dblX = (double) lngX/100;
    	return dblX;
    }
}
