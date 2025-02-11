package com.telus.cmb.common.validation;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;

public class BanValidator {
	
	public static boolean isValidRange(int ban) {
		return ban > 0;
	}
	
	public static void validate(int ban) throws ApplicationException {
		if (!BanValidator.isValidRange(ban)) {
			throw new ApplicationException(SystemCodes.CMB_EJB, ErrorCodes.BAN_NOT_FOUND
					, "Ban is invalid range (Ban passed = " + ban + "). Valid range: > 0", "");
		}
	}

}
