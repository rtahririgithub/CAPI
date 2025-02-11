package com.telus.provider.account;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.ActivationCredit;
import com.telus.api.account.Subscriber;
import com.telus.api.util.SessionUtil;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.ActivationCreditInfo;
//import com.telus.eas.framework.info.LMSRequestInfo;
//import com.telus.eas.framework.info.LMSVariableInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;

public class TMActivationCredit extends TMCredit implements ActivationCredit
{
  /*
  * @link aggregation
  */
  private final ActivationCreditInfo delegate;
  private final String ACTIVATION_CREDIT_MEMO_TYPE = "ACCR";
  private final String CONTRACT_TERM_ADJUSTMENT_CODE_MIKE_0MONTH = "CHM0";
  private final String CONTRACT_TERM_ADJUSTMENT_CODE_MIKE_12MONTH = "CHM1";
  private final String CONTRACT_TERM_ADJUSTMENT_CODE_MIKE_24MONTH = "CHM2";
  private final String CONTRACT_TERM_ADJUSTMENT_CODE_MIKE_36MONTH = "CHM3";
  private final String CONTRACT_TERM_ADJUSTMENT_CODE_CELL_0MONTH = "CHC0";
  private final String CONTRACT_TERM_ADJUSTMENT_CODE_CELL_12MONTH = "CHC1";
  private final String CONTRACT_TERM_ADJUSTMENT_CODE_CELL_24MONTH = "CHC2";
  private final String CONTRACT_TERM_ADJUSTMENT_CODE_CELL_36MONTH = "CHC3";

  private final String AOM_CONTRACT_TERM_ADJUSTMENT_CODE = "AOMP";
  
  private final int CONTRACT_TERM_MTM = 0;
  private final int CONTRACT_TERM_12MONTH = 12;
  private final int CONTRACT_TERM_24MONTH = 24;
  private final int CONTRACT_TERM_36MONTH = 36;

  /* these are for Subscriber Level ONLY */
  private final String LMS_CONTRACT_RENEWAL_CATEGORY = "REE";
  private final String LMS_CONTRACT_RENEWAL_LETTERCODE_FRENCH = "FPHC";
  private final String LMS_CONTRACT_RENEWAL_LETTERCODE_ENGLISH = "EPHC";

  private final String DOUBLE_FORMAT = "###,###,##0.00";
  private final String DATE_FORMAT = "yyyy-MMM-dd HH:mm:ss";
  
  
  public TMActivationCredit(TMProvider provider, ActivationCreditInfo delegate, AccountSummary accountSummary)
  {
    super(provider, delegate, accountSummary);
    this.delegate = delegate;
    
  }

  /**
   * Decorative Methods
   */
  public String getCode() {
    return delegate.getCode();
  }

  public String getDescriptionFrench() {
    return delegate.getDescriptionFrench();
  }

  public String getDescription() {
    return delegate.getDescription();
  }

  public String getBarCode() {
    return delegate.getBarCode();
  }

  public int getContractTerm() {
    return delegate.getContractTerm();
  }

  public Date getExpiryDate() {
    return delegate.getExpiryDate();
  }

  public boolean isContractTermCredit() {
    return delegate.isContractTermCredit();
  }

  public boolean isNewActivationCredit() {
    return delegate.isNewActivationCredit();
  }

  public boolean isPromotionCredit() {
    return delegate.isPromotionCredit();
  }

  public boolean isPricePlanCredit() {
    return delegate.isPricePlanCredit();
  }
  
  public boolean isFidoCredit() {
      return delegate.isFidoCredit();
  }

  public void setBarCode(String barCode) {
    delegate.setBarCode(barCode);
  }

  public void setContractTerm(int contractTerm) {
    delegate.setContractTerm(contractTerm);
  }

  public void setExpiryDate(Date expiryDate) {
    delegate.setExpiryDate(expiryDate);
  }

  private String getAdjustmentCode() throws TelusAPIException {
	  String adjustmentCode = "";

      if(delegate.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
      {
        switch (getContractTerm())
        {
          case CONTRACT_TERM_MTM:
            adjustmentCode = this.CONTRACT_TERM_ADJUSTMENT_CODE_MIKE_0MONTH;
            break;
          case CONTRACT_TERM_12MONTH:
            adjustmentCode = this.CONTRACT_TERM_ADJUSTMENT_CODE_MIKE_12MONTH;
            break;
          case CONTRACT_TERM_24MONTH:
            adjustmentCode = this.CONTRACT_TERM_ADJUSTMENT_CODE_MIKE_24MONTH;
            break;
          case CONTRACT_TERM_36MONTH:
            adjustmentCode = this.CONTRACT_TERM_ADJUSTMENT_CODE_MIKE_36MONTH;
            break;
          default:
            throw new TelusAPIException("Not Supported Contract Term : " + getContractTerm());
          
        }
      }
      else if(delegate.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS))
      {
        switch (getContractTerm())
        {
          case CONTRACT_TERM_MTM:
            adjustmentCode = this.CONTRACT_TERM_ADJUSTMENT_CODE_CELL_0MONTH;
            break;
          case CONTRACT_TERM_12MONTH:
            adjustmentCode = this.CONTRACT_TERM_ADJUSTMENT_CODE_CELL_12MONTH;
            break;
          case CONTRACT_TERM_24MONTH:
            adjustmentCode = this.CONTRACT_TERM_ADJUSTMENT_CODE_CELL_24MONTH;
            break;
          case CONTRACT_TERM_36MONTH:
            adjustmentCode = this.CONTRACT_TERM_ADJUSTMENT_CODE_CELL_36MONTH;
            break;
          default:
           throw new TelusAPIException("Not Supported Contract Term : " + getContractTerm());
        }
      }
      else
        throw new TelusAPIException("Non-Existant Adjustment Code for Product Type: " + delegate.getProductType());
      return adjustmentCode;
  }
  
  private String getAdjustmentCodeForAOM() throws TelusAPIException {
	  String adjustmentCode = this.AOM_CONTRACT_TERM_ADJUSTMENT_CODE;      
	  return adjustmentCode;
  }
  
  /**
   * Service Methods
   */
  public void apply() throws TelusAPIException {
    boolean subscriberLevel = true; // default to apply to subscriber level
    try{
      validateBan();
      String adjustmentCode = getAdjustmentCode();
      if(delegate.getOfferCode() != null && delegate.getOfferCode().length() > 0) {
    	 adjustmentCode = getAdjustmentCodeForAOM();
      }

      delegate.setReasonCode(adjustmentCode);
      
      SubscriberInfo subscriberInfo = null;
      if(delegate.getSubscriberId() != null && delegate.getSubscriberId().length() > 0)
      {
    	subscriberInfo = provider.getSubscriberLifecycleHelper().retrieveSubscriber(delegate.getBan(), delegate.getSubscriberId());
      } else {
        subscriberLevel = false; // cannot apply to subscriber level
      }
      
      // Compare activation date to curr date for proper productionDate assignment
      Date curDate = new Date();
      SimpleDateFormat yrFormat = new SimpleDateFormat("yyyy");
      SimpleDateFormat mnthFormat = new SimpleDateFormat("MM");
      SimpleDateFormat dayFormat = new SimpleDateFormat("d");
      int day = Integer.parseInt(dayFormat.format(curDate));
      int month = Integer.parseInt(mnthFormat.format(curDate));
      int year = Integer.parseInt(yrFormat.format(curDate));

      GregorianCalendar today = new GregorianCalendar(year, month, day);
      
      Date effectiveDate = new Date();
      delegate.setBalanceImpactFlag(true);
      
      if (subscriberInfo != null &&
          subscriberInfo.getStartServiceDate() != null &&
          subscriberInfo.getStartServiceDate().compareTo(today.getTime()) > 0) {
        // future dated
        effectiveDate = subscriberInfo.getStartServiceDate();
        delegate.setBalanceImpactFlag(false);
      }
      
      String handsetModelType = "";
      String dealerCode = "";
      if (subscriberInfo != null)
      {
    	  EquipmentInfo equipmentInfo = null;
    	  equipmentInfo = provider.getProductEquipmentHelper().getEquipmentInfobySerialNo(subscriberInfo.getSerialNumber());
         
    	  handsetModelType = equipmentInfo.getProductType().equals(EquipmentInfo.PRODUCT_TYPE_PAGER) ?
          equipmentInfo.getEquipmentModel() == null ?
          equipmentInfo.getProductName() : equipmentInfo.getEquipmentModel() :
          equipmentInfo.getEquipmentModel(); 
          
        dealerCode = subscriberInfo.getDealerCode();
        if (subscriberInfo.getStatus() != Subscriber.STATUS_ACTIVE) {
        	Logger.debug("Subsriber is NOT ACTIVE " + delegate.getBan() + " " + delegate.getSubscriberId());
          // this may fail in appending credit to a subscriber level, but subscriber is not Active
          // @TODO
          //
        }
      }
      else {
        subscriberLevel = false; // cannot apply to subscriber level
      }
      
      delegate.setEffectiveDate(effectiveDate);
     
      StringBuffer memoTxt = new StringBuffer(256);
              
      memoTxt.append("Bill Credit: ");
      memoTxt.append(getDescription());
      memoTxt.append(" [").append("Dealer Code: ").append(dealerCode);
      memoTxt.append(", Contract Term: ").append(getContractTerm()).append(" Months");
      memoTxt.append(", Handset Model Type: ").append(handsetModelType).append(" ] ");
                        
      memoTxt.append("Reference BAN Credit memo on ");
      memoTxt.append(dateString(new Date(), DATE_FORMAT));
      memoTxt.append(", effective ");
      memoTxt.append(dateString(effectiveDate, DATE_FORMAT));
      memoTxt.append(", for amount: ");
      memoTxt.append("$").append(doubleString(new Double(delegate.getAmount()), DOUBLE_FORMAT));

      delegate.setText(memoTxt.toString());// set SAME AS MEMO TXT

      // apply credit
      provider.getAccountLifecycleManager().applyCreditToAccountWithOverride(delegate,
    		  SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
      // create credit letter since instance applies bill credit
//      this.createManualLetterRequest(effectiveDate, subscriberLevel);

      // create memo for bill credit
      this.memo(true, effectiveDate, memoTxt.toString());

    }catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
	}
  }

  public void apply(int ban, String subscriberId) throws TelusAPIException {
    try{
      delegate.setBan(ban);
      delegate.setSubscriberId(subscriberId);
      this.apply();
    } catch (Throwable e) {
      throw new TelusAPIException(e);
    }
  }

  public void apply(int ban) throws TelusAPIException {
    try{
      delegate.setBan(ban);
      this.apply();
    } catch (Throwable e) {
      throw new TelusAPIException(e);
    }
  }
  
  private void memo(boolean creditAppliedToClientBill) throws TelusAPIException {
     memo(creditAppliedToClientBill, new Date(), "");
  }
  
  private void memo(boolean creditAppliedToClientBill, Date effectiveDate, String inMemoText) throws TelusAPIException {
    try{
      
      validateBan();
      
      StringBuffer memoTxt = new StringBuffer(256);
      if (inMemoText != null && inMemoText.length() > 0 )
      {
        memoTxt.append(inMemoText);
      }
      else
      {
        if(creditAppliedToClientBill)
          memoTxt.append("Bill Credit: ");
        else
          memoTxt.append("Upfront Credit: ");
      
        memoTxt.append(getDescription());
        memoTxt.append(" [ ").append(getContractTerm()).append(" Months ] - ");
        if(creditAppliedToClientBill)
        {
          memoTxt.append("Reference BAN Credit memo on ");
          memoTxt.append(dateString(new Date(), DATE_FORMAT));
          memoTxt.append(", effective ");
          memoTxt.append(dateString(effectiveDate, DATE_FORMAT));
          memoTxt.append(", for amount: ");
        }
        memoTxt.append("$").append(doubleString(new Double(delegate.getAmount()), DOUBLE_FORMAT));
      }

      MemoInfo memo = new MemoInfo(delegate.getBan(),
                                   ACTIVATION_CREDIT_MEMO_TYPE,
                                   delegate.getSubscriberId(),
                                   delegate.getProductType(),
                                   memoTxt.toString());
      provider.getAccountLifecycleFacade().asyncCreateMemo(memo, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
     
    } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
  }

  public void memo() throws TelusAPIException {
    try{
      this.memo(false);
    } catch (Throwable e) {
      throw new TelusAPIException(e);
    }
  }
  public void memo(int ban, String subscriberId) throws TelusAPIException {
    try{
      delegate.setBan(ban);
      delegate.setSubscriberId(subscriberId);
      this.memo();
    } catch (Throwable e) {
      throw new TelusAPIException(e);
    }
  }
  public void memo(int ban) throws TelusAPIException {
    try{
      delegate.setBan(ban);
      this.memo();
    } catch (Throwable e) {
      throw new TelusAPIException(e);
    }
  }

/*
  private void validateBanSubscriber() throws TelusAPIException {
    if(delegate.getBan() == 0 ||
      (delegate.getSubscriberId() == null || delegate.getSubscriberId().length() == 0))
      throw new TelusAPIException("BAN and Subscriber ID missing");
  }
*/
  private void validateBan() throws TelusAPIException {
    if(delegate.getBan() == 0)
      throw new TelusAPIException("BAN missing");
  }

  private String doubleString(Double dblValue, String dblFormat)
  {
    if (dblValue == null || dblFormat == null) return "";
    DecimalFormat formatter = new DecimalFormat (dblFormat);
    return formatter.format(dblValue);
  }
  private static String dateString(Date dtValue, String dtFormat)
  {
    if (dtValue == null || dtFormat == null) return "";
    SimpleDateFormat formatter = new SimpleDateFormat (dtFormat);
    return formatter.format(dtValue);
  }

//  private void createManualLetterRequest(Date effectiveDate, boolean subscriberLevel) throws TelusAPIException
//  {
//    if ( ! subscriberLevel ) {
//      /* 
//       * below lmsCategory and lmsLetterCode is good for subsriber level only
//       * until we have new codes
//       */
//      return;
//    }
//    
//    try
//    {
//      String language = provider.getAccountInformationHelper().retrieveLwAccountByBan(delegate.getBan()).getLanguage();
//
//      String lmsCategory = null;
//      String lmsLetterCode = null;
//
//      if (language.trim().equalsIgnoreCase("FR")) {
//        lmsCategory = LMS_CONTRACT_RENEWAL_CATEGORY;
//        lmsLetterCode = LMS_CONTRACT_RENEWAL_LETTERCODE_FRENCH;
//      }
//      else {
//        lmsCategory = LMS_CONTRACT_RENEWAL_CATEGORY;
//        lmsLetterCode = LMS_CONTRACT_RENEWAL_LETTERCODE_ENGLISH;
//      }
//
//      LMSVariableInfo[] variables = new LMSVariableInfo[1];
//      variables[0] = new LMSVariableInfo("VALUE_PHONE_CREDIT", doubleString(new Double(getAmount()), DOUBLE_FORMAT));
//
//      LMSRequestInfo lmsRequestInfo = new LMSRequestInfo();
//      lmsRequestInfo.setBan(delegate.getBan());
//      lmsRequestInfo.setCategory(lmsCategory);
//      lmsRequestInfo.setLetterCode(lmsLetterCode);
//      lmsRequestInfo.setProductionDate(effectiveDate);
//      
//      if(delegate.getSubscriberId() != null && delegate.getSubscriberId().length() > 0)
//        lmsRequestInfo.setSubscriberId(delegate.getSubscriberId());
//
//      lmsRequestInfo.setVariables(variables);
//	  provider.getAccountLifecycleManager().createManualLetterRequest(lmsRequestInfo,
//		  SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
//      
//    }
//    catch (Throwable e) {
//    	provider.getExceptionHandler().handleException(e);
//    }
//  }
}
