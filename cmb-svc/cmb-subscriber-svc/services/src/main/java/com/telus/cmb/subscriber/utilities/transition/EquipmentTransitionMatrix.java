package com.telus.cmb.subscriber.utilities.transition;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.equipment.Equipment;
import com.telus.eas.utility.info.SwapRuleInfo;

public final class EquipmentTransitionMatrix extends TransitionMatrix {
	private static final Log logger = LogFactory.getLog(EquipmentTransitionMatrix.class);

	public EquipmentTransitionMatrix() {
		super();
	}

	@Override
	void initialize() throws Throwable {
		SwapRuleInfo[] swapRules = getReferenceDataHelper().retrieveSwapRules();
		int swapRulesSz = swapRules != null ? swapRules.length : 0;
		logger.info("Loaded " + swapRulesSz + " swap rules.");
		SwapRuleInfo oldSwapRule = null;
		Hashtable<String, String> swapTypesList = null;
		Hashtable<String, String> swapAppsList = null;

		for (int i = 0; i < swapRulesSz; i++) {
			if (oldSwapRule == null || swapRules[i].getSwapGroupId() != oldSwapRule.getSwapGroupId()) {
				if (oldSwapRule != null) {
					String[] swapTypes = (String[]) swapTypesList.values().toArray(new String[swapTypesList.size()]);
					String[] swapApps = (String[]) swapAppsList.values().toArray(new String[swapAppsList.size()]);

					EquipmentState initState = new EquipmentState(oldSwapRule.getProductType1(), oldSwapRule.getEquipmentType1(), oldSwapRule.getProductClass1(), swapTypes, swapApps,
							oldSwapRule.isInclusiveApp());
					initState.addInvalidFinalState(new EquipmentState(oldSwapRule.getProductType2(), oldSwapRule.getEquipmentType2(), oldSwapRule.getProductClass2(), new ValidationResult(oldSwapRule
							.getMessageId())));
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

			EquipmentState initState = new EquipmentState(oldSwapRule.getProductType1(), oldSwapRule.getEquipmentType1(), oldSwapRule.getProductClass1(), swapTypes, swapApps,
					oldSwapRule.isInclusiveApp());
			initState.addInvalidFinalState(new EquipmentState(oldSwapRule.getProductType2(), oldSwapRule.getEquipmentType2(), oldSwapRule.getProductClass2(), new ValidationResult(oldSwapRule
					.getMessageId())));
			addInitState(initState);
		}
		
		setInitializedSuccessfully(true);
		logger.info("EquipmentTransitionMatrix initialized successfully.");
	}

	public ValidationResult validTransition(Equipment oldEquipment, Equipment newEquipment, String swapType, String appId) {
		if (isInitializedSuccessfully() == false) {
			retryInitialization();
		}
		
		return doValidTransition(oldEquipment, newEquipment, swapType, appId);
	}
	
	private ValidationResult doValidTransition(Equipment oldEquipment, Equipment newEquipment, String swapType, String appId) {
		EquipmentState oldEquipmentState = new EquipmentState(oldEquipment, swapType, appId);
		EquipmentState newEquipmentState = new EquipmentState(newEquipment, swapType, appId);

		return validTransition(oldEquipmentState, newEquipmentState);		
	}

}
