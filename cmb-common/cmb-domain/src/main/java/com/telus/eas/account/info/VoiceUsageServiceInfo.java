package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.Info;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TELUS Mobility</p>
 * @author Michael Qin
 * @version 1.0
 */

public class VoiceUsageServiceInfo extends Info implements VoiceUsageService {

   static final long serialVersionUID = 1L;

  private String serviceCode;
  private String usageRecordTypeCode;
  private String iMAllocationIndicator;
  private String productType;
  private VoiceUsageServiceDirection[] voiceUsageServiceDirections;

  public String getServiceCode() {
    return serviceCode;
  }

  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  public String getUsageRecordTypeCode() {
    return usageRecordTypeCode;
  }

  public void setUsageRecordTypeCode(String usageRecordTypeCode){
    this.usageRecordTypeCode = usageRecordTypeCode;
  }

  public String getIMAllocationIndicator() {
    return iMAllocationIndicator;
  }

  public void setIMAllocationIndicator(String iMAllocationIndicator){
    this.iMAllocationIndicator = iMAllocationIndicator;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType){
    this.productType = productType;
  }

  public VoiceUsageServiceDirection[] getVoiceUsageServiceDirections() {
    return voiceUsageServiceDirections;
  }

  public void setVoiceUsageServiceDirections(VoiceUsageServiceDirection[] voiceUsageServiceDirections) {
    this.voiceUsageServiceDirections = voiceUsageServiceDirections;
  }

}