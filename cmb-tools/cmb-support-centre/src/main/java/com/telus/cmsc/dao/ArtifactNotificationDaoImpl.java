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
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.cmsc.domain.artifact.ArtifactNotification;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ArtifactNotificationDaoImpl extends JdbcDaoSupport implements ArtifactNotificationDao  {

	public ArtifactNotificationDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Cacheable("artifactNotifications")
	public Collection<ArtifactNotification> getNotifications(String logicalEnvironmentName) {
		
		String sql = "select n.armx_notification_id, n.update_ts, e.environment_cd, n.version_txt, "
				+ "m.module_cd, s.cluster_nm, s.domain_nm, s.node_nm, s.host_nm, s.port_num, s.admin_host_nm, s.admin_port_num "
				+ "from armx_notification n, armx_environment e, armx_module m, armx_server s "
				+ "where n.armx_environment_id = e.armx_environment_id "
				+ "and n.armx_module_id = m.armx_module_id "
				+ "and s.armx_server_id = n.armx_server_id";
		
		return getJdbcTemplate().query(sql, new RowMapper<ArtifactNotification>() {
			
			@Override
			public ArtifactNotification mapRow(ResultSet rs, int rowNum) throws SQLException {

				ArtifactNotification notification = new ArtifactNotification();
				
				notification.setNotificationId(rs.getInt("armx_notification_id"));
				notification.setArtifactCode(rs.getString("module_cd"));
				notification.setEnvironmentCode(rs.getString("environment_cd"));
				notification.setVersion(rs.getString("version_txt"));
				notification.setDomainName(rs.getString("domain_nm"));
				notification.setClusterName(rs.getString("cluster_nm"));
				notification.setNodeName(rs.getString("node_nm"));
				notification.setHostName(rs.getString("host_nm"));
				notification.setPortNumber(rs.getInt("port_num"));
				notification.setAdminHostName(rs.getString("admin_host_nm"));
				notification.setAdminPortNumber(rs.getInt("admin_port_num"));
				notification.setTime(rs.getTimestamp("update_ts"));
				
				return notification;
			}
		});
	}
}
