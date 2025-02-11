package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.subscriber.lifecyclehelper.dao.ProvisioningDao;
import com.telus.eas.subscriber.info.ProvisioningTransactionInfo;

public class ProvisioningDaoImpl extends MultipleJdbcDaoTemplateSupport   implements ProvisioningDao {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	@Override
	public List<ProvisioningTransactionInfo> retrieveProvisioningTransactions(
			final int customerID, final String subscriberID, final Date from, final Date to) {

		String fromDate = dateFormat.format(from) ;
		String toDate =   dateFormat.format(to) ;
		String sql = "SELECT r.subscriber_no \"Subscriber No\", " +
		"       r.customer_id \"BAN\", " +
		"       r.srv_trx_s_no \"Transaction Number\", " +
		"       r.srv_sts_cd \"Status\", " +
		"       r.eff_date_time \"Effective Date/Time\", " +
		"       r.srv_trx_tp_cd \"Transaction Type Code\", " +
		"       r.product_type \"Product Type Code\", " +
		"       r.err_cd \"Error Reason\", " +
		"       r.user_id \"User ID\" " +
		"FROM   srv_trx_repos r " +
		"WHERE  r.subscriber_no = ? " +
		"AND    r.customer_id = ? " +
		"AND    r.eff_date_time between to_date(?, 'mm/dd/yyyy') AND to_date(?, 'mm/dd/yyyy') " +
		"ORDER BY r.eff_date_time desc";
		return super.getKnowbilityJdbcTemplate().query(sql,new Object[]{subscriberID,customerID,fromDate,toDate}, new RowMapper<ProvisioningTransactionInfo>(){

			@Override
			public ProvisioningTransactionInfo mapRow(ResultSet result, int rowNum)
			throws SQLException {

				ProvisioningTransactionInfo provisioningTransactionInfo  = new ProvisioningTransactionInfo();
				provisioningTransactionInfo.setSubscriberId(subscriberID);
				provisioningTransactionInfo.setTransactionNo(result.getString(3));
				provisioningTransactionInfo.setStatus(result.getString(4));
				provisioningTransactionInfo.setEffectiveDate(result.getTimestamp(5));
				provisioningTransactionInfo.setTransactionType(result.getString(6));
				provisioningTransactionInfo.setProductType(result.getString(7));
				provisioningTransactionInfo.setErrorReason(result.getString(8));
				provisioningTransactionInfo.setUserID(result.getString(9));
				return provisioningTransactionInfo;
			}
		});
	}

	@Override
	public String retrieveSubscriberProvisioningStatus(int ban,
			String phoneNumber) {
		phoneNumber = AttributeTranslator.replaceString(phoneNumber.trim()," ","");
		phoneNumber = AttributeTranslator.replaceString(phoneNumber.trim(),"-","");
		String queryString = "select SRV_STS_CD from  SRV_TRX_REPOS where   SUBSCRIBER_NO = ? and  CUSTOMER_ID = ? order by SYS_UPDATE_DATE";
		return super.getKnowbilityJdbcTemplate().query(queryString,new Object[]{phoneNumber,ban}, new ResultSetExtractor<String>(){

			@Override
			public String extractData(ResultSet rset) throws SQLException,
					DataAccessException {
				
				if(rset.next()){
					return rset.getString(1);
				}else{
					return null;
				}
			}
		});
	}

}
