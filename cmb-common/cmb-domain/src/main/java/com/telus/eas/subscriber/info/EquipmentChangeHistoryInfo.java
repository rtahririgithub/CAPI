package com.telus.eas.subscriber.info;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.EquipmentChangeHistory;
import com.telus.api.equipment.Equipment;
import com.telus.eas.framework.info.Info;

/**
 * Title:        EquipmentChangeHistoryInfo<p>
 * Description:  The EquipmentChangeHistoryInfo class holds the attributes for an equipment change transaction.<p>
 * Copyright:    Copyright (c) 2005<p>
 * Company:      Telus Mobility Inc<p>
 * 
 * @author R. Fong
 * @version 1.0
 */
public class EquipmentChangeHistoryInfo extends Info implements EquipmentChangeHistory{

	static final long serialVersionUID = 1L;

	private Date effectiveDate;
	private Date expiryDate;	
	private int esnLevel;	
	private String serialNumber;
  private String productType;
  private String encodingFormat;

  /**
	 * @return Returns the effectiveDate.
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate The effectiveDate to set.
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}	
	
	/**
	 * @return Returns the expiryDate.
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}
	/**
	 * @param expiryDate The expiryDate to set.
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	/**
	 * @return Returns the esnLevel.
	 */
	public int getEsnLevel() {
		return esnLevel;
	}
	/**
	 * @param esnLevel The esnLevel to set.
	 */
	public void setEsnLevel(int esnLevel) {
		this.esnLevel = esnLevel;
	}
	
	/**
	 * @return Returns the serialNumber.
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber The serialNumber to set.
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getEncodingFormat() {
    return encodingFormat;
  }

  public void setEncodingFormat(String encodingFormat) {
    this.encodingFormat = encodingFormat;
  }

  /**
	 * NO-OP method.
	 */
	public Equipment getEquipment() throws TelusAPIException{
	    throw new UnsupportedOperationException("Method not implemented here.");
	}
	
	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("EquipmentChangeHistoryInfo:[\n");
		s.append("    effectiveDate=[").append(effectiveDate).append("]\n");
		s.append("    expiryDate=[").append(expiryDate).append("]\n");
		s.append("    esnLevel=[").append(esnLevel).append("]\n");
		s.append("    productType=[").append(productType).append("]\n");
		s.append("    encodingFormat=[").append(encodingFormat).append("]\n");
		s.append("]");

		return s.toString();
	}
	
}
