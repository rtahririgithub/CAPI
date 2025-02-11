/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc;

import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.NumberGroupInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public interface ResourceOrderReferenceFacade {

	NumberGroupInfo[] getAvailableNumberGroups(String accountType, String accountSubType, String productType, String equipmentType, String marketAreaCode) throws TelusException;

	NumberGroupInfo getNumberGroupByPhoneNumberAndProductType(String phoneNumber, String productType) throws TelusException;

	NumberGroupInfo getNumberGroupByPortedInPhoneNumberAndProductType(String phoneNumber, String productType) throws TelusException;
	
}
