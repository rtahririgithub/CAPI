///**
// * 
// */
//package com.telus.cmb.account.informationhelper.dao.impl;
//
//import java.sql.CallableStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Date;
//
//import oracle.jdbc.OracleTypes;
//
//import org.apache.log4j.Logger;
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.CallableStatementCallback;
//
//import com.telus.api.ApplicationException;
//import com.telus.api.SystemCodes;
//import com.telus.api.SystemException;
//import com.telus.api.account.AccountManager;
//import com.telus.api.reference.ChargeType;
//import com.telus.cmb.account.informationhelper.dao.LetterDao;
//import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
//import com.telus.eas.account.info.LMSLetterRequestInfo;
//import com.telus.eas.account.info.SearchResultsInfo;
//
///**
// * @author Canh Tran
// *
// */
//public class LetterDaoImpl extends MultipleJdbcDaoTemplateSupport implements LetterDao {
//
////	private final Logger LOGGER = Logger.getLogger(LetterDaoImpl.class);
////
////	@Override
////	public SearchResultsInfo retrieveLetterRequests(final int banId, final Date from,
////			final Date to, final char level, final String pSubscriber, final int maximum)
////	throws ApplicationException {
////
////		if (from == null || to == null)
////			throw new SystemException(SystemCodes.CMB_AIH_DAO, "Inputs fromDate and toDate should not be null", "");
////
////		String call = "{? = call HISTORY_UTILITY_PKG.GetLetterRequests(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
////
////		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<SearchResultsInfo>() {
////			@Override
////			public SearchResultsInfo doInCallableStatement(
////					CallableStatement callable) throws SQLException,
////					DataAccessException {
////
////				ResultSet result = null;
////				// return object
////				SearchResultsInfo searchResults = new SearchResultsInfo();
////				searchResults.setItems(new LMSLetterRequestInfo[0]);				
////
////				// set/register input/output parameters
////				callable.registerOutParameter(1, OracleTypes.NUMBER);
////				callable.setInt(2, banId);
////				callable.setDate(3, new java.sql.Date(from.getTime()));
////				callable.setDate(4, new java.sql.Date(to.getTime()));
////				callable.setString(5, pSubscriber);
////				callable.setInt(6, ChargeType.CHARGE_LEVEL_ACCOUNT == level || AccountManager.Search_All == level ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
////				callable.setInt(7, ChargeType.CHARGE_LEVEL_SUBSCRIBER == level || AccountManager.Search_All == level ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
////				callable.setInt(8, maximum);
////				callable.registerOutParameter(9, OracleTypes.CURSOR);
////				callable.registerOutParameter(10, OracleTypes.NUMBER);
////				callable.registerOutParameter(11, OracleTypes.VARCHAR);
////
////				callable.execute();
////
////				boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;
////
////				try {
////					if (success) {
////						result = (ResultSet) callable.getObject(9);
////						ArrayList<LMSLetterRequestInfo> list = new ArrayList<LMSLetterRequestInfo>();
////
////						while (result.next()) {
////							LMSLetterRequestInfo info = new LMSLetterRequestInfo();
////							info.setId(result.getInt(1));
////							info.setOperatorId(result.getString(2));
////							info.setLetterCategory(result.getString(3));
////							info.setLetterCode(result.getString(4));
////							info.setSubscriberId(result.getString(5));
////							info.setSubmitDate(result.getTimestamp(6));
////							info.setStatus(result.getString(7));
////							info.setProductionDate(result.getTimestamp(8));
////							info.setAllVariables(result.getString(9));
////							info.setLetterVersion(result.getInt(10));
////							info.setBanId(banId);
////
////							list.add(info);
////						}
////
////						searchResults.setItems((LMSLetterRequestInfo[])list.toArray(new LMSLetterRequestInfo[list.size()]));
////						searchResults.setHasMore(callable.getInt(10) == AccountManager.NUMERIC_TRUE);
////					} else {
////						String errorMessage = callable.getString(11);
////						LOGGER.warn("Call to HISTORY_UTILITY_PKG.GetLetterRequests returned failure with message: " + errorMessage);
////
////						searchResults.setItems(new LMSLetterRequestInfo[0]);
////						searchResults.setHasMore(false);
////					}				
////				} finally {
////					if (result != null ) {
////						result.close();
////					}
////				}
////
////				return searchResults;
////			}
////		});
////	}
//}
