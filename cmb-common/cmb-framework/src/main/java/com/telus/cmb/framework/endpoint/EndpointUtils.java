/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.endpoint;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanUtils;

/**
 * @author Pavel Simonovsky
 *
 */
public class EndpointUtils {

	@SuppressWarnings("unchecked")
	public static <T> T getValue(Object target, Class<T> propertyType) {
		try {
			PropertyDescriptor descriptor = getPropertyDescriptor(target.getClass(), propertyType);
			if (descriptor != null) {
				return (T) descriptor.getReadMethod().invoke(target, new Object[0]);
			}
		} catch (Exception e) {
			throw new RuntimeException(String.format("Unable to get value of type [%s] from [%s]: %s", propertyType, target, e.getMessage()), e);
		}
		return null;
	}
	
	public static void setValue(Object target, Object value) {
		try {
			PropertyDescriptor descriptor = getPropertyDescriptor(target.getClass(), value.getClass());
			if (descriptor != null) {
				descriptor.getWriteMethod().invoke(target, value);
			}
		} catch (Exception e) {
			throw new RuntimeException(String.format("Unable to set value [%s] to [%s]: %s", target, value, e.getMessage()), e);
		}
	}
	
	private static PropertyDescriptor getPropertyDescriptor(Class<?> targetType, Class<?> propertyType) {
		for (PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(targetType)) {
			if (propertyType.isAssignableFrom(descriptor.getPropertyType())) {
				return descriptor;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newPropertyValue(Object target, Class<T> propertyBaseType) {
		try {
			PropertyDescriptor descriptor = getPropertyDescriptor(target.getClass(), propertyBaseType);
			if (descriptor != null) {
				T value = (T) descriptor.getPropertyType().newInstance();
				descriptor.getWriteMethod().invoke(target, value);
				return value;
			}
		} catch (Exception e) {
			throw new RuntimeException(String.format("Unable to instantiate property value [%s] for [%s]: %s", propertyBaseType, target, e.getMessage()), e);
		}
		return null;
	}
}
