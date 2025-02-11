
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class BillCycleInfo extends Info implements BillCycle {

  static final long serialVersionUID = 1L;

  protected String code ;
  protected String description  ;
  private int closeDay;
  private int dueDay;
  private String allocationIndicator;
  private String populationCode;
  private int billDay;
  private long numberOfAllocatedAccounts;
  private long numberOfAllocatedSubscribers;
  private long weight;

   public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return getDescription();
  }

  public void setCode(String newCode) {
    code = newCode;
  }

  public void setDescription(String newDescription) {
    description = newDescription;
  }
  public int getCloseDay() {
    return closeDay;
  }
  public void setCloseDay(int closeDay) {
    this.closeDay = closeDay;
  }
  public int getDueDay() {
    return dueDay;
  }
  public void setDueDay(int dueDay) {
    this.dueDay = dueDay;
  }
  public String getAllocationIndicator() {
    return allocationIndicator;
  }
  public void setAllocationIndicator(String allocationIndicator) {
    this.allocationIndicator = allocationIndicator;
  }
  public String getPopulationCode() {
    return populationCode;
  }
  public void setPopulationCode(String populationCode) {
    this.populationCode = populationCode;
  }
  public int getBillDay() {
    return billDay;
  }
  public void setBillDay(int billDay) {
    this.billDay = billDay;
  }
  public long getNumberOfAllocatedAccounts() {
    return numberOfAllocatedAccounts;
  }
  public void setNumberOfAllocatedAccounts(long numberOfAllocatedAccounts) {
    this.numberOfAllocatedAccounts = numberOfAllocatedAccounts;
  }
  public long getNumberOfAllocatedSubscribers() {
    return numberOfAllocatedSubscribers;
  }
  public void setNumberOfAllocatedSubscribers(long numberOfAllocatedSubscribers) {
    this.numberOfAllocatedSubscribers = numberOfAllocatedSubscribers;
  }
  public long getWeight() {
    return weight;
  }
  public void setWeight(long weight) {
    this.weight = weight;
  }
  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("BillCycleInfo:[\n");
      s.append("    code=[").append(code).append("]\n");
      s.append("    description=[").append(description).append("]\n");
      s.append("    closeDay=[").append(closeDay).append("]\n");
      s.append("    dueDay=[").append(dueDay).append("]\n");
      s.append("    allocationIndicator=[").append(allocationIndicator).append("]\n");
      s.append("    populationCode=[").append(populationCode).append("]\n");
      s.append("    billDay=[").append(billDay).append("]\n");
      s.append("    numberOfAllocatedAccounts=[").append(numberOfAllocatedAccounts).append("]\n");
      s.append("    numberOfAllocatedSubscribers=[").append(numberOfAllocatedSubscribers).append("]\n");
      s.append("    weight=[").append(weight).append("]\n");

      return s.toString();
  }
}