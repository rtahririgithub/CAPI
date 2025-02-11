package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.reference.ServicePeriodType;
import com.telus.cmb.account.informationhelper.dao.UsageDao;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.account.info.AirtimeUsageChargeInfo;
import com.telus.eas.account.info.UsageChargeInfo;
import com.telus.eas.account.info.VoiceUsageServiceDirectionInfo;
import com.telus.eas.account.info.VoiceUsageServiceInfo;
import com.telus.eas.account.info.VoiceUsageServicePeriodInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.utility.info.FeatureInfo;

public class UsageDaoImpl extends MultipleJdbcDaoTemplateSupport implements UsageDao{

	private Logger LOGGER = Logger.getLogger(UsageDaoImpl.class);
	
	public static final int AIRTIME_RECORD_TYPE = 1;
	
	@Override
	public Collection<VoiceUsageSummaryInfo> retrieveVoiceUsageSummary(final int ban, final String featureCode) {

			if (AppConfiguration.isWRPPh3Rollback()) {
			String call = "{ call usage_utility_pkg.GetUnbilledUsageSummary (?,?,?,?) }";
			
			return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<Collection<VoiceUsageSummaryInfo>>() {				

				@Override
				public Collection<VoiceUsageSummaryInfo> doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {

					Vector<String> badHomeProvinces = new Vector<String>();
					badHomeProvinces.add("PE");
					badHomeProvinces.add("NB");
					badHomeProvinces.add("NS");
					badHomeProvinces.add("NF");
					badHomeProvinces.add("SK");
					badHomeProvinces.add("MB");

					// Local declare
					ResultSet result = null;

					List<VoiceUsageSummaryInfo> voiceUsageSummaries = new ArrayList<VoiceUsageSummaryInfo>();
					VoiceUsageSummaryInfo voiceUsageSummary = null;

					List<VoiceUsageServiceInfo> voiceUsageServices = new ArrayList<VoiceUsageServiceInfo>();
					VoiceUsageServiceInfo voiceUsageService = null;

					List<VoiceUsageServiceDirectionInfo> voiceUsageServiceDirections = new ArrayList<VoiceUsageServiceDirectionInfo>();
					VoiceUsageServiceDirectionInfo voiceUsageServiceDirection = null;

					List<VoiceUsageServicePeriodInfo> voiceUsageServicePeriods = new ArrayList<VoiceUsageServicePeriodInfo>();
					VoiceUsageServicePeriodInfo voiceUsageServicePeriod = null;

					String includedMinutesIndicator = null;
					//String directionCode = null;

					String phoneNumber = "";
					String newPhoneNumber = "";
					String serviceCode = "";
					String newServiceCode = "";
					String direction = "";
					String newDirection = "";

					// CM. indicator to track shareable price plans
					boolean isShareablePricePlan = false;
					boolean newIsShareablePricePlan = false;

					try {
						callable.setString(1, featureCode);
						callable.setInt(2, ban);
						callable.setString(3, null);
						callable.registerOutParameter(4, -10);
						callable.execute();
						result = (ResultSet) callable.getObject(4);

						LOGGER.debug("== enter while loop ...");

						while (result != null && result.next()) {

							newPhoneNumber = AttributeTranslator.trimString(result.getString("SUBSCRIBER_NO"));
							includedMinutesIndicator = AttributeTranslator.trimString(result.getString("IM_ALLOCATION_IND"));
							newDirection = AttributeTranslator.trimString(result.getString("ACTION_DIRECTION_CD"));
							newServiceCode = result.getString("PRICE_PLAN_CODE");
							// CM. Sharable price plan indicator.  'X' means shareable price plan.
							if (result.getString("RATING_LEVEL_CODE") != null) {
								newIsShareablePricePlan = result.getString("RATING_LEVEL_CODE").equals("X");
							}

							// Check if service code on this row is the same as in previous row
							if (!phoneNumber.equals(newPhoneNumber)) {
								// New row is new phone number

								//CM. If last price plan of previous row was shareable,
								//    then remove entire tree to avoid displaying old non-shareable price plan(s)
								if (isShareablePricePlan) {
									if (voiceUsageSummaries.remove(voiceUsageSummary)) {
										LOGGER.debug("== VoiceUsageSummary was removed since LAST price plan was shareable" + "for sub=" + phoneNumber);
									}
								}

								voiceUsageSummary = new VoiceUsageSummaryInfo();
								voiceUsageSummary.setFeatureCode(AttributeTranslator.trimString(result.getString("AIRTIME_FEATURE_CD")));
								voiceUsageSummary.setUnitOfMeasureCode(AttributeTranslator.trimString(result.getString("UNIT_OF_MEASURE")));
								voiceUsageSummary.setPhoneNumber(AttributeTranslator.trimString(result.getString("SUBSCRIBER_NO")));
								voiceUsageSummary.setViewable(!badHomeProvinces.contains(AttributeTranslator.trimString(result.getString("HOME_PROVINCE"))));

								voiceUsageSummaries.add(voiceUsageSummary);
								phoneNumber = newPhoneNumber;
								serviceCode = "";
								direction = "";

								// Reset services, directions and periods
								voiceUsageServices = new ArrayList<VoiceUsageServiceInfo>();
								voiceUsageService = null;
								voiceUsageServiceDirections = new ArrayList<VoiceUsageServiceDirectionInfo>();
								voiceUsageServiceDirection = null;
								voiceUsageServicePeriods = new ArrayList<VoiceUsageServicePeriodInfo>();
								voiceUsageServicePeriod = null;
							} else {
								// New row is the same phone number - get the last record
								voiceUsageSummary = (VoiceUsageSummaryInfo) voiceUsageSummaries.get(voiceUsageSummaries.size() - 1);
							}

							// Check if service code on this row is the same as in previous row
							if (!serviceCode.equals(newServiceCode)) {
								// New row is new service

								// CM.  update indicator
								isShareablePricePlan = newIsShareablePricePlan;
								// CM. If last price plan was shareable, then remove Service by skiping this row.
								if (newIsShareablePricePlan) {
									LOGGER.debug("== VoiceUsageService was removed since is shareable, soc=" + newServiceCode + ", sub=" + phoneNumber);
									continue;
								}
								voiceUsageService = new VoiceUsageServiceInfo();
								voiceUsageService.setServiceCode(newServiceCode);
								voiceUsageService.setUsageRecordTypeCode(result.getString("RECORD_TYPE"));
								voiceUsageService.setIMAllocationIndicator(includedMinutesIndicator);
								voiceUsageServices.add(voiceUsageService);
								serviceCode = newServiceCode;
								direction = "";
								// Reset directions and periods
								voiceUsageServiceDirections = new ArrayList<VoiceUsageServiceDirectionInfo>();
								voiceUsageServiceDirection = null;
								voiceUsageServicePeriods = new ArrayList<VoiceUsageServicePeriodInfo>();
								voiceUsageServicePeriod = null;
							} else {
								// New row is the same service - get the last record
								voiceUsageService = (VoiceUsageServiceInfo) voiceUsageServices.get(voiceUsageServices.size() - 1);
							}

							// Check if direction on this row is the same as in previous row
							if (!direction.equals(newDirection)) {
								//	New row for direction
								voiceUsageServiceDirection = new VoiceUsageServiceDirectionInfo();
								voiceUsageServiceDirection.setDirectionCode(newDirection);
								voiceUsageServiceDirections.add(voiceUsageServiceDirection);
								direction = newDirection;
								// Reset periods
								voiceUsageServicePeriods = new ArrayList<VoiceUsageServicePeriodInfo>();
								voiceUsageServicePeriod = null;
							} else {
								// New row is the same direction
								voiceUsageServiceDirection = (VoiceUsageServiceDirectionInfo) voiceUsageServiceDirections.get(voiceUsageServiceDirections.size() - 1);
							}

							if (ServicePeriodType.PERIOD.equals(includedMinutesIndicator)) {
								int offset = 0;
								for (int j = 0; j < 6; j++) {
									voiceUsageServicePeriod = new VoiceUsageServicePeriodInfo();
									offset = j + 1;
									voiceUsageServicePeriod.setPeriodCode("0" + offset);
									voiceUsageServicePeriod.setCalls(result.getInt("NUM_OF_CALLS_" + offset));
									voiceUsageServicePeriod.setTotalUsed(result.getDouble("TOTAL_USED_" + offset));
									voiceUsageServicePeriod.setIncluded(result.getDouble("IM_ALLOWED_" + offset));
									voiceUsageServicePeriod.setIncludedUsed(result.getDouble("IM_USED_" + offset));
									voiceUsageServicePeriod.setFree(result.getDouble("FREE_" + offset));
									voiceUsageServicePeriod.setRemaining(result.getDouble("IM_REMAINING_" + offset));
									voiceUsageServicePeriod.setChargeable(result.getDouble("CHARGEABLE_" + offset));
									voiceUsageServicePeriod.setChargeAmount(result.getDouble("CHARGE_" + offset));

									voiceUsageServicePeriods.add(voiceUsageServicePeriod);
								}

							} else {

								voiceUsageServicePeriod = new VoiceUsageServicePeriodInfo();
								voiceUsageServicePeriod.setPeriodCode(includedMinutesIndicator);
								voiceUsageServicePeriod.setCalls(result.getInt("TOTAL_CALLS"));
								voiceUsageServicePeriod.setTotalUsed(result.getDouble("TOTAL_USED"));
								voiceUsageServicePeriod.setIncluded(result.getDouble("IM_ALLOWED"));
								voiceUsageServicePeriod.setIncludedUsed(result.getDouble("IM_USED"));
								voiceUsageServicePeriod.setFree(result.getDouble("FREE"));
								voiceUsageServicePeriod.setRemaining(result.getDouble("IM_REMAINING"));
								voiceUsageServicePeriod.setChargeable(result.getDouble("CHARGEABLE"));
								voiceUsageServicePeriod.setChargeAmount(result.getDouble("TOTAL_CHARGE"));

								voiceUsageServicePeriods.add(voiceUsageServicePeriod);

							}

							// Save data back to array
							voiceUsageServiceDirection.setVoiceUsageServicePeriods((VoiceUsageServicePeriodInfo[]) voiceUsageServicePeriods
									.toArray(new VoiceUsageServicePeriodInfo[voiceUsageServicePeriods.size()]));
							voiceUsageService.setVoiceUsageServiceDirections((VoiceUsageServiceDirectionInfo[]) voiceUsageServiceDirections
									.toArray(new VoiceUsageServiceDirectionInfo[voiceUsageServiceDirections.size()]));
							voiceUsageSummary.setVoiceUsageServices((VoiceUsageServiceInfo[]) voiceUsageServices.toArray(new VoiceUsageServiceInfo[voiceUsageServices.size()]));
							voiceUsageSummaries.set(voiceUsageSummaries.size() - 1, voiceUsageSummary);

						}

						//CM. If last price plan of previous row was shareable,
						//    then remove entire tree to avoid displaying old non-shareable price plan(s)
						if (isShareablePricePlan) {
							if (voiceUsageSummaries.remove(voiceUsageSummary)) {
								LOGGER.debug("VoiceUsageSummary was removed since LAST price plan was shareable" + ", sub=" + phoneNumber);
							}
						}

						// CM. Check last voiceUsageSummary, which could have empty voiceUsageService due
						//     to LAST shareable price plan filtering.
						if (voiceUsageSummary != null && voiceUsageSummary.getVoiceUsageServices() == null) {
							if (voiceUsageSummaries.remove(voiceUsageSummary)) {
								LOGGER.debug(" VoiceUsageSummary was removed since VoiceUsageService is null" + ", sub=" + phoneNumber);
							}
						}
					} finally {
						if (result != null) {
							result.close();
						}
					}
					return voiceUsageSummaries;
				}
			});

		} else {
			String query = "SELECT BAN, SUBSCRIBER_NO, RECORD_TYPE, AIRTIME_FEATURE_CD, PRICE_PLAN_CODE, ACTION_DIRECTION_CD, SOC_EFFECTIVE_DATE, IM_ALLOCATION_IND, TOTAL_CALLS, "
					+ "NUM_OF_CALLS_1, NUM_OF_CALLS_2, NUM_OF_CALLS_3, NUM_OF_CALLS_4, NUM_OF_CALLS_5, NUM_OF_CALLS_6, NUM_FREE_AIR_CALLS, NUM_SPECIAL_CALLS, "
					+ "TOTAL_USED, TOTAL_USED_1, TOTAL_USED_2, TOTAL_USED_3, TOTAL_USED_4, TOTAL_USED_5, TOTAL_USED_6, "
					+ "IM_ALLOWED, IM_ALLOWED_COMBD IM_ALLOWED, IM_ALLOWED_1, IM_ALLOWED_2, IM_ALLOWED_3, IM_ALLOWED_4, IM_ALLOWED_5, IM_ALLOWED_6, "
					+ "IM_USED, IM_USED_1, IM_USED_2, IM_USED_3, IM_USED_4, IM_USED_5, IM_USED_6, "
					+ "FREE, FREE_1, FREE_2, FREE_3, FREE_4, FREE_5, FREE_6, UNIT_OF_MEASURE, PRODUCT_TYPE, "
					+ "DECODE( record_type,7,0,(GREATEST(IM_ALLOWED_COMBD                                       - IM_USED,0)) ) im_remaining, "
					+ "DECODE( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_1                                         - IM_USED_1 ,0), 0 ) im_remaining_1, "
					+ "DECODE( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_2                                         - IM_USED_2 ,0), 0 ) im_remaining_2, "
					+ "DECODE( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_3                                         - IM_USED_3 ,0), 0 ) im_remaining_3, "
					+ "DECODE( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_4                                         - IM_USED_4 ,0), 0 ) im_remaining_4, "
					+ "DECODE( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_5                                         - IM_USED_5 ,0), 0 ) im_remaining_5, "
					+ "DECODE( IM_ALLOWED_COMBD, 0, GREATEST(IM_ALLOWED_6                                         - IM_USED_6 ,0), 0 ) im_remaining_6, "
					+ "DECODE( upper(UNIT_OF_MEASURE),'M', DECODE( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED   - IM_USED - FREE), 0 ), (TOTAL_CALLS - IM_USED ) ) chargeable, "
					+ "DECODE( upper(UNIT_OF_MEASURE),'M', DECODE( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_1 - IM_USED_1 - FREE_1), 0 ), (NUM_OF_CALLS_1 - IM_USED_1 ) ) chargeable_1, "
					+ "DECODE( upper(UNIT_OF_MEASURE),'M', DECODE( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_2 - IM_USED_2 - FREE_2), 0 ), (NUM_OF_CALLS_2 - IM_USED_2 ) ) chargeable_2, "
					+ "DECODE( upper(UNIT_OF_MEASURE),'M', DECODE( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_3 - IM_USED_3 - FREE_3), 0 ), (NUM_OF_CALLS_3 - IM_USED_3 ) ) chargeable_3, "
					+ "DECODE( upper(UNIT_OF_MEASURE),'M', DECODE( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_4 - IM_USED_4 - FREE_4), 0 ), (NUM_OF_CALLS_4 - IM_USED_4 ) ) chargeable_4, "
					+ "DECODE( upper(UNIT_OF_MEASURE),'M', DECODE( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_5 - IM_USED_5 - FREE_5), 0 ), (NUM_OF_CALLS_5 - IM_USED_5 ) ) chargeable_5, "
					+ "DECODE( upper(UNIT_OF_MEASURE),'M', DECODE( IM_ALLOWED_COMBD, IM_USED_COMBD, (TOTAL_USED_6 - IM_USED_6 - FREE_6), 0 ), (NUM_OF_CALLS_6 - IM_USED_6 ) ) chargeable_6, "
					+ "TOTAL_CHARGE, CHARGE_1, CHARGE_2, CHARGE_3, CHARGE_4, CHARGE_5, CHARGE_6, DECODE( record_type,7,GREATEST(0,IM_ALLOWED   - TOTAL_CHARGE) ,0) AP_REMAINING, "
					+ "DECODE( record_type,7,GREATEST(0,TOTAL_CHARGE - IM_ALLOWED) ,0) AP_CHARGE, TO_CHAR(LAST_CALL_DATE,'YYYY-MM-DD') LAST_CALL_DATE, PRORATION_IND, RATING_LEVEL_CODE "
					+ "FROM (SELECT MAX ( au.BAN ) ban, MAX ( SUBSCRIBER_NO ) subscriber_no, RECORD_TYPE, AIRTIME_FEATURE_CD, PRICE_PLAN_CODE, ACTION_DIRECTION_CD, "
					+ "MAX(SOC_EFFECTIVE_DATE) SOC_EFFECTIVE_DATE, MAX(RATING_LEVEL_CODE) RATING_LEVEL_CODE, MAX( PRODUCT_TYPE ) PRODUCT_TYPE, MAX( IM_ALLOCATION_IND ) IM_ALLOCATION_IND, "
					+ "SUM( NUM_OF_CALLS_PRD_1 + NUM_OF_CALLS_PRD_2 + NUM_OF_CALLS_PRD_3 + NUM_OF_CALLS_PRD_4 + NUM_OF_CALLS_PRD_5 + NUM_OF_CALLS_PRD_6 + NUM_OF_CALLS ) total_calls, "
					+ "SUM( NUM_OF_CALLS_PRD_1 ) num_of_calls_1, SUM( NUM_OF_CALLS_PRD_2 ) num_of_calls_2, SUM( NUM_OF_CALLS_PRD_3 ) num_of_calls_3, "
					+ "SUM( NUM_OF_CALLS_PRD_4 ) num_of_calls_4, SUM( NUM_OF_CALLS_PRD_5 ) num_of_calls_5, SUM( NUM_OF_CALLS_PRD_6 ) num_of_calls_6, "
					+ "SUM( NUM_OF_FREE_AIR_CALLS ) num_free_air_calls, SUM( NUM_OF_NO_AIR_CALLS ) num_special_calls, "
					+ "SUM( CTN_MINS_PRD_1 + CTN_MINS_PRD_2 + CTN_MINS_PRD_3 + CTN_MINS_PRD_4 + CTN_MINS_PRD_5 + CTN_MINS_PRD_6 + CTN_MINS ) total_used, "
					+ "SUM( CTN_MINS_PRD_1 ) total_used_1, SUM( CTN_MINS_PRD_2 ) total_used_2, SUM( CTN_MINS_PRD_3 ) total_used_3, "
					+ "SUM( CTN_MINS_PRD_4 ) total_used_4, SUM( CTN_MINS_PRD_5 ) total_used_5, SUM( CTN_MINS_PRD_6 ) total_used_6, "
					+ "SUM ( CHRG_AMT_PRD_1 + CHRG_AMT_PRD_2 + CHRG_AMT_PRD_3 + CHRG_AMT_PRD_4 + CHRG_AMT_PRD_5 + CHRG_AMT_PRD_6 + CHRG_AMT ) total_charge, "
					+ "SUM (CHRG_AMT_PRD_1) charge_1, SUM (CHRG_AMT_PRD_2) charge_2, SUM (CHRG_AMT_PRD_3) charge_3, "
					+ "SUM (CHRG_AMT_PRD_4) charge_4, SUM (CHRG_AMT_PRD_5) charge_5, SUM (CHRG_AMT_PRD_6) charge_6, "
					+ "SUM (ROUND( NVL(IM_ALLOWED_COMBD,0)                                  * DECODE (NVL(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ) ) im_allowed_combd, "
					+ "SUM (DECODE( NVL(IM_ALLOWED_COMBD,0), 0 , ( (NVL(IM_ALLOWED_PRD_1,0) + NVL(IM_ALLOWED_PRD_2,0) + NVL(IM_ALLOWED_PRD_3,0) + NVL(IM_ALLOWED_PRD_4,0) + "
						+ "NVL(IM_ALLOWED_PRD_5,0) + NVL(IM_ALLOWED_PRD_6,0) ) * DECODE (NVL(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ), "
						+ "( NVL(IM_ALLOWED_COMBD,0) * DECODE (NVL(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ) ) ) im_allowed, "
					+ "SUM (NVL(IM_ALLOWED_PRD_1,0)                                         * DECODE (NVL(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ) im_allowed_1, "
					+ "SUM (NVL(IM_ALLOWED_PRD_2,0)                                         * DECODE (NVL(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ) im_allowed_2, "
					+ "SUM (NVL(IM_ALLOWED_PRD_3,0)                                         * DECODE (NVL(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ) im_allowed_3, "
					+ "SUM (NVL(IM_ALLOWED_PRD_4,0)                                         * DECODE (NVL(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ) im_allowed_4, "
					+ "SUM (NVL(IM_ALLOWED_PRD_5,0)                                         * DECODE (NVL(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ) im_allowed_5, "
					+ "SUM (NVL(IM_ALLOWED_PRD_6,0)                                         * DECODE (NVL(PRORATION_FACTOR,0),0,1,PRORATION_FACTOR) ) im_allowed_6, "
					+ "SUM ( CTN_IM_USED_COMBD ) im_used_combd, "
					+ "SUM ( DECODE( CTN_IM_USED_COMBD, 0 , ( CTN_IM_USED_PRD_1 + CTN_IM_USED_PRD_2 + CTN_IM_USED_PRD_3 + CTN_IM_USED_PRD_4 + CTN_IM_USED_PRD_5 + CTN_IM_USED_PRD_6 ), CTN_IM_USED_COMBD ) ) im_used, "
					+ "SUM( CTN_IM_USED_PRD_1 ) im_used_1, SUM( CTN_IM_USED_PRD_2 ) im_used_2, SUM( CTN_IM_USED_PRD_3 ) im_used_3, "
					+ "SUM( CTN_IM_USED_PRD_4 ) im_used_4, SUM( CTN_IM_USED_PRD_5 ) im_used_5, SUM( CTN_IM_USED_PRD_6 ) im_used_6, "
					+ "SUM ( FREE_MIN_USED_PRD_1 + FREE_MIN_USED_PRD_2 + FREE_MIN_USED_PRD_3 + FREE_MIN_USED_PRD_4 + FREE_MIN_USED_PRD_5 + FREE_MIN_USED_PRD_6 ) free, "
					+ "SUM (FREE_MIN_USED_PRD_1) free_1, SUM (FREE_MIN_USED_PRD_2) free_2, SUM (FREE_MIN_USED_PRD_3) free_3, "
					+ "SUM (FREE_MIN_USED_PRD_4) free_4, SUM (FREE_MIN_USED_PRD_5) free_5, SUM (FREE_MIN_USED_PRD_6) free_6, "
					+ "MIN( DECODE( trim(UNIT_MEASUR_CODE), NULL, DECODE(airtime_feature_cd,'MMMAIL','s','m'), trim(UNIT_MEASUR_CODE) ) ) unit_of_measure, "
					+ "MAX( LAST_IM_CALL_DT ) last_call_date, MAX( DECODE(NVL(PRORATION_FACTOR,0),1,'N',0,'N','Y') ) proration_ind "
					+ "FROM :usage_table au JOIN billing_account ba ON au.ban                                   = ba.ban "
					+ "WHERE record_type = :record_type AND airtime_feature_cd = :feature_code AND au.ban = :ban AND bl_cur_bill_seq_no = bill_seq_no "
					+ "AND ( NVL(trim(im_allocation_ind),'?') NOT IN ('N') ) GROUP BY subscriber_no, record_type, airtime_feature_cd, price_plan_code, action_direction_cd) "
					+ "ORDER BY ban, subscriber_no, record_type, airtime_feature_cd, soc_effective_date, price_plan_code, action_direction_cd";
								
			try {
				query = query.replaceFirst(":usage_table", retrieveUnbilledUsageTableName(ban));
			} catch (ApplicationException ae) {
				// TODO - to prevent an interface change, let's just return null and log that there is no usage	
				LOGGER.info("No usage table found for ban=[" + ban + "], returning null for unbilledVoiceUsageSummary.");
				return null;
			}
			
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("record_type", AIRTIME_RECORD_TYPE); 
			namedParameters.addValue("feature_code", FeatureInfo.padFeature(featureCode));
			namedParameters.addValue("ban", ban);
			
			return super.getKnowbilityNamedParameterJdbcTemplate().execute(query, namedParameters, new PreparedStatementCallback<Collection<VoiceUsageSummaryInfo>>() {
				
				@Override
				public Collection<VoiceUsageSummaryInfo> doInPreparedStatement(PreparedStatement stmt) throws SQLException, DataAccessException {
					
					ResultSet result = null;

					List<VoiceUsageSummaryInfo> voiceUsageSummaries = new ArrayList<VoiceUsageSummaryInfo>();
					VoiceUsageSummaryInfo voiceUsageSummary = null;

					List<VoiceUsageServiceInfo> voiceUsageServices = new ArrayList<VoiceUsageServiceInfo>();
					VoiceUsageServiceInfo voiceUsageService = null;

					List<VoiceUsageServiceDirectionInfo> voiceUsageServiceDirections = new ArrayList<VoiceUsageServiceDirectionInfo>();
					VoiceUsageServiceDirectionInfo voiceUsageServiceDirection = null;

					List<VoiceUsageServicePeriodInfo> voiceUsageServicePeriods = new ArrayList<VoiceUsageServicePeriodInfo>();
					VoiceUsageServicePeriodInfo voiceUsageServicePeriod = null;

					String includedMinutesIndicator = null;
					//String directionCode = null;

					String phoneNumber = "";
					String newPhoneNumber = "";
					String serviceCode = "";
					String newServiceCode = "";
					String direction = "";
					String newDirection = "";

					// TODO: Need to refactor the results below so it's easier to read... this is just a cut and paste from above
					// CM. indicator to track shareable price plans
					boolean isShareablePricePlan = false;
					boolean newIsShareablePricePlan = false;
					
					try {
						result = stmt.executeQuery();
						
						while (result != null && result.next()) {

							newPhoneNumber = AttributeTranslator.trimString(result.getString("SUBSCRIBER_NO"));
							includedMinutesIndicator = AttributeTranslator.trimString(result.getString("IM_ALLOCATION_IND"));
							newDirection = AttributeTranslator.trimString(result.getString("ACTION_DIRECTION_CD"));
							newServiceCode = result.getString("PRICE_PLAN_CODE");
							// CM. Sharable price plan indicator.  'X' means shareable price plan.
							if (result.getString("RATING_LEVEL_CODE") != null) {
								newIsShareablePricePlan = result.getString("RATING_LEVEL_CODE").equals("X");
							}

							// Check if service code on this row is the same as in previous row
							if (!phoneNumber.equals(newPhoneNumber)) {
								// New row is new phone number

								//CM. If last price plan of previous row was shareable,
								//    then remove entire tree to avoid displaying old non-shareable price plan(s)
								if (isShareablePricePlan) {
									if (voiceUsageSummaries.remove(voiceUsageSummary)) {
										LOGGER.debug("== VoiceUsageSummary was removed since LAST price plan was shareable" + "for sub=" + phoneNumber);
									}
								}

								voiceUsageSummary = new VoiceUsageSummaryInfo();
								voiceUsageSummary.setFeatureCode(AttributeTranslator.trimString(result.getString("AIRTIME_FEATURE_CD")));
								voiceUsageSummary.setUnitOfMeasureCode(AttributeTranslator.trimString(result.getString("UNIT_OF_MEASURE")));
								voiceUsageSummary.setPhoneNumber(AttributeTranslator.trimString(result.getString("SUBSCRIBER_NO")));
								voiceUsageSummary.setViewable(true); // Always true for BAN level 

								voiceUsageSummaries.add(voiceUsageSummary);
								phoneNumber = newPhoneNumber;
								serviceCode = "";
								direction = "";

								// Reset services, directions and periods
								voiceUsageServices = new ArrayList<VoiceUsageServiceInfo>();
								voiceUsageService = null;
								voiceUsageServiceDirections = new ArrayList<VoiceUsageServiceDirectionInfo>();
								voiceUsageServiceDirection = null;
								voiceUsageServicePeriods = new ArrayList<VoiceUsageServicePeriodInfo>();
								voiceUsageServicePeriod = null;
							} else {
								// New row is the same phone number - get the last record
								voiceUsageSummary = (VoiceUsageSummaryInfo) voiceUsageSummaries.get(voiceUsageSummaries.size() - 1);
							}

							// Check if service code on this row is the same as in previous row
							if (!serviceCode.equals(newServiceCode)) {
								// New row is new service

								// CM.  update indicator
								isShareablePricePlan = newIsShareablePricePlan;
								// CM. If last price plan was shareable, then remove Service by skiping this row.
								if (newIsShareablePricePlan) {
									LOGGER.debug("== VoiceUsageService was removed since is shareable, soc=" + newServiceCode + ", sub=" + phoneNumber);
									continue;
								}
								voiceUsageService = new VoiceUsageServiceInfo();
								voiceUsageService.setServiceCode(newServiceCode);
								voiceUsageService.setUsageRecordTypeCode(result.getString("RECORD_TYPE"));
								voiceUsageService.setIMAllocationIndicator(includedMinutesIndicator);
								voiceUsageServices.add(voiceUsageService);
								serviceCode = newServiceCode;
								direction = "";
								// Reset directions and periods
								voiceUsageServiceDirections = new ArrayList<VoiceUsageServiceDirectionInfo>();
								voiceUsageServiceDirection = null;
								voiceUsageServicePeriods = new ArrayList<VoiceUsageServicePeriodInfo>();
								voiceUsageServicePeriod = null;
							} else {
								// New row is the same service - get the last record
								voiceUsageService = (VoiceUsageServiceInfo) voiceUsageServices.get(voiceUsageServices.size() - 1);
							}

							// Check if direction on this row is the same as in previous row
							if (!direction.equals(newDirection)) {
								//	New row for direction
								voiceUsageServiceDirection = new VoiceUsageServiceDirectionInfo();
								voiceUsageServiceDirection.setDirectionCode(newDirection);
								voiceUsageServiceDirections.add(voiceUsageServiceDirection);
								direction = newDirection;
								// Reset periods
								voiceUsageServicePeriods = new ArrayList<VoiceUsageServicePeriodInfo>();
								voiceUsageServicePeriod = null;
							} else {
								// New row is the same direction
								voiceUsageServiceDirection = (VoiceUsageServiceDirectionInfo) voiceUsageServiceDirections.get(voiceUsageServiceDirections.size() - 1);
							}

							if (ServicePeriodType.PERIOD.equals(includedMinutesIndicator)) {
								int offset = 0;
								for (int j = 0; j < 6; j++) {
									voiceUsageServicePeriod = new VoiceUsageServicePeriodInfo();
									offset = j + 1;
									voiceUsageServicePeriod.setPeriodCode("0" + offset);
									voiceUsageServicePeriod.setCalls(result.getInt("NUM_OF_CALLS_" + offset));
									voiceUsageServicePeriod.setTotalUsed(result.getDouble("TOTAL_USED_" + offset));
									voiceUsageServicePeriod.setIncluded(result.getDouble("IM_ALLOWED_" + offset));
									voiceUsageServicePeriod.setIncludedUsed(result.getDouble("IM_USED_" + offset));
									voiceUsageServicePeriod.setFree(result.getDouble("FREE_" + offset));
									voiceUsageServicePeriod.setRemaining(result.getDouble("IM_REMAINING_" + offset));
									voiceUsageServicePeriod.setChargeable(result.getDouble("CHARGEABLE_" + offset));
									voiceUsageServicePeriod.setChargeAmount(result.getDouble("CHARGE_" + offset));

									voiceUsageServicePeriods.add(voiceUsageServicePeriod);
								}

							} else {

								voiceUsageServicePeriod = new VoiceUsageServicePeriodInfo();
								voiceUsageServicePeriod.setPeriodCode(includedMinutesIndicator);
								voiceUsageServicePeriod.setCalls(result.getInt("TOTAL_CALLS"));
								voiceUsageServicePeriod.setTotalUsed(result.getDouble("TOTAL_USED"));
								voiceUsageServicePeriod.setIncluded(result.getDouble("IM_ALLOWED"));
								voiceUsageServicePeriod.setIncludedUsed(result.getDouble("IM_USED"));
								voiceUsageServicePeriod.setFree(result.getDouble("FREE"));
								voiceUsageServicePeriod.setRemaining(result.getDouble("IM_REMAINING"));
								voiceUsageServicePeriod.setChargeable(result.getDouble("CHARGEABLE"));
								voiceUsageServicePeriod.setChargeAmount(result.getDouble("TOTAL_CHARGE"));

								voiceUsageServicePeriods.add(voiceUsageServicePeriod);

							}

							// Save data back to array
							voiceUsageServiceDirection.setVoiceUsageServicePeriods((VoiceUsageServicePeriodInfo[]) voiceUsageServicePeriods
									.toArray(new VoiceUsageServicePeriodInfo[voiceUsageServicePeriods.size()]));
							voiceUsageService.setVoiceUsageServiceDirections((VoiceUsageServiceDirectionInfo[]) voiceUsageServiceDirections
									.toArray(new VoiceUsageServiceDirectionInfo[voiceUsageServiceDirections.size()]));
							voiceUsageSummary.setVoiceUsageServices((VoiceUsageServiceInfo[]) voiceUsageServices.toArray(new VoiceUsageServiceInfo[voiceUsageServices.size()]));
							voiceUsageSummaries.set(voiceUsageSummaries.size() - 1, voiceUsageSummary);

						}

						//CM. If last price plan of previous row was shareable,
						//    then remove entire tree to avoid displaying old non-shareable price plan(s)
						if (isShareablePricePlan) {
							if (voiceUsageSummaries.remove(voiceUsageSummary)) {
								LOGGER.debug("VoiceUsageSummary was removed since LAST price plan was shareable" + ", sub=" + phoneNumber);
							}
						}

						// CM. Check last voiceUsageSummary, which could have empty voiceUsageService due
						//     to LAST shareable price plan filtering.
						if (voiceUsageSummary != null && voiceUsageSummary.getVoiceUsageServices() == null) {
							if (voiceUsageSummaries.remove(voiceUsageSummary)) {
								LOGGER.debug(" VoiceUsageSummary was removed since VoiceUsageService is null" + ", sub=" + phoneNumber);
							}
						}
						
					} finally {
						if (result != null) {
							result.close();
						}
					}
					
					return voiceUsageSummaries;
				}
			});
		}		
	}

	@Override
	public double retrieveUnpaidAirTimeTotal(final int ban) throws ApplicationException {

		String query = "select sum(NVL(chrg_amt_prd_1,0) + NVL(chrg_amt_prd_2,0) + NVL(chrg_amt_prd_3,0) + NVL(chrg_amt_prd_4,0) + NVL(chrg_amt_prd_5,0) + NVL(chrg_amt_prd_6,0) + NVL(CHRG_AMT,0)) "
				+ "from " + retrieveAirtimeUsageTableName(ban) + " where ban = ? and record_type in (1, 2, 4, 5)";

		return super.getKnowbilityJdbcTemplate().execute(query, new PreparedStatementCallback<Double>() {
			@Override
			public Double doInPreparedStatement(PreparedStatement stmt1) throws SQLException, DataAccessException {
				ResultSet rset1 = null;
				double unpaidAirTimeTotal = 0;
				try {
					stmt1.setInt(1, ban);
					rset1 = stmt1.executeQuery();
					if (rset1.next()) {
						unpaidAirTimeTotal = rset1.getDouble(1);
					}
				} finally {
					if (rset1 != null) {
						rset1.close();
					}
				}
				return unpaidAirTimeTotal;
			}

		});
	}
	

	@Override
	public AirtimeUsageChargeInfo retrieveUnpaidAirtimeUsageChargeInfo(final int ban) throws ApplicationException {
		
		String query = "select record_type, sum(NVL(chrg_amt_prd_1,0) + NVL(chrg_amt_prd_2,0) + NVL(chrg_amt_prd_3,0) + NVL(chrg_amt_prd_4,0) + NVL(chrg_amt_prd_5,0) + NVL(chrg_amt_prd_6,0) "
				+ "+ NVL(CHRG_AMT,0)) amount from " + retrieveAirtimeUsageTableName(ban) + " where ban = ? and record_type in (1,2,4,5) group by record_type";

		return super.getKnowbilityJdbcTemplate().execute(query, new PreparedStatementCallback<AirtimeUsageChargeInfo>() {
			
			@Override
			public AirtimeUsageChargeInfo doInPreparedStatement(PreparedStatement stmt1) throws SQLException, DataAccessException {
				
				ResultSet rset1 = null;
				AirtimeUsageChargeInfo airtimeUsageChargeInfo = new AirtimeUsageChargeInfo();
				try {
					stmt1.setInt(1, ban);
					rset1 = stmt1.executeQuery();
					while (rset1.next()) {
						UsageChargeInfo usageChargeInfo = new UsageChargeInfo();
						usageChargeInfo.setChargeRecordType(rset1.getString("record_type"));
						usageChargeInfo.setChargeAmount(rset1.getDouble("amount"));
						airtimeUsageChargeInfo.addChargeInfo(usageChargeInfo);
					}
				} finally {
					if (rset1 != null) {
						rset1.close();
					}
				}
				return airtimeUsageChargeInfo;
			}

		});
	}
	
	private String retrieveAirtimeUsageTableName(final int ban) throws ApplicationException {

		String query1 = "select au_issue_code from cycle_control c, billing_account ba, bill b where ba.ban = ? and (ba.bill_cycle = c.cycle_code) and (b.ban = ba.ban) "
				+ "and (b.bill_seq_no = ba.bl_cur_bill_seq_no) and (b.prd_cvrg_strt_date between c.cycle_start_date and c.cycle_close_date )";

		String result1 = null;
		result1 = super.getKnowbilityJdbcTemplate().execute(query1, new PreparedStatementCallback<String>() {
			
			@Override
			public String doInPreparedStatement(PreparedStatement stmt1) throws SQLException, DataAccessException {
				String tblName = null;
				ResultSet rset1 = null;
				try {
					stmt1.setInt(1, ban);
					rset1 = stmt1.executeQuery();
					if (rset1.next()) {
						tblName = rset1.getString("au_issue_code");
					}
				} finally {
					if (rset1 != null) {
						rset1.close();
					}
				}
				return tblName;
			}
		});
		
		if (StringUtils.isBlank(result1)) {
			String msgEn = "Usage table not found for billing account number [" + ban + "]";
			throw new ApplicationException(SystemCodes.CMB_SLH_DAO, ErrorCodes.USAGE_TABLE_NOT_FOUND, msgEn, "");
		}

		return result1;
	}

	private String retrieveUnbilledUsageTableName(final int ban) throws ApplicationException {

		String query = "SELECT au_issue_code FROM cycle_control c JOIN billing_account ba ON ba.bill_cycle = c.cycle_code JOIN logical_date ld "
				+ "ON ld.logical_date BETWEEN cycle_start_date AND c.cycle_close_date WHERE ban = :ban AND (ld.logical_date_type = 'O' )";

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("ban", ban);
		
		String result1 = super.getKnowbilityNamedParameterJdbcTemplate().execute(query, namedParameters, new PreparedStatementCallback<String>() {
			
			@Override
			public String doInPreparedStatement(PreparedStatement stmt1) throws SQLException, DataAccessException {
				
				String tblName = null;
				ResultSet rset = null;
				try {
					stmt1.setInt(1, ban);
					rset = stmt1.executeQuery();
					if (rset.next()) {
						tblName = rset.getString("au_issue_code");
					}
				} finally {
					if (rset != null) {
						rset.close();
					}
				}
				
				return tblName;
			}
		});
		
		if (StringUtils.isBlank(result1)) {
			String msgEn = "Usage table not found for billing account number [" + ban + "]";
			throw new ApplicationException(SystemCodes.CMB_SLH_DAO, ErrorCodes.USAGE_TABLE_NOT_FOUND, msgEn, "");
		}

		return result1;
	}	
}
