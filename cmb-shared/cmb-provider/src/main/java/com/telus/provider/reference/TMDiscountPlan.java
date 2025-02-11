/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.reference;

import com.telus.api.reference.*;
import com.telus.eas.utility.info.*;
import java.util.*;
import com.telus.api.*;

public class TMDiscountPlan implements DiscountPlan {
	/**
	 *@link aggregation
	 */
	protected final TMReferenceDataManager referenceDataManager;

	/**
	 * @link aggregation
	 */
	private final DiscountPlanInfo delegate;

	public TMDiscountPlan(
		TMReferenceDataManager referenceDataManager,
		DiscountPlanInfo delegate) {
		this.referenceDataManager = referenceDataManager;
		this.delegate = delegate;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public Date getEffectiveDate() {
		return delegate.getEffectiveDate();
	}

	public Date getExpiration() {
		return delegate.getExpiration();
	}

	public Date getOfferExpirationDate() {
		return delegate.getOfferExpirationDate();
	}

	public String[] getGroupCodes() {
		return delegate.getGroupCodes();
	}

	public int getMonths() {
		return delegate.getMonths();
	}

	public double getAmount() {
		return delegate.getAmount();
	}

	public double getPercent() {
		return delegate.getPercent();
	}

	public String getDescription() {
		return delegate.getDescription();
	}

	public String getDescriptionFrench() {
		return delegate.getDescriptionFrench();
	}

	public String getCode() {
		return delegate.getCode();
	}

	public boolean isAvailableForActivation() {
		return delegate.isAvailableForActivation();
	}

	public boolean isAvailableForChange() {
		return delegate.isAvailableForChange();
	}

	public String getProductType() {
		return delegate.getProductType();
	}

	public String getLevel() {
		return delegate.getLevel();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public DiscountPlanInfo getDelegate() {
		return delegate;
	}

	public boolean isAssociated(ServiceSummary serviceSummary, int term)
		throws TelusAPIException {
		try {
			DiscountPlanInfo[] discountCodes =
				referenceDataManager
					.getReferenceDataHelperEJB()
					.retrieveDiscountPlans(
					false,
					serviceSummary.getCode(),
					term);
			for (int i = 0; i < discountCodes.length; i++) {
				if ((discountCodes[i].getCode()).equals(getCode())) {
					return true;
				}
			}
			return false;
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	public int[] getDiscountBrandIDs (){
		return delegate.getDiscountBrandIDs();
	}

}
