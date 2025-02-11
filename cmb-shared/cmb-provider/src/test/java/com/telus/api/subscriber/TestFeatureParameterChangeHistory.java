package com.telus.api.subscriber;

import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.FeatureParameterHistory;
import com.telus.api.account.Subscriber;

/**
 * This unit test contains test cases for WCP_Q1_2010_SELF-SERVE project
 * @author tongts
 *
 */
public class TestFeatureParameterChangeHistory extends BaseTest {

	String testSubscriberNumber = "6472104749";
	int testBAN = 0;
	
	static {
		setupEASECA_QA();
	}
	
	public TestFeatureParameterChangeHistory(String name) throws Throwable {
		super(name);
	}

	
	/**
	 * Description: Retrieve feature parameter change history for a start date and end date in the past where start date < end date
	 * Input Data: start date < end date; both in the past
	 * Expected Result: valid result set
	 * @Test
	 * @throws Throwable
	 */
	public void testCase01() throws Throwable {
		System.out.println("tsetCase01..");
		Date fromDate = new Date(2002-1900, 1, 1);
		Date toDate = new Date(2009-1900, 12, 31);

		getFeatureParameterChangeHistory (testSubscriberNumber, fromDate, toDate);
	}
	
	/**
	 * Description: Retrieve feature parameter change history for a start date and end date in the past where start date > end date
	 * Input Data: start date > end date; both in the past
	 * Expected Result: no results
	 * @throws Throwable
	 */
	public void testCase02() throws Throwable {
		System.out.println("tsetCase02..");
		Date fromDate = new Date(2009-1900, 12, 31);
		Date toDate = new Date(2002-1900, 1, 1);

		getFeatureParameterChangeHistory (testSubscriberNumber, fromDate, toDate);
	}
	
	/**
	 * Description: Retrieve feature parameter change history for a start date and end date in the future where start date < end date
	 * Input Data: start date < end date; both in the future 
	 * Expected Result: no results
	 * @throws Throwable
	 */
	public void testCase03() throws Throwable {
		System.out.println("tsetCase03..");
		Date fromDate = new Date(2010-1900, 12, 31);
		Date toDate = new Date(2011-1900, 12, 31);

		getFeatureParameterChangeHistory (testSubscriberNumber, fromDate, toDate);
	}
	
	/**
	 * Description: Retrieve feature parameter change history for a start date and end date in the future where start date > end date
	 * Input Data: start date > end date; both in the future
	 * Expected Result: no results
	 * @throws Throwable
	 */
	public void testCase04() throws Throwable {
		System.out.println("tsetCase04..");
		Date fromDate = new Date(2011-1900, 12, 31);
		Date toDate = new Date(2010-1900, 12, 31);

		getFeatureParameterChangeHistory (testSubscriberNumber, fromDate, toDate);
	}
	
	/**
	 * Description: start date in the past, end date in the future
	 * Input Data: start in the past, end in the future
	 * Expected Result: results from past to present
	 * @throws Throwable
	 */
	public void testCase05() throws Throwable {
		System.out.println("tsetCase05..");
		Date fromDate = new Date(2002-1900, 1, 1);
		Date toDate = new Date(2010-1900, 12, 31);

		getFeatureParameterChangeHistory (testSubscriberNumber, fromDate, toDate);
	}
	
	/**
	 * Description: end date in the past, start date in the future
	 * Input Data: end in the past, start in the future
	 * Expected Result: no results
	 * @throws Throwable
	 */
	public void testCase06() throws Throwable {
		System.out.println("tsetCase06..");
		Date fromDate = new Date(2010-1900, 12, 31);
		Date toDate = new Date(2002-1900, 1, 1);

		getFeatureParameterChangeHistory (testSubscriberNumber, fromDate, toDate);
	}
	
	/**
	 * Description: Show feature parameter change history for varying SOCs including visual voicemail, birthday calling and calling circle
	 * Input Data: subscriber with with feature parameter changes
	 * Expected Result: accurate feature parameter change history
	 * @throws Throwable
	 */
	public void testCase07() throws Throwable {
		System.out.println("tsetCase07..");
		Date fromDate = new Date(2002-1900, 1, 1);
		Date toDate = new Date(2009-1900, 12, 31);

		getFeatureParameterChangeHistory (testSubscriberNumber, fromDate, toDate);
		
	}
	
	/**
	 * Description: No start date
	 * Input Data: no start date
	 * Expected Result: descriptive error message
	 * @throws Throwable
	 */
	public void testCase08() throws Throwable {
		System.out.println("tsetCase08..");
		Date fromDate = null;
		Date toDate = new Date(2009-1900, 12, 31);
		try{	
			getFeatureParameterChangeHistory (testSubscriberNumber, fromDate, toDate);
		}catch(TelusAPIException e){
			assertEquals("The Start Date and End Date cannot be null.", e.getMessage());
		}
	}
	
	/**
	 * Description: No end date
	 * Input Data: no end date
	 * Expected Result: descriptive error message
	 * @throws Throwable
	 */
	public void testCase09() throws Throwable {
		System.out.println("tsetCase09..");
		Date fromDate = new Date(2002-1900, 1, 1);
		Date toDate = null;
		try{
		getFeatureParameterChangeHistory (testSubscriberNumber, fromDate, toDate);
		
		}catch(TelusAPIException e){
			assertEquals("The Start Date and End Date cannot be null.", e.getMessage());
		}
	}
	
	/**
	 * Description: No start date or end date
	 * Input Data: no start or end date
	 * Expected Result: descriptive error message
	 * @throws Throwable
	 */
	public void testCase10() throws Throwable {
		System.out.println("tsetCase10..");
		Date fromDate = null;
		Date toDate = null;
		try{
			getFeatureParameterChangeHistory (testSubscriberNumber, fromDate, toDate);
		}catch(TelusAPIException e){
			assertEquals("The Start Date and End Date cannot be null.", e.getMessage());
		}
	}
	
	private void getFeatureParameterChangeHistory(String subscriber, Date fromDate, Date toDate) throws Throwable {
		Subscriber sub = api.getAccountManager().findSubscriberByPhoneNumber(subscriber);
		FeatureParameterHistory[] history = sub.getFeatureParameterChangeHistory(fromDate, toDate);
		//FeatureParameterHistory[] history = sub.getFeatureParameterHistory(new String[]{}, fromDate, toDate);
		if (history != null && history.length > 0) {
			for (int i = 0 ; i < history.length; i++) {
				FeatureParameterHistory hist = history[i];
				System.out.println(hist.getCreationDate()+","+hist.getParameterName()+","+hist.getParameterValue()+","+hist.getServiceCode());
			}
		}else {
			System.out.println("FeatureParameterHistory is null or empty.");
		}
	}

}
