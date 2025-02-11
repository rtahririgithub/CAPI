package com.telus.provider.rules;

import com.telus.eas.utility.info.ConditionInfo;
import com.telus.eas.utility.info.RuleInfo;

public class RuleTemplate {

	private RuleInfo delegate;

	public RuleTemplate(RuleInfo delegate) {
		this.delegate = delegate;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public String getCode() {
		return delegate.getCode();
	}
	
	public long getId() {
		return delegate.getId();
	}

	public String getName() {
		return delegate.getName();
	}	

	public String getDescription() {
		return delegate.getDescription();
	}

	public int getType() {
		return delegate.getType();
	}

	public int getRole() {
		return delegate.getRole();
	}

	public int getCategory() {
		return delegate.getCategory();
	}

	public ConditionInfo[] getConditions() {
		return delegate.getConditions();
	}
	
	/**
	 * This method will only allow conditions to be set if the role type is equal to 
	 * RuleInfo.ROLE_TYPE_TEST_RULE.  Otherwise, nothing happens.
	 * 
	 * @param ConditionInfo[] conditions
	 */
	public void setConditions(ConditionInfo[] conditions) {
		if (getRole() == RuleConstants.ROLE_TYPE_TEST_RULE)
			delegate.setConditions(conditions);
	}

	public String toString() {
		return delegate.toString();
	}
	
	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public RuleInfo getDelegate() {
		return delegate;
	}
	
	/**
	 * A test rule is simply an instance of a rule template, with a cloned delegate.  This delegate has
	 * a role type of RuleInfo.ROLE_TYPE_TEST_RULE and can be modified (i.e., new conditions set into the
	 * test rule delegate) in preparation for evaluation against a rule.
	 * 
	 * @return RuleTemplate
	 */
	public RuleTemplate getTestRule() {

		// get the template conditions
		ConditionInfo[] templateConditions = delegate.getConditions();
		
		// clone the template conditions
		ConditionInfo[] testConditions = new ConditionInfo[templateConditions.length];
		for (int i = 0; i < templateConditions.length; i++) {
			testConditions[i] = (ConditionInfo)templateConditions[i].clone();
		}	

		// clone the template
		RuleInfo testRuleInfo = (RuleInfo)delegate.clone();
		
		// set the cloned conditions into the cloned template
		testRuleInfo.setConditions(testConditions);
		
		// set the role of the cloned template
		testRuleInfo.setRole(RuleConstants.ROLE_TYPE_TEST_RULE);
		
		return new RuleTemplate(testRuleInfo);
	}

}
