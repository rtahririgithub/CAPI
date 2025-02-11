/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.customer_information_10;

import com.telus.eas.utility.info.SegmentationInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customer_information_reference_types_1_0.Segmentation;

/**
 * @author Pavel Simonovsky
 *
 */
public class SegmentationMapper extends ReferenceMapper<Segmentation, SegmentationInfo> {

	public SegmentationMapper() {
		super(Segmentation.class, SegmentationInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected Segmentation performSchemaMapping(SegmentationInfo source, Segmentation target) {
		
		target.setSegment(source.getSegment());
		target.setSubSegment(source.getSubSegment());
		
		return super.performSchemaMapping(source, target);
	}
}
