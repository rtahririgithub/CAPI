/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.customer_management_common_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.AddressInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.Address;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.AddressType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.ProvinceCode;

/**
 * @author Dimitry Siganevich
 *
 */
public class AddressMapper extends AbstractSchemaMapper<Address, AddressInfo> {

	public AddressMapper(){
		super(Address.class, AddressInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Address performSchemaMapping(AddressInfo source, Address target) {
		target.setAddressType(toEnum(source.getAddressType(), AddressType.class));
		target.setAttention(source.getAttention());
		target.setPrimaryLine(source.getPrimaryLine());
		target.setSecondaryLine(source.getSecondaryLine());
		target.setCity(source.getCity());
		target.setCivicNo(source.getCivicNo());
		target.setCivicNoSuffix(source.getCivicNoSuffix());
		target.setCountry(source.getCountry());

		String country = target.getCountry();
		if (country != null && !country.trim().equals("") && country.trim().equals("CAN")) {
			if (source.getProvince() != null && !source.getProvince().trim().equals("")) {
				String province = source.getProvince();
				if (province.equals("QC"))
					province = "PQ";
				if (province.equals("NL"))
					province = "NF";
				target.setProvince(toEnum(province, ProvinceCode.class));
			}
		}
		target.setPostalCode(source.getPostalCode());
		target.setStreetNumber(source.getStreetNumber());
		target.setStreetNumberSuffix(source.getStreetNumberSuffix());
		target.setStreetDirection(source.getStreetDirection());
		target.setStreetName(source.getStreetName());
		target.setStreetType(source.getStreetType());
		target.setUnitType(source.getUnitType());
		target.setUnit(source.getUnit());
		target.setPoBox(source.getPoBox());
		target.setRuralLocation(source.getRuralLocation());
		target.setRuralQualifier(source.getRuralQualifier());
		target.setRuralSite(source.getRuralSite());
		target.setRuralCompartment(source.getRuralCompartment());
		target.setRuralDeliveryType(source.getRuralDeliveryType());
		target.setRuralGroup(source.getRuralGroup());
		target.setRuralNumber(source.getRuralNumber());
		target.setRuralType(source.getRuralType());
		target.setZipGeoCode(source.getZipGeoCode());
		target.setForeignState(source.getForeignState());
		return super.performSchemaMapping(source, target);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected AddressInfo performDomainMapping(Address source, AddressInfo target) {
		if (source.getAddressType() != null)
			target.setAddressType(source.getAddressType().value());
		target.setAttention(source.getAttention());
		target.setCity(source.getCity());
		target.setCivicNo(source.getCivicNo());
		target.setCivicNoSuffix(source.getCivicNoSuffix());
		target.setCountry(source.getCountry());
		target.setCountry0(source.getCountry());
		target.setForeignState(source.getForeignState());
		target.setPoBox(source.getPoBox());
		target.setPostalCode(source.getPostalCode());
		target.setPrimaryLine(source.getPrimaryLine());
		if (source.getProvince() != null) {
			target.setProvince(source.getProvince().value());
			target.setProvince0(source.getProvince().value());
		}
		target.setRuralCompartment(source.getRuralCompartment());
		target.setRuralDeliveryType(source.getRuralDeliveryType());
		target.setRuralGroup(source.getRuralGroup());
		target.setRuralLocation(source.getRuralLocation());
		target.setRuralNumber(source.getRuralNumber());
		target.setRuralQualifier(source.getRuralQualifier());
		target.setRuralSite(source.getRuralSite());
		target.setRuralType(source.getRuralType());
		target.setSecondaryLine(source.getSecondaryLine());
		target.setStreetDirection(source.getStreetDirection());
		target.setStreetName(source.getStreetName());
		target.setStreetNumber(source.getStreetNumber());
		target.setStreetNumberSuffix(source.getStreetNumberSuffix());
		target.setStreetType(source.getStreetType());
		target.setUnit(source.getUnit());
		target.setUnitType(source.getUnitType());
		target.setZipGeoCode(source.getZipGeoCode());

		return super.performDomainMapping(source, target);
	}
	
}
