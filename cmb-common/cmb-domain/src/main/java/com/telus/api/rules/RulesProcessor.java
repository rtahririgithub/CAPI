package com.telus.api.rules;

import com.telus.api.TelusAPIException;

/**
 * @author R. Fong
 * @version 1.0, 04-Jul-2008 
 **/
public interface RulesProcessor {
	
	static final int PROCESSOR_ID_MESSAGE = 1;
	
	Result[] processRules(Context processingContext) throws TelusAPIException;
	
}
