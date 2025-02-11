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

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;

import com.telus.cmsc.domain.artifact.ArtifactGroup;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ArtifactGroupDaoJdbcImpl extends BaseJdbcDao implements ArtifactGroupDao {

	public ArtifactGroupDaoJdbcImpl(DataSource dataSource) {
		super(dataSource);
	}
	
	public class ArtifactGroupRowMapper implements RowMapper<ArtifactGroup> {
		
		/*
		 * (non-Javadoc)
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public ArtifactGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
			ArtifactGroup group = new ArtifactGroup();
			
			group.setGroupId(rs.getInt("group_id"));
			group.setName(rs.getString("name_txt"));
			group.setDescription(rs.getString("desc_txt"));
			
			return group;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactGroupDao#findGroups()
	 */
	@Override
	public List<ArtifactGroup> findGroups() {
		return getJdbcTemplate().query("select * from artifact_group", new ArtifactGroupRowMapper());
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactGroupDao#findGroup(java.lang.Integer)
	 */
	@Override
	public ArtifactGroup findGroup(int groupId) {
		return getJdbcTemplate().queryForObject("select * from artifact_group where group_id = ?", new ArtifactGroupRowMapper(), groupId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactGroupDao#createGroup(com.telus.cmbsc.domain.model.ArtifactGroup)
	 */
	@Override
	public ArtifactGroup createGroup(ArtifactGroup group) {
		group.setGroupId(generateEntityId("seq_artifact_group"));
		String sql = "insert into artifact_group (name_txt, desc_txt, group_id) values (?,?,?)";
		return persistGroup(group, sql);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactGroupDao#updateGroup(com.telus.cmbsc.domain.model.ArtifactGroup)
	 */
	@Override
	public ArtifactGroup updateGroup(ArtifactGroup group) {
		String sql = "update artifact_group set name_txt = ?, desc_txt = ? where group_id = ?";
		return persistGroup(group, sql);
	}
	
	private ArtifactGroup persistGroup(ArtifactGroup group, String sql) {
		getJdbcTemplate().update(sql, group.getName(), group.getDescription(), group.getId());
		return group; 
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactGroupDao#deleteGroup(com.telus.cmbsc.domain.model.ArtifactGroup)
	 */
	@Override
	public void deleteGroup(ArtifactGroup artifact) {
		getJdbcTemplate().update("delete from artifact_group where group_id = ?");
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactGroupDao#getGroupArtifactIds(int)
	 */
	@Override
	public List<Integer> getGroupArtifactIds(int groupId) {
		return getJdbcTemplate().queryForList("select artifact_id from artifact_group_rel where group_id = ?", Integer.class, groupId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactGroupDao#getArtifactGroupIds(int)
	 */
	@Override
	public List<Integer> getArtifactGroupIds(int artifactId) {
		return getJdbcTemplate().queryForList("select group_id from artifact_group_rel where artifact_id = ?", Integer.class, artifactId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactGroupDao#setArtifactGroups(int, java.util.Collection)
	 */
	@Override
	public void setArtifactGroups(final int artifactId, final Collection<Integer> groupIds) {
		
		getJdbcTemplate().update("delete from artifact_group_rel where artifact_id = ?", artifactId);
		getJdbcTemplate().execute("insert into artifact_group_rel (artifact_id, group_id) values (?, ?)", new PreparedStatementCallback<Object>() {
			
			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				for (Integer groupId : groupIds) {
					ps.setInt(1, artifactId);
					ps.setInt(2, groupId);
					ps.executeUpdate();
				}
				return null;
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.ArtifactGroupDao#setGroupArtifacts(int, java.util.Collection)
	 */
	@Override
	public void setGroupArtifacts(final int groupId, final Collection<Integer> artifactIds) {

		getJdbcTemplate().update("delete from artifact_group_rel where group_id = ?", groupId);
		getJdbcTemplate().execute("insert into artifact_group_rel (artifact_id, group_id) values (?, ?)", new PreparedStatementCallback<Object>() {
			
			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				for (Integer artifactId : artifactIds) {
					ps.setInt(1, artifactId);
					ps.setInt(2, groupId);
					ps.executeUpdate();
				}
				return null;
			}
		});
		
	}
}
