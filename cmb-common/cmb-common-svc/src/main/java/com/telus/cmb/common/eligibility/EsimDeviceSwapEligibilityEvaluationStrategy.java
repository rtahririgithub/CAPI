package com.telus.cmb.common.eligibility;

import java.io.Reader;
import java.io.StringReader;
import com.telus.eas.framework.eligibility.EvaluationRuleFactory;

public class EsimDeviceSwapEligibilityEvaluationStrategy extends CmbRuleBasedEligibilityEvaluationStrategy<Boolean> {
	private static EsimDeviceSwapEligibilityEvaluationStrategy instance;
	private static String lDAP_KEY_ESIM_DEVICE_SWAP_ELIGIBILITY_XML = "EsimDeviceSwapEligibilityXml";
	
	private EsimDeviceSwapEligibilityEvaluationStrategy() throws Exception {
		this(null);
	}

	private EsimDeviceSwapEligibilityEvaluationStrategy(Reader reader) throws Exception {
		EvaluationRuleFactory factory = getSaxFactory(reader);
		evaluationRule = factory.createEvaluationRule();
	}

	public static EsimDeviceSwapEligibilityEvaluationStrategy getInstance() throws Exception {
		if (instance == null) {
			instance = new EsimDeviceSwapEligibilityEvaluationStrategy();
		}
		return instance;
	}

	EvaluationRuleFactory getSaxFactory(Reader reader) throws Exception {
		if (reader == null) {
			return new EsimDeviceSwapEligibilityEvaluationRuleSaxFactory(new StringReader(getXmlStringFromLdap()));
		} else {
			return new EsimDeviceSwapEligibilityEvaluationRuleSaxFactory(reader);
		}
	}

	@Override
	 String getLdapKey() {
		return lDAP_KEY_ESIM_DEVICE_SWAP_ELIGIBILITY_XML;
	}
}
