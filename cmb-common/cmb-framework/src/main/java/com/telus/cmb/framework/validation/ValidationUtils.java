/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.validation;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Pavel Simonovsky
 *
 */
public class ValidationUtils {
	
	public static void rejectIfEmpty(String objectName, String value, ValidationResult result) {
		if (StringUtils.isEmpty(value)) {
			result.addError(objectName, "VAL-0001", String.format("Argument [%s] cannot be empty", objectName));
		}
	}
}
