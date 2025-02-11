/**
 * 
 */
package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.subscriber.lifecyclehelper.dao.CommunicationSuiteDao;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.PairingGroupInfo;

/**
 * @author T837288
 *
 */
public class CommunicationSuiteDaoImpl extends MultipleJdbcDaoTemplateSupport implements CommunicationSuiteDao {
	
	private static final Logger LOGGER = Logger.getLogger(CommunicationSuiteDaoImpl.class);
	
	private static final String PARAMETER_NAME_PRIMARY_TN = "PRIMARY_TN";
	private static final String PARAMETER_NAME_COMPANION_TN_IN_PRIMARY = "COMPANION_TNS";
	private static final String PARAMETER_NAME_COMPANION_TN_IN_COMPANION = "COMPANION_TNS_2";
	
	private static final String PARAMETER_NAME_PRIMARY_TN_STANDALONE = "STA_PHONE_TN";
	private static final String PARAMETER_NAME_COMPANION_TNS_IN_STANDALONE_PRIMARY = "STA_WATCH_TNS";

	/**
	 * Returns communication suite pairing info. A communication suite consists of exactly one primary and one or more companions.
	 * There're two pairing modes, ONE NUMBER (Husky) or STANDALONE (Tremblant) that a primary may belong to at the same time.
	 * If no pairing exists, it's not considered a communication suite.
	 * For a standalone communication suite retireval, if the specified subscriber is the companion, this method does not return the existence of other siblings.
	 */
	@Override
	public CommunicationSuiteInfo retrieveCommunicationSuite(final int ban, final String subscriberNum, final int companionCheckLevel) {
		
		LOGGER.debug("retrieveCommunicationSuite parameter value ban=["+ban+"], subscriberNum=["+subscriberNum+"], companionCheckLevel=["+companionCheckLevel+"]");
		
		if (ban < 0 || subscriberNum == null || subscriberNum.length() < 10) {
			return null;
		}
		
		String sql = "SELECT SF.FEATURE_CODE, FCR.CATEGORY_CODE, SF.FTR_ADD_SW_PRM " +
					 "FROM service_feature sf, feature_category_relation fcr " +
					 "WHERE     sf.feature_code = fcr.feature_code " +
					 "AND fcr.category_code IN (" + getCategorycodeCriteria(companionCheckLevel) + ") " +
					 "AND (   FTR_EXPIRATION_DATE IS NULL " +
					 "OR FTR_EXPIRATION_DATE > (SELECT logical_date FROM logical_date WHERE logical_date_type = 'O')) " +
					 "AND FTR_EFFECTIVE_DATE <= (SELECT logical_date FROM logical_date WHERE logical_date_type = 'O') " +
                     "AND SUBSCRIBER_NO = ? AND ban = ?";
		
		return super.getKnowbilityJdbcTemplate().query(sql, new Object[] { subscriberNum, ban }, new ResultSetExtractor<CommunicationSuiteInfo>() {

			@Override
			public CommunicationSuiteInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				CommunicationSuiteInfo commSuiteInfo = null;
				while (rs.next()) {
					String featureParameterValue = rs.getString("FTR_ADD_SW_PRM");
					String categoryCode = rs.getString("CATEGORY_CODE").trim();
					
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Retrieved communication sutie parameter value ["+featureParameterValue+"], categoryCode = ["+categoryCode+"] for ban=["+ban+"], subscriberNum=["+subscriberNum+"], companionCheckLevel=["+companionCheckLevel+"]");
					}
					
					if (featureParameterValue != null && featureParameterValue.trim().isEmpty() == false) {
						String primaryTn = null;
						String companionTn = null;
						
						boolean isCompanion = CommunicationSuiteInfo.COMPANION_FEATURE_CATEGORY_LIST.contains(categoryCode);
						
						if (isCompanion) {
							if (CommunicationSuiteInfo.COMPANION_STANDALONE_FEATURE_CATEGORY_CODE.equals(categoryCode)) {
								primaryTn = extractValue(featureParameterValue, PARAMETER_NAME_PRIMARY_TN_STANDALONE);
							}else {
								primaryTn = extractValue(featureParameterValue, PARAMETER_NAME_PRIMARY_TN);
							}
							companionTn = subscriberNum;
						}else {
							primaryTn = subscriberNum;
						}
						
						PairingGroupInfo pairingGroup = createPairingGroup(primaryTn, companionTn, featureParameterValue, categoryCode);
						
						if (pairingGroup != null) {
							if (commSuiteInfo == null) {
								commSuiteInfo = new CommunicationSuiteInfo();
								commSuiteInfo.setBan(ban);
								commSuiteInfo.setPrimaryPhoneNumber(isCompanion ? primaryTn : subscriberNum);
								commSuiteInfo.setRetrievedAsPrimary(isCompanion == false);
							}
							
							commSuiteInfo.addPairingGroup(pairingGroup);
						}

					}
				}

				return commSuiteInfo; //commSuiteInfo may be null, which means there's no pairing info exists.
			}

		});
	}

	
	private static String getCategorycodeCriteria(int companionCheckLevel) {
		String primaryCategorySql = "'" + CommunicationSuiteInfo.PRIMARY_FEATURE_CATEGORY_CODE + "', '" + CommunicationSuiteInfo.PRIMARY_STANDALONE_FEATURE_CATEGORY_CODE + "'";
		String companionCategorySql = "'" + CommunicationSuiteInfo.COMPANION_FEATURE_CATEGORY_CODE + "', '" + CommunicationSuiteInfo.COMPANION_STANDALONE_FEATURE_CATEGORY_CODE + "'";

		if (companionCheckLevel == CommunicationSuiteInfo.CHECK_LEVEL_PRIMARY_ONLY) {
			return primaryCategorySql;
		} else if (companionCheckLevel == CommunicationSuiteInfo.CHECK_LEVEL_COMPANION_ONLY) {
			return companionCategorySql;
		} else {
			return primaryCategorySql + ", " + companionCategorySql;
		}

	}
	
	private List<String> extractCompanionList(String featureParameterValue, String parameterNameToParse) {
		String companionListStr = extractValue(featureParameterValue, parameterNameToParse);
		if (companionListStr != null && companionListStr.trim().isEmpty() == false) {
			String[] companionList = companionListStr.split (",");
			List<String> companionPhoneNumberList = new ArrayList<String>(Arrays.asList(companionList));
			return companionPhoneNumberList;
		}
		return null;
	}
	
	
	private PairingGroupInfo createPairingGroup(String primaryTn, String selfCompanionTn, String featureParameterValue, String categoryCode) {
		if (selfCompanionTn != null) { // this implies this is a companion record
			return createCompanionPairingGroup(primaryTn, selfCompanionTn, featureParameterValue, categoryCode);
		}else {
			return createPrimaryPairingGroup(primaryTn, featureParameterValue, categoryCode);
		}
	}
	
	private PairingGroupInfo createCompanionPairingGroup(String primaryTn, String selfCompanionTn, String featureParameterValue, String categoryCode) {
		if (primaryTn == null || primaryTn.trim().isEmpty()) { //orphan companion
			return null;
		}
		
		PairingGroupInfo pairingGroup = new PairingGroupInfo();
		pairingGroup.setPairingModeByCategory(categoryCode);
		
		
		List<String> companionPhoneNumberList = extractCompanionList(featureParameterValue, PARAMETER_NAME_COMPANION_TN_IN_COMPANION); //applicable to Husky only
		if(companionPhoneNumberList !=null && companionPhoneNumberList.isEmpty() == false){
			pairingGroup.setCompanionPhoneNumberList(companionPhoneNumberList);;
		}
		pairingGroup.getCompanionPhoneNumberList().add(selfCompanionTn);
		pairingGroup.setPrimaryPhoneNumber(primaryTn);
		
		return pairingGroup;
	}
	
	private PairingGroupInfo  createPrimaryPairingGroup(String primaryTn, String featureParameterValue, String categoryCode) {
		String parameterName = CommunicationSuiteInfo.PRIMARY_STANDALONE_FEATURE_CATEGORY_CODE.equals(categoryCode) ? PARAMETER_NAME_COMPANION_TNS_IN_STANDALONE_PRIMARY : PARAMETER_NAME_COMPANION_TN_IN_PRIMARY; 
		List<String> companionPhoneNumberList = extractCompanionList(featureParameterValue, parameterName);
		
		PairingGroupInfo pairingGroup = null;
		
		if (companionPhoneNumberList != null && companionPhoneNumberList.isEmpty() == false) {
			pairingGroup = new PairingGroupInfo();
			pairingGroup.setPairingModeByCategory(categoryCode);
			pairingGroup.setCompanionPhoneNumberList(companionPhoneNumberList);
			pairingGroup.setPrimaryPhoneNumber(primaryTn);
		}
		
		return pairingGroup;
		
	}

	
	private static String extractValue(String parameterValue, String parameterName) {
		if (parameterValue == null || parameterValue.trim().length() < parameterName.length()) {
			return "";
		}

		String searchString = parameterName + "=";
		int searchIndex = parameterValue.indexOf(searchString);
		if (searchIndex >= 0) {
			int endIndex = parameterValue.indexOf("@", searchIndex);
			return parameterValue.substring(searchIndex + searchString.length(), endIndex);
		}

		return "";
	}
	
/** [ Naresh.A ] : leaving below code here as examples how we parse the feature param's..
 * public static void main (String[] args) throws Throwable {
		String testStr = "COMPANION_TNS=4162222222,4163333333@";
		String testStr2 = "PRIMARY_TN=4161111111@COMPANION_TNS_2=4163333333,4164444444@";
		String testStr3 = "PRIMARY_TN=@COMPANION_TNS_2=@";
		String testStr4 = "COMPANION_TNS=@";

		System.out.println(extractValue (testStr, PARAMETER_NAME_COMPANION_TN_IN_PRIMARY));
		System.out.println(extractValue (testStr, PARAMETER_NAME_PRIMARY_TN));
		System.out.println(extractValue (testStr2, PARAMETER_NAME_COMPANION_TN_IN_PRIMARY));
		System.out.println(extractValue (testStr2, PARAMETER_NAME_PRIMARY_TN));
		System.out.println(extractValue (testStr2, PARAMETER_NAME_COMPANION_TN_IN_COMPANION));
		System.out.println(extractValue (testStr3, PARAMETER_NAME_PRIMARY_TN));
		System.out.println(extractValue (testStr3, PARAMETER_NAME_COMPANION_TN_IN_COMPANION));
		System.out.println(extractValue (testStr4, PARAMETER_NAME_COMPANION_TN_IN_PRIMARY));
	} */

}
