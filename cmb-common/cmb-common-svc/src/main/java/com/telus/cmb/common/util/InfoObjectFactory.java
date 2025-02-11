package com.telus.cmb.common.util;

import com.telus.api.account.Subscriber;
import com.telus.eas.subscriber.info.CDPDSubscriberInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.PCSSubscriberInfo;
import com.telus.eas.subscriber.info.PagerSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.TangoSubscriberInfo;


public class InfoObjectFactory {

	public static SubscriberInfo getNewSubscriberInfo(String productType) {
		if (productType.equals(Subscriber.PRODUCT_TYPE_IDEN)) {
			return new IDENSubscriberInfo();
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_PCS)) {
			return new PCSSubscriberInfo();
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_CDPD)) {
			return new CDPDSubscriberInfo();
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_TANGO)) {
			return new TangoSubscriberInfo();
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_PAGER)) {
			return new PagerSubscriberInfo();
		} else {			
			return new SubscriberInfo();
		}		
	}
	

}
