package com.telus.eas.subscriber.info;

import com.telus.eas.framework.info.Info;

public class DataSharingSocTransferInfo extends Info {

	private static final long serialVersionUID = 1L;

	private String candidateSubscriberId;
	private String candidateSocCode;
	private String candidateSocDescription;
	private String candidateSocDescriptionFrench;
	private String contributorSocCode;
	private String contributorSocDescription;
	private String contributorSocDescriptionFrench;

	public DataSharingSocTransferInfo() {
	}
		
	public String getCandidateSubscriberId() {
		return candidateSubscriberId;
	}

	public void setCandidateSubscriberId(String candidateSubscriberId) {
		this.candidateSubscriberId = candidateSubscriberId;
	}

	public String getCandidateSocCode() {
		return candidateSocCode;
	}

	public void setCandidateSocCode(String candidateSocCode) {
		this.candidateSocCode = candidateSocCode;
	}

	public String getCandidateSocDescription() {
		return candidateSocDescription;
	}

	public void setCandidateSocDescription(String candidateSocDescription) {
		this.candidateSocDescription = candidateSocDescription;
	}

	public String getCandidateSocDescriptionFrench() {
		return candidateSocDescriptionFrench;
	}

	public void setCandidateSocDescriptionFrench(
			String candidateSocDescriptionFrench) {
		this.candidateSocDescriptionFrench = candidateSocDescriptionFrench;
	}

	public String getContributorSocCode() {
		return contributorSocCode;
	}

	public void setContributorSocCode(String contributorSocCode) {
		this.contributorSocCode = contributorSocCode;
	}

	public String getContributorSocDescription() {
		return contributorSocDescription;
	}

	public void setContributorSocDescription(String contributorSocDescription) {
		this.contributorSocDescription = contributorSocDescription;
	}

	public String getContributorSocDescriptionFrench() {
		return contributorSocDescriptionFrench;
	}

	public void setContributorSocDescriptionFrench(
			String contributorSocDescriptionFrench) {
		this.contributorSocDescriptionFrench = contributorSocDescriptionFrench;
	}

	public String toString() {
        StringBuffer s = new StringBuffer();

		s.append("DataSharingSocTransferInfo:[\n");
		s.append("    candidateSubscriberId=[").append(candidateSubscriberId).append("]\n");
		s.append("    candidateSocCode=[").append(candidateSocCode).append("]\n");
		s.append("    candidateSocDescription=[").append(candidateSocDescription).append("]\n");
		s.append("    candidateSocDescriptionFrench=[").append(candidateSocDescriptionFrench).append("]\n");
		s.append("    contributorSocCode=[").append(contributorSocCode).append("]\n");
		s.append("    contributorSocDescription=[").append(contributorSocDescription).append("]\n");
		s.append("    contributorSocDescriptionFrench=[").append(contributorSocDescriptionFrench).append("]\n");
		s.append("]");
		
	    return s.toString();
	}

}