package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.InvoiceDao;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.EBillRegistrationReminderInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.framework.info.ChargeInfo;

public class InvoiceDaoImplIntTest extends BaseInformationHelperIntTest{

	@Autowired
	InvoiceDao invoiceDao;
	@SuppressWarnings("deprecation")
	@Test
	public void testGetLastEBillNotificationSent(){
		BillNotificationContactInfo billNot = invoiceDao.getLastEBillNotificationSent(15324300);
		assertEquals("SMS",billNot.getContactType());
		assertEquals(new java.sql.Date(2006-1900,0,01) ,billNot.getLastNotificationDate());
	}
	@Test
	public void testGetLastEBillRegistrationReminderSent(){
		EBillRegistrationReminderInfo billReg = invoiceDao.getLastEBillRegistrationReminderSent(15324300);
		try{
			assertEquals(null,billReg.getActivatedPhoneNumber());
			fail("Exception Expected");
		}catch(Exception e)
		{}
	}
	@Test
	public void testGetBillNotificationHistory(){
		Collection<BillNotificationHistoryRecord> billNot = invoiceDao.getBillNotificationHistory(15324300,"EPOST");
		assertEquals(48,billNot.size());
		for(BillNotificationHistoryRecord hisInfo : billNot){
			assertEquals("1234",hisInfo.getSrcReferenceId());
			assertEquals(true,hisInfo.getMostRecentInd());
			break;
		}	
	}

	@Test
	public void testExpireBillNotificationDetails(){
		invoiceDao.expireBillNotificationDetails(15324300);
	}

	@Test
	public void testRetrieveSubscriberInvoiceDetails(){
		assertEquals(2,invoiceDao.retrieveSubscriberInvoiceDetails(51836, 8).size());
		assertEquals(0,invoiceDao.retrieveSubscriberInvoiceDetails(51836, 1).size());

	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveBilledCharges(){
		Collection<ChargeInfo> chInf = invoiceDao.retrieveBilledCharges(8,54,"4033404108",new java.sql.Date(2002-1900,8,23),new java.sql.Date(2002-1900,8,23));
		assertEquals(35,chInf.size());
		for(ChargeInfo conInfo : chInf){
			assertEquals(0,conInfo.getAmount(),0);
			assertEquals(54 ,conInfo.getBillSequenceNo());
			assertEquals("CHG " ,conInfo.getChargeCode());
			break;
		}
	}

	@Test
	public void testRetrieveBillNotificationContacts(){
		Collection<BillNotificationContactInfo> bollNotInf = invoiceDao.retrieveBillNotificationContactsHasEPost(8,7806813);
		assertEquals(0,bollNotInf.size());
		Collection<BillNotificationContactInfo> bollNotInf1 = invoiceDao.retrieveBillNotificationContactsHasEPostFalse(8,7806813);
		assertEquals(0,bollNotInf1.size());
	}

	@Test
	public void testisEBillRegistrationReminderExist(){
		assertEquals(false,invoiceDao.isEBillRegistrationReminderExist(15324300));
	}

	@Test
	public void testhasEPostSubscription(){

		assertEquals(false,invoiceDao.hasEPostSubscription(8));
		assertEquals(false,invoiceDao.hasEPostSubscription(24));
	}
	@Test
	public void testgetInvoiceProperties(){
		InvoicePropertiesInfo invoicePropertiesInfo = invoiceDao.getInvoiceProperties(24);
		assertEquals("2",invoicePropertiesInfo.getInvoiceSuppressionLevel());
		assertEquals("1",invoicePropertiesInfo.getHoldRedirectDestinationCode());
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveInvoiceHistory(){
		Collection<InvoiceHistoryInfo> invoiceHistoryInfo = new ArrayList<InvoiceHistoryInfo>();
		invoiceHistoryInfo = invoiceDao.retrieveInvoiceHistory(210879, new Date(2006-1900,0,01), new Date(2006-1900,0,31));
		assertEquals(1,invoiceHistoryInfo.size());
		for(InvoiceHistoryInfo invObj:invoiceHistoryInfo){
			assertEquals(50.55,invObj.getAmountDue(),0);
			assertEquals(84.07,invObj.getPreviousBalance(),0);
			break;
		}
	}	

}
