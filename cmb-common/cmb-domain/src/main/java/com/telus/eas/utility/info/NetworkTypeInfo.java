package com.telus.eas.utility.info;

import com.telus.api.reference.NetworkType;
import com.telus.eas.framework.info.Info;

public class NetworkTypeInfo extends Info implements NetworkType{
	static final long serialVersionUID = 1L;	
	 
	protected String description;	
	protected String descriptionFrench;	
	protected String networkTypeCode;	
	 
	public String getCode() {		
		return networkTypeCode;	
	}
	 
	public String getDescription() {
		return description;	
	}	
	
	public String getDescriptionFrench() {
		return descriptionFrench;
	}	
	
	public void setDescription(String description) {
		this.description = description;	
	}
	
	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}
	
	public void setCode(String networkTypeCode) {	
		this.networkTypeCode = networkTypeCode;	
	}

    public String toString() {

        StringBuffer s = new StringBuffer();

        s.append("NetworkTypeInfo:[\n");
        s.append("    code=[").append(String.valueOf(networkTypeCode)).append("]\n");
        s.append("    description=[").append(description).append("]\n");
        s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
        s.append("]");

        return s.toString();

    }

}