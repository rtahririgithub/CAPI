package com.telus.eas.framework.info;

import java.io.Serializable;

import com.telus.eas.account.info.TaxExemptionInfo;
import com.telus.eas.account.info.TaxSummaryInfo;


public class ChargeAdjustmentWithTaxInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ChargeAdjustmentInfo chargeAdjustmentInfo;
	private TaxSummaryInfo taxSummaryInfo;
	private TaxExemptionInfo taxExemptionInfo;
	
	public ChargeAdjustmentInfo getChargeAdjustmentInfo() {
		return chargeAdjustmentInfo;
	}

	public void setChargeAdjustmentInfo(ChargeAdjustmentInfo chargeAdjustmentInfo) {
		this.chargeAdjustmentInfo = chargeAdjustmentInfo;
	}

	public TaxSummaryInfo getTaxSummaryInfo() {
		return taxSummaryInfo;
	}

	public void setTaxSummaryInfo(TaxSummaryInfo taxSummaryInfo) {
		this.taxSummaryInfo = taxSummaryInfo;
	}
		
	public TaxExemptionInfo getTaxExemptionInfo() {
		return taxExemptionInfo;
	}

	public void setTaxExemptionInfo(TaxExemptionInfo taxExemptionInfo) {
		this.taxExemptionInfo = taxExemptionInfo;
	}
	
	public String toString() {
		return chargeAdjustmentInfo.toString() + taxSummaryInfo.toString();
	}
	
}
