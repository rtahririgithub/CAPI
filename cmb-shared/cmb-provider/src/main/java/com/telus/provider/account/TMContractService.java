/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.PrepaidPromotionDetail;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.util.ClientApiUtils;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMContractService extends BaseProvider implements ContractService {

	private static final long serialVersionUID = 1L;
	/**
	 * @link aggregation
	 */
	private final ServiceAgreementInfo delegate;
	private Subscriber subscriber;
	private TMContract contract; 

	public TMContractService(TMProvider provider,
			ServiceAgreementInfo delegate, Subscriber subscriber) {
		super(provider);
		this.delegate = delegate;
		this.subscriber = subscriber;
	}

	public ServiceAgreementInfo getDelegate() {
		return delegate;
	}

	// --------------------------------------------------------------------
	// Decorative Methods
	// --------------------------------------------------------------------
	public ContractFeature addFeature(RatedFeature feature)
			throws UnknownObjectException {
		return decorate(delegate.addFeature(feature));
	}

	public void removeFeature(String featureCode) throws UnknownObjectException {
		delegate.removeFeature(featureCode);
	}

	public int getFeatureCount() {
		return delegate.getFeatureCount();
	}

	public ContractFeature[] getFeatures() {
		return decorate(delegate.getFeatures());
	}

	public ContractFeature getFeature(String code)
			throws UnknownObjectException {
		return decorate(delegate.getFeature(code));
	}
	
	public ContractFeature getFeatureBySwitchCode(String switchCode) throws UnknownObjectException {
		return decorate(delegate.getFeatureBySwitchCode(switchCode));
	}

	public Date getEffectiveDate() {
		return delegate.getEffectiveDate();
	}

	public void setEffectiveDate(Date effectiveDate) {
		delegate.setEffectiveDate(effectiveDate);
	}

	public Date getExpiryDate() {
		return delegate.getExpiryDate();
	}

	public void setExpiryDate(Date expiryDate) {
		delegate.setExpiryDate(expiryDate);
	}

	public String[] getAdditionalNumbers() {
		return delegate.getAdditionalNumbers();
	}

	public boolean getAutoRenew() {
		return delegate.getAutoRenew();
	}

	public void setAutoRenew(boolean autoRenew) {
		delegate.setAutoRenew(autoRenew);
		if (!autoRenew) {
			delegate.setAutoRenewFundSource(ServiceSummary.AUTORENEW_NOT_DEFINED);
		}
	}

	public void setAutoRenewFundSource(int autoRenewFundSource) {
		delegate.setAutoRenewFundSource(autoRenewFundSource);
	}
	
	public int getAutoRenewFundSource() {
		return delegate.getAutoRenewFundSource();
	}
	
	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

	public String getDescription() {
		return delegate.getDescription();
	}

	public String getDescriptionFrench() {
		return delegate.getDescriptionFrench();
	}

	public String getCode() {
		return delegate.getCode();
	}

	public ContractFeature[] getChangedFeatures() {
		return decorate(delegate.getChangedFeatures());
	}

	// --------------------------------------------------------------------
	// Service Methods
	// --------------------------------------------------------------------
	public Service getService() throws TelusAPIException {
		return provider.getReferenceDataManager0().decorate(
				delegate.getService());
	}

	public ContractFeature decorate(ContractFeature contractFeature) {
		if ((contractFeature instanceof TMContractFeature) == false) {
			contractFeature = new TMContractFeature(provider,
					(ServiceFeatureInfo) contractFeature, subscriber);
			((TMContractFeature)contractFeature).setContract(contract);

		}
		return contractFeature;
	}

	public ContractFeature[] decorate(ContractFeature[] contractFeatures) {
		ContractFeature[] decoratedFeatures = new ContractFeature[contractFeatures.length];
		for (int i = 0; i < contractFeatures.length; i++) {
			decoratedFeatures[i] = decorate(contractFeatures[i]);
		}
		return decoratedFeatures;
	}

	public PrepaidPromotionDetail getPrepaidPromotionDetail() {
		return delegate.getPrepaidPromotionDetail();
	}
	
	public boolean containsFeature(String featureCode) {
		return delegate.containsFeature(featureCode);
	}
	
	public String getDealerCode() {
		return delegate.getDealerCode();
	}

	public String getSalesRepId() {
		return delegate.getSalesRepId();
	}

	public int getPurchaseFundSource() {
		return delegate.getPurchaseFundSource();
	}

	public void setPurchaseFundSource(int purchaseFundSource) {
		delegate.setPurchaseFundSource( purchaseFundSource);
	}

	TMContract getContract() {
		return contract;
	}

	void setContract(TMContract contract) {
		this.contract = contract;
	}
	
	
	public static ContractService undecorate(ContractService contractService) {
		if(contractService instanceof TMContractService){
			contractService = ((TMContractService)contractService).getDelegate();
		}

		return contractService;
	}

	public static ContractService[] undecorate(ContractService[] contractServices) {
		ContractService[] undecoratedContractServices = new ContractService[contractServices.length];
		for(int i=0; i<contractServices.length; i++) {
			undecoratedContractServices[i] = undecorate(contractServices[i]);
		}
		return undecoratedContractServices;
	}

	/**
	 * Generates service mapping code for in-memory services management.
	 * The code consists of serviceCode + EffectiveDate.
	 * Please make sure this generation is the same through all implementations
	 * of ContractService interface, or come up with a base abstract class to make it uniform.
	 */
	public String getServiceMappingCode() {
		return ClientApiUtils.getContractServiceMappingKey(this);
	}
	
	/**
	 * [March -2021] , removed the all x-hour logic which is not in use but keeping the method interface without any implementation to avoid issues on consumer side
	 * Dont implement below three methods for future code refactor in Rest API code stack.
	 */
	
	public Calendar getDurationServiceStartTime() {
		return delegate.getDurationServiceStartTime();
	}

	
	public Calendar getDurationServiceEndTime() {
		return delegate.getDurationServiceEndTime();
	}
	
	public boolean isDurationService() {
		try {
			return getDurationServiceStartTime() != null && getDurationServiceEndTime() != null;
		} catch(Exception ignored) {
			return false;
		}
	}
}
