package com.telus.provider;

import java.lang.reflect.Method;
import java.util.Map;

import junit.framework.Assert;

import com.telus.api.util.ComponentEndpointConfiguration;
import com.telus.api.util.ComponentEndpointConfigurationManager;

public class NewEjbTestMethods {

	 
	public static void testNewOldSwitch(TestTMProvider testTMProvider, Object obj, Method m, Object[] args) {
		testTMProvider.setComponentEndpointConfigurationManager(new ComponentEndpointConfigurationManagerTestImpl(true));
				
		try {
			m.invoke(obj, args);
			Assert.fail ("Exception expected.");
		} catch (Throwable t) {
			
		}
		
		Assert.assertTrue(testTMProvider.isNewEjbCalled());		
		
		testTMProvider.setComponentEndpointConfigurationManager(new ComponentEndpointConfigurationManagerTestImpl(false));
		try {
			m.invoke(obj, args);
			Assert.fail ("Exception expected.");
		} catch (Throwable t) {
			
		}
		
		Assert.assertFalse(testTMProvider.isNewEjbCalled());
	}
	
	static class ComponentEndpointConfigurationManagerTestImpl implements ComponentEndpointConfigurationManager {
		
		ComponentEndpointConfiguration componentEndpointConfiguration;
		
		public ComponentEndpointConfigurationManagerTestImpl(boolean value) {
			componentEndpointConfiguration = new ComponentEndpointConfiguration();
			componentEndpointConfiguration.setUsedByProvider(value);
			componentEndpointConfiguration.setUsedByWebServices(value);
		}
		
		public Map getComponentEndpointConfigurations() {
			return null;
		}

		public ComponentEndpointConfiguration getComponentEndpointConfiguration(
				String name) {
			return componentEndpointConfiguration;
		}

		public ComponentEndpointConfiguration getComponentEndpointConfiguration(
				Class type) {
			return componentEndpointConfiguration;
		}
	}
}
