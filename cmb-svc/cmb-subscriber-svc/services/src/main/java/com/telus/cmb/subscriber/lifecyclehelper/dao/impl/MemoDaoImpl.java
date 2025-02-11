package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.subscriber.lifecyclehelper.dao.MemoDao;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.eas.framework.info.MemoInfo;
import oracle.jdbc.OracleTypes;

public class MemoDaoImpl extends MultipleJdbcDaoTemplateSupport implements MemoDao {

	private static final String[] creditCheckMemoTypes = new String[]{"DPCH","REFC"};

	@Override
	public MemoInfo retrieveLastMemo(final int ban, final String subscriberID, final String memoType) {
		if (AppConfiguration.isWRPPh3Rollback()) {
			String callString = "{ call memo_utility_pkg.GetLastMemo (?,?,?,?) }";
			return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<MemoInfo>() {
	
				@Override
				public MemoInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
					MemoInfo memoInfo = null;
					ResultSet result = null;
					try {
						callable.setInt(1, ban);
						callable.setString(2, subscriberID);
						callable.setString(3, memoType);
						callable.registerOutParameter(4, OracleTypes.CURSOR);
						callable.execute();
						result = (ResultSet) callable.getObject(4);
						while (result.next()) {
							memoInfo = new MemoInfo();
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
						}
					} finally {
						if (result != null) {
							result.close();
						}
					}
					return memoInfo;
				}
	
			});
		} else {
			
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			
			String subscriberIdClause = StringUtils.isEmpty(subscriberID) ? "" : " AND memo_subscriber = :subscriberId";
			
			String sql = "SELECT * FROM ( SELECT memo_date, memo_type, memo_subscriber, memo_product_type, memo_manual_txt, memo_system_txt,sys_update_date, " +
					" operator_id, memo_id FROM memo  WHERE memo_ban = :ban "+subscriberIdClause+" AND memo_type IN (:memoTypeArray) ORDER BY memo_ban desc ,memo_id desc )  WHERE ROWNUM <= 1";

			// add the ban namedParameter
			namedParameters.addValue("ban", ban);
			
			// add the subscriberID namedParameter
			if (!StringUtils.isEmpty(subscriberID)) {
				namedParameters.addValue("subscriberId", subscriberID);
			}
			
			// add the memo_type namedParameter
			String[] memoTypes = StringUtils.equalsIgnoreCase(memoType,"CreditCheck") ? creditCheckMemoTypes : new String[]{memoType};
			namedParameters.addValue("memoTypeArray", Arrays.asList(memoTypes));
			
			return getKnowbilityNamedParameterJdbcTemplate().query(sql, namedParameters, new ResultSetExtractor<MemoInfo>() {
				@Override
				public MemoInfo extractData(ResultSet result) throws SQLException {
					MemoInfo memoInfo = null;
					if (result.next()) {			
						memoInfo = new MemoInfo();
						memoInfo.setBanId(ban);
						memoInfo.setDate(result.getTimestamp("memo_date"));
						memoInfo.setMemoType(result.getString("memo_type"));
						memoInfo.setSubscriberId(result.getString("memo_subscriber"));
						memoInfo.setProductType(result.getString("memo_product_type"));
						memoInfo.setText(result.getString("memo_manual_txt"));
						memoInfo.setSystemText(result.getString("memo_system_txt"));
						memoInfo.setModifyDate(DateUtil.offsetKBSystemDateTimeToServerTimezone(result.getDate("sys_update_date")));
						memoInfo.setOperatorId(result.getInt("operator_id"));
						memoInfo.setMemoId(result.getDouble("memo_id"));
					}
					return memoInfo;
				}
			});
		}
	}
}
