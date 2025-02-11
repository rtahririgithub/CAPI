package com.telus.api.reference;

public interface PrepaidCategory extends Reference {
	public final static String CATEGORY_CODE_CALLING_CIRCLE = "FAV";
	int getPriority();
	Service[] getFeatures();

}
