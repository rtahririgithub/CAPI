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
		
		target.setCDMACoverage(source.isCdmaCoverageInd());
		target.setCDMAPostpaidCoverage(source.isCdmaPostPaidCoverageInd());
		target.setCDMAPrepaidCoverage(source.isCdmaPrePaidCoverageInd());
		target.setCurrentServiceProvider(source.getCurrentServiceProviderCd());
		target.setHSPACoverage(source.isHspaCoverageInd());
		target.setHSPAPostpaidCoverage(source.isHspaPostPaidCoverageInd());
		target.setHSPAPrepaidCoverage(source.isHspaPrePaidCoverageInd());
		target.setIdenCoverage(source.isIdenCoverageInd());
		target.setOutgoingBrandId(Integer.parseInt(source.getOutgoingBrandCd()));
		target.setIncomingBrandId(Integer.parseInt(source.getIncommingBrandCd()));
		target.setPhoneNumber(source.getPhoneNumber());
		target.setPlatformId(Integer.parseInt(source.getPlatformId()));
		target.setPortDirectionIndicator(source.getPortDirectionCd());
		target.setPortVisibility(source.getPortVisibilityCd());
		
		return super.performDomainMapping(source, target);
	}
	
}