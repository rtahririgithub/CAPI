/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint.mapping;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.PortRequestData;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.portability.info.PortRequestAddressInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.portability.info.PortRequestNameInfo;

public class PortRequestMapper extends AbstractSchemaMapper<PortRequestData, PortRequestInfo> {
	
	private static String[] AFFIRMATIVE_CODES = { "Y", "TRUE" };
	
	public PortRequestMapper() {
		super(PortRequestData.class, PortRequestInfo.class);
	}

	@Override
	protected PortRequestInfo performDomainMapping(PortRequestData source, PortRequestInfo target) {
		
		target.setAgencyAuthorizationDate(source.getAgentAuthDate());
		target.setAgencyAuthorizationIndicator(source.getAgentAuthInd());
		target.setAgencyAuthorizationName(source.getAgentAuthName());
		target.setAlternateContactNumber(source.getAlternateContactNumber());
		// WNP's 'AutoActivate' flag means the opposite of our 'ActivateAndHoldCd', so we set it as such below
		target.setAutoActivate(!ArrayUtils.contains(AFFIRMATIVE_CODES, StringUtils.upperCase(source.getActivateAndHoldCd())));
		if (source.getBillingAccountNumber() != null) {
			target.setBanId(Integer.parseInt(source.getBillingAccountNumber()));
		}
		target.setDslInd(source.getDigitalSubscriberLineCd());
		if (source.getDigitalSubscriberLineNumber() != null) {
			target.setDslLineNumber(Integer.parseInt(source.getDigitalSubscriberLineNumber()));
		}
		target.setEndUserMovingInd(source.isEndUserMovingInd());
		target.setExpedite(source.getExpediteInd());
		target.setOldReseller(source.getOldResellerName());
		if (source.getOSPAccountInfoType() != null) {
			target.setOSPAccountNumber(source.getOSPAccountInfoType().getAccountNumber());
			target.setOSPPin(source.getOSPAccountInfoType().getPin());
			target.setOSPSerialNumber(source.getOSPAccountInfoType().getEsn());
		}
		target.setPhoneNumber(source.getPhoneNumber());
		if (source.getPlatformId() != null) {
			target.setPlatformId(Integer.parseInt(source.getPlatformId()));
		}
		if (source.getPortRequestAddress() != null) {
			PortRequestAddressInfo targetAddress = new PortRequestAddressInfo();
			targetAddress.setCity(source.getPortRequestAddress().getCityName());
			targetAddress.setCountry(source.getPortRequestAddress().getCountryName());
			targetAddress.setPostalCode(source.getPortRequestAddress().getPostalCd());
			targetAddress.setProvince(source.getPortRequestAddress().getProvinceCd());
			targetAddress.setStreetDirection(source.getPortRequestAddress().getStreetDirectionTxt());
			targetAddress.setStreetName(source.getPortRequestAddress().getStreetName());
			targetAddress.setStreetNumber(source.getPortRequestAddress().getStreetNum());
			target.setPortRequestAddress(targetAddress);
			target.setBusinessName(source.getPortRequestAddress().getBusinessName());
		}
		target.setPortRequestId(source.getPortRequestId());
		if (source.getPortRequestAddress() != null) {
			PortRequestNameInfo portRequestNameInfo = new PortRequestNameInfo();
			portRequestNameInfo.setFirstName(source.getPortRequestAddress().getFirstName());
			portRequestNameInfo.setLastName(source.getPortRequestAddress().getLastName());
			portRequestNameInfo.setGeneration(source.getPortRequestAddress().getSuffixTxt());
			portRequestNameInfo.setTitle(source.getPortRequestAddress().getPrefixTxt());
			portRequestNameInfo.setMiddleInitial(source.getPortRequestAddress().getMiddleInitialTxt());
			target.setPortRequestName(portRequestNameInfo);
		}
		target.setPortDirectionIndicator(source.getNumberPortabilityDirectionInd());
		
		EquipmentInfo equipmentInfo = new EquipmentInfo();
		equipmentInfo.setNetworkType("H");
		target.setEquipment(equipmentInfo);
		
		return super.performDomainMapping(source, target);
	}
}