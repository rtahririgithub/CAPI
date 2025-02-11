package com.telus.eas.utility.info;

import com.telus.api.reference.ServiceDataSharingGroup;
import com.telus.eas.framework.info.Info;

public class ServiceDataSharingGroupInfo extends Info implements ServiceDataSharingGroup {

	private static final long serialVersionUID = 1L;
	
	private String code;
	private boolean contributing;
	
	public String getDataSharingGroupCode() {
		return code;
	}
	
	public void setDataSharingGroupCode(String code) {
		this.code = code;
	}
	
	public boolean isContributing() {
		return contributing;
	}
	
	public void setContributing(boolean contributing) {
		this.contributing = contributing;
	}
	
	public String toString() {
	    StringBuffer s = new StringBuffer();
	    s.append("ServiceDataSharingGroupInfo:{\n");
	    s.append("    code=[").append(code).append("]\n");
	    s.append("    contributing=[").append(contributing).append("]\n");
	    s.append("}");

	    return s.toString();
	  }


}
