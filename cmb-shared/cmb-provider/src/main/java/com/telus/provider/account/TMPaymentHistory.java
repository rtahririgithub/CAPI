/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.Cheque;
import com.telus.api.account.CreditCard;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.PaymentActivity;
import com.telus.api.account.PaymentHistory;
import com.telus.api.account.PaymentTransfer;
import com.telus.api.util.SessionUtil;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.PaymentActivityInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;
import com.telus.provider.util.ProviderCreditCardExceptionTranslator;

public class TMPaymentHistory extends BaseProvider implements PaymentHistory {
	
	/**
	 * @link aggregation
	 */
	private final PaymentHistoryInfo delegate;
	private int ban;
	private int brandId;
	
	public TMPaymentHistory(TMProvider provider, PaymentHistoryInfo delegate, int ban, int brandId) {
		super(provider);
		this.delegate = delegate;
		this.ban = ban;
		this.brandId = brandId;
	}
	
	public PaymentHistoryInfo getDelegate() {
		return delegate;
	}
	
	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
    public int getSeqNo() {
    	return delegate.getSeqNo();
    }

    public Date getDate() {
    	return delegate.getDate();
    }

    public String getPaymentMethodCode() {
    	return delegate.getPaymentMethodCode();
    }

    public String getPaymentMethodSubCode() {
    	return delegate.getPaymentMethodSubCode();
    }

    public String getSourceTypeCode() {
    	return delegate.getSourceTypeCode();
    }

    public String getSourceID() {
    	return delegate.getSourceID();
    }

    public double getOriginalAmount() {
    	return delegate.getOriginalAmount();
    }

    public double getAmountDue() {
    	return delegate.getAmountDue();
    }
    
    public Date getDepositDate() {
    	return delegate.getDepositDate();
    }
    
    public double getActualAmount() {
    	return delegate.getActualAmount();
    }
    
    public Date getBillDate() {
    	return delegate.getBillDate();
    }
    
    public String getActivityCode() {
    	return delegate.getActivityCode();
    }
    
    public String getActivityReasonCode() {
    	return delegate.getActivityReasonCode();
    }

    public boolean isBalanceIgnoreFlag() {
    	return delegate.isBalanceIgnoreFlag();
    }

    public CreditCard getCreditCard() {
    	return delegate.getCreditCard();
    }

    public Cheque getCheque() {
    	return delegate.getCheque();
    }
    
    public int getOriginalBanId() {
    	return delegate.getOriginalBanId();
    }
    
    public int getFileSequenceNumber() {
    	return delegate.getFileSequenceNumber();
    }
    
    public int getBatchNumber() {
    	return delegate.getBatchNumber();
    }
    
    public int getBatchLineNumber() {
    	return delegate.getBatchLineNumber();	
    }
	
	public String toString() {
		return delegate.toString();
	}
	
	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public PaymentActivity[] getPaymentActivities() throws TelusAPIException {
		PaymentActivity[] paymentActivities= null;
		try {
			List list = provider.getAccountInformationHelper().retrievePaymentActivities(ban, getSeqNo());
			paymentActivities= (PaymentActivityInfo[])list.toArray(new PaymentActivityInfo[list.size()]);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return paymentActivities;
	}

	public void transferPayment(PaymentTransfer[] paymentTransfers, boolean allowOverPayment, String memonText) throws TelusAPIException {
		try {
			provider.getAccountLifecycleManager().updateTransferPayment(ban, getSeqNo(), paymentTransfers,
					allowOverPayment, memonText, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}
  
	public boolean isPaymentBackedout() {
		return delegate.isPaymentBackedout();
	}

	public boolean isPaymentFullyTransferred() {
		return delegate.isPaymentFullyTransferred();
	}

	public boolean isPaymentRefunded() {
		return delegate.isPaymentRefunded();
	}

	public boolean isPaymentSufficient() {
		return delegate.isPaymentSufficient();
	}
	
  	public void refundPayment ( String businessRole, String reasonCode, String memoText, boolean isManual, AuditHeader auditHeader) throws InvalidCreditCardException, TelusAPIException {
  		
  		if ( getActualAmount()<=0 ) 
  			throw new TelusAPIException ( "The actual amount on this payment is not positive, refund cannot proceed");
  		
  		 //defect PROD00175842 fix, only base on the reason code, 
  		if (PaymentActivity.REFUND_REASON_CREDIT_CARD_REFUND.equals( reasonCode )
  			&& isManual == false //calling application want us to refund the credit card. 
  			) { 
  			
  			if ( auditHeader==null ) throw new TelusAPIException ("The required AuditHeader is missing for credit card transaction.");
  			
  			//defect PROD00175842 fix, since we only base on the reason code, we need to make sure that the payment does contain credit card information
  			if ( delegate.getCreditCard0()==null || delegate.getCreditCard0().hasToken()==false)
  				throw new TelusAPIException ("Missing credit card information for credit card payment refund. BAN(" + ban + "), paymentSeq(" + getSeqNo() +")" );
  			
  			CreditCardTransactionInfo ccTransactionInfo = new CreditCardTransactionInfo();
  			ccTransactionInfo.getCreditCardHolderInfo().setBusinessRole(businessRole);
  			ccTransactionInfo.setAuditHeader( provider.appendToAuditHeader(auditHeader) );
  			ccTransactionInfo.setBan(ban);
  			ccTransactionInfo.setBrandId(brandId);
  			ccTransactionInfo.setAmount( getActualAmount());
  			
  			//defect PROD00173138 fix, 
  			//Root cause:  TMPapymentHistory.getCreditCard() use to return CreditCardInfo,  with fix of PROD00172351, it returns
  			// TMCreditCard , which cause CreditCardInfo.copyFrom(CreditCard) throw ClassCastException
  			//Solution: 
  			//Always go by delegate.getCrditCard0().
  			ccTransactionInfo.getCreditCardInfo().copyFrom(delegate.getCreditCard0());
  			
  			try {
  				//this EJB call does two things: refund credit card, record the refund transaction in KB 
  				provider.getAccountLifecycleFacade().refundCreditCardPayment( ban, delegate.getSeqNo(), reasonCode, memoText, ccTransactionInfo, 
  							 SessionUtil.getSessionId(provider.getAccountLifecycleFacade()) );
  				
  			} catch (Throwable t) {
  				TelusExceptionTranslator telusExceptionTranslator= new ProviderCreditCardExceptionTranslator(getCreditCard());
  				provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
  			}
  		} 
  		else { 
  			try {
  				//this EJB call does only one thing: record the refund transaction in KB 
  				provider.getAccountLifecycleManager().refundPaymentToAccount( ban, delegate.getSeqNo(),
  						reasonCode, memoText, isManual, null, // we don't know the authorizationCode, so pass null to back end. 
  						SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
  			} catch ( Throwable t ) {
  				Logger.debug("Non credit card payment refund failed: ban=" + ban + ", seqNo=" +delegate.getSeqNo() + ", reason=" + reasonCode);
  				Logger.debug(t );
  				
  				provider.getExceptionHandler().handleException(t);
  			}
  		}
  	}
}




