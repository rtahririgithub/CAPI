package com.telus.cmb.framework.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ConfigurationManagerTest {

	private ConfigurationManager manager;
	
	@BeforeTest
	public void setup() {
		manager = ConfigurationManagerFactory.getInstance();
	}
 
	@DataProvider
	public Object[][] getValueProvider() {
		return new Object [][] {
				{"OrderServiceUrl"}, {"AccountInformationHelperUrl"}, {"ReferenceDataFacadeUrl"},
				{"webservices/contractids/clientapiejb/password"}, {"webservices/contractids/clientapiejb/username"}};
	}
	
	@Test(dataProvider = "getValueProvider")
	public void testGetStringValue(String key) {
		
		Log logger = LogFactory.getLog(this.getClass());
		logger.info("xxx");
		
		System.out.println(manager.getValue(key, String.class));
	}
	
	@DataProvider
	public Object[][] getValuesProvider() {
		return new Object [][] {
				{"ClientAPI/911CategoryCodeByBrandId_keys"}
//				,
//				{"ClientAPI/911FeeProvinces"}, {"ClientAPI/Foo"},
//				{manager.buildPath("ClientAPI", "911FeeProvinces")}
				};
	}
	
	@Test(dataProvider = "getValuesProvider")
	public void testGetValues(String key) {
		
		String[] testValues = {"a", "b"};
		
		List<String> defaultValues = Arrays.asList(testValues);
		
		System.out.println(manager.getValues(key, String.class, defaultValues));
	}
	
	
	@Test
	public void testOverrrides() {
		System.out.println(manager.getStringValue("services/CMBQueue/cmbJmsConnectionFactoryJndi"));
		
	}
}
