/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telus.cmsc.dao.ArtifactDao;
import com.telus.cmsc.dao.ArtifactGroupDao;
import com.telus.cmsc.dao.ReferenceVersionDao;
import com.telus.cmsc.domain.artifact.Artifact;
import com.telus.cmsc.domain.artifact.ArtifactGroup;
import com.telus.cmsc.domain.artifact.ReferenceVersion;

/**
 * @author Pavel Simonovsky	
 *
 */
@Service
public class ArtifactServiceImpl implements ArtifactService {

	@Autowired
	private ArtifactDao artifactDao;

	@Autowired
	private ArtifactGroupDao groupDao;
	
	@Autowired
	private ReferenceVersionDao versionDao;
	
	@Autowired
	private EnvironmentService environmentService;
	
	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#getAllArtifacts()
	 */
	@Override
	public Collection<Artifact> getArtifacts() {
		return artifactDao.findArtifacts();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#getArtifact(int)
	 */
	@Override
	public Artifact getArtifact(int artifactId) {
		return artifactDao.findArtifact(artifactId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#getArtifactByCode(java.lang.String)
	 */
	@Override
	public Artifact getArtifactByCode(String artifactCode) {
		return artifactDao.findArtifactByCode(artifactCode);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#saveArtifact(com.telus.cmbsc.domain.model.Artifact, java.util.Collection)
	 */
	@Override
	public Artifact saveArtifact(Artifact artifact, Collection<Integer> groupIds) {
		if (artifact.getArtifactId() == null) {
			artifactDao.createArtifact(artifact);
		} else {
			artifactDao.updateArtifact(artifact);
		}
		
		if (groupIds != null) {
			groupDao.setArtifactGroups(artifact.getArtifactId(), groupIds);
		}
		
		return artifact;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#deleteArtifact(int)
	 */
	@Override
	public void deleteArtifact(int artifactId) {
		Artifact artifact = artifactDao.findArtifact(artifactId);
		artifactDao.deleteArtifact(artifact);
		versionDao.deleteArtifactVersions(artifactId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#getAllGroups()
	 */
	@Override
	public Collection<ArtifactGroup> getGroups() {
		return groupDao.findGroups();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#getGroup(int)
	 */
	@Override
	public ArtifactGroup getGroup(int groupId) {
		return groupDao.findGroup(groupId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#saveGroup(com.telus.cmbsc.domain.model.ArtifactGroup)
	 */
	@Override
	public ArtifactGroup saveGroup(ArtifactGroup group, Collection<Integer> artifactIds) {
		
		if (group.getGroupId() == null) {
			groupDao.createGroup(group);
		} else {
			groupDao.updateGroup(group);
		}
		
		if (artifactIds != null) {
			groupDao.setGroupArtifacts(group.getGroupId(), artifactIds);
		}
		
		return  group;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#deleteGroup(int)
	 */
	@Override
	public void deleteGroup(int groupId) {
		ArtifactGroup group = getGroup(groupId);
		groupDao.deleteGroup(group);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#getGroupArtifacts(int)
	 */
	@Override
	public Collection<Artifact> getGroupArtifacts(int groupId) {
		List<Artifact> result = new ArrayList<Artifact>();
		for (Integer artifactId : groupDao.getGroupArtifactIds(groupId)) {
			result.add(artifactDao.findArtifact(artifactId));
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#getArtifactGroups(int)
	 */
	@Override
	public Collection<ArtifactGroup> getArtifactGroups(int artifactId) {
		List<ArtifactGroup> result = new ArrayList<ArtifactGroup>();
		for (Integer groupId : groupDao.getArtifactGroupIds(artifactId)) {
			result.add(groupDao.findGroup(groupId));
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#getReferenceVersion(int, int)
	 */
	@Override
	public ReferenceVersion getReferenceVersion(int artifactId, int environmentId) {
		return versionDao.findVersion(artifactId, environmentId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactService#saveReferenceVersions(int, java.util.Collection)
	 */
	@Override
	public void saveReferenceVersions(int artifactId, Collection<ReferenceVersion> versions) {
		versionDao.persistArtifactVersions(artifactId, versions);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.modules.artifact.service.ArtifactService#saveReferenceVersion(com.telus.cmbsc.modules.artifact.domain.ReferenceVersion)
	 */
	@Override
	public void saveReferenceVersion(ReferenceVersion version) {
		versionDao.persistVersion(version);
	}
}
