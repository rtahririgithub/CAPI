/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.dao;

import java.util.List;

import com.telus.cmsc.domain.artifact.Environment;

/**
 * @author Pavel Simonovsky	
 *
 */
public interface EnvironmentDao {

	List<Environment> findEnvironments();
	
	Environment findEnvironment(Integer environmentId);

	Environment findEnvironmentByCode(String environmentCode);
	
	Environment createEnvironment(Environment environment);

	Environment updateEnvironment(Environment environment);
	
	void deleteEnvironment(Environment environment);
	
}
