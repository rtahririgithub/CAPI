package com.telus.eas.subscriber.info;

import com.telus.api.account.ServiceChangeHistory;
import com.telus.api.account.VendorServiceChangeHistory;
import com.telus.eas.framework.info.Info;

public class VendorServiceChangeHistoryInfo extends Info implements VendorServiceChangeHistory {

	static final long serialVersionUID = 1L;
	private String vendorServiceCode; //represented by Category Code
	private ServiceChangeHistory[] promoSocs;

	public VendorServiceChangeHistoryInfo() {
	}

	public String getVendorServiceCode() {
		return vendorServiceCode;
	}

	public void setVendorServiceCode(String vendorServiceCode) {
		this.vendorServiceCode = vendorServiceCode;
	}

	public ServiceChangeHistory[] getPromoSOCs() {
		return promoSocs;
	}
	
	public void setPromoSOCs(ServiceChangeHistory[] promoSocs) {
		this.promoSocs = promoSocs;
	}

	public String toString() {

		StringBuffer s = new StringBuffer(128);
		s.append("VendorServiceChangeHistoryInfo:[\n");
		s.append("    VendorServiceCode=[").append(vendorServiceCode).append("]\n");
		s.append("    PromoSOCs=[").append("\n");
		for (int i = 0; i < (promoSocs != null ? promoSocs.length : 0); i++)
			s.append(promoSocs[i].getServiceCode()).append("\n");
		s.append("    ]\n");
		s.append("]");

		return s.toString();
	}
}
