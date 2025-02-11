/*
 * Created on 11-Nov-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.telus.eas.utility.info;

import com.telus.api.TelusAPIException;
import com.telus.api.reference.PrepaidRechargeDenomination;

/**
 * @author x119734
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PrepaidRechargeDenominationInfo implements
		PrepaidRechargeDenomination {
	
	private double amount;
	private int rateId;
	private String rechargeType;
	private String[] businessRole;
	private String code;
	private String description;
	private String descriptionFrench;
	

	/**
	 * @return Returns the amount.
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	/**
	 * @return Returns the businessRole.
	 */
	public String[] getBusinessRole() {
		return businessRole;
	}
	/**
	 * @param businessRole The businessRole to set.
	 */
	public void setBusinessRole(String[] businessRole) {
		this.businessRole = businessRole;
	}
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return Returns the rateId.
	 */
	public int getRateId() {
		return rateId;
	}
	/**
	 * @param rateId The rateId to set.
	 */
	public void setRateId(int rateId) {
		this.rateId = rateId;
	}
	/**
	 * @return Returns the rechargeType.
	 */
	public String getRechargeType() {
		return rechargeType;
	}
	/**
	 * @param rechargeType The rechargeType to set.
	 */
	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the descriptionFrench.
	 */
	public String getDescriptionFrench() {
		return descriptionFrench;
	}
	/**
	 * @param descriptionFrench The descriptionFrench to set.
	 */
	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}
	
	/**
	 * This method is intended to be used for one-time call.  
	 * When multiple calls are made to this method within for a given transaction, better performance
	 * can be attained through the  retainByPrivilege method on ReferenceDataManager interface
	 */
	public boolean containsPrivilege(String businessRole) throws TelusAPIException {
		return true;
	}
}
