/**
 * Title:        MemoInfo<p>
 * Description:  The MemoInfo holds all attributes for a memo.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

package com.telus.eas.framework.info;

import java.math.BigDecimal;
import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Memo;


public class MemoInfo extends Info implements Memo {

  static final long serialVersionUID = 1L;

  private int banId               = 0;
  private String memoType         = "";
  private String subscriberId = "";
  private String productType      = "";
  private String text             = "";
  private Date date               = new Date();
  private String systemText       = "";
  private Date modifyDate;
  private int operatorId;
  private double memoId;

  public MemoInfo() {
  }

  public MemoInfo(int pBanId, String pMemoType, String pSubscriberId, String pProductType,
                  String pText, Date pDate) {
    setBanId(pBanId);
    setMemoType(pMemoType);
    setSubscriberId(pSubscriberId);
    setProductType(pProductType);
    setText(pText);
    setDate(pDate);
  }

  public MemoInfo(int pBanId, String pMemoType, String pSubscriberId, String pProductType, String pText) {
    setBanId(pBanId);
    setMemoType(pMemoType);
    setSubscriberId(pSubscriberId);
    setProductType(pProductType);
    setText(pText);
  }

  public int getBanId() {
    return banId;
  }

  public void setBanId(int newBanId) {
    banId = newBanId;
  }

  public String getMemoType() {
    return memoType;
  }

  public void setMemoType(String newMemoType) {
    memoType = newMemoType;
  }

  public String getSubscriberId() {
    return subscriberId;
  }

  public void setSubscriberId(String newSubscriberId) {
    subscriberId = newSubscriberId;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String newProductType) {
    productType = newProductType;
  }

  public String getText() {
    return text;
  }

  public void setText(String newText) {
    text = newText;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date newDate) {
    date = newDate;
  }

  public boolean isSubscriberLevel() {
    if (subscriberId == null || productType == null)  return false;
    if (subscriberId.trim().equals("") || productType.trim().equals("")) return false;
    return true;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("MemoInfo:{\n");
    s.append("    banId=[").append(banId).append("]\n");
    s.append("    memoType=[").append(memoType).append("]\n");
    s.append("    subscriberId=[").append(subscriberId).append("]\n");
    s.append("    productType=[").append(productType).append("]\n");
    s.append("    text=[").append(text).append("]\n");
    s.append("    systemText=[").append(systemText).append("]\n");
    s.append("    date=[").append(date).append("]\n");
    s.append("    modifyDate=[").append(modifyDate).append("]\n");
    s.append("    operatorId=[").append(operatorId).append("]\n");
    s.append("    memoId=[").append(new BigDecimal(memoId).toString()).append("]\n");
    s.append("}");

    return s.toString();
  }

  public void setSystemText(String newSystemText) {
    systemText = newSystemText;
  }

  public String getSystemText() {
    return systemText;
  }


  public void create() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void create(boolean async) throws TelusAPIException {
	    throw new UnsupportedOperationException("Method not implemented here");
  }
  public void save() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void setModifyDate(Date modifyDate) {
    this.modifyDate = modifyDate;
  }
  public Date getModifyDate() {
    return modifyDate;
  }

  public int getOperatorId() {
      return operatorId;
  }

  public void setOperatorId(int operatorId) {
      this.operatorId = operatorId;
  }

/**
 * @return Returns the memoId.
 */
public double getMemoId() {
	return memoId;
}
/**
 * @param memoId The memoId to set.
 */
public void setMemoId(double memoId) {
	this.memoId = memoId;
}
}

