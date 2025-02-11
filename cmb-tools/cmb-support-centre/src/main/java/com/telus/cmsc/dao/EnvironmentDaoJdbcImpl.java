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
import java.util.List;

import javax.sql.DataSource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.telus.cmsc.domain.artifact.Environment;

/**
 * @author Pavel Simonovsky	
 *
 */
public class EnvironmentDaoJdbcImpl extends BaseJdbcDao implements EnvironmentDao {

	public class EnvironmentMapper implements RowMapper<Environment> {
	
		/*
		 * (non-Javadoc)
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public Environment mapRow(ResultSet rs, int rowNum) throws SQLException {
			Environment environment = new Environment();
			environment.setEnvironmentId(rs.getInt("environment_id"));
			environment.setCode(rs.getString("code_txt"));
			environment.setDescription(rs.getString("desc_txt"));
			environment.setLdapUrl(rs.getString("ldap_url"));
			environment.setName(rs.getString("name_txt"));
			environment.setFlipperMember(rs.getString("flipper_flag").equalsIgnoreCase("y"));
			environment.setConfigCode(rs.getString("config_code_txt"));
			return environment;
		}
	}
	
	private EnvironmentMapper enviromentMapper = new EnvironmentMapper();
	
	public EnvironmentDaoJdbcImpl(DataSource dataSource) {
		super(dataSource);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.EnvironmentRepository#findEnvironments()
	 */
	@Override
	public List<Environment> findEnvironments() {
		return getJdbcTemplate().query("select * from environment", new EnvironmentMapper());
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.EnvironmentRepository#findEnvironment(java.lang.Integer)
	 */
	@Override
	@Cacheable("environments")
	public Environment findEnvironment(Integer environmentId) {
		return getJdbcTemplate().queryForObject("select * from environment where environment_id = ?", enviromentMapper, environmentId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.EnvironmentDao#findEnvironmentByCode(java.lang.String)
	 */
	@Override
	@Cacheable("environments")
	public Environment findEnvironmentByCode(String environmentCode) {
		List<Environment> environments = getJdbcTemplate().query("select * from environment where code_txt = ?", enviromentMapper, environmentCode);
		return environments.isEmpty() ? null : environments.get(0);	
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.EnvironmentRepository#createEnvironment(com.telus.cmbsc.domain.model.Environment)
	 */
	@Override
	@CacheEvict(value="environments", allEntries = true)
	public Environment createEnvironment(Environment environment) {
		environment.setEnvironmentId(generateEntityId("seq_environment"));
		String sql = "insert into environment (code_txt, name_txt, desc_txt, ldap_url, flipper_flag, config_code_txt, environment_id) values(?, ?, ?, ?, ?, ?, ?)";
		return persistEnvironment(environment, sql);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.EnvironmentRepository#updateEnvironment(com.telus.cmbsc.domain.model.Environment)
	 */
	@Override
	@CacheEvict(value="environments", allEntries = true)
	public Environment updateEnvironment(Environment environment) {
		String sql = "update environment set code_txt = ?, name_txt = ?, desc_txt = ?, ldap_url = ?, flipper_flag = ?, config_code_txt = ?  where environment_id = ?";
		return persistEnvironment(environment, sql);
	}
	
	private Environment persistEnvironment(final Environment environment, String sql) {

		getJdbcTemplate().update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, environment.getCode());
				ps.setString(2, environment.getName());
				ps.setString(3, environment.getDescription());
				ps.setString(4, environment.getLdapUrl());
				ps.setString(5, environment.isFlipperMember() ? "Y" : "N");
				ps.setString(6, environment.getConfigCode());
				ps.setInt(7, environment.getEnvironmentId());
			}
		});
		
		return environment;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmbsc.domain.repository.EnvironmentRepository#deleteEnvironment(com.telus.cmbsc.domain.model.Environment)
	 */
	@Override
	@CacheEvict(value="environments", allEntries = true)
	public void deleteEnvironment(Environment environment) {
		getJdbcTemplate().update("delete from environment where environment_id = ?", environment.getEnvironmentId());

	}

}
