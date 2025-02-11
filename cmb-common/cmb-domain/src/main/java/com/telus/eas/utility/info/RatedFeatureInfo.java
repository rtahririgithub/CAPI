
/**
 * Title:      RatedFeatureInfo   <p>
 * Description:  <p>
  * Copyright:    Copyright (c) Ludmila Pomirche <p>
 * Company:     Telus Mobilty INC. <p>
 * @author Ludmila Pomirche
 * @version 1.0
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;

public class RatedFeatureInfo extends FeatureInfo implements RatedFeature{

	static final long serialVersionUID = 1L;

	public RatedFeatureInfo() {
	}
 
  private double recurringCharge=0;
  private double usageCharge=0;
  private double additionalCharge=0;
  private int recurringChargeFrequency;
  private boolean minutePoolingContributor;
  private int callingCircleSize;
  private boolean prepaidCallingCircle = false;
  private boolean wps = false;

  public boolean isPrepaidCallingCircle() {
	return isWPS() && (prepaidCallingCircle || 
			(
			 (categoryCode != null) && 
			 (CATEGORY_CODE_CALLING_CIRCLE.equalsIgnoreCase(categoryCode.trim()) || 
			 CATEGORY_CODE_CALL_HOME_FREE.equalsIgnoreCase(categoryCode.trim()) )
			)
		   );
  }
  
  /*
  public boolean isCallingCircle() {
	  return (super.isCallingCircle() || isPrepaidCallingCircle());
  }
*/
  public void setPrepaidCallingCircle(boolean isPrepaidCallingCircle) {
	this.prepaidCallingCircle = isPrepaidCallingCircle;
  }

  public void setRecurringCharge(double newRecurringCharge) {
    recurringCharge = newRecurringCharge;
  }

  public double getRecurringCharge() {
    return recurringCharge;
  }

  public void setUsageCharge(double newUsageCharge) {
    usageCharge = newUsageCharge;
  }

  public double getUsageCharge() {
		return usageCharge;
	}

	public double getAdditionalCharge() {
		return additionalCharge;
	}

	public void setAdditionalCharge(double additionalCharge) {
		this.additionalCharge = additionalCharge;
	}
	
  public void setRecurringChargeFrequency(int newRecurringChargeFrequency) {
    recurringChargeFrequency = newRecurringChargeFrequency;
  }

  public int getRecurringChargeFrequency() {
    return recurringChargeFrequency;
  }

  public boolean  isMinutePoolingContributor(){
	return minutePoolingContributor;
  }

  public void  setMinutePoolingContributor(boolean minutePoolingContributor){
	  this.minutePoolingContributor = minutePoolingContributor;
  }

  public int getCallingCircleSize(){
	return callingCircleSize;
  }
  public void setCallingCircleSize( int size )
  {
	  this.callingCircleSize = size;
  }
  
  /**
   * Set to true when this is created under a WPS (Prepaid) service
   * @param wps
   */
  public void setWPS(boolean wps){
	  this.wps = wps;
  }
  
  /**
   * Returns true when this is a manipulated feature created under a Prepaid service
   * @return boolean
   */
  public boolean isWPS() {
	  return wps;
  }
 
    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("RatedFeatureInfo:[\n");
        s.append(super.toString()).append("\n");
        s.append("    recurringCharge=[").append(recurringCharge).append("]\n");
        s.append("    usageCharge=[").append(usageCharge).append("]\n");
        s.append("    additionalCharge=[").append(additionalCharge).append("]\n");
        s.append("    recurringChargeFrequency=[").append(recurringChargeFrequency).append("]\n");
        s.append("    minutePoolingContributor=[").append(minutePoolingContributor).append("]\n");  // Dmitry S.
        s.append("    prepaidCallingCircle=[").append(isPrepaidCallingCircle()).append("]\n");
        s.append("    wps=[").append(wps).append("]\n");
        s.append("]");

        return s.toString();
    }


}
