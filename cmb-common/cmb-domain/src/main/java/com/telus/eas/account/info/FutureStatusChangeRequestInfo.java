/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */
package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.FutureStatusChangeRequest;
import com.telus.eas.framework.info.Info;

/**
 * Title:        FutureStatusChangeRequestInfo<p>
 * Description:  The FutureStatusChangeRequestInfo class holds all future status change request related attributes for an account.<p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author R. Fong
 * @version 1.0
 */
public class FutureStatusChangeRequestInfo extends Info implements FutureStatusChangeRequest {

	 static final long serialVersionUID = 1L;

	private String subscriberId;
	private int ban;
	private long sequenceNumber;	
	private String phoneNumber;	
	private String productType;	
	private String activityCode;	
	private String activityReasonCode;	
	private Date createDate;	
	private Date effectiveDate;
	private String updateFlag;
	
	public FutureStatusChangeRequestInfo() {
	}
	
	/**
	 * @return Returns the activityCode.
	 */
	public String getActivityCode() {
		return activityCode;
	}	
	/**
	 * @param activityCode The activityCode to set.
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	
	/**
	 * @return Returns the activityReasonCode.
	 */
	public String getActivityReasonCode() {
		return activityReasonCode;
	}
	/**
	 * @param activityReasonCode The activityReasonCode to set.
	 */
	public void setActivityReasonCode(String activityReasonCode) {
		this.activityReasonCode = activityReasonCode;
	}
	
	/**
	 * @return Returns the ban.
	 */
	public int getBan() {
		return ban;
	}
	/**
	 * @param ban The ban to set.
	 */
	public void setBan(int ban) {
		this.ban = ban;
	}
	
	/**
	 * @return Returns the createDate.
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate The createDate to set.
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	/**
	 * @return Returns the effectiveDate.
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate The effectiveDate to set.
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	/**
	 * @return Returns the phoneNumber.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber The phoneNumber to set.
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * @return Returns the productType.
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType The productType to set.
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	/**
	 * @return Returns the sequenceNumber.
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	/**
	 * @param sequenceNumber The sequenceNumber to set.
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	/**
	 * @return Returns the subscriberId.
	 */
	public String getSubscriberId() {
		return subscriberId;
	}
	/**
	 * @param subscriberId The subscriberId to set.
	 */
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	
	/**
	 * @return Returns the updateFlag.
	 */
	public String getUpdateFlag() {
		return updateFlag;
	}
	/**
	 * @param updateFlag The updateFlag to set.
	 */
	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
	    s.append("FutureStatusRequestInfo:{\n");
	    s.append("    activityCode=[").append(activityCode).append("]\n");
	    s.append("    activityReasonCode=[").append(activityReasonCode).append("]\n");
	    s.append("    ban=[").append(ban).append("]\n");
	    s.append("    createDate=[").append(createDate).append("]\n");
	    s.append("    effectiveDate=[").append(effectiveDate).append("]\n");
	    s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
	    s.append("    productType=[").append(productType).append("]\n");
	    s.append("    sequenceNumber=[").append(sequenceNumber).append("]\n");
	    s.append("    subscriberId=[").append(subscriberId).append("]\n");
	    s.append("    updateFlag=[").append(updateFlag).append("]\n");
	    s.append("}");
	    return s.toString();
	}
	
	public void save() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	public void delete() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
}
