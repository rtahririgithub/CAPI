package com.telus.eas.portability.info;

import com.telus.api.portability.PortInEligibility;
import com.telus.eas.framework.info.Info;

public class PortInEligibilityInfo extends Info implements PortInEligibility {

	private static final long serialVersionUID = 1L;

	private String portVisibility;
	private String  currentServiceProvider;
	private boolean pcsCoverage ;
	private boolean idenCoverage;
	private boolean prepaidCoverage;
	private boolean postPaidCoverage;
	private String portDirectionIndicator;
	private int incomingBrandId;
	private int outgoingBrandId;
	private String phoneNumber;
	private boolean cdmaCoverage;
	private boolean hspaCoverage;
	private boolean cdmaPostpaidCoverage;
	private boolean hspaPostpaidCoverage;
	private boolean cdmaPrepaidCoverage;
	private boolean hspaPrepaidCoverage;
	private int platformId = PortInEligibility.PORT_PLATFORM_TELUS;

	public String getPortVisibility() {
		return portVisibility;
	}
	public String  getCurrentServiceProvider() {
		return currentServiceProvider;
	}
	
	/**
	 * @deprecated
	 * @see com.telus.api.portability.PortInEligibility#isPCSCoverage()
	 */
	public boolean isPCSCoverage() {
		return pcsCoverage;
	}
	
	public boolean isCDMACoverage() {
		return cdmaCoverage;
	}
	
	public boolean isHSPACoverage() {
		return hspaCoverage;
	}
	
	public boolean isIDENCoverage() {
		return idenCoverage;
	}
	/**
	 * @deprecated
	 * @see com.telus.api.portability.PortInEligibility#isPrepaidCoverage()
	 */
	public boolean isPrepaidCoverage() {
		return prepaidCoverage;
	}
	
	public boolean isCDMAPrepaidCoverage() {
		return cdmaPrepaidCoverage;
	}
	
	public boolean isHSPAPrepaidCoverage() {
		return hspaPrepaidCoverage;
	}
	
	/**
	 * @deprecated
	 * @see com.telus.api.portability.PortInEligibility#isPostPaidCoverage()
	 */
	public boolean isPostPaidCoverage() {
		return postPaidCoverage;
	}
	
	public boolean isCDMAPostpaidCoverage() {
		return cdmaPostpaidCoverage;
	}
	
	public boolean isHSPAPostpaidCoverage() {
		return hspaPostpaidCoverage;
	}
	
	public String getPortDirectionIndicator() {
		return portDirectionIndicator;
	}
	public int getIncomingBrandId() {
		return incomingBrandId;
	}
	public int getOutgoingBrandId() {
		return outgoingBrandId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public int getPlatformId() {
		return this.platformId;
	}
	
	public void setCurrentServiceProvider(String currentServiceProvider) {
		this.currentServiceProvider = currentServiceProvider;
	}
	public void setIdenCoverage(boolean idenCoverage) {
		this.idenCoverage = idenCoverage;
	}
	/**@deprecated**/
	public void setPcsCoverage(boolean pcsCoverage) {
		this.pcsCoverage = pcsCoverage;
	}
	public void setPortDirectionIndicator(String portDirectionIndicator) {
		this.portDirectionIndicator = portDirectionIndicator;
	}
	public void setPortVisibility(String portVisibility) {
		this.portVisibility = portVisibility;
	}
	/**@deprecated**/
	public void setPostPaidCoverage(boolean postPaidCoverage) {
		this.postPaidCoverage = postPaidCoverage;
	}
	/**@deprecated**/
	public void setPrepaidCoverage(boolean prepaidCoverage) {
		this.prepaidCoverage = prepaidCoverage;
	}
	public void setIncomingBrandId(int incomingBrandId) {
		this.incomingBrandId = incomingBrandId;
	}
	public void setOutgoingBrandId(int outgoingBrandId) {
		this.outgoingBrandId = outgoingBrandId;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public void setCDMACoverage(boolean cdmaCoverage) {
		this.cdmaCoverage = cdmaCoverage;
	}
	public void setHSPACoverage(boolean hspaCoverage) {
		this.hspaCoverage = hspaCoverage;
	}
	public void setCDMAPostpaidCoverage(boolean cdmaPostpaidCoverage) {
		this.cdmaPostpaidCoverage = cdmaPostpaidCoverage;
	}
	public void setCDMAPrepaidCoverage(boolean cdmaPrepaidCoverage) {
		this.cdmaPrepaidCoverage = cdmaPrepaidCoverage;
	}
	public void setHSPAPostpaidCoverage(boolean hspaPostpaidCoverage) {
		this.hspaPostpaidCoverage = hspaPostpaidCoverage;
	}
	public void setHSPAPrepaidCoverage(boolean hspaPrepaidCoverage) {
		this.hspaPrepaidCoverage = hspaPrepaidCoverage;
	}
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}
	public boolean isPortInFromMVNE() {
		return this.platformId != PortInEligibility.PORT_PLATFORM_TELUS;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("PortInEligibilityInfo:{\n");
		s.append("    portVisibility=[").append(portVisibility).append("]\n");
		s.append("    currentServiceProvider=[").append(currentServiceProvider).append("]\n");
		s.append("    pcsCoverage=[").append(pcsCoverage).append("]\n");
		s.append("    cdmaCoverage=[").append(cdmaCoverage).append("]\n");
		s.append("    cdmaPostpaidCoverage=[").append(cdmaPostpaidCoverage).append("]\n");
		s.append("    cdmaPrepaidCoverage=[").append(cdmaPrepaidCoverage).append("]\n");
		s.append("    hspaCoverage=[").append(hspaCoverage).append("]\n");
		s.append("    hspaPostpaidCoverage=[").append(hspaPostpaidCoverage).append("]\n");
		s.append("    hspaPrepaidCoverage=[").append(hspaPrepaidCoverage).append("]\n");
		s.append("    idenCoverage=[").append(idenCoverage).append("]\n");
		s.append("    prepaidCoverage=[").append(prepaidCoverage).append("]\n");
		s.append("    postPaidCoverage=[").append(postPaidCoverage).append("]\n");
		s.append("    portDirectionIndicator=[").append(portDirectionIndicator).append("]\n");
		s.append("    incomingBrandId=[").append(incomingBrandId).append("]\n");
		s.append("    outgoingBrandId=[").append(outgoingBrandId).append("]\n");
		s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
		s.append("    platformId=[").append(platformId).append("]\n");
		s.append("}");

		return s.toString();
	}
}
