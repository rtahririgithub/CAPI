/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.chargeableservices;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Charge;
import com.telus.api.account.Subscriber;
import com.telus.api.chargeableservices.ChargeableService;
import com.telus.api.chargeableservices.ChargeableServiceManager;
import com.telus.api.chargeableservices.Waiver;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.reference.BusinessRole;
import com.telus.eas.config.info.ChargeableServiceInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.account.TMAccount;
import com.telus.provider.account.TMCharge;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.util.Logger;

public class TMChargeableService extends TMChargeableServiceDescriptor implements ChargeableService {

	private final TMSubscriber subscriber;  // can be null


	public TMChargeableService(TMProvider provider, ChargeableServiceInfo delegate, TMSubscriber subscriber) {
		super(provider, delegate);
		this.subscriber = subscriber;
	}

	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public void assertNotAppliedOrWaived() throws TelusAPIException {
		if (isAppliedOrWaived()) {
			throw new TelusAPIException("ChargeableService [" + getCode() + "] has alread been " + ((isApplied())?"applied":"waived"));
		}
	}


	public synchronized void apply() throws TelusAPIException {
		assertNotAppliedOrWaived();

		//    if (getCharge() == 0.0) {

		Account a = subscriber.getAccount();
		if (getCharge() == 0.0 && (getCode().equals(ChargeableServiceManager.SERVICE_CLIENT_PCS_ACTIVATION_FEE)) ||
				(getCode().equals(ChargeableServiceManager.SERVICE_ONEXRTT_ACTIVATION_FEE)) ||
				(getCode().equals(ChargeableServiceManager.SERVICE_PCS_ACTIVATION_FEE))) {
			delegate.setWaived(true);
			provider.getInteractionManager0().subscriberNewCharge(subscriber, getCode(), Waiver.WAIVER_NO_CHARGE);
		} else if (getCharge() == 0.0 ||
				(getCode().equals(ChargeableServiceManager.SERVICE_MIKE_ACTIVATION_FEE)
						// All corporate Mike activations are waived
						&& (((TMAccount)a).getDelegate0().isCorporate() && a.isIDEN()))) { //&& (a.isCorporateRegular() || a.isCorporatePrivateNetworkPlus()))) {
			delegate.setWaived(true);
			provider.getInteractionManager0().subscriberNewCharge(subscriber, getCode(), Waiver.WAIVER_NO_CHARGE);
		} else {
			Charge charge = subscriber.newCharge();

			charge.setAmount(getCharge());

			if (getCode().equals(ChargeableServiceManager.SERVICE_HANDSET_EXCHANGE)) {

				if (delegate.getRoleName().equals(BusinessRole.BUSINESS_ROLE_AGENT)) {
					if (subscriber.isIDEN()) {
						charge.setChargeCode(TMChargeableServiceManager.CHARGE_IDEN_HANDSET_EXCHANGE_AGENT);
					} else if (((TMCharge)charge).getDelegate().isPrepaid()) {
						charge.setChargeCode(TMChargeableServiceManager.CHARGE_PREPAID_HANDSET_EXCHANGE_AGENT);
					} else {
						charge.setChargeCode(TMChargeableServiceManager.CHARGE_PCS_HANDSET_EXCHANGE_AGENT);
					}
				} else if (delegate.getRoleName().equals(BusinessRole.BUSINESS_ROLE_CLIENT) ) {
					if (subscriber.isIDEN()) {
						charge.setChargeCode(TMChargeableServiceManager.CHARGE_IDEN_HANDSET_EXCHANGE_CLIENT);
					} else if (((TMCharge)charge).getDelegate().isPrepaid()) {
						charge.setChargeCode(TMChargeableServiceManager.CHARGE_PREPAID_HANDSET_EXCHANGE_CLIENT);
					} else {
						charge.setChargeCode(TMChargeableServiceManager.CHARGE_PCS_HANDSET_EXCHANGE_CLIENT);
					}
				}
			} else if (getCode().equals(ChargeableServiceManager.SERVICE_PRICEPLAN_CHANGE)) {
				charge.setChargeCode(TMChargeableServiceManager.CHARGE_PRICEPLAN_CHANGE);
			} else if (getCode().equals(ChargeableServiceManager.SERVICE_MIKE_ACTIVATION_FEE) && (subscriber.getStatus() == Subscriber.STATUS_RESERVED)) {
				//if the subscriber in reserve status for following reason feature date activation
				charge = subscriber.getAccount().newCharge();
				if (subscriber.getEquipment().isRIM()){
					charge.setChargeCode("MBSF");
				} else if (subscriber.getEquipment().isSIMCard()) {
					MuleEquipment mule = ((SIMCardEquipment)subscriber.getEquipment()).getLastMule();
					if (mule != null && mule.isIDENRIM())
						charge.setChargeCode("MBSF");
					else
						charge.setChargeCode("ACTMK1");
				} else {
					charge.setChargeCode("ACTMK1");
				}
			} else if (getCode().equals(ChargeableServiceManager.SERVICE_MIKE_ACTIVATION_FEE)) {
				if (subscriber.getEquipment().isRIM()) {
					charge = subscriber.newCharge();
					charge.setChargeCode("MBSF");
				} else if (subscriber.getEquipment().isSIMCard()) {
					MuleEquipment mule = ((SIMCardEquipment)subscriber.getEquipment()).getLastMule();
					if (mule != null && mule.isIDENRIM()){
						charge = subscriber.newCharge();
						charge.setChargeCode("MBSF");
					} else
						charge.setChargeCode(getCode());
				} else {
					charge.setChargeCode(getCode());
				}
			} else if ((getCode().equals(ChargeableServiceManager.SERVICE_CLIENT_PCS_ACTIVATION_FEE)) && (subscriber.getStatus() == Subscriber.STATUS_RESERVED)) {
				charge = subscriber.getAccount().newCharge();
				charge.setChargeCode("ACTVWB");
			} else if ((getCode().equals(ChargeableServiceManager.SERVICE_PCS_ACTIVATION_FEE)) && (subscriber.getStatus() == Subscriber.STATUS_RESERVED)) {
				charge = subscriber.getAccount().newCharge();
				charge.setChargeCode("ACTV35");
			} else if ((getCode().equals(ChargeableServiceManager.SERVICE_ONEXRTT_ACTIVATION_FEE)) && (subscriber.getStatus() == Subscriber.STATUS_RESERVED)) {
				charge = subscriber.getAccount().newCharge();
				charge.setChargeCode("ACT1X");
			} else if ((getCode().equals(ChargeableServiceManager.SERVICE_PHONE_NUMBER_CHANGE_FEE))) {
				// If the account is prepaid, use the prepaid charge code.
				if (((TMCharge)charge).getDelegate().isPrepaid()) {
					charge.setChargeCode(TMChargeableServiceManager.CHARGE_PREPAID_PHONE_NUMBER_CHANGE);
				} else {
					charge.setChargeCode(TMChargeableServiceManager.CHARGE_POSTPAID_PHONE_NUMBER_CHANGE);
				} 			
			} else {
				charge.setChargeCode(getCode());
			}

			Logger.debug("Service = [" + getCode() + "]");
			Logger.debug("charge  = [" + charge.getChargeCode() + "]");
			Logger.debug("Charge amount = " + charge.getAmount() + "]");

			charge.apply();

			delegate.setApplied(true);

			provider.getInteractionManager0().subscriberNewCharge(subscriber, getCode(), null);
		}
	}

	public synchronized void waive(Waiver waiver) throws TelusAPIException {
		assertNotAppliedOrWaived();

		if (waiver == null) {
			throw new NullPointerException("waiver is null, use one of ChargeableService.getWaivers()");
		}

		if (!containsWaiver(waiver.getCode())) {
			throw new TelusAPIException("Waiver [" + waiver.getCode() + "] is not one that can be used on ChargeableService [" + getCode() + "]");
		}

		provider.getInteractionManager0().subscriberNewCharge(subscriber, getCode(), waiver.getCode());
		delegate.setWaived(true);
	}

	public String toString()
	{
		StringBuffer s = new StringBuffer(128);

		s.append("TMChargeableService:[\n");
		s.append("    subscriber.getPhoneNumber()=[").append(subscriber.getPhoneNumber()).append("]\n");
		s.append("    ").append(super.toString()).append("]\n");
		s.append("]");

		return s.toString();
	}



}



