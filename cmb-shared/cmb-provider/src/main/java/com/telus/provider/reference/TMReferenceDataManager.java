/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.reference;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.telus.api.BrandNotSupportedException;
import com.telus.api.InvalidServiceException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentPossession;
import com.telus.api.fleet.FleetClass;
import com.telus.api.reference.*;
import com.telus.api.util.RemoteBeanProxy;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.eas.utility.info.CollectionStateInfo;
import com.telus.eas.utility.info.CountryInfo;
import com.telus.eas.utility.info.CoverageRegionInfo;
import com.telus.eas.utility.info.DataSharingGroupInfo;
import com.telus.eas.utility.info.DiscountPlanInfo;
import com.telus.eas.utility.info.LineRangeInfo;
import com.telus.eas.utility.info.NotificationMessageTemplateInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.NumberRangeInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.PricePlanSelectionCriteriaInfo;
import com.telus.eas.utility.info.PricePlanTermInfo;
import com.telus.eas.utility.info.ProvisioningPlatformTypeInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServicePolicyInfo;
import com.telus.eas.utility.info.ServiceRelationInfo;
import com.telus.eas.utility.info.ServiceUsageInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.cache.CacheKeyProvider;
import com.telus.provider.cache.CacheLoader;
import com.telus.provider.cache.CacheManager;
import com.telus.provider.cache.CacheReflectiveLoader;
import com.telus.provider.cache.CacheStaticDataLoader;
import com.telus.provider.cache.DataCache;
import com.telus.provider.cache.DataCache.CacheElement;
import com.telus.provider.cache.DataEntryCache;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.Logger;

/**
 * @author Pavel Simonovsky
 *
 */
public class TMReferenceDataManager extends BaseProvider  implements ReferenceDataManager {

	private static final long serialVersionUID = 1L;
	
	public static final String NUMBER_LOCATION_POSTPAID = "TLS";
	public static final String NUMBER_LOCATION_PREPAID = "PPE";
	
	public static final int DATA_ENTRY_CACHE_TIME_TO_LIVE = 7200000; // 2 hours
	public static final int DATA_ENTRY_CACHE_IDLE_TIME = 0;
	public static final int CACHE_EVICTION_RATE = 900000; // 15 minutes

	private ReferenceDataHelper referenceDataHelper;
	private ReferenceDataFacade referenceDataFacade;
	
	private boolean useLocalCache = false;
	private boolean useLocalPPCache = false;
	
	private CacheManager cacheManager = CacheManager.getInstance();
	private CacheKeyProvider referenceKeyProvider = new CacheKeyProvider() {

		@Override
		public String getKey(Object object) {
			return ((Reference) object).getCode();
		}
	};
	
	private Calendar refreshTrigger;

	public TMReferenceDataManager(TMProvider provider, ReferenceDataHelper referenceDataHelper, ReferenceDataFacade referenceDataFacade) {
		super(provider);
		this.referenceDataHelper = referenceDataHelper;
		this.referenceDataFacade = referenceDataFacade;
			
		useLocalCache = AppConfiguration.isUseLocalCache();
		Logger.debug("Using " + (useLocalCache ? "local" : "remote") + " caching of reference data.");
		
		
		if (useLocalCache) {
			// group cache
//			cacheManager.createDataGroupCache(Service.class, "regular", new CacheReflectiveLoader(referenceDataFacade, "getRegularServices"), referenceKeyProvider);
			cacheManager.createDataGroupCache(AccountType.class, new CacheReflectiveLoader(referenceDataFacade, "getAccountTypes"), referenceKeyProvider);
			cacheManager.createDataGroupCache(Brand.class, new CacheReflectiveLoader(referenceDataFacade, "getBrands"), referenceKeyProvider);
			cacheManager.createDataGroupCache(Country.class, "all", new CacheStaticDataLoader(CountryInfo.getAll()), referenceKeyProvider);
			cacheManager.createDataGroupCache(Country.class, "foreign", new CacheReflectiveLoader(referenceDataHelper, "retrieveCountries"), referenceKeyProvider);
			cacheManager.createDataGroupCache(PricePlanTermInfo.class, new CacheReflectiveLoader(referenceDataHelper, "retrievePricePlanTerms"), referenceKeyProvider);
			cacheManager.createDataGroupCache(ServiceExclusionGroups.class, new CacheReflectiveLoader(referenceDataHelper, "retrieveServiceExclusionGroups"), referenceKeyProvider);
			cacheManager.createDataGroupCache(ApplicationSummary.class, new CacheReflectiveLoader(referenceDataHelper, "retrieveApplicationSummaries"), referenceKeyProvider);
			cacheManager.createDataGroupCache(NumberRangeInfo.class, new CacheReflectiveLoader(referenceDataHelper, "retrieveNumberRanges"), referenceKeyProvider);
			cacheManager.createDataGroupCache(PoolingGroup.class, new CacheReflectiveLoader(referenceDataHelper, "retrievePoolingGroups"), referenceKeyProvider);
			cacheManager.createDataGroupCache(ServicePolicyInfo.class, new CacheReflectiveLoader(referenceDataHelper, "retrieveServicePolicyExceptions"), referenceKeyProvider);
			cacheManager.createDataGroupCache(DataSharingGroupInfo.class, new CacheReflectiveLoader(referenceDataFacade, "getDataSharingGroups"), referenceKeyProvider);
			
			// entry cache
			cacheManager.createDataEntryCache(Service.class);
			cacheManager.createDataEntryCache(ServiceUsageInfo.class);
		}
		
		useLocalPPCache = AppConfiguration.isUseLocalPPCache();
		Logger.debug("Using " + (useLocalPPCache ? "local" : "remote") + " caching of price plan.");
		
		if (useLocalPPCache) { 
			cacheManager.createDataEntryCache(PricePlanInfo.class);
		}
		
		if ( useLocalCache || useLocalPPCache ) {
			scheduleLocalCacheRefresh();
		}
	}
	
	private void scheduleLocalCacheRefresh() {
		
		final int cacheEvictionRate = getIntSystemProperty("cacheEvictionRate", CACHE_EVICTION_RATE);
		final int dataEntryCacheTimeToLive = getIntSystemProperty("dataEntryTimeToLive", DATA_ENTRY_CACHE_TIME_TO_LIVE);
		final int dataEntryIdleTime = getIntSystemProperty("dataEntryIdleTime", DATA_ENTRY_CACHE_IDLE_TIME);
		
		Logger.debug("Local cache settings: cacheEvictionRate = [" + cacheEvictionRate + "] ms, dataEntryCacheTimeToLive = [" + dataEntryCacheTimeToLive + "] ms, dataEntryIdleTime = [" + dataEntryIdleTime + "] ms.");

		refreshTrigger = Calendar.getInstance();
		
		refreshTrigger.add(Calendar.DATE, 1);
		refreshTrigger.set(Calendar.HOUR_OF_DAY, 4);
		refreshTrigger.set(Calendar.MINUTE, 35);
		refreshTrigger.set(Calendar.SECOND, 0);
		
		Thread thread = new Thread("ReferenceDataRefreshThread") {
			
			@Override
			public void run() {
				
				Logger.debug("Next cache refresh time is [" + refreshTrigger.getTime() + "]");

				while (true) {
					try {
						if (System.currentTimeMillis() > refreshTrigger.getTimeInMillis()) {
							refresh();
							refreshTrigger.add(Calendar.DATE, 1);
							Logger.debug("Next cache refresh time is [" + refreshTrigger + "]");
						}
						sleep(cacheEvictionRate);
						
						if ( isUseLocalPPCache() ) {
							//refresh price plan cache start
							DataEntryCache ppCache = cacheManager.getDataEntryCache(PricePlanInfo.class);
							long startTimestamp = System.currentTimeMillis();
							CacheElement[] cacheElements = ppCache.evictCacheElements(dataEntryCacheTimeToLive, dataEntryIdleTime);
							long finishTimestamp = System.currentTimeMillis();
							
							Logger.debug(new Date() + ": evicted [" + cacheElements.length + "] elements in [" + (finishTimestamp - startTimestamp) + "] msec. Number of elements in PricePlan cache: [" + ppCache.getSize() + "]");
							//refresh price plan cache end
						}
						
					} catch (Throwable e) {
						Logger.debug("Error refreshing reference data: " + e.getMessage());
					}
				}
			};
		};
		thread.setDaemon(true);
		thread.start();
	}
	
	private int getIntSystemProperty(String propertyName, int defaultValue) {
		int value = defaultValue;
		String propertyValue = System.getProperty(propertyName);
		if (propertyValue != null) {
			try {
				value = Integer.parseInt(propertyValue);
			} catch (Throwable t) {
				System.err.println("Property [" + propertyName + "] parsing error [" + propertyValue + "]: " + t.getMessage());
			}
		}
		return value;
	}

	public ReferenceDataHelper getReferenceDataHelperEJB() {
		return referenceDataHelper;
	}

	public ReferenceDataHelper getReferenceDataHelper() {
		return referenceDataHelper;
	}

	public ReferenceDataFacade getReferenceDataFacade() {
		return referenceDataFacade;
	}
	
	private boolean isUseLocalCache() {
		return useLocalCache;
	}
	
	private boolean isUseLocalPPCache() {
		return useLocalPPCache;
	}

	@Override
	protected void finalize() throws Throwable {
		RemoteBeanProxy helperProxy = (RemoteBeanProxy) Proxy.getInvocationHandler(referenceDataHelper);
		helperProxy.dispose();
		
		RemoteBeanProxy facadeProxy = (RemoteBeanProxy) Proxy.getInvocationHandler(referenceDataFacade);
		facadeProxy.dispose();
	}

	protected TelusAPIException translateException(String methodName, Throwable t) throws TelusAPIException {
		String message = "Error in ReferenceDataManager." + methodName + "(): " + t.getMessage();
		Logger.debug(message);
		return new TelusAPIException(message, t);
	}
	
	@Override
	public Department[] getDepartments() throws TelusAPIException {
		try {
			return referenceDataFacade.getDepartments(); 
		} catch (Throwable throwable) {
			throw translateException("getDepartments", throwable);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.api.reference.ReferenceDataManager#getInvoiceSuppressionLevels()
	 */
	@Override
	public InvoiceSuppressionLevel[] getInvoiceSuppressionLevels() throws TelusAPIException {
		try {
			return referenceDataFacade.getInvoiceSuppressionLevels();
		} catch (Throwable throwable) {
			throw translateException("getInvoiceSuppressionLevels", throwable);
		}
	}

	@Override
	public Title[] getTitles() throws TelusAPIException {
		try {
			return referenceDataFacade.getTitles();
		} catch (Throwable throwable) {
			throw translateException("getTitles", throwable);
		}
	}

	@Override
	public Title[] getAllTitles() throws TelusAPIException {
		try {
			return referenceDataFacade.getAllTitles();
		} catch (Throwable throwable) {
			throw translateException("getAllTitles", throwable);
		}
	}

	@Override
	public State[] getStates() throws TelusAPIException {
		try {
			return referenceDataFacade.getStates();
		} catch (Throwable throwable) {
			throw translateException("getStates", throwable);
		}
	}

	@Override
	public FollowUpType[] getFollowUpTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getFollowUpTypes();
		} catch (Throwable throwable) {
			throw translateException("getFollowUpTypes", throwable);
		}
	}

	@Override
	public FollowUpType[] getAllFollowUpTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getAllFollowUpTypes();
		} catch (Throwable throwable) {
			throw translateException("getAllFollowUpTypes", throwable);
		}
	}

	@Override
	public Network[] getNetworks() throws TelusAPIException {
		try {
			return referenceDataFacade.getNetworks();
		} catch (Throwable throwable) {
			throw translateException("getNetworks", throwable);
		}
	}

	@Override
	public AccountType[] getAccountTypes() throws TelusAPIException {
		try {
			if (isUseLocalCache()) { 
				return (AccountType[]) cacheManager.getDataGroupCache(AccountType.class).getAll();
			} else {
				return referenceDataFacade.getAccountTypes();
			}
		} catch (Throwable throwable) {
			throw translateException("getAccountTypes", throwable);
		}
	}

	@Override
	public CoverageRegion[] getCoverageRegions() throws TelusAPIException {
		try {
			return decorate(referenceDataFacade.getCoverageRegions());
		} catch (Throwable throwable) {
			throw translateException("getCoverageRegions", throwable);
		}
	}

	@Override
	public ServiceExclusionGroups[] getServiceExclusionGroups() throws TelusAPIException {
		try {
			if (isUseLocalCache()) {
				return (ServiceExclusionGroups[]) cacheManager.getDataGroupCache(ServiceExclusionGroups.class).getAll();
			} else {
				return referenceDataFacade.getServiceExclusionGroups();
			}
		} catch (Throwable throwable) {
			throw translateException("getServiceExclusionGroups", throwable);
		}
	}

	@Override
	public EncodingFormat[] getEncodingFormats() throws TelusAPIException {
		try {
			return referenceDataFacade.getEncodingFormats();
		} catch (Throwable throwable) {
			throw translateException("getEncodingFormats", throwable);
		}
	}

	@Override
	public Province[] getProvinces() throws TelusAPIException {
		try {
			return referenceDataFacade.getProvinces();
		} catch (Throwable throwable) {
			throw translateException("getProvinces", throwable);
		}
	}

	@Override
	public Province[] getAllProvinces() throws TelusAPIException {
		try {
			return referenceDataFacade.getAllProvinces();
		} catch (Throwable throwable) {
			throw translateException("getAllProvinces", throwable);
		}
	}

	@Override
	public Province[] getProvinces(String countryCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getProvinces(countryCode);
		} catch (Throwable throwable) {
			throw translateException("getProvinces", throwable);
		}
	}

	@Override
	public CreditMessage[] getCreditMessages() throws TelusAPIException {
		try {
			return referenceDataFacade.getCreditMessages();
		} catch (Throwable throwable) {
			throw translateException("getCreditMessages", throwable);
		}
	}

	@Override
	public CreditCardType[] getCreditCardTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getCreditCardTypes();
		} catch (Throwable throwable) {
			throw translateException("getCreditCardTypes", throwable);
		}
	}

	@Override
	public CreditCardPaymentType[] getCreditCardPaymentTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getCreditCardPaymentTypes();
		} catch (Throwable throwable) {
			throw translateException("getCreditCardPaymentTypes", throwable);
		}
	}

	@Override
	public Language[] getLanguages() throws TelusAPIException {
		try {
			return referenceDataFacade.getLanguages();
		} catch (Throwable throwable) {
			throw translateException("getLanguages", throwable);
		}
	}

	@Override
	public Language[] getAllLanguages()  throws TelusAPIException{
		try {
			return referenceDataFacade.getAllLanguages();
		} catch (Throwable throwable) {
			throw translateException("getAllLanguages", throwable);
		}
	}

	@Override
	public ServicePeriodType[] getServicePeriodTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getServicePeriodTypes();
		} catch (Throwable throwable) {
			throw translateException("getServicePeriodTypes", throwable);
		}
	}

	@Override
	public PaymentMethod[] getPaymentMethods() throws TelusAPIException {
		try {
			return referenceDataFacade.getPaymentMethods();
		} catch (Throwable throwable) {
			throw translateException("getPaymentMethods", throwable);
		}
	}

	@Override
	public PaymentMethodType[] getPaymentMethodTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getPaymentMethodTypes();
		} catch (Throwable throwable) {
			throw translateException("getPaymentMethodTypes", throwable);
		}
	}

	@Override
	public Country[] getCountries() throws TelusAPIException {
		return getCountries(false);
	}

	@Override
	public Country[] getCountries(boolean includeForeign) throws TelusAPIException {
		try {
			if (isUseLocalCache()) {
				if (includeForeign) {
					return (Country[]) cacheManager.getDataGroupCache(Country.class, "foreign").getAll();
				} else {
					return (Country[]) cacheManager.getDataGroupCache(Country.class, "all").getAll();
				}
			} else {
				return referenceDataFacade.getCountries(includeForeign);
			}
		} catch (Throwable throwable) {
			throw translateException("getCountries", throwable);
		}
	}

	@Override
	public CommitmentReason[] getCommitmentReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getCommitmentReasons();
		} catch (Throwable throwable) {
			throw translateException("getCommitmentReasons", throwable);
		}
	}

	@Override
	public PhoneType[] getPhoneTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getPhoneTypes();
		} catch (Throwable throwable) {
			throw translateException("getPhoneTypes", throwable);
		}
	}

	@Override
	public UnitType[] getUnitTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getUnitTypes();
		} catch (Throwable throwable) {
			throw translateException("getUnitTypes", throwable);
		}
	}

	@Override
	public MemoType[] getMemoTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getMemoTypes();
		} catch (Throwable throwable) {
			throw translateException("getMemoTypes", throwable);
		}
	}

	@Override
	public MemoType[] getAllMemoTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getAllMemoTypes();
		} catch (Throwable throwable) {
			throw translateException("getAllMemoTypes", throwable);
		}
	}
	
	@Override
	public Service[] getRegularServices(String[] codes) throws TelusAPIException {
		try {
			Service[] result = referenceDataFacade.getRegularServices(codes);
			
			return decorate(result);
		}catch (Throwable throwable) {
			throw translateException("getRegularServices(String)", throwable);
		}
	}

	@Override
	public Service[] getWPSServices() throws TelusAPIException {
		try {
			return decorateWPS(referenceDataFacade.getWPSServices());
		} catch (Throwable throwable) {
			throw translateException("getWPSServices", throwable);
		}
	}

	public Feature[] getFeatureCategories() throws TelusAPIException {
		try {
			return referenceDataFacade.getFeatureCategories();
		} catch (Throwable throwable) {
			throw translateException("getFeatureCategories", throwable);
		}
	}

	@Override
	public ServiceRelation[] getServiceRelations(String serviceCode) throws TelusAPIException {
		try {
			return decorate(referenceDataHelper.retrieveRelations(serviceCode)); 
		} catch (Throwable throwable) {
			throw translateException("getServiceRelations", throwable);
		}
	}

	@Override
	public ServiceRelation[] getServiceRelations(String serviceCode, String relationType) throws TelusAPIException {
		try {
			ServiceRelation[] info = referenceDataHelper.retrieveRelations(serviceCode);

			List list = new ArrayList(info.length);
			for (int i=0; i<info.length; i++) {
				ServiceRelation r = info[i];
				if (relationType.equals(r.getType())) {
					list.add(r);
				}
			}

			info = (ServiceRelation[])list.toArray(new ServiceRelation[list.size()]);

			return decorate(info);
		} catch (Throwable throwable) {
			throw translateException("getServiceRelations", throwable);
		}
	}

	@Override
	public PrepaidEventType[] getPrepaidEventTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getPrepaidEventTypes();
		} catch (Throwable throwable) {
			throw translateException("getPrepaidEventTypes", throwable);
		}
	}

	@Override
	public BusinessRole[] getBusinessRoles() throws TelusAPIException {
		try {
			return referenceDataFacade.getBusinessRoles(); 
		} catch (Throwable throwable) {
			throw translateException("getBusinessRoles", throwable);
		}
	}

	@Override
	public FleetClass[] getFleetClasses() throws TelusAPIException { 
		try {
			return referenceDataFacade.getFleetClasses();
		} catch (Throwable throwable) {
			throw translateException("getFleetClasses", throwable);
		}
	}

	@Override
	public UsageUnit[] getUsageUnits() throws TelusAPIException {
		try {
			return referenceDataFacade.getUsageUnits(); 
		} catch (Throwable throwable) {
			throw translateException("getUsageUnits", throwable);
		}
	}

	@Override
	public UsageRateMethod[] getUsageRateMethods() throws TelusAPIException {
		try {
			return referenceDataFacade.getUsageRateMethods(); 
		} catch (Throwable throwable) {
			throw translateException("getUsageRateMethods", throwable);
		}
	}

	@Override
	public UsageRecordType[] getUsageRecordTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getUsageRecordTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getUsageRecordTypes", throwable);
		}
	}

	@Override
	public PrepaidAdjustmentReason[] getPrepaidAdjustmentReason() throws TelusAPIException {
		try {
			return referenceDataFacade.getPrepaidAdjustmentReason(); 
		} catch (Throwable throwable) {
			throw translateException("getPrepaidAdjustmentReason", throwable);
		}
	}

	@Override
	public PrepaidAdjustmentReason[] getPrepaidFeatureAddWaiveReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getPrepaidFeatureAddWaiveReasons(); 
		} catch (Throwable throwable) {
			throw translateException("getPrepaidFeatureAddWaiveReasons", throwable);
		}
	}

	@Override
	public PrepaidAdjustmentReason[] getPrepaidManualAdjustmentReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getPrepaidManualAdjustmentReasons(); 
		} catch (Throwable throwable) {
			throw translateException("getPrepaidManualAdjustmentReasons", throwable);
		}
	}

	@Override
	public PrepaidAdjustmentReason[] getPrepaidTopUpWaiveReasons()  throws TelusAPIException {
		try {
			return referenceDataFacade.getPrepaidTopUpWaiveReasons(); 
		} catch (Throwable throwable) {
			throw translateException("getPrepaidTopUpWaiveReasons", throwable);
		}
	}

	@Override
	public PrepaidAdjustmentReason[] getPrepaidDeviceDirectFulfillmentReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getPrepaidDeviceDirectFulfillmentReasons();
		} catch (Throwable throwable) {
			throw translateException("getPrepaidDeviceDirectFulfillmentReasons", throwable);
		}
	}

	@Override
	public SubscriptionRoleType[] getSubscriptionRoleTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getSubscriptionRoleTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getSubscriptionRoleTypes", throwable);
		}
	}

	@Override
	public ClientStateReason[] getClientStateReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getClientStateReasons(); 
		} catch (Throwable throwable) {
			throw translateException("getClientStateReasons", throwable);
		}
	}

	@Override
	public AmountBarCode[] getAmountBarCodes() throws TelusAPIException{
		try {
			return referenceDataFacade.getAmountBarCodes(); 
		} catch (Throwable throwable) {
			throw translateException("getAmountBarCodes", throwable);
		}
	}

	@Override
	public BillCycle[] getBillCycles() throws TelusAPIException {
		try {
			return referenceDataFacade.getBillCycles();
		} catch (Throwable throwable) {
			throw translateException("getBillCycles", throwable);
		}
	}

	@Override
	public AdjustmentReason[] getAdjustmentReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getAdjustmentReasons(); 
		} catch (Throwable throwable) {
			throw translateException("getAdjustmentReasons", throwable);
		}
	}

	@Override
	public ChargeType[] getManualChargeTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getManualChargeTypes();
		} catch (Throwable throwable) {
			throw translateException("getManualChargeTypes", throwable);
		}
	}

	@Override
	public TalkGroupPriority[] getTalkGroupPriorities() throws TelusAPIException {
		try {
			return referenceDataFacade.getTalkGroupPriorities(); 
		} catch (Throwable throwable) {
			throw translateException("getTalkGroupPriorities", throwable);
		}
	}

	@Override
	public Department getDepartment(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getDepartment(code); 
		} catch (Throwable throwable) {
			throw translateException("getDepartment", throwable);
		}
	}

	@Override
	public InvoiceSuppressionLevel getInvoiceSuppressionLevel(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getInvoiceSuppressionLevel(code); 
		} catch (Throwable throwable) {
			throw translateException("getInvoiceSuppressionLevel", throwable);
		}
	}

	@Override
	public Title getTitle(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getTitle(code); 
		} catch (Throwable throwable) {
			throw translateException("getTitle", throwable);
		}
	}

	@Override
	public State getState(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getState(code); 
		} catch (Throwable throwable) {
			throw translateException("getState", throwable);
		}
	}

	@Override
	public FollowUpType getFollowUpType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getFollowUpType(code); 
		} catch (Throwable throwable) {
			throw translateException("getFollowUpType", throwable);
		}
	}

	@Override
	public Network getNetwork(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getNetwork(code); 
		} catch (Throwable throwable) {
			throw translateException("getNetwork", throwable);
		}
	}

	@Override
	public AccountType getAccountType(AccountSummary account) throws TelusAPIException {
		return getAccountType(String.valueOf(account.getAccountType()) + String.valueOf(account.getAccountSubType()), account.getBrandId());
	}

	@Override
	public AccountType getAccountType(String code) throws TelusAPIException {
		try {
			if (isUseLocalCache()) {
				return (AccountType) cacheManager.getDataGroupCache(AccountType.class).get(code);
			} else {
				return referenceDataFacade.getAccountType(code, Brand.BRAND_ID_ALL);
			}
		} catch (Throwable throwable) {
			throw translateException("getAccountType", throwable);
		}
	}

	@Override
	public AccountType getAccountType(String code, int brandId) throws TelusAPIException {
		if (!ReferenceDataManager.Helper.validateBrandId(brandId, getBrands())) {
			throw new BrandNotSupportedException(brandId);
		}
		
		try {
			if (isUseLocalCache()) {
				AccountTypeInfo accountType = (AccountTypeInfo) cacheManager.getDataGroupCache(AccountType.class).get(code);
				if (accountType != null && brandId == Brand.BRAND_ID_KOODO) {
					accountType = (AccountTypeInfo) accountType.clone();
					accountType.setBrandId(brandId);
					accountType.setDefaultDealer(AppConfiguration.getDefaultKoodoDealerCode());
					accountType.setDefaultSalesCode(AppConfiguration.getDefaultKoodoSalesRepCode());
				} 
				
					if (brandId != Brand.BRAND_ID_ALL && brandId != Brand.BRAND_ID_AMPD && brandId != Brand.BRAND_ID_TELUS && brandId != Brand.BRAND_ID_KOODO) {
						if (accountType != null) {
							accountType = (AccountTypeInfo) accountType.clone();
							accountType.setBrandId(brandId);

							Map defaultDealerCodeMap = AppConfiguration.getDefaultDealerCodeMap();
							Map defaultSalesRepCodeMap = AppConfiguration.getDefaultSalesRepCodeMap();
							
							if (defaultDealerCodeMap.containsKey(String.valueOf(brandId))) {
								accountType.setDefaultDealer((String) defaultDealerCodeMap.get(String.valueOf(brandId)));
							}
							if (defaultSalesRepCodeMap.containsKey(String.valueOf(brandId)))
								accountType.setDefaultSalesCode((String) defaultSalesRepCodeMap.get(String.valueOf(brandId)));
						}
					}
				
				return accountType;
				
			} else {
				return referenceDataFacade.getAccountType(code, brandId);
			}
		} catch (Throwable throwable) {
			throw translateException("getAccountType", throwable);
		}
	}

	@Override
	public CollectionActivity getCollectionActivity(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getCollectionActivity(code); 
		} catch (Throwable throwable) {
			throw translateException("getCollectionActivity", throwable);
		}
	}

	@Override
	public CollectionAgency getCollectionAgency(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getCollectionAgency(code); 
		} catch (Throwable throwable) {
			throw translateException("getCollectionAgency", throwable);
		}
	}

	public CollectionStateInfo getCollectionState(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getCollectionState(code); 
		} catch (Throwable throwable) {
			throw translateException("getCollectionState", throwable);
		}
	}

	@Override
	public CoverageRegion getCoverageRegion(String code) throws TelusAPIException {
		try {
			return decorate(referenceDataFacade.getCoverageRegion(code)); 
		} catch (Throwable throwable) {
			throw translateException("getCoverageRegion", throwable);
		}
	}

	@Override
	public EncodingFormat getEncodingFormat(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getEncodingFormat(code); 
		} catch (Throwable throwable) {
			throw translateException("getEncodingFormat", throwable);
		}
	}

	@Override
	public ServiceExclusionGroups getServiceExclusionGroups(String code) throws TelusAPIException {
		
		code = Info.padTo(code, ' ', 9);
		
		try {
			if (isUseLocalCache()) {
				return (ServiceExclusionGroups) cacheManager.getDataGroupCache(ServiceExclusionGroups.class).get(code);
			} else {
				return referenceDataFacade.getServiceExclusionGroups(code); 
			}
		} catch (Throwable throwable) {
			throw translateException("getServiceExclusionGroups", throwable);
		}
	}

	@Override
	public Province getProvince(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getProvince(code); 
		} catch (Throwable throwable) {
			throw translateException("getProvince", throwable);
		}
	}

	@Override
	public Province getProvince(String countryCode, String provinceCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getProvince(countryCode, provinceCode); 
		} catch (Throwable throwable) {
			throw translateException("getProvince", throwable);
		}
	}

	@Override
	public CreditMessage getCreditMessage(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getCreditMessage(code); 
		} catch (Throwable throwable) {
			throw translateException("getCreditMessage", throwable);
		}
	}

	@Override
	public CreditCardType getCreditCardType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getCreditCardType(code); 
		} catch (Throwable throwable) {
			throw translateException("getCreditCardType", throwable);
		}
	}

	@Override
	public CreditCardPaymentType getCreditCardPaymentType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getCreditCardPaymentType(code); 
		} catch (Throwable throwable) {
			throw translateException("getCreditCardPaymentType", throwable);
		}
	}

	@Override
	public Language getLanguage(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getLanguage(code); 
		} catch (Throwable throwable) {
			throw translateException("getLanguage", throwable);
		}
	}

	@Override
	public ServicePeriodType getServicePeriodType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getServicePeriodType(code); 
		} catch (Throwable throwable) {
			throw translateException("getServicePeriodType", throwable);
		}
	}

	@Override
	public PaymentMethod getPaymentMethod(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getPaymentMethod(code); 
		} catch (Throwable throwable) {
			throw translateException("getPaymentMethod", throwable);
		}
	}

	@Override
	public PaymentMethodType getPaymentMethodType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getPaymentMethodType(code); 
		} catch (Throwable throwable) {
			throw translateException("getPaymentMethodType", throwable);
		}
	}

	@Override
	public Country getCountry(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getCountry(code); 
		} catch (Throwable throwable) {
			throw translateException("getCountry", throwable);
		}
	}

	@Override
	public CommitmentReason getCommitmentReason(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getCommitmentReason(code); 
		} catch (Throwable throwable) {
			throw translateException("getCommitmentReason", throwable);
		}
	}
	
	@Override
	public PhoneType getPhoneType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getPhoneType(code); 
		} catch (Throwable throwable) {
			throw translateException("getPhoneType", throwable);
		}
	}

	@Override
	public UnitType getUnitType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getUnitType(code); 
		} catch (Throwable throwable) {
			throw translateException("getUnitType", throwable);
		}
	}

	@Override
	public MemoType getMemoType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getMemoType(code); 
		} catch (Throwable throwable) {
			throw translateException("getMemoType", throwable);
		}
	}

	@Override
	public Service getRegularService(String code) throws TelusAPIException {
//		try {
//			
//			ServiceInfo result = null;
//			
//			if (isUseLocalCache()) {
//				result = (ServiceInfo) cacheManager.getDataGroupCache(Service.class, "regular").get(Info.padTo(code, ' ', 9));
//			} else {
//				result = referenceDataFacade.getRegularService(code);
//			}
//			
//			return decorate(result); 
//		} catch (Throwable throwable) {
//			throw translateException("getRegularService", throwable);
//		}
		
		try {
			Service result = null;
			if (isUseLocalCache()) {
				result = (Service) cacheManager.getDataEntryCache(Service.class).get(code, new CacheLoader() {
					
					/*
					 * (non-Javadoc)
					 * @see com.telus.provider.cache.CacheLoader#load(java.lang.String)
					 */
					@Override
					public Object load(String key) throws Exception {
						return referenceDataFacade.getRegularService(key);
					}
				});
				
			} else {
				result =  referenceDataFacade.getRegularService(code); 
			}
			
			return decorate(result); 
		} catch (Throwable throwable) {
			throw translateException("getRegularService", throwable);
		}
	}

	@Override
	public Service getWPSService(String code) throws TelusAPIException {
		try {
			//PT168 Defect:PROD00172991 fix (change method call from decorate to decorateWPS)
			return decorateWPS(referenceDataFacade.getWPSService(code)); 
		} catch (Throwable throwable) {
			throw translateException("getWPSService", throwable);
		}
	}

	@Override
	public PromoTerm getPromoTerm(String promoCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getPromoTerm(promoCode); 
		} catch (Throwable throwable) {
			throw translateException("getPromoTerm", throwable);
		}
	}

	public  PricePlanTermInfo getPricePlanTermInfo(String pricePlanCode) throws TelusAPIException {
		try {
			if (isUseLocalCache()) {
				return (PricePlanTermInfo) cacheManager.getDataGroupCache(PricePlanTermInfo.class).get(pricePlanCode);
			} else {
				return referenceDataFacade.getPricePlanTerm(pricePlanCode);
			}
		} catch (Throwable throwable) {
			throw translateException("getPricePlanTermInfo", throwable);
		}
	}

	@Override
	public Dealer getDealer(String dealerCode) throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveDealerbyDealerCode(dealerCode);
		} catch (Throwable throwable) {
			throw translateException("getDealer", throwable);
		}
	}

	@Override
	public Dealer  getDealer(String dealerCode, boolean expired) throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveDealerbyDealerCode(dealerCode, expired);
		} catch (Throwable throwable) {
			throw translateException("getDealer", throwable);
		}
	}
	
	@Override
	public int getUrbanId(NumberGroup numberGroup)  throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveUrbanIdByNumberGroup((NumberGroupInfo)numberGroup);
		} catch (Throwable throwable) {
			throw translateException("getUrbanId", throwable);
		}
	}

	@Override
	public SalesRep[] getSalesReps(String dealerCode) throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveSalesRepListByDealer(dealerCode); 
		} catch (Throwable throwable) {
			throw translateException("getSalesReps", throwable);
		}
	}

	@Override
	public SalesRep getDealerSalesRep(String dealerCode, String salesRepCode) throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveDealerSalesRepByCode(dealerCode,salesRepCode);
		} catch (Throwable throwable) {
			throw translateException("getDealerSalesRep", throwable);
		}
	}

	@Override
	public SalesRep getDealerSalesRep(String dealerCode, String salesRepCode, boolean expired) throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveDealerSalesRepByCode(dealerCode,salesRepCode,expired);
		} catch (Throwable throwable) {
			throw translateException("getDealerSalesRep", throwable);
		}
	}

	@Override
	public String[]  findPromotionalDiscounts(String pricePlanCode, long[] productPromoTypeList, boolean initialActivation, int term) throws TelusAPIException {
		try {
			return referenceDataHelper.retrievePromotionalDiscounts(pricePlanCode, productPromoTypeList, initialActivation); 
		} catch (Throwable throwable) {
			throw translateException("findPromotionalDiscounts", throwable);
		}
	}

	@Override
	public NumberGroup[] getNumberGroups(char accountType, char accountSubTypeType, String productType,  String equipmentType) throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveNumberGroupList(accountType, accountSubTypeType, productType, equipmentType);
		} catch (Throwable throwable) {
			throw translateException("getNumberGroups", throwable);
		}
	}

	@Override
	public NumberGroup[] getNumberGroups(char accountType, char accountSubTypeType,String productType,  String equipmentType, String marketAreaCode) throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveNumberGroupList(accountType, accountSubTypeType, productType, equipmentType, marketAreaCode);
		} catch (Throwable throwable) {
			throw translateException("getNumberGroups", throwable);
		}
	}

	@Override
	public NumberGroup[] getNumberGroupsByProvince(char accountType, char accountSubTypeType,String productType, String equipmentType, String provinceCode) throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveNumberGroupListByProvince(accountType, accountSubTypeType, productType, equipmentType, provinceCode);
		} catch (Throwable throwable) {
			throw translateException("getNumberGroupsByProvince", throwable);
		}
	}

	@Override
	public SubscriptionRoleType getSubscriptionRoleType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getSubscriptionRoleType(code); 
		} catch (Throwable throwable) {
			throw translateException("getSubscriptionRoleType", throwable);
		}
	}

	@Override
	public ClientStateReason getClientStateReason(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getClientStateReason(code); 
		} catch (Throwable throwable) {
			throw translateException("getClientStateReason", throwable);
		}
	}

	@Override
	public BillCycle getBillCycle(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getBillCycle(code); 
		} catch (Throwable throwable) {
			throw translateException("getBillCycle", throwable);
		}
	}

	@Override
	public AdjustmentReason getAdjustmentReason(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getAdjustmentReason(code); 
		} catch (Throwable throwable) {
			throw translateException("getAdjustmentReason", throwable);
		}
	}

	@Override
	public ChargeType getManualChargeType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getManualChargeType(code); 
		} catch (Throwable throwable) {
			throw translateException("getManualChargeType", throwable);
		}
	}

	@Override
	public KnowbilityOperator getKnowbilityOperator(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getKnowbilityOperator(code); 
		} catch (Throwable throwable) {
			throw translateException("getKnowbilityOperator", throwable);
		}
	}

	/**
	 * Queries the AmountBarCode array  that was previously retrieved by the ReferenceDataManager for a bar code that matches the amount and reason provided. In the event that multiple bar codes match the criteria, the first bar code found by the search will be returned. In the event that no match is found, an exception is thrown.
	 * @exception TelusAPIException
	 * @return AmountBarCode
	 * @param double amount
	 * @param String barCodeReason
	 */
	@Override
	public AmountBarCode getAmountBarCode(double amount, String barCodeReason) throws TelusAPIException	{
		
		AmountBarCode[] info = getAmountBarCodes();
		int i = 0;
		while (i < info.length) {
			if ((info[i].getAmount() == amount) &&
					(info[i].getBarCodeReason().equalsIgnoreCase(barCodeReason))) {
				return info[i];
			}
			i++;
		}
		throw new TelusAPIException("Could not find AmountBarCode for amount: "
				+ amount
				+ " reason:"
				+ barCodeReason);
	}

	/**
	 * @deprecated As of February release, 2011, 
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	@Override
	public PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, String equipmentType) throws TelusAPIException {
		return findPricePlans (productType, provinceCode, accountType, accountSubType, Brand.BRAND_ID_TELUS, equipmentType);
	}

	/**
	 * @deprecated As of February release, 2011, 
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	public PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, int brandId, String equipmentType) 
		throws TelusAPIException {
		try {
			PricePlanInfo[] pricePlans = referenceDataHelper.retrievePricePlanList(
					productType, 
					equipmentType, 
					provinceCode,
					accountType, 
					accountSubType, 
					brandId, 
					NetworkType.NETWORK_TYPE_ALL);
			
			return  decorate(pricePlans);
		} catch (Throwable throwable) {
			throw translateException("findPricePlans", throwable);
		}
	}

	
	/**
	 * @deprecated As of February release, 2011, 
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	@Override
	public PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, String equipmentType, boolean currentPlansOnly, boolean availableForActivationOnly) throws TelusAPIException {
		return findPricePlans (productType, provinceCode, accountType, accountSubType, equipmentType, Brand.BRAND_ID_TELUS, currentPlansOnly, availableForActivationOnly);
	}

	/**
	 * @deprecated As of February release, 2011, 
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	@Override
	public PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, String equipmentType, boolean currentPlansOnly, boolean availableForActivationOnly, int term) throws TelusAPIException {
		return findPricePlans(productType, provinceCode, accountType, accountSubType, equipmentType, currentPlansOnly, availableForActivationOnly, Brand.BRAND_ID_TELUS, term);
	}	

	
	/**
	 * @deprecated As of February release, 2011, USED
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	@Override
	public PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, String equipmentType, int brandId, boolean currentPlansOnly, boolean availableForActivationOnly) throws TelusAPIException {
		try {
			PricePlanInfo[] pricePlans = referenceDataHelper.retrievePricePlanList(
					productType, 
					equipmentType, 
					provinceCode, 
					accountType, 
					accountSubType, 
					brandId, 
					currentPlansOnly, 
					availableForActivationOnly, 
					NetworkType.NETWORK_TYPE_ALL);
			
			return  decorate(pricePlans);
		} catch (Throwable throwable) {
			throw translateException("findPricePlans", throwable);
		}
	}

	/**
	 * @deprecated As of February release, 2011, USED
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	@Override
	public PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, String equipmentType, int brandId, boolean currentPlansOnly, boolean availableForActivationOnly, String networkType) throws TelusAPIException {
		try {
			PricePlanInfo[] pricePlans = referenceDataHelper.retrievePricePlanList(
					productType,
					equipmentType,
					provinceCode,
					accountType,
					accountSubType,
					brandId,
					currentPlansOnly,
					availableForActivationOnly,
					networkType);
			
			return  decorate(pricePlans);
		} catch (Throwable throwable) {
			throw translateException("findPricePlans", throwable);
		}
	}
	
	/**
	 * @deprecated As of February release, 2011, USED
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	@Override
	public PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType,
			String equipmentType, long[] productPromoTypeList, boolean initialActivation, int brandId, String networkType) throws TelusAPIException {
		try {
			PricePlanInfo[] pricePlans = referenceDataHelper.retrievePricePlanList(
					productType,
					equipmentType,
					provinceCode,
					accountType,
					accountSubType,
					brandId,
					productPromoTypeList,
					initialActivation,
					networkType);
			
			return  decorate(pricePlans);
		} catch (Throwable throwable) {
			throw translateException("findPricePlans", throwable);
		}
	}
	
	/**
	 * @deprecated As of February release, 2011, USED
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */	
	@Deprecated
	@Override
	public PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, String equipmentType, boolean currentPlansOnly, boolean availableForActivationOnly, int brandId, int term) throws TelusAPIException {
		try {
			PricePlanInfo[] pricePlans = referenceDataHelper.retrievePricePlanList(
					productType,
					equipmentType,
					provinceCode,
					accountType,
					accountSubType,
					brandId,
					currentPlansOnly,
					availableForActivationOnly,
					term,
					NetworkType.NETWORK_TYPE_ALL,
					null);
			
			return  decorate(pricePlans);
		} catch (Throwable throwable) {
			throw translateException("findPricePlans", throwable);
		}
	}
	
	/**
	 * @deprecated As of February release, 2011, USED
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	@Override
	public PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, String equipmentType, boolean currentPlansOnly, boolean availableForActivationOnly, int brandId, int term, String networkType) throws TelusAPIException {
		try {
			PricePlanInfo[] pricePlans = referenceDataHelper.retrievePricePlanList(productType, equipmentType,
							provinceCode, accountType, accountSubType, brandId,
							currentPlansOnly, availableForActivationOnly, term,
							networkType, null);
			
			return  decorate(pricePlans);
		} catch (Throwable throwable) {
			throw translateException("findPricePlans", throwable);
		}
	}	


	@Override
	public PricePlanSummary[] findPricePlans(PricePlanSelectionCriteria criteria) throws TelusAPIException {
		try {
			
			// Map criteria to criteriaInfo, setting seatTypeCode to empty string
			PricePlanSelectionCriteriaInfo criteriaInfo = new PricePlanSelectionCriteriaInfo();		
			criteriaInfo.setAccountSubType(criteria.getAccountSubType());
			criteriaInfo.setAccountType(criteria.getAccountType());
			criteriaInfo.setActivityCode(criteria.getActivityCode());
			criteriaInfo.setActivityReasonCode(criteria.getActivityReasonCode());
			criteriaInfo.setAvailableForActivationOnly(criteria.getAvailableForActivationOnly());
			criteriaInfo.setBrandId(criteria.getBrandId());
			criteriaInfo.setCurrentPlansOnly(criteria.getCurrentPlansOnly());
			criteriaInfo.setEquipmentType(criteria.getEquipmentType());
			criteriaInfo.setIncludeFeaturesAndServices(criteria.getIncludeFeaturesAndServices());
			criteriaInfo.setInitialActivation(criteria.getInitialActivation());
			criteriaInfo.setNetworkType(criteria.getNetworkType());
			criteriaInfo.setProductPromoTypes(criteria.getProductPromoTypes());
			criteriaInfo.setProductType(criteria.getProductType());
			criteriaInfo.setProvinceCode(criteria.getProvinceCode());
			criteriaInfo.setSeatTypeCode("");
			criteriaInfo.setTerm(criteria.getTerm());
			
			PricePlanInfo[] pricePlans = referenceDataFacade.getPricePlans(criteriaInfo);
			return decorate(pricePlans);
		} catch (Throwable throwable) {
			throw translateException("findPricePlans", throwable);
		}
	}
	
	/**
	 * @deprecated As of February release, 2011, USED
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	@Override
	public PricePlanSummary[] findPricePlans(String productType, String equipmentType, String provinceCode, char accountType, char accountSubType, boolean currentPlansOnly,
			boolean availableForActivationOnly, String activityCode, String activityReasonCode, int brandId, String networkType) throws TelusAPIException {

		try {
			PricePlanInfo[] pricePlans = referenceDataHelper.retrievePricePlanList(
					productType,
					equipmentType,
					provinceCode,
					accountType,
					accountSubType,
					brandId, 
					currentPlansOnly,
					availableForActivationOnly,
					activityCode,
					activityReasonCode,
					networkType);

			return decorate(pricePlans);
		} catch (Throwable throwable) {
			throw translateException("findPricePlans", throwable);
		}
	}	
	
	 /**
	  * @deprecated replaced by {@link #getPricePlan(String,  String, String, String, char, char, int)}
	  */
	@Deprecated
	@Override
	public PricePlan getPricePlan(String productType, String pricePlanCode, String equipmentType, String provinceCode, char accountType, char accountSubType) throws TelusAPIException {
		 return getPricePlan(pricePlanCode, equipmentType, provinceCode, accountType, accountSubType, Brand.BRAND_ID_TELUS);
	}

	/**
	 * @deprecated replaced by {@link #getPricePlan(String, String, String, char, char, int)
	 */
	@Deprecated
	@Override
	public PricePlan getPricePlan(String productType, String pricePlanCode,
			String equipmentType, String provinceCode, char accountType,
			char accountSubType, int brandId) throws TelusAPIException {
		// Ignore productType, it's implied in accountType-accountSubType.
		return getPricePlan(pricePlanCode, equipmentType, provinceCode, accountType, accountSubType, brandId);
	}

	/**
	 * @deprecated replaced by {@link #getPricePlan(String,  String, String, char, char, int)
	 */
	@Deprecated
	@Override
	public PricePlan getPricePlan(String pricePlanCode, String equipmentType, String provinceCode, char accountType, char accountSubType) throws TelusAPIException {
		 return getPricePlan(pricePlanCode, equipmentType, provinceCode, accountType, accountSubType, Brand.BRAND_ID_TELUS);
	}

	@Override
	public PricePlan getPricePlan(final String pricePlanCode, final String equipmentType, final String provinceCode, final char accountType, final char accountSubType, final int brandId) throws TelusAPIException {
		
		String key = DataCache.getComplexKey( new Object[] {
			pricePlanCode, equipmentType, provinceCode, Character.toString(accountType), Character.toString(accountSubType), Integer.toString(brandId)	
		});
		
		try {

			if (isUseLocalPPCache()) {
				return (PricePlan) cacheManager.getDataEntryCache(PricePlanInfo.class).get(key, new CacheLoader() {
					
					/*
					 * (non-Javadoc)
					 * @see com.telus.provider.cache.CacheLoader#load(java.lang.String)
					 */
					@Override
					public Object load(String key) throws Exception {
						return decorate(referenceDataFacade.retrievePricePlan(Info.padTo(pricePlanCode, ' ', 9), equipmentType, provinceCode, accountType, accountSubType, brandId));
					}
				});
			} else {
				return decorate(referenceDataFacade.getPricePlan("", pricePlanCode, equipmentType, provinceCode, Character.toString(accountType), Character.toString(accountSubType), brandId)); 
			}

		} catch (Throwable throwable) {
			throw translateException("getPricePlan", throwable);
		}
	}

	@Override
	public PricePlanSummary getPricePlan(final String pricePlanCode) throws TelusAPIException {
		
		try {
			
			if (isUseLocalPPCache()) {
				return (PricePlanSummary) cacheManager.getDataEntryCache(PricePlanInfo.class).get(pricePlanCode, new CacheLoader() {
					
					/*
					 * (non-Javadoc)
					 * @see com.telus.provider.cache.CacheLoader#load(java.lang.String)
					 */
					@Override
					public Object load(String key) throws Exception {
						return decorate(referenceDataHelper.retrievePricePlan(Info.padTo(pricePlanCode, ' ', 9)));
					}
				});
			} else { 
				return decorate(referenceDataFacade.getPricePlan(pricePlanCode));
			}
			
		} catch (Throwable throwable) {
			throw translateException("getPricePlan", throwable);
		}
	}

	@Override
	public DiscountPlan getDiscountPlan(String discountCode) throws TelusAPIException {
		try {
			return decorate(referenceDataFacade.getDiscountPlan(discountCode)); 
		} catch (Throwable throwable) {
			throw translateException("getDiscountPlan", throwable);
		}
	}

	@Override
	public DiscountPlan[] getDiscountPlans(boolean current) throws TelusAPIException {
		try {
			return  referenceDataFacade.getDiscountPlans(current);
		} catch (Throwable throwable) {
			throw translateException("getDiscountPlans", throwable);
		}
	}

	@Override
	public DiscountPlan[] getDiscountPlans(boolean current, String priceplanCode, String provinceCode, int term) throws TelusAPIException {
		try {
			return decorate(referenceDataHelper.retrieveDiscountPlans(current, priceplanCode, provinceCode,term));
		} catch (Throwable throwable) {
			throw translateException("getDiscountPlans", throwable);
		}
	}

	@Override
	public DiscountPlan[] getDiscountPlans(boolean current, String priceplanCode, String provinceCode, Equipment equipment, int term) throws TelusAPIException {
		try  {
			return decorate(referenceDataHelper.retrieveDiscountPlans(current, priceplanCode, provinceCode, equipment.getProductPromoTypeList(), equipment.isInitialActivation(),term));
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	@Override
	public PrepaidEventType getPrepaidEventType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getPrepaidEventType(code); 
		} catch (Throwable throwable) {
			throw translateException("getPrepaidEventType", throwable);
		}
	}

	@Override
	public BusinessRole getBusinessRole(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getBusinessRole(code); 
		} catch (Throwable throwable) {
			throw translateException("getBusinessRole", throwable);
		}
	}

	@Override
	public UsageUnit getUsageUnit(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getUsageUnit(code); 
		} catch (Throwable throwable) {
			throw translateException("getUsageUnit", throwable);
		}
	}

	@Override
	public UsageRateMethod getUsageRateMethod(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getUsageRateMethod(code); 
		} catch (Throwable throwable) {
			throw translateException("getUsageRateMethod", throwable);
		}
	}

	@Override
	public UsageRecordType getUsageRecordType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getUsageRecordType(code); 
		} catch (Throwable throwable) {
			throw translateException("getUsageRecordType", throwable);
		}
	}

	@Override
	public PrepaidAdjustmentReason getPrepaidAdjustmentReason(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getPrepaidAdjustmentReason(code); 
		} catch (Throwable throwable) {
			throw translateException("getPrepaidAdjustmentReason", throwable);
		}
	}

	@Override
	public PrepaidAdjustmentReason getPrepaidFeatureAddWaiveReason(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getPrepaidFeatureAddWaiveReason(code); 
		} catch (Throwable throwable) {
			throw translateException("getPrepaidFeatureAddWaiveReason", throwable);
		}
	}
	
	@Override
	public PrepaidAdjustmentReason getPrepaidManualAdjustmentReason(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getPrepaidManualAdjustmentReason(code); 
		} catch (Throwable throwable) {
			throw translateException("getPrepaidManualAdjustmentReason", throwable);
		}
	}
	
	@Override
	public PrepaidAdjustmentReason getPrepaidTopUpWaiveReason(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getPrepaidTopUpWaiveReason(code); 
		} catch (Throwable throwable) {
			throw translateException("getPrepaidTopUpWaiveReason", throwable);
		}
	}

	@Override
	public PrepaidAdjustmentReason getPrepaidDeviceDirectFulfillmentReason(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getPrepaidDeviceDirectFulfillmentReason(code);
		} catch (Throwable throwable) {
			throw translateException("getPrepaidDeviceDirectFulfillmentReason", throwable);
		}
	}

	@Override
	public Date getSystemDate() throws TelusAPIException {
		return new Date();
	}

	@Override
	public Date getLogicalDate() throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveLogicalDate();
		} catch (Throwable throwable) {
			throw translateException("getLogicalDate", throwable);
		}
	}

	@Override
	public ActivityType[] getActivityTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getActivityTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getActivityTypes", throwable);
		}
	}

	@Override
	public CollectionActivity[] getCollectionActivities() throws TelusAPIException {
		try {
			return referenceDataFacade.getCollectionActivities(); 
		} catch (Throwable throwable) {
			throw translateException("getCollectionActivities", throwable);
		}
	}

	@Override
	public CollectionAgency[] getCollectionAgencies() throws TelusAPIException {
		try {
			return referenceDataFacade.getCollectionAgencies(); 
		} catch (Throwable throwable) {
			throw translateException("getCollectionAgencies", throwable);
		}
	}

	public CollectionStateInfo[] getCollectionStates() throws TelusAPIException {
		try {
			return referenceDataFacade.getCollectionStates();
		} catch (Throwable throwable) {
			throw translateException("getCollectionStates", throwable);
		}
	}

	@Override
	public Feature[] getFeatures() throws TelusAPIException {
		try {
			return referenceDataFacade.getFeatures();
		} catch (Throwable throwable) {
			throw translateException("getFeatures", throwable);
		}
	}

	@Override
	public PaymentSourceType[] getPaymentSourceTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getPaymentSourceTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getPaymentSourceTypes", throwable);
		}
	}

	@Override
	public ProductType [] getProductTypes() throws TelusAPIException{
		try {
			return referenceDataFacade.getProductTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getProductTypes", throwable);
		}
	}

	@Override
	public EquipmentProductType [] getEquipmentProductTypes() throws TelusAPIException{
		try {
			return referenceDataFacade.getEquipmentProductTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getEquipmentProductTypes", throwable);
		}
	}

	@Override
	public EquipmentType [] getPagerEquipmentTypes() throws TelusAPIException{
		try {
			return referenceDataFacade.getPagerEquipmentTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getPagerEquipmentTypes", throwable);
		}
	}

	@Override
	public PagerFrequency[] getPagerFrequencies() throws TelusAPIException {
		try {
			return referenceDataFacade.getPagerFrequencies();
		} catch (Throwable throwable) {
			throw translateException("getPagerFrequencies", throwable);
		}
	}

	@Override
	public TermUnit[] getTermUnits() throws TelusAPIException {
		try {
			return referenceDataFacade.getTermUnits();
		} catch (Throwable throwable) {
			throw translateException("getTermUnits", throwable);
		}
	}

	@Override
	public TaxationPolicy[] getTaxationPolicies() throws TelusAPIException {
		try {
			return referenceDataFacade.getTaxationPolicies(); 
		} catch (Throwable throwable) {
			throw translateException("getTaxationPolicies", throwable);
		}
	}

	public NumberRangeInfo[] getNumberRanges() throws TelusAPIException {
		try {
			NumberRangeInfo[] result = null;
			if (isUseLocalCache()) {
				result = (NumberRangeInfo[]) cacheManager.getDataGroupCache(NumberRangeInfo.class).getAll();
			} else {
				result = referenceDataFacade.getNumberRanges(); 
			}
			return result;
		} catch (Throwable throwable) {
			throw translateException("getNumberRanges", throwable);
		}
	}

	public ProvisioningPlatformTypeInfo[] getProvisioningPlatformTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getProvisioningPlatformTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getProvisioningPlatformTypes", throwable);
		}
	}

	@Override
	public ProvisioningTransactionStatus getProvisioningTransactionStatus(String code ) throws TelusAPIException {
		try {
			return referenceDataFacade.getProvisioningTransactionStatus(code); 
		} catch (Throwable throwable) {
			throw translateException("getProvisioningTransactionStatus", throwable);
		}
	}

	@Override
	public ProvisioningTransactionStatus[] getProvisioningTransactionStatuses() throws TelusAPIException {
		try {
			return referenceDataFacade.getProvisioningTransactionStatuses(); 
		} catch (Throwable throwable) {
			throw translateException("getProvisioningTransactionStatuses", throwable);
		}
	}

	@Override
	public ProvisioningTransactionType getProvisioningTransactionType(String code ) throws TelusAPIException {
		try {
			return referenceDataFacade.getProvisioningTransactionType(code); 
		} catch (Throwable throwable) {
			throw translateException("getProvisioningTransactionType", throwable);
		}
	}

	@Override
	public ProvisioningTransactionType[] getProvisioningTransactionTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getProvisioningTransactionTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getProvisioningTransactionTypes", throwable);
		}
	}

	@Override
	public SID getSID(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getSID(code); 
		} catch (Throwable throwable) {
			throw translateException("getSID", throwable);
		}
	}

	@Override
	public SID[] getSIDs() throws TelusAPIException {
		try {
			return referenceDataFacade.getSIDs(); 
		} catch (Throwable throwable) {
			throw translateException("getSIDs", throwable);
		}
	}
	
	@Override
	public Route getRoute(String switchId, String routeId) throws TelusAPIException {
		try {
			return referenceDataFacade.getRoute(switchId, routeId); 
		} catch (Throwable throwable) {
			throw translateException("getRoute", throwable);
		}
	}

	@Override
	public Route[] getRoutes() throws TelusAPIException {
		try {
			return referenceDataFacade.getRoutes(); 
		} catch (Throwable throwable) {
			throw translateException("getRoutes", throwable);
		}
	}

	@Override
	public ActivityType getActivityType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getActivityType(code); 
		} catch (Throwable throwable) {
			throw translateException("getActivityType", throwable);
		}
	}

	@Override
	public Feature getFeature(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getFeature(code); 
		} catch (Throwable throwable) {
			throw translateException("getFeature", throwable);
		}
	}

	public Feature getFeatureCategory(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getFeatureCategory(code); 
		} catch (Throwable throwable) {
			throw translateException("getFeatureCategory", throwable);
		}
	}

	@Override
	public FleetClass getFleetClass(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getFleetClass(code); 
		} catch (Throwable throwable) {
			throw translateException("getFleetClass", throwable);
		}
	}

	@Override
	public PaymentSourceType getPaymentSourceType(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getPaymentSourceType(code); 
		} catch (Throwable throwable) {
			throw translateException("getPaymentSourceType", throwable);
		}
	}

	@Override
	public PaymentSourceType getPaymentSourceType(String sourceID, String sourceType) throws TelusAPIException{
		return getPaymentSourceType(sourceType.trim() + sourceID.trim());
	}

	@Override
	public ProductType getProductType(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getProductType(code); 
		} catch (Throwable throwable) {
			throw translateException("getProductType", throwable);
		}
	}

	@Override
	public EquipmentProductType getEquipmentProductType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getEquipmentProductType(code); 
		} catch (Throwable throwable) {
			throw translateException("getEquipmentProductType", throwable);
		}
	}

	@Override
	public EquipmentType getPagerEquipmentType(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getPagerEquipmentType(code); 
		} catch (Throwable throwable) {
			throw translateException("getPagerEquipmentType", throwable);
		}
	}

	@Override
	public PagerFrequency getPagerFrequency(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getPagerFrequency(code); 
		} catch (Throwable throwable) {
			throw translateException("getPagerFrequency", throwable);
		}
	}

	@Override
	public TermUnit getTermUnit(String code) throws TelusAPIException{
		try {
			return referenceDataFacade.getTermUnit(code); 
		} catch (Throwable throwable) {
			throw translateException("getTermUnit", throwable);
		}
	}

	@Override
	public TaxationPolicy getTaxationPolicy(String provinceCode) throws TelusAPIException{
		try {
			return referenceDataFacade.getTaxationPolicy(provinceCode); 
		} catch (Throwable throwable) {
			throw translateException("getTaxationPolicy", throwable);
		}
	}

	public NumberRangeInfo getNumberRange(String npaNxx) throws TelusAPIException{
		try {
			if (isUseLocalCache()) {
				return (NumberRangeInfo) cacheManager.getDataGroupCache(NumberRangeInfo.class).get(npaNxx);
			} else {
				return referenceDataFacade.getNumberRange(npaNxx); 
			}
		} catch (Throwable throwable) {
			throw translateException("getNumberRange", throwable);
		}
	}

	public ProvisioningPlatformTypeInfo getProvisioningPlatformType(int provisioningPlatformId) throws TelusAPIException{
		return getProvisioningPlatformType(String.valueOf(provisioningPlatformId));
	}

	public ProvisioningPlatformTypeInfo getProvisioningPlatformType(String provisioningPlatformId) throws TelusAPIException{
		try {
			return referenceDataFacade.getProvisioningPlatformType(provisioningPlatformId); 
		} catch (Throwable throwable) {
			throw translateException("getProvisioningPlatformType", throwable);
		}
	}

	@Override
	public EquipmentStatus getEquipmentStatus(long statusId, long statusTypeId) throws TelusAPIException {
		try {
			return referenceDataFacade.getEquipmentStatus(statusId, statusTypeId); 
		} catch (Throwable throwable) {
			throw translateException("getEquipmentStatus", throwable);
		}
	}
	
	@Override
	public EquipmentStatus [] getEquipmentStatuses()throws TelusAPIException {
		try {
			return referenceDataFacade.getEquipmentStatuses(); 
		} catch (Throwable throwable) {
			throw translateException("getEquipmentStatuses", throwable);
		}
	}
	@Override
	public EquipmentType [] getEquipmentTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getEquipmentTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getEquipmentTypes", throwable);
		}
	}
	
	@Override
	public EquipmentType getEquipmentType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getEquipmentType(code); 
		} catch (Throwable throwable) {
			throw translateException("getEquipmentType", throwable);
		}
	}

	@Override
	public EquipmentPossession[] getEquipmentPossessions() throws TelusAPIException {
		try {
			return referenceDataFacade.getEquipmentPossessions(); 
		} catch (Throwable throwable) {
			throw translateException("getEquipmentPossessions", throwable);
		}
	}

	@Override
	public EquipmentPossession getEquipmentPossession(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getEquipmentPossession(code);
		} catch (Throwable throwable) {
			throw translateException("getEquipmentPossession", throwable);
		}
	}

	@Override
	public ApplicationSummary getApplicationSummary(String applicationCode) throws TelusAPIException {
		try {
			if (isUseLocalCache()) {
				return (ApplicationSummary) cacheManager.getDataGroupCache(ApplicationSummary.class).get(applicationCode);
			} else {
				return referenceDataFacade.getApplicationSummary(applicationCode); 
			}
		} catch (Throwable throwable) {
			throw translateException("getApplicationSummary", throwable);
		}
	}

	@Override
	public AudienceType getAudienceType(String audienceTypeCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getAudienceType(audienceTypeCode); 
		} catch (Throwable throwable) {
			throw translateException("getAudienceType", throwable);
		}
	}

	//-----------------------------------------------------------------------------------
	// Utilities.
	//-----------------------------------------------------------------------------------
	
	public boolean isGrandFathered(ServiceSummary service) throws TelusAPIException {
		Date serviceExpiryDate = service.getExpiryDate();
		Date now               = getSystemDate();
		return serviceExpiryDate != null && serviceExpiryDate.before(now);
	}

	@Override
	public Service[] getServicesByFeatureCategory(String featureCategory, String productType, boolean current) throws TelusAPIException {
		try {
			ServiceInfo[] services = referenceDataHelper.retrieveRegularServices(featureCategory,  productType,  current);
			return  decorate(services);
		} catch (Throwable throwable) {
			throw translateException("getServicesByFeatureCategory", throwable);
		}
	}

	public Service[] getFullServices(ServiceInfo[] partialService) throws InvalidServiceException, TelusAPIException {
		try {
			TMService[] services = new TMService[partialService.length];
			for (int i=0; i<partialService.length; i++) {
				ServiceInfo info = partialService[i];
				if (info.isWPS()) {
					services[i] = (TMService)getWPSService(info.getCode());
				} else {
					services[i] = (TMService)getRegularService(info.getCode());
				}

				TMService s = services[i];

				if(s == null) {
					Logger.debug0(info);
					throw new UnknownObjectException("no service found", partialService[i].getCode());
				}

				s.getDelegate().setWPS(info.isWPS());
				s.getDelegate().setKnowbility(info.isKnowbility());
				s.getDelegate().setTerm(info.getTerm());
				s.getDelegate().setTermUnits(info.getTermUnits());
				s.getDelegate().setTermMonths(info.getTermMonths());

			}

			return services;
		} catch (Throwable throwable) {
			throw translateException("getFullServices", throwable);
		}
	}

	public String[] getServiceCodes(ServiceSummary[] services) throws TelusAPIException{
		String[] codes = new String[services.length];
		for(int i=0; i<codes.length; i++) {
			codes[i] = services[i].getCode();
		}
		return codes;
	}

	public ServiceSummary[] filterServicesByCode(ServiceSummary[] services, String[] codes) {
		Logger.debug0("filterServicesByCode("+services.length+", "+codes.length+")");
		List list = new ArrayList(codes.length);
		CODE_LOOP:
			for (int i=0; i<codes.length; i++) {
				//codes[i] = codes[i].trim();
				for (int j=0; j<services.length; j++) {
					Logger.debug0("    "+services[j].getCode()+".equals("+codes[i]+")");
					if(services[j].getCode().equals(codes[i])) {
						list.add(services[j]);
						continue CODE_LOOP;  // only 1 match will be found.
					}
				}
			}

		services = (ServiceSummary[])java.lang.reflect.Array.newInstance(services.getClass().getComponentType(), list.size());
		return (ServiceSummary[])list.toArray(services);
	}

	public ServiceSummary[] filterServicesByPricePlan(ServiceSummary[] services, PricePlan pricePlan) throws TelusAPIException {
		try {
			String[] codes = referenceDataHelper.filterServiceListByPricePlan(getServiceCodes(services), pricePlan.getCode());
			return filterServicesByCode(services, codes);
		} catch (Throwable throwable) {
			throw translateException("filterServicesByPricePlan", throwable);
		}
	}

	public ServiceSummary[] filterServicesByProvince(ServiceSummary[] services, String province) throws TelusAPIException {
		try {
			String[] codes = referenceDataHelper.filterServiceListByProvince(getServiceCodes(services), province);
			return filterServicesByCode(services, codes);
		} catch (Throwable throwable) {
			throw translateException("filterServicesByProvince", throwable);
		}
	}


	//
	// Decorators
	//
	
	public Service decorate(Service service) {
		if(service == null) {
			return null;
		}

		return new TMService(this, (ServiceInfo)service);
	}

	public Service decorateWPS(Service service) throws TelusAPIException {
		if(service == null) {
			return null;
		}

		ServiceInfo info = (ServiceInfo)service;
		
		String kbSoc = info.getWPSMappedKBSocCode();
		if(kbSoc!=null){
			
			Service kbMappedPrepaidService = getRegularService(kbSoc);
		
			boolean callingCircleFeature = false;
			boolean callHomeFreeFeature = false;  
			
			if ( kbMappedPrepaidService!= null ) {
				RatedFeature[] kbFeatures = kbMappedPrepaidService.getFeatures();
				int callingCircleSize = 0;
				String switchCode = "";
				for( int i=0; i<kbFeatures.length; i++ ) {
					//determine calling circle feature
					if(Feature.CATEGORY_CODE_CALLING_CIRCLE.equals(kbFeatures[i].getCategoryCode())){
						callingCircleSize= kbFeatures[i].getCallingCircleSize();
						callingCircleFeature = true;
						switchCode =  kbFeatures[i].getSwitchCode();
						break;
					}
				}
				
				for( int i=0; i<kbFeatures.length; i++ ) {
					//determine call home free feature
					if(Feature.CATEGORY_CODE_CALL_HOME_FREE.equals(kbFeatures[i].getCategoryCode())){
						callingCircleSize=1;
						callHomeFreeFeature = true;
						switchCode =  kbFeatures[i].getSwitchCode();
						break;
					}
				}
				
				String[] categoryCodes = new String [1];
				if (callingCircleFeature){
					categoryCodes[0] = Feature.CATEGORY_CODE_CALLING_CIRCLE;
					info.setCategoryCodes(categoryCodes);
								
					RatedFeatureInfo[] prepaidFeatures = info.getFeatures0();
					for( int i=0; i<prepaidFeatures.length; i++ ) {
					
						prepaidFeatures[i].setCallingCircleSize(callingCircleSize);
						prepaidFeatures[i].setCategoryCode(Feature.CATEGORY_CODE_CALLING_CIRCLE);
						prepaidFeatures[i].setParameterRequired(true);
						prepaidFeatures[i].setPrepaidCallingCircle(true);
						prepaidFeatures[i].setSwitchCode(switchCode);						
					}
				}
				else if (callHomeFreeFeature){
					categoryCodes[0] = Feature.CATEGORY_CODE_CALL_HOME_FREE;
					info.setCategoryCodes(categoryCodes);
								
					RatedFeatureInfo[] prepaidFeatures = info.getFeatures0();
					for( int i=0; i<prepaidFeatures.length; i++ ) {
						
						prepaidFeatures[i].setCallingCircleSize(callingCircleSize);
						prepaidFeatures[i].setCategoryCode(Feature.CATEGORY_CODE_CALL_HOME_FREE);
						prepaidFeatures[i].setParameterRequired(true);
						prepaidFeatures[i].setPrepaidCallingCircle(true);
						prepaidFeatures[i].setSwitchCode(switchCode);						
					}
				}
				//set category codes from feature's category code for non calling circle feature
				else{
					Feature feature = getFeatureCategory(info.getCode().trim());
					if (feature != null ) {
						categoryCodes[0] = feature.getCategoryCode();
						if (categoryCodes[0]!= null )
							info.setCategoryCodes(categoryCodes);
					} else
						Logger.debug0("No FeatureCategory found for code:" + info.getCode()+ ".");
				}
			}
		}
		return new TMService(this, info);
	}

	public ServiceSet[] decorate(ServiceSet[] serviceSet) {
		if (serviceSet == null)
			return null;
		return serviceSet;
	}

	public CoverageRegion decorate(CoverageRegion coverageRegion) {
		if(coverageRegion == null) {
			return null;
		}
		return new TMCoverageRegion(this, (CoverageRegionInfo)coverageRegion);
	}

	public Service[] decorate(Service[] services) {
		Service[] decoratedServices = new Service[services.length];
		for(int i = 0; i < services.length; i++) {
			decoratedServices[i] = new TMService(this, (ServiceInfo)services[i]);
		}
		return decoratedServices;
	}

	public Service[] decorateWPS(Service[] services)throws TelusAPIException {
		Service[] decoratedServices = new Service[services.length];
		for(int i=0; i<services.length; i++) {
			decoratedServices[i] = decorateWPS(services[i] );
		}
		return decoratedServices;
	}

	public CoverageRegion[] decorate(CoverageRegion[] coverageRegions) {
		CoverageRegion[] decoratedCoverageRegions = new CoverageRegion[coverageRegions.length];
		for(int i=0; i<coverageRegions.length; i++) {
			decoratedCoverageRegions[i] = new TMCoverageRegion(this, (CoverageRegionInfo)coverageRegions[i]);
		}
		return decoratedCoverageRegions;
	}

	public ServiceInfo[] undecorate(Service[] services) {
		ServiceInfo[] undecoratedServices = new ServiceInfo[services.length];
		for(int i=0; i<services.length; i++) {
			undecoratedServices[i] = ((TMService)services[i]).getDelegate();
		}
		return undecoratedServices;
	}

	public ServiceRelation[] decorate(ServiceRelation[] relations) throws TelusAPIException {
		ServiceRelation[] decoratedRelations = new ServiceRelation[relations.length];
		for(int i=0; i<relations.length; i++) {
			ServiceRelationInfo r = (ServiceRelationInfo)relations[i];
			TMService tmservice = (TMService)getRegularService(r.getServiceCode());
			ServiceInfo serviceInfo = tmservice.getDelegate();
			if (serviceInfo.getServiceType().equals(ServiceInfo.SERVICE_TYPE_CODE_PROMO_SOC)
					|| serviceInfo.getServiceType().equals(ServiceInfo.SERVICE_TYPE_CODE_OPTIONAL_AUTOEXP_SOC) 
					|| serviceInfo.getServiceType().equals(ServiceInfo.SERVICE_TYPE_CODE_REG_AUTOEXP_SOC))
			{
				ServiceTermDto serviceTerm = referenceDataFacade.getServiceTerm(r.getServiceCode());
				serviceInfo.setTerm(serviceTerm.getTermDuration());
				serviceInfo.setTermUnits(serviceTerm.getTermUnit());
			}
			decoratedRelations[i] = new TMServiceRelation(r, tmservice);
		}
		return decoratedRelations;
	}

	public ServiceSummary decorate(ServiceSummary service) {
		if(service == null) {
			return null;
		}
		return new TMServiceSummary(this, (ServiceInfo)service);
	}

	public ServiceSummary[] decorate(ServiceSummary[] services) {
		ServiceSummary[] decoratedServices = new ServiceSummary[services.length];
		for(int i=0; i<services.length; i++) {
			decoratedServices[i] = new TMServiceSummary(this, (ServiceInfo)services[i]);
		}
		return decoratedServices;
	}

	public PricePlan decorate(PricePlan pricePlan) {
		if(pricePlan == null) {
			return null;
		}

		if (pricePlan.isSharable()) {
			return new TMShareablePricePlan(this, (PricePlanInfo)pricePlan);
		} else {
			return new TMPricePlan(this, (PricePlanInfo)pricePlan);
		}
	}

	public PricePlan[] decorate(PricePlan[] pricePlans) {    
		PricePlan[] decoratedPricePlans = new PricePlan[pricePlans.length];
		for(int i=0; i<pricePlans.length; i++) {
			decoratedPricePlans[i] = decorate(pricePlans[i]);
		}
		return decoratedPricePlans;
	}

	public PricePlanSummary decorate(PricePlanSummary pricePlan) {
		if(pricePlan == null) {
			return null;
		}      
		return new TMPricePlanSummary(this, (PricePlanInfo)pricePlan);
	}

	public PricePlanSummary[] decorate(PricePlanSummary[] pricePlans) {

		PricePlanSummary[] decoratedPricePlans = new PricePlanSummary[pricePlans.length];
		for(int i=0; i<pricePlans.length; i++) {
			decoratedPricePlans[i] = new TMPricePlanSummary(this, (PricePlanInfo)pricePlans[i]);
		}
		return decoratedPricePlans;
	}

	public DiscountPlan decorate(DiscountPlan discountPlan) {
		if(discountPlan == null) {
			return null;
		}
		return new TMDiscountPlan(this, (DiscountPlanInfo)discountPlan);
	}

	public DiscountPlan[] decorate(DiscountPlan[] discountPlans) {
		DiscountPlan[] decoratedDiscountPlans = new DiscountPlan[discountPlans.length];
		for(int i=0; i<discountPlans.length; i++) {
			decoratedDiscountPlans[i] = decorate(discountPlans[i]);
		}
		return decoratedDiscountPlans;
	}

	public ServiceUsageInfo getServiceUsageInfo(String serviceCode) throws TelusAPIException {
		try {
			if (isUseLocalCache()) {
				return (ServiceUsageInfo) cacheManager.getDataEntryCache(ServiceUsageInfo.class).get(serviceCode, new CacheLoader() {
					
					/*
					 * (non-Javadoc)
					 * @see com.telus.provider.cache.CacheLoader#load(java.lang.String)
					 */
					@Override
					public Object load(String key) throws Exception {
						return referenceDataHelper.retrieveServiceUsageInfo(key);
					}
				});
				
			} else {
				return referenceDataFacade.getServiceUsage(serviceCode); 
			}
		} catch (Throwable throwable) {
			throw translateException("getServiceUsageInfo", throwable);
		}
	}

	/**
	 * Removes number groups according to the specified restriction pattern:
	 * array of ("PROVINCE_CODE:GROUP_CODE_A, GROUP_CODE_B, ..., GROUP_CODE_N)
	 * 
	 * @param numberGroups - the source array of number groups
	 * @param provinceCodeAndGroupRestrictions - the array of province restriction pattern ("PROVINCE_CODE:GROUP_CODE_A, GROUP_CODE_B, ..., GROUP_CODE_N);
	 * @return - the array of remaining number groups
	 */
	public NumberGroupInfo [] removeNumberGroupsByGroupAndProvinceCode(NumberGroupInfo [] numberGroups, String [] provinceCodeAndGroupRestrictions) {
		
		List result = new ArrayList();
		
		Map restrictions = new Hashtable();
		
		// parse restrictions
		
		for (int idx = 0; idx < provinceCodeAndGroupRestrictions.length; idx++) {
			StringTokenizer tokenizer = new StringTokenizer(provinceCodeAndGroupRestrictions[idx], ":,");
			if (tokenizer.hasMoreTokens()) {
				String provinceCode = tokenizer.nextToken();
				List groupCodes = new ArrayList();
				while (tokenizer.hasMoreTokens()) {
					groupCodes.add(tokenizer.nextToken());
				}
				restrictions.put(provinceCode, groupCodes);
			}
		}
		
		// perform filtering
		
		for (int idx = 0; idx < numberGroups.length; idx++) {
			NumberGroupInfo numberGroup = numberGroups[idx];
			
			List restrictedGroupCodes = (List) restrictions.get(numberGroup.getProvinceCode());
			
			if (restrictedGroupCodes == null || !(restrictedGroupCodes.contains("*") || restrictedGroupCodes.contains(numberGroup.getCode()))) {
				result.add(numberGroup);
			}
		}
		
		return (NumberGroupInfo []) result.toArray( new NumberGroupInfo[result.size()]);
	}
	
	public final NumberGroupInfo[] retainNumberGroupsByNumberLocation(NumberGroupInfo[] numberGroups, String numberLocation) {
		List list = new ArrayList(numberGroups.length);
		for (int i = 0; i < numberGroups.length; i++) {
			NumberGroupInfo n = numberGroups[i];
			if (numberLocation.equals(n.getNumberLocation())) {
				list.add(n);
			}
		}
		return (NumberGroupInfo[])list.toArray(new NumberGroupInfo[list.size()]);
	}

	public final NumberGroupInfo[] retainNumberGroupsByProvisioningPlatformGroup(NumberGroupInfo[] numberGroups, char Group) throws TelusAPIException {
		NumberGroupInfo[] result = new NumberGroupInfo[0];
		ProvisioningPlatformTypeInfo[] platformTypes = getProvisioningPlatformTypes();
		for (int i = 0; i < platformTypes.length; i++) {
			if (platformTypes[i].getProvisioningPlatformGroup() == Group) {
				result = (NumberGroupInfo[])Helper.union(result, retainNumberGroupsByProvisioningPlatform(numberGroups, platformTypes[i].getProvisioningPlatformId()));
			}
		}
		return result;
	}

	/**
	 * Returns number groups with available line/number ranges on the specfied provisioning platform.
	 * This method will also attach those line/number ranges to the returned number groups.
	 *
	 * @param numberGroups
	 * @param provisioningPlatformId
	 * @return
	 */
	public final NumberGroupInfo[] retainNumberGroupsByProvisioningPlatform(NumberGroupInfo[] numberGroups, int provisioningPlatformId) throws TelusAPIException {
		List list = new ArrayList(numberGroups.length);

		for (int i = 0; i < numberGroups.length; i++) {
			NumberGroupInfo numberGroup = numberGroups[i];
			String[] npanxx = numberGroup.getNpaNXX();
			numberGroup.setNumberRanges(null);

			boolean matchFound = false;
			for (int j = 0; j < npanxx.length; j++) {
				NumberRangeInfo numberRange = getNumberRange(npanxx[j]);

				//-------------------------------------------------------------------------
				// if the numberRange (NPANXX) has 1 or more line ranges on the specified
				// provisioning platform add its clone (with the non-matching line ranges
				// removed) to the number group.
				//-------------------------------------------------------------------------
				if (numberRange != null && numberRange.isProvisionedOn(provisioningPlatformId)) {
					numberRange = (NumberRangeInfo)numberRange.clone();
					numberRange.retainLineRangesByProvisioningPlatform(provisioningPlatformId);
					numberGroup.addNumberRange(numberRange);
					matchFound = true;
				}
			}
			if (matchFound) {
				list.add(numberGroup);
			}
		}

		return (NumberGroupInfo[])list.toArray(new NumberGroupInfo[list.size()]);
	}

	public final NumberGroupInfo[] removePostpaidPeers(NumberGroupInfo[] numberGroups) throws TelusAPIException {
		NumberGroupInfo[] postpaid = retainNumberGroupsByNumberLocation(numberGroups, NUMBER_LOCATION_POSTPAID);
		NumberGroupInfo[] prepaid  = retainNumberGroupsByNumberLocation(numberGroups, NUMBER_LOCATION_PREPAID);

		// prepaid + (postpaid - prepaid)
		NumberGroupInfo[] result = (NumberGroupInfo[])Helper.union(prepaid, Helper.difference(postpaid, prepaid));

		return result;
	}

	public int[] getProvisioningPlatformIdByGroup(int provisioningPlatformGroup) throws TelusAPIException {
		ProvisioningPlatformTypeInfo[] type = getProvisioningPlatformTypes();
		int[] matches = new int[type.length];

		int matchCount = 0;
		for (int i = 0; i < type.length; i++) {
			if (type[i].getProvisioningPlatformGroup() == provisioningPlatformGroup) {
				matches[matchCount] = type[i].getProvisioningPlatformId();
				matchCount++;
			}
		}

		int[] provisioningPlatformId = new int[matchCount];
		System.arraycopy(matches, 0, provisioningPlatformId, 0, matchCount);
		return provisioningPlatformId;
	}

	public int getProvisioningPlatformIdByCode(String platformCode) throws TelusAPIException {
		ProvisioningPlatformTypeInfo[] type = getProvisioningPlatformTypes();
		for (int i = 0; i < type.length; i++) {
			if (type[i].getProvisioningPlatformCode().equalsIgnoreCase(platformCode)) {
				return type[i].getProvisioningPlatformId();
			}
		}
		return 0;
	}

	public final NumberGroupInfo[] attachNumberRangesToWinPostpaid(NumberGroupInfo[] numberGroups) throws TelusAPIException {
		int[] winPlatformId = getProvisioningPlatformIdByGroup(Subscriber.PROVISIONING_PLATFORM_WIN);
		for (int i = 0; i < numberGroups.length; i++) {
			NumberGroupInfo numberGroup = numberGroups[i];
			if (NUMBER_LOCATION_POSTPAID.equals(numberGroup.getNumberLocation())) {

				String[] npanxx = numberGroup.getNpaNXX();
				numberGroup.setNumberRanges(null);

				for (int j = 0; j < npanxx.length; j++) {
					NumberRangeInfo numberRange = getNumberRange(npanxx[j]);

					//-------------------------------------------------------------------------
					// if the numberRange (NPANXX) has 1 or more line ranges on the specified
					// provisioning platform add its clone (with the non-matching line ranges
					// removed) to the number group.
					//-------------------------------------------------------------------------
					if (numberRange != null) {
						if (numberRange.isProvisionedOn(winPlatformId)) {
							numberRange = (NumberRangeInfo)numberRange.clone();
							numberRange.retainLineRangesByProvisioningPlatform(winPlatformId);
							numberGroup.addNumberRange(numberRange);
						} else {
//							numberGroup.addNumberRange(numberRange);  -- BUGFIX: was adding numberRanges for unsupported NPANXXs
						}
					} else {
//						numberGroup.addNumberRange(NumberRangeInfo.newInstanmce(npanxx[j]));  -- BUGFIX: was adding numberRanges for unsupported NPANXXs
					}
				}
				numberGroup.removeNPANXXsWithoutNumberRanges(); // BUGFIX: was adding numberRanges for unsupported NPANXXs
			}
		}
		return numberGroups;
	}

	public final NumberGroupInfo[] removeNonWinPostpaid(NumberGroupInfo[] numberGroups) throws TelusAPIException {
		NumberGroupInfo[] postpaid = retainNumberGroupsByNumberLocation(numberGroups, NUMBER_LOCATION_POSTPAID);

		NumberGroupInfo[] winPostpaid  = retainNumberGroupsByProvisioningPlatformGroup(postpaid, LineRangeInfo.PROVISIONING_PLATFORM_WIN);

		NumberGroupInfo[] nonWinPostpaid  = (NumberGroupInfo[])Helper.difference(postpaid, winPostpaid);

		NumberGroupInfo[] result = (NumberGroupInfo[])Helper.difference(numberGroups, nonWinPostpaid);

		return result;
	}

	@Override
	public NotificationType getNotificationType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getNotificationType(code); 
		} catch (Throwable throwable) {
			throw translateException("getNotificationType", throwable);
		}
	}

	@Override
	public NotificationType[] getNotificationTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getNotificationTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getNotificationTypes", throwable);
		}
	}

	public NotificationMessageTemplateInfo getNotificationMessageTemplateInfo(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getNotificationMessageTemplate(code); 
		} catch (Throwable throwable) {
			throw translateException("getNotificationMessageTemplateInfo", throwable);
		}
	}

	public NotificationMessageTemplateInfo[] getNotificationMessageTemplateInfos() throws TelusAPIException {
		try {
			return referenceDataFacade.getNotificationMessageTemplates(); 
		} catch (Throwable throwable) {
			throw translateException("getNotificationMessageTemplateInfos", throwable);
		}
	}

	@Override
	public AddressType[] getAddressTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getAddressTypes();
		} catch (Throwable throwable) {
			throw translateException("getAddressTypes", throwable);
		}
	}

	@Override
	public AddressType getAddressType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getAddressType(code); 
		} catch (Throwable throwable) {
			throw translateException("getAddressType", throwable);
		}
	}

	@Override
	public RuralType[] getRuralTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getRuralTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getRuralTypes", throwable);
		}
	}

	@Override
	public RuralType getRuralType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getRuralType(code); 
		} catch (Throwable throwable) {
			throw translateException("getRuralType", throwable);
		}
	}

	@Override
	public RuralDeliveryType[] getRuralDeliveryTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getRuralDeliveryTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getRuralDeliveryTypes", throwable);
		}
	}

	@Override
	public RuralDeliveryType getRuralDeliveryType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getRuralDeliveryType(code); 
		} catch (Throwable throwable) {
			throw translateException("getRuralDeliveryType", throwable);
		}
	}

	@Override
	public StreetDirection[] getStreetDirections() throws TelusAPIException {
		try {
			return referenceDataFacade.getStreetDirections(); 
		} catch (Throwable throwable) {
			throw translateException("getStreetDirections", throwable);
		}
	}

	@Override
	public StreetDirection getStreetDirection(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getStreetDirection(code); 
		} catch (Throwable throwable) {
			throw translateException("getStreetDirection", throwable);
		}
	}

	@Override
	public ClientConsentIndicator[] getClientConsentIndicators() throws TelusAPIException {
		try {
			return referenceDataFacade.getClientConsentIndicators(); 
		} catch (Throwable throwable) {
			throw translateException("getClientConsentIndicators", throwable);
		}
	}

	@Override
	public ClientConsentIndicator getClientConsentIndicator(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getClientConsentIndicator(code); 
		} catch (Throwable throwable) {
			throw translateException("getClientConsentIndicator", throwable);
		}
	}

	@Override
	public InvoiceCallSortOrderType[] getInvoiceCallSortOrderTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getInvoiceCallSortOrderTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getInvoiceCallSortOrderTypes", throwable);
		}
	}

	@Override
	public InvoiceCallSortOrderType getInvoiceCallSortOrderType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getInvoiceCallSortOrderType(code); 
		} catch (Throwable throwable) {
			throw translateException("getInvoiceCallSortOrderType", throwable);
		}
	}

	@Override
	public FeeWaiverType[] getFeeWaiverTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getFeeWaiverTypes();
		} catch (Throwable throwable) {
			throw translateException("getFeeWaiverTypes", throwable);
		}
	}

	@Override
	public FeeWaiverType getFeeWaiverType(String typeCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getFeeWaiverType(typeCode); 
		} catch (Throwable throwable) {
			throw translateException("getFeeWaiverType", throwable);
		}
	}

	@Override
	public FeeWaiverReason[] getFeeWaiverReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getFeeWaiverReasons(); 
		} catch (Throwable throwable) {
			throw translateException("getFeeWaiverReasons", throwable);
		}
	}

	@Override
	public FeeWaiverReason getFeeWaiverReason(String reasonCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getFeeWaiverReason(reasonCode); 
		} catch (Throwable throwable) {
			throw translateException("getFeeWaiverReason", throwable);
		}
	}

//	@Override
//	public LetterCategory[] getLetterCategories() throws TelusAPIException {
//		try {
//			return referenceDataFacade.getLetterCategories(); 
//		} catch (Throwable throwable) {
//			throw translateException("getLetterCategories", throwable);
//		}
//	}
//
//	@Override
//	public LetterCategory getLetterCategory (String code)  throws TelusAPIException {
//		try {
//			return referenceDataFacade.getLetterCategory(code); 
//		} catch (Throwable throwable) {
//			throw translateException("getLetterCategory", throwable);
//		}
//	}
//
//	@Override
//	public LetterSubCategory[] getLetterSubCategories(String letterCategory)  throws TelusAPIException {
//		try {
//			return referenceDataFacade.getLetterSubCategories(letterCategory); 
//		} catch (Throwable throwable) {
//			throw translateException("getLetterSubCategories", throwable);
//		}
//	}

//	@Override
//	public LetterSubCategory getLetterSubCategory(String code)  throws TelusAPIException {
//		try {
//			return referenceDataFacade.getLetterSubCategory(code); 
//		} catch (Throwable throwable) {
//			throw translateException("getLetterSubCategory", throwable);
//		}
//	}
//
//	@Override
//	public LetterSubCategory getLetterSubCategory(String letterCategory,
//			String letterSubCategory) throws TelusAPIException {
//		try {
//			return referenceDataFacade.getLetterSubCategory(letterCategory,letterSubCategory); 
//		} catch (Throwable throwable) {
//			throw translateException("getLetterSubCategory", throwable);
//		}
//	}
	
//	@Override
//	public Letter[] getLettersByTitleKeyword(String titleKeyword)  throws TelusAPIException {
//		try {
//			return referenceDataHelper.retrieveLettersByTitleKeyword(titleKeyword);
//		} catch (Throwable throwable) {
//			throw translateException("getLettersByTitleKeyword", throwable);
//		}
//	}
//
//	@Override
//	public Letter[] getLettersByCategory(String letterCategory)  throws TelusAPIException {
//		try {
//			return referenceDataHelper.retrieveLettersByCategory(letterCategory);
//		} catch (Throwable throwable) {
//			throw translateException("getLettersByCategory", throwable);
//		}
//	}

//	@Override
//	public Letter getLetter(String letterCategory, String letterCode)  throws TelusAPIException {
//		try {
//			return referenceDataHelper.retrieveLetter(letterCategory, letterCode);
//		} catch (Throwable throwable) {
//			throw translateException("getLetter", throwable);
//		}
//	}

//	@Override
//	public Letter getLetter(String letterCategory, String letterCode, int version)  throws TelusAPIException {
//		try {
//			return referenceDataHelper.retrieveLetter(letterCategory, letterCode, version);
//		} catch (Throwable throwable) {
//			throw translateException("getLetter", throwable);
//		}
//	}

//	@Override
//	public LetterVariable[] getLetterVariables(String letterCategory, String letterCode, int letterVersion) throws TelusAPIException {
//		try {
//			return referenceDataHelper.retrieveLetterVariables(letterCategory, letterCode, letterVersion);
//		} catch (Throwable throwable) {
//			throw translateException("getLetterVariables", throwable);
//		}
//	}

	@Override
	public WorkPosition getWorkPosition(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getWorkPosition(code); 
		} catch (Throwable throwable) {
			throw translateException("getWorkPosition", throwable);
		}
	}

	@Override
	public WorkPosition[] getWorkPositions(String functionCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getWorkPositions(functionCode); 
		} catch (Throwable throwable) {
			throw translateException("getWorkPositions", throwable);
		}
	}

	@Override
	public FollowUpCloseReason[] getFollowUpCloseReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getFollowUpCloseReasons(); 
		} catch (Throwable throwable) {
			throw translateException("getFollowUpCloseReasons", throwable);
		}
	}

	@Override
	public FollowUpCloseReason getFollowUpCloseReason(String reasonCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getFollowUpCloseReason(reasonCode); 
		} catch (Throwable throwable) {
			throw translateException("getFollowUpCloseReason", throwable);
		}
	}

	@Override
	public WorkFunction[] getWorkFunctions() throws TelusAPIException {
		try {
			return referenceDataFacade.getWorkFunctions(); 
		} catch (Throwable throwable) {
			throw translateException("getWorkFunctions", throwable);
		}
	}

	@Override
	public WorkFunction[] getWorkFunctions(String departmentCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getWorkFunctions(departmentCode); 
		} catch (Throwable throwable) {
			throw translateException("getWorkFunctions", throwable);
		}
	}

	@Override
	public String getResourceStatus(String productType, String resourceType, String resourceNumber) throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveResourceStatus(productType,resourceType,resourceNumber); 
		} catch (Throwable throwable) {
			throw translateException("getResourceStatus", throwable);
		}
	}

	@Override
	public BillHoldRedirectDestination[] getBillHoldRedirectDestinations() throws TelusAPIException {
		try {
			return referenceDataFacade.getBillHoldRedirectDestinations(); 
		} catch (Throwable throwable) {
			throw translateException("getBillHoldRedirectDestinations", throwable);
		}
	}

	@Override
	public CreditClass[] getCreditClasses() throws TelusAPIException {
		try {
			return referenceDataFacade.getCreditClasses();
		} catch (Throwable throwable) {
			throw translateException("getCreditClasses", throwable);
		}
	}

	@Override
	public BillHoldRedirectDestination getBillHoldRedirectDestination(String destinationCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getBillHoldRedirectDestination(destinationCode); 
		} catch (Throwable throwable) {
			throw translateException("getBillHoldRedirectDestination", throwable);
		}
	}

	@Override
	public CorporateAccountRep getCorporateAccountRep(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getCorporateAccountRep(code); 
		} catch (Throwable throwable) {
			throw translateException("getCorporateAccountRep", throwable);
		}
	}

	@Override
	public CorporateAccountRep[] getCorporateAccountReps() throws TelusAPIException {
		try {
			return referenceDataFacade.getCorporateAccountReps(); 
		} catch (Throwable throwable) {
			throw translateException("getCorporateAccountReps", throwable);
		}
	}

	@Override
	public Generation[] getGenerations() throws TelusAPIException {
		try {
			return referenceDataFacade.getGenerations(); 
		} catch (Throwable throwable) {
			throw translateException("getGenerations", throwable);
		}
	}

	@Override
	public Generation getGeneration(String generationCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getGeneration(generationCode); 
		} catch (Throwable throwable) {
			throw translateException("getGeneration", throwable);
		}
	}

	@Override
	public ExceptionReason[] getExceptionReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getExceptionReasons(); 
		} catch (Throwable throwable) {
			throw translateException("getExceptionReasons", throwable);
		}
	}

	@Override
	public ExceptionReason getExceptionReason(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getExceptionReason(code); 
		} catch (Throwable throwable) {
			throw translateException("getExceptionReason", throwable);
		}
	}

	@Override
	public MigrationType getMigrationType(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getMigrationType(code); 
		} catch (Throwable throwable) {
			throw translateException("getMigrationType", throwable);
		}
	}

	@Override
	public MigrationType[] getMigrationTypes() throws TelusAPIException {
		try {
			return referenceDataFacade.getMigrationTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getMigrationTypes", throwable);
		}
	}

	@Override
	public CreditCheckDepositChangeReason getCreditCheckDepositChangeReason(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getCreditCheckDepositChangeReason(code);
		} catch (Throwable throwable) {
			throw translateException("getCreditCheckDepositChangeReason", throwable);
		}
	}

	@Override
	public CreditCheckDepositChangeReason[] getCreditCheckDepositChangeReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getCreditCheckDepositChangeReasons(); 
		} catch (Throwable throwable) {
			throw translateException("getCreditCheckDepositChangeReasons", throwable);
		}
	}

	@Override
	public boolean isServiceAssociatedToPricePlan(String pricePlanCode, String serviceCode)throws TelusAPIException{
		try {			 
			pricePlanCode = Info.padTo(pricePlanCode, ' ', 9);
			serviceCode = Info.padTo(serviceCode, ' ', 9);			  
			return referenceDataHelper.isServiceAssociatedToPricePlan(pricePlanCode, serviceCode);
		} catch (Throwable throwable) {
			throw translateException("isServiceAssociatedToPricePlan", throwable);
		}
	}

	@Override
	public MemoTypeCategory[] getMemoTypeCategories() throws TelusAPIException {
		try {
			return referenceDataFacade.getMemoTypeCategories(); 
		} catch (Throwable throwable) {
			throw translateException("getMemoTypeCategories", throwable);
		}
	}

	@Override
	public MemoTypeCategory getMemoTypeCategory(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getMemoTypeCategory(code); 
		} catch (Throwable throwable) {
			throw translateException("getMemoTypeCategory", throwable);
		}
	}

	@Override
	public CollectionPathDetail[] getCollectionPathDetails(String pathCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getCollectionPathDetails(pathCode); 
		} catch (Throwable throwable) {
			throw translateException("getCollectionPathDetails", throwable);
		}
	}

	public CollectionActivity decorateCollectionActivity(String code) throws TelusAPIException{
		return getCollectionActivity(code);
	}

	@Override
	public CollectionActivity getCollectionActivity(String pathCode, int stepNumber) throws TelusAPIException {
		try {
			return referenceDataFacade.getCollectionActivity(pathCode, stepNumber);
		} catch (Throwable throwable) {
			throw translateException("getCollectionActivity", throwable);
		}
	}

	@Override
	public CollectionStepApproval[] getCollectionStepApprovals() throws TelusAPIException {
		try {
			return referenceDataFacade.getCollectionStepApprovals(); 
		} catch (Throwable throwable) {
			throw translateException("getCollectionStepApprovals", throwable);
		}
	}

	@Override
	public CollectionStepApproval getCollectionStepApproval(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getCollectionStepApproval(code); 
		} catch (Throwable throwable) {
			throw translateException("getCollectionStepApproval", throwable);
		}
	}

	@Override
	public String[] getCollectionPaths() throws TelusAPIException {
		try {
			return referenceDataFacade.getCollectionPaths(); 
		} catch (Throwable throwable) {
			throw translateException("getCollectionPaths", throwable);
		}
	}

	@Override
	public PaymentTransferReason[] getPaymentTransferReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getPaymentTransferReasons(); 
		} catch (Throwable throwable) {
			throw translateException("getPaymentTransferReasons", throwable);
		}
	}

	@Override
	public PaymentTransferReason getPaymentTransferReason(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getPaymentTransferReason(code); 
		} catch (Throwable throwable) {
			throw translateException("getPaymentTransferReason", throwable);
		}
	}

	@Override
	public AvailablePhoneNumber getAvailablePhoneNumber(String phoneNumber, String productType, String dealerCode) throws TelusAPIException {
		
		AvailablePhoneNumberInfo availablePhoneNumber = new AvailablePhoneNumberInfo();
		try {
			NumberGroupInfo ngp = referenceDataHelper.retrieveNumberGroupByPortedInPhoneNumberProductType(phoneNumber, productType);
			availablePhoneNumber.setPhoneNumber(phoneNumber);
			Dealer dealer = getDealer(dealerCode, true);
			ngp.setNumberLocation(dealer.getNumberLocationCD());
			ngp.setDefaultDealerCode(dealerCode);
			ngp.setDefaultSalesCode("0000");
			availablePhoneNumber.setNumberLocationCode(ngp.getNumberLocation());
			availablePhoneNumber.setNumberGroup(ngp);

		} catch(Throwable throwable) {
			throw translateException("getAvailablePhoneNumber", throwable);
		}
		
		return availablePhoneNumber;
	}


	@Override
	public Brand[] getBrands() throws TelusAPIException {
		try {
			if (isUseLocalCache()) {
				return (Brand[]) cacheManager.getDataGroupCache(Brand.class).getAll();
			} else {
				return referenceDataFacade.getBrands(); 
			}
		} catch (Throwable throwable) {
			throw translateException("getBrands", throwable);
		}
	}

	@Override
	public Brand getBrand(String code) throws TelusAPIException {
		try {
			if (isUseLocalCache()) {
				return (Brand) cacheManager.getDataGroupCache(Brand.class).get(code);
			} else {
				return referenceDataFacade.getBrand(code); 
			}
		} catch (Throwable throwable) {
			throw translateException("getBrand", throwable);
		}
	}

	@Override
	public Brand getBrand(int brandId) throws TelusAPIException {
		return getBrand(Integer.toString(brandId));
	}

	@Override
	public Segmentation[] getSegmentations() throws TelusAPIException {
		try {
			return referenceDataFacade.getSegmentations(); 
		} catch (Throwable throwable) {
			throw translateException("getSegmentations", throwable);
		}
	}

	@Override
	public Segmentation getSegmentation(int brandId, String accountTypeCode, String provinceCode) throws TelusAPIException {
		try {
			return referenceDataFacade.getSegmentation(brandId, accountTypeCode, provinceCode);
		} catch (Throwable throwable) {
			throw translateException("getSegmentation", throwable);
		}
	}

	public Segmentation getSegmentation(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getSegmentation(code); 
		} catch (Throwable throwable) {
			throw translateException("getSegmentation", throwable);
		}
	}

	@Override
	public LockReason[] getLockReasons() throws TelusAPIException {
		try {
			return referenceDataFacade.getLockReasons(); 
		} catch (Throwable throwable) {
			throw translateException("getLockReasons", throwable);
		}
	}

	@Override
	public LockReason getLockReason(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getLockReason(code); 
		} catch (Throwable throwable) {
			throw translateException("getLockReason", throwable);
		}
	}

	@Override
	public LockReason getLockReason(long lockReasonId) throws TelusAPIException {
		return getLockReason(Long.toString(lockReasonId));
	}

	@Override
	public String[] getAvailableNpaNxxForMsisdnReservation(String phoneNumber, boolean isPortedInNumber) throws TelusAPIException {
		try {
			return referenceDataHelper.retrieveNpaNxxForMsisdnReservation(phoneNumber, isPortedInNumber);
		} catch (Throwable throwable) {
			throw translateException("getAvailableNpaNxxForMsisdnReservation", throwable);
		}
	}

	@Override
	public SpecialNumber getSpecialNumber(String phoneNumber) throws TelusAPIException  {
		try {
			return referenceDataFacade.getSpecialNumber(phoneNumber); 
		} catch (Throwable throwable) {
			throw translateException("getSpecialNumber", throwable);
		}
	}

	public SpecialNumber[] getSpecialNumbers() throws TelusAPIException {
		try {
			return referenceDataFacade.getSpecialNumbers(); 
		} catch (Throwable throwable) {
			throw translateException("getSpecialNumbers", throwable);
		}
	}
	
	@Override
	public SpecialNumberRange getSpecialNumberRange(String phoneNumber) throws TelusAPIException  {
		try {
			return referenceDataFacade.getSpecialNumberRange(phoneNumber);
		} catch (Throwable throwable) {
			throw translateException("getSpecialNumberRange", throwable);
		}
	}
	
	public SpecialNumberRange[] getSpecialNumberRanges() throws TelusAPIException {
		try {
			return referenceDataFacade.getSpecialNumberRanges(); 
		} catch (Throwable throwable) {
			throw translateException("getSpecialNumberRanges", throwable);
		}
	}
	
	@Override
	public PrepaidCategory[] getWPSCategories() throws TelusAPIException {
		try {
			return referenceDataFacade.getWPSCategories(); 
		} catch (Throwable throwable) {
			throw translateException("getWPSCategories", throwable);
		}
	}
	
	@Override
	public PrepaidCategory getWPSCategory(String code) throws TelusAPIException {
		try {
			return referenceDataFacade.getWPSCategory(code); 
		} catch (Throwable throwable) {
			throw translateException("getWPSCategory", throwable);
		}
	}
	
    @Override
	public PoolingGroup getPoolingGroup(String code) throws TelusAPIException {
    	try {
    		if (isUseLocalCache()) {
    			return (PoolingGroup) cacheManager.getDataGroupCache(PoolingGroup.class).get(code);
    		} else {
    			return referenceDataFacade.getPoolingGroup(code); 
    		}
		} catch (Throwable throwable) {
			throw translateException("getPoolingGroup", throwable);
		}
    }

    @Override
	public PoolingGroup[] getPoolingGroups() throws TelusAPIException {
    	try {
    		if (isUseLocalCache()) {
    			return (PoolingGroup[]) cacheManager.getDataGroupCache(PoolingGroup.class).getAll();
    		} else {
    			return referenceDataFacade.getPoolingGroups(); 
    		}
		} catch (Throwable throwable) {
			throw translateException("getPoolingGroups", throwable);
		}
    }
    
    @Override
	public VendorService[] getVendorServices() throws TelusAPIException {
    	try {
			return referenceDataFacade.getVendorServices(); 
		} catch (Throwable throwable) {
			throw translateException("getVendorServices", throwable);
		}
    }
    
    @Override
	public VendorService getVendorService(String vendorServiceCode) throws TelusAPIException {
    	try {
			return referenceDataFacade.getVendorService(vendorServiceCode); 
		} catch (Throwable throwable) {
			throw translateException("getVendorService", throwable);
		}
    }
 	
	/*
	 * (non-Javadoc)
	 * @see com.telus.api.reference.ReferenceDataManager#getServiceRequestNoteTypes()
	 */
    @Override
	public ServiceRequestNoteType [] getServiceRequestNoteTypes() {
    	try {
    		return provider.getActivityLoggingService().getServiceRequestNoteTypes();
    		
		} catch (Throwable throwable) {
			Logger.debug(throwable);
			return new ServiceRequestNoteType[0];
		}
    }
    
    /*
     * (non-Javadoc)
     * @see com.telus.api.reference.ReferenceDataManager#getServiceRequestNoteType(long)
     */
    @Override
	public ServiceRequestNoteType getServiceRequestNoteType(long typeId) throws TelusAPIException {
    	ServiceRequestNoteType [] noteTypes = getServiceRequestNoteTypes();
    	
    	for (int idx = 0; idx < noteTypes.length; idx++) {
    		if (noteTypes[idx].getId() == typeId) {
    			return noteTypes[idx];
    		}
    	}
    	return null;
    }

    /*
     * (non-Javadoc)
     * @see com.telus.api.reference.ReferenceDataManager#getServiceRequestRelationshipTypes()
     */
    @Override
	public ServiceRequestRelationshipType [] getServiceRequestRelationshipTypes() {
    	try {
    		
    		return provider.getActivityLoggingService().getServiceRequestRelationshipTypes();
    		
    	} catch (Throwable t) {
    		return new ServiceRequestRelationshipType[0];
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see com.telus.api.reference.ReferenceDataManager#getServiceRequestRelationshipType(long)
     */
    @Override
	public ServiceRequestRelationshipType getServiceRequestRelationshipType(long typeId) {
    	ServiceRequestRelationshipType [] relationshipTypes = getServiceRequestRelationshipTypes();
    	
    	for (int idx = 0; idx < relationshipTypes.length; idx++) {
    		if (relationshipTypes[idx].getId() == typeId) {
    			return relationshipTypes[idx];
    		}
    	}
    	return null;
    }
   
	public Service[] getZeroMinutePoolingContributorServices() throws TelusAPIException {
		try {
			return referenceDataFacade.getZeroMinutePoolingContributorServices(); 
		} catch (Throwable throwable) {
			throw translateException("getZeroMinutePoolingContributorServices", throwable);
		}
	}
	
	public Service[] getMinutePoolingContributorServices() throws TelusAPIException {
		try {
			return referenceDataFacade.getMinutePoolingContributorServices(); 
		} catch (Throwable throwable) {
			throw translateException("getMinutePoolingContributorServices", throwable);
		}
	}

	@Override
	public PrepaidRechargeDenomination [] getPrepaidRechargeDenominations(String rechargeType) throws TelusAPIException {
		try {
			return referenceDataFacade.getPrepaidRechargeDenominations(rechargeType); 
		} catch (Throwable throwable) {
			throw translateException("getPrepaidRechargeDenominations", throwable);
		}
	}
	
    @Override
	public NetworkType[] getNetworkTypes() throws TelusAPIException {
    	try {
			return referenceDataFacade.getNetworkTypes(); 
		} catch (Throwable throwable) {
			throw translateException("getNetworkTypes", throwable);
		}
    }
    
    @Override
	public HandsetRoamingCapability[] getRoamingCapability() throws TelusAPIException {
    	try {
			return referenceDataFacade.getRoamingCapability(); 
		} catch (Throwable throwable) {
			throw translateException("getRoamingCapability", throwable);
		}
    }
    
    @Override
	public PrepaidRateProfile[] getAllPrepaidRates() throws TelusAPIException {
    	try {
			return referenceDataFacade.getAllPrepaidRates();
		} catch (Throwable throwable) {
			throw translateException("getAllPrepaidRates", throwable);
		}
    }
    
    @Override
	public PrepaidRateProfile[] getPrepaidRatesbyRateId(int rateId) throws TelusAPIException {
    	PrepaidRateProfile [] prepaidRateProfile  = getAllPrepaidRates();
		List list = new ArrayList();
		for (int i=0; i<prepaidRateProfile.length; i++) {
			PrepaidRateProfile rateProfile = prepaidRateProfile[i];
			if(rateId == rateProfile.getRateId()){
				list.add(rateProfile);
			}
		}
		return (PrepaidRateProfile[]) list.toArray(new PrepaidRateProfile[list.size()]);
    }
    
    @Override
	public PrepaidRateProfile[] getPrepaidRates(int rateId, String countryCode) throws TelusAPIException {
    	PrepaidRateProfile [] prepaidRateProfile  = getAllPrepaidRates();
		List list = new ArrayList();
		for (int i=0; i<prepaidRateProfile.length; i++) {
			PrepaidRateProfile rateProfile = prepaidRateProfile[i];
			if(null != countryCode) {
				if((rateId == rateProfile.getRateId()) && countryCode.trim().equalsIgnoreCase(rateProfile.getCountryCode().trim()) ){
					list.add(rateProfile);
				}
			}
		}
		return (PrepaidRateProfile[]) list.toArray(new PrepaidRateProfile[list.size()]);
    }
    
    @Override
	public PrepaidRateProfile[] getPrepaidRatesbyAppId(String appId) throws TelusAPIException {
    	PrepaidRateProfile [] prepaidRateProfile  = getAllPrepaidRates();
		List list = new ArrayList();
		for (int i=0; i<prepaidRateProfile.length; i++) {
			PrepaidRateProfile rateProfile = prepaidRateProfile[i];
			String[] appIds = rateProfile.getApplicationIds();
			for (int k=0; k<appIds.length; k++) {
				if(appId.trim().equalsIgnoreCase(appIds[k].trim())) {
					list.add(rateProfile);
					break;
				}
			}
		}
		return (PrepaidRateProfile[]) list.toArray(new PrepaidRateProfile[list.size()]);
    }
    
    @Override
	public void refresh() throws TelusAPIException {
    	cacheManager.clearAll();
    }

    @Override
	public ServiceSummary[] removeByPrivilege(ServiceSummary[] services,  String businessRoleCode, String privilegeCode) throws TelusAPIException {
    	try {
    		ServiceInfo[] filteredServices=null;
    		ServiceInfo[] serviceInfos = new ServiceInfo[services.length];
    		for (int idx = 0; idx < services.length; idx++) {
    			serviceInfos[idx] = ((TMServiceSummary) services[idx]).getDelegate();
    		}
    		filteredServices= referenceDataFacade.removeByPrivilege(serviceInfos, businessRoleCode, privilegeCode);
    		return(ServiceSummary[])ReferenceDataManager.Helper.intersection(services, filteredServices) ;
		} catch (Throwable throwable) {
			throw translateException("removeByPrivilege", throwable);
		}
    }
    
    @Override
	public ServiceSummary[] retainByPrivilege(ServiceSummary[] services,  String businessRoleCode, String privilegeCode) throws TelusAPIException {
    	try {
    		ServiceInfo[] filteredServices=null;
    		ServiceInfo[] serviceInfos = new ServiceInfo[services.length];
    		for (int idx = 0; idx < services.length; idx++) {
    			serviceInfos[idx] = ((TMServiceSummary) services[idx]).getDelegate();
    		}
    		filteredServices= referenceDataFacade.retainByPrivilege(serviceInfos, businessRoleCode, privilegeCode);
			return(ServiceSummary[])ReferenceDataManager.Helper.intersection(services, filteredServices) ;
    		
		} catch (Throwable throwable) {
			throw translateException("retainByPrivilege", throwable);
		}
    }

    public ServicePolicyInfo[] checkServicePrivilege(ServiceInfo[] services, String businessRoleCode, String privilegeCode) throws TelusAPIException {
    	try {
    		ServicePolicyInfo[] result = null;
    		
    		if (isUseLocalCache()) {
    			result = getServicePolicies(services, businessRoleCode, privilegeCode); 
    		} else {
    			result = referenceDataFacade.checkServicePrivilege(services, businessRoleCode, privilegeCode); 
    		}
    		return result;
		} catch (Throwable throwable) {
			throw translateException("checkServicePrivilege", throwable);
		}
    }
    
    public ServicePolicyInfo getServicePolicy(String servicePolicyCode) throws TelusAPIException {
    	try {
    		return (ServicePolicyInfo) cacheManager.getDataGroupCache(ServicePolicyInfo.class).get(servicePolicyCode);
		} catch (Throwable throwable) {
			throw translateException("getServicePolicy", throwable);
		}
    }

	private ServicePolicyInfo[] getServicePolicies(ServiceInfo[] services, String businessRoleCode, String privilegeCode) throws TelusAPIException  {
		List result = new ArrayList();
		for (int idx = 0; idx < services.length; idx++) {
			result.add(getServicePolicy(services[idx], businessRoleCode, privilegeCode));
		}
		return (ServicePolicyInfo[]) result.toArray( new ServicePolicyInfo[result.size()]);
	}
    
	private ServicePolicyInfo getServicePolicy(ServiceInfo service, String businessRoleCode, String privilegeCode) throws TelusAPIException  {

		ServicePolicyInfo servicePolicy;

		servicePolicy = getServicePolicy(service.getCode().trim() + businessRoleCode.trim() + privilegeCode.trim());
		if (servicePolicy == null) {
			servicePolicy = getServicePolicy(service.getCode().trim() + BusinessRole.BUSINESS_ROLE_ALL + privilegeCode.trim());
		}

		if (servicePolicy == null) {
			
			servicePolicy = new ServicePolicyInfo();
			
			servicePolicy.setServiceCode(service.getCode());
			servicePolicy.setBusinessRoleCode(businessRoleCode);
			servicePolicy.setPrivilegeCode(privilegeCode);

			if (ServiceSummary.PRIVILEGE_AUTOADD.equals(privilegeCode)) {
				servicePolicy.setAvailable(false);
			} else {
				
				if ((businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_AGENT)) || (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_AGENT_LNR))
						|| (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_AGENT_ACTIVATIONS))
						|| (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_CORPORATE_STORE))
						|| (privilegeCode.equals(ServiceSummary.PRIVILEGE_SERVICE_DISPLAY))
						|| (privilegeCode.equals(ServiceSummary.PRIVILEGE_SERVICE_REMOVE))
						|| (privilegeCode.equals(ServiceSummary.PRIVILEGE_SERVICE_RETAIN_ON_PRICE_PLAN_CHANGE))
						|| (privilegeCode.equals(ServiceSummary.PRIVILEGE_SERVICE_RETAIN_ON_RENEWAL))) {
					
					servicePolicy.setAvailable(true);
					
				} else if (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_CLIENT)
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_CLIENT_ACTIVATIONS)
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_IVR_CLIENT)
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_IVR_CLIENT)) {
					
					servicePolicy.setAvailable(service.isClientActivation());
					
				} else if (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_DEALER) 
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_DEALER_ACTIVATIONS)
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_CONTRACT_RENEWAL)
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_CONTRACT_RENEWAL_CORP)
						|| businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_RETAIL_STORE)) {
					
					servicePolicy.setAvailable(service.isDealerActivation());
					
				} else {
					servicePolicy.setAvailable(true);
				}
			}
		}
		
		return servicePolicy;
	}

  
    

    
    //Added for Charge paper bill by Anitha Duraisamy- Start 
    
    @Override
	public ChargeType getPaperBillChargeType(int brandId, String provinceCode, char accountType, 
    		char accountSubType, String segment, String invoiceSuppressionLevel) throws TelusAPIException {
        return referenceDataFacade.getPaperBillChargeType(brandId, provinceCode, accountType, accountSubType, segment, invoiceSuppressionLevel);
    	
    }
    //Added for Charge paper bill by Anitha Duraisamy- End 
   
    @Override
	public String[] getServiceCodesByGroup (String serviceGroupCode) throws TelusAPIException{
    	return referenceDataFacade.getServiceCodesByGroup(serviceGroupCode.trim());
    	
    }

	@Override
	public BillCycle[] removeBillCyclesByProvince(BillCycle[] billCycles,
			String province) throws TelusAPIException {
		return referenceDataFacade.removeBillCyclesByProvince(billCycles, province);
	}

	/**
	* Returns a list of all possible data sharing groups configured in the system.
	*/
	@Override
	public DataSharingGroup[] getDataSharingGroups() throws TelusAPIException {
    	try {
    		if (isUseLocalCache()) {
    			return (DataSharingGroup[]) cacheManager.getDataGroupCache(DataSharingGroupInfo.class).getAll();
    		} else {
    			return referenceDataFacade.getDataSharingGroups();
    		}
		} catch (Throwable throwable) {
			throw translateException("getDataSharingGroups", throwable);
		}
	}
	
	/**
	* Returns a specific data sharing group given the code.  If there is no data sharing
	* group that matches code, this method returns null.
	*/
	@Override
	public DataSharingGroup getDataSharingGroup(String code) throws TelusAPIException {
    	try {
    		if (isUseLocalCache()) {
    			return (DataSharingGroup) cacheManager.getDataGroupCache(DataSharingGroupInfo.class).get(code);
    		} else {
    			return referenceDataFacade.getDataSharingGroup(code);
    		}
		} catch (Throwable throwable) {
			throw translateException("getDataSharingGroup", throwable);
		}
	}

	//@Override
		@Override
		public boolean isPPSEligible(char accountType, char accountSubType)
				throws TelusAPIException {
			try {
				return referenceDataFacade.isPPSEligible(accountType, accountSubType);
			} catch (Throwable throwable) {
				throw translateException("isPPSEligible", throwable);
			}
		}

}
    
