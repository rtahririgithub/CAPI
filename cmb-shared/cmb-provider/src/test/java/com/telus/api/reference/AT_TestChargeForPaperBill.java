package com.telus.api.reference;

import com.telus.api.BaseTest;
import com.telus.api.ClientAPI;
import com.telus.api.account.Account;
import com.telus.api.account.InvoiceProperties;
import com.telus.api.account.PostpaidAccount;

public class AT_TestChargeForPaperBill extends BaseTest {
	
	private static ClientAPI api;
	
	static {	    
	    //setuplocalHost();
		setupSMARTDESKTOP_QA();
		//setupSMARTDESKTOP_PT168();

		try {
			api = ClientAPI.getInstance("18654", "apollo", ApplicationSummary.APP_SD);
			System.out.println("API instance created.");
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public AT_TestChargeForPaperBill(String name) throws Throwable{
		super(name);
	}

	public void _testRetrievePaperBillChargeType() {

		ChargeType chargeType = null;
		
		try {
			System.out.println("\nTesting retrieving paper bill charge type...");

			ReferenceDataManager rdm = api.getProvider().getReferenceDataManager();
			
			System.out.println("\nTEST CASE #1");
			chargeType = rdm.getPaperBillChargeType(3, "SK", 'I', '\u0000', null, "8");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #2");
			chargeType = rdm.getPaperBillChargeType(1, "ON", 'I', '\u0000', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #3");
			chargeType = rdm.getPaperBillChargeType(1, null, 'C', '\u0000', null, "0");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #4");
			chargeType = rdm.getPaperBillChargeType(1, "PQ", 'I', 'R', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #5");
			chargeType = rdm.getPaperBillChargeType(1, null, 'C', '\u0000', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #6");
			chargeType = rdm.getPaperBillChargeType(1, "PQ", 'I', 'R', null, "2");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #7");
			chargeType = rdm.getPaperBillChargeType(1, null, 'B', '\u0000', null, "0");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #8");
			chargeType = rdm.getPaperBillChargeType(1, "ON", 'B', 'P', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #9");
			chargeType = rdm.getPaperBillChargeType(1, null, 'B', '\u0000', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #10");
			chargeType = rdm.getPaperBillChargeType(1, "ON", 'B', 'P', null, "2");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #11");
			chargeType = rdm.getPaperBillChargeType(3, null, 'I', '\u0000', null, "0");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #12");
			chargeType = rdm.getPaperBillChargeType(1, "ON", '\u0000', '\u0000', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			System.out.println("\nTEST CASE #13");
			chargeType = rdm.getPaperBillChargeType(3, null, 'I', '\u0000', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #14");
			chargeType = rdm.getPaperBillChargeType(1, "ON", '\u0000', '\u0000', null, "2");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			System.out.println("\nTEST CASE #15");
			chargeType = rdm.getPaperBillChargeType(3, null, 'I', '\u0000', null, "2");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #16");
			chargeType = rdm.getPaperBillChargeType(3, null, 'I', '\u0000', null, "3");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #17");
			chargeType = rdm.getPaperBillChargeType(3, null, 'I', '\u0000', null, "4");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			System.out.println("\nTEST CASE #18");
			chargeType = rdm.getPaperBillChargeType(3, null, 'I', '\u0000', null, "5");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			System.out.println("\nTEST CASE #19");
			chargeType = rdm.getPaperBillChargeType(3, null, 'I', '\u0000', null, "6");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			System.out.println("\nTEST CASE #20");
			chargeType = rdm.getPaperBillChargeType(3, null, 'I', '\u0000', null, "7");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #21");
			chargeType = rdm.getPaperBillChargeType(3, null, 'I', '\u0000', null, "8");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #22");
			chargeType = rdm.getPaperBillChargeType(1, "ON", 'B', 'R', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #23");
			chargeType = rdm.getPaperBillChargeType(1, "ON", 'B', 'R', null, "2");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			System.out.println("\nTEST CASE #24");
			chargeType = rdm.getPaperBillChargeType(1, "MB", '\u0000', 'R', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #25");
			chargeType = rdm.getPaperBillChargeType(1, "ON", '\u0000', 'R', null, "2");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			// for all first five parameters null, should throw IllegalArgumentException(
			// "All parameters passed in are either null or empty. This operation requires at least one parameter passed in to be populated.")
			//System.out.println("\nTEST CASE #26");
			//chargeType = rdm.getPaperBillChargeType(0, null, '\u0000', '\u0000', null, "1");

			// for all first five parameters null, should throw IllegalArgumentException(
			// "All parameters passed in are either null or empty. This operation requires at least one parameter passed in to be populated.")
			//System.out.println("\nTEST CASE #27");
			//chargeType = rdm.getPaperBillChargeType(0, null, '\u0000', '\u0000', null, "2");

			System.out.println("\nTEST CASE #28");
			chargeType = rdm.getPaperBillChargeType(3, "ON", '\u0000', '\u0000', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			System.out.println("\nTEST CASE #29");
			chargeType = rdm.getPaperBillChargeType(3, "ON", '\u0000', '\u0000', null, "2");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #30");
			chargeType = rdm.getPaperBillChargeType(3, null, '\u0000', '\u0000', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #31");
			chargeType = rdm.getPaperBillChargeType(3, null, '\u0000', '\u0000', null, "2");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			System.out.println("\nTEST CASE #32");
			chargeType = rdm.getPaperBillChargeType(1, "BC", '\u0000', '\u0000', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));

			System.out.println("\nTEST CASE #33");
			chargeType = rdm.getPaperBillChargeType(3, "ON", 'I', '\u0000', null, "2");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			System.out.println("\nTEST CASE #34");
			chargeType = rdm.getPaperBillChargeType(3, "BC", '\u0000', '\u0000', null, "1");
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			System.out.println("\nFinished!");
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}

	public void _testOneRetrievePaperBillChargeType() {

		ChargeType chargeType = null;
		
		try {
			System.out.println("\nTesting retrieving paper bill charge type...");

			ReferenceDataManager rdm = api.getProvider().getReferenceDataManager();
			
			chargeType = rdm.getPaperBillChargeType(3, "ON", 'I', 'R', null, null);
			System.out.println("\n\nChargeType : " + (chargeType == null ? "null" : chargeType.toString()));
			
			System.out.println("\nFinished!");
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}

	public void _testDefect_171692() {

		try {
			System.out.println("\nTesting defect 171692...");

			int ban =197806; 
			
			Account account = api.getAccountManager().findAccountByBAN(ban);
			assertNotNull(account);
			System.out.println(account.toString());

			boolean paperBillFlag = false;
			InvoiceProperties invoice = ((PostpaidAccount) account).getInvoiceProperties();
			if (invoice != null) {
			if (invoice.getInvoiceSuppressionLevel() != InvoiceSuppressionLevel.SUPPRESS_ALL)
				paperBillFlag = true;
			}
			System.out.println("\n\ninvoice -> " + invoice);
			System.out.println("invoice.getInvoiceSuppressionLevel() -> " + invoice.getInvoiceSuppressionLevel());
			System.out.println("paperBillFlag -> " + paperBillFlag);

			System.out.println("\nFinished!");
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}

	public void testDefect_171765() {

		try {
			System.out.println("\nTesting defect 171765...");

			int ban = 197806;
			
			Account account = api.getAccountManager().findAccountByBAN(ban);
			assertNotNull(account);
			System.out.println(account.toString());

			InvoiceProperties invoiceProperties = ((PostpaidAccount) account).getInvoiceProperties();

			System.out.println("\n\ninvoiceProperties -> " + invoiceProperties);
			System.out.println("invoiceProperties.getInvoiceSuppressionLevel() -> " + invoiceProperties.getInvoiceSuppressionLevel());

/*			if (invoiceProperties != null) {
				invoiceProperties.setInvoiceSuppressionLevel("2");
				((PostpaidAccount) account).setInvoiceProperties(invoiceProperties);
				account.save();
			}
*/
			System.out.println("\nFinished!");
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}

}