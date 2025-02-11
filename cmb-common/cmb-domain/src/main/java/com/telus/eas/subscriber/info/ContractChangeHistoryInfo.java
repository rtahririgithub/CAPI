package com.telus.eas.subscriber.info;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

import com.telus.api.account.*;

import com.telus.eas.framework.info.*;

import java.util.*;





public class ContractChangeHistoryInfo extends Info implements ContractChangeHistory {


  static final long serialVersionUID = 1L;

  private Date date;

  private Date newCommitmentStartDate;

  private Date newCommitmentEndDate;

  private String reasonCode;

  private String dealerCode;
  
  private String salesRepId;
    
  // Use this to represent new commitment months
  private int newCommitmentMonths;
  
  private String knowbilityOperatorID;
  
  private String applicationID;
  
  
  public ContractChangeHistoryInfo() {

  }



public String getApplicationID() {
	
	return applicationID;
	
}


public String getKnowbilityOperatorID() {
	
	return knowbilityOperatorID;
}


  public Date getDate() {

    return date;

  }



  public Date getNewCommitmentStartDate() {

    return newCommitmentStartDate;

  }



  public Date getNewCommitmentEndDate() {

    return newCommitmentEndDate;

  }



  public String getReasonCode() {

    return reasonCode;

  }



  public String getDealerCode() {

    return dealerCode;

  }

  public int getNewCommitmentMonths() {
  	return newCommitmentMonths;
  }


  public void setDate(Date date) {

    this.date = date;

  }



  public void setNewCommitmentStartDate(Date newCommitmentStartDate) {

    this.newCommitmentStartDate = newCommitmentStartDate;

  }



  public void setNewCommitmentEndDate(Date newCommitmentEndDate) {

    this.newCommitmentEndDate = newCommitmentEndDate;

  }



  public void setReasonCode(String reasonCode) {

    this.reasonCode = reasonCode;

  }



  public void setDealerCode(String dealerCode) {

    this.dealerCode = dealerCode;

  }


  public void setNewCommitmentMonths( int newCommitmentMonths ) {
  	this.newCommitmentMonths = newCommitmentMonths;
  }
  
  public void setApplicationID(String applicationID) {
	
	this.applicationID = applicationID;
}


  public void setKnowbilityOperatorID(String knowbilityOperatorID) {
	
	this.knowbilityOperatorID = knowbilityOperatorID;
}

    public String toString()

    {

        StringBuffer s = new StringBuffer(128);



        s.append("ContractChangeHistoryInfo:[\n");

        s.append("    date=[").append(date).append("]\n");

        s.append("    newCommitmentStartDate=[").append(newCommitmentStartDate).append("]\n");

        s.append("    newCommitmentEndDate=[").append(newCommitmentEndDate).append("]\n");

        s.append("    reasonCode=[").append(reasonCode).append("]\n");

        s.append("    dealerCode=[").append(dealerCode).append("]\n");
        
		s.append("    newCommitmentMonths=[").append(newCommitmentMonths).append("]\n");
		
		s.append("    knowbilityOperatorID=[").append(knowbilityOperatorID).append("]\n");
		
		s.append("    applicationID=[").append(applicationID).append("]\n");
								        
        s.append("    salesRepId=[").append(salesRepId).append("]\n");
        
        s.append("]");



        return s.toString();

    }



/**
 * @return Returns the salesRepId.
 */
public String getSalesRepId() {
	return salesRepId;
}
/**
 * @param salesRepId The salesRepId to set.
 */
public void setSalesRepId(String salesRepId) {
	this.salesRepId = salesRepId;
}
}
