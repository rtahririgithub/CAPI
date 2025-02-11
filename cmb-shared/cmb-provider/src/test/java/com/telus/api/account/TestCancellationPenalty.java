package com.telus.api.account;

import com.telus.api.BaseTest;
import com.telus.api.account.CancellationPenalty;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.Subscriber;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.provider.account.TMAccount;
import com.telus.provider.account.TMSubscriber;

public class TestCancellationPenalty extends BaseTest {

	private int BAN = 12474;
	private String subscriberId;

	static {
          setupEASECA_QA();
		 //setupCHNLECA_PT168();
		// System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://localhost:8001");
		// System.setProperty("cmb.services.ReferenceDataFacade.url","t3://localhost:8001");

	}
	private TMSubscriber subscriber;
	private TMAccount tmAccount;
	

	public TestCancellationPenalty(String name) throws Throwable {

		super(name);
	}

	/*
	 * ==================================================================
	 * getRequiredPaymentForRestoral test case
	 * =================================================================
	 */

	public void testAll() throws Throwable {

		setupData("qa");
		_testCancellationPenaltyWithAccount();
		_testCancellationPenaltyWithAccount1();
		_testCancellationPenaltyListWithAccount();
		_testCancellationPenaltyWithSubscriber();
	


	}

	/**
	 * @throws Throwable
	 */
	public void _testCancellationPenaltyWithAccount() throws Throwable {

		try {

			setupData("pt148");

			System.out.println("***** START OF TEST RUN _testCancellationPenaltyWithAccount****");

			PostpaidAccount postpaidAccount = (PostpaidAccount) api	.getAccountManager().findAccountByBAN(BAN);
				
			assertEquals(BAN,postpaidAccount.getBanId());
			CancellationPenaltyInfo cancellationPenalty = (CancellationPenaltyInfo)postpaidAccount.getCancellationPenalty();	
			assertNotNull(cancellationPenalty);	
			System.out.println("CancellationPenalty Obj :"+ cancellationPenalty);
			System.out.println("##### _testCancellationPenaltyWithAccount****");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out
					.println("***** Finished! - _testCancellationPenaltyWithAccount() ****");
		}

	}

	
	public void _testCancellationPenaltyWithAccount1() throws Throwable {

		try {

			setupData("pt148");

			System.out.println("***** START OF TEST RUN _testCancellationPenaltyWithAccount1****");
			PostpaidAccount postpaidAccount = (PostpaidAccount) api.getAccountManager().findAccountByPhoneNumber("4033946834");
					

			assertEquals(BAN,postpaidAccount.getBanId());
			CancellationPenaltyInfo cancellationPenalty = (CancellationPenaltyInfo)postpaidAccount
					.getCancellationPenalty();
			
			assertNotNull(cancellationPenalty);
			
			
			System.out.println("CancellationPenalty Obj :"
					+ cancellationPenalty);

			System.out.println("##### _testCancellationPenaltyWithAccount1****");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out
					.println("***** Finished! - _testCancellationPenaltyWithAccount1() ****");
		}

	}
	
	
	/**
	 * @throws Throwable
	 */
	public void _testCancellationPenaltyListWithAccount() throws Throwable {

		try {

			setupData("pt148");

			System.out
					.println("***** START OF TEST RUN _testCancellationPenaltyListWithAccount****");

			PostpaidAccount postpaidAccount = (PostpaidAccount) api
					.getAccountManager().findAccountByBAN(BAN);

			
			System.out.println(postpaidAccount.getProductSubscriberLists()[0].getActiveSubscriberIdentifiers()[0].getSubscriberId());
			System.out.println(postpaidAccount.getProductSubscriberLists()[0].getActiveSubscriberIdentifiers()[1].getSubscriberId());
			
			
			CancellationPenalty[] cancellationPenalty =postpaidAccount.getCancellationPenaltyList(new String[]{"4033946834","4032130201"});

			assertTrue(cancellationPenalty != null);

			System.out.println("cancellationPenalty[0]-->"+cancellationPenalty[0] );


			System.out.println("##### _testCancellationPenaltyWithAccount****");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out
					.println("***** Finished! - _testCancellationPenaltyListWithAccount() ****");
		}

	}
	
	
	/**
	 * @throws Throwable
	 */
	public void _testCancellationPenaltyWithSubscriber() throws Throwable {

		try {

			setupData("pt148");

			System.out
					.println("***** START OF TEST RUN _testCancellationPenaltyWithSubscriber****");

			Subscriber subscriber= (Subscriber) api
					.getAccountManager().findSubscribersByBAN(BAN,1)[0];

			CancellationPenalty cancellationPenalty =subscriber.getCancellationPenalty();

			assertTrue(cancellationPenalty != null);

			System.out.println("cancellationPenalty-->"+cancellationPenalty );

			System.out.println("##### _testCancellationPenaltyWithSubscriber****");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out
					.println("***** Finished! - _testCancellationPenaltyWithSubscriber() ****");
		}

	}
	
	
	/**
	 * @param env
	 */
	public void setupData(String env) {

		if (env.equalsIgnoreCase("d1")) {

		} else if (env.equalsIgnoreCase("d2")) {

		} else if (env.equalsIgnoreCase("d3")) {

			BAN = 70019618;

		} else if (env.equalsIgnoreCase("sit")) {

		} else if (env.equalsIgnoreCase("qa")||env.equalsIgnoreCase("pt148")) {

			BAN = 12474;
			subscriberId = "4032130201";

		} else if (env.equalsIgnoreCase("pt168")) {

			BAN = 70546547;
			
		} else if (env.equalsIgnoreCase("stag")) {

		} else if (env.equalsIgnoreCase("csi")) {

		} else if (env.equalsIgnoreCase("prod")) {

		} else { 

			int BAN = 20007009;

		}
	}
}