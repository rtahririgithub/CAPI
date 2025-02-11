package com.telus.eas.subscriber.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.api.account.Subscriber;

public class PairingGroupInfo implements Serializable {
	static final long serialVersionUID = 1L;
	
	private char pairingMode;
	private String primaryPhoneNumber;
	private LightWeightSubscriberInfo lwPrimarySubscriberInfo;
	
	private List<String> companionPhoneNumberList = new ArrayList<String>();
	private Map<String, LightWeightSubscriberInfo> lwCompanionSubMap = new HashMap<String, LightWeightSubscriberInfo>();
	
	public String getPrimaryPhoneNumber() {
		return primaryPhoneNumber;
	}
	
	public void setPrimaryPhoneNumber(String primaryPhoneNumber) {
		this.primaryPhoneNumber = primaryPhoneNumber;
	}

	public List<String> getCompanionPhoneNumberList() {
		return companionPhoneNumberList;
	}
	
	public void setCompanionPhoneNumberList(List<String> companionPhoneNumberList) {
		if (companionPhoneNumberList != null) {
			this.companionPhoneNumberList = companionPhoneNumberList;
		}
	}


	/**
	 * 
	 * @param phoneNumber One of the phone numbers in companion list or the primary phone #
	 * @return LightWeightSubscriberInfo - contains returns some basic info of the subscriber such as brand, status and network type
	 */
	public LightWeightSubscriberInfo getLwCompanionSubInfo(String phoneNumber) {
		return this.lwCompanionSubMap.get(phoneNumber);
	}

	public LightWeightSubscriberInfo getLwPrimarySubscriberInfo() {
		return lwPrimarySubscriberInfo;
	}

	public void setLwPrimarySubscriberInfo(LightWeightSubscriberInfo lwPrimarySubscriberInfo) {
		this.lwPrimarySubscriberInfo = lwPrimarySubscriberInfo;
	}

	public char getPairingMode() {
		return pairingMode;
	}

	public void setPairingMode(char pairingMode) {
		this.pairingMode = pairingMode;
	}
	
	public void setPairingModeByCategory(String categoryCode) {
		if (CommunicationSuiteInfo.COMPANION_FEATURE_CATEGORY_CODE.equals(categoryCode) || CommunicationSuiteInfo.PRIMARY_FEATURE_CATEGORY_CODE.equals(categoryCode)) {
			setPairingMode(CommunicationSuiteInfo.PAIRING_MODE_ONE_NUMBER);
		}else if (CommunicationSuiteInfo.COMPANION_STANDALONE_FEATURE_CATEGORY_CODE.equals(categoryCode) || CommunicationSuiteInfo.PRIMARY_STANDALONE_FEATURE_CATEGORY_CODE.equals(categoryCode)) {
			setPairingMode(CommunicationSuiteInfo.PAIRING_MODE_STANDALONE);
		}
	}
	
	public int getActiveCompanionCount() {
		return getCompanionSubCountByStatus(Subscriber.STATUS_ACTIVE);
	}

	public int getSuspendedCompanionCount() {
		return getCompanionSubCountByStatus(Subscriber.STATUS_SUSPENDED);
	}

	public int getCancelledCompanionCount() {
		return getCompanionSubCountByStatus(Subscriber.STATUS_CANCELED);
	}
	
	public int getActiveAndSuspendedCompanionCount() {
		return getActiveAndSuspendedCompanionSubscribers().size();
	}
	
	private int getCompanionSubCountByStatus(char status) {
		return getCompanionSubCountByStatusList(new char[] {status});
	}
	
	private int getCompanionSubCountByStatusList(char[] statusList) {
		int count = 0;
		if (lwCompanionSubMap != null) {
			for (LightWeightSubscriberInfo sub : lwCompanionSubMap.values()) {
				for (char status : statusList) {
					if (sub.getStatus() == status) {
						count++;
					}
				}

			}
		}

		return count;
	}
	
	public Map<String, LightWeightSubscriberInfo> getLwCompanionSubMap() {
		return lwCompanionSubMap;
	}

	public List<LightWeightSubscriberInfo> getActiveAndSuspendedCompanionSubscribers() {
		List<LightWeightSubscriberInfo> LwSubList = new ArrayList<LightWeightSubscriberInfo>();
		if (lwCompanionSubMap != null) {
			for (LightWeightSubscriberInfo sub : lwCompanionSubMap.values()) {
				if ((sub.getStatus() == Subscriber.STATUS_ACTIVE) || (sub.getStatus() == Subscriber.STATUS_SUSPENDED)) {
					LwSubList.add(sub);
				}
			}
		}
		return LwSubList;
	}
	
	
	public List<String> getActiveAndSuspendedCompanionPhoneNumberList() {
		List<String> activeAndSuspendedPhoneNumberList = new ArrayList<String> ();
		List<LightWeightSubscriberInfo> activeAndSuspendedList = getActiveAndSuspendedCompanionSubscribers();
		for (LightWeightSubscriberInfo sub : activeAndSuspendedList) {
			activeAndSuspendedPhoneNumberList.add(sub.getSubscriberId());
		}
		
		return activeAndSuspendedPhoneNumberList;
	}
	
	public List<String> getSuitePhoneNumberList() {
		List<String> suitePhoneNumberList = new ArrayList<String>();
		suitePhoneNumberList.addAll(companionPhoneNumberList);
		suitePhoneNumberList.add(primaryPhoneNumber);
		return suitePhoneNumberList;
	}

	@Override
	public String toString() {
		return "PairingGroupInfo [pairingMode=" + pairingMode + ", primaryPhoneNumber=" + primaryPhoneNumber + ", lwPrimarySubscriberInfo=" + lwPrimarySubscriberInfo + ", companionPhoneNumberList="
				+ companionPhoneNumberList + ", lwCompanionSubMap=" + lwCompanionSubMap + "]";
	}
	
	
}
