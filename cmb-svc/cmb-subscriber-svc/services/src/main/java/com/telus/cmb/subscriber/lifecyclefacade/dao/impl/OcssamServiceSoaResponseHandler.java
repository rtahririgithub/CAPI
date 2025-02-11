/*
 *  Copyright (c) 2013 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.dao.soa.spring.SoaDefaultResponseHandler;
import com.telus.cmb.subscriber.mapping.OcssamServiceMapper;
import com.telus.eas.utility.info.SapccThresholdInfo;
import com.telus.eas.utility.info.SapccUpdateAccountPurchaseInfo;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.onlinechargingsubscriberaccountmgmtservicerequestresponse_v3.UpdateAccountPurchaseAmountResponse;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.onlinechargingsubscriberaccountmgmtservicerequestresponse_v3.UpdatePurchaseAmountResult;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;

public class OcssamServiceSoaResponseHandler extends SoaDefaultResponseHandler {

	private static final Log LOGGER = LogFactory.getLog(OcssamServiceSoaResponseHandler.class);
	
	public OcssamServiceSoaResponseHandler(String componentName) {
		super(componentName);
	}

	@Override
	public <T extends ResponseMessage> T handleErrorResponse(T response, String systemCode) throws ApplicationException {

		// Check if the OCSSAM response is valid
		if (response == null) {
			String message = "Invalid response from OCSSAM service.";
			LOGGER.error(message);
			throw new ApplicationException(SystemCodes.CMB_OCSSAMS_DAO, ErrorCodes.OCS_GENERAL_ERROR, message, StringUtils.EMPTY);
		}
		
		if (response instanceof UpdateAccountPurchaseAmountResponse) {
			UpdateAccountPurchaseAmountResponse updateAccountPurchaseAmountResponse = (UpdateAccountPurchaseAmountResponse) response;
			// Check if there are WCC threshold breaches
			if (!updateAccountPurchaseAmountResponse.isReturnStatusInd()) {
				List<SapccUpdateAccountPurchaseInfo> breachlist = new ArrayList<SapccUpdateAccountPurchaseInfo>();
				for (UpdatePurchaseAmountResult result : updateAccountPurchaseAmountResponse.getUpdatePurchaseAmountResultList()) {
					SapccUpdateAccountPurchaseInfo info = OcssamServiceMapper.UpdatePurchaseAmountResultMapper().mapToDomain(result);
					// The result list may contain both breached and non-breached items - add only breached items
					if (info.isBreachedInd()) {
						breachlist.add(info);
					}
				}
				handleBreachErrorResponse(breachlist);
			}
		}

		return super.handleErrorResponse(response, systemCode);
	}
	
	private void handleBreachErrorResponse(List<SapccUpdateAccountPurchaseInfo> breachlist) throws ApplicationException {

		// Iterate through the list to build the breach message for each item. Note: this will build the breach error message as follows:
		// 		- zone=DOMESTIC,chargedAmount=49.00,purchaseAmount=20.00,lastConsentAmount=0.00,thresholdType=TEMPORARY,thresholdLimitAmount=50.00
		// Consumers of this functionality will parse the above message format to extract the breach attributes. Multiple breaches will be pipe ('|') delimited.
		StringBuilder sb = new StringBuilder();
		Iterator<SapccUpdateAccountPurchaseInfo> iterator = breachlist.iterator();
		while (iterator.hasNext()) {
			SapccUpdateAccountPurchaseInfo info = iterator.next();
			sb.append(getFormattedBreachErrorMessage(info));
			if (iterator.hasNext()) {
				sb.append("|");
			}
		}

		String message = sb.toString();
		if (StringUtils.isNotBlank(message)) {
			// If the message is not blank, that means there's a breach; log the error and throw an ApplicationException.
			LOGGER.error("OCSSAM.updateAccountPurchaseAmount breach error; breachlist [" + message + "].");
			throw new ApplicationException(SystemCodes.CMB_OCSSAMS_DAO, ErrorCodes.OCS_BAN_PPU_BREACH, message, StringUtils.EMPTY);
		}
	}
	
	private String getFormattedBreachErrorMessage(SapccUpdateAccountPurchaseInfo info) {

		// Returns the breach attributes as a string of comma-delimited key-value pairs using the attribute names as the keys.
		StringBuffer buffer = new StringBuffer();
		buffer.append("zone=").append(info.getZone());
		buffer.append(",chargedAmount=").append(info.getChargedAmount());
		buffer.append(",purchaseAmount=").append(info.getPurchaseAmount());
		buffer.append(",lastConsentAmount=").append(info.getLastConsentAmount());
		for (SapccThresholdInfo threshold : info.getThresholdList()) {
			buffer.append(",thresholdType=").append(threshold.getThresholdType());
			buffer.append(",thresholdLimitAmount=").append(threshold.getThresholdLimitAmount());
		}
		return buffer.toString();
	}

}