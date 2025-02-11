package com.telus.cmb.subscriber.kafka.domain;

import java.io.Serializable;
import java.util.Date;

import com.telus.cmb.common.kafka.subscriber_v2.AuditInfo;

public class PhoneNumberChangeEventInfo implements ChangeEventInfo, Serializable {

	private static final long serialVersionUID = 1L;
	
	private String eventType;	
	private Date transactionDate;
	private AuditInfo auditInfo;
    private int ban;
	private String subscriberId;
	private String oldPhoneNumber;
	private String newPhoneNumber;
	private String portProcess;
	private String sessionId;

	
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}	
	public AuditInfo getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}    
    public int getBan() {
        return ban;
    }
    public void setBan(int ban) {
        this.ban = ban;
    }
	public String getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	public String getOldPhoneNumber() {
		return oldPhoneNumber;
	}
	public void setOldPhoneNumber(String oldPhoneNumber) {
		this.oldPhoneNumber = oldPhoneNumber;
	}
	public String getNewPhoneNumber() {
		return newPhoneNumber;
	}
	public void setNewPhoneNumber(String newPhoneNumber) {
		this.newPhoneNumber = newPhoneNumber;
	}
	public String getPortProcess() {
		return portProcess;
	}
	public void setPortProcess(String portProcess) {
		this.portProcess = portProcess;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
