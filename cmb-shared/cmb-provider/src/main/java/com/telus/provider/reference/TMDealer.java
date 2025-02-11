package com.telus.provider.reference;
/**
 * Title:        Telus ECA
 * Description:  Enterprise Development
 * Copyright:    Copyright (c) 2002
 * Company:      TELUS Mobility
 * @author       Lam Tran
 * @version      1.0
 */

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.reference.Dealer;
import com.telus.api.reference.SalesRep;
import com.telus.api.util.SessionUtil;
import com.telus.eas.utility.info.DealerInfo;
import com.telus.eas.utility.info.SalesRepInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;


public class TMDealer extends BaseProvider implements Dealer {

	private static final long serialVersionUID = 1L;
	private  Dealer delegate;

	public TMDealer(TMProvider provider, Dealer delegate) {
		super(provider);
		this.delegate = delegate;
	}

	public String getCode() {
		return delegate.getCode();
	}

	public String getName() {
		return delegate.getName();
	}

	public String getDepartmentCode() {
		return delegate.getDepartmentCode();
	}

	public String getNumberLocationCD() {
		return delegate.getNumberLocationCD();
	}

	public String getDescription() {
		return delegate.getDescription();
	}

	public String getDescriptionFrench() {
		return delegate.getDescriptionFrench();
	}

	public Date getEffectiveDate() {
		return delegate.getEffectiveDate();
	}

	public Date getExpiryDate() {
		return delegate.getExpiryDate();
	}

	public void setCode(String newCode) {
		delegate.setCode(newCode);
	}

	public void setName(String newName) {
		delegate.setName(newName);
	}

	public void setDepartmentCode(String newDepartmentCode) {
		delegate.setDepartmentCode(newDepartmentCode);
	}

	public void setNumberLocationCD(String newNumberLocationCD) {
		delegate.setNumberLocationCD(newNumberLocationCD);
	}

	public void setDescription(String newDescription) {
		delegate.setDescription(newDescription);
	}

	public void setDescriptionFrench(String newDescription) {
		delegate.setDescriptionFrench(newDescription);
	}

	public void setEffectiveDate(Date newDate) {
		delegate.setEffectiveDate(newDate);
	}

	public void setExpiryDate(Date newDate) {
		delegate.setExpiryDate(newDate);
	}

	/**
	 * Find a sales rep for the given dealer using the sales rep code.
	 * @param salesRepCode - the Knowbility sales rep code
	 * @return the sales rep instance or null if not found
	 * @exception com.telus.api.TelusAPIException
	 */
	public SalesRep findSalesRep(String salesRepCode) throws TelusAPIException {

		String dealerCode = "";
		if (this.delegate != null) {
			dealerCode = delegate.getCode();
		} else throw new TelusAPIException("Failed to find sales rep - no dealer info found");

		try {
			return (SalesRep)provider.getReferenceDataHelperEJB().retrieveDealerSalesRepByCode(dealerCode, salesRepCode, false);

		} catch (Throwable e) {
			Logger.warning("Failed to find sales rep " + salesRepCode + "; returning null");
			return null;
		}
	}

	/**
	 * Create a new unsaved sales rep for the given dealer.
	 * @exception TelusAPIException
	 * @return a new sales rep with the current dealers dealer code and given sales rep code
	 * @param salesRepCode - the Knowbility sales rep code*/
	public SalesRep newSalesRep(String salesRepCode) throws TelusAPIException {

		String dealerCode = "";
		if (this.delegate != null) {
			dealerCode = delegate.getCode();

		} else throw new TelusAPIException("Failed to create new sales rep - no dealer info found");

		SalesRepInfo  salesRepInfo = new SalesRepInfo();
		if (salesRepInfo != null) {
			salesRepInfo.setCode(salesRepCode);
			salesRepInfo.setDealerCode(dealerCode);
		}

		return new TMSalesRep(provider, salesRepInfo);
	}

	/**
	 * Create a new unsaved sales rep for the given dealer, cloning the existing sales rep.
	 * @exception TelusAPIException
	 * @return a new sales rep with the current dealers dealer code with name and sales rep code copied from the original Sales Rep.
	 * @param salesRep - an existing sales rep*/
	public SalesRep newSalesRep(SalesRep salesRep) throws TelusAPIException {

		SalesRepInfo newSalesRepInfo = new SalesRepInfo();
		if (newSalesRepInfo != null) {
			copySalesRepInfo(newSalesRepInfo, salesRep);
		}

		return new TMSalesRep(provider, newSalesRepInfo);
	}

	/**
	 * Save the dealer information
	 * @exception TelusAPIException
	 */
	public void save()throws TelusAPIException {

		String dealerCode = "";
		if (this.delegate != null) {
			dealerCode = delegate.getCode();

		} else throw new TelusAPIException("Failed to create new dealer - no dealer info found");

		Dealer dealer = null;
		try {
			dealer = (Dealer)provider.getReferenceDataHelperEJB().retrieveDealerbyDealerCode(dealerCode, true);

		} catch (Throwable e) {
			Logger.warning("Failed to find dealer " + dealerCode + "; will attempt to add dealer " + dealerCode);
		} 

		if (dealer == null) {
			try {
				provider.getDealerManagerEJB().addDealer((DealerInfo)delegate, SessionUtil.getSessionId(provider.getDealerManagerEJB()));
			} catch (Throwable e) {
				Logger.warning("Failed to addDealer " + dealerCode);
				provider.getExceptionHandler().handleException(e);
			}

		} else {
			String newName = delegate.getName();
			if (!newName.equals(dealer.getName())) {
				try {
					provider.getDealerManagerEJB().changeDealerName(delegate.getCode(), newName, SessionUtil.getSessionId(provider.getDealerManagerEJB()));
				} catch (Throwable e) {
					Logger.warning("Failed to change name for dealer " + dealerCode);
					provider.getExceptionHandler().handleException(e);
				}
			}

			Date curExpiryDate = dealer.getExpiryDate();
			Date newExpiryDate = delegate.getExpiryDate();

			if (newExpiryDate == null && curExpiryDate != null) {
				try {
					provider.getDealerManagerEJB().unexpireDealer(delegate.getCode(), SessionUtil.getSessionId(provider.getDealerManagerEJB()));
				} catch (Throwable e) {
					Logger.warning("Failed to unexpire dealer " + dealerCode);
					provider.getExceptionHandler().handleException(e);
				}

			} else if (newExpiryDate != null) {
				try {
						provider.getDealerManagerEJB().expireDealer(delegate.getCode(), newExpiryDate, SessionUtil.getSessionId(provider.getDealerManagerEJB()));
				} catch (Throwable e) {
					Logger.warning("Failed to expire dealer " + dealerCode);
					provider.getExceptionHandler().handleException(e);
				}
			}
		} 
	}

	/** supporting method */
	public void copySalesRepInfo(SalesRep newSalesRep, SalesRep oldSalesRep){

		if (oldSalesRep == null || newSalesRep == null)
			return;

		newSalesRep.setCode(oldSalesRep.getCode());
		newSalesRep.setDealerCode(oldSalesRep.getDealerCode());
		newSalesRep.setName(oldSalesRep.getName());
		newSalesRep.setDescription(oldSalesRep.getDescription());
		newSalesRep.setDescriptionFrench(oldSalesRep.getDescriptionFrench());
		newSalesRep.setEffectiveDate(oldSalesRep.getEffectiveDate());
		newSalesRep.setExpiryDate(oldSalesRep.getExpiryDate());
	}
}
