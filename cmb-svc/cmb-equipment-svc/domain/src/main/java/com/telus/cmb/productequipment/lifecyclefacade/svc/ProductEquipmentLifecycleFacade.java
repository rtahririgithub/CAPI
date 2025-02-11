package com.telus.cmb.productequipment.lifecyclefacade.svc;

import com.telus.api.ApplicationException;
import com.telus.api.equipment.Warranty;
import com.telus.eas.equipment.info.CellularDigitalEquipmentUpgradeInfo;
import com.telus.eas.equipment.info.DeviceSwapValidateInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.UsimProfileInfo;
import com.telus.eas.equipment.productdevice.info.ProductInfo;

public interface ProductEquipmentLifecycleFacade {
	
	 /**
	  * Returns false if one of the values in DPIneligibleVendors matches the manufacturer(vendor) ID
	  * 
	  * @deprecated
	  */
	 public boolean isDeviceProtectionEligible(EquipmentInfo equipmentInfo);
	 
	 /**
	   * Calls P3MS EJB to determine if productCode belongs to Apple product
	   *
	   * @param String			productCode
	   * @returns true if the productCode belongs to Apple
	   *
	   * @exception ApplicationException
	   * @deprecated consumers should go to P3MS directly - will remove in July 2017
	   */
	public boolean isApple(String productCode) throws ApplicationException;
	
	/**
	 * Calls NRT Eligibility Manager EJB to getCellularDigitalEquipmentUpgrades
	 *
	 * @param EquipmentInfo		equipmentInfo
	 * @returns CellularDigitalEquipmentUpgradeInfo array
	 *
	 * @exception ApplicationException
	 */
	public CellularDigitalEquipmentUpgradeInfo[] getCellularDigitalEquipmentUpgrades(
			EquipmentInfo equipmentInfo) throws ApplicationException;

	/**
	 * 
	 * @param phoneNumber				Phone number, mandatory 10 digits.
	 * @param serialNumber				HSPA USIM ID, mandatory.
	 * @param associatedHandsetIMEI		HSPA handset serial number, optional.
	 * 
	 * @throws ApplicationException
	 */
	public void assignEquipmentToPhoneNumber(String phoneNumber, String serialNumber, 
			String associatedHandsetIMEI) throws ApplicationException;
	
	/**
	 * 
	 * @param serialNumber		HSPA USIM ID, mandatory.
	 * @param newPhoneNumber	Phone number, mandatory 10 digits.
	 * 
	 * @throws ApplicationException
	 */
	public void changePhoneNumber(String serialNumber, String newPhoneNumber) throws ApplicationException;
	
	/**
	 * 
	 * @param phoneNumber				Phone number, mandatory 10 digits.
	 * @param oldSerialNumber			Mandatory.
	 * @param oldAssociatedHandsetIMEI	Old HSPA handset serial number, optional.
	 * @param oldNetworkType			Old network type, mandatory.
	 * @param newSerialNumber			Mandatory.
	 * @param newAssociatedHandsetIMEI	New HSPA handset serial number, optional.
	 * @param newNetworkType			New network type, mandatory.
	 * 
	 * @throws ApplicationException
	 */
	public void swapEquipmentForPhoneNumber(String phoneNumber, String oldUsimId,String oldAssociatedHandsetIMEI,
			String oldNetworkType,	String newUsimId, String newAssociatedHandsetIMEI,String newNetworkType) throws ApplicationException;

	/**
	 * 
	 * @param usimId				HSPA USIM ID.
	 * @param equipmentGroup		Equipment group.
	 * 
	 * @throws ApplicationException
	 */
	public void markEquipmentStolen(String usimId, String equipmentGroup) throws ApplicationException;
	
	/**
	 * 
	 * @param usimId				HSPA USIM ID.
	 * @param equipmentGroup		Equipment group.
	 * 
	 * @throws ApplicationException
	 */
	public void markEquipmentLost(String usimId, String equipmentGroup) throws ApplicationException;
	
	/**
	 * 
	 * @param usimId				HSPA USIM ID.
	 * @param equipmentGroup		Equipment group.
	 * 
	 * @throws ApplicationException
	 */
	public void markEquipmentFound(String usimId, String equipmentGroup) throws ApplicationException ;
	
	/**
	 * 
	 * @param phoneNumber				Phone number, mandatory 10 digits.
	 * @param serialNumber				HSPA old USIM ID.
	 * @param associatedHandsetIMEI		HSPA handset serial number, optional.
	 * 
	 * @throws ApplicationException
	 */
	public void approveReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber,
			String associatedHandsetIMEI) throws ApplicationException;

	/**
	 * 
	 * @param phoneNumber				Phone number, mandatory 10 digits.
	 * @param serialNumber				HSPA old USIM ID.
	 * @param associatedHandsetIMEI		HSPA handset serial number, optional.
	 * 
	 * @throws ApplicationException
	 */
	public void releaseReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber,
			String associatedHandsetIMEI) throws ApplicationException;

	/**
	 * 
	 * @param phoneNumber				Phone number, mandatory 10 digits.
	 * @param serialNumber				HSPA old USIM ID.
	 * 
	 * @throws ApplicationException
	 */
	public void disassociateEquipmentFromPhoneNumber(String phoneNumber, String usimId) throws ApplicationException;
	
	/**
	 * 
	 * @param serialNumber			HSPA old USIM ID.
	 * @param equipmentGroup		Equipment group.
	 * 
	 * @throws ApplicationException
	 */
	public Warranty getWarrantySummary(String serialNumber,	String equipmentGroup) throws ApplicationException;
	
	/**
	 * 
	 * @param phoneNumber			
	 * @param oldSerialNumber		
	 * @param newSerialNumber
	 * @param oldAssociatedHandsetIMEI
	 * @param newAssociatedHandsetIMEI
	 * 
	 * @throws ApplicationException, RemoteException
	 */
	public void swapHSPAOnlyEquipmentForPhoneNumber(String phoneNumber, String oldSerialNumber, String newSerialNumber,
			String oldAssociatedHandsetIMEI, String newAssociatedHandsetIMEI) throws ApplicationException;
	
	public void asyncAssignEquipmentToPhoneNumber(String phoneNumber, String serialNumber, String associatedHandsetIMEI) throws ApplicationException;
	
	public void asyncChangePhoneNumber(String serialNumber, String newPhoneNumber) throws ApplicationException;
	
	public void asyncApproveReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber,String associatedHandsetIMEI) throws ApplicationException;
	
	public void asyncReleaseReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber,String associatedHandsetIMEI) throws ApplicationException;
	
	public void asyncDisassociateEquipmentFromPhoneNumber(String phoneNumber, String usimId) throws ApplicationException;
	
	public void asyncSwapHSPAOnlyEquipmentForPhoneNumber(String phoneNumber, String oldSerialNumber, String newSerialNumber,String oldAssociatedHandsetIMEI, String newAssociatedHandsetIMEI) throws ApplicationException;
	
	public ProductInfo getProduct(String productCode) throws ApplicationException;
	
	public ProductInfo getProduct(String productCode, boolean isEsim) throws ApplicationException;
	
	public UsimProfileInfo getProductUSIMProfile(long usimProductId) throws ApplicationException;
	
	public EquipmentInfo reserveSimProfile(String imei, String simProfileCd, String embeddedIccId) throws ApplicationException;
	
	public DeviceSwapValidateInfo validateEsimDeviceSwap(EquipmentInfo currentUsimEquipment, EquipmentInfo newEsimDevice) throws ApplicationException;
	
}