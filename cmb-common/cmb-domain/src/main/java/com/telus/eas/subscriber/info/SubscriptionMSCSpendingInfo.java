package com.telus.eas.subscriber.info;

import com.telus.eas.framework.info.Info;

public class SubscriptionMSCSpendingInfo extends Info {

	private static final long serialVersionUID = 1L;
	
	private String subscriptionId;
	private Double planSpentAmount;
	private Double serviceSpentAmount;

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Double getPlanSpentAmount() {
		return planSpentAmount;
	}

	public void setPlanSpentAmount(Double planSpentAmount) {
		this.planSpentAmount = planSpentAmount;
	}

	public Double getServiceSpentAmount() {
		return serviceSpentAmount;
	}

	public void setServiceSpentAmount(Double serviceSpentAmount) {
		this.serviceSpentAmount = serviceSpentAmount;
	}

}
