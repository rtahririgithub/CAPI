package com.telus.eas.servicerequest.info;

import java.sql.Timestamp;

import com.telus.api.servicerequest.*;
import com.telus.eas.framework.info.Info;

public class ServiceRequestParentInfo extends Info implements ServiceRequestParent {
	
	private static final long serialVersionUID = 1L;
	private long parentId;
	private Timestamp timestamp;
	private long relationshipTypeId;
	
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	
	public long getServiceRequestParentId() {
		return parentId;
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Timestamp getServiceRequestTimestamp() {
		return timestamp;
	}
	
	public void setRelationshipTypeId(long relationshipTypeId) {
		this.relationshipTypeId = relationshipTypeId;
	}
	
	public long getServiceRequestRelationshipTypeId() {
		return relationshipTypeId;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("ServiceRequestParentInfo:[\n");
		s.append("    parentId=[").append(parentId).append("]\n");
		s.append("    timestamp=[").append(timestamp).append("]\n");
		s.append("    relationshipTypeId=[").append(relationshipTypeId).append("]\n");
		s.append("]");

		return s.toString();
	}
}
