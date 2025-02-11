package com.telus.cmb.account.informationhelper.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.telus.eas.account.info.AccountInfo;

public class ContactNameMapper {
	public static void transform (AccountInfo accountInfo, ResultSet resultSet) throws SQLException {
		if (accountInfo == null || resultSet == null) {
			return;
		}
		
		if (resultSet.next()) {
			accountInfo.getContactName().setFirstName(resultSet.getString("first_name"));
			accountInfo.getContactName().setLastName(resultSet.getString("last_business_name"));
			accountInfo.getContactName().setTitle(resultSet.getString("name_title"));
			accountInfo.getContactName().setMiddleInitial(resultSet.getString("middle_initial"));
			accountInfo.getContactName().setAdditionalLine(resultSet.getString("additional_title"));
			accountInfo.getContactName().setGeneration(resultSet.getString("name_suffix"));
		}
	}
}
