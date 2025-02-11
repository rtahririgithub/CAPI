package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import com.telus.cmb.common.kafka.subscriber_v2.SeatData;
import com.telus.cmb.common.kafka.subscriber_v2.Resource;


public class SeatDataMapper {
	public static SeatData mapSeatData(com.telus.api.account.SeatData info) {
		SeatData seatData = new SeatData();
		seatData.setSeatType(info.getSeatType());
		seatData.setSeatGroup(info.getSeatGroup());
		for (com.telus.api.resource.Resource resource : info.getResources()) {
			seatData.getSeatResourceList().add(mapSeatResource(resource));
		}
		return seatData;
	}

	private static Resource mapSeatResource(com.telus.api.resource.Resource resourceData) {
		Resource resource = new Resource();
		resource.setResourceType(resource.getResourceType());
		resource.setResourceNumber(resource.getResourceNumber());
		resource.setResourceStatus(resource.getResourceStatus());
		return resource;
	}
}
