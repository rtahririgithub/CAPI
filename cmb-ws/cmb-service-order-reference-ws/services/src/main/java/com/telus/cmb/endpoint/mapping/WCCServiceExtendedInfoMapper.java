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

import java.util.Arrays;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.WccServiceExtendedInformation;
import com.telus.eas.utility.info.WCCServiceExtendedInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.Description;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.MultilingualCodeDescriptionList;

/**
 * @author Wilson Cheong
 *
 */
public class WCCServiceExtendedInfoMapper extends AbstractSchemaMapper<WccServiceExtendedInformation, WCCServiceExtendedInfo> {

	private static WCCServiceExtendedInfoMapper INSTANCE;

	private WCCServiceExtendedInfoMapper() {
		super(WccServiceExtendedInformation.class, WCCServiceExtendedInfo.class);
	}

	public synchronized static WCCServiceExtendedInfoMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new WCCServiceExtendedInfoMapper();
		}
		return INSTANCE;
	}

	@Override
	protected WccServiceExtendedInformation performSchemaMapping(WCCServiceExtendedInfo source, WccServiceExtendedInformation target) {
		
		target.setServiceCode(source.getCode());
		target.setServiceDescription(getMCDescriptionList(source.getDescription(), source.getDescriptionFrench()));
		target.setChargeAmt(source.getChargeAmount());
		target.setSapccOfferId(source.getSapccOfferInfo() != null ? source.getSapccOfferInfo().getOfferId() : null);
		target.setZoneCd(source.getSapccOfferInfo().getZone());

		return super.performSchemaMapping(source, target);
	}

	private MultilingualCodeDescriptionList getMCDescriptionList(String descriptionEn, String descriptionFr) {
		
		MultilingualCodeDescriptionList marketingDescriptionList = new MultilingualCodeDescriptionList();
		Description descriptionEnglish = new Description();
		descriptionEnglish.setDescriptionText(descriptionEn);
		descriptionEnglish.setLocale("EN");
		Description descriptionFrench = new Description();
		descriptionFrench.setDescriptionText(descriptionFr);
		descriptionFrench.setLocale("FR");
		marketingDescriptionList.setDescription(Arrays.asList(new Description[]{descriptionEnglish, descriptionFrench}));
		
		return marketingDescriptionList;
	}
	
}
