/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */
package com.telus.provider.account;

import java.util.Date;

import com.telus.api.account.ProvisioningTransactionDetail;
import com.telus.eas.subscriber.info.ProvisioningTransactionDetailInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

/**
 * Title:        TMProvisioningTransactionDetail<p>
 * Description:  <p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author R. Fong
 * @version 1.0
 */
public class TMProvisioningTransactionDetail extends BaseProvider implements ProvisioningTransactionDetail {

	/**
	 * @link aggregation
	 */
	private final ProvisioningTransactionDetailInfo delegate;
	
	public TMProvisioningTransactionDetail(TMProvider provider, ProvisioningTransactionDetailInfo delegate) {
		super(provider);
		this.delegate = delegate;
	}
	
	public ProvisioningTransactionDetailInfo getDelegate() {
		return delegate;
	}
	
	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
    public String getStatus() {
        return delegate.getStatus();
    }

    public Date getEffectiveDate() {
        return delegate.getEffectiveDate();
    }

    public String getService() {
        return delegate.getService();
    }

    public String getErrorReason() {
        return delegate.getErrorReason();
    }

}
