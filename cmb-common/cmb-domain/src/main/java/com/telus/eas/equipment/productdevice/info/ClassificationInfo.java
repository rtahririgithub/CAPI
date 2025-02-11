package com.telus.eas.equipment.productdevice.info;

import java.util.List;

import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.MultilingualTextInfo;

public class ClassificationInfo extends Info {

	static final long serialVersionUID = 1L;

	private long classificationID;
	private String classificationCode;
	private List<MultilingualTextInfo> classificationDescriptionList;

	public long getClassificationID() {
		return classificationID;
	}

	public void setClassificationID(long classificationID) {
		this.classificationID = classificationID;
	}

	public String getClassificationCode() {
		return classificationCode;
	}

	public void setClassificationCode(String classificationCode) {
		this.classificationCode = classificationCode;
	}

	public List<MultilingualTextInfo> getClassificationDescriptionList() {
		return classificationDescriptionList;
	}

	public void setClassificationDescriptionList(List<MultilingualTextInfo> classificationDescriptionList) {
		this.classificationDescriptionList = classificationDescriptionList;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("ClassificationInfo: {\n");
		s.append("    classificationID=[").append(getClassificationID()).append("]\n");
		s.append("    classificationCode=[").append(getClassificationCode()).append("]\n");
		s.append("    classificationDescriptionList=[\n");
		if (classificationDescriptionList != null) {
			for (MultilingualTextInfo info : getClassificationDescriptionList()) {
				s.append(info).append("\n");
			}
		}
		s.append("    ]\n");
		s.append("}");
		
		return s.toString();
	}

}