package com.telus.cmb.common.eligibility;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.telus.cmb.common.eligibility.rules.CurrentDeviceEvaluationRule;
import com.telus.cmb.common.eligibility.rules.CurrentSimEvaluationRule;
import com.telus.cmb.common.eligibility.rules.NewDeviceEvaluationRule;
import com.telus.eas.framework.eligibility.EvaluationRuleSaxFactory;
import com.telus.eas.framework.eligibility.rules.AccountSubTypeEvaluationRule;
import com.telus.eas.framework.eligibility.rules.AccountTypeEvaluationRule;
import com.telus.eas.framework.eligibility.rules.BrandEvaluationRule;
import com.telus.eas.framework.eligibility.rules.EvaluationRule;

@SuppressWarnings("unchecked")
public class EsimDeviceSwapEligibilityEvaluationRuleSaxFactory extends EvaluationRuleSaxFactory {

	private static Map rulesRegistry = Collections.synchronizedMap(new HashMap());

	static {
		rulesRegistry.put("evaluation-rules", EvaluationRule.class);
		rulesRegistry.put("current-sim-rule", CurrentSimEvaluationRule.class);
		rulesRegistry.put("current-device-rule", CurrentDeviceEvaluationRule.class);
		rulesRegistry.put("new-device-rule", NewDeviceEvaluationRule.class);
		rulesRegistry.put("result", EsimDeviceSwapEligibilityCheckResult.class);
	}

	public EsimDeviceSwapEligibilityEvaluationRuleSaxFactory(Reader reader) throws Exception {
		super(reader);
	}

	@Override
	protected Map getRulesRegistry() {
		return rulesRegistry;
	}
}
