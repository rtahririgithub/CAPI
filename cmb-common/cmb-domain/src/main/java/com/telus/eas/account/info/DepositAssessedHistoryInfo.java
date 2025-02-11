/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.account.DepositAssessedHistory;
import com.telus.eas.framework.info.Info;

public class DepositAssessedHistoryInfo  extends Info implements DepositAssessedHistory {

   static final long serialVersionUID = 1L;

   private String description;
   private String descriptionFrench;
   private String user;
   private Date changeDate;
   private String productType;
   private double depositAssessed;
   
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	public double getDepositAssessed() {
		return depositAssessed;
	}
	public void setDepositAssessed(double depositAssessed) {
		this.depositAssessed = depositAssessed;
	}
	public String getDescription() {
		return description;
	}

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public void setDescription(String description) {
    this.description = description;
  }
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }

  public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("DepositAssessedHistoryInfo[");
		buffer.append("changeDate = ").append(changeDate).append("\n");
		buffer.append(" depositAssessed = ").append(depositAssessed).append("\n");
    buffer.append(" description = ").append(description).append("\n");
    buffer.append(" descriptionFrench = ").append(descriptionFrench).append("\n");
		buffer.append(" productType = ").append(productType).append("\n");
		buffer.append(" user = ").append(user).append("\n");
		buffer.append("]");
		return buffer.toString();
	}
   

}



