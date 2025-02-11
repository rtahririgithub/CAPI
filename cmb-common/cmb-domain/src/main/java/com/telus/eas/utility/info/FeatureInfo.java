/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class FeatureInfo extends Info implements Feature {

	static final long serialVersionUID = 1L;

	public static final String FEATURE_CATEGORY_PTT = "PTT";
	public static final String FEATURE_CATEGORY_SMS = "SNF";
	public static final String FEATURE_CATEGORY_WF = "WF";
	public static final String SWITCH_CODE_CALLING_CIRCLE  = "CLCRCL";
	public static final String FEATURE_TYPE_DOLLAR_POOLING = "A";
	public static final String FEATURE_TYPE_POOLING = "P";
	public static final String SWITCH_CODE_CALL_HOME_FREE = "CHF";


	protected String code = "";
	protected String description = "";
	protected String descriptionFrench = "";
	protected boolean parameterRequired;
	protected boolean duplFeatureAllowed;
	protected boolean additionalNumberRequired;
	protected boolean wirelessWeb;
	protected boolean telephony;
	protected boolean dispatch;
	private String switchCode;
	String categoryCode;
	private String featureType;
	private String poolGroupId;
	private String parameterDefault;

	public FeatureInfo() {
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public void setCode(String code) {
		this.code = Info.padTo(code, ' ', 6);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}

	public void setParameterRequired(boolean parameterRequired) {
		this.parameterRequired = parameterRequired;
	}
	
	public boolean isParameterRequired() {
		return parameterRequired;
	}
	
	public void setDuplFeatureAllowed(boolean duplFeatureAllowed) {
		this.duplFeatureAllowed = duplFeatureAllowed;
	}
	
	public boolean isDuplFeatureAllowed() {
		return duplFeatureAllowed;
	}
	
	public void setAdditionalNumberRequired(boolean additionalNumberRequired) {
		this.additionalNumberRequired = additionalNumberRequired;
	}
	
	public boolean isAdditionalNumberRequired() {
		return additionalNumberRequired;
	}
	
	public void setWirelessWeb(boolean wirelessWeb) {
		this.wirelessWeb = wirelessWeb;
	}
	
	public boolean isWirelessWeb() {
		return wirelessWeb;
	}
	
	public void setTelephony(boolean telephony) {
		this.telephony = telephony;
	}
	
	public boolean isTelephony() {
		return telephony;
	}
	
	public void setDispatch(boolean dispatch) {
		this.dispatch = dispatch;
	}
	
	public boolean isDispatch() {
		return dispatch;
	}
	
	public void setSwitchCode(String switchCode) {
		this.switchCode = switchCode;
	}

	public String getSwitchCode() {
		return switchCode;
	}
	
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	public String getCategoryCode() {
		return categoryCode;
	}

	public boolean isSMSNotification() {
		return FEATURE_CATEGORY_SMS.equalsIgnoreCase(categoryCode);
	}

	public String getType(){
		return featureType;
	}
	
	public void setFeatureType(String featureType){
		this.featureType = featureType;
	}

	public String getPoolGroupId() {
		return poolGroupId;
	}

	public void setPoolGroupId(String poolGroupId) {
		this.poolGroupId = poolGroupId;
	}

	public String getParameterDefault() {
		return parameterDefault;
	}

	public void setParameterDefault(String parameterDefault) {
		this.parameterDefault = parameterDefault;
	}

	public boolean isCallingCircle() {
		boolean result = false;
		if (categoryCode != null) {
			result = CATEGORY_CODE_CALLING_CIRCLE.equalsIgnoreCase(categoryCode.trim());
		}
		return result;
	}
	
	public boolean isPooling() {
		return featureType != null && featureType.trim().equalsIgnoreCase(FEATURE_TYPE_POOLING);
	}
	
	public boolean isDollarPooling() {
		return featureType != null && featureType.trim().equalsIgnoreCase(FEATURE_TYPE_DOLLAR_POOLING);
	}
	
	public String toString() {
		
		StringBuffer s = new StringBuffer(128);
		s.append("FeatureInfo:[\n");
		s.append("    code=[").append(code).append("]\n");
		s.append("    description=[").append(description).append("]\n");
		s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
		s.append("    parameterRequired=[").append(parameterRequired).append("]\n");
		s.append("    duplFeatureAllowed=[").append(duplFeatureAllowed).append("]\n");
		s.append("    additionalNumberRequired=[").append(additionalNumberRequired).append("]\n");
		s.append("    wirelessWeb=[").append(wirelessWeb).append("]\n");
		s.append("    telephony=[").append(telephony).append("]\n");
		s.append("    dispatch=[").append(dispatch).append("]\n");
		s.append("    switchCode=[").append(switchCode).append("]\n");
		s.append("    categoryCode=[").append(categoryCode).append("]\n");
		s.append("    featureType=[").append(featureType).append("]\n");
		s.append("    isCallingCircle=[").append(isCallingCircle()).append("]\n");
		s.append("    isDollarPooling=[").append(isDollarPooling()).append("]\n");
		s.append("    isPooling=[").append(isPooling()).append("]\n");
		s.append("    poolGroupId=[").append(poolGroupId).append("]\n");
		s.append("]");

		return s.toString();
	}
}



