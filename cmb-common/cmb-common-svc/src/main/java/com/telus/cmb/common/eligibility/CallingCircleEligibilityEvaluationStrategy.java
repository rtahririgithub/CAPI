package com.telus.cmb.common.eligibility;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.eligibility.rules.CallingCircleEligibilityEvaluationResult;
import com.telus.eas.framework.eligibility.EvaluationRuleFactory;
import com.telus.eas.framework.eligibility.EvaluationRuleSaxFactory;
import com.telus.eas.framework.eligibility.rules.AccountTypeEvaluationRule;
import com.telus.eas.framework.eligibility.rules.BrandTokenSetEvaluationRule;
import com.telus.eas.framework.eligibility.rules.EvaluationRule;
import com.telus.eas.framework.eligibility.rules.ProductTypeEvaluationRule;

public class CallingCircleEligibilityEvaluationStrategy extends CmbRuleBasedEligibilityEvaluationStrategy<CallingCircleEligibilityEvaluationResult> {
	private static CallingCircleEligibilityEvaluationStrategy inst = null;
	
	private CallingCircleEligibilityEvaluationStrategy() throws ApplicationException {
		this (null);
	}

	private CallingCircleEligibilityEvaluationStrategy(Reader reader) throws ApplicationException {
		EvaluationRuleFactory factory = getSaxFactory(reader);
		evaluationRule = factory.createEvaluationRule();
	}
	
	public static CallingCircleEligibilityEvaluationStrategy getInstance() throws ApplicationException {
		if (inst == null) {
			inst = new CallingCircleEligibilityEvaluationStrategy();
		}
		return inst;
	}
	
	EvaluationRuleFactory getSaxFactory(Reader reader) throws ApplicationException {
		try {
			if (reader == null) {
				return new RuleFactory(new StringReader(getXmlStringFromLdap()));
			}else {
				return new RuleFactory(reader);
			}
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Fail to create CallingCircleEligibilityEvaluationStrategy object", "", e);
		}
	}

	@Override
	String getLdapKey() {
		return "CallingCircleEligibilityXml";
	}
	
	private static class RuleFactory extends EvaluationRuleSaxFactory {
		private static Map<String, Object> rulesRegistry = Collections.synchronizedMap( new HashMap<String, Object>());

		static {
			rulesRegistry.put("evaluation-rules", EvaluationRule.class);
			rulesRegistry.put("account-type-rule", AccountTypeEvaluationRule.class);
			rulesRegistry.put("brand-rule", BrandTokenSetEvaluationRule.class);
			rulesRegistry.put("product-type-rule", ProductTypeEvaluationRule.class);
			rulesRegistry.put("result", CallingCircleEligibilityEvaluationResult.class);
		}

		public RuleFactory(Reader reader) throws Exception {
			super(reader);
		}

		protected Map<String, Object> getRulesRegistry() {
			return rulesRegistry;
		}
	}
}

