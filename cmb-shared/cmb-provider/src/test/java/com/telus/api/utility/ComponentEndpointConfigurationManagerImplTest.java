package com.telus.api.utility;

import junit.framework.TestCase;

import com.telus.api.BaseTest;
import com.telus.api.util.ComponentEndpointConfigurationManagerImpl;

public class ComponentEndpointConfigurationManagerImplTest extends TestCase {
	
	public void testGetComponentEndpointConfigurations() {
		BaseTest.setupD3();
		
		new ComponentEndpointConfigurationManagerImpl("cn=services,cn=CMB", "cmb.services").getComponentEndpointConfigurations();

		System.setProperty("cmb.ejb.useSecondaryUrl", "true");
		System.setProperty("cmb.ejb.useBatchUrl", "false");
		new ComponentEndpointConfigurationManagerImpl("cn=services,cn=CMB", "cmb.services").getComponentEndpointConfigurations();

		System.setProperty("cmb.ejb.useSecondaryUrl", "false");
		System.setProperty("cmb.ejb.useBatchUrl", "true");
		new ComponentEndpointConfigurationManagerImpl("cn=services,cn=CMB", "cmb.services").getComponentEndpointConfigurations();

		System.setProperty("cmb.ejb.useSecondaryUrl", "true");
		System.setProperty("cmb.ejb.useBatchUrl", "true");
		new ComponentEndpointConfigurationManagerImpl("cn=services,cn=CMB", "cmb.services").getComponentEndpointConfigurations();
	}

}
