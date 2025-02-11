package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.subscriber.lifecyclehelper.dao.AdjustmentDao;


public class AdjustmentDaoImpl extends MultipleJdbcDaoTemplateSupport implements AdjustmentDao {

	@Override
	public List<Double> retrieveAdustmentDetails(final int ban, final String adjustmentReasonCode, final String subscriberId, final Date searchFromDate,
			final Date searchToDate) throws ApplicationException {

		String sql = "SELECT ACTV_AMT  FROM ADJUSTMENT  WHERE BAN = ? AND SUBSCRIBER_NO = ? AND ACTV_REASON_CODE = ? AND ADJ_CREATION_DATE BETWEEN ? AND ?"+
				"  UNION  SELECT ACTV_AMT  FROM PENDING_ADJUSTMENT  WHERE BAN = ? AND SUBSCRIBER_NO = ? AND ACTV_REASON_CODE = ? AND ADJ_CREATION_DATE BETWEEN ? AND ? ";

		final java.sql.Timestamp fromDate = new Timestamp(searchFromDate.getTime());
		final java.sql.Timestamp toDate = new Timestamp(searchToDate.getTime());
		return super.getKnowbilityJdbcTemplate().execute(sql, new PreparedStatementCallback<List<Double>>() {
			@Override
			public List<Double> doInPreparedStatement(PreparedStatement pstmt)
			throws SQLException, DataAccessException {

				ResultSet rset = null;
				List<Double> list = new ArrayList<Double>();
				try {
					pstmt.setInt(1, ban);
					pstmt.setString(2, subscriberId);
					pstmt.setString(3, adjustmentReasonCode);
					pstmt.setTimestamp(4, fromDate);
					pstmt.setTimestamp(5, toDate);
					pstmt.setInt(6, ban);
					pstmt.setString(7, subscriberId);
					pstmt.setString(8, adjustmentReasonCode);
					pstmt.setTimestamp(9, fromDate);
					pstmt.setTimestamp(10, toDate);
					
					rset = pstmt.executeQuery();
					while(rset.next()) {
						list.add(rset.getDouble("ACTV_AMT"));
					}
				} finally {
					if (rset != null ) {
						rset.close();
					}
				}

				return list;
			}
		});
	}
	
}
