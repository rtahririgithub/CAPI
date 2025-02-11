package com.telus.eas.account.info;

public class MarketingInfo {
	
	private final static long serialVersionUID = 1L;
    protected String offerCode;
    protected String promoCode;
    protected String offerDescriptionEn;
    protected String offerDescriptionFr;
    protected String marketingTextEn;
    protected String marketingTextFr;
    protected String rank;
    protected String validationCode;
    
	public String getOfferCode() {
		return offerCode;
	}
	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}
	public String getPromoCode() {
		return promoCode;
	}
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	public String getOfferDescriptionEn() {
		return offerDescriptionEn;
	}
	public void setOfferDescriptionEn(String offerDescriptionEn) {
		this.offerDescriptionEn = offerDescriptionEn;
	}
	public String getOfferDescriptionFr() {
		return offerDescriptionFr;
	}
	public void setOfferDescriptionFr(String offerDescriptionFr) {
		this.offerDescriptionFr = offerDescriptionFr;
	}
	public String getMarketingTextEn() {
		return marketingTextEn;
	}
	public void setMarketingTextEn(String marketingTextEn) {
		this.marketingTextEn = marketingTextEn;
	}
	public String getMarketingTextFr() {
		return marketingTextFr;
	}
	public void setMarketingTextFr(String marketingTextFr) {
		this.marketingTextFr = marketingTextFr;
	}
	public String getRank() {
		return rank;
	}
	@Override
	public String toString() {
		return "MarketingInfo [offerCode=" + offerCode + ", promoCode="
				+ promoCode + ", offerDescriptionEn=" + offerDescriptionEn
				+ ", offerDescriptionFr=" + offerDescriptionFr
				+ ", marketingTextEn=" + marketingTextEn + ", marketingTextFr="
				+ marketingTextFr + ", rank=" + rank + ", validationCode="
				+ validationCode + "]";
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getValidationCode() {
		return validationCode;
	}
	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}
}
