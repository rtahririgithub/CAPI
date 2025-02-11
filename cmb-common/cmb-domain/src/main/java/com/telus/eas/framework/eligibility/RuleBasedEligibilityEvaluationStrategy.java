package com.telus.eas.framework.eligibility;

import com.telus.eas.framework.eligibility.rules.EvaluationRule;

public abstract class RuleBasedEligibilityEvaluationStrategy implements EligibilityCheckStrategy  {

	protected EvaluationRule evaluationRule = null;

	public Object evaluate(EligibilityCheckCriteria criteria) {
		EvaluationResult result = evaluationRule.evaluate(criteria); 
		
//		System.out.println("result evaluation path: \n" + evaluationPathToString(result.getEvaluationPath()));
		
		return result.getEligibilityCheckResult();
	}

//	private String evaluationPathToString(EvaluationRule [] path) {
//		StringBuffer buffer = new StringBuffer();
//		
//		for (int idx = 0; idx < path.length; idx++) {
//				
//				buffer.append('[').append(idx).append(']');	
//				char [] padch = new char [idx * 2 + 1];
//				Arrays.fill(padch, '-');
//				buffer.append(padch).append(">");
//				buffer.append('[').append(path[idx].toString()).append(']');
//				buffer.append('\n');
////			buffer.append('[').append(path[idx].getName()).append(']');
//		}
//		
//		return buffer.toString();
//	}

}
