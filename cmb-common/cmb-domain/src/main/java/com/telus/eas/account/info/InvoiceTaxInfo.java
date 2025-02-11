package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.Info;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class InvoiceTaxInfo extends Info implements InvoiceTax {

	static final long serialVersionUID = 1L;

	private boolean gstExempt;
	private boolean pstExempt;
	private boolean hstExempt;
	private boolean roamingExempt;
	private ChargeTypeTax[] chargeTypeTaxes;

	public boolean isGSTExempt() {
		return this.gstExempt;
	}

	public boolean isPSTExempt() {
		return this.pstExempt;
	}

	public boolean isHSTExempt() {
		return this.hstExempt;
	}

	public boolean isRoamingExempt() {
		return this.roamingExempt;
	}

	public ChargeTypeTax[] getChargeTypeTaxes() {
		return this.chargeTypeTaxes;
	}

	public void setGSTExempt(boolean gstExempt) {
		this.gstExempt = gstExempt;
	}

	public void setPSTExempt(boolean pstExempt) {
		this.pstExempt = pstExempt;
	}

	public void setHSTExempt(boolean hstExempt) {
		this.hstExempt = hstExempt;
	}

	public void setRoamingExempt(boolean roamingExempt) {
		this.roamingExempt = roamingExempt;
	}

	public void setChargeTypeTaxes(ChargeTypeTax[] chargeTypeTaxes) {
		this.chargeTypeTaxes = chargeTypeTaxes;
	}

	public String toString() {

		StringBuffer s = new StringBuffer(128);
		s.append("InvoiceTaxInfo:[\n");
		s.append("    gstExempt=[").append(String.valueOf(gstExempt)).append("]\n");
		s.append("    pstExempt=[").append(String.valueOf(pstExempt)).append("]\n");
		s.append("    hstExempt=[").append(String.valueOf(hstExempt)).append("]\n");
		s.append("    roamingExempt=[").append(String.valueOf(roamingExempt)).append("]\n");
		if (chargeTypeTaxes == null || chargeTypeTaxes.length == 0) {
			s.append("    chargeTypeTaxes={}\n");
		} else {
			for (int i = 0; i < chargeTypeTaxes.length; i++) {
				s.append("    chargeTypeTaxes[" + i + "]=[").append(chargeTypeTaxes[i]).append("]\n");
			}
		}
		s.append("]");
		return s.toString();
	}
}
