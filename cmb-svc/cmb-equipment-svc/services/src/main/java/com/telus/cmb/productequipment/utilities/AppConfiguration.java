package com.telus.cmb.productequipment.utilities;

import java.util.ArrayList;
import java.util.List;

import com.telus.cmb.common.util.BaseAppConfiguration;

public class AppConfiguration extends BaseAppConfiguration {

	public static List<String> getPricePlanCreditFamilyPlans() {
		return getConfigurationManager().getStringValues(LdapKeys.PRICE_PLAN_CREDIT_FAMILY_PLANS, ";", new ArrayList<String>());
	}

	public static boolean isUseAcme() {
		return getConfigurationManager().getBooleanValue(LdapKeys.USE_ACME, Boolean.FALSE);
	}

	public static String getEquipmentInfoServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.EQUIPMENT_INFO_SERVICE);
	}

	public static String getEqupimentLifecycleManagementServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.EQUIPMENT_LIFECYCLE_MGMT_SERVICE);
	}

	public static String getP3MSproductFacadeEjbUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.P3MS_PRODUCT_FACADE_URL);
	}

	public static String getNrtEligibilityManagerEJBUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.NRT_ELIGIBILITY_MANAGER_EJB_URL);
	}

	public static boolean isIDBProjectRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.IDB_ROLLBACK_FLAG, Boolean.FALSE);
	}

	public static boolean isSurepayRetirementRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.SUREPAY_RETIREMENT_ROLLBACK, false);
	}
	
	public static boolean isEsimSupportEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ENABLE_ESIM_SUPPORT, Boolean.TRUE);
	}
}
