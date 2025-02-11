package com.telus.provider.rules;

import java.util.HashMap;
import java.util.Iterator;

import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.ConditionInfo;
import com.telus.eas.utility.info.ResultInfo;
import com.telus.eas.utility.info.RuleInfo;

public class Rule extends RuleTemplate {

	private RuleInfo delegate;
	
	public Rule(RuleInfo delegate) {
		super(delegate);
		this.delegate = delegate;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public ResultInfo getResult() {
		return delegate.getResult();
	}
	
	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public boolean evaluate(RuleInfo testRule) {

		// check that the test rule and conditions are not null
		if (testRule == null || testRule.getConditions() == null)
			return false;
		
		// map the conditions to HashMaps to make them easier to work with
		HashMap conditionsMap = mapConditions(getConditions());
		HashMap testConditionsMap = mapConditions(testRule.getConditions());
		
		// check if there are any conditions to be tested
		if (testConditionsMap.isEmpty())
			return false;
		
		// compare the number of conditions between the delegate and the testRule
		if (testConditionsMap.size() != conditionsMap.size())
			return false;
		
		// iterate through the list of rule conditions
		Iterator iterator = conditionsMap.keySet().iterator();
		while (iterator.hasNext()) {			
			// get the test condition to compare with the rule condition, using condition ID to match them
			String conditionId = (String)iterator.next();
			ConditionInfo condition = (ConditionInfo)conditionsMap.get(conditionId);
		    ConditionInfo testCondition = (ConditionInfo)testConditionsMap.get(conditionId);
		    
		    // check if the condition types are different
		    if (testCondition.getConditionType() != condition.getConditionType())
		    	return false;

		    // based on condition type, make the required evaluation
		    switch (condition.getConditionType()) {
		    case RuleConstants.CONDITION_TYPE_TEXT:
		    	if (!Info.compare(testCondition.getValue(), condition.getValue()))
		    		return false;
		    	break;

		    case RuleConstants.CONDITION_TYPE_BOOLEAN:
		    	if (!Info.compare(testCondition.getValue(), condition.getValue()))
		    		return false;
		    	break;
		    	
		    case RuleConstants.CONDITION_TYPE_DATE_RANGE:
		    	if (!Info.intersects(testCondition.getFromDate(), condition.getFromDate(),
		    			condition.getToDate()))
		    		return false;
		    	break;
		    	
		    case RuleConstants.CONDITION_TYPE_NUMBER_RANGE:
		    	if (!Info.intersects(testCondition.getFromAmount(), condition.getFromAmount(),
		    			condition.getToAmount()))
		    		return false;
		    	break;
		    	
		    default:
		    	// if we've reached here, then the condition type is unsupported, so return false
		    	return false;
		    }	
		}
		// if we've reached this point, every condition must match, so return true
		return true;
	}
	
	private HashMap mapConditions(ConditionInfo[] conditions) {
		
		HashMap map = new HashMap(conditions.length);
		for (int i = 0; i < conditions.length; i++) {
			map.put(conditions[i].getCode(), conditions[i]);
		}
		
		return map;
	}
}
