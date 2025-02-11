/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Memo;
import com.telus.api.util.SessionUtil;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;


public class TMMemo extends BaseProvider implements Memo {
  /**
   * @link aggregation
   */
  private final MemoInfo delegate;

  public TMMemo(TMProvider provider, MemoInfo delegate) {
    super(provider);
    this.delegate = delegate;
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public int getBanId() {
    return delegate.getBanId();
  }

  public String getMemoType() {
    return delegate.getMemoType();
  }

  public void setMemoType(String newMemoType) {
    delegate.setMemoType(newMemoType);
  }

  public String getSubscriberId() {
    return delegate.getSubscriberId();
  }

  public String getProductType() {
    return delegate.getProductType();
  }

  public void setProductType(String newProductType) {
    delegate.setProductType(newProductType);
  }

  public String getText() {
    return delegate.getText();
  }

  public void setText(String newText) {
    delegate.setText(newText);
  }

  public String getSystemText() {
    return delegate.getSystemText();
  }

  public Date getDate() {
    return delegate.getDate();
  }

  public Date getModifyDate()  {
    return delegate.getModifyDate() ;
  }


  public void setDate(Date newDate) {
    delegate.setDate(newDate);
  }

  /*
  public double getAmount() {
    return delegate.getAmount();
  }

  public void setAmount(double newAmount) {
    delegate.setAmount(newAmount);
  }
  */

 public int getOperatorId() {
     return delegate.getOperatorId();
 }
 
 public double getMemoId() {
    return delegate.getMemoId();
}

// public void setOperatorId(int operatorId) {
//     delegate.setOperatorId(operatorId);
// }

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }

  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------
  public void create() throws TelusAPIException {
	  create( false );
  }
  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------
  public void create(boolean async ) throws TelusAPIException {
	  try {
		  if ( async ) {
			  provider.getAccountLifecycleFacade().asyncCreateMemo(delegate, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
		  } else {
			  provider.getAccountLifecycleManager().createMemo(delegate, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
		  }
	  } catch (Throwable e){
		  provider.getExceptionHandler().handleException(e);
	  }
  }

  public void save() throws TelusAPIException {
	  try {
		  if (MEMO_TYPE_SPECIAL_INSTRUCTIONS.equals(getMemoType())) {
			  provider.getAccountLifecycleManager().updateSpecialInstructions(getBanId(), getText(), SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
		  } else {
			  throw new TelusAPIException("only special instuction memos (" + MEMO_TYPE_SPECIAL_INSTRUCTIONS + ") can be updated");
		  }
	  } catch (Throwable e){
		  provider.getExceptionHandler().handleException(e);
	  }
  }

}




