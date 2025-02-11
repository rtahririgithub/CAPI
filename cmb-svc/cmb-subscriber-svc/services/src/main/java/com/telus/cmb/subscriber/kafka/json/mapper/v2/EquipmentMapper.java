package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.common.kafka.subscriber_v2.Equipment;
import com.telus.eas.equipment.info.EquipmentInfo;

public class EquipmentMapper {
	public static Equipment mapEquipment(EquipmentInfo equipmentInfo) {
		if (equipmentInfo == null || StringUtils.isBlank(equipmentInfo.getSerialNumber())) {
			return null;
		}
		Equipment equip = new Equipment();
		equip.setSerialNumber(equipmentInfo.getSerialNumber());
		equip.setImsiNumber(equipmentInfo.getProfile() != null ? equipmentInfo.getProfile().getLocalIMSI() : null);
		equip.setEquipmentType(equipmentInfo.getEquipmentType());
		return equip;
	}

}
