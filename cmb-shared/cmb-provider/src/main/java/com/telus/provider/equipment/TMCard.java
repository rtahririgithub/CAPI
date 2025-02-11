/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.equipment;

import java.util.Date;

import com.telus.api.InvalidServiceException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ProrationMinutes;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Card;
import com.telus.api.reference.Service;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.productequipment.manager.svc.ProductEquipmentManager;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;


public class TMCard extends BaseProvider implements Card {

	/**
	 * @link aggregation
	 */
	private final CardInfo delegate;

	// Moved BILLINGTYPE_POSTPAID and BILLINGTYPE_PREPAID static variables from the EquipmentHelperEJBRemote
	// interface - R. Fong, 2004/04/27.
	private static String BILLINGTYPE_POSTPAID = "POSTPAID";
	private static String BILLINGTYPE_PREPAID = "PREPAID";

	public TMCard(TMProvider provider, CardInfo delegate) {
		super(provider);
		this.delegate = delegate;
	}

	public CardInfo getDelegate() {
		return delegate;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public String getType() {
		return delegate.getType();
	}

	public String getProductTypeId() {
		return delegate.getProductTypeId();
	}

	public int getStatus() {
		return delegate.getStatus();
	}

	public Date getStatusDate() {
		return delegate.getStatusDate();
	}

	public String getSerialNumber() {
		return delegate.getSerialNumber();
	}

	public String getDescription() {
		return delegate.getDescription();
	}

	public String getDescriptionFrench() {
		return delegate.getDescriptionFrench();
	}

	public Date getAvailableFromDate() {
		return delegate.getAvailableFromDate();
	}

	public Date getAvailableToDate() {
		return delegate.getAvailableToDate();
	}

	public double getAmount() {
		return delegate.getAmount();
	}

	public String getPhoneNumber() {
		return delegate.getPhoneNumber();
	}

	public int getBanId() {
		return delegate.getBanId();
	}

	public String getAdjustmentCode() {
		return delegate.getAdjustmentCode();
	}

	public boolean isFeatureCard() {
		return delegate.isFeatureCard();
	}

	public boolean isGameCard() {
		return delegate.isGameCard();
	}

	public boolean isMinuteCard() {
		return delegate.isMinuteCard();
	}

	public boolean isAirtimeCard() {
		return delegate.isAirtimeCard();
	}

	public int getTotalGames() {
		return delegate.getTotalGames();
	}

	public int getTotalMinutes() {
		return delegate.getTotalMinutes();
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
	public String getBillingType(Subscriber subscriber) throws TelusAPIException {
		// Moved the BILLINGTYPE_POSTPAID and BILLINGTYPE_PREPAID static variables from the EquipmentHelperEJBRemote
		// to the TMCard class as static variables - R. Fong, 2004/04/27.
		//return (subscriber.getAccount().isPostpaid())?EquipmentHelperEJBRemote.BILLINGTYPE_POSTPAID:EquipmentHelperEJBRemote.BILLINGTYPE_PREPAID;
		return (subscriber.getAccount().isPostpaid())?BILLINGTYPE_POSTPAID:BILLINGTYPE_PREPAID;
	}


	public Service[] getServices(Subscriber subscriber) throws InvalidServiceException, TelusAPIException {
		try {
			Logger.debug("ProductEquipmentHelper.getCardServices(\""+getSerialNumber()+"\", \""+subscriber.getEquipment().getTechType()+"\", \""+getBillingType(subscriber)+"\");");
			ServiceInfo[] info = null;
			info = provider.getProductEquipmentHelper().getCardServices(getSerialNumber(), 
				subscriber.getEquipment().getTechType(), getBillingType(subscriber));
			
			if(info.length == 0) {
				// TODO: failure could be TECHNOLOGY_MISMATCH or BILLING_TYPE_MISMATCH.
				throw new InvalidServiceException(InvalidServiceException.TECHNOLOGY_MISMATCH, "card ("+getSerialNumber()+") cannot be applied to subscriber ("+subscriber.getPhoneNumber()+")");
			}

			if(subscriber.getAccount().isPostpaid()) {
				info = (ServiceInfo[])provider.getReferenceDataManager0().filterServicesByPricePlan(info, subscriber.getContract().getPricePlan());
				if(info.length == 0) {
					throw new InvalidServiceException(InvalidServiceException.PRICEPLAN_MISMATCH, "card ("+getSerialNumber()+") services are not applicable to price plan ("+subscriber.getContract().getPricePlan().getCode()+")");
				}

				info = (ServiceInfo[])provider.getReferenceDataManager0().filterServicesByProvince(info, subscriber.getMarketProvince());
				if(info.length == 0) {
					throw new InvalidServiceException(InvalidServiceException.PROVINCE_MISMATCH, "card ("+getSerialNumber()+") services are not applicable to this province ("+subscriber.getMarketProvince()+")");
				}
			}

			return provider.getReferenceDataManager0().getFullServices(info);
		}catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public void setCredited(Subscriber subscriber, boolean autoRenew) throws TelusAPIException {
		try {
			provider.getProductEquipmentManager().creditCard(getSerialNumber(), subscriber.getBanId(), subscriber.getPhoneNumber(), 
				subscriber.getEquipment().getSerialNumber(), autoRenew,	provider.getUser());
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	public void setStolen() throws TelusAPIException {
		try {
			provider.getProductEquipmentManager().setCardStatus(getSerialNumber(), STATUS_STOLEN, provider.getUser());
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	public ProrationMinutes[] getProrationMinutes(Account account, int months) throws TelusAPIException{
		return account.getProrationMinutes(months, getTotalMinutes());
	}

	public boolean isLive (){
		return delegate.isLive ();
	}


}



