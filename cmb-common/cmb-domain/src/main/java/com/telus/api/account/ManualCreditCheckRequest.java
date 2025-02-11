package com.telus.api.account;

import java.util.Date;

import com.telus.api.TelusAPIException;

public interface ManualCreditCheckRequest {
	
	void setBusinessPhone(String phoneNumber);

	void setHomePhone(String phoneNumber);

	void setIncorporationDate(Date incorporationDate);

	void setIncorporationNumber(String incorporationNumber);

	void setCompanyName(String companyName);

	int getBanId();

	char getAccountType();

	char getAccountSubType();

	char getStatus();

	ConsumerName getConsumerName();

	PersonalCredit getPersonalCreditInformation();

	int getBrandId();

	String getBusinessPhone();

	String getHomePhone();

	Address getAddress();

	PostpaidConsumerAccount transformToPostpaidConsumerAccount() throws TelusAPIException;

	PostpaidBusinessRegularAccount transformToBusinessRegularAccount() throws TelusAPIException;
	
}