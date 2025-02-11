package com.telus.cmb.account.informationhelper.task;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.telus.cmb.account.informationhelper.mapping.LightWeightAccountRetrievalMapper;

public class LightweightAccountRetrievalTask extends AccountRetrievalTask {

	public LightweightAccountRetrievalTask(char accountType, char accountSubType, ResultSet resultSet) {
		super(accountType, accountSubType, resultSet);
	}

	@Override
	public void mapData() throws SQLException {
		LightWeightAccountRetrievalMapper mapper = new LightWeightAccountRetrievalMapper (accountInfo, resultSet, logicalDate);
		
		mapper.setCommonDetails();
	}

	
}
