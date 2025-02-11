package com.telus.eas.equipment.productdevice.info;

import java.util.List;

import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.MultilingualTextInfo;

public class FeatureInfo extends Info {

	static final long serialVersionUID = 1L;

	private long featureID;
	private String featureCode;
	private String featureGroupCode;
	private List<MultilingualTextInfo> featureDescriptionList;
	private List<MultilingualTextInfo> featureCommentList;

	public long getFeatureID() {
		return featureID;
	}

	public void setFeatureID(long featureID) {
		this.featureID = featureID;
	}

	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public String getFeatureGroupCode() {
		return featureGroupCode;
	}

	public void setFeatureGroupCode(String featureGroupCode) {
		this.featureGroupCode = featureGroupCode;
	}

	public List<MultilingualTextInfo> getFeatureDescriptionList() {
		return featureDescriptionList;
	}

	public void setFeatureDescriptionList(List<MultilingualTextInfo> featureDescriptionList) {
		this.featureDescriptionList = featureDescriptionList;
	}

	public List<MultilingualTextInfo> getFeatureCommentList() {
		return featureCommentList;
	}

	public void setFeatureCommentList(List<MultilingualTextInfo> featureCommentList) {
		this.featureCommentList = featureCommentList;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("FeatureInfo: {\n");
		s.append("    featureID=[").append(getFeatureID()).append("]\n");
		s.append("    featureCode=[").append(getFeatureCode()).append("]\n");
		s.append("    featureGroupCode=[").append(getFeatureGroupCode()).append("]\n");
		s.append("    featureDescriptionList=[\n");
		for (MultilingualTextInfo info : getFeatureDescriptionList()) {
			s.append(info).append("\n");
		}
		s.append("    ]\n");
		s.append("    featureCommentList=[\n");
		
		if (featureCommentList != null) {
			for (MultilingualTextInfo info : getFeatureCommentList()) {
				s.append(info).append("\n");
			}
		}
		
		s.append("    ]\n");
		s.append("}");

		return s.toString();
	}

}