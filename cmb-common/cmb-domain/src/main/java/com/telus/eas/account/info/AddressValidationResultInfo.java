package com.telus.eas.account.info;
/**
 * Title:        AddressValidationResultInfo<p>
 * Description:  The AddressValidationResultInfo holds the result of an address validation including the
 *               potentially corrected address and any error/warnings returned from Code1.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

import java.util.List;

import com.telus.eas.framework.info.*;

public class AddressValidationResultInfo extends Info {

	static final long serialVersionUID = 1L;

	private AddressInfo verifiedAddress = new AddressInfo();
	private AddressValidationMessagesInfo[] validationMessages = null;
	private int countValidationMessages = 0;
	private int countInformationalMessages = 0;
	private int countWarningMessages = 0;
	private int countErrorMessages = 0;
	private int countDBErrorMessages = 0;
	private int countDBWarningMessages = 0;
	private int countOtherMessages = 0;
	private List verificationResultStateList = null;
	private List matchingAddressList = null;
	private String nCodeReturnStatus = "";
	private boolean validAddressInd = false;

	public AddressValidationResultInfo(){}

	public AddressInfo getVerifiedAddress() {
		return verifiedAddress;
	}
	public void setVerifiedAddress(AddressInfo newVerifiedAddress) {
		verifiedAddress = newVerifiedAddress;
	}
	public AddressValidationMessagesInfo[] getValidationMessages() {
		return validationMessages;
	}
	public void setValidationMessages(AddressValidationMessagesInfo[] newValidationMessages) {
		validationMessages = newValidationMessages;
	}
	public int getCountValidationMessages() {
		return countValidationMessages;
	}
	public void setCountValidationMessages(int newCountValidationMessages) {
		countValidationMessages = newCountValidationMessages;
	}
	public int getCountInformationalMessages() {
		return countInformationalMessages;
	}
	public void setCountInformationalMessages(int newCountInformationalMessages) {
		countInformationalMessages = newCountInformationalMessages;
	}
	public int getCountWarningMessages() {
		return countWarningMessages;
	}
	public void setCountWarningMessages(int newCountWarningMessages) {
		countWarningMessages = newCountWarningMessages;
	}
	public int getCountErrorMessages() {
		return countErrorMessages;
	}
	public void setCountErrorMessages(int newCountErrorMessages) {
		countErrorMessages = newCountErrorMessages;
	}
	public int getCountDBErrorMessages() {
		return countDBErrorMessages;
	}
	public void setCountDBErrorMessages(int newCountDBErrorMessages) {
		countDBErrorMessages = newCountDBErrorMessages;
	}
	public int getCountDBWarningMessages() {
		return countDBWarningMessages;
	}
	public void setCountDBWarningMessages(int newCountDBWarningMessages) {
		countDBWarningMessages = newCountDBWarningMessages;
	}
	public int getCountOtherMessages() {
		return countOtherMessages;
	}
	public void setCountOtherMessages(int newCountOtherMessages) {
		countOtherMessages = newCountOtherMessages;
	}

	public String toString()
	{
		StringBuffer s = new StringBuffer(128);

		s.append("AddressValidationResultInfo:[\n");
		s.append("    countDBErrorMessages=[").append(countDBErrorMessages).append("]\n");
		s.append("    countDBWarningMessages=[").append(countDBWarningMessages).append("]\n");
		s.append("    countErrorMessages=[").append(countErrorMessages).append("]\n");
		s.append("    countInformationalMessages=[").append(countInformationalMessages).append("]\n");
		s.append("    countOtherMessages=[").append(countOtherMessages).append("]\n");
		s.append("    countValidationMessages=[").append(countValidationMessages).append("]\n");
		s.append("    countWarningMessages=[").append(countWarningMessages).append("]\n");
		if(validationMessages == null)
		{
			s.append("    validationMessages=[null]\n");
		}
		else if(validationMessages.length == 0)
		{
			s.append("    validationMessages={}\n");
		}
		else
		{
			for(int i=0; i<validationMessages.length; i++)
			{
				s.append("    validationMessages["+i+"]=[").append(validationMessages[i]).append("]\n");
			}
		}
		s.append("    verifiedAddress=[").append(verifiedAddress).append("]\n");
		s.append("]");

		return s.toString();
	}
	
	public List getVerificationResultStateList() {
		return verificationResultStateList;
	}

	public void setVerificationResultStateList(List verificationResultStateList) {
		this.verificationResultStateList = verificationResultStateList;
	}

	public List getMatchingAddressList() {
		return matchingAddressList;
	}

	public void setMatchingAddressList(List matchingAddressList) {
		this.matchingAddressList = matchingAddressList;
	}

	public String getnCodeReturnStatus() {
		return nCodeReturnStatus;
	}

	public void setnCodeReturnStatus(String nCodeReturnStatus) {
		this.nCodeReturnStatus = nCodeReturnStatus;
	}

	public boolean isValidAddressInd() {
		return validAddressInd;
	}

	public void setValidAddressInd(boolean validAddressInd) {
		this.validAddressInd = validAddressInd;
	}

	public class VerificationResultStateInfo extends Info{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String state;
		private String description;
		
		public VerificationResultStateInfo() {
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}

}
