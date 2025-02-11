package com.telus.eas.account.info;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.telus.api.account.Address;
import com.telus.api.account.EnterpriseAddress;
import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.RuralTypeInfo;

public class EnterpriseAddressInfo extends Info implements EnterpriseAddress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long 	addressId;
	private	String	addressTypeCode;
	private	List	additionalAddressInformation;
	private	List	renderedAddress;
	private	long	addrAssgnmtId;
	private	String	addressAssignmentSubTypeCode;
	private	String	addressAssignmentTypeCode;
	private	String	addressMatchingStatusCode;
	private	String	addressSearchText;
	private	String	canadaPostBuildName;
	private	String	canadaPostLocnName;
	private	String	canadaPostRecordType;
	private	String	careOf;
	private	String	countryCode;
	private	String	emailAddressText;
	private	String	externalAddressId;
	private	long	externalAddressSourceId;
	private	String	externalServiceAddressId;
	private	String	mailingTypeCode;
	private	String	municipalityName;
	private	String	postOfficeBoxNumber;
	private	String	postalZipCode;
	private	String	provinceStateCode;
	private	long	relatedAddressAssignmentId;
	private	String	streetDirectionCode;
	private	String	streetName;
	private	String	streetTypeCode;
	private	String	unitNumber;
	private	String	validateAddressIndicator;
	private	String	unitTypeCode;
	private	String	civicNumber;
	private	String	civicNumberSuffix;
	private	String	ruralRouteNumber;
	private	String	ruralRouteTypeCode;
	private	String	stationName;
	private	String	stationQualifier;
	private	String	stationTypeCode;
	private	String	fleetMailOfficeName;
	private	String	hmcsName;
	private	long	masterSourceId;
	private	Date	lastUpdateTimeStamp;

	public EnterpriseAddressInfo() {
		
	}
	
	public EnterpriseAddressInfo(AddressInfo addressInfo) {
		if (addressInfo != null) {
			translateAddress(addressInfo);
		}
	}

	public long getAddressId() {
		return this.addressId;
	}
	public void setAddressId(long addressId) {
		this.addressId = addressId;

	}
	public String getAddressTypeCode() {
		return addressTypeCode;
	}
	public void setAddressTypeCode(String addressTypeCode) {
		this.addressTypeCode = addressTypeCode;
	}
	public List getAdditionalAddressInformation() {
		return additionalAddressInformation;
	}
	public void setAdditionalAddressInformation(List additionalAddressInformation) {
		this.additionalAddressInformation = additionalAddressInformation;
	}
	public List getRenderedAddress() {
		return renderedAddress;
	}
	public void setRenderedAddress(List renderedAddress) {
		this.renderedAddress = renderedAddress;
	}
	public long getAddrAssgnmtId() {
		return addrAssgnmtId;
	}
	public void setAddrAssgnmtId(long addrAssgnmtId) {
		this.addrAssgnmtId = addrAssgnmtId;
	}
	public String getAddressAssignmentSubTypeCode() {
		return addressAssignmentSubTypeCode;
	}
	public void setAddressAssignmentSubTypeCode(String addressAssignmentSubTypeCode) {
		this.addressAssignmentSubTypeCode = addressAssignmentSubTypeCode;
	}
	public String getAddressAssignmentTypeCode() {
		return addressAssignmentTypeCode;
	}
	public void setAddressAssignmentTypeCode(String addressAssignmentTypeCode) {
		this.addressAssignmentTypeCode = addressAssignmentTypeCode;
	}
	public String getAddressMatchingStatusCode() {
		return addressMatchingStatusCode;
	}
	public void setAddressMatchingStatusCode(String addressMatchingStatusCode) {
		this.addressMatchingStatusCode = addressMatchingStatusCode;
	}
	public String getAddressSearchText() {
		return addressSearchText;
	}
	public void setAddressSearchText(String addressSearchText) {
		this.addressSearchText = addressSearchText;
	}
	public String getCanadaPostBuildName() {
		return canadaPostBuildName;
	}
	public void setCanadaPostBuildName(String canadaPostBuildName) {
		this.canadaPostBuildName = canadaPostBuildName;
	}
	public String getCanadaPostLocnName() {
		return canadaPostLocnName;
	}
	public void setCanadaPostLocnName(String canadaPostLocnName) {
		this.canadaPostLocnName = canadaPostLocnName;
	}
	public String getCanadaPostRecordType() {
		return canadaPostRecordType;
	}
	public void setCanadaPostRecordType(String canadaPostRecordType) {
		this.canadaPostRecordType = canadaPostRecordType;
	}
	public String getCareOf() {
		return careOf;
	}
	public void setCareOf(String careOf) {
		this.careOf = careOf;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getEmailAddressText() {
		return emailAddressText;
	}
	public void setEmailAddressText(String emailAddressText) {
		this.emailAddressText = emailAddressText;
	}
	public String getExternalAddressId() {
		return externalAddressId;
	}
	public void setExternalAddressId(String externalAddressId) {
		this.externalAddressId = externalAddressId;
	}
	public long getExternalAddressSourceId() {
		return externalAddressSourceId;
	}
	public void setExternalAddressSourceId(long externalAddressSourceId) {
		this.externalAddressSourceId = externalAddressSourceId;
	}
	public String getExternalServiceAddressId() {
		return externalServiceAddressId;
	}
	public void setExternalServiceAddressId(String externalServiceAddressId) {
		this.externalServiceAddressId = externalServiceAddressId;
	}
	public String getMailingTypeCode() {
		return mailingTypeCode;
	}
	public void setMailingTypeCode(String mailingTypeCode) {
		this.mailingTypeCode = mailingTypeCode;
	}
	public String getMunicipalityName() {
		return municipalityName;
	}
	public void setMunicipalityName(String municipalityName) {
		this.municipalityName = municipalityName;
	}
	public String getPostOfficeBoxNumber() {
		return postOfficeBoxNumber;
	}
	public void setPostOfficeBoxNumber(String postOfficeBoxNumber) {
		this.postOfficeBoxNumber = postOfficeBoxNumber;
	}
	public String getPostalZipCode() {
		return postalZipCode;
	}
	public void setPostalZipCode(String postalZipCode) {
		this.postalZipCode = postalZipCode;
	}
	public String getProvinceStateCode() {
		return provinceStateCode;
	}
	public void setProvinceStateCode(String provinceStateCode) {
		this.provinceStateCode = provinceStateCode;
	}
	public long getRelatedAddressAssignmentId() {
		return relatedAddressAssignmentId;
	}
	public void setRelatedAddressAssignmentId(long relatedAddressAssignmentId) {
		this.relatedAddressAssignmentId = relatedAddressAssignmentId;
	}
	public String getStreetDirectionCode() {
		return streetDirectionCode;
	}
	public void setStreetDirectionCode(String streetDirectionCode) {
		this.streetDirectionCode = streetDirectionCode;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getStreetTypeCode() {
		return streetTypeCode;
	}
	public void setStreetTypeCode(String streetTypeCode) {
		this.streetTypeCode = streetTypeCode;
	}
	public String getUnitNumber() {
		return unitNumber;
	}
	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}
	public String getValidateAddressIndicator() {
		return validateAddressIndicator;
	}
	public void setValidateAddressIndicator(String validateAddressIndicator) {
		this.validateAddressIndicator = validateAddressIndicator;
	}
	public String getUnitTypeCode() {
		return unitTypeCode;
	}
	public void setUnitTypeCode(String unitTypeCode) {
		this.unitTypeCode = unitTypeCode;
	}
	public String getCivicNumber() {
		return civicNumber;
	}
	public void setCivicNumber(String civicNumber) {
		this.civicNumber = civicNumber;
	}
	public String getCivicNumberSuffix() {
		return civicNumberSuffix;
	}
	public void setCivicNumberSuffix(String civicNumberSuffix) {
		this.civicNumberSuffix = civicNumberSuffix;
	}
	public String getRuralRouteNumber() {
		return ruralRouteNumber;
	}
	public void setRuralRouteNumber(String ruralRouteNumber) {
		this.ruralRouteNumber = ruralRouteNumber;
	}
	public String getRuralRouteTypeCode() {
		return ruralRouteTypeCode;
	}
	public void setRuralRouteTypeCode(String ruralRouteTypeCode) {
		this.ruralRouteTypeCode = ruralRouteTypeCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationQualifier() {
		return stationQualifier;
	}

	public void setStationQualifier(String stationQualifier) {
		this.stationQualifier = stationQualifier;
	}

	public String getStationTypeCode() {
		return stationTypeCode;
	}

	public void setStationTypeCode(String stationTypeCode) {
		this.stationTypeCode = stationTypeCode;
	}

	public String getFleetMailOfficeName() {
		return fleetMailOfficeName;
	}

	public void setFleetMailOfficeName(String fleetMailOfficeName) {
		this.fleetMailOfficeName = fleetMailOfficeName;
	}

	public String getHmcsName() {
		return hmcsName;
	}

	public void setHmcsName(String hmcsName) {
		this.hmcsName = hmcsName;
	}

	public long getMasterSourceId() {
		return masterSourceId;
	}

	public void setMasterSourceId(long masterSourceId) {
		this.masterSourceId = masterSourceId;
	}

	public Date getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}

	public void setLastUpdateTimeStamp(Date lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}

	public void translateAddress(Address address){
		if (address == null) {
			return;
		}

		if(address.getAddressType()!= null){ 
			String addressType = address.getAddressType();

			switch (addressType.charAt(0)) {

			case 'C':
				this.addressTypeCode = "C";
				break;
			case 'D':
				if (address.getRuralType() != null) {
					if (address.getRuralType().matches("[R|M|S]")) {
						this.addressTypeCode = "R";
						break;
					} else if (address.getRuralType().matches("[P]")) {
						this.addressTypeCode = "P";
						break;
					} else if (address.getRuralType().matches("[G]")) {
						this.addressTypeCode = "G";
						break;
					}
				}
				break;
			case 'F':
				if (address.getCountry() != null) {
					if (address.getCountry().matches("USA")) {
						this.addressTypeCode = "U";
						break;
					} else {
						this.addressTypeCode = "I";
						break;
					}
				}
			default:
				break;
			}

			switch(addressType.charAt(0)){

			case 'D':
				if(address.getRuralType() != null && address.getRuralType().matches("[R|M|S]")){
					this.additionalAddressInformation = new ArrayList();
					/** The length of additionalAddressInformation should be fixed to 3 because the EnterpriseAddress WS checks based on the order of additionalAdressInformation **/
					boolean addAdditionalAddressInfo = address.getRuralSite() != null || address.getRuralCompartment() != null || address.getRuralGroup() != null;
					if (addAdditionalAddressInfo) {
						this.additionalAddressInformation.add(address.getRuralSite()!= null ? address.getRuralSite() : new String(""));
						this.additionalAddressInformation.add(address.getRuralCompartment()!= null ? address.getRuralCompartment() : new String(""));
						this.additionalAddressInformation.add(address.getRuralGroup()!= null ? address.getRuralGroup() : new String(""));
					}
				}
				break;
			case 'F':
				this.additionalAddressInformation = new ArrayList();
				if(address.getPrimaryLine() != null) this.additionalAddressInformation.add(address.getPrimaryLine());
				if(address.getSecondaryLine() != null) this.additionalAddressInformation.add(address.getSecondaryLine());
				break;
			default:
				break;
			}

			if(this.addressTypeCode != null && !this.addressTypeCode.equals("P") && address.getRuralType() != null ){ //do only if it is not PO BOX
				RuralTypeInfo[] ruralTypeInfo = RuralTypeInfo.getAll();
				for(int i=0; i < ruralTypeInfo.length; i++){
					if(ruralTypeInfo[i].getCode().equals(address.getRuralType())){
						this.ruralRouteTypeCode = ruralTypeInfo[i].getShortDescription();break;
					}
				}
			}

		} // if AddressType is not null

		if(address.getAttention() != null) this.careOf = address.getAttention();
		if(address.getCountry() != null) this.countryCode = address.getCountry();
		if(address.getCity() != null) this.municipalityName = address.getCity();
		if(address.getPoBox() != null) this.postOfficeBoxNumber = address.getPoBox();
		
		if (address.getCountry()!=null && address.getCountry().matches("CAN")) {
			if(address.getPostalCode() != null) this.postalZipCode = address.getPostalCode();
			if(address.getProvince() != null)  this.provinceStateCode = address.getProvince();
		} else {
			String zipGeoCode  = ((AddressInfo)address).getZipGeoCode();
			String foreignStateCode = ((AddressInfo)address).getForeignState();
			this.postalZipCode = zipGeoCode != null? zipGeoCode:address.getPostalCode();
			this.provinceStateCode = foreignStateCode !=null? foreignStateCode:address.getProvince();
		}
		
		if(address.getStreetDirection() != null) this.streetDirectionCode = address.getStreetDirection();
		if(address.getStreetName() != null) this.streetName = address.getStreetName();
		if(address.getStreetType() != null) this.streetTypeCode = address.getStreetType();
		if(address.getUnit() != null) this.unitNumber = address.getUnit();
		if(address.getUnitType() != null) this.unitTypeCode = address.getUnitType();
		if(address.getStreetNumber() != null) this.civicNumber = address.getStreetNumber();
		if(address.getStreetNumberSuffix() != null) this.civicNumberSuffix = address.getStreetNumberSuffix();
		if(address.getRuralNumber() != null) this.ruralRouteNumber = address.getRuralNumber();
		if(address.getRuralLocation() != null) this.stationName = address.getRuralLocation();
		if(address.getRuralQualifier() != null) this.stationQualifier = address.getRuralQualifier();
		if(address.getRuralDeliveryType() != null) this.stationTypeCode = address.getRuralDeliveryType();

		this.mailingTypeCode ="M";
		this.addressAssignmentTypeCode = "M";
		this.masterSourceId = 130L;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("EnterpriseAddressInfo:{\n");
		s.append("    addressId=[").append(addressId).append("]\n");
		s.append("    addressTypeCode=[").append(addressTypeCode).append("]\n");
		s.append("    additionalAddressInformation=[").append(additionalAddressInformation).append("]\n");
		s.append("    renderedAddress=[").append(renderedAddress).append("]\n");
		s.append("    addrAssgnmtId=[").append(addrAssgnmtId).append("]\n");
		s.append("    addressAssignmentSubTypeCode=[").append(addressAssignmentSubTypeCode).append("]\n");
		s.append("    addressAssignmentTypeCode=[").append(addressAssignmentTypeCode).append("]\n");
		s.append("    addressMatchingStatusCode=[").append(addressMatchingStatusCode).append("]\n");
		s.append("    addressSearchText=[").append(addressSearchText).append("]\n");
		s.append("    canadaPostBuildName=[").append(canadaPostBuildName).append("]\n");
		s.append("    canadaPostLocnName=[").append(canadaPostLocnName).append("]\n");
		s.append("    canadaPostRecordType=[").append(canadaPostRecordType).append("]\n");
		s.append("    careOf=[").append(careOf).append("]\n");
		s.append("    countryCode=[").append(countryCode).append("]\n");
		s.append("    emailAddressText=[").append(emailAddressText).append("]\n");
		s.append("    externalAddressId=[").append(externalAddressId).append("]\n");
		s.append("    externalAddressSourceId=[").append(externalAddressSourceId).append("]\n");
		s.append("    externalServiceAddressId=[").append(externalServiceAddressId).append("]\n");
		s.append("    mailingTypeCode=[").append(mailingTypeCode).append("]\n");
		s.append("    municipalityName=[").append(municipalityName).append("]\n");
		s.append("    postOfficeBoxNumber=[").append(postOfficeBoxNumber).append("]\n");
		s.append("    postalZipCode=[").append(postalZipCode).append("]\n");
		s.append("    provinceStateCode=[").append(provinceStateCode).append("]\n");
		s.append("    relatedAddressAssignmentId=[").append(relatedAddressAssignmentId).append("]\n");
		s.append("    streetDirectionCode=[").append(streetDirectionCode).append("]\n");
		s.append("    streetName=[").append(streetName).append("]\n");
		s.append("    streetTypeCode=[").append(streetTypeCode).append("]\n");
		s.append("    unitNumber=[").append(unitNumber).append("]\n");
		s.append("    validateAddressIndicator=[").append(validateAddressIndicator).append("]\n");
		s.append("    unitTypeCode=[").append(unitTypeCode).append("]\n");
		s.append("    civicNumber=[").append(civicNumber).append("]\n");
		s.append("    civicNumberSuffix=[").append(civicNumberSuffix).append("]\n");
		s.append("    ruralRouteNumber=[").append(ruralRouteNumber).append("]\n");
		s.append("    ruralRouteTypeCode=[").append(ruralRouteTypeCode).append("]\n");
		s.append("    stationName=[").append(stationName).append("]\n");
		s.append("    stationQualifier=[").append(stationQualifier).append("]\n");
		s.append("    stationTypeCode=[").append(stationTypeCode).append("]\n");
		s.append("    fleetMailOfficeName=[").append(fleetMailOfficeName).append("]\n");
		s.append("    hmcsName=[").append(hmcsName).append("]\n");
		s.append("    masterSourceId=[").append(masterSourceId).append("]\n");
		s.append("    lastUpdateTimeStamp=[").append(lastUpdateTimeStamp).append("]\n");
		s.append("}");

		return s.toString();
	}

}
