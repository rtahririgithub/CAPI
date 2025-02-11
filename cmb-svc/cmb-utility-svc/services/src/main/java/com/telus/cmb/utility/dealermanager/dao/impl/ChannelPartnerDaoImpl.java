package com.telus.cmb.utility.dealermanager.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.api.ApplicationException;
import com.telus.cmb.utility.dealermanager.dao.ChannelPartnerDao;
import com.telus.eas.equipment.info.CPMSDealerInfo;
import com.telus.eas.equipment.info.HoursOfOperationInfo;

public class ChannelPartnerDaoImpl extends JdbcDaoSupport implements ChannelPartnerDao {


	@Override
	public boolean resetUserPassword(final String channelCode, final String userCode,
			final String newPassword) throws ApplicationException {
		
		String call = "{? = call CHANNEL#.ResetPassword(?,?,?)}";

		return getJdbcTemplate().execute(call, new CallableStatementCallback<Boolean>() {
			
			@Override
			public Boolean doInCallableStatement(CallableStatement callable)	
						throws SQLException, DataAccessException {
				int returnCode = 0;
				callable.registerOutParameter(1,Types.INTEGER);
				callable.setString(2,channelCode);
				callable.setString(3,userCode);
				callable.setString(4,newPassword);
				callable.execute();

				returnCode = callable.getInt(1);
				return (returnCode != 0);
			}
		});
	}	
	
	@Override
	public boolean changeUserPassword(final String channelCode, final String userCode,
			final String oldPassword, final String newPassword) throws ApplicationException {
		
		String call = "{? = call CHANNEL#.ChangeUserPassword(?,?,?,?)}" ;
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<Boolean>() {
			
			@Override
			public Boolean doInCallableStatement(CallableStatement callable) 
					throws SQLException, DataAccessException {
				int returnCode = 0;
				callable.registerOutParameter(1,Types.INTEGER);
				callable.setString(2,channelCode);
				callable.setString(3,userCode);
				callable.setString(4,oldPassword);
				callable.setString(5,newPassword);
				callable.execute();

				returnCode = callable.getInt(1);
				return (returnCode != 0);
			}
		});
	}

	@Override
	public boolean validUser(final String channelCode, final String userCode, 
			final String password) throws ApplicationException {

		String call = "{? = call CHANNEL#.ValidUser(?,?,?)}" ;

		return getJdbcTemplate().execute(call, new CallableStatementCallback<Boolean>() {

			@Override
			public Boolean doInCallableStatement(CallableStatement callable)
					throws SQLException, DataAccessException {
				int returnCode = -1;
				callable.registerOutParameter(1,Types.INTEGER);
				callable.setString(2,channelCode);
				callable.setString(3,userCode);
				callable.setString(4,password);
				callable.execute();

				returnCode = callable.getInt(1);
				return (returnCode != 0);
			}
		} );

	}

	@Override
	public CPMSDealerInfo getCPMSDealerByKBDealerCode(String pKBDealerCode, String pKBSalesRepCode) throws ApplicationException {
		if ((pKBSalesRepCode == null) || (pKBSalesRepCode.equals("")) || (pKBSalesRepCode.equals("0000"))) {
			String queryStr = 	
				" select unique chnl_org_cd, o.outlet_code, ttcba.brand_id " +
				" from cross_reference cr, outlet o, channel_organization co, " +
				" 	outlet_prod_class_tech_brand opctb, tech_type_class_brand_assoc ttcba " +
				" where cr.referencing_key_type_id = 4 " +  // CPMS Outlet ID
				" and cr.referenced_key_type_id = 7 " + // Knowbility Dealer Code
				" and cr.referenced_key_value = ? " +
				" and nvl(cr.expiry_dt, sysdate + 1) > sysdate " +
				" and o.outlet_id = cr.referencing_key_value " +
				" and nvl(o.expiration_dt, sysdate + 1) > sysdate " +
				" and o.effective_dt < sysdate  " +
				" and nvl(o.bp_status_cd, 'X') <> 'OT_INACT' " +
				" and (o.outlet_code <> '999' or outlet_des != 'BSCS Default Location') " +
				" and co.chnl_org_id = o.chnl_org_id " +
				" and nvl(co.expiration_dt, sysdate + 1) > sysdate " +
				" and opctb.outlet_id = o.outlet_id " +
				" and opctb.tech_type_class_brand_assoc_id = ttcba.tech_type_class_brand_assoc_id " +
				" and (opctb.assoc_expiry_dt is null or opctb.assoc_expiry_dt > sysdate) ";


			return getJdbcTemplate().query(queryStr,new Object[]{pKBDealerCode}, new ResultSetExtractor<CPMSDealerInfo>() {
				@Override
				public CPMSDealerInfo extractData(ResultSet result) throws SQLException,DataAccessException {
					ArrayList<Integer> list = new ArrayList<Integer>();
					CPMSDealerInfo pCPMSDealerInfo = null;
					while (result.next()) {
						pCPMSDealerInfo = new CPMSDealerInfo();
						pCPMSDealerInfo.setChannelCode(result.getString(1));
						pCPMSDealerInfo.setUserCode(result.getString(2));
						list.add(new Integer(result.getInt(3)));
					}
					
					int[] brandIds = new int[list.size()];
					for (int i = 0; i < brandIds.length; i++) {
						brandIds[i] = ((Integer)list.get(i)).intValue();
					}
					if (pCPMSDealerInfo != null)
						pCPMSDealerInfo.setBrandIds(brandIds);					
					return pCPMSDealerInfo;
				}
			});
		} else {
			String queryStr = 
				" select unique co.chnl_org_cd, sr.sales_rep_cd, ttcba.brand_id " +
				" from cross_reference cr, sales_representative sr, sales_rep_chnl_org srco, channel_organization co, " +
				"	outlet_sales_rep osr, outlet_prod_class_tech_brand opctb, tech_type_class_brand_assoc ttcba " +
				" where referencing_key_type_id = 8 " + // CPMS Sales Rep ID
				" and referenced_key_type_id = 9 " + // Knowbility Sales Rep PIN
				" and referenced_key_value = ? " +
				" and nvl(expiry_dt, sysdate + 1) > sysdate " +
				" and sr.sales_rep_id = cr.referencing_key_value " +
				" and srco.sales_rep_id = sr.sales_rep_id " +
				" and nvl(srco.expiration_dt, sysdate + 1) > sysdate " +
				" and co.chnl_org_id = srco.chnl_org_id " +
				" and nvl(co.expiration_dt, sysdate + 1) > sysdate " +
				" and osr.sales_rep_id = sr.sales_rep_id " +
				" and osr.outlet_id = opctb.outlet_id " +
				" and opctb.tech_type_class_brand_assoc_id = ttcba.tech_type_class_brand_assoc_id " +
				" and (opctb.assoc_expiry_dt is null or opctb.assoc_expiry_dt > sysdate) ";		
			return getJdbcTemplate().query(queryStr,new Object[]{pKBSalesRepCode}, new ResultSetExtractor<CPMSDealerInfo>() {
				@Override
				public CPMSDealerInfo extractData(ResultSet result) throws SQLException,	DataAccessException {
					ArrayList<Integer> list = new ArrayList<Integer>();
					CPMSDealerInfo pCPMSDealerInfo = null;
					while (result.next()) {
						pCPMSDealerInfo = new CPMSDealerInfo();
						pCPMSDealerInfo.setChannelCode(result.getString(1));
						pCPMSDealerInfo.setUserCode(result.getString(2));
						list.add(new Integer(result.getInt(3)));
					}
					int[] brandIds = new int[list.size()];
					for (int i = 0; i < brandIds.length; i++) {
						brandIds[i] = ((Integer)list.get(i)).intValue();
					}
					if (pCPMSDealerInfo != null)
						pCPMSDealerInfo.setBrandIds(brandIds);
					return pCPMSDealerInfo;
				}
			} );
		}
	}

	@Override
	public CPMSDealerInfo getCPMSDealerByLocationTelephoneNumber(String pLocationTelephoneNumber) throws ApplicationException {
		
		String npa = null;
		String nxx = null;
		String nnnn = null;

		//Parse telephone number into component parts
		npa = pLocationTelephoneNumber.substring(0,3);
		nxx = pLocationTelephoneNumber.substring(3,6);
		nnnn = pLocationTelephoneNumber.substring(6);	
		
		String queryStr = 
			" select chnl_org_cd, o.outlet_code, ttcba.brand_id " +
			" from contact_phone cp, outlet o, channel_organization co, " +
			" 	outlet_prod_class_tech_brand opctb, tech_type_class_brand_assoc ttcba " +
			" where cp.npa = " + "'" + npa + "'" +
			" and cp.nxx = " + "'" + nxx + "'" +
			" and cp.nnnn = " + "'" + nnnn + "'" +
			" and cp.contact_group_cd = 'OT' " +
			" and cp.reason_type_cd = 'Location' " +
			" and cp.phone_type_cd = 'LandLine' and cp.contacting_id = o.outlet_id " +
			" and nvl(o.expiration_dt, sysdate + 1) > sysdate and o.effective_dt < sysdate " +
			" and nvl(o.bp_status_cd, 'X') <> 'OT_INACT' " +
			" and (o.outlet_code <> '999' or outlet_des != 'BSCS Default Location') " +
			" and co.chnl_org_id = o.chnl_org_id " +
			" and nvl(co.expiration_dt, sysdate + 1) > sysdate " +
			" and opctb.outlet_id = o.outlet_id " +			
			" and opctb.tech_type_class_brand_assoc_id = ttcba.tech_type_class_brand_assoc_id " +
			" and (opctb.assoc_expiry_dt is null or opctb.assoc_expiry_dt > sysdate) ";
		
		return getJdbcTemplate().query(queryStr, new ResultSetExtractor<CPMSDealerInfo>() {
			
			@Override
			public CPMSDealerInfo extractData(ResultSet result) throws SQLException, DataAccessException {
				ArrayList<Integer> list = new ArrayList<Integer>();		
				CPMSDealerInfo pCPMSDealerInfo = null;
				while (result.next()) {
					pCPMSDealerInfo = new CPMSDealerInfo();
					pCPMSDealerInfo.setChannelCode(result.getString(1));
					pCPMSDealerInfo.setUserCode(result.getString(2));
					list.add(new Integer(result.getInt(3)));
				}
				int[] brandIds = new int[list.size()];
				for (int i = 0; i < brandIds.length; i++) {
					brandIds[i] = ((Integer)list.get(i)).intValue();
				}
				if (pCPMSDealerInfo != null)
					pCPMSDealerInfo.setBrandIds(brandIds);				
				return pCPMSDealerInfo;
			}
		}); 
	}

	@Override
	public CPMSDealerInfo getUserInformation2(final String channelCode, final String userCode) throws ApplicationException {
		String call = "{call CHANNEL#.GetMoreUserInformation " + 
						"(?,?,?,?,?,?,?,?,?,?,?," + 
						"?,?,?,?,?,?,?,?,?,?,?," + 
						"?,?,?,?,?,?,?,?,?,?,?)}" ;

		return getJdbcTemplate().execute(call, new CallableStatementCallback<CPMSDealerInfo>() {
			
			@Override
			public CPMSDealerInfo doInCallableStatement(CallableStatement callable)	throws SQLException, DataAccessException {
				CPMSDealerInfo channelUserInfo = null;
				HoursOfOperationInfo hrsOfOperationInfo = null;
				
				callable.setString(1, channelCode);
				callable.setString(2, userCode);
				callable.registerOutParameter(3, Types.VARCHAR);   //chnl_org_desc_out OUT   
				callable.registerOutParameter(4, Types.VARCHAR);   //user_desc_out OUT
				callable.registerOutParameter(5, Types.VARCHAR);   //address_province_cd OUT
				callable.registerOutParameter(6, Types.VARCHAR);   //chnl_org_type_cd OUT 
				callable.registerOutParameter(7, Types.VARCHAR);   //ivr_privilege_routing_ind 
				callable.registerOutParameter(8, Types.VARCHAR);   //street_name OUT 
				callable.registerOutParameter(9, Types.VARCHAR);   //city_name OUT 
				callable.registerOutParameter(10, Types.VARCHAR);  //location_province_cd OUT
				callable.registerOutParameter(11, Types.VARCHAR);  //postal_cd OUT
				callable.registerOutParameter(12, Types.VARCHAR);  //outlet_close_time_1 OUT
				callable.registerOutParameter(13, Types.VARCHAR);  //outlet_close_time_2 OUT 
				callable.registerOutParameter(14, Types.VARCHAR);  //outlet_close_time_3 OUT
				callable.registerOutParameter(15, Types.VARCHAR);  //outlet_close_time_4 OUT
				callable.registerOutParameter(16, Types.VARCHAR);  //outlet_close_time_5 OUT
				callable.registerOutParameter(17, Types.VARCHAR);  //outlet_close_time_6 OUT
				callable.registerOutParameter(18, Types.VARCHAR);  //outlet_close_time_7 OUT
				callable.registerOutParameter(19, Types.VARCHAR);  //outlet_open_time_1 OUT
				callable.registerOutParameter(20, Types.VARCHAR);  //outlet_open_time_2 OUT
				callable.registerOutParameter(21, Types.VARCHAR);  //outlet_open_time_3 OUT
				callable.registerOutParameter(22, Types.VARCHAR);  //outlet_open_time_4 OUT
				callable.registerOutParameter(23, Types.VARCHAR);  //outlet_open_time_5 OUT
				callable.registerOutParameter(24, Types.VARCHAR);  //outlet_open_time_6 OUT
				callable.registerOutParameter(25, Types.VARCHAR);  //outlet_open_time_7 OUT
				callable.registerOutParameter(26, Types.VARCHAR);  //sequence_value_1 OUT 
				callable.registerOutParameter(27, Types.VARCHAR);  //sequence_value_2 OUT
				callable.registerOutParameter(28, Types.VARCHAR);  //sequence_value_3 OUT
				callable.registerOutParameter(29, Types.VARCHAR);  //sequence_value_4 OUT
				callable.registerOutParameter(30, Types.VARCHAR);  //sequence_value_5 OUT
				callable.registerOutParameter(31, Types.VARCHAR);  //sequence_value_6 OUT
				callable.registerOutParameter(32, Types.VARCHAR);  //sequence_value_7 OUT
				callable.registerOutParameter(33, Types.VARCHAR);

				callable.execute();

				channelUserInfo =  new CPMSDealerInfo();

				channelUserInfo.setChannelCode(channelCode);
				channelUserInfo.setUserCode(userCode);
				channelUserInfo.setChannelDesc(callable.getString(3) == null ? "" : callable.getString(3));
				channelUserInfo.setUserDesc(callable.getString(4) == null ? "" : callable.getString(4));
				channelUserInfo.setProvinceCode(callable.getString(5) == null ? "" : callable.getString(5));
				channelUserInfo.setChannelOrgTypeCode(callable.getString(6) == null ? "" : callable.getString(6));
				channelUserInfo.setHighPriority(callable.getString(7) == null ? false : callable.getString(7).equals("Y") ? true : false);

				ArrayList<String> address = new ArrayList<String>();
				address.add(callable.getString(8) == null ? "" : callable.getString(8));
				address.add(callable.getString(9) == null ? "" : callable.getString(9));
				address.add(callable.getString(10) == null ? "" : callable.getString(10));
				address.add(callable.getString(11) == null ? "" : callable.getString(11));

				String[] adr = new String[address.size()]; 
				for (int i=0; i< address.size();i++) {
					adr[i]= (String)(address.get(i));
				}
				channelUserInfo.setAddress(adr);

				String  closeTime_1 = (callable.getString(12) == null ? "" : callable.getString(12));
				String  closeTime_2 = (callable.getString(13) == null ? "" : callable.getString(13));
				String  closeTime_3 = (callable.getString(14) == null ? "" : callable.getString(14));
				String  closeTime_4 = (callable.getString(15) == null ? "" : callable.getString(15));
				String  closeTime_5 = (callable.getString(16) == null ? "" : callable.getString(16));
				String  closeTime_6 = (callable.getString(17) == null ? "" : callable.getString(17));
				String  closeTime_7 = (callable.getString(18) == null ? "" : callable.getString(18));
				String  openTime_1 = (callable.getString(19) == null ? "" : callable.getString(19));
				String  openTime_2 = (callable.getString(20) == null ? "" : callable.getString(20));
				String  openTime_3 = (callable.getString(21) == null ? "" : callable.getString(21));
				String  openTime_4 = (callable.getString(22) == null ? "" : callable.getString(22));
				String  openTime_5 = (callable.getString(23) == null ? "" : callable.getString(23));
				String  openTime_6 = (callable.getString(24) == null ? "" : callable.getString(24));
				String  openTime_7 = (callable.getString(25) == null ? "" : callable.getString(25));

				int  seqValue_1 = Integer.parseInt(callable.getString(26)== null ? "0" : callable.getString(26));
				int  seqValue_2 = Integer.parseInt(callable.getString(27) == null ? "0" : callable.getString(27));
				int  seqValue_3 = Integer.parseInt(callable.getString(28) == null ? "0" : callable.getString(28));
				int  seqValue_4 = Integer.parseInt(callable.getString(29) == null ? "0" : callable.getString(29));
				int  seqValue_5 = Integer.parseInt(callable.getString(30) == null ? "0" : callable.getString(30));
				int  seqValue_6 = Integer.parseInt(callable.getString(31) == null ? "0" : callable.getString(31));
				int  seqValue_7 = Integer.parseInt(callable.getString(32) == null ? "0" : callable.getString(32));

				HoursOfOperationInfo[] hoursOfOperationInfo = new HoursOfOperationInfo[7];

				for (int i=1; i < 8; i++) {
					hrsOfOperationInfo = new HoursOfOperationInfo();

					hrsOfOperationInfo.setDay(i == 1 ? seqValue_1 : i == 2 ? seqValue_2 : i == 3 ? seqValue_3 : i == 4 ? seqValue_4 : i == 5 ? seqValue_5 : i == 6 ? seqValue_6 : i == 7 ? seqValue_7 : 0 );
					hrsOfOperationInfo.setCloseTime(i == 1 ? closeTime_1 : i == 2 ? closeTime_2 : i == 3 ? closeTime_3 : i == 4 ? closeTime_4 : i == 5 ? closeTime_5 : i == 6 ? closeTime_6 : i == 7 ? closeTime_7 : "" );
					hrsOfOperationInfo.setOpenTime(i == 1 ? openTime_1 : i == 2 ? openTime_2 : i == 3 ? openTime_3 : i == 4 ? openTime_4 : i == 5 ? openTime_5 : i == 6 ? openTime_6 : i == 7 ? openTime_7 : "" );
					hoursOfOperationInfo[i - 1] = hrsOfOperationInfo;
				}
				channelUserInfo.setHoursOfOperation(hoursOfOperationInfo);

				channelUserInfo.setPhone(callable.getString(33)==null ? "" :callable.getString(33));
				
				return channelUserInfo;
			}
		} );
	}
	
	
	
	@Override
	public CPMSDealerInfo getUserInformation(final String channelCode, final String userCode)throws ApplicationException {
		String call = "{call CHANNEL#.GetUserInformation(?,?,?,?)}" ;

		return getJdbcTemplate().execute(call, new CallableStatementCallback<CPMSDealerInfo>() {
			
			@Override
			public CPMSDealerInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				CPMSDealerInfo channelUserInfo = null;
				callable.setString(1,channelCode);
				callable.setString(2,userCode);
				callable.registerOutParameter(3,Types.VARCHAR);
				callable.registerOutParameter(4,Types.VARCHAR);
				callable.execute();

				channelUserInfo =  new CPMSDealerInfo();

				channelUserInfo.setChannelCode(channelCode);
				channelUserInfo.setUserCode(userCode);
				channelUserInfo.setChannelDesc(callable.getString(3) == null ? "" : callable.getString(3));
				channelUserInfo.setUserDesc(callable.getString(4) == null ? "" : callable.getString(4));
				return channelUserInfo;
			}
		}); 
	}

	
	@Override
	public long[] getChnlOrgAssociation(long chnlOrgId, String associateReasonCd)throws ApplicationException {
		StringBuffer buff = new StringBuffer(1024);

		buff.append("SELECT chnl_org_parent_id , chnl_org_subordinate_id ");
		buff.append("FROM channel_org_association ");
		buff.append("WHERE ");
		buff.append("(chnl_org_parent_id = ? or chnl_org_subordinate_id = ? ) ");
		buff.append("and effective_dt < sysdate ");
		buff.append("and (expiration_dt is null or expiration_dt > sysdate ) ");
		buff.append("and chnl_org_assoc_rsn_cd = ? ");
		
		String queryStr = buff.toString();
		
		return getJdbcTemplate().query(queryStr,new Object[]{chnlOrgId,chnlOrgId,associateReasonCd} ,new ResultSetExtractor<long[]>() {
			@Override
			public long[] extractData(ResultSet result) throws SQLException, DataAccessException {
				long [] returnChnlOrgIds = null;
				Collection<Long> chnlOrgAssocList = new ArrayList<Long>();
				Long[] chnlOrgAssocArray = null;
				
				while (result.next()) {
					Long assChnlOrgIdParent =  new Long(result.getLong(1));
					if (!chnlOrgAssocList.contains(assChnlOrgIdParent)) {
						chnlOrgAssocList.add(assChnlOrgIdParent);
					}

					Long assChnlOrgIdSubOrdinate = new Long(result.getLong(2));
					if (!chnlOrgAssocList.contains(assChnlOrgIdSubOrdinate)) {
						chnlOrgAssocList.add(assChnlOrgIdSubOrdinate);
					}
				}
				chnlOrgAssocArray = (Long []) chnlOrgAssocList.toArray(new Long[0]);

				if (chnlOrgAssocArray != null) {
					chnlOrgAssocList = null;

					returnChnlOrgIds = new long[chnlOrgAssocArray.length];
					for (int i = 0; i < chnlOrgAssocArray.length; i++) {
						returnChnlOrgIds[i] = chnlOrgAssocArray[i].longValue();
					}
				}
				return returnChnlOrgIds;
			}
		});
	}
}
