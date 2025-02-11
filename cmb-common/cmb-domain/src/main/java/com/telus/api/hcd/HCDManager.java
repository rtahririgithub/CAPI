package com.telus.api.hcd;

import com.telus.api.TelusAPIException;

/**
 * @author x119951
 */

public interface HCDManager {
	
	HCDclpActivationOptionDetails getCLPActivationOptionDetails(int ban) throws TelusAPIException;
	
}
