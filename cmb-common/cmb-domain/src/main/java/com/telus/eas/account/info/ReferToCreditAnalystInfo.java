package com.telus.eas.account.info;

import java.io.Serializable;

public class ReferToCreditAnalystInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean referToCreditAnalyst;
	
	private String reasonCode;
	
	private String reasonMessage;
	
	public boolean isReferToCreditAnalyst() {
		return referToCreditAnalyst;
	}
	public void setReferToCreditAnalyst(boolean referToCreditAnalyst) {
		this.referToCreditAnalyst = referToCreditAnalyst;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReasonMessage() {
		return reasonMessage;
	}
	public void setReasonMessage(String reasonMessage) {
		this.reasonMessage = reasonMessage;
	}
	public String toString() {
		
		StringBuffer s = new StringBuffer();

		s.append("ReferToCreditAnalystInfo: {\n");
		s.append("    referToCreditAnalyst=[").append(referToCreditAnalyst).append("]\n");
		s.append("    reasonCode=[").append(reasonCode).append("]\n");
		s.append("    reasonMessage=[").append(reasonMessage).append("]\n");
		s.append("}");

		return s.toString();
	}	

}
