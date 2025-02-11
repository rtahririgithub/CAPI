/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint.mapping;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.SeatDataInfo;
import com.telus.cmb.schema.SeatData;
import com.telus.cmb.schema.SeatResource;

public class SeatDataMapper extends AbstractSchemaMapper<SeatData, SeatDataInfo> {

	public SeatDataMapper() {
		super(SeatData.class, SeatDataInfo.class);
	}

	@Override
	protected SeatDataInfo performDomainMapping(SeatData source, SeatDataInfo target) {
		
		if (source != null) {
			target.setSeatType(source.getSeatTypeCd());
			target.setSeatGroup(source.getSeatGroupCd());
			
			for (SeatResource resource : source.getSeatResourceList()) {
				target.addSeatResource(resource.getSeatResourceTypeCd(), resource.getSeatResourceNumber());
			}
		}
		
		return super.performDomainMapping(source, target);
	}

}