/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.provider.util;

import java.util.Locale;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.PhoneNumberException;
import com.telus.api.account.WCCAccountThresholdBreachException;
import com.telus.api.account.WPSFeatureException;
import com.telus.api.message.ApplicationMessageType;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.AudienceType;
import com.telus.api.reference.Brand;
import com.telus.eas.message.info.ApplicationMessageInfo;

/**
 * @author R. Fong
 *
 */
public class ProviderServiceChangeExceptionTranslator extends ProviderDefaultExceptionTranslator {

	public TelusAPIException translateException(Throwable throwable) {

		TelusAPIException exception = null;

		if (throwable instanceof ApplicationException) {
			exception = handleApplicationException((ApplicationException) throwable);
		} else {
			return super.translateException(throwable);
		}

		return exception;
	}

	protected TelusAPIException handleApplicationException(ApplicationException ae) {

		if (SystemCodes.CMB_OCSSAMS_DAO.equalsIgnoreCase(ae.getSystemCode()) && ErrorCodes.OCS_BAN_PPU_BREACH.equalsIgnoreCase(ae.getErrorCode())) {
			ApplicationMessageInfo info = new ApplicationMessageInfo(ae.getErrorCode(), ApplicationSummary.DEFAULT.getId(), AudienceType.DEFAULT.getId(), ApplicationMessageType.MESSAGE_TYPE_ID_ERROR,
					Brand.BRAND_ID_ALL);
			info.setText(Locale.ENGLISH.getLanguage(), ae.getMessage());
			return new WCCAccountThresholdBreachException(ae, info);
		}
		
		return super.handleApplicationException(ae);
	}
	
	protected TelusAPIException handleSystemException(SystemException se) {

		if (SystemCodes.CMB_VOIP_SUPPLEMENTARY_DAO.equalsIgnoreCase(se.getSystemCode())) {
			return new InvalidServiceChangeException(InvalidServiceChangeException.VOIP_SUPPLEMENTARY_DATA_RETRIEVAL_ERROR, se);
		}
		if (SystemCodes.CMB_VOIP_LICENSE_DAO.equalsIgnoreCase(se.getSystemCode())) {
			if (ErrorCodes.NO_UNASSIGNED_LICENSE_ERROR.equalsIgnoreCase(se.getErrorCode())) {
				return new InvalidServiceChangeException(InvalidServiceChangeException.NO_UNASSIGNED_VOIP_LICENSE, se);
			}
			return new InvalidServiceChangeException(InvalidServiceChangeException.VOIP_LICENSE_CHANGE_ERROR, se);
		}
		
		return super.handleSystemException(se);
	}

	protected TelusAPIException getExceptionForErrorId(String errorId, Throwable cause) {

		if ("APP20001".equals(errorId)) {
			return new PhoneNumberException(PhoneNumberException.ADDITIONAL_RESERVATION_FAILED, cause.getMessage(), cause);
		} else if ("APP20003".equals(errorId)) {
			return new InvalidServiceChangeException(InvalidServiceChangeException.DUPLICATE_FEATURE, cause);
		} else if ("PREPAID_ADDTOOMANYFEATURES_EXCEPTION".equals(errorId)) {
			return new WPSFeatureException(cause, WPSFeatureException.ADD_TOOMANYFEATURES, WPSFeatureException.ADD_TOOMANYFEATURES_MESSAGE_EN, WPSFeatureException.ADD_TOOMANYFEATURES_MESSAGE_FR);
		}
		
		return super.getExceptionForErrorId(errorId, cause);
	}

}