package com.telus.eas.equipment.productdevice.info;

import java.util.List;

import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.MultilingualTextInfo;

public class ProductTechnologyTypeInfo extends Info {

	static final long serialVersionUID = 1L;

	private String technologyType;
	private List<MultilingualTextInfo> technologyTypeDescriptionList;

	public String getTechnologyType() {
		return technologyType;
	}

	public void setTechnologyType(String technologyType) {
		this.technologyType = technologyType;
	}

	public List<MultilingualTextInfo> getTechnologyTypeDescriptionList() {
		return technologyTypeDescriptionList;
	}

	public void setTechnologyTypeDescriptionList(List<MultilingualTextInfo> technologyTypeDescriptionList) {
		this.technologyTypeDescriptionList = technologyTypeDescriptionList;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("ProductTechnologyTypeInfo: {\n");
		s.append("    technologyType=[").append(getTechnologyType()).append("]\n");
		s.append("    technologyTypeDescriptionList=[\n");
		for (MultilingualTextInfo info : getTechnologyTypeDescriptionList()) {
			s.append(info).append("\n");
		}
		s.append("    ]\n");
		s.append("}");
		
		return s.toString();
	}

}