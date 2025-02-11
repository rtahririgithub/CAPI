/*
 * Created on 22-Jun-04
 *
 */
package com.telus.eas.subscriber.info;

import com.telus.api.TelusAPIException;
import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;

/**
 * @author zhangji
 *
 */
public class ProvisioningTransactionInfo extends Info implements ProvisioningTransaction {

	static final long serialVersionUID = 1L;

	private String subscriberId;
	private String transactionNo;
	private String status;
	private Date effectiveDate;
	private String transactionType;
	private String productType;
	private String errorReason;
	private String userID;
	
	public String getTransactionNo() {
		return transactionNo;	
	}
	
	public String getSubscriberId() {
		return subscriberId;	
	}
	
	public String getStatus(){
		return status;
	}
	
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	
	public String getTransactionType(){
		return transactionType;
	}
	
	public String getProductType() {
		return productType;
	}
	
	public String getErrorReason() {
		return errorReason;
	}
	
	public String getUserID() {
		return userID;	
	}
	
	public void setSubscriberId( String subscriberId ) {
		this.subscriberId = subscriberId;
	}
	
	public void setTransactionNo( String transactionNo ) {
		this.transactionNo = transactionNo;
	}
	
	public void setStatus( String status ) {
		this.status = status;
	}
	
	public void setEffectiveDate( Date effectiveDate ) {
		this.effectiveDate = effectiveDate;
	}
	
	public void setTransactionType( String transactionType ) {
		this.transactionType = transactionType;
	}
	
	public void setProductType( String productType ) {
		this.productType = productType;
	}
	
	public void setErrorReason( String errorReason ) {
		this.errorReason = errorReason;
	}
	
	public void setUserID( String userID ) {
		this.userID = userID;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("ProvisioningTransactionInfo:[\n");
		s.append("    subscriberId=[").append(subscriberId).append("]\n");
		s.append("    transactionNo=[").append(transactionNo).append("]\n");
		s.append("    status=[").append(status).append("]\n");
		s.append("    effectiveDate=[").append(effectiveDate).append("]\n");
		s.append("    transactionType=[").append(transactionType).append("]\n");
		s.append("    productType=[").append(productType).append("]\n");   
		s.append("    errorReason=[").append(errorReason).append("]\n");
		s.append("    userID=[").append(userID).append("]\n");
		s.append("]");
		
		return s.toString();
	}	
	
	public ProvisioningTransactionDetail[] getDetails() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
}
