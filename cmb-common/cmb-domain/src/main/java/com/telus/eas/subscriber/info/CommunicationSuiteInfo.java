package com.telus.eas.subscriber.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.telus.api.account.Subscriber;
import com.telus.api.subscriber.CommunicationSuite;
import com.telus.eas.framework.info.Info;

public class CommunicationSuiteInfo extends Info implements CommunicationSuite {
	
	static final long serialVersionUID = 1L;

	public static final String MEMO_COMPANION_CANCELLATION_FAILURE = "CDCF";
//	public static final String PRIMARY_SWITCH_CODE= "SNPRIM";
//	public static final String COMPANION_SWITCH_CODE = "SNCOMP";
	
	public static final String PRIMARY_FEATURE_CATEGORY_CODE= "ENP"; //Enablement-Primary
	public static final String COMPANION_FEATURE_CATEGORY_CODE = "ENC"; //Enablement-Companion
	public static final String PRIMARY_STANDALONE_FEATURE_CATEGORY_CODE= "STP"; //Enablement-Primary-Tremblant
	public static final String COMPANION_STANDALONE_FEATURE_CATEGORY_CODE = "STC"; //Enablement-Companion-Tremblant

	public static final List<String> PRIMARY_FEATURE_CATEGORY_LIST = Arrays.asList(PRIMARY_FEATURE_CATEGORY_CODE, PRIMARY_STANDALONE_FEATURE_CATEGORY_CODE);
	public static final List<String> COMPANION_FEATURE_CATEGORY_LIST = Arrays.asList(COMPANION_FEATURE_CATEGORY_CODE, COMPANION_STANDALONE_FEATURE_CATEGORY_CODE);
	
	public static final int CHECK_LEVEL_ALL = 0;
	public static final int CHECK_LEVEL_PRIMARY_ONLY = 1;
	public static final int CHECK_LEVEL_COMPANION_ONLY = 2;

	public static final char PAIRING_MODE_ONE_NUMBER = '1';
	public static final char PAIRING_MODE_STANDALONE = 'S';

	private int ban;
	private String primaryPhoneNumber;
	private LightWeightSubscriberInfo lwPrimarySubscriberInfo;
	private boolean retrievedUsingPrimaryPhoneNumber = false; 
	private List<PairingGroupInfo> pairingGroupList = new ArrayList<PairingGroupInfo> ();
	
	@Override
	public int getBan() {
		return ban;
	}
	
	public void setBan(int billingAccountNumber) {
		this.ban = billingAccountNumber;
	}

	@Override
	public String getPrimaryPhoneNumber() {
		return primaryPhoneNumber;
	}
	
	public void setPrimaryPhoneNumber(String primaryPhoneNumber) {
		this.primaryPhoneNumber = primaryPhoneNumber;
	}

	@Override
	public List<String> getCompanionPhoneNumberList() {
		List<String> companionPhoneList = new ArrayList<String>();
		for (PairingGroupInfo group : this.pairingGroupList) {
			companionPhoneList.addAll(group.getCompanionPhoneNumberList());
		}
		
		return companionPhoneList;
	}
	
	public LightWeightSubscriberInfo getLwPrimarySubscriberInfo() {
		return lwPrimarySubscriberInfo;
	}

	public void setLwPrimarySubscriberInfo(LightWeightSubscriberInfo lwPrimarySubscriberInfo) {
		this.lwPrimarySubscriberInfo = lwPrimarySubscriberInfo;
	}
	
	/**
	 * 
	 * @return True if this communication suite retrieved based on the primary's feature data. Otherwise, false. Equivalent to checking if subscriberNum.equals(getPrimaryPhoneNumber)
	 */
	public boolean isRetrievedAsPrimary() {
		return retrievedUsingPrimaryPhoneNumber;
	}
	
	public void setRetrievedAsPrimary(boolean retrievedUsingPrimaryPhoneNumber) {
		this.retrievedUsingPrimaryPhoneNumber = retrievedUsingPrimaryPhoneNumber;
	}

	/**
	 * 
	 * @param phoneNumber One of the phone numbers in companion list or the primary phone #
	 * @return LightWeightSubscriberInfo - contains returns some basic info of the subscriber such as brand, status and network type
	 */
	public LightWeightSubscriberInfo getLwCompanionSubInfo(String phoneNumber) {
		for (PairingGroupInfo group : this.pairingGroupList) {
			LightWeightSubscriberInfo sub = group.getLwCompanionSubInfo(phoneNumber);
			if (sub != null) {
				return sub;
			}
		}
		
		return null;
	}

	
	public boolean isSuspendedPrimary() {
		return getLwPrimarySubscriberInfo().getStatus() == Subscriber.STATUS_SUSPENDED;
	}

	@Override
	public int getActiveCompanionCount() {
		int total = 0;
		
		for (PairingGroupInfo group : this.pairingGroupList) {
			total += group.getActiveCompanionCount();
		}
		return total;
	}

	@Override
	public int getSuspendedCompanionCount() {
		int total = 0;
		
		for (PairingGroupInfo group : this.pairingGroupList) {
			total += group.getSuspendedCompanionCount();
		}
		return total;
	}

	@Override
	public int getCancelledCompanionCount() {
		int total = 0;
		
		for (PairingGroupInfo group : this.pairingGroupList) {
			total += group.getCancelledCompanionCount();
		}
		return total;
	}
	
	@Override
	public int getActiveAndSuspendedCompanionCount() {
		return getActiveAndSuspendedCompanionSubscribers().size();
	}
	
	
	public List<LightWeightSubscriberInfo> getActiveAndSuspendedCompanionSubscribers() {
		List<LightWeightSubscriberInfo> LwSubList = new ArrayList<LightWeightSubscriberInfo>();
		
		for (PairingGroupInfo group : this.pairingGroupList) {
			LwSubList.addAll(group.getActiveAndSuspendedCompanionSubscribers());
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

	
	public void addPairingGroup(PairingGroupInfo pairingGroup) {
		if (pairingGroup != null) {
			this.pairingGroupList.add(pairingGroup);
		}
	}
	
	
	public List<PairingGroupInfo> getPairingGroupList() {
		return pairingGroupList;
	}

	public void setPairingGroupList(List<PairingGroupInfo> pairingGroupList) {
		this.pairingGroupList = pairingGroupList;
	}

	@Override
	public String toString() {
		return "CommunicationSuiteInfo [ban=" + ban + ", primaryPhoneNumber=" + primaryPhoneNumber + ", lwPrimarySubscriberInfo=" + lwPrimarySubscriberInfo + ", retrievedUsingPrimaryPhoneNumber="
				+ retrievedUsingPrimaryPhoneNumber + ", pairingGroupList=" + pairingGroupList + "]";
	}

	
}
