package com.telus.eas.equipment.productdevice.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class CatalogueItemInfo extends Info {

	static final long serialVersionUID = 1L;

	private long catalogueItemID;
	private String catalogueItemTypeCode;
	private Date catalogueItemEffectiveDate;
	private Date catalogueItemExpirationDate;

	public long getCatalogueItemID() {
		return catalogueItemID;
	}

	public void setCatalogueItemID(long catalogueItemID) {
		this.catalogueItemID = catalogueItemID;
	}

	public String getCatalogueItemTypeCode() {
		return catalogueItemTypeCode;
	}

	public void setCatalogueItemTypeCode(String catalogueItemTypeCode) {
		this.catalogueItemTypeCode = catalogueItemTypeCode;
	}

	public Date getCatalogueItemEffectiveDate() {
		return catalogueItemEffectiveDate;
	}

	public void setCatalogueItemEffectiveDate(Date catalogueItemEffectiveDate) {
		this.catalogueItemEffectiveDate = catalogueItemEffectiveDate;
	}

	public Date getCatalogueItemExpirationDate() {
		return catalogueItemExpirationDate;
	}

	public void setCatalogueItemExpirationDate(Date catalogueItemExpirationDate) {
		this.catalogueItemExpirationDate = catalogueItemExpirationDate;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("CatalogueItemInfo: {\n");
		s.append("    catalogueItemID=[").append(getCatalogueItemID()).append("]\n");
		s.append("    catalogueItemTypeCode=[").append(getCatalogueItemTypeCode()).append("]\n");
		s.append("    catalogueItemEffectiveDate=[").append(getCatalogueItemEffectiveDate()).append("]\n");
		s.append("    catalogueItemExpirationDate=[").append(getCatalogueItemExpirationDate()).append("]\n");
		s.append("}");
		
		return s.toString();
	}

}