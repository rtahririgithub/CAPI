package com.telus.api.reference;

public interface FeatureAirTimeAllocation extends Reference {

	ServiceFeatureClassification getClassification();
	
	String getFeatureGroup();
	boolean isPeriodBased();  //byte ucPeriodInd;
	String getInclusiveQuantityType();
	String getPeriodName();
	String getPeriodValueCode();
	String getUsageChargeDependCode();
	int getTierLevelCode();
	
	double getFromQuantity(); //double fromQuantity;
	double getToQuantity(); //double toQuantity;
	boolean isPooling(); //byte poolingInd;
	boolean isSharing(); //byte sharingInd;
	boolean isMinimumCommitmentRequired();	//public byte mcInd;
	String getCallingDirection();	//byte actionDirectionCode;
	String getParameterType();  //byte svcFtrParamInd;
	String getParameterName();  //String paramName;
	String getParameterValue(); //String paramVal;
	String  getFreeMinuteType(); //byte fmType;
	String getCallingRouteType(); //byte callingRouteType;
	double getFreeInlcudedMinutes(); //double fmImAllowed;
	
	boolean isUsageCharge();  //byte ucInfoInd;
	double getUsageCharge();  //double ucRate;
	boolean isRecurringCharge();  //byte rcInfoInd;
	double getRecurringCharge(); //double rcRate;
	boolean isOneTimeCharge(); //byte ocInfoInd;
	double getOneTimeCharge();  //double ocRate;
	
	boolean isSwitchActionRequired(); //byte switchActNeeded;
	String getSwitchCode(); //String switchCode;
	String getSwitchParameter(); //String switchParam;
	
}
