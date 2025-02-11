package com.telus.eas.subscriber.info;

import java.util.Date;

import com.telus.api.account.CallingCircleCommitmentAttributeData;
import com.telus.eas.framework.info.Info;

public class CallingCircleCommitmentAttributeDataInfo extends Info implements	CallingCircleCommitmentAttributeData {
	private static final long serialVersionUID = 1L;
	
	private Date effectiveDate;
	private int totalAllowedModifications;
	private boolean prepaidModificationBlocked;
	private int remainingAllowedModifications;
	
	private int modificationCount;
	
	public CallingCircleCommitmentAttributeDataInfo() {
		
	}
	
	public CallingCircleCommitmentAttributeDataInfo(int totalAllowedModifications) {
		this.totalAllowedModifications = totalAllowedModifications;
		this.remainingAllowedModifications = totalAllowedModifications;
	}
	
	public int getModificationCount() {
		return modificationCount;
	}
	public void setModificationCount(int modificationCount) {
		this.modificationCount = modificationCount;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public int getTotalAllowedModifications() {
		return totalAllowedModifications;
	}
	public void setTotalAllowedModifications(int totalAllowedModifications) {
		this.totalAllowedModifications = totalAllowedModifications;
	}
	public int getRemainingAllowedModifications() {
		return remainingAllowedModifications;
	}
	public void setRemainingAllowedModifications(int remainingAllowedModifications) {
		this.remainingAllowedModifications = remainingAllowedModifications;
	}
	public boolean isPrepaidModificationBlocked() {
		return prepaidModificationBlocked;
	}
	public void setPrepaidModificationBlocked(boolean prepaidModificationBlocked) {
		this.prepaidModificationBlocked = prepaidModificationBlocked;
	}
}
