/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;
import java.util.*;

public class DiscountPlanInfo extends Info implements DiscountPlan {

	static final long serialVersionUID = 1L;

	private Date effectiveDate;
	private Date expiration;
	private String[] groupCodes;
	private int months;
	private double amount = 0;
	private double percent = 0;
	private String description = "";
	private String descriptionFrench = "";
	private String code = "";
	private boolean availableForActivation = true;
	private boolean availableForChange = false;
	private Date offerExpirationDate;
	private boolean pricePlanDiscount;
	private String productType;
	private String level;
	private int[] discountBrandIDs;

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Date getExpiration() {
		return expiration;
	}

	public String[] getGroupCodes() {
		return groupCodes;
	}

	public int getMonths() {
		return months;
	}

	public double getAmount() {
		return amount;
	}

	public double getPercent() {
		return percent;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public String getCode() {
		return code;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public void setGroupCodes(String[] groupCodes) {
		this.groupCodes = groupCodes;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setAvailableForActivation(boolean newAvailableForActivation) {
		availableForActivation = newAvailableForActivation;
	}

	public boolean isAvailableForActivation() {
		return availableForActivation;
	}

	public void setAvailableForChange(boolean newAvailableForChange) {
		availableForChange = newAvailableForChange;
	}

	public boolean isAvailableForChange() {
		return availableForChange;
	}

	public boolean isAssociated(ServiceSummary serviceSummary, int term) {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("DiscountPlanInfo:[\n");
		s.append("    effectiveDate=[").append(effectiveDate).append("]\n");
		s.append("    expiration=[").append(expiration).append("]\n");
		if (groupCodes == null) {
			s.append("    groupCodes=[null]\n");
		} else if (groupCodes.length == 0) {
			s.append("    groupCodes={}\n");
		} else {
			for (int i = 0; i < groupCodes.length; i++) {
				s.append("    groupCodes[" + i + "]=[").append(
					groupCodes[i]).append(
					"]\n");
			}
		}
		s.append("    months=[").append(months).append("]\n");
		s.append("    amount=[").append(amount).append("]\n");
		s.append("    percent=[").append(percent).append("]\n");
		s.append("    description=[").append(description).append("]\n");
		s.append("    descriptionFrench=[").append(descriptionFrench).append(
			"]\n");
		s.append("    code=[").append(code).append("]\n");
		s.append("    availableForActivation=[").append(
			availableForActivation).append(
			"]\n");
		s.append("    availableForChange=[").append(availableForChange).append(
			"]\n");
		s.append("    productType=[").append(productType).append("]\n");
		s.append("    level=[").append(level).append("]\n");
		if (discountBrandIDs == null) {
			s.append("    discountBrandIDs=[null]\n");
		} else if (discountBrandIDs.length == 0) {
			s.append("    discountBrandIDs={}\n");
		} else {
			for (int i = 0; i < discountBrandIDs.length; i++) {
				s.append("    discountBrandIDs[" + i + "]=[").append(
						discountBrandIDs[i]).append(
					"]\n");
			}
		}
		s.append("]");

		return s.toString();
	}
	public void setOfferExpirationDate(Date offerExpirationDate) {
		this.offerExpirationDate = offerExpirationDate;
	}
	public Date getOfferExpirationDate() {
		return offerExpirationDate;
	}
	public boolean isPricePlanDiscount() {
		return pricePlanDiscount;
	}
	public void setPricePlanDiscount(boolean pricePlanDiscount) {
		this.pricePlanDiscount = pricePlanDiscount;
	}

	public int[] getDiscountBrandIDs() {
		return discountBrandIDs;
	}

	public void setDiscountBrandIDs(int[] discountBrandIDs) {
		this.discountBrandIDs = discountBrandIDs;
	}

}
