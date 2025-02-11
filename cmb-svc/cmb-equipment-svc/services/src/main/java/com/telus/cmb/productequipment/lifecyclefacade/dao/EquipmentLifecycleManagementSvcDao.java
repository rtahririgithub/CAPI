package com.telus.cmb.productequipment.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface EquipmentLifecycleManagementSvcDao {
	public TestPointResultInfo test();
	
	public void assignEquipmentToPhoneNumber(String phoneNumber, String serialNumber, String associatedHandsetIMEI) throws ApplicationException;
	public void changePhoneNumber(String serialNumber, String newPhoneNumber) throws ApplicationException;
	public void markEquipmentStolen(String usimId, String equipmentGroup) throws ApplicationException;
	public void markEquipmentLost(String usimId, String equipmentGroup) throws ApplicationException;
	public void markEquipmentFound(String usimId, String equipmentGroup) throws ApplicationException;
	public void approveReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber, String associatedHandsetIMEI) throws ApplicationException;
	public void releaseReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber, String associatedHandsetIMEI) throws ApplicationException;
	public void disassociateEquipmentFromPhoneNumber(String phoneNumber, String usimId) throws ApplicationException;
	public void swapHSPAOnlyEquipmentForPhoneNumber(String phoneNumber, String oldSerialNumber, String newSerialNumber, String oldAssociatedHandsetIMEI, String newAssociatedHandsetIMEI) throws ApplicationException;
	
}
