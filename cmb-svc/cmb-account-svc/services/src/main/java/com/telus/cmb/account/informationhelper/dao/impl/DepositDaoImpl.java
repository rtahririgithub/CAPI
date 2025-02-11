package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.telus.api.account.AccountManager;
import com.telus.cmb.account.informationhelper.dao.DepositDao;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.eas.account.info.DepositAssessedHistoryInfo;
import com.telus.eas.account.info.DepositHistoryInfo;

public class DepositDaoImpl extends MultipleJdbcDaoTemplateSupport implements DepositDao {
	private static final Logger LOGGER = Logger.getLogger(DepositDaoImpl.class);

	private String dateFormatSt = "MM/dd/yyyy";
	private SimpleDateFormat dateFormat   =  new SimpleDateFormat(dateFormatSt);

	@Override
	public List<DepositHistoryInfo> retrieveDepositHistory(final int ban, final Date fromDate,
			final Date toDate) {
		
		if(AppConfiguration.isWRPPh3GetDepositHistoryRollback()){
			String callString = "{? = call HISTORY_UTILITY_PKG.GetAccountDepositHistory(?, ?, ?, ?, ?)}";
			return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<List<DepositHistoryInfo>>() {

				@Override
				public List<DepositHistoryInfo> doInCallableStatement(
						CallableStatement callable) throws SQLException,
						DataAccessException {
					List<DepositHistoryInfo> list = new ArrayList<DepositHistoryInfo>();

					// set/register input/output parameters
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, ban);
					callable.setString(3, dateFormat.format(fromDate));
					callable.setString(4, dateFormat.format(toDate));
					callable.registerOutParameter(5, OracleTypes.CURSOR);
					callable.registerOutParameter(6, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					ResultSet result = null;
					try {
						if (success) {
							result = (ResultSet) callable.getObject(5);

							while (result.next()) {
								DepositHistoryInfo info = new DepositHistoryInfo();
								info.setInvoiceCreationDate(result.getDate("inv_creation_date"));
								info.setInvoiceDueDate(result.getDate("inv_due_date"));
								info.setInvoiceStatus(result.getString("inv_status"));
								info.setChargesAmount(result.getDouble("charges_amt"));
								info.setDepositPaidAmount(result.getDouble("dep_paid_amt"));
								info.setDepositPaidDate(result.getDate("dep_paid_date"));
								info.setDepositReturnDate(result.getDate("dep_return_date"));
								info.setDepositReturnMethod(result.getString("dep_return_mthd"));
								info.setDepositTermsCode(result.getString("dep_terms_code"));
								info.setCancellationDate(result.getDate("cancel_date"));
								info.setCancellationReasonCode(result.getString("cancel_rsn_code"));
								info.setPaymentExpIndicator(result.getString("pym_exp_ind"));
								info.setSubscriberId(result.getString("subscriber_no"));
								info.setOperatorId(result.getInt("operator_id"));

								info.setReturnedAmount(result.getDouble("dep_return_amt"));

								double charge = result.getDouble("charges_amt");

								if ((charge != 0.0) && (info.getCancellationDate() != null))
									info.setCancelledAmount(charge);

								list.add(info);
							}
						}
						else {
							LOGGER.debug("Stored procedure failed: " + callable.getString(6));
						}
					} finally {
						if (result != null ) {
							result.close();
						}
					}

					return list;
				}


			});
		} else {
			
			if(ban <= 0 || null==fromDate || null == toDate ){
				LOGGER.warn(" Error in the parameter ban ["+ban+"] fromDate["+fromDate+"] toDate["+toDate+"] ");
				return new ArrayList<DepositHistoryInfo>();
			}
			
			StringBuffer sql = new StringBuffer("  SELECT inv_creation_date, inv_due_date, inv_status, ");
			sql.append(" charges_amt, dep_paid_amt, dep_paid_date, ");
			sql.append(" dep_return_date, dep_return_mthd, dep_terms_code, ");
			sql.append(" cancel_date, cancel_rsn_code, pym_exp_ind, ");
			sql.append(" subscriber_no, operator_id, dep_return_amt,charges_amt ");
			sql.append(" FROM invoice_item ");
			sql.append(" WHERE ban = :ban ");
			sql.append(" AND inv_type = 'D' ");
			sql.append(" AND inv_creation_date BETWEEN TO_DATE ('"+dateFormat.format(fromDate)+"','mm/dd/yyyy')AND TO_DATE ('"+dateFormat.format(toDate)+"','mm/dd/yyyy') ");
			sql.append(" ORDER BY inv_creation_date DESC, sys_creation_date DESC ");
			
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("ban", ban);
			
			LOGGER.debug("retrieveDepositHistory (BAN:"+ban+") SQL:"+sql.toString());
			
			return (List<DepositHistoryInfo>) super.getKnowbilityNamedParameterJdbcTemplate().query(sql.toString(), namedParameters, new ResultSetExtractor<Collection<DepositHistoryInfo>>(){
				
				@Override
				public Collection<DepositHistoryInfo> extractData(ResultSet result)	throws SQLException, DataAccessException {
					
					Collection<DepositHistoryInfo> list = new ArrayList<DepositHistoryInfo>();
					try{		
						if(result.isBeforeFirst()) {
							while (result.next()) {
								DepositHistoryInfo info = new DepositHistoryInfo();
								info.setInvoiceCreationDate(result.getDate("inv_creation_date"));
								info.setInvoiceDueDate(result.getDate("inv_due_date"));
								info.setInvoiceStatus(result.getString("inv_status"));
								info.setChargesAmount(result.getDouble("charges_amt"));
								info.setDepositPaidAmount(result.getDouble("dep_paid_amt"));
								info.setDepositPaidDate(result.getDate("dep_paid_date"));
								info.setDepositReturnDate(result.getDate("dep_return_date"));
								info.setDepositReturnMethod(result.getString("dep_return_mthd"));
								info.setDepositTermsCode(result.getString("dep_terms_code"));
								info.setCancellationDate(result.getDate("cancel_date"));
								info.setCancellationReasonCode(result.getString("cancel_rsn_code"));
								info.setPaymentExpIndicator(result.getString("pym_exp_ind"));
								info.setSubscriberId(result.getString("subscriber_no"));
								info.setOperatorId(result.getInt("operator_id"));

								info.setReturnedAmount(result.getDouble("dep_return_amt"));

								double charge = result.getDouble("charges_amt");

								if ((charge != 0.0) && (info.getCancellationDate() != null))
									info.setCancelledAmount(charge);

								list.add(info);
							}
						} else {
							LOGGER.debug("The given BAN, fromDate and toDate didnt return a record from the stored query!");
						}
						
					}finally{
						if (result != null ) {
							result.close();
						}
					}
					
					return list;
				}
			});
		}
		
	}
	
	@Override
	public List<DepositAssessedHistoryInfo> retrieveDepositAssessedHistoryList(
			final int ban) {
		String callString = "{ call HISTORY_UTILITY_PKG.GetAccountDepAssessedHistory(?,?) }";
		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<List<DepositAssessedHistoryInfo>>() {

			@Override
			public List<DepositAssessedHistoryInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,
					DataAccessException {
				List<DepositAssessedHistoryInfo> list = new ArrayList<DepositAssessedHistoryInfo>();

				// set/register input/output parameters
				callable.setInt(1, ban);
				callable.registerOutParameter(2, OracleTypes.CURSOR);

				callable.execute();

				ResultSet result = null;
				try {
					result = (ResultSet) callable.getObject(2);

					while (result.next()) {
						DepositAssessedHistoryInfo info = new DepositAssessedHistoryInfo();
						info.setChangeDate(result.getDate("credit_date"));
						info.setDepositAssessed(result.getDouble("deposit_amt"));
						info.setDescription(result.getString("gen_desc"));
						info.setProductType(result.getString("product_type"));
						info.setUser(result.getString("user_full_name")+" ("+result.getString("operator_id")+")");
						info.setDescriptionFrench(result.getString("gen_desc_f"));

						list.add(info);
					}
				} finally {
					if (result != null ) {
						result.close();
					}
				}
				return list;
			}
		});
	}

	@Override
	public List<DepositAssessedHistoryInfo> retrieveOriginalDepositAssessedHistoryList(
			final int ban) {
		String callString = "{ call HISTORY_UTILITY_PKG.GetOrgAccDepAssessedHistory(?,?) }";
		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<List<DepositAssessedHistoryInfo>>() {

			@Override
			public List<DepositAssessedHistoryInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,
					DataAccessException {
				List<DepositAssessedHistoryInfo> list = new ArrayList<DepositAssessedHistoryInfo>();
				ResultSet result = null;
				
				callable.setInt(1, ban);
				callable.registerOutParameter(2, OracleTypes.CURSOR);

				callable.execute();

				try {
					result = (ResultSet) callable.getObject(2);

					while (result.next()) {
						DepositAssessedHistoryInfo info = new DepositAssessedHistoryInfo();
						info.setChangeDate(result.getDate("credit_date"));
						info.setDepositAssessed(result.getDouble("deposit_amt"));
						info.setDescription(null);
						info.setProductType(result.getString("product_type"));
						info.setUser(result.getString("operator_id"));
						info.setDescriptionFrench(null);

						list.add(info);
					}
				} finally {
					if (result != null) {
						result.close();
					}
				}
				
				return list;
			}

		});
	}
}
