package com.telus.eas.servicerequest.info;

import com.telus.api.servicerequest.*;
import com.telus.eas.framework.info.Info;

public class ServiceRequestHeaderInfo extends Info implements ServiceRequestHeader {
	
	private static final long serialVersionUID = 1L;
	private String languageCode;
	private long applicationId;
	private String referenceNumber;
	private ServiceRequestParent parentRequest;
	private ServiceRequestNote note;
	private String applicationName = "N/A"; //this field is for debugging purpose only

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	
	public String getLanguageCode() {
		return languageCode;
	}
	
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	
	public long getApplicationId() {
		return applicationId;
	}
	
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}
	
	public void setServiceRequestParent(ServiceRequestParent parentRequest) {
		this.parentRequest = parentRequest;
	}
	
	public ServiceRequestParent getServiceRequestParent() {
		return parentRequest;
	}
	
	public void setServiceRequestNote(ServiceRequestNote note) {
		this.note = note;
	}
	
	public ServiceRequestNote getServiceRequestNote() {
		return note;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("ServiceRequestHeaderInfo:[\n");

		s.append("    languageCode=[").append(languageCode).append("]\n");
		s.append("    applicationId=[").append(applicationId).append("]\n");
		s.append("    applicationName=[").append(applicationName).append("]\n");
		s.append("    referenceNumber=[").append(referenceNumber).append("]\n");
		s.append("    requestParent=[").append(parentRequest == null ? "null" : parentRequest.toString()).append("]\n");
		s.append("    note=[").append(note.toString()).append("]\n");
		s.append("]");

		return s.toString();
	}
}
