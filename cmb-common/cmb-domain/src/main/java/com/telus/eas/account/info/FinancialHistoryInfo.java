/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.CollectionHistory;
import com.telus.api.account.CollectionState;
import com.telus.api.account.CollectionStep;
import com.telus.api.account.DebtSummary;
import com.telus.api.account.FinancialHistory;
import com.telus.api.account.MonthlyFinancialActivity;
import com.telus.api.account.PaymentHistory;
import com.telus.eas.framework.info.Info;

public class FinancialHistoryInfo extends Info implements FinancialHistory {

  static final long serialVersionUID = 1L;

  private boolean delinquent;

  private boolean lastPaymentRefunded;

  private boolean hotlined;
  private Date hotlinedDate;
  private Date delinquentDate;
  private Date writtenOffDate;
  private CollectionState collectionState;
  private PaymentHistoryInfo lastPayment;

  /**
   * @link aggregation
   */
  private DebtSummaryInfo debtSummary = new DebtSummaryInfo();
  //private MonthlyFinancialActivityInfo[] monthlyFinancialActivity;


  /**
   *@link aggregation
   */
  private List monthlyFinancialActivity = new ArrayList(12);
  private java.util.Date lastPaymentDate;
  private double lastPaymentAmount;
  private boolean writtenOff;
  private CollectionStepInfo collectionStep=new CollectionStepInfo();
  private String collectionAgency;
  private CollectionStepInfo nextCollectionStep=new CollectionStepInfo();
  private CollectionStateInfo collectionStateInfo;
  
  
  public boolean isDelinquent() {
    return delinquent;
  }
/*
public boolean isLastPaymentRefunded() {
    return lastPaymentRefunded;
  }
*/
  public DebtSummary getDebtSummary() {
    return debtSummary;
  }

  public DebtSummaryInfo getDebtSummary0() {
    return debtSummary;
  }

  public MonthlyFinancialActivity[] getMonthlyFinancialActivity() {
    return getMonthlyFinancialActivity0();
  }

  public MonthlyFinancialActivityInfo[] getMonthlyFinancialActivity0() {
    return (MonthlyFinancialActivityInfo[]) monthlyFinancialActivity.toArray(
        new MonthlyFinancialActivityInfo[monthlyFinancialActivity.size()]);
  }

  public void addMonthlyFinancialActivity(MonthlyFinancialActivityInfo info) {
    monthlyFinancialActivity.add(info);
  }

  public void setDelinquent(boolean delinquent) {
    this.delinquent = delinquent;
  }

  public void setLastPaymentDate(java.util.Date newLastPaymentDate) {
    lastPaymentDate = newLastPaymentDate;
  }
/*
  public java.util.Date getLastPaymentDate() {
    return lastPaymentDate;
  }
*/
  public void setLastPaymentAmount(double newLastPaymentAmount) {
    lastPaymentAmount = newLastPaymentAmount;
  }

  /*
  public double getLastPaymentAmount() {
    return lastPaymentAmount;
  }
*/
  public int getDishonoredPaymentCount() {
    int count = 0;
    for(int i=0; i<monthlyFinancialActivity.size(); i++) {
      MonthlyFinancialActivity m = (MonthlyFinancialActivity)monthlyFinancialActivity.get(i);
      count += m.getDishonoredPaymentCount();
    }

    return count;
  }

  public int getSuspensionCount() {
    int count = 0;
    for(int i=0; i<monthlyFinancialActivity.size(); i++) {
      MonthlyFinancialActivity m = (MonthlyFinancialActivity)monthlyFinancialActivity.get(i);
      if(m.isSuspended()) {
        count++;
      }
    }

    return count;
  }

  public int getCancellationCount() {
    int count = 0;
    for(int i=0; i<monthlyFinancialActivity.size(); i++) {
      MonthlyFinancialActivity m = (MonthlyFinancialActivity)monthlyFinancialActivity.get(i);
      if(m.isCancelled()) {
        count++;
      }
    }

    return count;
  }

  public void copyFrom(FinancialHistoryInfo o) {
     delinquent    = o.delinquent;
     writtenOff    = o.writtenOff;
     debtSummary.copyFrom(o.debtSummary);
     monthlyFinancialActivity.clear();
     monthlyFinancialActivity.addAll(o.monthlyFinancialActivity);
     copyLastPayment(o.lastPayment);
     lastPaymentDate   = cloneDate(o.lastPaymentDate);
     lastPaymentAmount   = o.lastPaymentAmount;
     lastPaymentRefunded   = o.lastPaymentRefunded;
     collectionStep.copyFrom(o.collectionStep);
     nextCollectionStep.copyFrom(o.nextCollectionStep);
     collectionAgency =o.collectionAgency;
     setHotlined(o.hotlined);
     hotlinedDate   = cloneDate(o.hotlinedDate);
     delinquentDate   = cloneDate(o.delinquentDate);
     writtenOffDate   = cloneDate(o.writtenOffDate);
     collectionState = o.collectionState;
  }



  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("FinancialHistoryInfo:{\n");
    //s.append("    =[").append().append("]\n");
    s.append("    delinquent=[").append(delinquent).append("]\n");
    s.append("    writtenOff=[").append(writtenOff).append("]\n");
    s.append("debtSummary=[").append(debtSummary).append("]\n");
    for(int i=0; i<monthlyFinancialActivity.size(); i++) {
      s.append("monthlyFinancialActivity["+i+"]=[").append(monthlyFinancialActivity.get(i)).append("]\n");
    }
    s.append("    lastPaymentDate=[").append(lastPaymentDate).append("]\n");
    s.append("    lastPaymentAmount=[").append(lastPaymentAmount).append("]\n");
    s.append("    collectionState=[").append(collectionStep).append("]\n");
    s.append("    lastPaymentRefunded=[").append(lastPaymentRefunded).append("]\n");
    s.append("    hotlined=[").append(hotlined).append("]\n");
    s.append("    hotlinedDate=[").append(hotlinedDate).append("]\n");
    s.append("    delinquentDate=[").append(delinquentDate).append("]\n");
    s.append("    writtenOffDate=[").append(writtenOffDate).append("]\n");
    s.append("    collectionStateInfo=[").append(collectionStateInfo).append("]\n");
    s.append("}");

    return s.toString();
  }
  public boolean isWrittenOff() {
    return writtenOff;
  }
  public void setWrittenOff(boolean writtenOff) {
    this.writtenOff = writtenOff;
  }

  /**
   * @deprecated Use getCollectionState0 instead.
   * since May 29, 2006
   * @see #getCollectionState0()
   */
  public CollectionStep getCollectionStep() {
    return collectionStep;
  }

  public CollectionStepInfo getCollectionStep0() {
   return collectionStep;
 }

  public void setCollectionStep(CollectionStepInfo collectionStep) {
    this.collectionStep = collectionStep;
  }
  /**
   * @deprecated Use getCollectionState0 instead.
   * since May 29, 2006
   * @see #getCollectionState0()
   */
  public String getCollectionAgency() {
    return collectionAgency;
  }
  public void setCollectionAgency(String collectionAgency) {
    this.collectionAgency = collectionAgency;
  }
  /**
   * @deprecated Use getCollectionState0 instead.
   * since May 29, 2006
   * @see #getCollectionState0()
   */
  public CollectionStep getNextCollectionStep() {
    return nextCollectionStep;
  }

  public CollectionStepInfo getNextCollectionStep0() {
   return nextCollectionStep;
 }

  public void setNextCollectionStep(CollectionStepInfo nextCollectionStep) {
    this.nextCollectionStep = nextCollectionStep;
  }

/**
 * @param lastPaymentRefunded The lastPaymentRefunded to set.
 */
  public void setLastPaymentRefunded(boolean lastPaymentRefunded) {
	this.lastPaymentRefunded = lastPaymentRefunded;
  }

  public boolean isHotlined(){
	  return hotlined;
  }

  public void setHotlined(boolean hotlined) {
	    this.hotlined = hotlined;
  }

  public Date getHotlinedDate(){
	  return hotlinedDate;
 }

  public void setHotlinedDate(Date lastActHotline){
	  hotlinedDate = lastActHotline;
 }
  public Date getDelinquentDate(){
	  return delinquentDate;
  }

  public void setDelinquentDate(Date colDelinqStsDate){
	  delinquentDate = colDelinqStsDate;
 }
  public Date getWrittenOffDate(){
	  return writtenOffDate;
  }
  public void setWrittenOffDate(Date colActvDate){
	  writtenOffDate = colActvDate;
  }
  public CollectionState getCollectionState() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
  }
  public CollectionStateInfo getCollectionState0() {
		return this.collectionStateInfo;
  }

  public void setCollectionState(CollectionStateInfo collectionStateInfo) {
	    this.collectionStateInfo = collectionStateInfo;
  }

  public CollectionHistory[] getCollectionHistory(Date from, Date to) throws TelusAPIException {
    throw new UnsupportedOperationException("Method getCollectionHistory() not implemented here.");
  }
  
  public void setLastPayment(PaymentHistoryInfo info){
	  lastPayment = info;
  }
  public PaymentHistory getLastPayment(){
	  return this.lastPayment;
  }
  public Date getLastPaymentDate(){
	  if (lastPayment != null) return lastPayment.getDepositDate();
	  return null;
  }
  public double getLastPaymentAmount(){
	  //defect PROD00176130 fix , shall return the original amount
	  if (lastPayment != null) return lastPayment.getOriginalAmount();
	  return 0.0;
  }
  
  public boolean isLastPaymentRefunded(){
	  if (lastPayment != null) return lastPayment.isPaymentRefunded();
	  return false;
  }
 
  public boolean isLastPaymentBackedout(){
	  if (lastPayment != null) return lastPayment.isPaymentBackedout();
	  return false;

  }
 public boolean isLastPaymentFullyTransferred(){
	  if (lastPayment != null) return lastPayment.isPaymentFullyTransferred();
	  return false;
 }
 
 public boolean isLastPaymentSufficient(){
	  if (lastPayment != null) return lastPayment.isPaymentSufficient();
	  return false;
 }
 public String getLastPaymentActivityCode(){
	  if (lastPayment != null) return lastPayment.getActivityCode();
	  return "";
 }
 
 private void copyLastPayment(PaymentHistoryInfo p){
	 if (p != null){
		 if (lastPayment == null) {
			   lastPayment = new PaymentHistoryInfo();
		 }
			lastPayment.copyFrom(p);
	 }else{
		 lastPayment = null;
	 }			 
 }
 
}




