/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import com.telus.api.account.*;
import com.telus.eas.account.info.*;
import com.telus.provider.*;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.Logger;

import java.util.*;


public class TMPersonalCredit extends BaseProvider implements PersonalCredit {

	private static final long serialVersionUID = 1L;
/**
   * @link aggregation
   */
  private TMAccount account;
  private final PersonalCreditInfo delegate;
  private final TMCreditCard creditCard;


  public TMPersonalCredit(TMProvider provider, PersonalCreditInfo delegate, TMAccount account) {
    super(provider);
    this.delegate = delegate;
    this.account = account;
    creditCard = new TMCreditCard(provider, delegate.getCreditCard0(), account);
  }

  private  boolean getBlockRuleStatus(){
	  
	  boolean block=false;
		try{
			block=provider.getAccountLifecycleFacade().isEnterpriseManagedData(account.getBrandId(), account.getAccountType(),
					account.getAccountSubType(), account.getProductType(), AccountSummary.PROCESS_TYPE_ACCOUNT_UPDATE);
		} catch (Throwable e) {
			Logger.warning("Error while executing EnterpriseManagedData block rule");
		}
		return block;
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public Date getBirthDate() {
    return delegate.getBirthDate();
  }

  public void setBirthDate(Date birthDate) {
	  if(!(account.getBanId()!=0 && isBlockDirectUpdate() && getBlockRuleStatus()))
	    delegate.setBirthDate(birthDate);
  }

  public String getSin() {
    return delegate.getSin();
  }

  public void setSin(String sin) {
	  if(!(account.getBanId()!=0 && isBlockDirectUpdate() && getBlockRuleStatus()))
	    delegate.setSin(sin);
  }

  public String getDriversLicense() {
    return delegate.getDriversLicense();
  }

  public void setDriversLicense(String driversLicense) {
	  if(!(account.getBanId()!=0 && isBlockDirectUpdate() && getBlockRuleStatus()))
		  delegate.setDriversLicense(driversLicense);
  }

  public Date getDriversLicenseExpiry() {
    return delegate.getDriversLicenseExpiry();
  }

  public void setDriversLicenseExpiry(Date driversLicenseExpiry) {
	  if(!(account.getBanId()!=0 && isBlockDirectUpdate() && getBlockRuleStatus()))
		  delegate.setDriversLicenseExpiry(driversLicenseExpiry);
  }

  public String getDriversLicenseProvince() {
    return delegate.getDriversLicenseProvince();
  }

  public void setDriversLicenseProvince(String driversLicenseProvince) {
	  if(!(account.getBanId()!=0 && isBlockDirectUpdate() && getBlockRuleStatus()))
	  {
		  String province = driversLicenseProvince;
		  if ("NL".equals(province)) {
			  province = "NF";
		  }
		  if ("QC".equals(province)) {
			  province = "PQ";
		  }
	
		  delegate.setDriversLicenseProvince(province);
	  }
  }

  public boolean isDriversLicenseExpired() {
    return delegate.isDriversLicenseExpired();
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
  public PersonalCreditInfo getDelegate() {
    return delegate;
  }

  public CreditCard getCreditCard() {
    return creditCard;
  }

  public TMCreditCard getCreditCard0() {
    return creditCard;
  }

  public void commit() {
    creditCard.commit();
  }

  private boolean isBlockDirectUpdate() {
		return AppConfiguration.isBlockDirectUpdate();
  }
}



