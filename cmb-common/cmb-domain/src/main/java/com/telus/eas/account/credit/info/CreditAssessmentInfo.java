package com.telus.eas.account.credit.info;

import java.util.Date;
import java.util.List;

import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.MultilingualTextInfo;

public class CreditAssessmentInfo extends Info {

	static final long serialVersionUID = 1L;

	public static final String TYPE_ACCOUNT_CREDIT_CHECK = "ACCOUNT_CREDIT_CHECK";
	public static final String TYPE_OVERRIDE_CREDIT_WORTHINESS = "OVERRIDE_CREDIT_WORTHINESS";
	public static final String TYPE_ELIGIBILITY_CREDIT_CHECK = "ELIGIBILITY_CREDIT_CHECK";
	
	private long creditAssessmentID;
	private String creditAssessmentResultCode;
	private String creditAssessmentResultReasonCode;
	private String creditAssessmentTypeCode;
	private String creditAssessmentSubTypeCode;
	private List<MultilingualTextInfo> assessmentMessageList;
	private Date creditAssessmentDate;
	private List<CreditWarningInfo> creditWarningList;
	private String creditAssessmentType;
	
	// ACCOUNT_CREDIT_CHECK attributes
	private CreditBureauDocumentInfo bureauReportDocument;
	private CreditBureauDocumentInfo printImageDocument;
	
	// ACCOUNT_CREDIT_CHECK and OVERRIDE_CREDIT_WORTHINESS attributes
	private CreditWorthinessInfo creditWorthiness;

	// ELIGIBILITY_CREDIT_CHECK attributes
	private List<CreditProgramSubscriberEligibilityInfo> creditProgramSubscriberEligibilityList;
	
	public long getCreditAssessmentID() {
		return creditAssessmentID;
	}

	public void setCreditAssessmentID(long creditAssessmentID) {
		this.creditAssessmentID = creditAssessmentID;
	}

	public String getCreditAssessmentResultCode() {
		return creditAssessmentResultCode;
	}

	public void setCreditAssessmentResultCode(String creditAssessmentResultCode) {
		this.creditAssessmentResultCode = creditAssessmentResultCode;
	}

	public String getCreditAssessmentResultReasonCode() {
		return creditAssessmentResultReasonCode;
	}

	public void setCreditAssessmentResultReasonCode(String creditAssessmentResultReasonCode) {
		this.creditAssessmentResultReasonCode = creditAssessmentResultReasonCode;
	}

	public String getCreditAssessmentTypeCode() {
		return creditAssessmentTypeCode;
	}

	public void setCreditAssessmentTypeCode(String creditAssessmentTypeCode) {
		this.creditAssessmentTypeCode = creditAssessmentTypeCode;
	}

	public String getCreditAssessmentSubTypeCode() {
		return creditAssessmentSubTypeCode;
	}

	public void setCreditAssessmentSubTypeCode(String creditAssessmentSubTypeCode) {
		this.creditAssessmentSubTypeCode = creditAssessmentSubTypeCode;
	}

	public List<MultilingualTextInfo> getAssessmentMessageList() {
		return assessmentMessageList;
	}

	public void setAssessmentMessageList(List<MultilingualTextInfo> assessmentMessageList) {
		this.assessmentMessageList = assessmentMessageList;
	}

	public Date getCreditAssessmentDate() {
		return creditAssessmentDate;
	}

	public void setCreditAssessmentDate(Date creditAssessmentDate) {
		this.creditAssessmentDate = creditAssessmentDate;
	}
	
	public String getCreditAssessmentType() {
		return creditAssessmentType;
	}

	public void setCreditAssessmentType(String creditAssessmentType) {
		this.creditAssessmentType = creditAssessmentType;
	}	
	
	public CreditWorthinessInfo getCreditWorthiness() {
		return creditWorthiness;
	}

	public void setCreditWorthiness(CreditWorthinessInfo creditWorthiness) {
		this.creditWorthiness = creditWorthiness;
	}
	
	public CreditBureauDocumentInfo getBureauReportDocument() {
		return bureauReportDocument;
	}

	public void setBureauReportDocument(CreditBureauDocumentInfo bureauReportDocument) {
		this.bureauReportDocument = bureauReportDocument;
	}

	public CreditBureauDocumentInfo getPrintImageDocument() {
		return printImageDocument;
	}

	public void setPrintImageDocument(CreditBureauDocumentInfo printImageDocument) {
		this.printImageDocument = printImageDocument;
	}
	
	public List<CreditProgramSubscriberEligibilityInfo> getCreditProgramSubscriberEligibilityList() {
		return creditProgramSubscriberEligibilityList;
	}

	public void setCreditProgramSubscriberEligibilityList(List<CreditProgramSubscriberEligibilityInfo> creditProgramSubscriberEligibilityList) {
		this.creditProgramSubscriberEligibilityList = creditProgramSubscriberEligibilityList;
	}

	public List<CreditWarningInfo> getCreditWarningList() {
		return creditWarningList;
	}

	public void setCreditWarningList(List<CreditWarningInfo> creditWarningList) {
		this.creditWarningList = creditWarningList;
	}
	
	public boolean referToCreditAnalyst() {
		return !(getCreditWarningList() == null || getCreditWarningList().isEmpty());
	}
	
	public String toString() {

		StringBuffer s = new StringBuffer();

		s.append("CreditAssessmentInfo: {\n");
		s.append("    creditAssessmentID=[").append(getCreditAssessmentID()).append("]\n");
		s.append("    creditAssessmentResultCode=[").append(getCreditAssessmentResultCode()).append("]\n");
		s.append("    creditAssessmentResultReasonCode=[").append(getCreditAssessmentResultReasonCode()).append("]\n");
		s.append("    creditAssessmentTypeCode=[").append(getCreditAssessmentTypeCode()).append("]\n");
		s.append("    creditAssessmentSubTypeCode=[").append(getCreditAssessmentSubTypeCode()).append("]\n");
		s.append("    assessmentMessageList=[\n");
		if (getAssessmentMessageList() != null && !getAssessmentMessageList().isEmpty()) {
			for (MultilingualTextInfo info : getAssessmentMessageList()) {
				s.append(info).append("\n");
			}
		} else {
			s.append("    <null>\n");
		}
		s.append("    ]\n");
		s.append("    creditAssessmentDate=[").append(getCreditAssessmentDate()).append("]\n");
		s.append("    creditAssessmentType=[").append(getCreditAssessmentType()).append("]\n");
		s.append("    creditWorthiness=[").append(getCreditWorthiness()).append("]\n");
		s.append("    creditProgramSubscriberEligibilityList=[\n");
		if (getCreditProgramSubscriberEligibilityList() != null && !getCreditProgramSubscriberEligibilityList().isEmpty()) {
			for (CreditProgramSubscriberEligibilityInfo info : getCreditProgramSubscriberEligibilityList()) {
				s.append(info).append("\n");
			}
		} else {
			s.append("    <null>\n");
		}
		s.append("    ]\n");
		s.append("    creditWarningList=[\n");
		if (getCreditWarningList() != null && !getCreditWarningList().isEmpty()) {
			for (CreditWarningInfo info : getCreditWarningList()) {
				s.append(info).append("\n");
			}
		} else {
			s.append("    <null>\n");
		}
		s.append("    ]\n");
		s.append("    referToCreditAnalyst=[").append(referToCreditAnalyst()).append("]\n");
		s.append("}");

		return s.toString();
	}

}