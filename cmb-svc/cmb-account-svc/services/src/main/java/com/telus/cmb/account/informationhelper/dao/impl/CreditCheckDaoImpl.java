package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.telus.api.account.AccountManager;
import com.telus.cmb.account.informationhelper.dao.CreditCheckDao;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.PersonalCreditInfo;

public class CreditCheckDaoImpl extends MultipleJdbcDaoTemplateSupport implements CreditCheckDao {

	@Override
	public CreditCheckResultInfo retrieveLastCreditCheckResultByBan(final int ban, final String productType) {
		
		if (AppConfiguration.isWRPPh3Rollback()) {

			String call = "{? = call CRDCHECK_RESULT_PKG.getLastCreditCheckResultByBan(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

			return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<CreditCheckResultInfo>() {
				
				@SuppressWarnings("deprecation")
				@Override
				public CreditCheckResultInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {

					CreditCheckResultInfo creditCheckResultInfo = null;
					BusinessCreditIdentityInfo selectedBusiness = null;
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, ban);
					callable.setString(3, productType);
					callable.registerOutParameter(4, OracleTypes.VARCHAR);
					callable.registerOutParameter(5, OracleTypes.NUMBER);
					callable.registerOutParameter(6, OracleTypes.NUMBER);
					callable.registerOutParameter(7, OracleTypes.VARCHAR);
					callable.registerOutParameter(8, OracleTypes.NUMBER);
					callable.registerOutParameter(9, OracleTypes.VARCHAR);
					callable.registerOutParameter(10, OracleTypes.VARCHAR);
					callable.registerOutParameter(11, OracleTypes.VARCHAR);
					callable.registerOutParameter(12, OracleTypes.VARCHAR);
					callable.registerOutParameter(13, OracleTypes.VARCHAR);
					callable.registerOutParameter(14, OracleTypes.DATE);
					callable.registerOutParameter(15, OracleTypes.VARCHAR);
					callable.registerOutParameter(16, OracleTypes.DATE);
					callable.registerOutParameter(17, OracleTypes.VARCHAR);
					callable.registerOutParameter(18, OracleTypes.DATE);
					callable.registerOutParameter(19, OracleTypes.DATE);
					callable.registerOutParameter(20, OracleTypes.VARCHAR);
					callable.registerOutParameter(21, OracleTypes.VARCHAR);
					callable.registerOutParameter(22, OracleTypes.NUMBER);
					callable.registerOutParameter(23, OracleTypes.VARCHAR);

					//PCI changes: retrieve table credit_hisotry pymt_card_first_six_str,pymt_card_last_four_str 
					callable.registerOutParameter(24, OracleTypes.VARCHAR);
					callable.registerOutParameter(25, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						creditCheckResultInfo = new CreditCheckResultInfo();
						creditCheckResultInfo.setCreditClass(callable.getString(4));
						creditCheckResultInfo.setLimit(callable.getDouble(5));
						creditCheckResultInfo.setDeposit(callable.getDouble(6), productType);
						creditCheckResultInfo.setMessage(StringUtils.trimToEmpty(callable.getString(7)));
						creditCheckResultInfo.setCreditScore(callable.getInt(8));
						creditCheckResultInfo.setReferToCreditAnalyst(callable.getString(9) == null ? true : callable.getString(9).equals("Y") ? true : false);
						creditCheckResultInfo.setMessageFrench(StringUtils.trimToEmpty(callable.getString(10)));
						creditCheckResultInfo.setCreditCheckResultStatus(callable.getString(11));

						PersonalCreditInfo personalCreditInfo = new PersonalCreditInfo();
						personalCreditInfo.setSin(callable.getString(12));
						personalCreditInfo.setDriversLicense(callable.getString(13));
						personalCreditInfo.setBirthDate(callable.getDate(14));
						//PCI change 
						personalCreditInfo.getCreditCard0().setToken(callable.getString(15));
						personalCreditInfo.getCreditCard0().setLeadingDisplayDigits(callable.getString(24));
						personalCreditInfo.getCreditCard0().setTrailingDisplayDigits(callable.getString(25));
						//personalCreditInfo.getCreditCard0().setNumber(callable.getString(15));
						if (callable.getDate(16) != null) {
							personalCreditInfo.getCreditCard0().setExpiryMonth(callable.getDate(16).getMonth());
							personalCreditInfo.getCreditCard0().setExpiryYear(callable.getDate(16).getYear() + 1900);
						}
						creditCheckResultInfo.setLastCreditCheckPersonalnformation(personalCreditInfo);

						creditCheckResultInfo.setLastCreditCheckIncorporationNumber(callable.getString(17));
						creditCheckResultInfo.setLastCreditCheckIncorporationDate(callable.getDate(18));
						creditCheckResultInfo.setCreditDate(callable.getDate(19));
						creditCheckResultInfo.setCreditParamType(callable.getString(20));
						creditCheckResultInfo.setDepositChangeReasonCode(callable.getString(21));
						if (callable.getString(23) != null) {
							selectedBusiness = new BusinessCreditIdentityInfo();
							selectedBusiness.setCompanyName(callable.getString(23));
							selectedBusiness.setMarketAccount(callable.getDouble(22));
							creditCheckResultInfo.setLastCreditCheckSelectedBusiness(selectedBusiness);
						}
					} else {
						creditCheckResultInfo = new CreditCheckResultInfo();
						creditCheckResultInfo.setCreditClass(CreditCheckResultInfo.CREDIT_CLASS_CONSTANT);
					}

					return creditCheckResultInfo;
				}
			});
		} else {
			
			// TODO: externalize this query
			String query = "SELECT ch.credit_class, ch.credit_limit, cd.deposit_amt, ch.credit_result_2, beacon_score, ccd.credit_referral_flg, ccd.french_message, ch.credit_req_sts, ch.SIN, "
					+ "ch.drivr_licns_no, ch.date_of_birth, ch.credit_card_no, ch.crd_card_exp_date, ch.incorporation_no, ch.incorporation_date, ch.credit_date, ch.credit_param_type, "
					+ "ch.dep_chg_rsn_cd, cbl.market_account, cbl.company_name, ch.pymt_card_first_six_str, ch.pymt_card_last_four_str, "
					+ "cbl2.market_account crd_bus_market_account, cbl2.company_name crd_bus_company_name "
					+ "FROM credit_history ch LEFT JOIN crd_deposit cd ON cd.deposit_seq_no = ch.deposit_seq_no "
					+ "LEFT JOIN credit_class_decision ccd ON ccd.credit_message_cd = SUBSTR (ch.credit_result_2, 1, 3) "
					+ "LEFT JOIN crd_business_list cbl ON cbl.business_l_seq_no = ch.business_l_seq_no AND cbl.crd_seq_no = ch.crd_seq_no "
					+ "LEFT JOIN crd_business_list cbl2 ON cbl2.business_l_seq_no = NVL(ch.business_l_seq_no, -1) AND cbl2.business_l_line  = NVL(ch.business_l_line, 0) "
					+ "WHERE ch.ban = :ban AND cd.product_type = :product_type AND ch.crd_seq_no = ("
					+ "SELECT MAX (crd_seq_no) FROM credit_history ch2 WHERE ch2.ban = ch.ban AND (ch2.credit_req_sts = 'D' OR ch2.credit_req_sts  IS NULL) ) "
					+ "AND (ch.credit_req_sts = 'D' OR ch.credit_req_sts  IS NULL)";
			
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("product_type", productType);
			namedParameters.addValue("ban", ban);
			
			return super.getKnowbilityNamedParameterJdbcTemplate().execute(query, namedParameters, new PreparedStatementCallback<CreditCheckResultInfo>() {
				
				@Override
				public CreditCheckResultInfo doInPreparedStatement(PreparedStatement stmt) throws SQLException, DataAccessException {
					
					CreditCheckResultInfo creditCheckResultInfo = null;
					ResultSet result = null;

					try {
						result = stmt.executeQuery();
						
						if (result.next()) {
							creditCheckResultInfo = new CreditCheckResultInfo();
							creditCheckResultInfo.setCreditClass(result.getString("credit_class"));
							creditCheckResultInfo.setLimit(result.getDouble("credit_limit"));
							creditCheckResultInfo.setDeposit(result.getDouble("deposit_amt"), productType);
							creditCheckResultInfo.setMessage(StringUtils.trimToEmpty(result.getString("credit_result_2")));
							creditCheckResultInfo.setMessageFrench(StringUtils.trimToEmpty(result.getString("french_message")));
							creditCheckResultInfo.setCreditScore(result.getInt("beacon_score"));
							creditCheckResultInfo.setCreditCheckResultStatus(result.getString("credit_req_sts"));
							creditCheckResultInfo.setCreditDate(result.getDate("credit_date"));
							creditCheckResultInfo.setCreditParamType(result.getString("credit_param_type"));
							creditCheckResultInfo.setDepositChangeReasonCode(result.getString("dep_chg_rsn_cd"));							
							creditCheckResultInfo.setLastCreditCheckIncorporationNumber(result.getString("incorporation_no"));
							creditCheckResultInfo.setLastCreditCheckIncorporationDate(result.getDate("incorporation_date"));
							
							PersonalCreditInfo personalCreditInfo = new PersonalCreditInfo();
							personalCreditInfo.setSin(result.getString("SIN"));
							personalCreditInfo.setDriversLicense(result.getString("drivr_licns_no"));
							personalCreditInfo.setBirthDate(result.getDate("date_of_birth"));
							personalCreditInfo.getCreditCard0().setToken(result.getString("credit_card_no"));
							personalCreditInfo.getCreditCard0().setLeadingDisplayDigits(result.getString("pymt_card_first_six_str"));
							personalCreditInfo.getCreditCard0().setTrailingDisplayDigits(result.getString("pymt_card_last_four_str"));
							Date creditCardExpiry = result.getDate("crd_card_exp_date");
							if (creditCardExpiry != null) {			
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeInMillis(creditCardExpiry.getTime());								
								personalCreditInfo.getCreditCard0().setExpiryMonth(calendar.get(Calendar.MONTH));
								personalCreditInfo.getCreditCard0().setExpiryYear(calendar.get(Calendar.YEAR));
							}
							creditCheckResultInfo.setLastCreditCheckPersonalnformation(personalCreditInfo);
							
							String referToCreditAnalystFlag = result.getString("credit_referral_flg");
							creditCheckResultInfo.setReferToCreditAnalyst(referToCreditAnalystFlag == null ? true : referToCreditAnalystFlag.equals("Y") ? true : false);
														
							// Set the BusinessCreditIdentityInfo based on "company name"
							String companyName = result.getString("company_name");
							String creditBusinessCompanyName = result.getString("crd_bus_company_name");							
							if (companyName != null) {
								BusinessCreditIdentityInfo selectedBusiness = new BusinessCreditIdentityInfo();
								selectedBusiness.setCompanyName(result.getString("company_name"));
								selectedBusiness.setMarketAccount(result.getDouble("market_account"));
								creditCheckResultInfo.setLastCreditCheckSelectedBusiness(selectedBusiness);	
							} else if (creditBusinessCompanyName != null) {
								BusinessCreditIdentityInfo selectedBusiness = new BusinessCreditIdentityInfo();
								selectedBusiness.setCompanyName(result.getString("crd_bus_company_name"));
								selectedBusiness.setMarketAccount(result.getDouble("crd_bus_market_account"));
								creditCheckResultInfo.setLastCreditCheckSelectedBusiness(selectedBusiness);
							}
						}
					} finally {
						if (result != null) {
							result.close();
						}
					}

					// Return default value
					if (creditCheckResultInfo == null) {
						creditCheckResultInfo = new CreditCheckResultInfo();
						creditCheckResultInfo.setCreditClass(CreditCheckResultInfo.CREDIT_CLASS_CONSTANT);
					}

				return creditCheckResultInfo;
				}
			});
		}		
	}

	@Override
	public CreditCheckResultDepositInfo[] retrieveDepositsByBan(final int ban) {

		String sql = "SELECT cd.deposit_amt, cd.product_type " + "FROM credit_history ch, crd_deposit cd " + "WHERE ch.ban = ? "
				+ "AND ch.crd_seq_no = (SELECT MAX (crd_seq_no) FROM credit_history ch2 WHERE ch2.ban = ch.ban AND (ch2.credit_req_sts = 'D' OR ch2.credit_req_sts IS NULL)) "
				+ "AND (ch.credit_req_sts = 'D' OR ch.credit_req_sts IS NULL) AND cd.deposit_seq_no = ch.deposit_seq_no";

		return super.getKnowbilityJdbcTemplate().execute(sql, new PreparedStatementCallback<CreditCheckResultDepositInfo[]>() {

			@Override
			public CreditCheckResultDepositInfo[] doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {

				ArrayList<CreditCheckResultDepositInfo> list = new ArrayList<CreditCheckResultDepositInfo>();
				ResultSet rset = null;
				try {
					pstmt.setInt(1, ban);
					rset = pstmt.executeQuery();
					while (rset.next()) {
						CreditCheckResultDepositInfo creditCheckResultDeposit = new CreditCheckResultDepositInfo();
						creditCheckResultDeposit.setDeposit(rset.getDouble(1));
						creditCheckResultDeposit.setProductType(rset.getString(2));
						list.add(creditCheckResultDeposit);
					}

				} finally {
					if (rset != null) {
						rset.close();
					}
				}

				return (CreditCheckResultDepositInfo[]) list.toArray(new CreditCheckResultDepositInfo[list.size()]);
			}
		});
	}

}