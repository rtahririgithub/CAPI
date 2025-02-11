
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import java.util.Date;
import com.telus.api.*;
import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class DealerInfo extends Info implements Dealer{

  static final long serialVersionUID = 1L;

  protected String code;
  protected String name;
  protected String numberLocationCD;
  protected String departmentCode;
  protected String description;
  protected String descriptionFrench;
  protected Date   expiryDate;
  protected Date   effectiveDate;


  public String getCode() {
     return code;
   }

   public String getName() {
     return name;
   }

   public String getDepartmentCode() {
      return departmentCode;
    }

   public String getNumberLocationCD() {
     return numberLocationCD;
   }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public Date getEffectiveDate() {
    return effectiveDate;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setCode(String newCode) {
    code = newCode;
  }

  public void setName(String newName) {
    name = newName;

  }

  public void setDepartmentCode(String newDepartmentCode) {
    departmentCode = newDepartmentCode;

  }

  public void setNumberLocationCD(String newNumberLocationCD) {
    numberLocationCD = newNumberLocationCD;
  }

  public void setDescription(String newDescription) {
    description = newDescription;
  }

  public void setDescriptionFrench(String newDescription) {
    descriptionFrench = newDescription;

  }

  public void setEffectiveDate(Date newDate) {
    effectiveDate = newDate;
  }

  public void setExpiryDate(Date newDate) {
    expiryDate = newDate;
  }

 public SalesRep findSalesRep(String salesCode) throws TelusAPIException {
   throw new java.lang.UnsupportedOperationException("Method not implemented");
 }


 public SalesRep newSalesRep(String salesCode) throws TelusAPIException {
   throw new java.lang.UnsupportedOperationException("Method not implemented");
 }


 public SalesRep newSalesRep(SalesRep salesRep) throws TelusAPIException {
   throw new java.lang.UnsupportedOperationException("Method not implemented");

 }


  public void save()throws TelusAPIException {
    throw new java.lang.UnsupportedOperationException("Method not implemented");
 }


}
