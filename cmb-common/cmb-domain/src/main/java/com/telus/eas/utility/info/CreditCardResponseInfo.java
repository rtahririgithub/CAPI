package com.telus.eas.utility.info;

/**
 * Title:        CreditCardResponseInfo class
 * Description:  The class credit card transaction response returned by the bank.
 * Copyright:    Copyright (c) 2003
 * Company:      Telus Mobility
 * @author Stefan Pavlov
 * @version 1.0
 */
import com.telus.eas.framework.info.*;

public class CreditCardResponseInfo extends Info {

  static final long serialVersionUID = 1L;

  private String authorizationCode; // bank authorization code
  private String referenceNum;      // bank reference code
  
  private String responseCode;
  private String responseText;
  private boolean isApproved;
  private String cardVerificationDataResult;



public CreditCardResponseInfo() {
     authorizationCode = "";
     referenceNum = "";
  }

 public String getAuthorizationCode() {
    return authorizationCode;
  }

  public String getReferenceNum() {
    return referenceNum;
  }

  public void setAuthorizationCode(String authorizationCode) {
    this.authorizationCode = authorizationCode;
  }

  public void setReferenceNum(String referenceNum) {
    this.referenceNum = referenceNum;
  }

 public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("CreditCardResponseInfo:{\n");
    s.append("    isApproved=[").append(isApproved).append("]\n");
    s.append("    authorizationCode=[").append(authorizationCode).append("]\n");
    s.append("    referenceNum=[").append(referenceNum).append("]\n");
    s.append("}");

    return s.toString();
  }

  public String getResponseCode() {
	return responseCode;
  }

  public void setResponseCode(String responseCode) {
	this.responseCode = responseCode;
  }

  public String getResponseText() {
	return responseText;
  }

  public void setResponseText(String reponseText) {
	this.responseText = reponseText;
  }

  public boolean isApproved() {
		return isApproved;
	  }

  public void setApproved(boolean isApproved) {
	this.isApproved = isApproved;
  }

  public String getCardVerificationDataResult() {
		return cardVerificationDataResult;
	}

	public void setCardVerificationDataResult(String cardVerificationDataResult) {
		this.cardVerificationDataResult = cardVerificationDataResult;
	}

}