/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.interaction.AddressChange;
import com.telus.api.interaction.InteractionManager;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;


/**
  * Value (info) object for the Address Change interaction detail information.
  *
  */
public class AddressChangeInfo extends ConfigurationManagerInfo implements AddressChange {

  static final long serialVersionUID = 1L;

  private String oldAddressLine = "";
  private String oldCity = "";
  private String oldProvince = "";
  private String oldCountry = "";
  private String oldPostal = "";

  private String newAddressLine = "";
  private String newCity = "";
  private String newProvince = "";
  private String newCountry = "";
  private String newPostal = "";
  
  private AddressInfo oldAddress;
  private AddressInfo newAddress;
  
  /**
    * Default empty constructor
    */
  public AddressChangeInfo() {
  }

  /**
    * Constructs an AddressChangeInfo object with the given address change interaction information.
    *
    * @param oldAddressLine
    * @param oldCity
    * @param oldProvince
    * @param oldCountry
    * @param oldPostal
    *
    * @param newAddressLine
    * @param newCity
    * @param newProvince
    * @param newCountry
    * @param newPostal
    */
  public AddressChangeInfo(
    String oldAddressLine,
    String oldCity,
    String oldProvince,
    String oldCountry,
    String oldPostal,
    String newAddressLine,
    String newCity,
    String newProvince,
    String newCountry,
    String newPostal
  ) {
    setOldAddressLine(oldAddressLine);
    setOldCity(oldCity);
    setOldProvince(oldProvince);
    setOldCountry(oldCountry);
    setOldPostal(oldPostal);

    setNewAddressLine(newAddressLine);
    setNewCity(newCity);
    setNewProvince(newProvince);
    setNewCountry(newCountry);
    setNewPostal(newPostal);
  }

  /**
    * Copies the information from the given dao to this object.
    *
    * @param dao -- The data source
    */
//  public void copyFrom(TmiAddressTransactionDAO dao) {
//    setOldAddressLine(dao.getOldAddressLine());
//    setOldCity(dao.getOldCity());
//    setOldProvince(dao.getOldProvince());
//    setOldCountry(dao.getOldCountry());
//    setOldPostal(dao.getOldPostal());
//
//    setNewAddressLine(dao.getNewAddressLine());
//    setNewCity(dao.getNewCity());
//    setNewProvince(dao.getNewProvince());
//    setNewCountry(dao.getNewCountry());
//    setNewPostal(dao.getNewPostal());
//  }

  /**
    * Returns the interaction detail type.
    *
    * @return String -- Always InteractionManager.TYPE_BILL_PAYMENT
    */
  public String getType() {
    return InteractionManager.TYPE_BILL_PAYMENT;
  }

  public String getOldAddressLine() {
    return oldAddressLine;
  }

  public void setOldAddressLine(String oldAddressLine) {
    this.oldAddressLine = oldAddressLine;
  }

  public String getOldCity() {
    return oldCity;
  }

  public void setOldCity(String oldCity) {
    this.oldCity = oldCity;
  }


  public String getOldProvince() {
    return oldProvince;
  }

  public void setOldProvince(String oldProvince) {
    this.oldProvince = oldProvince;
  }

  public String getOldCountry() {
    return oldCountry;
  }

  public void setOldCountry(String oldCountry) {
    this.oldCountry = oldCountry;
  }

  public String getOldPostal() {
    return oldPostal;
  }

  public void setOldPostal(String oldPostal) {
    this.oldPostal = oldPostal;
  }

  public String getNewAddressLine() {
    return newAddressLine;
  }

  public void setNewAddressLine(String newAddressLine) {
    this.newAddressLine = newAddressLine;
  }

  public String getNewCity() {
    return newCity;
  }

  public void setNewCity(String newCity) {
    this.newCity = newCity;
  }

  public String getNewProvince() {
    return newProvince;
  }

  public void setNewProvince(String newProvince) {
    this.newProvince = newProvince;
  }

  public String getNewCountry() {
    return newCountry;
  }

  public void setNewCountry(String newCountry) {
    this.newCountry = newCountry;
  }

  public String getNewPostal() {
    return newPostal;
  }

  public void setNewPostal(String newPostal) {
    this.newPostal = newPostal;
  }

	public String getMessageType() {
		return MESSAGE_TYPE_ADDRESS_CHANGE;
	}
	
	public AddressInfo getOldAddress() {
		return oldAddress;
	}
	
	public void setOldAddress(AddressInfo oldAddress) {
		this.oldAddress = oldAddress;
	}
	
	public AddressInfo getNewAddress() {
		return newAddress;
	}
	
	public void setNewAddress(AddressInfo newAddress) {
		this.newAddress = newAddress;
	}
}