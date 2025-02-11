package com.telus.eas.subscriber.info;

import java.util.ArrayList;
import java.util.List;

import com.telus.api.portability.PortInEligibility;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.framework.info.Info;

public class PhoneNumberChangeInfo extends Info {
	protected AvailablePhoneNumberInfo newNumberGroup;
	protected AvailablePhoneNumberInfo oldNumberGroup;
	protected String reasonCode;
	protected String salesRepCode;
	protected String dealerCode;
	protected String subscriberId;
	protected String newSubscriberId;
	protected String newPhoneNumber;
	protected SubscriberInfo outgoingSubscriberInfo;
	protected boolean portIn;
	protected boolean changeOtherPhoneNumbers;
	protected String portProcess = PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT;
	protected List applicationWarningList;
	protected List systemWarningList;
	/**
	 * @return the newNumberGroup
	 */
	public AvailablePhoneNumberInfo getNewNumberGroup() {
		return newNumberGroup;
	}
	/**
	 * @param newNumberGroup the newNumberGroup to set
	 */
	public void setNewNumberGroup(AvailablePhoneNumberInfo newNumberGroup) {
		this.newNumberGroup = newNumberGroup;
	}
	/**
	 * @return the oldNumberGroup
	 */
	public AvailablePhoneNumberInfo getOldNumberGroup() {
		return oldNumberGroup;
	}
	/**
	 * @param oldNumberGroup the oldNumberGroup to set
	 */
	public void setOldNumberGroup(AvailablePhoneNumberInfo oldNumberGroup) {
		this.oldNumberGroup = oldNumberGroup;
	}
	/**
	 * @return the reasonCode
	 */
	public String getReasonCode() {
		return reasonCode;
	}
	/**
	 * @param reasonCode the reasonCode to set
	 */
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	/**
	 * @return the salesRepCode
	 */
	public String getSalesRepCode() {
		return salesRepCode;
	}
	/**
	 * @param salesRepCode the salesRepCode to set
	 */
	public void setSalesRepCode(String salesRepCode) {
		this.salesRepCode = salesRepCode;
	}
	/**
	 * @return the dealerCode
	 */
	public String getDealerCode() {
		return dealerCode;
	}
	/**
	 * @param dealerCode the dealerCode to set
	 */
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	/**
	 * @return the subscribrId
	 */
	public String getSubscriberId() {
		return subscriberId;
	}
	/**
	 * @param subscribrId the subscribrId to set
	 */
	public void setSubscriberId(String subscribrId) {
		this.subscriberId = subscribrId;
	}
	/**
	 * @return the newPhoneNumber
	 */
	public String getNewPhoneNumber() {
		return newPhoneNumber;
	}
	/**
	 * @param newPhoneNumber the newPhoneNumber to set
	 */
	public void setNewPhoneNumber(String newPhoneNumber) {
		this.newPhoneNumber = newPhoneNumber;
	}
	/**
	 * @return the outgoingSubscriberInfo
	 */
	public SubscriberInfo getOutgoingSubscriberInfo() {
		return outgoingSubscriberInfo;
	}
	/**
	 * @param outgoingSubscriberInfo the outgoingSubscriberInfo to set
	 */
	public void setOutgoingSubscriberInfo(SubscriberInfo outgoingSubscriberInfo) {
		this.outgoingSubscriberInfo = outgoingSubscriberInfo;
	}
	/**
	 * @return the portIn
	 */
	public boolean isPortIn() {
		return portIn;
	}
	/**
	 * @param portIn the portIn to set
	 */
	public void setPortIn(boolean portIn) {
		this.portIn = portIn;
	}
	/**
	 * @return the applicationWarningList
	 */
	public List getApplicationWarningList() {
		if (applicationWarningList == null) {
			applicationWarningList = new ArrayList();
		}
		return applicationWarningList;
	}
	/**
	 * @param applicationWarningList the applicationWarningList to set
	 */
	public void setApplicationWarningList(List applicationWarningList) {
		this.applicationWarningList = applicationWarningList;
	}
	/**
	 * @return the systemWarningList
	 */
	public List getSystemWarningList() {
		if (systemWarningList == null) {
			systemWarningList = new ArrayList();
		}
		return systemWarningList;
	}
	/**
	 * @param systemWarningList the systemWarningList to set
	 */
	public void setSystemWarningList(List systemWarningList) {
		this.systemWarningList = systemWarningList;
	}
	/**
	 * @return the portProcess
	 */
	public String getPortProcess() {
		return portProcess;
	}
	/**
	 * @param portProcess the portProcess to set
	 */
	public void setPortProcess(String portProcess) {
		this.portProcess = portProcess;
	}
	/**
	 * @return the changeOtherPhoneNumbers
	 */
	public boolean isChangeOtherPhoneNumbers() {
		return changeOtherPhoneNumbers;
	}
	/**
	 * @param changeOtherPhoneNumbers the changeOtherPhoneNumbers to set
	 */
	public void setChangeOtherPhoneNumbers(boolean changeOtherPhoneNumbers) {
		this.changeOtherPhoneNumbers = changeOtherPhoneNumbers;
	}
	/**
	 * @return the newSubscriberId
	 */
	public String getNewSubscriberId() {
		return newSubscriberId;
	}
	/**
	 * @param newSubscriberId the newSubscriberId to set
	 */
	public void setNewSubscriberId(String newSubscriberId) {
		this.newSubscriberId = newSubscriberId;
	}

	
	
}
