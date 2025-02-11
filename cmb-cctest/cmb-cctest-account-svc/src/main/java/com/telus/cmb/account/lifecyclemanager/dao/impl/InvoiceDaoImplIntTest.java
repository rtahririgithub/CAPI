package com.telus.cmb.account.lifecyclemanager.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.cmb.account.lifecyclemanager.dao.InvoiceDao;
import com.telus.eas.account.info.BillNotificationContactInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-lifecyclemanager.xml", "classpath:application-context-datasources-informationhelper-testing-d3.xml"})
public class InvoiceDaoImplIntTest {

	@Autowired
	InvoiceDao invoiceDao;

	@Test
	public void testSaveEBillRegistrationReminder(){
		try{
			invoiceDao.saveEBillRegistrationReminder(86, "2121211212", "2121211212", "HS");
		}catch(UncategorizedSQLException unCatEx)
		{
			unCatEx.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testHasEPostFalseNotificationTypeNotEPost(){
		try{
			int ban=24;
			long portalUserID=323212;
			BillNotificationContactInfo[] billNotAr = new BillNotificationContactInfo[1];
			BillNotificationContactInfo billNotificatioCont = new BillNotificationContactInfo();
			billNotificatioCont.setBillNotificationType("");
			billNotificatioCont.setNotificationAddress("asasdasasas");
			billNotificatioCont.setContactType("5");
			billNotAr[0]=billNotificatioCont;
			String applicationCode="test";
			invoiceDao.hasEPostFalseNotificationTypeNotEPost(ban, portalUserID, billNotAr, applicationCode);
		}catch(UncategorizedSQLException unCatEx)
		{}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testHasEPostFalseNotificationTypeEPost(){
		try{
			int ban=24;
			long portalUserID=323212;
			BillNotificationContactInfo[] billNotAr = new BillNotificationContactInfo[1];
			BillNotificationContactInfo billNotificatioCont = new BillNotificationContactInfo();
			billNotificatioCont.setBillNotificationType("EPOST");
			billNotificatioCont.setNotificationAddress("asasdasasas");
			billNotificatioCont.setContactType("5");
			billNotAr[0]=billNotificatioCont;
			String applicationCode="test";
			invoiceDao.hasEPostFalseNotificationTypeEPost(ban, portalUserID, billNotAr, applicationCode);
		}catch(UncategorizedSQLException unCatEx)
		{}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testHasEPostNotificationTypeNotEPost(){
		try{
			int ban=8;
			long portalUserID=323212;
			BillNotificationContactInfo[] billNotAr = new BillNotificationContactInfo[1];
			BillNotificationContactInfo billNotificatioCont = new BillNotificationContactInfo();
			billNotificatioCont.setBillNotificationType("");
			billNotificatioCont.setNotificationAddress("asasdasasas");
			billNotificatioCont.setContactType("5");
			billNotAr[0]=billNotificatioCont;
			String applicationCode="test";
			invoiceDao.hasEPostNotificationTypeNotEPost(ban, portalUserID, billNotAr, applicationCode);
		}catch(UncategorizedSQLException unCatEx)
		{	}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testHasEPostNotificationTypeEPost(){
		try{
			int ban=8;
			long portalUserID=323212;
			BillNotificationContactInfo[] billNotAr = new BillNotificationContactInfo[1];
			BillNotificationContactInfo billNotificatioCont = new BillNotificationContactInfo();
			billNotificatioCont.setBillNotificationType("EPOST");
			billNotificatioCont.setNotificationAddress("asasdasasas");
			billNotificatioCont.setContactType("5");
			billNotAr[0]=billNotificatioCont;
			String applicationCode="test";
			invoiceDao.hasEPostNotificationTypeEPost(ban, portalUserID, billNotAr, applicationCode);
		}catch(UncategorizedSQLException unCatEx)
		{}
	}

}
