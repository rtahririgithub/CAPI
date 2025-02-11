/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Address;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.CreditCard;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.api.account.PrepaidConsumerAccount;
import com.telus.api.util.SessionUtil;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.CreditCardHolderInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;
import com.telus.provider.util.ProviderCreditCardExceptionTranslator;


public class TMCreditCard extends BaseProvider implements CreditCard {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @link aggregation
	 */
	//private final CreditCardInfo delegate;
	private  Address address;
	//private final CreditCardHolderInfo creditCardHolderInfo = new CreditCardHolderInfo();
	private final CreditCardTransactionInfo creditCardTransactionInfo = new CreditCardTransactionInfo();
	
	public TMCreditCard(TMProvider provider, CreditCardInfo delegate, Account account) {
		super(provider);
		creditCardTransactionInfo.setCreditCardInfo(delegate);
		copyCreditInformation(account);
	}



	public TMCreditCard(TMProvider provider, CreditCardInfo delegate) {
		this(provider, delegate, null);
		creditCardTransactionInfo.getCreditCardHolderInfo().setlastName(delegate.getHolderName());
	}




	public void copyCreditInformation(Account account){
		if (account != null) {
			address = account.getAddress();
			creditCardTransactionInfo.setBan(account.getBanId());
			//defect PROD00143130 fix: set the correct BrandId when we know it.
			creditCardTransactionInfo.setBrandId( account.getBrandId() );

			if (account.isPostpaidConsumer() || account.isPostpaidBusinessPersonal()) {
				creditCardTransactionInfo.getCreditCardHolderInfo().setFirstName( ( (PostpaidConsumerAccount) account).
						getName().getFirstName());
				creditCardTransactionInfo.getCreditCardHolderInfo().setlastName( ( (PostpaidConsumerAccount) account).
						getName().getLastName());
				//creditCardHolderInfo.setBirthDate( ( (PostpaidConsumerAccount) account).
				//                                  getCreditInformation().getBirthDate());
			}
			else if (account.isPrepaidConsumer()) {
				creditCardTransactionInfo.getCreditCardHolderInfo().setFirstName( ( (PrepaidConsumerAccount) account).
						getName().getFirstName());
				creditCardTransactionInfo.getCreditCardHolderInfo().setlastName( ( (PrepaidConsumerAccount) account).
						getName().getLastName());
				creditCardTransactionInfo.getCreditCardHolderInfo().setBirthDate( ( (PrepaidConsumerAccount) account).
						getBirthDate());
			}
			else {
				//CR for handling corporate account: not concatenate the first name / last name into holder's lastName
				//Let's try our best to separate the name fields, but for real corporate account only has one name as legal name.
				//Only PostpaidConsumerAccount has first/last name, so check the account class type.
				CreditCardHolderInfo holderInfo  = creditCardTransactionInfo.getCreditCardHolderInfo(); 
				if ( account instanceof PostpaidConsumerAccount ) {
					PostpaidConsumerAccount pca = (PostpaidConsumerAccount) account;
					holderInfo.setFirstName( pca.getName().getFirstName() );
					holderInfo.setlastName( pca.getName().getLastName() );
				} else {
					holderInfo.setlastName(account.getFullName());
				}
			}

			creditCardTransactionInfo.getCreditCardHolderInfo().setClientID("" +account.getBanId());
			creditCardTransactionInfo.getCreditCardHolderInfo().setPostalCode(account.getAddress().getPostalCode());
			creditCardTransactionInfo.getCreditCardHolderInfo().setActivationDate(account.getCreateDate());
			creditCardTransactionInfo.getCreditCardHolderInfo().setAccountType("" + account.getAccountType());
			creditCardTransactionInfo.getCreditCardHolderInfo().setAccountSubType("" + account.getAccountSubType());
		}else{
			creditCardTransactionInfo.getCreditCardHolderInfo().setAccountType("" + Account.ACCOUNT_TYPE_CONSUMER);
			creditCardTransactionInfo.getCreditCardHolderInfo().setAccountSubType("" + Account.ACCOUNT_SUBTYPE_PCS_PREPAID);
			creditCardTransactionInfo.getCreditCardHolderInfo().setActivationDate(new Date());
			creditCardTransactionInfo.getCreditCardHolderInfo().setPostalCode(getAddress().getPostalCode());
		}

	}
	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	/*  public String getNumber() {
    return creditCardTransactionInfo.getCreditCardInfo().getNumber();
    //return delegate.getNumber();
  }

  public void setNumber(String number) {
    creditCardTransactionInfo.getCreditCardInfo().setNumber(number);
    //delegate.setNumber(number);
  }
	 */
	public int getExpiryMonth() {
		return creditCardTransactionInfo.getCreditCardInfo().getExpiryMonth();
	}

	public void setExpiryMonth(int expiryMonth) {
		creditCardTransactionInfo.getCreditCardInfo().setExpiryMonth(expiryMonth);
	}

	public int getExpiryYear() {
		return creditCardTransactionInfo.getCreditCardInfo().getExpiryYear();
	}

	public void setExpiryYear(int expiryYear) {
		creditCardTransactionInfo.getCreditCardInfo().setExpiryYear(expiryYear);
	}

	public String getHolderName() {
		return creditCardTransactionInfo.getCreditCardInfo().getHolderName();
	}

	public void setHolderName(String holderName) {
		creditCardTransactionInfo.getCreditCardHolderInfo().setlastName(holderName);
		creditCardTransactionInfo.getCreditCardInfo().setHolderName(holderName);
	}

	public String getAuthorizationCode() {
		return creditCardTransactionInfo.getCreditCardInfo().getAuthorizationCode();
	}

	public void setAuthorizationCode(String authorizationCode) {
		creditCardTransactionInfo.getCreditCardInfo().setAuthorizationCode(authorizationCode);
	}

	public boolean isModified() {
		return creditCardTransactionInfo.getCreditCardInfo().isModified();
	}

	public void commit() {
		creditCardTransactionInfo.getCreditCardInfo().commit();
	}

	public boolean getNeedsValidation() {
		return creditCardTransactionInfo.getCreditCardInfo().getNeedsValidation();
	}

	public int hashCode() {
		return creditCardTransactionInfo.getCreditCardInfo().hashCode();
	}

	public String toString() {
		return creditCardTransactionInfo.getCreditCardInfo().toString();
	}


	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public CreditCardInfo getDelegate(){
		return creditCardTransactionInfo.getCreditCardInfo();
	}

	public void copyFrom(CreditCard o) {
		//defect PROD00177260, we have to be more flexible on this, to support both TMCreditCard and CreditCardInfo.
		if ( o instanceof TMCreditCard ) {
			creditCardTransactionInfo.getCreditCardInfo().copyFrom(((TMCreditCard)o).creditCardTransactionInfo.getCreditCardInfo());
		} else {
			//if not TMCreditCard, then we assume it's CreditCardInfo
			creditCardTransactionInfo.getCreditCardInfo().copyFrom( (CreditCardInfo) o );
		}
	}

	public boolean equals(CreditCard o) {
		return creditCardTransactionInfo.getCreditCardInfo().equals(((TMCreditCard)o).creditCardTransactionInfo.getCreditCardInfo());
	}

	public boolean equals(Object o) {
		return equals((CreditCard)o);
	}

	/* refactor the logic as part of PCI 
	 * public void validate(String reason, String businessRole) throws TelusAPIException, InvalidCreditCardException {
    //
    if (creditCardTransactionInfo.getCreditCardInfo().getNumber() == null) {
      throw new TelusAPIException("CreditCard Number is missing: " + reason);
    }
    getCreditCardTransactionInfo().getCreditCardHolderInfo().setBusinessRole(businessRole);
    if(creditCardTransactionInfo.getCreditCardHolderInfo().getPostalCode() == null){
      creditCardTransactionInfo.getCreditCardHolderInfo().setPostalCode(address.getPostalCode());
    }

    try {
      String authorizationNumber = provider.getAccountManagerEJB().validateCreditCard(creditCardTransactionInfo);
      //String authorizationNumber = provider.getAccountManagerEJB().validateCreditCard(delegate);
      provider.debug("authorizationNumber=" + authorizationNumber);
      commit();  // TODO: make sure we're (Peter) not using the modified flag later.
    } catch (TelusCreditCardException e){
      provider.checkServiceFailure(e);
      throw new InvalidCreditCardException(e, e.id, this, e.getCCardMessageEN(), e.getCCardMessageFR());
    } catch (TelusAPIException e){
      throw e;
    } catch (Throwable e){
      throw new TelusAPIException(e);
    }
  }
	 */
	public Address getAddress(){
		if(address != null){
			return address;
		}
		address = new TMAddress(provider, new AddressInfo());
		return address;
	}

	public CreditCardTransactionInfo getCreditCardTransactionInfo(){
		return creditCardTransactionInfo;
	}



	//--------------------------------------------------------------------
	//  PCI related changes
	//--------------------------------------------------------------------
	private boolean isValidated;

	private static final Pattern TOKEN_PATTERN =  Pattern.compile("\\d{21}");  
	private static final Pattern F6D_PATTERN  =  Pattern.compile("\\d{6}");  
	private static final Pattern L4D_PATTERN =  Pattern.compile("\\d{4}");  

	private boolean checkDigits( String value, Pattern pattern ) {
		Matcher m = pattern.matcher( value );
		return m.matches();
	}

	public void setToken(String token, String leadingDisplayDigits,	String trailingDispayDigits) throws TelusAPIException {

		clear();

		if ( token==null ) {
			//if token is null, leading / trailing digits must be null as well
			if ( leadingDisplayDigits!=null ) throw new TelusAPIException ( "token is null but leadingDisplayDigits is not null");
			if ( trailingDispayDigits!=null ) throw new TelusAPIException ( "token is null but trailingDispayDigits is not null");
		}
		else { //token is not null

			//validate token - 21 digits
			if ( checkDigits( token, TOKEN_PATTERN )==false )
				throw new TelusAPIException ( "token must be 21 digits");

			//validate leading digits - 6 digits
			if ( leadingDisplayDigits==null ) 
				throw new TelusAPIException ( "Missing leadingDisplayDigits");
			else if (checkDigits ( leadingDisplayDigits, F6D_PATTERN)==false) 
				throw new TelusAPIException ( "leadingDisplayDigits must be six digits");

			//validate trailing digits - 4 digits
			if (trailingDispayDigits==null )  
				throw new TelusAPIException ( "Missing trailingDispayDigits");
			else if ( checkDigits ( trailingDispayDigits, L4D_PATTERN)==false)  
				throw new TelusAPIException ( "trailingDispayDigits must be four digits");

			getDelegate().setToken(token, leadingDisplayDigits, trailingDispayDigits);
		}
	}

	public String getLeadingDisplayDigits() {
		return getDelegate().getLeadingDisplayDigits();
	}
	public String getToken() {
		return getDelegate().getToken();
	}
	public String getTrailingDisplayDigits() {
		return getDelegate().getTrailingDisplayDigits();
	}

	boolean hasToken( ) {
		return getDelegate().hasToken();
	}

	public void clear() {
		isValidated = false;
		getDelegate().clear();
	}


	public void setAuditHeader( AuditHeader auditHeader ) {
		creditCardTransactionInfo.setAuditHeader(auditHeader);
	}

	private void validateCard(String reason, String businessRole) throws TelusAPIException {
		if (creditCardTransactionInfo.getCreditCardInfo().hasToken() == false) {
			throw new TelusAPIException("CreditCard token is missing: " + reason);
		}

		// This check is to avoid multiple credit card validation call to
		// back-end;
		if (isValidated == true) {
			return;
		}

		getCreditCardTransactionInfo().getCreditCardHolderInfo().setBusinessRole(businessRole);
		if (creditCardTransactionInfo.getCreditCardHolderInfo().getPostalCode() == null) {
			creditCardTransactionInfo.getCreditCardHolderInfo().setPostalCode(address.getPostalCode());
		}
		CreditCardResponseInfo creditCardResponseInfo=new CreditCardResponseInfo();
		try {
			creditCardResponseInfo = provider.getAccountLifecycleFacade().validateCreditCard(creditCardTransactionInfo, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
			Logger.debug("authorizationNumber=" + creditCardResponseInfo.getAuthorizationCode());
			commit(); // TODO: make sure we're (Peter) not using the modified
			// flag later.
			isValidated = true;
		} catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderCreditCardExceptionTranslator(this);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
	}

	public void validate(String reason, String businessRole, AuditHeader auditHeader) throws TelusAPIException, InvalidCreditCardException {
		if ( auditHeader==null ) {
			throw new TelusAPIException ( "The required AuditHeader is missing for credit card transaction");
		}
		setAuditHeader(auditHeader);
		validateCard( reason, businessRole );
	}

	void validate(String reason, String businessRole) throws TelusAPIException, InvalidCreditCardException {

		if ( creditCardTransactionInfo.getAuditHeader()==null ) {
			setAuditHeader( provider.getAuditHeader() );
		}
		validateCard( reason, businessRole );
	}



	public void setCardVerificationData(String cardVerificationData) {
		creditCardTransactionInfo.getCreditCardInfo().setCardVerificationData(cardVerificationData);
	}

	public String getCardVerificationData() {
		return creditCardTransactionInfo.getCreditCardInfo().getCardVerificationData();
	}


}



