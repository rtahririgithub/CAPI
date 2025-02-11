package com.telus.cmb.subscriber.kafka;

import java.util.Date;

import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.eas.framework.info.Info;
import com.telus.eas.transaction.info.AuditInfo;

public class ChangePhoneNumberTransactionEventInfo extends Info {

	private static final long serialVersionUID = 1L;

	private KafkaEventType eventType;
	private boolean notificationSuppressionInd;
	private AuditInfo auditInfo;
	private String salesRepCode;
	private String dealerCode;
	private Date transactionDate;
	private int ban;
	private String oldPhoneNumber;
	private String newPhoneNumber;
	private boolean portIn;
	private String portProcess;
	private String sessionId;
	
	public KafkaEventType getEventType() {
		return eventType;
	}
	public void setEventType(KafkaEventType eventType) {
		this.eventType = eventType;
	}	
	public boolean isNotificationSuppressionInd() {
		return notificationSuppressionInd;
	}
	public void setNotificationSuppressionInd(boolean notificationSuppressionInd) {
		this.notificationSuppressionInd = notificationSuppressionInd;
	}
	public AuditInfo getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
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
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}	
	public int getBan() {
		return ban;
	}
	public void setBan(int ban) {
		this.ban = ban;
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
	public boolean isPortIn() {
		return portIn;
	}
	public void setPortIn(boolean portIn) {
		this.portIn = portIn;
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
