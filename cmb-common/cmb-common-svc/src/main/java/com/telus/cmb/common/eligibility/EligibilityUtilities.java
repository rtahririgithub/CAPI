package com.telus.cmb.common.eligibility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.eas.account.info.AccountInfo;

public class EligibilityUtilities {
	private static final Log logger = LogFactory.getLog(EligibilityUtilities.class);
	
	/**
	 * 
	 * @param account
	 * @param processType
	 * @return
	 */
	public static boolean isEnterpriseDataEligible (AccountInfo account, String processType) {
		boolean eligible = false;
		
		EnterpriseManagementEligibilityCheckCriteria criteria = new EnterpriseManagementEligibilityCheckCriteria();
		if (account.isPCS()) {
			criteria.setProductType("C");
		}else if (account.isIDEN()) {
			criteria.setProductType("I");
		}else if (account.isPager()) {
			criteria.setProductType("P");
		}
		criteria.setProcessType(processType);
		criteria.setAccountCombinedType(String.valueOf(account.getAccountType())+String.valueOf(account.getAccountSubType()));
		criteria.setBrandId(account.getBrandId());
		try {
			Boolean result = EnterpriseManagementEligibilityEvaluationStrategy.getInstance().checkEligibility(criteria);
			if (result != null) {
				eligible = result.booleanValue();
			}
		} catch (Exception e) {
			logger.error("Error in isEnterpriseDataEligible: " + e + ". EnterpriseDataSyncEligibilityCheckCriteria="+criteria);
		}
		
		return eligible;
	}
	
	public static boolean isEnterpriseDataEligible (AccountInfo oldAccountInfo, AccountInfo newAccountInfo, String processType) {	
		return (isEnterpriseDataEligible(oldAccountInfo, processType) || 
				isEnterpriseDataEligible(newAccountInfo, processType) );
	}

}
