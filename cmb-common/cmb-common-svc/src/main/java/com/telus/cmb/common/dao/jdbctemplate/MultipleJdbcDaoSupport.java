package com.telus.cmb.common.dao.jdbctemplate;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class MultipleJdbcDaoSupport extends MultipleJdbcDaoTemplateSupport {
	
	public void setKnowbilityJdbcDataSource(DataSource knowbilityJdbcDataSource) {
		setKnowbilityJdbcTemplate(createJdbcTemplate(knowbilityJdbcDataSource));
	}
	
	public void setKnowbilityNamedParameterJdbcDataSource(DataSource knowbilityJdbcDataSource) {
		setKnowbilityNamedParameterJdbcTemplate(createNamedJdbcTemplate(knowbilityJdbcDataSource));
	}
	public void setEcpcsJdbcDataSource(DataSource ecpcsJdbcDataSource) {
		setEcpcsJdbcTemplate(createJdbcTemplate(ecpcsJdbcDataSource));
	}
	public void setEasJdbcDataSource(DataSource easJdbcDataSource) {
		setEasJdbcTemplate(createJdbcTemplate(easJdbcDataSource));
	}
	public void setDistJdbcDataSource(DataSource distJdbcDataSource) {
		setDistJdbcTemplate(createJdbcTemplate(distJdbcDataSource));
	}
	@Deprecated 
	//As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more.
	public void setConeJdbcDataSource(DataSource coneJdbcDataSource) {
		setConeJdbcTemplate(createJdbcTemplate(coneJdbcDataSource));
	}
	public void setCodsJdbcDataSource(DataSource codsJdbcDataSource) {
		setCodsJdbcTemplate(createJdbcTemplate(codsJdbcDataSource));
	}
	public void setServJdbcDataSource(DataSource servJdbcDataSource) {
		setServJdbcTemplate(createJdbcTemplate(servJdbcDataSource));
	}
	public void setKnowbilityRefJdbcDataSource(DataSource knowbilityRefJdbcDataSource) {
		setKnowbilityRefJdbcTemplate(createJdbcTemplate(knowbilityRefJdbcDataSource));
	}
	public void setCcEventsJdbcDataSource(DataSource ccEventsJdbcDataSource) {
		setCcEventsJdbcTemplate(createJdbcTemplate(ccEventsJdbcDataSource));
	}
	public void setCconJdbcDataSource(DataSource cconJdbcDataSource) {
		setCconJdbcTemplate(createJdbcTemplate(cconJdbcDataSource));
	}
	public void setEmcmJdbcDataSource(DataSource emcmJdbcDataSource) {
		setEmcmJdbcTemplate(createJdbcTemplate(emcmJdbcDataSource));
	}

	protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	protected NamedParameterJdbcTemplate createNamedJdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
}
