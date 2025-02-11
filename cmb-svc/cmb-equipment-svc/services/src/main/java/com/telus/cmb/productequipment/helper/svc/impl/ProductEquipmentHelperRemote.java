package com.telus.cmb.productequipment.helper.svc.impl;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.EquipmentModeInfo;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.eas.framework.info.ActivationCreditInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.utility.info.ServiceInfo;

public interface ProductEquipmentHelperRemote extends EJBObject {

	EquipmentInfo getEquipmentInfobySerialNo(String pSerialNo) throws ApplicationException, RemoteException;
	 
	EquipmentInfo getEquipmentInfobySerialNumber(String pSerialNo) throws ApplicationException, RemoteException;;

	EquipmentInfo [] getEquipmentInfobySerialNo(String pSerialNo, 
			 boolean checkPseudoESN)   throws ApplicationException, RemoteException;
	 
	EquipmentInfo getEquipmentInfobyPhoneNo(String pPhoneNo) throws ApplicationException, RemoteException;
	 
	EquipmentInfo getEquipmentInfobyProductCode(String pProductCode)   throws ApplicationException, RemoteException;
	 
	EquipmentInfo getEquipmentInfoByCapCode(String capCode, 
			 String encodingFormat) throws ApplicationException, RemoteException;
	 
	WarrantyInfo getWarrantyInfo(String pSerialNo) throws ApplicationException, RemoteException;
	 
	String getIMEIBySIM (String pSimID) throws ApplicationException, RemoteException;
	 
	EquipmentInfo getMuleBySIM (String pSimID) throws ApplicationException, RemoteException;
	 
	String getSIMByIMEI (String pImeiID) throws ApplicationException, RemoteException;
	 
	String[] getEquipmentList  (String pTechTypeClass, int n, boolean inUse, 
			 String startSerialNo)   throws ApplicationException, RemoteException;
	 
	CardInfo getCardBySerialNo(String serialNo) throws ApplicationException, RemoteException;
	 
	ServiceInfo[] getCardServices(String serialNo, String techType, 
			 String billType) throws ApplicationException, RemoteException;
	 
	CardInfo[] getCards(String phoneNumber, String cardType) throws ApplicationException, RemoteException;
	 
	CardInfo[] getCards(String phoneNumber) throws ApplicationException, RemoteException;
	 
	double getBaseProductPrice(String serialNumber, String province, String npa) 
	 	throws ApplicationException, RemoteException;
	 
	long getShippedToLocation(String serialNumber) throws ApplicationException, RemoteException;
	 
	long getPCSShippedToLocation(String serialNumber) throws ApplicationException, RemoteException;
	 
	long getIDENShippedToLocation(String serialNumber) throws ApplicationException, RemoteException;
	 
	CardInfo getAirCardByCardNo(String fullCardNo, String phoneNumber, String equipmentSerialNo, 
			 String userId)throws ApplicationException, RemoteException;
	 
	String[] getProductFeatures(String pProductCode) throws ApplicationException, RemoteException;
	 
	EquipmentModeInfo[] getEquipmentModes(String pProductCode) throws ApplicationException, RemoteException;
	 
	boolean isNewPrepaidHandset(String serialNo, String productCode) throws ApplicationException, RemoteException;
	 
	double getBaseProductPriceByProductCode(String productCode, 
			String province, String npa) throws ApplicationException, RemoteException;

	EquipmentInfo getAssociatedHandsetByUSIMID( String USIMID ) throws  ApplicationException, RemoteException;

	boolean isValidESNPrefix(String pSerialNo)throws ApplicationException, RemoteException;;
	
	boolean isVirtualESN(String pSerialNo) throws ApplicationException, RemoteException;
	
	String[] getESNByPseudoESN(String pSerialNo)	throws ApplicationException, RemoteException;

	CardInfo getCardByFullCardNo(String fullCardNo, String phoneNumber, 
			 String equipmentSerialNo, String userId) throws ApplicationException, RemoteException;

	EquipmentInfo retrieveVirtualEquipment(String serialNumber, String techTypeClass) throws ApplicationException, RemoteException ;

	EquipmentInfo retrievePagerEquipmentInfo(String serialNo) throws ApplicationException, RemoteException;
	
	boolean isProductFeatureEnabled(String productCode, String productFeature) throws ApplicationException, RemoteException;	
	
	/**
	  * @deprecated use P3MS EJB method
	  */
	ActivationCreditInfo[] getActivationCredits(String serialNumber, String province,
		    String npa, int contractTermMonths, boolean fidoConversion) throws ApplicationException, RemoteException;
	/**
	  * @deprecated use P3MS EJB method
	  */	 
	ActivationCreditInfo[] getActivationCredits(String serialNumber, String province,
		    String npa, int contractTermMonths, String pricePlan, boolean fidoConversion) throws ApplicationException, RemoteException;
	/**
	  * @deprecated use P3MS EJB method
	  */	 
	ActivationCreditInfo[] getActivationCredits(String serialNumber, String province,
		    String npa, int contractTermMonths, java.util.Date activationDate, 
		    boolean fidoConversion) throws ApplicationException, RemoteException;
	/**
	  * @deprecated use P3MS EJB method
	  */	 
	HashMap getActivationCreditsByProductCodes(String[] productCodes, String province,
		    String npa, int contractTermMonths, java.util.Date activationDate, boolean fidoConversion,
		    String[] productTypes, boolean isInitialActivation) throws ApplicationException, RemoteException;
	/**
	  * @deprecated use P3MS EJB method
	  */	 
	ActivationCreditInfo[] getActivationCreditsByProductCode(String productCode, String province,
		    String npa, int contractTermMonths, java.util.Date activationDate, boolean fidoConversion,
		    String productType, boolean isInitialActivation) throws ApplicationException, RemoteException;
	/**
	  * @deprecated use P3MS EJB method
	  */	 
	ActivationCreditInfo[] getActivationCredits(String serialNumber, String province,
		    String npa, String creditType) throws ApplicationException, RemoteException;
	 
}
