package com.telus.eas.utility.info;

import java.util.List;

import com.telus.eas.framework.info.Info;

public class MultilingualCodeDescriptionInfo extends Info {

	public static final long serialVersionUID = 1L;

	private String code;
	private List<MultilingualTextInfo> descriptionList;

	public MultilingualCodeDescriptionInfo() { }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<MultilingualTextInfo> getDescriptionList() {
		return descriptionList;
	}

	public void setDescriptionList(List<MultilingualTextInfo> descriptionList) {
		this.descriptionList = descriptionList;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("MultilingualCodeDescriptionInfo: {\n");
		s.append("    code=[").append(getCode()).append("]\n");
		s.append("    descriptionList=[\n");
		if (getDescriptionList() != null && !getDescriptionList().isEmpty()) {
			for (MultilingualTextInfo info : getDescriptionList()) {
				s.append(info).append("\n");
			}
		} else {
			s.append("    <null>\n");
		}
		s.append("    ]\n");
		s.append("}");

		return s.toString();
	}

}