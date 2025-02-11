package com.telus.provider.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.CallingCircleCommitmentAttributeData;
import com.telus.api.account.CallingCircleParameters;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.DurationServiceCommitmentAttributeData;
import com.telus.api.account.Subscriber;
import com.telus.api.account.VoiceToTextOptions;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.Service;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;

public class TMContractFeature extends BaseProvider implements ContractFeature {

	private static final long serialVersionUID = 1L;
	private final ServiceFeatureInfo delegate;
	private Subscriber subscriber;
	private TMContract contract;
	private DurationServiceCommitmentAttributeData durationServiceData;



	
	public TMContractFeature(TMProvider provider, ServiceFeatureInfo info, Subscriber subscriber ) {
		super(provider);
		delegate = info;
		this.subscriber = subscriber;
	}

	public ServiceFeatureInfo getDelegate() {
		return delegate; 
	}
	
	public String getAdditionalNumber() {
		return delegate.getAdditionalNumber();
	}

	public Date getEffectiveDate() {
		return delegate.getEffectiveDate();
	}

	public Date getExpiryDate() {
		return delegate.getExpiryDate();
	}

	public RatedFeature getFeature() {
		return delegate.getFeature();
	}

	public String getParameter() {
		return delegate.getParameter();
	}

	public String getServiceCode() {
		return delegate.getServiceCode();
	}

	public void setAdditionalNumber(String additionalNumber) {
		delegate.setAdditionalNumber(additionalNumber);
	}

	public void setEffectiveDate(Date effectiveDate) {
		delegate.setEffectiveDate(effectiveDate);
	}

	public void setExpiryDate(Date expiryDate) {
		delegate.setExpiryDate(expiryDate);
	}

	public void setParameter(String parameter) {
		if ( delegate.isCallingCircle() ) {
			//setting calling circle parameter requires extra logic, to centralise the implementation,
			//route the call to setCallingCirclePhoneNumberList()
			String [] newPhoneNumbers = ServiceFeatureInfo.parseCallingCircleParameter(parameter);
			setCallingCirclePhoneNumberList( newPhoneNumbers );
		} else {
			delegate.setParameter(parameter);
		}
	}

	public String getCode() {
		return delegate.getCode();
	}

	public String getDescription() {
		return delegate.getDescription();
	}

	public String getDescriptionFrench() {
		return delegate.getDescriptionFrench();
	}

	public void setCallingCirclePhoneNumberList(String[] phoneNumbers) {
		if ( delegate.isCallingCircle() || delegate.isPrepaidCallingCircle()) {
			//if CC list contain same set of phone numbers, then do not proceed
			boolean hasDifferentCCList = !hasSameCCList(phoneNumbers); 
			if (hasDifferentCCList==true) {
				delegate.setCallingCirclePhoneNumberList(phoneNumbers);
				try {
					getCallingCircleCommitmentAttributeData();
				} catch (TelusAPIException e) {
					Logger.debug("Ban=" + subscriber.getBanId() + " subId=" + subscriber.getSubscriberId() + " getCallingCircleCommitmentAttributeData() encountered error; ignored");
				}
			}
		}
	}

	public CallingCircleParameters getCallingCircleParameters() throws TelusAPIException {
		if ( delegate.getFeature().isCallingCircle() && !delegate.getFeature().isWPS()) {
			//on demand lazy retrieval
			if ( delegate.getCallingCircleParameters()==null ) {
				try {
					CallingCircleParameters result = null;
					result = provider.getSubscriberManagerBean().retrieveCallingCircleParameters(
							subscriber.getBanId(),
							subscriber.getSubscriberId(),
							subscriber.getProductType(),
							getServiceCode(),
							getFeature().getCode()
							);
					//and cache it.
					delegate.setCallingCirclParameters( result );
				} catch (Throwable e) {
					throw new TelusAPIException ( e );
				}
			}
		} else if (delegate.getFeature().isPrepaidCallingCircle()){ 
			if (delegate.getCallingCircleParameters()==null && delegate.getFeature().isCallingCircle()) {
				//Regular Calling Circle
				
				Service prepaidCallingCircleSrv = provider.getReferenceDataManager().getWPSService(delegate.getCode());
				String kbSocCode = prepaidCallingCircleSrv.getWPSMappedKBSocCode();
				// Check if KB SOC already added to the subscriber
				ContractService kbSoc = getKbMappedPrepaidService(kbSocCode);
				if (kbSoc == null)
					return null;
				String kbFtrCode = getKbCallingCircleFeature(kbSoc).getCode();

				try {
					CallingCircleParameters result = null;
					result = provider.getSubscriberManagerBean().retrieveCallingCircleParameters(subscriber.getBanId(), subscriber.getSubscriberId(), subscriber.getProductType(), kbSocCode, kbFtrCode);
					// and cache it.
					delegate.setCallingCirclParameters(result);
				} catch (Throwable e) {
					throw new TelusAPIException(e);
				}
			}else if (delegate.getFeature().getCategoryCode() != null &&
				FeatureInfo.CATEGORY_CODE_CALL_HOME_FREE.equalsIgnoreCase(delegate.getFeature().getCategoryCode().trim())) {
				// Call Home Free that should be treated as Calling Circle in Prepaid
				
				Service prepaidCallingCircleSrv = provider.getReferenceDataManager().getWPSService(delegate.getCode());
				String kbSocCode = prepaidCallingCircleSrv.getWPSMappedKBSocCode();
				// Check if KB SOC already added to the subscriber
				ContractService kbSoc = getKbMappedPrepaidService(kbSocCode);
				if (kbSoc == null)
					return null;
				String kbFtrCode = getKbCallingCircleFeature(kbSoc).getCode();

				ContractFeature[] features = subscriber.getContract().getFeatures(true);
				CallingCircleParametersInfo params = null;
				for (int i = 0; i < features.length; i++) {
					if (features[i].getCode().trim().equals(kbFtrCode.trim())) {
						params = new CallingCircleParametersInfo();

						CallingCirclePhoneListInfo currentPhoneList = new CallingCirclePhoneListInfo();
						currentPhoneList.setEffectiveDate(features[i].getEffectiveDate());
						currentPhoneList.setExpiryDate(delegate.getExpiryDate());
						currentPhoneList.setPhoneNumberList(((TMContractFeature) features[i]).delegate.getCallingCirclePhoneNumbersFromParam());
						params.setCallingCircleCurrentPhoneNumberList(currentPhoneList);

						CallingCirclePhoneListInfo futurePhoneList = new CallingCirclePhoneListInfo();
						futurePhoneList.setEffectiveDate(features[i].getEffectiveDate());
						futurePhoneList.setExpiryDate(delegate.getExpiryDate());
						futurePhoneList.setPhoneNumberList(((TMContractFeature) features[i]).delegate.getCallingCirclePhoneNumbersFromParam());
						params.setCallingCircleFuturePhoneNumberList(futurePhoneList);
					}
				}
				delegate.setCallingCirclParameters(params);
			}
			
		}
		return delegate.getCallingCircleParameters();
	}

	
	private ContractService getKbMappedPrepaidService(String kbMappedPrepaidServiceCode) throws TelusAPIException {
		ContractService[] allServices = subscriber.getContract().getServices();
		ContractService aService = null;
		for(int i=0; i<allServices.length; i++) {
			aService = allServices[i];
			if (aService.getCode().trim().equals(kbMappedPrepaidServiceCode.trim())){
				return aService;
			}
		}		
		return null;
	}
	
	private ContractFeature getKbCallingCircleFeature(ContractService kbMappedPrepaidService)	{
		ContractFeature [] allfeatures = kbMappedPrepaidService.getFeatures();
		ContractFeature feature = null;
		for(int i=0; i<allfeatures.length; i++) {
			feature = allfeatures[i];
			if (feature.getFeature().getSwitchCode().trim().equals(FeatureInfo.SWITCH_CODE_CALLING_CIRCLE) 
				 ||	feature.getFeature().getSwitchCode().trim().equals(FeatureInfo.SWITCH_CODE_CALL_HOME_FREE))
				return feature;
		}
		return null;
	}	
	
	public VoiceToTextOptions getVoiceToTextOptions() {
		return delegate.getVoiceToTextOptions();
	}
	
	public void setVoiceToTextOptions(VoiceToTextOptions option) {
		delegate.setVoiceToTextOptions(option);
	}

	void setContract(TMContract contract) {
		this.contract = contract;
	}

	public CallingCircleCommitmentAttributeData getCallingCircleCommitmentAttributeData() throws TelusAPIException {
		
		if ( delegate.isCallingCircle()==true && delegate.getCallingCircleCommitmentAttributeData()==null ) {
			try {
				contract.evaluateCallingCircleCommitmentData();
			} catch (TelusAPIException e ) {
				Logger.debug("Ban=" + subscriber.getBanId() + " subId=" + subscriber.getSubscriberId() + " getCallingCircleCommitmentAttributeData() encountered error.");
				Logger.debug(e );
				throw e;
			}
		}
		return delegate.getCallingCircleCommitmentAttributeData();
	}
	
	private boolean hasSameCCList(String[] newPhoneNumbers ) {
		boolean result = false;
		String[] oldPhoneNumbers = delegate.getCallingCirclePhoneNumbersFromParam();
		if (oldPhoneNumbers.length>0) {
			result = isStringArraySame( oldPhoneNumbers, newPhoneNumbers);
		}
		return result;
	}
	

	/**
	 * This method compares two String array to see if they contain exact same string elements. The order of element is ignored.
	 * @param strArray1
	 * @param strArray2
	 * @return
	 */
	public static boolean isStringArraySame(String[] strArray1, String[] strArray2 ) {
		boolean result = false;
		if ( strArray1.length==strArray2.length) {
			List set1 = new ArrayList( Arrays.asList(strArray1));
			List set2 = new ArrayList( Arrays.asList(strArray2) );
			set1.removeAll(set2);
			if (set1.isEmpty() ) result=true;
		}
		
		return result;
	}
	
	public static ContractFeature undecorate(ContractFeature contractFeature) {
		if(contractFeature instanceof TMContractFeature){
			contractFeature = ((TMContractFeature)contractFeature).getDelegate();
		}

		return contractFeature;
	}

	public static ContractFeature[] undecorate(ContractFeature[] contractFeatures) {
		ContractFeature[] undecoratedContractFeatures = new ContractFeature[contractFeatures.length];
		for(int i=0; i<contractFeatures.length; i++) {
			undecoratedContractFeatures[i] = undecorate(contractFeatures[i]);
		}
		return undecoratedContractFeatures;
	}
	
	/**
	 * [March -2021] , removed the all x-hour logic which is not in use but keeping the method interface without any implementation to avoid issues on consumer side
	 * Dont implement this method for future code refactor in Rest API code stack.
	 */
	public DurationServiceCommitmentAttributeData getDurationServiceCommitmentAttributeData() {
		// this attribute is only applicable for XHOUR feature
		if(!DurationServiceCommitmentAttributeData.SWITCH_CODE_XHOUR.equals(this.getFeature().getSwitchCode())) {
			return null;
		} else if(durationServiceData == null) {
			String serializableParam = getParameter();
			// parse serializable and create an object out of it
			durationServiceData = new DurationServiceCommitmentAttributeData(serializableParam);
		}
		return durationServiceData;
	}

}
