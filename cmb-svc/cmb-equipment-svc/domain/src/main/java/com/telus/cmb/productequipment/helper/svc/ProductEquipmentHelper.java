package com.telus.cmb.productequipment.helper.svc;

import java.util.HashMap;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.EquipmentModeInfo;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.eas.framework.info.ActivationCreditInfo;
import com.telus.eas.utility.info.ServiceInfo;

public interface ProductEquipmentHelper {

	EquipmentInfo getEquipmentInfobySerialNo(String pSerialNo) throws ApplicationException;

	EquipmentInfo getEquipmentInfobySerialNumber(String pSerialNo) throws ApplicationException;
	
	EquipmentInfo [] getEquipmentInfobySerialNo(String pSerialNo, 
			 boolean checkPseudoESN)   throws ApplicationException;
	 
	EquipmentInfo getEquipmentInfobyPhoneNo(String pPhoneNo)   throws ApplicationException;
	 
	EquipmentInfo getEquipmentInfobyProductCode(String pProductCode)   throws ApplicationException;
	 
	EquipmentInfo getEquipmentInfoByCapCode(String capCode, 
			 String encodingFormat) throws ApplicationException;
	 	 
	WarrantyInfo getWarrantyInfo(String pSerialNo) throws ApplicationException;
	 
	String getIMEIBySIM (String pSimID) throws ApplicationException;
	 
	EquipmentInfo getMuleBySIM (String pSimID) throws ApplicationException;
	 
	String getSIMByIMEI (String pImeiID) throws ApplicationException;
	 
	String[] getEquipmentList  (String pTechTypeClass, int n, boolean inUse, 
			 String startSerialNo)   throws ApplicationException;
	 
	CardInfo getCardBySerialNo(String serialNo) throws ApplicationException;
	 
	ServiceInfo[] getCardServices(String serialNo, String techType, 
			 String billType) throws ApplicationException;
	 
	CardInfo[] getCards(String phoneNumber, String cardType) throws ApplicationException;
	 
	CardInfo[] getCards(String phoneNumber) throws ApplicationException;
	 
	double getBaseProductPrice(String serialNumber, String province, String npa) 
	 	throws ApplicationException;
	 
	long getShippedToLocation(String serialNumber) throws ApplicationException;
	 
	long getPCSShippedToLocation(String serialNumber) throws ApplicationException;
	 
	long getIDENShippedToLocation(String serialNumber) throws ApplicationException;
	 
	CardInfo getAirCardByCardNo(String fullCardNo, String phoneNumber, String equipmentSerialNo, 
			 String userId)throws ApplicationException;
	
	long getProductIdByProductCode(String productCode) throws ApplicationException;
	
	String[] getProductFeatures(String pProductCode) throws ApplicationException;
	 
	EquipmentModeInfo[] getEquipmentModes(String pProductCode) throws ApplicationException;
	 
	boolean isNewPrepaidHandset(String serialNo, String productCode) throws ApplicationException;
	 
	double getBaseProductPriceByProductCode(String productCode, 
			String province, String npa) throws ApplicationException;

	EquipmentInfo getAssociatedHandsetByUSIMID( String USIMID ) throws  ApplicationException;

	boolean isValidESNPrefix(String pSerialNo)throws ApplicationException;
	
	boolean isVirtualESN(String pSerialNo) throws ApplicationException;
	
	String[] getESNByPseudoESN(String pSerialNo)	throws ApplicationException;
	
	CardInfo getCardByFullCardNo(String fullCardNo, String phoneNumber, 
			 String equipmentSerialNo, String userId) throws ApplicationException;
	
	EquipmentInfo retrieveVirtualEquipment(String serialNumber, String techTypeClass) throws ApplicationException ;
	
	EquipmentInfo retrievePagerEquipmentInfo(String serialNo) throws ApplicationException;
	/**
	  * @deprecated use P3MS EJB method
	  */
	ActivationCreditInfo[] getActivationCredits(String serialNumber, String province,
		    String npa, int contractTermMonths, boolean fidoConversion) throws ApplicationException;
	/**
	  * @deprecated use P3MS EJB method
	  */	 
	ActivationCreditInfo[] getActivationCredits(String serialNumber, String province,
		    String npa, int contractTermMonths, String pricePlan, boolean fidoConversion) throws ApplicationException;
	/**
	  * @deprecated use P3MS EJB method
	  */	 
	ActivationCreditInfo[] getActivationCredits(String serialNumber, String province,
		    String npa, int contractTermMonths, java.util.Date activationDate, 
		    boolean fidoConversion) throws ApplicationException;
	/**
	  * @deprecated use P3MS EJB method
	  */	 
	HashMap getActivationCreditsByProductCodes(String[] productCodes, String province,
		    String npa, int contractTermMonths, java.util.Date activationDate, boolean fidoConversion,
		    String[] productTypes, boolean isInitialActivation) throws ApplicationException;
	/**
	  * @deprecated use P3MS EJB method
	  */	 
	ActivationCreditInfo[] getActivationCreditsByProductCode(String productCode, String province,
		    String npa, int contractTermMonths, java.util.Date activationDate, boolean fidoConversion,
		    String productType, boolean isInitialActivation) throws ApplicationException;
	/**
	  * @deprecated use P3MS EJB method
	  */	 
	ActivationCreditInfo[] getActivationCredits(String serialNumber, String province,
		    String npa, String creditType) throws ApplicationException;

	boolean isProductFeatureEnabled(String productCode, String productFeature) throws ApplicationException;
}
