package com.telus.cmb.endpoint.mapper;

import weblogic.wsee.util.StringUtil;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.AddressInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.Address;

public class AddressMapper extends AbstractSchemaMapper<Address, AddressInfo> {

	public AddressMapper() {
		super(Address.class, AddressInfo.class);
	}

	@Override
	protected AddressInfo performDomainMapping(Address source, AddressInfo target) {
		if (source.getAddressType() != null) {
			target.setAddressType(source.getAddressType().value());
		}
		target.setAttention(source.getAttention());
		target.setCity(source.getCity());
		target.setCountry(source.getCountry());
		target.setPoBox(source.getPoBox());
		target.setPostalCode(source.getPostalCode());
		target.setPrimaryLine(source.getPrimaryLine());
		target.setSecondaryLine(source.getSecondaryLine());
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
		target.setStreetDirection(source.getStreetDirection());
		target.setStreetName(source.getStreetName());
		target.setStreetType(source.getStreetType());
		target.setUnit(source.getUnit());
		target.setUnitType(source.getUnitType());
		target.setZipGeoCode(source.getZipGeoCode());
		target.setCivicNo(getCivicNo(source));
		target.setCivicNoSuffix(getCivicNoSuffix(source));
		target.setForeignState(source.getForeignState());
		return super.performDomainMapping(source, target);
	}

	private String getCivicNo(Address source) {
		if ((!StringUtil.isEmpty(source.getCivicNo()) && !StringUtil.isEmpty(source.getStreetNumber())) || (!StringUtil.isEmpty(source.getCivicNo()) && StringUtil.isEmpty(source.getStreetNumber()))) {
			return source.getCivicNo();
		} else if (StringUtil.isEmpty(source.getCivicNo()) && !StringUtil.isEmpty(source.getStreetNumber())) {
			return source.getStreetNumber();
		} else {
			return null;
		}
	}

	private String getCivicNoSuffix(Address source) {
		if ((!StringUtil.isEmpty(source.getCivicNoSuffix()) && !StringUtil.isEmpty(source.getStreetNumberSuffix()))
				|| (!StringUtil.isEmpty(source.getCivicNoSuffix()) && StringUtil.isEmpty(source.getStreetNumberSuffix()))) {
			return source.getCivicNoSuffix();
		} else if (StringUtil.isEmpty(source.getCivicNoSuffix()) && !StringUtil.isEmpty(source.getStreetNumberSuffix())) {
			return source.getStreetNumberSuffix();
		} else {
			return null;
		}
	}

	protected Address performSchemaMapping(AddressInfo source, Address target) {
		return super.performSchemaMapping(source, target);
	}

}
