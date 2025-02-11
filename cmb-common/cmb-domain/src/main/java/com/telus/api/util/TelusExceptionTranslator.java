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

import com.telus.api.TelusAPIException;

/**
 * @author Pavel Simonovsky
 *
 */
public interface TelusExceptionTranslator {

	TelusAPIException translateException(Throwable throwable);
	
}
