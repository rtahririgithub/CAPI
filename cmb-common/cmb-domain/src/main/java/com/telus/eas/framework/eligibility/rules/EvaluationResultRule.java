package com.telus.eas.framework.eligibility.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.EvaluationResult;

public abstract class EvaluationResultRule extends EvaluationRule implements EvaluationResult  {

	public EvaluationResult evaluate(EligibilityCheckCriteria criteria) {
		return this;
	}

	public EvaluationRule [] getEvaluationPath() {
		
		List path = new ArrayList();
		
		EvaluationRule rule = this;
		while (rule != null) {
			path.add(rule);
			rule = rule.getParent();
		}
		
		Collections.reverse(path);
		
		return (EvaluationRule []) path.toArray( new EvaluationRule [path.size()]);
	}
}
