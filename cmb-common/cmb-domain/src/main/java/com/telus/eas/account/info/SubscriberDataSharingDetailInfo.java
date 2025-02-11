package com.telus.eas.account.info;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.telus.api.account.SubscriberDataSharingDetail;
import com.telus.eas.framework.info.Info;

public class SubscriberDataSharingDetailInfo extends Info implements SubscriberDataSharingDetail {

	private static final long serialVersionUID = 1L;
	
	private String subscriberId;
	private Date contractStartDate;
	private Date contractEndDate;
	private long subscriptionId;
	private String pricePlanCode;
	private double pricePlanRecurringCharge;
	private String pricePlanDSGroupCode;
	private boolean pricePlanContributingInd;
	private Collection<NonDataSharingRegularSoc> nonDataSharingRegularSocList = new ArrayList<NonDataSharingRegularSoc>();
	private Collection<DataSharingDetail> dataSharingInfoList = new ArrayList<DataSharingDetail>();

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public Date getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getPricePlanCode() {
		return pricePlanCode;
	}

	public void setPricePlanCode(String pricePlanCode) {
		this.pricePlanCode = pricePlanCode;
	}

	public double getPricePlanRecurringCharge() {
		return pricePlanRecurringCharge;
	}

	public void setPricePlanRecurringCharge(double pricePlanRecurringCharge) {
		this.pricePlanRecurringCharge = pricePlanRecurringCharge;
	}

	public String getPricePlanDSGroupCode() {
		return pricePlanDSGroupCode;
	}

	public void setPricePlanDSGroupCode(String pricePlanDSGroupCode) {
		this.pricePlanDSGroupCode = pricePlanDSGroupCode;
	}

	public boolean isPricePlanContributingInd() {
		return pricePlanContributingInd;
	}

	public void setPricePlanContributingInd(boolean pricePlanContributingInd) {
		this.pricePlanContributingInd = pricePlanContributingInd;
	}

	public Collection<NonDataSharingRegularSoc> getNonDataSharingRegularSocList() {
		return nonDataSharingRegularSocList;
	}

	public void setNonDataSharingRegularSocList(Collection<NonDataSharingRegularSoc> nonDataSharingRegularSocList) {
		this.nonDataSharingRegularSocList = nonDataSharingRegularSocList;
	}
	
	public void addNonDataSharingRegularSocInfo(NonDataSharingRegularSoc nonDataSharingRegularSocInfo) {
		this.nonDataSharingRegularSocList.add(nonDataSharingRegularSocInfo);
	}

	public Collection<DataSharingDetail> getDataSharingInfoList() {
		return dataSharingInfoList;
	}

	public void setDataSharingInfoList(Collection<DataSharingDetail> dataSharingInfoList) {
		this.dataSharingInfoList = dataSharingInfoList;
	}
	
	/**
	 * Merge the Data Sharing SOC Info List under the same Data Sharing Group Code
	 */
	public void mergeDataSharingDetailInfo(DataSharingDetail dataSharingDetailInfo) {
		DataSharingDetail dsDetailInfo = searchDataSharingDetailInfoByGroupCode(dataSharingDetailInfo.getDataSharingGroupCode());
		if (dsDetailInfo == null) {
			this.dataSharingInfoList.add(dataSharingDetailInfo);
		} else {
			// Merge the underlying data sharing SOC Info list
			dsDetailInfo.getDataSharingSocList().addAll(dataSharingDetailInfo.getDataSharingSocList());
		}
	}
	
	/**
	 * Search the data sharing detail info list by data sharing group code
	 */
	public DataSharingDetail searchDataSharingDetailInfoByGroupCode(String dataSharingGroupCode) {
		
		for (DataSharingDetail dsDetailInfo : this.dataSharingInfoList) {
			if (dsDetailInfo.getDataSharingGroupCode().equals(dataSharingGroupCode)) {
				return dsDetailInfo;
			}
		}
		return null;
		
	}

	public String toString() {
		
	    StringBuffer s = new StringBuffer();
	    s.append("SubscriberDataSharingDetailInfo:{\n");
	    s.append("    subscriberId=[").append(subscriberId).append("]\n");
	    s.append("    contractStartDate=[").append(contractStartDate).append("]\n");
	    s.append("    subscriptionId=[").append(subscriptionId).append("]\n");
	    s.append("    pricePlanCode=[").append(pricePlanCode).append("]\n");
	    s.append("    pricePlanRecurringCharge=[").append(pricePlanRecurringCharge).append("]\n");
	    s.append("    nonDataSharingRegularSocList=[");
	    if (nonDataSharingRegularSocList != null) {
	    	for (NonDataSharingRegularSoc ndsRegSoc : nonDataSharingRegularSocList) {
	    		s.append(ndsRegSoc.toString());
	    	}	    	
	    }
	    s.append("]\n");
	    s.append("    dataSharingInfoList=[");
	    if (dataSharingInfoList != null) {
	    	for (DataSharingDetail dsDetail : dataSharingInfoList) {
	    		s.append(dsDetail.toString()).append("]\n");
	    	}
	    }
	    s.append("]\n");	    
	    s.append("}");
	   
	    return s.toString();
	}
	
	/**
	 * Inner class for the Non Data Sharing Regular SOC Info.
	 *
	 */
	public static class NonDataSharingRegularSocInfo extends Info implements NonDataSharingRegularSoc {
		
		private static final long serialVersionUID = 1L;
		
		private String socCode;
		private Collection<String> familyTypes = new ArrayList<String>();
		private double socRecurringCharge;
		
		public String getSocCode() {
			return socCode;
		}
		
		public void setSocCode(String socCode) {
			this.socCode = socCode;
		}
		
		public Collection<String> getFamilyTypes() {
			return familyTypes;
		}
		
		public void setFamilyTypes(Collection<String> familyTypes) {
			this.familyTypes = familyTypes;
		}
		
		public double getSocRecurringCharge() {
			return socRecurringCharge;
		}
		
		public void setSocRecurringCharge(double socRecurringCharge) {
			this.socRecurringCharge = socRecurringCharge;
		}
		
		public void addFamilyType(String familyType) {
			familyTypes.add(familyType);
		}
		
		public String toString() {
			
		    StringBuffer s = new StringBuffer();
		    s.append("NonDataSharingRegularSocInfo:{\n");
		    s.append("    socCode=[").append(socCode).append("]\n");
		    s.append("    familyTypes=[");
		    if (familyTypes != null) {
		    	for (String familyType : familyTypes) {
			    	s.append(familyType);
			    	s.append(", ");
		    	}
		    }
		    s.append("]\n");
		    s.append("    socRecurringCharge=[").append(socRecurringCharge).append("]\n");
		    s.append("}");
		   
		    return s.toString();
		}
	}
	
	/**
	 * Inner class for Data Sharing Detail Info
	 *
	 */
	public static class DataSharingDetailInfo extends Info implements DataSharingDetail {
		
		private static final long serialVersionUID = 1L;

		private String dataSharingGroupCode;
		private Collection<DataSharingSoc> dataSharingSocList = new ArrayList<DataSharingSoc>();
		
		public String getDataSharingGroupCode() {
			return dataSharingGroupCode;
		}
		
		public void setDataSharingGroupCode(String dataSharingGroupCode) {
			this.dataSharingGroupCode = dataSharingGroupCode;
		}
		
		public Collection<DataSharingSoc> getDataSharingSocList() {
			return dataSharingSocList;
		}
		
		public void setDataSharingSocList(Collection<DataSharingSoc> dataSharingSocLis) {
			this.dataSharingSocList = dataSharingSocLis;
		}
		
		public void addDataSharingSoc(DataSharingSoc dataSharingSoc) {
			this.dataSharingSocList.add(dataSharingSoc);
		}
		
		public String toString() {
			
		    StringBuffer s = new StringBuffer();
		    s.append("DataSharingDetailInfo:{\n");
		    s.append("    dataSharingGroupCode=[").append(dataSharingGroupCode).append("]\n");
		    s.append("    dataSharingSocList=[");
		    if (dataSharingSocList != null) {
		    	for (DataSharingSoc dsSoc : dataSharingSocList) {
		    		s.append(dsSoc.toString());
			    	s.append(", ");
		    	}
		    }
		    s.append("]\n");
		    s.append("}");
		   
		    return s.toString();
		}

	}
	
	/**
	 * Inner class for Data Sharing SOC Info
	 *
	 */
	public static class DataSharingSocInfo extends Info implements DataSharingSoc {
	
		private static final long serialVersionUID = 1L;
		
		private String dataSharingSocCode;
		private String dataSharingSocDescription;
		private String dataSharingSocDescriptionFrench;
		private Collection<String> familyTypes = new ArrayList<String>();
		private double dataSharingSpentAmt;
		private boolean contributingInd;
		
		public String getDataSharingSocCode() {
			return dataSharingSocCode;
		}
		
		public void setDataSharingSocCode(String dataSharingSocCode) {
			this.dataSharingSocCode = dataSharingSocCode;
		}
		
		public String getDataSharingSocDescription() {
			return dataSharingSocDescription;
		}

		public void setDataSharingSocDescription(String dataSharingSocDescription) {
			this.dataSharingSocDescription = dataSharingSocDescription;
		}

		public String getDataSharingSocDescriptionFrench() {
			return dataSharingSocDescriptionFrench;
		}

		public void setDataSharingSocDescriptionFrench(String dataSharingSocDescriptionFrench) {
			this.dataSharingSocDescriptionFrench = dataSharingSocDescriptionFrench;
		}

		public Collection<String> getFamilyTypes() {
			return familyTypes;
		}
		
		public void setFamilyTypes(Collection<String> familyTypes) {
			this.familyTypes = familyTypes;
		}
		
		public double getDataSharingSpentAmt() {
			return dataSharingSpentAmt;
		}
		
		public void setDataSharingSpentAmt(double dataSharingSpentAmt) {
			this.dataSharingSpentAmt = dataSharingSpentAmt;
		}
		
		public boolean getContributingInd() {
			return contributingInd;
		}
		
		public void setContributingInd(boolean contributingInd) {
			this.contributingInd = contributingInd;
		}

		public void addFamilyType(String familyType) {
			this.familyTypes.add(familyType);
		}

		public String toString() {
			
		    StringBuffer s = new StringBuffer();
		    s.append("DataSharingSocInfo:{\n");
		    s.append("    dataSharingSocCode=[").append(dataSharingSocCode).append("]\n");
		    s.append("    dataSharingSocDescription=[").append(dataSharingSocDescription).append("]\n");
		    s.append("    dataSharingSocDescriptionFrench=[").append(dataSharingSocDescriptionFrench).append("]\n");
		    s.append("    familyTypes=[");
		    if (familyTypes != null) {
		    	for (String familyType : familyTypes) {
			    	s.append(familyType);
			    	s.append(", ");
		    	}
		    }
		    s.append("]\n");
		    s.append("    dataSharingSpentAmt=[").append(dataSharingSpentAmt).append("]\n");
		    s.append("    contributionInd=[").append(contributingInd).append("]\n");
		    s.append("}");
		   
		    return s.toString();
		}
	}

	/**
	 * Inner class for Data Sharing Result Info - data returned straight from KB database
	 *
	 */
	public static class DataSharingResultInfo extends Info implements DataSharingResult {

		private static final long serialVersionUID = 1L;
		
		private String subscriberId;
		private String socCode;
		private String socDescription;
		private String socDescriptionFrench;
		private String serviceType;
		private double rate;
		private String allowSharingGroupCd;
		private String allowSharingAccessTypeCd;
		
		public String getSubscriberId() {
			return subscriberId;
		}
		
		public void setSubscriberId(String subscriberId) {
			this.subscriberId = subscriberId;
		}
		
		public String getSocCode() {
			return socCode;
		}
		
		public void setSocCode(String socCode) {
			this.socCode = socCode;
		}
		
		public String getSocDescription() {
			return socDescription;
		}
		
		public void setSocDescription(String socDescription) {
			this.socDescription = socDescription;
		}
		
		public String getSocDescriptionFrench() {
			return socDescriptionFrench;
		}
		
		public void setSocDescriptionFrench(String socDescriptionFrench) {
			this.socDescriptionFrench = socDescriptionFrench;
		}
		
		public String getServiceType() {
			return serviceType;
		}
		
		public void setServiceType(String serviceType) {
			this.serviceType = serviceType;
		}
		
		public double getRate() {
			return rate;
		}
		
		public void setRate(double rate) {
			this.rate = rate;
		}
		
		public String getAllowSharingGroupCd() {
			return allowSharingGroupCd;
		}
		
		public void setAllowSharingGroupCd(String allowSharingGroupCd) {
			this.allowSharingGroupCd = allowSharingGroupCd;
		}
		
		public String getAllowSharingAccessTypeCd() {
			return allowSharingAccessTypeCd;
		}
		
		public void setAllowSharingAccessTypeCd(String allowSharingAccessTypeCd) {
			this.allowSharingAccessTypeCd = allowSharingAccessTypeCd;
		}

		public String toString() {
			
		    StringBuffer s = new StringBuffer();
		    s.append("DataSharingResultInfo:{\n");
		    s.append("    subscriberId=[").append(subscriberId).append("]\n");
		    s.append("    socCode=[").append(socCode).append("]\n");
		    s.append("    socDescription=[").append(socDescription).append("]\n");
		    s.append("    socDescriptionFrench=[").append(socDescriptionFrench).append("]\n");
		    s.append("    serviceType=[").append(serviceType).append("]\n");
		    s.append("    rate=[").append(rate).append("]\n");
		    s.append("    allowSharingGroupCd=[").append(allowSharingGroupCd).append("]\n");
		    s.append("    allowSharingAccessTypeCd=[").append(allowSharingAccessTypeCd).append("]\n");
		    s.append("}");
		   
		    return s.toString();
		}
	}
	
	/**
	 * Collect an unique set of SOC codes from the Data Sharing Result Info list.
	 */
	public static String[] collectSocCodeSetFromResultList(Collection<DataSharingResultInfo> dataSharingResultInfoList) {
		
		Set<String> socCodeSet = new HashSet<String>();

		if (dataSharingResultInfoList != null) {
			for (DataSharingResultInfo resultInfo : dataSharingResultInfoList) {
				String socCode = resultInfo.getSocCode();
				if (socCode != null) {
					socCodeSet.add(socCode);
				}
			}
		}
		return socCodeSet.toArray(new String[socCodeSet.size()]);
	}
}
