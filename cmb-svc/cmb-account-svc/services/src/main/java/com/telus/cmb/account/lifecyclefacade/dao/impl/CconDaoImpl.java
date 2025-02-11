package com.telus.cmb.account.lifecyclefacade.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclefacade.dao.CconDao;
import com.telus.cmb.account.payment.CCMessage;

public class CconDaoImpl extends JdbcDaoSupport implements CconDao {
	
	@Override
	public Map<String, HashMap<String, CCMessage>> loadAppMessages() throws ApplicationException {
		String queryStr = "SELECT * FROM APP_MESSAGES";
		
		return getJdbcTemplate().query(queryStr, new ResultSetExtractor <Map<String, HashMap<String, CCMessage>>>() {
			
			@Override
			public Map<String, HashMap<String, CCMessage>> extractData(ResultSet rs)	throws SQLException, DataAccessException {
				
				HashMap<String, HashMap<String, CCMessage>> appMessages =
					new HashMap<String, HashMap<String, CCMessage>>();
				
				while (rs.next()) {
					String appId = rs.getString("APP_ROLE_ID");
					String responseCode = rs.getString("RESPONSE_CODE");

					CCMessage ccMessage = new CCMessage();
					ccMessage.setEnglishMessage(rs.getString("RESPONSE_MESSAGE_EN"));
					ccMessage.setFrenchMessage(rs.getString("RESPONSE_MESSAGE_FR"));
					ccMessage.setKbMemoMessage(rs.getString("RESPONSE_KB"));

					if (appId != null && responseCode != null) {
						HashMap<String, CCMessage> codeMessages = (HashMap<String, CCMessage>) appMessages.get(appId);
						if (codeMessages == null)
							codeMessages = new HashMap<String, CCMessage>();

						codeMessages.put(responseCode, ccMessage);

						appMessages.put(appId, codeMessages);
					}
				}
				return appMessages;
			}
		}) ;
	
	
	}
	
	
	@Override
	public Map<String, String> loadErrorCodeMappings()	throws ApplicationException {
		String queryStr = "SELECT * FROM APP_TMPG_MESSAGES WHERE APP_ROLE_ID='ECA_WPS'";
		return getJdbcTemplate().query(queryStr, new ResultSetExtractor <Map<String, String>>() {

			@Override
			public Map<String, String> extractData(ResultSet rs) throws SQLException,	DataAccessException {
				HashMap<String, String> codeMapping = new HashMap<String, String>();
				while (rs.next()) {
					String tmpgCode = rs.getString("TMPG_CODE");
					String responseCode = rs.getString("RESPONSE_CODE");
					codeMapping.put(tmpgCode.trim(), responseCode.trim());
				}
				return codeMapping;			
			}
		}); 	

	}
}
