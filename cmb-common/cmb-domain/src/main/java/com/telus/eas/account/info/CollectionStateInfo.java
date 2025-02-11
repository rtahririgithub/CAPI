package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.account.CollectionState;
import com.telus.api.account.CollectionStep;
import com.telus.eas.framework.info.Info;

public class CollectionStateInfo extends Info implements CollectionState{
	static final long serialVersionUID = 1L;
	private String pathCode;
	private String collectorCode;
	private String collectorName;
	private Date collectorAssignedDate;
	private String agencyCode;
	private Date agencyAssignedDate;
	private boolean autoTreatment;
	private CollectionStep prevCollectionStep;
	private CollectionStep nextCollectionStep;
	private String nextStepApprovalCode;
	
	public String getPathCode(){
		return pathCode;
	}
	
	public String getCollectorCode(){
		return collectorCode;
	}
	
	public String getCollectorName(){
		return collectorName;
	}
	
	public Date getCollectorAssignedDate(){
		return collectorAssignedDate;
	}
		
	public String getAgencyCode(){
		return agencyCode;
	}
	
	public Date getAgencyAssignedDate(){
		return agencyAssignedDate;
	}
	
	public boolean isAutoTreatment(){
		return autoTreatment;
	}
	
	public CollectionStep getPreviousCollectionStep(){
		return prevCollectionStep;
	}
	
	public CollectionStep getNextCollectionStep(){
		return nextCollectionStep;
	}
	
	public String getNextStepApprovalCode(){
		return nextStepApprovalCode;
	}

	public void setAgencyAssignedDate(Date agencyAssignedDate) {
		this.agencyAssignedDate = agencyAssignedDate;
	}

	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}

	public void setAutoTreatment(boolean autoTreatment) {
		this.autoTreatment = autoTreatment;
	}

	public void setCollectorAssignedDate(Date collectorAssignedDate) {
		this.collectorAssignedDate = collectorAssignedDate;
	}

	public void setCollectorCode(String collectorCode) {
		this.collectorCode = collectorCode;
	}

	public void setCollectorName(String collectorName) {
		this.collectorName = collectorName;
	}

	public void setNextCollectionStep(CollectionStep nextCollectionStep) {
		this.nextCollectionStep = nextCollectionStep;
	}

	public void setNextStepApprovalCode(String nextStepApprovalCode) {
		this.nextStepApprovalCode = nextStepApprovalCode;
	}

	public void setPathCode(String pathCode) {
		this.pathCode = pathCode;
	}

	public void setPrevCollectionStep(CollectionStep prevCollectionStep) {
		this.prevCollectionStep = prevCollectionStep;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("CollectionStateInfo:{\n");
		s.append("    pathCode=[").append(pathCode).append("]\n");          
		s.append("    collectorCode=[").append(collectorCode).append("]\n");
		s.append("    collectorName=[").append(collectorName).append("]\n");
		s.append("    collectorAssignedDate=[").append(collectorAssignedDate).append("]\n");
		s.append("    agencyCode=[").append(agencyCode).append("]\n");
		s.append("    agencyAssignedDate=[").append(agencyAssignedDate).append("]\n");
		s.append("    autoTreatment=[").append(autoTreatment).append("]\n");
		s.append("    prevCollectionStep=[").append(prevCollectionStep).append("]\n");
		s.append("    nextCollectionStep=[").append(nextCollectionStep).append("]\n");
		s.append("    nextStepApprovalCode=[").append(nextStepApprovalCode).append("]\n");
		s.append("}");

		return s.toString();
	}	
}
