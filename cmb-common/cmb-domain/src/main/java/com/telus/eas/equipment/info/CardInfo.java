/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.equipment.info;

import com.telus.api.*;
import com.telus.api.account.*;
import com.telus.api.equipment.*;
import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class CardInfo extends Info implements Card, FeatureCard, GameCard, MinuteCard, AirtimeCard {

  static final long serialVersionUID = 1L;

  private final static Map GAME_CARDS   = new HashMap();
  private final static Map MINUTE_CARDS = new HashMap();

  static {
    //------------------------------
    // Game card product types.
    //------------------------------
    GAME_CARDS.put(PRODUCT_TYPE_100_GAMES, new Integer(100));

    //------------------------------
    // Minute card product types.
    //------------------------------
    MINUTE_CARDS.put(PRODUCT_TYPE_100_MINUTES, new Integer(100));
  }

  private static int getIntValue(Map map, String key) {
    Integer i = (Integer)map.get(key);
    if(i == null) {
      return 0;
    }
    return i.intValue();
  }


  private String type;
  private String productTypeId;
  private int status;
  private Date statusDate;
  private String serialNumber;
  private String PIN = "";
  private String description;
  private String descriptionFrench;
  private Date availableFromDate;
  private Date availableToDate;
  private double amount;
  private String phoneNumber;
  private int banId;
  private String adjustmentCode;
  private String subscriberEquipmentSerialNumber;
  private boolean autoRenew;
  private int totalGames;
  private int totalMinutes;
  private boolean featureCard;
  private boolean gameCard;
  private boolean minuteCard;
  private boolean airtimeCard;


  public CardInfo() {
    reset();
  }

  private void reset() {
    featureCard = type != null && TYPE_FEATURE.equals(type);
    airtimeCard = type != null && TYPE_AIRTIME.equals(type);
    //minuteCard  = MINUTE_CARDS.containsKey(productTypeId);
    //gameCard    = GAME_CARDS.containsKey(productTypeId);

    totalMinutes = getIntValue(MINUTE_CARDS, productTypeId);
    totalGames   = getIntValue(GAME_CARDS,   productTypeId);

    minuteCard  = totalMinutes > 0;
    gameCard    = totalGames > 0;
  }

  public String getType() {
    return type;
  }

  public String getProductTypeId() {
    return productTypeId;
  }

  public int getStatus() {
    return status;
  }

  public Date getStatusDate() {
    return statusDate;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public String getPIN() {
    return PIN;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public Date getAvailableFromDate() {
    return availableFromDate;
  }

  public Date getAvailableToDate() {
    return availableToDate;
  }

  public double getAmount() {
    return amount;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public int getBanId() {
    return banId;
  }

  public String getAdjustmentCode() {
    return adjustmentCode;
  }

  public Service[] getServices(Subscriber subscriber) throws TelusAPIException {
    throw new UnsupportedOperationException("method not implemented here");
  }

  public boolean isFeatureCard() {
    return featureCard;
  }

  public boolean isMinuteCard() {
    return minuteCard;
  }

  public boolean isGameCard() {
    return gameCard;
  }

  public boolean isAirtimeCard() {
    return airtimeCard;
  }

  public void setType(String type) {
    this.type = type;
    reset();
  }

  public void setProductTypeId(String productTypeId) {
    this.productTypeId = productTypeId;
    reset();
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setStatusDate(Date statusDate) {
    this.statusDate = statusDate;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public void setPIN(String PIN) {
    this.PIN = PIN;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }

  public void setAvailableFromDate(Date availableFromDate) {
    this.availableFromDate = availableFromDate;
  }

  public void setAvailableToDate(Date availableToDate) {
    this.availableToDate = availableToDate;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setBanId(int banId) {
    this.banId = banId;
  }

  public void setAdjustmentCode(String adjustmentCode) {
    this.adjustmentCode = adjustmentCode;
  }

  public void setCredited(Subscriber subscriber, boolean autoRenew) throws TelusAPIException{
    throw new UnsupportedOperationException("method not implemented here");
  }

  public void setStolen() throws TelusAPIException{
    throw new UnsupportedOperationException("method not implemented here");
  }

  public String getSubscriberEquipmentSerialNumber(){
    return subscriberEquipmentSerialNumber;
  }

  public void setSubscriberEquipmentSerialNumber(String subscriberEquipmentSerialNumber){
    this.subscriberEquipmentSerialNumber = subscriberEquipmentSerialNumber;
  }

  public boolean getAutoRenew(){
    return autoRenew;
  }

  public void setAutoRenew(boolean autoRenew){
    this.autoRenew = autoRenew;
  }

  public int getTotalGames(){
    return totalGames;
  }

  public int getTotalMinutes(){
    return totalMinutes;
  }

  public ProrationMinutes[] getProrationMinutes(Account account, int months) throws TelusAPIException{
    throw new UnsupportedOperationException("method not implemented here");
  }

  public boolean isLive (){
	  if (status == STATUS_LIVE)
		  return true;
	  else
		  return false;
	  
  }
  
  public boolean isValidAmountForApp(){
	  throw new UnsupportedOperationException("method not implemented here");
	  
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("CardInfo:[\n");
        s.append("    type=[").append(type).append("]\n");
        s.append("    productTypeId=[").append(productTypeId).append("]\n");
        s.append("    status=[").append(status).append("]\n");
        s.append("    statusDate=[").append(statusDate).append("]\n");
        s.append("    serialNumber=[").append(serialNumber).append("]\n");
        s.append("    description=[").append(description).append("]\n");
        s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
        s.append("    availableFromDate=[").append(availableFromDate).append("]\n");
        s.append("    availableToDate=[").append(availableToDate).append("]\n");
        s.append("    amount=[").append(amount).append("]\n");
        s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
        s.append("    banId=[").append(banId).append("]\n");
        s.append("    adjustmentCode=[").append(adjustmentCode).append("]\n");
        s.append("    subscriberEquipmentSerialNumber=[").append(subscriberEquipmentSerialNumber).append("]\n");
        s.append("    autoRenew=[").append(autoRenew).append("]\n");
        s.append("    totalGames=[").append(totalGames).append("]\n");
        s.append("    totalMinutes=[").append(totalMinutes).append("]\n");
        s.append("]");

        return s.toString();
    }

}




