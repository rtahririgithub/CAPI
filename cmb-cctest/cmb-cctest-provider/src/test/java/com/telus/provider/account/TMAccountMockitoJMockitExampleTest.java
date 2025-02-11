package com.telus.provider.account;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.CreditCard;
import com.telus.api.account.UnknownBANException;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.utility.info.PaymentSourceTypeInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.interaction.TMInteractionManager;
import com.telus.provider.monitoring.MethodMonitorFlagReader;
import com.telus.provider.monitoring.aspectj.ProviderAspect;

/**
 * This class is an example test that uses a mix of Mockito and JMockit.
 * The Mockito syntax is simple and natural to use but it does not allow
 * mocking of private/static/constructors.  PowerMock with Mockito can do
 * this but it has been determined that PowerMock has performance issues
 * requiring upwards of 12 seconds to initialise.  Using JMockit reduces
 * the startup to less than 1 second.
 * 
 * This example serves as a template for others to understand.
 * 
 * @author Canh Tran
 *
 */
public class TMAccountMockitoJMockitExampleTest {
	
	// We are using a combination of Mockito and JMockit mocks in this test class.
	// @Mocked are JMockit mocks, @Mock are Mockito mocks
	// JMockit provides the same capabilities as Mockito
	@Mocked TMProvider providerMock;
	@Mocked AccountInfo accountInfoMock;
	@Mocked ProviderAspect providerAspectMock;
	
	@Mock CreditCard creditCardMock;	
	@Mock MethodMonitorFlagReader methodMonitorFlagReaderMock;	
	@Mock AuditHeader auditHeaderMock;
	@Mock ReferenceDataManager referenceDataManagerMock;
	@Mock AccountLifecycleManager accountLifecycleManagerMOck;
	@Mock TMInteractionManager tmInteractionMgrMock;	
	
	// class under test
	TMAccount cutAccount;	

	@Before
	public void setUp() throws Exception {		
		MockitoAnnotations.initMocks(this);
		
		// this is used to suppress any static initializers in the TMAccountSummary class
		new mockit.MockUp<TMAccountSummary>() {
			@SuppressWarnings("unused")
			@mockit.Mock
			void $clinit() {
			}
		};				
		
		// Mock out the ProviderAspect that will try to log and determine whether to
		// log through an LDAP flag.  This mocks the MethodMonitorFlagReader so that
		// a call to isMethodMonitoringEnabled() is false.		
		new NonStrictExpectations() {
			{
				invoke(ProviderAspect.class, "aspectOf"); returns(providerAspectMock);
				providerAspectMock.getMethodMonitorFlagReader(); returns(methodMonitorFlagReaderMock);
			}
		};
		
		// class under test
		cutAccount = new TMAccount(providerMock, accountInfoMock);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void testPayDeposit() throws Exception {
		int subscriberCount = 1;
		double amount = 0.01;			
		String businessRole = "bizRole";
		AuditHeader auditHeader = null;						
		
		try {
			cutAccount.payDeposit(subscriberCount, amount, creditCardMock, businessRole, auditHeader);
			fail("Expected UnknownBANException");
		} catch (UnknownBANException ex) {
			assertEquals("This Account has not yet been created", ex.getMessage());
		}
		
		new NonStrictExpectations() {
			{
				accountInfoMock.getBanId(); returns(1);
			}
		};
		
		// if we supply a null AuditHeader, we should expect a TelusAPIException
		try {
			cutAccount.payDeposit(subscriberCount, amount, creditCardMock, businessRole, auditHeader);
			fail ("Expected TelusAPIException");
		} catch (TelusAPIException ex) {
			assertEquals("The required AuditHeader is missing", ex.getMessage());
		}		
		
		new NonStrictExpectations() {
			{
				providerMock.getReferenceDataManager(); returns(referenceDataManagerMock);
			}
		};
				
		// tries to retrieve the paymentSourceType from ReferenceDataManager
		try {
			cutAccount.payDeposit(subscriberCount, amount, creditCardMock, businessRole, auditHeaderMock);
			fail("Expected UnknownObjectException");
		} catch (UnknownObjectException ex) {
			assertEquals("Unknown PaymentSourceType: [sourceID=DEPOSIT, sourceType=I]", ex.getMessage());
		}

		Mockito.when(referenceDataManagerMock.getPaymentSourceType(Mockito.anyString(), Mockito.anyString())).thenReturn(new PaymentSourceTypeInfo());
		
		TMCreditCard creditCard = Mockito.mock(TMCreditCard.class);
		try {
			cutAccount.payDeposit(subscriberCount, amount, creditCard, businessRole, auditHeaderMock);
			fail("Expected TelusAPIException");
		} catch (TelusAPIException ex) {
			assertEquals("CreditCard token is missing", ex.getMessage());
		}
		
		Mockito.when(creditCard.hasToken()).thenReturn(true);
		Mockito.when(creditCard.getCreditCardTransactionInfo()).thenReturn(new CreditCardTransactionInfo());
		
		new NonStrictExpectations() {
			{
				providerMock.getAccountLifecycleManager(); returns(accountLifecycleManagerMOck);
				providerMock.getInteractionManager0(); returns(tmInteractionMgrMock);
				providerMock.getUser(); returns("12345"); // possible bug; implicit conversion from String to int
			}
		};
		
		cutAccount.payDeposit(subscriberCount, amount, creditCard, businessRole, auditHeaderMock);
		
	}
}
