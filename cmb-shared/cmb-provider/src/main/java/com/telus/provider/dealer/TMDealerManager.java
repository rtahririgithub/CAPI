package com.telus.provider.dealer;
/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.dealer.CPMSDealer;
import com.telus.api.dealer.DealerManager;
import com.telus.api.reference.Dealer;
import com.telus.api.reference.SalesRep;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.equipment.info.CPMSDealerInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.DealerInfo;
import com.telus.eas.utility.info.SalesRepInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.reference.TMDealer;
import com.telus.provider.reference.TMSalesRep;
import com.telus.provider.util.Logger;
import com.telus.provider.util.ProviderDealerExceptionTranslator;


public class TMDealerManager extends BaseProvider implements DealerManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TMDealerManager(TMProvider provider) {
		super(provider);
	}

	public CPMSDealer getCPMSDealer(String dealerCode, String salesRepCode) throws UnknownObjectException, TelusAPIException {

		String channelOrgCode = dealerCode;
		String userCode = salesRepCode;
		int[] brandIds = null;

		try {
			try {
				// check if it's KB dealer Code				
				if (provider.getReferenceDataFacade().getDealerSalesRepByCode(dealerCode, salesRepCode, false) == null)
					throw new TelusException(); // throw exception to be caught below
				try {
					CPMSDealer cpmsDealer = null;
					cpmsDealer = (CPMSDealer)provider.getDealerManagerEJB().getCPMSDealerByKBDealerCode(dealerCode, salesRepCode);
					channelOrgCode = cpmsDealer.getChannelCode();
					userCode = cpmsDealer.getUserCode();
					brandIds = cpmsDealer.getBrandIds();
					
				} catch (Throwable e) {
					TelusExceptionTranslator telusExceptionTranslator= new ProviderDealerExceptionTranslator(dealerCode + salesRepCode);
					provider.getExceptionHandler().handleException(e,telusExceptionTranslator);					
				}
			} catch (TelusException e){
				// not KB dealer
				Logger.warning("Not a KB dealer - retrieving CPMS dealer info using dealer code and sales rep code");
			} catch (Throwable e) {
				throw new TelusAPIException(e);
			}
			
			CPMSDealerInfo cpmsDealerInfo = null;
			cpmsDealerInfo = provider.getDealerManagerEJB().getCPMSDealerInfo(channelOrgCode, userCode);
			cpmsDealerInfo.setBrandIds(brandIds);

			return (new TMCPMSDealer(provider, cpmsDealerInfo));

		} catch (Throwable e) { 					
			TelusExceptionTranslator telusExceptionTranslator= new ProviderDealerExceptionTranslator(dealerCode + salesRepCode);
			provider.getExceptionHandler().handleException(e,telusExceptionTranslator);
		}
		return null;
	}

	public CPMSDealer getCPMSDealerByLocationTelephoneNumber(String locationTelephoneNumber) throws UnknownObjectException, TelusAPIException {

		String channelOrgCode = null;
		String userCode = null;
		int[] brandIds = null;

		try {
			try {
				CPMSDealer cpmsDealer = null;
				cpmsDealer = (CPMSDealer)provider.getDealerManagerEJB().getCPMSDealerByLocationTelephoneNumber(locationTelephoneNumber);
				channelOrgCode = cpmsDealer.getChannelCode();
				userCode = cpmsDealer.getUserCode();
			} catch (Throwable e) { 					
				TelusExceptionTranslator telusExceptionTranslator= new ProviderDealerExceptionTranslator(locationTelephoneNumber);
				provider.getExceptionHandler().handleException(e,telusExceptionTranslator);
			}

			CPMSDealerInfo cpmsDealerInfo = null;
			cpmsDealerInfo = provider.getDealerManagerEJB().getCPMSDealerInfo(channelOrgCode, userCode);
			cpmsDealerInfo.setBrandIds(brandIds);

			return (new TMCPMSDealer(provider,cpmsDealerInfo));

		} catch (Throwable e) { 					
			TelusExceptionTranslator telusExceptionTranslator= new ProviderDealerExceptionTranslator(locationTelephoneNumber);
			provider.getExceptionHandler().handleException(e,telusExceptionTranslator);
		}
		return null;
	}

	public long[] getChnlOrgAssociationSAPSoldToParty(long chnlOrgId) throws TelusAPIException {
		try {
			return provider.getDealerManagerEJB().getChnlOrgAssociationSAPSoldToParty(chnlOrgId);
		} catch (Throwable e) { 					
			TelusExceptionTranslator telusExceptionTranslator= new ProviderDealerExceptionTranslator(String.valueOf(chnlOrgId));
			provider.getExceptionHandler().handleException(e,telusExceptionTranslator);
		}
		return null;
	}

	/**
	 * Find a Knowbility dealer using it's dealer code.
	 * @return the dealer instance or null of not found
	 * @exception TelusAPIException
	 * @param dealerCode - the Knowbility dealer code
	 * 
	 * */
	public Dealer findDealer(String dealerCode) throws TelusAPIException {
		return findDealer(dealerCode, false);
	}

	/**
	 * Find a Knowbility dealer using it's dealer code.
	 * @return the dealer instance or null of not found
	 * @exception TelusAPIException
	 * @param dealerCode - the Knowbility dealer code
	 * @param expired - boolean to indicate if include expired dealer
	 * 
	 * */
	public Dealer findDealer(String dealerCode, boolean expired) throws TelusAPIException {

		try {
			Dealer dealer = null;
			dealer = (Dealer)provider.getReferenceDataHelperEJB().retrieveDealerbyDealerCode(dealerCode, expired);

			if (dealer != null)
				return (new TMDealer(provider,dealer));

			return null;

		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * Find a Knowbility sales rep using the dealer code and sales rep code
	 * @exception TelusAPIException
	 * @return the sales rep instance or null if not found
	 * @param dealerCode - the Knowbility dealer code
	 * @param salesRepCode - the Knowbility sales rep code
	 * 
	 */
	public SalesRep findSalesRep(String dealerCode, String salesRepCode) throws TelusAPIException {
		return findSalesRep(dealerCode, salesRepCode, false);
	}

	/**
	 * Find a Knowbility sales rep using the dealer code and sales rep code
	 * @exception TelusAPIException
	 * @return the sales rep instance or null if not found
	 * @param dealerCode - the Knowbility dealer code
	 * @param salesRepCode - the Knowbility sales rep code
	 * @param expired   -  booean to indicate if include expired salesrep
	 */
	public SalesRep findSalesRep(String dealerCode, String salesRepCode, boolean expired) throws TelusAPIException {

		try {
			SalesRep salesRep = null;
			salesRep = (SalesRep)provider.getReferenceDataHelperEJB().retrieveDealerSalesRepByCode(dealerCode, salesRepCode, expired);

			if (salesRep != null)
				return new TMSalesRep(provider, salesRep);

			return null;

		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * Create a new unsaved Knowbility dealer.
	 * @exception TelusAPIException
	 * @return a new dealer with the given dealer code
	 * @param dealerCode - the Knowbility dealer code
	 */
	public Dealer newDealer(String dealerCode) throws TelusAPIException {
		DealerInfo dealerInfo = new DealerInfo();
		dealerInfo.setCode(dealerCode);
		return new TMDealer(provider, dealerInfo);
	}

	/**
	 * Create a new unsaved Knowbility sales rep.
	 * @exception TelusAPIException
	 * @return a new sales rep with the given dealer and sales rep codes
	 * @param dealerCode - the Knowbility dealer code
	 * @param salesRepCode - the Knowbility sales rep code
	 */
	public SalesRep newSalesRep(String dealerCode, String salesRepCode) throws TelusAPIException {
		SalesRepInfo  salesRepInfo = new SalesRepInfo();
		salesRepInfo.setCode(salesRepCode);
		salesRepInfo.setDealerCode(dealerCode);
		return new TMSalesRep(provider, salesRepInfo);
	}

}
