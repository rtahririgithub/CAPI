/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.enterprise_10;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.LanguageInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.Language;

/**
 * @author Pavel Simonovsky
 *
 */
public class LanguageMapper extends ReferenceMapper<Language, LanguageInfo>{

	public LanguageMapper() {
		super(Language.class, LanguageInfo.class);
	}
}
