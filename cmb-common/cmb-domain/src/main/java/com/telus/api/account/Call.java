package com.telus.api.account;

/**
 * <CODE>Call</CODE>
 * 
 */
public interface Call {
	
	String CALL_TERMINATION_NORMAL = "0";
	String CALL_TERMINATION_COMPLETED_NOT_TERMINATED = "1";
	String CALL_TERMINATION_DROPPED = "2";
	String CALL_TERMINATION_NORMAL_NOT_COMPLETED = "3";

	//	String CALL_DIRECTION_MOBILE_TO_LAND = "0";
	//	String CALL_DIRECTION_LAND_TO_MOBILE = "1";
	//	String CALL_DIRECTION_MOBILE_TO_MOBILE = "2";
	//	String CALL_DIRECTION_LAND_TO_LAND = "3";
	//	String CALL_DIRECTION_CFW_TO_LAND = "4";
	//	String CALL_DIRECTION_CFW_TO_MOBILE = "5";
	//	String CALL_DIRECTION_MOBILE_TERMINATED = "6";

	String getSerialNumber();
	
	String getOrigCellTrunkId();
	
	String getTermCellTrunkId();
	
	String getTerminationCode();
	
	String getAirtimeServiceCode();
	
	String getAirtimeFeatureCode();
	
	String getTollServiceCode();
	
	String getTollFeatureCode();
	
	String getAdditionalChargeServiceCode();
	
	String[] getFeatureCodes();
	
	String getOrigRouteDescription();
	
	String getTermRouteDescription();
	
    double getGSTAmount();
    
    double getPSTAmount();
    
    double getHSTAmount();
    
    double getTaxableGSTAmount();
    
    double getTaxablePSTAmount();
    
    double getTaxableHSTAmount();
    
    double getAdjustmentAmount();
    
    double getAdjustmentGSTAmount();
    
    double getAdjustmentPSTAmount();
    
    double getAdjustmentHSTAmount();
    
    double getAdjustmentRoamingTaxAmount();

}
