package com.telus.cmb.account.payment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.telus.cmb.account.lifecyclefacade.dao.CconDao;

public class MessageResolver {
	
	private static Logger logger = Logger.getLogger(MessageResolver.class);

	private static final String APP_ID_DEFAULT = "AMDOCS";
	private static final String ROLE_ID_DEFAULT = "DEFAULT";

	public static final String TM_CREDIT_CARD_API_ERRNUM_APPLICATION_MESSAGES = "MSG001";
	public static final String TM_CREDIT_CARD_API_ERROR_APPLICATION_MESSAGES = "Missing Telus Mobility Payment Gateway response message definitions. Please contact the application support to set TMPG response message definitions.";
	
	private static Map<String,String> WPS_ERRORCODE_MAPPING = new HashMap<String,String>();
	private static Map<String, HashMap<String, CCMessage>> APP_MESSAGES = new HashMap<String, HashMap<String, CCMessage>>();
	private static long REFRESH_TIME=0;
	
	
	public MessageResolver(CconDao cconDao) {
		init(cconDao);
	}

	public CCMessage resolveMessage(String applicationID, String businessRole, String msgId, String errorMessage) {
		
		try {
			// check out the business role. If business role is empty we set up DEFAULT value
			if (businessRole == null || businessRole.trim().equals(""))	businessRole = ROLE_ID_DEFAULT;
			
			HashMap<String, CCMessage> msg = APP_MESSAGES.get(applicationID + "_"+ businessRole);
			if (msg == null) {
				// we are going to use AMDOCS default application id
				applicationID = APP_ID_DEFAULT;
				msg = APP_MESSAGES.get(applicationID + "_"	+ businessRole);
			}
			
			if (msg == null && businessRole.equals(ROLE_ID_DEFAULT)==false )  {
				msg =  APP_MESSAGES.get(applicationID + "_"+ ROLE_ID_DEFAULT);
			}

			if (msg != null) {
				// retrieve application RESPONSE_MESSAGE
				CCMessage response = (CCMessage) msg.get(msgId);
				if (response != null) {
					return response;
				}
			}
			
			return new CCMessage(errorMessage);
			
		} catch (Exception e) {
			logger.error( "Exception occurred: " + e.getMessage(), e  );
			return new CCMessage(errorMessage);
		}
	}
	
	public String mapMessageId(String wpsErrorCode) {		
		return WPS_ERRORCODE_MAPPING.get(wpsErrorCode);
	}
	
	private void init(CconDao cconDao ) {

		long now = System.currentTimeMillis();

		if ( now > REFRESH_TIME ) {
			
			logger.debug("begin to load WPS messages" );
			try {
				APP_MESSAGES = cconDao.loadAppMessages();
				WPS_ERRORCODE_MAPPING = cconDao.loadErrorCodeMappings();
				
				//setup next time for reloading the message: next day 5AM
				Calendar nextRefresh = Calendar.getInstance();
				nextRefresh.add(Calendar.DATE, 1);
				nextRefresh.set(Calendar.HOUR_OF_DAY, 5);
				nextRefresh.set(Calendar.MINUTE, 0);
				nextRefresh.set(Calendar.SECOND, 0);
				
				REFRESH_TIME = nextRefresh.getTimeInMillis();
				logger.debug("finish loading WPS messages, set next refresh time:" + new Date(REFRESH_TIME ));
				
			} catch (Throwable e) {
				logger.error( "loading WPS error mapping / message failed.", e);
			}
		}
	}
	

	
}