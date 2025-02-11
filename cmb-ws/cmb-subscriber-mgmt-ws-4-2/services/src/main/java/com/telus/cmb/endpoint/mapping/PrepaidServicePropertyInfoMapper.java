/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint.mapping;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.PrepaidServicePropertyInfo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PrepaidPropertyListType;

public class PrepaidServicePropertyInfoMapper extends AbstractSchemaMapper<PrepaidPropertyListType, PrepaidServicePropertyInfo> {

	public PrepaidServicePropertyInfoMapper() {
		super(PrepaidPropertyListType.class, PrepaidServicePropertyInfo.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	protected PrepaidServicePropertyInfo performDomainMapping(PrepaidPropertyListType source, PrepaidServicePropertyInfo target) {
		if (source.getAutoRenewPropertyList() != null) {
			if (source.getAutoRenewPropertyList().getRenewalFundSource() != null) {
				target.setAutoRenewalFundSource(source.getAutoRenewPropertyList().getRenewalFundSource().intValue());
			}
			target.setAutoRenewalInd(source.getAutoRenewPropertyList().isAutoRenewInd());
		}
		target.setPurchaseFundSource(source.getPurchaseFundSource());
		return super.performDomainMapping(source, target);
	}
}