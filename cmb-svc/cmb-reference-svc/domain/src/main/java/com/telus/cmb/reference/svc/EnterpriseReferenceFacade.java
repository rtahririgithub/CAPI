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
import com.telus.eas.utility.info.CountryInfo;
import com.telus.eas.utility.info.LanguageInfo;
import com.telus.eas.utility.info.ProvinceInfo;
import com.telus.eas.utility.info.StateInfo;
import com.telus.eas.utility.info.UnitTypeInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public interface EnterpriseReferenceFacade {

	CountryInfo[] getCountries() throws TelusException;

	LanguageInfo[] getLanguages() throws TelusException;

	ProvinceInfo[] getProvinces() throws TelusException;

	StateInfo[] getStates() throws TelusException;

	UnitTypeInfo[] getUnitTypes() throws TelusException;

}
