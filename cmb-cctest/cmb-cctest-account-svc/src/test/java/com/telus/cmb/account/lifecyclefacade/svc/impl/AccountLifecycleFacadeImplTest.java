package com.telus.cmb.account.lifecyclefacade.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.cmb.account.lifecyclemanager.svc.impl.AccountLifecycleManagerImpl;
import com.telus.cmb.common.aop.utilities.BANValue;
import com.telus.cmb.common.logging.Sensitive;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;


public class AccountLifecycleFacadeImplTest {
	
	
	protected boolean updatePaymentMethodCalled = false;
	protected boolean creditCardVoidCalled = false;
	protected boolean applyPaymentToAccountCalled = false;
	protected String authorizationNumber = "AUTHORIZATION_NUMBER";
	protected String errorMessage = "ERROR_MESSAGE";
	
	
	protected int testBan;
	protected int testPaymentSeq;
	protected String testReasonCode;
	protected String testMemoText;
	protected boolean testIsManual;
	protected String testAuthorizationCode;
	protected String testSessionId;

	@Test
	public void testPayBillAndPayDeposit() throws ApplicationException {
		
		AccountLifecycleFacadeImpl impl = new AccountLifecycleFacadeImpl() {
			@Override
			protected String payBillOrDeposit(@BANValue int ban, boolean payDeposit, String paymentSourceType, String paymentSourceID, double amount
					, @Sensitive CreditCardTransactionInfo creditCardTransactionInfo, AccountInfo accountInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {
					return String.valueOf(payDeposit);
			}
		};
		
		assertEquals(String.valueOf(false), impl.payBill(1, null, 1, null, null, null, false, null, null));
		
		assertEquals(String.valueOf(true), impl.payDeposit(1, null, 1, null, null, null ));		
		
		impl = new AccountLifecycleFacadeImpl() {
			@Override
			public String applyCreditCardCharge(CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException {
				return authorizationNumber;
			}
			
			
			@Override
			public void voidCreditCardCharge(CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException {
				creditCardVoidCalled = true;
			}
		};
		
		impl.setAccountLifecycleManager(new AccountLifecycleManagerImpl() {
			@Override
			public void applyPaymentToAccount(PaymentInfo pPaymentInfo, String sessionId) throws ApplicationException {
				applyPaymentToAccountCalled = true;
				return;
			}
		});
		
		CreditCardTransactionInfo creditCardTransactionInfo = new CreditCardTransactionInfo();
		creditCardTransactionInfo.setAmount(100);
		CreditCardInfo newCreditCardInfo = new CreditCardInfo();
		newCreditCardInfo.setLeadingDisplayDigits("3212");
		creditCardTransactionInfo.setCreditCardInfo(newCreditCardInfo);
		
		authorizationNumber = "";
		try {
			impl.payBill(0, creditCardTransactionInfo , 1, null, null, null, false, null, null);
		} catch (ApplicationException ae) {
			assertEquals("Credit Card Transaction not successful - Bank Interface Handler not responding.", ae.getErrorMessage());
			assertEquals(SystemCodes.CMB_ALF_EJB, ae.getSystemCode());
		}
		
		authorizationNumber = "AUTHORIZATION_NUMBER";
		assertEquals(authorizationNumber, impl.payBill(0, creditCardTransactionInfo , 1, null, null, null, false, null, null));		
		assertTrue(applyPaymentToAccountCalled);
		assertFalse(creditCardVoidCalled);
		
		impl.setAccountLifecycleManager(new AccountLifecycleManagerImpl() {
			@Override
			public void applyPaymentToAccount(PaymentInfo pPaymentInfo, String sessionId) throws ApplicationException {
				throw new ApplicationException(SystemCodes.CMB_ALF_EJB, errorMessage, "");
			}
		});
		
		try {
			impl.payBill(0, creditCardTransactionInfo , 1, null, null, null, false, null, null);
		} catch (ApplicationException ae) {
			assertTrue(creditCardVoidCalled);
			assertEquals(errorMessage, ae.getErrorMessage());
		}
		
		impl.setAccountLifecycleManager(new AccountLifecycleManagerImpl() {
			@Override
			public void applyPaymentToAccount(PaymentInfo pPaymentInfo, String sessionId) throws ApplicationException {
				throw new RuntimeException();
			}
		});
		
		try {
			impl.payBill(0, creditCardTransactionInfo , 1, null, null, null, false, null, null);
		} catch (RuntimeException re) {
			assertTrue(creditCardVoidCalled);
		}		
	}
	
	@Test
	public void testRefundCreditCardPayment() throws ApplicationException {
		AccountLifecycleFacadeImpl impl = new AccountLifecycleFacadeImpl() {
			@Override
			protected CreditCardResponseInfo performCreditCardTransaction(CreditCardTransactionInfo ccTxnInfo, 
					final String sessionId) throws ApplicationException {
				return new CreditCardResponseInfo();
			}
			
			@Override
			public String applyCreditCardCharge(CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException {
				return authorizationNumber;
			}
						
			@Override
			public void voidCreditCardCharge(CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException {
				creditCardVoidCalled = true;
			}
		};
		
		impl.setAccountLifecycleManager(new AccountLifecycleManagerImpl() {
			

			@Override
			public void refundPaymentToAccount( int ban, int paymentSeq, String reasonCode, String memoText
					, boolean isManual, String authorizationCode,String sessionId)throws ApplicationException {
				testBan = ban;
				testPaymentSeq = paymentSeq;
				testReasonCode = reasonCode;
				testMemoText = memoText;
				testIsManual = isManual;
				testAuthorizationCode = authorizationCode;
				testSessionId = sessionId;
				
			}
		});
		
		int banId = 321;
		int paymentSeq = 321;
		String reasonCode = "321";
		String memoText = "memotest";
		CreditCardTransactionInfo creditCardTransactionInfo = new CreditCardTransactionInfo();
		String sessionId = "321";
		impl.refundCreditCardPayment(banId, paymentSeq, reasonCode, memoText, creditCardTransactionInfo, sessionId);
		
		assertEquals(testBan, banId);
		assertEquals(testPaymentSeq, paymentSeq);
		assertEquals(testReasonCode, reasonCode);
		assertEquals(testMemoText, memoText);
		assertEquals(testAuthorizationCode, sessionId);
		assertEquals(testSessionId, sessionId);
		
		

		impl.setAccountLifecycleManager(new AccountLifecycleManagerImpl() {
			@Override
			public void refundPaymentToAccount( int ban, int paymentSeq, String reasonCode, String memoText
					, boolean isManual, String authorizationCode,String sessionId) throws ApplicationException {
				throw new ApplicationException(SystemCodes.CMB_ALF_EJB, errorMessage, "");
			}
			
		});
		
		try {
			impl.refundCreditCardPayment(banId, paymentSeq, reasonCode, memoText, creditCardTransactionInfo, sessionId);
		} catch (ApplicationException ae) {
			assertTrue(creditCardVoidCalled);
			assertEquals(errorMessage, ae.getErrorMessage());
		}
		
		impl.setAccountLifecycleManager(new AccountLifecycleManagerImpl() {
			@Override
			public void refundPaymentToAccount( int ban, int paymentSeq, String reasonCode, String memoText
					, boolean isManual, String authorizationCode,String sessionId) throws ApplicationException {
				throw new RuntimeException();
			}
		});
		
		try {
			impl.refundCreditCardPayment(banId, paymentSeq, reasonCode, memoText, creditCardTransactionInfo, sessionId);
		} catch (RuntimeException re) {
			assertTrue(creditCardVoidCalled);
		}		
	}
	
	@Test
	public void testValidateCreditCard() throws ApplicationException{
		AccountLifecycleFacadeImpl impl = new AccountLifecycleFacadeImpl() {
			@Override
			public CreditCardResponseInfo validateCreditCard(@Sensitive CreditCardTransactionInfo pCreditCardTransactionInfo, String sessionId) throws ApplicationException {
				
				return new CreditCardResponseInfo();
			}
		};
		
		assertTrue(impl.validateCreditCard(new CreditCardTransactionInfo(), "abcd")==null?false:true);
	}
	
	@Test
	public void testRegisterTopUpCreditCard() throws ApplicationException{
		AccountLifecycleFacadeImpl impl = new AccountLifecycleFacadeImpl() {
			@Override
			public void registerTopUpCreditCard(@BANValue int billingAccountNumber,@Sensitive CreditCardInfo creditCard, String sessionId)
			throws ApplicationException {
				throw new ApplicationException(SystemCodes.CMB_ALF_EJB, errorMessage, "");
			}
		};
		
		try {
			impl.registerTopUpCreditCard(123, new CreditCardInfo(), "abcd");
		} catch (ApplicationException ae) {
			assertEquals(errorMessage, ae.getErrorMessage());
		}
		
		///
		
		impl = new AccountLifecycleFacadeImpl();
		impl.setAccountLifecycleManager(new AccountLifecycleManagerImpl(){
			@Override
			public PaymentMethodInfo updatePaymentMethod(@BANValue int pBan, @Sensitive PaymentMethodInfo pPaymentMethodInfo, 
					String sessionId)	throws ApplicationException {
				updatePaymentMethodCalled=true;
				return null;
			}
		});
		try{
			impl.registerTopUpCreditCard(123, new CreditCardInfo(), "abcd");
		}catch(Throwable t){
			assertTrue(updatePaymentMethodCalled);
			assertEquals(null,t.getMessage());
		}
		
	}
}

