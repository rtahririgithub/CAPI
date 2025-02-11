/**
 * Title:        ActivationCreditInfo<p>
 * Description:  The ActivationCreditInfo holds all attributes for a Activationcredit.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

package com.telus.eas.framework.info;

import com.telus.api.*;
import com.telus.api.account.*;
import java.util.*;

public class ActivationCreditInfo extends CreditInfo implements ActivationCredit {

  static final long serialVersionUID = 1L;

  private String offerCode;
  private Date expiryDate;
  private String barCode;
  private int contractTermMonths;
  private String creditType;
  private String descriptionEnglish;
  private String descriptionFrench;

  public ActivationCreditInfo() {
    super();
    this.offerCode = "";
    this.expiryDate = null;
    this.barCode = "";
    this.contractTermMonths = 0;
    this.creditType = "";
    this.descriptionEnglish = "";
    this.descriptionFrench = "";
  }
  
  public ActivationCreditInfo(char taxOption) {
    super(taxOption);
    this.offerCode = "";
    this.expiryDate = null;
    this.barCode = "";
    this.contractTermMonths = 0;
    this.creditType = "";
    this.descriptionEnglish = "";
    this.descriptionFrench = "";
  }

  public String getBarCode() {
    return this.barCode;
  }

  public void setBarCode(String barCode)
  {
    this.barCode = barCode;
  }

  public int getContractTerm() {
    return this.contractTermMonths;
  }

  public void setContractTerm(int contractTermMonths)
  {
    this.contractTermMonths = contractTermMonths ;
  }

  public Date getExpiryDate() {
    return this.expiryDate;
  }

  public void setExpiryDate(Date expiryDate){
    this.expiryDate = expiryDate;
  }

  public boolean isContractTermCredit() {
    return (ActivationCredit.CREDIT_TYPE_CONTRACT_TERM.equals(this.creditType));
  }

  public boolean isNewActivationCredit() {
    return (ActivationCredit.CREDIT_TYPE_NEW_ACTIVATION.equals(this.creditType));
  }

  public boolean isPromotionCredit() {
    return (ActivationCredit.CREDIT_TYPE_PROMOTION.equals(this.creditType));
  }

  public boolean isPricePlanCredit() {
    return (ActivationCredit.CREDIT_TYPE_PRICE_PLAN.equals(this.creditType));
  }

  @Deprecated
  public boolean isFidoCredit() {
      return (ActivationCredit.CREDIT_TYPE_FIDO.equals(this.creditType));
    }
  public String getCode() {
    return this.creditType;
  }

  public void setCode(String creditType) {
    this.creditType = creditType;
  }

  public String getCreditType() {
    return this.creditType;
  }

  public void setCreditType(String creditType) {
    this.creditType = creditType;
  }

  public String getDescription() {
    return this.descriptionEnglish;
  }

  public void setDescription(String descriptionEnglish) {
    this.descriptionEnglish = descriptionEnglish;
  }

  public String getDescriptionFrench()
  {
    return this.descriptionFrench;
  }

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }
    
  public String getOfferCode() {
	return offerCode;
  }

  public void setOfferCode(String offerCode) {
	this.offerCode = offerCode;
  }

  public String toString()
  {
        StringBuffer s = new StringBuffer(128);
        s.append("ActivationCreditInfo: [ extends - CreditInfo:[\n");
        s.append(super.toString());

        if (this.offerCode == null) {
            s.append("    offerCode=[null]\n");
        }
        else {
        	s.append("    offerCode=[").append(this.offerCode).append("]\n");
        }        
        if (this.expiryDate == null) {
          s.append("    expiryDate=[null]\n");
        }
        else {
          s.append("    expiryDate=[").append(this.expiryDate.toString()).append("]\n");
        }
        s.append("    barcode=[").append(this.barCode).append("]\n");
        s.append("    creditType=[").append(this.creditType).append("]\n");
        s.append("    contractTermMonths=[").append(this.contractTermMonths).append("]\n");
        s.append("    descriptionEnglish=[").append(this.descriptionEnglish).append("]\n");
        s.append("    descriptionFrench=[").append(this.descriptionFrench).append("]\n");
        s.append("]");
        return s.toString();
  }
  public void apply(int ban, String subscriberId) throws TelusAPIException{
    throw new TelusAPIException("Method not implemented here");
  }
  public void apply(int ban) throws TelusAPIException{
    throw new TelusAPIException("Method not implemented here");
  }
  public void apply() throws TelusAPIException{
    throw new TelusAPIException("Method not implemented here");
  }
  public void memo(int ban, String subscriberId) throws TelusAPIException{
    throw new TelusAPIException("Method not implemented here");
  }
  public void memo(int ban) throws TelusAPIException{
    throw new TelusAPIException("Method not implemented here");
  }
  public void memo() throws TelusAPIException{
    throw new TelusAPIException("Method not implemented here");
  }

}


