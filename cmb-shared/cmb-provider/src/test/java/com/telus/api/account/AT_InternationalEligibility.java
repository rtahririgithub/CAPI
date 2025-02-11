package com.telus.api.account;

import com.telus.api.BaseTest;
import com.telus.api.BrandNotSupportedException;
import com.telus.api.TelusAPIException;
import com.telus.api.reference.Brand;
import com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria;
import com.telus.provider.eligibility.interservice.impl.rules.InternationalServiceEligibilityEvaluationStrategy;
import com.telus.provider.util.AppConfiguration;

/**
 * This class's primary goal is to test the Rule, so I take a shortcut to directly create/populate EligibilityCheckCriteria, 
 * and then use the InternationalServiceEligibilityCheckStrategy to evaluate the criteria to get the result.
 * 
 * This will avoid finding account matching different scenarios, which in Development env, is extremely difficult to do. 
 *
 */
public class AT_InternationalEligibility extends BaseTest {

	static {
		//setupSMARTDESKTOP_D3();
		 setupEASECA_QA();
	}
	
	public AT_InternationalEligibility(String name) throws Throwable {
		super(name);
		System.out.println( "haha");
	}
	
	
	private InternationalServiceEligibilityEvaluationStrategy internationalServiceEligibilityEvaluationStrategy;
	public void setUp() throws Exception {
		super.setUp();
		 if (internationalServiceEligibilityEvaluationStrategy == null) {
			 internationalServiceEligibilityEvaluationStrategy = (InternationalServiceEligibilityEvaluationStrategy) AppConfiguration.getInternationalServiceEligibilityEvaluationStrategy();
		 }
	}

	public void testKoodoCreditClassCR_HolbornW2() throws UnknownBANException, BrandNotSupportedException, TelusAPIException {
		
		do_test_HolbornW2_koodoCR_testCase1();
		do_test_HolbornW2_koodoCR_testCase2();
		do_test_HolbornW2_koodoCR_testCase3();
		do_test_HolbornW2_koodoCR_testCase4();
	}

	private void do_test_HolbornW2_koodoCR_testCase1() throws TelusAPIException {

		String testContext = "new Koodo account - ";

		InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();

		criteria.setAccountCombinedType("IR");
		criteria.setBrandId(Brand.BRAND_ID_KOODO);
		criteria.setTenure(0);
		criteria.setCollectionActivityPresent(false);
		criteria.setNewAccount(true);

		criteria.setCreditClass("X");
		InternationalServiceEligibilityCheckResult classXResult = (InternationalServiceEligibilityCheckResult)internationalServiceEligibilityEvaluationStrategy.evaluate(criteria);

		criteria.setCreditClass("L");
		InternationalServiceEligibilityCheckResult classLResult = (InternationalServiceEligibilityCheckResult)internationalServiceEligibilityEvaluationStrategy.evaluate(criteria);
		assertEquals(testContext + "deposit amount", 0.0d, classLResult.getDepositAmount(), 0.0);
		assertEquals(testContext + "dialing", true, classLResult.isEligibleForInternationalDialing());
		assertEquals(testContext + "roaming", false, classLResult.isEligibleForInternationalRoaming());
		
		//CR says, Credit class L's rules are supposed to be identical as credit class X, so compare the result
		assertEquals(testContext + "deposit amount", classXResult.getDepositAmount(), classLResult.getDepositAmount(), 0.0);
		assertEquals(testContext + "dialing", classXResult.isEligibleForInternationalDialing(), classLResult.isEligibleForInternationalDialing());
		assertEquals(testContext + "roaming", classXResult.isEligibleForInternationalRoaming(), classLResult.isEligibleForInternationalRoaming());
		
	}
	
	private void do_test_HolbornW2_koodoCR_testCase2() throws TelusAPIException {
		
		String testContext = "Koodo account >6month and no collection ";
		
		InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();

		criteria.setAccountCombinedType("IR");
		criteria.setBrandId(Brand.BRAND_ID_KOODO);
		criteria.setTenure(7);
		criteria.setCollectionActivityPresent(false);
		criteria.setNewAccount(false);
		
		criteria.setCreditClass("X");
		InternationalServiceEligibilityCheckResult classXResult = (InternationalServiceEligibilityCheckResult)internationalServiceEligibilityEvaluationStrategy.evaluate(criteria);

		criteria.setCreditClass("L");
		InternationalServiceEligibilityCheckResult classLResult = (InternationalServiceEligibilityCheckResult)internationalServiceEligibilityEvaluationStrategy.evaluate(criteria);
		assertEquals( testContext + "deposit amount", 0.0d, classLResult.getDepositAmount(), 0.0);
		assertEquals( testContext + "dialing", true, classLResult.isEligibleForInternationalDialing());
		assertEquals( testContext + "roaming", true, classLResult.isEligibleForInternationalRoaming());
		
		//CR says, Credit class L's rules are supposed to be identical as credit class X, so compare the result
		assertEquals(testContext + "deposit amount", classXResult.getDepositAmount(), classLResult.getDepositAmount(), 0.0);
		assertEquals(testContext + "dialing", classXResult.isEligibleForInternationalDialing(), classLResult.isEligibleForInternationalDialing());
		assertEquals(testContext + "roaming", classXResult.isEligibleForInternationalRoaming(), classLResult.isEligibleForInternationalRoaming());
	}
	
	private void do_test_HolbornW2_koodoCR_testCase3() throws TelusAPIException {
		
		String testContext = "Koodo account >6month but has collection ";
		
		InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();

		criteria.setAccountCombinedType("IR");
		criteria.setBrandId(Brand.BRAND_ID_KOODO);
		criteria.setTenure(7);
		criteria.setCollectionActivityPresent(true);
		criteria.setNewAccount(false);
		
		criteria.setCreditClass("X");
		InternationalServiceEligibilityCheckResult classXResult = (InternationalServiceEligibilityCheckResult)internationalServiceEligibilityEvaluationStrategy.evaluate(criteria);

		criteria.setCreditClass("L");
		InternationalServiceEligibilityCheckResult classLResult = (InternationalServiceEligibilityCheckResult)internationalServiceEligibilityEvaluationStrategy.evaluate(criteria);
		assertEquals( testContext + "deposit amount", 0.0d, classLResult.getDepositAmount(), 0.0);
		assertEquals( testContext + "dialing", true, classLResult.isEligibleForInternationalDialing());
		assertEquals( testContext + "roaming", false, classLResult.isEligibleForInternationalRoaming());
		
		
		//CR says, Credit class L's rules are supposed to be identical as credit class X, so compare the result
		assertEquals(testContext + "deposit amount", classXResult.getDepositAmount(), classLResult.getDepositAmount(), 0.0);
		assertEquals(testContext + "dialing", classXResult.isEligibleForInternationalDialing(), classLResult.isEligibleForInternationalDialing());
		assertEquals(testContext + "roaming", classXResult.isEligibleForInternationalRoaming(), classLResult.isEligibleForInternationalRoaming());
		
	}
	
	private void do_test_HolbornW2_koodoCR_testCase4() throws TelusAPIException {
		
		String testContext = "Koodo account <=6month ";
		
		InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();

		criteria.setAccountCombinedType("IR");
		criteria.setBrandId(Brand.BRAND_ID_KOODO);
		criteria.setTenure(6);
		criteria.setCollectionActivityPresent(false);
		criteria.setNewAccount(false);
		
		criteria.setCreditClass("X");
		InternationalServiceEligibilityCheckResult classXResult = (InternationalServiceEligibilityCheckResult)internationalServiceEligibilityEvaluationStrategy.evaluate(criteria);

		criteria.setCreditClass("L");
		InternationalServiceEligibilityCheckResult classLResult = (InternationalServiceEligibilityCheckResult)internationalServiceEligibilityEvaluationStrategy.evaluate(criteria);
		assertEquals( testContext + "deposit amount", 0.0d, classLResult.getDepositAmount(), 0.0);
		assertEquals( testContext + "dialing", true, classLResult.isEligibleForInternationalDialing());
		assertEquals( testContext + "roaming", false, classLResult.isEligibleForInternationalRoaming());
		
		//CR says, Credit class L's rules are supposed to be identical as credit class X, so compare the result
		assertEquals(testContext + "deposit amount", classXResult.getDepositAmount(), classLResult.getDepositAmount(), 0.0);
		assertEquals(testContext + "dialing", classXResult.isEligibleForInternationalDialing(), classLResult.isEligibleForInternationalDialing());
		assertEquals(testContext + "roaming", classXResult.isEligibleForInternationalRoaming(), classLResult.isEligibleForInternationalRoaming());
		
	}
	
}
