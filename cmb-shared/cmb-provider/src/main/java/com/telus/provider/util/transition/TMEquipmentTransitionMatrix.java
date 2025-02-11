package com.telus.provider.util.transition;



import java.util.Hashtable;

import com.telus.api.equipment.Equipment;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.eas.utility.info.SwapRuleInfo;
import com.telus.provider.util.Logger;



/**

 * @author Vladimir Tsitrin

 * @version 1.0, 6-Feb-2006

 */



public final class TMEquipmentTransitionMatrix extends TransitionMatrix {

  // remote interface of the ReferenceDataHelperEJB

  private final ReferenceDataHelper referenceDataHelper;



  public TMEquipmentTransitionMatrix(ReferenceDataHelper referenceDataHelper) throws Throwable {

    this.referenceDataHelper = referenceDataHelper;

    initialize();

  }



  void initialize() throws Throwable {

    SwapRuleInfo[] swapRules = referenceDataHelper.retrieveSwapRules();



    int swapRulesSz = swapRules != null ? swapRules.length : 0;

    Logger.debug("Loaded " + swapRulesSz + " swap rules.");



    SwapRuleInfo oldSwapRule = null;

    Hashtable swapTypesList = null;

    Hashtable swapAppsList = null;



    for (int i = 0; i < swapRulesSz; i++) {

      if (oldSwapRule == null || swapRules[i].getSwapGroupId() != oldSwapRule.getSwapGroupId()) {

        if (oldSwapRule != null) {

          String[] swapTypes = (String[]) swapTypesList.values().toArray(new String[swapTypesList.size()]);

          String[] swapApps = (String[]) swapAppsList.values().toArray(new String[swapAppsList.size()]);



          EquipmentState initState = new EquipmentState(oldSwapRule.getProductType1(), oldSwapRule.getEquipmentType1(), oldSwapRule.getProductClass1(), swapTypes, swapApps, oldSwapRule.isInclusiveApp());

          initState.addInvalidFinalState(new EquipmentState(oldSwapRule.getProductType2(), oldSwapRule.getEquipmentType2(), oldSwapRule.getProductClass2(), new ValidationResult(oldSwapRule.getMessageId())));

          addInitState(initState);

        }



        swapTypesList = new Hashtable();

        swapAppsList = new Hashtable();

      }



      if (swapTypesList.get(swapRules[i].getSwapType()) == null)

        swapTypesList.put(swapRules[i].getSwapType(), swapRules[i].getSwapType());



      if (swapAppsList.get(swapRules[i].getSwapApp()) == null)

        swapAppsList.put(swapRules[i].getSwapApp(), swapRules[i].getSwapApp());



      oldSwapRule = swapRules[i];

    }



    if (oldSwapRule != null) {

      String[] swapTypes = (String[]) swapTypesList.values().toArray(new String[swapTypesList.size()]);

      String[] swapApps = (String[]) swapAppsList.values().toArray(new String[swapAppsList.size()]);



      EquipmentState initState = new EquipmentState(oldSwapRule.getProductType1(), oldSwapRule.getEquipmentType1(), oldSwapRule.getProductClass1(), swapTypes, swapApps, oldSwapRule.isInclusiveApp());

      initState.addInvalidFinalState(new EquipmentState(oldSwapRule.getProductType2(), oldSwapRule.getEquipmentType2(), oldSwapRule.getProductClass2(), new ValidationResult(oldSwapRule.getMessageId())));

      addInitState(initState);

    }

  }



  public ValidationResult validTransition(Equipment oldEquipment, Equipment newEquipment, String swapType, String appId) {

    EquipmentState oldEquipmentState = new EquipmentState(oldEquipment, swapType, appId);

    EquipmentState newEquipmentState = new EquipmentState(newEquipment, swapType, appId);



    return validTransition(oldEquipmentState, newEquipmentState);

  }

}

