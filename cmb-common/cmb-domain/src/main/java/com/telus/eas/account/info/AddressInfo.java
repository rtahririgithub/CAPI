package com.telus.eas.account.info;
/**
 * Title:        AddressInfo<p>
 * Description:  The AddressInfo holds all address related attributes of an account.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Address;
import com.telus.api.account.EnterpriseAddress;
import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.RuralTypeInfo;

public class AddressInfo extends Info implements Address {

	public static final long serialVersionUID = -6971339780900767012L;

	private String addressType = ADDRESS_TYPE_CITY;
	private String attention;
	private String secondaryLine;
	private String primaryLine;
	private String city;
	private String province;
	private String postalCode;
	private String civicNo;
	private String civicNoSuffix;
	private String streetDirection;
	private String streetName;
	private String streetType;
	private String unitDesignator;
	private String unitIdentifier;
	private String rrDesignator;
	private String rrIdentifier;
	private String rrAreaNumber;
	private String rrDeliveryType;
	private String rrQualifier;
	private String rrSite;
	private String rrCompartment;
	private String rrGroup;
	private String rrBox;
	private String country;
	private String zipGeoCode;
	private String foreignState;

	public AddressInfo() {}
	
	//==========================================================================================
	// Address Type
	//==========================================================================================
	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String newAddressType) {
		addressType = toUpperCase(newAddressType);
		if (addressType == null || (!ADDRESS_TYPE_FOREIGN.equals(addressType) && !ADDRESS_TYPE_RURAL.equals(addressType))) {
			//      System.err.println("AddressInfo::setAddressType(\"" + newAddressType + "\")  --invalid type, changing to \"" + ADDRESS_TYPE_FOREIGN + "\"");
			addressType = ADDRESS_TYPE_CITY;
		}
	}

	//==========================================================================================
	// Primary & Secondary Lines
	//==========================================================================================
	public String getPrimaryLine() {
		return primaryLine;
	}

	public void setPrimaryLine(String newPrimaryLine) {
		primaryLine = toUpperCase(newPrimaryLine);
	}

	public String getSecondaryLine() {
		return secondaryLine;
	}

	public void setSecondaryLine(String newSecondaryLine) {
		secondaryLine = toUpperCase(newSecondaryLine);
	}

	//==========================================================================================
	// Attention
	//==========================================================================================
	public String getAttention() {
		return attention;
	}

	public void setAttention(String newAttention) {
		attention = toUpperCase(newAttention);
	}

	//==========================================================================================
	// Foreign
	//==========================================================================================
	public String getZipGeoCode() {
		return zipGeoCode;
	}

	public void setZipGeoCode(String newZipGeoCode) {
		zipGeoCode = toUpperCase(newZipGeoCode);
	}

	public String getForeignState() {
		if (!ADDRESS_TYPE_FOREIGN.equals(addressType) && "NL".equals(foreignState)) {
			return "NF";
		}else {
			return foreignState;
		}
	}

	public void setForeignState(String newForeignState) {
		if (("NF").equals(newForeignState)) { 
			foreignState = toUpperCaseAndSetAddressType(foreignState, "NL", ADDRESS_TYPE_FOREIGN);
		}else {
			foreignState = toUpperCaseAndSetAddressType(foreignState, newForeignState, ADDRESS_TYPE_FOREIGN);
		}
	}

	//==========================================================================================
	// City
	//==========================================================================================
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String newPostalCode) {
		postalCode = toUpperCase(newPostalCode);
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String newCountry) {
		country = toUpperCase(newCountry);
	}

	public void setCountry0(String newCountry) {
		setCountry(newCountry);
		handleInternationalAddresses();
	}

	public String getProvince() {
		if ("NL".equals(province)) {
			return "NF";
		}else {
			return province;
		}
	}

	public String getProvince0() {
		if(province == null) {
			return foreignState;
		}
		return getProvince();
	}

	public void setProvince(String newProvince) {
		if ("NF".equals(newProvince)) {
			province = "NL";
		}else {
			province = toUpperCase(newProvince);
		}
	}

	public void setProvince0(String newProvince) {
		setProvince(newProvince);
		setForeignState(newProvince);
		handleInternationalAddresses();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String newCity) {
		city = toUpperCase(newCity);
	}

	public String getUnitDesignator() {
		return unitDesignator;
	}

	public void setUnitDesignator(String newUnitDesignator) {
		unitDesignator = toUpperCase(newUnitDesignator);
	}

	public String getUnitIdentifier() {
		return unitIdentifier;
	}

	public void setUnitIdentifier(String newUnitIdentifier) {
		unitIdentifier = toUpperCase(newUnitIdentifier);
	}

	public String getUnitType() {
		return unitDesignator;
	}

	public void setUnitType(String unitType) {
		setUnitDesignator(unitType);
	}

	public String getUnit() {
		return unitIdentifier;
	}

	public void setUnit(String suite) {
		setUnitIdentifier(suite);
	}

	public String getCivicNoSuffix() {
		return civicNoSuffix;
	}

	public void setCivicNoSuffix(String newCivicNoSuffix) {
		civicNoSuffix = toUpperCase(newCivicNoSuffix);
	}

	public String getCivicNo() {
		return civicNo;
	}

	public void setCivicNo(String newCivicNo) {
		civicNo = toUpperCase(newCivicNo);
	}

	public String getStreetDirection() {
		return streetDirection;
	}

	public void setStreetDirection(String newStreetDirection) {
		streetDirection = toUpperCase(newStreetDirection);
	}

	public String getStreetType() {
		return streetType;
	}

	public void setStreetType(String newStreetType) {
		streetType = toUpperCase(newStreetType);
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String newStreetName) {
		streetName = toUpperCase(newStreetName);
		if(streetName != null && !streetName.equals("") && streetName.charAt(streetName.length()-1) == '.') {
			streetName = streetName.substring(0, streetName.length()-1);
		}
	}

	public String getStreetNumberSuffix() {
		return getCivicNoSuffix();
	}

	public void setStreetNumberSuffix(String streetNumberSuffix) {
		setCivicNoSuffix(streetNumberSuffix);
	}

	public String getStreetNumber() {
		return civicNo;
	}

	public void setStreetNumber(String streetNumber) {
		civicNo = toUpperCase(streetNumber);
	}

	//==========================================================================================
	// Rural Route
	//==========================================================================================
	public String getRrDesignator() {
		return rrDesignator;
	}
	public void setRrDesignator(String newRrDesignator) {
		if (rrDesignator != newRrDesignator) { // BUGFIX: stackoverflow
			rrDesignator = toUpperCaseAndSetAddressType(rrDesignator, newRrDesignator, ADDRESS_TYPE_RURAL);
		}
	}

	public String getRuralType() {
		return getRrDesignator();
	}

	public void setRuralType(String ruralType) {
		setRrDesignator(ruralType);
	}

	public String getRrIdentifier() {
		return rrIdentifier;
	}

	public void setRrIdentifier(String newRrIdentifier) {
		rrIdentifier = toUpperCaseAndSetAddressType(rrIdentifier, newRrIdentifier, ADDRESS_TYPE_RURAL);
	}

	public void setRrIdentifier(String newRrIdentifier, String newRrDesignator) {
		rrIdentifier = toUpperCaseAndSetAddressTypeAndRuralType(rrIdentifier, newRrIdentifier, ADDRESS_TYPE_RURAL, newRrDesignator);
	}

	public String getRuralNumber() {
		return getRrIdentifier();
	}

	public void setRuralNumber(String ruralNumber) {
		setRrIdentifier(ruralNumber);
	}

	public String getRrAreaNumber() {
		return rrAreaNumber;
	}

	public void setRrAreaNumber(String newRrAreaNumber) {
		rrAreaNumber = toUpperCaseAndSetAddressType(rrAreaNumber, newRrAreaNumber, ADDRESS_TYPE_RURAL);
	}

	public void setRrAreaNumber(String newRrAreaNumber, String newRrDesignator) {
		rrAreaNumber = toUpperCaseAndSetAddressTypeAndRuralType(rrAreaNumber, newRrAreaNumber, ADDRESS_TYPE_RURAL, newRrDesignator);
	}

	public String getRuralLocation() {
		return getRrAreaNumber();
	}

	public void setRuralLocation(String ruralLocation) {
		setRrAreaNumber(ruralLocation);
	}

	public String getRrDeliveryType() {
		return rrDeliveryType;
	}

	public void setRrDeliveryType(String newRrDeliveryType) {
		rrDeliveryType = toUpperCaseAndSetAddressType(rrDeliveryType, newRrDeliveryType, ADDRESS_TYPE_RURAL);
	}

	public String getRuralDeliveryType() {
		return getRrDeliveryType();
	}

	public void setRuralDeliveryType(String newRuralDeliveryType) {
		setRrDeliveryType(newRuralDeliveryType);
	}

	//  public boolean isGeneralDelivery() {
		//    return rrDesignator != null && rrDesignator.equals(RR_DESIGNATOR_GENERAL_DELIVERY);
	//  }
	//
	//  public void setGeneralDelivery(boolean newGeneralDelivery) {
	//    if (newGeneralDelivery) {
	//        setRrDesignator(RR_DESIGNATOR_GENERAL_DELIVERY);
	//    } else if (isGeneralDelivery()) {
	//      setRrDesignator(null);
	//    }
	//  }

	public String getRrQualifier() {
		return rrQualifier;
	}

	public void setRrQualifier(String newRrQualifier) {
		rrQualifier = toUpperCaseAndSetAddressType(rrQualifier, newRrQualifier, ADDRESS_TYPE_RURAL);
	}

	public String getRuralQualifier() {
		return getRrQualifier();
	}

	public void setRuralQualifier(String newRuralQualifier) {
		setRrQualifier(newRuralQualifier);
	}

	public String getRrSite() {
		return rrSite;
	}

	public void setRrSite(String newRrSite) {
		rrSite = toUpperCaseAndSetAddressType(rrSite, newRrSite, ADDRESS_TYPE_RURAL);
	}

	public String getRuralSite() {
		return getRrSite();
	}

	public void setRuralSite(String newRuralSite) {
		setRrSite(newRuralSite);
	}

	public String getRrCompartment() {
		return rrCompartment;
	}

	public void setRrCompartment(String newRrCompartment) {
		rrCompartment = toUpperCaseAndSetAddressType(rrCompartment, newRrCompartment, ADDRESS_TYPE_RURAL);
	}

	public String getRuralCompartment() {
		return getRrCompartment();
	}

	public void setRuralCompartment(String newRuralCompartment) {
		setRrCompartment(newRuralCompartment);
	}

	public String getRrGroup() {
		return rrGroup;
	}

	public void setRrGroup(String newRrGroup) {
		rrGroup = toUpperCaseAndSetAddressType(rrGroup, newRrGroup, ADDRESS_TYPE_RURAL);
	}

	public String getRuralGroup() {
		return getRrGroup();
	}

	public void setRuralGroup(String newRuralGroup) {
		setRrGroup(newRuralGroup);
	}

	public String getRrBox() {
		return rrBox;
	}

	public void setRrBox(String newRrBox) {
		// We do not want to change the RR designator if it is already set to something else.
		// Otherwise, set it to RURAL_TYPE_PO_BOX. 
		if (!isEmpty(rrDesignator)) {
			rrBox = toUpperCaseAndSetAddressType(rrBox, newRrBox, ADDRESS_TYPE_RURAL);
		} else {
			rrBox = toUpperCaseAndSetAddressTypeAndRuralType(rrBox, newRrBox, ADDRESS_TYPE_RURAL, RURAL_TYPE_PO_BOX);
		}
	}

	public String getPoBox() {
		return rrBox;
	}

	public void setPoBox(String poBox) {
		this.setRrBox(poBox);
	}

	public String getRr() {
		return rrAreaNumber;
	}

	public void setRr(String rr) {
		this.setRrAreaNumber(rr, RURAL_TYPE_RURAL_ROUTE);
	}

	//==========================================================================================
	// Rural Route: API
	//==========================================================================================

	//==========================================================================================
	// Misc.
	//==========================================================================================
	
	public String[] getFullAddress() {
		return getFullAddress(false);
	}

	public String[] getFullAddress(boolean includeAttentionLine) {

		List list = new ArrayList(6);
		String primaryLine = "";
		String secondaryLine = "";

		//------------------------------------------------------------------
		//  Attention line
		//------------------------------------------------------------------
		if (includeAttentionLine && !isEmpty(getAttention())) {
			list.add("ATTN: " + getAttention());
		}

		//------------------------------------------------------------------
		//  Address lines: Primary, Civic part
		//------------------------------------------------------------------
		if (ADDRESS_TYPE_CITY.equals(getAddressType()) || ADDRESS_TYPE_RURAL.equals(getAddressType())) {

			boolean isQuebec = "PQ".equalsIgnoreCase(getProvince()) || "QC".equalsIgnoreCase(getProvince());

			primaryLine +=
				nullToString(getStreetNumber()) +
				nullToString(getStreetNumberSuffixPrefix(), getStreetNumberSuffix());

			if (isQuebec) {
				primaryLine +=
					nullToString(" ", getStreetType()) +
					nullToString(" ", getStreetDirection()) +
					nullToString(" ", getStreetName());
			} else {
				primaryLine +=
					nullToString(" ", getStreetName()) +
					nullToString(" ", getStreetType()) +
					nullToString(" ", getStreetDirection());
			}

			primaryLine += nullToString(nullToString(" ", getUnitType()) + " ", getUnit());   

			//------------------------------------------------------------------
			//  Address lines: Primary, Rural part
			//------------------------------------------------------------------
			if (ADDRESS_TYPE_RURAL.equals(getAddressType())) {
				// TODO: rural info
				primaryLine +=
					nullToString(" ", (getRuralType() == null ? null :
						getRuralType().equals(RURAL_TYPE_RURAL_ROUTE) ? "RR" :
							getRuralType().equals(RURAL_TYPE_PO_BOX) ? "PO BOX" :
								getRuralType().equals(RURAL_TYPE_GENERAL_DELIVERY) ? "GD" :
									getRuralType().equals(RURAL_TYPE_STATION_MAIN) ? "MR" :
										getRuralType().equals(RURAL_TYPE_TOWNSHIP) ? "SS" :
											null)) +
											nullToString(" ", getRuralNumber()) +
											nullToString(" ", getRuralLocation()) +
											nullToString(" ", getRuralDeliveryType()) +
											nullToString(" ", getRuralQualifier());

				//------------------------------------------------------------------
				//  Address lines: Secondary, Rural part
				//------------------------------------------------------------------
				secondaryLine +=
					nullToString(" SITE ", getRuralSite()) +
					nullToString(" BOX ", getPoBox()) +
					nullToString(" COMPARTMENT ", getRuralCompartment()) +
					nullToString(" GROUP ", getRuralGroup());
			}
		} else {
			//------------------------------------------------------------------
			//  Address lines: Primary & Secondary, Foreign part
			//------------------------------------------------------------------
			primaryLine = getPrimaryLine();
			secondaryLine = getSecondaryLine();
		}      

		//------------------------------------------------------------------
		//  Add primary & secondary lines to list
		//------------------------------------------------------------------
		if (!isEmpty(primaryLine)) {
			list.add(primaryLine.trim());
		}

		if (!isEmpty(secondaryLine)) {
			list.add(secondaryLine.trim());
		}

		//------------------------------------------------------------------
		//  Add subsequent lines
		//------------------------------------------------------------------
		list.add(nullToString(getCity()) + nullToString(" ", getProvince0()));
		list.add(nullToString(getPostalCode()));
		list.add(nullToString(getCountry()));

		return (String[]) list.toArray(new String[list.size()]);
	}

	private void handleInternationalAddresses() {

		if (!isEmpty(country) && !country.startsWith("CA")) {  // Foreign country
			addressType = ADDRESS_TYPE_FOREIGN;

			province = null;  // provinces are set for Canada, US, and Mexico; but we'll only set Canada.

			//---------------------------------------------
			// Setup the primary and secondary lines
			//---------------------------------------------
			String[] fullAddress = getFullAddress();

			if(fullAddress.length > 1) {
				primaryLine = fullAddress[0];
				secondaryLine = fullAddress[1];
			} else if(fullAddress.length > 0) {
				primaryLine = fullAddress[0];
			}
		} else { // Canada or country is not yet set

			if (isEmpty(province)) {
				province = foreignState;
			}

			if (addressType == ADDRESS_TYPE_FOREIGN) {
				if (!isEmpty(rrAreaNumber) ||
						!isEmpty(rrDeliveryType) ||
						!isEmpty(rrQualifier) ||
						!isEmpty(rrSite) ||
						!isEmpty(rrCompartment) ||
						!isEmpty(rrGroup) ||
						!isEmpty(rrBox)) {
					addressType = ADDRESS_TYPE_RURAL;
				} else {
					addressType = ADDRESS_TYPE_CITY;
				}
			}
		}
	}

	public void validate() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void clear() {
		
		addressType = ADDRESS_TYPE_CITY;
		attention = null;
		secondaryLine = null;
		primaryLine = null;
		city = null;
		province = null;
		postalCode = null;
		civicNo = null;
		civicNoSuffix = null;
		streetDirection = null;
		streetName = null;
		streetType = null;
		unitDesignator = null;
		unitIdentifier = null;
		rrDesignator = null;
		rrIdentifier = null;
		rrAreaNumber = null;
		rrDeliveryType = null;
		rrQualifier = null;
		rrSite = null;
		rrCompartment = null;
		rrGroup = null;
		rrBox = null;
		country = null;
		zipGeoCode = null;
		foreignState = null;
	}

	public void normalize() {
		
		if (ADDRESS_TYPE_CITY.equals(addressType)) {
			secondaryLine = null;
			primaryLine = null;
			rrDesignator = null;
			rrIdentifier = null;
			rrAreaNumber = null;
			rrDeliveryType = null;
			rrQualifier = null;
			rrSite = null;
			rrCompartment = null;
			rrGroup = null;
			rrBox = null;
			foreignState = null;
		} else if (ADDRESS_TYPE_RURAL.equals(addressType)) {
			secondaryLine = null;
			primaryLine = null;
			streetDirection = null;
			unitDesignator = null;
			unitIdentifier = null;
			foreignState = null;
		} else if (ADDRESS_TYPE_FOREIGN.equals(addressType)) {
			civicNo = null;
			civicNoSuffix = null;
			streetDirection = null;
			streetName = null;
			streetType = null;
			unitDesignator = null;
			unitIdentifier = null;
			rrDesignator = null;
			rrIdentifier = null;
			rrAreaNumber = null;
			rrDeliveryType = null;
			rrQualifier = null;
			rrSite = null;
			rrCompartment = null;
			rrGroup = null;
			rrBox = null;
		}
		
	}

	public void copyFrom(Address o) {
		
		AddressInfo a = (AddressInfo)o;
		if (a != null) {
			addressType      = toUpperCase(a.addressType);
			attention        = toUpperCase(a.attention);
			secondaryLine    = toUpperCase(a.secondaryLine);
			primaryLine      = toUpperCase(a.primaryLine);
			city             = toUpperCase(a.city);
			province         = toUpperCase(a.province);
			postalCode       = toUpperCase(a.postalCode);
			civicNo          = toUpperCase(a.civicNo);
			civicNoSuffix    = toUpperCase(a.civicNoSuffix);
			streetDirection  = toUpperCase(a.streetDirection);
			streetName       = toUpperCase(a.streetName);
			streetType       = toUpperCase(a.streetType);
			unitDesignator   = toUpperCase(a.unitDesignator);
			unitIdentifier   = toUpperCase(a.unitIdentifier);
			rrDesignator     = toUpperCase(a.rrDesignator);
			rrIdentifier     = toUpperCase(a.rrIdentifier);
			rrAreaNumber     = toUpperCase(a.rrAreaNumber);
			rrDeliveryType   = toUpperCase(a.rrDeliveryType);
			rrQualifier      = toUpperCase(a.rrQualifier);
			rrSite           = toUpperCase(a.rrSite);
			rrCompartment    = toUpperCase(a.rrCompartment);
			rrGroup          = toUpperCase(a.rrGroup);
			rrBox            = toUpperCase(a.rrBox);
			country          = toUpperCase(a.country);
			zipGeoCode       = toUpperCase(a.zipGeoCode);
			foreignState     = toUpperCase(a.foreignState);
		}
	}

	public boolean shallowEquals(Address o) {
		
		if(!(o instanceof AddressInfo)) {
			return false;
		}

		AddressInfo a = (AddressInfo)o;

		return compare(city            , a.city) &&
		compare(province        , a.province) &&
		compare(postalCode      , a.postalCode) &&
		compare(civicNo         , a.civicNo) &&
		compare(civicNoSuffix   , a.civicNoSuffix) &&
		compare(streetName + 
				((streetType == null) ? "" : " " + streetType), a.streetName + ((a.streetType == null) ? "" : " " + a.streetType)) &&
		compare(unitDesignator  , a.unitDesignator) &&
		compare(unitIdentifier  , a.unitIdentifier) &&
		compare(rrDesignator    , a.rrDesignator) &&
		compare(rrIdentifier    , a.rrIdentifier) &&
		compare(rrAreaNumber    , a.rrAreaNumber) &&
		compare(rrDeliveryType  , a.rrDeliveryType) &&
		compare(rrQualifier     , a.rrQualifier) &&
		compare(rrSite          , a.rrSite) &&
		compare(rrCompartment   , a.rrCompartment) &&
		compare(rrGroup         , a.rrGroup) &&
		compare(rrBox           , a.rrBox) &&
		compare(country         , a.country) &&
		compare(zipGeoCode      , a.zipGeoCode) &&
		compare(foreignState    , a.foreignState);
	}

	public boolean equals(Address o){
		
		if(!(o instanceof AddressInfo)) {
			return false;
		}

		AddressInfo a = (AddressInfo)o;

		return  compare(attention       , a.attention) &&
		compare(secondaryLine   , a.secondaryLine) &&
		compare(primaryLine     , a.primaryLine) &&
		compare(city            , a.city) &&
		compare(province        , a.province) &&
		compare(postalCode      , a.postalCode) &&
		compare(civicNo         , a.civicNo) &&
		compare(civicNoSuffix   , a.civicNoSuffix) &&
		compare(streetDirection , a.streetDirection) &&
		compare(streetName      , a.streetName) &&
		compare(streetType      , a.streetType) &&
		compare(unitDesignator  , a.unitDesignator) &&
		compare(unitIdentifier  , a.unitIdentifier) &&
		compare(rrDesignator    , a.rrDesignator) &&
		compare(rrIdentifier    , a.rrIdentifier) &&
		compare(rrAreaNumber    , a.rrAreaNumber) &&
		compare(rrDeliveryType  , a.rrDeliveryType) &&
		compare(rrQualifier     , a.rrQualifier) &&
		compare(rrSite          , a.rrSite) &&
		compare(rrCompartment   , a.rrCompartment) &&
		compare(rrGroup         , a.rrGroup) &&
		compare(rrBox           , a.rrBox) &&
		compare(country         , a.country) &&
		compare(zipGeoCode      , a.zipGeoCode) &&
		compare(foreignState    , a.foreignState);
	}

	public boolean equals(Object o){
		return o != null &&
		o instanceof Address &&
		equals((Address)o);
	}

	public final String toUpperCaseAndSetAddressTypeAndRuralType(String oldValue, String newValue, String newAddressType, String newRrDesignator) {

		newValue = (newValue == null)? null : newValue.trim().toUpperCase();
		oldValue = (oldValue == null)? null : oldValue.trim().toUpperCase();

		if (!isEmpty(newValue) && !compare(oldValue, newValue)) {
			setAddressType(newAddressType);
			setRrDesignator(newRrDesignator);
		}

		return newValue;
	}

	public final String toUpperCaseAndSetAddressType(String oldValue, String newValue, String newAddressType) {
		return toUpperCaseAndSetAddressTypeAndRuralType(oldValue, newValue, newAddressType, rrDesignator);
	}

	public boolean isEmpty() {
		return  attention == null &&
		secondaryLine == null &&
		primaryLine == null &&
		city == null &&
		province == null &&
		postalCode == null &&
		civicNo == null &&
		civicNoSuffix == null &&
		streetDirection == null &&
		streetName == null &&
		streetType == null &&
		unitDesignator == null &&
		unitIdentifier == null &&
		rrDesignator == null &&
		rrIdentifier == null &&
		rrAreaNumber == null &&
		rrDeliveryType == null &&
		rrQualifier == null &&
		rrSite == null &&
		rrCompartment == null &&
		rrGroup == null &&
		rrBox == null &&
		country == null &&
		zipGeoCode == null &&
		foreignState == null;
	}

	public String getStreetNumberSuffixPrefix() {
		if (!isEmpty(civicNoSuffix) && Character.isLetter(civicNoSuffix.charAt(0)))
			return "";

		return " ";
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("AddressInfo:{\n");
		//s.append("    =[").append().append("]\n");
		s.append("    addressType=[").append(addressType).append("]\n");
		s.append("    attention=[").append(attention).append("]\n");
		s.append("    secondaryLine=[").append(secondaryLine).append("]\n");
		s.append("    primaryLine=[").append(primaryLine).append("]\n");
		s.append("    city=[").append(city).append("]\n");
		s.append("    province=[").append(province).append("]\n");
		s.append("    postalCode=[").append(postalCode).append("]\n");
		s.append("    civicNo=[").append(civicNo).append("]\n");
		s.append("    civicNoSuffix=[").append(civicNoSuffix).append("]\n");
		s.append("    streetDirection=[").append(streetDirection).append("]\n");
		s.append("    streetName=[").append(streetName).append("]\n");
		s.append("    streetType=[").append(streetType).append("]\n");
		s.append("    unitDesignator=[").append(unitDesignator).append("]\n");
		s.append("    unitIdentifier=[").append(unitIdentifier).append("]\n");
		s.append("    rrDesignator=[").append(rrDesignator).append("]\n");
		s.append("    rrIdentifier=[").append(rrIdentifier).append("]\n");
		s.append("    rrAreaNumber=[").append(rrAreaNumber).append("]\n");
		s.append("    rrDeliveryType=[").append(rrDeliveryType).append("]\n");
		s.append("    rrQualifier=[").append(rrQualifier).append("]\n");
		s.append("    rrSite=[").append(rrSite).append("]\n");
		s.append("    rrCompartment=[").append(rrCompartment).append("]\n");
		s.append("    rrGroup=[").append(rrGroup).append("]\n");
		s.append("    rrBox=[").append(rrBox).append("]\n");
		s.append("    country=[").append(country).append("]\n");
		s.append("    zipGeoCode=[").append(zipGeoCode).append("]\n");
		s.append("    foreignState=[").append(foreignState).append("]\n");
		s.append("}");

		return s.toString();
	}

	public void translateAddress(EnterpriseAddress enterpriseAddress){

		if(enterpriseAddress.getRuralRouteTypeCode() != null){
			RuralTypeInfo[] ruralTypeInfo = RuralTypeInfo.getAll();
			for(int i=0; i < ruralTypeInfo.length; i++){
				if(ruralTypeInfo[i].getShortDescription().equals(enterpriseAddress.getRuralRouteTypeCode())){
					this.rrDesignator = ruralTypeInfo[i].getCode();
				}
			}
		}

		if(enterpriseAddress.getAddressTypeCode() != null){
			String addressTypeCode = enterpriseAddress.getAddressTypeCode();

			if(addressTypeCode.equals("C")){
				this.addressType = "C";
			}else if(addressTypeCode.equals("R") || addressTypeCode.equals("M") || addressTypeCode.equals("P") || addressTypeCode.equals("G")){
				this.addressType = "D";
			}else if(addressTypeCode.equals("U") || addressTypeCode.equals("I") ){
				this.addressType = "F";
			}

			if(this.rrDesignator == null){
				if(addressTypeCode.equals("C")){
					this.rrDesignator = null;
				}else if(addressTypeCode.equals("R") || addressTypeCode.equals("M")){
					this.rrDesignator = "R";
				}else if(addressTypeCode.equals("P")){
					this.rrDesignator = "P";
				}else if(addressTypeCode.equals("G")){
					this.rrDesignator = "G";
				}else if(addressTypeCode.equals("U") || addressTypeCode.equals("I") ){
					this.rrDesignator = "F";
				}
			}
			if(enterpriseAddress.getAdditionalAddressInformation() != null) {
				if(addressTypeCode.equals("R") || addressTypeCode.equals("M")){
					int addlAddressInfoSize = enterpriseAddress.getAdditionalAddressInformation().size();
					if (addlAddressInfoSize > 0) {
						this.rrSite = (String) enterpriseAddress.getAdditionalAddressInformation().get(0);
					}
					if (addlAddressInfoSize > 1) {
						this.rrCompartment = (String) enterpriseAddress.getAdditionalAddressInformation().get(1);
					}
					if (addlAddressInfoSize > 2) {
						this.rrGroup = (String) enterpriseAddress.getAdditionalAddressInformation().get(2);
					}
				}else if(addressTypeCode.equals("U") || addressTypeCode.equals("I") ){
					int addlAddressInfoSize = enterpriseAddress.getAdditionalAddressInformation().size();
					if (addlAddressInfoSize > 0) {
						this.primaryLine = (String) enterpriseAddress.getAdditionalAddressInformation().get(0);
					}
					if (addlAddressInfoSize > 1) {
						this.secondaryLine = (String) enterpriseAddress.getAdditionalAddressInformation().get(1);
					}
				}
			}
		} // if addressTypeCode is not null

		if(enterpriseAddress.getCareOf() != null) this.attention = enterpriseAddress.getCareOf();
		if(enterpriseAddress.getCountryCode() != null) this.country = enterpriseAddress.getCountryCode();
		if(enterpriseAddress.getMunicipalityName() != null) this.city = enterpriseAddress.getMunicipalityName();
		if(enterpriseAddress.getPostOfficeBoxNumber() != null) this.rrBox = enterpriseAddress.getPostOfficeBoxNumber();
		
		if(enterpriseAddress.getCountryCode()!= null && enterpriseAddress.getCountryCode().matches("CAN")){
			if(enterpriseAddress.getPostalZipCode() != null) this.postalCode = enterpriseAddress.getPostalZipCode();
			if(enterpriseAddress.getProvinceStateCode() != null) this.province = enterpriseAddress.getProvinceStateCode();
		} else {
			if(enterpriseAddress.getPostalZipCode() != null) { 
				this.zipGeoCode = enterpriseAddress.getPostalZipCode();
				this.postalCode = enterpriseAddress.getPostalZipCode();
			}
			if(enterpriseAddress.getProvinceStateCode() != null) {
				this.foreignState = enterpriseAddress.getProvinceStateCode();
				this.province = enterpriseAddress.getProvinceStateCode();
			}
		}
		
		if(enterpriseAddress.getStreetDirectionCode() != null) this.streetDirection = enterpriseAddress.getStreetDirectionCode();
		if(enterpriseAddress.getStreetName() != null) this.streetName = enterpriseAddress.getStreetName();
		if(enterpriseAddress.getStreetTypeCode() != null) this.streetType = enterpriseAddress.getStreetTypeCode();
		if(enterpriseAddress.getUnitNumber() != null) this.unitIdentifier = enterpriseAddress.getUnitNumber();
		if(enterpriseAddress.getUnitTypeCode() != null) this.unitDesignator = enterpriseAddress.getUnitTypeCode();
		if(enterpriseAddress.getCivicNumber() != null) this.civicNo = enterpriseAddress.getCivicNumber();
		if(enterpriseAddress.getCivicNumberSuffix() != null) this.civicNoSuffix = enterpriseAddress.getCivicNumberSuffix();
		if(enterpriseAddress.getRuralRouteNumber() != null) this.rrIdentifier = enterpriseAddress.getRuralRouteNumber();
		if(enterpriseAddress.getStationName() != null) this.rrAreaNumber = enterpriseAddress.getStationName();
		if(enterpriseAddress.getStationQualifier() != null) this.rrQualifier = enterpriseAddress.getStationQualifier();
		if(enterpriseAddress.getStationTypeCode() != null) this.rrDeliveryType = enterpriseAddress.getStationTypeCode();

	}

	public EnterpriseAddress newEnterpriseAddress() {
		// Method not implemented here, check TMAddress instead.
		return null;
	}

}
