///**
// * Title:        LMSRequestInfo<p>
// * Description:  The LMSRequestInfo holds all attributes for a LMS request.<p>
// * Copyright:    Copyright (c) Peter Frei<p>
// * Company:      Telus Mobility Inc<p>
// * @author Peter Frei
// * @version 1.0
// */
//
//package com.telus.eas.framework.info;
//
//import java.util.*;
//
//
//public class LMSRequestInfo extends Info {
//
//  static final long serialVersionUID = 1L;
//
////  public LMSRequestInfo() {
////  }
////  public LMSRequestInfo(int pBan, String pSubscriberId, String pCategory, String pLetterCode) {
////    setBan(pBan);
////    setSubscriberId(pSubscriberId);
////    setCategory(pCategory);
////    setLetterCode(pLetterCode);
////  }
////
////  public LMSRequestInfo(int pBan, String pSubscriberId, String pCategory, String pLetterCode, LMSVariableInfo[] pVariables) {
////    setBan(pBan);
////    setSubscriberId(pSubscriberId);
////    setCategory(pCategory);
////    setLetterCode(pLetterCode);
////    setVariables(pVariables);
////  }
////
////  private int ban;
////  private String subscriberId;
////  private String category;
////  private String letterCode;
////  private Date productionDate;
////  private byte productionType = (byte)'I';   // LMS Default;
////  private LMSVariableInfo[] variables = new LMSVariableInfo[0];
////
////  public int getBan() {
////    return ban;
////  }
////  public void setBan(int ban) {
////    this.ban = ban;
////  }
////  public String getSubscriberId() {
////    return subscriberId;
////  }
////  public void setSubscriberId(String subscriberId) {
////    this.subscriberId = subscriberId;
////  }
////  public String getCategory() {
////    return category;
////  }
////  public void setCategory(String category) {
////    this.category = category;
////  }
////  public String getLetterCode() {
////    return letterCode;
////  }
////  public void setLetterCode(String letterCode) {
////    this.letterCode = letterCode;
////  }
////  public Date getProductionDate() {
////    return productionDate;
////  }
////  public void setProductionDate(Date productionDate) {
////    this.productionDate = productionDate;
////  }
////  public byte getProductionType() {
////    return productionType;
////  }
////  public LMSVariableInfo[] getVariables() {
////    return variables;
////  }
////  public void setVariables(LMSVariableInfo[] variables) {
////    this.variables = variables;
////  }
////  public String toString() {
////    StringBuffer s = new StringBuffer();
////
////    s.append("LMSRequestInfo:{\n");
////    s.append("    ban=[").append(ban).append("]\n");
////    s.append("    subscriberId=[").append(subscriberId).append("]\n");
////    s.append("    category=[").append(category).append("]\n");
////    s.append("    letterCode=[").append(letterCode).append("]\n");
////    s.append("    productionDate=[").append(productionDate).append("]\n");
////    s.append("    variables=[").append(variables).append("]\n");
////    s.append("}");
////
////    return s.toString();
////  }
//}
//
