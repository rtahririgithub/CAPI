package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.ClientAPI.Provider;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Discount;
import com.telus.api.account.Subscriber;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.account.TMDiscount;

public class TMDiscountIntTest extends BaseTest {

	static {
		//setupD3();
		//setupEASECA_QA();
		setupCHNLECA_PT168();
		//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
		//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
		//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}

	public TMDiscountIntTest(String name) throws Throwable {
		super(name);
	}

	private TMAccountManager accountManager;

	
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
	}

	public void testApply() throws  Throwable{
		System.out.println("testApply start");
		try{
			TMAccount account =(TMAccount) api.getAccountManager().findAccountByBAN(70672592);
			Discount [] discount= account.getDiscounts();

			//discount.setDiscountCode("BD10PCSB");
			DiscountInfo discountInfo = new DiscountInfo();
			discountInfo.setSubscriberId("5141661867");
			discountInfo.setDiscountCode("BD10PCSB");
			Discount d = discountInfo;
			//discount.set
			//discount[0].setExpiryDate(new Date(2013,03,22));
			d.apply();
			discount[0].apply();
		}catch(TelusAPIException e){
			System.out.println("error code"+e.getMessage());
			e.printStackTrace();
		}

		System.out.println("testApply End");
	}

	
	// Koodo Smart Pay Account level test cases begin
	
	public void testApplyingDiscountsAtAccountrLevelForSuccesCases()
	{
		try {
			/** success Scenarios */
			TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70672592);
			// 1.Applying discount
			
			TMDiscount discount = (TMDiscount) account.newDiscount();
			discount.setDiscountCode("BD10PCSB ");
			discount.setEffectiveDate(getDateInput(2013,03,15));
			discount.setExpiryDate(getDateInput(2013,04,17));
			 //discount.apply();

			// 2.Retrieving existing discounts on subscriber
			
			Discount [] discounts1 = account.getDiscounts();
			for (int i = 0; i < discounts1.length; i++) {
				System.out.println("Exististing discounts on given account"+discounts1[i].toString());	
			}

		}
			
		catch (Exception ex) {
			System.out.println("$$$$$$$$$$$$$$$$");
			ex.printStackTrace();
		}

	}

	public void testApplyDiscountAtAccountLevelForErrorCases() throws  Throwable{
		System.out.println("testApply start");

		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70672592);
		TMAccount koodoAccount = (TMAccount) api.getAccountManager().findAccountByBAN(70673086);

		try {
			// 1 .adding existing discount code again on account

			TMDiscount discount = (TMDiscount) account.newDiscount();
			discount.setDiscountCode("BD10PCSB ");
			discount.setEffectiveDate(getDateInput(2013,03,15));
			discount.setExpiryDate(getDateInput(2013,04,17));
			//discount.apply();
		} catch (Exception ex) {
		//	assertEquals(": The given discount has already been applied to the BAN/product.", ex.getMessage());
		}
		try {
			// 2 .adding Invalid discount code on account

			TMDiscount discount = (TMDiscount) account.newDiscount();
			discount.setDiscountCode("BD10PCSB1 ");
			discount.setEffectiveDate(getDateInput(2013,03,15));
			discount.setExpiryDate(getDateInput(2013,04,17));
			discount.apply();
		} catch (Exception ex) {
			assertEquals(": The discount BD10PCSB1  is not valid.", ex.getMessage());
		}
		//3. modify the existing discount
		
		try {
		Discount[] discounts =  account.getDiscounts();
		//discounts[0].setEffectiveDate(getDateInput(2013,03,21));
		discounts[0].setExpiryDate(getDateInput(2013,05,25));
		discounts[0].apply();
		}
		catch (Exception e) {
			assertEquals(": The given discount has already been applied to the BAN/product.", e.getMessage());	
		}
	
		//4.test case for ExpiryDate is before effective Date
		try {

			TMDiscount discount = (TMDiscount) account.newDiscount();
			discount.setDiscountCode("BD10PCSB");
			discount.setEffectiveDate(getDateInput(2013, 05, 18));
			discount.setExpiryDate(getDateInput(2013, 04, 17));
			discount.apply();
		} catch (Exception e) {
			assertEquals(": Effective Date is invalid.", e.getMessage());
		}
		try
		{
			//5.Applying subscriber level discount at ban level
			TMDiscount discount = (TMDiscount) account.newDiscount();
			discount.setDiscountCode("XTSD30C");
			discount.setEffectiveDate(getDateInput(2013,03,16));
			discount.setExpiryDate(getDateInput(2013,03,17));
			discount.apply();
		}
		catch (Exception e) {
			assertEquals(": Product level discounts cannot be applied to a Ban.", e.getMessage());
		}
		
		boolean exceptionoccured = false;
		try {
			// 6.passing effective  is past date , i.e before todays's date
			
			TMDiscount discount = (TMDiscount) account.newDiscount();
			discount.setDiscountCode("BD10PCSB");
			discount.setEffectiveDate(getDateInput(2012, 04, 31));
			discount.setExpiryDate(getDateInput(2013, 04, 28));
			//discount.apply();
		} catch (Exception ex)
		{
			exceptionoccured  = true;
			assertEquals(": Effective Date is invalid.", ex.getMessage());
		}
		finally {
//			assertEquals(true, exceptionoccured);
		}
		try {
			// 7.passing telus brand discount on koodo account
			TMDiscount koododiscount = (TMDiscount) koodoAccount.newDiscount();
			koododiscount.setDiscountCode("BD10PCSB");
			koododiscount.setEffectiveDate(getDateInput(2013, 04, 03));
			koododiscount.setExpiryDate(getDateInput(2014, 04, 28));
			//koododiscount.apply();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			assertEquals(": Discount code is not  valid /select telus brand discount code..", ex.getMessage());
		}

		boolean exceptionoccured3 = false;
		try {
			// 8 .passing setExpiryDate is longer than the DiscountPlan.getMonths
			// DiscountPlan.getMonths will return 0 for discount " BD10PCSB " , but we are passing discount term more than year for this year
			
			TMDiscount discount = (TMDiscount) account.newDiscount();
			discount.setDiscountCode("BD10PCSB");  
			discount.setEffectiveDate(getDateInput(2013, 04, 03));
			discount.setExpiryDate(getDateInput(2014, 05, 28));
			discount.apply();
		} catch (Exception ex) {
			exceptionoccured3 =  true;
			ex.printStackTrace();
			assertEquals(": Invalid term plan", ex.getMessage());
		}
		finally {
			assertEquals(true, exceptionoccured3);
		}
	}

	
	//Koodo SmartPay Subscriber level test cases Begin

		public void testApplyingDiscountsAtSubscriberLevelForSuccesCases()
		{
			try {
				/** success Scenarios */
				// 1.Applying discount
				Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
				TMDiscount discount = (TMDiscount) subscriber.newDiscount();
				discount.setDiscountCode("D10P");
				discount.setEffectiveDate(getDateInput(2003,03,16));
				discount.setExpiryDate(getDateInput(20004,03,17));
				discount.apply();

				// 2.Retrieving existing discounts on subscriber
				Subscriber subscriber1 = accountManager.findSubscriberByPhoneNumber("5141661867");
				Discount [] discounts1 = subscriber1.getDiscounts();
				for (int i = 0; i < discounts1.length; i++) {
					System.out.println("Exististing discounts on given subscriber"+discounts1[i].toString());	
				}

				System.out.println("start mofifying discount");
				// 3 .modifying existing discount 
				Discount[] discounts2 = subscriber1.getDiscounts();
				Discount newDiscount = subscriber1.newDiscount();
				newDiscount.setDiscountCode(discounts2[0].getDiscountCode());
				discounts2[0].setExpiryDate(getDateInput(2015, 05, 25));
				discounts2[0].apply();
				System.out.println("end mofifying discount");
				
				try {
					// 4.passing effective & expiry as past dates , i.e before todays's date
					
					TMDiscount discount2 = (TMDiscount) subscriber1.newDiscount();
					discount2.setDiscountCode("D10P");
					discount2.setEffectiveDate(getDateInput(2011, 03, 31));
					discount2.setExpiryDate(getDateInput(2011, 05, 28));
					discount2.apply();
				} catch (Exception ex)
				{
					assertEquals(": The given discount has already been applied to the BAN/product.", ex.getMessage());
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		public void testApplyingDiscountsAtSubscriberLevelForErrorCases()  {

			
			/** failure Scenarios */

			try {
				// 1 .adding existing discount code again
				Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
				TMDiscount discount = (TMDiscount) subscriber.newDiscount();
				discount.setDiscountCode("XTSD30C");
				discount.setEffectiveDate(getDateInput(2013,03,13));
				discount.setExpiryDate(getDateInput(2013,03,17));
				discount.apply();
			} catch (TelusAPIException ex) {
				assertEquals(": The given discount has already been applied to the BAN/product.", ex.getApplicationMessage());
			}

			try {
				// 2 .Invalid  discount code 
				Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
				TMDiscount discount = (TMDiscount) subscriber.newDiscount();
				discount.setDiscountCode("XTSD30C1");
				discount.setEffectiveDate(getDateInput(2013,03,13));
				discount.setExpiryDate(getDateInput(2013,03,17));
				discount.apply();
			} catch (Exception ex) {
				assertEquals(": The discount XTSD30C1 is not valid.", ex.getMessage());
			}
			try {
				// 3 .Applying Ban level discount code at subscriberLevel
				Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
				TMDiscount discount = (TMDiscount) subscriber.newDiscount();
				discount.setDiscountCode("BD10PCSB");
				discount.setEffectiveDate(getDateInput(2013, 03, 16));
				discount.setExpiryDate(getDateInput(2013, 03, 28));
				discount.apply();
			} catch (Exception ex) {
				assertEquals(": Ban level discounts cannot be applied to a Product.", ex.getMessage());
			}

			try {
				// 4.Giving expire Date before effective Date
				Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
				TMDiscount discount = (TMDiscount) subscriber.newDiscount();
				discount.setDiscountCode("XTSD30C");
				discount.setEffectiveDate(getDateInput(2013, 03, 31));
				discount.setExpiryDate(getDateInput(2013, 03, 28));
				discount.apply();
			} catch (Exception ex) {
				assertEquals(": Effective Date is invalid.", ex.getMessage());
			}

			try {
				// 5 .empty discount code
				Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
				TMDiscount discount = (TMDiscount) subscriber.newDiscount();
				discount.setDiscountCode("");
				discount.setEffectiveDate(getDateInput(2013, 03, 31));
				discount.setExpiryDate(getDateInput(2013, 04, 28));
				discount.apply();
			} catch (Exception ex) {
				assertEquals(": The discount  is not valid.", ex.getMessage());
			}	
			
			try {
				// 6 .passing effective  before DiscountPlan.getEffectiveDate
				Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
				TMDiscount discount = (TMDiscount) subscriber.newDiscount();
				discount.setDiscountCode("6MTPD5");
				discount.setEffectiveDate(getDateInput(1979, 03, 31));
				discount.setExpiryDate(getDateInput(2013, 04, 28));
			discount.apply();
			} catch (Exception ex) {
				ex.printStackTrace();
				assertEquals(": Effective Date is invalid.", ex.getMessage());
			}
			
			
			boolean exceptionoccured1 = false;
			try {
				// 7.passing setExpiryDate is longer than the DiscountPlan.getMonths
				// DiscountPlan.getMonths will return 6 for discount " 6MTPD5 " , but we are passing discount term more than year for this year
				Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
				TMDiscount discount = (TMDiscount) subscriber.newDiscount();
				discount.setDiscountCode("6MTPD5");  
				discount.setEffectiveDate(getDateInput(2013, 04, 03));
				discount.setExpiryDate(getDateInput(2014, 05, 28));
				//discount.apply();
			} catch (Exception ex) {
				ex.printStackTrace();
				assertEquals(": Invalid term plan", ex.getMessage());
			}
			finally {
				assertEquals(true, exceptionoccured1);
			}
			
			try {
				// 8.passing koodo brand discount on telus subscriber
				Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
				TMDiscount discount = (TMDiscount) subscriber.newDiscount();
				discount.setDiscountCode("3RD50");
				discount.setEffectiveDate(getDateInput(2013, 04, 03));
				discount.setExpiryDate(getDateInput(2013, 04, 28));
				//discount.apply();
			} catch (Exception ex) {
				ex.printStackTrace();
				assertEquals(": Discount code is not  valid /select telus brand discount code..", ex.getMessage());
			}
		}



	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}



}


