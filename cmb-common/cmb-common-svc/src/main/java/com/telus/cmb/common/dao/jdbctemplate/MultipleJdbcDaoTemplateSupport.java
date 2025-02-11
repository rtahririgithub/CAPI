package com.telus.cmb.common.dao.jdbctemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.ResourceUtils;

import com.telus.api.SystemException;
import com.telus.eas.framework.info.TestPointResultInfo;

public class MultipleJdbcDaoTemplateSupport {
	
	protected static final int ORACLE_REF_CURSOR  = -10;
	
	private JdbcTemplate knowbilityJdbcTemplate;
	private JdbcTemplate ecpcsJdbcTemplate;
	private JdbcTemplate easJdbcTemplate;
	private JdbcTemplate distJdbcTemplate;
	@Deprecated
	//As of 2015 Oct release, as part of SERV DB upgrade, this variable is marked as deprecated and should not use any more. 
	private JdbcTemplate coneJdbcTemplate;
	private JdbcTemplate codsJdbcTemplate;
	private JdbcTemplate servJdbcTemplate;
	private JdbcTemplate knowbilityRefJdbcTemplate;
	private JdbcTemplate ccEventsJdbcTemplate;
	private JdbcTemplate cconJdbcTemplate;
	private JdbcTemplate emcmJdbcTemplate;
	private NamedParameterJdbcTemplate knowbilityNamedParameterJdbcTemplate;
	
	private static final Logger LOGGER = Logger.getLogger(MultipleJdbcDaoTemplateSupport.class);
	
	public NamedParameterJdbcTemplate getKnowbilityNamedParameterJdbcTemplate() {
		return knowbilityNamedParameterJdbcTemplate;
	}
	public void setKnowbilityNamedParameterJdbcTemplate(NamedParameterJdbcTemplate knowbilityNamedParameterJdbcTemplate) {
		this.knowbilityNamedParameterJdbcTemplate = knowbilityNamedParameterJdbcTemplate;
	}
	
	public JdbcTemplate getKnowbilityJdbcTemplate() {
		return knowbilityJdbcTemplate;
	}
	public void setKnowbilityJdbcTemplate(JdbcTemplate knowbilityJdbcTemplate) {
		this.knowbilityJdbcTemplate = knowbilityJdbcTemplate;
	}
	public JdbcTemplate getEcpcsJdbcTemplate() {
		return ecpcsJdbcTemplate;
	}
	public void setEcpcsJdbcTemplate(JdbcTemplate ecpcsJdbcTemplate) {
		this.ecpcsJdbcTemplate = ecpcsJdbcTemplate;
	}
	public JdbcTemplate getEasJdbcTemplate() {
		return easJdbcTemplate;
	}
	public void setEasJdbcTemplate(JdbcTemplate easJdbcTemplate) {
		this.easJdbcTemplate = easJdbcTemplate;
	}
	public JdbcTemplate getDistJdbcTemplate() {
		return distJdbcTemplate;
	}
	public void setDistJdbcTemplate(JdbcTemplate distJdbcTemplate) {
		this.distJdbcTemplate = distJdbcTemplate;
	}
	@Deprecated 
	//As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more.
	public JdbcTemplate getConeJdbcTemplate() {
		LOGGER.info("Deprecated method was called: getConeJdbcTemplate.");
		return coneJdbcTemplate;
	}
	@Deprecated 
	//As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more.
	public void setConeJdbcTemplate(JdbcTemplate coneJdbcTemplate) {
		LOGGER.info("Deprecated method was called: setConeJdbcTemplate.");
		this.coneJdbcTemplate = coneJdbcTemplate;
	}
	public JdbcTemplate getCodsJdbcTemplate() {
		return codsJdbcTemplate;
	}
	public void setCodsJdbcTemplate(JdbcTemplate codsJdbcTemplate) {
		this.codsJdbcTemplate = codsJdbcTemplate;
	}
	public JdbcTemplate getServJdbcTemplate() {
		return servJdbcTemplate;
	}
	public void setServJdbcTemplate(JdbcTemplate servJdbcTemplate) {
		this.servJdbcTemplate = servJdbcTemplate;
	}
	public JdbcTemplate getKnowbilityRefJdbcTemplate() {
		return knowbilityRefJdbcTemplate;
	}
	public void setKnowbilityRefJdbcTemplate(JdbcTemplate knowbilityRefJdbcTemplate) {
		this.knowbilityRefJdbcTemplate = knowbilityRefJdbcTemplate;
	}
	public JdbcTemplate getCcEventsJdbcTemplate() {
		return ccEventsJdbcTemplate;
	}
	public void setCcEventsJdbcTemplate(JdbcTemplate ccEventsJdbcTemplate) {
		this.ccEventsJdbcTemplate = ccEventsJdbcTemplate;
	}
	public JdbcTemplate getCconJdbcTemplate() {
		return cconJdbcTemplate;
	}
	public void setCconJdbcTemplate(JdbcTemplate cconJdbcTemplate) {
		this.cconJdbcTemplate = cconJdbcTemplate;
	}
	public JdbcTemplate getEmcmJdbcTemplate() {
		return emcmJdbcTemplate;
	}
	public void setEmcmJdbcTemplate(JdbcTemplate emcmJdbcTemplate) {
		this.emcmJdbcTemplate = emcmJdbcTemplate;
	}
	
	/**
	 * 
	 * @param dataSourceName
	 * @param jdbcTemplate
	 * @return
	 */
	public static TestPointResultInfo testJdbcTemplate(String dataSourceName, JdbcTemplate jdbcTemplate) {
		String testQuery = "SELECT 1 FROM DUAL";
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName(dataSourceName);
		try {
			jdbcTemplate.execute(testQuery, new PreparedStatementCallback<TestPointResultInfo>() {

				@Override
				public TestPointResultInfo doInPreparedStatement(PreparedStatement arg0) throws SQLException, DataAccessException {
					resultInfo.setPass(true);
					return null;
				}

			});
		} catch (Throwable t) {
			resultInfo.setExceptionDetail(t);
		}

		return resultInfo;
	}
	
	protected static TestPointResultInfo getPackageVersion(String packageName, JdbcTemplate jdbcTemplate) {
		String call = "{ ? = call " + packageName + ".getVersion() } ";
		TestPointResultInfo testResultInfo = new TestPointResultInfo();
		testResultInfo.setPass(true);
		testResultInfo.setTestPointName(packageName + ".getVersion()");
		testResultInfo.setTimestamp(new Date());
		try {
			String version = jdbcTemplate.execute(call, new CallableStatementCallback<String>() {

				@Override
				public String doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
					String versionNo = null;
					callable.registerOutParameter(1, OracleTypes.VARCHAR);

					callable.execute();
					versionNo = callable.getString(1);

					return versionNo;
				}
			});
			testResultInfo.setResultDetail("Version=" + version);
		} catch (Throwable t) {
			testResultInfo.setExceptionDetail(t);
		}

		return testResultInfo;

	}
	
	public TestPointResultInfo testKnowbilityDataSource() {
		return testJdbcTemplate("KnowbilityDataSource", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo testDistDataSource() {
		return testJdbcTemplate("DistDataSource", this.distJdbcTemplate);
	}
	
	public TestPointResultInfo testEcpcsDataSource() {
		return testJdbcTemplate("EcpcsDataSource", this.ecpcsJdbcTemplate);
	}
	
	public TestPointResultInfo testConeDataSource() {
		return testJdbcTemplate("ConeDataSource", this.coneJdbcTemplate);
	}
	
	public TestPointResultInfo testCodsDataSource() {
		return testJdbcTemplate("CodsDataSource", this.codsJdbcTemplate);
	}
	
	public TestPointResultInfo testServDataSource(){
		return testJdbcTemplate("ServDataSource", this.servJdbcTemplate);
	}
	
	public TestPointResultInfo testCconDataSource(){
		return testJdbcTemplate("CconDataSource", this.cconJdbcTemplate);
	}
	
	public TestPointResultInfo testEasDataSource(){
		return testJdbcTemplate("EasDataSource", this.easJdbcTemplate);
	}
	
	public TestPointResultInfo testRefDataSource(){
		return testJdbcTemplate("KBRefDataSource", this.knowbilityRefJdbcTemplate);
	}
	
	public TestPointResultInfo testCcEventsDataSource(){
		return testJdbcTemplate("CcEventsDataSource", this.ccEventsJdbcTemplate);
	}

	public TestPointResultInfo testEmcmDataSource(){
		return testJdbcTemplate("EMCMDataSource", this.emcmJdbcTemplate);
	}
	
	public TestPointResultInfo getRaUtilityPkgVersion() {
		return getPackageVersion("RA_Utility_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getSubscriberPkgVersion() {
		return getPackageVersion("subscriber_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getClientEquipmentPkgVersion() {
		return getPackageVersion("client_equipment", this.distJdbcTemplate);
	}
	
	public TestPointResultInfo getccEventPkgVersion() {
		return getPackageVersion("cc_event_pkg", this.ccEventsJdbcTemplate);
	}
	
	public TestPointResultInfo getUsageUtilityPkgVersion() {
		return getPackageVersion("Usage_Utility_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getClientInfoPkgVersion() {
		return getPackageVersion("Client_Info_pkg", this.codsJdbcTemplate);
	}

	public TestPointResultInfo getContactEventPkgVersion() {
		return getPackageVersion("CONTACT_EVENT_PKG", this.coneJdbcTemplate);
	}
	
	public TestPointResultInfo getEasUtilityPkgVersion() {
		return getPackageVersion("eas_utility_pkg", this.codsJdbcTemplate);
	}

	public TestPointResultInfo getFleetUtilityPkgVersion() {
		return getPackageVersion("Fleet_Utility_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getHistoryUtilityPkgVersion() {
		return getPackageVersion("history_utility_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getLogUtilityPkgVersion() {
		return getPackageVersion("Log_Utility", this.easJdbcTemplate);
	}
	
	public TestPointResultInfo getMemoUtilityPkgVersion() {
		return getPackageVersion("memo_utility_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getNlNgpPkgVersion() {
		return getPackageVersion("NL_NGP_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getPricePlanUtilityPkgVersion() {
		return getPackageVersion("price_plan_utility_pkg",this.knowbilityRefJdbcTemplate);
	}
	
	public TestPointResultInfo getReferencePkgVersion() {
		return getPackageVersion("reference_pkg",this.knowbilityRefJdbcTemplate);
	}
	
	public TestPointResultInfo getRuleUtilityPkgVersion() {
		return getPackageVersion("rule_utility_pkg", this.easJdbcTemplate);
	}
	
	public TestPointResultInfo getReferenceAppPkgVersion() {
		return getPackageVersion("reference_app_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getPortalNotificationPkgVersion() {
		return getPackageVersion("portal_notification_pkg", this.easJdbcTemplate);
	}
	
	public TestPointResultInfo getSubscriberPrefPkgVersion() {
		return getPackageVersion("subscriber_pref_pkg", this.codsJdbcTemplate);
	}
	
	public TestPointResultInfo getSubsRetrievalPkgVersion() {
		return getPackageVersion("sub_retrieval_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getSubsAttrbRetrievalPkgVersion() {
		return getPackageVersion("sub_attrib_retrieval_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getAccRetrievalPkgVersion() {
		return getPackageVersion("acc_retrieval_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getAccAttrbRetrievalPkgVersion() {
		return getPackageVersion("acc_attrib_retrieval_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getCrdCheckResultPkgVersion() {
		return getPackageVersion("crdcheck_result_pkg", this.knowbilityJdbcTemplate);
	}
	
	public TestPointResultInfo getInvoicePkgVersion() {
		return getPackageVersion("invoice_pkg", this.knowbilityJdbcTemplate);
	}

	public TestPointResultInfo getSubscriberCountPkgVersion() {
		return getPackageVersion("subscriber_count_pkg", this.knowbilityJdbcTemplate);
	}
	protected <T> T executeCallableWithConnection(Connection connection, String call, CallableStatementCallback<T> callable) {
		CallableStatement cs = null;
		try {
			cs = connection.prepareCall(call);
			return callable.doInCallableStatement(cs);
		} catch (SQLException e) {
			throw new SystemException ("DAO", "ORA-"+e.getErrorCode(), e.getMessage(), "");
		}finally {
			if (cs != null) {
				try {
					cs.close();
				}catch (SQLException e) {}
			}
		}
		
	}
	
	protected <T> T executeCallbackWithConnection(Connection connection, ConnectionCallback<T> callback) {
		try {
			return callback.doInConnection(connection);
		} catch (SQLException e) {
			throw new SystemException ("DAO", "ORA-"+e.getErrorCode(), e.getMessage(), "");
		}finally {
			
		}
		
	}

	protected <T> T executePreparedStatementWithConnection(Connection connection, String sql,SqlParameterSource paramSource,PreparedStatementCallback<T> preparable) {
		PreparedStatement ps = null;
		try {
			ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
			String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
			Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);
			List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(parsedSql, paramSource);
			PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sqlToUse, declaredParameters);
			PreparedStatementCreator psc = pscf.newPreparedStatementCreator(params);
			ps = psc.createPreparedStatement(connection);
			return preparable.doInPreparedStatement(ps);
		} catch (SQLException e) {
			throw new SystemException("DAO", "ORA-" + e.getErrorCode(),e.getMessage(), "");
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
		}

	}

	public static String getQueryFromFile(final String database, final String component, final String file) {
		return StringUtils.join(new String[] {"sql", database, component, file + ".sql"}, File.separator);
    }

	public static String getQueryFromFile(final String file) {
        String line = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		
        try {
        	br = new BufferedReader(new InputStreamReader(new ClassPathResource("sql/" + file + ".sql").getInputStream(), Charsets.UTF_8));
            
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        	return sb.toString();
        } catch (IOException exception) {
            LOGGER.error("IOException reading file [" + file + "].", exception);
            return null;
        } finally {
            if (br != null) {
            	try {
            		br.close();
            	} catch (IOException ioe) {
            		LOGGER.error("Closing BufferedReader for file [" + file + "].", ioe);
            	}
            }
        }
    }
}
