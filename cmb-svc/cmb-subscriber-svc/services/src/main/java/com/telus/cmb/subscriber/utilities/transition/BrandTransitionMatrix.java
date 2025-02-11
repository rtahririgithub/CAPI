package com.telus.cmb.subscriber.utilities.transition;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.equipment.Equipment;
import com.telus.eas.utility.info.BrandInfo;
import com.telus.eas.utility.info.BrandSwapRuleInfo;

public final class BrandTransitionMatrix extends TransitionMatrix {
	private static final Log logger = LogFactory.getLog(BrandTransitionMatrix.class);
	// array of all available brand IDs
	private int[] allBrandIds;

	public BrandTransitionMatrix() {
		super();
	}

	@Override
	void initialize() throws Throwable {
		loadAllBrandIds();
		BrandSwapRuleInfo[] swapRules = getReferenceDataHelper().retrieveBrandSwapRules();
		int swapRulesSz = swapRules != null ? swapRules.length : 0;
		logger.info("Loaded " + swapRulesSz + " brand swap rules.");

		BrandSwapRuleInfo oldSwapRule = null;
		Hashtable<String, String> swapTypesList = null;
		Hashtable<String, String> swapAppsList = null;

		for (int i = 0; i < swapRulesSz; i++) {
			if (oldSwapRule == null || swapRules[i].getBrandSwapRuleId() != oldSwapRule.getBrandSwapRuleId()) {
				if (oldSwapRule != null) {
					String[] swapTypes = (String[]) swapTypesList.values().toArray(new String[swapTypesList.size()]);
					String[] swapApps = (String[]) swapAppsList.values().toArray(new String[swapAppsList.size()]);
					BrandState initState = new BrandState(oldSwapRule.getBrandId1(), swapTypes, swapApps, oldSwapRule.isInclusiveApp());
					initState.addInvalidFinalState(new BrandState(oldSwapRule.getBrandId2(), new ValidationResult(oldSwapRule.getMessageId())));
					addInitState(initState);
				}
				swapTypesList = new Hashtable<String, String>();
				swapAppsList = new Hashtable<String, String>();
			}

			if (swapTypesList.get(swapRules[i].getSwapType()) == null) {
				swapTypesList.put(swapRules[i].getSwapType(), swapRules[i].getSwapType());
			}

			if (swapAppsList.get(swapRules[i].getSwapApp()) == null) {
				swapAppsList.put(swapRules[i].getSwapApp(), swapRules[i].getSwapApp());
			}

			oldSwapRule = swapRules[i];

		}

		if (oldSwapRule != null) {
			String[] swapTypes = (String[]) swapTypesList.values().toArray(new String[swapTypesList.size()]);
			String[] swapApps = (String[]) swapAppsList.values().toArray(new String[swapAppsList.size()]);
			BrandState initState = new BrandState(oldSwapRule.getBrandId1(), swapTypes, swapApps, oldSwapRule.isInclusiveApp());
			initState.addInvalidFinalState(new BrandState(oldSwapRule.getBrandId2(), new ValidationResult(oldSwapRule.getMessageId())));
			addInitState(initState);
		}
		setInitializedSuccessfully(true);
		logger.info("BrandTransitionMatrix initialized successfully.");
	}

	public ValidationResult validTransition(int oldBrandId, Equipment newEquipment, String swapType, String appId, boolean matchAll) {
		if (isInitializedSuccessfully() == false) {
			retryInitialization();
		}
		
		return doValidTransition(oldBrandId, newEquipment, swapType, appId, matchAll);
	}
	
	private ValidationResult doValidTransition(int oldBrandId, Equipment newEquipment, String swapType, String appId, boolean matchAll) {
		if (newEquipment == null || swapType == null || appId == null) {
			throw new NullPointerException("BrandTransitionMatrix.doValidTransition(): Input parameters cannot be null.");
		}

		int[] newBrandIds = newEquipment.getBrandIds();

		if (newBrandIds == null) {
			throw new IllegalArgumentException("BrandTransitionMatrix.doValidTransition(): newEquipment.getBrandIds() cannot be null.");
		}

		if (newBrandIds.length == 0) {
			newBrandIds = allBrandIds;
		}

		BrandState oldBrandState = new BrandState(oldBrandId, swapType, appId);
		BrandState[] newBrandStates = new BrandState[newBrandIds.length];
		for (int i = 0; i < newBrandIds.length; i++) {
			newBrandStates[i] = new BrandState(newBrandIds[i], swapType, appId);
		}

		return validTransition(oldBrandState, newBrandStates, matchAll);

	}

	private void loadAllBrandIds() throws Throwable {
		BrandInfo[] brands = getReferenceDataHelper().retrieveBrands();
		int brandsSz = brands != null ? brands.length : 0;

		allBrandIds = new int[brandsSz];
		for (int i = 0; i < brandsSz; i++) {
			allBrandIds[i] = brands[i].getBrandId();
		}

	}
	

}
