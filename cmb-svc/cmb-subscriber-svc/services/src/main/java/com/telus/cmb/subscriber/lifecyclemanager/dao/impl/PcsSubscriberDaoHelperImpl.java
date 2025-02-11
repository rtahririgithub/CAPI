package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;

class PcsSubscriberDaoHelperImpl extends SubscriberDaoImpl{
	
	private SubscriberLifecycleHelper subscriberLifecycleHelper;
	
	public SubscriberLifecycleHelper getSubscriberLifecycleHelper() {
		if (subscriberLifecycleHelper == null) {
			subscriberLifecycleHelper = EJBUtil.getLocalStatelessProxy(SubscriberLifecycleHelper.class, EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER );
		}
		return subscriberLifecycleHelper;
	}
}
