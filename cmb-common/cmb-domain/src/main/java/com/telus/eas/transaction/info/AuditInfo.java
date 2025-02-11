package com.telus.eas.transaction.info;

import java.io.Serializable;
import java.util.Date;

public class AuditInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	
	/**
	 * User type code. Possible values are:
	 *	1	TELUS Employee ID
	 *	2	KB User ID
	 *	3	Channel Org/Outlet/Sales Rep ID
	 *	4	Web/Portal ID
	 *	5	SOA Application ID
	 */
	private String userTypeCode;
	
	private String salesRepId;
	 
	private String channelOrgId;
	 
	private String outletId;
	 
	private String originatorAppId;

	private String correlationId;

	private Date timestamp;
	
	private boolean  kbAppCode;
	
	private boolean sendToParentRoleInd;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserTypeCode() {
		return userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode) {
		this.userTypeCode = userTypeCode;
	}

	public String getSalesRepId() {
		return salesRepId;
	}

	public void setSalesRepId(String salesRepId) {
		this.salesRepId = salesRepId;
	}

	public String getChannelOrgId() {
		return channelOrgId;
	}

	public void setChannelOrgId(String channelOrgId) {
		this.channelOrgId = channelOrgId;
	}

	public String getOutletId() {
		return outletId;
	}

	public void setOutletId(String outletId) {
		this.outletId = outletId;
	}

	public String getOriginatorAppId() {
		return originatorAppId;
	}

	public void setOriginatorAppId(String originatorAppId) {
		this.originatorAppId = originatorAppId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isinternalPopulatedAppId() {
		return kbAppCode;
	}

	public void setinternalPopulatedAppId(boolean internalPopulated) {
		this.kbAppCode = internalPopulated;
	}

	public boolean isSendToParentRoleInd() {
		return sendToParentRoleInd;
	}

	public void setSendToParentRoleInd(boolean sendToParentRoleInd) {
		this.sendToParentRoleInd = sendToParentRoleInd;
	}
}
