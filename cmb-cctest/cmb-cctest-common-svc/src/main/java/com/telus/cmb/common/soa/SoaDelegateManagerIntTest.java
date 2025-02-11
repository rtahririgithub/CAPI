/** 
 * This class is useless, because SoaDelegateManager retired in TFS
 */
package com.telus.cmb.common.soa;

public class SoaDelegateManagerIntTest {
}

/*
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.rpc.Stub;

import mockit.Deencapsulation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telus.cmb.common.IntTestPropertyConfig;
import com.telus.soa.client.gatewayagent.TelusSoaClientDelegate;
import com.telus.soa.client.gatewayagent.exception.ClientDelegateException;
import com.telus.ws.client.cardpayment.CardPaymentServicePortType;
import com.telus.ws.client.cardpayment.CardPaymentServicePortType_Stub;

/**
 * @author Canh Tran
 *
 
public class SoaDelegateManagerIntTest {
	/**
	 * Use binding key of CardPaymentService
	 *
	String bindingKey = "CardPaymentService_vs0_BK";		
	static SoaDelegateManager soaDelegateMgr;
	static String appDomain;
	static String appName;
	static String appPass;
	/**
	 * @throws java.lang.Exception
	 *
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		IntTestPropertyConfig.pt168();
		soaDelegateMgr = SoaDelegateManager.getInstance();		
		appDomain = Deencapsulation.getField(soaDelegateMgr, "DEFAULT_AUTHORIZATION_DOMAIN");
		appName = Deencapsulation.getField(soaDelegateMgr, "DEFAULT_APPLICATION_NAME");
		appPass = Deencapsulation.getField(soaDelegateMgr, "DEFAULT_APPLICATION_PASSWORD");		
	}

	/**
	 * @throws java.lang.Exception
	 *
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 *
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 *
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.telus.cmb.common.soa.SoaDelegateManager#getServiceInstance(java.lang.Class, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws ClientDelegateException 
	 *
	@Test
	public void testGetServiceInstanceClassOfTStringStringStringString() throws ClientDelegateException {
		CardPaymentServicePortType portType = soaDelegateMgr.getServiceInstance(CardPaymentServicePortType.class, bindingKey, appDomain, appName, appPass);
		assertTrue(portType instanceof CardPaymentServicePortType_Stub);
		assertNotNull(((Stub)portType)._getProperty(Stub.ENDPOINT_ADDRESS_PROPERTY)); 
	}

	/**
	 * Test method for {@link com.telus.cmb.common.soa.SoaDelegateManager#getServiceInstance(java.lang.Class, java.lang.String)}.
	 * @throws ClientDelegateException 
	 *
	@Test
	public void testGetServiceInstanceClassOfTString() throws ClientDelegateException {
		CardPaymentServicePortType portType = soaDelegateMgr.getServiceInstance(CardPaymentServicePortType.class, bindingKey);
		assertTrue(portType instanceof CardPaymentServicePortType_Stub);
		assertNotNull(((Stub)portType)._getProperty(Stub.ENDPOINT_ADDRESS_PROPERTY)); 
	}

	/**
	 * Test method for {@link com.telus.cmb.common.soa.SoaDelegateManager#getDelegate(java.lang.Class, java.lang.String)}.
	 *
	@Test
	public void testGetDelegateClassOfTString() {
		TelusSoaClientDelegate delegate = soaDelegateMgr.getDelegate(CardPaymentServicePortType.class, bindingKey);
		assertNotNull(delegate.toString()); 
		
	}

	/**
	 * Test method for {@link com.telus.cmb.common.soa.SoaDelegateManager#getDelegate(java.lang.Class, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 *
	@Test
	public void testGetDelegateClassOfTStringStringStringString() {
		TelusSoaClientDelegate delegate = soaDelegateMgr.getDelegate(CardPaymentServicePortType.class, bindingKey, appDomain, appName, appPass);
		assertNotNull(delegate.toString()); 
	}

}
*/
