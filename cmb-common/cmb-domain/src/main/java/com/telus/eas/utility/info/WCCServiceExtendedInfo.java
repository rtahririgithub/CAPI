package com.telus.eas.utility.info;


public class WCCServiceExtendedInfo extends ReferenceInfo {

	private static final long serialVersionUID = 1L;

	double chargeAmount;
	SapccOfferInfo sapccOfferInfo;

	public double getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public SapccOfferInfo getSapccOfferInfo() {
		return sapccOfferInfo;
	}

	public void setSapccOfferInfo(SapccOfferInfo sapccOfferInfo) {
		this.sapccOfferInfo = sapccOfferInfo;
	}
	
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("WCCServiceExtendedInfo [").append("code=").append(code).append(", chargeAmount=")
				.append(chargeAmount).append(", ").append(sapccOfferInfo.toString()).append("]");
		
		return buffer.toString();
	}

}