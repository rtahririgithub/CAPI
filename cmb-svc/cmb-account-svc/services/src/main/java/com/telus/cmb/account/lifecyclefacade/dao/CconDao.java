package com.telus.cmb.account.lifecyclefacade.dao;

import java.util.HashMap;
import java.util.Map;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.payment.CCMessage;

public interface CconDao {
	
	Map<String, HashMap<String, CCMessage>> loadAppMessages() throws ApplicationException;
	Map<String,String> loadErrorCodeMappings() throws ApplicationException;

}
