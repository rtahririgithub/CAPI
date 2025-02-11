package com.telus.eas.servicerequest.info;

import com.telus.api.servicerequest.*;
import com.telus.eas.framework.info.Info;

public class ServiceRequestNoteInfo extends Info implements ServiceRequestNote {
	
	private static final long serialVersionUID = 1L;
	private long noteTypeId;
	private String noteText;
	
	public void setNoteTypeId(long noteTypeId) {
		this.noteTypeId = noteTypeId;
	}
	
	public long getServiceRequestNoteTypeId() {
		return noteTypeId;
	}
	
	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}
	
	public String getServiceRequestNoteText() {
		return noteText;
	}

	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("ServiceRequestNoteInfo:[\n");
		s.append("    noteTypeId=[").append(noteTypeId).append("]\n");
		s.append("    noteText=[").append(noteText).append("]\n");
		s.append("]");

		return s.toString();
	}
}
