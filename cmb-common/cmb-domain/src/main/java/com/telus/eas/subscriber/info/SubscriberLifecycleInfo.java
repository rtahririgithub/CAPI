package com.telus.eas.subscriber.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.framework.info.Info;

public class SubscriberLifecycleInfo extends Info {
	private String reasonCode;
	private String memoText;
	private String salesRepCode;
	private String dealerCode;
	private SubscriberInfo oldSubscriberInfo;
	private SubscriberInfo newSubscriberInfo;
	private List systemWarningList = new ArrayList();
	
	
	public SubscriberLifecycleInfo() {
	}
	
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasoncode) {
		this.reasonCode = reasoncode;
	}
	public String getMemoText() {
		return memoText;
	}
	public void setMemoText(String memoText) {
		this.memoText = memoText;
	}
	public String getSalesRepCode() {
		return salesRepCode;
	}
	public void setSalesRepCode(String salesRepCode) {
		this.salesRepCode = salesRepCode;
	}
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public SubscriberInfo getOldSubscriberInfo() {
		return oldSubscriberInfo;
	}
	public void setOldSubscriberInfo(SubscriberInfo oldSubscriberInfo) {
		this.oldSubscriberInfo = oldSubscriberInfo;
	}
	public SubscriberInfo getNewSubscriberInfo() {
		return newSubscriberInfo;
	}
	public void setNewSubscriberInfo(SubscriberInfo newSubscriberInfo) {
		this.newSubscriberInfo = newSubscriberInfo;
	}

	public List getSystemWarningList() {
		return systemWarningList;
	}

	public void setSystemWarningList(List systemWarningList) {
		this.systemWarningList = systemWarningList;
	}
	
	public void addSystemWarning(WarningFaultInfo systemWarning) {
		if (systemWarning != null) {
			systemWarningList.add(systemWarning);
		}
	}
	
	public void addSystemWarningList(WarningFaultInfo[] systemWarnings) {
		this.systemWarningList.addAll(Arrays.asList(systemWarnings));
	}
	
}

