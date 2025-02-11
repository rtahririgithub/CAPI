/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.equipment;

import java.util.Iterator;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.equipment.AirtimeCard;
import com.telus.api.reference.PrepaidRechargeDenomination;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.util.AppConfiguration;

public class TMAirtimeCard extends TMCard implements AirtimeCard {

	private static final long serialVersionUID = 1L;

	public TMAirtimeCard(TMProvider provider, CardInfo delegate) {
		super(provider, delegate);
	}
  
  public boolean isValidAmountForApp () throws TelusAPIException {
  	
  	double amountInCard = getAmount(); 
  	boolean amountFound =  false;
  	if (amountInCard > 0.0) {
		String application = provider.getApplication().trim();
		String rechargeType = "";
		
		List appNameList =  AppConfiguration.getAppNamesForAirtimeCard();
		
		if(!appNameList.isEmpty()) {
			boolean nameFound = false;
			Iterator it = appNameList.iterator();
			while(it.hasNext()) {
				if(application.equalsIgnoreCase(((String)it.next()).trim())) {
					rechargeType = ReferenceDataManager.RECHARGE_TYPE_ACTIVATION_CREDIT;
					nameFound=true;
					break;
				}
			}
			if(!nameFound)
				rechargeType = ReferenceDataManager.RECHARGE_TYPE_ACTIVATION_CREDIT_FS;
		}
		else {
			rechargeType = ReferenceDataManager.RECHARGE_TYPE_ACTIVATION_CREDIT_FS;
		}
		PrepaidRechargeDenomination[] denominations = provider.getReferenceDataManager().getPrepaidRechargeDenominations(rechargeType);
		
		for(int i=0; i<denominations.length; i++) {
			PrepaidRechargeDenomination prepaidRechargeDenomination = denominations[i];
			if(amountInCard == prepaidRechargeDenomination.getAmount()){
				amountFound = true;
				break;
			}
		}
  	}
	return amountFound;
  }
  
}


