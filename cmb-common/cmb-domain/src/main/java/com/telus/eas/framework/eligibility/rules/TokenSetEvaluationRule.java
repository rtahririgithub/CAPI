package com.telus.eas.framework.eligibility.rules;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;

public abstract class TokenSetEvaluationRule extends ConditionalEvaluationRule {

	private static final String TYPE_DELIMITER = ", ";
	private static final String ALL_TYPES = "*";
	
	protected Set tokens = new HashSet();

	public void setTokens(String token) {
		StringTokenizer tokenizer = new StringTokenizer(token, TYPE_DELIMITER);
		while (tokenizer.hasMoreTokens()) { 
			tokens.add(tokenizer.nextToken());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.telus.provider.eligibility.interservice.impl.rules.ConditionalEvaluationRule#matchCondition(com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria)
	 */
	protected boolean matchCondition(EligibilityCheckCriteria criteria) {
		return tokens.contains(ALL_TYPES) ? true : checkToken(getTargetToken(criteria));
	}

	private boolean checkToken(String targetToken) {
		return targetToken == null ? false : matchToken(targetToken.trim());
	}

	private boolean matchToken(String targetToken) {
		Iterator iter = tokens.iterator();
		while (iter.hasNext()) {
			String token = (String) iter.next();
			if (targetToken.startsWith(token)) {
				return true;
			}
		}
		return false;
	}
	
	protected abstract String getTargetToken(EligibilityCheckCriteria criteria);



}
