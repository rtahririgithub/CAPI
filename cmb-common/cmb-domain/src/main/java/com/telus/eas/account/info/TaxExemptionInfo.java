package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.account.TaxExemption;
import com.telus.eas.framework.info.Info;

public class TaxExemptionInfo extends Info implements TaxExemption{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean pstExemptionInd;
	private boolean gstExemptionInd;
	private boolean hstExemptionInd;
	
	public boolean isGSTExempt() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public boolean isPSTExempt() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public boolean isHSTExempt() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public Date getGSTExemptEffectiveDate() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public Date getPSTExemptEffectiveDate() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public Date getHSTExemptEffectiveDate() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public Date getGSTExemptExpiryDate() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public Date getPSTExemptExpiryDate() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public Date getHSTExemptExpiryDate() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public String getGSTCertificateNumber() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public String getPSTCertificateNumber() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public String getHSTCertificateNumber() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void isGSTExempt(boolean val) {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void isPSTExempt(boolean val) {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void isHSTExempt(boolean val) {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void setGSTExemptEffectiveDate(Date effDate) {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void setPSTExemptEffectiveDate(Date effDate) {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void setHSTExemptEffectiveDate(Date effDate) {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void setGSTExemptExpiryDate(Date expDate) {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void setPSTExemptExpiryDate(Date expDate) {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void setHSTExemptExpiryDate(Date expDate) {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void setGSTCertificateNumber(String certificate) {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public boolean isPstExemptionInd() {
		return pstExemptionInd;
	}

	public void setPstExemptionInd(boolean pstExemptionInd) {
		this.pstExemptionInd = pstExemptionInd;
	}

	public boolean isGstExemptionInd() {
		return gstExemptionInd;
	}

	public void setGstExemptionInd(boolean gstExemptionInd) {
		this.gstExemptionInd = gstExemptionInd;
	}

	public boolean isHstExemptionInd() {
		return hstExemptionInd;
	}

	public void setHstExemptionInd(boolean hstExemptionInd) {
		this.hstExemptionInd = hstExemptionInd;
	}

}
