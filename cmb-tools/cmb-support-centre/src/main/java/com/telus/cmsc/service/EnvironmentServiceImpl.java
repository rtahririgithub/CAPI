package com.telus.cmsc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telus.cmsc.dao.EnvironmentDao;
import com.telus.cmsc.dao.ReferenceVersionDao;
import com.telus.cmsc.domain.artifact.Environment;

/**
 * @author Pavel Simonovsky	
 *
 */

@Service
public class EnvironmentServiceImpl implements EnvironmentService {

	@Autowired
	private EnvironmentDao environmentDao;
	
	@Autowired
	private ReferenceVersionDao versionDao;

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.portal.application.service.EnvironmentManagementService#getEnvironments()
	 */
	@Override
	public List<Environment> getEnvironments() {
		return environmentDao.findEnvironments();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.portal.application.service.EnvironmentManagementService#saveEnvironment(com.telus.cmbsc.domain.model.Environment)
	 */
	@Override
	public Environment saveEnvironment(Environment environment) {
		if (environment.getEnvironmentId() == null) {
			environmentDao.createEnvironment(environment);
		} else {
			environmentDao.updateEnvironment(environment);
		}
		return environment;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.portal.application.service.EnvironmentManagementService#deleteEnvironment(java.lang.Integer)
	 */
	@Override
	public void deleteEnvironment(Integer environmentId) {
		Environment environment = getEnvironment(environmentId);
		environmentDao.deleteEnvironment(environment);
		versionDao.deleteEnvironmentVersions(environmentId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.portal.application.service.EnvironmentManagementService#getEnvironment(int)
	 */
	@Override
	public Environment getEnvironment(int environmentId) {
		return environmentDao.findEnvironment(environmentId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.EnvironmentService#getEnvironmentByCode(java.lang.String)
	 */
	@Override
	public Environment getEnvironmentByCode(String environmentCode) {
		return environmentDao.findEnvironmentByCode(environmentCode);
	}
}
