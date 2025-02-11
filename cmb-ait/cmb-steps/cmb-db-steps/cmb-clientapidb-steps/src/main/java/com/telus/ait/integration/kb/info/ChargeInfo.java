package com.telus.ait.integration.kb.info;

public class ChargeInfo {
	private String gstTaxAmount;
	private String pstTaxAmount;
	private String hstTaxAmount;
	private String gstTaxableAmount;
	private String pstTaxableAmount;
	private String hstTaxableAmount;

    public ChargeInfo(String gstTaxAmount, String pstTaxAmount, String hstTaxAmount, String gstTaxableAmount, String pstTaxableAmount, String hstTaxableAmount) {
        this.gstTaxAmount = gstTaxAmount;
        this.pstTaxAmount = pstTaxAmount;
        this.hstTaxAmount = hstTaxAmount;
        this.gstTaxableAmount = gstTaxableAmount;
        this.pstTaxableAmount = pstTaxableAmount;
        this.hstTaxableAmount = hstTaxableAmount;
    }

	public String getGstTaxAmount() {
		return gstTaxAmount;
	}

	public String getPstTaxAmount() {
		return pstTaxAmount;
	}

	public String getHstTaxAmount() {
		return hstTaxAmount;
	}

	public String getGstTaxableAmount() {
		return gstTaxableAmount;
	}

	public String getPstTaxableAmount() {
		return pstTaxableAmount;
	}

	public String getHstTaxableAmount() {
		return hstTaxableAmount;
	}
}
