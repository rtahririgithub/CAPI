package com.telus.cmb.common.eligibility;

import java.io.Reader;
import java.io.StringReader;
import com.telus.eas.framework.eligibility.EvaluationRuleFactory;

public class CommunicationSuiteEligibilityCheckStrategy extends CmbRuleBasedEligibilityEvaluationStrategy<Boolean>
{
	private static CommunicationSuiteEligibilityCheckStrategy instance;

	private CommunicationSuiteEligibilityCheckStrategy() throws Exception {
		this(null);
	}

	private CommunicationSuiteEligibilityCheckStrategy(Reader reader) throws Exception {
		EvaluationRuleFactory factory = getSaxFactory(reader);
		evaluationRule = factory.createEvaluationRule();
	}

	public static CommunicationSuiteEligibilityCheckStrategy getInstance() throws Exception {
		if (instance == null) {
			instance = new CommunicationSuiteEligibilityCheckStrategy();
		}
		return instance;
	}

	 EvaluationRuleFactory getSaxFactory(Reader reader) throws Exception {
		if (reader == null) {
			return new CommunicationSuiteEvaluationRuleSaxFactory(new StringReader(getXmlStringFromLdap()));
		} else {
			return new CommunicationSuiteEvaluationRuleSaxFactory(reader);
		}
	}

	@Override
	 String getLdapKey() {
		return "CommunicationSuiteEligibilityXml";
	}

}
