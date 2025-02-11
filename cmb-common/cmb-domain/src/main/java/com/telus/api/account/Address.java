/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.TelusAPIException;


/**
 * <CODE>Address</CODE>
 *
 */
public interface Address {

  public static final String ADDRESS_TYPE_CITY = "C";
  public static final String ADDRESS_TYPE_RURAL = "D";
  public static final String ADDRESS_TYPE_FOREIGN = "F";

  public static final String RURAL_TYPE_RURAL_ROUTE = "R";
  public static final String RURAL_TYPE_PO_BOX = "P";
  public static final String RURAL_TYPE_GENERAL_DELIVERY = "G";
  public static final String RURAL_TYPE_STATION_MAIN = "M";
  public static final String RURAL_TYPE_TOWNSHIP = "S";

  /**
   * Authenticates this address with the CODE 1 service.  This method returns silently
   * if the address is ok, otherwise it throws an InvalidAddressException.
   *
   * <P>This method may involve a remote method call.
   *
   */
  void validate() throws TelusAPIException, InvalidAddressException;

  /**
   * Sets all fields to <CODE>null</CODE>.
   *
   */
  void clear();

  /**
   * Nullifies fields that are not appropriate for the given address type.
   *
   */
  void normalize();

  String getAttention();

  void setAttention(String newAttention);

//  boolean isGeneralDelivery();
//
//  void setGeneralDelivery(boolean newGeneralDelivery);

  String getStreetNumber();

  void setStreetNumber(String streetNumber);

  String getStreetNumberSuffix();

  void setStreetNumberSuffix(String streetNumberSuffix);

  String getStreetName();

  void setStreetName(String streetName);

  String getStreetType();

  void setStreetType(String newStreetType);

  String getStreetDirection();

  void setStreetDirection(String newStreetDirection);

  String getUnitType();

  void setUnitType(String unitType);

  String getUnit();

  void setUnit(String unit);

  String getRuralType();

  void setRuralType(String ruralType);

  String getPoBox();

  void setPoBox(String poBox);

  String getRr();

  void setRr(String rr);

  String getRuralNumber();

  void setRuralNumber(String ruralNumber);

  String getRuralLocation();

  void setRuralLocation(String ruralLocation);

  String getRuralDeliveryType();

  void setRuralDeliveryType(String newRuralDeliveryType);

  String getRuralQualifier();

  void setRuralQualifier(String newRuralQualifier);

  String getRuralSite();

  void setRuralSite(String newRuralSite);

  String getRuralCompartment();

  void setRuralCompartment(String newRuralCompartment);

  String getRuralGroup();

  void setRuralGroup(String newRuralGroup);

  String getCity();

  void setCity(String city);

  String getProvince();

  void setProvince(String province);

  String getPostalCode();

  void setPostalCode(String postalCode);

  String getCountry();

  void setCountry(String country);

  String getAddressType();

  void setAddressType(String newAddressType);

  String getPrimaryLine();

  void setPrimaryLine(String newPrimaryLine);

  String getSecondaryLine();

  void setSecondaryLine(String newSecondaryLine);


  void copyFrom(Address o);

  boolean shallowEquals(Address o);

  boolean equals(Address o);

  boolean equals(Object o);

  boolean isEmpty();


  /**
   * Returns the address lines without the attention line.
   */
  String[] getFullAddress();

  /**
   * Returns the address lines possibly including the attention line.
   */
  String[] getFullAddress(boolean includeAttentionLine);

  void translateAddress(EnterpriseAddress enterpriseAddress);
  
  EnterpriseAddress newEnterpriseAddress();
}