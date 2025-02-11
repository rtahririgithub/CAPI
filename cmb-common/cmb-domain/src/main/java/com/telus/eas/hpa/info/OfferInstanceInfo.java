package com.telus.eas.hpa.info;

import java.util.Date;
import java.util.List;

import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.MultilingualTextInfo;

public class OfferInstanceInfo extends Info {

	static final long serialVersionUID = 1L;

	private String offerCode;
	private List<MultilingualTextInfo> descriptionList;
	private double lowerBalanceLimit;
	private double upperBalanceLimit;
	private double minRedeemableAmount;
	private double maxRedeemableAmount;
	private String pricePlanGroupCode;
	private double accrualRate;
	private double discountAmount;
	private double discountPercentage;
	private String discountCode;
	private double chargeAmount;
	private String chargeCode;
	private double minPricePlanCost;
	private String chargePromoCode;
	private Date chargePromoExpiryDate;
	private int chargePromotionMonthCount;
	private double productDiscountAmount;
	private String offerTypeCode;
	private double lastBillChargeAmount;
	private int recoveryMonthsCount;
	private boolean pricePlanHighTierIndicator;
	private OfferTypeInfo redeemedOffer;

	public String getOfferCode() {
		return offerCode;
	}

	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}

	public List<MultilingualTextInfo> getDescriptionList() {
		return descriptionList;
	}

	public void setDescriptionList(List<MultilingualTextInfo> descriptionList) {
		this.descriptionList = descriptionList;
	}

	public double getLowerBalanceLimit() {
		return lowerBalanceLimit;
	}

	public void setLowerBalanceLimit(double lowerBalanceLimit) {
		this.lowerBalanceLimit = lowerBalanceLimit;
	}

	public double getUpperBalanceLimit() {
		return upperBalanceLimit;
	}

	public void setUpperBalanceLimit(double upperBalanceLimit) {
		this.upperBalanceLimit = upperBalanceLimit;
	}

	public double getMinRedeemableAmount() {
		return minRedeemableAmount;
	}

	public void setMinRedeemableAmount(double minRedeemableAmount) {
		this.minRedeemableAmount = minRedeemableAmount;
	}

	public double getMaxRedeemableAmount() {
		return maxRedeemableAmount;
	}

	public void setMaxRedeemableAmount(double maxRedeemableAmount) {
		this.maxRedeemableAmount = maxRedeemableAmount;
	}

	public String getPricePlanGroupCode() {
		return pricePlanGroupCode;
	}

	public void setPricePlanGroupCode(String pricePlanGroupCode) {
		this.pricePlanGroupCode = pricePlanGroupCode;
	}

	public double getAccrualRate() {
		return accrualRate;
	}

	public void setAccrualRate(double accrualRate) {
		this.accrualRate = accrualRate;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public double getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public double getMinPricePlanCost() {
		return minPricePlanCost;
	}

	public void setMinPricePlanCost(double minPricePlanCost) {
		this.minPricePlanCost = minPricePlanCost;
	}

	public String getChargePromoCode() {
		return chargePromoCode;
	}

	public void setChargePromoCode(String chargePromoCode) {
		this.chargePromoCode = chargePromoCode;
	}

	public Date getChargePromoExpiryDate() {
		return chargePromoExpiryDate;
	}

	public void setChargePromoExpiryDate(Date chargePromoExpiryDate) {
		this.chargePromoExpiryDate = chargePromoExpiryDate;
	}

	public int getChargePromotionMonthCount() {
		return chargePromotionMonthCount;
	}

	public void setChargePromotionMonthCount(int chargePromotionMonthCount) {
		this.chargePromotionMonthCount = chargePromotionMonthCount;
	}

	public double getProductDiscountAmount() {
		return productDiscountAmount;
	}

	public void setProductDiscountAmount(double productDiscountAmount) {
		this.productDiscountAmount = productDiscountAmount;
	}

	public String getOfferTypeCode() {
		return offerTypeCode;
	}

	public void setOfferTypeCode(String offerTypeCode) {
		this.offerTypeCode = offerTypeCode;
	}

	public double getLastBillChargeAmount() {
		return lastBillChargeAmount;
	}

	public void setLastBillChargeAmount(double lastBillChargeAmount) {
		this.lastBillChargeAmount = lastBillChargeAmount;
	}

	public int getRecoveryMonthsCount() {
		return recoveryMonthsCount;
	}

	public void setRecoveryMonthsCount(int recoveryMonthsCount) {
		this.recoveryMonthsCount = recoveryMonthsCount;
	}

	public boolean isPricePlanHighTierIndicator() {
		return pricePlanHighTierIndicator;
	}

	public void setPricePlanHighTierIndicator(boolean pricePlanHighTierIndicator) {
		this.pricePlanHighTierIndicator = pricePlanHighTierIndicator;
	}

	public OfferTypeInfo getRedeemedOffer() {
		return redeemedOffer;
	}

	public void setRedeemedOffer(OfferTypeInfo redeemedOffer) {
		this.redeemedOffer = redeemedOffer;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("OfferInstanceInfo: {\n");
		s.append("    offerCode=[").append(getOfferCode()).append("]\n");
		s.append("    descriptionList=[\n");
		for (MultilingualTextInfo info : getDescriptionList()) {
			s.append(info).append("\n");
		}
		s.append("    lowerBalanceLimit=[").append(getLowerBalanceLimit()).append("]\n");
		s.append("    upperBalanceLimit=[").append(getUpperBalanceLimit()).append("]\n");
		s.append("    minRedeemableAmount=[").append(getMinRedeemableAmount()).append("]\n");
		s.append("    maxRedeemableAmount=[").append(getMaxRedeemableAmount()).append("]\n");
		s.append("    pricePlanGroupCode=[").append(getPricePlanGroupCode()).append("]\n");
		s.append("    accrualRate=[").append(getAccrualRate()).append("]\n");
		s.append("    discountAmount=[").append(getDiscountAmount()).append("]\n");
		s.append("    discountPercentage=[").append(getDiscountPercentage()).append("]\n");
		s.append("    discountCode=[").append(getDiscountCode()).append("]\n");
		s.append("    chargeAmount=[").append(getChargeAmount()).append("]\n");
		s.append("    chargeCode=[").append(getChargeCode()).append("]\n");
		s.append("    minPricePlanCost=[").append(getMinPricePlanCost()).append("]\n");
		s.append("    chargePromoCode=[").append(getChargePromoCode()).append("]\n");
		s.append("    chargePromoExpiryDate=[").append(getChargePromoExpiryDate()).append("]\n");
		s.append("    chargePromotionMonthCount=[").append(getChargePromotionMonthCount()).append("]\n");
		s.append("    productDiscountAmount=[").append(getProductDiscountAmount()).append("]\n");
		s.append("    offerTypeCode=[").append(getOfferTypeCode()).append("]\n");
		s.append("    lastBillChargeAmount=[").append(getLastBillChargeAmount()).append("]\n");
		s.append("    recoveryMonthsCount=[").append(getRecoveryMonthsCount()).append("]\n");
		s.append("    pricePlanHighTierIndicator=[").append(isPricePlanHighTierIndicator()).append("]\n");
		s.append("    redeemedOffer=[").append(getRedeemedOffer()).append("]\n");
		s.append("}");
		
		return s.toString();
	}

}