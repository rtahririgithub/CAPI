package com.telus.cmb.utility.contactevent.svc.impl;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.account.DurationServiceCommitmentAttributeData;
import com.telus.api.contactevent.SMSNotification;
import com.telus.api.reference.NotificationType;
import com.telus.cmb.utility.contacteventmanager.svc.ContactEventManager;
import com.telus.eas.contactevent.info.NewSMSNotificationInfo;
import com.telus.eas.contactevent.info.SMSNotificationInfo;
import com.telus.eas.utility.info.NotificationMessageTemplateInfo;
import com.telus.eas.utility.info.NotificationTypeInfo;
import com.telus.eas.utility.info.ServiceInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-datasources.xml", 
									"classpath:application-context-dao-utility-contactevent.xml",
									"classpath:com/telus/cmb/utility/contactevent/svc/impl/application-context-svc-local.xml"})
									
public class ContactEventImplTest {

	@Autowired
	ContactEventManager contactEvent;
	static {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
	}
	
	@Test
	public void processNotification() throws Throwable {
	    NotificationTypeInfo nTypeInfo = new NotificationTypeInfo();
	    nTypeInfo.setCode(NotificationType.NOTIFICATION_TYPE_USAGE_SUMMARY);
	    nTypeInfo.setBillable("N");
	    nTypeInfo.setDeliveryReceiptRequired("N");
	    
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(2004, 7, 1, 00, 00, 01);

	    SMSNotificationInfo n = new SMSNotificationInfo();
	    
	    n.setApplication("SMARTDESKTOP");
	    n.setUser("18654");

	    n.setBanId(117);
	    // "500", "253", "0", "01-AUG-2004"
	    n.setContentParameters(new String[] {"500", "253", "0", "01-AUG-2004"});
	    n.setDeliveryDate(calendar.getTime());
	    n.setEquipmentSerialNumber("52431526374");
	    n.setLanguage(SMSNotification.LANGUAGE_ENGLISH);
	    n.setNotificationTypeInfo(nTypeInfo);
	    NotificationMessageTemplateInfo template = new NotificationMessageTemplateInfo();
	    template.setCode(NotificationType.NOTIFICATION_TYPE_USAGE_SUMMARY);
	    template.setMessageTemplate("You have ? included, ? remaining and ? chargeable minutes on your TELUS Mobility phone. Your billing date is ?.");
	    n.setNotificationMessageTemplateInfo(template);
	    n.setPhoneNumber("8006842345");
	    n.setPreventingDuplicate(false);
	    n.setPriority(SMSNotification.PRIORITY_SMS_NORMAL);
	    n.setProductType(SMSNotification.PRODUCT_TYPE_PCS);
	    n.setSubscriberNumber("8006842345");
	    n.setTimeToLive(10000);
	    n.setValidatingInputRequest(false);


		contactEvent.processNotification(n);

	}

	public String readXMLData(){
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("com\\telus\\cmb\\utility\\contactevent\\svc\\impl\\Xhour-data.xml");
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    org.w3c.dom.Document doc = dBuilder.parse(in);

		    return doc.toString();

		} catch (Exception e) {

		}
		return null;
	}
	
/**
	@Test
	public void newXhourSocAddes() throws Throwable {
		NewSMSNotificationInfo info = new NewSMSNotificationInfo();
		info.setBanId(70711245);
		info.setSubscriptionId("8356512");
		info.setLanguage("EN");
		info.setNotificationType("RLH_SOC_ACTIVE");
		info.setOriginatorApplicationId("5284");	
		info.setDeliveryDate(new Date());
		DurationServiceCommitmentAttributeData data = new DurationServiceCommitmentAttributeData("XHR-PARAM=XHR-START|20140821101340;XHR-END|20140822101340;XHR-TZ_ID|Canada/Mountain;XHR-UTC|-6;BP_START|20140821101340;BP_END|20140822101340;BP_TZ|UTC-0700@");
		info.setSchedulerReferenceId("8356512" + "0SRLH23" + data.getBpServiceStartTime().getTime().getTime());
		info.setHoursBeforeExpiry(2);
		info.setPhoneNumber("");
		info.setTransactionType("ADD");
		info.setXmlContent("");
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setCode("0SRLH23");
		serviceInfo.setDescription("test 0SRLH23");
		serviceInfo.setDescriptionFrench("test French 0SRLH23");
		info.setServiceInfo(serviceInfo);
		info.setXmlContent(readXMLData());
		contactEvent.scheduleSMSNotification(info);
	}
*/	
	@Test
	public void logAccountAuthentication() throws Throwable {
		String ban = "8";
		boolean isAuthenticationSucceeded = true;
		String channelOrganizationID = "9999";
		String outletID = "1234";
		String salesRepID = "0000";
		String applicationID = "SMARTDESKTOP";
		String userID = "18654";
		contactEvent.logAccountAuthentication(ban, isAuthenticationSucceeded, channelOrganizationID, outletID, salesRepID, applicationID, userID);
	}
	
	@Test
	public void logSubscriberAuthentication() throws Throwable {
		String min = "4161324567";
		boolean isAuthenticationSucceeded = false;
		String channelOrganizationID = "9999";
		String outletID = "1234";
		String salesRepID = "0000";
		String applicationID = "SMARTDESKTOP";
		String userID = "18654";
		
		contactEvent.logSubscriberAuthentication(min, isAuthenticationSucceeded, channelOrganizationID, outletID, salesRepID, applicationID, userID);
	}
}
