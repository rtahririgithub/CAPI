package com.telus.cmb.subscriber.domain;

import com.telus.eas.framework.info.Info;

public class CommunicationSuiteRepairData extends Info {
	public static final String COMM_SUITE_CTN_CHANGE_REPAIR_REASON_CODE = "CTN_CHNG";
	public static final String COMM_SUITE_SUB_RESUME_REPAIR_REASON_CODE = "SUB_RESUME_FROM_CANCEL";
	public static final String COMM_SUITE_RESYNC_ACTION_CODE = "SUITE_RESYNC";
	public static final String COMM_SUITE_RESET_ACTION_CODE = "SUITE_RESET";

	private static final long serialVersionUID = 1L;

	private int ban;

	private String sessionId;

	private String actionCode;
	private String reasonCode;
	private boolean applyToWholeSuiteInd = Boolean.TRUE;
	private String oldSubscriberId;

	private String subscriberId;

	public int getBan() {
		return ban;
	}

	public void setBan(int ban) {
		this.ban = ban;
	}

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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	@Override
	public String toString() {
		return "CommunicationSuiteRepairData [ban=" + ban + ", sessionId=" + sessionId + ", actionCode=" + actionCode + ", reasonCode=" + reasonCode + ", applyToWholeSuiteInd=" + applyToWholeSuiteInd
				+ ", oldSubscriberId=" + oldSubscriberId + ", subscriberId=" + subscriberId + "]";
	}
}