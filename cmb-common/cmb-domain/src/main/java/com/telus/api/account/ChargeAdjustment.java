package com.telus.api.account;

public interface ChargeAdjustment {

	public String getErrorCode();
	public String getErrorMessage();
	public String getExceptionTrace();
	public double getAdjustmentAmount() ;
	public String getAdjustmentReasonCode() ;
	public String getAdjustmentMemoText();
	public boolean isBypassAuthorization() ;
	public boolean isChargeApplied();
	public double getChargeSequenceNumber() ;
	public double getAdjustmentId() ;
}
