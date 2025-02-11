package com.telus.api.rules;

/**
 * @author R. Fong
 * @version 1.0, 04-Jul-2008 
 **/
public interface Result {
	
	// category constants
	static final int CATEGORY_MESSAGE_ALL = 0;
	static final int CATEGORY_MESSAGE_POOLING = 1;
	static final int CATEGORY_MESSAGE_MOBILE_APPLICATIONS = 2;
	
	int getCategory();
	
}
