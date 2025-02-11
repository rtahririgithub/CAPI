package com.telus.cmb.subscriber.utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.telus.cmb.common.util.DateUtil;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class FeatureTransactionContext {
	
	private String serviceCode;
	private String featureCode;
	private boolean isAdd;
	private Date effectiveDate;
	private Date expirationDate;
	private boolean isPrepaid;
	private String wpsMappedKbSoc;

	private ServiceFeatureInfo serviceFeatureInfo;

	private FeatureTransactionContext associatedFeature;
	
	public String getServiceCode() {
		return serviceCode;
	}
	public String getFeatureCode() {
		return featureCode;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public boolean isAdd() {
		return isAdd;
	}
	public ServiceFeatureInfo getServiceFeatureInfo() {
		return serviceFeatureInfo;
	}
	public boolean isPrepaid() {
		return isPrepaid;
	}
	public String getWpsMappedKbSoc() {
		return wpsMappedKbSoc;
	}
	public FeatureTransactionContext getAssociatedFeature() {
		return associatedFeature;
	}
	public void setAssociatedFeature(FeatureTransactionContext associatedFeatureInfo) {
		this.associatedFeature = associatedFeatureInfo;
	}
	public FeatureTransactionContext ( ServiceFeatureInfo serviceFeature, SubscriberContractInfo contractInfo, boolean isPPChange, Date logicalDate ) {
		
		this.serviceFeatureInfo = serviceFeature;
		serviceCode=serviceFeature.getServiceCode().trim();
		featureCode=serviceFeature.getFeatureCode().trim();
		effectiveDate = serviceFeature.getEffectiveDate();
		expirationDate = serviceFeature.getExpiryDate();
		
		if ( contractInfo.getPricePlanCode().equals(serviceFeature.getParentCode()) ) {
			//PP included feature
			if ( effectiveDate==null) {
				effectiveDate = contractInfo.getEffectiveDate();
			}
			if (isPPChange) {
				isAdd = true;
			}
		} else {
			
			ServiceAgreementInfo serviceAgreement = contractInfo.getService0(serviceFeature.getParentCode(), false );
			if ( effectiveDate==null ) {
				//if null take its parent SOC's effective date
				effectiveDate = serviceAgreement.getEffectiveDate();
			}
			
			if ( ServiceInfo.SERVICE_TYPE_CODE_OPTIONAL_SOC.equals( serviceAgreement.getServiceType() ) 
				|| ServiceInfo.SERVICE_TYPE_CODE_OPTIONAL_AUTOEXP_SOC.equals( serviceAgreement.getServiceType() )
				) { 
				
				if (isPPChange && serviceAgreement.getTransaction0()!=BaseAgreementInfo.DELETE ) {
					//PP change imply adding included service (optional SOC)'s feature, as long as the SOC is not excluded
					isAdd = true;
				}
				//included service can be added individually ( assuming was excluded previously )
				if ( effectiveDate==null ) {
					//if still null, take the PricePlan effective date
					effectiveDate = contractInfo.getEffectiveDate();
				}
			} else {
				//add-on service's feature
				isAdd = serviceAgreement.getTransaction0()==BaseAgreementInfo.ADD;
				isPrepaid = serviceAgreement.isWPS();
				wpsMappedKbSoc = (serviceAgreement.getWpsMappedKbSoc()!=null)? serviceAgreement.getWpsMappedKbSoc().trim(): null;
				
				if ( serviceAgreement.isWPS() && expirationDate!=null) {
					
					//prepaid contract service only has expiration date, because the prepaid feature has term (usually 30 days), and expires 
					//by end of the term. So we calculate effective date base on term and expiration date. 
					effectiveDate = calculatePrepaidServiceEffectiveDate( expirationDate, serviceAgreement.getServiceTerm());
				} 
			}
		}
		
		if ( effectiveDate==null ) {
			effectiveDate = logicalDate;
		} 
	}
	
	private Date calculatePrepaidServiceEffectiveDate( Date expirationDate, int term) {
		Calendar today = DateUtil.getEODTodayCalendar();
		Calendar cal = Calendar.getInstance();
		cal.setTime(expirationDate);
		while ( cal.after(today) ) {
			cal.add(Calendar.DATE, (0-term) );
		}
		return cal.getTime();
	}

	public boolean isSameFeature( FeatureParameterHistoryInfo featureParam ) {
		
		boolean result = this.serviceCode.equals(featureParam.getServiceCode().trim()) 
				&& this.featureCode.equals(featureParam.getFeatureCode().trim())
				;
		
		result = result 
			&& featureParam.getServiceSequenceNo().equals(this.serviceFeatureInfo.getServiceSequenceNo())
			&& featureParam.getFeatureSequenceNo().equals(this.serviceFeatureInfo.getFeatureSequenceNo())
			;
		
		return result; 
	}
	
	public static FeatureTransactionContext[] newFeatureTransactionContexts( ServiceFeatureInfo[] serviceFeatures, SubscriberContractInfo contractInfo, boolean isPPChange, Date logicalDate ) {
		FeatureTransactionContext[] ftcs = new FeatureTransactionContext[serviceFeatures.length];
		for( int i=0; i<serviceFeatures.length; i++  ) {
			ftcs[i] = new FeatureTransactionContext( serviceFeatures[i], contractInfo, isPPChange, logicalDate );
		}
		
		//consolidate prepaid feature if there is any.
		List<FeatureTransactionContext> list = new ArrayList<FeatureTransactionContext>();
		for( FeatureTransactionContext feature: ftcs ) {
			if (feature.isPrepaid()) {
				for( FeatureTransactionContext f: ftcs) {
					if ( f.getServiceCode().equals( feature.getWpsMappedKbSoc())) {
						feature.setAssociatedFeature( f );
						list.add( feature );
					}
				}
			}
		}
		
		if (list.isEmpty() ) {
			return ftcs;
		} else {
			return list.toArray(new FeatureTransactionContext[list.size()]);
		}
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServiceFeature [serviceCode=")
				.append(serviceCode).append(", featureCode=")
				.append(featureCode).append(", isAdd=").append(isAdd)
				.append(", effectiveDate=").append(effectiveDate)
				.append(", expirationDate=").append(expirationDate).append("]");
		return builder.toString();
	}
}