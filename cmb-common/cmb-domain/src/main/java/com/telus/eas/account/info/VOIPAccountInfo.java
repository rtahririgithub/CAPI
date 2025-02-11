/*
 * $Id$
 *
 * Copyright Telus.  All rights reserved.  Proprietary and Confidential.
 * @author
 */
package com.telus.eas.account.info;

import com.telus.eas.framework.info.Info;

public class VOIPAccountInfo extends Info {

	private static final long serialVersionUID = 1L;

	private int ban;
	private long operatorSubscriptionId;
	private String mainCompanyNumber;
    private String servicePlanId;
    private String serviceEditionCode;
    private String statusCode;
    private String phoneNumber;

    public VOIPAccountInfo() { }
    
	public int getBan() {
		return ban;
	} 

	public void setBan(int ban) {
		this.ban = ban;
	}

	public long getOperatorSubscriptionId() {
		return operatorSubscriptionId;
	}

	public void setOperatorSubscriptionId(long operatorSubscriptionId) {
		this.operatorSubscriptionId = operatorSubscriptionId;
	}

	public String getMainCompanyNumber() {
		return mainCompanyNumber;
	}

	public void setMainCompanyNumber(String mainCompanyNumber) {
		this.mainCompanyNumber = mainCompanyNumber;
	}

	public String getServicePlanId() {
		return servicePlanId;
	}

	public void setServicePlanId(String servicePlanId) {
		this.servicePlanId = servicePlanId;
	}

	public String getServiceEditionCode() {
		return serviceEditionCode;
	}

	public void setServiceEditionCode(String serviceEditionCode) {
		this.serviceEditionCode = serviceEditionCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("VOIPAccountInfo:[\n");
		s.append("    ban=[").append(ban).append("]\n");
		s.append("    operatorSubscriptionId=[").append(operatorSubscriptionId).append("]\n");
		s.append("    mainCompanyNumber=[").append(mainCompanyNumber).append("]\n");
		s.append("    servicePlanId=[").append(servicePlanId).append("]\n");
		s.append("    serviceEditionCode=[").append(serviceEditionCode).append("]\n");
		s.append("    statusCode=[").append(statusCode).append("]\n");
		s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
		s.append("super=" + super.toString());

		return s.toString();
	}	
	
}