package com.telus.cmb.common.eligibility;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.telus.eas.framework.eligibility.EvaluationRuleSaxFactory;
import com.telus.eas.framework.eligibility.rules.AccountSubTypeEvaluationRule;
import com.telus.eas.framework.eligibility.rules.AccountTypeEvaluationRule;
import com.telus.eas.framework.eligibility.rules.BrandEvaluationRule;
import com.telus.eas.framework.eligibility.rules.EvaluationRule;

@SuppressWarnings("unchecked")
public class CommunicationSuiteEvaluationRuleSaxFactory extends EvaluationRuleSaxFactory {
	
	private static Map rulesRegistry = Collections.synchronizedMap(new HashMap());

	static {
		rulesRegistry.put("evaluation-rules", EvaluationRule.class);
		rulesRegistry.put("account-type-rule", AccountTypeEvaluationRule.class);
		rulesRegistry.put("account-sub-type-rule", AccountSubTypeEvaluationRule.class);
		rulesRegistry.put("brand-rule", BrandEvaluationRule.class);
		rulesRegistry.put("result",CommunicationSuiteEligibilityCheckResult.class);
	}

	public CommunicationSuiteEvaluationRuleSaxFactory(Reader reader) throws Exception {
		super(reader);
	}

	@Override
	protected Map getRulesRegistry() {
		return rulesRegistry;
	}

}
