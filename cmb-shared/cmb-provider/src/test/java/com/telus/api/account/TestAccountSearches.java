package com.telus.api.account;

import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.reference.Brand;

public class TestAccountSearches extends BaseTest {

	static {
          setupEASECA_QA();
//		localhostWithD3Ldap();
//		localhostWithPT168Ldap();
	}
	
	public TestAccountSearches(String name) throws Throwable {
		super(name);
	}

	public void testFindAccountByBan() throws Throwable {
		int ban = 12474;
		
		Account account = api.getAccountManager().findAccountByBAN(ban);
		assertTrue(account != null);
		printAccountSummary ("testFindAccountByBan", account);
	}
	
	public void testFindAccountsByBans() throws Throwable {
		int[] banIds = {12474, 17605};
		
		Account[] accounts = api.getAccountManager().findAccountsByBANs(banIds);
		assertTrue(accounts != null);
		for (int i = 0 ; i < accounts.length; i++) {
			printAccountSummary ("testFindAccountsByBans", accounts[i]);
		}
	}
	
	public void testFindAccountByPhoneNumber() throws Throwable {
		String phoneNumber = "4033946834";
		
		Account account = api.getAccountManager().findAccountByPhoneNumber(phoneNumber);
		assertTrue(account != null);
		printAccountSummary ("testFindAccountByPhoneNumber", account);
	}
		
	public void testFindAccountsBySerialNumber() throws Throwable {
		String serialNumber = "21101107129";
		AccountSummary[] accountSummary = api.getAccountManager().findAccountsBySerialNumber(serialNumber);
		assertTrue (accountSummary != null);
		for (int i = 0; i < accountSummary.length; i++) {
			printAccountSummary ("testFindAccountsBySerialNumber", accountSummary[i]);
		}
	}
	
	/**
	 * SELECT IMSI_NUMBER  FROM subscriber_rsource sr1
     *         WHERE sr1.resource_type = 'Q'
	 *           AND sr1.record_exp_date = TO_DATE ('12/31/4700', 'mm/dd/yyyy')
	 *           
	 * @throws Throwable
	 */
	public void testFindAccountsByImsi() throws Throwable {
		String imsi = "214030000052116";
		AccountSummary[] accountSummary = api.getAccountManager().findAccountsByImsi(imsi);
		assertTrue (accountSummary != null);
		for (int i = 0; i < accountSummary.length; i++) {
			printAccountSummary ("testFindAccountsByImsi", accountSummary[i]);
		}
	}
	
	public void testFindAccountsByBusinessName1() throws Throwable {
		String nameType = "*";
		String legalBusinessName = "a";
		boolean legalBusinessNameExactMatch = false;
		char accountStatus= '*';
		char accountType = '*';
		String provinceCode = "*";
		int maximum = 100;
		SearchResults results = api.getAccountManager().findAccountsByBusinessName(nameType, legalBusinessName, legalBusinessNameExactMatch, accountStatus, accountType, provinceCode, maximum);
		assertTrue(results != null);
		printSearchResults("testFindAccountsByBusinessName1", results);
	}
	
	public void testFindAccountsByBusinessName2() throws Throwable {
		String nameType = "*";
		String legalBusinessName = "a";
		boolean legalBusinessNameExactMatch = false;
		char accountStatus= '*';
		char accountType = '*';
		String provinceCode = "*";
		int maximum = 100;
		int brandId = Brand.BRAND_ID_TELUS;
		
		SearchResults results = api.getAccountManager().findAccountsByBusinessName(nameType, legalBusinessName, legalBusinessNameExactMatch, accountStatus, accountType, provinceCode, brandId, maximum);
		assertTrue(results != null);
		printSearchResults("testFindAccountsByBusinessName2", results);
	}
	
	public void testFindAccountsByName1() throws Throwable {
		String nameType = "*";
		String firstName = "a";
		boolean firstNameExactMatch = false;
		String lastName = "a";
		boolean lastNameExactMatch = false;
		char accountStatus= '*';
		char accountType = '*';
		String provinceCode = "*";
		int maximum = 100;
		int brandId = Brand.BRAND_ID_TELUS;
		
		SearchResults results = api.getAccountManager().findAccountsByName(nameType, firstName, firstNameExactMatch, lastName, lastNameExactMatch, accountStatus, accountType, provinceCode, brandId, maximum);
		assertTrue(results != null);
		printSearchResults("testFindAccountsByName1", results);
	}
	
	public void testFindAccountsByName2() throws Throwable {
		String nameType = "*";
		String firstName = "a";
		boolean firstNameExactMatch = false;
		String lastName = "a";
		boolean lastNameExactMatch = false;
		char accountStatus= '*';
		char accountType = '*';
		String provinceCode = "*";
		int maximum = 100;
		
		SearchResults results = api.getAccountManager().findAccountsByName(nameType, firstName, firstNameExactMatch, lastName, lastNameExactMatch, accountStatus, accountType, provinceCode, maximum);
		assertTrue(results != null);
		printSearchResults("testFindAccountsByName2", results);
	}
	
	public void testFindAccountsByPhoneNumber() throws Throwable {
		String phoneNumber = "4033946834";
		
		AccountSummary[] accountSummary = api.getAccountManager().findAccountsByPhoneNumber(phoneNumber);
		assertTrue(accountSummary != null);
		for (int i = 0; i < accountSummary.length; i++) {
			printAccountSummary ("testFindAccountsByPhoneNumber", accountSummary[i]);
		}
	}
	
	public void testFindAccountsByPhoneNumber2() throws Throwable {
		String phoneNumber = "4033946834";
		boolean includePastAccounts = true;
		boolean onlyLastAccount = false;
		
		AccountSummary[] accountSummary =  api.getAccountManager().findAccountsByPhoneNumber(phoneNumber, includePastAccounts, onlyLastAccount);
		assertTrue(accountSummary != null);
		for (int i = 0; i < accountSummary.length; i++) {
			printAccountSummary ("testFindAccountsByPhoneNumber2", accountSummary[i]);
		}
	}
	
	/**
	 * select ad.adr_postal, nd.control_name from address_data ad, name_data nd, address_name_link anl where ad.address_id=anl.address_id and anl.name_id = nd.name_id and anl.link_type = 'B' and anl.expiration_date  is null
	 * @throws Throwable
	 */
	public void testFindAccountsByPostalCode() throws Throwable {

		String lastName = "NICOLE";

		String postalCode = "M1H3E4";

		int maximum = 100;
		
		AccountSummary[] accountSummary = api.getAccountManager().findAccountsByPostalCode(lastName, postalCode, maximum);
		assertTrue(accountSummary != null);
		for (int i = 0; i < accountSummary.length; i++) {
			printAccountSummary ("testFindAccountsByPostalCode", accountSummary[i]);
		}
	}
	
	public void testFindAccountsByDealership1() throws Throwable {
		char accountStatus = 'O';
		String dealerCode = "A001000001";
		Date startDate = new Date(2001-1900, 1, 1);
		int maximum = 100;
		
		Account[] accounts = api.getAccountManager().findAccountsByDealership(accountStatus, dealerCode, startDate, maximum);
		assertTrue(accounts != null);
		for (int i = 0; i < accounts.length; i++) {
			printAccountSummary ("testFindAccountsByDealership1", accounts[i]);
		}
	}
	
	public void testFindAccountsByDealership2() throws Throwable {
		char accountStatus = 'O';
		String dealerCode = "1100025800";
		Date startDate = new Date(2001-1900, 1, 1);
		Date endDate = new Date();
		int maximum = 100;
		
		Account[] accounts = api.getAccountManager().findAccountsByDealership(accountStatus, dealerCode, startDate, endDate, maximum);
		assertTrue(accounts != null);
		for (int i = 0; i < accounts.length; i++) {
			printAccountSummary ("testFindAccountsByDealership2", accounts[i]);
		}
	}
	
	public void testFindAccountsBySalesRep1() throws Throwable {
		char accountStatus = 'O';
		String dealerCode = "A001000001";
		String salesRepCode = "0000";
		Date startDate = new Date(2001-1900, 1, 1);
		int maximum = 100;
		
		Account[] accounts = api.getAccountManager().findAccountsBySalesRep(accountStatus, dealerCode, salesRepCode, startDate, maximum);
		assertTrue(accounts != null);
		for (int i = 0; i < accounts.length; i++) {
			printAccountSummary ("testFindAccountsBySalesRep1", accounts[i]);
		}
	}
	
	public void testFindAccountsBySalesRep2() throws Throwable {
		char accountStatus = 'O';
		String dealerCode = "1100025800";
		String salesRepCode = "0000";
		Date startDate = new Date(2001-1900, 1, 1);
		Date endDate = new Date();
		int maximum = 100;
		
		Account[] accounts = api.getAccountManager().findAccountsBySalesRep(accountStatus, dealerCode, salesRepCode, startDate, endDate, maximum);
		assertTrue(accounts != null);
		for (int i = 0; i < accounts.length; i++) {
			printAccountSummary ("testFindAccountsBySalesRep2", accounts[i]);
		}
	}
	
	/**
	 * in KB
	 * 
	 * select * from ban_tg_matrix 
	 * @throws Throwable
	 */
	public void testFindAccountsByTalkGroup() throws Throwable {
		int urbanId = 905;
		int fleetId = 70;
		int talkGroupId = 1;
		
		AccountSummary[] accountSummary = api.getAccountManager().findAccountsByTalkGroup(urbanId, fleetId, talkGroupId);
		assertTrue(accountSummary != null);
		for (int i = 0; i < accountSummary.length; i++) {
			printAccountSummary ("testFindAccountsByTalkGroup", accountSummary[i]);
		}
	}
	
	/**
	 * Added per Winnie's request
	 * @throws Throwable
	 */
	public void testFindAccountByBan1() throws Throwable {
		  int ban = 12474;
		  
		  Account account = api.getAccountManager().findAccountByBAN(ban);
		  assertTrue(account != null);
		  printAccountObj ("testFindAccountByBan1", account);
		 }
		 
	private void printAccountObj(String methodName, Account account) {
		System.out.println(methodName + ": ban=" + account.getBanId() + ",dealer code=" + account.getDealerCode() + ",Sales Rep Code=" + account.getSalesRepCode());
	}
	
	private void printSearchResults (String methodName, SearchResults results) {
		AccountSummary[] accountSummary = (AccountSummary[]) results.getItems();
		for (int i = 0; i < accountSummary.length; i++) {
			printAccountSummary(methodName, accountSummary[i]);
		}
	}
	
	private void printAccountSummary (String methodName, AccountSummary accountSummary) {
		System.out.println(methodName + ": ban="+accountSummary.getBanId() +","+accountSummary.getDealerCode()+","+accountSummary.getSalesRepCode());
	}
}
