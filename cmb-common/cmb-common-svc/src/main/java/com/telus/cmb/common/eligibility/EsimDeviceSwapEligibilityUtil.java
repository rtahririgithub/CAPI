package com.telus.cmb.common.eligibility;

import org.apache.log4j.Logger;
import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;

public class EsimDeviceSwapEligibilityUtil {

	private static final Logger LOGGER = Logger.getLogger(EsimDeviceSwapEligibilityUtil.class);

	public static boolean validateEsimDeviceSwapEligibility(String simType, String newDeviceType, String currentDeviceType) throws ApplicationException {
		boolean isEligible = false;
		
		EsimDeviceSwapEligibilityCheckCriteria criteria = new EsimDeviceSwapEligibilityCheckCriteria();
		criteria.setSimType(simType);
		criteria.setNewDeviceType(newDeviceType);
		criteria.setCurrentDeviceType(currentDeviceType);

		try {
			Boolean result = EsimDeviceSwapEligibilityEvaluationStrategy.getInstance().checkEligibility(criteria);
			if (result != null) {
				isEligible = result.booleanValue();
			}
		} catch (Exception e) {
			String errMsg = "Error in validateEsimDeviceSwapEligibility: " + e + ". EsimDeviceSwapEligibilityCheckCriteria=" + criteria;
			LOGGER.error(errMsg);
			throw new ApplicationException(SystemCodes.CMB_EJB, errMsg, "", e);
		}

		return isEligible;
	}
}
