package com.telus.api.account;

import java.util.Date;

public interface DepositAssessedHistory  {

	String getDescription();
  String getDescriptionFrench();
  String getUser();
	Date getChangeDate();
	String getProductType();
	double getDepositAssessed();

}