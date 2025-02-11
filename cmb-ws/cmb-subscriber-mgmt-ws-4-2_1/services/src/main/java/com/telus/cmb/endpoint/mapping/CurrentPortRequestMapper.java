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

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.CompletePortRequestDataType;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourceorderservicecommontypes_v2.AccountInfoType;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourceorderservicecommontypes_v2.BillingAddressType;

public class CurrentPortRequestMapper extends AbstractSchemaMapper<CompletePortRequestDataType, PortRequestInfo> {

	public CurrentPortRequestMapper() {
		super(CompletePortRequestDataType.class, PortRequestInfo.class);
	}

	@Override
	protected CompletePortRequestDataType performSchemaMapping(PortRequestInfo source, CompletePortRequestDataType target) {

		target.setAgentAuthDate((source.getAgencyAuthorizationDate()));
		if (null != source.getAuthorizationIndicator()) {
			target.setAgentAuthInd(Boolean.valueOf(source.getAuthorizationIndicator()));
		}
		target.setAgentAuthName(source.getAgencyAuthorizationName());
		target.setAlternateContactNumber(source.getAlternateContactNumber());
		target.setAutoActivateInd(source.isAutoActivate());
		target.setBillingAccountNumber(new Integer(source.getBanId()).toString());
		target.setCanBeActivateInd(source.canBeActivated());
		target.setCanBeCanceledInd(source.canBeCanceled());
		target.setCanBeModifiedInd(source.canBeModified());
		target.setCanBeSubmitedInd(source.canBeSubmitted());
		if (null != source.getCreationDate()) {
			target.setCreationDate((source.getCreationDate()));
		}
		if (null != source.getDesiredDateTime()) {
			target.setDesiredDueDate((source.getDesiredDateTime()));
		}
		if (null != source.getDslInd()) {
			target.setDigitalSubscriberLineInd(Boolean.valueOf(source.getDslInd()));
		}
		if (null != source.getDslLineNumber()) {
			target.setDigitalSubscriberLineNumber(new Integer(source.getDslLineNumber()).toString());
		}
		target.setEndUserMovingInd(source.getEndUserMovingInd());
		if (null != source.getExpedite()) {
			target.setExpediteInd(Boolean.valueOf(source.getExpedite()));
		}
		target.setNumberPortabilityDirectionCd(source.getPortDirectionIndicator());
		target.setOldResellerName(source.getOldReseller());

		AccountInfoType accountInfoType = new AccountInfoType();
		accountInfoType.setAccountNumber(source.getOSPAccountNumber());
		accountInfoType.setEsn(source.getOSPSerialNumber());
		accountInfoType.setPin(source.getOSPPin());
		target.setOSPAccountInfoType(accountInfoType);

		target.setPhoneNumber(source.getPhoneNumber());
		target.setPlatformId(new Integer(source.getPlatformId()).toString());
		target.setPlatformIdUpdatedInd(source.isPlatformIdUpdated());
		target.setPortProcessTypeCd(source.getType());

		BillingAddressType billingAddressType = new BillingAddressType();
		billingAddressType.setBusinessName(source.getBusinessName());
		billingAddressType.setFirstName(source.getPortRequestName().getFirstName());
		billingAddressType.setLastName(source.getPortRequestName().getLastName());
		billingAddressType.setMiddleInitialTxt(source.getPortRequestName().getMiddleInitial());
		billingAddressType.setCityName(source.getPortRequestAddress().getCity());
		billingAddressType.setCountryName(source.getPortRequestAddress().getCountry());
		billingAddressType.setProvinceCd(source.getPortRequestAddress().getProvince());
		billingAddressType.setStreetName(source.getPortRequestAddress().getStreetName());
		billingAddressType.setStreetNum(source.getPortRequestAddress().getStreetNumber());
		billingAddressType.setStreetDirectionTxt(source.getPortRequestAddress().getStreetDirection());
		billingAddressType.setPostalCd(source.getPortRequestAddress().getPostalCode());
		target.setPortRequestAddress(billingAddressType);

		target.setPortRequestId(source.getPortRequestId());
		target.setRemarksTxt(source.getRemarks());
		target.setSourceBrandId(new Integer(source.getIncomingBrandId()).toString());
		target.setStatusCategoryCd(source.getStatusCategory());
		target.setStatusCd(source.getStatusCode());
		target.setStatusReasonCd(source.getStatusReasonCode());
		target.setTargetBrandId(new Integer(source.getOutgoingBrandId()).toString());

		return super.performSchemaMapping(source, target);
	}

}