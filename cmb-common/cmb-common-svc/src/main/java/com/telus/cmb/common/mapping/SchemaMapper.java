/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.mapping;



/**
 * @author Pavel Simonovsky
 *
 */
public interface SchemaMapper<S, T> {

	S mapToSchema(T source);

	T mapToDomain(S source);
	
}
