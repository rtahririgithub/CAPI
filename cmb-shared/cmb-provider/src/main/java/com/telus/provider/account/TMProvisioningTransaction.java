/*
 * Created on 23-Jun-04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.ProvisioningTransaction;
import com.telus.api.account.ProvisioningTransactionDetail;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.subscriber.info.ProvisioningTransactionDetailInfo;
import com.telus.eas.subscriber.info.ProvisioningTransactionInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

/**
 * @author zhangji
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TMProvisioningTransaction extends BaseProvider implements ProvisioningTransaction {
	private final ProvisioningTransactionInfo delegate;

	public TMProvisioningTransaction(TMProvider provider, ProvisioningTransactionInfo delegate) {
	  super(provider);
	  this.delegate = delegate;
	}
	
	public String getSubscriberId() {
		return delegate.getSubscriberId();
	}
	
	public String getTransactionNo() {
		return delegate.getTransactionNo();
	}
	
	public String getStatus() {
		return delegate.getStatus();
	}
	
	public Date getEffectiveDate(){
		return delegate.getEffectiveDate();
	}
	
	public String getTransactionType() {
		return delegate.getTransactionType();
	}
	
	public String getProductType() {
		return delegate.getProductType();
	}
	
	public String getErrorReason() {
		return delegate.getErrorReason();
	}
	
	public String getUserID() {
		return delegate.getUserID();
	}
	
	/**
	 * Retrieves an array of ProvisioningTransactionDetail objects.
	 *
	 * <P>This method may involve a remote method call.
	 */
	public ProvisioningTransactionDetail[] getDetails() throws TelusAPIException {
	    
	    try {
	    	ProvisioningTransactionDetailInfo[] provisioningTransactionDetailInfo = null;
    		provisioningTransactionDetailInfo = provider.getSubscriberLifecycleFacade().retrieveProvisioningTransactionDetails(getSubscriberId(), getTransactionNo());
	        return decorate(provisioningTransactionDetailInfo);
	    } catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}
	
	/**
	 * Decorate ProvisioningTransactionDetails.
	 */
	private ProvisioningTransactionDetail[] decorate(ProvisioningTransactionDetail[] details) throws TelusAPIException {
	    
	    TMProvisioningTransactionDetail[] tmProvisioningTransactionDetails = new TMProvisioningTransactionDetail[details.length];
	    for (int i=0; i < details.length; i++) {
	        tmProvisioningTransactionDetails[i] = new TMProvisioningTransactionDetail(provider, (ProvisioningTransactionDetailInfo)details[i]);
	    }
	    return tmProvisioningTransactionDetails;
	}
}
