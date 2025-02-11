package com.telus.cmb.account.informationhelper.dao.impl;
/*
 * Created by Inbaselvan Gandhi for WL10 Upgrade
 */
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.reference.FollowUpCriteria;
import com.telus.cmb.account.informationhelper.dao.FollowUpDao;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.eas.account.info.FollowUpStatisticsInfo;
import com.telus.eas.account.info.FollowUpTextInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.FollowUpCriteriaInfo;

public class FollowUpDaoImpl extends MultipleJdbcDaoTemplateSupport implements FollowUpDao {

	private static String dateFormatSt = "MM/dd/yyyy";
	private static SimpleDateFormat dateFormat   =  new SimpleDateFormat(dateFormatSt);
	private static Calendar calendar = Calendar.getInstance();

	@Override
	public List<FollowUpTextInfo> retrieveFollowUpAdditionalText(final int Ban,
			final int followUpId) {

		String sql="SELECT TXSG.SYS_CREATION_DATE, "
			+ "       TXSG.OPERATOR_ID, "
			+ "       TXSG.TXT_TEXT "
			+ "  FROM FOLLOW_UP FU, "
			+ "       TXT_SEGMENTS TXSG "
			+ " WHERE FU.FU_BAN = ? "
			+ "   AND FU.FU_ID = ? "
			+ "   AND TXSG.ANCESTOR_TXT_ID = FU.TXT_SEQ_NO "
			+ " ORDER BY TXSG.TXT_SEQ_NO";

		return super.getKnowbilityJdbcTemplate().query(sql, new Object[]{Ban, followUpId}, new RowMapper<FollowUpTextInfo>(){

			@Override
			public FollowUpTextInfo mapRow(ResultSet result, int rowNum)
			throws SQLException {

				FollowUpTextInfo followUpTextInfo = new FollowUpTextInfo();
				followUpTextInfo.setCreateDate(result.getTimestamp(1));
				followUpTextInfo.setOperatorId(result.getString(2));
				followUpTextInfo.setText(result.getString(3));
				return followUpTextInfo;
			}
		});



	}

	@Override
	public List<FollowUpInfo> retrieveFollowUpHistory(final int followUpId) {

		String sql = "SELECT FU.FU_BAN, "
			+ "       FU.FU_ID, "
			+ "       FU.FU_TYPE, "
			+ "       FU.FU_DUE_DATE, "
			+ "       FU.FU_STATUS, "
			+ "       FU.FU_ASSIGNED_TO, "
			+ "       FU.FU_SUBSCRIBER, "
			+ "       FU.FU_PRODUCT_TYPE, "
			+ "       FU.FU_TEXT, "
			+ "       FU.FU_OPENED_BY, "
			+ "       FU.FU_OPEN_DATE, "
			+ "       FU.FU_CLOSED_BY, "
			+ "       FU.FU_CLOSE_DATE, "
			+ "       FU.FU_CLOSE_REASON_CODE "
			+ "  FROM FUNCTION                 FUNC, "
			+ "       FOLLOW_UP                FU, "
			+ "       FOLLOW_UP_TYPE           FUTP, "
			+ "       USERS_LTD                USR, "
			+ "       USERS_LTD                OPENED, "
			+ "       USERS_LTD                CLOSED, "
			+ "       WORK_POSITION            WP, "
			+ "       WORK_POSITION_ASSIGNMENT WPA "
			+ " WHERE FU.ANCESTOR_FU_ID = ? "
			+ "   AND FU.FU_TYPE = FUTP.FUTP_FU_TYPE(+) "
			+ "   AND WPA.WPASN_USER_ID = USR.USER_ID "
			+ "   AND FU.FU_OPENED_BY = OPENED.USER_ID(+) "
			+ "   AND FU.FU_CLOSED_BY = CLOSED.USER_ID(+) "
			+ "   AND FU.FU_ASSIGNED_TO = WPA.WPASN_WORK_POSITION_CODE(+) "
			+ "   AND FU.FU_ASSIGNED_TO = WP.WP_WORK_POSITION_CODE(+) "
			+ "   AND WPA.WPASN_EFFECTIVE_DATE(+) <= FU.FU_DUE_DATE "
			+ "   AND NVL(WPA.WPASN_EXPIRATION_DATE(+), TO_DATE('2100-12-31','YYYY-MM-DD')) > FU.FU_DUE_DATE "
			+ "   AND WP.WP_EFFECTIVE_DATE(+) <= FU.FU_DUE_DATE "
			+ "   AND NVL(WP.WP_EXPIRATION_DATE(+), TO_DATE('2100-12-31','YYYY-MM-DD')) > FU.FU_DUE_DATE "
			+ "   AND WP.WP_FUNCTION_CODE = FUNC.FUNC_FUNCTION_CODE(+) "
			+ " ORDER BY FU_ID";

		return super.getKnowbilityJdbcTemplate().query(sql, new Object[]{followUpId}, new RowMapper<FollowUpInfo>(){

			@Override
			public FollowUpInfo mapRow(ResultSet result, int rowNum) 
			throws SQLException {

				FollowUpInfo followUpInfo = new FollowUpInfo();
				followUpInfo.setBan(result.getInt(1));
				followUpInfo.setFollowUpId(result.getInt(2));
				followUpInfo.setFollowUpType(result.getString(3));
				followUpInfo.setDueDate(result.getDate(4));
				followUpInfo.setStatus(result.getString(5).charAt(0));
				followUpInfo.setAssignedToWorkPositionId(result.getString(6));
				followUpInfo.setSubscriberId(result.getString(7));
				followUpInfo.setProductType(result.getString(8));
				followUpInfo.setText(result.getString(9));
				followUpInfo.setOpenedBy(result.getString(10));
				followUpInfo.setOpenDate(result.getDate(11));
				followUpInfo.setClosedBy(result.getString(12));
				followUpInfo.setCloseDate(result.getDate(13));
				followUpInfo.setCloseReasonCode(result.getString(14));

				return followUpInfo;
			}
		});
	}

	@Override
	public FollowUpInfo retrieveFollowUpInfoByBanFollowUpID(final int Ban,
			final int followUpId) {
		String sql="SELECT fu_type, fu_due_date, fu_status, "
			+ " 	fu_assigned_to, fu_subscriber, "
			+ " 	fu_product_type, fu_text, fu_open_date, "
			+ " 	operator_id, fu_opened_by, fu_closed_by, fu_close_date, fu_close_reason_code "
			+ " FROM follow_up "
			+ " WHERE fu_ban = ? "
			+ " 	AND  fu_id = ? ";

		return super.getKnowbilityJdbcTemplate().query(sql, new Object[]{Ban, followUpId}, new ResultSetExtractor<FollowUpInfo>() {

			@Override
			public FollowUpInfo extractData(ResultSet result)
			throws SQLException, DataAccessException {

				FollowUpInfo followUpInfo = new FollowUpInfo();

					while (result.next()) {
						followUpInfo.setBanId(Ban);
						followUpInfo.setFollowUpId(followUpId);
						followUpInfo.setFollowUpType(result.getString(1));
						followUpInfo.setDueDate(result.getTimestamp(2));
						followUpInfo.setStatus(result.getString(3).charAt(0));
						followUpInfo.setAssignedToWorkPositionId(result.getString(4));
						followUpInfo.setSubscriberId(result.getString(5));
						followUpInfo.setProductType(result.getString(6));
						followUpInfo.setText(result.getString(7));
						followUpInfo.setOpenDate(result.getTimestamp(8));
						followUpInfo.setOperatorId(result.getInt(9));
						followUpInfo.setOpenedBy(result.getString(10));
						followUpInfo.setClosedBy(result.getString(11));
						followUpInfo.setCloseDate(result.getTimestamp(12));
						followUpInfo.setCloseReasonCode(result.getString(13));
					}

				return followUpInfo;
			}
		});
	}

	@Override
	public List<FollowUpInfo> retrieveFollowUps(FollowUpCriteriaInfo followUpCriteria) {

		String sql="SELECT FU.FU_BAN, "
			+ "       FU.FU_ID, "
			+ "       FU.FU_TYPE, "
			+ "       FU.FU_DUE_DATE, "
			+ "       FU.FU_STATUS, "
			+ "       FU.FU_ASSIGNED_TO, "
			+ "       FU.FU_SUBSCRIBER, "
			+ "       FU.FU_PRODUCT_TYPE, "
			+ "       FU.FU_TEXT, "
			+ "       FU.FU_OPENED_BY, "
			+ "       FU.FU_OPEN_DATE, "
			+ "       FU.FU_CLOSED_BY, "
			+ "       FU.FU_CLOSE_DATE, "
			+ "       FU.FU_CLOSE_REASON_CODE "
			+ "  FROM FOLLOW_UP                FU, "
			+ "       BILLING_ACCOUNT          BAN, "
			+ "       CYCLE                    CYC "
			+ " WHERE BAN.BAN = FU.FU_BAN "
			+ "   AND CYC.CYCLE_CODE = BAN.BILL_CYCLE ";

		StringBuilder sqlBuilder = new StringBuilder(sql);

		// set work position id
		String workPositionId = followUpCriteria.getWorkPositionId();
		if (workPositionId != null) {
			if (followUpCriteria.getFollowUpGroup().equals(FollowUpCriteria.FOLLOW_UP_GROUP_ASSIGNED_TO)) {
				sqlBuilder.append(" AND FU.FU_ASSIGNED_TO = \'")
				.append(Info.padTo(workPositionId, ' ', 8))
				.append("\' ");
			}
			else if (followUpCriteria.getFollowUpGroup().equals(FollowUpCriteria.FOLLOW_UP_GROUP_CREATED_BY)) {
				sqlBuilder.append(" AND FU.FU_OPENED_BY = ")
				.append(workPositionId)
				.append(" ");
			}
			else {
				sqlBuilder.append(" AND (FU.FU_ASSIGNED_TO = \'")
				.append(Info.padTo(workPositionId, ' ', 8))
				.append("\' OR FU.FU_OPENED_BY = ")
				.append(workPositionId)
				.append(") ");
			}
		}
		else {
			throw new SystemException(SystemCodes.CMB_AIH_EJB, "Work Position ID is null...", "");
		}

		// set follow up status
		String followUpStatus = followUpCriteria.getFollowUpStatus();
		if (!followUpStatus.equals(FollowUpCriteria.FOLLOW_UP_STATUS_ALL)) {
			sqlBuilder.append(" AND FU.FU_STATUS = \'")
			.append(followUpStatus)
			.append("\' ");
		}

		// set follow up type
		String followUpType = followUpCriteria.getFollowUpType();
		if (followUpType != null && !followUpType.trim().equals("")) {
			sqlBuilder.append(" AND FU.FU_TYPE = \'")
			.append(Info.padTo(followUpType.trim(), ' ', 4))
			.append("\' ");
		}

		// set due date From
		java.util.Date dueDateFrom = followUpCriteria.getDueDateFrom();
		if (dueDateFrom != null) {
			sqlBuilder.append(" AND FU.FU_DUE_DATE >= TO_DATE(\'")
			.append(dateFormat.format(dueDateFrom))
			.append("\', \'")
			.append(dateFormatSt)
			.append("\') ");
		}

		// set due date To
		java.util.Date dueDateTo = followUpCriteria.getDueDateTo();
		if (dueDateTo != null) {
			sqlBuilder.append(" AND FU.FU_DUE_DATE <= TO_DATE(\'")
			.append(dateFormat.format(dueDateTo))
			.append("\', \'")
			.append(dateFormatSt)
			.append("\') ");
		}

		// set account status
		String accountStatus = followUpCriteria.getAccountStatus();
		if (accountStatus != null && !accountStatus.trim().equals("")) {
			sqlBuilder.append(" AND BAN.BAN_STATUS = \'")
			.append(accountStatus.trim())
			.append("\' ");
		}

		// set cycle close day
		if (followUpCriteria.setBillCycleCloseDay()) {
			sqlBuilder.append(" AND CYC.CYCLE_CLOSE_DAY = ")
			.append(followUpCriteria.getBillCycleCloseDay())
			.append(" ");
		}

		// set account balance From
		if (followUpCriteria.setAccountBalanceFrom()) {
			sqlBuilder.append(" AND BAN.AR_BALANCE >= ")
			.append(followUpCriteria.getAccountBalanceFrom())
			.append(" ");
		}

		// set account balance To
		if (followUpCriteria.setAccountBalanceTo()) {
			sqlBuilder.append(" AND BAN.AR_BALANCE <= ")
			.append(followUpCriteria.getAccountBalanceTo())
			.append(" ");
		}

		// add ORDER BY statement
		sqlBuilder.append(" ORDER BY FU.FU_ID");

		return super.getKnowbilityJdbcTemplate().query(sqlBuilder.toString(), new RowMapper<FollowUpInfo>() {


			@Override
			public FollowUpInfo mapRow(ResultSet result, int rowNum) throws SQLException {

				FollowUpInfo followUpInfo = new FollowUpInfo();
				followUpInfo.setBanId(result.getInt(1));
				followUpInfo.setFollowUpId(result.getInt(2));
				followUpInfo.setFollowUpType(result.getString(3));
				followUpInfo.setDueDate(result.getDate(4));
				followUpInfo.setStatus(result.getString(5).charAt(0));
				followUpInfo.setAssignedToWorkPositionId(result.getString(6));
				followUpInfo.setSubscriberId(result.getString(7));
				followUpInfo.setProductType(result.getString(8));
				followUpInfo.setText(result.getString(9));
				followUpInfo.setOpenedBy(result.getString(10));
				followUpInfo.setOpenDate(result.getDate(11));
				followUpInfo.setClosedBy(result.getString(12));
				followUpInfo.setCloseDate(result.getDate(13));
				followUpInfo.setCloseReasonCode(result.getString(14));

				return followUpInfo;
			}
		});
	}

	@Override
	public List<FollowUpInfo> retrieveFollowUps(final int Ban, int Count) {

		String select_count="";
		if (Count > 0){
			select_count = "  WHERE ROWNUM <= " + Count;
		}

		String sql="SELECT * FROM "
			+ " 	(SELECT fu_type, fu_due_date, fu_status, "
			+ " 		fu_assigned_to, fu_subscriber, "
			+ " 		fu_product_type, fu_text, fu_open_date, "
			+ " 		operator_id, fu_opened_by, fu_closed_by, fu_close_date, fu_close_reason_code, fu_id "
			+ " 	FROM follow_up "
			+ " 	WHERE fu_ban = ? "
			+ " 	order by fu_id desc) "
			+ select_count  ;

		return super.getKnowbilityJdbcTemplate().query(sql, new Object[]{Ban}, new RowMapper<FollowUpInfo>() {

			@Override
			public FollowUpInfo mapRow(ResultSet result, int rowNum)
			throws SQLException {

				FollowUpInfo followUpInfo = new FollowUpInfo();
				followUpInfo.setBanId(Ban);
				followUpInfo.setFollowUpType(result.getString(1));
				followUpInfo.setDueDate(result.getTimestamp(2));
				followUpInfo.setStatus(result.getString(3).charAt(0));
				followUpInfo.setAssignedToWorkPositionId(result.getString(4));
				followUpInfo.setSubscriberId(result.getString(5));
				followUpInfo.setProductType(result.getString(6));
				followUpInfo.setText(result.getString(7));
				followUpInfo.setOpenDate(result.getTimestamp(8));
				followUpInfo.setOperatorId(result.getInt(9));
				followUpInfo.setOpenedBy(result.getString(10));
				followUpInfo.setClosedBy(result.getString(11));
				followUpInfo.setCloseDate(result.getTimestamp(12));
				followUpInfo.setCloseReasonCode(result.getString(13));
				followUpInfo.setFollowUpId(result.getInt(14));

				return followUpInfo;
			}	
		});
	}

	@Override
	public FollowUpStatisticsInfo retrieveFollowUpStatistics(int Ban) {

		String sql1="SELECT MIN(fu_due_date) "
			+ " 		FROM	follow_up	"
			+ " 	WHERE fu_ban = ? "
			+ " 		AND fu_status = 'O'";

		String sql2 = "SELECT 1 FROM dual WHERE EXISTS "
			+ " (SELECT fu_id FROM follow_up WHERE fu_ban = ? AND fu_due_date = ?) ";

		final Date today = new Date(calendar.getTime().getTime());
		FollowUpStatisticsInfo fsi=null;

		try{
			fsi= super.getKnowbilityJdbcTemplate().queryForObject(sql1, new Object[]{Ban}, new RowMapper<FollowUpStatisticsInfo>(){

				@Override
				public FollowUpStatisticsInfo mapRow(ResultSet result, int rowNum)
				throws SQLException {
					FollowUpStatisticsInfo followUpStatisticsInfo = new FollowUpStatisticsInfo();

					Date minDueDate = result.getDate(1);

					if (minDueDate != null) {
						followUpStatisticsInfo.setHasOpenFollowUps(true);

						int compareDates = minDueDate.compareTo(today);

						if (compareDates > 0) {
							followUpStatisticsInfo.setHasDueFollowUps(false);
							followUpStatisticsInfo.setHasOverdueFollowUps(false);
						}
						else if (compareDates == 0) {
							followUpStatisticsInfo.setHasDueFollowUps(true);
							followUpStatisticsInfo.setHasOverdueFollowUps(false);
						}
						else {
							followUpStatisticsInfo.setHasOverdueFollowUps(true);
						}
					}
					else {
						followUpStatisticsInfo.setHasOpenFollowUps(false);
						followUpStatisticsInfo.setHasDueFollowUps(false);
						followUpStatisticsInfo.setHasOverdueFollowUps(false);
					}
					return followUpStatisticsInfo;
				}

			});

			if(fsi.hasOpenFollowUps() && fsi.hasOverdueFollowUps()){
				try{ 
					fsi.setHasDueFollowUps(super.getKnowbilityJdbcTemplate().queryForInt(sql2, new Object[]{Ban,today})==0?false:true);
				}catch(EmptyResultDataAccessException e){
					fsi.setHasDueFollowUps(false);
				}
			}

		}catch(EmptyResultDataAccessException e){
			fsi=new FollowUpStatisticsInfo();
		}

		return fsi;
	}

	@Override
	public int retrieveLastFollowUpIDByBanFollowUpType(final int ban,
			final String followUpType) {

		String sql="SELECT MAX(fu_id) "
			+ " FROM follow_up "
			+ " WHERE fu_ban = ? "
			+ " AND  fu_type = ? ";

		try{
			return super.getKnowbilityJdbcTemplate().queryForInt(sql, new Object[]{ban,followUpType});
		}catch(EmptyResultDataAccessException e){
			return 0;
		}
	}

}
