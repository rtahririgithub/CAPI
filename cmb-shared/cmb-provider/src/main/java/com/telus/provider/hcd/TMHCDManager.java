package com.telus.provider.hcd;

import com.telus.api.TelusAPIException;
import com.telus.api.hcd.HCDManager;
import com.telus.api.hcd.HCDclpActivationOptionDetails;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

/**
 * @author x119951
 *
 */

public class TMHCDManager extends BaseProvider implements HCDManager {
	
	private static final long serialVersionUID = 1L;

	public TMHCDManager(TMProvider provider) {
		super(provider);
	}

	public HCDclpActivationOptionDetails getCLPActivationOptionDetails(int ban) throws TelusAPIException {
		try {
			return provider.getAccountLifecycleFacade().getCLPActivationOptionsDetail(ban);
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

}
