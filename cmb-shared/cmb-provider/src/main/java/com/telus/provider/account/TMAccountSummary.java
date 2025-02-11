package com.telus.provider.account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.BillNotificationContact;
import com.telus.api.account.Charge;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.Credit;
import com.telus.api.account.Discount;
import com.telus.api.account.FollowUp;
//import com.telus.api.account.LMSLetterRequest;
import com.telus.api.account.LightWeightSubscriber;
import com.telus.api.account.Memo;
import com.telus.api.account.PhoneNumberSearchOption;
//import com.telus.api.account.SearchResults;
import com.telus.api.account.Subscriber;
import com.telus.api.account.TaxExemption;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSubscriberException;
//import com.telus.api.reference.Letter;
import com.telus.api.reference.SubscriptionRoleType;
import com.telus.api.util.SessionUtil;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
//import com.telus.eas.account.info.LMSLetterRequestInfo;
//import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.CDPDSubscriberInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.PCSSubscriberInfo;
import com.telus.eas.subscriber.info.PagerSubscriberInfo;
import com.telus.eas.subscriber.info.TangoSubscriberInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.Logger;


public class TMAccountSummary extends BaseProvider implements AccountSummary {

	private static final long serialVersionUID = 1L;
	/**
	 * @link aggregation
	 */
	private AccountInfo delegate;
	private TMAccount account;
	private Vector statusActivityReasonCodes = new Vector();
	private String corporateName;
	private AccountInformationHelper accountInformationHelper;
	private AccountLifecycleManager accountLifecycleManager;

	public TMAccountSummary(TMProvider provider, AccountInfo delegate) {
		super(provider);
		this.delegate = delegate;
		statusActivityReasonCodes.add("SFI");
		statusActivityReasonCodes.add("SNP");
		statusActivityReasonCodes.add("SNP1");
		accountInformationHelper = provider.getAccountInformationHelper();
		accountLifecycleManager = provider.getAccountLifecycleManager();
	}

  
  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public int getBanId() {
    return delegate.getBanId();
  }

  public int getCustomerId() {
    return delegate.getCustomerId();
  }

	public String getDealerCode() {
		return delegate.getDealerCode();
	}

	public String getSalesRepCode() {
		return delegate.getSalesRepCode();
	}

	public void setSalesRepCode(String salesRepCode) {
		delegate.setSalesRepCode(salesRepCode);
	}

	public void setDealerCode(String dealerCode) {
		delegate.setDealerCode(dealerCode);
	}

  public char getStatus() {
    return delegate.getStatus();
  }

  public Date getStatusDate() {
     return delegate.getStatusDate();
   }

  public String getFullName() {
    return delegate.getFullName();
  }

  public String[] getFullAddress() {
    return delegate.getFullAddress();
  }

  public boolean isAutotel() {
    return delegate.isAutotel();
  }

  public boolean isBrand(int brandId) {
	  return delegate.isBrand(brandId);
  }

  public boolean isIDEN() {
    return delegate.isIDEN();
  }

  public boolean isPCS() {
    return delegate.isPCS();
  }

  public boolean isPager() {
    return delegate.isPager();
  }

  public boolean isPagerPrepaidConsumeraccount(){
    return delegate.isPagerPrepaidConsumeraccount();
  }

  public boolean isPCSPostpaidCorporateRegularAccount(){
    return delegate.isPCSPostpaidCorporateRegularAccount();
  }


  public boolean isPostpaidEmployee() {
    return delegate.isPostpaidEmployee();
  }


  public boolean isPostpaidConsumer() {
    return delegate.isPostpaidConsumer();
  }

  public boolean isPostpaidBoxedConsumer() {
    return delegate.isPostpaidBoxedConsumer();
  }


  public boolean isPostpaidBusinessRegular() {
    return delegate.isPostpaidBusinessRegular();
  }

  public boolean isPostpaidBusinessPersonal() {
    return delegate.isPostpaidBusinessPersonal();
  }

  public boolean isPostpaidBusinessDealer() {
    return delegate.isPostpaidBusinessDealer();
  }

  public boolean isPostpaidOfficial(){
    return delegate.isPostpaidOfficial();
  }

  public boolean isPostpaidBusinessOfficial(){
     return delegate.isPostpaidBusinessOfficial();
   }

  public boolean isPrepaidConsumer() {
    return delegate.isPrepaidConsumer();
  }

  public boolean isQuebectelPrepaidConsumer() {
    return delegate.isQuebectelPrepaidConsumer();
  }

  public boolean isWesternPrepaidConsumer() {
    return delegate.isWesternPrepaidConsumer();
  }

  public boolean isSuspendedDueToNonPayment() {

    if (getStatus() == AccountSummary.STATUS_SUSPENDED) {
      if (statusActivityReasonCodes.contains(getStatusActivityReasonCode().trim())) {
          return true;
      }
    }

    return false;
  }

  public boolean isCorporate() {
    return delegate.isCorporate();
  }

  public boolean isCorporateRegular() {
    return delegate.isCorporateRegular();
  }

  public boolean isCorporateRegional() {
    return delegate.isCorporateRegional();
  }

  public boolean isCorporatePrivateNetworkPlus() {
    return delegate.isCorporatePrivateNetworkPlus();
  }
  
  public boolean isPostpaid() {
    return delegate.isPostpaid();
  }

  public char getAccountType() {
    return delegate.getAccountType();
  }

  public char getAccountSubType() {
    return delegate.getAccountSubType();
  }

  public String getPin() {
    return delegate.getPin();
  }

  public Date getCreateDate() {
    return delegate.getCreateDate();
  }

  public Date getStartServiceDate() {
    return delegate.getStartServiceDate();
  }

  public String getStatusActivityCode() {
    return delegate.getStatusActivityCode();
  }

  public String getStatusActivityReasonCode() {
    return delegate.getStatusActivityReasonCode();
  }

  public boolean isGSTExempt(){
    return delegate.isGSTExempt();
  }

  public boolean isPSTExempt(){
    return delegate.isPSTExempt();
  }

  public boolean isHSTExempt(){
    return delegate.isHSTExempt();
  }

  public boolean isCorporateHierarchy() {
    return delegate.isCorporateHierarchy();
  }

  public String getCorporateAccountRepCode() {
  return delegate.getCorporateAccountRepCode();
  }


  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }

  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------
  public final void assertAccountExists() throws UnknownBANException {
    if (getBanId() == 0) {
      throw new UnknownBANException("This Account has not yet been created");
    }
  }

  /**
   * Called after retieve and save to ensure objects are in correct state.
   */
  public void commit() {
  }

  public Account getAccount() throws TelusAPIException {
    return getAccount0();
  }

  public TMAccount getAccount0() throws TelusAPIException {
    if (account == null) {
      account = (TMAccount)provider.getAccountManager().findAccountByBAN(getBanId());
      delegate = account.getDelegate0();
    }
    return account;
  }

  public Subscriber[] getSubscribers(int maximum) throws TelusAPIException {
    assertAccountExists();
    Subscriber[] subscribers = provider.getAccountManager0().findSubscribersByBAN(getBanId(), maximum);
    for (int i = 0; i < subscribers.length; i++) {
      TMSubscriber s = (TMSubscriber)subscribers[i];
      s.setAccountSummary(this);
    }
    return subscribers;
  }

   public Subscriber getSubscriber(String subscriberId) throws  UnknownSubscriberException, TelusAPIException {
    assertAccountExists();
    try {
    	Subscriber subscriberInfo = provider.getSubscriberLifecycleHelper().retrieveSubscriber(getBanId(), subscriberId);
		return decorate(subscriberInfo ,true);
    }catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
	}
    return null;
   }

   public Subscriber getSubscriberByPhoneNumber(String phoneNumber) throws UnknownSubscriberException, TelusAPIException {
    assertAccountExists();
    try {
    	Subscriber subscriberInfo = provider.getSubscriberLifecycleHelper().retrieveSubscriberByBanAndPhoneNumber(getBanId(), phoneNumber);
		return decorate(subscriberInfo ,true);
    }catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
	}
    return null;
  }
   
   public Subscriber getSubscriberByPhoneNumber(String phoneNumber,PhoneNumberSearchOption phoneNumberSearchOption) throws UnknownSubscriberException, TelusAPIException {
	    assertAccountExists();
	    try {
	    	Subscriber subscriberInfo = provider.getSubscriberLifecycleHelper().retrieveSubscriberByBanAndPhoneNumber(getBanId(),phoneNumber, ((TMPhoneNumberSearchOption)phoneNumberSearchOption).getDelegate());
			return decorate(subscriberInfo ,true);
	    }catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	    return null;
	  }
  
  public Subscriber[] getSubscribers(int maximum, boolean includeCancelled) throws TelusAPIException
  {
    assertAccountExists();
    Subscriber[] subscribers = provider.getAccountManager0().findSubscribersByBAN(getBanId(), maximum, includeCancelled);
    for (int i = 0; i < subscribers.length; i++) {
      TMSubscriber s = (TMSubscriber)subscribers[i];
      s.setAccountSummary(this);
    }
    return subscribers;
  }

  
  public Subscriber[] getPortedSubscribers(int maximum) throws TelusAPIException {
	  
	  assertAccountExists();
	  Subscriber[] subscribers = provider.getAccountManager0().findPortedSubscribersByBAN(getBanId(), maximum);
	  for (int i = 0; i < subscribers.length; i++) {
		  TMSubscriber s = (TMSubscriber)subscribers[i];
		  s.setAccountSummary(this);
	  }
	  
	  return subscribers;
	  
  }
  
  public String [] getActiveSubscriberPhoneNumbers(int maxNumbers) throws TelusAPIException {
    /*
    assertAccountExists();
    try {
      return provider.getAccountHelperEJB().retrieveActiveSubscriberPhoneNumbers(getBanId(), maxNumbers);
    } catch (Throwable e) {
      throw new TelusAPIException(e);
    }
    */
    return getSubscriberPhoneNumbersByStatus(Subscriber.STATUS_ACTIVE, maxNumbers);
  }

  public String[] getSuspendedSubscriberPhoneNumbers(int maxNumbers) throws TelusAPIException
  {
	/*
    assertAccountExists();
    try {
      return provider.getAccountHelperEJB().retrieveSuspendedSubscriberPhoneNumbers(getBanId(), maxNumbers);
    } catch (Throwable e) {
      throw new TelusAPIException(e);
    }
    */
    return getSubscriberPhoneNumbersByStatus(Subscriber.STATUS_SUSPENDED, maxNumbers);
  }

  public String[] getSubscriberPhoneNumbersByStatus(char status, int maximum) throws TelusAPIException
  { 
	String[] phoneNumbers=null; 
    assertAccountExists();
    try {
		List list = accountInformationHelper.retrieveSubscriberPhoneNumbersByStatus(getBanId(), status, maximum);
		phoneNumbers =(String[])list.toArray(new String[list.size()]);
    } catch (Throwable e) {
    	 provider.getExceptionHandler().handleException(e);
    }
    return phoneNumbers;
  }

  private boolean getBlockRuleStatus(){
		boolean block=false;
		try{
			block=provider.getAccountLifecycleFacade().isEnterpriseManagedData(getBrandId(), getAccountType(),
					getAccountSubType(), delegate.getEvaluationProductType(), AccountSummary.PROCESS_TYPE_ACCOUNT_UPDATE);
		} catch (Throwable e) {
			Logger.warning("Error while executing EnterpriseManagedData block rule : "+e);
		}
		return block;
	}


  public void savePin(String newPin) throws UnknownBANException, TelusAPIException {
    assertAccountExists();
    try {
    	if(isBlockDirectUpdate()){
			  if(getBlockRuleStatus())
		      		 throw new TelusAPIException("Direct update password for ban[" +getBanId()+"] AccountType["+getAccountType()+"] AccountSubType["+
								  getAccountSubType()+"] BrandId["+getBrandId()+"] is blocked. This action should go to enterprise service");
		} 
    	
		 provider.getAccountLifecycleFacade().updateAccountPassword(getBanId(), newPin, 
						  SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
		 delegate.setPin(newPin);
    } catch (Throwable e) {
    	 provider.getExceptionHandler().handleException(e);
    }
  }
  
  


  public Memo getLastCreditCheckMemo() throws TelusAPIException {
    assertAccountExists();
    MemoInfo info =null;
    try {
    	 info = accountInformationHelper.retrieveLastCreditCheckMemoByBan(getBanId());
    } catch (Throwable e) {
    	 provider.getExceptionHandler().handleException(e);
    }
    return new TMMemo(provider, info);
  }

  private TMMemo newMemo0() {
    MemoInfo info = new MemoInfo();
    info.setBanId(getBanId());
    info.setProductType(delegate.getEvaluationProductType());
    info.setOperatorId(Integer.parseInt(provider.getUser()));
    return new TMMemo(provider, info);
  }

  public Memo newMemo() throws TelusAPIException {
    assertAccountExists();
    return newMemo0();
  }

  public FollowUp newFollowUp() throws TelusAPIException {
    assertAccountExists();
    FollowUpInfo info =  new FollowUpInfo();
    info.setBanId(getBanId());
    info.setProductType(delegate.getEvaluationProductType());
    info.setOperatorId(Integer.parseInt(provider.getUser()));
    return new TMFollowUp(provider, info);
  }

  public Charge newCharge() throws TelusAPIException{
    assertAccountExists();
    ChargeInfo info =  new ChargeInfo();
    info.setBan(getBanId());
    info.setProductType(delegate.getEvaluationProductType());
    info.setPrepaid(!isPostpaid());
    return new TMCharge(provider, info);
  }

  public Credit newCredit() throws TelusAPIException{
    return newCredit(false);
  }

  public Credit newCredit(boolean taxable) throws TelusAPIException{
      return taxable ? newCredit(Credit.TAX_OPTION_ALL_TAXES) : newCredit(Credit.TAX_OPTION_NO_TAX);
  }

  public Credit newCredit(char taxOption) throws TelusAPIException{
      assertAccountExists();
      CreditInfo info =  new CreditInfo(taxOption);
      info.setBan(getBanId());
      info.setProductType(delegate.getEvaluationProductType());
      info.setPrepaid(!isPostpaid());
      info.getTaxSummary().setProvince(getAccount().getAddress().getProvince());
      return new TMCredit(provider, info, this);
    }

  public Discount newDiscount() throws TelusAPIException{
    assertAccountExists();
    DiscountInfo info =  new DiscountInfo();
    info.setBan(getBanId());
    info.setProductType(delegate.getEvaluationProductType());
    return new TMDiscount(provider, info);
  }

  public Memo getLastMemo(String memoType) throws TelusAPIException {
    assertAccountExists();
    MemoInfo info =null;
    try {
    	 info = accountInformationHelper.retrieveLastMemo(getBanId(), memoType);
    } catch (Throwable e) {
    	 provider.getExceptionHandler().handleException(e);
    }
    return new TMMemo(provider, info);
  }

  public Memo getSpecialInstructionsMemo() throws TelusAPIException {
    assertAccountExists();
    MemoInfo info =null;
    try {
  		info = accountInformationHelper.retrieveLastMemo(getBanId(), Memo.MEMO_TYPE_SPECIAL_INSTRUCTIONS);
      
    	if (info != null) {
            return new TMMemo(provider, info);
          }
    } catch (ApplicationException e){
    	if (!ErrorCodes.MEMO_NOT_FOUND.equals(e.getErrorCode())) 
    		provider.getExceptionHandler().handleException(e);  
    }

    // Return a new instance when no memo exists.
    Memo m = newMemo0();
    m.setMemoType(Memo.MEMO_TYPE_SPECIAL_INSTRUCTIONS);
    return m;
  }

  public void createMemo(String memoType, String text) throws TelusAPIException {
    assertAccountExists();

    Memo memo = newMemo();

    memo.setMemoType(memoType);
    memo.setText(text);

    memo.create();
  }

  public void createFollowUp(String followUpType, String assignedTo, String text) throws TelusAPIException {
    assertAccountExists();

    FollowUp followUp = newFollowUp();

    followUp.setFollowUpType(followUpType);
    followUp.setAssignedToWorkPositionId(assignedTo);
    followUp.setText(text);

    followUp.create();
  }

  /**
    * Returns in chronological order of the last <code>count</code> number of memos for this account
    * (or one of its subscribers).
    *
    * <P>This method may involve a remote method call.
    *
    * @return Memo[] -- Never null.
    */
  public Memo[] getMemos(int count) throws TelusAPIException {
    assertAccountExists();
    Memo[] memo=null;
    try {
      // TODO: decorate
		List list = accountInformationHelper.retrieveMemos(getBanId(), count);
		memo=(MemoInfo[])list.toArray(new MemoInfo[list.size()]);

    } catch (Throwable e) {
    	 provider.getExceptionHandler().handleException(e);
    }
    return memo;
  }

  /**
    * Returns in chronological order the last <code>count</code> number of followups for this account
    * (or one of its subscribers).
    *
    * <P>This method may involve a remote method call.
    *
    * @return FollowUp[] -- Never null.
    */
  public FollowUp[] getFollowUps(int count) throws TelusAPIException {
    assertAccountExists();
    TMFollowUp[] followUps = null;
    try {
    	FollowUpInfo[] followUpInfos =null;
		List list = accountInformationHelper.retrieveFollowUps(getBanId(), count);
		followUpInfos=(FollowUpInfo[])list.toArray(new FollowUpInfo[list.size()]);

      if (followUpInfos != null) {
        int followUpInfosSize = followUpInfos.length;

        followUps = new TMFollowUp[followUpInfosSize];

        for (int i = 0; i < followUpInfosSize; i++)
          followUps[i] = new TMFollowUp(provider, followUpInfos[i]);
      }
    } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
    return followUps;
  }

  public ConsumerName[] getAuthorizedNames() throws TelusAPIException {
    assertAccountExists();
    ConsumerName[] consumerNames=null;
    try {
    	consumerNames = accountInformationHelper.retrieveAuthorizedNames(getBanId());

    } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
    return consumerNames;
  }

  public void saveAuthorizedNames(ConsumerName[] authorizedNames) throws UnknownBANException, TelusAPIException {
    assertAccountExists();
    try {
      ConsumerNameInfo[] info = new ConsumerNameInfo[authorizedNames.length];
      System.arraycopy(authorizedNames, 0, info, 0, authorizedNames.length);
	  accountLifecycleManager.updateAuthorizationNames(getBanId(),info,SessionUtil.getSessionId(accountLifecycleManager));
//      provider.getInteractionManager0().changeAuthorizedNames(this, oldPaymentMethod.getPaymentMethod().charAt(0), newPaymentMethod.getPaymentMethod().charAt(0));
//      delegate.getAuthorizedNames0().copyFrom(info); or set cached value to null
    } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
  }


  public TaxExemption getTaxExemption()throws TelusAPIException {
    return new TMAccountTaxExemption(provider, getAccount0().getDelegate0());
  }

//  public TMLMSLetterRequest newLMSLetterRequest(Letter letter, TMSubscriber subscriber) throws TelusAPIException {
//    LMSLetterRequestInfo info = new LMSLetterRequestInfo();
//
//    info.setLetterCode(letter.getCode());
//    info.setLetterCategory(letter.getLetterCategory());
//    info.setLetterVersion(letter.getLetterVersion());
//    info.setOperatorId(provider.getUser());
//    info.setProductionDate(new Date());
//    info.setSubmitDate(new Date());
//    info.setBanId(getBanId());
//    if (subscriber != null) {
//      info.setSubscriberId(subscriber.getSubscriberId());
////      info.setProductType(subscriber.getProductType().charAt(0));
//    }
//
//    return new TMLMSLetterRequest(provider, info);
//  }
//
//  public LMSLetterRequest newLMSLetterRequest(Letter letter) throws TelusAPIException {
//    return newLMSLetterRequest(letter, null);
//  }
//
//  public SearchResults getLMSLetterRequests(Date from, Date to, char level, String subscriberId, int maximum) throws TelusAPIException {
//	  SearchResultsInfo results =null;
//	  try {
//
//		  results = accountInformationHelper.retrieveLetterRequests(getBanId(), from, to, level, subscriberId, maximum);
//		  LMSLetterRequestInfo[] info = (LMSLetterRequestInfo[])results.getItems();
//		  TMLMSLetterRequest[] decorated = new TMLMSLetterRequest[info.length];
//
//		  for (int i = 0; i < decorated.length; i++)
//			  decorated[i] = new TMLMSLetterRequest(provider, info[i]);
//
//		  results.setItems(decorated);
//
//	  } catch (Throwable e) {
//    	  provider.getExceptionHandler().handleException(e);
//    }
//    return results;
//  }

  public String getHotlinedSubscriberPhoneNumber() throws TelusAPIException {
	  assertAccountExists();
	  String phoneNumber=null;
	  try {
		  phoneNumber = accountInformationHelper.retrieveHotlinedSubscriberPhoneNumber(getBanId());
	  } catch (Throwable e) {
		  provider.getExceptionHandler().handleException(e);
	  }
	  return phoneNumber;
  }

  //-----------------------------------------------------------------------------------
  // Decorators.
  //-----------------------------------------------------------------------------------
  private Subscriber decorate(Subscriber subscriber, boolean existingSubscriber) throws TelusAPIException, UnknownBANException {
    if (subscriber.isIDEN()) {
      return new TMIDENSubscriber(provider, (IDENSubscriberInfo) subscriber, !existingSubscriber, null, getAccount0(), false);
    }
    else if (subscriber.isPCS()) {
      return new TMPCSSubscriber(provider, (PCSSubscriberInfo) subscriber, !existingSubscriber, null, getAccount0(), false);
    }
    else if (subscriber.isPager()) {
      return new TMPagerSubscriber(provider, (PagerSubscriberInfo) subscriber, !existingSubscriber, null, getAccount0(), false);
    }
    else if (subscriber.isTango()) {
      return new TMTangoSubscriber(provider, (TangoSubscriberInfo) subscriber, !existingSubscriber, null, getAccount0(), false);
    }
    else if (subscriber.isCDPD()) {
      return new TMCDPDSubscriber(provider, (CDPDSubscriberInfo) subscriber, !existingSubscriber, null, getAccount0(), false);
    }
    else throw new TelusAPIException("Unknown subscriber product type: " + subscriber.getProductType());
  }

  public boolean isFidoConversion() {
    return delegate.isFidoConversion();
  }

  public SubscriptionRoleType[] getValidSubscriptionRoleTypes()throws TelusAPIException{
    SubscriptionRoleType[] subscriptionRoleTypes = provider.getReferenceDataManager().getSubscriptionRoleTypes();

    if(!(isCorporate())) {
         ArrayList arrayList = new ArrayList(subscriptionRoleTypes.length);
       for(int i = 0; i < subscriptionRoleTypes.length; i++){
          if(subscriptionRoleTypes[i].getCode().equals(SubscriptionRoleType.SUBSCRIPTION_ROLE_ENTERPRISE)){
            //don't add the Subscription Role
          }else{
            arrayList.add(subscriptionRoleTypes[i]);
          }
       }
      return (SubscriptionRoleType[]) arrayList.toArray(new SubscriptionRoleType[arrayList.size()]);
    }
    return subscriptionRoleTypes;
  }

  public String getCorporateName() throws TelusAPIException {

    if (corporateName != null)
      return corporateName;

    String idStr = delegate.getCorporateId();

    if (idStr == null)
      return "";

    try {
      int id = Integer.parseInt(idStr.trim());
      Logger.debug("First time retrieval of corporate name ...");
      corporateName = accountInformationHelper.retrieveCorporateName(id);
    } catch(Throwable t) {
    	 provider.getExceptionHandler().handleException(t);
    }
    return corporateName;
  }

  public boolean isPostpaidCorporatePersonal() {
    return delegate.isPostpaidCorporatePersonal();
  }

  public boolean isPostpaidCorporateRegular() {
    return delegate.isPostpaidCorporateRegular();
  }

  public int getBrandId() {
    return delegate.getBrandId();
  }

  public boolean isAmpd() {
	  return delegate.isAmpd();
  }
  
  public String getPortProtectionIndicator () throws TelusAPIException {

	  try {
		  return  provider.getSubscriberLifecycleHelper().getPortProtectionIndicator(getBanId(), null, null, null);
	  } catch (Throwable t) {
		  provider.getExceptionHandler().handleException(t);
	  }
	  return null;
  }
  
  public void updatePortRestriction(boolean restrictPort)  throws TelusAPIException {
  	
  	 try {
  		  provider.getSubscriberManagerBean().updatePortRestriction(getBanId(), null, restrictPort, provider.getUser());
	  } catch (Throwable e) {
		  provider.getExceptionHandler().handleException(e);
	  }
  	
    }
   
  public String getBanSegment() throws TelusAPIException {
	  return delegate.getBanSegment();
  }
  
  public String getBanSubSegment() throws TelusAPIException {
	  return delegate.getBanSubSegment();
  }
  
  private BillNotificationContact[] decorate(BillNotificationContactInfo[] infos) {
		TMBillNotificationContact[] tms = new TMBillNotificationContact[infos.length];
		TMBillNotificationContact tm = null;
		for (int i = 0; i < tms.length; i++) {
			tm = new TMBillNotificationContact(infos[i]);
			tms[i] = tm;
		}
		return tms;
	}
  /**
   * @deprecated  replaced by BillNotificationManagementService_v3_0.getBillNotificationInfo
   */
  public BillNotificationContact[] getBillNotificationContacts() throws TelusAPIException {
	  BillNotificationContactInfo[] infos = null;
	  try {
		  List list = accountInformationHelper.retrieveBillNotificationContacts(getBanId());
		  infos =(BillNotificationContactInfo[])list.toArray(new BillNotificationContactInfo[list.size()]);
	  } catch (Throwable e) {
		  provider.getExceptionHandler().handleException(e);
	  }
	  return decorate(infos);

  }
  /**
   * @deprecated  replace  - TBD – This is only consumed by Smart Desktop
   */
	public BillNotificationContact getLastEBillNotificationSent() throws TelusAPIException {

		BillNotificationContactInfo info = null;
		try {
			info = accountInformationHelper.getLastEBillNotificationSent(getBanId());
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return info;
	}
  /**
   * @deprecated replace - TBD – This is only consumed by Smart Desktop
   */
	public String getPaperBillSupressionAtActivationInd() throws TelusAPIException {
		String SupressionAtActivationInd = null;
		try {
			SupressionAtActivationInd = accountInformationHelper.getPaperBillSupressionAtActivationInd(getBanId());

		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return SupressionAtActivationInd;
	}


	public String[] getSubscriberIdsByStatus( char status, int maximum ) throws TelusAPIException {
		assertAccountExists();
		String[] subscriberIds=null;
		try {
			List list =  accountInformationHelper.retrieveSubscriberIdsByStatus(getBanId(), status, maximum);
			subscriberIds =(String[])list.toArray(new String[list.size()]);

		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return subscriberIds;
	}

	public LightWeightSubscriber[] getLightWeightSubscribers(int maximum, boolean includeCancelled ) throws TelusAPIException {
		
	    assertAccountExists();
		int lightWeightSubMax = AppConfiguration.getLightWeightSubMax();
		if ( maximum > lightWeightSubMax) {
			throw new LimitExceededException ("Maximum subscriber search limit["+lightWeightSubMax+"] exceeded: ("+ maximum +".");
		}
		
		if ( maximum<=0 ) 
			return new LightWeightSubscriber[0];
	    
		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrieveLightWeightSubscriberListByBAN( getBanId(), isIDEN(), maximum, includeCancelled );
			return (LightWeightSubscriber[] ) c.toArray( new LightWeightSubscriber[ c.size() ]);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}


	public String[] getSubscriberIdsByServiceGroupFamily(String familyType,Date effectiveDate )
			throws LimitExceededException, TelusAPIException {
		try{
			return provider.getAccountInformationHelper().retrieveSubscriberIdsByServiceFamily(delegate, familyType, effectiveDate);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}
	
	protected boolean isBlockDirectUpdate() {
		return AppConfiguration.isBlockDirectUpdate();
	}


	public boolean isPostpaidBusinessConnect() {
		return delegate.isPostpaidBusinessConnect();
	}
}
