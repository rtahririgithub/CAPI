package com.telus.cmb.productequipment.helper.dao;

import java.util.Hashtable;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.EquipmentModeInfo;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public interface EquipmentHelperDao {

	String[] getKBEquipmentList(String productType, int no) throws   ApplicationException;
	
	boolean isValidESNPrefix(String pSerialNo)throws   ApplicationException;  

	
	String[] getEquipmentList (String pTechTypeClass, int n, 
			String startSerialNo) throws   ApplicationException;
	
	long getProductIdByProductCode(String productCode) throws ApplicationException;
	
	String[] getProductFeatures(String pProductCode) throws   ApplicationException;
	
	EquipmentModeInfo[] getEquipmentModes(String pProductCode)throws   ApplicationException;
	
	boolean isNewPrepaidHandset(String serialNo, String productCode) throws   ApplicationException;

	EquipmentInfo getEquipmentInfobyProductCode(String productCode)throws   ApplicationException;
	
	EquipmentInfo getEquipmentInfobyCapCode(String pCapCode,
			String pEncodingFormat) throws   ApplicationException;

	long getShippedToLocation(String serialNumber, int locationType) throws   ApplicationException;
	
	long getPCSShippedToLocation(String serialNumber, int locationType) throws   ApplicationException;
	
	long getIDENShippedToLocation(String serialNumber, int locationType) throws   ApplicationException;

	EquipmentInfo getVirtualEquipment(String pSerialNo, String techTypeClass)throws   ApplicationException;
	
	boolean isVirtualESN(String pSerialNo) throws   ApplicationException;

	EquipmentInfo getEquipmentInfobyPhoneNo(String pPhoneNo) throws   ApplicationException;
	
	String getUSIMByIMSI (String pIMSI) throws   ApplicationException;
	
	String[] getIMSIsByUSIM (String pUSIM_Id)throws   ApplicationException;
	
	String[] getIMSIsBySerialNumber(String serialNumber) throws   ApplicationException;  

	WarrantyInfo getWarrantyInfo(String pSerialNo)throws   ApplicationException;

	EquipmentInfo getMuleBySIM (String pSimID) throws   ApplicationException;
	
	String getIMEIBySIM (String pSimID) throws   ApplicationException;
	
	String getSIMByIMEI (String pImeiID) throws   ApplicationException;

	Hashtable<String, String> getUSIMListByIMSIs (String[] IMISIs ) throws   ApplicationException;
	
	String getIMEIByUSIMID (String pUSimID) throws   ApplicationException;
	
	EquipmentInfo getAssociatedHandsetByUSIMID (String pUSimID)throws   ApplicationException;

	boolean isInUse(String pSerialNo) throws   ApplicationException;
	
	EquipmentInfo getSubscriberByEquipment(EquipmentInfo equipmentInfo) throws   ApplicationException;
	
	EquipmentInfo getSubscriberByIMSI(EquipmentInfo equipmentInfo) throws   ApplicationException;
	
	EquipmentInfo retrievePagerEquipmentInfo(String serialNo) throws ApplicationException;
	
	SubscriberInfo[] retrieveHSPASubscriberIdsByIMSI (String IMSI) throws ApplicationException;

	
}
