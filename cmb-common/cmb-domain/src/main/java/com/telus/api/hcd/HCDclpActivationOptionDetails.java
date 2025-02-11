package com.telus.api.hcd;

import com.telus.api.account.CLPActivationOptionDetail;

/**
 * @author x119951
 *
 */
public interface HCDclpActivationOptionDetails extends CLPActivationOptionDetail {
	public Integer getMaxCLPContractTerm();
	public Double getCLPPricePlanLimitAmount();
	public String getOperationResultCd();
}
