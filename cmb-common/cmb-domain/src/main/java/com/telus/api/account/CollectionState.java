package com.telus.api.account;

import java.util.Date;

public interface CollectionState {
	String getPathCode();
	String getCollectorCode();
	String getCollectorName();
	Date getCollectorAssignedDate();
	String getAgencyCode();
	Date getAgencyAssignedDate();
	boolean isAutoTreatment();
	CollectionStep getPreviousCollectionStep();
	CollectionStep getNextCollectionStep();
	String getNextStepApprovalCode();
}
