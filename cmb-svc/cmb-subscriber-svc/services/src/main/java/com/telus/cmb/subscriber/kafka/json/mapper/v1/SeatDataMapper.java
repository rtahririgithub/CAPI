package com.telus.cmb.subscriber.kafka.json.mapper.v1;

import com.telus.api.resource.Resource;
import com.telus.cmb.common.kafka.subscriber_v1.ResourceData;
import com.telus.cmb.common.kafka.subscriber_v1.SeatData;

public class SeatDataMapper {
	public static SeatData mapSeatData(com.telus.api.account.SeatData info) {
		SeatData seatData = new SeatData();
		seatData.setSeatType(info.getSeatType());
		seatData.setSeatGroup(info.getSeatGroup());
		Resource[] resources = info.getResources();
		for (Resource resource : resources) {
			seatData.getSeatResourceList().add(mapResourceData(resource));
		}
		return seatData;
	}

	private static ResourceData mapResourceData(Resource resource) {
		ResourceData resourceData = new ResourceData();
		resourceData.setResourceType(resource.getResourceType());
		resourceData.setResourceNumber(resource.getResourceNumber());
		resourceData.setResourceStatus(resource.getResourceStatus());
		return resourceData;
	}
}
