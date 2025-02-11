package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.FeeWaiver;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.FeeWaiverInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TMFeeWaiver extends BaseProvider implements FeeWaiver {

  private FeeWaiverInfo delegate;
  
  public TMFeeWaiver(TMProvider provider, FeeWaiverInfo delegate) {
    super(provider);
    this.delegate = delegate;
  }

  public void add() throws TelusAPIException {
    try {
		if (FeeWaiverInfo.INSERT == delegate.getMode()) {
			provider.getAccountLifecycleManager().applyFeeWaiver(delegate, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
		}
      else {
        throw new TelusAPIException("FeeWaiver: Wrong mode. FeeWaiver exists, it cannot be inserted into table.");
      }

    } catch (Throwable t) {
    	provider.getExceptionHandler().handleException(t);
    }
  }

  public void delete() throws TelusAPIException {
    try {
      if (FeeWaiverInfo.INSERT != delegate.getMode()) {
        delegate.setMode(FeeWaiverInfo.DELETE);
        provider.getAccountLifecycleManager().applyFeeWaiver(delegate, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
      }
      else {
        throw new TelusAPIException("FeeWaiver: Wrong mode. FeeWaiver does not exists. It cannot be deleted.");
      }

    } catch (Throwable t) {
    	provider.getExceptionHandler().handleException(t);
    }
  }

  public void update() throws TelusAPIException {
    try {
      if (FeeWaiverInfo.INSERT != delegate.getMode()) {
        delegate.setMode(FeeWaiverInfo.UPDATE);
        provider.getAccountLifecycleManager().applyFeeWaiver(delegate, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
      }
      else {
        throw new TelusAPIException("FeeWaiver: Wrong mode. FeeWaiver does not exists. It cannot be updated.");
      }

    } catch (Throwable t) {
    	provider.getExceptionHandler().handleException(t);
    }
  }

  public String getReasonCode() {
    return delegate.getReasonCode();
  }

  public String getTypeCode() {
    return delegate.getTypeCode();
  }

  public Date getEffectiveDate() {
    return delegate.getEffectiveDate();
  }

  public Date getExpiryDate() {
    return delegate.getExpiryDate();
  }

  public void setEffectiveDate(Date effectiveDate) {
    delegate.setEffectiveDate(effectiveDate);
  }

  public void setExpiryDate(Date expiryDate) {
    delegate.setExpiryDate(expiryDate);
  }

  public void setReasonCode(String reasonCode) {
    delegate.setReasonCode(reasonCode);
  }

}
