package com.telus.provider.util.transition;



import java.util.Hashtable;

import com.telus.api.equipment.Equipment;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.eas.utility.info.BrandInfo;
import com.telus.eas.utility.info.BrandSwapRuleInfo;
import com.telus.provider.util.Logger;



/**

 * @author Vladimir Tsitrin

 * @version 1.0, 2-Nov-2006

 */



public final class TMBrandTransitionMatrix extends TransitionMatrix {

  // remote interface of the ReferenceDataHelperEJB

  private final ReferenceDataHelper referenceDataHelper;

  // array of all available brand IDs

  private int[] allBrandIds;



  public TMBrandTransitionMatrix(ReferenceDataHelper referenceDataHelper) throws Throwable {

    this.referenceDataHelper = referenceDataHelper;

    loadAllBrandIds();

    initialize();

  }



  void initialize() throws Throwable {

    BrandSwapRuleInfo[] swapRules = referenceDataHelper.retrieveBrandSwapRules();



    int swapRulesSz = swapRules != null ? swapRules.length : 0;

    Logger.debug("Loaded " + swapRulesSz + " brand swap rules.");



    BrandSwapRuleInfo oldSwapRule = null;

    Hashtable swapTypesList = null;

    Hashtable swapAppsList = null;



    for (int i = 0; i < swapRulesSz; i++) {

      if (oldSwapRule == null || swapRules[i].getBrandSwapRuleId() != oldSwapRule.getBrandSwapRuleId()) {

        if (oldSwapRule != null) {

          String[] swapTypes = (String[]) swapTypesList.values().toArray(new String[swapTypesList.size()]);

          String[] swapApps = (String[]) swapAppsList.values().toArray(new String[swapAppsList.size()]);



          BrandState initState = new BrandState(oldSwapRule.getBrandId1(), swapTypes, swapApps, oldSwapRule.isInclusiveApp());

          initState.addInvalidFinalState(new BrandState(oldSwapRule.getBrandId2(), new ValidationResult(oldSwapRule.getMessageId())));

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



      BrandState initState = new BrandState(oldSwapRule.getBrandId1(), swapTypes, swapApps, oldSwapRule.isInclusiveApp());

      initState.addInvalidFinalState(new BrandState(oldSwapRule.getBrandId2(), new ValidationResult(oldSwapRule.getMessageId())));

      addInitState(initState);

    }

  }



  public ValidationResult validTransition(int oldBrandId, Equipment newEquipment, String swapType, String appId, boolean matchAll) {

    if (newEquipment == null || swapType == null || appId == null)

      throw new NullPointerException("TMBrandTransitionMatrix.validTransition(): Input parameters cannot be null.");



    int[] newBrandIds = newEquipment.getBrandIds();



    if (newBrandIds == null)

      throw new IllegalArgumentException("TMBrandTransitionMatrix.validTransition(): newEquipment.getBrandIds() cannot be null.");



    if (newBrandIds.length == 0)

      newBrandIds = allBrandIds;



    BrandState oldBrandState = new BrandState(oldBrandId, swapType, appId);

    BrandState[] newBrandStates = new BrandState[newBrandIds.length];



    for (int i = 0; i < newBrandIds.length; i++)

      newBrandStates[i] = new BrandState(newBrandIds[i], swapType, appId);



    return validTransition(oldBrandState, newBrandStates, matchAll);

  }



  private void loadAllBrandIds() throws Throwable {

    BrandInfo[] brands = referenceDataHelper.retrieveBrands();





    int brandsSz = brands != null ? brands.length : 0;



    allBrandIds = new int[brandsSz];



    for (int i = 0; i < brandsSz; i++)

      allBrandIds[i] = brands[i].getBrandId();

  }

}

