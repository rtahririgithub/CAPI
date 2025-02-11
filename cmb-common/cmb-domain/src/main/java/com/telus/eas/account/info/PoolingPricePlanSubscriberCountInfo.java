/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.PoolingPricePlanSubscriberCount;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.eas.framework.info.Info;

public class PoolingPricePlanSubscriberCountInfo extends Info implements PoolingPricePlanSubscriberCount {

	public static final long serialVersionUID = 7215727533501515874L;
  
	// constant representation of all pooling groups used for PoolingPricePlanSubscriberCountInfo data retrieval
	public static final int POOLING_GROUP_ALL = -1;

	private int poolingGroupId;
	private PricePlanSubscriberCountInfo[] pricePlanSubscriberCounts;
 
	public PoolingPricePlanSubscriberCountInfo() {
	}
  
	public int getPoolingGroupId() {
		return poolingGroupId;
	}
  
	public void setPoolingGroupId(int poolingGroupId) {
		this.poolingGroupId = poolingGroupId;
	}
  
	public PricePlanSubscriberCount[] getPricePlanSubscriberCount() {
		return pricePlanSubscriberCounts;
	}
    
	public void setPricePlanSubscriberCount(PricePlanSubscriberCountInfo[] pricePlanSubscriberCounts) {
		this.pricePlanSubscriberCounts = pricePlanSubscriberCounts;
	}
  
}




