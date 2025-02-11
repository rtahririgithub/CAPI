package com.telus.api.account;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.BrandNotSupportedException;
import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.reference.ChargeType;

public class AT_TestGetChargeByCodes extends BaseTest {

	static {
		setupSMARTDESKTOP_QA();
	}
	
	public AT_TestGetChargeByCodes(String name) throws Throwable {
		super(name);
	}
	
	public void testValidation() throws UnknownBANException, BrandNotSupportedException, TelusAPIException {
		int banId = 12474;
		
		String [] chargeCodes = new String []{ "STD", "PNUM" , "PCF1"};
		String billState="";
		char chargeLevel=0;
		String subscriberId="4035049404";
		Calendar cal = Calendar.getInstance();
		cal.set(2005, 8,1);
		Date from= cal.getTime();
		cal.set(2005, 9, 31);
		Date to=cal.getTime();
		int maximum=10;
		
		Account account = api.getAccountManager().findAccountByBAN(banId);
		
		//null chargeCode
		negativeTests(account, null, billState, chargeLevel, subscriberId, from, to, maximum);

		//empty chargeCode
		negativeTests(account, new String[0], billState, chargeLevel, subscriberId, from, to, maximum);

		//invalid billState
		negativeTests(account, chargeCodes, "@", chargeLevel, subscriberId, from, to, maximum);

		//invalid chargeLevel
		negativeTests(account, chargeCodes, Account.BILL_STATE_ALL, '@', subscriberId, from, to, maximum);

		//null subscriber 
		negativeTests(account, chargeCodes, Account.BILL_STATE_ALL, ChargeType.CHARGE_LEVEL_SUBSCRIBER, null, from, to, maximum);

		//empty subscriber 
		negativeTests(account, chargeCodes, Account.BILL_STATE_ALL, ChargeType.CHARGE_LEVEL_SUBSCRIBER, "  ", from, to, maximum);

		//null from date 
		negativeTests(account, chargeCodes, Account.BILL_STATE_ALL, ChargeType.CHARGE_LEVEL_ALL, subscriberId, null, to, maximum);

		//null to date 
		negativeTests(account, chargeCodes, Account.BILL_STATE_ALL, ChargeType.CHARGE_LEVEL_ALL, subscriberId, from, null, maximum);

		//max overflow 
		negativeTests(account, chargeCodes, Account.BILL_STATE_ALL, ChargeType.CHARGE_LEVEL_ALL, subscriberId, from, to, Integer.MAX_VALUE);
	}

	public void testRetrieval() throws Throwable {

		int banId = 12474;
		String subscriberId="4033946834";
		
		String [] chargeCodes = new String []{ "PNUM", };
		Calendar cal = Calendar.getInstance();
		cal.set(2002, 7,1); //2002 Aug 01
		Date from= cal.getTime();
		cal.set(2005, 9, 31); //2005 Oct 31
		Date to=cal.getTime();
		int maximum=100;
		
		Account account = api.getAccountManager().findAccountByBAN(banId);
		
		retrieveCharges(account, chargeCodes, subscriberId, from, to, maximum);
	}

	public void testRetrieval_DavidXu() throws Throwable {

		int banId = 12474;
		String subscriberId="4033946834";
		
		String [] chargeCodes = {"DCC200","DCC100"};
		Calendar cal = Calendar.getInstance();
		cal.set(2010, 6, 22); 
		Date from= cal.getTime();
		cal.set(2005, 9, 31); 
		Date to= new Date();
		int maximum=100;
		
		Account account = api.getAccountManager().findAccountByBAN(banId);
		
		retrieveCharges(account, chargeCodes, subscriberId, from, new Date(), maximum);
	}
	
	public void testRetrieval_Annie() throws Throwable {

		int banId = 70564897;
		String subscriberId="4161752651";
		
		String [] chargeCodes = {"DCC100", "ACT0", "MARC"};
		Calendar cal = Calendar.getInstance();
		cal.set(2010, 6, 26, 0, 0, 0); 
		Date from= cal.getTime();
		cal.set(2010, 6, 28, 23,59,59); 
		Date to= cal.getTime();
		int maximum=100;
		
		Account account = api.getAccountManager().findAccountByBAN(banId);
		
		retrieveCharges(account, chargeCodes, subscriberId, from, to, maximum);
	}
	
	private void negativeTests(Account account, String[] chargeCodes, String billState, char chargeLevel, String subscriberId, Date from, Date to, int maximum) {
	
		StringBuffer sb = new StringBuffer( "Input ")
			.append( " banId=").append( account.getBanId() )
			.append(" chargeCode=").append( (chargeCodes==null)? null : Arrays.asList(chargeCodes))
			.append( " billState=").append( billState )
			.append( " chargeLevel=").append( chargeLevel)
			.append( " subscriberId=").append( subscriberId )
			.append( " from=").append( from )
			.append( " to=").append( to )
			.append( " max=").append( maximum );
		
		try {
			
			account.getCharges(chargeCodes, billState, chargeLevel, subscriberId, from, to, maximum);
			System.out.println( "Negatvie test failed, " + sb.toString() + "  No exception");
			
		} catch (LimitExceededException e) {
			
			System.out.println( "Negatvie test passed, " + sb.toString() + "  LimitExceededException: " + e.getMessage());
		} catch ( IllegalArgumentException e ) {
			
			System.out.println( "Negatvie test passed, " + sb.toString() + "  IllegalArgumentException: " + e.getMessage());
		} catch (TelusAPIException e) {
			
			System.err.println( "Negatvie test failed, " + sb.toString() + "  TelusAPIException: " + e.getMessage());
		} catch ( Throwable e ) {
			System.err.println( "Negatvie test failed, " + sb.toString() + "  Unexpected : " + e.toString());
			
		}
	}
	
	private void retrieveCharges( Account account, String[] chargeCodes, String subscriberId, Date from, Date to, int maximum) throws Throwable {
		retrieveCharges(account, chargeCodes, ChargeType.CHARGE_LEVEL_ALL, 	 subscriberId, from, to, maximum );
		retrieveCharges(account, chargeCodes, ChargeType.CHARGE_LEVEL_ACCOUNT, 	  subscriberId, from, to, maximum );
		retrieveCharges(account, chargeCodes, ChargeType.CHARGE_LEVEL_SUBSCRIBER, subscriberId, from, to, maximum );
	}
	
	private void retrieveCharges( Account account, String[] chargeCodes, char chargeLevel, String subscriberId, Date from, Date to, int maximum) throws Throwable {
		
		
		retrieveCharges( account, chargeCodes, Account.BILL_STATE_BILLED, chargeLevel,  subscriberId, from, to, maximum);

		retrieveCharges( account, chargeCodes, Account.BILL_STATE_UNBILLED, chargeLevel,  subscriberId, from, to, maximum);
		
		retrieveCharges( account, chargeCodes,Account.BILL_STATE_ALL, chargeLevel,  subscriberId, from, to, maximum);
	}
	 
	private void retrieveCharges(Account account, String[] chargeCodes, String billState, char chargeLevel, String subscriberId, Date from, Date to, int maximum) throws Throwable {
		
		StringBuffer sb = new StringBuffer( "Input Criteria:")
			.append( " banId=").append( account.getBanId() )
			.append(" chargeCode=").append( (chargeCodes==null)? null : Arrays.asList(chargeCodes))
			.append( " billState=").append( billState )
			.append( " chargeLevel=").append( chargeLevel)
			.append( " subscriberId=").append( subscriberId )
			.append( " from=").append( from )
			.append( " to=").append( to )
			.append( " max=").append( maximum );
		
		Charge [] charges = account.getCharges(chargeCodes, billState, chargeLevel, subscriberId, from, to, maximum);
		System.out.println();
		System.out.println( sb.toString());
		System.out.println ( "Result [" + charges.length + "]" );
		printOut( charges);
	}

	private void printOut(Charge[] charges) {
		NumberFormat nf = new DecimalFormat("#");
		for ( int i=0; i<charges.length; i++ ) {
			Charge charge = charges[i];
			System.out.print( " id=" + nf.format(charge.getId() ));
			System.out.print( " amnt=" + charge.getAmount() );
			System.out.print (" chargeCode=" + charge.getChargeCode() );
			System.out.print( " creationDt=" + charge.getCreationDate() );
			System.out.print( " effDt=" + charge.getEffectiveDate() );
			System.out.print( " featureCode=" + charge.getFeatureCode());
			System.out.print( " featureRevCode=" + charge.getFeatureRevenueCode());
			System.out.print( " gst=" + charge.getGSTAmount());
			System.out.print( " hst=" + charge.getHSTAmount());
			System.out.print( " pst=" + charge.getPSTAmount());
			System.out.print( " converage=" + charge.getPeriodCoverageStartDate() + "-" +charge.getPeriodCoverageEndDate());
			System.out.print( " productType=" + charge.getProductType());
			System.out.print( " reasonCode=" + charge.getReasonCode());
			System.out.print( " roamingTax=" + charge.getRoamingTaxAmount());
			System.out.print( " serviceCode=" + charge.getServiceCode());
			System.out.print( " subscriberId=" + charge.getSubscriberId());
			System.out.print( " text=" + charge.getText());
			System.out.print( " isBalanceIgnored=" + charge.isBalanceIgnoreFlag());
			System.out.print( " isBalanceImpacted=" + charge.isBalanceImpactFlag());
			System.out.print( " isBilled=" + charge.isBilled());
			System.out.print( " isGSTExcempt=" + charge.isGSTExempt());
			System.out.print( " isHSTExcempt=" + charge.isHSTExempt());
			System.out.print( " isPSTExcempt=" + charge.isPSTExempt());
			System.out.print( " isRoamingTaxExcempt=" + charge.isRoamingTaxExempt());
			System.out.println();
		}
		
	}
	
	
}
