package com.telus.cmb.common.eligibility;

import java.io.Reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.cmb.framework.config.ConfigurationManagerFactory;
import com.telus.eas.framework.eligibility.DefaultEligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.EvaluationRuleFactory;
import com.telus.eas.framework.eligibility.RuleBasedEligibilityEvaluationStrategy;

public abstract class CmbRuleBasedEligibilityEvaluationStrategy<T> extends
		RuleBasedEligibilityEvaluationStrategy {
	private static final Log logger = LogFactory.getLog(CmbRuleBasedEligibilityEvaluationStrategy.class);

	@SuppressWarnings("unchecked")
	public T checkEligibility (DefaultEligibilityCheckCriteria criteria) throws ApplicationException {
		Object checkResult = null;
		try {
			logger.debug(criteria);
			checkResult = (T)evaluate(criteria);
		} catch (Exception e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.LDAP_CONFIGURATION_ERROR, "Failed to retrieve " + getLdapKey() + " from LDAP.", e);
		}

		if (checkResult != null ) {
			return (T)checkResult;
		} else { 
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.LDAP_CONFIGURATION_ERROR, "Result is null. Failed to retrieve " + getLdapKey() + " from LDAP.");
		}
	}
	
	protected String getXmlStringFromLdap() throws ApplicationException {
		return ConfigurationManagerFactory.getInstance().getStringValue(getLdapKey());
	}
	
	abstract String getLdapKey();
	abstract EvaluationRuleFactory getSaxFactory(Reader reader)  throws Exception;
}
