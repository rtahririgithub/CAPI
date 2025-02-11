package com.telus.api.account;

import java.util.Date;
import java.util.List;


public interface EnterpriseAddress {

	long getAddressId();
	
	void setAddressId(long addressId);
	
	String getAddressTypeCode();

	void setAddressTypeCode(String addressTypeCode);

	List getAdditionalAddressInformation();

	void setAdditionalAddressInformation(
			List additionalAddressInformation);

	List getRenderedAddress();

	void setRenderedAddress(List renderedAddress);

	long getAddrAssgnmtId();

	void setAddrAssgnmtId(long addrAssgnmtId);

	String getAddressAssignmentSubTypeCode();

	void setAddressAssignmentSubTypeCode(
			String addressAssignmentSubTypeCode);

	String getAddressAssignmentTypeCode();

	void setAddressAssignmentTypeCode(
			String addressAssignmentTypeCode);

	String getAddressMatchingStatusCode();

	void setAddressMatchingStatusCode(
			String addressMatchingStatusCode);

	String getAddressSearchText();

	void setAddressSearchText(String addressSearchText);

	String getCanadaPostBuildName();

	void setCanadaPostBuildName(String canadaPostBuildName);

	String getCanadaPostLocnName();

	void setCanadaPostLocnName(String canadaPostLocnName);

	String getCanadaPostRecordType();

	void setCanadaPostRecordType(String canadaPostRecordType);

	String getCareOf();

	void setCareOf(String careOf);

	String getCountryCode();

	void setCountryCode(String countryCode);

	String getEmailAddressText();

	void setEmailAddressText(String emailAddressText);

	String getExternalAddressId();

	void setExternalAddressId(String externalAddressId);

	long getExternalAddressSourceId();

	void setExternalAddressSourceId(long externalAddressSourceId);

	String getExternalServiceAddressId();

	void setExternalServiceAddressId(
			String externalServiceAddressId);

	String getMailingTypeCode();

	void setMailingTypeCode(String mailingTypeCode);

	String getMunicipalityName();

	void setMunicipalityName(String municipalityName);

	String getPostOfficeBoxNumber();

	void setPostOfficeBoxNumber(String postOfficeBoxNumber);

	String getPostalZipCode();

	void setPostalZipCode(String postalZipCode);

	String getProvinceStateCode();

	void setProvinceStateCode(String provinceStateCode);

	long getRelatedAddressAssignmentId();

	void setRelatedAddressAssignmentId(
			long relatedAddressAssignmentId);

	String getStreetDirectionCode();

	void setStreetDirectionCode(String streetDirectionCode);

	String getStreetName();

	void setStreetName(String streetName);

	String getStreetTypeCode();

	void setStreetTypeCode(String streetTypeCode);

	String getUnitNumber();

	void setUnitNumber(String unitNumber);

	String getValidateAddressIndicator();

	void setValidateAddressIndicator(
			String validateAddressIndicator);

	String getUnitTypeCode();

	void setUnitTypeCode(String unitTypeCode);

	String getCivicNumber();

	void setCivicNumber(String civicNumber);

	String getCivicNumberSuffix();

	void setCivicNumberSuffix(String civicNumberSuffix);

	String getRuralRouteNumber();

	void setRuralRouteNumber(String ruralRouteNumber);

	String getRuralRouteTypeCode();

	void setRuralRouteTypeCode(String ruralRouteTypeCode);

	String getStationName();

	void setStationName(String stationName);

	String getStationQualifier();

	void setStationQualifier(String stationQualifier);

	String getStationTypeCode();

	void setStationTypeCode(String stationTypeCode);

	String getFleetMailOfficeName();

	void setFleetMailOfficeName(String fleetMailOfficeName);

	String getHmcsName();

	void setHmcsName(String hmcsName);

	long getMasterSourceId();

	void setMasterSourceId(long masterSourceId);

	Date getLastUpdateTimeStamp();

	void setLastUpdateTimeStamp(Date lastUpdateTimeStamp);

	void translateAddress(Address address);

}