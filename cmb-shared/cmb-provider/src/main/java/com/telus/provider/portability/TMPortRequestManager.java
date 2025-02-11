package com.telus.provider.portability;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.portability.PRMReferenceData;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortInEligibilityException;
import com.telus.api.portability.PortOutEligibility;
import com.telus.api.portability.PortRequestException;
import com.telus.api.portability.PortRequestManager;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.Brand;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMPortRequestManager extends BaseProvider implements PortRequestManager {

	private TMPortRequestSO portRequestSO = new TMPortRequestSO(provider);

	public TMPortRequestManager(TMProvider provider) {
		super(provider);
	}

	public PortInEligibility testEligibility(String phoneNumber, String portVisibility) throws PortRequestException, TelusAPIException {
		return testPortInEligibility(phoneNumber, portVisibility, Brand.BRAND_ID_TELUS);
	}
	
	public PortInEligibility testPortInEligibility(String phoneNumber, String portVisibility, int incomingBrand) throws PortRequestException, TelusAPIException {
		try{
			return provider.getSubscriberLifecycleFacade().checkPortInEligibility(phoneNumber, portVisibility, incomingBrand);
		}catch(ApplicationException ae){
			if (!ae.getErrorCode().equals("")) {
				if (ae.getMessage().equals("PRM_FALSE")) {
					throw new PortInEligibilityException(provider.getApplicationMessage(ApplicationSummary.APP_PRM, ae.getErrorCode(), incomingBrand,ae), phoneNumber);
				}
				throw new PortRequestException(ae,provider.getApplicationMessage(ApplicationSummary.APP_PRM, ae.getErrorCode(), incomingBrand,ae));
			}
			throw new TelusAPIException(ae);
		}
		catch(Throwable t) {
			throw new PRMSystemException("check PortInEligibility -  failed for phoneNumber [" + phoneNumber + "]["+portVisibility+"/"+incomingBrand+"] cause: " + t);
		}
	}
	 /**
	 * Checks if a phone number is eligible for Porting to a different Carrier/Brand
	 * 
	 * @param phoneNumber - Phone Number to be checked for eligibility 
	 * @param ndpInd - Porting type : Wireless-to-Wireless , Wireless-to-Wireline , Wireline-to-Wireless . See PortOutEligibity for allowed values .
	 * @return A PortOutEligibility object containing eligibility details
	 * @throws PortRequestException
	 * @throws TelusAPIException
	 */
	public PortOutEligibility testPortOutEligibility(String phoneNumber,
			String ndpInd) throws PortRequestException, TelusAPIException {
		try {
			return provider.getSubscriberLifecycleHelper().checkSubscriberPortOutEligibility(phoneNumber, ndpInd);		
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;

	}

  public PRMReferenceData getPRMReferenceData(String category, String code) {
    return getPRMReferenceData(code);
  }

  public PRMReferenceData getPRMReferenceData(String code) {
    return portRequestSO.getReferenceData(code, Brand.BRAND_ID_NOT_APPLICABLE);
  }
  
  protected PRMReferenceData getPRMReferenceData(String code, int brandId) {
	    return portRequestSO.getReferenceData(code, brandId);
  }
  	
}
