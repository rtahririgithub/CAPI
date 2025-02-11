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

import java.util.Date;

import com.telus.api.reference.ServiceSummary;

import com.telus.api.TelusAPIException;





public class ServiceChangeHistoryInfo extends Info implements ServiceChangeHistory {

  static final long serialVersionUID = 1L;

  private Date date;

  private String serviceCode;

  private Date newExpirationDate;
  
  private String knowbilityOperatorID;
  
  private String applicationID;
  
  private String dealerCode;
  
  private String salesRepId;



  public ServiceChangeHistoryInfo() {

  }



public String getApplicationID() {
	
	return applicationID;
}


public String getKnowbilityOperatorID() {
	
	return knowbilityOperatorID;
}


  public Date getDate(){

    return date;

  }



  public String getServiceCode(){

    return serviceCode;

  }



  public ServiceSummary getService() throws TelusAPIException{

    throw new java.lang.UnsupportedOperationException("Method not implemented.");

  }



  public Date getNewExpirationDate(){

    return newExpirationDate;

  }



  public void setDate(Date date){

    this.date = date;

  }



  public void setServiceCode(String serviceCode){

    this.serviceCode = serviceCode;

  }



  public void setNewExpirationDate(Date newExpirationDate){

    this.newExpirationDate = newExpirationDate;

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



        s.append("ServiceChangeHistoryInfo:[\n");

        s.append("    date=[").append(date).append("]\n");

        s.append("    serviceCode=[").append(serviceCode).append("]\n");

        s.append("    newExpirationDate=[").append(newExpirationDate).append("]\n");
        
        s.append("    knowbilityOperatorID=[").append(knowbilityOperatorID).append("]\n");
		
		s.append("    applicationID=[").append(applicationID).append("]\n");
        
        s.append("    dealerCode=[").append(dealerCode).append("]\n");
        
        s.append("    salesRepId=[").append(salesRepId).append("]\n");

        s.append("]");



        return s.toString();

    }



/**
 * @return Returns the dealerCode.
 */
public String getDealerCode() {
	return dealerCode;
}
/**
 * @param dealerCode The dealerCode to set.
 */
public void setDealerCode(String dealerCode) {
	this.dealerCode = dealerCode;
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
