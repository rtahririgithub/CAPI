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

import java.util.ArrayList;
import java.util.List;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.Equipment;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.EquipmentChangeRequest;

public class EquipmentChangeRequestMapper extends AbstractSchemaMapper<EquipmentChangeRequest, EquipmentChangeRequestInfo> {

	public EquipmentChangeRequestMapper() {
		super(EquipmentChangeRequest.class, EquipmentChangeRequestInfo.class);
	}

	@Override
	protected EquipmentChangeRequestInfo performDomainMapping(EquipmentChangeRequest source, EquipmentChangeRequestInfo target) {
		if (source != null) {
			if (source.getDuplicateSerialNumbersIndicator() != null) {
				target.setAllowDuplicateSerialNumber(source.getDuplicateSerialNumbersIndicator().value().charAt(0));
			}

			if (source.getSwapType() != null) {
				target.setSwapType(source.getSwapType().value());
			}

			target.setRepairId(source.getRepairId());
			target.setRequestorId(source.getRequestorId());
			target.setPreserveDigitalServices(source.isPreserveDigitalServices());

			if (source.getPrimaryEquipment() != null) {
				target.setNewEquipmentSerialNumber(source.getPrimaryEquipment().getSerialNumber());
			}

			if (source.getAssociatedHandsetEquipment() != null) {
				target.setNewAssociatedHandsetSerialNumber(source.getAssociatedHandsetEquipment().getSerialNumber());
			}

			List<Equipment> equipmentList = source.getSecondaryEquipmentList();
			List<String> secondaryEquipmentSerialNumberList = new ArrayList<String>();
			for (Equipment e : equipmentList) {
				secondaryEquipmentSerialNumberList.add(e.getSerialNumber());
			}
			target.setSecondaryEquipmentSerialNumberList(secondaryEquipmentSerialNumberList.toArray(new String[0]));
		}
		return super.performDomainMapping(source, target);
	}

	@Override
	protected EquipmentChangeRequest performSchemaMapping(EquipmentChangeRequestInfo source, EquipmentChangeRequest target) {
		if (source.getAssociatedHandset() != null) {
			Equipment equipment = new Equipment();
			equipment.setSerialNumber(source.getAssociatedHandset().getSerialNumber());
			equipment.setEquipmentType(source.getAssociatedHandset().getEquipmentType());
			target.setAssociatedHandsetEquipment(equipment);
		}
		if (source.getNewEquipment() != null) {
			Equipment equipment = new Equipment();
			equipment.setSerialNumber(source.getNewEquipment().getSerialNumber());
			equipment.setEquipmentType(source.getNewEquipment().getEquipmentType());
			target.setPrimaryEquipment(equipment);
		}

		// remaining field values are not mapped here. its only needed to
		// process the equipment change request.
		return super.performSchemaMapping(source, target);
	}
}