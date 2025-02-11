package com.telus.cmb.productequipment.domain;

public class SemsEquipmentInfo  implements java.io.Serializable{

	
	private static final long serialVersionUID = 1L;
	public static final String METHOD_TYPE_ASSIGN_EQUIPMENT_PHONENUMBER = "assignEquipmentToPhoneNumber";
	public static final String METHOD_TYPE_CHANGE_PHONENUMBER = "changePhoneNumber";
	public static final String METHOD_TYPE_APPROVE_RESERVED_EQUIPMENT_PHONENUMBER = "approveReservedEquipmentForPhoneNumber";
	public static final String METHOD_TYPE_RELEASE_RESERVED_EQUIPMENT_PHONENUMBER = "releaseReservedEquipmentForPhoneNumber";
	public static final String METHOD_TYPE_DISASSOCIATE_EQUIPMENT_PHONENUMBER = "disassociateEquipmentFromPhoneNumber";
	public static final String METHOD_TYPE_SWAP_HSPA_EQUIPMENT_PHONENUMBER = "swapHSPAOnlyEquipmentForPhoneNumber";

	private String phoneNumber;
	private String serialNumber;
	private String associatedHandsetIMEI;
	private String equipmentGroup;
	private String usimId;
	private String oldSerialNumber;
	private String newSerialNumber;
	private String oldAssociatedHandsetIMEI;
	private String newAssociatedHandsetIMEI;
	private String semsMethodType;

	public String getSemsMethodType() {
		return semsMethodType;
	}

	public void setSemsMethodType(String semsMethodType) {
		this.semsMethodType = semsMethodType;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getAssociatedHandsetIMEI() {
		return associatedHandsetIMEI;
	}

	public void setAssociatedHandsetIMEI(String associatedHandsetIMEI) {
		this.associatedHandsetIMEI = associatedHandsetIMEI;
	}

	public String getEquipmentGroup() {
		return equipmentGroup;
	}

	public void setEquipmentGroup(String equipmentGroup) {
		this.equipmentGroup = equipmentGroup;
	}

	public String getUsimId() {
		return usimId;
	}

	public void setUsimId(String usimId) {
		this.usimId = usimId;
	}

	public String getOldSerialNumber() {
		return oldSerialNumber;
	}

	public void setOldSerialNumber(String oldSerialNumber) {
		this.oldSerialNumber = oldSerialNumber;
	}

	public String getNewSerialNumber() {
		return newSerialNumber;
	}

	public void setNewSerialNumber(String newSerialNumber) {
		this.newSerialNumber = newSerialNumber;
	}

	public String getOldAssociatedHandsetIMEI() {
		return oldAssociatedHandsetIMEI;
	}

	public void setOldAssociatedHandsetIMEI(String oldAssociatedHandsetIMEI) {
		this.oldAssociatedHandsetIMEI = oldAssociatedHandsetIMEI;
	}

	public String getNewAssociatedHandsetIMEI() {
		return newAssociatedHandsetIMEI;
	}

	public void setNewAssociatedHandsetIMEI(String newAssociatedHandsetIMEI) {
		this.newAssociatedHandsetIMEI = newAssociatedHandsetIMEI;
	}

	@Override
	public String toString() {
		return "SemsEquipmentInfo [phoneNumber=" + phoneNumber
				+ ", serialNumber=" + serialNumber + ", associatedHandsetIMEI="
				+ associatedHandsetIMEI + ", equipmentGroup=" + equipmentGroup
				+ ", usimId=" + usimId + ", oldSerialNumber=" + oldSerialNumber
				+ ", newSerialNumber=" + newSerialNumber
				+ ", oldAssociatedHandsetIMEI=" + oldAssociatedHandsetIMEI
				+ ", newAssociatedHandsetIMEI=" + newAssociatedHandsetIMEI
				+ ", semsMethodType=" + semsMethodType + "]";
	}

}
