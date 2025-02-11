package com.telus.cmb.account.informationhelper.svc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.telus.api.ApplicationException;
import com.telus.api.account.SubscriberDataSharingDetail.DataSharingDetail;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo.DataSharingDetailInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo.DataSharingResultInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo.DataSharingSocInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo.NonDataSharingRegularSocInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public class SubscriberDataSharingInfoMapper {

	private static final String SERVICE_TYPE_PRICE_PLAN = "P";
	private static final String DATA_SHARING_CONTRIBUTOR_CODE = "C";
	private static final double DATA_SHARING_PRICE_PLAN_RATE = 0.00;
	
	/**
	 * Consolidate information from subscriber data sharing result info (from DAO), subscriber info list and soc-family type list into the resulting
	 * subscriber data sharing detail info. 
	 * 
	 * Refer to the following data structure when reading the code:
	 * 
	 * For each subscriber, the data sharing information will include:
	 *    subscriber ID
	 *    price plan code
	 *    recurring charge for price plan SOC
	 *    contract (service agreement) start date
	 *    subscription ID
	 *    list of non-data sharing SOC:
	 *       non data sharing SOC
	 *       recurring charge
	 *       list of family type
	 *    list of data sharing group:
	 *       data sharing group name
	 *       for each data sharing group name:
	 *          data sharing SOC
	 *          list of family type for the SOC
	 *          recurring charge of the SOC
	 *          an boolean indicator showing the subscriber is contributing or not
	 * 
	 */
	public static Collection<SubscriberDataSharingDetailInfo> mapResultToDetailInfo(Collection<DataSharingResultInfo> resultList, 
			Collection<SubscriberInfo> subscriberInfoList, Map<String, List<String>> familyTypesSocMap, String[] dataSharingGroupCodes) 
			throws ApplicationException {
		
		// Null input results in null output
		if (resultList ==  null) {
			return null;
		}
		
		Collection<SubscriberDataSharingDetailInfo> subscriberDSDetailInfoList = new ArrayList<SubscriberDataSharingDetailInfo>();
		
		// Empty input results in empty output
		if (resultList.size() == 0) {
			return subscriberDSDetailInfoList;
		}

		// First loop is to create list of subscriberDataSharingDetailInfo
		for (DataSharingResultInfo resultInfo : resultList) {
			String subscriberId = resultInfo.getSubscriberId();
			if (subscriberId == null || subscriberId.trim().length() == 0) {
				continue;
			}
			if (searchFromSubscriberDataSharingDetailInfoList(subscriberDSDetailInfoList, subscriberId) == null) {
				SubscriberDataSharingDetailInfo detailInfo = new SubscriberDataSharingDetailInfo();
				detailInfo.setSubscriberId(subscriberId);
				// Populate the newly created subscriber with other info
				SubscriberInfo subscriberInfo = searchFromSubscriberInfoList(subscriberInfoList, subscriberId);
				if (subscriberInfo != null) {
					detailInfo.setContractStartDate(subscriberInfo.getCommitment().getStartDate());
					detailInfo.setContractEndDate(subscriberInfo.getCommitment().getEndDate());
					detailInfo.setSubscriptionId(subscriberInfo.getSubscriptionId());
				}
				subscriberDSDetailInfoList.add(detailInfo);
			}
		}
		
		// Second loop is to populate the subscribers' data sharing info from result info list
		for (DataSharingResultInfo resultInfo : resultList) {
			String subscriberId = resultInfo.getSubscriberId();
			SubscriberDataSharingDetailInfo subscriberDSDetailInfo = searchFromSubscriberDataSharingDetailInfoList(subscriberDSDetailInfoList, subscriberId);
			
			// Check whether the SOC is data sharing 
			if (resultInfo.getAllowSharingGroupCd() != null && resultInfo.getAllowSharingGroupCd().trim().length() > 0) {
				// Adding data sharing SOC to the list
				DataSharingSocInfo socInfo = new DataSharingSocInfo();
				socInfo.setDataSharingSocCode(resultInfo.getSocCode());
				socInfo.setDataSharingSocDescription(resultInfo.getSocDescription());
				socInfo.setDataSharingSocDescriptionFrench(resultInfo.getSocDescriptionFrench());
				socInfo.setContributingInd(isContributingSoc(resultInfo));
				if (isPricePlan(resultInfo)) { 
					socInfo.setDataSharingSpentAmt(DATA_SHARING_PRICE_PLAN_RATE); // data sharing price plan SOC amount does not contributes to MSC
				} else {
					socInfo.setDataSharingSpentAmt(resultInfo.getRate());					
				}					
				if (familyTypesSocMap != null) { 
					socInfo.setFamilyTypes(familyTypesSocMap.get(resultInfo.getSocCode()));
				}
				DataSharingDetailInfo dsDetailInfo = new DataSharingDetailInfo();
				dsDetailInfo.setDataSharingGroupCode(resultInfo.getAllowSharingGroupCd());
				dsDetailInfo.getDataSharingSocList().add(socInfo);
				subscriberDSDetailInfo.mergeDataSharingDetailInfo(dsDetailInfo);				
			} else {
				// adding non-data sharing SOC to the list
				if (isPricePlan(resultInfo) == false) {
					NonDataSharingRegularSocInfo ndsSocInfo = new NonDataSharingRegularSocInfo();
					ndsSocInfo.setSocCode(resultInfo.getSocCode());
					ndsSocInfo.setSocRecurringCharge(resultInfo.getRate());
					if (familyTypesSocMap != null) { 
						ndsSocInfo.setFamilyTypes(familyTypesSocMap.get(resultInfo.getSocCode()));
					}
					subscriberDSDetailInfo.getNonDataSharingRegularSocList().add(ndsSocInfo);
				}
			}	
			
			// If SOC is a price plan, populate the subscriber's price plan info
			if (isPricePlan(resultInfo)) {
				subscriberDSDetailInfo.setPricePlanCode(resultInfo.getSocCode());
				subscriberDSDetailInfo.setPricePlanRecurringCharge(resultInfo.getRate());
				subscriberDSDetailInfo.setPricePlanDSGroupCode(resultInfo.getAllowSharingAccessTypeCd());
				subscriberDSDetailInfo.setPricePlanContributingInd(DATA_SHARING_CONTRIBUTOR_CODE.equals(resultInfo.getAllowSharingAccessTypeCd()));
			}
		}
		
		// Clean up subscriber data sharing entries with no data sharing info and filter on dataSharingGroupCodes (from parameter)
		return removeNonDataSharingAndReservedSubscribersAndFilterDataSharingGroupCodes(subscriberDSDetailInfoList, dataSharingGroupCodes, subscriberInfoList);
	}

	private static boolean isContributingSoc(DataSharingResultInfo resultInfo) {
		return resultInfo.getAllowSharingAccessTypeCd().equals(DATA_SHARING_CONTRIBUTOR_CODE);
	}

	private static boolean isPricePlan(DataSharingResultInfo resultInfo) {
		return resultInfo.getServiceType().equals(SERVICE_TYPE_PRICE_PLAN);
	}
	
	// Search the subscriber from the subscriberDataSharingDetailInfoList
	private static SubscriberDataSharingDetailInfo searchFromSubscriberDataSharingDetailInfoList(Collection<SubscriberDataSharingDetailInfo> subscriberDsInfoList, String subscriberId) {
		
		if (subscriberDsInfoList == null || subscriberId == null || subscriberDsInfoList.size() == 0 || subscriberId.trim().length() == 0) {
			return null;
		}
		
		for (SubscriberDataSharingDetailInfo info : subscriberDsInfoList) {
			if (info.getSubscriberId().equals(subscriberId)) {
				return info;				
			}
		}
		return null;
	}
	
	// Search the subscriber from the subscriber info list
	private static SubscriberInfo searchFromSubscriberInfoList(Collection<SubscriberInfo> subscriberInfoList, String subscriberId) {
		
		if (subscriberInfoList == null || subscriberId == null || subscriberInfoList.size() == 0 || subscriberId.trim().length() == 0) {
			return null;
		}
		
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			if (subscriberInfo.getSubscriberId().equals(subscriberId)) {
				return subscriberInfo;
			}			
		}
		return null;
	}
	
	private static Collection<SubscriberDataSharingDetailInfo> removeNonDataSharingAndReservedSubscribersAndFilterDataSharingGroupCodes(
			Collection<SubscriberDataSharingDetailInfo> subDataSharingDetails, String[] dataSharingGroupCodes, Collection<SubscriberInfo> subscriberInfoList) {
		List<String> reservedSubscriberIds = getReservedSubscriberIds(subscriberInfoList);
		Collection<SubscriberDataSharingDetailInfo> filteredList = new ArrayList<SubscriberDataSharingDetailInfo> ();
		List<String> dataSharingGroupCodeList = getRealValuesFromArrayAsList(dataSharingGroupCodes);
		for (SubscriberDataSharingDetailInfo subDataSharingDetail : subDataSharingDetails) {
			if (reservedSubscriberIds.contains(subDataSharingDetail.getSubscriberId()) == false) {
				Collection<DataSharingDetail> dataSharingDetailList = subDataSharingDetail.getDataSharingInfoList();
				if (hasValidDataSharingGroup(dataSharingDetailList, dataSharingGroupCodeList)) {
					filteredList.add(subDataSharingDetail);
				}
			}
		}
		return filteredList;
	}
	
	private static List<String> getReservedSubscriberIds(Collection<SubscriberInfo> subscriberInfoList) {
		List<String> reservedSubscriberIds = new ArrayList<String>();
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			if (subscriberInfo.getStatus() == SubscriberInfo.STATUS_RESERVED) {
				reservedSubscriberIds.add(subscriberInfo.getSubscriberId());
			}			
		}
		return reservedSubscriberIds;
	}
	
	private static boolean hasValidDataSharingGroup(Collection<DataSharingDetail> dataSharingDetailList, List<String> dataSharingGroupCodeList) {
		if (hasDataSharing(dataSharingDetailList)) {
			if (isDataSharingGroupFilterEmpty(dataSharingGroupCodeList)) {
				return true;
			}			
			for (DataSharingDetail dataSharingDetail : dataSharingDetailList) {
				if (dataSharingGroupCodeList.contains(dataSharingDetail.getDataSharingGroupCode())) {
					return true;
				}
			}
		}
		return false;				
	}

	private static boolean isDataSharingGroupFilterEmpty(List<String> dataSharingGroupCodeList) {
		return dataSharingGroupCodeList.size() == 0;
	}

	private static boolean hasDataSharing(Collection<DataSharingDetail> dataSharingDetailList) {
		return dataSharingDetailList != null && dataSharingDetailList.isEmpty() == false;
	}

	private static List<String> getRealValuesFromArrayAsList(String[] dataSharingGroupCodes) {
		List<String> dataSharingGroupCodeList = new ArrayList<String>();
		if (dataSharingGroupCodes != null) {
			dataSharingGroupCodeList.addAll(Arrays.asList(dataSharingGroupCodes));
			dataSharingGroupCodeList.removeAll(Arrays.asList("", null));
		}
		return dataSharingGroupCodeList;
	}
	
}
