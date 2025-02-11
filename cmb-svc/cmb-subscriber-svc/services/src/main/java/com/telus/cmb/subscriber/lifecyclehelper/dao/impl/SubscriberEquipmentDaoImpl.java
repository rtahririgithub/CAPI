package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.RowMapper;

import com.telus.api.account.AccountManager;
import com.telus.api.account.Subscriber;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.subscriber.lifecyclehelper.dao.SubscriberEquipmentDao;
import com.telus.eas.equipment.info.EquipmentSubscriberInfo;
import com.telus.eas.subscriber.info.EquipmentChangeHistoryInfo;
import com.telus.eas.subscriber.info.HandsetChangeHistoryInfo;
import com.telus.eas.subscriber.info.IdenResourcesInfo;

public class SubscriberEquipmentDaoImpl extends MultipleJdbcDaoTemplateSupport implements SubscriberEquipmentDao {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private Logger logger = Logger.getLogger(SubscriberEquipmentDaoImpl.class);

	@Override
	public int getCountForRepairID(String repairID) {
		String sql="Select count(*) from tmi_equipment_change where repair_id = '" + repairID +"' ";
		try{
			return super.getServJdbcTemplate().queryForInt(sql);
		}catch(EmptyResultDataAccessException e){
			return 0;
		}
	}

	@Override
	public List<EquipmentChangeHistoryInfo> retrieveEquipmentChangeHistory(
			final int ban, final String subscriberID, final Date from,final  Date to) {
		String callString="{? = call SUBSCRIBER_PKG.GetEquipmentChangeHistory(?, ?, ?, ?, ?, ?)}";
		return  super.getKnowbilityJdbcTemplate().execute(callString,new CallableStatementCallback<List<EquipmentChangeHistoryInfo>>(){
			@Override
			public List<EquipmentChangeHistoryInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{
				List<EquipmentChangeHistoryInfo> list = new ArrayList<EquipmentChangeHistoryInfo>();
				ResultSet result = null;
				try{
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, ban);
					callable.setString(3, subscriberID);
					callable.setString(4, dateFormat.format(from));
					callable.setString(5, dateFormat.format(to));
					callable.registerOutParameter(6, OracleTypes.CURSOR);
					callable.registerOutParameter(7, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;
					if (success) {
						result = (ResultSet) callable.getObject(6);
						while (result.next()) {
							EquipmentChangeHistoryInfo info = new EquipmentChangeHistoryInfo();
							info.setProductType(result.getString(1));
							info.setSerialNumber(result.getString(2));
							info.setEffectiveDate(result.getDate(3));
							info.setExpiryDate(result.getDate(4));
							info.setEsnLevel(result.getInt(5));
							info.setEncodingFormat(result.getString(6));

							list.add(info);
						}
					}else{
						logger.debug("Stored procedure Failed"+ callable.getString(7));
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
	public List<HandsetChangeHistoryInfo> retrieveHandsetChangeHistory(int ban,
			String subscriberID, Date from, Date to) {
		String sql = " SELECT  create_date, new_sm_num, old_sm_num  " +
		"           ,old_technology_type   " +
		"           ,new_technology_type   " +
		"           ,old_product_cd   " +
		"           ,new_product_cd   " +
		" FROM  tmi_equipment_change " +
		" WHERE    ban = ?  " +
		" AND          subscriber_no = ?  " +
		" AND      trunc(create_date) >= to_date(?,'mm/dd/yyyy') " +
		" AND          trunc(create_date) <= to_date(?,'mm/dd/yyyy') " +
		" order by create_date desc" ;
		String fromDate = dateFormat.format(from) ;
		String toDate =   dateFormat.format(to) ;
		return super.getServJdbcTemplate().query(sql,new Object[]{ban,subscriberID,fromDate,toDate}, new RowMapper<HandsetChangeHistoryInfo>(){

			@Override
			public HandsetChangeHistoryInfo mapRow(ResultSet rset, int rowNum)
			throws SQLException {
				HandsetChangeHistoryInfo hChangeHistoryInfo = new HandsetChangeHistoryInfo();
				hChangeHistoryInfo.setDate(rset.getTimestamp(1));
				hChangeHistoryInfo.setNewSerialNumber(rset.getString(2));
				hChangeHistoryInfo.setOldSerialNumber(rset.getString(3));
				hChangeHistoryInfo.setOldTechnologyType(rset.getString(4)==null ? "" : rset.getString(4));
				hChangeHistoryInfo.setNewTechnologyType(rset.getString(5)==null ? "" : rset.getString(5));
				hChangeHistoryInfo.setOldProductCode(rset.getString(6)==null ? "" : rset.getString(6));
				hChangeHistoryInfo.setNewProductCode(rset.getString(7)==null ? "" : rset.getString(7));
				return hChangeHistoryInfo;
			}
		});
	}

	@Override
	public Hashtable<String,String> getUSIMListByIMSIs(final String[] IMISIs) {
		String callString = "{ call Client_Equipment.getUSIMListByIMSIs(?,?) }";
		return  super.getDistJdbcTemplate().execute(callString, new CallableStatementCallback<Hashtable<String,String>>() {

			@Override
			public Hashtable<String,String> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				ResultSet result = null;
				Hashtable<String,String> USIMList = new Hashtable<String,String>();
				// create array descriptor
				try{
					ArrayDescriptor IMSIsArrayDesc = ArrayDescriptor.createDescriptor("T_IMSI_ARRAY", callable.getConnection());

					// create Oracle array of IMSIs
					ARRAY IMSIsArray = new ARRAY(IMSIsArrayDesc, callable.getConnection(), IMISIs);

					// set/register input/output parameters
					callable.registerOutParameter(2, OracleTypes.CURSOR);
					callable.setArray(1, IMSIsArray);
					callable. execute();
					result = (ResultSet)callable.getObject(2);

					while (result.next()) {
						USIMList.put(result.getString(1),result.getString(2));
					}
				}finally{
					if(result != null)
						result.close();
				}
				return USIMList;
			}
		});
	}

	@Override
	public List<String> getIMSIsByUSIM(final String USIM_Id) {
		String callString = "{call Client_Equipment.getIMSIsByUSIM(?,?)}";

		return super.getDistJdbcTemplate().execute(callString, new CallableStatementCallback<List<String>>(){

			@Override
			public List<String> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				List<String> list = new ArrayList<String>();
				ResultSet result = null;
				try{
					callable.registerOutParameter(2, OracleTypes.CURSOR);
					callable.setString(1, USIM_Id);
					callable. execute();
					result = (ResultSet) callable.getObject(2);
					while (result.next()) {
						list.add(result.getString(1));
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
	public List<EquipmentSubscriberInfo> retrieveEquipmentSubscribers(
			String serialNumber, boolean active) {
		String queryStr =
			"SELECT pd.customer_id, pd.subscriber_no, pd.product_type, pd.effective_date, pd.expiration_date, pd.esn_level " +
			"  FROM physical_device pd " +
			" WHERE unit_esn = ? ";

		if (active) {
			queryStr +=
				"   AND NVL(effective_date, TO_DATE('1970/01/01', 'YYYY/MM/DD')) <= sysdate " +
				"   AND NVL(expiration_date, TO_DATE('2099/12/31', 'YYYY/MM/DD')) > sysdate ";
		}
		return super.getKnowbilityJdbcTemplate().query(queryStr,new Object[]{serialNumber}, new RowMapper<EquipmentSubscriberInfo>() {
			@Override
			public EquipmentSubscriberInfo mapRow(ResultSet result, int rowNum)
			throws SQLException {
				EquipmentSubscriberInfo equipmentSubscriber = new EquipmentSubscriberInfo();

				equipmentSubscriber.setBanId(result.getInt(1));
				equipmentSubscriber.setSubscriberId(result.getString(2));
				equipmentSubscriber.setProductType(result.getString(3));
				equipmentSubscriber.setEffectiveDate(result.getDate(4));
				equipmentSubscriber.setExpiryDate(result.getDate(5));
				equipmentSubscriber.setEsnLevel(result.getInt(6));

				if (equipmentSubscriber.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
					int banId = equipmentSubscriber.getBanId();
					String subscriberId = equipmentSubscriber.getSubscriberId();
					IdenResourcesInfo idenResources = (IdenResourcesInfo) retrieveIdenResources(new int[] {banId}, new String[] {subscriberId}).get(subscriberId + "_" + String.valueOf(banId));
					equipmentSubscriber.setPhoneNumber(idenResources.getPhoneNumber());
				}
				else {
					equipmentSubscriber.setPhoneNumber(equipmentSubscriber.getSubscriberId());
				}
				return equipmentSubscriber;
			}
		});
	}

	private Hashtable<String,IdenResourcesInfo> retrieveIdenResources(final int[] bans, final String[] subscriberIds) {
		String callString = "{? = call SUBSCRIBER_PKG.GetIdenResources(?, ?, ?, ?)}"; 
		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Hashtable<String,IdenResourcesInfo>>() {

			@Override
			public Hashtable<String,IdenResourcesInfo> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				Hashtable<String,IdenResourcesInfo> idenResources = null;
				ResultSet result = null;
				try {
					idenResources = new Hashtable<String,IdenResourcesInfo>();

					if (bans != null && bans.length > 0 && subscriberIds != null && subscriberIds.length > 0) {
						// create array descriptors
						ArrayDescriptor banArrayDesc = ArrayDescriptor.createDescriptor("T_BAN_ARRAY", callable.getConnection());
						ArrayDescriptor subscriberIdArrayDesc = ArrayDescriptor.createDescriptor("T_SUBSCRIBER_ARRAY", callable.getConnection());

						// create Oracle array of BANs
						ARRAY banArray = new ARRAY(banArrayDesc, callable.getConnection(), bans);

						// create Oracle array of subscriber IDs
						ARRAY subscriberIdsArray = new ARRAY(subscriberIdArrayDesc, callable.getConnection(), subscriberIds);

						// set/register input/output parameters
						callable.registerOutParameter(1, OracleTypes.NUMBER);
						callable.setArray(2, banArray);
						callable.setArray(3, subscriberIdsArray);
						callable.registerOutParameter(4, OracleTypes.CURSOR);
						callable.registerOutParameter(5, OracleTypes.VARCHAR);

						callable.execute();

						boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

						if (success) {
							String curSubscriberId = null;
							int curBanId = 0;
							IdenResourcesInfo idenResource = null;

							result = (ResultSet) callable.getObject(4);

							while (result.next()) {
								String newSubscriberId = result.getString(1);
								int newBanId = result.getInt(2);

								if (newSubscriberId != null && !(newSubscriberId.equals(curSubscriberId) && newBanId == curBanId)) {
									if (curSubscriberId != null)
										idenResources.put(curSubscriberId + "_" + String.valueOf(curBanId), idenResource);

									curSubscriberId = newSubscriberId;
									curBanId = newBanId;
									idenResource = new IdenResourcesInfo();
								}

								String resourceType = result.getString(8);

								if (resourceType != null) {
									if (resourceType.equals("H")) {
										idenResource.setUrbanId(result.getInt(3));
										idenResource.setFleetId(result.getInt(4));
										idenResource.setMemberId(result.getString(5));
										idenResource.setResourceStatus(result.getString(9));
									}
									else if (resourceType.equals("N")) {
										idenResource.setPhoneNumber(result.getString(7));
									}
									else {
										idenResource.setIp(result.getString(7));
										idenResource.setIpType(result.getString(8));
									}

									// IMSI
									idenResource.setImsi(result.getString("imsi_number"));
								}
							}

							if (curSubscriberId != null)
								idenResources.put(curSubscriberId + "_" + curBanId, idenResource);
						}
						else {
	//			            print(getClass().getName(), methodName, "Failed to retrieve IDEN resources: " + callable.getString(5));
						}
					}
					else {
	//			          print(getClass().getName(), methodName, "Array of subscriber IDs is either null or empty.");
					}
				} finally {
					if(result!=null)result.close();
				}
				return idenResources;
			}
		});
	}

	@Override
	public List<String> getIMSIsBySerialNumber(final String serialNumber) {
		try{
		String callString="{call Client_Equipment.getusimbyimei(?,?)}";
		return getDistJdbcTemplate().execute(callString, new CallableStatementCallback<List<String>>() {
			
			@Override
			public List<String> doInCallableStatement(CallableStatement callable)throws SQLException, DataAccessException {
				List<String> listofIMSIs = new ArrayList<String>();

					// assuming serial number is USIM 
					listofIMSIs = getIMSIsByUSIM(serialNumber); 
	
					//Serial Number is EMEI if found nothing
					if(listofIMSIs.size() == 0) {
						//it means serial number is EMEI
						callable.setString(1, serialNumber);
						callable.registerOutParameter(2, Types.VARCHAR);
				
						callable. execute();
				        
						String usimID = callable.getString(2);
						listofIMSIs = getIMSIsByUSIM(usimID);
					}

		    	return listofIMSIs;
			}
		} );
		}catch(UncategorizedSQLException ex){
			return new ArrayList<String>();
		}
	}

}
