package com.telus.eas.framework.eligibility.interservice.impl.rules;

import java.io.Reader;
import java.io.StringReader;

import com.telus.eas.framework.eligibility.EvaluationRuleFactory;
import com.telus.eas.framework.eligibility.RuleBasedEligibilityEvaluationStrategy;
import com.telus.eas.framework.eligibility.interservice.InternationalServiceEvaluationRuleSaxFactory;

public class InternationalServiceEligibilityEvaluationStrategy extends RuleBasedEligibilityEvaluationStrategy  {

	public InternationalServiceEligibilityEvaluationStrategy(String xml) throws Exception {
		this( new StringReader(xml));
	}

	public InternationalServiceEligibilityEvaluationStrategy(Reader reader) throws Exception {
		EvaluationRuleFactory factory = new InternationalServiceEvaluationRuleSaxFactory(reader);
		evaluationRule = factory.createEvaluationRule();
	}

}
