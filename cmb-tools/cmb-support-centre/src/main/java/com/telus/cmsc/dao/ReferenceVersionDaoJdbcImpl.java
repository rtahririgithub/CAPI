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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;

import com.telus.cmsc.domain.artifact.ReferenceVersion;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ReferenceVersionDaoJdbcImpl extends BaseJdbcDao implements ReferenceVersionDao {

	private class ReferenceVersionRowMapper implements RowMapper<ReferenceVersion> {
	
		/*
		 * (non-Javadoc)
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public ReferenceVersion mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReferenceVersion version = new ReferenceVersion();
			version.setArtifactId(rs.getInt("artifact_id"));
			version.setEnvironmentId(rs.getInt("environment_id"));
			version.setVersion(rs.getString("version_txt"));
			version.setNotes(rs.getString("notes_txt"));
			return version;
		}
	}
	
	public ReferenceVersionDaoJdbcImpl(DataSource dataSource) {
		super(dataSource);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ReferenceVersionDao#findVersion(int, int)
	 */
	@Override
	@Cacheable("referenceVersions")
	public ReferenceVersion findVersion(int artifactId, int environmentId) {
		String sql = "select * from reference_version where artifact_id = ? and environment_id = ?";
		List<ReferenceVersion> result = getJdbcTemplate().query(sql, new ReferenceVersionRowMapper(), artifactId, environmentId);
		return result.isEmpty() ? null : result.get(0);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ReferenceVersionDao#deleteArtifactVersions(int)
	 */
	@Override
	@CacheEvict(value="referenceVersions", allEntries = true)
	public void deleteArtifactVersions(int artifactId) {
		getJdbcTemplate().update("delete from reference_version where artifact_id = ?", artifactId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ReferenceVersionDao#deleteEnvironmentVersions(int)
	 */
	@Override
	@CacheEvict(value="referenceVersions", allEntries = true)
	public void deleteEnvironmentVersions(int environmentId) {
		getJdbcTemplate().update("delete from reference_version where environment_id = ?", environmentId);
	}
	
	@CacheEvict(value="referenceVersions", allEntries = true)
	public ReferenceVersion persistVersion(ReferenceVersion version) {
		String sql = "update reference_version set version_txt = ?, notes_txt = ? where artifact_id = ? and environment_id = ?";
		int count = getJdbcTemplate().update(sql, version.getVersion(), version.getNotes(), version.getArtifactId(), version.getEnvironmentId());
		if (count == 0) {
			sql = "insert into reference_version (version_txt, notes_txt, artifact_id, environment_id) values (?, ?, ?, ?)";
			getJdbcTemplate().update(sql, version.getVersion(), version.getNotes(), version.getArtifactId(), version.getEnvironmentId());
		}
		return version;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ReferenceVersionDao#persistArtifactVersions(int, java.util.Collection)
	 */
	@Override
	@CacheEvict(value="referenceVersions", allEntries = true)
	public void persistArtifactVersions(final int artifactId, final Collection<ReferenceVersion> versions) {
		
		deleteArtifactVersions(artifactId);
		
		String sql = "insert into reference_version (artifact_id, environment_id, version_txt, notes_txt) values (?, ?, ?, ?)";
		getJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {
			
			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				for (ReferenceVersion version : versions) {
					ps.setInt(1, artifactId);
					ps.setInt(2, version.getEnvironmentId());
					ps.setString(3, version.getVersion());
					ps.setString(4, version.getNotes());
					ps.executeUpdate();
				}
				return null;
			}
		});
	}
}
