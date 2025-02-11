package com.telus.cmb.productequipment.helper.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.ActivationCreditInfo;

public interface CreditAndPricingHelperDao {

	double[] retrieveContractTermCredits(long productGroupTypeID, 
			long productTypeID) throws   ApplicationException;
	
	ActivationCreditInfo[] getDefaultActivationCredits(
	    String productType,  String province, String npa, int contractTermMonths, String creditType,
	    java.util.Date activationDate) throws   ApplicationException;


	double getBaseProductPrice(String serialNumber, String province, String npa, 
			java.util.Date activationDate) throws   ApplicationException;
	
	double getBaseProductPriceByProductCodeFromACME(String productCode, String province, 
			String npa, java.util.Date activationDate) throws   ApplicationException;
	
	double getBaseProductPriceByProductCodeFromP3MS(String productCode, String province, 
			java.util.Date activationDate)throws   ApplicationException;

	EquipmentInfo[] getEquipmentInfobySerialNo(String serialNo,
			boolean checkPseudoESN) throws ApplicationException;

	String[] getESNByPseudoESN(String pSerialNo) throws ApplicationException;

	EquipmentInfo getEquipmentInfobySerialNo(String pSerialNo)
			throws ApplicationException;
	
	ActivationCreditInfo[] getActivationCredits(String serialNumber,
			String province, String npa, int contractTermMonths, String creditType,
			java.util.Date activationDate)throws   ApplicationException;
		
	ActivationCreditInfo[] getActivationCreditsByProductCodeFromACME(String productCode,
		    String province, String npa, int contractTermMonths, String creditType,
		    java.util.Date activationDate, String productType, 
		    boolean isInitialActivation) throws   ApplicationException;

		
	ActivationCreditInfo[] getActivationCreditsByProductCodeFromP3MS( String productCode,
			String province, int contractTermMonths, String creditType,
			java.util.Date activationDate, String productType, 
			boolean isInitialActivation )throws   ApplicationException;
}
