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
import com.telus.api.reference.SalesRep;
import com.telus.api.util.SessionUtil;
import com.telus.eas.utility.info.SalesRepInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;


public class TMSalesRep extends BaseProvider implements SalesRep {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SalesRep delegate;

	public TMSalesRep(TMProvider provider, SalesRep delegate) {
		super(provider);
		this.delegate = delegate;
	}

	public String getCode() {
		return delegate.getCode();
	}

	public String getName() {
		return delegate.getName();
	}

	public String getDealerCode() {
		return delegate.getDealerCode();
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

	public void setDealerCode(String newDealerCode) {
		delegate.setDealerCode(newDealerCode);
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
	 * Save the sales rep information
	 * @exception TelusAPIException
	 */
	public void save() throws TelusAPIException {

		String dealerCode = "";
		String salesRepCode = "";

		if (this.delegate != null) {
			dealerCode = delegate.getDealerCode();
			salesRepCode = delegate.getCode();

		} else throw new TelusAPIException("Failed to save sales rep - no dealer and sales rep info found");


		SalesRep salesRep = null;
		try {
			salesRep = (SalesRep)provider.getReferenceDataHelperEJB().retrieveDealerSalesRepByCode(dealerCode, salesRepCode, true); 

		} catch (Throwable e) {
			Logger.warning("Failed to find sales rep " + salesRepCode + "; will attempt to add sales rep " + salesRepCode);
		}
		
		if (salesRep == null) {
			try {
				provider.getDealerManagerEJB().addSalesperson((SalesRepInfo)delegate, SessionUtil.getSessionId(provider.getDealerManagerEJB()));
			} catch (Throwable e) {
				Logger.warning("Failed to save sales rep " + salesRepCode);
				provider.getExceptionHandler().handleException(e);
			}

		} else {
			String newName = delegate.getName();
			if (!newName.equals(salesRep.getName())) {
				try {
					provider.getDealerManagerEJB().changeSalespersonName(delegate.getDealerCode(), delegate.getCode(), newName, SessionUtil.getSessionId(provider.getDealerManagerEJB()));
				} catch (Throwable e) {
					Logger.warning("Failed to change name for sales rep " + salesRepCode);
					provider.getExceptionHandler().handleException(e);
				}
			}

			Date curExpiryDate = salesRep.getExpiryDate();
			Date newExpiryDate = delegate.getExpiryDate();

			if (newExpiryDate == null && curExpiryDate != null) {
				try {
					provider.getDealerManagerEJB().unexpireSalesperson(delegate.getDealerCode(), delegate.getCode(), SessionUtil.getSessionId(provider.getDealerManagerEJB()));
				} catch (Throwable e) {
					Logger.warning("Failed to unexpire sales rep " + salesRepCode);
					provider.getExceptionHandler().handleException(e);
				}

			} else if (newExpiryDate != null ) {
				try {
					provider.getDealerManagerEJB().expireSalesperson(delegate.getDealerCode(), delegate.getCode(), newExpiryDate, SessionUtil.getSessionId(provider.getDealerManagerEJB()));
				} catch (Throwable e) {
					Logger.warning("Failed to expire sales rep " + salesRepCode);
					provider.getExceptionHandler().handleException(e);
				}
			}
		}
	}

	/**
	 * Transfer a sales rep to a new dealer from the current dealer.
	 * @param dealer - the Dealer to transfer to
	 * @param transferDate - the effective date of the trasnfer
	 * @throws TelusAPIExcpetion
	 */
	public void transferSalesRep(String dealerCode, Date transferDate) throws TelusAPIException {

		try {
			provider.getReferenceDataHelperEJB().retrieveDealerSalesRepByCode(
					delegate.getDealerCode(), delegate.getCode(), false);
		} catch (Throwable e) {
			Logger.warning("SalesRep doesn't exist - salesCode = " + delegate.getCode());
			throw new TelusAPIException(e);
		}
		
		try {
			provider.getDealerManagerEJB().transferSalesperson((SalesRepInfo)delegate, dealerCode, transferDate, SessionUtil.getSessionId(provider.getDealerManagerEJB()));
		} catch (Throwable e) {
			Logger.warning("Failed to transfer sales rep " + delegate.getCode());
			provider.getExceptionHandler().handleException(e);
		}
	}
}
