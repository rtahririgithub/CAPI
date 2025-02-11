package com.telus.eas.portability.info;

import com.telus.api.portability.PortOutEligibility;
import com.telus.eas.framework.info.Info;

public class PortOutEligibilityInfo extends Info implements PortOutEligibility {
	
	private boolean eligible;
	private boolean transferBlocking;

	public boolean isEligible() {
		return eligible;
	}

	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}

	public boolean isTransferBlocking() {
		return transferBlocking;
	}

	public void setTransferBlocking(boolean transferBlocking) {
		this.transferBlocking = transferBlocking;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer();

		s.append("PortOutEligibilityInfo:{\n");
		s.append("    eligible=[").append(eligible).append("]\n");
		s.append("    transferBlocking=[").append(transferBlocking).append("]\n");
		s.append("}");

		return s.toString();
	}
}
