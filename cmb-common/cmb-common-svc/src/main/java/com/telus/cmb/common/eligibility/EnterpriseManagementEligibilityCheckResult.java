package com.telus.cmb.common.eligibility;

/**
 * This class is not in use but may be modified for future use.
 * @author tongts
 *
 */
public class EnterpriseManagementEligibilityCheckResult {
	private boolean dataSyncInd;

	public boolean isDataSyncInd() {
		return dataSyncInd;
	}

	public void setDataSyncInd(boolean dataSyncInd) {
		this.dataSyncInd = dataSyncInd;
	}

	@Override
	public String toString() {
		return "EnterpriseDataSyncEligibilityCheckResult [dataSyncInd="
				+ dataSyncInd + "]";
	}
	
	
}
