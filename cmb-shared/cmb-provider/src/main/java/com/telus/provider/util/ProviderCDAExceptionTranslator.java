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
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.message.ApplicationMessageType;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.AudienceType;
import com.telus.api.reference.Brand;
import com.telus.eas.message.info.ApplicationMessageInfo;

/**
 * @author R. Fong
 *
 */
public class ProviderCDAExceptionTranslator extends ProviderDefaultExceptionTranslator {

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

		if (SystemCodes.CMB_CDA_DAO.equalsIgnoreCase(ae.getSystemCode())) {
			ApplicationMessageInfo info = new ApplicationMessageInfo(ae.getErrorCode(), ApplicationSummary.DEFAULT.getId(), AudienceType.DEFAULT.getId(), ApplicationMessageType.MESSAGE_TYPE_ID_ERROR,
					Brand.BRAND_ID_ALL);
			info.setText(Locale.ENGLISH.getLanguage(), ae.getMessage());
			return new TelusAPIException(ae, info, 0);
		} else {
			TelusAPIException result = getExceptionForErrorId(ae.getErrorCode(), ae);
			return result == null ? new TelusAPIException(ae) : result;
		}
	}

}