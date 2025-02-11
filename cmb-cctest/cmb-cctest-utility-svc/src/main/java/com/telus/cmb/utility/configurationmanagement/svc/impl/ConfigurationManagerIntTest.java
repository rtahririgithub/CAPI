package com.telus.cmb.utility.configurationmanagement.svc.impl;

import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;

import org.junit.BeforeClass;
import org.junit.Test;

import com.telus.api.util.RemoteBeanProxyFactory;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager;
import com.telus.eas.config.info.ConfigurationInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public class ConfigurationManagerIntTest {
	static ConfigurationManager configurationManagerEjb;

	@BeforeClass
	public static void prepare() {
		String url="t3://sn25257:30152";
		//String url = "t3://localhost:7001";
		Hashtable<Object, Object> env = new Hashtable<Object, Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");

		configurationManagerEjb = (ConfigurationManager) RemoteBeanProxyFactory.createProxy(ConfigurationManager.class, "ConfigurationManager#com.telus.cmb.utility.configurationmanager.svc.impl.ConfigurationManagerHome", url);
	}
	
	@Test
	public void report_subscriberChangeEquipment() throws Throwable {
		long transactionId = 5;
		Date transactionDate = new Date(2010-1900, 9, 21, 15, 22, 01);
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		EquipmentInfo oldEquipmentInfo = new EquipmentInfo();
		oldEquipmentInfo.setSerialNumber("11914582716");
		EquipmentInfo newEquipmentInfo = new EquipmentInfo();
		newEquipmentInfo.setSerialNumber("15603173789");
		String dealerCode = "10000";
		String salesRepCode = "0000";
		String requestorId = "233111@33";
		String repairId = "";
		String swapType = "REPAIR";
		EquipmentInfo associatedMuleEquipmentInfo = null;
		String applicationName = "";
				
		configurationManagerEjb.report_subscriberChangeEquipment(transactionId, transactionDate, subscriberInfo, oldEquipmentInfo, newEquipmentInfo, dealerCode, salesRepCode, requestorId, repairId, swapType, associatedMuleEquipmentInfo, applicationName);
	}
	
	@Test
	public void testProductEquipmentHelper() throws Throwable {
		Hashtable<Object, Object> env = new Hashtable<Object, Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		
		
		ProductEquipmentHelper pehEjb = (ProductEquipmentHelper) RemoteBeanProxyFactory.createProxy(ProductEquipmentHelper.class, "ProductEquipmentHelper#com.telus.cmb.productequipment.helper.svc.impl.ProductEquipmentHelperHome", "t3://sn25257:31152");
		//WarrantyInfo warranty = pehEjb.getWarrantyInfo("11914582716");
		EquipmentInfo eqip = pehEjb.getEquipmentInfobySerialNo("11914582716");
		System.out.println(eqip.toString());
	}
	
	@Test
	public void getConfiguration() throws Throwable {
		String[] path={"telus", "applications", "webactivations"};

		
		ConfigurationInfo[] config = configurationManagerEjb.getConfiguration(path);
		for (ConfigurationInfo c : config) {
			System.out.println(c.getName());
		}
	}
	
	@Test
	public void getLdapConfiguration() throws Throwable {
		String[] path = { "Telus-ECA", "ClientAPI"};
//		String[] path = { "CMB", "services", "ApplicationMessageFacade"};
//		String[] path = { "CMB", "services"};
		
		com.telus.eas.config1.info.ConfigurationInfo config = configurationManagerEjb.getConfiguration1(path);
		for (String propertyName : config.getPropertyNames()) {
			System.out.println(propertyName + ":" + config.getPropertyAsString(propertyName));
		}
//		System.out.println(config.getProperty("url"));
//		Map properties = config.getProperties();
//		
//		System.out.println(properties);
	}
}
