package com.telus.provider.account;

import com.telus.api.account.*;
import com.telus.provider.TMProvider;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;import com.telus.eas.account.info.PostpaidCorporateRegularAccountInfo;

/**
 * <p>Title: Client API</p>
 *
 * <p>Description: Client API</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Telus Mobility</p>
 *
 * @author Michael Qin
 * @version 1.0
 */
public class TMPostpaidCorporateRegularAccount
    extends TMPostpaidBusinessRegularAccount implements
    PostpaidCorporateRegularAccount {

  private final PostpaidCorporateRegularAccountInfo delegate;  private final TMPersonalCredit personalCredit;    
  public TMPostpaidCorporateRegularAccount(TMProvider provider,
                                           PostpaidCorporateRegularAccountInfo
                                           delegate) {
    super(provider, delegate);
    this.delegate = delegate;    personalCredit = new TMPersonalCredit(provider, delegate.getPersonalCreditInformation0(), this);    
  }
  
  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }//-------------------------------------------------//Service Methods//-------------------------------------------------  public PersonalCredit getPersonalCreditInformation() {  	return personalCredit;  }    
}
