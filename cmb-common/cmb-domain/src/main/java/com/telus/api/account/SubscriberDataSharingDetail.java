package com.telus.api.account;

import java.util.Collection;
import java.util.Date;

/**
 * This interface defines the result of calling the getSubscriberDataSharingInfo method.
 */
public interface SubscriberDataSharingDetail {

	public String getSubscriberId();
	public Date getContractStartDate();
	public long getSubscriptionId();
	public String getPricePlanCode();
	public double getPricePlanRecurringCharge();
	public Collection<NonDataSharingRegularSoc> getNonDataSharingRegularSocList();
	public Collection<DataSharingDetail> getDataSharingInfoList();

	public interface NonDataSharingRegularSoc {
		
		public String getSocCode();
		public Collection<String> getFamilyTypes();
		public double getSocRecurringCharge();
	}
	
	public interface DataSharingDetail {
		
		public String getDataSharingGroupCode();
		public Collection<DataSharingSoc> getDataSharingSocList();
	}
	
	public interface DataSharingSoc {
		
		public String getDataSharingSocCode();
		public String getDataSharingSocDescription();
		public String getDataSharingSocDescriptionFrench();
		public Collection<String> getFamilyTypes();
		public double getDataSharingSpentAmt();
		public boolean getContributingInd();
		
	}
	
	public interface DataSharingResult {
		
		public String getSubscriberId();
		public String getSocCode();
		public String getSocDescription();
		public String getSocDescriptionFrench();
		public String getServiceType();
		public double getRate();
		public String getAllowSharingGroupCd();
		public String getAllowSharingAccessTypeCd();
	}
}
