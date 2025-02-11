package com.telus.cmb.subscriber.kafka.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.telus.cmb.common.kafka.subscriber_v2.Equipment;
import com.telus.cmb.common.kafka.subscriber_v2.SubscriberEvent;

public class EquipmentChangeEventInfo extends SubscriberEvent implements ChangeEventInfo {

	private static final long serialVersionUID = 1L;
	
	@JsonInclude(Include.NON_NULL)
	private Equipment oldEquipment;

	@JsonInclude(Include.NON_NULL)
	private String sessionId;

	public Equipment getOldEquipment() {
		return oldEquipment;
	}

	public void setOldEquipment(Equipment equipment) {
		this.oldEquipment = equipment;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
