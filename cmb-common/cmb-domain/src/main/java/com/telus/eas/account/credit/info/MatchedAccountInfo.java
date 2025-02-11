package com.telus.eas.account.credit.info;

import java.util.List;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.framework.info.Info;

public class MatchedAccountInfo extends Info {

	static final long serialVersionUID = 1L;

	private AccountInfo account;
	private List<MatchedReasonInfo> matchedReasonList;

	public AccountInfo getAccount() {
		return account;
	}

	public void setAccount(AccountInfo account) {
		this.account = account;
	}

	public List<MatchedReasonInfo> getMatchedReasonList() {
		return matchedReasonList;
	}

	public void setMatchedReasonList(List<MatchedReasonInfo> matchedReasonList) {
		this.matchedReasonList = matchedReasonList;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer();

		s.append("MatchedAccountInfo: {\n");
		s.append("    account=[").append(getAccount()).append("]\n");
		s.append("    matchedReasonList=[\n");
		if (getMatchedReasonList() != null && !getMatchedReasonList().isEmpty()) {
			for (MatchedReasonInfo info : getMatchedReasonList()) {
				s.append(info).append("\n");
			}
		} else {
			s.append("    <null>\n");
		}
		s.append("    ]\n");
		s.append("}");

		return s.toString();
	}

}