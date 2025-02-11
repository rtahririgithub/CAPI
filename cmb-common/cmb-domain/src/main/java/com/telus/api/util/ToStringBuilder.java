/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.api.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * @author Pavel Simonovsky
 *
 */
public class ToStringBuilder {

	private static final int FIELD_OFFSET = 3;


	public static String toString(Object obj) {
		StringBuffer buffer = new StringBuffer();
		addObject(obj, buffer, FIELD_OFFSET);
		return buffer.toString();
	}

	private static String getOffsetString(int length) {
		char [] chars = new char[length];
		for (int idx = 0; idx < length; idx++) {
			chars[idx] = ' ';
		}
		return new String(chars);
	}
	
	private static StringBuffer addObject(Object obj, StringBuffer buffer, int offset) {
		buffer.append(obj.getClass().getName()+ "'@" + obj.hashCode() +":");
		buffer.append("\n").append(getOffsetString(offset - FIELD_OFFSET)).append("[");
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor [] descriptors = beanInfo.getPropertyDescriptors();
			for (int idx = 0; idx < descriptors.length; idx++) {
				PropertyDescriptor descriptor = descriptors[idx];
				if (!descriptor.getName().equals("class")) {
					Method readMethod = descriptors[idx].getReadMethod();
					if (readMethod == null) {
//						System.err.println("Unable to find getter for property [" + descriptors[idx].getName() + "] on class [" + beanInfo.getBeanDescriptor().getName() + "]");
					} else {
						Object value = readMethod.invoke(obj, new Object[0]);
						addField(descriptor.getName(), value, buffer, offset);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}				
		buffer.append("\n").append(getOffsetString(offset - FIELD_OFFSET)).append("]");
		return buffer;
	}
	
	private static StringBuffer addField(String fieldName, Object fieldValue, StringBuffer buffer, int offset) {
		buffer.append("\n");
		buffer.append(getOffsetString(offset));
		buffer.append(fieldName);
		buffer.append(" = ");
		addFieldValue(fieldValue, buffer, offset + FIELD_OFFSET);
		return buffer;
	}
	
	private static StringBuffer addFieldValue(Object value, StringBuffer buffer, int offset) {
		
		if (value == null) {
			buffer.append("null");
		} else if (isSimpleObject(value)) {
			addSimpleObjectValue(value, buffer, offset);
		} else if (value.getClass().isArray()) {
	        addArrayValue((Object []) value, buffer, offset);
		} else if (value instanceof Collection) {
	        addCollectionValue((Collection) value, buffer, offset);
		} else {
			addObject(value, buffer, offset);
		}

		return buffer;

	}
	
	private static boolean isSimpleObject(Object o) {
		if (o instanceof String || o instanceof Number || o instanceof Date || o instanceof Timestamp) {
			return true;
		}
		return false;
	}

	private static StringBuffer addSimpleObjectValue(Object value, StringBuffer buffer, int offset) {

		if (value instanceof String) {
			buffer.append("'").append(value).append("'");
		} else {
			buffer.append(value.toString());
		}

		return buffer;
	}

	private static StringBuffer addArrayValue(Object [] array, StringBuffer buffer, int offset) {

		buffer.append("array <").append(array.getClass().getComponentType().getName() +
		        ">, size = " + array.length + " :");
		buffer.append("\n").append(getOffsetString(offset - FIELD_OFFSET)).append("[");

		if (array.length == 0) {
			buffer.append("\n").append(getOffsetString(offset)).append("<empty>");
		} else {
			for (int idx = 0; idx < array.length; idx++) {
				buffer.append("\n").append(getOffsetString(offset));
				Object value = array[idx];
				addFieldValue(value, buffer, offset + FIELD_OFFSET);
			}
		}
		buffer.append("\n").append(getOffsetString(offset - FIELD_OFFSET)).append("]");
		return buffer;
	}
	
	private static StringBuffer addCollectionValue(Collection collection, StringBuffer buffer, int offset) {

		buffer.append("collection ").append(", size = " + collection.size() + " :");
		buffer.append("\n").append(getOffsetString(offset - FIELD_OFFSET)).append("[");

		if (collection.size() == 0) {
			buffer.append("\n").append(getOffsetString(offset)).append("<empty>");
		} else {
			Iterator iter = collection.iterator();
			
			while (iter.hasNext()) {
				buffer.append("\n").append(getOffsetString(offset));
				addFieldValue(iter.next(), buffer, offset + FIELD_OFFSET);
			}
		}
		buffer.append("\n").append(getOffsetString(offset - FIELD_OFFSET)).append("]");
		return buffer;
	}
	
}
