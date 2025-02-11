package com.telus.cmb.jws.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.telus.api.account.Subscriber;
import com.telus.api.reference.Brand;

public class AppConfiguration extends WSBaseAppConfiguration {
	// public consts
	public final static String POSTPAID = "O";
	public final static String PREPAID = "R";
	// private consts
	private final static String STRING_TOKENIZER_DEFAULT_REGEXP = " |\t|\n|\r|\f";
	
	private static Map<String, String> interBrandPortActivityReasonCodesKeyMap;
	private static Set<String> interBrandPortActivityReasonCodesSet;
	
	public static synchronized Map<String, String> getInterBrandPortActivityReasonCodesKeyMap() {
		if (interBrandPortActivityReasonCodesKeyMap == null) {
			Map<String, String> hashMap = new HashMap<String, String>();
			try {
				String valueFromLdap = getConfigurationManager().getStringValue(LdapKeys.INTERBRAND_PORT_ACTIVITY_REASON_CODES_KEYS);
				String[] ldapKeyValuePairs = valueFromLdap.split(STRING_TOKENIZER_DEFAULT_REGEXP);
				for(String keyValuePair : ldapKeyValuePairs) {
					String[] pair = keyValuePair.split(",");
					hashMap.put(pair[0], pair[1]);
				}
			} catch (Throwable t) {
				// use these as default values
				hashMap.put(String.valueOf(Brand.BRAND_ID_TELUS) + Subscriber.PRODUCT_TYPE_PCS + POSTPAID, "PO");
				hashMap.put(String.valueOf(Brand.BRAND_ID_TELUS) + Subscriber.PRODUCT_TYPE_PCS + PREPAID, "PR");
				hashMap.put(String.valueOf(Brand.BRAND_ID_AMPD) + Subscriber.PRODUCT_TYPE_PCS + POSTPAID, "PO");
				hashMap.put(String.valueOf(Brand.BRAND_ID_AMPD) + Subscriber.PRODUCT_TYPE_PCS + PREPAID, "PR");
				hashMap.put(String.valueOf(Brand.BRAND_ID_TELUS) + Subscriber.PRODUCT_TYPE_IDEN + POSTPAID, "MK");
				hashMap.put(String.valueOf(Brand.BRAND_ID_TELUS) + Subscriber.PRODUCT_TYPE_IDEN + PREPAID, "MK");
				hashMap.put(String.valueOf(Brand.BRAND_ID_KOODO) + Subscriber.PRODUCT_TYPE_PCS + POSTPAID, "KO");
				hashMap.put(String.valueOf(Brand.BRAND_ID_KOODO) + Subscriber.PRODUCT_TYPE_PCS + PREPAID, "KR");
				hashMap.put(String.valueOf(Brand.BRAND_ID_CLEARNET) + Subscriber.PRODUCT_TYPE_PCS + POSTPAID, "CL");
				hashMap.put(String.valueOf(Brand.BRAND_ID_WALMART) + Subscriber.PRODUCT_TYPE_PCS + POSTPAID, "WM");
			}
			interBrandPortActivityReasonCodesKeyMap = hashMap;
		}
		return interBrandPortActivityReasonCodesKeyMap;
	}
	
	public static Set<String> getInterBrandPortActivityReasonCodes() {
		if (interBrandPortActivityReasonCodesSet == null) {
			Set<String> set = new HashSet<String>();

			try {
				String ldapValue = getConfigurationManager().getStringValue(LdapKeys.INTERBRAND_PORT_ACTIVITY_REASON_CODES);
				String[] stringArray = ldapValue.toUpperCase().split(STRING_TOKENIZER_DEFAULT_REGEXP);

				for (int i = 0; i < stringArray.length; i++) {
					set.add(stringArray[i]);
				}
			} catch (Throwable t) {
				// use these as default values
				set.add("POKO");
				set.add("PRKO");
				set.add("KOPO");
				set.add("KOPR");
				set.add("MKKO");
				set.add("KOMK");
				set.add("CLKO");
				set.add("KOCL");
				set.add("CLMK");
				set.add("MKCL");
				set.add("CLPO");
				set.add("POCL");
				set.add("CLPR");
				set.add("PRCL");
				set.add("CLWM");
				set.add("WMCL");
				set.add("WMKO");
				set.add("KOWM");
				set.add("WMMK");
				set.add("MKWM");
				set.add("WMPO");
				set.add("POWM");
				set.add("WMPR");
				set.add("PRWM");

			}
			interBrandPortActivityReasonCodesSet = set;
		}

		return interBrandPortActivityReasonCodesSet;
	}

}
