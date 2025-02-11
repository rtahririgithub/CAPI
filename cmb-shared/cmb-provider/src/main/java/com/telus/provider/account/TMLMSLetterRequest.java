///*
// * $Id$
// * %E% %W%
// * Copyright (c) Telus Mobility Inc. All Rights Reserved.
// */
//
//package com.telus.provider.account;
//
//import java.util.Date;
//
//import com.telus.api.TelusAPIException;
//import com.telus.api.account.LMSLetterRequest;
//import com.telus.api.util.SessionUtil;
//import com.telus.eas.account.info.LMSLetterRequestInfo;
//import com.telus.provider.BaseProvider;
//import com.telus.provider.TMProvider;
//import com.telus.provider.util.AppConfiguration;
//
//public class TMLMSLetterRequest extends BaseProvider implements LMSLetterRequest {
//
//	public TMLMSLetterRequest(TMProvider provider) {
//		super(provider);
//		// TODO Auto-generated constructor stub
//	}
//  /**
//   * @link aggregation
//   */
////  private LMSLetterRequestInfo delegate;
////
////  public TMLMSLetterRequest(TMProvider provider, LMSLetterRequestInfo delegate) {
////    super(provider);
////    this.delegate = delegate;
////  }
////
////  public LMSLetterRequestInfo getDelegate() {
////    return delegate;
////  }
////
////  public int getId() {
////    return delegate.getId();
////  }
////
////  public String getOperatorId() {
////    return delegate.getOperatorId();
////  }
////
////  public String getLetterCategory() {
////    return delegate.getLetterCategory();
////  }
////
////  public int getLetterVersion() {
////    return delegate.getLetterVersion();
////  }
////
////  public String getLetterCode() {
////    return delegate.getLetterCode();
////  }
////
////  public String getSubscriberId() {
////    return delegate.getSubscriberId();
////  }
////
//////  public String getPhoneNumber() {
//////    return delegate.getPhoneNumber();
//////  }
////
////  public String getStatus() {
////    return delegate.getStatus();
////  }
////
////  public Date getSubmitDate() {
////    return delegate.getSubmitDate();
////  }
////
////  public Date getProductionDate() {
////    return delegate.getProductionDate();
////  }
////
////  public String getAllVariables() {
////    return delegate.getAllVariables();
////  }
////
////  public void setLetterCategory(String letterCategory) {
////    delegate.setLetterCategory(letterCategory);
////  }
////
////  public void setLetterCode(String letterCode) {
////    delegate.setLetterCode(letterCode);
////  }
////
////  public void setProductionDate(Date productionDate) {
////    delegate.setProductionDate(productionDate);
////  }
////
//////  public void setPhoneNumber(String phoneNumber) {
//////    delegate.setPhoneNumber(phoneNumber);
//////  }
////
////  public void setManualVariable(String name, String value) {
////    delegate.setManualVariable(name, value);
////  }
////
////  public void clearManualVariables() {
////    delegate.clearManualVariables();
////  }
////
////  //---------------------------------------------------------------------------
////
////	public void save() throws TelusAPIException {
////		if (AppConfiguration.isLMSAPIEnabled()) {
////			try {
////				provider.getAccountLifecycleManager().createManualLetterRequest(delegate, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
////			} catch (Throwable e) {
////				provider.getExceptionHandler().handleException(e);
////			}
////		} else {
////			throw new UnsupportedOperationException("This method has been decomissioned.");
////		}
////	}
////
////  @Deprecated
////  public void resend() throws TelusAPIException {
////    save();
////  }
////
////  @Deprecated
////	public void delete() throws TelusAPIException {
////		if (AppConfiguration.isLMSAPIEnabled()) {
////			try {
////				provider.getAccountLifecycleManager().removeManualLetterRequest(getDelegate().getBanId(), getDelegate().getId(), SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
////			} catch (Throwable e) {
////				provider.getExceptionHandler().handleException(e);
////			}
////		} else {
////			throw new UnsupportedOperationException("This method has been decomissioned.");
////		}
////	}
////	  
//
//
//}
