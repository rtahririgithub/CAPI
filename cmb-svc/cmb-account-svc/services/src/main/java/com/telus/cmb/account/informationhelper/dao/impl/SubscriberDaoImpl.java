package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.telus.api.ApplicationException;
import com.telus.api.account.AccountManager;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.NetworkType;
import com.telus.cmb.account.informationhelper.dao.SubscriberDao;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.ServiceSubscriberCountInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo.DataSharingResultInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo.DataSharingSubscriberInfo;

public class SubscriberDaoImpl extends MultipleJdbcDaoTemplateSupport implements SubscriberDao {

	protected static final Logger LOGGER = Logger.getLogger(SubscriberDaoImpl.class); 

	@Override
	public Collection<String> retrieveActiveSubscriberPhoneNumbers( int ban,
			 int maximum) {				
		return retrieveSubscriberPhoneNumbersByStatus(ban, 'A', maximum);
	}

	@Override
	public Collection<String> retrieveSuspendedSubscriberPhoneNumbers(int ban,
			int maximum) {	
		return retrieveSubscriberPhoneNumbersByStatus(ban, 'S', maximum);
	}


	/**
	 * This method is for IDEN only. Improper method name. TO DO: Rename method or change the query to support what the method name 
	 * truly suggests.
	 */
	@Override
	public Map<String, String> retrievePhoneNumbersForBAN(final int ban) {
		String call = "{call ra_utility_pkg.getphonenumbersbyban(?,?) }";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<Map<String, String>>() {

			@Override
			public Map<String, String> doInCallableStatement(
					CallableStatement callable) throws SQLException,
					DataAccessException {
				Map<String, String> phones = new HashMap<String, String>(50);

				ResultSet result = null;
				try {
					callable.setInt(1,ban);
					callable.registerOutParameter(2,OracleTypes.CURSOR);
					callable.execute();
					result = (ResultSet)callable.getObject(2);

					while (result.next()) {
						phones.put(result.getString(1),result.getString(2));
					}
				} finally {
					if (result != null) {
						result.close();
					}
				}

				return phones;
			}

		});
	}

	@Override
	public int retrieveAttachedSubscribersCount(final int ban,
			final FleetIdentityInfo fleetIdentityInfo) {
		String callString = "{ call fleet_utility_pkg.GetNumberAttachedSubscribers (?,?,?,?) }";

		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Integer>() {

			@Override
			public Integer doInCallableStatement(CallableStatement stmt)
			throws SQLException, DataAccessException {

				stmt.setInt(1,fleetIdentityInfo.getUrbanId());
				stmt.setInt(2,fleetIdentityInfo.getFleetId());
				stmt.setInt(3,ban);
				stmt.registerOutParameter(4,Types.NUMERIC);
				stmt.execute();

				return stmt.getInt(4);
			}
		});
	}

	@Override
	public Collection<ProductSubscriberListInfo> retrieveProductSubscriberLists(final int ban) {
		
		if (AppConfiguration.isWRPPh3Rollback()) {
			String callString = "{? = call SUBSCRIBER_PKG.GetProductSubscriberLists(?, ?, ?)}";

			return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Collection<ProductSubscriberListInfo>>() {
				@Override
				public Collection<ProductSubscriberListInfo> doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
					Collection<ProductSubscriberListInfo> productSubscriberLists = new ArrayList<ProductSubscriberListInfo>();
					ResultSet result = null;
					try {
						// set/register input/output parameters
						callable.registerOutParameter(1, OracleTypes.NUMBER);
						callable.setInt(2, ban);
						callable.registerOutParameter(3, OracleTypes.CURSOR);
						callable.registerOutParameter(4, OracleTypes.VARCHAR);
						callable.setFetchSize(100);

						callable.execute();

						boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

						if (success) {
							HashMap<String, HashMap<String, List<SubscriberIdentifierInfo>>> productSubscriberListMap = new HashMap<String, HashMap<String, List<SubscriberIdentifierInfo>>>();
							result = (ResultSet) callable.getObject(3);

							/* Retrieve result and create SubscriberIdentifier list */
							while (result.next()) {
								String newProductType = result.getString(1);
								String subStatus = result.getString(2);

								SubscriberIdentifierInfo subIdentifierInfo = new SubscriberIdentifierInfo();
								subIdentifierInfo.setSubscriberId(result.getString(3));
								subIdentifierInfo.setSubscriptionId(result.getLong(4));
								subIdentifierInfo.setBrandId(result.getInt(5));
								subIdentifierInfo.setSeatType(result.getString(6));
								subIdentifierInfo.setSeatGroup(result.getString(7));

								if (newProductType != null && subStatus != null) {
									HashMap<String, List<SubscriberIdentifierInfo>> subIdentifierMap = productSubscriberListMap.get(newProductType);

									if (subIdentifierMap == null) {
										subIdentifierMap = new HashMap<String, List<SubscriberIdentifierInfo>>();
										productSubscriberListMap.put(newProductType, subIdentifierMap);
									}

									List<SubscriberIdentifierInfo> subIdentifierList = subIdentifierMap.get(String.valueOf(subStatus.charAt(0)));

									if (subIdentifierList == null) {
										subIdentifierList = new ArrayList<SubscriberIdentifierInfo>();
										subIdentifierMap.put(String.valueOf(subStatus.charAt(0)), subIdentifierList);
									}

									subIdentifierList.add(subIdentifierInfo);
								} else {
									LOGGER.error("ban=" + ban + ",newProductType=" + newProductType + ",subStatus=" + subStatus);
								}
							}

							/* Populate data into product subscriber list */

							for (String productType : productSubscriberListMap.keySet()) {
								ProductSubscriberListInfo productSubscriberList = new ProductSubscriberListInfo();
								productSubscriberList.setProductType(productType);
								HashMap<String, List<SubscriberIdentifierInfo>> subIdentifierMap = productSubscriberListMap.get(productType);
								if (subIdentifierMap != null) {
									for (String subStatus : subIdentifierMap.keySet()) {
										List<SubscriberIdentifierInfo> subIdentifierList = subIdentifierMap.get(subStatus);
										if (subIdentifierList != null) {
											SubscriberIdentifierInfo[] subscriberIdentifierInfoArray = (SubscriberIdentifierInfo[]) subIdentifierList.toArray(new SubscriberIdentifierInfo[subIdentifierList.size()]);
											switch (subStatus.charAt(0)) {
											case Subscriber.STATUS_ACTIVE:
												productSubscriberList.setActiveSubscriberIdentifiers(subscriberIdentifierInfoArray);
												break;
											case Subscriber.STATUS_CANCELED:
												productSubscriberList.setCancelledSubscriberIdentifiers(subscriberIdentifierInfoArray);
												break;
											case Subscriber.STATUS_RESERVED:
												productSubscriberList.setReservedSubscriberIdentifiers(subscriberIdentifierInfoArray);
												break;
											case Subscriber.STATUS_SUSPENDED:
												productSubscriberList.setSuspendedSubscriberIdentifiers(subscriberIdentifierInfoArray);
												break;
											}
										}
									}
								}
								productSubscriberLists.add(productSubscriberList);
							}
						} else {
							LOGGER.debug("Stored procedure failed: " + callable.getString(4));
						}
					} finally {
						if (result != null) {
							result.close();
						}
					}

					return productSubscriberLists;
				}
			});
		} else {
			String sql = "SELECT product_type, sub_status, subscriber_no, NVL(external_id, 0) external_id, brand_id, seat_type, seat_group " + " FROM subscriber WHERE customer_id = :ban AND sub_status IN ('A', 'S', 'C') "
					+ " UNION SELECT s.product_type, s.sub_status, s.subscriber_no, NVL(s.external_id, 0) external_id, s.brand_id,s.seat_type, s.seat_group "
					+ " FROM subscriber s, physical_device p WHERE s.customer_id = :ban AND s.sub_status = 'R' AND p.customer_id = s.customer_id AND p.subscriber_no = s.subscriber_no "
					+ " AND p.product_type = s.product_type AND p.esn_level = 1 AND p.expiration_date IS NULL ORDER BY product_type, sub_status ";

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("ban", ban);

			return (Collection<ProductSubscriberListInfo>) getKnowbilityNamedParameterJdbcTemplate().query(sql, namedParameters, new ResultSetExtractor<Collection<ProductSubscriberListInfo>>() {
				@Override
				public Collection<ProductSubscriberListInfo> extractData(ResultSet result) throws SQLException {
					
					Collection<ProductSubscriberListInfo> productSubscriberLists = new ArrayList<ProductSubscriberListInfo>();

					try {
						HashMap<String, HashMap<String, List<SubscriberIdentifierInfo>>> productSubscriberListMap = new HashMap<String, HashMap<String, List<SubscriberIdentifierInfo>>>();

						/* Retrieve result and create SubscriberIdentifier list */
						while (result.next()) {
							String newProductType = result.getString(1);
							String subStatus = result.getString(2);

							SubscriberIdentifierInfo subIdentifierInfo = new SubscriberIdentifierInfo();
							subIdentifierInfo.setSubscriberId(result.getString(3));
							subIdentifierInfo.setSubscriptionId(result.getLong(4));
							subIdentifierInfo.setBrandId(result.getInt(5));
							subIdentifierInfo.setSeatType(result.getString(6));
							subIdentifierInfo.setSeatGroup(result.getString(7));

							if (newProductType != null && subStatus != null) {
								HashMap<String, List<SubscriberIdentifierInfo>> subIdentifierMap = productSubscriberListMap.get(newProductType);

								if (subIdentifierMap == null) {
									subIdentifierMap = new HashMap<String, List<SubscriberIdentifierInfo>>();
									productSubscriberListMap.put(newProductType, subIdentifierMap);
								}

								List<SubscriberIdentifierInfo> subIdentifierList = subIdentifierMap.get(String.valueOf(subStatus.charAt(0)));

								if (subIdentifierList == null) {
									subIdentifierList = new ArrayList<SubscriberIdentifierInfo>();
									subIdentifierMap.put(String.valueOf(subStatus.charAt(0)), subIdentifierList);
								}

								subIdentifierList.add(subIdentifierInfo);
							} else {
								LOGGER.error("ban = " + ban + ", newProductType = " + newProductType + ", subStatus = " + subStatus);
							}
						}

						/* Populate data into product subscriber list */

						for (String productType : productSubscriberListMap.keySet()) {
							ProductSubscriberListInfo productSubscriberList = new ProductSubscriberListInfo();
							productSubscriberList.setProductType(productType);
							HashMap<String, List<SubscriberIdentifierInfo>> subIdentifierMap = productSubscriberListMap.get(productType);
							if (subIdentifierMap != null) {
								for (String subStatus : subIdentifierMap.keySet()) {
									List<SubscriberIdentifierInfo> subIdentifierList = subIdentifierMap.get(subStatus);
									if (subIdentifierList != null) {
										SubscriberIdentifierInfo[] subscriberIdentifierInfoArray = (SubscriberIdentifierInfo[]) subIdentifierList.toArray(new SubscriberIdentifierInfo[subIdentifierList.size()]);
										switch (subStatus.charAt(0)) {
										case Subscriber.STATUS_ACTIVE:
											productSubscriberList.setActiveSubscriberIdentifiers(subscriberIdentifierInfoArray);
											break;
										case Subscriber.STATUS_CANCELED:
											productSubscriberList.setCancelledSubscriberIdentifiers(subscriberIdentifierInfoArray);
											break;
										case Subscriber.STATUS_RESERVED:
											productSubscriberList.setReservedSubscriberIdentifiers(subscriberIdentifierInfoArray);
											break;
										case Subscriber.STATUS_SUSPENDED:
											productSubscriberList.setSuspendedSubscriberIdentifiers(subscriberIdentifierInfoArray);
											break;
										}
									}
								}
							}
							productSubscriberLists.add(productSubscriberList);
						}
					} finally {
						if (result != null) {
							result.close();
						}
					}
					return productSubscriberLists;
				};
			});
		}
	}

	@Override
	public List<String> retrieveSubscriberIdsByStatus(final int banId, final char status, final int maximum){

		String callString = "{ call SUBSCRIBER_PKG.GetBanSubIdsBySubStatus(?, ?, ?, ?) }";
		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<List<String>>() {
			@Override
			public List<String> doInCallableStatement(
					CallableStatement callable) throws SQLException,
					DataAccessException {
				ResultSet result = null;
				List<String> subIds = new ArrayList<String>();

				try{
					callable.setInt(1, banId);
					callable.setString(2, String.valueOf(status));
					callable.setInt(3, maximum);
					callable.registerOutParameter(4, OracleTypes.CURSOR);
					callable.execute();
					result = (ResultSet) callable.getObject(4);
					while (result.next()) {
						subIds.add(result.getString(1));
					}
					return subIds;  

				}finally {
					if (result != null ){
						result.close();
					}
				}
			}

		});
	}

	@Override
	public List<String> retrieveSubscriberPhoneNumbersByStatus(final int banId, final char status, final int maximum){

		String callString = "{ call SUBSCRIBER_PKG.GetBanSubPhoneNosBySubStatus(?, ?, ?, ?) }";
		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<List<String>>() {
			@Override
			public List<String> doInCallableStatement(
					CallableStatement callable) throws SQLException,
					DataAccessException {
				ResultSet result = null;
				List<String> phoneList = new ArrayList<String>();

				try{
					callable.setInt(1, banId);
					callable.setString(2, String.valueOf(status));
					callable.setInt(3, maximum);
					callable.registerOutParameter(4, OracleTypes.CURSOR);
					callable.execute();
					result = (ResultSet) callable.getObject(4);
					while (result.next()) {
						phoneList.add(result.getString(1));
					}  
					return phoneList;  
				}finally {
					if (result != null ){
						result.close();
					}
				}
			}

		});

	}

	@Override
	public boolean isFeatureCategoryExistOnSubscribers(final int ban,
			final String pCategoryCode) {
		String call = "{ call ra_utility_pkg.isFeatureCategoryExist(?,?,?) }";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<Boolean>() {

			@Override
			public Boolean doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				boolean categoryExist ;
				callable.setInt(1, ban);
				callable.setString(2,pCategoryCode);
				callable.registerOutParameter(3, OracleTypes.NUMBER);

				callable.execute();

				categoryExist = callable.getInt(3) > 0 ? true:false;

				return categoryExist;

			}
		});
	}

	@Override
	public String retrieveHotlinedSubscriberPhoneNumber(final int ban) {
		String call = "{ ? = call subscriber_pkg.getHotlinedPhoneNoByBan(?) }";
		
		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				callable.registerOutParameter(1, OracleTypes.VARCHAR);
				callable.setInt(2, ban);		
				callable.execute();
				
				String phoneNumber = callable.getString(1);
				return (phoneNumber != null ? phoneNumber : "");
			}
		});
	}

	@Override
	public HashMap<String,Integer> retrievePCSNetworkCountByBan(final int ban) {

		String call = " { call ra_utility_pkg.getPCSNetworkCountByBAN(?, ?) }";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<HashMap<String,Integer>>() {

			@Override
			public HashMap<String,Integer> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {

				HashMap<String,Integer> mapNetworkCount = new HashMap<String,Integer>();
				ResultSet result = null;
				try{
					callable.setInt(1, ban);
					callable.registerOutParameter(2, OracleTypes.CURSOR);

					callable.execute();

					result = (ResultSet) callable.getObject(2);
					int hspa = 0;
					int cdma = 0;

					while (result.next()) {
						mapNetworkCount.put(NetworkType.NETWORK_TYPE_HSPA + "_" + result.getString("SUB_STATUS"), new Integer(result.getInt("hspa")));
						mapNetworkCount.put(NetworkType.NETWORK_TYPE_CDMA + "_" + result.getString("SUB_STATUS"), new Integer(result.getInt("cdma")));
						hspa += result.getInt("hspa");
						cdma += result.getInt("cdma");
					}

					mapNetworkCount.put(NetworkType.NETWORK_TYPE_HSPA, new Integer(hspa));
					mapNetworkCount.put(NetworkType.NETWORK_TYPE_CDMA, new Integer(cdma));
					return mapNetworkCount;
				} finally {
					if (result != null ){
						result.close();
					}
				}
			}
		});
	}

	public List<PoolingPricePlanSubscriberCountInfo> retrievePoolingPricePlanSubscriberCounts(
			final int banId, final int poolGroupId, final boolean zeroMinute){

		String sql1="{? = call RA_UTILITY_PKG.getzeropoolingsubscribercounts(?, ?, ?, ?)}";
		String sql2="{? = call RA_UTILITY_PKG.getminutepoolingsubcounts(?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute((zeroMinute)?sql1:sql2, new CallableStatementCallback<List<PoolingPricePlanSubscriberCountInfo>>(){

			@Override
			public List<PoolingPricePlanSubscriberCountInfo> doInCallableStatement(
					CallableStatement callstmt) throws SQLException,
					DataAccessException {
				ResultSet result=null;
				Collection<PricePlanSubscriberCountInfo> pricePlanSubscriberCountInfoList = new ArrayList<PricePlanSubscriberCountInfo>();
				List<PoolingPricePlanSubscriberCountInfo> poolingPricePlanSubscriberCountList = new ArrayList<PoolingPricePlanSubscriberCountInfo>();
				HashMap<String,Object> subscriberCountInfoMap = new HashMap<String,Object>();

				PoolingPricePlanSubscriberCountInfo  poolingPricePlanSubscriberCountInfo  = new PoolingPricePlanSubscriberCountInfo();

				String code = "^";
				int id = -1;

				try {
					callstmt.registerOutParameter(1, OracleTypes.NUMBER);
					callstmt.setInt(2, banId);		 
					callstmt.setInt(3, poolGroupId);
					callstmt.registerOutParameter(4, OracleTypes.CURSOR);
					callstmt.registerOutParameter(5, OracleTypes.VARCHAR);

					callstmt.execute();

					boolean success = callstmt.getInt(1) == AccountManager.NUMERIC_TRUE;
					if (success) {
						result = (ResultSet)callstmt.getObject(4);	    	  

						//initialize the subscriberCountInfoMap
						subscriberCountInfoMap.put("info", new PricePlanSubscriberCountInfo());
						subscriberCountInfoMap.put("active", new ArrayList<String>());
						subscriberCountInfoMap.put("reserved", new ArrayList<String>());
						subscriberCountInfoMap.put("cancelled", new ArrayList<String>());
						subscriberCountInfoMap.put("suspended", new ArrayList<String>());
						subscriberCountInfoMap.put("future", new ArrayList<String>());

						while (result.next()) {
							if (id != result.getInt(7)) {
								if (id != -1) {				  
									subscriberCountInfoMap = populatePricePlanSubscriberCount(code, result, subscriberCountInfoMap, pricePlanSubscriberCountInfoList, true);

									code = result.getString(1);	//need to update code in case it has changed

									poolingPricePlanSubscriberCountInfo.setPricePlanSubscriberCount((PricePlanSubscriberCountInfo[])
											pricePlanSubscriberCountInfoList.toArray(
													new PricePlanSubscriberCountInfo[pricePlanSubscriberCountInfoList.size()]));

									poolingPricePlanSubscriberCountList.add(poolingPricePlanSubscriberCountInfo);

									//force reset of PricePlanSubscriberCountInfo object and subcriber count arrays for new Pool group
									pricePlanSubscriberCountInfoList = new ArrayList<PricePlanSubscriberCountInfo>();
									poolingPricePlanSubscriberCountInfo  = new PoolingPricePlanSubscriberCountInfo();
									subscriberCountInfoMap.put("info", new PricePlanSubscriberCountInfo());
									subscriberCountInfoMap.put("active", new ArrayList<String>());
									subscriberCountInfoMap.put("reserved", new ArrayList<String>());
									subscriberCountInfoMap.put("cancelled", new ArrayList<String>());
									subscriberCountInfoMap.put("suspended", new ArrayList<String>());
									subscriberCountInfoMap.put("future", new ArrayList<String>());
								}		        		
							}

							poolingPricePlanSubscriberCountInfo.setPoolingGroupId(result.getInt(7));  //set Pooling Group ID

							subscriberCountInfoMap = populatePricePlanSubscriberCount(code, result, subscriberCountInfoMap, pricePlanSubscriberCountInfoList, false);

							code = result.getString(1);	//need to update code in case it has changed
							id = result.getInt(7);		//ensure poolGroupId variable to contain the latest pool group id
						}

						if (id != -1 && poolingPricePlanSubscriberCountInfo != null) {
							populateLastPricePlanSubscriberCount(code, subscriberCountInfoMap, pricePlanSubscriberCountInfoList);

							poolingPricePlanSubscriberCountInfo.setPricePlanSubscriberCount((PricePlanSubscriberCountInfo[])
									pricePlanSubscriberCountInfoList.toArray(
											new PricePlanSubscriberCountInfo[pricePlanSubscriberCountInfoList.size()]));
							poolingPricePlanSubscriberCountList.add(poolingPricePlanSubscriberCountInfo);
						}  
					} 

				} finally {
					if (result != null ){
						result.close();
					}
				}


				return poolingPricePlanSubscriberCountList;
			}

		});
	}

	private HashMap<String,Object> populatePricePlanSubscriberCount(String code, 
			ResultSet result,
			HashMap<String,Object> subscriberCountInfoMap,
			Collection<PricePlanSubscriberCountInfo> pricePlanSubscriberCountList,
			boolean newPoolGroup) throws SQLException {  
		//NOTE: newPoolGroup boolean parameter added to address problem where subscriber is lost 
		//      when Price Plans are the same but pool group Ids are different. 
		//	  (only to be set to true for pooling scenarios where the pool group id has changed)

		PricePlanSubscriberCountInfo pricePlanSubscriberCountInfo = (PricePlanSubscriberCountInfo)subscriberCountInfoMap.get("info");
		Collection<String> activeSubscribers = (Collection) subscriberCountInfoMap.get("active");
		Collection<String> reservedSubscribers = (Collection)subscriberCountInfoMap.get("reserved");
		Collection<String> cancelledSubscribers = (Collection)subscriberCountInfoMap.get("cancelled");
		Collection<String> suspendedSubscribers = (Collection)subscriberCountInfoMap.get("suspended");
		Collection<String> futureSubscribers = (Collection)subscriberCountInfoMap.get("future");

		if (!code.equals(result.getString(1)) || newPoolGroup) {
			if (!code.equals("^")) {
				pricePlanSubscriberCountInfo.setActiveSubscribers((String[])activeSubscribers.toArray(new String[activeSubscribers.size()]));
				pricePlanSubscriberCountInfo.setReservedSubscribers((String[])reservedSubscribers.toArray(new String[reservedSubscribers.size()]));
				pricePlanSubscriberCountInfo.setCanceledSubscribers((String[])cancelledSubscribers.toArray(new String[cancelledSubscribers.size()]));
				pricePlanSubscriberCountInfo.setSuspendedSubscribers((String[])suspendedSubscribers.toArray(new String[suspendedSubscribers.size()]));
				pricePlanSubscriberCountInfo.setFutureDatedSubscribers((String[])futureSubscribers.toArray(new String[futureSubscribers.size()]));
				pricePlanSubscriberCountList.add(pricePlanSubscriberCountInfo);

				pricePlanSubscriberCountInfo = new PricePlanSubscriberCountInfo();
				activeSubscribers = new ArrayList<String>();
				reservedSubscribers = new ArrayList<String>();
				cancelledSubscribers = new ArrayList<String>();
				suspendedSubscribers = new ArrayList<String>();
				futureSubscribers = new ArrayList<String>();
			}
		}

		pricePlanSubscriberCountInfo.setPricePlanCode(result.getString(1));
		pricePlanSubscriberCountInfo.setMaximumSubscriberReached(false);
		if (result.getString(3).equals(String.valueOf(Subscriber.STATUS_ACTIVE))&&(result.getDate(5).compareTo(new java.util.Date()) <= 0))
			activeSubscribers.add(result.getString(2));
		else if ((result.getString(3).equals(String.valueOf(Subscriber.STATUS_ACTIVE)) || result.getString(3).equals(String.valueOf(Subscriber.STATUS_RESERVED)))
				&& (result.getDate(5).compareTo(new java.util.Date()) > 0))
			futureSubscribers.add(result.getString(2));
		else if (result.getString(3).equals(String.valueOf(Subscriber.STATUS_RESERVED)) && (result.getDate(5).compareTo(new java.util.Date()) <= 0))
			reservedSubscribers.add(result.getString(2));
		else if (result.getString(3).equals(String.valueOf(Subscriber.STATUS_CANCELED)))
			cancelledSubscribers.add(result.getString(2));
		else if (result.getString(3).equals(String.valueOf(Subscriber.STATUS_SUSPENDED))&& (result.getDate(5).compareTo(new java.util.Date()) <= 0))
			suspendedSubscribers.add(result.getString(2));

		subscriberCountInfoMap.put("info", pricePlanSubscriberCountInfo);		  
		subscriberCountInfoMap.put("active", activeSubscribers);
		subscriberCountInfoMap.put("reserved", reservedSubscribers);
		subscriberCountInfoMap.put("cancelled", cancelledSubscribers);
		subscriberCountInfoMap.put("suspended", suspendedSubscribers);
		subscriberCountInfoMap.put("future", futureSubscribers);
		return subscriberCountInfoMap;
	}

	private void populateLastPricePlanSubscriberCount(String code, HashMap<String,Object> subscriberCountInfoMap, Collection<PricePlanSubscriberCountInfo> pricePlanSubscriberCountList) {

		PricePlanSubscriberCountInfo pricePlanSubscriberCountInfo = (PricePlanSubscriberCountInfo)subscriberCountInfoMap.get("info");
		Collection<String> activeSubscribers = (Collection)subscriberCountInfoMap.get("active");
		Collection<String> reservedSubscribers = (Collection)subscriberCountInfoMap.get("reserved");
		Collection<String> cancelledSubscribers = (Collection)subscriberCountInfoMap.get("cancelled");
		Collection<String> suspendedSubscribers = (Collection)subscriberCountInfoMap.get("suspended");
		Collection<String> futureSubscribers = (Collection)subscriberCountInfoMap.get("future");

		if (!code.equals("^")) {
			pricePlanSubscriberCountInfo.setActiveSubscribers((String[])activeSubscribers.toArray(new String[activeSubscribers.size()]));
			pricePlanSubscriberCountInfo.setReservedSubscribers((String[])reservedSubscribers.toArray(new String[reservedSubscribers.size()]));
			pricePlanSubscriberCountInfo.setCanceledSubscribers((String[])cancelledSubscribers.toArray(new String[cancelledSubscribers.size()]));
			pricePlanSubscriberCountInfo.setSuspendedSubscribers((String[])suspendedSubscribers.toArray(new String[suspendedSubscribers.size()]));
			pricePlanSubscriberCountInfo.setFutureDatedSubscribers((String[])futureSubscribers.toArray(new String[futureSubscribers.size()]));
			pricePlanSubscriberCountList.add(pricePlanSubscriberCountInfo);
		}
	}

	@Override
	public List<ServiceSubscriberCount> retrieveServiceSubscriberCounts(
			final int banId, final String[] serviceCodes, final boolean includeExpired) {

		String sql="{? = call RA_UTILITY_PKG.GetServiceSubscriberCounts(?, ?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<ServiceSubscriberCount>>(){

			@Override
			public List<ServiceSubscriberCount> doInCallableStatement(
					CallableStatement callstmt) throws SQLException,
					DataAccessException {


				ResultSet result = null;
				List<ServiceSubscriberCount> serviceSubscriberCountList = new ArrayList<ServiceSubscriberCount>();
				Collection<ServiceSubscriberCountInfo> serviceSubscriberCountInfoList = new ArrayList<ServiceSubscriberCountInfo>();
				Collection<String> activeSubscribers = new ArrayList<String>();
				Collection<String> reservedSubscribers = new ArrayList<String>();
				Collection<String> cancelledSubscribers = new ArrayList<String>();
				Collection<String> suspendedSubscribers = new ArrayList<String>();
				ServiceSubscriberCountInfo serviceSubscriberCountInfo = new  ServiceSubscriberCountInfo();
				String code = "^";
				
				try {

					if (serviceCodes != null && serviceCodes.length > 0 && banId != 0 ) {
						// create array descriptor
						ArrayDescriptor serviceCodesArrayDesc = ArrayDescriptor.createDescriptor("T_SERVICE_CODES", callstmt.getConnection());

						// create Oracle array of serviceCodes
						ARRAY serviceCodesArray = new ARRAY(serviceCodesArrayDesc, callstmt.getConnection(), serviceCodes);

						// set/register input/output parameters
						callstmt.registerOutParameter(1, OracleTypes.NUMBER);
						callstmt.setArray(2, serviceCodesArray);

						if (includeExpired)
							callstmt.setInt(3, AccountManager.NUMERIC_TRUE);
						else
							callstmt.setInt(3, AccountManager.NUMERIC_FALSE);
						callstmt.setInt(4, banId);
						callstmt.registerOutParameter(5, OracleTypes.CURSOR);
						callstmt.registerOutParameter(6, OracleTypes.VARCHAR);
						callstmt.execute();

						boolean success = callstmt.getInt(1) == AccountManager.NUMERIC_TRUE;

						if (success) {
							result = (ResultSet) callstmt.getObject(5);

							while (result.next()) {
								if (!code.equals(result.getString(1))) {
									if (!code.equals("^")) {
										serviceSubscriberCountInfo.setActiveSubscribers((String[])activeSubscribers.toArray(new String[activeSubscribers.size()]));
										serviceSubscriberCountInfo.setReservedSubscribers((String[])reservedSubscribers.toArray(new String[reservedSubscribers.size()]));
										serviceSubscriberCountInfo.setSuspendedSubscribers((String[])suspendedSubscribers.toArray(new String[suspendedSubscribers.size()]));
										serviceSubscriberCountInfoList.add(serviceSubscriberCountInfo);
										serviceSubscriberCountInfo  = new  ServiceSubscriberCountInfo();
										activeSubscribers = new ArrayList<String>();
										reservedSubscribers = new ArrayList<String>();
										cancelledSubscribers = new ArrayList<String>();
										suspendedSubscribers = new ArrayList<String>();
									}
								}
								serviceSubscriberCountInfo.setServiceCode(result.getString(1));
								if (result.getString(3).equals(String.valueOf(Subscriber.STATUS_ACTIVE)))
								{activeSubscribers.add(result.getString(2));
								}
								else if (result.getString(3).equals(String.valueOf(Subscriber.STATUS_RESERVED)))
								{reservedSubscribers.add(result.getString(2));
								}
								else if (result.getString(3).equals(String.valueOf(Subscriber.STATUS_SUSPENDED)))
								{suspendedSubscribers.add(result.getString(2));
								}
								else if (result.getString(3).equals(String.valueOf(Subscriber.STATUS_CANCELED)))
								{cancelledSubscribers.add(result.getString(2));
								}
								code=result.getString(1);
							}
							if (!code.equals("^"))
							{
								serviceSubscriberCountInfo.setActiveSubscribers((String[])activeSubscribers.toArray(new String[activeSubscribers.size()]));
								serviceSubscriberCountInfo.setReservedSubscribers((String[])reservedSubscribers.toArray(new String[reservedSubscribers.size()]));
								serviceSubscriberCountInfo.setSuspendedSubscribers((String[])suspendedSubscribers.toArray(new String[suspendedSubscribers.size()]));
								serviceSubscriberCountInfo.setCanceledSubscribers((String[])cancelledSubscribers.toArray(new String[cancelledSubscribers.size()]));

								serviceSubscriberCountInfoList.add(serviceSubscriberCountInfo);
							}
							serviceSubscriberCountList.addAll(serviceSubscriberCountInfoList);
						}
					} else {
						LOGGER.debug("Array of Service Codes is either null or empty, or ban is 0.");
					}
				}finally {
					if (result != null ){
						result.close();
					}
				}

				return serviceSubscriberCountList;
			}

		});
	}

	@Override
	public List<PricePlanSubscriberCount> retrieveMinutePoolingEnabledPricePlanSubscriberCounts(
			final int banId) {

		String sql="{? = call RA_UTILITY_PKG.getminutepoolingsubscribers(?, ?, ?)}";
		return super.getKnowbilityJdbcTemplate().execute(sql, new RetrieveMinutePollingCallback(banId));
	}

	@Override
	public List<PricePlanSubscriberCount> retrieveMinutePoolingEnabledPricePlanSubscriberCounts(
			final int banId, final String[] poolingCoverageTypes) {

		String sql="{? = call RA_UTILITY_PKG.getminutepoolingsubsbycoverage(?, ?, ?, ?)}";
		return super.getKnowbilityJdbcTemplate().execute(sql, new RetrieveMinutePollingCallback(banId, poolingCoverageTypes));
	}


	private class RetrieveMinutePollingCallback implements CallableStatementCallback<List<PricePlanSubscriberCount>>{

		private int banId;
		private String[] poolingCoverageTypes=null;

		public RetrieveMinutePollingCallback(int banId, String[] poolingCoverageTypes){
			this.banId=banId;
			this.poolingCoverageTypes=poolingCoverageTypes;
		}

		public RetrieveMinutePollingCallback(int banId){
			this.banId=banId;
		}

		@Override
		public List<PricePlanSubscriberCount> doInCallableStatement(
				CallableStatement callstmt) throws SQLException,
				DataAccessException {

			ResultSet result = null;
			List<PricePlanSubscriberCount> pricePlanSubscriberCountList= new ArrayList<PricePlanSubscriberCount>();
			Collection<PricePlanSubscriberCountInfo> pricePlanSubscriberCountInfoList = new ArrayList<PricePlanSubscriberCountInfo>();
			HashMap<String,Object> subscriberCountInfoMap = new HashMap<String,Object>();
			String code = "^";

			try {
				callstmt.registerOutParameter(1, OracleTypes.NUMBER);
				callstmt.setInt(2, banId);

				if (poolingCoverageTypes != null) {

					// create array descriptor
					ArrayDescriptor coverageTypesArrayDesc = ArrayDescriptor.createDescriptor("T_PARAMETER_NAME_ARRAY", callstmt.getConnection());

					// create Oracle array of coverage types
					ARRAY coverageTypesArray = new ARRAY(coverageTypesArrayDesc, callstmt.getConnection(), poolingCoverageTypes);

					// set/register input/output parameters
					callstmt.setArray(3, coverageTypesArray);
					callstmt.registerOutParameter(4, OracleTypes.CURSOR);
					callstmt.registerOutParameter(5, OracleTypes.VARCHAR);

				} else {

					// set/register input/output parameters
					callstmt.registerOutParameter(3, OracleTypes.CURSOR);
					callstmt.registerOutParameter(4, OracleTypes.VARCHAR);
				}
				callstmt.execute();

				boolean success = callstmt.getInt(1) == AccountManager.NUMERIC_TRUE;
				if (success) {

					//initialize the statusCountMap
					subscriberCountInfoMap.put("info", new PricePlanSubscriberCountInfo());
					subscriberCountInfoMap.put("active", new ArrayList<String>());
					subscriberCountInfoMap.put("reserved", new ArrayList<String>());
					subscriberCountInfoMap.put("cancelled", new ArrayList<String>());
					subscriberCountInfoMap.put("suspended", new ArrayList<String>());
					subscriberCountInfoMap.put("future", new ArrayList<String>());

					result = poolingCoverageTypes != null ? (ResultSet)callstmt.getObject(4) : (ResultSet)callstmt.getObject(3);

					while (result.next()){
						subscriberCountInfoMap = populatePricePlanSubscriberCount(code, result, subscriberCountInfoMap, pricePlanSubscriberCountInfoList, false);
						code = result.getString(1);	//need to update code in case it has changed
					}

					populateLastPricePlanSubscriberCount(code, subscriberCountInfoMap, pricePlanSubscriberCountInfoList);
					pricePlanSubscriberCountList.addAll(pricePlanSubscriberCountInfoList);

				}

			}  finally {
				if (result != null ){
					result.close();
				}
			}

			return pricePlanSubscriberCountList;

		}

	}

	@Override
	public List<PricePlanSubscriberCountInfo> retrievePricePlanSubscriberCountInfo(
			final int banId, final String productType) {

		String sql="{? = call RA_UTILITY_PKG.getdollarpoolingsubcounts(?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<PricePlanSubscriberCountInfo>>(){

			@Override
			public List<PricePlanSubscriberCountInfo> doInCallableStatement(
					CallableStatement callstmt) throws SQLException,
					DataAccessException {

				ResultSet result = null;
				Collection<PricePlanSubscriberCountInfo> ppsciList = new ArrayList<PricePlanSubscriberCountInfo>();
				List<PricePlanSubscriberCountInfo> pricePlanSubscriberCountInfoList = new ArrayList<PricePlanSubscriberCountInfo>();
				Collection<PoolingPricePlanSubscriberCountInfo> poolingPricePlanSubscriberCountInfoList = new ArrayList<PoolingPricePlanSubscriberCountInfo>();
				HashMap<String,Object> subscriberCountInfoMap = new HashMap<String,Object>();

				PoolingPricePlanSubscriberCountInfo  poolingPricePlanSubscriberCountInfo  = new PoolingPricePlanSubscriberCountInfo();

				String code = "^";
				int id = -1;

				try {
					// set/register input/output parameters
					callstmt.registerOutParameter(1, OracleTypes.NUMBER);
					callstmt.setInt(2, banId);	
					callstmt.setString(3, productType);
					callstmt.registerOutParameter(4, OracleTypes.CURSOR);
					callstmt.registerOutParameter(5, OracleTypes.VARCHAR);

					callstmt.execute();

					boolean success = callstmt.getInt(1) == AccountManager.NUMERIC_TRUE;
					if (success) {
						result = (ResultSet)callstmt.getObject(4);	    	  

						//initialize the subscriberCountInfoMap
						subscriberCountInfoMap.put("info", new PricePlanSubscriberCountInfo());
						subscriberCountInfoMap.put("active", new ArrayList<String>());
						subscriberCountInfoMap.put("reserved", new ArrayList<String>());
						subscriberCountInfoMap.put("cancelled", new ArrayList<String>());
						subscriberCountInfoMap.put("suspended", new ArrayList<String>());
						subscriberCountInfoMap.put("future", new ArrayList<String>());

						while (result.next()) {
							if (id != result.getInt(7)) {
								if (id != -1) {
									subscriberCountInfoMap = populatePricePlanSubscriberCount(code, result, subscriberCountInfoMap, ppsciList, true);

									code = result.getString(1);	//need to update code in case it has changed

									poolingPricePlanSubscriberCountInfo.setPricePlanSubscriberCount((PricePlanSubscriberCountInfo[])
											ppsciList.toArray(
													new PricePlanSubscriberCountInfo[ppsciList.size()]));

									poolingPricePlanSubscriberCountInfoList.add(poolingPricePlanSubscriberCountInfo);

									//force reset of PricePlanSubscriberCountInfo object and subcriber count arrays for new Pool group
									ppsciList = new ArrayList<PricePlanSubscriberCountInfo>();
									poolingPricePlanSubscriberCountInfo  = new PoolingPricePlanSubscriberCountInfo();
									subscriberCountInfoMap.put("info", new PricePlanSubscriberCountInfo());
									subscriberCountInfoMap.put("active", new ArrayList<String>());
									subscriberCountInfoMap.put("reserved", new ArrayList<String>());
									subscriberCountInfoMap.put("cancelled", new ArrayList<String>());
									subscriberCountInfoMap.put("suspended", new ArrayList<String>());
									subscriberCountInfoMap.put("future", new ArrayList<String>());
								}		        		
							}

							poolingPricePlanSubscriberCountInfo.setPoolingGroupId(result.getInt(7));  //set Pooling Group ID

							subscriberCountInfoMap = populatePricePlanSubscriberCount(code, result, subscriberCountInfoMap, ppsciList, false);

							code = result.getString(1);	//need to update code in case it has changed
							id = result.getInt(7);		//ensure poolGroupId variable to contain the latest pool group id
						}

						if (id != -1 && poolingPricePlanSubscriberCountInfo != null) {
							populateLastPricePlanSubscriberCount(code, subscriberCountInfoMap, ppsciList);

							poolingPricePlanSubscriberCountInfo.setPricePlanSubscriberCount((PricePlanSubscriberCountInfo[])
									ppsciList.toArray(
											new PricePlanSubscriberCountInfo[ppsciList.size()]));
							poolingPricePlanSubscriberCountInfoList.add(poolingPricePlanSubscriberCountInfo);
						}  
					} 

				}finally {
					if (result != null){
						result.close();
					}
				}
				PoolingPricePlanSubscriberCountInfo[] pppsciArray=poolingPricePlanSubscriberCountInfoList.toArray(
						new PoolingPricePlanSubscriberCountInfo[poolingPricePlanSubscriberCountInfoList.size()]);

				if(pppsciArray.length>0){
					pricePlanSubscriberCountInfoList=Arrays.asList((PricePlanSubscriberCountInfo[])pppsciArray[0].getPricePlanSubscriberCount());
				}

				return  pricePlanSubscriberCountInfoList;
			}

		});
	}

	@Override
	public List<PricePlanSubscriberCountInfo> retrieveShareablePricePlanSubscriberCount(
			final int ban) {

		String sql1="{ call ra_utility_pkg.GetSharePricePlanSubscribers (?,?) }";
		String sql2="{ call ra_utility_pkg.GetShareableSOCSubscribers (?,?,?) }";
		PricePlanSubscriberCountInfo[] pricePlanSubscriberCountInfo=new PricePlanSubscriberCountInfo[0];

		pricePlanSubscriberCountInfo=super.getKnowbilityJdbcTemplate().execute(sql1, 
				new CallableStatementCallback<PricePlanSubscriberCountInfo[]>(){

					@Override
					public PricePlanSubscriberCountInfo[] doInCallableStatement(
							CallableStatement callstmt) throws SQLException,
							DataAccessException {

						PricePlanSubscriberCountInfo[] pricePlanSubscriberCountInfoArray = new PricePlanSubscriberCountInfo[0] ;
						Collection<PricePlanSubscriberCountInfo> pricePlanSubscriberCountList = new ArrayList<PricePlanSubscriberCountInfo>();
						HashMap<String,Object> subscriberCountInfoMap = new HashMap<String,Object>();
						ResultSet result = null;
						String code = "^";

						try {
							callstmt.setInt(1, ban);
							callstmt.registerOutParameter(2, ORACLE_REF_CURSOR);
							callstmt.execute();
							result = (ResultSet)callstmt.getObject(2);

							//initialize the statusCountMap
							subscriberCountInfoMap.put("info", new PricePlanSubscriberCountInfo());
							subscriberCountInfoMap.put("active", new ArrayList<String>());
							subscriberCountInfoMap.put("reserved", new ArrayList<String>());
							subscriberCountInfoMap.put("cancelled", new ArrayList<String>());
							subscriberCountInfoMap.put("suspended", new ArrayList<String>());
							subscriberCountInfoMap.put("future", new ArrayList<String>());

							while (result.next()) {
								subscriberCountInfoMap = populatePricePlanSubscriberCount(code, result, subscriberCountInfoMap, pricePlanSubscriberCountList, false);
								code = result.getString(1);	
							}

							populateLastPricePlanSubscriberCount(code, subscriberCountInfoMap, pricePlanSubscriberCountList);
							pricePlanSubscriberCountInfoArray = (PricePlanSubscriberCountInfo[])pricePlanSubscriberCountList.toArray(new PricePlanSubscriberCountInfo[pricePlanSubscriberCountList.size()]);
						}finally{
							if(result!=null ){
								result.close();
							}
						}
						return pricePlanSubscriberCountInfoArray;
					}

				});

		final PricePlanSubscriberCountInfo[] pricePlanSubscriberCountInfo1=pricePlanSubscriberCountInfo;

		return super.getKnowbilityJdbcTemplate().execute(sql2, new CallableStatementCallback<List<PricePlanSubscriberCountInfo>>(){

			@Override
			public List<PricePlanSubscriberCountInfo> doInCallableStatement(
					CallableStatement callstmt) throws SQLException,
					DataAccessException {

				PricePlanSubscriberCountInfo[] pricePlanSubscriberCountInfoArray = pricePlanSubscriberCountInfo1 ;
				ServiceSubscriberCountInfo[] serviceSubscriberCountArray = new ServiceSubscriberCountInfo[0] ;
				Collection<ServiceSubscriberCountInfo> serviceSubscriberCountInfoList = new ArrayList<ServiceSubscriberCountInfo>();
				Collection<String> activeSubscribers = new ArrayList<String>();
				Collection<String> reservedSubscribers = new ArrayList<String>();
				Collection<String> suspendedSubscribers = new ArrayList<String>();
				ServiceSubscriberCountInfo serviceSubscriberCountInfo = new ServiceSubscriberCountInfo();
				ResultSet result = null;
				String code = "^";

				try{
					// Retrieve subscribers with shareable services(not incuded in the priceplan) for every shareable priceplan
					code = "^";
					activeSubscribers = new ArrayList<String>();
					reservedSubscribers = new ArrayList<String>();
					suspendedSubscribers = new ArrayList<String>();

					callstmt.setInt(1, ban);

					for (int j=0; j < pricePlanSubscriberCountInfoArray.length; j++) {
						callstmt.setString(2,pricePlanSubscriberCountInfoArray[j].getPricePlanCode());
						callstmt.registerOutParameter(3, ORACLE_REF_CURSOR);
						callstmt.execute();
						result = (ResultSet)callstmt.getObject(3);
						while (result.next()) {
							if (!code.equals(result.getString(1))) {
								if (!code.equals("^")) {
									serviceSubscriberCountInfo.setActiveSubscribers((String[])activeSubscribers.toArray(new String[activeSubscribers.size()]));
									serviceSubscriberCountInfo.setReservedSubscribers((String[])reservedSubscribers.toArray(new String[reservedSubscribers.size()]));
									serviceSubscriberCountInfo.setSuspendedSubscribers((String[])suspendedSubscribers.toArray(new String[suspendedSubscribers.size()]));
									serviceSubscriberCountInfoList.add(serviceSubscriberCountInfo);
									serviceSubscriberCountInfo  = new  ServiceSubscriberCountInfo();
									activeSubscribers = new ArrayList<String>();
									reservedSubscribers = new ArrayList<String>();
									suspendedSubscribers = new ArrayList<String>();
								}
							}
							serviceSubscriberCountInfo.setServiceCode(result.getString(1));
							if (result.getString(3).equals(String.valueOf(Subscriber.STATUS_ACTIVE))) {
								activeSubscribers.add(result.getString(2));
							} else if (result.getString(3).equals(String.valueOf(Subscriber.STATUS_RESERVED))) {
								reservedSubscribers.add(result.getString(2));
							} else if (result.getString(3).equals(String.valueOf(Subscriber.STATUS_SUSPENDED))) {
								suspendedSubscribers.add(result.getString(2));
							}
							code = result.getString(1);
						}

						if (!code.equals("^")) {
							serviceSubscriberCountInfo.setActiveSubscribers((String[])activeSubscribers.toArray(new String[activeSubscribers.size()]));
							serviceSubscriberCountInfo.setReservedSubscribers((String[])reservedSubscribers.toArray(new String[reservedSubscribers.size()]));
							serviceSubscriberCountInfo.setSuspendedSubscribers((String[])suspendedSubscribers.toArray(new String[suspendedSubscribers.size()]));
							serviceSubscriberCountInfoList.add(serviceSubscriberCountInfo);
						}
						serviceSubscriberCountArray = (ServiceSubscriberCountInfo[])serviceSubscriberCountInfoList.toArray(new ServiceSubscriberCountInfo[serviceSubscriberCountInfoList.size()]);
						pricePlanSubscriberCountInfoArray[j].setServiceSubscriberCounts(serviceSubscriberCountArray);
					}
				}finally{
					if(result!=null){
						result.close();
					}
				}

				return Arrays.asList(pricePlanSubscriberCountInfoArray);
			}

		});
	}

	@Override
	public String[] retrieveSubscriberIdsByServiceFamily(final int banId, final String familyTypeCode, final Date effectiveDate) {
		
		String callString = "{? = call ra_utility_pkg.getSubscribersByServiceFamily(?,?,?,?,?)}";
		
		return getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<String[]>() {

			@Override
			public String[] doInCallableStatement(CallableStatement cs)	throws SQLException, DataAccessException {
				
				ResultSet rs = null;
				try {
					cs.registerOutParameter(1, OracleTypes.NUMBER);
					cs.setInt(2, banId );
					cs.setString(3, familyTypeCode );
					cs.setDate(4, new java.sql.Date( effectiveDate.getTime() ) );
					cs.registerOutParameter(5, OracleTypes.CURSOR);
					cs.registerOutParameter(6, OracleTypes.VARCHAR );
					cs.execute();
					
					List<String> list  = new ArrayList<String>();
					boolean success = cs.getInt(1) == AccountManager.NUMERIC_TRUE;
					if (success) {
						rs = (ResultSet)cs.getObject(5);
						while( rs.next() ) {
							list.add(rs.getString("SUBSCRIBER_NO"));
						}
					}
					return list.toArray( new String[ list.size()] );
				
				} finally {
					if(rs!=null ){
						rs.close();
					}
				}
			}
			
		});
	}

	@Override
	public SubscribersByDataSharingGroupResultInfo[] retrieveSubscribersByDataSharingGroupCodes(final int banId, final String[] dataSharingGroupCodes, final Date effectiveDate) {
		String callString = "{? = call ra_utility_pkg.getSubscribersBySharingGroups(?,?,?,?,?)}";
		
		return getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<SubscribersByDataSharingGroupResultInfo[]>() {

			@Override
			public SubscribersByDataSharingGroupResultInfo[] doInCallableStatement(CallableStatement cs)	throws SQLException, DataAccessException {
				
				ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("T_PARAMETER_NAME_ARRAY", cs.getConnection());

				// create Oracle array of serviceCodes
				ARRAY array = new ARRAY(arrayDescriptor, cs.getConnection(), dataSharingGroupCodes );
				cs.registerOutParameter(1, OracleTypes.NUMBER);
				cs.setInt(2, banId );
				cs.setArray(3, array );
				cs.setDate(4, new java.sql.Date( effectiveDate.getTime() ) );
				cs.registerOutParameter(5, OracleTypes.CURSOR);
				cs.registerOutParameter(6, OracleTypes.VARCHAR );
				cs.execute();
				
				Map<String, SubscribersByDataSharingGroupResultInfo> result   = new HashMap<String, SubscribersByDataSharingGroupResultInfo>();
				boolean success = cs.getInt(1) == AccountManager.NUMERIC_TRUE;
				if (success) {
					ResultSet rs = null;
					try {
						rs = (ResultSet)cs.getObject(5);
						while( rs.next() ) {
							String groupCode = rs.getString("ALLOW_SHARING_GROUP_CD");
							SubscribersByDataSharingGroupResultInfo groupResult = result.get(groupCode);
							if ( groupResult==null ) {
								groupResult = new SubscribersByDataSharingGroupResultInfo();
								groupResult.setDataSharingGroupCode( groupCode );
								result.put(groupCode, groupResult);
							}
							DataSharingSubscriberInfo dssInfo = new DataSharingSubscriberInfo();
							dssInfo.setSubscriberId(rs.getString("SUBSCRIBER_NO"));
							dssInfo.setContributing("C".equals(rs.getString("ALLOW_SHARING_ACCESS_TYPE_CD")));
							groupResult.addDataSharingSubscriber( dssInfo );
						}
					} finally {
						if(rs!=null)rs.close();
					}
				}
				return result.values().toArray( new SubscribersByDataSharingGroupResultInfo[ result.size()] );
			}
			
		});
	}

	@Override
	public String retrieveChangedSubscriber(final int ban,final String subscriberId,final String productType,final Date searchFromDate, final Date searchToDate) throws ApplicationException {

		String sql = "select customer_id, subscriber_no from subscriber s join "+
				"(select prv_ctn from subscriber where customer_id = ? and subscriber_no = ? and product_type =? ) s0 "+
				" on s.subscriber_no=s0.prv_ctn "+
				" where customer_id = ? and product_type = ? and sys_creation_date between ? and ? ";


		final java.sql.Timestamp fromDate = new Timestamp(searchFromDate.getTime());
		final java.sql.Timestamp toDate = new Timestamp(searchToDate.getTime());
		return super.getKnowbilityJdbcTemplate().execute(sql, new PreparedStatementCallback<String>() {
			@Override
			public String doInPreparedStatement(PreparedStatement pstmt)
					throws SQLException, DataAccessException {

				ResultSet rset = null;
				try {
					pstmt.setInt(1, ban);
					pstmt.setString(2, subscriberId);
					pstmt.setString(3, productType);
					pstmt.setInt(4, ban);
					pstmt.setString(5, productType);
					pstmt.setTimestamp(6, fromDate);
					pstmt.setTimestamp(7, toDate);
					rset = pstmt.executeQuery();
					if(rset.next()) {
						return rset.getString("subscriber_no");
					}
				} finally {
					if (rset != null ) {
						rset.close();
					}
				}
				return null;
			}

		});
	}

	@Override
	public Collection<DataSharingResultInfo> retrieveSubscriberDataSharingInfoList(final int banId, final String[] dataSharingGroupCodes) throws ApplicationException {
		LOGGER.debug("BAN [" + banId + "] retrieveSubscriberDataSharingInfoList");
		if (dataSharingGroupCodes != null) {
			for (String dataSharingGroupCodeStr : dataSharingGroupCodes) {
				LOGGER.debug("dataSharingGroupCode = [" + dataSharingGroupCodeStr + "]");
			}
		}
		
		StringBuffer sqlSB = new StringBuffer();
		
		// Construct data sharing group code SQL segment, e.g. " and sgasg1.allow_sharing_group_cd in ('CAD_DATA')"
		StringBuffer dsGroupSB = new StringBuffer();
		dsGroupSB.append(" ");
		dsGroupSB.append(constructSQLInSetStatement(dataSharingGroupCodes, " and sgasg.allow_sharing_group_cd "));
		
		// Construct the main SQL query statement
		sqlSB.append("select");
		if (AppConfiguration.isWRPRollback()) {
			sqlSB.append(" /*+use_hash(prr,sg,sgasg)*/");
		}
		sqlSB.append(" sa.subscriber_no, sa.soc, s.soc_description, s.soc_description_f, sa.service_type, prr.rate, sgasg.allow_sharing_group_cd, sgasg.allow_sharing_access_type_cd");
		sqlSB.append(" from service_agreement sa, pp_rc_rate prr, soc_group sg, soc_grp_allow_sharing_grp sgasg, soc s");
		sqlSB.append(" where sa.ban = ?");
		sqlSB.append(" and ((TRUNC(sa.effective_date) <= TRUNC(sysdate)) and ((TRUNC(sa.expiration_date) > TRUNC(sysdate)) or sa.expiration_date is null))");
		sqlSB.append(" and ((sa.service_type = 'P' and prr.feature_code = 'STD') or sa.service_type not in ('P', 'O'))");
		sqlSB.append(" and prr.soc = sa.soc");
		sqlSB.append(" and sa.soc = sg.soc");
		sqlSB.append(" and sg.gp_soc = sgasg.gp_soc");
		sqlSB.append(" and s.soc = sa.soc");
		sqlSB.append(dsGroupSB.toString());
		sqlSB.append(" union");
		sqlSB.append(" select /*+first_rows*/ DISTINCT sa.subscriber_no, sa.soc, s.soc_description, s.soc_description_f, sa.service_type, prr.rate, null, null");    
		sqlSB.append(" from service_agreement sa, pp_rc_rate prr, soc s"); 
		sqlSB.append(" where sa.subscriber_no in (");
		if (AppConfiguration.isWRPRollback()) {
			sqlSB.append("    select /*+use_hash(sg,sgasg,sa)*/ subscriber_no");
		}else {
			sqlSB.append("    select subscriber_no");
		}
		sqlSB.append("    from service_agreement sa, soc_group sg, soc_grp_allow_sharing_grp sgasg"); 
		sqlSB.append("    where sa.soc = sg.soc"); 
		sqlSB.append("    and sg.gp_soc = sgasg.gp_soc");
		sqlSB.append(dsGroupSB.toString());
		sqlSB.append("    and sa.ban = ?");
		sqlSB.append(" )");
		sqlSB.append(" and sa.ban = ?");
		sqlSB.append(" and ((TRUNC(sa.effective_date) <= TRUNC(sysdate)) and ((TRUNC(sa.expiration_date) > TRUNC(sysdate)) or sa.expiration_date is null))");
		sqlSB.append(" and ((sa.service_type = 'P' and prr.feature_code = 'STD') or sa.service_type not in ('P', 'O'))");
		sqlSB.append(" and sa.soc = prr.soc");
		sqlSB.append(" and sa.soc = s.soc");
		sqlSB.append(" and not exists (select 1 from soc_group sg, soc_grp_allow_sharing_grp sgasg where sg.gp_soc = sgasg.gp_soc and sg.soc = sa.soc)");
		
		return super.getKnowbilityJdbcTemplate().execute(sqlSB.toString(), new PreparedStatementCallback<Collection<DataSharingResultInfo>>() {
			@Override
			public Collection<DataSharingResultInfo> doInPreparedStatement(PreparedStatement pstmt)
					throws SQLException, DataAccessException {

				ResultSet rset = null;
				Collection<DataSharingResultInfo> dataSharingResultInfoList = new ArrayList<DataSharingResultInfo>();
				try {
					pstmt.setInt(1, banId);
					pstmt.setInt(2, banId);
					pstmt.setInt(3, banId);
					rset = pstmt.executeQuery();
					
					while(rset.next()) {
						DataSharingResultInfo dataSharingResultInfo = new DataSharingResultInfo();
						
						dataSharingResultInfo.setSubscriberId(rset.getString("subscriber_no"));
						dataSharingResultInfo.setSocCode(rset.getString("soc"));
						dataSharingResultInfo.setSocDescription(rset.getString("soc_description"));
						dataSharingResultInfo.setSocDescriptionFrench(rset.getString("soc_description_f"));
						dataSharingResultInfo.setServiceType(rset.getString("service_type"));						
						dataSharingResultInfo.setRate(rset.getDouble("rate"));
						dataSharingResultInfo.setAllowSharingGroupCd(rset.getString("allow_sharing_group_cd"));
						dataSharingResultInfo.setAllowSharingAccessTypeCd(rset.getString("allow_sharing_access_type_cd"));
						
						dataSharingResultInfoList.add(dataSharingResultInfo);
					}
					LOGGER.debug("BAN [" + banId + "] retrieveSubscriberDataSharingInfoList exit successfully");
					return dataSharingResultInfoList;
					
				} finally {
					if (rset != null ) {
						rset.close();
					}
				}
			}
		});		
	}

	/*
	 * This method will eventually replace the method above, but will stay here until it's proven (performance and functionality)
	 */
	public Collection<DataSharingResultInfo> retrieveSubscriberDataSharingInfoListUsingSqlPackage(final int banId, final String[] dataSharingGroupCodes) throws ApplicationException {
		
		LOGGER.info("BAN [" + banId + "] retrieveSubscriberDataSharingInfoListUsingSqlPackage");
		if (dataSharingGroupCodes != null) {
			for (String dataSharingGroupCodeStr : dataSharingGroupCodes) {
				LOGGER.info("dataSharingGroupCode = [" + dataSharingGroupCodeStr + "]");
			}
		}
		String callString = "{? = call ra_utility_pkg.getSubscriberDataSharingInfo(?,?,?)}";		
		
		return getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Collection<DataSharingResultInfo>>() {
			@Override
			public Collection<DataSharingResultInfo> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {

				Collection<DataSharingResultInfo> dataSharingResultInfoList = new ArrayList<DataSharingResultInfo>();
				List<String> dataSharingGroupCodeList = dataSharingGroupCodes == null ? new ArrayList<String>() : Arrays.asList(dataSharingGroupCodes);
				ResultSet rs = null;
				
				try {				
					cs.registerOutParameter(1, OracleTypes.NUMBER);
					cs.setInt(2, banId );
					cs.registerOutParameter(3, OracleTypes.CURSOR);
					cs.registerOutParameter(4, OracleTypes.VARCHAR );
					cs.execute();
					
					boolean success = cs.getInt(1) == AccountManager.NUMERIC_TRUE;
					if (success) {
						rs = (ResultSet) cs.getObject(3);
						while (rs.next()) {
							String dataSharingGroupCode = rs.getString("allow_sharing_group_cd");
							if (dataSharingGroupCode == null || dataSharingGroupCodeList.isEmpty() || dataSharingGroupCodeList.contains(dataSharingGroupCode)) {
								DataSharingResultInfo dataSharingResultInfo = new DataSharingResultInfo();
								
								dataSharingResultInfo.setSubscriberId(rs.getString("subscriber_no"));
								dataSharingResultInfo.setSocCode(rs.getString("soc"));
								dataSharingResultInfo.setSocDescription(rs.getString("soc_description"));
								dataSharingResultInfo.setSocDescriptionFrench(rs.getString("soc_description_f"));
								dataSharingResultInfo.setServiceType(rs.getString("service_type"));
								dataSharingResultInfo.setRate(rs.getDouble("rate"));
								dataSharingResultInfo.setAllowSharingGroupCd(dataSharingGroupCode);
								dataSharingResultInfo.setAllowSharingAccessTypeCd(rs.getString("allow_sharing_access_type_cd"));
								
								dataSharingResultInfoList.add(dataSharingResultInfo);	
							}
						}
					}
					
				} finally {
					if (rs != null ) {
						rs.close();
					}
				}
				LOGGER.info("BAN [" + banId + "] retrieveSubscriberDataSharingInfoListUsingSqlPackage exit successfully");
				return dataSharingResultInfoList;
			}
		});
	}
	
	// Construct a SQL segment, e.g. " prefix in ('element1', 'element2')"
	private String constructSQLInSetStatement(String[] elements, String prefix) {
		
		StringBuffer result = new StringBuffer();
		
		if (elements != null) {
			ArrayList<String> trimmedElementList = new ArrayList<String>();
			// First round to take out all empty string in the array
			for (String element : elements) {
				if (element != null && element.trim().length() > 0) {
					trimmedElementList.add(element);
				}
			}
			String[] trimmedElementArray = (String[])trimmedElementList.toArray(new String[trimmedElementList.size()]);
	
			if (trimmedElementArray.length > 0) {
				result.append(" ");
				result.append(prefix);
				result.append(" in (");
				// Second round to construct the SQL segment
				for (int i = 0; i < trimmedElementArray.length; i++) {
					result.append("'");
					result.append(trimmedElementArray[i]);
					result.append("'");
					if (i < trimmedElementArray.length - 1) { // last one don't have comma
						result.append(",");
					}
				}
				result.append(")");
			}
		}
		return result.toString();
	}
	
	@Override
	public Map<String, List<String>> retrieveFamilyTypesBySocs(String[] socCodes) throws ApplicationException {
		
		// Verify input parameter
		if (socCodes == null || socCodes.length == 0) {
			return null;
		}
		
		StringBuffer sqlSB = new StringBuffer();
		
		// Construct the SQL query statement
		sqlSB.append("select distinct sg.soc, sfg.family_type");  
		sqlSB.append(" from soc_group sg, soc_family_group sfg"); 
		sqlSB.append(" where sfg.soc_group = sg.gp_soc");
		// e.g.      " and sg.soc in ('FETK40EM3', 'STA1234  ')"
		sqlSB.append(constructSQLInSetStatement(socCodes, " and sg.soc "));
		sqlSB.append(" order by sg.soc");
		
		return super.getKnowbilityJdbcTemplate().execute(sqlSB.toString(), new PreparedStatementCallback<Map<String, List<String>>>() {
			@Override
			public Map<String, List<String>> doInPreparedStatement(PreparedStatement pstmt)
					throws SQLException, DataAccessException {

				ResultSet rset = null;
				Map<String, List<String>> familyTypesMap = new HashMap<String, List<String>>();
				try {
					rset = pstmt.executeQuery();
					
					String currentSoc = "";
					List<String> familyTypes = null;
					while(rset.next()) {
						if (!currentSoc.equalsIgnoreCase(rset.getString("soc"))) { // SOC changes to a new one
							if (familyTypes != null && familyTypes.size() > 0) {
								// save the last SOC family types list to map
								familyTypesMap.put(currentSoc, familyTypes);
							}
							// Start a new SOC family types list
							currentSoc = rset.getString("soc");
							familyTypes = new ArrayList<String>();
						}
						familyTypes.add(rset.getString("family_type"));
					}
					// Save the last SOC family types list to map
					familyTypesMap.put(currentSoc, familyTypes);
					
					return familyTypesMap;
					
				} finally {
					if (rset != null ) {
						rset.close();
					}
				}
			}
		});
	}
}