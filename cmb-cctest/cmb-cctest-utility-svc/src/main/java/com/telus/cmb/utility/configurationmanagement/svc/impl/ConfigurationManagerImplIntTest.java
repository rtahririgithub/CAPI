package com.telus.cmb.utility.configurationmanagement.svc.impl;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.interaction.Interaction;
import com.telus.api.interaction.InteractionDetail;
import com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.config.info.ConfigurationInfo;
import com.telus.eas.config.info.EquipmentChangeInfo;
import com.telus.eas.config.info.LogInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-datasources.xml", 
									"classpath:application-context-dao-utility-configurationmanager.xml",
									"classpath:com/telus/cmb/utility/configurationmanagement/svc/impl/application-context-svc-local.xml"})

public class ConfigurationManagerImplIntTest {

	static {
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.provider", "com.telus.provider.config.PropertiesOverridingConfigurationProvider");
		System.setProperty("com.telusmobility.config.propertiesFile", "configuration.properties");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
	}
	
	@Autowired
	ConfigurationManager cm;
	
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	
	@Test
	public void getActivationLogID() throws Throwable {
		System.out.println(cm.getActivationLogID());
	}

	@Test
	public void report_accountStatus() throws Throwable {
		long transactionId = 69020;
		Date transactionDate = getDateInput(2010, 9, 21);
		char oldHotlinedInd = 'N';
		char newHotlinedInd = 'N';
		char oldStatus = 'R';
		char newStatus = 'A';
		char statusFlag = 'A';
		
		cm.report_accountStatusChange(transactionId, transactionDate, oldHotlinedInd, newHotlinedInd, oldStatus, newStatus, statusFlag);
	}
	
	@Test
	public void report_subscriberNewCharge() throws Throwable {
		long transactionId = 69020;
		Date transactionDate = getDateInput(2010, 9, 21);
		String chargeCode = "ACTMMK";
		String waiverCode = null;
		
		cm.report_subscriberNewCharge(transactionId, transactionDate, chargeCode, waiverCode);
	}
	
	@Test
	public void getChildConfiguration() throws Throwable {
		ConfigurationInfo[] configs = cm.getChildConfigurations(0);
		for (ConfigurationInfo config : configs) {
			System.out.println(config);
		}
	}
	
	@Test
	public void addProperties() throws Throwable {
		String[] names = {"activationFeeChargeCode", "applicationName", "dealer"};
		String[] originalValues = {"CMER", "CSA", "false"};
	//	String[] newValues =  {"CMEX", "CSI", "true"};
		int configurationId = 144105430;
		
		cm.addProperties(configurationId, names, originalValues);
	}
	
	@Test
	public void addProperties2() throws Throwable {
		String[] names = {"test"};
		String[] originalValues = {"XXX"};
		int configurationId = 999;
		
		cm.addProperties(configurationId, names, originalValues);
	}
	
	@Test
	public void getEquipmentChangeByESN() throws Throwable {
		String oldESN = "24703375609";
		String newESN = "24701409889";
		
		EquipmentChangeInfo info = cm.getEquipmentChangeByESN(oldESN, newESN);
		System.out.println(info.getBan());
	}
	
	@Test
	public void removeProperties() throws Throwable {
		int configurationId = 999;
		boolean recursively = false;
		cm.removeProperties(configurationId, recursively);
	}
	
	@Test
	public void logActivation() throws Throwable {
		LogInfo plogInfo = new LogInfo();
		plogInfo.setCustomerID(8);
		cm.logApplication(plogInfo);
	}
	
	@Test
	public void newConfiguration() throws Throwable {
		cm.newConfiguration(999, "chung");
	}
	
	@Test
	public void removeConfiguration() throws Throwable {
		cm.removeConfiguration(56069095);
	}
	
	@Test
	public void report_changeAddress() throws Throwable {
		long transactionId = 69020;
		Date transactionDate = getDateInput(2010, 9, 21);
		AddressInfo oldAddr = new AddressInfo();
		AddressInfo newAddr = new AddressInfo();

		oldAddr.setStreetNumber("90");
		oldAddr.setStreetName("GERRARD ST W");
		oldAddr.setCity("TORONTO");
		oldAddr.setProvince("ON");
		oldAddr.setPostalCode("M1M1M1");
		oldAddr.setCountry("CAN");
		
		newAddr.setStreetNumber("99");
		newAddr.setStreetName("GERRARD ST W");
		newAddr.setCity("TORONTO");
		newAddr.setProvince("ON");
		newAddr.setPostalCode("M1M1M1");
		newAddr.setCountry("CAN");
		cm.report_changeAddress(transactionId, transactionDate, oldAddr, newAddr);
	}
	
	@Test
	public void report_changePaymentMethod() throws Throwable {
		long transactionId = 69020;
		Date transactionDate = getDateInput(2010, 9, 21);
		char oldPaymentMethod = 'R';
		char newPaymentMethod = 'C';
		
		cm.report_changePaymentMethod(transactionId, transactionDate, oldPaymentMethod, newPaymentMethod);
	}
	
	@Test
	public void report_changePhoneNumber() throws Throwable {
		long transactionId = 69020;
		Date transactionDate = getDateInput(2010, 9, 21);
		String oldPhoneNumber = "4161234567";
		String newPhoneNumber = "4167654321";
		
		cm.report_changePhoneNumber(transactionId, transactionDate, oldPhoneNumber, newPhoneNumber);
	}
	
	@Test
	public void report_changePricePlan() throws Throwable {
		long transactionId = 69020;
		Date transactionDate = getDateInput(2010, 9, 21);
		String oldPlan = "PTLK75CF";
		String newPlan = "PCS25MPTC";
		ServiceAgreementInfo[] services = new ServiceAgreementInfo[0];
		
		cm.report_changePricePlan(transactionId, transactionDate, oldPlan, newPlan, services);
	}
	
	@Test
	public void report_changeRole() throws Throwable {
		long transactionId = 69020;
		Date transactionDate = getDateInput(2010, 9, 21);
		String oldRole = null;
		String newRole = "AA";
		
		cm.report_changeRole(transactionId, transactionDate, oldRole, newRole);
	}
	
	@Test
	public void getInteractionDetails() throws Throwable {
		InteractionDetail[] details = cm.getInteractionDetails(69020);
		
		for (InteractionDetail detail : details) {
			System.out.println(detail.getType());
		}
		
		int ban = 2790733;
		Date from = getDateInput(2001, 9, 21);
		Date to = getDateInput(2010, 9, 21);
		String type = null;
		
		Interaction[] interactions = cm.getInteractionsByBan(ban, from, to, type);
		for (Interaction interaction : interactions) {
			System.out.println(interaction.getApplicationId() + "," + interaction.getSubscriberId());
		}
	}
	
	@Test
	public void getConfiguration() throws Throwable {
		String[] path={"telus", "applications", "webactivations"};
		
		ConfigurationInfo[] config = cm.getConfiguration(path);
		for (ConfigurationInfo c : config) {
			System.out.println(c.getName());
		}
		
		
	}
	
	@Test
	public void getLdapConfiguration() throws Throwable {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
		String[] ldapPath = {"CMB", "services", "ReferenceDataFacade"}; 
		com.telus.eas.config1.info.ConfigurationInfo ldapConfig = cm.getConfiguration1(ldapPath);
		System.out.println(ldapConfig.getProperty("url"));
	}
}
