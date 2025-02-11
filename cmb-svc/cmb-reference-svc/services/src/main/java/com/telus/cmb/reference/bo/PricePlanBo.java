/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.telus.api.reference.Service;
import com.telus.cmb.common.cache.CacheKeyBuilder;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.OfferPricePlanSetInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.PricePlanTermInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class PricePlanBo extends ReferenceBo<PricePlanInfo> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Log logger = LogFactory.getLog(PricePlanBo.class);

	/**
	 * Added below method for koodo 3.1 to set the price plan offer indicator and exclude KB price plans based on TOM Offer Info
	   Setting the offer indicator based on TOM results in the following order , OfferIndicator is null before setting the flag
		 * 
		 *  1.  Set offer indicator as "true" from kb sql based TOM returned offer group code if price plan is set as "Y" in KB. ( refer the kb price plan sql )
		 *  
		 *  2.  Set offer indicator as "true" if the price plan exists in Offer compatiblePricePlan list
		 *  
		 *  3.  Set offer indicator as "false" if the price plan exists in Offer IncompatiblePricePlan list
		 *  
		 *  4.  Set offer indicator as "true" if  fetchInMarketPricePlansInd is true and InMarketPricePlansOfferInd is true 
		 *  
		 *  5.  Set offer indicator as "false" if  fetchInMarketPricePlansInd is true and InMarketPricePlansOfferInd is false
		 *  
		 *  6.  return the price plan in response if offer indicator is not null from step 1 to 5 
		 * 
	*/
	
	
	public static PricePlanInfo[] filterKBPricePlanListByTOMOffer(OfferPricePlanSetInfo offerPricePlanSet,PricePlanInfo[] kbPricePlans)
			throws TelusException {

		List<PricePlanInfo> filteredPriceplanList = new ArrayList<PricePlanInfo>();
	
		for (PricePlanInfo pricePlanInfo : kbPricePlans) {

			// Apply step 1 , already applied through kb sql

			// Apply step 2 & 3
			setOfferIndicatorFromOfferPricePlanList(offerPricePlanSet,pricePlanInfo);

			// Apply step 4 & 5
			if (offerPricePlanSet.isFetchInMarketPricePlansInd() && pricePlanInfo.isSelectedOffer() == null) {
				pricePlanInfo.setSelectedOffer(offerPricePlanSet.isInMarketPricePlansOfferInd());
			}

			// Apply step 6
			if (pricePlanInfo.isSelectedOffer() != null) {
				filteredPriceplanList.add(pricePlanInfo);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("isFetchInMarketPricePlansInd set to false , excluding the kb priceplan : ["+ pricePlanInfo.getCode() + " ] ");
				}
			}
		}
	
		return filteredPriceplanList.toArray(new PricePlanInfo[0]);

	}

	private static void setOfferIndicatorFromOfferPricePlanList(OfferPricePlanSetInfo offerPricePlanSet, PricePlanInfo pricePlanInfo) {
		
		if (offerPricePlanSet.isCompatiblePricePlan(pricePlanInfo.getCode().trim())) {
			pricePlanInfo.setSelectedOffer(true);
		}

		if (pricePlanInfo.isSelectedOffer() == null && offerPricePlanSet.isIncompatiblePricePlan(pricePlanInfo.getCode().trim())) {
			pricePlanInfo.setSelectedOffer(false);
		}

	}
	
	public static PricePlanBo[] decorate(PricePlanInfo[] pricePlans, ReferenceDataFacade referenceDataFacade) throws TelusException {
		List<PricePlanBo> result = new ArrayList<PricePlanBo>();
		for (PricePlanInfo pricePlan : pricePlans) {
			PricePlanBo pricePlanBo = new PricePlanBo(referenceDataFacade.getPricePlan(pricePlan.getCode()), referenceDataFacade);
			//Add decorate exception for selected offer ind 
			pricePlanBo.getDelegate().setSelectedOffer(pricePlan.isSelectedOffer());
			result.add(pricePlanBo);
		}
		return result.toArray( new PricePlanBo[result.size()]);
	}
	
	public static PricePlanInfo[] undecorate(PricePlanBo[] pricePlanBos) {
		List<PricePlanInfo> result = new ArrayList<PricePlanInfo>();
		for (PricePlanBo pricePlanBo: pricePlanBos) {
			result.add(pricePlanBo.getDelegate());
		}
		return result.toArray( new PricePlanInfo[result.size()]);
	}
	
	public PricePlanBo(PricePlanInfo pricePlan, ReferenceDataFacade referenceDataFacade) throws TelusException {
		super(pricePlan);		
		updateAvailableTermsInMonths(referenceDataFacade);		
	}
	
	public boolean checkServiceAssociation(String serviceCode) {
		Service [] optionalServices = getDelegate().getOptionalServices();
		if (optionalServices != null) {
			for (Service optionalService : optionalServices) {
				if (optionalService.getCode().endsWith(serviceCode)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Calculates available terms in months
	 * 
	 * @param referenceService
	 */
	public void updateAvailableTermsInMonths(ReferenceDataFacade referenceDataFacade) throws TelusException {
		PricePlanTermInfo planTerm = referenceDataFacade.getPricePlanTerm(getDelegate().getCode());
		if (planTerm != null && planTerm.getTermsInMonths().length != 0) {
			getDelegate().setAvailableTermsInMonths(planTerm.getTermsInMonths());
		} else {
			getDelegate().setAvailableTermsInMonths(new int[] {getDelegate().getTermMonths()});
		}
	}
	
	public static class PricePlanCtrieria implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private String code;
		private String equipmentType;
		private String provinceCode;
		private char accountType;
		private char accountSubType;
		private int brandId;
		private String strValue;
		private int hashValue;
		
		public PricePlanCtrieria ( String soc, String equipmentType, String provinceCode, char accountType, char accountSubType, int brandId ) {
			this.code = soc;
			this.equipmentType = equipmentType;
			this.provinceCode = provinceCode;
			this.accountType = accountType;
			this.accountSubType = accountSubType;
			this.brandId = brandId;
			
			//calculate this value only once when object created.
			this.strValue = CacheKeyBuilder.createComplexKey( code, provinceCode, equipmentType, accountType, accountSubType, brandId);
			this.hashValue = strValue.hashCode();
		}

		public String getCode() {
			return code;
		}

		public String getProvinceCode() {
			return provinceCode;
		}

		public String getEquipmentType() {
			return equipmentType;
		}

		public char getAccountType() {
			return accountType;
		}

		public char getAccountSubType() {
			return accountSubType;
		}

		public int getBrandId() {
			return brandId;
		}

		@Override 
		public boolean equals( Object o ) {
			boolean result = false; 
			if ( o instanceof PricePlanCtrieria ) {
				PricePlanCtrieria that = (PricePlanCtrieria)o;
				
				//every attribute has to exact match each others
				result = this.hashValue==that.hashValue
				  && Info.compare(this.code,that.code)
				  && this.accountType==that.accountType
				  && this.accountSubType==that.accountSubType
				  && this.brandId==that.brandId
				  && Info.compare( this.equipmentType,that.equipmentType )
				  && Info.compare( this.provinceCode, that.provinceCode )
				  ;
			}
			return result;
		}
		
		@Override
		public int hashCode() {
			return hashValue;
		}
		
		@Override
		public String toString() {
			return strValue;
		}
	}

}
