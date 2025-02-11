package com.telus.cmb.account.informationhelper.dao;

public interface EquipmentDao {
	
	String getUsimBySerialNumber(String serialNumber);
	
	String[] getImsisByUsim(String usimId);

}
