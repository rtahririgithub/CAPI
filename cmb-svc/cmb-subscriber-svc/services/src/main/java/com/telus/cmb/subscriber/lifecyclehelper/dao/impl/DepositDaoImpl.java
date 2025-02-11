package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.telus.api.account.AccountManager;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.subscriber.lifecyclehelper.dao.DepositDao;
import com.telus.eas.account.info.DepositHistoryInfo;

public class DepositDaoImpl extends MultipleJdbcDaoTemplateSupport implements
DepositDao {

	private Logger logger = Logger.getLogger(DepositDaoImpl.class);

	@Override
	public List<DepositHistoryInfo> retrieveDepositHistory(final int ban,
			final String subscriber) {
		String callString="{? = call HISTORY_UTILITY_PKG.GetSubscriberDepositHistory(?, ?, ?, ?)}";
		return  super.getKnowbilityJdbcTemplate().execute(callString,new CallableStatementCallback<List<DepositHistoryInfo>>(){
			@Override
			public List<DepositHistoryInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{
				List<DepositHistoryInfo> list = new ArrayList<DepositHistoryInfo>();
				ResultSet result = null;
				try{
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, ban);
					callable.setString(3, subscriber);
					callable.registerOutParameter(4, OracleTypes.CURSOR);
					callable.registerOutParameter(5, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;
					if (success) {
						result = (ResultSet) callable.getObject(4);
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

							list.add(info);
						}
					}else{
						logger.debug("Stored procedure Failed");
					}
				}finally{
					if(result != null)
						result.close();
				}
				return list;
			}
		});	

	}

	@Override
	public double retrievePaidSecurityDeposit(final int banId,
			final String subscriberNo, final String productType) {
		String callString = "{? = call SUBSCRIBER_PKG.getPaidSecurityDeposit(?,?,?)}";
		return super.getKnowbilityJdbcTemplate().execute(callString,
				new CallableStatementCallback<Double>() {
			@Override
			public Double doInCallableStatement(
					CallableStatement callable) throws SQLException,
					DataAccessException {
				double result = 0;
				
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, banId);
					callable.setString(3, subscriberNo);
					callable.setString(4, productType);
					callable.execute();
					result = callable.getDouble(1);
					return result;
			}
		});
	}

}
