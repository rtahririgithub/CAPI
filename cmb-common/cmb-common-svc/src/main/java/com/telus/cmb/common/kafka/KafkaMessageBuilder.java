package com.telus.cmb.common.kafka;

public interface KafkaMessageBuilder {

	void populate(KafkaMessage message) throws Throwable;
	
}
