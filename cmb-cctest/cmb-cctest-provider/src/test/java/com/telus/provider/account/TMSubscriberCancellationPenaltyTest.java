package com.telus.provider.account;

import junit.framework.TestCase;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.telus.api.TelusAPIException;
import com.telus.api.account.CancellationPenalty;
//import com.telus.eas.account.ejb.AccountManagerEJBRemote;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.config1.info.ConfigurationInfo;
import com.telus.eas.framework.exception.TelusException;
//import com.telus.eas.subscriber.ejb.SubscriberManagerEJBRemote;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.monitoring.MethodMonitorFlagReader;
import com.telus.provider.monitoring.aspectj.ProviderAspect;

public class TMSubscriberCancellationPenaltyTest extends TestCase {

	@Mocked
	TMProvider providerMock;
	@Mock
	SubscriberInfo subscriberInfo;
	@Mocked
	ProviderAspect providerAspectMock;
	@Mock
	SubscriberLifecycleManager subscriberLifecycleManager;
	@Mock
	MethodMonitorFlagReader methodMonitorFlagReaderMock;
	@Mock
	AccountInfo delegate;
	@Mock
	ConfigurationInfo configurationInfo;
	@Mock
	AccountLifecycleManager accountLifecycleManager;

	// classes under test
	TMSubscriber tMSubscriber;

	CancellationPenaltyInfo cancellationPenaltyInfo = null;

	static{
		System.out.println("Testing class: TMSubscriber");
	}
	
	@Before
	public void setUp() throws Exception {

		
		
		MockitoAnnotations.initMocks(this);

		// instantiate cancellation penalty object
		cancellationPenaltyInfo = new CancellationPenaltyInfo();

		// suppress any static initializers in the TMSubscriber class
		new mockit.MockUp<TMSubscriber>() {
			@SuppressWarnings("unused")
			@mockit.Mock
			void $clinit() {

			}
		};

		// suppress any static initializers in the TMProvider class
		new mockit.MockUp<TMProvider>() {
			@SuppressWarnings("unused")
			@mockit.Mock
			void $clinit() {
			}
		};

		new NonStrictExpectations() {
			{
				// mocking static method aspectOf
				invoke(ProviderAspect.class, "aspectOf");
				returns(providerAspectMock);

				// mock method of provider aspect mock
				providerAspectMock.getMethodMonitorFlagReader();
				returns(methodMonitorFlagReaderMock);

				// mock provider ejb reference retrieval to get mock ejb
				// interface
				providerMock.getSubscriberLifecycleManager();
				returns(subscriberLifecycleManager);

			}
		};

		// instantiate classes under test(TMAccount and TMSubscriber)
		tMSubscriber = new TMSubscriber(providerMock, subscriberInfo, true,
				"dummy");

	}

	@Test
	public void testCancellationPenalty() throws Exception {

		System.out
				.println("**************Started:testCancellationPenalty*******");

		// declare object
		CancellationPenalty cp = null;

		// mock ejb method to return some obj
		Mockito.when(
				subscriberLifecycleManager.retrieveCancellationPenalty(
						Mockito.anyInt(), Mockito.anyString(),
						Mockito.anyString(),Mockito.anyString())).thenReturn(
				cancellationPenaltyInfo);

		// calling the subscriber cancellation penalty method
		cp = tMSubscriber.getCancellationPenalty();
		assertEquals(cancellationPenaltyInfo, cp);

		// expected that the method will call only once
		new Verifications() {
			{

				providerMock.getSubscriberLifecycleManager();
				times = 1;

			}
		};

		// expected that the method will call only once
		Mockito.verify(subscriberLifecycleManager, Mockito.times(1))
				.retrieveCancellationPenalty(Mockito.anyInt(),
						Mockito.anyString(),Mockito.anyString(), Mockito.anyString());

		
		// test method again
		cp = tMSubscriber.getCancellationPenalty();	
		assertEquals((CancellationPenalty) cancellationPenaltyInfo, cp);

		// expected that the method will call only once because
		// CancellationPenalty obj will be non null
		new Verifications() {
			{

				providerMock.getSubscriberLifecycleManager();
				times = 1;

			}
		};

		// expected that the method will call only once because
		// CancellationPenalty obj will be non null
		Mockito.verify(subscriberLifecycleManager, Mockito.times(1))
				.retrieveCancellationPenalty(Mockito.anyInt(),
						Mockito.anyString(),Mockito.anyString(), Mockito.anyString());

		System.out
				.println("**************End:testCancellationPenalty*******");

	}
	


	@Test
	public void testCancellationPenaltyWithNotNullObj() throws Exception {

		System.out
				.println("\n**************Started:testCancellationPenaltyWithNotNullObj*******");

		// declare the obj
		CancellationPenalty cp = null;

		// mock ejb method to return the mock obj
		Mockito.when(
				subscriberLifecycleManager.retrieveCancellationPenalty(
						Mockito.anyInt(), Mockito.anyString(),Mockito.anyString(),
						Mockito.anyString())).thenReturn(
				cancellationPenaltyInfo);

		// set the private field to and object
		Deencapsulation.setField(tMSubscriber, "cancellationPenalty",
				new CancellationPenaltyInfo());
		
		// calling the subscriber cancellation penalty method
		cp = tMSubscriber.getCancellationPenalty();

		// expected that the method should never be called
		new Verifications() {
			{

				providerMock.getSubscriberLifecycleManager();
				times = 0;

			}
		};

		// expected that the method should never be called
		Mockito.verify(subscriberLifecycleManager, Mockito.never())
				.retrieveCancellationPenalty(Mockito.anyInt(),
						Mockito.anyString(), Mockito.anyString(),Mockito.anyString());

		System.out
				.println("**************End:testCancellationPenaltyWithNotNullObj*******");

	}

	@Test
	public void testCancellationPenaltyWithTelusAPIException() throws Exception {

		System.out
				.println("\n**************Started:testCancellationPenaltyWithTelusAPIException*******");

		// decalre obj
		CancellationPenalty cp = null;

		// create new exception object
		TelusException tEx = new TelusException("Telus Exception occured");

		// mock ejb method to return exception
		Mockito.when(
				subscriberLifecycleManager.retrieveCancellationPenalty(
						Mockito.anyInt(), Mockito.anyString(),Mockito.anyString(),
						Mockito.anyString())).thenThrow(tEx);
		Exception exc = null;
		try {
			// test class
			cp = tMSubscriber.getCancellationPenalty();
			assertEquals(cancellationPenaltyInfo, cp);
			// System.out.println("Passed");
		} catch (TelusAPIException ex) {
			exc = ex;
			assertNotNull("Telust API Exception occured", exc);
			// assertEquals(tEx.getMessage(), ex.getMessage());
		} catch (Exception ex) {
			// System.out.println("Exception occured");
			assertEquals("Exception occured; ", ex.getMessage());
		} catch (Throwable ex) {
			// System.out.println("Exception occured");
			assertEquals("Exception occured; ", ex.getMessage());
		}

		// expected to call only once
		new Verifications() {
			{

				providerMock.getSubscriberLifecycleManager();
				times = 1;

			}
		};

		// expected to call only once
		Mockito.verify(subscriberLifecycleManager, Mockito.times(1))
				.retrieveCancellationPenalty(Mockito.anyInt(),
						Mockito.anyString(), Mockito.anyString(),Mockito.anyString());

		System.out
				.println("**************End:testCancellationPenaltyWithTelusAPIException*******");

	}

	@After
	public void tearDown() throws Exception {
	}
}
