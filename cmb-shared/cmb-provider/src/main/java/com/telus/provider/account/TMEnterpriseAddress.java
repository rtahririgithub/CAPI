package com.telus.provider.account;

import java.util.Date;
import java.util.List;

import com.telus.api.account.Address;
import com.telus.api.account.EnterpriseAddress;
import com.telus.eas.account.info.EnterpriseAddressInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMEnterpriseAddress extends BaseProvider implements EnterpriseAddress {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final EnterpriseAddressInfo delegate;
	
	public TMEnterpriseAddress(TMProvider provider, EnterpriseAddressInfo delegate) {
		super(provider);
		this.delegate = delegate;
	}
	
	public EnterpriseAddressInfo getDelegate(){
		return delegate;
	}

	public long getAddressId() {
		return delegate.getAddressId();
	}

	public void setAddressId(long addressId) {
		delegate.setAddressId(addressId);
		
	}
	
	public String getAddressTypeCode() {
		return delegate.getAddressTypeCode();
	}

	public void setAddressTypeCode(String addressTypeCode) {
		delegate.setAddressTypeCode(addressTypeCode);

	}

	public List getAdditionalAddressInformation() {
		return delegate.getAdditionalAddressInformation();
	}

	public void setAdditionalAddressInformation(
			List additionalAddressInformation) {
		delegate.setAdditionalAddressInformation(
				additionalAddressInformation);

	}

	public List getRenderedAddress() {
		return delegate.getRenderedAddress();
	}

	public void setRenderedAddress(List renderedAddress) {
		delegate.setRenderedAddress(renderedAddress);

	}

	public long getAddrAssgnmtId() {
		return delegate.getAddrAssgnmtId();
	}

	public void setAddrAssgnmtId(long addrAssgnmtId) {
		delegate.setAddrAssgnmtId(addrAssgnmtId);

	}

	public String getAddressAssignmentSubTypeCode() {
		return delegate.getAddressAssignmentSubTypeCode();
	}

	public void setAddressAssignmentSubTypeCode(
			String addressAssignmentSubTypeCode) {
		delegate.setAddressAssignmentSubTypeCode(
				addressAssignmentSubTypeCode);

	}

	public String getAddressAssignmentTypeCode() {
		return delegate.getAddressAssignmentTypeCode();
	}

	public void setAddressAssignmentTypeCode(String addressAssignmentTypeCode) {
		delegate.setAddressAssignmentTypeCode(addressAssignmentTypeCode);
	}

	public String getAddressMatchingStatusCode() {
		return delegate.getAddressMatchingStatusCode();
	}

	public void setAddressMatchingStatusCode(String addressMatchingStatusCode) {
		delegate.setAddressMatchingStatusCode(addressMatchingStatusCode);

	}

	public String getAddressSearchText() {
		return delegate.getAddressSearchText();
	}

	public void setAddressSearchText(String addressSearchText) {
		delegate.setAddressSearchText(addressSearchText);

	}

	public String getCanadaPostBuildName() {
		
		return delegate.getCanadaPostBuildName();
	}

	public void setCanadaPostBuildName(String canadaPostBuildName) {
		delegate.setCanadaPostBuildName(canadaPostBuildName);

	}

	public String getCanadaPostLocnName() {
		
		return delegate.getCanadaPostLocnName();
	}

	public void setCanadaPostLocnName(String canadaPostLocnName) {
		delegate.setCanadaPostLocnName(canadaPostLocnName);

	}

	public String getCanadaPostRecordType() {
		
		return delegate.getCanadaPostRecordType();
	}

	public void setCanadaPostRecordType(String canadaPostRecordType) {
		delegate.setCanadaPostRecordType(canadaPostRecordType);

	}

	public String getCareOf() {
		
		return delegate.getCareOf();
	}

	public void setCareOf(String careOf) {
		delegate.setCareOf(careOf);

	}

	public String getCountryCode() {
		
		return delegate.getCountryCode();
	}

	public void setCountryCode(String countryCode) {
		delegate.setCountryCode(countryCode);

	}

	public String getEmailAddressText() {
		
		return delegate.getEmailAddressText();
	}

	public void setEmailAddressText(String emailAddressText) {
		delegate.setEmailAddressText(emailAddressText);

	}

	public String getExternalAddressId() {
		
		return delegate.getExternalAddressId();
	}

	public void setExternalAddressId(String externalAddressId) {
		delegate.setExternalAddressId(externalAddressId);

	}

	public long getExternalAddressSourceId() {
		
		return delegate.getExternalAddressSourceId();
	}

	public void setExternalAddressSourceId(long externalAddressSourceId) {
		delegate.setExternalAddressSourceId(externalAddressSourceId);

	}

	public String getExternalServiceAddressId() {
		
		return delegate.getExternalServiceAddressId();
	}

	public void setExternalServiceAddressId(String externalServiceAddressId) {
		delegate.setExternalServiceAddressId(externalServiceAddressId);

	}

	public String getMailingTypeCode() {
		
		return delegate.getMailingTypeCode();
	}

	public void setMailingTypeCode(String mailingTypeCode) {
		delegate.setMailingTypeCode(mailingTypeCode);

	}

	public String getMunicipalityName() {
		
		return delegate.getMunicipalityName();
	}

	public void setMunicipalityName(String municipalityName) {
		delegate.setMunicipalityName(municipalityName);

	}

	public String getPostOfficeBoxNumber() {
		
		return delegate.getPostOfficeBoxNumber();
	}

	public void setPostOfficeBoxNumber(String postOfficeBoxNumber) {
		delegate.setPostOfficeBoxNumber(postOfficeBoxNumber);

	}

	public String getPostalZipCode() {
		
		return delegate.getPostalZipCode();
	}

	public void setPostalZipCode(String postalZipCode) {
		delegate.setPostalZipCode(postalZipCode);

	}

	public String getProvinceStateCode() {
		
		return delegate.getProvinceStateCode();
	}

	public void setProvinceStateCode(String provinceStateCode) {
		delegate.setProvinceStateCode(provinceStateCode);

	}

	public long getRelatedAddressAssignmentId() {
		
		return delegate.getRelatedAddressAssignmentId();
	}

	public void setRelatedAddressAssignmentId(long relatedAddressAssignmentId) {
		delegate.setRelatedAddressAssignmentId(relatedAddressAssignmentId);

	}

	public String getStreetDirectionCode() {
		
		return delegate.getStreetDirectionCode();
	}

	public void setStreetDirectionCode(String streetDirectionCode) {
		delegate.setStreetDirectionCode(streetDirectionCode);

	}

	public String getStreetName() {
		
		return delegate.getStreetName();
	}

	public void setStreetName(String streetName) {
		delegate.setStreetName(streetName);

	}

	public String getStreetTypeCode() {
		
		return delegate.getStreetTypeCode();
	}

	public void setStreetTypeCode(String streetTypeCode) {
		delegate.setStreetTypeCode(streetTypeCode);

	}

	public String getUnitNumber() {
		
		return delegate.getUnitNumber();
	}

	public void setUnitNumber(String unitNumber) {
		delegate.setUnitNumber(unitNumber);

	}

	public String getValidateAddressIndicator() {
		
		return delegate.getValidateAddressIndicator();
	}

	public void setValidateAddressIndicator(String validateAddressIndicator) {
		delegate.setValidateAddressIndicator(validateAddressIndicator);

	}

	public String getUnitTypeCode() {
		
		return delegate.getUnitTypeCode();
	}

	public void setUnitTypeCode(String unitTypeCode) {
		delegate.setUnitTypeCode(unitTypeCode);

	}

	public String getCivicNumber() {
		
		return delegate.getCivicNumber();
	}

	public void setCivicNumber(String civicNumber) {
		delegate.setCivicNumber(civicNumber);

	}

	public String getCivicNumberSuffix() {
		
		return delegate.getCivicNumberSuffix();
	}

	public void setCivicNumberSuffix(String civicNumberSuffix) {
		delegate.setCivicNumberSuffix(civicNumberSuffix);

	}

	public String getRuralRouteNumber() {
		
		return delegate.getRuralRouteNumber();
	}

	public void setRuralRouteNumber(String ruralRouteNumber) {
		delegate.setRuralRouteNumber(ruralRouteNumber);

	}

	public String getRuralRouteTypeCode() {
		
		return delegate.getRuralRouteTypeCode();
	}

	public void setRuralRouteTypeCode(String ruralRouteTypeCode) {
		delegate.setRuralRouteTypeCode(ruralRouteTypeCode);

	}

	public String getStationName() {
		
		return delegate.getStationName();
	}

	public void setStationName(String stationName) {
		delegate.setStationName(stationName);

	}

	public String getStationQualifier() {
		
		return delegate.getStationQualifier();
	}

	public void setStationQualifier(String stationQualifier) {
		delegate.setStationQualifier(stationQualifier);

	}

	public String getStationTypeCode() {
		
		return delegate.getStationTypeCode();
	}

	public void setStationTypeCode(String stationTypeCode) {
		delegate.setStationTypeCode(stationTypeCode);

	}

	public String getFleetMailOfficeName() {
		
		return delegate.getFleetMailOfficeName();
	}

	public void setFleetMailOfficeName(String fleetMailOfficeName) {
		delegate.setFleetMailOfficeName(fleetMailOfficeName);

	}

	public String getHmcsName() {
		
		return delegate.getHmcsName();
	}

	public void setHmcsName(String hmcsName) {
		delegate.setHmcsName(hmcsName);

	}

	public long getMasterSourceId() {
		
		return delegate.getMasterSourceId();
	}

	public void setMasterSourceId(long masterSourceId) {
		delegate.setMasterSourceId(masterSourceId);

	}

	public Date getLastUpdateTimeStamp() {
		
		return delegate.getLastUpdateTimeStamp();
	}

	public void setLastUpdateTimeStamp(Date lastUpdateTimeStamp) {
		delegate.setLastUpdateTimeStamp(lastUpdateTimeStamp);

	}

	public void translateAddress(Address address) {
		delegate.translateAddress(address);

	}



}
