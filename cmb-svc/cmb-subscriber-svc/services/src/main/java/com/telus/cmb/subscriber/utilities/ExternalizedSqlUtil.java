package com.telus.cmb.subscriber.utilities;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;

import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;

public class ExternalizedSqlUtil extends MultipleJdbcDaoTemplateSupport {

	private String sqlGetSubListByBANAndSubIDsIncludeCancelledSub;
	private static final Logger LOGGER = Logger.getLogger(ExternalizedSqlUtil.class);
	
	@PostConstruct
    public void init() {
		try {
			sqlGetSubListByBANAndSubIDsIncludeCancelledSub = getQueryFromFile("GetSubListByBANAndSubIDsIncludeCancelledSub");
		} catch (Throwable t) {
			LOGGER.error("Failed to read GetSubListByBANAndSubIDsIncludeCancelledSub.sql due to error ", t);
			sqlGetSubListByBANAndSubIDsIncludeCancelledSub = null;
		}
    }

	public String getSqlGetSubListByBANAndSubIDsIncludeCancelledSub() {
		return sqlGetSubListByBANAndSubIDsIncludeCancelledSub;
	}
}
