package com.telus.cmb.subscriber.lifecyclefacade.svc.impl;

public class SubscriberLifeCycleFacadeV3SIMTest {

	
/** Useless anymore
 * 
import static org.junit.Assert.*;

import mockit.Mock;
import mockit.MockUp;

import org.junit.Before;
import org.junit.Test;

import com.telus.api.reference.NetworkType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.logicalresourceservicerequestresponsetypes_v2.ChangeNetwork;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.logicalresourceservicerequestresponsetypes_v2.ChangeNetworkResponse;
import com.telus.tmi.xmlschema.xsd.resource.resource.commonlogicalresources_v3_0.TelephoneNumberType;
import com.telus.tmi.xmlschema.xsd.resource.resource.commonlogicalresources_v3_0.TnProvisioningStatusType;
import com.telus.wsdl.rmo.inventorymgmt.logicalresourceservice_2.LogicalResourceServicePortType;

public class SubscriberLifeCycleFacadeV3SIMTest {
	private static final String PHONE_NUMBER = "4165556666";
	private static final String LOCAL_IMSI = "localimsi";
	private static final String EMPTY_REMOTE_IMSI = "";
	private static final String NULL_REMOTE_IMSI = null;
	private static final String PROVISIONING_STATUS = "AA";
	private static final String USIM_ID = "1234567890123456";

	SubscriberLifecycleFacadeImpl facade = null;

	@Before
	public void setup() {
		facade = new SubscriberLifecycleFacadeImpl();
	}
	
	@Test
	public void assignTNResources() throws Exception {
		final LogicalResourceServicePortType logicalResourceServicePortType = new MockUp<LogicalResourceServicePortType>()
		{
			@SuppressWarnings("unused")
			@Mock
			TelephoneNumberType assignTNResources(
					com.telus.tmi.xmlschema.xsd.resource.resource.commonlogicalresources_v3_0.TelephoneNumberType TelephoneNumber, 
					com.telus.tmi.xmlschema.xsd.resource.resource.commonlogicalresources_v3_0.NetworkType NetworkType, 
					String LocalIMSI, String RemoteIMSI) {
				assertEquals(LocalIMSI, LOCAL_IMSI);
				assertEquals(RemoteIMSI, EMPTY_REMOTE_IMSI);
				return null;
			}
		}.getMockInstance();
		new MockUp<SubscriberLifecycleFacadeImpl> ()
		{
			@SuppressWarnings("unused")
			@Mock
			TelephoneNumberType getTelephoneNumberType(String phoneNumber) {
				assertEquals(phoneNumber, PHONE_NUMBER);
				return null;
			};
			
			@SuppressWarnings("unused")
			@Mock
			com.telus.tmi.xmlschema.xsd.resource.resource.commonlogicalresources_v3_0.NetworkType translateNetworkTypeForWS(String networkType) {
				assertEquals(networkType, NetworkType.NETWORK_TYPE_HSPA);
				return null;
			}
			
			@SuppressWarnings("unused")
			@Mock
			LogicalResourceServicePortType getLogicalResourceService() {
				return logicalResourceServicePortType;
			}
		};
		facade.assignTNResources(PHONE_NUMBER, NetworkType.NETWORK_TYPE_HSPA, LOCAL_IMSI, EMPTY_REMOTE_IMSI);
		assertTrue(true);
	}

	@Test
	public void changeIMSIs() throws Exception {
		final LogicalResourceServicePortType logicalResourceServicePortType = new MockUp<LogicalResourceServicePortType>()
		{
			@SuppressWarnings("unused")
			@Mock
			void changeIMSI(
					com.telus.tmi.xmlschema.xsd.resource.resource.commonlogicalresources_v3_0.TelephoneNumberType TelephoneNumber, 
					com.telus.tmi.xmlschema.xsd.resource.resource.commonlogicalresources_v3_0.NetworkType NetworkType, 
					String LocalIMSI, String RemoteIMSI) {
				assertEquals(LocalIMSI, LOCAL_IMSI);
				assertEquals(RemoteIMSI, NULL_REMOTE_IMSI);
			}
		}.getMockInstance();
		new MockUp<SubscriberLifecycleFacadeImpl> ()
		{
			@SuppressWarnings("unused")
			@Mock
			TelephoneNumberType getTelephoneNumberType(String phoneNumber) {
				assertEquals(phoneNumber, PHONE_NUMBER);
				return null;
			};

			@SuppressWarnings("unused")
			@Mock
			com.telus.tmi.xmlschema.xsd.resource.resource.commonlogicalresources_v3_0.NetworkType translateNetworkTypeForWS(String networkType) {
				assertEquals(networkType, NetworkType.NETWORK_TYPE_HSPA);
				return null;
			}
			
			@SuppressWarnings("unused")
			@Mock
			LogicalResourceServicePortType getLogicalResourceService() {
				return logicalResourceServicePortType;
			}
		};
		facade.changeIMSIs(PHONE_NUMBER, NetworkType.NETWORK_TYPE_HSPA, LOCAL_IMSI, NULL_REMOTE_IMSI);
		assertTrue(true);
	}

	@Test
	public void changeNetwork() throws Exception {
		final LogicalResourceServicePortType logicalResourceServicePortType = new MockUp<LogicalResourceServicePortType>()
		{
			@SuppressWarnings("unused")
			@Mock
			ChangeNetworkResponse changeNetwork(ChangeNetwork parameters) {
//				TelephoneNumberType tnType = parameters.getTelephoneNumber();
//				assertEquals(tnType.getNpa()+tnType.getNxx()+tnType.getLineNumber(), PHONE_NUMBER);
//				assertEquals(parameters.getOldNetworkType(), NetworkType.NETWORK_TYPE_CDMA); 
//				assertEquals(parameters.getNewNetworkType(), NetworkType.NETWORK_TYPE_HSPA); 
				assertEquals(parameters.getLocalIMSI(), LOCAL_IMSI);
				assertEquals(parameters.getRemoteIMSI(), EMPTY_REMOTE_IMSI);
				assertEquals(parameters.getSerialNo(), USIM_ID);
				return null;
			}
		}.getMockInstance();
		new MockUp<SubscriberLifecycleFacadeImpl> ()
		{
			@SuppressWarnings("unused")
			@Mock
			LogicalResourceServicePortType getLogicalResourceService() {
				return logicalResourceServicePortType;
			}
		};
		facade.changeNetwork(PHONE_NUMBER, NetworkType.NETWORK_TYPE_CDMA, NetworkType.NETWORK_TYPE_HSPA, LOCAL_IMSI, EMPTY_REMOTE_IMSI, USIM_ID);
		assertTrue(true);
	}

	@Test
	public void setIMSIStatus() throws Exception {
		final LogicalResourceServicePortType logicalResourceServicePortType = new MockUp<LogicalResourceServicePortType>()
		{
			@SuppressWarnings("unused")
			@Mock
			void setIMSIStatus(String LocalIMSI, String RemoteIMSI, TnProvisioningStatusType ProvisioningStatus) {
				assertEquals(ProvisioningStatus.toString(), PROVISIONING_STATUS); 
				assertEquals(LocalIMSI, LOCAL_IMSI);
				assertEquals(RemoteIMSI, NULL_REMOTE_IMSI);
			}
		}.getMockInstance();
		new MockUp<SubscriberLifecycleFacadeImpl> ()
		{
			@SuppressWarnings("unused")
			@Mock
			LogicalResourceServicePortType getLogicalResourceService() {
				return logicalResourceServicePortType;
			}
		};
		facade.setIMSIStatus(NetworkType.NETWORK_TYPE_HSPA, LOCAL_IMSI, NULL_REMOTE_IMSI, PROVISIONING_STATUS);
		assertTrue(true);
	}
*/

}
