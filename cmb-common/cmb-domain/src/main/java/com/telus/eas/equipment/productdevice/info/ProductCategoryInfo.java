package com.telus.eas.equipment.productdevice.info;

import java.util.List;

import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.MultilingualTextInfo;

public class ProductCategoryInfo extends Info {

	static final long serialVersionUID = 1L;

	private long categoryID;
	private List<MultilingualTextInfo> categoryDescriptionList;

	public long getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(long categoryID) {
		this.categoryID = categoryID;
	}

	public List<MultilingualTextInfo> getCategoryDescriptionList() {
		return categoryDescriptionList;
	}

	public void setCategoryDescriptionList(List<MultilingualTextInfo> categoryDescriptionList) {
		this.categoryDescriptionList = categoryDescriptionList;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("ProductCategoryInfo: {\n");
		s.append("    categoryID=[").append(getCategoryID()).append("]\n");
		s.append("    categoryDescriptionList=[\n");
		for (MultilingualTextInfo info : getCategoryDescriptionList()) {
			s.append(info).append("\n");
		}
		s.append("    ]\n");
		s.append("}");

		return s.toString();
	}

}