package com.telus.cmb.tool.services.log.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.cmb.tool.services.log.config.domain.AppMap;
import com.telus.cmb.tool.services.log.domain.RAPiDRelease;

public class EmtoolsDaoImpl extends JdbcDaoSupport implements EmtoolsDao {

	@Override
	public List<AppMap> getAppMap(final String appMapName) {
		
		String sql = "select e.name as environment, d.name as domain, substring_index(substring_index(xref.target, ',', n.digit+1), ',', -1) cluster, s.Name as node, s.Machine as host, "
				+ "adms.ListenAddress as adminhost "
				+ "from envmatrixall.am_map xref "
				+ "left join envmatrixall.am_application a on xref.targetgroupID = a.targetgroupID "
				+ "left join envmatrixall.wls_domain d on xref.domainID = d.domainID "
				+ "left join envmatrixall.wls_domain_conf dc on d.domainID = dc.ID "
				+ "left join envmatrixall.environment e on dc.environmentID = e.ID "
				+ "left join envmatrixall.wls_cluster c on c.domainID = d.domainID "
				+ "left join envmatrixall.wls_server s on s.domainID = d.domainID and s.Cluster = c.name "
				+ "left join envmatrixall.wls_server adms on adms.domainID = d.domainID and adms.Name = d.AdminServerName "
				+ "inner join (select 0 digit union all select 1 union all select 2 union all select 3) n on length(replace(xref.target, ',' , '')) <= length(xref.target)-n.digit "
				+ "where a.name = ? and substring_index(substring_index(xref.target, ',', n.digit+1), ',', -1) = c.name order by deploymentfile, e.name, d.name, s.Name";
				
		return getJdbcTemplate().query(sql, new String[] { appMapName }, new RowMapper<AppMap>() {

			@Override
			public AppMap mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				AppMap appMap = new AppMap();
				appMap.setName(appMapName);
				appMap.setEnvironment(rs.getString("environment"));
				appMap.setDomain(rs.getString("domain"));
				appMap.setCluster(rs.getString("cluster"));
				appMap.setNode(rs.getString("node"));
				appMap.setHost(getHostname(rs.getString("host")));
				
				return appMap;
			}
			
			private String getHostname(String hostname) {				
				return (StringUtils.contains(hostname, ".")) ? hostname.substring(0, hostname.indexOf(".")) : hostname;
			}

		});
	}

	@Override
	public List<RAPiDRelease> getActiveReleases() {

		String sql = "select id, name, release_date from rapidpr.rpd_release where release_date > curdate()";
		
		return getJdbcTemplate().query(sql, new String[] {}, new RowMapper<RAPiDRelease>() {

			@Override
			public RAPiDRelease mapRow(ResultSet rs, int rowNum) throws SQLException {				
				RAPiDRelease release = new RAPiDRelease();
				release.setId(rs.getInt("id"));
				release.setName(rs.getString("name"));
				release.setReleaseDate(rs.getDate("release_date"));				
				return release;
			}
		});
	}

	@Override
	public List<String> getLdifpaths(int releaseId) {
		
		String sql = "select buildpath from rapidpr.rpd_deployment where application_id in ('550', '445', '563') and valid = 1 and buildpath like '%ldif' and release_id = ?";
		
		return getJdbcTemplate().query(sql, new Integer[] { releaseId }, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {				
				return rs.getString("buildpath");
			}
		});
	}

}
