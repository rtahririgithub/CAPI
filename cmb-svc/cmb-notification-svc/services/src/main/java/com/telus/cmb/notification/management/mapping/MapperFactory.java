package com.telus.cmb.notification.management.mapping;

public class MapperFactory {
	private final static AccountTransactionEmailTemplateMapper accountMapper_v1 = new com.telus.cmb.notification.management.mapping.customer_customerorder_accountinfo_10.AccountTransactionMapper();
	private final static AccountTransactionEmailTemplateMapper accountMapper_v2 = new com.telus.cmb.notification.management.mapping.customer_customerorder_accountinfo_20.AccountTransactionMapper();
	private final static AccountTransactionEmailTemplateMapper accountMapper_v3 = new com.telus.cmb.notification.management.mapping.customer_customerorder_accountinfo_30.AccountTransactionMapper();

	private final static SubscriberTransactionEmailTemplateMapper subscriberMapper_v1 = new com.telus.cmb.notification.management.mapping.customer_customerorder_accountinfo_10.SubscriberTransactionMapper();
	private final static SubscriberTransactionEmailTemplateMapper subscriberMapper_v2 = new com.telus.cmb.notification.management.mapping.customer_customerorder_accountinfo_20.SubscriberTransactionMapper();
	
	public static AccountTransactionEmailTemplateMapper getAccountTransactionMapper(String schemaVersion) {
		if ("3.0".equals(schemaVersion)) {
			return accountMapper_v3;
		}else if ("2.0".equals(schemaVersion)) {
			return accountMapper_v2;
		}else {
			return accountMapper_v1;
		}
	}
	
	public static SubscriberTransactionEmailTemplateMapper getSubscriberTransactionMapper(String schemaVersion) {
		if ("2.0".equals(schemaVersion)) {
			return subscriberMapper_v2;
		}else {
			return subscriberMapper_v1;
		}
	}
}
