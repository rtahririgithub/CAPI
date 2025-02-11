package com.telus.cmsc.service;

import java.util.List;

import com.telus.cmsc.domain.artifact.Environment;

/**
 * @author Pavel Simonovsky	
 *
 */
public interface EnvironmentService {

	List<Environment> getEnvironments();
	
	Environment getEnvironment(int environmentId);

	Environment getEnvironmentByCode(String environmentCode);
	
	Environment saveEnvironment(Environment environment);
	
	void deleteEnvironment(Integer environmentId);
	
}
