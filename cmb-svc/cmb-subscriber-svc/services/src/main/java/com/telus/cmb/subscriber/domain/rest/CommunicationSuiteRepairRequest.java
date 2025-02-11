package com.telus.cmb.subscriber.domain.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.telus.cmb.subscriber.domain.CommunicationSuiteRepairData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunicationSuiteRepairRequest {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("actionCd")
	private String actionCode;

	@JsonProperty("reasonCd")
	private String reasonCode;

	@JsonProperty("applyToWholeSuiteInd")
	private boolean applyToWholeSuiteInd = Boolean.TRUE;
	
	@JsonProperty("previousCtn")
	private String oldSubscriberId;

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public boolean isApplyToWholeSuiteInd() {
		return applyToWholeSuiteInd;
	}

	public void setApplyToWholeSuiteInd(boolean applyToWholeSuiteInd) {
		this.applyToWholeSuiteInd = applyToWholeSuiteInd;
	}

	public String getOldSubscriberId() {
		return oldSubscriberId;
	}

	public void setOldSubscriberId(String oldSubscriberId) {
		this.oldSubscriberId = oldSubscriberId;
	}
	
	public static CommunicationSuiteRepairRequest fromData(CommunicationSuiteRepairData data ) {
		CommunicationSuiteRepairRequest request = null;
		if (data != null) {
			request = new CommunicationSuiteRepairRequest();
			request.setActionCode(data.getActionCode());
			request.setApplyToWholeSuiteInd(data.isApplyToWholeSuiteInd());
			request.setOldSubscriberId(data.getOldSubscriberId());
			request.setReasonCode(data.getReasonCode());
		}
		
		return request;
	}

	@Override
	public String toString() {
		return "CommunicationSuiteMgmtRESTRequest [actionCode=" + actionCode + ", reasonCode=" + reasonCode + ", applyToWholeSuiteInd=" + applyToWholeSuiteInd + ", oldSubscriberId=" + oldSubscriberId
				+ "]";
	}
	
	
}
