/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import java.util.Date;
import com.telus.api.*;
import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class SalesRepInfo extends Info implements SalesRep {
	static final long serialVersionUID = 1L;

	protected String code;
	protected String name;
	protected String dealerCode;
	protected String description;
	protected String descriptionFrench;
	protected Date expiryDate;
	protected Date effectiveDate;

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public String getDescription() {
		return "";
	}

	public String getDescriptionFrench() {
		return "";
	}

	public Date getEffectiveDate() {
		return effectiveDate;

	}

	public Date getExpiryDate() {
		return expiryDate;

	}

	public void setName(String newName) {
		name = newName;
	}

	public void setCode(String newCode) {
		code = newCode;
	}

	public void setDealerCode(String newDealerCode) {
		dealerCode = newDealerCode;
	}

	public void setDescription(String newDescription) {
		description = newDescription;
	}

	public void setDescriptionFrench(String newDescription) {
		descriptionFrench = newDescription;
	}

	public void setEffectiveDate(Date newDate) {
		effectiveDate = newDate;
	}

	public void setExpiryDate(Date newDate) {
		expiryDate = newDate;
	}

	public void save() throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method not implemented");
	}

	public void transferSalesRep(String dealerCode, Date transferDate) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method not implemented");
	}

	public String toString() {
		return "SalesRepInfo [code=" + code + ", name=" + name + ", dealerCode=" + dealerCode + ", description=" + description + ", descriptionFrench=" + descriptionFrench + ", expiryDate="
				+ expiryDate + ", effectiveDate=" + effectiveDate + "]";
	}

	
}
