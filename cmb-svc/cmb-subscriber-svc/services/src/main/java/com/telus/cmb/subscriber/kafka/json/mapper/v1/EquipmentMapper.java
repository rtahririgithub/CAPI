package com.telus.cmb.subscriber.kafka.json.mapper.v1;

import com.telus.cmb.common.kafka.subscriber_v1.Equipment;
import com.telus.eas.equipment.info.EquipmentInfo;

public class EquipmentMapper {
	public static Equipment mapEquipment(EquipmentInfo equipmentInfo){
		Equipment equip = new Equipment();
		equip.setPrimaryEquipmentSerialNumber(equipmentInfo.getSerialNumber());
		equip.setHspaImsi(equipmentInfo.getProfile() != null ? equipmentInfo.getProfile().getLocalIMSI() : null);
		equip.setEquipmentGroup(equipmentInfo.getEquipmentGroup());
		equip.setHandSetSerialNumber(equipmentInfo.getAssociatedHandsetIMEI());
		return equip;
	}
}
