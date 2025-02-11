package com.telus.eas.equipment.productdevice.info;

import com.telus.eas.framework.info.Info;

public class CatalogueItemCategoryInfo extends Info {

	static final long serialVersionUID = 1L;

	private String catalogueItemCategoryCode;
	private String catalogueItemCategoryTypeCode;

	public String getCatalogueItemCategoryCode() {
		return catalogueItemCategoryCode;
	}

	public void setCatalogueItemCategoryCode(String catalogueItemCategoryCode) {
		this.catalogueItemCategoryCode = catalogueItemCategoryCode;
	}

	public String getCatalogueItemCategoryTypeCode() {
		return catalogueItemCategoryTypeCode;
	}

	public void setCatalogueItemCategoryTypeCode(String catalogueItemCategoryTypeCode) {
		this.catalogueItemCategoryTypeCode = catalogueItemCategoryTypeCode;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("CatalogueItemCategoryInfo: {\n");
		s.append("    catalogueItemCategoryCode=[").append(getCatalogueItemCategoryCode()).append("]\n");
		s.append("    catalogueItemCategoryTypeCode=[").append(getCatalogueItemCategoryTypeCode()).append("]\n");
		s.append("}");

		return s.toString();
	}

}