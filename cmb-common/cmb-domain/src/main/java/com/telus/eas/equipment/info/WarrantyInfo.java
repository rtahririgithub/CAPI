package com.telus.eas.equipment.info;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

import com.telus.api.equipment.Warranty;
import com.telus.eas.framework.info.Info;

public class WarrantyInfo extends Info implements Warranty {

	static final long serialVersionUID = 1L;

	public WarrantyInfo() {
	}

	private java.util.Date warrantyExpiryDate;
	private java.util.Date initialActivationDate;
	private java.util.Date DOAExpiryDate;
	private String message;

	public java.util.Date getWarrantyExpiryDate() {
		return warrantyExpiryDate;
	}
	public void setWarrantyExpiryDate(java.util.Date warrantyExpiryDate) {
		this.warrantyExpiryDate = warrantyExpiryDate;
	}
	public void setInitialActivationDate(java.util.Date initialActivationDate) {
		this.initialActivationDate = initialActivationDate;
	}
	public java.util.Date getInitialActivationDate() {
		return initialActivationDate;
	}
	/**
	 * @deprecated
	 */
	public void setInitialManufactureDate(java.util.Date initialManufactureDate) {
	}
	/**
	 * @deprecated
	 */
	public java.util.Date getInitialManufactureDate() {
		return null;
	}
	/**
	 * @deprecated
	 */
	public void setLatestPendingDate(java.util.Date latestPendingDate) {
	}
	/**
	 * @deprecated
	 */
	public java.util.Date getLatestPendingDate() {
		return null;
	}
	/**
	 * @deprecated
	 */
	public void setLatestPendingModel(String latestPendingModel) {
	}
	/**
	 * @deprecated
	 */
	public String getLatestPendingModel() {
		return null;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	/**
	 * @deprecated
	 */
	public void setWarrantyExtensionDate(java.util.Date warrantyExtensionDate) {
	}
	/**
	 * @deprecated
	 */
	public java.util.Date getWarrantyExtensionDate() {
		return null;
	}
	public void setDOAExpiryDate(java.util.Date DOAExpiryDate) {
		this.DOAExpiryDate = DOAExpiryDate;
	}
	public java.util.Date getDOAExpiryDate() {
		return DOAExpiryDate;
	}
}