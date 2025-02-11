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
import com.telus.cmb.schema.PortInEligibility;
import com.telus.eas.portability.info.PortInEligibilityInfo;

public class PortInEligibilityMapper extends AbstractSchemaMapper<PortInEligibility, PortInEligibilityInfo> {

	public PortInEligibilityMapper() {
		super(PortInEligibility.class, PortInEligibilityInfo.class);
	}

	@Override
	protected PortInEligibilityInfo performDomainMapping(PortInEligibility source, PortInEligibilityInfo target) {
		target.setCDMACoverage(source.isCDMACoverageInd());
		target.setCDMAPostpaidCoverage(source.isCDMAPostPaidCoverageInd());
		target.setCDMAPrepaidCoverage(source.isCDMAPrePaidCoverageInd());
		target.setCurrentServiceProvider(source.getCurrentServiceProviderCd());
		target.setHSPACoverage(source.isHSPACoverageInd());
		target.setHSPAPostpaidCoverage(source.isHSPAPostPaidCoverageInd());
		target.setHSPAPrepaidCoverage(source.isHSPAPrePaidCoverageInd());
		target.setIdenCoverage(source.isIDENCoverageInd());
		target.setOutgoingBrandId(Integer.parseInt(source.getOutgoingBrandCd()));
		target.setIncomingBrandId(Integer.parseInt(source.getIncommingBrandCd()));
		target.setPhoneNumber(source.getPhoneNumberCd());
		target.setPlatformId(Integer.parseInt(source.getPlatformId()));
		target.setPortDirectionIndicator(source.getPortDirectionCd());
		target.setPortVisibility(source.getPortVisibilityCd());
		return super.performDomainMapping(source, target);
	}
}