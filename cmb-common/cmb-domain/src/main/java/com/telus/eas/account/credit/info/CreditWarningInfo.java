package com.telus.eas.account.credit.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class CreditWarningInfo extends Info {

	static final long serialVersionUID = 1L;

	private String categoryCode;
	private String typeCode;
	private String code;
	private String itemTypeCode;
	private Date detectionDate;
	private String statusCode;

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getItemTypeCode() {
		return itemTypeCode;
	}

	public void setItemTypeCode(String itemTypeCode) {
		this.itemTypeCode = itemTypeCode;
	}

	public Date getDetectionDate() {
		return detectionDate;
	}

	public void setDetectionDate(Date detectionDate) {
		this.detectionDate = detectionDate;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer();

		s.append("CreditWarningInfo: {\n");
		s.append("    categoryCode=[").append(getCategoryCode()).append("]\n");
		s.append("    typeCode=[").append(getTypeCode()).append("]\n");
		s.append("    code=[").append(getCode()).append("]\n");
		s.append("    itemTypeCode=[").append(getItemTypeCode()).append("]\n");
		s.append("    detectionDate=[").append(getDetectionDate()).append("]\n");
		s.append("    statusCode=[").append(getStatusCode()).append("]\n");
		s.append("}");

		return s.toString();
	}

}