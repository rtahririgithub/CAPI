package com.telus.eas.account.info;

import com.telus.api.account.CLMSummary;
import com.telus.eas.framework.info.Info;

/**
 * @author Roman Tov
 * @version 1.0, 20-Jul-2006
 */

public class CLMSummaryInfo extends Info implements CLMSummary  {

  private double unpaidBillCharges;
  private double unpaidAirTime;
  private double unpaidData;
  private double requiredMinimumPayment;
  private double unpaidUnBilledAmount;

  public double getUnpaidBillCharges(){
    return unpaidBillCharges;
  }

  public double getUnpaidAirTime(){
    return unpaidAirTime;
  }

  public double getUnpaidData(){
    return unpaidData;
  }

  public double getRequiredMinimumPayment(){
    return requiredMinimumPayment;
  }

  public CLMSummaryInfo (double uBillCharges, double uAirTime, double uData, double rMinimumPayment, double unpaidUnBilledAmount){
    this.unpaidBillCharges = uBillCharges;
    this.unpaidAirTime = uAirTime;
    this.unpaidData = uData;
    this.requiredMinimumPayment = rMinimumPayment;    
    this.unpaidUnBilledAmount = unpaidUnBilledAmount;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("CLMSummaryInfo:{\n");
    s.append(super.toString());
    s.append("    unpaidBillCharges=[").append(unpaidBillCharges).append("]\n");
    s.append("    unpaidAirTime=[").append(unpaidAirTime).append("]\n");
    s.append("    unpaidData=[").append(unpaidData).append("]\n");
    s.append("    UnpaidUnBilledAmount=[").append(unpaidUnBilledAmount).append("]\n");
    s.append("    requiredMinimumPayment=[").append(requiredMinimumPayment).append("]\n");
    s.append("}");

    return s.toString();
  }public double getUnpaidUnBilledAmount() {        return unpaidUnBilledAmount;}
}
