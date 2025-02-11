package com.telus.cmb.common.eligibility;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.telus.cmb.common.eligibility.rules.EnterpriseManagementEligibilityEvaluationResultRule;
import com.telus.cmb.common.eligibility.rules.ProcessTypeEvaluationRule;
import com.telus.eas.framework.eligibility.EvaluationRuleSaxFactory;
import com.telus.eas.framework.eligibility.rules.AccountTypeEvaluationRule;
import com.telus.eas.framework.eligibility.rules.BrandEvaluationRule;
import com.telus.eas.framework.eligibility.rules.EvaluationRule;
import com.telus.eas.framework.eligibility.rules.ProductTypeEvaluationRule;

public class EnterpriseManagementEligibilityEvaluationRuleSaxFactory extends EvaluationRuleSaxFactory {
	private static Map rulesRegistry = Collections.synchronizedMap( new HashMap());

	static {
		rulesRegistry.put("evaluation-rules", EvaluationRule.class);
		rulesRegistry.put("account-type-rule", AccountTypeEvaluationRule.class);
		rulesRegistry.put("brand-rule", BrandEvaluationRule.class);
		rulesRegistry.put("product-type-rule", ProductTypeEvaluationRule.class);
		rulesRegistry.put("process-type-rule", ProcessTypeEvaluationRule.class);
		rulesRegistry.put("result", EnterpriseManagementEligibilityEvaluationResultRule.class);
	}

	public EnterpriseManagementEligibilityEvaluationRuleSaxFactory(Reader reader) throws Exception {
		super(reader);
	}

	@Override
	protected Map getRulesRegistry() {
		return rulesRegistry;
	}

}
