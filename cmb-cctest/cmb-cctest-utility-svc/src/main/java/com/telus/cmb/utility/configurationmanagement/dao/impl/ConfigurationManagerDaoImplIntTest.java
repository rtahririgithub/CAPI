package com.telus.cmb.utility.configurationmanagement.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.cmb.utility.configurationmanager.dao.impl.ConfigurationManagerDaoImpl;
import com.telus.eas.config.info.ConfigurationInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-datasources.xml", "classpath:application-context-dao-utility-configurationmanager.xml"})


public class ConfigurationManagerDaoImplIntTest {
	@Autowired
	ConfigurationManagerDaoImpl cmDao;

	@Test
	public void getConfigurationTest() throws Exception {
		cmDao.getConfiguration(new String[]{"telus"});
	}
	
	@Test
	public void getEquipmentChangeDetailsByESNs() throws Exception {
		String oldESN = "123";
		String newESN = "456";
		
		cmDao.getEquipmentChangeDetailsByESNs(oldESN, newESN);
	}

	@Test
	public void loadProperties() throws Exception {
		ConfigurationInfo info = new ConfigurationInfo();
		info.setId(1);
		cmDao.loadProperties(info);
		
	}

	@Test
	public void addProperties() throws Exception {
		int configurationId = 39;
		String name[] = new String[] {};
		String value[] = new String[] {};
		cmDao.addProperties(configurationId, name, value);
	}

	@Test
	public void getConfiguration() throws Exception {
		ConfigurationInfo[] config = cmDao.getConfiguration(new String[] { "CMB", "web_services", "useNewEjbServices" });
		for (ConfigurationInfo c : config) {
			System.out.println(c);
		}
	}

	@Test
	public void getChildConfigurations() throws Exception {
		ConfigurationInfo[] config = cmDao.getChildConfigurations(1);
		for (ConfigurationInfo c : config) {
			System.out.println(c);
		}
	}

	@Test
	public void removeProperties() throws Exception {
		cmDao.removeProperties(34);
	}

	@Test
	public void removePropertiesRecursively() throws Exception {
	}

	@Test
	public void removeConfiguration() throws Exception {
	}

	@Test
	public void newConfiguration() throws Exception {
	}

	@Test
	public void getActivationLogID() throws Exception {
	}

	@Test
	public void logActivation() throws Exception {

		
	}

	@Test
	public void logActivationSummary() throws Exception {

		
	}

	@Test
	public void logApplication() throws Exception {

		
	}

	@Test
	public void newReport() throws Exception {
	}

	@Test
	public void report_changePricePlan() throws Exception {
		
	}

	@Test
	public void report_changeAddress() throws Exception {
		
	}

	@Test
	public void report_subscriberNewCharge() throws Exception {
		
	}

	@Test
	public void report_changePaymentMethod() throws Exception {
		
	}

	@Test
	public void report_makePayment() throws Exception {
		
	}

	@Test
	public void report_accountStatusChange() throws Exception {
		
	}

	@Test
	public void report_prepaidAccountTopUp() throws Exception {
		
	}

	@Test
	public void report_changeRole() throws Exception {
				
	}

	@Test
	public void report_changePhoneNumber() throws Exception {
		
	}

	@Test
	public void report_changeService() throws Exception {
		
	}

	@Test
	public void report_changeSubscriber() throws Exception {
		
	}

	@Test
	public void report_subscriberChangeEquipment() throws Exception {
		
	}

	@Test
	public void getInteractionsByBan() throws Exception {
	}

	@Test
	public void getInteractionsBySubscriber() throws Exception {
	}

	@Test
	public void getInteractionDetails() throws Exception {
	}
}
