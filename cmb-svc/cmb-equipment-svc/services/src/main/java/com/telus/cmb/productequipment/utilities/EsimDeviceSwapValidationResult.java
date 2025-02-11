package com.telus.cmb.productequipment.utilities;

import com.telus.api.util.EsimConstants;

public enum EsimDeviceSwapValidationResult {
	SUCCESS(EsimConstants.VALIDATION_SUCCESS),
	UNSUPPORTED_ESIM_DEVICE(EsimConstants.UNSUPPORTED_ESIM_DEVICE),
	SIM_PROFILE_MISMATCH(EsimConstants.SIM_PROFILE_MISMATCH),
	LOST_STOLEN(EsimConstants.DEVICE_LOST_STOLEN);
	
    private String resultCd;

    public String getResultCd() { 
        return this.resultCd; 
    } 

    private EsimDeviceSwapValidationResult(String resultCd) {
        this.resultCd = resultCd;
    }
}
