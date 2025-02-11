/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.api.account.AccountManager;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.cmb.common.util.ArrayUtil;
import com.telus.cmb.common.util.StringUtil;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.BillCycleInfo;
import com.telus.eas.utility.info.DealerInfo;
import com.telus.eas.utility.info.KnowbilityOperatorInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.PhoneNumberResourceInfo;
import com.telus.eas.utility.info.ReferenceInfo;
import com.telus.eas.utility.info.SalesRepInfo;
import com.telus.eas.utility.info.SapccMappedServiceInfo;
import com.telus.eas.utility.info.WorkPositionInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceDataKnowbilityDao extends JdbcDaoSupport {

	private static final int ORACLE_REF_CURSOR  = -10;
	private static final int FETCH_SIZE = 400;
	
	private <T> T getReferenceData(final String refType, final String param, final ResultSetExtractor<T> rse) {
		
		String call = "{? = call REFERENCE_APP_PKG.GetReferenceData(?, ?, ?, ?)}";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<T>() {
			
			@Override
			public T doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {

				cs.setFetchSize(FETCH_SIZE);
				cs.registerOutParameter(1, OracleTypes.NUMBER);
				cs.setString(2, refType);
				cs.setString(3, param);
				cs.registerOutParameter(4, OracleTypes.CURSOR);
				cs.registerOutParameter(5, OracleTypes.VARCHAR);
				
				cs.execute();

				boolean success = cs.getInt(1) == AccountManager.NUMERIC_TRUE;

				if (success) {
				
					ResultSet rs = (ResultSet) cs.getObject(4);
					
					try {
						return rse.extractData(rs);
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				}
				else {
					throw new SQLException("Strored procedure failed: " + cs.getString(5));
				}
			}
		});
		
	}
	
	public BillCycleInfo retrieveBillCycle(String billCycleCode) {
		
		String sql = " select cycle_code, " +
        " cycle_desc, " +
        " cycle_close_day, " +
        " cycle_population_cd, " +
        " no_of_bans, " +
        " cycle_bill_day, " +
        " cycle_due_day, " +
        " no_of_subs, " +
        " allocation_ind, " +
        " cycle_weight " +
        " from cycle " +
        " where cycle_code = ? ";

		return getJdbcTemplate().query(sql, new Object[] {billCycleCode}, new ResultSetExtractor<BillCycleInfo>() {
			
			@Override
			public BillCycleInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				BillCycleInfo info = new BillCycleInfo();

				while (rs.next()) {
					info = new BillCycleInfo();

					info.setCode(rs.getString(1));
					info.setDescription(rs.getString(2));
					info.setCloseDay(rs.getInt(3));
					info.setPopulationCode(rs.getString(4));
					info.setNumberOfAllocatedAccounts(rs.getLong(5));
					info.setBillDay(rs.getInt(6));
					info.setDueDay(rs.getInt(7));
					info.setNumberOfAllocatedSubscribers(rs.getLong(8));
					info.setAllocationIndicator(rs.getString(9));
					info.setWeight(rs.getLong(10));
				}
				return info;
			}
		});
	}
	@Deprecated
	public int retrieveBillCycleLeastUsed() throws DataAccessException {
		
		String sql = " select cycle_code " +
		" from " +
		" (select  to_date(lpad(to_char(decode(cycle_bill_day,29,28,30,28,31,28,cycle_bill_day)),2,'0')||to_char(sysdate,'mmyy'),'ddmmyy') billdate" +
		" ,cycle_code " +
		" ,no_of_bans " +
		" ,allocation_ind " +
		" from  cycle " +
		" where  allocation_ind='Y'  " +
		"  union " +
		" select  to_date(lpad(to_char(decode(cycle_bill_day,29,28,30,28,31,28,cycle_bill_day)),2,'0')||to_char(add_months(sysdate,1),'mmyy'),'ddmmyy') billdate " +
		" ,cycle_code " +
		" ,no_of_bans " +
		" ,allocation_ind " +
		" from  cycle " +
		" where  allocation_ind='Y' ) " +
		" where  billdate >= trunc(sysdate) + 2 " +
		" and     billdate < trunc(sysdate) + 7 " +
		" and no_of_bans= " +
		" (select min(no_of_bans) " +
		" from " +
		" (select to_date(lpad(to_char(decode(cycle_bill_day,29,28,30,28,31,28,cycle_bill_day)),2,'0')||to_char(sysdate,'mmyy'),'ddmmyy') billdate " +
		" ,no_of_bans " +
		" ,allocation_ind " +
		" from  cycle " +
		" where  allocation_ind='Y'  " +
		" union " +
		" select  to_date(lpad(to_char(decode(cycle_bill_day,29,28,30,28,31,28,cycle_bill_day)),2,'0')||to_char(add_months(sysdate,1),'mmyy'),'ddmmyy') billdate " +
		" ,no_of_bans " +
		" ,allocation_ind " +
		" from  cycle " +
		" where  allocation_ind='Y' ) " +
		" where billdate  >= trunc(sysdate) + 2 " +
		" and     billdate < trunc(sysdate) + 7 " +
		" ) " +
		" and rownum < 2 ";
		
		int cycle = getJdbcTemplate().query(sql, new ResultSetExtractor<Integer>() {
			
			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int billCycle = 0;
				while (rs.next()){
					billCycle = rs.getInt(1);
				}
				return billCycle;
			}
		});
		
		if (cycle == 0) {
			
			sql = " select cycle_code " +
			" from cycle " +
			" where no_of_bans = " +
			"(select min(no_of_bans) from cycle where allocation_ind <> 'N') " +
			"and  allocation_ind <> 'N' " +
			"and rownum < 2";
			
			cycle = getJdbcTemplate().query(sql, new ResultSetExtractor<Integer>() {
				
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					int billCycle = 0;
					while (rs.next()){
						billCycle = rs.getInt(1);
					}
					return billCycle;
				}
			});
		}
		return cycle;
	}
	
	public Collection<BillCycleInfo> retrieveBillCycles() {
		return getReferenceData("BillCycles", "", new ResultSetExtractor<Collection<BillCycleInfo>>() {
			
			@Override
			public Collection<BillCycleInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				List<BillCycleInfo> result = new ArrayList<BillCycleInfo>();
				
				while (rs.next()) {
					BillCycleInfo billCycleInfo = new BillCycleInfo();

					billCycleInfo.setCode(rs.getString(1));
					billCycleInfo.setDescription(rs.getString(2));
					billCycleInfo.setCloseDay(rs.getInt(3));
					billCycleInfo.setPopulationCode(rs.getString(4));
					billCycleInfo.setNumberOfAllocatedAccounts(rs.getLong(5));
					billCycleInfo.setBillDay(rs.getInt(6));
					billCycleInfo.setDueDay(rs.getInt(7));
					billCycleInfo.setNumberOfAllocatedSubscribers(rs.getLong(8));
					billCycleInfo.setAllocationIndicator(rs.getString(9));
					billCycleInfo.setWeight(rs.getLong(10));
					
					result.add(billCycleInfo);
				}
				return result;
			}
		});
	}
	
	public Collection<BillCycleInfo> retrieveBillCycles(String populationCode) {
		
	      String sql = " select cycle_code, cycle_desc, cycle_close_day, cycle_population_cd, " +
          " no_of_bans,  cycle_bill_day, cycle_due_day, no_of_subs, allocation_ind, cycle_weight " +
          " from cycle where allocation_ind <> 'N' and cycle_population_cd = ? ";
		
		return getJdbcTemplate().query(sql, new Object[] {populationCode}, new RowMapper<BillCycleInfo>() {
			
			@Override
			public BillCycleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				BillCycleInfo billCycleInfo = new BillCycleInfo();

				billCycleInfo.setCode(rs.getString(1));
		        billCycleInfo.setDescription(rs.getString(2));
		        billCycleInfo.setCloseDay(rs.getInt(3));
		        billCycleInfo.setPopulationCode(rs.getString(4));
		        billCycleInfo.setNumberOfAllocatedAccounts(rs.getLong(5));
		        billCycleInfo.setBillDay(rs.getInt(6));
		        billCycleInfo.setDueDay(rs.getInt(7));
		        billCycleInfo.setNumberOfAllocatedSubscribers(rs.getLong(8));
		        billCycleInfo.setAllocationIndicator(rs.getString(9));
		        billCycleInfo.setWeight(rs.getLong(10));
				
				return billCycleInfo;
			}
		});
	}
	
	public DealerInfo retrieveDealerbyDealerCode(String dealerCode) throws DataAccessException {
		return retrieveDealerbyDealerCode(dealerCode, false);
	}
	
	public DealerInfo retrieveDealerbyDealerCode(String dealerCode, boolean expired) throws DataAccessException {
		
		if (dealerCode == null || dealerCode.trim().isEmpty()) {
			return null;
		}
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select dealer, " +
        " dlr_name , nl_cd, start_date, end_date " +
        " from dealer_profile dp " +
        " ,logical_date ld " +
        " where dp.dealer = ? " +
        " and trunc(dp.start_date) <= trunc(ld.logical_date) ");
		
		if (!expired) {
			sql.append(" and (trunc(dp.end_date) is null or trunc(dp.end_date) >  trunc(ld.logical_date)) ");
		}
		
        sql.append(" and ld.logical_date_type='O'");		
        
		return getJdbcTemplate().query(sql.toString(), new Object[] {dealerCode}, new ResultSetExtractor<DealerInfo>() {
			
			@Override
			public DealerInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				DealerInfo info = null;
				
				if (rs.next()) {
					info = new DealerInfo();
					info.setCode(rs.getString(1));
					info.setName(rs.getString(2));
					info.setNumberLocationCD(rs.getString(3));
					info.setEffectiveDate(rs.getDate(4));
					info.setExpiryDate(rs.getDate(5));
				}
				return info;
			}
		});
	}
	
	public SalesRepInfo retrieveDealerSalesRepByCode(String dealerCode, String salesRepCode, boolean expired) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select dealer_code, sales_code, sales_name, dsp.effective_date, dsp.expiration_date " +
		" from dealer_sales_rep dsp, logical_date ld " +
		" where dealer_code = ? and   sales_code = ? and trunc(dsp.effective_date)< = trunc(ld.logical_date) ");
		if (!expired) {
			sql.append(" and (trunc(dsp.expiration_date) is null or trunc(dsp.expiration_date) >  trunc(ld.logical_date)) ");
		}
		sql.append(" and ld.logical_date_type='O'");
		
		return getJdbcTemplate().query(sql.toString(), new Object[] {dealerCode, salesRepCode}, new ResultSetExtractor<SalesRepInfo>() {
			
			@Override
			public SalesRepInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				SalesRepInfo info = null;
				
				if (rs.next()) {
					info = new SalesRepInfo();
					info.setDealerCode(rs.getString(1));
					info.setCode(rs.getString(2));
					info.setName(rs.getString(3));
					info.setEffectiveDate(rs.getDate(4));
					info.setExpiryDate(rs.getDate(5));
				}
				return info;
			}
		});
	}
	
	public FleetInfo retrieveFleetByFleetIdentity(final FleetIdentityInfo fleetIdentity) {
		
		String call = "{ call fleet_utility_pkg.GetFleetsByFleetIdentity (?,?,?) }";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<FleetInfo>() {
			
			@Override
			public FleetInfo doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				
				cs.setInt(1, fleetIdentity.getUrbanId());
				cs.setInt(2, fleetIdentity.getFleetId());
				cs.registerOutParameter(3, ORACLE_REF_CURSOR);
				cs.execute();
				
				ResultSet rs = (ResultSet) cs.getObject(3);
				
				FleetInfo info = null;
				
				try {
					
					if (rs.next()) {
				          
						int urbanId = rs.getInt(1);
						int fleetId = rs.getInt(2);

						info = new FleetInfo() ;
				          
						info.getIdentity0().setUrbanId(urbanId);
						info.getIdentity0().setFleetId(fleetId);
						info.setName(rs.getString(3));
						info.setType(rs.getString(4).charAt(0));
						info.setExpectedSubscribers(rs.getInt(5));
						info.setExpectedTalkGroups(rs.getInt(6));
						info.setBanId0(rs.getInt(7));
						info.setNetworkId(rs.getInt(8));
						info.setActiveSubscribersCount(rs.getInt(9));
						info.setDAPId(rs.getInt(10));
						info.setPTNBased(rs.getString(12).equals("N") ? false : true);
						info.setFleetClass(rs.getString(11) == null ? "E" : rs.getString(11));
						info.setOwnerName(StringUtil.emptyFromNull(rs.getString(13)));

						int accountsCounter = retrieveAssociatedAccountsCount(urbanId,fleetId);
						info.setAssociatedAccountsCount(accountsCounter);
					}
				} finally {
					rs.close();
				}
				return info;
			}
		});
	}

	public int retrieveAssociatedAccountsCount(final int urbanId, final int fleetId) {
		
		String call = "{ call fleet_utility_pkg.GetNumberAssociatedAccounts (?,?,?) }";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<Integer>() {
			
			@Override
			public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				
				cs.setInt(1, urbanId);
				cs.setInt(2, fleetId);
				cs.registerOutParameter(3, Types.NUMERIC);
				cs.execute();
				return cs.getInt(3);
			}
		});
	}
	
	public Collection<FleetInfo> retrieveFleetsByFleetType(final char fleetType) {
		
		String call = "{ call fleet_utility_pkg.GetFleetsByFleetType (?,?) }";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<Collection<FleetInfo>>() {
			
			@Override
			public Collection<FleetInfo> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				
				List<FleetInfo> result = new ArrayList<FleetInfo>();

				cs.setString(1, String.valueOf(fleetType));
				cs.registerOutParameter(2, ORACLE_REF_CURSOR);
				cs.execute();
				ResultSet rs = (ResultSet) cs.getObject(2);
				
				try {
					while(rs.next()) {
						
						FleetInfo info = new FleetInfo() ;
						
						int urbanId = rs.getInt(1);
						int fleetId = rs.getInt(2);
						info.getIdentity0().setUrbanId(urbanId);
						info.getIdentity0().setFleetId(fleetId);
						info.setName(rs.getString(3));
						info.setType(rs.getString(4).charAt(0));
						info.setExpectedSubscribers(rs.getInt(5));
						info.setExpectedTalkGroups(rs.getInt(6));
						info.setBanId0(rs.getInt(7));
						info.setNetworkId(rs.getInt(8));
						info.setActiveSubscribersCount(rs.getInt(9));
						info.setDAPId(rs.getInt(10));
						info.setOwnerName(StringUtil.emptyFromNull(rs.getString(11)));
						
						int accountsCounter = retrieveAssociatedAccountsCount(urbanId,fleetId);
						info.setAssociatedAccountsCount(accountsCounter);

						result.add(info);
					}
				} finally {
					rs.close();
				}
				return result;
			}
		});
	}
	
	public KnowbilityOperatorInfo retrieveKnowbilityOperator(String operatorId) {
		String sql = " select u.user_id, " +
		"        u.user_full_name, " +
		"        wpa.wpasn_work_position_code, " +
		"        wp.wp_supervisor, " +
		"        wal.charge_threshold, " +
		"        wal.adjustment_threshold, " +
		"        func.FUNC_DEPARTMENT_CODE " +
		" from users_ltd u, " +                        // users_ltd is view w/o password
		"      work_position_assignment wpa, " +
		"      work_position wp, " +
		"      wp_approval_level wal, " +
		"      function func " +
		" where u.user_id = ? " +
		" and   u.user_id = wpa.wpasn_user_id " +
		" and   wpa.wpasn_work_position_code = wp.wp_work_position_code " +
		" and   wp.approval_level = wal.approval_level " +
		" and  (wpa.wpasn_expiration_date is null or wpa.wpasn_expiration_date > sysdate) " +
		" and  (wp.wp_expiration_date is null or wp.wp_expiration_date > sysdate) " +
		" and  func.FUNC_FUNCTION_CODE = wp.wp_function_code";
		
		return getJdbcTemplate().query(sql, new Object[] {Integer.parseInt(operatorId)}, 
				new ResultSetExtractor<KnowbilityOperatorInfo>() {
			
			@Override
			public KnowbilityOperatorInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				KnowbilityOperatorInfo info = new KnowbilityOperatorInfo();

			      if (rs.next()) {
			          info = new KnowbilityOperatorInfo();
			          info.setID(rs.getString(1));
			          info.setName(rs.getString(2).trim());
			          info.setWorkPositionId(rs.getString(3));
			          info.setSupervisorId(rs.getString(4));
			          info.setChargeThresholdAmount(rs.getDouble(5));
			          info.setCreditThresholdAmount(rs.getDouble(6));
			          info.setDepartmentCode(rs.getString(7));
			        }

				return info;
			}
		});
	}
	
	public Collection<KnowbilityOperatorInfo> retrieveKnowbilityOperators() {
		return getReferenceData("KnowbilityOperators", "", new ResultSetExtractor<Collection<KnowbilityOperatorInfo>>() {
			
			@Override
			public Collection<KnowbilityOperatorInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<KnowbilityOperatorInfo> result = new ArrayList<KnowbilityOperatorInfo>();
				while (rs.next()) {
					KnowbilityOperatorInfo info = new KnowbilityOperatorInfo();
					
					info.setID(rs.getString(1));
					info.setName(rs.getString(2).trim());
					info.setWorkPositionId(rs.getString(3));
					info.setSupervisorId(rs.getString(4));
					info.setChargeThresholdAmount(rs.getDouble(5));
					info.setCreditThresholdAmount(rs.getDouble(6));
					info.setDepartmentCode(rs.getString(7));
					
					result.add(info);
				}
				return result;
			}
		});
	}
	
	public Date retrieveLogicalDate() {
		return getReferenceData("LogicalDate", "", new ResultSetExtractor<Date>() {
			
			@Override
			public Date extractData(ResultSet rs) throws SQLException, DataAccessException {
				Date date = null;
				if (rs.next()) {
					date = rs.getTimestamp(1);
				}
				return date;
			}
		});
	}
	
	public NumberGroupInfo retrieveNumberGroupByPhoneNumberProductType(String phoneNumber, String productType) {
		
		if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
			return null;
		}
		
		final ResultSetExtractor<NumberGroupInfo> extractor = new ResultSetExtractor<NumberGroupInfo>() {
			
			@Override
			public NumberGroupInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					NumberGroupInfo info = new NumberGroupInfo();

					info.setCode(rs.getString(1));
					info.setDescription(rs.getString(2));
					info.setProvinceCode(rs.getString(3));
					info.setDescriptionFrench(rs.getString(4)==null ? rs.getString(2) : rs.getString(4).equals("") ? rs.getString(2) : rs.getString(4));
					info.setNetworkId(rs.getInt(5));
					info.setNumberLocation(rs.getString(6));
					
					return info;
				}
				return null;
			}
		};
			
		if (productType.trim().toUpperCase().equals("I")) {

			String sql = "select ng.ngp_id, substr(ng.ngp_dsc,1,instr(ng.ngp_dsc,',',1,1) - 1)  " +
			", substr(rtrim(ng.ngp_dsc),-2), ng.ngp_dsc_f, ng.n_dap_id, pi.nl  " +
			" from  number_group ng , ptn_inv  pi" +
			" where pi.npa = ? and   pi.nxx = ? and   pi.line_number = ? and   ng.ngp_id = pi.ngp ";

			return getJdbcTemplate().query(sql, new Object[] {phoneNumber.substring(0,3), phoneNumber.substring(3,6), phoneNumber.substring(6,10)}, extractor);

		} else {
			String sql = "select ng.ngp_id, substr(ng.ngp_dsc,1,instr(ng.ngp_dsc,',',1,1) - 1), substr(rtrim(ng.ngp_dsc),-2) " +
			", ng.ngp_dsc_f, ng.n_dap_id, ci.nl  " +
			" from  number_group ng, ctn_inv  ci" +
			" where ci.ctn = ? and ng.ngp_id = ci.ngp ";

			return getJdbcTemplate().query(sql, new Object[] {phoneNumber}, extractor);
		}
	};
	
	public Collection<NumberGroupInfo> retrieveNumberGroupList(char accountType, char accountSubType, String productType, String equipmentType) {
		return retrieveNumberGroupList(accountType, accountSubType, productType, equipmentType, "TME");
	}
	
	public Collection<NumberGroupInfo> retrieveNumberGroupList(char accountType, char accountSubType,  String productType, String equipmentType, String marketAreaCode) {

		marketAreaCode = StringUtils.isEmpty(marketAreaCode) ? "TME" : marketAreaCode;
		
		String sql = "select distinct ngp_id   " +
		" , substr(ngp_dsc,1,instr(ngp_dsc,',',1,1) - 1)   city   " +
		" ,substr(rtrim(ngp_dsc),-2) prov   " +
		" , ngp_dsc_f   " +
		" , nnn.npanxx   " +
		" , atnl.number_location_id " +
		" , atnl.dealer " +
		" , atnl.sales_code " +
		" , ng.n_dap_id " +
		" from   number_group ng   " +
		"       ,npanxx_nl_ngp  nnn   " +
		"       ,acct_type_number_locations  atnl" +
		"  where   atnl.acct_type =" + "'" + accountType + "'"  +
		" and     atnl.acct_sub_type =" + "'" + accountSubType + "'"  +
		" and     nnn.nl= atnl.number_location_id " +
		" and     nnn.product_type=" + "'" + productType + "'"  +
		" and     nnn.available_ind='Y' " +
		" and     ng.ngp_id=nnn.ngp " +
		" and     ng.ngp_dsc not like '%ASIAN%'  " +
		" and not exists  " +
		" (select  1 " +
		" from ngp_exceptions  ne " +
		" where ne.ngp=ng.ngp_id " +
		" and ne.exclude_ind='Y') " +
		" and " + "'" + marketAreaCode + "'!='TMQ'" +
		" union " +
		"select distinct ngp_id   " +
		" , substr(ngp_dsc,1,instr(ngp_dsc,',',1,1) - 1)   city   " +
		" ,substr(rtrim(ngp_dsc),-2) prov   " +
		" , ngp_dsc_f   " +
		" , nnn.npanxx   " +
		" ,atnl.number_location_id " +
		" ,atnl.dealer " +
		" ,atnl.sales_code " +
		" , ng.n_dap_id " +
		" from   number_group ng   " +
		"       ,npanxx_nl_ngp  nnn   " +
		"       ,ngp_exceptions  ne  " +
		"       ,acct_type_number_locations  atnl" +
		" where   atnl.acct_type =" + "'" + accountType + "'"  +
		" and     atnl.acct_sub_type =" + "'" + accountSubType + "'"  +
		" and     nnn.nl= atnl.number_location_id " +
		" and     nnn.product_type=" + "'" + productType + "'"  +
		" and     nnn.available_ind='Y' " +
		" and     ng.ngp_id=nnn.ngp " +
		" and     ng.ngp_dsc not like '%ASIAN%'  " +
		" and     ne.ngp=nnn.ngp " +
		" and     ne.market_area_code='TMQ' " +
		" and " + "'" + marketAreaCode + "'='TMQ'" +
		" order by 2,6,3 ";
		
		return getJdbcTemplate().query(sql, new ResultSetExtractor<Collection<NumberGroupInfo>>() {
			
			@Override
			public Collection<NumberGroupInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				List<NumberGroupInfo> result = new ArrayList<NumberGroupInfo>();
				NumberGroupInfo numberGroup = new NumberGroupInfo();
				ArrayList<String> npanxxList = new ArrayList<String>();
				
				String ngp = "^";
				String nl = "^";

				while (rs.next()) {
					if (!ngp.equals(rs.getString(1))||!nl.equals(rs.getString(6)))
					{
						if (!ngp.equals("^"))
						{
							numberGroup.setNpaNXX(npanxxList.toArray( new String[npanxxList.size()]));
							result.add(numberGroup);
							numberGroup = new NumberGroupInfo();
							npanxxList = new ArrayList<String>();
						}
						numberGroup.setCode(rs.getString(1));
						numberGroup.setDescription(rs.getString(2));
						numberGroup.setDescriptionFrench(rs.getString(4)==null ? rs.getString(2) : rs.getString(4).equals("") ? rs.getString(2) : rs.getString(4));
						numberGroup.setProvinceCode(rs.getString(3));
						numberGroup.setNumberLocation(rs.getString(6));
						numberGroup.setDefaultDealerCode(rs.getString(7));
						numberGroup.setDefaultSalesCode(rs.getString(8));
						numberGroup.setNetworkId(rs.getInt(9));
						npanxxList.add(rs.getString(5));
					}
					else
					{
						npanxxList.add(rs.getString(5));
					}
					ngp = rs.getString(1);
					nl = rs.getString(6);
				}
				
			    numberGroup.setNpaNXX(npanxxList.toArray(new String[npanxxList.size()]));
			    result.add(numberGroup);

			    return result;
			}
		});
	}
	
	public Collection<NumberGroupInfo> retrieveNumberGroupListByProvince(char accountType, char accountSubType, String productType, String equipmentType, String province) {
		
		String sql = "select distinct ngp_id   " +
		" , substr(ngp_dsc,1,instr(ngp_dsc,',',1,1) - 1)   city   " +
		" ,substr(rtrim(ngp_dsc),-2) prov   " +
		" , ngp_dsc_f   " +
		" , nnn.npanxx   " +
		" , atnl.number_location_id " +
		" , atnl.dealer " +
		" ,atnl.sales_code " +
		" , ng.n_dap_id " +
		" from  number_group ng   " +
		"       ,npanxx_nl_ngp  nnn   " +
		"       ,acct_type_number_locations  atnl" +
		" where   atnl.acct_type =" + "'" + accountType + "'"  +
		" and     atnl.acct_sub_type =" + "'" + accountSubType + "'"  +
		" and     nnn.nl= atnl.number_location_id " +
		" and     nnn.product_type=" + "'" + productType + "'"  +
		" and     nnn.available_ind='Y' " +
		" and     ng.ngp_id=nnn.ngp " +
		" and     substr(ng.ngp_dsc,-2)=" + "'" + province.trim() + "'" +
		" and     ng.ngp_dsc not like '%ASIAN%'  " +
		" and not exists  " +
		" (select  1 " +
		" from ngp_exceptions  ne " +
		" where ne.ngp=ng.ngp_id " +
		" and ne.exclude_ind='Y') " +
		" order by prov,city ";		
		
		return getJdbcTemplate().query(sql, new ResultSetExtractor<Collection<NumberGroupInfo>>() {
			
			@Override
			public Collection<NumberGroupInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<NumberGroupInfo> result = new ArrayList<NumberGroupInfo>();
				ArrayList<String> npanxxList = new ArrayList<String>();
				NumberGroupInfo numberGroup = new NumberGroupInfo();
				String ngp = "^";
				while (rs.next()){
					if (!ngp.equals(rs.getString(1)))
					{
						if (!ngp.equals("^"))
						{
							numberGroup.setNpaNXX(npanxxList.toArray(new String[npanxxList.size()]));
							result.add(numberGroup);
							numberGroup = new NumberGroupInfo();
							npanxxList = new ArrayList<String>();
						}
						numberGroup.setCode(rs.getString(1));
						numberGroup.setDescription(rs.getString(2));
						numberGroup.setDescriptionFrench(rs.getString(4)==null ? rs.getString(2) : rs.getString(4).equals("") ? rs.getString(2) : rs.getString(4));
						numberGroup.setProvinceCode(rs.getString(3));
						numberGroup.setNumberLocation(rs.getString(6));
						numberGroup.setDefaultDealerCode(rs.getString(7));
						numberGroup.setDefaultSalesCode(rs.getString(8));
						numberGroup.setNetworkId(rs.getInt(9));
						npanxxList.add(rs.getString(5));
					}
					else
					{
						npanxxList.add(rs.getString(5));
					}
					ngp=rs.getString(1);
				}
				
				numberGroup.setNpaNXX(npanxxList.toArray(new String[npanxxList.size()]));
				result.add(numberGroup);
				
				return result;
			}
		});
	}
	
	public PhoneNumberResourceInfo retrievePhoneNumberResource(final String phoneNumber) {
		
		PhoneNumberResourceInfo info = null;
		
		String sql = "select ctn, product_type, ngp, ctn_status from ctn_inv where ctn = ?";
		
		info = getJdbcTemplate().query(sql, new Object[] {phoneNumber}, new ResultSetExtractor<PhoneNumberResourceInfo>() {
			
			@Override
			public PhoneNumberResourceInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PhoneNumberResourceInfo info = new PhoneNumberResourceInfo();

					info.setPhoneNumber(phoneNumber);
					info.setProductType(rs.getString(2));
					info.setNumberGroupCode(rs.getString(3));
					info.setStatus(rs.getString(4));

					return info;
				}
				return null;
			}
		});
		
		if (info == null) {
			sql = "select npa, nxx, line_number, ngp, resource_status from ptn_inv where npa = ? and nxx = ? and line_number = ?";
			
			PhoneNumber number = parsePhoneNumber(phoneNumber);
			
			info = getJdbcTemplate().query(sql, new Object[] {number.npa, number.nxx, number.line}, new ResultSetExtractor<PhoneNumberResourceInfo>() {
				
				@Override
				public PhoneNumberResourceInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						PhoneNumberResourceInfo info = new PhoneNumberResourceInfo();
						
						info.setPhoneNumber(phoneNumber);
						info.setProductType("I");
						info.setNumberGroupCode(rs.getString(4));
						info.setStatus(rs.getString(5));
						
						return info;
					}
					return null;
				}
			});
		}
		
		return info;
	}
	
	public <T extends ReferenceInfo> Collection<T> retrieveReferencesByTable(final Class<T> javaType, String table, String codeField, String descriptionField, String descriptionFrenchField, String criteria) {
		
		String sql = "select " + codeField + ", " + descriptionField + ", " + descriptionFrenchField + " from " + table;
		if (criteria != null && criteria.trim().length() > 0) {
			sql += " where " + criteria;
		}
		
		return getJdbcTemplate().query(sql, new RowMapper<T>() {
			
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				T info = null;
				
				try {
					info = javaType.newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public String retrieveResourceStatus(String productType, String resourceType, String phoneNumber) {

		String sql = null;
		String status = "";
		
		if (productType.equals(ReferenceDataManager.PRODUCT_TYPE_IDEN)) {
			if (resourceType.equals(ReferenceDataManager.RESOURCE_TYPE_PHONE_NUMBER)) {
				sql = " select resource_status  from ptn_inv p where p.npa = '" + phoneNumber.substring(0, 3) + "' and p.nxx = '" +
				phoneNumber.substring(3, 6) + "' and p.line_number = '" + phoneNumber.substring(6, 10) + "'";
			}
		} else {
			sql = " select  ctn_status from   ctn_inv c where c.ctn = " + "'" + phoneNumber + "'";
		}
		
		if (sql != null) {
			status = getJdbcTemplate().query(sql, new ResultSetExtractor<String>() {
				
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						return rs.getString(1);
					}
					return null;
				}
			});
		}
		
		return status;
	}
	
	public Collection<SalesRepInfo> retrieveSalesRepListByDealer(String dealerCode) throws DataAccessException {
		
		String sql = " select dealer_code, " +
		" sales_code, " +
		" sales_name " +
		" from dealer_sales_rep " +
		" where dealer_code = ? " ;
		
		return getJdbcTemplate().query(sql, new Object[] {dealerCode}, new RowMapper<SalesRepInfo>() {

			@Override
			public SalesRepInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				SalesRepInfo info = new SalesRepInfo();

				info.setDealerCode(rs.getString(1));
				info.setCode(rs.getString(2));
				info.setName(rs.getString(3));

				return info;
			}
		});
	}
	
	public Date retrieveSystemDate() {
		return getReferenceData("SystemDate", "", new ResultSetExtractor<Date>() {
			
			@Override
			public Date extractData(ResultSet rs) throws SQLException, DataAccessException {
				Date date = null;
				if (rs.next()) {
					date = rs.getTimestamp(1);
				}
				return date;
			}
		});
	}
	
	public Collection<TalkGroupInfo> retrieveTalkGroupsByFleetIdentity(final FleetIdentityInfo fleetIdentity) {
		
		String call = "{ call fleet_utility_pkg.GetTalkGroupsByFleetIdentity (?,?,?) }";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<Collection<TalkGroupInfo>>() {
			
			@Override
			public Collection<TalkGroupInfo> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				
				cs.setInt(1, fleetIdentity.getUrbanId());
				cs.setInt(2, fleetIdentity.getFleetId());
				cs.registerOutParameter(3, ORACLE_REF_CURSOR);
				cs.execute();

				ResultSet rs = (ResultSet) cs.getObject(3);
		      
				List<TalkGroupInfo> result = new ArrayList<TalkGroupInfo>();
				
				try {
					while (rs.next()) {
						
						TalkGroupInfo info = new  TalkGroupInfo();
						
						info.getFleetIdentity().setUrbanId(fleetIdentity.getUrbanId());
						info.getFleetIdentity().setFleetId(fleetIdentity.getFleetId());
						info.setTalkGroupId(rs.getInt(1));
						info.setName(rs.getString(2));
						info.setPriority(rs.getInt(3));
						info.setOwnerBanId(rs.getInt(4));
						
						result.add(info);
					}
				} finally {
					rs.close();
				}
				return result;
			}
		});
	}
	
	public Integer retrieveUrbanIdByNumberGroup(NumberGroupInfo numberGroup) throws DataAccessException {

		String sql = " select urban_id from uf_inv where n_dap_id = ? and rownum < 2 ";
		
		return getJdbcTemplate().query(sql, new Object[] {numberGroup.getNetworkId()}, new ResultSetExtractor<Integer>() {
			
			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getInt(1);
				}
				return null;
			}
		});
	}
	
	public WorkPositionInfo retrieveWorkPosition(final String workPositionId) {
		
		String sql = "SELECT WP.WP_SUPERVISOR, "
            + "       WP.WP_DESIGNATE, "
            + "       WP.WP_FUNCTION_CODE, "
            + "       WP.WP_EFFECTIVE_DATE, "
            + "       WP.WP_EXPIRATION_DATE, "
            + "       FU.FUNC_DEPARTMENT_CODE "
            + "  FROM WORK_POSITION WP, "
            + "       FUNCTION FU"
            + " WHERE WP.WP_WORK_POSITION_CODE = ? "
            + "   AND WP_EFFECTIVE_DATE <= SYSDATE "
            + "   AND NVL(WP_EXPIRATION_DATE, TO_DATE('2100-12-31','YYYY-MM-DD')) > SYSDATE "
            + "   AND FU.FUNC_FUNCTION_CODE = WP.WP_FUNCTION_CODE";
		
		WorkPositionInfo workPositionInfo = getJdbcTemplate().query(sql, new Object[] {Info.padTo(workPositionId, ' ', 8)}, new ResultSetExtractor<WorkPositionInfo>() {
			
			@Override
			public WorkPositionInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					WorkPositionInfo info = new WorkPositionInfo();

	                info.setId(workPositionId);
	                info.setSupervisorWorkPositionId(rs.getString(1));
	                info.setDesignateWorkPositionId(rs.getString(2));
	                info.setFunctionCode(rs.getString(3));
	                info.setEffectiveDate(rs.getDate(4));
	                info.setExpiryDate(rs.getDate(5));
	                info.setDepartmentCode(rs.getString(6));
					
					return info;
				}
				return null;
			}
		});
		
		if (workPositionInfo != null) {
			
			// assigned users
			
			sql = "SELECT DISTINCT WPASN_USER_ID FROM WORK_POSITION_ASSIGNMENT WHERE WPASN_WORK_POSITION_CODE = ? " +
			" AND WPASN_EFFECTIVE_DATE <= SYSDATE AND NVL(WPASN_EXPIRATION_DATE, TO_DATE('2100-12-31','YYYY-MM-DD')) > SYSDATE";
			
			List<String> users = getJdbcTemplate().queryForList(sql, new Object[] {Info.padTo(workPositionId, ' ', 8)}, String.class);
			workPositionInfo.setAssignedUsers(users.toArray( new String[users.size()]));
			
			// subordinates
			
			sql = "SELECT DISTINCT WP_WORK_POSITION_CODE FROM WORK_POSITION WHERE WP_SUPERVISOR = ? " +
			" AND WP_EFFECTIVE_DATE <= SYSDATE AND NVL(WP_EXPIRATION_DATE, TO_DATE('2100-12-31','YYYY-MM-DD')) > SYSDATE";
			
			List<String> subordinates = getJdbcTemplate().queryForList(sql, new Object[] {Info.padTo(workPositionId, ' ', 8)}, String.class);
			workPositionInfo.setSubordinateWorkPositionIds(subordinates.toArray( new String[subordinates.size()]));
		}
		
		return workPositionInfo;
	}
	
	public WorkPositionInfo[] retrieveWorkPositions() {

		String sql = "SELECT WP.WP_WORK_POSITION_CODE, " +
		"       WP.WP_SUPERVISOR, " +
		"       WP.WP_DESIGNATE, " +
		"       WP.WP_FUNCTION_CODE, " +
		"       WP.WP_EFFECTIVE_DATE, " +
		"       WP.WP_EXPIRATION_DATE, " +
		"       FU.FUNC_DEPARTMENT_CODE, " +
		"       WPASN.WPASN_USER_ID, " +
		"       SUB.WP_WORK_POSITION_CODE " +
		"  FROM WORK_POSITION WP, " +
		"       FUNCTION FU, " +
		"       WORK_POSITION_ASSIGNMENT WPASN, " +
		"       WORK_POSITION SUB " +
		" WHERE WP.WP_EFFECTIVE_DATE <= SYSDATE " +
		"   AND NVL(WP.WP_EXPIRATION_DATE, TO_DATE('2100-12-31','YYYY-MM-DD')) > SYSDATE " +
		"   AND FU.FUNC_FUNCTION_CODE = WP.WP_FUNCTION_CODE " +
		"   AND WPASN.WPASN_WORK_POSITION_CODE(+) = WP.WP_WORK_POSITION_CODE " +
		"   AND WPASN.WPASN_EFFECTIVE_DATE(+) <= SYSDATE " +
		"   AND NVL(WPASN.WPASN_EXPIRATION_DATE, TO_DATE('2100-12-31','YYYY-MM-DD')) > SYSDATE " +
		"   AND SUB.WP_SUPERVISOR(+) = WP.WP_WORK_POSITION_CODE " +
		"   AND SUB.WP_EFFECTIVE_DATE(+) <= SYSDATE " +
		"   AND NVL(SUB.WP_EXPIRATION_DATE, TO_DATE('2100/12/31', 'YYYY/MM/DD')) > SYSDATE " +
		" ORDER BY WP.WP_WORK_POSITION_CODE";
		
		return getJdbcTemplate().query(sql, new ResultSetExtractor<WorkPositionInfo[]>() {
			
			@Override
			public WorkPositionInfo[] extractData(ResultSet rs) throws SQLException, DataAccessException {

				List<WorkPositionInfo> result = new ArrayList<WorkPositionInfo>();
				List<String> assignedUsers = new ArrayList<String>();
				List<String> subordinates = new ArrayList<String>();
				WorkPositionInfo workPosition = null;
				String currentWorkPositionId = "";
		        
				
				while (rs.next()) {
					String newWorkPositionId = rs.getString(1);

					if (!newWorkPositionId.equals(currentWorkPositionId)) {
						if (workPosition != null) {
							
							workPosition.setAssignedUsers(assignedUsers.toArray( new String[assignedUsers.size()]));
							workPosition.setSubordinateWorkPositionIds(subordinates.toArray( new String[subordinates.size()]));

							result.add(workPosition);
						}

						workPosition = new WorkPositionInfo();

						workPosition.setId(newWorkPositionId);
						workPosition.setSupervisorWorkPositionId(rs.getString(2));
						workPosition.setDesignateWorkPositionId(rs.getString(3));
						workPosition.setFunctionCode(rs.getString(4));
						workPosition.setEffectiveDate(rs.getDate(5));
						workPosition.setExpiryDate(rs.getDate(6));
						workPosition.setDepartmentCode(rs.getString(7));

						assignedUsers = new ArrayList<String>();
						subordinates = new ArrayList<String>();

						currentWorkPositionId = newWorkPositionId;
					}

					String assignedUser = rs.getString(8);
					if (assignedUser != null && !assignedUsers.contains(assignedUser)){
						assignedUsers.add(assignedUser);
					}

					String subordinateWorkPositionId = rs.getString(9);
					if (subordinateWorkPositionId != null && !subordinates.contains(subordinateWorkPositionId)) {
						subordinates.add(subordinateWorkPositionId);
					}
				}

				if (workPosition != null) {
					workPosition.setAssignedUsers(assignedUsers.toArray( new String[assignedUsers.size()]));
					workPosition.setSubordinateWorkPositionIds(subordinates.toArray( new String[subordinates.size()]));
					result.add(workPosition);
				}

				return result.toArray( new WorkPositionInfo[result.size()]);
			}
		});
	}
	
	public WorkPositionInfo[] retrieveWorkPositions(String functionCode) {
		
        String sql = "SELECT WP.WP_WORK_POSITION_CODE, "
            + "       WP.WP_SUPERVISOR, "
            + "       WP.WP_DESIGNATE, "
            + "       WP.WP_FUNCTION_CODE, "
            + "       WP.WP_EFFECTIVE_DATE, "
            + "       WP.WP_EXPIRATION_DATE, "
            + "       FU.FUNC_DEPARTMENT_CODE, "
            + "       WPASN.WPASN_USER_ID, "
            + "       SUB.WP_WORK_POSITION_CODE "
            + "  FROM WORK_POSITION WP, "
            + "       FUNCTION FU, "
            + "       WORK_POSITION_ASSIGNMENT WPASN, "
            + "       WORK_POSITION SUB "
            + " WHERE WP.WP_FUNCTION_CODE = ? "
            + "   AND WP.WP_EFFECTIVE_DATE <= SYSDATE "
            + "   AND NVL(WP.WP_EXPIRATION_DATE, TO_DATE('2100/12/31', 'YYYY/MM/DD')) > SYSDATE "
            + "   AND FU.FUNC_FUNCTION_CODE = WP.WP_FUNCTION_CODE "
            + "   AND WPASN.WPASN_WORK_POSITION_CODE(+) = WP.WP_WORK_POSITION_CODE "
            + "   AND WPASN.WPASN_EFFECTIVE_DATE(+) <= SYSDATE "
            + "   AND NVL(WPASN.WPASN_EXPIRATION_DATE, TO_DATE('2100-12-31','YYYY-MM-DD')) > SYSDATE "
            + "   AND SUB.WP_SUPERVISOR(+) = WP.WP_WORK_POSITION_CODE "
            + "   AND SUB.WP_EFFECTIVE_DATE(+) <= SYSDATE "
            + "   AND NVL(SUB.WP_EXPIRATION_DATE, TO_DATE('2100/12/31', 'YYYY/MM/DD')) > SYSDATE "
            + " ORDER BY WP.WP_WORK_POSITION_CODE";
		
        return getJdbcTemplate().query(sql, new Object[] {Info.padTo(functionCode, ' ', 4)}, new ResultSetExtractor<WorkPositionInfo[]>() {

        	@Override
        	public WorkPositionInfo[] extractData(ResultSet rs) throws SQLException, DataAccessException {

        		List<WorkPositionInfo> result = new ArrayList<WorkPositionInfo>();
        		List<String> assignedUsers = new ArrayList<String>();
        		List<String> subordinates = new ArrayList<String>();
        		WorkPositionInfo workPosition = null;
        		String currentWorkPositionId = "";

        		while (rs.next()) {
        			String newWorkPositionId = rs.getString(1);

        			if (!newWorkPositionId.equals(currentWorkPositionId)) {
        				if (workPosition != null) {
        					workPosition.setAssignedUsers(assignedUsers.toArray( new String[assignedUsers.size()]));
        					workPosition.setSubordinateWorkPositionIds(subordinates.toArray( new String[subordinates.size()]));
        					result.add(workPosition);
        				}

        				workPosition = new WorkPositionInfo();

        				workPosition.setId(newWorkPositionId);
        				workPosition.setSupervisorWorkPositionId(rs.getString(2));
        				workPosition.setDesignateWorkPositionId(rs.getString(3));
        				workPosition.setFunctionCode(rs.getString(4));
        				workPosition.setEffectiveDate(rs.getDate(5));
        				workPosition.setExpiryDate(rs.getDate(6));
        				workPosition.setDepartmentCode(rs.getString(7));

        				assignedUsers = new ArrayList<String>();
        				subordinates = new ArrayList<String>();

        				currentWorkPositionId = newWorkPositionId;
        			}

        			String assignedUser = rs.getString(8);
        			if (assignedUser != null && !assignedUsers.contains(assignedUser))
        				assignedUsers.add(assignedUser);

        			String subordinateWorkPositionId = rs.getString(9);
        			if (subordinateWorkPositionId != null && !subordinates.contains(subordinateWorkPositionId))
        				subordinates.add(subordinateWorkPositionId);
        		}

        		if (workPosition != null) {
        			workPosition.setAssignedUsers(assignedUsers.toArray( new String[assignedUsers.size()]));
        			workPosition.setSubordinateWorkPositionIds(subordinates.toArray( new String[subordinates.size()]));
        			result.add(workPosition);
        		}

        		return result.toArray( new WorkPositionInfo[result.size()]);
        	}
        });
	}
	
	/////
	
	private class PhoneNumber {
		String npa;
		String nxx;
		String line;
	}

	private PhoneNumber parsePhoneNumber(String phone) {
		PhoneNumber phoneNumber = new PhoneNumber();

		if (phone != null) {
			phoneNumber.npa = phone.substring(0, 3);
			phoneNumber.nxx = phone.substring(3, 6);
			phoneNumber.nxx = phone.substring(6);
		}

		return phoneNumber;
	}

	public Collection<String> retrieveNpaNxxForMsisdnReservation(final String phoneNumber, boolean isPortedInNumber) {
		
		String call;
		
		if(isPortedInNumber) {
			call = "{call REFERENCE_APP_PKG.getnpanxxforportedinmsisdn(?, ?, ?, ?)}";
		} else {
			call = "{call REFERENCE_APP_PKG.getnpanxxfortelusmsisdn(?, ?, ?, ?)}";
		}
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback< Collection<String>>() {
			
			@Override
			public Collection<String> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				
				List<String> result = new ArrayList<String>();
				
				cs.setFetchSize(FETCH_SIZE);
				cs.setString(1, phoneNumber.substring(0,3));
				cs.setString(2, phoneNumber.substring(3,6));
				cs.setString(3, phoneNumber.substring(6,10));
				cs.registerOutParameter(4, OracleTypes.CURSOR);
				
				cs.execute();
				
				ResultSet rs = (ResultSet) cs.getObject(4);

				try {
					while (rs.next()) {
						String npaNxx = rs.getString(1);
						if (npaNxx != null && !npaNxx.equals("")) {
							result.add(npaNxx);
						}
					}
					return result;
				} finally {
					rs.close();
				}
			}
		});
	}
	
	public NumberGroupInfo retrieveNumberGroupByPortedInPhoneNumberProductType(final String phoneNumber, final String productType) {
		
		final String type = (productType != null && productType.equals(Subscriber.PRODUCT_TYPE_IDEN)) ? NumberGroup.MARKET_NPA_NXX_LR_RESOURCE_TYPE_PTN : productType; 

		String sql = "select ng.ngp_id   " +
		" , substr(ng.ngp_dsc,1,instr(ng.ngp_dsc,',',1,1) - 1)  " +
		" , substr(rtrim(ng.ngp_dsc),-2) " +
		" , ng.ngp_dsc_f   " +
		" , ng.n_dap_id  " +
		" from  number_group ng   " +
		"       ,market_npa_nxx_lr mnnl" +
		" where mnnl.npa = ? " +
		" and   mnnl.nxx = ? " +
		" and   mnnl.begin_line_range <= ? " +
		" and   mnnl.end_line_range >= ? " +
		" and   ng.ngp_id = mnnl.ngp " +
		" and   mnnl.product_type = ? ";

		Object[] args = {phoneNumber.substring(0,3), phoneNumber.substring(3,6), 
				phoneNumber.substring(6,10), phoneNumber.substring(6,10), type};
		
		return getJdbcTemplate().query(sql, args, new ResultSetExtractor<NumberGroupInfo>() {
			
			@Override
			public NumberGroupInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				NumberGroupInfo info = new NumberGroupInfo();
				
				if (rs.next()) {
					info.setCode(rs.getString(1));
					info.setDescription(rs.getString(2));
					info.setProvinceCode(rs.getString(3));
					info.setDescriptionFrench(rs.getString(4) == null ? rs.getString(2) : rs.getString(4).equals("") ? rs.getString(2) : rs.getString(4));
					info.setNetworkId(rs.getInt(5));

					if (type.equals(NumberGroup.MARKET_NPA_NXX_LR_RESOURCE_TYPE_PTN)) {
						String[] npanxx = retrieveNpaNxxForMsisdnReservation(phoneNumber, true).toArray( new String[0]);
						info.setNpaNXX(npanxx);
					} else {
						String[] npanxx = new String[1];
						npanxx[0] = new String();
						npanxx[0] = phoneNumber.substring(0,6);
						info.setNpaNXX(npanxx);
					}
				}
				
				return info;
			}
		});
	}
	
	 public int[] retrieveBillCycleListLeastUsed() {

			String sql = "select cycle_code " +
			"from " +
			"(select  to_date(lpad(to_char(decode(cycle_bill_day,29,28,30,28,31,28,cycle_bill_day)),2,'0')" +
			"||to_char(sysdate,'mmyy'),'ddmmyy') billdate,cycle_code ,no_of_bans  ,allocation_ind  from  cycle  " +
			"where  allocation_ind='Y'  	 " +
			"union  " +
			"select  to_date(lpad(to_char(decode(cycle_bill_day,29,28,30,28,31,28,cycle_bill_day)),2,'0')" +
			"||to_char(add_months(sysdate,1),'mmyy'),'ddmmyy') billdate 	,cycle_code ,no_of_bans ,allocation_ind " +
			"from  cycle where  allocation_ind='Y' ) " +
			"where  billdate >= trunc(sysdate) + 2 " +
			"and     billdate < trunc(sysdate) + 10 " +
			"order by no_of_bans";

			return getJdbcTemplate().query(sql,new Object[]{}, new ResultSetExtractor<int[]>() {
				
				@Override
				public int[] extractData(ResultSet rs) throws SQLException, DataAccessException {
					
					List<Integer> billCycles = new ArrayList<Integer>();
					while(rs.next()){
						billCycles.add(rs.getInt(1));
					}
					return ArrayUtil.castToPrimitiveInt(billCycles.toArray(new Integer[billCycles.size()]));
				}
			});
	}

	public List<SapccMappedServiceInfo> retrieveSapccMappedServiceInfo() {

		String sql = "select ssom.soc_code, ssom.offer_id from sapcc_soc_offer_map ssom where ssom.expiration_date is null";

		return getJdbcTemplate().query(sql, new ResultSetExtractor<List<SapccMappedServiceInfo>>() {
			
			@Override
			public List<SapccMappedServiceInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				List<SapccMappedServiceInfo> result = new ArrayList<SapccMappedServiceInfo>();
				while (rs.next()) {
					SapccMappedServiceInfo info = new SapccMappedServiceInfo();
					info.setCode(rs.getString("soc_code"));
					info.setOfferId(rs.getString("offer_id"));
					result.add(info);
				}
				
				return result;
			}
		});
	}

}
