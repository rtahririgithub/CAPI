package com.telus.cmb.common.kafka;

public enum KafkaContentType {
	XML("application/xml"), JSON("application/json");

	private String contentType;

	KafkaContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getValue() {
		return this.contentType;
	}
}
