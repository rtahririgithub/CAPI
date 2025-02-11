package com.telus.eas.subscriber.info;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.DurationServiceCommitmentAttributeData;
import com.telus.api.account.PrepaidPromotionDetail;
import com.telus.api.reference.FundSource;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.util.ClientApiUtils;
import com.telus.api.util.DateUtil;
import com.telus.eas.framework.info.Info;
import com.telus.eas.framework.info.PublicCloneable;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ServiceInfo;

/**
 * Title: Telus - Amdocs Domain Beans Description: Copyright: Copyright (c) 2001
 * Company: Telus Mobility
 * 
 * @author Michael Lapish
 * @version 1.0
 */

public class ServiceAgreementInfo extends BaseAgreementInfo implements ContractService {

	static final long serialVersionUID = 1L;

	
	public static final String SERVICE_TYPE_REGULAR = "R";
	public static final String SERVICE_TYPE_OPTIONAL = "O";
	public static final String SERVICE_TYPE_PRICEPLAN = "P";
	public static final String SERVICE_TYPE_REGULAR_AUTO_EXPIRE = "G";
	public static final String SERVICE_TYPE_OPTIONAL_AUTO_EXPIRE = "T";

	/**
	 * @link aggregation
	 */
	private HashMap serviceFeatures = new HashMap(0);
	private transient ServiceInfo service; // it was transient
	private String serviceType;
	private int serviceTerm;
	private String serviceTermUnits;
	// comment out, this field is never assigned a value anywhere
	//private String[] additionalNumbers;
	private boolean wps;
	private boolean autoRenew;
	private int autoRenewFundSource = ServiceSummary.AUTORENEW_NOT_DEFINED;
	private boolean featureCard;
	private double recurringCharge;
	private PrepaidPromotionDetailInfo prepaidPromotionDetail;
	private boolean prepaidLBM;
	private String dealerCode;
	private String salesRepId;
	private int puchaseFundSource = FundSource.FUND_SOURCE_NOT_DEFINED;
	private String wpsMappedKbSoc;
	private String billCycleTreatmentCode;
	private Date originalEffectiveDate;
    
	public ServiceAgreementInfo() { }

	public ServiceAgreementInfo(ServiceInfo service) {
		setService(service);

		if (service.isIncludedPromotion()) {
			if (service.getTermMonths() > 0) { // Assume units is months
				GregorianCalendar calendar = new GregorianCalendar();
				setEffectiveDate(calendar.getTime());
				calendar.add(Calendar.MONTH, service.getTermMonths());
				setExpiryDate(calendar.getTime());
			}
		}

		setTransaction(ADD);
	}

	public void setEffectiveDate(Date newEffectiveDate) {
		if (originalEffectiveDate == null) {
			originalEffectiveDate = newEffectiveDate;
		}
		super.setEffectiveDate(newEffectiveDate);
		setEffectiveDate(serviceFeatures, newEffectiveDate);
	}
	
	public Date getOriginalEffectiveDate() {
		return originalEffectiveDate;
	}

	public void setExpiryDate(Date newExpiryDate) {
		super.setExpiryDate(newExpiryDate);
		setExpiryDate(serviceFeatures, newExpiryDate);
	}

	public void setFeatures(Map serviceFeatures) {
		this.serviceFeatures = (HashMap) serviceFeatures;
		setThisAsParent(this.serviceFeatures);
	}

	public ServiceFeatureInfo[] getFeatures0(boolean includeDeleted) {
		return (ServiceFeatureInfo[]) getChildren(serviceFeatures, ServiceFeatureInfo.class, includeDeleted);
	}

	public List getFeatures0(boolean includeDeleted, List destinationList) {
		return getChildren(serviceFeatures, includeDeleted, destinationList);
	}

	public ContractFeature[] getFeatures() {
		return (ContractFeature[]) getChildren(serviceFeatures, ServiceFeatureInfo.class, false);
	}

	public ContractFeature[] getChangedFeatures() {
		return (ContractFeature[]) getChildrenByTransaction(serviceFeatures, BaseAgreementInfo.UPDATE, ServiceFeatureInfo.class);
	}

	public void setService(ServiceInfo service) {
		this.service = service;
		setServiceCode(service.getCode());
		this.serviceType = service.getServiceType();
		this.wps = service.isWPS();
		this.serviceTerm = service.getTerm();
		this.serviceTermUnits = service.getTermUnits();
		this.recurringCharge = service.getRecurringCharge();
		this.prepaidLBM = service.isPrepaidLBM();
		this.wpsMappedKbSoc = service.getWPSMappedKBSocCode();
		this.billCycleTreatmentCode = service.getBillCycleTreatmentCode();
	}

	public String getBillCycleTreatmentCode() {
		return billCycleTreatmentCode;
	}

	public Service getService() {
		return service;
	}

	public ServiceInfo getService0() {
		return service;
	}

	public boolean containsFeature(String code, Date effectiveDate, Date expiryDate, boolean includeDeleted) {
		code = Info.padTo(code, ' ', 6);
		return containsChild(serviceFeatures, code, effectiveDate, expiryDate, includeDeleted);
	}

	public ContractFeature addFeature(RatedFeature feature) {
		return addFeature((RatedFeatureInfo) feature);
	}

	public ServiceFeatureInfo addFeature(RatedFeatureInfo feature) {
		if (feature == null) {
			throw new IllegalArgumentException("feature is null");
		}

		ServiceFeatureInfo sf = (ServiceFeatureInfo) serviceFeatures.get(feature.getCode());

		if (sf != null) { // Exists in list.
			if (sf.getTransaction() == ServiceFeatureInfo.DELETE) {
				sf.setTransaction(ServiceFeatureInfo.NO_CHG);
			}
		} else {
			sf = new ServiceFeatureInfo(feature);
			serviceFeatures.put(feature.getCode(), sf);
			sf.setParent(this);
		}

		return sf;
	}

	public void removeFeature(String featureCode) throws UnknownObjectException {
		featureCode = Info.padTo(featureCode, ' ', 6);
		removeFeatureChild(serviceFeatures, featureCode);
	}

	public int getFeatureCount() {
		return getFeatureCount0(false);
	}

	public int getFeatureCount0(boolean includeDeleted) {
		return getChildCount(serviceFeatures, includeDeleted);
	}

	public ContractFeature getFeature(String code) throws UnknownObjectException {
		return getFeature0(code, false);
	}

	public ServiceFeatureInfo getFeature0(String code, boolean includeDeleted) throws UnknownObjectException {
		code = Info.padTo(code, ' ', 6);
		return (ServiceFeatureInfo) getChild(serviceFeatures, code, includeDeleted);
	}

	public ContractFeature getFeatureBySwitchCode(String switchCode) throws UnknownObjectException {
		for (Iterator keyIter = serviceFeatures.keySet().iterator(); keyIter.hasNext();) {
			ServiceFeatureInfo serviceFeatureInfo = (ServiceFeatureInfo) serviceFeatures.get(keyIter.next());
			if (serviceFeatureInfo.getSwitchCode() != null && serviceFeatureInfo.getSwitchCode().trim().equals(switchCode)) {
				return serviceFeatureInfo;
			}
		}
		throw new UnknownObjectException("code=[" + switchCode + "]");
	}

	public String getServiceCode() {
		return getCode();
	}

	public void setServiceCode(String serviceCode) {
		setCode(serviceCode, 9);
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public int getServiceTerm() {
		return serviceTerm;
	}

	public void setServiceTerm(int serviceTerm) {
		this.serviceTerm = serviceTerm;
		// setChanged();
	}

	public String getServiceTermUnits() {
		return serviceTermUnits;
	}

	public void setServiceTermUnits(String serviceTermUnits) {
		this.serviceTermUnits = serviceTermUnits;
		// setChanged();
	}

	public boolean isModified() {
		return isModified(this, serviceFeatures);
	}

	public void commit() {
		commit(this, serviceFeatures);
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("ServiceAgreementInfo:{\n");
		s.append("    super=[").append(super.toString()).append("]\n");
		s.append("    Orginal Effective Date=[").append(originalEffectiveDate).append("]\n");
		s.append("    serviceType=[").append(serviceType).append("]\n");
		s.append("    serviceTerm=[").append(serviceTerm).append("]\n");
		s.append("    serviceTermUnits=[").append(serviceTermUnits).append("]\n");
		s.append("    recurringCharge=[").append(recurringCharge).append("]\n");
		s.append("    wps=[").append(wps).append("]\n");
		s.append("    autoRenew=[").append(autoRenew).append("]\n");
		s.append("    autoRenewFundSource=[").append(autoRenewFundSource).append("]\n");
		s.append("    featureCard=[").append(featureCard).append("]\n");

		/*
		 * additionalNumbers is never assigned a value anywhere, useless
		 * attribute if (additionalNumbers == null) {
		 * s.append("    additionalNumbers=[null]\n"); } else if
		 * (additionalNumbers.length == 0) {
		 * s.append("    additionalNumbers={}\n"); } else { for (int i = 0; i <
		 * additionalNumbers.length; i++) {
		 * s.append("    additionalNumbers[").append(i).append("]=[")
		 * .append(additionalNumbers[i]).append("]\n"); } }
		 */

		if (serviceFeatures == null) {
			s.append("    serviceFeatures=[null]\n");
		} else if (serviceFeatures.size() == 0) {
			s.append("    serviceFeatures={}\n");
		} else {
			Object[] key = serviceFeatures.keySet().toArray();
			for (int i = 0; i < key.length; i++) {
				s.append("    serviceFeatures[").append(i).append("]=[").append(key[i]).append("]\n");
			}
		}

		s.append("    service=[").append(service).append("]\n");

		s.append("}");

		return s.toString();
	}

	public String[] getAdditionalNumbers() {
		ArrayList additionalNumbers = new ArrayList();
		try {
			for (int i = 0; i < this.getFeatures().length; i++) {
				if ((this.getFeatures()[i].getAdditionalNumber() != null) && !(this.getFeatures()[i].getAdditionalNumber().equals("")))
					additionalNumbers.add(this.getFeatures()[i].getAdditionalNumber());
			}
		} catch (Throwable t) {
		}
		return (String[]) additionalNumbers.toArray(new String[additionalNumbers.size()]);
	}

	public void setWPS(boolean wps) {
		this.wps = wps;
	}

	public boolean isWPS() {
		return wps;
	}

	public boolean getAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(boolean autoRenew) {
		this.autoRenew = autoRenew;
		setChanged();
	}

	public boolean isFeatureCard() {
		return featureCard;
	}

	public void setFeatureCard(boolean featureCard) {
		this.featureCard = featureCard;
	}

	public double getRecurringCharge() {
		return recurringCharge;
	}

	public void setRecurringCharge(double recurringCharge) {
		this.recurringCharge = recurringCharge;
	}

	public String getDescription() {
		return service.getDescription();
	}

	public String getDescriptionFrench() {
		return service.getDescriptionFrench();
	}

	public void setTransaction(byte newTransaction, boolean includeFeatures, boolean includeDeletedFeatures) {
		super.setTransaction(newTransaction);
		if (includeFeatures) {
			ServiceFeatureInfo[] features = getFeatures0(includeDeletedFeatures);
			for (int i = 0; i < features.length; i++) {
				features[i].setTransaction(newTransaction);
			}
		}
	}

	/**
	 * @deprecated Deprecating this method,as the Prepaid API
	 *             call,getPromotionBucketDetail is Deprecated.
	 */
	public PrepaidPromotionDetail getPrepaidPromotionDetail() {
		return prepaidPromotionDetail;
	}

	/**
	 * @deprecated Deprecating this method,as the Prepaid API
	 *             call,getPromotionBucketDetail is Deprecated.
	 */
	public void setPrepaidPromotionDetail(PrepaidPromotionDetailInfo ppd) {
		prepaidPromotionDetail = ppd;
	}

	public boolean isPrepaidLBM() {
		return prepaidLBM;
	}

	public void setAutoRenewFundSource(int autoRenewFundSource) {
		this.autoRenewFundSource = autoRenewFundSource;
		setChanged();
	}

	public int getAutoRenewFundSource() {
		return autoRenewFundSource;
	}

	public boolean containsFeature(String featureCode) {
		return containsFeature(featureCode, null, null, false);
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getSalesRepId() {
		return salesRepId;
	}

	public void setSalesRepId(String salesRepId) {
		this.salesRepId = salesRepId;
	}

	public Object clone() {
		ServiceAgreementInfo o = (ServiceAgreementInfo) super.clone();
		o.serviceFeatures = PublicCloneable.Helper.clone(serviceFeatures);

		// the following are reference data, it does not hurt to just keep the
		// reference.
		// o.service = (ServiceInfo ) clone( service );
		// o.prepaidPromotionDetail = (PrepaidPromotionDetailInfo) clone
		// (prepaidPromotionDetail) ;

		return o;
	}

	public void setPurchaseFundSource(int purchaseFundSource) {
		this.puchaseFundSource = purchaseFundSource;

	}

	public int getPurchaseFundSource() {
		return puchaseFundSource;
	}

	public String getWpsMappedKbSoc() {
		return wpsMappedKbSoc;
	}

	/**
	 * Generates service mapping code for in-memory services management. The
	 * code consists of serviceCode + EffectiveDate. Please make sure this
	 * generation is the same through all implementations of ContractService
	 * interface, or come up with a base abstract class to make it uniform.
	 */
	public String getServiceMappingCode() {
		return ClientApiUtils.getContractServiceMappingKey(this);
	}
	
	/**
	 * [March -2021] , removed the all x-hour logic which is not in use but keeping the method interface without any implementation to avoid issues on consumer side
	 * Dont implement below four x-hour duration methods for future code refactor in Rest API code stack.
	 */
	
	public Calendar getDurationServiceStartTime() {
		ContractFeature xhourFeature = getXhourFeature();
		DurationServiceCommitmentAttributeData durationAttributeData = new DurationServiceCommitmentAttributeData(xhourFeature.getParameter());
		Calendar cal = durationAttributeData.getDisplayTimeZone().getID().equals(DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN) ? durationAttributeData.getXhrServiceStartTime()
				: DateUtil.calendarToTimezone(durationAttributeData.getXhrServiceStartTime(), durationAttributeData.getDisplayTimeZone().getID());
		return cal;
	}

	public Calendar getDurationServiceEndTime() {
		ContractFeature xhourFeature = getXhourFeature();
		DurationServiceCommitmentAttributeData durationAttributeData = new DurationServiceCommitmentAttributeData(xhourFeature.getParameter());
		Calendar cal = durationAttributeData.getDisplayTimeZone().getID().equals(DurationServiceCommitmentAttributeData.TIME_ZONE_CANADA_MOUNTAIN) ? durationAttributeData.getXhrServiceEndTime()
				: DateUtil.calendarToTimezone(durationAttributeData.getXhrServiceEndTime(), durationAttributeData.getDisplayTimeZone().getID());
		return cal;
	}

	public ContractFeature getXhourFeature() {
			return null;
	}

	public boolean isDurationService() {
		return ((service != null && service.getDurationServiceHours() > 0) || getXhourFeature() != null);
	}


	
}