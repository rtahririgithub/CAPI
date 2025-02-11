package com.telus.cmb.common.eligibility;

import java.io.Reader;
import java.io.StringReader;

import com.telus.eas.framework.eligibility.EvaluationRuleFactory;

public class EnterpriseManagementEligibilityEvaluationStrategy extends CmbRuleBasedEligibilityEvaluationStrategy<Boolean> {
	private static EnterpriseManagementEligibilityEvaluationStrategy instance;
	
	private EnterpriseManagementEligibilityEvaluationStrategy() throws Exception {
		this(null);
	}

	private EnterpriseManagementEligibilityEvaluationStrategy(Reader reader) throws Exception {
		EvaluationRuleFactory factory = getSaxFactory(reader);
		evaluationRule = factory.createEvaluationRule();
	}
	
	public static EnterpriseManagementEligibilityEvaluationStrategy getInstance() throws Exception {
		if (instance == null) {
			instance = new EnterpriseManagementEligibilityEvaluationStrategy();
		}
		return instance;
	}
	
	EvaluationRuleFactory getSaxFactory(Reader reader) throws Exception {
		if (reader == null) {
			return new EnterpriseManagementEligibilityEvaluationRuleSaxFactory(new StringReader(getXmlStringFromLdap()));
		}else {
			return new EnterpriseManagementEligibilityEvaluationRuleSaxFactory(reader);
		}
	}
	
	@Override
	String getLdapKey() {
		return "EnterpriseDataEligibilityXml";
	}

}
