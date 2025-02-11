package com.telus.eas.account.info;

import java.util.Date;


public class CreditBalanceTransferInfo implements  java.io.Serializable{

	/**
	 *  Naresh Annabathula
	 */
	private static final long serialVersionUID = 1L;

	private int creditBalanceTransferSeqNum;
	private int sourceBan;
	private int targetBan;
	private Date reqCreateDt;
	private Date statusUpdateDt;
	private char transferStatus;
	private int billCycle;
	private double chargeSeqNo;
	private int adjSeqNo;
	private double transferAmt;
	private Date systemCreationDate;
	private Date systemUpdateDate;
	private int operatorId;
	private String applicationId;
	private short updateStamp;
	private String failureCd;
	
	public int getCreditBalanceTransferSeqNum() {
		return creditBalanceTransferSeqNum;
	}

	public void setCreditBalanceTransferSeqNum(int creditBalanceTransferSeqNum) {
		this.creditBalanceTransferSeqNum = creditBalanceTransferSeqNum;
	}

	public int getSourceBan() {
		return sourceBan;
	}

	public void setSourceBan(int sourceBan) {
		this.sourceBan = sourceBan;
	}

	public int getTargetBan() {
		return targetBan;
	}

	public void setTargetBan(int targetBan) {
		this.targetBan = targetBan;
	}

	public Date getReqCreateDt() {
		return reqCreateDt;
	}

	public void setReqCreateDt(Date reqCreateDt) {
		this.reqCreateDt = reqCreateDt;
	}

	public Date getStatusUpdateDt() {
		return statusUpdateDt;
	}

	public void setStatusUpdateDt(Date statusUpdateDt) {
		this.statusUpdateDt = statusUpdateDt;
	}

	public char getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(char transferStatus) {
		this.transferStatus = transferStatus;
	}

	public int getBillCycle() {
		return billCycle;
	}

	public void setBillCycle(int billCycle) {
		this.billCycle = billCycle;
	}

	public double getChargeSeqNo() {
		return chargeSeqNo;
	}

	public void setChargeSeqNo(double chargeSeqNo) {
		this.chargeSeqNo = chargeSeqNo;
	}

	public int getAdjSeqNo() {
		return adjSeqNo;
	}

	public void setAdjSeqNo(int adjSeqNo) {
		this.adjSeqNo = adjSeqNo;
	}

	public double getTransferAmt() {
		return transferAmt;
	}
	public void setTransferAmt(double transferAmt) {
		this.transferAmt = transferAmt;
	}
	public Date getSystemCreationDate() {
		return systemCreationDate;
	}

	public void setSystemCreationDate(Date systemCreationDate) {
		this.systemCreationDate = systemCreationDate;
	}

	public Date getSystemUpdateDate() {
		return systemUpdateDate;
	}

	public void setSystemUpdateDate(Date systemUpdateDate) {
		this.systemUpdateDate = systemUpdateDate;
	}

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public short getUpdateStamp() {
		return updateStamp;
	}

	public void setUpdateStamp(short updateStamp) {
		this.updateStamp = updateStamp;
	}

	public String getFailureCd() {
		return failureCd;
	}

	public void setFailureCd(String failureCd) {
		this.failureCd = failureCd;
	}

	public String toString() {
		return "CreditBalanceTransferInfo [creditBalanceTransferSeqNum="
				+ creditBalanceTransferSeqNum + ", sourceBan=" + sourceBan
				+ ", targetBan=" + targetBan + ", reqCreateDt=" + reqCreateDt
				+ ", statusUpdateDt=" + statusUpdateDt + ", transferStatus="
				+ transferStatus + ", billCycle=" + billCycle
				+ ", chargeSeqNo=" + chargeSeqNo + ", adjSeqNo=" + adjSeqNo
				+ ", transferAmt=" + transferAmt + ", systemCreationDate="
				+ systemCreationDate + ", systemUpdateDate=" + systemUpdateDate
				+ ", operatorId=" + operatorId + ", applicationId="
				+ applicationId + ", updateStamp=" + updateStamp
				+ ", failureCd=" + failureCd + "]";
	}

	

	
}
