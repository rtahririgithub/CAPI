package com.telus.cmb.subscriber.utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.Account;
import com.telus.api.account.SubscriberDataSharingDetail.DataSharingDetail;
import com.telus.api.account.SubscriberDataSharingDetail.DataSharingSoc;
import com.telus.api.account.SubscriberDataSharingDetail.NonDataSharingRegularSoc;
import com.telus.api.reference.ServiceDataSharingGroup;
import com.telus.api.reference.ServiceSummary;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo;
import com.telus.eas.subscriber.info.DataSharingSocTransferInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionMSCSpendingInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class DataSharingUtil {
	
	private static final char DATA_SHARING_CONTRIBUTOR = 'C';
	private static final char DATA_SHARING_ACCESSOR = 'A';
	private static final char NOT_IN_DATA_SHARING = 'N';
	
	public static boolean isValidAccountForDataSharingValidation (SubscriberInfo subscriber, AccountInfo account) {	
		return DataSharingUtil.isActiveOrSuspendedSubscriber(subscriber) && DataSharingUtil.isOpenOrSuspendedAccount(account);
	}
	
	public static boolean isOpenOrSuspendedAccount(AccountInfo account) {
		return account.getStatus() == Account.STATUS_OPEN || account.getStatus() == Account.STATUS_SUSPENDED;
	}
	
	public static boolean isActiveOrSuspendedSubscriber(SubscriberInfo subscriber) {		
		return subscriber.getStatus() == SubscriberInfo.STATUS_ACTIVE || subscriber.getStatus() == SubscriberInfo.STATUS_SUSPENDED;
	}
	
	public static boolean isSupportedDataSharingBrand(SubscriberInfo subscriber) {
		return AppConfiguration.getSupportedDataSharingBrands().contains(subscriber.getBrandId());
	}
		
	public static List<String> getContributingServiceDataSharingGroups(ServiceInfo[] services) {
		List<String> dataSharingGroups = new ArrayList<String>();
		for (ServiceInfo service : services) {
			addContributingDataSharingGroupsToList(dataSharingGroups, service.getDataSharingGroups());
		}	
		return dataSharingGroups;
	}
	
	public static List<String> getContributingPricePlanDataSharingGroups(PricePlanInfo pricePlan) {
		List<String> dataSharingGroups = new ArrayList<String>();
		if (pricePlan != null) {
			addContributingDataSharingGroupsToList(dataSharingGroups, pricePlan.getDataSharingGroups());
		}	
		return dataSharingGroups;
	}
		
	private static void addContributingDataSharingGroupsToList(List<String> dataSharingGroups, ServiceDataSharingGroup[] serviceDataSharingGroupList) {
		for (ServiceDataSharingGroup serviceDataSharingGroup : serviceDataSharingGroupList) {
			if (serviceDataSharingGroup.isContributing()) {
				dataSharingGroups.add(serviceDataSharingGroup.getDataSharingGroupCode());
			}
		}
	}
	
	public static int getContributorCount(SubscriberDataSharingDetailInfo[] subscriberDataSharingDetails, String dataSharingGroup) {
		int contributorCount = 0;
		for (SubscriberDataSharingDetailInfo subscriberDataSharingDetail : subscriberDataSharingDetails) {			
			if (isContributor(subscriberDataSharingDetail, dataSharingGroup)) {
				contributorCount++;
			} 
		}
		return contributorCount;
	}
	
	public static boolean isContributor(SubscriberDataSharingDetailInfo subscriberDataSharingDetail, String dataSharingGroup) {
		return getDataSharingContributingType(subscriberDataSharingDetail, dataSharingGroup) == DATA_SHARING_CONTRIBUTOR;
	}
	
	public static boolean isAccessor(SubscriberDataSharingDetailInfo subscriberDataSharingDetail, String dataSharingGroup) {		
		return getDataSharingContributingType(subscriberDataSharingDetail, dataSharingGroup) == DATA_SHARING_ACCESSOR;
	}

	private static char getDataSharingContributingType(SubscriberDataSharingDetailInfo subscriberDataSharingDetail, String dataSharingGroup) {
		List<DataSharingDetail> dataSharingDetails = new ArrayList<DataSharingDetail> (subscriberDataSharingDetail.getDataSharingInfoList());
		char contributingType = NOT_IN_DATA_SHARING;
		if (dataSharingGroup.equals(subscriberDataSharingDetail.getPricePlanDSGroupCode()) && subscriberDataSharingDetail.isPricePlanContributingInd()) {
			return DATA_SHARING_CONTRIBUTOR;
		}
		for (DataSharingDetail dataSharingDetail : dataSharingDetails) {
			if (dataSharingDetail.getDataSharingGroupCode().equals(dataSharingGroup)) {
				contributingType = DATA_SHARING_ACCESSOR;
				if (hasContributingDataSharingSoc(dataSharingDetail)) {
					return DATA_SHARING_CONTRIBUTOR;
				}
			}
		}
		return contributingType;
	}
	
	private static boolean hasContributingDataSharingSoc(DataSharingDetail dataSharingDetail) {
		List<DataSharingSoc> dataSharingSocs = new ArrayList<DataSharingSoc> (dataSharingDetail.getDataSharingSocList());
		for (DataSharingSoc dataSharingSoc : dataSharingSocs) {
			if (dataSharingSoc.getContributingInd()) {
				return true;
			}
		}
		return false;
	}
	
	public static List<SubscriptionMSCSpendingInfo> getSubscriptionMSCSpending(SubscriberDataSharingDetailInfo[] subscriberDataSharingDetails,
			String skippedSubscriberId) {
		List<SubscriptionMSCSpendingInfo> subscriptionMSCSpendingInfoList = new ArrayList<SubscriptionMSCSpendingInfo>();
		Map<String, Double> dataSharingPoolSpending = getDataSharingPoolSpending(subscriberDataSharingDetails, skippedSubscriberId);
		for (SubscriberDataSharingDetailInfo subscriberDataSharingDetail : subscriberDataSharingDetails) {
			if (subscriberDataSharingDetail.getSubscriberId().equals(skippedSubscriberId) == false) {
				SubscriptionMSCSpendingInfo subscriptionMSCSpendingInfo = new SubscriptionMSCSpendingInfo();
				subscriptionMSCSpendingInfo.setSubscriptionId(Long.toString(subscriberDataSharingDetail.getSubscriptionId()));			
				subscriptionMSCSpendingInfo.setPlanSpentAmount(subscriberDataSharingDetail.getPricePlanRecurringCharge());						
				subscriptionMSCSpendingInfo.setServiceSpentAmount(getServiceSpending(subscriberDataSharingDetail, dataSharingPoolSpending));
				subscriptionMSCSpendingInfoList.add(subscriptionMSCSpendingInfo);	
			}
		}
		return subscriptionMSCSpendingInfoList;
	}
	
	private static Map<String, Double> getDataSharingPoolSpending(SubscriberDataSharingDetailInfo[] subscriberDataSharingDetails, String skippedSubscriberId) {
		Map<String, Double> dataSharingPoolMap = new HashMap<String, Double>();		
		for (SubscriberDataSharingDetailInfo subscriberDataSharingDetail : subscriberDataSharingDetails) {
			if (subscriberDataSharingDetail.getSubscriberId().equals(skippedSubscriberId) == false) {
				for (DataSharingDetail dataSharingDetail : subscriberDataSharingDetail.getDataSharingInfoList()) {
					for (DataSharingSoc dataSharingSoc : dataSharingDetail.getDataSharingSocList()) {
						if (isMSCService(dataSharingSoc.getFamilyTypes()) && dataSharingSoc.getContributingInd()) {
							Double dataSharingPoolSpending = dataSharingPoolMap.get(dataSharingDetail.getDataSharingGroupCode());
							if (dataSharingPoolSpending == null) {
								dataSharingPoolSpending = 0.00;
							}
							dataSharingPoolSpending += dataSharingSoc.getDataSharingSpentAmt();
							dataSharingPoolMap.put(dataSharingDetail.getDataSharingGroupCode(), dataSharingPoolSpending);
						}
					}
				}
			}
		}
		return dataSharingPoolMap;
	}
	
	private static double getServiceSpending(SubscriberDataSharingDetailInfo subscriberDataSharingDetail, Map<String, Double> dataSharingPoolSpending) {
		double serviceSpending = 0.00;
		for (NonDataSharingRegularSoc regularSoc : subscriberDataSharingDetail.getNonDataSharingRegularSocList()) {				
			if (isMSCService(regularSoc.getFamilyTypes())) {
				serviceSpending += regularSoc.getSocRecurringCharge();
			}
		}		
		Set<String> dataSharingPools = new HashSet<String>();
		for (DataSharingDetail dataSharingDetail : subscriberDataSharingDetail.getDataSharingInfoList()) {
			dataSharingPools.add(dataSharingDetail.getDataSharingGroupCode());
			for (DataSharingSoc dataSharingSoc : dataSharingDetail.getDataSharingSocList()) {
				if (isMSCService(dataSharingSoc.getFamilyTypes()) && dataSharingSoc.getContributingInd() == false) {
					serviceSpending += dataSharingSoc.getDataSharingSpentAmt();					
				}
			}
		}
		for (String dataSharingPool : dataSharingPools) {
			Double poolSpending = dataSharingPoolSpending.get(dataSharingPool);
			serviceSpending += (poolSpending != null) ? poolSpending : 0.00;
		}
		
		return serviceSpending;
	}
	
	private static boolean isMSCService(Collection<String> familyTypes) {
		return familyTypes == null || familyTypes.contains(ServiceSummary.FAMILY_TYPE_CODE_NON_MSC_SERVICE) == false;		
	}
	
	public static DataSharingSocTransferInfo createDataSharingSocTransferInfo(SubscriberDataSharingDetailInfo[] subscriberDataSharingDetails, String dataSharingGroup, String contributorSubscriberId, 
			Map<String, SubscriberInfo> subscriberMap) throws ApplicationException {		
		SubscriberDataSharingDetailInfo contributor = findContributor(subscriberDataSharingDetails, contributorSubscriberId);
		if (contributor.getPricePlanDSGroupCode() != null && contributor.getPricePlanDSGroupCode().equals("") == false) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRIBUTING_SOC_IS_PRICE_PLAN, 
					"Leaving subscriber has a contributing data sharing price plan", "");
		}
		//TODO: Wrap the follow code into a new method findCandidateAccessor
		//TODO: Clarify "why" we are doing this logic (commenting or refactoring)
		List<SubscriberDataSharingDetailInfo> accessors = findDataSharingAccessors(subscriberDataSharingDetails, dataSharingGroup, subscriberMap);
		SubscriberDataSharingDetailInfo longestRemainingContract = findLongestRemainingContract(accessors);
		if (longestRemainingContract != null) {
			return populateDataSharingSocTransferInfo(longestRemainingContract, contributor, dataSharingGroup);
		} else {			
			SubscriberDataSharingDetailInfo mostRecentServicedAccessor = findSubscriberWithMostRecentStartServiceDate(accessors, subscriberMap);
			if (mostRecentServicedAccessor != null) {
				return populateDataSharingSocTransferInfo(mostRecentServicedAccessor, contributor, dataSharingGroup); 
			} else {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.NO_AVAILABLE_ACCESSORS, "No available accessors for data sharing soc transfer", "");
			}			
		}
	}
	
	private static SubscriberDataSharingDetailInfo findContributor(SubscriberDataSharingDetailInfo[] subscriberDataSharingDetails,
			String contributorSubscriberId) {	
		SubscriberDataSharingDetailInfo contributor = null;
		for (SubscriberDataSharingDetailInfo subscriberDataSharingDetail : subscriberDataSharingDetails) {
			if (subscriberDataSharingDetail.getSubscriberId().equals(contributorSubscriberId)) {
				contributor = subscriberDataSharingDetail;
				break;
			}
		}
		return contributor;
	}
	
	private static List<SubscriberDataSharingDetailInfo> findDataSharingAccessors(SubscriberDataSharingDetailInfo[] subscriberDataSharingDetails, String dataSharingGroup, 
			Map<String, SubscriberInfo> subscriberMap) {
		List<SubscriberDataSharingDetailInfo> accessors = new ArrayList<SubscriberDataSharingDetailInfo>();
		for (SubscriberDataSharingDetailInfo subscriberDataSharingDetail : subscriberDataSharingDetails) {
			SubscriberInfo subscriber = subscriberMap.get(subscriberDataSharingDetail.getSubscriberId());			
			if (isAccessor(subscriberDataSharingDetail, dataSharingGroup) && subscriberDataSharingDetail.getPricePlanDSGroupCode() == null &&
					subscriber != null && subscriber.getStatus() == SubscriberInfo.STATUS_ACTIVE) {
				accessors.add(subscriberDataSharingDetail);
			}				
		}
		return accessors;
	}
	
	private static boolean hasActiveContract(SubscriberDataSharingDetailInfo subscriberDataSharingDetail) {
		Calendar currentCalendar = Calendar.getInstance();
		Date todaysDate = currentCalendar.getTime();
		return subscriberDataSharingDetail.getContractEndDate() != null && todaysDate.before(subscriberDataSharingDetail.getContractEndDate());
	}
	
	private static SubscriberDataSharingDetailInfo findLongestRemainingContract(List<SubscriberDataSharingDetailInfo> accessors) {
		SubscriberDataSharingDetailInfo longestContractSub = null;
		Date latestContractEndDate = null;
		for (SubscriberDataSharingDetailInfo accessor : accessors) {
			if (hasActiveContract(accessor)) {
				if (longestContractSub == null) {
					longestContractSub = accessor;
					latestContractEndDate = accessor.getContractEndDate();
				} else {
					if (accessor.getContractEndDate().after(latestContractEndDate)) {
						longestContractSub = accessor;
						latestContractEndDate = accessor.getContractEndDate();
					}
				}
			}
		}
		return longestContractSub;
	}
	
	private static SubscriberDataSharingDetailInfo findSubscriberWithMostRecentStartServiceDate(List<SubscriberDataSharingDetailInfo> accessors, Map<String, SubscriberInfo> subscriberMap) {
		SubscriberDataSharingDetailInfo mrssdSubscriber = null;
		Date mostRecentStartServiceDate = null;
		for (SubscriberDataSharingDetailInfo accessor : accessors) {
			SubscriberInfo accessorInfo = subscriberMap.get(accessor.getSubscriberId());
			if (accessorInfo != null) {
				if (mrssdSubscriber == null) {
					mrssdSubscriber = accessor;
					mostRecentStartServiceDate = accessorInfo.getStartServiceDate();
				} else {
					if (accessorInfo.getStartServiceDate().after(mostRecentStartServiceDate)) {
						mrssdSubscriber = accessor;
						mostRecentStartServiceDate = accessorInfo.getStartServiceDate();
					}
				}
			}
		}
		return mrssdSubscriber;
	}
	
	private static DataSharingSocTransferInfo populateDataSharingSocTransferInfo(SubscriberDataSharingDetailInfo longestRemainingContract,
			SubscriberDataSharingDetailInfo contributor, String dataSharingGroup) {
		DataSharingSocTransferInfo dsSocTransferInfo = new DataSharingSocTransferInfo();
		dsSocTransferInfo.setCandidateSubscriberId(longestRemainingContract.getSubscriberId());						
		DataSharingSoc accessorSoc = getDataSharingSoc(longestRemainingContract.getDataSharingInfoList(), dataSharingGroup, false);
		dsSocTransferInfo.setCandidateSocCode(accessorSoc.getDataSharingSocCode());
		dsSocTransferInfo.setCandidateSocDescription(accessorSoc.getDataSharingSocDescription());
		dsSocTransferInfo.setCandidateSocDescriptionFrench(accessorSoc.getDataSharingSocDescriptionFrench());
		DataSharingSoc contributorSoc = getDataSharingSoc(contributor.getDataSharingInfoList(), dataSharingGroup, true);
		dsSocTransferInfo.setContributorSocCode(contributorSoc.getDataSharingSocCode());
		dsSocTransferInfo.setContributorSocDescription(contributorSoc.getDataSharingSocDescription());
		dsSocTransferInfo.setContributorSocDescriptionFrench(contributorSoc.getDataSharingSocDescriptionFrench());
		return dsSocTransferInfo;
	}
	
	private static DataSharingSoc getDataSharingSoc(Collection<DataSharingDetail> dataSharingDetailList, String dataSharingGroup, boolean contributing) {
		DataSharingSoc dsSoc = null;
		for (DataSharingDetail dataSharingDetail : dataSharingDetailList) {
			if (dataSharingDetail.getDataSharingGroupCode().equals(dataSharingGroup)) {
				for (DataSharingSoc dataSharingSoc : dataSharingDetail.getDataSharingSocList()) {
					if (dataSharingSoc.getContributingInd() == contributing) {
						dsSoc = dataSharingSoc;
					}
					if (dsSoc != null) {
						break;
					}
				}
				if (dsSoc != null) {
					break;
				}
			}			
		}
		return dsSoc;
	}
	
}
