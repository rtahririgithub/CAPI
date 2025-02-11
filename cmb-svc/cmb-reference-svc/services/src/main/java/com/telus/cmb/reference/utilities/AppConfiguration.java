package com.telus.cmb.reference.utilities;

import java.util.HashMap;
import java.util.List;

import com.telus.cmb.common.util.ArrayUtil;
import com.telus.cmb.common.util.BaseAppConfiguration;

public class AppConfiguration extends BaseAppConfiguration {
	private static HashMap<String,String> billCycleProvinceRestrictions = new HashMap<String, String>();
	private static HashMap<String, String> defaultDealerCodeMap;
	private static HashMap<String, String> defaultSalesRepCodeMap;

	public static boolean isEnterpriseDataSyncDisabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.DISABLE_ASYNC_PUBLISH_ENTERPRISE_DATA, true);
	}
	
	public static String getEhcacheConfig() {
		return getConfigurationManager().getStringValue(LdapKeys.EHCACHE_CONFIG);
	}
	
	public static String getCacheClearSchedule() {
		return getConfigurationManager().getStringValue(LdapKeys.CACHE_CLEAR_SCHEDULE, "");
	}
	
	public static String getCacheRefreshSchedule() {
		return getConfigurationManager().getStringValue(LdapKeys.CACHE_REFRESH_SCHEDULE, "0 0 4 * * ?");
	}
	
	public static String getRefpdsRefreshSchedule() {
		return getConfigurationManager().getStringValue(LdapKeys.REFPDS_REFRESH_SCHEDULE, "0 30 4 * * ?");
	}

    public static String getLogicalDateRefreshSchedule() {
        return getConfigurationManager().getStringValue(LdapKeys.LOGICALDATE_REFRESH_SCHEDULE, "0 0 2 * * ?");
    }
    
    public static int getLogicalDateRefreshDuration() {
        return getConfigurationManager().getIntegerValue( LdapKeys.LOGICALDATE_REFRESH_DURATION, 3600 );
    }
    
    public static int getLogicalDateRefreshTimeout() {
        return getConfigurationManager().getIntegerValue(LdapKeys.LOGICALDATE_REFRESH_TIMEOUT, 60);
    }
    
	public static HashMap<String, String> getBillCycleProvinceRestrictions() {
		if (billCycleProvinceRestrictions.size() == 0) {
			List<String> billCycleRestrictions = getConfigurationManager().getStringValues(LdapKeys.BILLCYCLES_PROVINCE_RESTRICTIONS);
			try {
				billCycleProvinceRestrictions = ArrayUtil.convertKeyValuePairsToHashMap(billCycleRestrictions, ":");
			}catch(ArrayIndexOutOfBoundsException ae){
				// Improper data in LDAP for the entry BillCyclesProvinceRestrictions, Nothing to update
			}
		}
		
		return billCycleProvinceRestrictions;
	}
	
	public static HashMap<String, String> getDefaultDealerCodeMap() {
		if (defaultDealerCodeMap == null) {
			List<String> dealerCodeArray = getConfigurationManager().getStringValues(LdapKeys.DEFAULT_DEALER_CODE);
			defaultDealerCodeMap = ArrayUtil.convertKeyValuePairsToHashMap(dealerCodeArray, ",");
		}
		
		return defaultDealerCodeMap;
	}
	
	public static HashMap<String, String> getDefaultSalesRepCodeMap() {
		if (defaultSalesRepCodeMap == null) {
			List<String> salesRepCodeArray = getConfigurationManager().getStringValues(LdapKeys.DEFAULT_SALESREP_CODE);
			defaultSalesRepCodeMap = ArrayUtil.convertKeyValuePairsToHashMap(salesRepCodeArray, ",");
		}
		
		return defaultSalesRepCodeMap;
	}
	
	public static String getDefaultKoodoDealerCode() {
		return getConfigurationManager().getStringValue(LdapKeys.DEFAULT_KOODO_DEALER);
	}
	
	public static String getDefaultKoodoSalesRepCode() {
		return getConfigurationManager().getStringValue(LdapKeys.DEFAULT_KOODO_SALESREP);
	}
	
	public static String getInvoiceSuppressoinLevelUpdateLevels() {
		return getConfigurationManager().getStringValue(LdapKeys.INVOICE_SUPPRESSION_LEVEL_UPDATE_LEVELS);
	}

	public static boolean isPrepaidLargeBalanceRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.PREPAID_LARGE_BALANCE_ROLLBACK, false).booleanValue();
	}
	
	public static boolean isKeystoneRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.KEYSTONE_ROLLBACK, false).booleanValue();
	}
	
	public static boolean isRefEjbCacheStrategyRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.CACHE_STRATEGY_ROLLBACK, false).booleanValue();
	}	

	public static boolean isTallboyComboPlanRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.TALLBOY_COMBOPLAN_ROLLBACK, false).booleanValue(); 
	}
	
}
