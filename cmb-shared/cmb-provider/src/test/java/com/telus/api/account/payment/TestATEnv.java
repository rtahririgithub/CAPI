package com.telus.api.account.payment;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.PaymentNotification;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.eas.account.info.AccountInfo;
import com.telus.provider.account.TMAccount;

public class TestATEnv extends BaseTest {

	static {
		//setupK();
         //setuplocalHost();
		setupEASECA_QA();
	}

	public TestATEnv(String name) throws Throwable {
		super(name);
	}

	public void testall() {
		try {
			_testDefects();
		} catch (Throwable t) {

		}
	}

	public void _testDefects() throws Throwable {
		try {
			System.out.println("***** START OF TEST RUN - testDefects() ****");	
			String MDN = "4033317837";  
			System.out.println("Testing MDN : " + MDN)	;	
			PostpaidAccount  postpaidAccount = (PostpaidAccount) api.getAccountManager().findAccountByPhoneNumber(MDN);
			double requiredPayment = postpaidAccount.getRequiredPaymentForRestoral();
			System.out.println("Required payment for Restoral : " + requiredPayment);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out.println("***** Finished! - testDefects() ****");
		}
	}
	
	public void testRetrievalOfAccountBalance() throws Throwable {
		try {
			System.out.println("***** START OF TEST RUN - testRetrievalOfAccountBalance() ****");
			
			
			String MDN = "4037109656"; 
			System.out.println("Testing MDN : " + MDN);
			
			Account account = api.getAccountManager().findAccountByPhoneNumber(MDN);

			// if CLP client
			if( ((TMAccount)account).isCLP() ) {

				// CLMSummary object
				System.out.println("CLMSummary object- > " + ((PostpaidConsumerAccount)account).getCLMSummary());
				
			} else { // non-CLP client

				// DebtSummary object
				System.out.println("DebtSummary object - > " + account.getFinancialHistory().getDebtSummary());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out.println("***** Finished! - testRetrievalOfAccountBalance() ****");
		}
	}
}