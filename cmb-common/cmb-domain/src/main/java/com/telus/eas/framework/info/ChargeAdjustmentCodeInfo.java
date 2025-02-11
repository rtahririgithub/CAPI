package com.telus.eas.framework.info;

import java.io.Serializable;

public class ChargeAdjustmentCodeInfo  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String chargeCode;
	private String adjustmentReasonCode;
	
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	public String getAdjustmentReasonCode() {
		return adjustmentReasonCode;
	}
	public void setAdjustmentReasonCode(String adjustmentReasonCode) {
		this.adjustmentReasonCode = adjustmentReasonCode;
	}
	
	public String toString() {
		return "ChargeAdjustmentInfo [chargeCode="	+ chargeCode + ", adjustmentReasonCode=" + adjustmentReasonCode + "]";
	}
	

}
