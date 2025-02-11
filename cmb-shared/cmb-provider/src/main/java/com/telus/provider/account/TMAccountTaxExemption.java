package com.telus.provider.account;

import java.util.Date;

import com.telus.api.account.TaxExemption;
import com.telus.eas.account.info.AccountInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMAccountTaxExemption extends BaseProvider implements TaxExemption{

	private static final long serialVersionUID = 1L;
	private final AccountInfo account;


	public TMAccountTaxExemption(TMProvider provider, AccountInfo account) {
		super(provider);
		this.account = account;
	}

  public boolean isGSTExempt(){return account.isGSTExempt();}
  public Date getGSTExemptExpiryDate(){return account.getGSTExemptExpiryDate();}
  public boolean isPSTExempt(){return account.isPSTExempt();}
  public Date getPSTExemptExpiryDate(){return account.getPSTExemptExpiryDate();}
  public boolean isHSTExempt(){return account.isHSTExempt();}
  public Date getHSTExemptExpiryDate(){return account.getHSTExemptExpiryDate();}

  public Date getGSTExemptEffectiveDate() {
    return account.getGSTExemptEffectiveDate();
  }

  public Date getPSTExemptEffectiveDate() {
    return account.getPSTExemptEffectiveDate();
  }

  public Date getHSTExemptEffectiveDate() {
    return account.getHSTExemptEffectiveDate();
  }

  public String getGSTCertificateNumber() {
    return account.getGSTCertificateNumber();
  }

  public String getPSTCertificateNumber() {
    return account.getPSTCertificateNumber();
  }

  public String getHSTCertificateNumber() {
    return account.getHSTCertificateNumber();
  }

  public void isGSTExempt(boolean val) {
	account.setGstExempt(val ? (byte)'Y' : (byte)'N');
  }
  
  public void isPSTExempt(boolean val) {
	account.setPstExempt(val ? (byte)'Y' : (byte)'N');
  }
  
  public void isHSTExempt(boolean val) {
	account.setHstExempt(val ? (byte)'Y' : (byte)'N');
  }
  
  public void setGSTExemptEffectiveDate(Date effDate){
	account.setGSTExemptEffectiveDate(effDate);
  }
  
  public void setPSTExemptEffectiveDate(Date effDate){
	account.setPSTExemptEffectiveDate(effDate);
  }
  
  public void setHSTExemptEffectiveDate(Date effDate) {
	account.setHSTExemptEffectiveDate(effDate);
  }
  
  public void setGSTExemptExpiryDate(Date expDate) {
	account.setGSTExemptExpiryDate(expDate);
  }
  
  public void setPSTExemptExpiryDate(Date expDate) {
	account.setPSTExemptExpiryDate(expDate);
  }
  
  public void setHSTExemptExpiryDate(Date expDate) {
	account.setHSTExemptExpiryDate(expDate);
  }
  
  public void setGSTCertificateNumber(String certificate) {
	account.setGSTCertificateNumber(certificate);
  }

}
