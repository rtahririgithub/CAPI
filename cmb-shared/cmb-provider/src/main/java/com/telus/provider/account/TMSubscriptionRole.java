package com.telus.provider.account;

import com.telus.api.account.SubscriptionRole;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;


public class TMSubscriptionRole extends BaseProvider implements SubscriptionRole {

	private static final long serialVersionUID = 1L;
	private SubscriptionRoleInfo delegate;
	
	public TMSubscriptionRole(TMProvider provider, SubscriptionRoleInfo delegate)
  {
    super(provider);
    this.delegate = delegate;
  }

  //--------------------------------------------------------------------

  //  Decorative Methods

  //--------------------------------------------------------------------

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }

  public String getCode()
  {
    return delegate.getCode();
  }

  public void  setCode(String code)
  {
    this.setModified(true);
    delegate.setCode(code);
  }

  public String getDealerCode()
  {
    return delegate.getDealerCode();
  }

  public void  setDealerCode(String dealerCode)
  {
    this.setModified(true);
    delegate.setDealerCode(dealerCode);
  }

  public String getSalesRepCode()
  {
    return delegate.getSalesRepCode();
  }

  public void  setSalesRepCode(String salesRepCode)
  {
    this.setModified(true);
    delegate.setSalesRepCode(salesRepCode);
  }

  public String getCsrId()
  {
    return delegate.getCsrId();
  }

  public void  setCsrId(String csrId)
  {
    this.setModified(true);
    delegate.setCsrId(csrId);
  }

  public boolean isModified()
  {
    return delegate.isModified();
  }

  public void setModified(boolean modified)
  {
      delegate.setModified(modified);
  }

  
}
