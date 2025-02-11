package com.telus.cmb.account.lifecyclemanager.dao;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.CollectionStateInfo;

public interface CollectionDao {
	
	 CollectionStateInfo retrieveBanCollectionInfo(int ban, String sessionId) throws ApplicationException;
	 void updateNextStepCollection(int ban, int stepNumber, Date stepDate, String pathCode, String sessionId) throws ApplicationException; 
}
