package com.telus.cmb.productequipment.lifecyclefacade.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.api.equipment.Warranty;
import com.telus.eas.equipment.info.CellularDigitalEquipmentUpgradeInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.UsimProfileInfo;
import com.telus.eas.equipment.productdevice.info.ProductInfo;

public interface ProductEquipmentLifecycleFacadeRemote extends EJBObject{
	

	 /**
	  * Returns false if one of the values in DPIneligibleVendors matches the
	  * manufacturer(vendor) ID
	  * @deprecated
	  */
	boolean isDeviceProtectionEligible(EquipmentInfo equipmentInfo) throws RemoteException;
	/*
	 * 	@deprecated consumers should go to P3MS directly - will remove in July 2017
	 */
	boolean isApple(String productCode) throws ApplicationException,RemoteException;

	CellularDigitalEquipmentUpgradeInfo[] getCellularDigitalEquipmentUpgrades(
			EquipmentInfo pEquipmentInfo) throws ApplicationException,RemoteException;
	/**
	 * 
	 * @param phoneNumber				Phone number , mandatory 10 digits .
	 * @param serialNumber				HSPA USIM ID , mandatory .
	 * @param associatedHandsetIMEI		HSPA handset serial number , optional .
	 * @throws ApplicationException,RemoteException
	 */

	public void assignEquipmentToPhoneNumber(String phoneNumber, String serialNumber, 
			String associatedHandsetIMEI) throws ApplicationException,RemoteException;
	/**
	 * 
	 * @param serialNumber		HSPA USIM ID , mandatory .
	 * @param newPhoneNumber	Phone number , mandatory 10 digits .
	 * @throws ApplicationException,RemoteException
	 */
	public void changePhoneNumber(String serialNumber, String newPhoneNumber) throws ApplicationException,RemoteException;
	/**
	 * 
	 * @param phoneNumber				Phone number , mandatory 10 digits .
	 * @param oldSerialNumber			mandatory.
	 * @param oldAssociatedHandsetIMEI	old HSPA handset serial number , optional .
	 * @param oldNetworkType			old network type, mandatory.
	 * @param newSerialNumber			mandatory.
	 * @param newAssociatedHandsetIMEI	new HSPA handset serial number , optional .
	 * @param newNetworkType			new network type, mandatory.
	 * @throws ApplicationException,RemoteException
	 */
	public void swapEquipmentForPhoneNumber(String phoneNumber, String oldUsimId,String oldAssociatedHandsetIMEI,
			String oldNetworkType,	String newUsimId, String newAssociatedHandsetIMEI,String newNetworkType)
	throws ApplicationException,RemoteException;

	/**
	 * 
	 * @param usimId				HSPA USIM ID.
	 * @param equipmentGroup		Equipment Group.
	 * @throws ApplicationException,RemoteException
	 */
	public void markEquipmentStolen(String usimId, String equipmentGroup) throws ApplicationException,RemoteException;
	/**
	 * 
	 * @param usimId				HSPA USIM ID.
	 * @param equipmentGroup		Equipment Group.
	 * @throws ApplicationException,RemoteException
	 */
	public void markEquipmentLost(String usimId, String equipmentGroup) throws ApplicationException,RemoteException;
	/**
	 * 
	 * @param usimId				HSPA USIM ID.
	 * @param equipmentGroup		Equipment Group.
	 * @throws ApplicationException,RemoteException
	 */
	public void markEquipmentFound(String usimId, String equipmentGroup) throws ApplicationException,RemoteException ;
	/**
	 * 
	 * @param phoneNumber				Phone number , mandatory 10 digits .
	 * @param serialNumber				HSPA old USIM ID .
	 * @param associatedHandsetIMEI		HSPA handset serial number , optional .
	 * @throws ApplicationException
	 */
	public void approveReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber,
			String associatedHandsetIMEI) throws ApplicationException,RemoteException;

	/**
	 * 
	 * @param phoneNumber				Phone number , mandatory 10 digits .
	 * @param serialNumber				HSPA old USIM ID .
	 * @param associatedHandsetIMEI		HSPA handset serial number , optional .
	 * @throws ApplicationException,RemoteException
	 */
	public void releaseReservedEquipmentForPhoneNumber(String phoneNumber, String serialNumber,
			String associatedHandsetIMEI) throws ApplicationException,RemoteException;

	/**
	 * 
	 * @param phoneNumber				Phone number , mandatory 10 digits .
	 * @param serialNumber				HSPA old USIM ID .
	 * @throws ApplicationException,RemoteException
	 */
	public void disassociateEquipmentFromPhoneNumber(String phoneNumber, String usimId) throws ApplicationException,RemoteException;
	/**
	 * 
	 * @param serialNumber			HSPA old USIM ID .
	 * @param equipmentGroup		Equipment Group.
	 * @throws ApplicationException,RemoteException
	 */
	public Warranty getWarrantySummary(String serialNumber,	String equipmentGroup) throws ApplicationException,RemoteException;

	/**
	 * 
	 * @param phoneNumber			
	 * @param oldSerialNumber		
	 * @param newSerialNumber
	 * @param oldAssociatedHandsetIMEI
	 * @param newAssociatedHandsetIMEI
	 * 
	 * @throws ApplicationException,RemoteException
	 */

	public void swapHSPAOnlyEquipmentForPhoneNumber(String phoneNumber, String oldSerialNumber, String newSerialNumber,String oldAssociatedHandsetIMEI, String newAssociatedHandsetIMEI) throws ApplicationException,RemoteException;
	
	public void asyncAssignEquipmentToPhoneNumber(String phoneNumber,String serialNumber, String associatedHandsetIMEI)throws ApplicationException, RemoteException;

	public void asyncChangePhoneNumber(String serialNumber,String newPhoneNumber) throws ApplicationException, RemoteException;

	public void asyncApproveReservedEquipmentForPhoneNumber(String phoneNumber,String serialNumber, String associatedHandsetIMEI)throws ApplicationException, RemoteException;

	public void asyncReleaseReservedEquipmentForPhoneNumber(String phoneNumber,String serialNumber, String associatedHandsetIMEI)throws ApplicationException, RemoteException;

	public void asyncDisassociateEquipmentFromPhoneNumber(String phoneNumber,String usimId) throws ApplicationException, RemoteException;

	public void asyncSwapHSPAOnlyEquipmentForPhoneNumber(String phoneNumber,String oldSerialNumber, String newSerialNumber,String oldAssociatedHandsetIMEI, String newAssociatedHandsetIMEI)
			throws ApplicationException, RemoteException;
	
	public ProductInfo getProduct(String productCode) throws ApplicationException, RemoteException;
	
	public ProductInfo getProduct(String productCode, boolean isEsim) throws ApplicationException, RemoteException;
	
	public UsimProfileInfo getProductUSIMProfile(long usimProductId) throws ApplicationException, RemoteException;
	
	public EquipmentInfo reserveSimProfile(String imei, String simProfileCd, String embeddedIccId) throws ApplicationException, RemoteException;
}
