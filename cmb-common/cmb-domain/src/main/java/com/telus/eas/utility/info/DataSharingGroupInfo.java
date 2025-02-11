package com.telus.eas.utility.info;

import com.telus.api.reference.DataSharingGroup;
import com.telus.eas.framework.info.Info;

public class DataSharingGroupInfo extends Info implements DataSharingGroup {

	private static final long serialVersionUID = 1L;
	
	private String description;
	private String descriptionFrench;
	private String code;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescriptionFrench() {
		return descriptionFrench;
	}
	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	public String toString() {
	    StringBuffer s = new StringBuffer();
	    s.append("DataSharingGroupInfo:{\n");
	    s.append("    description=[").append(description).append("]\n");
	    s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
	    s.append("    code=[").append(code).append("]\n");
	    s.append("}");

	    return s.toString();
	  }

}
