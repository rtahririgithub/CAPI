/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import java.util.Date;

import com.telus.api.interaction.EquipmentChange;
import com.telus.api.interaction.InteractionManager;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

/**
  * Value (info) object for the equipment change interaction detail information.
  *
  */
public class EquipmentChangeInfo extends ConfigurationManagerInfo implements EquipmentChange {

   static final long serialVersionUID = 1L;

  private long swapRequestId = 0;
  private String chnlOrgCode = "";
  private String userCode = "";
  private String requestorId = "";
  private String swapType = "";
  private int ban = -1;
  private String subscriberNumber = "";
  private String repairId = "";
  private String oldSmNumber = "";
  private String oldTechnologyType = "";
  private String oldProductCode = "";
  private String oldProductStatusCode = "";
  private String oldProductClassCode = "";
  private String oldProductGpTypeCode = "";
  private Date oldWarrantyDate = null;
  private Date oldDoaDate = null;
  private Date oldInitialMfgDate = null;
  private String oldAssocMuleImei = "";
  private Date oldPendingDate = null;
  private String oldPendingProductGpTypeCode = "";
  private String newSmNumber = "";
  private String newTechnologyType = "";
  private String newProductCode = "";
  private String newProductStatusCode = "";
  private String newProductClassCode = "";
  private String newProductGpTypeCode = "";
  private Date newWarrantyDate = null;
  private String leaseFlag = "";
  private String sapOrderNumber = "";
  private Date createDate = null;
  private String createUser = "";
  private Date modifyDate = null;
  private String modifyUser = "";

  private SubscriberInfo subscriberInfo;
  private EquipmentInfo oldEquipmentInfo;
  private EquipmentInfo newEquipmentInfo;
  private String dealerCode;
  private String salesRepCode;
  private EquipmentInfo associatedMuleEquipmentInfo;
  private String applicationName;
  /**
    * Default empty constructor
    */
  public EquipmentChangeInfo() {
  }

  // The other constructor is not implemented because of the volume of attributres.

  /**
    * Copies the information from the given dao to this object.
    *
    * DO NOT USE. Moved to TmiEquipmentChangeDAO
    * @param dao -- The data source
    */
//  public void copyFrom(TmiEquipmentChangeDAO dao) {
//    setSwapRequestId(dao.getSwapRequestId());
//    setChnlOrgCd(dao.getChnlOrgCd());
//    setUserCode(dao.getUserCode());
//    setRequestorId(dao.getRequestorId());
//    setSwapType(dao.getSwapType());
//    setBan(dao.getBan());
//    setSubscriberNo(dao.getSubscriberNo());
//    setRepairId(dao.getRepairId());
//    setOldSmNum(dao.getOldSmNum());
//    setOldTechnologyType(dao.getOldTechnologyType());
//    setOldProductCd(dao.getOldProductCd());
//    setOldProductStatusCd(dao.getOldProductStatusCd());
//    setOldProductClassCd(dao.getOldProductClassCd());
//    setOldProductGpTypeCd(dao.getOldProductGpTypeCd());
//    setOldWarrantyDate(dao.getOldWarrantyDate());
//    setOldDoaDate(dao.getOldDoaDate());
//    setOldInitialMfgDate(dao.getOldInitialMfgDate());
//    setOldAssocMuleImei(dao.getOldAssocMuleImei());
//    setOldPendDate(dao.getOldPendDate());
//    setOldPendProdGpTypeCd(dao.getOldPendProdGpTypeCd());
//    setNewSmNum(dao.getNewSmNum());
//    setNewTechnologyType(dao.getNewTechnologyType());
//    setNewProductCd(dao.getNewProductCd());
//    setNewProductStatusCd(dao.getNewProductStatusCd());
//    setNewProductClassCd(dao.getNewProductClassCd());
//    setNewProductGpTypeCd(dao.getNewProductGpTypeCd());
//    setNewWarrantyDate(dao.getNewWarrantyDate());
//    setLeaseFlg(dao.getLeaseFlg());
//    setSapOrderNum(dao.getSapOrderNum());
//    setCreateDate(dao.getCreateDate());
//    setCreateUser(dao.getCreateUser());
//    setModifyDate(dao.getModifyDate());
//    setModifyUser(dao.getModifyUser());
//  }


  /**
    * Returns the type of interaction detail this object represents.
    *
    * @return String -- Always InteractionManager.TYPE_EQUIPMENT_CHANGE
    */
  public String getType() {
    return InteractionManager.TYPE_EQUIPMENT_CHANGE;
  }

  public long getSwapRequestId() {
    return swapRequestId;
  }

  public void setSwapRequestId(long swapRequestId) {
    this.swapRequestId = swapRequestId;
  }

  public void setSwapRequestId(Long swapRequestId) {
    if(swapRequestId == null)
      setSwapRequestId(0L);
    else
      setSwapRequestId(swapRequestId.longValue());
  }

  public String getChnlOrgCd() {
    return chnlOrgCode;
  }

  public void setChnlOrgCd(String chnlOrgCode) {
    this.chnlOrgCode = chnlOrgCode;
  }

  public String getUserCode() {
    return userCode;
  }

  public void setUserCode(String userCode) {
    this.userCode = userCode;
  }

  public String getRequestorId() {
    return requestorId;
  }

  public void setRequestorId(String requestorId) {
    this.requestorId = requestorId;
  }

  public String getSwapType() {
    return swapType;
  }

  public void setSwapType(String swapType) {
    this.swapType = swapType;
  }

  public int getBan() {
    return ban;
  }

  public void setBan(int ban) {
    this.ban = ban;
  }

  public void setBan(Integer ban) {
    if(ban == null)
      setBan(-1);
    else
      setBan(ban.intValue());
  }

  public String getSubscriberNo() {
    return subscriberNumber;
  }

  public void setSubscriberNo(String subscriberNumber) {
    this.subscriberNumber = subscriberNumber;
  }

  public String getRepairId() {
    return repairId;
  }

  public void setRepairId(String repairId) {
    this.repairId = repairId;
  }

  public String getOldSmNum() {
    return oldSmNumber;
  }

  public void setOldSmNum(String oldSmNumber) {
    this.oldSmNumber = oldSmNumber;
  }

  public String getOldTechnologyType() {
    return oldTechnologyType;
  }

  public void setOldTechnologyType(String oldTechnologyType) {
    this.oldTechnologyType = oldTechnologyType;
  }

  public String getOldProductCd() {
    return oldProductCode;
  }

  public void setOldProductCd(String oldProductCode) {
    this.oldProductCode = oldProductCode;
  }

  public String getOldProductStatusCd() {
    return oldProductStatusCode;
  }

  public void setOldProductStatusCd(String oldProductStatusCode) {
    this.oldProductStatusCode = oldProductStatusCode;
  }

  public String getOldProductClassCd() {
    return oldProductClassCode;
  }

  public void setOldProductClassCd(String oldProductClassCode) {
    this.oldProductClassCode = oldProductClassCode;
  }

  public String getOldProductGpTypeCd() {
    return oldProductGpTypeCode;
  }

  public void setOldProductGpTypeCd(String oldProductGpTypeCode) {
    this.oldProductGpTypeCode = oldProductGpTypeCode;
  }

  public Date getOldWarrantyDate() {
    return oldWarrantyDate;
  }

  public void setOldWarrantyDate(Date oldWarrantyDate) {
    this.oldWarrantyDate = oldWarrantyDate;
  }

  public Date getOldDoaDate() {
    return oldDoaDate;
  }

  public void setOldDoaDate(Date oldDoaDate) {
    this.oldDoaDate = oldDoaDate;
  }

  public Date getOldInitialMfgDate() {
    return oldInitialMfgDate;
  }

  public void setOldInitialMfgDate(Date oldInitialMfgDate) {
    this.oldInitialMfgDate = oldInitialMfgDate;
  }

  public String getOldAssocMuleImei() {
    return oldAssocMuleImei;
  }

  public void setOldAssocMuleImei(String oldAssocMuleImei) {
    this.oldAssocMuleImei = oldAssocMuleImei;
  }

  public Date getOldPendDate() {
    return oldPendingDate;
  }

  public void setOldPendDate(Date oldPendingDate) {
    this.oldPendingDate = oldPendingDate;
  }

  public String getOldPendProdGpTypeCd() {
    return oldPendingProductGpTypeCode;
  }

  public void setOldPendProdGpTypeCd(String oldPendingProductGpTypeCode) {
    this.oldPendingProductGpTypeCode = oldPendingProductGpTypeCode;
  }

  public String getNewSmNum() {
    return newSmNumber;
  }

  public void setNewSmNum(String newSmNumber) {
    this.newSmNumber = newSmNumber;
  }

  public String getNewTechnologyType() {
    return newTechnologyType;
  }

  public void setNewTechnologyType(String newTechnologyType) {
    this.newTechnologyType = newTechnologyType;
  }

  public String getNewProductCd() {
    return newProductCode;
  }

  public void setNewProductCd(String newProductCode) {
    this.newProductCode = newProductCode;
  }

  public String getNewProductStatusCd() {
    return newProductStatusCode;
  }

  public void setNewProductStatusCd(String newProductStatusCode) {
    this.newProductStatusCode = newProductStatusCode;
  }

  public String getNewProductClassCd() {
    return newProductClassCode;
  }

  public void setNewProductClassCd(String newProductClassCode) {
    this.newProductClassCode = newProductClassCode;
  }

  public String getNewProductGpTypeCd() {
    return newProductGpTypeCode;
  }

  public void setNewProductGpTypeCd(String newProductGpTypeCode) {
    this.newProductGpTypeCode = newProductGpTypeCode;
  }

  public Date getNewWarrantyDate() {
    return newWarrantyDate;
  }

  public void setNewWarrantyDate(Date newWarrantyDate) {
    this.newWarrantyDate = newWarrantyDate;
  }

  public String getLeaseFlg() {
    return leaseFlag;
  }

  public void setLeaseFlg(String leaseFlag) {
    this.leaseFlag = leaseFlag;
  }

  public String getSapOrderNum() {
    return sapOrderNumber;
  }

  public void setSapOrderNum(String sapOrderNumber) {
    this.sapOrderNumber = sapOrderNumber;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }


  public Date getModifyDate() {
    return modifyDate;
  }

  public void setModifyDate(Date modifyDate) {
    this.modifyDate = modifyDate;
  }

  public String getModifyUser() {
    return modifyUser;
  }

  public void setModifyUser(String modifyUser) {
    this.modifyUser = modifyUser;
  }

	public String getMessageType() {
		return MESSAGE_TYPE_EQUIPMENT_CHANGE;
	}
	
	public SubscriberInfo getSubscriberInfo() {
		return subscriberInfo;
	}
	
	public void setSubscriberInfo(SubscriberInfo subscriberInfo) {
		this.subscriberInfo = subscriberInfo;
	}
	
	public EquipmentInfo getOldEquipmentInfo() {
		return oldEquipmentInfo;
	}
	
	public void setOldEquipmentInfo(EquipmentInfo oldEquipmentInfo) {
		this.oldEquipmentInfo = oldEquipmentInfo;
	}
	
	public EquipmentInfo getNewEquipmentInfo() {
		return newEquipmentInfo;
	}
	
	public void setNewEquipmentInfo(EquipmentInfo newEquipmentInfo) {
		this.newEquipmentInfo = newEquipmentInfo;
	}
	
	public String getDealerCode() {
		return dealerCode;
	}
	
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	
	public String getSalesRepCode() {
		return salesRepCode;
	}
	
	public void setSalesRepCode(String salesRepCode) {
		this.salesRepCode = salesRepCode;
	}
	
	public EquipmentInfo getAssociatedMuleEquipmentInfo() {
		return associatedMuleEquipmentInfo;
	}
	
	public void setAssociatedMuleEquipmentInfo(
			EquipmentInfo associatedMuleEquipmentInfo) {
		this.associatedMuleEquipmentInfo = associatedMuleEquipmentInfo;
	}
	
	public String getApplicationName() {
		return applicationName;
	}
	
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}