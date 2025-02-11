package com.telus.eas.account.credit.info;

import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.MultilingualCodeDescriptionInfo;

public class CreditWorthinessInfo extends Info {

	static final long serialVersionUID = 1L;

	private long creditWorthinessID;
	private long creditProfileID;
	private long creditAssessmentID;
	private String primaryCreditScoreCode;
	private String primaryCreditScoreTypeCode;
	private MultilingualCodeDescriptionInfo bureauDecisionCode;
	private CreditProgramInfo creditProgram;

	public long getCreditWorthinessID() {
		return creditWorthinessID;
	}

	public void setCreditWorthinessID(long creditWorthinessID) {
		this.creditWorthinessID = creditWorthinessID;
	}

	public long getCreditProfileID() {
		return creditProfileID;
	}

	public void setCreditProfileID(long creditProfileID) {
		this.creditProfileID = creditProfileID;
	}

	public long getCreditAssessmentID() {
		return creditAssessmentID;
	}

	public void setCreditAssessmentID(long creditAssessmentID) {
		this.creditAssessmentID = creditAssessmentID;
	}

	public String getPrimaryCreditScoreCode() {
		return primaryCreditScoreCode;
	}

	public void setPrimaryCreditScoreCode(String primaryCreditScoreCode) {
		this.primaryCreditScoreCode = primaryCreditScoreCode;
	}

	public String getPrimaryCreditScoreTypeCode() {
		return primaryCreditScoreTypeCode;
	}

	public void setPrimaryCreditScoreTypeCode(String primaryCreditScoreTypeCode) {
		this.primaryCreditScoreTypeCode = primaryCreditScoreTypeCode;
	}

	public MultilingualCodeDescriptionInfo getBureauDecisionCode() {
		return bureauDecisionCode;
	}

	public void setBureauDecisionCode(MultilingualCodeDescriptionInfo bureauDecisionCode) {
		this.bureauDecisionCode = bureauDecisionCode;
	}
	
	public CreditProgramInfo getCreditProgram() {
		return creditProgram;
	}

	public void setCreditProgram(CreditProgramInfo creditProgram) {
		this.creditProgram = creditProgram;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer();

		s.append("CreditWorthinessInfo: {\n");
		s.append("    creditWorthinessID=[").append(getCreditWorthinessID()).append("]\n");
		s.append("    creditProfileID=[").append(getCreditProfileID()).append("]\n");
		s.append("    creditAssessmentID=[").append(getCreditAssessmentID()).append("]\n");
		s.append("    primaryCreditScoreCode=[").append(getPrimaryCreditScoreCode()).append("]\n");
		s.append("    primaryCreditScoreTypeCode=[").append(getPrimaryCreditScoreTypeCode()).append("]\n");
		s.append("    bureauDecisionCode=[").append(getBureauDecisionCode()).append("]\n");
		s.append("    creditProgram=[").append(getCreditProgram()).append("]\n");
		s.append("}");

		return s.toString();
	}

}