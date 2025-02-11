/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.ArrayList;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Address;
import com.telus.api.account.EnterpriseAddress;
import com.telus.api.account.InvalidAddressException;
import com.telus.api.util.SessionUtil;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.EnterpriseAddressInfo;
import com.telus.eas.framework.info.Info;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.Logger;


public class TMAddress extends BaseProvider implements Address {
	
	private static final long serialVersionUID = 1L;

	/**
	 * @link aggregation
	 */
	private final AddressInfo delegate;
	
	private AccountLifecycleManager accountLifecycleManager;

	public TMAddress(TMProvider provider, AddressInfo delegate) {
		super(provider);
		this.delegate = delegate;
	    accountLifecycleManager = provider.getAccountLifecycleManager();
	}
	
	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public void clear() {
		delegate.clear();
	}

	public void normalize() {
		delegate.normalize();
	}

	public String getAttention() {
		return delegate.getAttention();
	}

	public void setAttention(String newAttention) {
		delegate.setAttention(newAttention);
	}

	public String getStreetNumber() {
		return delegate.getStreetNumber();
	}

	public void setStreetNumber(String streetNumber) {
		delegate.setStreetNumber(streetNumber);
	}

	public String getStreetName() {
		return delegate.getStreetName();
	}

	public void setStreetName(String streetName) {
		delegate.setStreetName(streetName);
	}
	
	public String getStreetType() {
		return delegate.getStreetType();
	}
	
	public void setStreetType(String newStreetType) {
		delegate.setStreetType(newStreetType);
	}
	
	public String getStreetDirection() {
		return delegate.getStreetDirection();
	}
	
	public void setStreetDirection(String newStreetDirection) {
		delegate.setStreetDirection(newStreetDirection);
	}
	
	public String getStreetNumberSuffix() {
		return delegate.getStreetNumberSuffix();
	}
	
	public void setStreetNumberSuffix(String streetNumberSuffix) {
		delegate.setStreetNumberSuffix(streetNumberSuffix);
	}

	public String getUnitType() {
		return delegate.getUnitType();
	}

	public void setUnitType(String unitType) {
		delegate.setUnitType(unitType);
	}

	public String getUnit() {
		return delegate.getUnit();
	}

	public void setUnit(String unit) {
		delegate.setUnit(unit);
	}

	public String getPoBox() {
		return delegate.getPoBox();
	}

	public void setPoBox(String poBox) {
		delegate.setPoBox(poBox);
	}

	public String getRr() {
		return delegate.getRr();
	}

	public void setRr(String rr) {
		delegate.setRr(rr);
	}

	public String getCity() {
		return delegate.getCity();
	}

	public void setCity(String city) {
		delegate.setCity(city);
	}

	public String getProvince() {
		return delegate.getProvince0();
	}

	public void setProvince(String province) {
		delegate.setProvince0(province);
	}

	public String getPostalCode() {
		return delegate.getPostalCode();
	}

	public void setPostalCode(String postalCode) {
		delegate.setPostalCode(postalCode);
	}

	public String getCountry() {
		return delegate.getCountry();
	}

	public void setCountry(String country) {
		delegate.setCountry0(country);
	}

	public String getAddressType() {
		return delegate.getAddressType();
	}

	public void setAddressType(String newAddressType) {
		delegate.setAddressType(newAddressType);
	}

	//------------------------------------------------------------------

	public String getPrimaryLine() {
		return delegate.getPrimaryLine();
	}

	public void setPrimaryLine(String newPrimaryLine) {
		delegate.setPrimaryLine(newPrimaryLine);
	}

	public String getSecondaryLine() {
		return delegate.getSecondaryLine();
	}

	public void setSecondaryLine(String newSecondaryLine) {
		delegate.setSecondaryLine(newSecondaryLine);
	}


	public String getRuralType() {
		return delegate.getRuralType();
	}

	public void setRuralType(String ruralType) {
		delegate.setRuralType(ruralType);
	}

	public String getRuralQualifier() {
		return delegate.getRuralQualifier();
	}

	public void setRuralQualifier(String newRuralQualifier) {
		delegate.setRuralQualifier(newRuralQualifier);
	}

	public String getRuralNumber() {
		return delegate.getRuralNumber();
	}

	public void setRuralNumber(String ruralNumber) {
		delegate.setRuralNumber(ruralNumber);
	}

	public String getRuralLocation() {
		return delegate.getRuralLocation();
	}

	public void setRuralLocation(String ruralLocation) {
		delegate.setRuralLocation(ruralLocation);
	}

	public void setRrDeliveryType(String newRrDeliveryType) {
		delegate.setRrDeliveryType(newRrDeliveryType);
	}

	public String getRuralDeliveryType() {
		return delegate.getRuralDeliveryType();
	}

	public void setRuralDeliveryType(String newRuralDeliveryType) {
		delegate.setRuralDeliveryType(newRuralDeliveryType);
	}

	public String getRuralSite() {
		return delegate.getRuralSite();
	}

	public void setRuralSite(String newRuralSite) {
		delegate.setRuralSite(newRuralSite);
	}

	public String getRuralCompartment() {
		return delegate.getRuralCompartment();
	}

	public void setRuralCompartment(String newRuralCompartment) {
		delegate.setRuralCompartment(newRuralCompartment);
	}

	public String getRuralGroup() {
		return delegate.getRuralGroup();
	}

	public void setRuralGroup(String newRuralGroup) {
		delegate.setRuralGroup(newRuralGroup);
	}

	//------------------------------------------------------------------

	public void copyFrom(Address o) {

		//Fix for defect # 119336 to address call from UpdateCreditProfileActivity (may pass in AddressInfo object and need to avoid ClassCastException)
		if (o instanceof AddressInfo){
			delegate.copyFrom(o);
		} else {
			delegate.copyFrom(((TMAddress)o).getDelegate());
		}
	}

	public boolean shallowEquals(Address o) {
		return delegate.shallowEquals(((TMAddress)o).getDelegate());
	}

	public boolean equals(Address o) {
		return delegate.equals(((TMAddress)o).getDelegate());
	}

	public boolean equals(Object o) {
		return delegate.equals(o);
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public String[] getFullAddress() {
		return getFullAddress0(false);
	}

	public String[] getFullAddress(boolean includeAttentionLine) {
		return getFullAddress0(includeAttentionLine);
	}

	private String getCountryDescription(String code) {
		try {
			return provider.getReferenceDataManager().getCountry(code).getDescription();
		} catch (Throwable e) {
			Logger.warning("delegate.getCountry()=[" + delegate.getCountry() + "]");
			Logger.warning(e);
		}
		return null;
	}


	private String getRuralTypeDescription(String code) {
		try {
			return provider.getReferenceDataManager().getRuralType(code).getShortDescription();
		} catch (Throwable e) {
			Logger.warning("delegate.getRuralDeliveryType()=[" + delegate.getRuralDeliveryType() + "]");
			Logger.warning(e);
		}
		return null;
	}


	public String[] getFullAddress0(boolean includeAttentionLine) {

		List list = new ArrayList(6);
		String primaryLine = "";
		String secondaryLine = "";

		//------------------------------------------------------------------
		//  Attention line
		//------------------------------------------------------------------
		if (includeAttentionLine && !Info.isEmpty(delegate.getAttention())) {
			list.add("ATTN: " + delegate.getAttention());
		}

		//------------------------------------------------------------------
		//  Address lines: Primary, Civic part
		//------------------------------------------------------------------
		if (ADDRESS_TYPE_CITY.equals(delegate.getAddressType()) || ADDRESS_TYPE_RURAL.equals(delegate.getAddressType())) {

			boolean isQuebec = "PQ".equalsIgnoreCase(delegate.getProvince()) || "QC".equalsIgnoreCase(delegate.getProvince());

			primaryLine +=
				Info.nullToString(delegate.getStreetNumber()) +
				Info.nullToString(delegate.getStreetNumberSuffixPrefix(), delegate.getStreetNumberSuffix());

			if (isQuebec) {
				primaryLine +=
					Info.nullToString(" ", delegate.getStreetType()) +
					Info.nullToString(" ", delegate.getStreetDirection()) +
					Info.nullToString(" ", delegate.getStreetName());
			} else {
				primaryLine +=
					Info.nullToString(" ", delegate.getStreetName()) +
					Info.nullToString(" ", delegate.getStreetType()) +
					Info.nullToString(" ", delegate.getStreetDirection());
			}

			primaryLine +=
				Info.nullToString(Info.nullToString(" ", delegate.getUnitType()) + " ", delegate.getUnit());



			//------------------------------------------------------------------
			//  Address lines: Primary, Rural part
			//------------------------------------------------------------------
			if (ADDRESS_TYPE_RURAL.equals(delegate.getAddressType())) {
				// TODO: rural info

				primaryLine +=
					Info.nullToString(" ", getRuralTypeDescription(delegate.getRuralType())) +
					Info.nullToString(" ", delegate.getRuralNumber()) +
					Info.nullToString(" ", delegate.getRuralLocation()) +
					Info.nullToString(" ", delegate.getRuralDeliveryType()) +
					Info.nullToString(" ", delegate.getRuralQualifier());


				//------------------------------------------------------------------
				//  Address lines: Secondary, Rural part
				//------------------------------------------------------------------
				secondaryLine +=
					Info.nullToString(" SITE ", delegate.getRuralSite()) +
					Info.nullToString(" BOX ", delegate.getPoBox()) +
					Info.nullToString(" COMPARTMENT ", delegate.getRuralCompartment()) +
					Info.nullToString(" GROUP ", delegate.getRuralGroup());
			}
		} else {
			//------------------------------------------------------------------
			//  Address lines: Primary & Secondary, Foreign part
			//------------------------------------------------------------------
			primaryLine = delegate.getPrimaryLine();
			secondaryLine = delegate.getSecondaryLine();
		}


		//------------------------------------------------------------------
		//  Add primary & secondary lines to list
		//------------------------------------------------------------------
		if (!Info.isEmpty(primaryLine)) {
			list.add(primaryLine.trim());
		}

		if (!Info.isEmpty(secondaryLine)) {
			list.add(secondaryLine.trim());
		}

		//------------------------------------------------------------------
		//  Add subsequent lines
		//------------------------------------------------------------------
		list.add(Info.nullToString(delegate.getCity()) + Info.nullToString(" ", delegate.getProvince0()));
		list.add(Info.nullToString(delegate.getPostalCode()));
		list.add(Info.nullToString(getCountryDescription(delegate.getCountry())));

		return (String[]) list.toArray(
				new String[list.size()]);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public void validate() throws TelusAPIException, InvalidAddressException {

		boolean EnterpriseAddress_ROLLBACK = AppConfiguration.getEnterpriseAddressRollback();

		if(!EnterpriseAddress_ROLLBACK){
			if (getCountry() != null && (getCountry().trim().toUpperCase().startsWith("CA"))) {

				AddressValidationResultInfo a = null;
				try {
					a = provider.getAccountLifecycleFacade().validateAddress(delegate);
				} catch (Throwable e) {
					provider.getExceptionHandler().handleException(e);
				}

				if (!a.isValidAddressInd() && a.getVerifiedAddress().isEmpty()) {
				      throw new InvalidAddressException("N-Code has no suggestion", this, null);
				} else if (!a.isValidAddressInd()) {
				      throw new InvalidAddressException("N-Code has a suggestion", this, new TMAddress(provider, a.getVerifiedAddress()));
				}

			}
		}else{

			if (getCountry() != null && (getCountry().trim().toUpperCase().startsWith("CA"))) {

				AddressValidationResultInfo a=null;
				try {
					a = accountLifecycleManager.validateAddress(delegate, SessionUtil.getSessionId(accountLifecycleManager));
				} catch (Throwable e) {
					provider.getExceptionHandler().handleException(e);
				}

				Logger.debug("Address Validation ResultInfo [" + a + "]");
				if (!a.getVerifiedAddress().shallowEquals(delegate)) {
					throw new InvalidAddressException("Code 1 has a suggestion", this, new TMAddress(provider, a.getVerifiedAddress()));
				} else if (a.getCountInformationalMessages() != a.getCountValidationMessages()) {
					throw new InvalidAddressException("Code 1 has no suggestion", this, null);
				}
			}
		}
	}

	public AddressInfo getDelegate() {
		return delegate;
	}

	public void translateAddress(EnterpriseAddress enterpriseAddress){
		delegate.translateAddress(enterpriseAddress);
	}
	
	public EnterpriseAddress newEnterpriseAddress(){
		EnterpriseAddressInfo enterpriseAddressInfo = new EnterpriseAddressInfo();
		enterpriseAddressInfo.translateAddress(delegate);
		return new TMEnterpriseAddress(provider, enterpriseAddressInfo);
	}

	
}




