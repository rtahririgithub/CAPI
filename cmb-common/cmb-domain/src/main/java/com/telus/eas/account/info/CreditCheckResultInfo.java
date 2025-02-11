/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.Address;
import com.telus.api.account.BusinessCreditIdentity;
import com.telus.api.account.CLPActivationOptionDetail;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.PersonalCredit;
import com.telus.eas.account.credit.info.CreditAssessmentInfo;
import com.telus.eas.account.credit.info.CreditProgramInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.MultilingualTextInfo;

public class CreditCheckResultInfo extends Info implements CreditCheckResult {

	static final long serialVersionUID = 1L;

	private String creditClass;
	private double limit;
	private String message;
	private String messageFrench;
	private CreditCheckResultDeposit[] deposits;
	private ReferToCreditAnalystInfo referToCreditAnalyst = new ReferToCreditAnalystInfo();
	private int creditScore;
	private String depositBarCode;
	private String creditCheckResultStatus;
	public final static String CREDIT_CLASS_CONSTANT = "0";
	public final static String PRODUCT_TYPE_CELLULAR = "C";
	private boolean creditCheckPerformed;
	private String transAlert1;
	private String transAlert2;
	private String transAlert3;
	private String transAlert4;
	private String tuReturnInd;
	private String node;
	private String existingNegInd;
	private String hawkAlertInd;
	private String bureauFile;
	private String defaultInd;
	private String transAlertInd;
	private int errorCode;
	private String errorMessage;
	private ActivationOptionInfo[] activationOptions = new ActivationOptionInfo[0];
	private ConsumerNameInfo consumerName;
	private AddressInfo address;
	private BusinessCreditIdentityInfo selectedBusiness;
	private BusinessCreditIdentityInfo[] listOfBusinesses;
	private PersonalCreditInfo personalCredit;
	private String incorporationNo;
	private Date incorporationDate;
	private Date creditDate = Calendar.getInstance().getTime(); // default to system date
	private String creditParamType;
	private String depositChgReasonCode;
	private double depositAmount;
	private String depositProductType;
	private CLPActivationOptionDetail clpActivationOptionDetail;
	private String bureauMessage;

	public String getCreditClass() {
		return creditClass;
	}

	public double getLimit() {
		return limit;
	}

	public String getMessage() {
		return message;
	}

	public String getMessageFrench() {
		return messageFrench;
	}

	public CreditCheckResultDeposit[] getDeposits() {
		return deposits;
	}

	public void setDeposits(CreditCheckResultDeposit[] deposits) {
		this.deposits = deposits;
	}

	public double getDeposit() {

		if (deposits == null || deposits.length == 0) {
			return 0;
		}

		CreditCheckResultDepositInfo[] depositsInfo = (CreditCheckResultDepositInfo[]) this.deposits;
		if (depositsInfo != null && depositsInfo.length > 0) {
			for (int i = 0; i < depositsInfo.length; i++) {
				if (depositsInfo[i] != null) {
					if (depositProductType == null) {
						if (depositsInfo[i].getProductType().equals(PRODUCT_TYPE_CELLULAR)) {
							return depositsInfo[i].getDeposit();
						} else if (depositsInfo[i].getProductType().equals("I")) {
							return depositsInfo[i].getDeposit();
						}
					} else {
						if (depositsInfo[i].getProductType().equals(depositProductType)) {
							return depositsInfo[i].getDeposit();
						}
					}
				}
			}
		}

		return 0;
	}
	
	/**
	 * 2018-07-26
	 * This method is used primarily when creating a duplicate account only. In the case of only IDEN deposit exists in current account, the new account
	 * created will not have any cellular information. Since IDEN has gone and all accounts should be cellular only now, this may create system error.
	 * To prevent the error, we check for it and create a ceullar deposit to be saved with the same amount as IDEN deposit, so that KB will not throw an error
	 * 
	 */
	public void fixNoCellularDepositInfo() {
		if (deposits != null && deposits.length > 0) {
			List<CreditCheckResultDepositInfo> newDepositList = Arrays.asList((CreditCheckResultDepositInfo[]) deposits);;
			
			double idenDepositAmount = -1;
			for (CreditCheckResultDepositInfo depositsInfo : newDepositList) {
				if (depositsInfo.getProductType().equals(PRODUCT_TYPE_CELLULAR)) {
					return; //no need to fix if the cellular deposit is found
				}else if (depositsInfo.getProductType().equals("I")) {
					idenDepositAmount = depositsInfo.getDeposit();
				}
			}
			
			if (idenDepositAmount > -1) { //no cellular deposit found if code reaches here. and if there's an iden deposit amount, use it
				CreditCheckResultDepositInfo cellularDeposit = new CreditCheckResultDepositInfo();
				cellularDeposit.setDeposit(idenDepositAmount);
				cellularDeposit.setProductType(PRODUCT_TYPE_CELLULAR);
				newDepositList.add(cellularDeposit);
				deposits = newDepositList.toArray(new CreditCheckResultDepositInfo[newDepositList.size()]);
			}
		}
	}

	public void setDeposit(double amount) {
		setDeposit(amount, PRODUCT_TYPE_CELLULAR);
	}

	public void setDeposit(double amount, String productType) {
		this.depositAmount = amount;
		this.depositProductType = productType;
	}

	public String getDepositBarCode() {
		return depositBarCode;
	}

	public boolean isReferToCreditAnalyst() {
		return getReferToCreditAnalyst().isReferToCreditAnalyst();
	}

	public int getCreditScore() {
		return creditScore;
	}

	public int getUniqueCode() {
		try {
			return Integer.parseInt(message.substring(1, 2));
		} catch (RuntimeException e) {
			return 0;
		}
	}

	public void setCreditClass(String creditClass) {
		this.creditClass = creditClass;
	}

	public void updateCreditClass(int ban, String creditClass, String memoText) {
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}

	public void updateCreditProfile(int ban, String newCreditClass, double newCreditLimit, String memoText) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}

	public ActivationOption[] getActivationOptions() {
		return activationOptions;
	}

	public void setActivationOptions(ActivationOption[] activationOptions) {
		this.activationOptions = (ActivationOptionInfo[]) activationOptions;
	}

	public void setLimit(double limit) {
		this.limit = limit;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessageFrench(String messageFrench) {
		this.messageFrench = messageFrench;
	}

	public void setDepositBarCode(String depositBarCode) {
		this.depositBarCode = depositBarCode;
	}

	public void setReferToCreditAnalyst(boolean referToCreditAnalyst) {
		getReferToCreditAnalyst().setReferToCreditAnalyst(referToCreditAnalyst);
	}

	public ReferToCreditAnalystInfo getReferToCreditAnalyst() {
		return referToCreditAnalyst;
	}

	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}

	public boolean isCreditCheckPerformed() {
		return creditCheckPerformed;
	}

	public String getTransAlert1() {
		return transAlert1;
	}

	public void setTransAlert1(String transAlert1) {
		this.transAlert1 = transAlert1;
	}

	public String getTransAlert2() {
		return transAlert2;
	}

	public void setTransAlert2(String transAlert2) {
		this.transAlert2 = transAlert2;
	}

	public String getTransAlert3() {
		return transAlert3;
	}

	public void setTransAlert3(String transAlert3) {
		this.transAlert3 = transAlert3;
	}

	public String getTransAlert4() {
		return transAlert4;
	}

	public void setTransAlert4(String transAlert4) {
		this.transAlert4 = transAlert4;
	}

	public String getTuReturnInd() {
		return tuReturnInd;
	}

	public void setTuReturnInd(String tuReturnInd) {
		this.tuReturnInd = tuReturnInd;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getExistingNegInd() {
		return existingNegInd;
	}

	public void setExistingNegInd(String existingNegInd) {
		this.existingNegInd = existingNegInd;
	}

	public String getHawkAlertInd() {
		return hawkAlertInd;
	}

	public void setHawkAlertInd(String hawkAlertInd) {
		this.hawkAlertInd = hawkAlertInd;
	}

	public String getBureauFile() {
		return bureauFile;
	}

	public void setBureauFile(String bureauFile) {
		this.bureauFile = bureauFile;
	}

	public String getDefaultInd() {
		return defaultInd;
	}

	public void setDefaultInd(String defaultInd) {
		this.defaultInd = defaultInd;
	}

	public String getTransAlertInd() {
		return transAlertInd;
	}

	public void setTransAlertInd(String transAlertInd) {
		this.transAlertInd = transAlertInd;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getCreditCheckResultStatus() {
		return creditCheckResultStatus;
	}

	public void setCreditCheckResultStatus(String creditCheckResultStatus) {
		this.creditCheckResultStatus = creditCheckResultStatus;
	}

	public String getCreditBureauFile() {
		return bureauFile;
	}

	public ConsumerName getLastCreditCheckName() {
		return consumerName;
	}

	public void setLastCreditCheckName(ConsumerNameInfo consumerName) {
		this.consumerName = consumerName;
	}

	public Address getLastCreditCheckAddress() {
		return address;
	}

	public void setLastCreditCheckAddress(AddressInfo address) {
		this.address = address;
	}

	public BusinessCreditIdentity[] getLastCreditCheckListOfBusinesses() {
		return this.listOfBusinesses;
	}

	public void setLastCreditCheckListOfBusiness(BusinessCreditIdentityInfo[] lob) {
		listOfBusinesses = lob;
	}

	public BusinessCreditIdentity getLastCreditCheckSelectedBusiness() {
		return selectedBusiness;
	}

	public void setLastCreditCheckSelectedBusiness(BusinessCreditIdentityInfo selectedBusiness) {
		this.selectedBusiness = selectedBusiness;
	}

	public PersonalCredit getLastCreditCheckPersonalnformation() {
		return personalCredit;
	}

	public void setLastCreditCheckPersonalnformation(PersonalCreditInfo personalCreditInfo) {
		this.personalCredit = personalCreditInfo;
	}

	public void setLastCreditCheckIncorporationNumber(String incorpNumber) {
		this.incorporationNo = incorpNumber;
	}

	public String getLastCreditCheckIncorporationNumber() {
		return incorporationNo;
	}

	public void setLastCreditCheckIncorporationDate(Date incorpDate) {
		this.incorporationDate = incorpDate;
	}

	public Date getLastCreditCheckIncorporationDate() {
		return incorporationDate;
	}

	public void setCreditDate(Date creditDate) {
		this.creditDate = creditDate;
	}

	public Date getCreditDate() {
		return creditDate;
	}

	public void setCreditParamType(String paramType) {
		this.creditParamType = paramType;
	}

	public String getCreditParamType() {
		return creditParamType;
	}

	public void setDepositChangeReasonCode(String reasonCode) {
		depositChgReasonCode = reasonCode;
	}

	public String getDepositChangeReasonCode() {
		return depositChgReasonCode;
	}

	public CLPActivationOptionDetail getCLPActivationOptionDetail() {
		return clpActivationOptionDetail;
	}

	public void setCLPActivationOptionDetail(CLPActivationOptionDetail clpActivationOptionDetail) {
		this.clpActivationOptionDetail = clpActivationOptionDetail;
	}

	public String getBureauMessage() {
		return bureauMessage;
	}

	public void setBureauMessage(String bureauMessage) {
		this.bureauMessage = bureauMessage;
	}

	public void copyFrom(CreditCheckResultInfo info) {
		
		if (info != null) {
			creditClass = info.creditClass;
			limit = info.limit;
			message = info.message;
			messageFrench = info.messageFrench;
			deposits = info.deposits;
			referToCreditAnalyst = info.referToCreditAnalyst;
			creditScore = info.creditScore;
			transAlert1 = info.depositBarCode;
			transAlert2 = info.transAlert2;
			transAlert3 = info.transAlert3;
			transAlert4 = info.transAlert4;
			tuReturnInd = info.tuReturnInd;
			node = info.node;
			existingNegInd = info.existingNegInd;
			bureauFile = info.bureauFile;
			defaultInd = info.defaultInd;
			transAlertInd = info.transAlertInd;
			errorCode = info.errorCode;
			errorMessage = info.errorMessage;
			creditCheckPerformed = info.creditCheckPerformed;
			creditCheckResultStatus = info.creditCheckResultStatus;
			activationOptions = info.activationOptions;
			consumerName = info.consumerName;
			address = info.address;
			listOfBusinesses = info.listOfBusinesses;
			selectedBusiness = info.selectedBusiness;
			incorporationNo = info.incorporationNo;
			incorporationDate = info.incorporationDate;
			creditDate = info.creditDate;
			creditParamType = info.creditParamType;
			depositChgReasonCode = info.depositChgReasonCode;
			personalCredit = info.personalCredit;
			clpActivationOptionDetail = info.clpActivationOptionDetail;
			bureauMessage = info.bureauMessage;
		}
	}

	public void copyAmdocsInfo(CreditCheckResultInfo info) {
		
		listOfBusinesses = info.listOfBusinesses;
		deposits = info.deposits;
		address = info.address;
		consumerName = info.consumerName;
		bureauFile = info.bureauFile;
	}

	public void copyHCDServiceInfo(CreditCheckResultInfo info) {
		
		if (info != null) {
			message = info.message;
			messageFrench = info.messageFrench;
			referToCreditAnalyst = info.referToCreditAnalyst;
			errorCode = info.errorCode;
			creditCheckResultStatus = info.creditCheckResultStatus;
			activationOptions = info.activationOptions;
			clpActivationOptionDetail = info.clpActivationOptionDetail;
		}
	}
	
	public void copyCDACreditAssessmentInfo(CreditAssessmentInfo info) {

		if (info.getCreditWorthiness() != null) {
			this.creditScore = info.getCreditWorthiness().getPrimaryCreditScoreCode() != null ? Integer.parseInt(info.getCreditWorthiness().getPrimaryCreditScoreCode()) : 0;			
			if (info.getCreditWorthiness().getBureauDecisionCode() != null && info.getCreditWorthiness().getBureauDecisionCode().getDescriptionList() != null) {
				for (MultilingualTextInfo description : info.getCreditWorthiness().getBureauDecisionCode().getDescriptionList()) {
					this.bureauMessage = MultilingualTextInfo.LOCALE_EN.equals(description.getLocale()) ? description.getText() : this.bureauMessage;
				}
			}
			if (info.getCreditWorthiness().getCreditProgram() != null) {
				CreditProgramInfo creditProgramInfo = info.getCreditWorthiness().getCreditProgram();				
				this.creditClass = creditProgramInfo.getCreditClassCode();
				this.limit = CreditProgramInfo.TYPE_CLP.equals(creditProgramInfo.getCreditProgramType()) ? creditProgramInfo.getCLPCreditLimitAmount() : this.limit;				
				if (CreditProgramInfo.TYPE_DEPOSIT.equals(creditProgramInfo.getCreditProgramType()) || CreditProgramInfo.TYPE_DECLINED.equals(creditProgramInfo.getCreditProgramType())
						|| CreditProgramInfo.TYPE_NDP.equals(creditProgramInfo.getCreditProgramType())) {
					CreditCheckResultDepositInfo cdaDeposit = new CreditCheckResultDepositInfo();
					cdaDeposit.setProductType(PRODUCT_TYPE_CELLULAR);
					cdaDeposit.setDeposit(creditProgramInfo.getSecurityDepositAmount());
					this.deposits = new CreditCheckResultDepositInfo[] { cdaDeposit };
				}
			}
		}
		this.bureauFile = info.getPrintImageDocument() != null ? info.getPrintImageDocument().getDocument() : info.getBureauReportDocument() != null ? info.getBureauReportDocument().getDocument() : null;
		if (info.getAssessmentMessageList() != null) {
			for (MultilingualTextInfo assessmentMessage : info.getAssessmentMessageList()) {
				this.message = MultilingualTextInfo.LOCALE_EN.equals(assessmentMessage.getLocale()) ? assessmentMessage.getText() : this.message;
				this.messageFrench = MultilingualTextInfo.LOCALE_FR.equals(assessmentMessage.getLocale()) ? assessmentMessage.getText() : this.messageFrench;
			}
		}
		setReferToCreditAnalyst(info.referToCreditAnalyst());
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer();

		s.append("CreditCheckResultInfo: {\n");
		s.append("    creditDate=[").append(creditDate).append("]\n");
		s.append("    creditParamType=[").append(creditParamType).append("]\n");
		s.append("    creditClass=[").append(creditClass).append("]\n");
		s.append("    limit=[").append(limit).append("]\n");
		s.append("    message=[").append(message).append("]\n");
		s.append("    messageFrench=[").append(messageFrench).append("]\n");
		s.append("    depositBarCode=[").append(depositBarCode).append("]\n");
		s.append("    referToCreditAnalyst=[").append(referToCreditAnalyst).append("]\n");
		s.append("    creditCheckPerformed=[").append(creditCheckPerformed).append("]\n");
		s.append("    creditScore=[").append(creditScore).append("]\n");
		s.append("    creditCheckResultStatus=[").append(creditCheckResultStatus).append("]\n");
		s.append("    transAlert1=[").append(transAlert1).append("]\n");
		s.append("    transAlert2=[").append(transAlert2).append("]\n");
		s.append("    transAlert3=[").append(transAlert3).append("]\n");
		s.append("    transAlert4=[").append(transAlert4).append("]\n");
		s.append("    tuReturnInd=[").append(tuReturnInd).append("]\n");
		s.append("    node=[").append(node).append("]\n");
		s.append("    existingNegInd=[").append(existingNegInd).append("]\n");
		s.append("    hawkAlertInd=[").append(hawkAlertInd).append("]\n");
		s.append("    bureauFile=[").append(bureauFile).append("]\n");
		s.append("    defaultInd=[").append(defaultInd).append("]\n");
		s.append("    transAlertInd=[").append(transAlertInd).append("]\n");
		s.append("    errorCode=[").append(errorCode).append("]\n");
		s.append("    errorMessage=[").append(errorMessage).append("]\n");
		if (deposits == null) {
			s.append("    deposits=[null]\n");
		} else if (deposits.length == 0) {
			s.append("    deposits={}\n");
		} else {
			for (int i = 0; i < deposits.length; i++) {
				s.append("    deposits[" + i + "]=[").append(deposits[i]).append("]\n");
			}
		}
		if (consumerName == null) {
			s.append("    consumerName=[null]\n");
		} else {
			s.append("    consumerName=[" + consumerName.toString());
		}
		if (address == null) {
			s.append("    address=[null]\n");
		} else {
			s.append("    address=[" + address.toString() + "]");
		}
		s.append("    incorporationDate=[").append(incorporationDate).append("]\n");
		s.append("    incorporationNumber=[").append(incorporationNo).append("]\n");
		s.append("    depositChgReasonCode=[").append(depositChgReasonCode).append("]\n");
		s.append("    clpActivationOptionDetail=[").append(clpActivationOptionDetail).append("]\n");
		s.append("    bureauMessage=[").append(bureauMessage).append("]\n");
		s.append("}");

		return s.toString();
	}

}