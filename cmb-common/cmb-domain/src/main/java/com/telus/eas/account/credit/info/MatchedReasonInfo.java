package com.telus.eas.account.credit.info;

import com.telus.eas.framework.info.Info;

public class MatchedReasonInfo extends Info {

	static final long serialVersionUID = 1L;

	private String matchCriteria;
	private boolean matchedInd;

	public String getMatchCriteria() {
		return matchCriteria;
	}

	public void setMatchCriteria(String matchCriteria) {
		this.matchCriteria = matchCriteria;
	}

	public boolean isMatchedInd() {
		return matchedInd;
	}

	public void setMatchedInd(boolean matchedInd) {
		this.matchedInd = matchedInd;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer();

		s.append("MatchedReasonInfo: {\n");
		s.append("    matchCriteria=[").append(getMatchCriteria()).append("]\n");
		s.append("    matchedInd=[").append(isMatchedInd()).append("]\n");
		s.append("}");

		return s.toString();
	}

}