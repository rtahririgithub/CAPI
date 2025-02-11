package com.telus.provider.portability;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestAddress;
import com.telus.api.portability.PortRequestException;
import com.telus.api.portability.PortRequestName;
import com.telus.api.reference.ApplicationSummary;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

import java.util.Date;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 13-Sep-2006
 */
public class TMPortRequest extends BaseProvider implements PortRequest {
	
	private final PortRequestInfo delegate;

	private final TMPortRequestSO portRequestSO;

	public TMPortRequest(TMProvider provider, PortRequestInfo delegate) {
		super(provider);
		this.delegate = delegate;
		this.portRequestSO = new TMPortRequestSO(provider);
	}

	public PortRequestInfo getDelegate() {	    
		return delegate;  
	}

	public String getPortDirectionIndicator() {
		return delegate.getPortDirectionIndicator();
	}

	public void setPortDirectionIndicator(String portDirectionIndicator) {
		delegate.setPortDirectionIndicator(portDirectionIndicator);
	}

	public PortRequestAddress getPortRequestAddress() {
		return delegate.getPortRequestAddress();
	}

	public void setPortRequestAddress(PortRequestAddress portRequestAddress) {
		delegate.setPortRequestAddress(portRequestAddress);
	}

	public PortRequestName getPortRequestName() {
		return delegate.getPortRequestName();
	}

	public void setPortRequestName(PortRequestName portRequestName) {
		delegate.setPortRequestName(portRequestName);
	}

	public String getBusinessName() {
		return delegate.getBusinessName();
	}

	public void setBusinessName(String businessName) {
		delegate.setBusinessName(businessName);
	}

	public String getAgencyAuthorizationName() {
		return delegate.getAgencyAuthorizationName();
	}

	public void setAgencyAuthorizationName(String agencyAuthorizationName) {
		delegate.setAgencyAuthorizationName(agencyAuthorizationName);
	}

	public Date getAgencyAuthorizationDate() {
		return delegate.getAgencyAuthorizationDate();
	}

	public void setAgencyAuthorizationDate(Date agencyAuthorizationDate) {
		delegate.setAgencyAuthorizationDate(agencyAuthorizationDate);
	}

	public String getAuthorizationIndicator() {
		return delegate.getAuthorizationIndicator();
	}

	public void setAgencyAuthorizationIndicator(String agencyAuthorizationIndicator) {
		delegate.setAgencyAuthorizationIndicator(agencyAuthorizationIndicator);
	}

	public String getAlternateContactNumber() {
		return delegate.getAlternateContactNumber();
	}

	public void setAlternateContactNumber(String alternateContactNumber) {
		delegate.setAlternateContactNumber(alternateContactNumber);
	}

	public Date getDesiredDateTime() {
		return delegate.getDesiredDateTime();
	}

	public void setDesiredDateTime(Date desiredDateTime) {
		delegate.setDesiredDateTime(desiredDateTime);
	}

	public String getOSPSerialNumber() {
		return delegate.getOSPSerialNumber();
	}

	public void setOSPSerialNumber(String oSPSerialNumber) {
		delegate.setOSPSerialNumber(oSPSerialNumber);
	}

	public String getOSPAccountNumber() {
		return delegate.getOSPAccountNumber();
	}

	public void setOSPAccountNumber(String oSPAccountNumber) {
		delegate.setOSPAccountNumber(oSPAccountNumber);
	}

	public String getOSPPin() {
		return delegate.getOSPPin();
	}

	public void setOSPPin(String oSPPin) {
		delegate.setOSPPin(oSPPin);
	}

	public String getRemarks() {
		return delegate.getRemarks();
	}

	public void setRemarks(String remarks) {
		delegate.setRemarks(remarks);
	}

	public boolean isAutoActivate() {
		return delegate.isAutoActivate();
	}

	public void setAutoActivate(boolean autoActivate) {
		delegate.setAutoActivate(autoActivate);
	}

	/**
	 * The validate method relies on the platform ID to be accurate. If the platform ID is incorrect,
	 * the validation may pass the wrong product type to the underlying service.
	 * 
	 * @throws PortRequestException
	 * @throws TelusAPIException
	 */
	public void validate() throws PortRequestException, TelusAPIException {
		try{
			provider.getSubscriberLifecycleFacade().validatePortInRequest(delegate, provider.getApplication(), provider.getUser());
		}catch(ApplicationException ae){
			if(!ae.getErrorCode().equals("")){
				if( ae.getErrorCode().equals("PRM_FALSE")){
					throw new PortRequestException(null, provider.getApplicationMessage("10005", ae.getErrorMessage(), ae.getErrorMessage()));
				}	
				throw new PortRequestException(ae, provider.getApplicationMessage(ApplicationSummary.APP_PRM, ae.getErrorCode(),ae));
		}
			throw new TelusAPIException(ae);
		}
	}

	public void activate() throws PortRequestException, TelusAPIException {
		try{
			provider.getSubscriberLifecycleFacade().activatePortInRequest(delegate,provider.getApplication());
		}catch(ApplicationException ae){
			if (!ae.getErrorCode().equals("")) {
				throw new PortRequestException(ae, provider.getApplicationMessage(ApplicationSummary.APP_PRM, ae.getErrorCode(),ae));
			}
			throw new TelusAPIException(ae);
		} catch (SystemException ae) {
			throw new TelusAPIException(ae);
		}
	}

	public String getPhoneNumber() {
		return delegate.getPhoneNumber();
	}

	public int getBanId() {
		return delegate.getBanId();
	}

	public String getPortRequestId() {
		return delegate.getPortRequestId();
	}

	public String getType() {
		return delegate.getType();
	}

	public String getStatusCategory() {
		return delegate.getStatusCategory();
	}

	public String getStatusCode() {
		return delegate.getStatusCode();
	}

	public String getStatusReasonCode() {
		return delegate.getStatusReasonCode();
	}

	public Date getCreationDate() {
		return delegate.getCreationDate();
	}

	public boolean canBeActivated() {
		return delegate.canBeActivated();
	}

	public boolean canBeSubmitted() {
		return delegate.canBeSubmitted();
	}

	public boolean canBeCanceled() {
		return delegate.canBeCanceled();
	}

	public boolean canBeModified() {
		return delegate.canBeModified();
	}
	
	public int getOutgoingBrandId() {
		return delegate.getOutgoingBrandId();
	}
	
	public void setOutgoingBrandId(int outgoingBrandId) {
		delegate.setOutgoingBrandId(outgoingBrandId);
	}	
	
	public int getIncomingBrandId() {
		return delegate.getIncomingBrandId();
	}
	
	public void setIncomingBrandId(int incomingBrandId) {
		delegate.setIncomingBrandId(incomingBrandId);
	}

	public String toString() {
		return delegate.toString();
	}

	public String getDslInd() {
		return delegate.getDslInd();
	}

	public Integer getDslLineNumber() {
		return delegate.getDslLineNumber();
	}

	public String getExpedite() {
		return delegate.getExpedite();
	}

	public String getOldReseller() {
		return delegate.getOldReseller();
	}

	public boolean getEndUserMovingInd() {
		return delegate.getEndUserMovingInd();
	}

	public void setDslInd(String dslInd) {
		delegate.setDslInd(dslInd);
	}

	public void setDslLineNumber(Integer dslLineNumber) {
		delegate.setDslLineNumber(dslLineNumber);
	}

	public void setEndUserMovingInd(boolean endUserMovingInd) {
		delegate.setEndUserMovingInd(endUserMovingInd);
	}

	public void setExpedite(String expedite) {
		delegate.setExpedite(expedite);
	}

	public void setOldReseller(String oldReseller) {
		delegate.setOldReseller(oldReseller);
	}

	public int getPlatformId() {
		return delegate.getPlatformId();
	}
	
	public void setPlatformId(int platformId) {
		delegate.setPlatformId(platformId);
	}

	public boolean isPortInFromMVNE() {
		return delegate.isPortInFromMVNE();
	}
}
