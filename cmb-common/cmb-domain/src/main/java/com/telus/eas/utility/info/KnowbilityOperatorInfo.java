
package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class KnowbilityOperatorInfo extends Info implements KnowbilityOperator{

	static final long serialVersionUID = 1L;

  private String name;
  private String iD;
  private String workPositionId;
  private String supervisorId;
  private String departmentCode;
  private double chargeThresholdAmount;
  private double creditThresholdAmount;

  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name = name;
  }

  public String getID(){
    return iD;
  }

  public void setID(String iD){
    this.iD = iD;
  }

  public String getDescription(){
    return name;
  }

  public String getDescriptionFrench(){
    return name;
  }

  public String getCode(){
    return iD;
  }

  public String getWorkPositionId(){
    return workPositionId;
  }

  public void setWorkPositionId(String workPositionId){
    this.workPositionId = workPositionId;
  }

  public String getSupervisorId(){
    return supervisorId;
  }

  public void setSupervisorId(String supervisorId){
    this.supervisorId = supervisorId;
  }

  public String getDepartmentCode() {
    return departmentCode;
  }

  public void setDepartmentCode(String departmentCode) {
    this.departmentCode = departmentCode;
  }

  public double getChargeThresholdAmount(){
    return chargeThresholdAmount;
  }
  public void setChargeThresholdAmount(double chargeThresholdAmount){
    this.chargeThresholdAmount = chargeThresholdAmount;
  }

  public double getCreditThresholdAmount(){
    return creditThresholdAmount;
  }
  public void setCreditThresholdAmount(double creditThresholdAmount){
    this.creditThresholdAmount = creditThresholdAmount;
  }

  public String toString() {
   StringBuffer s = new StringBuffer();

   s.append("KnowbilityOperatorInfo:{\n");
   s.append("    iD=[").append(iD).append("]\n");
   s.append("    name=[").append(name).append("]\n");
   s.append("    workPositionId=[").append(workPositionId).append("]\n");
   s.append("    supervisorId=[").append(supervisorId).append("]\n");
   s.append("    chargeThresholdAmount=[").append(chargeThresholdAmount).append("]\n");
   s.append("    creditThresholdAmount=[").append(creditThresholdAmount).append("]\n");
   s.append("}");

   return s.toString();
 }
}
