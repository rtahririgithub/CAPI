package com.telus.eas.account.info;

import com.telus.api.hcd.HCDclpActivationOptionDetails;

/**
 * @author x119951
 *
 */
public class HCDclpActivationOptionDetailsInfo extends CLPActivationOptionDetailInfo implements HCDclpActivationOptionDetails {
	private static final long serialVersionUID = 6714334204731343034L;

	private Integer maxCLPContractTerm;
	private Double cLPPricePlanLimitAmount;
	private String operationResultCd;
	
	public Integer getMaxCLPContractTerm() {
		return maxCLPContractTerm;
	}
	
	public Double getCLPPricePlanLimitAmount() {
		return cLPPricePlanLimitAmount;
	}
	
	public String getOperationResultCd() {
		return operationResultCd;
	}
	
	public void setMaxCLPContractTerm(Integer maxCLPContractTerm) {
		this.maxCLPContractTerm = maxCLPContractTerm;
	}

	public void setCLPPricePlanLimitAmount(Double cLPPricePlanLimitAmount) {
		this.cLPPricePlanLimitAmount = cLPPricePlanLimitAmount;
	}
	
	public void setOperationResultCd(String operationResultCd) {
		this.operationResultCd = operationResultCd;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("HCDclpActivationOptionDetailsInfo:{\n");
		buffer.append("\tmaxNumberOfCLPSubscribers=[" + getMaxNumberOfCLPSubscribers() + "]\n");
		buffer.append("\tmaxAdditionalCLPSubscribers=[" + getMaxAdditionalCLPSubscribers() + "]\n");
		buffer.append("\tcurrentNumberOfSubscribers=[" + getCurrentNumberOfSubscribers() + "]\n");
		buffer.append("\tcLPAccountInd=[" + isCLPAccountInd() + "]\n");
		buffer.append("\tmaxCLPContractTerm=[" + maxCLPContractTerm + "]\n");
		buffer.append("\tcLPPricePlanLimitAmount=[" + cLPPricePlanLimitAmount + "]\n");
		buffer.append("\toperationResultCd=[" + operationResultCd + "]\n");
		if (this.getResultReasonCodes() != null && this.getResultReasonCodes().length > 0) {
			for (int i=0; i<this.getResultReasonCodes().length; i++) {
				buffer.append("\tresultReasonCode=[" + this.getResultReasonCodes()[i] + "]\n");
			}
		}
		buffer.append("}");
		return buffer.toString();
	}

}
