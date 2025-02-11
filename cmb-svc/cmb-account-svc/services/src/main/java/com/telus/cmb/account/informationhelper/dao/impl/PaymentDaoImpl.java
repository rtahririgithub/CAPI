package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.telus.api.account.AccountManager;
import com.telus.cmb.account.informationhelper.dao.PaymentDao;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.common.util.TelusConstants;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.PaymentActivityInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PaymentMethodChangeHistoryInfo;
import com.telus.eas.account.info.RefundHistoryInfo;


public class PaymentDaoImpl extends MultipleJdbcDaoTemplateSupport implements
PaymentDao {

	public static String dateFormatSt = "MM/dd/yyyy";
	public static SimpleDateFormat dateFormat   =  new SimpleDateFormat(dateFormatSt);
	private final Logger LOGGER = Logger.getLogger(PaymentDaoImpl.class);

	@Override
	public PaymentHistoryInfo retrieveLastPaymentActivity(int ban) {

		String sql = " SELECT  DISTINCT pa.actv_date, p.pym_method, p.pym_sub_method, nvl(p.original_amt,0), " +
		"               nvl(p.amt_due,0), p.source_type, p.source_id, " +
		"               p.bank_code, p.bank_account_no, p.bank_branch_number, p.check_no, " +
		"               p.cr_card_no, to_char(p.cr_card_exp_date,'mm'), to_char(p.cr_card_exp_date,'yyyy'), " +
		"               p.deposit_date, nvl(pa.actv_amt,0), pa.actv_code, pa.actv_reason_code, " +
		"               pa.bl_ignore_ind, pa.actv_seq_no " +
		"               ,p.pymt_card_first_six_str, p.pymt_card_last_four_str " +
		" FROM    payment p, payment_activity pa " +
		" WHERE   pa.ban = ?" +
		" AND     p.ban = pa.ban " +
		" AND     p.ent_seq_no = pa.ent_seq_no " +
		" AND     pa.ent_seq_no = (select max(ent_seq_no) " +
		"                          from payment_activity pa2 " +
		"                          where pa2.ban = pa.ban) " +
		" ORDER BY pa.actv_seq_no desc ";

		return super.getKnowbilityJdbcTemplate().query(sql, new Object[] {ban}, new ResultSetExtractor<PaymentHistoryInfo>() {

			@Override
			public PaymentHistoryInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				PaymentHistoryInfo paymentHistoryInfo = new PaymentHistoryInfo();
				if (rs.next()) {
					paymentHistoryInfo.setDate(rs.getDate(1));
					paymentHistoryInfo.setPaymentMethodCode(rs.getString(2));
					paymentHistoryInfo.setPaymentMethodSubCode(rs.getString(3));
					paymentHistoryInfo.setOriginalAmount(rs.getDouble(4));
					paymentHistoryInfo.setAmountDue(rs.getDouble(5));
					paymentHistoryInfo.setSourceTypeCode(rs.getString(6));
					paymentHistoryInfo.setSourceID(rs.getString(7));
					paymentHistoryInfo.setDepositDate(rs.getDate(15));
					paymentHistoryInfo.setActualAmount(rs.getDouble(16));
					paymentHistoryInfo.setActivityCode(rs.getString(17) == null ? "" : rs.getString(17).trim() );
					paymentHistoryInfo.setActivityReasonCode(rs.getString(18) == null ? "" : rs.getString(18).trim());
					paymentHistoryInfo.isBalanceIgnoreFlag(rs.getString(19));
					paymentHistoryInfo.setSeqNo(rs.getInt(20));
					if (!(paymentHistoryInfo.getPaymentMethodCode()==null)) {
						if (paymentHistoryInfo.getPaymentMethodCode().equals(TelusConstants.PAYMENT_METHOD_CHEQUE))
						{
							paymentHistoryInfo.getCheque0().getBankAccount0().setBankCode(rs.getString(8));
							paymentHistoryInfo.getCheque0().getBankAccount0().setBankAccountNumber(rs.getString(9));
							paymentHistoryInfo.getCheque0().getBankAccount0().setBankBranchNumber(rs.getString(10));
							paymentHistoryInfo.getCheque0().setChequeNumber(rs.getString(11));
						}
						if (paymentHistoryInfo.getPaymentMethodCode().equals(TelusConstants.PAYMENT_METHOD_CREDIT_CARD))
						{
							paymentHistoryInfo.getCreditCard0().setToken(rs.getString(12)); 
							paymentHistoryInfo.getCreditCard0().setLeadingDisplayDigits(rs.getString(21)); 
							paymentHistoryInfo.getCreditCard0().setTrailingDisplayDigits(rs.getString(22)); 				        	 
							if (rs.getString(13) != null) paymentHistoryInfo.getCreditCard0().setExpiryMonth(Integer.parseInt(rs.getString(13)));
							if (rs.getString(14) != null) paymentHistoryInfo.getCreditCard0().setExpiryYear(Integer.parseInt(rs.getString(14)));
						}
					}
					return paymentHistoryInfo;
				}
				return null;
			}
		});	     
	}

	@Override
	public List<PaymentHistoryInfo> retrievePaymentHistory(final int ban, final Date fromDate, final Date toDate) {
		
		if (AppConfiguration.isWRPPh3Rollback()) {
			
			String call = "{ call history_utility_pkg.getpaymenthistory (?,?,?,?) } ";
			
			return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<List<PaymentHistoryInfo>>() {

				@Override
				public List<PaymentHistoryInfo> doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
					
					List<PaymentHistoryInfo> paymentHistoryList = new ArrayList<PaymentHistoryInfo>();
					ResultSet result = null;
					try {
						String startDate = dateFormat.format(fromDate);
						String endDate = dateFormat.format(toDate);

						callable.setInt(1, ban);
						callable.setString(2, startDate);
						callable.setString(3, endDate);
						callable.registerOutParameter(4, ORACLE_REF_CURSOR);
						callable.execute();
						result = (ResultSet) callable.getObject(4);

						while (result.next()) {
							PaymentHistoryInfo paymentHistoryInfo = new PaymentHistoryInfo();
							paymentHistoryInfo.setDate(result.getDate(1));
							paymentHistoryInfo.setPaymentMethodCode(result.getString(2));
							paymentHistoryInfo.setPaymentMethodSubCode(result.getString(3));
							paymentHistoryInfo.setOriginalAmount(result.getDouble(4));
							paymentHistoryInfo.setAmountDue(result.getDouble(5));
							paymentHistoryInfo.setSourceTypeCode(result.getString(6));
							paymentHistoryInfo.setSourceID(result.getString(7));
							paymentHistoryInfo.setDepositDate(result.getDate(15));
							paymentHistoryInfo.setActualAmount(result.getDouble(16));
							paymentHistoryInfo.setBillDate(result.getDate(17));
							paymentHistoryInfo.setActivityCode(result.getString(18));
							paymentHistoryInfo.setActivityReasonCode(result.getString(19));
							paymentHistoryInfo.isBalanceIgnoreFlag(result.getString(20));
							paymentHistoryInfo.setSeqNo(result.getInt(21));
							if (!(paymentHistoryInfo.getPaymentMethodCode() == null)) {
								if (paymentHistoryInfo.getPaymentMethodCode().equals(TelusConstants.PAYMENT_METHOD_CHEQUE)) {
									paymentHistoryInfo.getCheque0().getBankAccount0().setBankCode(result.getString(8));
									paymentHistoryInfo.getCheque0().getBankAccount0().setBankAccountNumber(result.getString(9));
									paymentHistoryInfo.getCheque0().getBankAccount0().setBankBranchNumber(result.getString(10));
									paymentHistoryInfo.getCheque0().setChequeNumber(result.getString(11));
								}
								if (paymentHistoryInfo.getPaymentMethodCode().equals(TelusConstants.PAYMENT_METHOD_CREDIT_CARD)
								//defect PROD00175842 fix, for pre-authorized credit card payment, we also need to populate the credit card information	  
										|| paymentHistoryInfo.getPaymentMethodCode().equals(TelusConstants.PAYMENT_METHOD_PREAUTH_CREDIT_CARD)) {
									//PCI changes
									CreditCardInfo paymentCCInfo = paymentHistoryInfo.getCreditCard0();
									paymentCCInfo.setToken(result.getString(12));
									paymentCCInfo.setLeadingDisplayDigits(result.getString("PYMT_CARD_FIRST_SIX_STR"));
									paymentCCInfo.setTrailingDisplayDigits(result.getString("PYMT_CARD_LAST_FOUR_STR"));
									//paymentHistoryInfo.getCreditCard0().setNumber(result.getString(12)); 
									if (result.getString(13) != null)
										paymentHistoryInfo.getCreditCard0().setExpiryMonth(Integer.parseInt(result.getString(13)));
									if (result.getString(14) != null)
										paymentHistoryInfo.getCreditCard0().setExpiryYear(Integer.parseInt(result.getString(14)));
									if (result.getString(15) != null)
										paymentHistoryInfo.getCreditCard0().setAuthorizationCode(result.getString(22));
								}
							}
							paymentHistoryInfo.setOriginalBanId(result.getInt(23));
							paymentHistoryInfo.setFileSequenceNumber(result.getInt(24));
							paymentHistoryInfo.setBatchNumber(result.getInt(25));
							paymentHistoryInfo.setBatchLineNumber(result.getInt(26));

							paymentHistoryList.add(paymentHistoryInfo);

						}
					} finally {
						if (result != null) {
							result.close();
						}

					}
					return paymentHistoryList;
				}

			});
		} else {
			
			// TODO: externalize this query
			String query = "SELECT DISTINCT pa.actv_date, p.pym_method, p.pym_sub_method, NVL (p.original_amt, 0) original_amount, NVL (p.amt_due, 0) amount_due, p.source_type, p.source_id, "
					+ "p.bank_code, p.bank_account_no, p.bank_branch_number, p.check_no, p.cr_card_no, TO_CHAR (p.cr_card_exp_date, 'mm') cc_exp_month, "
					+ "TO_CHAR (p.cr_card_exp_date, 'yyyy') cc_exp_year, p.deposit_date, SUM (NVL (parf.actv_amt, 0)) actual_amount, b.bill_date, pa1.actv_code, pa1.actv_reason_code, "
					+ "pa1.bl_ignore_ind, pa.ent_seq_no, pa1.cr_card_auth_code, p.original_ban, p.file_seq_no, p.batch_no, p.batch_line_no, p.PYMT_CARD_FIRST_SIX_STR, p.PYMT_CARD_LAST_FOUR_STR "
					+ "FROM payment p JOIN payment_activity pa ON pa.ban = p.ban JOIN payment_activity pa1 ON pa1.ban = p.ban JOIN bill b ON p.ban = b.ban "
					+ "JOIN payment_activity parf ON parf.ban = p.ban WHERE p.ban = :ban AND pa.ent_seq_no = p.ent_seq_no AND pa.actv_code IN ('PYM', 'FNTT') "
					+ "AND pa.actv_date BETWEEN TO_DATE (:from_date,'mm/dd/yyyy') AND TO_DATE (:to_date,'mm/dd/yyyy') AND pa1.ent_seq_no  = p.ent_seq_no "
					+ "AND pa1.actv_seq_no = (SELECT MAX(pa3.actv_seq_no) FROM payment_activity pa3 WHERE pa3.ban = p.ban AND pa3.ent_seq_no=p.ent_seq_no ) "
					+ "AND pa.actv_bill_seq_no = b.bill_seq_no AND parf.ent_seq_no = p.ent_seq_no "
					+ "GROUP BY pa.actv_date, p.pym_method, p.pym_sub_method, NVL (p.original_amt, 0), NVL (p.amt_due, 0), p.source_type, p.source_id, p.bank_code, p.bank_account_no, "
					+ "p.bank_branch_number, p.check_no, p.cr_card_no, TO_CHAR (p.cr_card_exp_date, 'mm'), TO_CHAR (p.cr_card_exp_date, 'yyyy'), p.deposit_date, b.bill_date, pa1.actv_code, "
					+ "pa1.actv_reason_code, pa1.bl_ignore_ind, pa.ent_seq_no, pa1.cr_card_auth_code, p.original_ban, p.file_seq_no, p.batch_no, p.batch_line_no, p.PYMT_CARD_FIRST_SIX_STR, "
					+ "p.PYMT_CARD_LAST_FOUR_STR";

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("from_date", dateFormat.format(fromDate)); 
			namedParameters.addValue("to_date", dateFormat.format(toDate));
			namedParameters.addValue("ban", ban);
			
			return super.getKnowbilityNamedParameterJdbcTemplate().execute(query, namedParameters, new PreparedStatementCallback<List<PaymentHistoryInfo>>() {
				
				@Override
				public List<PaymentHistoryInfo> doInPreparedStatement(PreparedStatement stmt) throws SQLException, DataAccessException {

					List<PaymentHistoryInfo> paymentHistoryList = new ArrayList<PaymentHistoryInfo>();
					ResultSet result = null;
					try {						
						result = stmt.executeQuery();
						while (result.next()) {
							
							PaymentHistoryInfo paymentHistoryInfo = new PaymentHistoryInfo();
							paymentHistoryInfo.setDate(result.getDate("actv_date"));
							paymentHistoryInfo.setPaymentMethodCode(result.getString("pym_method"));
							paymentHistoryInfo.setPaymentMethodSubCode(result.getString("pym_sub_method"));
							paymentHistoryInfo.setOriginalAmount(result.getDouble("original_amount"));
							paymentHistoryInfo.setAmountDue(result.getDouble("amount_due"));
							paymentHistoryInfo.setSourceTypeCode(result.getString("source_type"));
							paymentHistoryInfo.setSourceID(result.getString("source_id"));
							paymentHistoryInfo.setDepositDate(result.getDate("deposit_date"));
							paymentHistoryInfo.setActualAmount(result.getDouble("actual_amount"));
							paymentHistoryInfo.setBillDate(result.getDate("bill_date"));
							paymentHistoryInfo.setActivityCode(result.getString("actv_code"));
							paymentHistoryInfo.setActivityReasonCode(result.getString("actv_reason_code"));
							paymentHistoryInfo.isBalanceIgnoreFlag(result.getString("bl_ignore_ind"));
							paymentHistoryInfo.setSeqNo(result.getInt("ent_seq_no"));
							paymentHistoryInfo.setOriginalBanId(result.getInt("original_ban"));
							paymentHistoryInfo.setFileSequenceNumber(result.getInt("file_seq_no"));
							paymentHistoryInfo.setBatchNumber(result.getInt("batch_no"));
							paymentHistoryInfo.setBatchLineNumber(result.getInt("batch_line_no"));
							
							if (paymentHistoryInfo.getPaymentMethodCode() != null) {								
								if (StringUtils.equals(paymentHistoryInfo.getPaymentMethodCode(), TelusConstants.PAYMENT_METHOD_CHEQUE)) {
									paymentHistoryInfo.getCheque0().getBankAccount0().setBankCode(result.getString("bank_code"));
									paymentHistoryInfo.getCheque0().getBankAccount0().setBankAccountNumber(result.getString("bank_account_no"));
									paymentHistoryInfo.getCheque0().getBankAccount0().setBankBranchNumber(result.getString("bank_branch_number"));
									paymentHistoryInfo.getCheque0().setChequeNumber(result.getString("check_no"));
								}
								
								if (paymentHistoryInfo.getPaymentMethodCode().equals(TelusConstants.PAYMENT_METHOD_CREDIT_CARD)
										|| paymentHistoryInfo.getPaymentMethodCode().equals(TelusConstants.PAYMENT_METHOD_PREAUTH_CREDIT_CARD)) {
									CreditCardInfo paymentCCInfo = paymentHistoryInfo.getCreditCard0();
									paymentCCInfo.setToken(result.getString(12));
									paymentCCInfo.setLeadingDisplayDigits(result.getString("PYMT_CARD_FIRST_SIX_STR"));
									paymentCCInfo.setTrailingDisplayDigits(result.getString("PYMT_CARD_LAST_FOUR_STR"));
									if (result.getString("cc_exp_month") != null) {
										paymentHistoryInfo.getCreditCard0().setExpiryMonth(Integer.parseInt(result.getString("cc_exp_month")));
									}
									if (result.getString("cc_exp_year") != null) {
										paymentHistoryInfo.getCreditCard0().setExpiryYear(Integer.parseInt(result.getString("cc_exp_year")));
									}
									if (result.getString("deposit_date") != null) {
										paymentHistoryInfo.getCreditCard0().setAuthorizationCode(result.getString("cr_card_auth_code"));
									}
								}
							}

							paymentHistoryList.add(paymentHistoryInfo);
						}
					} finally {
						if (result != null) {
							result.close();
						}

					}
					return paymentHistoryList;
				}
			});
		}
	}

	@Override
	public List<PaymentActivityInfo> retrievePaymentActivities(final int banId, final int paymentSeqNo) {
		
		String call = "{? = call HISTORY_UTILITY_PKG.GetPaymentActivities(?, ?, ?, ?)}";
		
		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<List<PaymentActivityInfo>>() {

			@Override
			public List<PaymentActivityInfo> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {

				ArrayList<PaymentActivityInfo> paymentActivities = new ArrayList<PaymentActivityInfo>();
				ResultSet result = null;
				try{
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, banId);
					callable.setInt(3, paymentSeqNo);
					callable.registerOutParameter(4, ORACLE_REF_CURSOR);
					callable.registerOutParameter(5, OracleTypes.VARCHAR);
					callable.execute();
					if (callable.getInt(1) == AccountManager.NUMERIC_TRUE) {
						result = (ResultSet)callable.getObject(4);

						while (result.next()) {
							PaymentActivityInfo paymentActivity = new PaymentActivityInfo();
							paymentActivity.setActivityCode( result.getString(1));
							paymentActivity.setActivityReasonCode(result.getString(2));
							paymentActivity.setDate(result.getTimestamp(3));
							paymentActivity.setAmount(result.getDouble(4));
							paymentActivity.setBillDate(result.getTimestamp(5));
							paymentActivity.setFundTransferBanId(result.getInt(6));
							paymentActivity.setDeclined(result.getInt(7) > 0 ? true : false);
							paymentActivity.setExceptionReasonCode(result.getString(8));
							paymentActivity.setKnowbilityOperatorID(String.valueOf(result.getInt(9)));
							//defect PROD00175844 fix, need to handle Null value for bl_ignore_ind to avoid throwing NullPointerException
							//which cause no activity being returned
							//paymentActivity.setDisplayOnBill(result.getString(10).equals("Y") ? false : true);
							paymentActivity.setDisplayOnBill( "N".equals(result.getString(10)) ? true: false);
							paymentActivity.setCreditCardAuthorizationCode( result.getString("cr_card_auth_code"));

							paymentActivities.add(paymentActivity);
						}
					} else {
						String errorMessage = callable.getString(5);
						LOGGER.debug("Stored procedure failed: "+errorMessage);
					}
				}finally{
					if (result!=null ){
						result.close();
					}
				}
				return paymentActivities;			

			}
		});
	}

	@Override
	public List<PaymentMethodChangeHistoryInfo> retrievePaymentMethodChangeHistory(
			final int ban, final Date fromDate, final Date toDate) {
		String call = "{? = call HISTORY_UTILITY_PKG.GetPaymentMethodChangeHistory(?, ?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<List<PaymentMethodChangeHistoryInfo>>() {

			@Override
			public List<PaymentMethodChangeHistoryInfo> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {

				ArrayList<PaymentMethodChangeHistoryInfo> paymentMethodChange = new ArrayList<PaymentMethodChangeHistoryInfo>();
				ResultSet result = null;
				try{
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, ban);
					callable.setString(3, dateFormat.format(fromDate));
					callable.setString(4, dateFormat.format(toDate));
					callable.registerOutParameter(5, OracleTypes.CURSOR);
					callable.registerOutParameter(6, OracleTypes.VARCHAR);
					callable.execute();
					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						result = (ResultSet) callable.getObject(5);

						while (result.next()) {
							PaymentMethodChangeHistoryInfo paymentMethodChangeHistoryInfo = new PaymentMethodChangeHistoryInfo();
							paymentMethodChangeHistoryInfo.setDate(result.getDate(1));
							paymentMethodChangeHistoryInfo.setCreditCardType(result.getString(2));
							//PCI changes
							paymentMethodChangeHistoryInfo.setCreditCardToken(result.getString(3));
							paymentMethodChangeHistoryInfo.setCreditCardLeadingDisplayDigits(result.getString("PYMT_CARD_FIRST_SIX_STR"));
							paymentMethodChangeHistoryInfo.setCreditCardTrailingDisplayDigits(result.getString("PYMT_CARD_LAST_FOUR_STR"));
							//paymentMethodChangeHistoryInfo.setCreditCardNo(result.getString(3)); 
							paymentMethodChangeHistoryInfo.setCreditCardExpiry(result.getString(4));
							paymentMethodChangeHistoryInfo.setDirectDebitStatusCode(result.getString(5));
							paymentMethodChangeHistoryInfo.setBankCode(result.getString(6));
							paymentMethodChangeHistoryInfo.setBankAccountNumber(result.getString(7));
							paymentMethodChangeHistoryInfo.setBankBranchNumber(result.getString(8));
							paymentMethodChange.add(paymentMethodChangeHistoryInfo);
						}
					}
					else {
						String errorMessage = callable.getString(5);
						LOGGER.error("Stored procedure failed: "+errorMessage);
					}
				}finally{
					if (result!=null  ){
						result.close();
					}
				}
				return paymentMethodChange;			
			}
		});
	}


	@Override
	public List<RefundHistoryInfo> retrieveRefundHistory(final int ban, final Date fromDate,final Date toDate) {
		final String methodName = "retrieveRefundHistory()";
		String call = "{? = call HISTORY_UTILITY_PKG.GetRefundHistory(?, ?, ?, ?, ?)} ";
		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<List<RefundHistoryInfo>>() {

			@Override
			public List<RefundHistoryInfo> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {

				List<RefundHistoryInfo> refundHistoryList =  new ArrayList<RefundHistoryInfo>();
				ResultSet result = null;
				try {
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, ban);
					callable.setString(3, dateFormat.format(fromDate));
					callable.setString(4, dateFormat.format(toDate));
					callable.registerOutParameter(5, OracleTypes.CURSOR);
					callable.registerOutParameter(6, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE ;
					if (success) {
						result = (ResultSet) callable.getObject(5);

						while (result.next()) {
							RefundHistoryInfo info = new RefundHistoryInfo();
							info.setDate(result.getDate("actv_date"));
							info.setCode(result.getString("actv_code"));
							info.setReasonCode(result.getString("actv_reason_code"));
							info.setAmount(result.getDouble("actv_amt"));
							info.isAccountsPayableProcessFlag(result.getString("extract_to_ap_ind"));
							info.setAccountPayableProcessDate(result.getDate("extract_to_ap_date"));
							info.setRefPaymentMethod(result.getString("ref_payment_method"));
							info.setBankCode(result.getString("bank_code"));
							info.setBankAccountNumber(result.getString("bank_acct_no"));
							info.setChequeNumber(result.getString("check_no"));
							info.setCreditCardNumber(result.getString("cr_card_no"));
							info.setCreditCardAuthCode(result.getString("cr_card_auth_code"));
							info.setCreditCardExpiryDate(result.getDate("cr_card_exp_date"));
							info.setCouponGiftNumber(result.getString("coupon_gift_no"));

							refundHistoryList.add(info);

						}
					}else {

						LOGGER.error(getClass().getName()+"."+ methodName+"Stored procedure failed:"+ callable.getString(6));
					}

				} finally {
					if (result!=null ){
						result.close();
					}
				}

				return refundHistoryList;  


			}

		});

	}




}
