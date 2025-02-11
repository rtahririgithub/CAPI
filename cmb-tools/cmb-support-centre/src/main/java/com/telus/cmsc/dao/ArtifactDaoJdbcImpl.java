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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.RowMapper;

import com.telus.cmsc.domain.artifact.Artifact;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ArtifactDaoJdbcImpl extends BaseJdbcDao implements ArtifactDao {

	public static class ArtifactRowMapper implements RowMapper<Artifact> {
		
		/*
		 * (non-Javadoc)
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public Artifact mapRow(ResultSet rs, int rowNum) throws SQLException {
			Artifact artifact = new Artifact();
			
			artifact.setArtifactId(rs.getInt("artifact_id"));
			artifact.setCode(rs.getString("code_txt"));
			artifact.setName(rs.getString("name_txt"));
			artifact.setDescription(rs.getString("desc_txt"));
			artifact.setLogPathPattern(rs.getString("log_path_pattern_txt"));

			return artifact;
		}
	}
	
	public ArtifactDaoJdbcImpl(DataSource dataSource) {
		super(dataSource);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactDao#findArtifacts()
	 */
	@Override
	public List<Artifact> findArtifacts() {
		return getJdbcTemplate().query("select * from artifact", new ArtifactRowMapper());
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactDao#findArtifact(java.lang.Integer)
	 */
	@Override
	@Cacheable("artifacts")
	public Artifact findArtifact(Integer artifactId) {
		return getJdbcTemplate().queryForObject("select * from artifact where artifact_id = ?", new ArtifactRowMapper(), artifactId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactDao#findArtifactByCode(java.lang.String)
	 */
	@Override
	@Cacheable("artifacts")
	public Artifact findArtifactByCode(String artifactCode) {
		List<Artifact> artifacts = getJdbcTemplate().query("select * from artifact where code_txt = ?", new ArtifactRowMapper(), artifactCode); 
		return artifacts.isEmpty() ? null : artifacts.get(0);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactDao#createArtifact(com.telus.cmbsc.domain.model.Artifact)
	 */
	@Override
	@CacheEvict(value = "artifacts", allEntries = true)
	public Artifact createArtifact(Artifact artifact) {
		artifact.setArtifactId(generateEntityId("seq_artifact"));
		String sql = "insert into artifact (code_txt, name_txt, desc_txt, log_path_pattern_txt, artifact_id) values (?,?,?,?,?)";
		return persistArtifact(artifact, sql);
	}

	private Artifact persistArtifact(Artifact artifact, String sql) {
		getJdbcTemplate().update(sql, artifact.getCode(), artifact.getName(), artifact.getDescription(), artifact.getLogPathPattern(), artifact.getArtifactId());
		return artifact;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactDao#updateArtifact(com.telus.cmbsc.domain.model.Artifact)
	 */
	@Override
	@CacheEvict(value = "artifacts", allEntries = true)
	public Artifact updateArtifact(Artifact artifact) {
		String sql = "update artifact set code_txt = ?, name_txt = ?, desc_txt = ?, log_path_pattern_txt = ? where artifact_id = ?";
		return persistArtifact(artifact, sql);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactDao#deleteArtifact(com.telus.cmbsc.domain.model.Artifact)
	 */
	@Override
	public void deleteArtifact(Artifact artifact) {
		getJdbcTemplate().update("delete from artifact where artifact_id = ?", artifact.getArtifactId());
	}

}
