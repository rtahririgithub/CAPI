package com.telus.provider.account;

import com.telus.api.account.TaxExemption;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

import java.util.Date;

public class TMSubscriberTaxExemption extends BaseProvider implements TaxExemption {
    /**
     * @link aggregation
     */
    private final SubscriberInfo subscriber;

    /**
     * Constructor
     *
     * @param provider
     * @param subscriber
     */
    public TMSubscriberTaxExemption(TMProvider provider, SubscriberInfo subscriber) {
        super(provider);
        this.subscriber = subscriber;
    }

    /**
     * Determines if subscriber is GST exempt
     *
     * @return boolean
     */
    public boolean isGSTExempt() {
        return subscriber.isGSTExempt();
    }

    /**
     * Determines if subscriber is PST exempt
     *
     * @return boolean
     */
    public boolean isPSTExempt() {
        return subscriber.isPSTExempt();
    }

    /**
     * Determines if subscriber is HST exempt
     *
     * @return boolean
     */
    public boolean isHSTExempt() {
        return subscriber.isHSTExempt();
    }

    /**
     * Gets GST exemption expiry date
     *
     * @return Date
     */
    public Date getGSTExemptExpiryDate() {
        return subscriber.getGSTExemptionExpiryDate();
    }

    /**
     * Gets PST exemption expiry date
     *
     * @return Date
     */
    public Date getPSTExemptExpiryDate() {
        return subscriber.getPSTExemptionExpiryDate();
    }

    /**
     * Gets HST exemption expiry date
     *
     * @return Date
     */
    public Date getHSTExemptExpiryDate() {
        return subscriber.getHSTExemptionExpiryDate();
    }

    public Date getGSTExemptEffectiveDate() {
        return subscriber.getGSTExemptEffectiveDate();
    }

    public Date getPSTExemptEffectiveDate() {
        return subscriber.getPSTExemptEffectiveDate();
    }

    public Date getHSTExemptEffectiveDate() {
        return subscriber.getHSTExemptEffectiveDate();
    }

    public String getGSTCertificateNumber() {
        return subscriber.getGSTCertificateNumber();
    }

    public String getPSTCertificateNumber() {
        return subscriber.getPSTCertificateNumber();
    }

    public String getHSTCertificateNumber() {
        return subscriber.getHSTCertificateNumber();
    }
    
    public void isGSTExempt(boolean val) {
    	
    }
      
    public void isPSTExempt(boolean val) {
  
    }
      
    public void isHSTExempt(boolean val) {
    
    }
      
    public void setGSTExemptEffectiveDate(Date effDate){

    }
      
    public void setPSTExemptEffectiveDate(Date effDate){

    }
      
    public void setHSTExemptEffectiveDate(Date effDate) {

    }
      
    public void setGSTExemptExpiryDate(Date expDate) {

    }
      
    public void setPSTExemptExpiryDate(Date expDate) {

    }
      
    public void setHSTExemptExpiryDate(Date expDate) {

    }
      
    public void setGSTCertificateNumber(String certificate) {

    }  

}
