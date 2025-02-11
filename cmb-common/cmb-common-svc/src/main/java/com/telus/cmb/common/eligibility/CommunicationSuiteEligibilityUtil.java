package com.telus.cmb.common.eligibility;

import org.apache.log4j.Logger;
import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;

public class CommunicationSuiteEligibilityUtil {

	private static final Logger LOGGER = Logger.getLogger(CommunicationSuiteEligibilityUtil.class);

	public static boolean validateCommunicationSuiteEligibility(int brandId, char accountType, char accountSubType) throws ApplicationException {
		boolean eligible = false;
		CommunicationSuiteEligibilityCheckCriteria criteria = new CommunicationSuiteEligibilityCheckCriteria();
		criteria.setBrandId(brandId);
		criteria.setAccountType(String.valueOf(accountType));
		criteria.setAccountSubType(String.valueOf(accountSubType));

		try {
			Boolean result = CommunicationSuiteEligibilityCheckStrategy.getInstance().checkEligibility(criteria);
			if (result != null) {
				eligible = result.booleanValue();
			}
		} catch (Exception e) {
			LOGGER.error("Error in isCommunicationSuiteEligibility: " + e + ". CommunicationSuiteEligibilityCheckCriteria=" + criteria);
			throw new ApplicationException(SystemCodes.CMB_EJB, "Error in isCommunicationSuiteEligibility: " + e + ". CommunicationSuiteEligibilityCheckCriteria=" + criteria, "", e);
		}

		return eligible;
	}

}
