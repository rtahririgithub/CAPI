package com.telus.eas.framework.eligibility.interservice;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.telus.eas.framework.eligibility.EvaluationRuleSaxFactory;
import com.telus.eas.framework.eligibility.rules.AccountTypeEvaluationRule;
import com.telus.eas.framework.eligibility.rules.BrandEvaluationRule;
import com.telus.eas.framework.eligibility.rules.CreditClassEvaluationRule;
import com.telus.eas.framework.eligibility.rules.EvaluationRule;
import com.telus.eas.framework.eligibility.interservice.impl.rules.AccountLifecycleEvaluationRule;
import com.telus.eas.framework.eligibility.interservice.impl.rules.CollectionActivityEvaluationRule;
import com.telus.eas.framework.eligibility.interservice.impl.rules.InternationalServiceEvaluationResultRule;
import com.telus.eas.framework.eligibility.interservice.impl.rules.TenureEvaluationRule;

public class InternationalServiceEvaluationRuleSaxFactory extends EvaluationRuleSaxFactory {
	private static Map rulesRegistry = Collections.synchronizedMap( new HashMap());

	static {
		rulesRegistry.put("evaluation-rules", EvaluationRule.class);
		rulesRegistry.put("account-type-rule", AccountTypeEvaluationRule.class);
		rulesRegistry.put("brand-rule", BrandEvaluationRule.class);
		rulesRegistry.put("credit-class-rule", CreditClassEvaluationRule.class);
		rulesRegistry.put("collection-activity-rule", CollectionActivityEvaluationRule.class);
		rulesRegistry.put("account-lifecycle-rule", AccountLifecycleEvaluationRule.class);
		rulesRegistry.put("tenure-rule", TenureEvaluationRule.class);
		rulesRegistry.put("result", InternationalServiceEvaluationResultRule.class);
	}

	public InternationalServiceEvaluationRuleSaxFactory(Reader reader) throws Exception {
		super(reader);
	}

	protected Map getRulesRegistry() {
		return rulesRegistry;
	}

}
