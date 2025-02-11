package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import com.telus.cmb.common.kafka.subscriber_v2.ConsumerName;


public class ConsumerNameMapper {
	public static ConsumerName mapConsumerName(com.telus.api.account.ConsumerName source) {
		ConsumerName consumerName = new ConsumerName();
		consumerName.setTitle(source.getTitle());
		consumerName.setFirstName(source.getFirstName());
		consumerName.setMiddleInitial(source.getMiddleInitial());
		consumerName.setLastName(source.getLastName());
		consumerName.setAdditionalLine(source.getAdditionalLine());
		consumerName.setGeneration(source.getGeneration());
		consumerName.setNameFormat(source.getNameFormat());
		return consumerName;
	}
}
