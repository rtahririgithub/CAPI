/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.framework.info;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Restrictions on fields:
 *    1. Right now only int, long, String, Date and subclasses of Info
 *       are supported.
 *       The class can have fields with other primitive or class types,
 *       but they will not be dumped into the XML.
 */
public abstract class  Info implements PublicCloneable, java.io.Serializable
{

	public static final long serialVersionUID = 7552174121439632415L;


	static final String  CHAR_ARRAY_SYMBOL = "[C" ;
	static final String  STRING_SYMBOL = "java.lang.String" ;
	static final String  DATE_SYMBOL = "java.util.Date" ;
	static final String  LONG_SYMBOL = "long" ;
	static final String  INT_SYMBOL = "int" ;
	static final String  BOOLEAN_SYMBOL = "boolean" ;
	static final String  DOUBLE_SYMBOL = "double" ;
	static final String  FLOAT_SYMBOL = "float" ;
	static final String  STRING_ARRAY_SYMBOL = "[Ljava.lang.String;" ;
	static final String  BOOLEAN_ARRAY_SYMBOL = "[Z" ;
	static final String  INTEGER_ARRAY_SYMBOL = "[I" ;
	static final String  LONG_ARRAY_SYMBOL = "[J" ;
	static final String  DOUBLE_ARRAY_SYMBOL = "[D" ;
	static final String  FLOAT_ARRAY_SYMBOL = "[F" ;

	public static final Object[] convertArrayType(Object[] oldArray, Class arrayType) {

		if (!arrayType.isArray()) {
			throw new IllegalArgumentException(arrayType + " is not an array type, expected " + arrayType.getName() + "[]");
		}

		if (oldArray == null || oldArray.getClass() == arrayType) {
			return oldArray;
		}

		Object[] newArray = (Object[])Array.newInstance(arrayType.getComponentType(), oldArray.length);
		System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);

		return newArray;

		//		if (oldArray == null || oldArray.getClass().getComponentType() == newComponentType) {
		//		return oldArray;
		//		}

		//		Object[] newArray = (Object[])Array.newInstance(newComponentType, oldArray.length);
		//		System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);

		//		return newArray;

	}

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static final String nullToString(Object o) {
		return (o == null)?"":o.toString();
	}

	public static final String nullToString(String prefix, Object o) {
		return (o == null)?"":prefix + o.toString();
	}

	public static final String nullToString(String prefix, Object o, String suffix) {
		return (o == null)?"":prefix + o.toString() + suffix;
	}

	public static final String toUpperCase(String pString) {
		return pString == null ? null : pString.trim().toUpperCase();
	}

	public static String padTo(String string, char padChar, int length) {

		if (string == null) {
			string = "";
		}

		int stringLength = string.length();
		if (stringLength >= length) {
			return string;
		}

		StringBuffer s = new StringBuffer(length);
		s.append(string);
		for (int i = stringLength; i < length; i++) {
			s.append(padChar);
		}

		return s.toString();
	}

	public static String padService(String string) {
		return padTo(string, ' ', 9);
	}

	public static String padFeature(String string) {
		return padTo(string, ' ', 6);
	}

	public static String repeat(String string, int count) {
		StringBuffer s = new StringBuffer(string.length() * count);
		for (int i = 0; i < count; i++) {
			s.append(string);
		}
		return s.toString();
	}

	public static boolean intersects(Date effectiveDate1, Date expiryDate1, Date effectiveDate2, Date expiryDate2) {

		long a1 = (effectiveDate1 != null) ? effectiveDate1.getTime() : Long.MIN_VALUE;
		long a2 = (expiryDate1 != null) ? expiryDate1.getTime() : Long.MAX_VALUE;
		long b1 = (effectiveDate2 != null) ? effectiveDate2.getTime() : Long.MIN_VALUE;
		long b2 = (expiryDate2 != null) ? expiryDate2.getTime() : Long.MAX_VALUE;
		boolean result = (!(a2 <= b1 || a1 >= b2));

		/*
    System.err.println("    intersects("+effectiveDate1+", "+expiryDate1+", "+effectiveDate2+", "+expiryDate2+")");
    System.err.println("      a1="+a1);
    System.err.println("      a2="+a2);
    System.err.println("      b1="+b1);
    System.err.println("      b2="+b2);
    System.err.println("      !(a2 <= b1 || a1 >= b2)="+result);
    //System.err.println("      !(a2 < b1 || a1 > b2)="+(!(a2 < b1 || a1 > b2)));
		 */
		return result;
	}

	public static boolean intersects(Date date, Date firstDate, Date lastDate) {

		long a1 = (firstDate != null) ? firstDate.getTime() : Long.MIN_VALUE;
		long a2 = (lastDate != null) ? lastDate.getTime() : Long.MAX_VALUE;
		long time = date.getTime();
		boolean result = time >= a1 && time <= a2;

		/*
    System.err.println("    intersects("+effectiveDate1+", "+expiryDate1+", "+effectiveDate2+", "+expiryDate2+")");
    System.err.println("      a1="+a1);
    System.err.println("      a2="+a2);
    System.err.println("      b1="+b1);
    System.err.println("      b2="+b2);
    System.err.println("      !(a2 <= b1 || a1 >= b2)="+result);
    //System.err.println("      !(a2 < b1 || a1 > b2)="+(!(a2 < b1 || a1 > b2)));
		 */
		return result;
	}

	public static boolean intersects(double amount, double fromAmount, double toAmount) {
		return amount >= fromAmount && amount <= toAmount;
	}

	public String  toXML() throws IllegalAccessException, NoSuchMethodException,
	InvocationTargetException {

		StringBuffer  xml = new StringBuffer();
		String  className = getBaseName( getClass().getName() );
		xml.append( "<class name=\"" );
		xml.append( className );
		xml.append( "\">\n" );
		Field[]  fields = getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {

			String  fieldVal = null;
			if (fields[i].getType().getName().equals(STRING_SYMBOL)) {
				Object  obj = fields[i].get( this );

				if (obj != null) {
					fieldVal = (String)obj;
				} else {
					fieldVal = "";
				}
				fieldVal = addValueTag(fieldVal);

			} else if (fields[i].getType().getName().equals(STRING_ARRAY_SYMBOL)) {

				fieldVal = "";
				String[] stringarray = (String[])fields[i].get(this);

				if (stringarray != null) {
					for (int j = 0;j < stringarray.length;j++) {
						if (stringarray[j] != null) {
							fieldVal = fieldVal + "<value>" +
									stringarray[j] + "</value>";
						} else {
							fieldVal = fieldVal + "<value></value>";
						}
					}
				}
			} else if (fields[i].getType().getName().equals(BOOLEAN_ARRAY_SYMBOL)) {
				fieldVal = "";
				boolean[] booleanarray = (boolean[])fields[i].get(this);
				if (booleanarray != null) {
					for (int j = 0;j < booleanarray.length;j++) {
						fieldVal = fieldVal + "<value>" +
								String.valueOf(booleanarray[j]) + "</value>";
					}
				}
			} else if (fields[i].getType().getName().equals(INTEGER_ARRAY_SYMBOL)) {
				fieldVal = "";
				int[] integerarray = (int[])fields[i].get(this);
				if (integerarray != null) {
					for (int j = 0;j < integerarray.length;j++) {
						fieldVal = fieldVal + "<value>" +
								Integer.toString(integerarray[j]) + "</value>";
					}
				}
			} else if (fields[i].getType().getName().equals(LONG_ARRAY_SYMBOL)) {
				fieldVal = "";
				long[] longarray = (long[])fields[i].get(this);
				if (longarray != null) {
					for (int j = 0;j < longarray.length;j++) {
						fieldVal = fieldVal + "<value>" +
								Long.toString(longarray[j]) + "</value>";
					}
				}
			} else if (fields[i].getType().getName().equals(FLOAT_ARRAY_SYMBOL)) {
				fieldVal = "";
				float[] floatarray = (float[])fields[i].get(this);
				if (floatarray != null) {
					for (int j = 0;j < floatarray.length;j++) {
						fieldVal = fieldVal + "<value>" +
								Float.toString(floatarray[j]) + "</value>";
					}
				}
			} else if (fields[i].getType().getName().equals(DOUBLE_ARRAY_SYMBOL)) {
				fieldVal = "";
				double[] doublearray = (double[])fields[i].get(this);
				if (doublearray != null) {
					for (int j = 0;j < doublearray.length;j++) {
						fieldVal = fieldVal + "<value>" +
								Double.toString(doublearray[j]) + "</value>";
					}
				}
			} else if (fields[i].getType().getName().equals(LONG_SYMBOL)) {
				fieldVal = Long.toString( fields[i].getLong(this));
				fieldVal = addValueTag(fieldVal);

			} else if (fields[i].getType().getName().equals(INT_SYMBOL)) {
				fieldVal = Integer.toString( fields[i].getInt(this));
				fieldVal = addValueTag(fieldVal);

			} else if (fields[i].getType().getName().equals(BOOLEAN_SYMBOL)) {
				fieldVal = String.valueOf( fields[i].getBoolean(this));
				fieldVal = addValueTag(fieldVal);

			} else if (fields[i].getType().getName().equals(DOUBLE_SYMBOL)) {
				fieldVal = Double.toString( fields[i].getDouble(this));
				fieldVal = addValueTag(fieldVal);

			} else if (fields[i].getType().getName().equals(FLOAT_SYMBOL)) {
				fieldVal = Float.toString( fields[i].getFloat(this));
				fieldVal = addValueTag(fieldVal);

			} else if (fields[i].getType().getName().equals(DATE_SYMBOL)) {
				Object  obj = fields[i].get(this);
				if (obj != null) {
					fieldVal = ((Date)obj).toString();
				} else {
					fieldVal = "";
				}
				fieldVal = addValueTag(fieldVal);

			} else if (fields[i].getType().getSuperclass() == Info.class) {
				Object  obj = fields[i].get(this);
				if (obj == null) {
					fieldVal = "";
				} else {
					Method  method = fields[i].getType().getMethod("toXML", null);
					if (method == null) {
						throw new RuntimeException("Cannot find toXML() method!");
					}
					fieldVal = (String)method.invoke( obj, new Object[0] );
				}
				fieldVal = addValueTag(fieldVal);

			} else {
				// We don't know how to handle that type. So just skip it.
				continue;
			}

			String  fieldName = fields[i].getName();

			xml.append("<field name=\"");
			xml.append(fieldName).append("\">\n");
			xml.append(fieldVal);
			xml.append("\n</field>\n");
		}
		xml.append("</class>\n");

		return xml.toString();
	}

	private String addValueTag(String fieldValue) {
		return "<value>" + fieldValue + "</value>";
	}

	public final static String getBaseName(String fullNameA) {
		int  idx = fullNameA.lastIndexOf(".");
		return fullNameA.substring( idx + 1 );
	}

	public Object clone() {
		try {
			Info o = (Info)super.clone();
			return o;

		} catch(CloneNotSupportedException e) {
			throw new InternalError(this + " is not cloneable");
		}
	}

	public final static boolean compare(String s1, String s2) {
		return s1 == s2 || (s1 != null && s1.equals(s2));
	}

	public final static Date cloneDate(Date date) {
		if(date != null) {
			date = (Date)date.clone();
		}
		return date;
	}

	public final static Info clone(Info object) {
		if(object != null) {
			object = (Info)object.clone();
		}
		return object;
	}

	/**
	 * This method is called by the parameter logging aspect to output
	 * the Info object's values for debugging purposes.  By default,
	 * it calls the toString() method.  If the default toString() returns
	 * too much data or sensitive data that should not be written to a
	 * log file, you should override this method.
	 * @return
	 */
	public String toStringForLogging() {
		return toString();
	}
}