package com.telus.cmb.account.informationhelper.dao.impl;

/*
 * Created by Inbaselvan Gandhi for WL10 Upgrade
 */
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import com.telus.cmb.account.informationhelper.dao.MemoDao;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.common.util.DateUtil;
import com.telus.eas.account.info.MemoCriteriaInfo;
import com.telus.eas.framework.info.MemoInfo;

public class MemoDaoImpl extends MultipleJdbcDaoTemplateSupport implements MemoDao {

	private static final int ORACLE_REF_CURSOR = -10;
	private static String detailedDateFormatSt = "MM/dd/yyyy HH:mm:ss";
	private static SimpleDateFormat detailedDateFormat = new SimpleDateFormat(detailedDateFormatSt);

	private static final String sqlString = new String("{ call  }");
	private static final String[] creditCheckMemoTypes = new String[]{"DPCH","REFC"};
	private static final int MAX_MEMO_SEARCH_RESULT_COUNT = 1000;


	@Override
	public List<MemoInfo> retrieveMemos(final int ban, final int count) {
		if (AppConfiguration.isWRPPh3Rollback()) {
			String sql = new StringBuilder(sqlString).insert(7, "memo_utility_pkg.GetMemos (?,?,?)").toString();
	
			return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<MemoInfo>>() {
	
				@Override
				public List<MemoInfo> doInCallableStatement(CallableStatement callstmt) throws SQLException, DataAccessException {
					ResultSet result = null;
					List<MemoInfo> list = new ArrayList<MemoInfo>();
	
					try {
						callstmt.setInt(1, ban);
						callstmt.setInt(2, count);
						callstmt.registerOutParameter(3, ORACLE_REF_CURSOR);
						callstmt.execute();
						result = (ResultSet) callstmt.getObject(3);
						while (result.next()) {
							MemoInfo memoInfo = new MemoInfo();
	
							memoInfo.setBanId(ban);
							memoInfo.setDate(result.getTimestamp(1));
							memoInfo.setMemoType(result.getString(2));
							memoInfo.setSubscriberId(result.getString(3));
							memoInfo.setProductType(result.getString(4));
							memoInfo.setText(result.getString(5));
							memoInfo.setSystemText(result.getString(6));
							memoInfo.setModifyDate(DateUtil.offsetKBSystemDateTimeToServerTimezone(result.getDate(7)));
							memoInfo.setOperatorId(result.getInt(8));
							memoInfo.setMemoId(result.getDouble(9));
	
							list.add(memoInfo);
						}
	
					} finally {
						if (result != null)
							result.close();
					}
	
					return list;
				}
			});
		} else {
			
			String sql = "SELECT * FROM ( SELECT /*+INDEX(memo memo_PK)*/ memo_date, memo_type, memo_subscriber, memo_product_type, memo_manual_txt, memo_system_txt,sys_update_date, operator_id, memo_id FROM memo  "
					+ " WHERE memo_ban = :ban ORDER BY memo_ban desc ,memo_id desc )  WHERE ROWNUM <= :searchLimit";
			
			//define the named parameter
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			
			//add the ban namedParameter
			namedParameters.addValue("ban", ban);
		
			
			//set the default searchLimit size as 1000 if it exceeds 1000 or less than zero.
			if (count <= 0 || count > MAX_MEMO_SEARCH_RESULT_COUNT) {
				namedParameters.addValue("searchLimit",MAX_MEMO_SEARCH_RESULT_COUNT);
			} else {
				namedParameters.addValue("searchLimit", count);
			}
			
			return (List<MemoInfo>) getKnowbilityNamedParameterJdbcTemplate().query(sql, namedParameters, new ResultSetExtractor<List<MemoInfo>>() {
				@Override
				public List<MemoInfo> extractData(ResultSet result) throws SQLException {
					List<MemoInfo> memoList = new ArrayList<MemoInfo>();
					while (result.next()) {
						MemoInfo memo = new MemoInfo();
						memo.setBanId(ban);
						memo.setDate(result.getTimestamp("memo_date"));
						memo.setMemoType(result.getString("memo_type"));
						memo.setSubscriberId(result.getString("memo_subscriber"));
						memo.setProductType(result.getString("memo_product_type"));
						memo.setText(result.getString("memo_manual_txt"));
						memo.setSystemText(result.getString("memo_system_txt"));
						memo.setModifyDate(DateUtil.offsetKBSystemDateTimeToServerTimezone(result.getDate("sys_update_date")));
						memo.setOperatorId(result.getInt("operator_id"));
						memo.setMemoId(result.getDouble("memo_id"));
						memoList.add(memo);
					}
					return memoList;
				}
			});			
		}
		// helll
	}

	@Override
	public List<MemoInfo> retrieveMemos(final MemoCriteriaInfo memoCriteria) {
		if (AppConfiguration.isWRPPh3Rollback()) {
			String call = "memo_utility_pkg.GetMemosByCriteriaEnhanced (?,?,?,?,?,?,?,?,?)";
	
			String sql = new StringBuilder(sqlString).insert(7, call).toString();
	
			return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<MemoInfo>>() {
	
				@Override
				public List<MemoInfo> doInCallableStatement(CallableStatement callstmt) throws SQLException, DataAccessException {
	
					List<MemoInfo> list = new ArrayList<MemoInfo>();
					ResultSet result = null;
					String subscribers = "";
					String startDate = null;
					String endDate = null;
					String memoTypes = "";
	
					try {
						// check if searching by subscriber ids
						if (memoCriteria.getSubscriberIds() != null && memoCriteria.getSubscriberIds().length > 0) {
							for (int i = 0; i < memoCriteria.getSubscriberIds().length; i++) {
								if (i > 0) {
									subscribers += ",";
								}
								subscribers += memoCriteria.getSubscriberIds()[i];
							}
						} else {
							subscribers = null;
						}
						// check if searching by types
						if (memoCriteria.getTypes() != null && memoCriteria.getTypes().length > 0) {
							for (int i = 0; i < memoCriteria.getTypes().length; i++) {
								if (i > 0) {
									memoTypes += ",";
								}
								memoTypes += memoCriteria.getTypes()[i];
							}
						} else {
							memoTypes = null;
						}
						// check if searching by date
						if (memoCriteria.getDateFrom() != null || memoCriteria.getDateTo() != null) {
							if (memoCriteria.getDateFrom() != null) {
								startDate = detailedDateFormat.format(memoCriteria.getDateFrom());
							} else {
								startDate = detailedDateFormat.format(new java.util.Date(0));
							}
	
							if (memoCriteria.getDateTo() != null) {
								endDate = detailedDateFormat.format(memoCriteria.getDateTo());
							} else {
								java.util.Calendar cal = Calendar.getInstance();
								cal.add(Calendar.DATE, 1);
								cal.set(Calendar.HOUR, 0);
								cal.set(Calendar.MINUTE, 0);
								cal.set(Calendar.SECOND, 0);
								endDate = detailedDateFormat.format(cal.getTime());
							}
						}
	
						callstmt.setInt(1, memoCriteria.getBanId());
						callstmt.setString(2, subscribers);
						callstmt.setString(3, memoTypes);
						callstmt.setString(4, memoCriteria.getManualText());
						callstmt.setString(5, memoCriteria.getSystemText());
						callstmt.setString(6, startDate);
						callstmt.setString(7, endDate);
						callstmt.setInt(8, memoCriteria.getSearchLimit());
						callstmt.registerOutParameter(9, ORACLE_REF_CURSOR);
						callstmt.execute();
						result = (ResultSet) callstmt.getObject(9);
	
						while (result.next()) {
	
							MemoInfo memoInfo = new MemoInfo();
							memoInfo.setBanId(memoCriteria.getBanId());
							memoInfo.setDate(result.getTimestamp(1));
							memoInfo.setMemoType(result.getString(2));
							memoInfo.setSubscriberId(result.getString(3));
							memoInfo.setProductType(result.getString(4));
							memoInfo.setText(result.getString(5));
							memoInfo.setSystemText(result.getString(6));
							memoInfo.setModifyDate(result.getDate(7));
							memoInfo.setOperatorId(result.getInt(8));
							memoInfo.setMemoId(result.getDouble(9));
	
							list.add(memoInfo);
						}
	
					} finally {
						if (result != null)
							result.close();
					}
					return list;
				}
			});
		} else {
			StringBuilder sqlBuilder  = new StringBuilder();
			String selectClause = " SELECT * FROM ( SELECT /*+INDEX(memo memo_PK)*/ memo_date, memo_type, memo_subscriber, memo_product_type, memo_manual_txt, memo_system_txt,sys_update_date, operator_id, memo_id FROM memo  ";
			sqlBuilder.append(selectClause);
			
			// append ban clause and add the namedParameter
			sqlBuilder.append("WHERE memo_ban = :ban");
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("ban", memoCriteria.getBanId());

			// append subscriberIdArray clause and add the namedParameter
			if (memoCriteria.getSubscriberIds() != null && memoCriteria.getSubscriberIds().length > 0) {
				sqlBuilder.append(" AND memo_subscriber IN (:subscriberIdArray)");
				namedParameters.addValue("subscriberIdArray",
						Arrays.asList(memoCriteria.getSubscriberIds()));
			}

			// append memoTypeArray clause and add the namedParameter
			if (memoCriteria.getTypes() != null && memoCriteria.getTypes().length > 0) {
				sqlBuilder.append(" AND memo_type IN (:memoTypeArray)");
				namedParameters.addValue("memoTypeArray",Arrays.asList(memoCriteria.getTypes()));
			}

			// append manualText clause and add the namedParameter
			if (StringUtils.isNotBlank(memoCriteria.getManualText())) {
				sqlBuilder.append(" AND (upper(memo_manual_txt) like :manualText OR upper(memo_system_txt) like :manualText)");
				namedParameters.addValue("manualText","%"+memoCriteria.getManualText().toUpperCase()+"%");
			}
			
			// append date clause
			String startDate = memoCriteria.getDateFrom() != null ? detailedDateFormat.format(memoCriteria.getDateFrom()) : detailedDateFormat.format(new java.util.Date(0));
			String endDate = memoCriteria.getDateTo() != null ? detailedDateFormat.format(memoCriteria.getDateTo()) : detailedDateFormat.format(DateUtil.addDay(new Date(), 1));
			
			
			sqlBuilder.append(" AND memo_date between to_date ('"+startDate+"','mm/dd/yyyy hh24:mi:ss') and to_date ('"+endDate+"','mm/dd/yyyy hh24:mi:ss') ");
			
			
			// append orderBy clause
			sqlBuilder.append(" ORDER BY memo_ban desc ,memo_id desc ) ");
			
			// append searchLimit clause and add the namedParameter
			sqlBuilder.append(" WHERE ROWNUM <= :searchLimit");
			// set the default searchLimit size as 1000 if it exceeds 1000 or less than zero.
			if (memoCriteria.getSearchLimit() <= 0 || memoCriteria.getSearchLimit() > MAX_MEMO_SEARCH_RESULT_COUNT) {
				namedParameters.addValue("searchLimit", MAX_MEMO_SEARCH_RESULT_COUNT);
			} else {
				namedParameters.addValue("searchLimit",memoCriteria.getSearchLimit());
			}


			return (List<MemoInfo>) getKnowbilityNamedParameterJdbcTemplate().query(sqlBuilder.toString(), namedParameters, new ResultSetExtractor<List<MemoInfo>>() {
				@Override
				public List<MemoInfo> extractData(ResultSet result) throws SQLException {
					List<MemoInfo> memoList = new ArrayList<MemoInfo>();
					while (result.next()) {
						MemoInfo memo = new MemoInfo();
						memo.setBanId(memoCriteria.getBanId());
						memo.setDate(result.getTimestamp("memo_date"));
						memo.setMemoType(result.getString("memo_type"));
						memo.setSubscriberId(result.getString("memo_subscriber"));
						memo.setProductType(result.getString("memo_product_type"));
						memo.setText(result.getString("memo_manual_txt"));
						memo.setSystemText(result.getString("memo_system_txt"));
						memo.setModifyDate(DateUtil.offsetKBSystemDateTimeToServerTimezone(result.getDate("sys_update_date")));
						memo.setOperatorId(result.getInt("operator_id"));
						memo.setMemoId(result.getDouble("memo_id"));
						memoList.add(memo);
					}
					return memoList;
				}
			});				
		}
	}

	@Override
	public MemoInfo retrieveLastMemo(final int ban, final String memoType) {
		if (AppConfiguration.isWRPPh3Rollback()) {
			String sql = new StringBuilder(sqlString).insert(7, "memo_utility_pkg.GetLastMemo (?,?,?,?)").toString();
	
			return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<MemoInfo>() {
	
				@Override
				public MemoInfo doInCallableStatement(CallableStatement callstmt) throws SQLException, DataAccessException {
	
					MemoInfo lastMemoInfo = new MemoInfo();
					ResultSet result = null;
					String subscriberID = "";
	
					try {
						callstmt.setInt(1, ban);
						callstmt.setString(2, subscriberID);
						callstmt.setString(3, memoType);
						callstmt.registerOutParameter(4, ORACLE_REF_CURSOR);
						callstmt.execute();
						result = (ResultSet) callstmt.getObject(4);
	
						while (result.next()) {
							lastMemoInfo = new MemoInfo();
							lastMemoInfo.setBanId(ban);
							lastMemoInfo.setDate(result.getTimestamp(1));
							lastMemoInfo.setMemoType(result.getString(2));
							lastMemoInfo.setSubscriberId(result.getString(3));
							lastMemoInfo.setProductType(result.getString(4));
							lastMemoInfo.setText(result.getString(5));
							lastMemoInfo.setSystemText(result.getString(6));
							lastMemoInfo.setModifyDate(result.getDate(7));
							lastMemoInfo.setOperatorId(result.getInt(8));
							lastMemoInfo.setMemoId(result.getDouble(9));
						}
	
					} finally {
						if (result != null) {
							result.close();
						}
					}
					return lastMemoInfo;
				}
	
			});
		} else {
			
			String sql = "SELECT * FROM ( SELECT memo_date, memo_type, memo_subscriber, memo_product_type, memo_manual_txt, memo_system_txt,sys_update_date, " +
			" operator_id, memo_id FROM memo  WHERE memo_ban = :ban AND memo_type IN (:memoTypeArray) ORDER BY memo_ban desc ,memo_id desc )  WHERE ROWNUM <= 1";
			
			//append the ban namedParameter
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("ban", ban);
		
			// add the memo_type namedParameter
			String[] memoTypes = StringUtils.equalsIgnoreCase(memoType,"CreditCheck") ? creditCheckMemoTypes : new String[]{memoType};
			namedParameters.addValue("memoTypeArray", Arrays.asList(memoTypes));
			
			return getKnowbilityNamedParameterJdbcTemplate().query(sql, namedParameters, new ResultSetExtractor<MemoInfo>() {
				@Override
				public MemoInfo extractData(ResultSet result) throws SQLException {
					MemoInfo memo = new MemoInfo();	
					while (result.next()) {						
						memo.setBanId(ban);
						memo.setDate(result.getTimestamp("memo_date"));
						memo.setMemoType(result.getString("memo_type"));
						memo.setSubscriberId(result.getString("memo_subscriber"));
						memo.setProductType(result.getString("memo_product_type"));
						memo.setText(result.getString("memo_manual_txt"));
						memo.setSystemText(result.getString("memo_system_txt"));
						memo.setModifyDate(result.getDate("sys_update_date"));
						memo.setOperatorId(result.getInt("operator_id"));
						memo.setMemoId(result.getDouble("memo_id"));
					}
					return memo;
				}
			});
			
		} 
	}
}
