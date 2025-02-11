package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountManager;
import com.telus.api.reference.ChargeType;
import com.telus.cmb.account.informationhelper.dao.AdjustmentDao;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.common.util.PreparedStatementBuilder;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.Info;

@SuppressWarnings("deprecation")

public class AdjustmentDaoImpl extends MultipleJdbcDaoTemplateSupport implements
AdjustmentDao {

	private final Logger LOGGER = Logger.getLogger(AdjustmentDaoImpl.class);
	
	private final int MAX_MAXIMORUM = 1000;
	
	private String dateFormatSt = "MM/dd/yyyy";
	private SimpleDateFormat dateFormat   =  new SimpleDateFormat(dateFormatSt);

	@Override
	public CreditInfoHolder retrieveBilledCredits(int ban, Date fromDate,
			Date toDate, String billState, String knowbilityOperatorId,
			String reasonCode, char level, String subscriber, int maximum) {

		if(AppConfiguration.isWRPPh3GetBilledCreditRollback()){
			String sql="{? = call HISTORY_UTILITY_PKG.GetBilledCreditsEnhanced(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

			return getCreditInfoHolder(sql, ban, fromDate, toDate, billState, knowbilityOperatorId,reasonCode, level, subscriber, maximum, true);
		} else {

			if(!(maximum>0 && maximum<MAX_MAXIMORUM)) {
				maximum = MAX_MAXIMORUM;
			}
			
			int max = maximum;
			max++;
			
			String select = " SELECT adj.adj_creation_date, adj.effective_date, adj.actv_code, adj.actv_reason_code, adj.balance_impact_code, adj.subscriber_no, adj.product_type, adj.operator_id,"
					+ " adj.actv_amt, adj.tax_gst_amt, adj.tax_pst_amt, adj.tax_hst_amt, adj.soc, adj.feature_code, adj.ent_seq_no, adj.bl_ignore_ind, adj.charge_seq_no, adj.tax_roaming_amt, 'Y' "; //approval_status suppose to be 16

			StringBuffer sql = new StringBuffer("SELECT * FROM( ");
			sql.append("  SELECT a.*, ");
			sql.append(" (CASE WHEN (COUNT (*) OVER ()) > "+maximum+" THEN 0 ELSE 1 END) AS has_more ");
			sql.append(" FROM ( ");
			sql.append(" "+select+" ");
			sql.append(" FROM adjustment adj, bill b ");
			sql.append(" WHERE adj.ban = :iban AND adj_creation_date BETWEEN TO_DATE ('"+dateFormat.format(fromDate)+"','MM/dd/yyyy') AND TO_DATE ('"+dateFormat.format(toDate)+"','MM/dd/yyyy') ");
			sql.append(" AND adj.actv_bill_seq_no IS NOT NULL ");
			sql.append(" AND adj.ban = b.ban ");
			sql.append(" AND adj.actv_bill_seq_no = b.bill_seq_no ");
			sql.append(" AND b.bill_conf_status = 'C' ");
			
			
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("iban", ban);
	
			if(null!=subscriber && !"".equals(subscriber) && ChargeType.CHARGE_LEVEL_SUBSCRIBER == level ) {
				sql.append(" AND adj.subscriber_no = :subscriber ");
				namedParameters.addValue("subscriber", subscriber);
			}
			
			if(null!=reasonCode && !"".equals(reasonCode) ) {
				sql.append(" AND RTRIM(adj.actv_reason_code) = RTRIM(:reasonCode)  ");
				namedParameters.addValue("reasonCode", reasonCode);
			}
			
			if(null!=knowbilityOperatorId && !"".equals(knowbilityOperatorId)) {
				sql.append(" AND adj.operator_id = :knowbilityOperatorId  ");
				namedParameters.addValue("knowbilityOperatorId", knowbilityOperatorId);
			}
			
			
			sql.append(" ) a ");
			sql.append(" WHERE ROWNUM <= :max  ");
			sql.append(" ORDER BY adj_creation_date DESC, ent_seq_no DESC ");
			sql.append(" ) WHERE ROWNUM <= ( :max - 1) ");
			
			namedParameters.addValue("max", max);

			LOGGER.debug("retrieveBilledCredits (BAN:"+ban+") SQL:"+sql.toString());
			
			return getCreditInfoHolderWRP(sql.toString(),ban,namedParameters,true);
			
		}


	}
	
	@Override
	public CreditInfoHolder retrieveUnbilledCredits(int ban, Date fromDate,
			Date toDate, String billState, String knowbilityOperatorId,
			String reasonCode, char level, String subscriber, int maximum) {

		if(AppConfiguration.isWRPPh3GetUnBilledCreditRollback()){
			String sql="{? = call HISTORY_UTILITY_PKG.GetUnbilledCreditsEnhanced(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

			return getCreditInfoHolder(sql, ban, fromDate, toDate, billState, knowbilityOperatorId,reasonCode, level, subscriber, maximum, false);
		} else {
			
			if(!(maximum>0 && maximum<MAX_MAXIMORUM)) {
				maximum = MAX_MAXIMORUM;
			}
			
			int max = maximum;
			max++;
			
			String select = " SELECT adj.adj_creation_date, adj.effective_date, adj.actv_code, adj.actv_reason_code, adj.balance_impact_code, adj.subscriber_no, adj.product_type, adj.operator_id,"
					+ " adj.actv_amt, adj.tax_gst_amt, adj.tax_pst_amt, adj.tax_hst_amt, adj.soc, adj.feature_code, adj.ent_seq_no, adj.bl_ignore_ind, adj.charge_seq_no, adj.tax_roaming_amt "; //approval_status suppose to be 16
			
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("iban", ban);

			StringBuffer where = new  StringBuffer(" WHERE adj.ban = :iban AND adj_creation_date BETWEEN TO_DATE ('"+dateFormat.format(fromDate)+"','MM/dd/yyyy') AND TO_DATE ('"+dateFormat.format(toDate)+"','MM/dd/yyyy')");
		
			if(null!=subscriber && !"".equals(subscriber) && ChargeType.CHARGE_LEVEL_SUBSCRIBER == level ) {
				where.append(" AND adj.subscriber_no = :subscriber ");
				namedParameters.addValue("subscriber", subscriber);
			}
			
			if(null!=reasonCode && !"".equals(reasonCode) ) {
				where.append(" AND RTRIM(adj.actv_reason_code) = RTRIM(:reasonCode)  ");
				namedParameters.addValue("reasonCode", reasonCode);
			}
			
			if(null!=knowbilityOperatorId && !"".equals(knowbilityOperatorId)) {
				where.append(" AND adj.operator_id = :knowbilityOperatorId  ");
				namedParameters.addValue("knowbilityOperatorId",knowbilityOperatorId);
			}
						
			StringBuffer sql = new StringBuffer("SELECT * FROM( ");
			sql.append("  SELECT a.*, ");
			sql.append(" (CASE WHEN (COUNT (*) OVER ()) > :maximum THEN 0 ELSE 1 END) AS has_more ");
			sql.append(" FROM ( ");
			sql.append(" "+select+", adj.approval_status ");
			sql.append(" FROM pending_adjustment adj ");
			sql.append( where.toString());
			sql.append(" AND approval_status = 'P' ");
			sql.append(" AND ROWNUM <=:max ");
			sql.append(" UNION ");
			sql.append(" "+select+", 'Y' ");
			sql.append(" FROM adjustment adj ");
			sql.append( where.toString());
			sql.append(" AND actv_bill_seq_no IS NULL ");
			sql.append(" AND ROWNUM <=:max ");
			sql.append(" UNION ");
			sql.append(" "+select+", 'Y' ");
			sql.append(" FROM adjustment adj, bill b ");
			sql.append( where.toString());
			sql.append(" AND adj.ban = b.ban ");
			sql.append(" AND adj.actv_bill_seq_no = b.bill_seq_no ");
			sql.append(" AND b.bill_conf_status = 'T' ");
			sql.append(" AND ROWNUM <=:max ");
			sql.append(" ) a ");
			sql.append(" WHERE ROWNUM <=:max  ");
			sql.append(" ORDER BY adj_creation_date DESC, ent_seq_no DESC ");
			sql.append(" ) WHERE ROWNUM <= (:max - 1) ");
			
			namedParameters.addValue("maximum", maximum);
			namedParameters.addValue("max", max);
			
			LOGGER.debug("retrieveUnBilledCredits (BAN:"+ban+") SQL:"+sql.toString());
			
			return getCreditInfoHolderWRP(sql.toString(),ban,namedParameters,false);

		}
		
	}
	
	private CreditInfoHolder getCreditInfoHolderWRP(String sql, final int iban, MapSqlParameterSource namedParameters,final boolean billIndicatior) {
		
		return (CreditInfoHolder) super.getKnowbilityNamedParameterJdbcTemplate().query(sql, namedParameters, new ResultSetExtractor<CreditInfoHolder>() {

			@Override
			public CreditInfoHolder extractData(ResultSet result) throws SQLException, DataAccessException {
				Collection<CreditInfo> creditInfoList=new ArrayList<CreditInfo>();
				boolean hasMore=false;
				
				try{
					
					if(result.isBeforeFirst()) {
					
						while (result.next()) {
							CreditInfo creditInfo = new CreditInfo();
							creditInfo.setBan(iban);
							creditInfo.setCreationDate(result.getDate(1));
							creditInfo.setEffectiveDate(result.getDate(2));
							creditInfo.setActivityCode(result.getString(3)== null ? "":result.getString(3) );
							creditInfo.setReasonCode(result.getString(4)== null ? "":result.getString(4) );
							creditInfo.setBalanceImpactFlag(result.getString(5));
							creditInfo.setSubscriberId(result.getString(6));
							creditInfo.setProductType(result.getString(7));
							creditInfo.setOperatorId(result.getInt(8));
							creditInfo.setAmount(result.getDouble(9));
							creditInfo.getTaxSummary().setGSTAmount(result.getDouble(10));
							creditInfo.getTaxSummary().setPSTAmount(result.getDouble(11));
							creditInfo.getTaxSummary().setHSTAmount(result.getDouble(12));
							creditInfo.setSOC(result.getString(13));
							creditInfo.setFeatureCode(result.getString(14));
							creditInfo.setId(result.getInt(15));
							creditInfo.setBalanceIgnoreFlag(result.getString(16));
							creditInfo.setRelatedChargeId(result.getDouble(17));
							creditInfo.setRoamingTaxAmount(result.getDouble(18));
							creditInfo.setApprovalStatus(result.getString(19)); //move to last for approval_status
							creditInfo.setBilled(billIndicatior); //bill indicator
							hasMore = (result.getInt(20) == AccountManager.NUMERIC_TRUE);
							creditInfoList.add(creditInfo);
						}
						
					} else {
						LOGGER.debug(" NO FOUND DATA for BAN:"+iban);
					}
				}catch(Exception e) {
					LOGGER.error("error found for BAN:["+iban+"] while executing SQL:" + e.getMessage());
				} finally {
					if (result != null){
						result.close();
					}
				}
				return new CreditInfoHolder(creditInfoList, hasMore);
			}
			
		});
	}
	
	private CreditInfoHolder getCreditInfoHolder(String sql, final int ban, final Date fromDate, final Date toDate, final String billState, final String knowbilityOperatorId, final String reasonCode, final char level, final String subscriber, final int maximum,final boolean billIndicator){

		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<CreditInfoHolder>(){

			@Override
			public CreditInfoHolder doInCallableStatement(CallableStatement callstmt)
					throws SQLException, DataAccessException {
				Collection<CreditInfo> creditInfoList=new ArrayList<CreditInfo>();
				boolean hasMore=false;
				ResultSet result=null;

				callstmt.registerOutParameter(1, OracleTypes.NUMBER);
				callstmt.setInt(2, ban);
				callstmt.setDate(3, new java.sql.Date(fromDate.getTime()));
				callstmt.setDate(4, new java.sql.Date(toDate.getTime()));
				callstmt.setString(5, knowbilityOperatorId);
				callstmt.setString(6, subscriber);
				callstmt.setString(7, reasonCode);
				callstmt.setInt(8, ChargeType.CHARGE_LEVEL_ACCOUNT == level || AccountManager.Search_All == level ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
				callstmt.setInt(9, ChargeType.CHARGE_LEVEL_SUBSCRIBER == level || AccountManager.Search_All == level ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
				callstmt.setInt(10, maximum);
				callstmt.registerOutParameter(11, OracleTypes.CURSOR);
				callstmt.registerOutParameter(12, OracleTypes.VARCHAR);

				callstmt.execute();

				boolean success = callstmt.getInt(1) == AccountManager.NUMERIC_TRUE;

				try{
					if (success) {
						result = (ResultSet) callstmt.getObject(11);

						while (result.next()) {
							CreditInfo creditInfo = new CreditInfo();
							creditInfo.setBan(ban);
							creditInfo.setCreationDate(result.getDate(1));
							creditInfo.setEffectiveDate(result.getDate(2));
							creditInfo.setActivityCode(result.getString(3)== null ? "":result.getString(3) );
							creditInfo.setReasonCode(result.getString(4)== null ? "":result.getString(4) );
							creditInfo.setBalanceImpactFlag(result.getString(5));
							creditInfo.setSubscriberId(result.getString(6));
							creditInfo.setProductType(result.getString(7));
							creditInfo.setOperatorId(result.getInt(8));
							creditInfo.setAmount(result.getDouble(9));
							creditInfo.getTaxSummary().setGSTAmount(result.getDouble(10));
							creditInfo.getTaxSummary().setPSTAmount(result.getDouble(11));
							creditInfo.getTaxSummary().setHSTAmount(result.getDouble(12));
							creditInfo.setSOC(result.getString(13));
							creditInfo.setFeatureCode(result.getString(14));
							creditInfo.setId(result.getInt(15));
							creditInfo.setApprovalStatus(result.getString(16));
							creditInfo.setBalanceIgnoreFlag(result.getString(17));
							creditInfo.setRelatedChargeId(result.getDouble(18));
							creditInfo.setRoamingTaxAmount(result.getDouble(19));
							creditInfo.setBilled(billIndicator);
							hasMore = (result.getInt(20) == AccountManager.NUMERIC_TRUE);
							creditInfoList.add(creditInfo);
						}
					}
				} finally {
					if (result != null){
						result.close();
					}
				}
				return new CreditInfoHolder(creditInfoList, hasMore);
			}			
		});
	}

	@Override
	public List<CreditInfo> retrieveCreditByFollowUpId(final int followUpId) {

		String sql="{call HISTORY_UTILITY_PKG.GetUnBilledCreditByFollowUpId(?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<CreditInfo>>(){

			@Override
			public List<CreditInfo> doInCallableStatement(
					CallableStatement callstmt) throws SQLException,
					DataAccessException {
				ResultSet result=null;
				List<CreditInfo> creditInfoList = new ArrayList<CreditInfo>();

				try{
					callstmt.setInt(1, followUpId);
					callstmt.registerOutParameter(2, ORACLE_REF_CURSOR);
					callstmt.execute();
					result = (ResultSet) callstmt.getObject(2);

					while (result.next()) {
						CreditInfo creditInfo = new CreditInfo();
						creditInfo.setCreationDate(result.getDate(1));
						creditInfo.setEffectiveDate(result.getDate(2));
						creditInfo.setActivityCode(result.getString(3)== null ? "":result.getString(3) );
						creditInfo.setReasonCode(result.getString(4)== null ? "":result.getString(4) );
						creditInfo.setBalanceImpactFlag(result.getString(5));
						creditInfo.setSubscriberId(result.getString(6));
						creditInfo.setProductType(result.getString(7));
						creditInfo.setOperatorId(result.getInt(8));
						creditInfo.setAmount(result.getDouble(9));
						creditInfo.getTaxSummary().setGSTAmount(result.getDouble(10));
						creditInfo.getTaxSummary().setPSTAmount(result.getDouble(11));
						creditInfo.getTaxSummary().setHSTAmount(result.getDouble(12));
						creditInfo.setSOC(result.getString(13));
						creditInfo.setFeatureCode(result.getString(14));
						creditInfo.setId(result.getInt(15));
						creditInfo.setApprovalStatus(result.getString(16));
						creditInfo.setBalanceIgnoreFlag(result.getString(17));
						creditInfo.setRelatedChargeId(result.getDouble(18));
						creditInfo.setBan(result.getInt(19));
						creditInfo.setRoamingTaxAmount(result.getDouble(20));
						creditInfo.setBilled(false);
						creditInfoList.add(creditInfo);
					}
				}finally {
					if (result != null ){
						result.close();
					}
				}
				return creditInfoList;
			}
		});
	}

	@Override
	public List<CreditInfo> retrieveApprovedCreditByFollowUpId (final int banId, final int followUpId) {

		String sql="SELECT adj_creation_date, effective_date, actv_code, " +
				"                 actv_reason_code, balance_impact_code, subscriber_no, " +
				"                 product_type, operator_id, actv_amt, tax_gst_amt, " +
				"                 tax_pst_amt, tax_hst_amt, soc, feature_code, ent_seq_no, " +
				"                 approval_status, bl_ignore_ind, charge_seq_no, ban, " +
				"                 tax_roaming_amt " +
				"            FROM pending_adjustment " +
				"           WHERE ban = ? AND fu_id = ? AND approval_status = 'A' "; 

		return super.getKnowbilityJdbcTemplate().execute(sql, new PreparedStatementCallback<List<CreditInfo>>(){

			@Override
			public List<CreditInfo> doInPreparedStatement(PreparedStatement pstmt)
					throws SQLException, DataAccessException {
				ResultSet result=null;
				List<CreditInfo> creditInfoList = new ArrayList<CreditInfo>();

				try{
					pstmt.setInt(1, banId);
					pstmt.setInt(2, followUpId);
					result = pstmt.executeQuery();
					while (result.next()) {
						CreditInfo creditInfo = new CreditInfo();
						creditInfo.setBan( banId );
						creditInfo.setCreationDate(result.getDate("adj_creation_date"));
						creditInfo.setEffectiveDate(result.getDate("effective_date"));
						creditInfo.setActivityCode(result.getString("actv_code"));
						creditInfo.setReasonCode(result.getString("actv_reason_code"));
						creditInfo.setBalanceImpactFlag(result.getString("balance_impact_code"));
						creditInfo.setSubscriberId(result.getString("subscriber_no"));
						creditInfo.setProductType(result.getString("product_type"));
						creditInfo.setSOC(result.getString("soc"));
						creditInfo.setOperatorId(result.getInt("operator_id"));
						creditInfo.setAmount(result.getDouble("actv_amt"));
						creditInfo.getTaxSummary().setGSTAmount(result.getDouble("tax_gst_amt"));
						creditInfo.getTaxSummary().setPSTAmount(result.getDouble("tax_pst_amt"));
						creditInfo.getTaxSummary().setHSTAmount(result.getDouble("tax_hst_amt"));
						creditInfo.setId( result.getInt("ent_seq_no") );
						creditInfo.setApprovalStatus(result.getString("approval_status"));
						creditInfo.setBalanceIgnoreFlag(result.getString("bl_ignore_ind"));
						creditInfo.setRelatedChargeId(result.getDouble("charge_seq_no"));
						creditInfo.setRoamingTaxAmount(result.getDouble("tax_roaming_amt"));
						creditInfo.setBilled(false);
						creditInfoList.add(creditInfo);
					}
				}finally {
					if (result != null ){
						result.close();
					}
				}
				return creditInfoList;			
			}
		});
	}


	@Override
	public List<ChargeInfo> retrieveCharges(final int ban, final String[] chargeCodes,
			final String billState, final char level, final String subscriberId, final Date from,
			final Date to, final int maximum) {

		return super.getKnowbilityJdbcTemplate().execute(new ConnectionCallback<List<ChargeInfo>>(){

			@Override
			public List<ChargeInfo> doInConnection(Connection connection) throws
			SQLException, DataAccessException {
				List<ChargeInfo> returnList = new ArrayList<ChargeInfo>();
				PreparedStatement pstmt = null;
				ResultSet result = null;

				try {
					//parameters conversion - begin
					// primitive types to corresponding Object type
					Integer banNumber = new Integer( ban );

					// java.util.Date to java.sql.Date
					java.sql.Date fromDate = new java.sql.Date( from.getTime() );
					java.sql.Date toDate = new java.sql.Date( to.getTime() );

					//Because charge.feature_code's data type is char(6) when using preparedStatement.setString to bind the parameter
					//feature code has to be padded to exact 6 char long, otherwise won't find matches
					String [] featureCodes = new String[ chargeCodes.length ];
					for( int i=0; i<chargeCodes.length; i++ ) {
						featureCodes[i] = Info.padFeature( chargeCodes[i]);
					}
					//parameters conversion - end

					//start building the SQL statement: common select list for both charge and pending_charge tables
					String selectList = new StringBuffer()
					.append( "SELECT ent_seq_no, chg_creation_date, effective_date, actv_code, " )
					.append(       " actv_reason_code, feature_code, ftr_revenue_code," )
					.append(       " balance_impact_code, subscriber_no, product_type," )
					.append(       " c.operator_id, actv_amt, c.tax_gst_amt, c.tax_pst_amt," )
					.append(       " c.tax_hst_amt, bl_ignore_ind, soc, " )
					.append(       " actv_bill_seq_no, priod_cvrg_st_date, priod_cvrg_nd_date, " )
					.append(       " tax_roaming_amt, tax_gst_exmp_src," )
					.append(       " tax_pst_exmp_src, tax_hst_exmp_src, tax_roam_exmp_src ")
					.toString();

					ChargePSBuilder chargeQueryBuilder = null;

					   //Billed charges
					if ( billState.equals(Account.BILL_STATE_BILLED) ){ 
						chargeQueryBuilder = buildBilledChargesQuery(selectList, banNumber, fromDate, toDate, featureCodes, level, subscriberId, billState);
					}  //Unbilled charges
					else if ( billState.equals(Account.BILL_STATE_UNBILLED) ){ 
						chargeQueryBuilder = buildUnbilledChargesQuery(selectList, banNumber, fromDate, toDate, featureCodes, level, subscriberId, billState);
					}  //combined both billed and unbilled charges
					else if (billState.equals(Account.BILL_STATE_ALL)) { 
						chargeQueryBuilder =buildAllChargesQuery(selectList, banNumber,fromDate, toDate, featureCodes, level,subscriberId, billState);
					}

					pstmt = chargeQueryBuilder.getStatement(connection);

					result = pstmt.executeQuery();

					while ( result.next() && returnList.size() < maximum ) {
						ChargeInfo info = new ChargeInfo();
						info.setBan(ban);
						info.setId(result.getDouble("ent_seq_no"));
						info.setCreationDate(result.getTimestamp("chg_creation_date"));
						info.setEffectiveDate(result.getDate("effective_date"));
						info.setChargeCode(result.getString("actv_code"));
						info.setReasonCode(result.getString("actv_reason_code"));
						info.setFeatureCode(result.getString("feature_code"));
						info.setFeatureRevenueCode(result.getString("ftr_revenue_code"));
						info.setBalanceImpactFlag(result.getString("balance_impact_code"));
						info.setSubscriberId(result.getString("subscriber_no"));
						info.setProductType(result.getString("product_type"));
						info.setOperatorId(result.getInt("operator_id"));
						info.setAmount(result.getDouble("actv_amt"));
						info.setGSTAmount(result.getDouble("tax_gst_amt"));
						info.setPSTAmount(result.getDouble("tax_pst_amt"));
						info.setHSTAmount(result.getDouble("tax_hst_amt"));
						info.setBalanceIgnoreFlag(result.getString("bl_ignore_ind"));
						info.setServiceCode(result.getString("soc"));
						info.setBillSequenceNo(result.getInt("actv_bill_seq_no"));
						info.setPeriodCoverageStartDate(result.getDate("priod_cvrg_st_date"));
						info.setPeriodCoverageEndDate(result.getDate("priod_cvrg_nd_date"));
						info.setRoamingTaxAmount(result.getDouble("tax_roaming_amt"));
						info.setGSTExempt("T".equals(result.getString("tax_gst_exmp_src")));
						info.setPSTExempt("T".equals(result.getString("tax_pst_exmp_src")));
						info.setHSTExempt("T".equals(result.getString("tax_hst_exmp_src")));
						info.setRoamingTaxExempt("T".equals(result.getString("tax_roam_exmp_src")));
						info.setApprovalStatus(result.getString("approval_status"));
						info.setBilled( result.getString("is_billed")!=null );

						returnList.add(info);
					}
				} finally {
					if (result!=null ){
						result.close();					
					}
					if (pstmt != null ) {
						pstmt.close();
					}
				}
				return returnList;
			}


		});
	}

	
	@Override
	public List<ChargeInfo> retrieveRelatedChargesForCredit(final int pBan,
			final double pChargeSeqNo) {

		String call = "{? = call HISTORY_UTILITY_PKG.GetRelatedChargesForCredit(?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<List<ChargeInfo>>() {
			@Override
			public List<ChargeInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,
					DataAccessException {

				ArrayList<ChargeInfo> list = new ArrayList<ChargeInfo>();
				ResultSet result = null;

				// set/register input/output parameters
				callable.registerOutParameter(1, OracleTypes.NUMBER);
				callable.setInt(2, pBan);
				callable.setDouble(3, pChargeSeqNo);
				callable.registerOutParameter(4, OracleTypes.CURSOR);
				callable.registerOutParameter(5, OracleTypes.VARCHAR);

				callable.execute();

				boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

				try {
					if (success) {
						result = (ResultSet) callable.getObject(4);

						while (result.next()) {
							ChargeInfo info = new ChargeInfo();
							info.setBan(pBan);
							info.setId(result.getDouble("ent_seq_no"));
							info.setCreationDate(result.getDate("chg_creation_date"));
							info.setEffectiveDate(result.getDate("effective_date"));
							info.setChargeCode(result.getString("actv_code"));
							info.setReasonCode(result.getString("actv_reason_code"));
							info.setFeatureCode(result.getString("feature_code"));
							info.setFeatureRevenueCode(result.getString("ftr_revenue_code"));
							info.setBalanceImpactFlag(result.getString("balance_impact_code"));
							info.setSubscriberId(result.getString("subscriber_no"));
							info.setProductType(result.getString("product_type"));
							info.setOperatorId(result.getInt("operator_id"));
							info.setAmount(result.getDouble("actv_amt"));
							info.setGSTAmount(result.getDouble("tax_gst_amt"));
							info.setPSTAmount(result.getDouble("tax_pst_amt"));
							info.setHSTAmount(result.getDouble("tax_hst_amt"));
							info.setApprovalStatus("Y");
							info.setBalanceIgnoreFlag(result.getString("bl_ignore_ind"));
							info.setServiceCode(result.getString("soc"));
							info.setBilled(result.getString("is_billed")!=null);
							info.setBillSequenceNo(result.getInt("actv_bill_seq_no"));
							info.setPeriodCoverageStartDate(result.getDate("priod_cvrg_st_date"));
							info.setPeriodCoverageEndDate(result.getDate("priod_cvrg_nd_date"));
							info.setRoamingTaxAmount(result.getDouble("tax_roaming_amt"));
							info.setGSTExempt(result.getString("tax_gst_exmp_src")!=null?(result.getString("tax_gst_exmp_src").equals("T")):false);
							info.setPSTExempt(result.getString("tax_pst_exmp_src")!=null?(result.getString("tax_pst_exmp_src").equals("T")):false);
							info.setHSTExempt(result.getString("tax_hst_exmp_src")!=null?(result.getString("tax_hst_exmp_src").equals("T")):false);
							info.setRoamingTaxExempt(result.getString("tax_roam_exmp_src")!=null?(result.getString("tax_roam_exmp_src").equals("T")):false);

							list.add(info);
						}
					}
					else {
						LOGGER.debug("Stored procedure failed: " + callable.getString(5));
					}				
				} finally {
					if (result != null){						
						result.close();
					}
				}
				return list;
			}
		});
	}

	@Override
	public List<CreditInfo> retrieveRelatedCreditsForCharge(final int pBan,
			final double pChargeSeqNo) {

		String call = "{? = call HISTORY_UTILITY_PKG.GetRelatedCreditsForCharge(?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<List<CreditInfo>>() {
			@Override
			public List<CreditInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,
					DataAccessException {

				ArrayList<CreditInfo> list = new ArrayList<CreditInfo>();
				ResultSet result = null;

				// set/register input/output parameters
				callable.registerOutParameter(1, OracleTypes.NUMBER);
				callable.setInt(2, pBan);
				callable.setDouble(3, pChargeSeqNo);
				callable.registerOutParameter(4, OracleTypes.CURSOR);
				callable.registerOutParameter(5, OracleTypes.VARCHAR);

				callable.execute();

				boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

				try {
					if (success) {
						result = (ResultSet) callable.getObject(4);

						while (result.next()) {
							CreditInfo info = new CreditInfo();
							info.setBan(pBan);
							info.setId(result.getInt("ent_seq_no"));
							info.setCreationDate(result.getDate("adj_creation_date"));
							info.setEffectiveDate(result.getDate("effective_date"));
							info.setActivityCode(result.getString("actv_code"));
							info.setReasonCode(result.getString("actv_reason_code"));
							info.setBalanceImpactFlag(result.getString("balance_impact_code"));
							info.setSubscriberId(result.getString("subscriber_no"));
							info.setProductType(result.getString("product_type"));
							info.setSOC(result.getString("soc"));
							info.setOperatorId(result.getInt("operator_id"));
							info.setAmount(result.getDouble("actv_amt"));
							info.getTaxSummary().setGSTAmount(result.getDouble("tax_gst_amt"));
							info.getTaxSummary().setPSTAmount(result.getDouble("tax_pst_amt"));
							info.getTaxSummary().setHSTAmount(result.getDouble("tax_hst_amt"));
							info.setApprovalStatus(result.getString("approval_status"));
							info.setBalanceIgnoreFlag(result.getString("bl_ignore_ind"));
							info.setRelatedChargeId(result.getDouble("charge_seq_no"));
							info.setBilled(false);

							list.add(info);
						}
					}
					else {
						LOGGER.debug("Stored procedure failed: " + callable.getString(5));
					}
				} finally {
					if (result != null){
						result.close();
					}			    	  
				}
				return list;
			}
		});
	}

	@Override
	public SearchResultsInfo retrievePendingChargeHistory(final int pBan,
			final Date pFromDate, final Date pToDate, final char level, final String pSubscriber,
			final int maximum) {
		
		if (pFromDate == null || pToDate == null) {
			throw new SystemException(SystemCodes.CMB_AIH_DAO, "Inputs fromDate and toDate should not be null", "");
		}

		String call = "{? = call HISTORY_UTILITY_PKG.GetPendingChargesEnhanced(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<SearchResultsInfo>() {
			@Override
			public SearchResultsInfo doInCallableStatement(
					CallableStatement callable) throws SQLException,
					DataAccessException {

				ResultSet result = null;
				int hasMore = AccountManager.NUMERIC_FALSE;
				SearchResultsInfo searchResults = new SearchResultsInfo();
				searchResults.setItems(new ChargeInfo[0]);


				// set/register input/output parameters
				callable.registerOutParameter(1, OracleTypes.NUMBER);
				callable.setInt(2, pBan);
				callable.setDate(3, new java.sql.Date(pFromDate.getTime()));
				callable.setDate(4, new java.sql.Date(pToDate.getTime()));
				callable.setString(5, pSubscriber);
				callable.setInt(6, ChargeType.CHARGE_LEVEL_ACCOUNT == level || AccountManager.Search_All == level ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
				callable.setInt(7, ChargeType.CHARGE_LEVEL_SUBSCRIBER == level || AccountManager.Search_All == level ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
				callable.setInt(8, maximum);
				callable.registerOutParameter(9, OracleTypes.CURSOR);
				callable.registerOutParameter(10, OracleTypes.VARCHAR);

				callable.execute();

				boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

				try {
					if (success) {
						result = (ResultSet) callable.getObject(9);
						ArrayList<ChargeInfo> list = new ArrayList<ChargeInfo>();

						while (result.next()) {
							ChargeInfo info = new ChargeInfo();
							info.setBan(pBan);
							info.setId(result.getDouble("ent_seq_no"));
							info.setCreationDate(result.getDate("chg_creation_date"));
							info.setEffectiveDate(result.getDate("effective_date"));
							info.setChargeCode(result.getString("actv_code"));
							info.setReasonCode(result.getString("actv_reason_code"));
							info.setFeatureCode(result.getString("feature_code"));
							info.setFeatureRevenueCode(result.getString("ftr_revenue_code"));
							info.setBalanceImpactFlag(result.getString("balance_impact_code"));
							info.setSubscriberId(result.getString("subscriber_no"));
							info.setProductType(result.getString("product_type"));
							info.setOperatorId(result.getInt("operator_id"));
							info.setAmount(result.getDouble("actv_amt"));
							info.setGSTAmount(result.getDouble("tax_gst_amt"));
							info.setPSTAmount(result.getDouble("tax_pst_amt"));
							info.setHSTAmount(result.getDouble("tax_hst_amt"));
							info.setApprovalStatus(result.getString("approval_status"));
							info.setBalanceIgnoreFlag(result.getString("bl_ignore_ind"));
							info.setServiceCode(result.getString("soc"));
							info.setBilled(false);
							info.setBillSequenceNo(0);
							info.setRoamingTaxAmount(result.getDouble("tax_roaming_amt"));
							info.setGSTExempt(result.getString("tax_gst_exmp_src")!=null?(result.getString("tax_gst_exmp_src").equals("T")):false);
							info.setPSTExempt(result.getString("tax_pst_exmp_src")!=null?(result.getString("tax_pst_exmp_src").equals("T")):false);
							info.setHSTExempt(result.getString("tax_hst_exmp_src")!=null?(result.getString("tax_hst_exmp_src").equals("T")):false);
							info.setRoamingTaxExempt(result.getString("tax_roam_exmp_src")!=null?(result.getString("tax_roam_exmp_src").equals("T")):false);
							hasMore = result.getInt("has_more");
							list.add(info);
						}

						searchResults.setItems( (ChargeInfo[])list.toArray(new ChargeInfo[list.size()]));
						searchResults.setHasMore(hasMore == AccountManager.NUMERIC_TRUE);
					} else {
						String errorMessage = callable.getString(11);
						LOGGER.debug("Stored procedure failed: " + errorMessage);

						searchResults.setItems(new ChargeInfo[0]);
						searchResults.setHasMore(false);
					}
				} finally {
					if (result != null ){
						result.close();
					}
				}

				return searchResults;

			}
		});
	}

	@Override
	public List<Double> retrieveAdjustedAmounts(final int ban, final String adjustmentReasonCode, final String subscriberId, final Date searchFromDate,
			final Date searchToDate) throws ApplicationException {

		final String padddAdjustmentReasonCode = Info.padTo(adjustmentReasonCode,' ',6);
		
		String sql = "SELECT ACTV_AMT  FROM ADJUSTMENT  WHERE BAN = ? AND SUBSCRIBER_NO = ? AND ACTV_REASON_CODE = ? AND ADJ_CREATION_DATE BETWEEN ? AND ?"+
				"  UNION ALL SELECT ACTV_AMT  FROM PENDING_ADJUSTMENT  WHERE BAN = ? AND SUBSCRIBER_NO = ? AND ACTV_REASON_CODE = ? AND ADJ_CREATION_DATE BETWEEN ? AND ? ";

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
					pstmt.setString(3, padddAdjustmentReasonCode);
					pstmt.setTimestamp(4, fromDate);
					pstmt.setTimestamp(5, toDate);
					pstmt.setInt(6, ban);
					pstmt.setString(7, subscriberId);
					pstmt.setString(8, padddAdjustmentReasonCode);
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
	private ChargePSBuilder buildBilledChargesQuery(String selectList,int banNumber,Date fromDate ,Date toDate,String [] featureCodes,char level,String subscriberId,String billState)
	{
		ChargePSBuilder b = new ChargePSBuilder();

			b.append(selectList);
			b.append(" ,'Y' AS approval_status "); //charge table does not have this field.
			b.append(       " , 1 AS is_billed "); //indicate this record is billed
			b.append( " FROM   charge c, bill b ");
			b.append( "WHERE  c.ban =", banNumber );
			b.append( " AND  c.ban = b.ban ");
			b.append( " AND  c.actv_bill_seq_no = b.bill_seq_no ");
			b.append( " AND  b.cycle_close_date").between( fromDate, toDate ); 
			if (featureCodes.length > 0) {
				b.append(" AND  feature_code").in(featureCodes);
			}
			b.append( " AND  chg_creation_date").between( fromDate, toDate );
			b.appendLevelCriteria( level, subscriberId );
			b.append( " ORDER BY ent_seq_no DESC ");

		return b;
	}

	private ChargePSBuilder buildUnbilledChargesQuery(String selectList,int banNumber,Date fromDate ,Date toDate,String [] featureCodes,char level,String subscriberId,String billState)
	{
		ChargePSBuilder b = new ChargePSBuilder();

		// unbilled charges query consist of three SQLs

		// part 1 - pending charges
		b.append( selectList );
		b.append(       " , approval_status ");
		b.append(       " , NULL AS is_billed "); //indicate this record is unbilled
		b.append( " FROM  pending_charge c " ); 
		b.append( " WHERE ban = ", banNumber); 
		b.append(  " AND  approval_status = 'P' ");
		if (featureCodes.length > 0) {
			b.append(" AND  feature_code").in(featureCodes);
		}
		b.append(  " AND  chg_creation_date").between( fromDate, toDate );
		b.appendLevelCriteria( level, subscriberId );

		b.append( " UNION ALL " );

		// part 2 - charge that has No bill sequence number yet
		b.append( selectList );
		b.append(       " ,'Y' AS approval_status ");//charge table does not have this field.
		b.append(       " , NULL AS is_billed "); //indicate this record is unbilled
		b.append( " FROM  charge c " ); 
		b.append( " WHERE  ban = ", banNumber );
		b.append( "	AND	 actv_bill_seq_no is null " );
		if(featureCodes.length >0) {
			b.append( " AND  feature_code" ).in( featureCodes);		
		}
		b.append( " AND  chg_creation_date" ).between( fromDate, toDate );
		b.appendLevelCriteria( level, subscriberId );

		b.append( " UNION ALL " );

		// part 3 - charge that has bill sequence number, but bill_conf_status is tentative
		b.append( selectList );
		b.append(       " ,'Y' AS approval_status "); //charge table does not have this field.
		b.append(       " , NULL AS is_billed " ); //indicate this record is unbilled
		b.append( " FROM   charge c, bill b " );
		b.append( " WHERE  c.ban = ", banNumber);
		b.append( "	AND	 c.ban = b.ban " );
		b.append( " AND	 c.actv_bill_seq_no = b.bill_seq_no ");
		b.append( " AND	 b.bill_conf_status = 'T' ");
		if(featureCodes.length >0) {
			b.append( " AND  feature_code" ).in( featureCodes );	
		}
		b.append( " AND  chg_creation_date").between( fromDate, toDate );
		b.appendLevelCriteria( level, subscriberId );
		b.append( " ORDER BY ent_seq_no DESC ");

		return b;

	}

	private ChargePSBuilder buildAllChargesQuery(String selectList,int banNumber,Date fromDate ,Date toDate,String [] featureCodes,char level,String subscriberId,String billState)
	{
		ChargePSBuilder b = new ChargePSBuilder();
		
		// part 1 Billed charges
		b.append( selectList );
		b.append(       " ,'Y' AS approval_status "); //charge table does not have this field.
		b.append(       " , 1 AS is_billed "); //indicate this record is billed
		b.append( " FROM   charge c, bill b ");
		b.append( "WHERE  c.ban =", banNumber );
		b.append( " AND  c.ban = b.ban ");
		b.append( " AND  c.actv_bill_seq_no = b.bill_seq_no ");
		b.append( " AND  b.cycle_close_date").between( fromDate, toDate );
		if(featureCodes.length >0) {
			b.append( " AND  feature_code" ).in( featureCodes );	
		}
		b.append( " AND  chg_creation_date").between( fromDate, toDate );
		b.appendLevelCriteria( level, subscriberId );
		b.append( " UNION ALL " );
		
		// unbilled charges query consist of below two SQLs

		// part 2 - pending charges
		b.append( selectList );
		b.append(       " , approval_status ");
		b.append(       " , NULL AS is_billed "); //indicate this record is unbilled
		b.append( " FROM  pending_charge c " ); 
		b.append( " WHERE ban = ", banNumber); 
		b.append(  " AND  approval_status = 'P' ");
		if (featureCodes.length > 0) {
			b.append(" AND  feature_code").in(featureCodes);
		}
		b.append(  " AND  chg_creation_date").between( fromDate, toDate );
		b.appendLevelCriteria( level, subscriberId );

		b.append( " UNION ALL " );

		// part 3 - charge that has No bill sequence number yet
		b.append( selectList );
		b.append(       " ,'Y' AS approval_status ");//charge table does not have this field.
		b.append(       " , NULL AS is_billed "); //indicate this record is unbilled
		b.append( " FROM  charge c " ); 
		b.append( " WHERE  ban = ", banNumber );
		b.append( "	AND	 actv_bill_seq_no is null " );
		if(featureCodes.length >0) {
			b.append( " AND  feature_code" ).in( featureCodes);		
		}
		b.append( " AND  chg_creation_date" ).between( fromDate, toDate );
		b.appendLevelCriteria( level, subscriberId );
		b.append( " ORDER BY ent_seq_no DESC ");
		return b;

	}

	final class ChargePSBuilder extends PreparedStatementBuilder {
		void appendLevelCriteria( char level, String subscriberId) {
			if (level == ChargeType.CHARGE_LEVEL_ACCOUNT)
				append( " AND subscriber_no is NULL ");
			else if (level == ChargeType.CHARGE_LEVEL_SUBSCRIBER)
				append( " AND subscriber_no =", subscriberId );
		}
	}
	
	
	@Override
	public CreditInfo retrieveCreditById(final int banId, final int entSeqNo) {
		
		String sql="SELECT adj_creation_date, effective_date, actv_code, " +
				"                 actv_reason_code, balance_impact_code, subscriber_no, " +
				"                 product_type, operator_id, actv_amt, tax_gst_amt, " +
				"                 tax_pst_amt, tax_hst_amt, soc, feature_code,  " +
				"                 'Y' approval_status, bl_ignore_ind, charge_seq_no,  " +
				"                 tax_roaming_amt " +
				"            FROM adjustment " +
				"           WHERE ban = ? AND ent_seq_no=? AND actv_seq_no=1 " ; 

		return super.getKnowbilityJdbcTemplate().execute(sql, new PreparedStatementCallback<CreditInfo>(){

			@Override
			public CreditInfo doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				
				ResultSet result=null;
				try{
					pstmt.setInt(1, banId);
					pstmt.setInt(2, entSeqNo);
					result = pstmt.executeQuery();
					while (result.next()) {
						CreditInfo info = new CreditInfo();
						info.setBan(banId );
						info.setId( entSeqNo );
						info.setCreationDate(result.getDate("adj_creation_date"));
						info.setEffectiveDate(result.getDate("effective_date"));
						info.setActivityCode(result.getString("actv_code"));
						info.setReasonCode(result.getString("actv_reason_code"));
						info.setBalanceImpactFlag(result.getString("balance_impact_code"));
						info.setSubscriberId(result.getString("subscriber_no"));
						info.setProductType(result.getString("product_type"));
						info.setSOC(result.getString("soc"));
						info.setOperatorId(result.getInt("operator_id"));
						info.setAmount(result.getDouble("actv_amt"));
						info.getTaxSummary().setGSTAmount(result.getDouble("tax_gst_amt"));
						info.getTaxSummary().setPSTAmount(result.getDouble("tax_pst_amt"));
						info.getTaxSummary().setHSTAmount(result.getDouble("tax_hst_amt"));
						info.setApprovalStatus(result.getString("approval_status"));
						info.setBalanceIgnoreFlag(result.getString("bl_ignore_ind"));
						info.setRelatedChargeId(result.getDouble("charge_seq_no"));
						info.setRoamingTaxAmount(result.getDouble("tax_roaming_amt"));
						return info;
					}
				}finally {
					if (result != null ){
						result.close();
					}
				}
				return null;
			}
		});
	}

}
