package com.telus.cmb.notification.management.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.erm.referenceods.domain.RuleOutput;
import com.telus.erm.refpds.access.client.ReferencePdsAccess;

public class ReferencePdsWrapper {

	private static final Log LOGGER = LogFactory.getLog(ReferencePdsWrapper.class);
	
	public static String[] getFormLetterTemplate(String transactionType, int brandId, String accountType, String banSegment, String productType, String deliveryChannel, String language) throws ApplicationException {		
		HashMap<String, String> criteria = new HashMap<String, String>();
		criteria.put("TRANSACTION_TYPE", transactionType);
		criteria.put("BRAND_ID", String.valueOf(brandId));
		criteria.put("ACCOUNT_TYPE_SUBTYPE", accountType);
		criteria.put("BAN_SEGMENT", banSegment);
		if ( productType!=null ) {
			criteria.put("PRODUCT_TYPE", productType);
		}
		criteria.put("DELIVERY_CHANNEL_CD", deliveryChannel);
		if (language==null || language.trim().length()==0 ) {
			language = "EN";
		}
		criteria.put("LANGUAGE_CD", language);
		
		RuleOutput ruleOutput = ReferencePdsAccess.evaluateRule("WIRELESS_TRANSACTION_NOTIFICATION_TEMPLATE", criteria);
		if ( ruleOutput==null ) {
			throw new ApplicationException ( "ReferencePdsWrapper", "No GCC template found  for input:" + criteria , null);
		}
		
		try {
			
			String categoryCode = ruleOutput.getValue("CATEGORY_CD");
			String templateCode = ruleOutput.getValue("TEMPLATE_CD");
			String schemaVersionCode = "1.0";
			try {
				schemaVersionCode = ruleOutput.getValue("SCHEMA_VERSION_CD");
			}catch (Throwable t) {
				//TODO remove this try-catch block once the table has been updated in REFPDS
				if (templateCode != null && templateCode.endsWith("V2")) {
					schemaVersionCode = "2.0";
				}
			}
			
			return new String[]{ categoryCode,templateCode, schemaVersionCode};
			
		} catch (RuntimeException e ) {
			LOGGER.error("Encountered error while extracting rule output, criteria:" + criteria  + ruleOutput );
			throw e;
		}
	}
	
	public static boolean isNotificationEligible(String transactionType,String originatingeApp, int brandId, String accountType, String banSegment, String productType) {
		
		Map<String, String> criteria = new LinkedHashMap<String, String>();
		criteria.put("TRANSACTION_TYPE", transactionType);
		criteria.put("ORIGINATING_APPLICATION_ID", originatingeApp);
		criteria.put("BRAND_ID", String.valueOf(brandId));
		criteria.put("ACCOUNT_TYPE_SUBTYPE", accountType);
		criteria.put("BAN_SEGMENT", banSegment);
		if ( productType!=null ) {
			criteria.put("PRODUCT_TYPE", productType);
		}
		
		String tableName = "WIRELESS_TRANSACTION_NOTIFICATION_ELIGIBILITY";
		RuleOutput ruleOutput = ReferencePdsAccess.evaluateRule(tableName, criteria);

		boolean result = false;
		
		if ( ruleOutput!=null) {
			try {
				result = "YES".equalsIgnoreCase(ruleOutput.getValue("NOTIFICATION_IND"));
			}catch (RuntimeException e ) {
				LOGGER.error("Encountered error while extracting data from RuleOutput: " + ruleOutput );
				throw e;
			}
		}
		
		if ( result==false ) {
			LOGGER.info( "notification not eligible input:" + criteria );
		}
		
		return (result);
	}
}
