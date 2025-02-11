package com.telus.eas.subscriber.info;

import com.telus.eas.framework.info.Info;
import java.util.Date;

/**
 * Title:        Telus - Amdocs Domain Beans
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Telus Mobility
 * @author Michael Lapish
 * @version 1.0
 */

public class SwapRequestInfo extends Info {

  static final long serialVersionUID = 1L;

  private long swapRequestID = 0;
  private String chnlOrgCD = "";
  private String userCode = "";
  private String requestorID = "";
  private String swapType = "";
  private long ban = 0;
  private String subscriberNo = "";
  private String repairID = "";
  private String oldSerialNumber = "";
  private String oldTechnologyType = "";
  private String oldProductCD = "";
  private String oldProductStatusCD = "";
  private String oldProductClassificationCD = "";
  private String oldProductGroupTypeCD = "";
  private Date oldWarrantyDate;
  private Date oldDoaDate;
  private Date oldInitialMfgDate;
  private Date oldLatestPendDate;
  private String oldLatestPendProductGroupTypeCD = "";
  private String newSerialNumber = "";
  private String newTechnologyType = "";
  private String newProductCD = "";
  private String newProductStatusCD = "";
  private String newProductClassificationCD = "";
  private String newProductGroupTypeCD = "";
  private Date newWarrantyDate;
  private String associatedMuleSerialNumber = "";
  private boolean leaseFlg = false;
  private String SAPOrderNumber = "";
  private String subscriberName = "";
  private String customerCode = "";
  private Date createDate;
  private String createUser;
  private Date modifyDate;
  private String modifyUser;

  public SwapRequestInfo() {
  }

  public SwapRequestInfo(long swapRequestID,
          String chnlOrgCD,
          String userCode,
          String requestorID,
          String swapType,
          long ban,
          String subscriberNo,
          String repairID,
          String oldSerialNumber,
          String oldTechnologyType,
          String oldProductCD,
          String oldProductStatusCD,
          String oldProductClassificationCD,
          String oldProductGroupTypeCD,
          Date oldWarrantyDate,
          Date oldDoaDate,
          Date oldInitialMfgDate,
          Date oldLatestPendDate,
          String oldLatestPendProductGroupTypeCD,
          String newSerialNumber,
          String newTechnologyType,
          String newProductCD,
          String newProductStatusCD,
          String newProductClassificationCD,
          String newProductGroupTypeCD,
          Date newWarrantyDate,
          String associatedMuleSerialNumber,
          boolean leaseFlg,
          String SAPOrderNumber,
          Date createDate,
          String createUser,
          Date modifyDate,
          String modifyUser,
          String subscriberName,
          String customerCode) {

    this.setSwapRequestID(swapRequestID);
    this.setChnlOrgCD(chnlOrgCD);
    this.setUserCode(userCode);
    this.setRequestorID(requestorID);
    this.setSwapType(swapType);
    this.setBan(ban);
    this.setSubscriberNo(subscriberNo);
    this.setRepairID(repairID);
    this.setOldSerialNumber(oldSerialNumber);
    this.setOldProductCD(oldProductCD);
    this.setOldTechnologyType(oldTechnologyType);
    this.setOldProductStatusCD(oldProductStatusCD);
    this.setOldProductClassificationCD(oldProductClassificationCD);
    this.setOldProductGroupTypeCD(oldProductGroupTypeCD);
    this.setOldWarrantyDate(oldWarrantyDate);
    this.setOldDoaDate(oldDoaDate);
    this.setOldInitialMfgDate(oldInitialMfgDate);
    this.setOldLatestPendDate(oldLatestPendDate);
    this.setOldLatestPendProductGroupTypeCD(oldLatestPendProductGroupTypeCD);
    this.setNewSerialNumber(newSerialNumber);
    this.setNewTechnologyType(newTechnologyType);
    this.setNewProductCD(newProductCD);
    this.setNewProductStatusCD(newProductStatusCD);
    this.setNewProductClassificationCD(newProductClassificationCD);
    this.setNewProductGroupTypeCD(newProductGroupTypeCD);
    this.setNewWarrantyDate(newWarrantyDate);
    this.setAssociatedMuleSerialNumber(associatedMuleSerialNumber);
    this.setLeaseFlg(leaseFlg);
    this.setSAPOrderNumber(SAPOrderNumber);
    this.setCreateDate(createDate);
    this.setCreateUser(createUser);
    this.setModifyDate(modifyDate);
    this.setModifyUser(modifyUser);
    this.setSubscriberName(subscriberName);
    this.setCustomerCode(customerCode);

  }

  // Getter/Setter methods for all attributes
  // ----------------------------------------
  public void setSwapRequestID(long newSwapRequestID) {
    swapRequestID = newSwapRequestID;
  }
  public long getSwapRequestID() {
    return swapRequestID;
  }
  public void setChnlOrgCD(String newChnlOrgCD) {
    chnlOrgCD = newChnlOrgCD;
  }
  public String getChnlOrgCD() {
    return chnlOrgCD;
  }
  public void setUserCode(String newUserCode) {
    userCode = newUserCode;
  }
  public String getUserCode() {
    return userCode;
  }
  public void setRequestorID(String newRequestorID) {
    requestorID = newRequestorID;
  }
  public String getRequestorID() {
    return requestorID;
  }
  public void setSwapType(String newSwapType) {
    swapType = newSwapType;
  }
  public String getSwapType() {
    return swapType;
  }
  public void setBan(long newBan) {
    ban = newBan;
  }
  public long getBan() {
    return ban;
  }
  public void setSubscriberNo(String newSubscriberNo) {
    subscriberNo = newSubscriberNo;
  }
  public String getSubscriberNo() {
    return subscriberNo;
  }
  public void setRepairID(String newRepairID) {
    repairID = newRepairID;
  }
  public String getRepairID() {
    return repairID;
  }
  public void setOldSerialNumber(String newOldSerialNumber) {
    oldSerialNumber = newOldSerialNumber;
  }
  public String getOldSerialNumber() {
    return oldSerialNumber;
  }
  public void setOldProductCD(String newOldProductCD) {
    oldProductCD = newOldProductCD;
  }
  public String getOldProductCD() {
    return oldProductCD;
  }
  public void setOldProductStatusCD(String newOldProductStatusCD) {
    oldProductStatusCD = newOldProductStatusCD;
  }
  public String getOldProductStatusCD() {
    return oldProductStatusCD;
  }
  public void setOldProductClassificationCD(String newOldProductClassificationCD) {
    oldProductClassificationCD = newOldProductClassificationCD;
  }
  public String getOldProductClassificationCD() {
    return oldProductClassificationCD;
  }
  public void setOldProductGroupTypeCD(String newOldProductGroupTypeCD) {
    oldProductGroupTypeCD = newOldProductGroupTypeCD;
  }
  public String getOldProductGroupTypeCD() {
    return oldProductGroupTypeCD;
  }
  public void setOldWarrantyDate(Date newOldWarrantyDate) {
    oldWarrantyDate = newOldWarrantyDate;
  }
  public Date getOldWarrantyDate() {
    return oldWarrantyDate;
  }
  public void setOldDoaDate(Date newOldDoaDate) {
    oldDoaDate = newOldDoaDate;
  }
  public Date getOldDoaDate() {
    return oldDoaDate;
  }
  public void setOldInitialMfgDate(Date newOldInitialMfgDate) {
    oldInitialMfgDate = newOldInitialMfgDate;
  }
  public Date getOldInitialMfgDate() {
    return oldInitialMfgDate;
  }
  public void setAssociatedMuleSerialNumber(String newAssociatedMuleSerialNumber) {
    associatedMuleSerialNumber = newAssociatedMuleSerialNumber;
  }
  public String getAssociatedMuleSerialNumber() {
    return associatedMuleSerialNumber;
  }
  public void setOldLatestPendDate(Date newOldLatestPendDate) {
    oldLatestPendDate = newOldLatestPendDate;
  }
  public Date getOldLatestPendDate() {
    return oldLatestPendDate;
  }
  public void setOldLatestPendProductGroupTypeCD(String newOldLatestPendProductGroupTypeCD) {
    oldLatestPendProductGroupTypeCD = newOldLatestPendProductGroupTypeCD;
  }
  public String getOldLatestPendProductGroupTypeCD() {
    return oldLatestPendProductGroupTypeCD;
  }
  public void setNewSerialNumber(String newNewSerialNumber) {
    newSerialNumber = newNewSerialNumber;
  }
  public String getNewSerialNumber() {
    return newSerialNumber;
  }
  public void setNewProductCD(String newNewProductCD) {
    newProductCD = newNewProductCD;
  }
  public String getNewProductCD() {
    return newProductCD;
  }
  public void setNewProductStatusCD(String newNewProductStatusCD) {
    newProductStatusCD = newNewProductStatusCD;
  }
  public String getNewProductStatusCD() {
    return newProductStatusCD;
  }
  public void setNewProductClassificationCD(String newNewProductClassificationCD) {
    newProductClassificationCD = newNewProductClassificationCD;
  }
  public String getNewProductClassificationCD() {
    return newProductClassificationCD;
  }
  public void setNewProductGroupTypeCD(String newNewProductGroupTypeCD) {
    newProductGroupTypeCD = newNewProductGroupTypeCD;
  }
  public String getNewProductGroupTypeCD() {
    return newProductGroupTypeCD;
  }
  public void setNewWarrantyDate(Date newNewWarrantyDate) {
    newWarrantyDate = newNewWarrantyDate;
  }
  public Date getNewWarrantyDate() {
    return newWarrantyDate;
  }
  public void setLeaseFlg(boolean newLeaseFlg) {
    leaseFlg = newLeaseFlg;
  }
  public boolean isLeaseFlg() {
    return leaseFlg;
  }
  public void setSAPOrderNumber(String newSAPOrderNumber) {
    SAPOrderNumber = newSAPOrderNumber;
  }
  public String getSAPOrderNumber() {
    return SAPOrderNumber;
  }
  public void setSubscriberName(String newSubscriberName) {
    subscriberName = newSubscriberName;
  }
  public String getSubscriberName() {
    return subscriberName;
  }
  public void setCustomerCode(String newCustomerCode) {
    customerCode = newCustomerCode;
  }
  public String getCustomerCode() {
    return customerCode;
  }
  public void setCreateDate(Date newCreateDate) {
    createDate = newCreateDate;
  }
  public Date getCreateDate() {
    return createDate;
  }
  public void setCreateUser(String newCreateUser) {
    createUser = newCreateUser;
  }
  public String getCreateUser() {
    return createUser;
  }
  public void setModifyDate(Date newModifyDate) {
    modifyDate = newModifyDate;
  }
  public Date getModifyDate() {
    return modifyDate;
  }
  public void setModifyUser(String newModifyUser) {
    modifyUser = newModifyUser;
  }
  public String getModifyUser() {
    return modifyUser;
  }
  public void setOldTechnologyType(String newOldTechnologyType) {
    oldTechnologyType = newOldTechnologyType;
  }
  public String getOldTechnologyType() {
    return oldTechnologyType;
  }
  public void setNewTechnologyType(String newNewTechnologyType) {
    newTechnologyType = newNewTechnologyType;
  }
  public String getNewTechnologyType() {
    return newTechnologyType;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("SwapRequestInfo:[\n");
        s.append("    swapRequestID=[").append(swapRequestID).append("]\n");
        s.append("    chnlOrgCD=[").append(chnlOrgCD).append("]\n");
        s.append("    userCode=[").append(userCode).append("]\n");
        s.append("    requestorID=[").append(requestorID).append("]\n");
        s.append("    swapType=[").append(swapType).append("]\n");
        s.append("    ban=[").append(ban).append("]\n");
        s.append("    subscriberNo=[").append(subscriberNo).append("]\n");
        s.append("    repairID=[").append(repairID).append("]\n");
        s.append("    oldSerialNumber=[").append(oldSerialNumber).append("]\n");
        s.append("    oldTechnologyType=[").append(oldTechnologyType).append("]\n");
        s.append("    oldProductCD=[").append(oldProductCD).append("]\n");
        s.append("    oldProductStatusCD=[").append(oldProductStatusCD).append("]\n");
        s.append("    oldProductClassificationCD=[").append(oldProductClassificationCD).append("]\n");
        s.append("    oldProductGroupTypeCD=[").append(oldProductGroupTypeCD).append("]\n");
        s.append("    oldWarrantyDate=[").append(oldWarrantyDate).append("]\n");
        s.append("    oldDoaDate=[").append(oldDoaDate).append("]\n");
        s.append("    oldInitialMfgDate=[").append(oldInitialMfgDate).append("]\n");
        s.append("    oldLatestPendDate=[").append(oldLatestPendDate).append("]\n");
        s.append("    oldLatestPendProductGroupTypeCD=[").append(oldLatestPendProductGroupTypeCD).append("]\n");
        s.append("    newSerialNumber=[").append(newSerialNumber).append("]\n");
        s.append("    newTechnologyType=[").append(newTechnologyType).append("]\n");
        s.append("    newProductCD=[").append(newProductCD).append("]\n");
        s.append("    newProductStatusCD=[").append(newProductStatusCD).append("]\n");
        s.append("    newProductClassificationCD=[").append(newProductClassificationCD).append("]\n");
        s.append("    newProductGroupTypeCD=[").append(newProductGroupTypeCD).append("]\n");
        s.append("    newWarrantyDate=[").append(newWarrantyDate).append("]\n");
        s.append("    associatedMuleSerialNumber=[").append(associatedMuleSerialNumber).append("]\n");
        s.append("    leaseFlg=[").append(leaseFlg).append("]\n");
        s.append("    SAPOrderNumber=[").append(SAPOrderNumber).append("]\n");
        s.append("    subscriberName=[").append(subscriberName).append("]\n");
        s.append("    customerCode=[").append(customerCode).append("]\n");
        s.append("    createDate=[").append(createDate).append("]\n");
        s.append("    createUser=[").append(createUser).append("]\n");
        s.append("    modifyDate=[").append(modifyDate).append("]\n");
        s.append("    modifyUser=[").append(modifyUser).append("]\n");
        s.append("]");

        return s.toString();
    }

}