package com.telus.eas.account.info;

import com.telus.api.TelusAPIException;
import com.telus.api.account.FeeWaiver;
import java.util.Date;
import com.telus.eas.framework.info.Info;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FeeWaiverInfo extends Info implements FeeWaiver {

	 static final long serialVersionUID = 1L;

  public static final byte NO_CHANGE    = (byte)'N';
  public static final byte INSERT       = (byte)'I';
  public static final byte DELETE       = (byte)'D';
  public static final byte UPDATE       = (byte)'U';

  private String reasonCode;
  private String typeCode;
  private Date effectiveDate;
  private Date expiryDate;
  private byte mode = NO_CHANGE;
  private int banId;

  public FeeWaiverInfo() {
  }

  public byte getMode() {
    return mode;
  }

  public void setMode(byte mode) {
    this.mode = mode;
  }

  /**
   * add
   * @throws TelusAPIException
   */
  public void add() throws TelusAPIException {
    throw new java.lang.UnsupportedOperationException("Method not implemented here");
  }

  /**
   * delete
   * @throws TelusAPIException
   */
  public void delete() throws TelusAPIException {
    throw new java.lang.UnsupportedOperationException("Method not implemented here");
  }

  /**
   * update
   * @throws TelusAPIException
   */
  public void update() throws TelusAPIException {
    throw new java.lang.UnsupportedOperationException("Method not implemented here");
  }

  /**
   * Returns String reasonCode.
   * @return String
   */
  public String getReasonCode() {
    return reasonCode;
  }

  /**
   * set ReasonCode
   * @param reasonCode String
   */
  public void setReasonCode(String reasonCode) {
    this.reasonCode = reasonCode;
  }

  /**
   * Return String typeCode.
   * @return String
   */
  public String getTypeCode(){
    return typeCode;
  }

  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
  }

  /**
   * Return EffectiveDate.
   * @return Date
   */
  public Date getEffectiveDate() {
    return effectiveDate;
  }

  /**
   * Return ExpiryDate.
   * @return Date
   */
  public Date getExpiryDate() {
    return expiryDate;
  }

  /**
   * set EffectiveDate
   * @param effectiveDate Date
   */
  public void setEffectiveDate(Date effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  /**
   * set ExpiryDate
   * @param expiryDate Date
   */
  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public int getBanId() {
    return banId;
  }

  public void setBanId(int banId) {
    this.banId = banId;
  }
}
