///*
// * $Id$
// * %E% %W%
// * Copyright (c) Clearnet Inc. All Rights Reserved.
// */
//
//package com.telus.eas.account.info;
//
//import com.telus.api.*;
//import com.telus.api.account.*;
//import com.telus.eas.framework.info.*;
//import java.util.*;
//
//public class LMSLetterRequestInfo  extends Info implements LMSLetterRequest {
//
////  public static final long serialVersionUID = 6851884307366078225L;
//
//  static final long serialVersionUID = 1L;
////
////  private int banId;
////  private int id;
////  private String operatorId;
////  private String letterCategory;
////  private int letterVersion;
////  private String letterCode;
////  private String subscriberId;
////  private String status;
////  private Date submitDate;
////  private Date productionDate;
////  private String allVariables;
//////  private char productionType = 'N';
////  private char productionType = 'I';
////  private Map manualVariables = new HashMap();
////
////  public int getBanId() {
////    return banId;
////  }
////
////  public void setBanId(int banId){
////    this.banId = banId;
////  }
////
////  public int getId() {
////    return id;
////  }
////
////  public void setId(int id){
////    this.id = id;
////  }
////
////  public String getOperatorId() {
////    return operatorId;
////  }
////
////  public void setOperatorId(String operatorId){
////    this.operatorId = operatorId;
////  }
////
////  public String getLetterCategory() {
////    return letterCategory;
////  }
////
////  public int getLetterVersion() {
////    return letterVersion;
////  }
////
////  public void setLetterVersion(int letterVersion) {
////    this.letterVersion = letterVersion;
////  }
////
////  public String getLetterCode() {
////    return letterCode;
////  }
////
////  public String getSubscriberId() {
////    return subscriberId;
////  }
////
////  public String getStatus() {
////    return status;
////  }
////
////  public void setStatus(String status){
////    this.status = status;
////  }
////
////  public Date getSubmitDate() {
////    return submitDate;
////  }
////
////  public void setSubmitDate(Date submitDate){
////    this.submitDate = submitDate;
////  }
////
////  public Date getProductionDate() {
////    return productionDate;
////  }
////
////  public String getAllVariables() {
////    return allVariables;
////  }
////
////  public void setAllVariables(String allVariables){
////    this.allVariables = allVariables;
////  }
////
////  public void save() throws TelusAPIException {
////    throw new UnsupportedOperationException("Method not implemented here");
////  }
////
////  public void delete() throws TelusAPIException {
////    throw new UnsupportedOperationException("Method not implemented here");
////  }
////
////  public void resend() throws TelusAPIException {
////    throw new UnsupportedOperationException("Method not implemented here");
////  }
////
////  public void setLetterCategory(String letterCategory) {
////    this.letterCategory = letterCategory;
////  }
////
////  public void setLetterCode(String letterCode) {
////    this.letterCode = letterCode;
////  }
////
////  public void setProductionDate(Date productionDate) {
////    this.productionDate = productionDate;
////  }
////
////  public void setSubscriberId(String subscriberId) {
////    this.subscriberId = subscriberId;
////  }
////
////  public char getProductionType() {
////    return productionType;
////  }
////
////  public void setProductionType(char productionType){
////    this.productionType = productionType;
////  }
////
////  public void setManualVariable(String name, String value){
////    manualVariables.put(name, value);
////  }
////
////  public void clearManualVariables(){
////    manualVariables.clear();
////  }
////
////  public String[] getManualVariableNames(){
////    return (String[])manualVariables.keySet().toArray(new String[manualVariables.size()]);
////  }
////
////  public String getManualVariable(String name){
////    return (String)manualVariables.get(name);
////  }
////
////  public String toString() {
////    StringBuffer s = new StringBuffer(128);
////
////    s.append("LMSLetterRequestInfo:[\n");
////    s.append("    allVariables=[").append(allVariables).append("]\n");
////    s.append("    banId=[").append(banId).append("]\n");
////    s.append("    id=[").append(id).append("]\n");
////    s.append("    letterCategory=[").append(letterCategory).append("]\n");
////    s.append("    letterCode=[").append(letterCode).append("]\n");
////    s.append("    letterVersion=[").append(letterVersion).append("]\n");
////    s.append("    manualVariables=[").append(manualVariables).append("]\n");
////    s.append("    operatorId=[").append(operatorId).append("]\n");
////    s.append("    productionDate=[").append(productionDate).append("]\n");
////    s.append("    productionType=[").append(productionType).append("]\n");
////    s.append("    status=[").append(status).append("]\n");
////    s.append("    submitDate=[").append(submitDate).append("]\n");
////    s.append("    subscriberId=[").append(subscriberId).append("]\n");
////    s.append("]");
////
////    return s.toString();
////  }
////
/////*
////  public String toString() {
////    StringBuffer s = new StringBuffer();
////
////    s.append("CancellationPenaltyInfo:{\n");
////    s.append("    depositAmount=[").append(depositAmount).append("]\n");
////    s.append("    depositInterest=[").append(depositInterest).append("]\n");
////    s.append("    penalty=[").append(penalty).append("]\n");
////    s.append("}");
////
////    return s.toString();
////  }
////*/
//
//}
//
//
//
//
