package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.telus.api.SystemCodes;
import com.telus.cmb.account.informationhelper.dao.MaxVoipLineDao;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.eas.account.info.MaxVoipLineInfo;

public class MaxVoipLineDaoImpl extends MultipleJdbcDaoTemplateSupport implements MaxVoipLineDao {
	
	private static String padZeroThirty = "000000000000000000000000000000";
	private static String VOIP_SEAT_GROUP_CD = "VOIP";
	private static String DATE_FORMAT = "MM/dd/yyyy";
	private static String MAX_DATE_STR  = "12/31/9999";
	private static Date MAX_DATE;	
	
	static {
		try {
			MAX_DATE = new SimpleDateFormat(DATE_FORMAT).parse(MAX_DATE_STR);
		} catch (ParseException pe) {
			throw new IllegalStateException(SystemCodes.CMB_MVL_DAO + " SimpleDateFormat failed to parse '" + MAX_DATE_STR + "' using date format: " + DATE_FORMAT + pe.getMessage(), pe);
		}
	}
	
	@Override
	public String getNextSeatGroupId() {
		
		String sql = "select SEQ_SERVICE_COLL_GROUP_ID.NEXTVAL from dual";
		
		return getCodsJdbcTemplate().query(sql, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet resultSet) throws SQLException {
				
				if (resultSet.next()) {
					return leftPadZero(resultSet.getString(1));
				}
	
				return null;
			}
		});
	}
	
	@Override
	public List<MaxVoipLineInfo> getMaxVoipLineList(final int ban, final long subscriptionId) {
		
		String banQuery = "select ban, subscription_id, effective_start_ts, effective_end_ts, create_ts, last_updt_ts, create_user_id, " 
			+ "max_allow_capacity_cnt, last_updt_user_id from client_service_capacity where effective_end_ts > SYSTIMESTAMP and ban=" + ban;
		String banSubscriptionIdQuery = banQuery + " and subscription_id=" + subscriptionId;
		String sql = (subscriptionId != 0 ? banSubscriptionIdQuery : banQuery);
		
		return super.getCodsJdbcTemplate().query(sql, new RowMapper<MaxVoipLineInfo>() {
			@Override
			public MaxVoipLineInfo mapRow(ResultSet result, int rowNum) throws SQLException {
				
				MaxVoipLineInfo maxVoipLineInfo = new MaxVoipLineInfo();
				maxVoipLineInfo.setBan(Integer.parseInt(result.getString("ban")));
				maxVoipLineInfo.setSubscriptionId(result.getLong("subscription_id"));
				maxVoipLineInfo.setEffectiveStartDate(result.getTimestamp("effective_start_ts"));
				maxVoipLineInfo.setEffectiveEndDate(result.getTimestamp("effective_end_ts"));
				maxVoipLineInfo.setCreateDate(result.getTimestamp("create_ts"));
				maxVoipLineInfo.setLastUpdateDate(result.getTimestamp("last_updt_ts"));
				maxVoipLineInfo.setCreateUser(result.getString("create_user_id"));
				maxVoipLineInfo.setMaxVoipLines(result.getInt("max_allow_capacity_cnt")); 
				maxVoipLineInfo.setLastUpdateUser(result.getString("last_updt_user_id"));
				
				return maxVoipLineInfo;
			}
		});
	}

	@Override
	public void createMaxVoipLine(final MaxVoipLineInfo maxVoipLineInfo) {
		createOrUpdateMaxVoipLine(maxVoipLineInfo);
	}
	
	@Override
	public void updateMaxVoipLine(final MaxVoipLineInfo maxVoipLineInfo) {
		createOrUpdateMaxVoipLine(maxVoipLineInfo);
	}
	
	@Override
	public void updateMaxVoipLineList(final List<MaxVoipLineInfo> maxVoipLineInfoList) {
		for (MaxVoipLineInfo maxVoipLineInfo : maxVoipLineInfoList) {
			createOrUpdateMaxVoipLine(maxVoipLineInfo);
		}
	}
	
	private String leftPadZero(String groupId) {
		// Return the groupId left padded with '0' to make a 30 character string
		return padZeroThirty.substring(groupId.length()) + groupId;
	}

	private void createOrUpdateMaxVoipLine(final MaxVoipLineInfo maxVoipLineInfo) {
		// Expire any old records and then creates a new current record. Note that there should only be one current record for each BAN/subscription ID combination, however 
		// there is no accounting for bad data. Therefore, we will expire all records with a matching BAN/subscription ID and effective end date greater than the current
		// timestamp before creating a new one. This will help preserve the integrity of the data.

		// Expire all (old) records with matching BAN and subscription ID and effective end date greater than the current timestamp.
		expireMaxVoipLines(maxVoipLineInfo.getBan(), maxVoipLineInfo.getSubscriptionId());
		// Create the new current MaxVoipLine record.
		addMaxVoipLine(maxVoipLineInfo);
	}
	
	private void addMaxVoipLine(final MaxVoipLineInfo maxVoipLineInfo) {
		
		String sql = "insert into client_service_capacity (client_service_capacity_id, effective_start_ts, effective_end_ts, "
			+ "last_updt_ts, subscription_id, ban, service_package_grp_cd, max_allow_capacity_cnt, create_ts, create_user_id, last_updt_user_id) "
			+ "values (seq_client_service_capacity.NEXTVAL, SYSTIMESTAMP, ?, SYSTIMESTAMP, ?, ?, ?, ?, SYSTIMESTAMP, USER, USER)";

		getCodsJdbcTemplate().execute(sql, new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement pstmt) throws SQLException {
				
				if (maxVoipLineInfo.getEffectiveEndDate() != null) {
					pstmt.setTimestamp(1, new Timestamp(maxVoipLineInfo.getEffectiveEndDate().getTime()));
				} else {
					pstmt.setTimestamp(1, new Timestamp(MAX_DATE.getTime()));
				}
				pstmt.setLong(2, maxVoipLineInfo.getSubscriptionId());
				pstmt.setInt(3, maxVoipLineInfo.getBan());
				pstmt.setString(4, VOIP_SEAT_GROUP_CD);
				pstmt.setLong(5, maxVoipLineInfo.getMaxVoipLines());

				return pstmt.executeUpdate();
			}
		});
	}
	
	private void expireMaxVoipLines(final int ban, final long subscriptionId) {
		// Expire all records with a matching BAN/subscription ID and effective end date greater than the current timestamp.
		String sql = "update client_service_capacity set effective_end_ts=SYSTIMESTAMP, last_updt_ts=SYSTIMESTAMP where ban=? and subscription_id=? and effective_end_ts > SYSTIMESTAMP";
		
		getCodsJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement pstmt) throws SQLException {
				
				pstmt.setInt(1, ban);
				pstmt.setLong(2, subscriptionId);
				
				return pstmt.executeUpdate();
			}
		});
	}
	
}