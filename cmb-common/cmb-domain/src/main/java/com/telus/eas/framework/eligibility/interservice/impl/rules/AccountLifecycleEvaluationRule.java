package com.telus.eas.framework.eligibility.interservice.impl.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.interservice.InternationalServiceEligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.rules.ReferenceComparisonEvaluationRule;


public class AccountLifecycleEvaluationRule extends ReferenceComparisonEvaluationRule {

		public AccountLifecycleEvaluationRule() {
			super(OP_EQUAL);
		}
		
		public void setNewAccount(Boolean newAccount) {
			setReferenceValue(newAccount);
		}
		
		/* (non-Javadoc)
		 * @see com.telus.provider.eligibility.interservice.impl.rules.ReferenceValueEvaluationRule#getEvaluationValue(com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria)
		 */
		protected Object getEvaluationValue(EligibilityCheckCriteria criteria) {
			return new Boolean(((InternationalServiceEligibilityCheckCriteria)criteria).isNewAccount());
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "Account lifecycle reference value : [is new account = " + getReferenceValue();
		}
		

}
