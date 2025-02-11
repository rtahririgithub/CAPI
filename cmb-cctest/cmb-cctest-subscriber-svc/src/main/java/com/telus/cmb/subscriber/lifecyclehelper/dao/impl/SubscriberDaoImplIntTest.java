package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.ResourceChangeHistoryInfo;
import com.telus.eas.subscriber.info.ResourceInfo;
import com.telus.eas.subscriber.info.SubscriberHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.eas.utility.info.LineRangeInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.NumberRangeInfo;

public class SubscriberDaoImplIntTest extends BaseLifecycleHelperIntTest {

	@Autowired
	SubscriberDaoImpl dao;

	static {
		// System.setProperty ("getSubListByBan.method.rollback", "false");
		// System.setProperty ("getSubListByPhoneNumbers.method.rollback",
		// "true");
		// System.setProperty("getSubListByBanAndPhoneNumber.method.rollback",
		// "true");
		// System.setProperty("getLwSubListByBan.method.rollback", "true");
//		System.setProperty("getSubByBanAndPhoneNumber.method.rollback", "true");
	}

	@Test
	public void testRetrievePartiallyReservedSubscriberListByBan() throws ApplicationException {

		int ban = 70104723;
		int maximum = 100;
		List<String> subscriberList = dao.retrievePartiallyReservedSubscriberListByBan(ban, maximum);
		assertEquals(12, subscriberList.size());
		for (String list : subscriberList) {
			assertEquals("6471551974", list);
			break;
		}
	}

	@Test
	public void testRetrievePortedSubscriberListByBAN() throws ApplicationException {

		int ban = 20001552;
		int listLength = 100;
		Collection<SubscriberInfo> subscriberInfoList = dao.retrievePortedSubscriberListByBAN(ban, listLength);
		assertEquals(5, subscriberInfoList.size());
		for (SubscriberInfo list : subscriberInfoList) {
			assertEquals("SUS", list.getActivityCode());
			// System.out.println(list.getActivityCode());
			break;
		}
		// System.out.println(subscriberInfoList.iterator().next().getActivityCode());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveResourceChangeHistory() throws ApplicationException {

		int ban = 20007215;
		String subscriberID = "M000000484";
		String type = "H";
		Date from = new Date((2000 - 1900), (1 - 1), 1);
		Date to = new Date((2010 - 1900), (1 - 1), 1);
		List<ResourceChangeHistoryInfo> rchiList = dao.retrieveResourceChangeHistory(ban, subscriberID, type, from, to);
		assertEquals(1, rchiList.size());
		for (ResourceChangeHistoryInfo list : rchiList) {
			assertEquals("R", list.getStatus());
			assertEquals("API   ", list.getApplicationID());
			break;
		}
	}

	@Test
	public void testRetrieveLightWeightSubscriberListByBAN() throws ApplicationException {

		int banId = 20001552;
		banId = 25; // IDEN BAN
		int listLength = 100;
		boolean isIDEN = true;
		boolean includeCancelled = true;
		List<LightWeightSubscriberInfo> lwsiList = dao.retrieveLightWeightSubscriberListByBAN(banId, isIDEN, listLength, includeCancelled);
		System.out.println(lwsiList.size());
		for (LightWeightSubscriberInfo sub : lwsiList) {
			System.out.println(sub.getPhoneNumber());
		}
	}

	@Test
	public void testRetrieveLastAssociatedSubscriptionId() throws ApplicationException {

		String imsi = "302220999950762";
		String subscriptionId = dao.retrieveLastAssociatedSubscriptionId(imsi);
		assertEquals("1023761", subscriptionId);

	}

	@Test
	public void testRetrieveHotlineIndicator() throws ApplicationException {

		String subscriberId = "7807183927";
		assertTrue(dao.retrieveHotlineIndicator(subscriberId));
	}

	@Test
	public void testGetPortProtectionIndicator() throws ApplicationException {

		int ban = 70102365;
		String subscriberId = "4164160074";
		String phoneNumber = "416-4160074";
		String status = "C";
		String subscriptionId = dao.getPortProtectionIndicator(ban, subscriberId, phoneNumber, status);
		assertEquals("Y", subscriptionId);

	}

	@Test
	public void testRetrieveSubscriptionPreference() throws ApplicationException {

		long subscriptionId = 12255794;
		int preferenceTopicId = 1;
		SubscriptionPreferenceInfo spi = dao.retrieveSubscriptionPreference(subscriptionId, preferenceTopicId);
		assertEquals(1, spi.getPreferenceTopicId());
		assertEquals("N", spi.getPreferenceValueTxt());
	}

	@Test
	public void testRetrieveAvailableCellularPhoneNumbersByRanges() throws ApplicationException, TelusAPIException {

		LineRangeInfo[] lineRanges = new LineRangeInfo[1];
		lineRanges[0] = new LineRangeInfo();
		lineRanges[0].setStart(0000);
		lineRanges[0].setEnd(9999);

		NumberRangeInfo[] numberRanges = new NumberRangeInfo[2];
		numberRanges[0] = new NumberRangeInfo();
		numberRanges[0].setNXX(717);
		numberRanges[0].setNPA(306);
		numberRanges[0].setLineRanges(lineRanges);
		numberRanges[1] = new NumberRangeInfo();
		numberRanges[1].setNXX(974);
		numberRanges[1].setNPA(306);
		numberRanges[1].setLineRanges(lineRanges);

		NumberGroupInfo numberGroupInfo = new NumberGroupInfo();
		numberGroupInfo.setCode("SAS");
		numberGroupInfo.setProvinceCode("ON");
		numberGroupInfo.setNumberLocation("TLS");
		numberGroupInfo.setNumberRanges(numberRanges);

		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setNumberGroup(numberGroupInfo);
		String startFromPhoneNumber = "0";
		String searchPattern = "*";
		boolean asian = false;
		int maxNumber = 100;

		// System.out.println(((NumberGroupInfo)phoneNumberReservation.getNumberGroup()).getNumberRanges().length);

		List<String> phoneNumberList = dao.retrieveAvailableCellularPhoneNumbersByRanges(phoneNumberReservation, startFromPhoneNumber, searchPattern, asian, maxNumber);
		assertEquals(maxNumber, phoneNumberList.size());
		for (String list : phoneNumberList) {
			assertEquals("3067176004", list);
			break;
		}
		// System.out.println(phoneNumberList.get(0)+"----"+phoneNumberList.get(99));
	}



	@Test
	public void testRetrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber() throws ApplicationException {

		int ban = 8;
		String phoneNumber = "416-416-0074";
		SubscriberIdentifierInfo s = dao.retrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber(ban, phoneNumber);
		// assertEquals("4164160074",s.getSubscriberId());
		// System.out.println(s);
	}

	@Test
	public void testRetrieveSubscriptionRole() {
		SubscriptionRoleInfo subscriptionRoleInfo = null;
		subscriptionRoleInfo = dao.retrieveSubscriptionRole("4033501530");
		assertEquals("ES", subscriptionRoleInfo.getCode());
		assertEquals(null, subscriptionRoleInfo.getDealerCode());
	}

	@Test
	public void testRetrieveSubscriberListByBanAndTalkGroup() {
		Collection<SubscriberInfo> collection = null;
		collection = dao.retrieveSubscriberListByBanAndTalkGroup(84, 905, 131077, 1, 1);
		assertEquals(1, collection.size());
		for (SubscriberInfo subInf : collection) {
			assertEquals("I", subInf.getProductType());
			assertEquals('A', subInf.getStatus());
			assertEquals("0000000014", subInf.getDealerCode());
		}
	}

	@Test
	public void testRetrieveSubscriberListByBanAndFleet() {
		Collection<SubscriberInfo> collection = null;
		collection = dao.retrieveSubscriberListByBanAndFleet(84, 905, 131077, 8);
		assertEquals(8, collection.size());
		for (SubscriberInfo subInf : collection) {
			assertEquals("I", subInf.getProductType());
			assertEquals('A', subInf.getStatus());
			assertEquals("0000000014", subInf.getDealerCode());
			break;
		}
	}

	@Test
	public void testretrieveSubscriberHistory() {
		Collection<SubscriberHistoryInfo> collection = null;
		collection = dao.retrieveSubscriberHistory(8, "4033404108", new Date(2005 - 1900, 02, 31), new Date(2005 - 1900, 02, 31));
		assertEquals(1, collection.size());
		for (SubscriberHistoryInfo subInf : collection) {
			assertEquals('A', subInf.getStatus());
			assertEquals("CRQ ", subInf.getActivityReasonCode());
			assertEquals(0, subInf.getPreviousBanId());
			break;
		}
	}

	@Test
	public void testRetrieveSubscriberPhoneNumbers() {
		List<String> list = new ArrayList<String>();
		list = dao.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', 10);
		assertEquals(10, list.size());
		for (String str : list) {
			assertEquals("4033404108", str);
			break;
		}
		list = dao.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', 5);
		assertEquals(5, list.size());
		for (String str : list) {
			assertEquals("4033404108", str);
			break;
		}
		list = dao.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', 0);
		assertEquals(0, list.size());
		list = dao.retrieveSubscriberPhonenumbers('A', 'X', 'R', 'O', 20);
		assertEquals(0, list.size());
		list = dao.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', "S", 10);
		assertEquals(10, list.size());
		for (String str : list) {
			assertEquals("4033404108", str);
			break;
		}
		list = dao.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', "S", 20);
		assertEquals(20, list.size());
		for (String str : list) {
			assertEquals("4033404108", str);
			break;
		}
		list = dao.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', "S", 0);
		assertEquals(0, list.size());
		list = dao.retrieveSubscriberPhonenumbers('A', 'X', 'R', 'O', "S", 20);
		assertEquals(0, list.size());
	}

	@Test
	public void testRetrieveSubscriberListByPhoneNumbers() {
		String[] ary = { "4033501530", "4033404108", "9057160014" };
		// String[] ary={"4033501530"};
		Collection<SubscriberInfo> collection = dao.retrieveSubscriberListByPhoneNumbers(ary, true);
		// assertEquals(1,collection.size());
		for (SubscriberInfo inf : collection) {
			// System.out.println(inf);
		}
		// collection = dao.retrieveSubscriberListByPhoneNumbers(new
		// String[0],true);
		// assertEquals(null,collection);
	}

	
	
	@Test
	public void testRetrieveSubscriberListByPhoneNumber() throws ApplicationException {
		Collection<SubscriberInfo> collection = null;
		collection = dao.retrieveSubscriberListByPhoneNumbers(new String[] { "4033404108" }, 1, false);
		assertEquals(1, collection.size());
		for (SubscriberInfo inf : collection) {
			// System.out.println(inf);
		}
		collection = dao.retrieveSubscriberListByPhoneNumbers(new String[] { "4033501530" }, 1, false);
		assertEquals(0, collection.size());
	}

	@Test
	public void testRetrieveHSPASubscriberListByIMSI() throws ApplicationException {
		Collection<SubscriberInfo> collection = new ArrayList<SubscriberInfo>();

		collection = dao.retrieveHSPASubscriberListByIMSI("214030000050002", true);
		for (SubscriberInfo x : collection) {
			assertEquals(70104822, x.getBanId());
			assertEquals("3063063141", x.getSubscriberId());
		}

		collection = dao.retrieveHSPASubscriberListByIMSI("214030000050012", true);
		for (SubscriberInfo x : collection) {
			assertEquals(70104724, x.getBanId());
			assertEquals("9057160958", x.getSubscriberId());
			break;
		}
	}

	@Test
	public void testRetrieveSubscriberByPhoneNumber() throws ApplicationException {
		SubscriberInfo subInfo = null;
		String phoneNumber = "4033404108";
		subInfo = dao.retrieveSubscriberByPhoneNumber(phoneNumber);
		assertEquals("RSP", subInfo.getActivityCode());
		assertEquals("CRQ ", subInfo.getActivityReasonCode());
		assertEquals("C", subInfo.getProductType().trim());

	}

	@Test
	public void testRetrieveSubscriberListBySerialNumber() {
		Collection<SubscriberInfo> collection = new ArrayList<SubscriberInfo>();
		collection = dao.retrieveSubscriberListBySerialNumber("21101117206", true);
		assertEquals(2, collection.size());
		for (SubscriberInfo subinf : collection) {
			assertEquals("NAC", subinf.getActivityCode());
			assertEquals("MKPO", subinf.getActivityReasonCode());
			assertEquals("0000", subinf.getSalesRepId());
			break;
		}

	}

	@Test
	public void testRetrieveSubscriberListByBAN() {
		int ban = 0;
		int maximumCount = 100;
		boolean includeCancelled = true;
		String subId = "2507134539";
		// subId="";
		Collection<SubscriberInfo> collection = new ArrayList<SubscriberInfo>();
		collection = dao.retrieveSubscriberListByBAN(ban, maximumCount, includeCancelled, subId);
		// assertEquals(1,collection.size());
		if (collection == null) {
			// System.out.println("collection is null.");
		}
		for (SubscriberInfo subInfo : collection) {
			// assertEquals("RSP",subInfo.getActivityCode());
			// assertEquals("CRQ ",subInfo.getActivityReasonCode());
			// assertEquals("C",subInfo.getProductType().trim());
			System.out.println(subInfo);
		}
		// subId="";
		// collection =dao.retrieveSubscriberListByBAN(ban, maximumCount,
		// includeCancelled, subId);
		// assertEquals(null,collection);

	}

	@Test
	public void testRetrieveSubscriberListByBANNew() {
		int ban = 8;
		int maximumCount = 2;
		boolean includeCancelled = false;
		Collection<SubscriberInfo> collection = new ArrayList<SubscriberInfo>();
		collection = dao.retrieveSubscriberListByBAN(ban, maximumCount, includeCancelled);
		// assertEquals(1,collection.size());
		if (collection == null) {
			System.out.println("collection is null.");
		}
		for (SubscriberInfo subInfo : collection) {
			System.out.println(subInfo);
		}

	}

	@Test
	public void testRetrieveSubscriberListByBANAndSubId() {
		int ban = 70648203;
		int maximumCount = 10;
		boolean includeCancelled = false;
		String subId = "6472145381";
		Collection<SubscriberInfo> collection = new ArrayList<SubscriberInfo>();
		collection = dao.retrieveSubscriberListByBANAndSubscriberId(ban, subId, includeCancelled, maximumCount);
		if (collection == null) {
			// System.out.println("collection is null.");
		}
		for (SubscriberInfo subInfo : collection) {
			// System.out.println(subInfo);
		}
	}

	@Test
	public void testRetrieveSubscriberListBySubId() {
		int maximumCount = 1;
		boolean includeCancelled = true;
		String subId = "2507134539";
		Collection<SubscriberInfo> collection = new ArrayList<SubscriberInfo>();
		collection = dao.retrieveSubscriberListBySubscriberID(subId, includeCancelled, maximumCount);
		if (collection == null) {
			// System.out.println("collection is null.");
		}
		for (SubscriberInfo subInfo : collection) {
			// System.out.println(subInfo);
		}
	}

	@Test
	public void testIsPortRestricted() {
		int ban = 20070098;
		String subscriberId = "9057160034";
		String phoneNumber = "9057160034";
		String status = "C";

		boolean portRestricted = dao.isPortRestricted(ban, subscriberId, phoneNumber, status);
		assertFalse(portRestricted);
	}

	@Test
	public void testRetrieveSubscriptionId() throws ApplicationException {
		assertEquals(5203630, dao.retrieveSubscriptionId(8, "4164010950", "A"));
	}

	@Test
	public void testRetrieveSubscriberByBANAndPhoneNumberPT148() throws ApplicationException {
		try {
			long startTime = Calendar.getInstance().getTimeInMillis();
			SubscriberInfo si = dao.retrieveSubscriberByBANAndPhoneNumber(70655130, "5871901849");
			long endTime = Calendar.getInstance().getTimeInMillis();

			System.out.println("Time elapsed: " + (endTime - startTime));
			// assertEquals('C', si.getStatus());
//			System.out.println(si);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Test
	public void testRetrieveSubscriberByBANAndPhoneNumberD3() throws ApplicationException {
		try {
			long startTime = Calendar.getInstance().getTimeInMillis();
			SubscriberInfo si = dao.retrieveSubscriberByBANAndPhoneNumber(70104932, "9057160974");
			long endTime = Calendar.getInstance().getTimeInMillis();

			System.out.println("Time elapsed: " + (endTime - startTime));
			 assertEquals('C', si.getStatus());
//			System.out.println(si);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	
	// Business connect test cases - JULY 2014 Release
	
	@Test
	public void testRetrieveSubscriberListBySeatResourceNumber() throws ApplicationException {
		Collection<SubscriberInfo> collection = null;
		collection = dao.retrieveSubscriberListBySeatResourceNumber(25, "4166760000", 10, true);
		assertEquals(1, collection.size());
		for (SubscriberInfo inf : collection) {
			System.out.println(inf.getBanId());
		}
		collection = dao.retrieveSubscriberListBySeatResourceNumber(25, "4166780012", 10, false);
		assertEquals(0, collection.size());
	}
	
	@Test
	public void testRetrieveSubscriberListByPhoneNumberWithSeat() throws ApplicationException {
		Collection<SubscriberInfo> collection = null;
		collection = dao.retrieveSubscriberListByPhoneNumbers(new String[] { "4033404108" }, 1, false);
		assertEquals(1, collection.size());
		for (SubscriberInfo inf : collection) {
			System.out.println("Phone Number - "+inf.getPhoneNumber());
			System.out.println("Seat Type - "+inf.getSeatData().getSeatType());
			System.out.println("Seat group - "+inf.getSeatData().getSeatGroup());
		}
		collection = dao.retrieveSubscriberListByPhoneNumbers(new String[] { "4033501530" }, 1, false);
		assertEquals(0, collection.size());
	}

	
	
	@Test
	public void testRetrieveSubscriberListByBANAndSubIdWithSeat() {
		int ban = 8;
		int maximumCount = 10;
		boolean includeCancelled = false;
		String subId = "4033404108";
		Collection<SubscriberInfo> collection = new ArrayList<SubscriberInfo>();
		collection = dao.retrieveSubscriberListByBANAndSubscriberId(ban, subId, includeCancelled, maximumCount);
		for (SubscriberInfo subInfo : collection) {
			System.out.println("Phone Number - "+subInfo.getPhoneNumber());
			System.out.println("Seat Type - "+subInfo.getSeatData().getSeatType());
			System.out.println("Seat group - "+subInfo.getSeatData().getSeatGroup());
		}
	}
	
	@Test
	public void testRetrieveSubscriberListBySubIdWithSeat() {
		int maximumCount = 1;
		boolean includeCancelled = false;
		String subId = "2507134539";
		Collection<SubscriberInfo> collection = new ArrayList<SubscriberInfo>();
		collection = dao.retrieveSubscriberListBySubscriberID(subId, includeCancelled, maximumCount);
		for (SubscriberInfo subInfo : collection) {
			System.out.println("Phone Number - "+subInfo.getPhoneNumber());
			System.out.println("Seat Type - "+subInfo.getSeatData().getSeatType());
			System.out.println("Seat group - "+subInfo.getSeatData().getSeatGroup());
		}
	}
	
	@Test
	public void testRetrieveLightWeightSubscriberListByBANWithSeat() throws ApplicationException {

		int banId = 20002207;
		int listLength = 100;
		boolean isIDEN = false;
		boolean includeCancelled = true;
		List<LightWeightSubscriberInfo> lwsiList = dao.retrieveLightWeightSubscriberListByBAN(banId, isIDEN, listLength, includeCancelled);
		System.out.println(lwsiList.size());
		for (LightWeightSubscriberInfo sub : lwsiList) {
			System.out.println(sub.getPhoneNumber());
			if(sub.getSeatType()!=null)
			{
				System.out.println("Seat Type - "+sub.getSeatType());
				System.out.println("Seat group - "+sub.getSeatGroup());
			}
		}
	}
	
	@Test
	public void testRetrieveSubscriberListByBANWithSeat() {
		int ban = 20002207;
		int maximumCount = 100;
		boolean includeCancelled = true;
		Collection<SubscriberInfo> collection = new ArrayList<SubscriberInfo>();
		collection = dao.retrieveSubscriberListByBAN(ban, maximumCount, includeCancelled);
		for (SubscriberInfo subInfo : collection) {
			System.out.println("Phone Number - "+subInfo.getPhoneNumber());
			if(subInfo.getSeatData()!=null)
			{
			System.out.println("Seat Type - "+subInfo.getSeatData().getSeatType());
			System.out.println("Seat group - "+subInfo.getSeatData().getSeatGroup());
			}
		}
	}
	
	@Test
	public void testRetrievePortedSubscriberListByBANWithSeat() throws ApplicationException {

		int ban = 20002207;
		int listLength = 100;
		Collection<SubscriberInfo> subscriberInfoList = dao.retrievePortedSubscriberListByBAN(ban, listLength);
		for (SubscriberInfo list : subscriberInfoList) {
			System.out.println(list.getSubscriberId());
		}
		
	}
	
	@Test
	public void testRetrieveSeatResourceInfoByBanAndPhoneNumber() throws ApplicationException
	{
		List<ResourceInfo> resourceInfo = dao.retrieveSeatResourceInfoByBanAndPhoneNumber(25, "M000000041", true);
		for(ResourceInfo info: resourceInfo)
		{
			System.out.println("Resource Number"+info.getResourceNumber());
			System.out.println("Resource Type"+info.getResourceType());		
		}
	}
	
	@Test
	public void testRetrieveSubscriberBySeatResourceNumber() throws ApplicationException
	{
		SubscriberInfo info = dao.retrieveSubscriberBySeatResourceNumber("6471251999");
			System.out.println("Resource Number"+info.getSeatData().getSeatGroup());
			System.out.println("Resource Type"+info.getSeatData().getSeatType());	
	}
	
	@Test
	public void testRetrieveLatestSubscriberIdentifierInfoBySeatResourceNumber() throws ApplicationException
	{
		SubscriberIdentifierInfo info = dao.retrieveLatestSubscriberIdentifierInfoBySeatResourceNumber(0,"4166760000");
			System.out.println("Seat Group"+info.getSeatGroup());
			System.out.println("Seat Type"+info.getSeatType());		
	}
	
}