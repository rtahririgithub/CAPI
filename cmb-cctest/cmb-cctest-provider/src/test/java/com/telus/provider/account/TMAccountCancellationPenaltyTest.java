package com.telus.provider.account;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

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

import com.telus.api.ApplicationException;
import com.telus.api.AuthenticationException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.CancellationPenalty;
//import com.telus.eas.account.ejb.AccountManagerEJBRemote;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.config1.info.ConfigurationInfo;
import com.telus.eas.framework.exception.TelusException;
//import com.telus.eas.subscriber.ejb.SubscriberManagerEJBRemote;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.monitoring.MethodMonitorFlagReader;
import com.telus.provider.monitoring.aspectj.ProviderAspect;

//account ejb 
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
//subscriber ejb
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;

public class TMAccountCancellationPenaltyTest extends TestCase {

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

	// class under test
	TMAccount tmAccount;

	CancellationPenaltyInfo cancellationPenaltyInfo = null;
	
	
	static{
		System.out.println("Testing class: TMAccount");
	}
	
	
	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		// instantiate cancellation penalty object
		cancellationPenaltyInfo = new CancellationPenaltyInfo();

		// suppress any static initializers in the TMProvider class
		new mockit.MockUp<TMProvider>() {
			@SuppressWarnings("unused")
			@mockit.Mock
			void $clinit() {
			}
		};

		new mockit.MockUp<TMAccountSummary>() {
			@SuppressWarnings("unused")
			@mockit.Mock
			void $clinit() {
				Deencapsulation.setField(TMAccountSummary.class,
						"LIGHT_WEIGHT_SUB_MAX", 9000);
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
				providerMock.getAccountLifecycleManager();//();getAccountManagerEJB();
				returns(accountLifecycleManager);

			}
		};

		//create instance of TMAccount
		tmAccount = new TMAccount(providerMock, delegate);

	}

	@Test
	public void testCancellationPenalty()
			throws AuthenticationException, TelusAPIException, RemoteException,
			IllegalArgumentException, IllegalAccessException, ApplicationException {

		System.out
				.println("*****************Started:testCancellationPenalty*******");

		// declare the cancellation penalty object
		CancellationPenalty cp = null;

		// mock configuration method to retrieve some mock value
		Mockito.when(
				configurationInfo.getPropertyAsInt(Mockito.anyString(),
						Mockito.anyInt())).thenReturn(9000);

		// mock ejb method to retrieve the cancellationpenaltyInfo obj
		Mockito.when(
				accountLifecycleManager.retrieveCancellationPenalty(Mockito
						.anyInt(),Mockito
						.anyString())).thenReturn(cancellationPenaltyInfo);

		// test method
		cp = tmAccount.getCancellationPenalty();
		assertEquals(cancellationPenaltyInfo, cp);

		// cancellation penalty object is null so method should be called
		// once(expected)
		new Verifications() {
			{

				providerMock.getAccountLifecycleManager();
				times = 1;

			}
		};

		// cancellation penalty object is null so method should be called only
		// once(expected)
		Mockito.verify(accountLifecycleManager, Mockito.times(1))
				.retrieveCancellationPenalty(Mockito.anyInt(),Mockito
						.anyString());

		// call the same method again. In this case cancellation penalty object
		// is not null.
	
			cp = tmAccount.getCancellationPenalty();
			assertEquals((CancellationPenalty) cancellationPenaltyInfo, cp);

		// cancellation penalty object is not null so method should be called
		// only once(expected)
		new Verifications() {
			{

				providerMock.getAccountLifecycleManager();
				times = 1;

			}
		};

		// cancellation penalty object is not null so method should be called
		// only once(expected)
		Mockito.verify(accountLifecycleManager, Mockito.times(1))
				.retrieveCancellationPenalty(Mockito.anyInt(),Mockito.anyString());

		System.out
				.println("*****************End:testCancellationPenalty*******");
	}

	
	
	@Test
	public void testCancellationPenaltyWithNotNullObj()
			throws AuthenticationException, TelusAPIException, RemoteException,
			IllegalArgumentException, IllegalAccessException, ApplicationException {

		System.out
				.println("\n*****************Started:testCancellationPenaltyWithNotNullObj*******");

		// mock configuration method to retrieve some mock value
		Mockito.when(
				configurationInfo.getPropertyAsInt(Mockito.anyString(),
						Mockito.anyInt())).thenReturn(9000);

		// mock ejb method to retrieve the cancellationpenaltyInfo obj
		Mockito.when(
				accountLifecycleManager.retrieveCancellationPenalty(Mockito
						.anyInt(),Mockito.anyString())).thenReturn(cancellationPenaltyInfo);

		// set the private field to and object
		Deencapsulation.setField(tmAccount, "cancellationPenalty",
				new CancellationPenaltyInfo());
		// test method
		CancellationPenalty cp = tmAccount.getCancellationPenalty();

		// cancellation penalty object is null so method should be called
		// once(expected)
		new Verifications() {
			{

				providerMock.getAccountLifecycleManager();
				times = 0;

			}
		};

		// cancellation penalty object is null so method should be called only
		// once(expected)
		Mockito.verify(accountLifecycleManager, Mockito.never())
				.retrieveCancellationPenalty(Mockito.anyInt(),Mockito.anyString());

		System.out
				.println("*****************End:testCancellationPenaltyWithNotNullObj*******");
	}

	
	
	@Test
	public void testCancellationPenaltyWithTelusException()
			throws AuthenticationException, TelusAPIException, RemoteException, ApplicationException {

		System.out
				.println("\n*****************Started:testCancellationPenaltyWithTelusException*******");

		// declare the cancellation penalty object
		CancellationPenalty cp = null;

		// mock the method to return some dummy value
		Mockito.when(
				configurationInfo.getPropertyAsInt(Mockito.anyString(),
						Mockito.anyInt())).thenReturn(9000);

		// instantiate a new telusException object
		TelusException tEx = new TelusException("Telus Exception occured");

		// mock ejb method to
		Mockito.when(
				accountLifecycleManager.retrieveCancellationPenalty(Mockito
						.anyInt(),Mockito.anyString())).thenThrow(tEx);

		try {
			// test method with expected telus api exception
			cp = tmAccount.getCancellationPenalty();
			assertEquals(cancellationPenaltyInfo, cp);

		} catch (TelusAPIException e) {
			assertEquals(tEx, e);

		} catch (Exception e) {
			e.printStackTrace();
			assertEquals(tEx, e);
		}

		// test if method called only once(expected)
		new Verifications() {
			{

				providerMock.getAccountLifecycleManager();
				times = 1;

			}
		};

		// test if method called only once(exptected)
		Mockito.verify(accountLifecycleManager, Mockito.times(1))
				.retrieveCancellationPenalty(Mockito.anyInt(),Mockito.anyString());

		System.out
				.println("*****************End:testCancellationPenaltyWithTelusException*******");
	}

	
	
	@Test
	public void testCancellationPenaltyList()
			throws AuthenticationException, TelusAPIException, RemoteException, ApplicationException {

		System.out
				.println("\n*****************Started:testCancellationPenaltyList*******");

		// mock method to return some dummy value
		Mockito.when(
				configurationInfo.getPropertyAsInt(Mockito.anyString(),
						Mockito.anyInt())).thenReturn(9000);

		// declare the cancellation penalty list
		List cancellationPenaltyInfoList = new ArrayList();
		cancellationPenaltyInfoList.add(new CancellationPenaltyInfo());

		CancellationPenalty[] infos = null;
		infos = (CancellationPenaltyInfo[]) cancellationPenaltyInfoList
				.toArray(new CancellationPenaltyInfo[0]);

		// mock ejb method to return the object array
		Mockito.when(
				accountLifecycleManager.retrieveCancellationPenaltyList(
						Mockito.anyInt(), Mockito.any(String[].class),Mockito.anyString()))
				.thenReturn((CancellationPenaltyInfo[]) infos);

		// mock ban id
		Mockito.when(tmAccount.getAccount().getBanId()).thenReturn(100000);

		// test method
		CancellationPenalty cp[] = tmAccount
				.getCancellationPenaltyList(new String[] {});

		// exptected to return array of object
		assertEquals(infos, cp);

		// expected that the mathod will call only once
		new Verifications() {
			{

				providerMock.getAccountLifecycleManager();
				times = 1;

			}
		};

		// expected that the mathod will call only once
		Mockito.verify(accountLifecycleManager, Mockito.times(1))
				.retrieveCancellationPenaltyList(Mockito.anyInt(),
						Mockito.any(String[].class),Mockito.anyString());
		System.out
				.println("*****************End:testCancellationPenaltyList*******");
	}

	
	
	@After
	public void tearDown() throws Exception {
	}
}
