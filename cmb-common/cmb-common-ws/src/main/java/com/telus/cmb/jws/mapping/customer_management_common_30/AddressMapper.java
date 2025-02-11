package com.telus.cmb.jws.mapping.customer_management_common_30;

import weblogic.wsee.util.StringUtil;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.AddressInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Address;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.AddressType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode;

public class AddressMapper extends AbstractSchemaMapper<Address, AddressInfo> {
	private static AddressMapper INSTANCE = null;
	
	public AddressMapper(){
		super(Address.class, AddressInfo.class);
	}
	
	public static synchronized AddressMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AddressMapper();
		}
		
		return INSTANCE;
	}

	@Override
	protected AddressInfo performDomainMapping(Address source, AddressInfo target) {
		if (source.getAddressType() != null) {
			target.setAddressType(source.getAddressType().value());
		}
		target.setAttention(source.getAttention());
		target.setCity(source.getCity());
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
		
		target.setStreetType(source.getStreetType());
		target.setUnit(source.getUnit());
		target.setUnitType(source.getUnitType());
		target.setZipGeoCode(source.getZipGeoCode());
		
		if((!StringUtil.isEmpty(source.getCivicNo()) && !StringUtil.isEmpty(source.getStreetNumber()))||
				(!StringUtil.isEmpty(source.getCivicNo()) && StringUtil.isEmpty(source.getStreetNumber()))){
			target.setCivicNo(source.getCivicNo());
		} else if (StringUtil.isEmpty(source.getCivicNo()) && !StringUtil.isEmpty(source.getStreetNumber())){
			target.setCivicNo(source.getStreetNumber());
		} else {
			target.setCivicNo(null);
		}
				
		if((!StringUtil.isEmpty(source.getCivicNoSuffix()) && !StringUtil.isEmpty(source.getStreetNumberSuffix()))||
				(!StringUtil.isEmpty(source.getCivicNoSuffix()) && StringUtil.isEmpty(source.getStreetNumberSuffix()))){
			target.setCivicNoSuffix(source.getCivicNoSuffix());
		} else if (StringUtil.isEmpty(source.getCivicNoSuffix()) && !StringUtil.isEmpty(source.getStreetNumberSuffix())){
			target.setCivicNoSuffix(source.getStreetNumberSuffix());
		} else {
			target.setCivicNoSuffix(null);
		}
		
		return super.performDomainMapping(source, target);
	}

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
	
	
}
