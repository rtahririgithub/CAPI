package com.telus.cmb.subscriber.lifecyclehelper.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.ChargeTypeTax;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.DepositHistoryInfo;
import com.telus.eas.account.info.InvoiceTaxInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.equipment.info.EquipmentSubscriberInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.portability.info.PortOutEligibilityInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.ContractChangeHistoryInfo;
//import com.telus.eas.subscriber.info.DataUsageDetailsInfo;
//import com.telus.eas.subscriber.info.DataUsageSummaryInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.HandsetChangeHistoryInfo;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.PrepaidCallHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidEventHistoryInfo;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.eas.subscriber.info.ProvisioningTransactionInfo;
import com.telus.eas.subscriber.info.ResourceChangeHistoryInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.eas.subscriber.info.VendorServiceChangeHistoryInfo;
import com.telus.eas.utility.info.LineRangeInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.NumberRangeInfo;
import com.telus.eas.utility.info.PrepaidEventTypeInfo;

/*
 *  A modified version of SubscriberLifecycleHelperIntTest. 
 *  Optionally remove "classpath:application-context-wsclient-prepaid.xml"
 *  in the @ContextConfiguration as it is no longer needed.
 *  The code meant for that xml file is now under common-svc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-lifecyclehelper.xml","classpath:application-context-wsclients-prepaid.xml"})
public class SubscriberLifecycleHelperIntTest2{

	@Autowired
	SubscriberLifecycleHelper impl;
	@Autowired
	SubscriberLifecycleFacade facadeImpl;
	String url = "t3://localhost:7001";
	String sessionId = null;
	
	/*static {
		//String ldapUrl = "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration";
		
		String ldapUrlPt148 ="ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration";
		//String ldapUrl = "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration";
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", ldapUrlPt148);
		
		System.setProperty ("getSubListByBan.method.rollback", "false");
		System.setProperty ("getSubListByBanAndPhoneNumber.method.rollback", "false");
	}*/
	
	@Before
	public void setup() throws Exception {
	System.setProperty("com.telusmobility.config.java.naming.provider.url"
			,"ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
//	System.setProperty("cmb.webservices.Provisioning.WirelessProvisioningService.url"
//			,"https://soa-mp-laird-pt148.tsl.telus.com/v1/smo/activation/serviceactivationmgmt/wirelessprovisioningservice-v1-0_vs0");
		javax.naming.Context context = new javax.naming.InitialContext(setEnvContext());
		getSubscriberLifecycleFacadeRemote(context);
		sessionId = facadeImpl.openSession("18654", "apollo", "SMARTDESKTOP");
		context.close();
	}

	private Hashtable<Object,Object> setEnvContext(){
		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, url);
		return env;
	}

	private void getSubscriberLifecycleFacadeRemote(Context context) throws Exception{
		//facadeImpl = (SubscriberLifecycleFacade)context.lookup(EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_FACADE);
		
		impl = (SubscriberLifecycleHelper) context.lookup(EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER);
		
		//impl = (AccountInformationHelper) context.lookup(EJBUtil.TELUS_CMBSERVICE_ACCOUNT_INFORMATION_HELPER);
		
		//referenceDataFacade = EJBUtil.getHelperProxy(ReferenceDataFacade.class, "ReferenceDataFacade#com.telus.cmb.reference.svc.impl.ReferenceDataFacadeHome");
		//referenceDataHelper = EJBUtil.getHelperProxy(ReferenceDataHelper.class, "ReferenceDataHelper#com.telus.cmb.reference.svc.impl.ReferenceDataHelperHome");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	public void testRetrieveLatestSubscriberByPhoneNumber() throws ApplicationException {
		String phoneNumber = "4160704352";
		SubscriberInfo sub = impl.retrieveLatestSubscriberByPhoneNumber(phoneNumber);
		if (sub == null) {
			System.out.println("Sub is null.");
		}else {
			System.out.println(sub);
		}
	}
	
	@Test
	public void testRetrieveSubscriberListByPhoneNumber() throws ApplicationException {
		String phoneNumber = "4168940078";
		int listLength = 10;
		boolean includeCancelled = true;
		Collection<SubscriberInfo> subs = impl.retrieveSubscriberListByPhoneNumber(phoneNumber, listLength, includeCancelled);
		
		for (SubscriberInfo s : subs) {
			System.out.println(s);
		}
	}
	
	@Test
	public void testRetrieveSubscriber() throws ApplicationException {
		int ban = 8;
		String subscriberId = "2507134539";
		
		SubscriberInfo subInfo = impl.retrieveSubscriber(ban, subscriberId);
		System.out.println(subInfo);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePrepaidCallHistory()throws ApplicationException{
		List<PrepaidCallHistoryInfo> preHisList = new ArrayList<PrepaidCallHistoryInfo>();
		preHisList = impl.retrievePrepaidCallHistory("4165545503", new Date(2009-1900,00,01), new Date(2010-1900,00,01));
		assertEquals(26,preHisList.size());
		for(PrepaidCallHistoryInfo preCalHis :preHisList){
			assertEquals("4165545503",preCalHis.getCalledPhoneNumber());
			assertEquals(499.41,preCalHis.getStartBalance(),0);
			break;
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePrepaidEventHistory()throws ApplicationException{
		List<PrepaidEventHistoryInfo> preHisList = new ArrayList<PrepaidEventHistoryInfo>();
		preHisList = impl.retrievePrepaidEventHistory("4165545503", new Date(2009-1900,00,11), new Date(2009-1900,11,11));
		//assertEquals(3,preHisList.size());
		for(PrepaidEventHistoryInfo preCalHis :preHisList){
			assertEquals(100,preCalHis.getAmount(),0);
			assertEquals("-5",preCalHis.getPrepaidEventTypeCode());
			break;
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePrepaidEventHistory1()throws ApplicationException{
		PrepaidEventTypeInfo[] prepaidEventTypes = new PrepaidEventTypeInfo[1];
		PrepaidEventTypeInfo prepaidEventTypesob= new PrepaidEventTypeInfo();
		prepaidEventTypesob.setCode("-51");
		prepaidEventTypes[0]=prepaidEventTypesob;
		List<PrepaidEventHistoryInfo> preHisList = new ArrayList<PrepaidEventHistoryInfo>();
		preHisList = impl.retrievePrepaidEventHistory("4165545503", new Date(2009-1900,00,11), new Date(2009-1900,11,11),prepaidEventTypes);
		//assertEquals(1,preHisList.size());
		for(PrepaidEventHistoryInfo preCalHis :preHisList){
			assertEquals(0,preCalHis.getAmount(),0);
			assertEquals("-51",preCalHis.getPrepaidEventTypeCode());
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveProvisioningTransactions()throws ApplicationException{
		List<ProvisioningTransactionInfo> provTranList = new ArrayList<ProvisioningTransactionInfo>();
		provTranList = impl.retrieveProvisioningTransactions(120045010, "7780551297", new Date(2000-1900,00,01), new Date(2010-1900,00,01));
		assertEquals(2,provTranList.size());
		for(ProvisioningTransactionInfo provIn:provTranList){
			assertEquals("36444",provIn.getTransactionNo());
			assertEquals("CS",provIn.getStatus());
			break;
		}
	}
	@Test
	public void testRetrieveSubscriberProvisioningStatus()throws ApplicationException{
		assertEquals("CE",impl.retrieveSubscriberProvisioningStatus(20070599, "9057160244"));
		assertEquals("CE",impl.retrieveSubscriberProvisioningStatus(20070334, "4168940079"));
		assertEquals(null,impl.retrieveSubscriberProvisioningStatus(1212, "12112"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveCallingCirclePhoneNumberListHistory() throws ApplicationException{

		int banId=70103921;
		String subscriberNo="9057160845";
		String productType="C";
		Date from=new Date((2006-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		List<CallingCirclePhoneListInfo> list=Arrays.asList(impl.retrieveCallingCirclePhoneNumberListHistory(banId, subscriberNo, productType, from, to));
		for(CallingCirclePhoneListInfo ccpli:list){
			assertEquals(3,ccpli.getPhoneNumberList().length);
			assertEquals(new Date((2007-1900),(1-1),23),ccpli.getEffectiveDate());
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveContractChangeHistory() throws ApplicationException{

		int ban=70103921;
		String subscriberID="9057160845";
		Date from=new Date((2006-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		List<ContractChangeHistoryInfo> list=Arrays.asList(impl.retrieveContractChangeHistory(ban, subscriberID, from, to));
		for(ContractChangeHistoryInfo cchi:list){
			assertEquals("A001000001",cchi.getDealerCode());
			assertEquals("PPD",cchi.getReasonCode());
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveFeatureParameterHistory() throws ApplicationException{

		int banId=20007348;
		String subscriberNo="7807183952";
		String productType="C";
		Date from=new Date((2000-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		String[] parameterNames={"DATE-OF-BIRTH","CALLHOMEFREE","CALLING-CIRCLE"};
		List<FeatureParameterHistoryInfo> list=Arrays.asList(impl.retrieveFeatureParameterHistory(banId, subscriberNo, productType, parameterNames, from, to));
		for(FeatureParameterHistoryInfo fphi:list){
			assertEquals("FBC   ",fphi.getFeatureCode());
			assertEquals("1212",fphi.getParameterValue());
		}
	}

	@Test
	public void testRetrieveMultiRingPhoneNumbers() throws ApplicationException{

		String subscriberId="4039990012";
		List<String> list=Arrays.asList(impl.retrieveMultiRingPhoneNumbers(subscriberId));
		for(String s:list){
			assertEquals("4031110001",s);
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePricePlanChangeHistory() throws ApplicationException{

		int ban=20001552;
		String subscriberID="4162060035";
		Date from=new Date((2000-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		List<PricePlanChangeHistoryInfo> list=Arrays.asList(impl.retrievePricePlanChangeHistory(ban, subscriberID, from, to));
		System.out.println(list.size());
		for(PricePlanChangeHistoryInfo ppchi:list){
			System.out.println(ppchi.getDealerCode());
			assertEquals("A001000001",ppchi.getDealerCode());
			assertEquals("18681",ppchi.getKnowbilityOperatorID());
			break;
		}
	}


	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveServiceChangeHistory() throws ApplicationException{

		int ban=70103921;
		String subscriberID="9057160845";
		Date from=new Date((2000-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		boolean includeAllServices = false;
		List<ServiceChangeHistoryInfo> list=Arrays.asList(impl.retrieveServiceChangeHistory(ban, subscriberID, from, to, includeAllServices));
		for(ServiceChangeHistoryInfo schi:list){
			assertEquals("A001000001",schi.getDealerCode());
			assertEquals("CLHMAIRCB",schi.getServiceCode());
		}
	}


	@Test
	public void testRetrieveVoiceMailFeatureByPhoneNumber() throws ApplicationException{

		String phoneNumber = "4162060035";
		String productType = "C";
		List<String> list=Arrays.asList(impl.retrieveVoiceMailFeatureByPhoneNumber(phoneNumber, productType));
		System.out.println(list.size());
		for(String s:list){
			assertEquals("FNTK150  ",s);
			break;
		}
	}

	@Test
	public void testRetrieveServiceAgreementByPhoneNumber() throws ApplicationException{

		String phoneNumber = "9057160845";
		SubscriberContractInfo sci=impl.retrieveServiceAgreementByPhoneNumber(phoneNumber);
		assertEquals(10,sci.getServiceCount());
		assertEquals(22,sci.getFeatures(true).length);
		assertEquals(6,sci.getFeatures(false).length);
	}

	@Test
	public void testRetrieveVendorServiceChangeHistory() throws ApplicationException{

		int ban=20580920;
		String subscriberId="4168940511";
		String[] categoryCodes={"N51","CL","VM"};
		List<VendorServiceChangeHistoryInfo> list=Arrays.asList(impl.retrieveVendorServiceChangeHistory(ban, subscriberId, categoryCodes));
		System.out.println(list.size());
		for(VendorServiceChangeHistoryInfo vschi:list){
			assertEquals("A001000001",vschi.getVendorServiceCode());
			assertEquals(2,vschi.getPromoSOCs().length);
		}
	}
	@Test
	public void testRetrieveBanForPartiallyReservedSub()throws ApplicationException{
		SubscriberInfo subscriberInfo= new SubscriberInfo();
		subscriberInfo=impl.retrieveBanForPartiallyReservedSub("4164160869");
		assertEquals("4164160869",subscriberInfo.getSubscriberId());
		assertEquals('A',subscriberInfo.getStatus());
		assertEquals(99999991,subscriberInfo.getBanId());
		subscriberInfo=impl.retrieveBanForPartiallyReservedSub("1221");
		assertEquals(0,subscriberInfo.getBanId());

	}

	@Test
	public void testRetrieveBanIdByPhoneNumber()throws ApplicationException{
		assertEquals(25,impl.retrieveBanIdByPhoneNumber("M000000021"));
		assertEquals(25,impl.retrieveBanIdByPhoneNumber("905*131072*3"));
		assertEquals(0,impl.retrieveBanIdByPhoneNumber("2143321"));

	}
	@Test
	public void testRetrieveSubscriberAddress()throws ApplicationException{
		AddressInfo addressInfo= new AddressInfo();
		addressInfo= impl.retrieveSubscriberAddress(8,"4033404108");
		assertEquals("58, STONETON DR",addressInfo.getPrimaryLine());
		//assertEquals("5775",addressInfo.getRrIdentifier());
		addressInfo=impl.retrieveSubscriberAddress(121,"4033404108");
		assertEquals(null,addressInfo);
	}
	/* No longer used
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveDataUsageDetails()throws ApplicationException{
		List<DataUsageDetailsInfo> list = new ArrayList<DataUsageDetailsInfo>();
		int ban=20581442;
		String phoneNumber="2049903359";
		Date date= new Date(2008-1900,01,12);
		String serviceType="subs";
		boolean isPostpaid=false;
		list=impl.retrieveDataUsageDetails(ban, phoneNumber, date, serviceType, isPostpaid);
		assertEquals(2,list.size());
		for(DataUsageDetailsInfo dinf:list){
			assertEquals("Pocket Express Promo", dinf.getEventName());
			assertEquals("handmark", dinf.getEventSource());
			break;
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveDataUsageSummary()throws ApplicationException{
		List<DataUsageSummaryInfo> list = new ArrayList<DataUsageSummaryInfo>();
		int ban=20581442;
		String phoneNumber="2049903359";
		Date fromDate= new Date(2008-1900,01,12);
		Date toDate= new Date(2008-1900,01,13);
		boolean isPostpaid=false;
		String[] emptyServiceTypes={};
		list=impl.retrieveDataUsageSummary(ban, phoneNumber, fromDate, toDate,emptyServiceTypes, isPostpaid);
		assertEquals(2,list.size());
		for(DataUsageSummaryInfo dinf:list){
			assertEquals("subs", dinf.getServiceType());
			assertEquals(2, dinf.getCount());
			break;
		}
		list=impl.retrieveDataUsageSummary(ban, phoneNumber, fromDate, toDate,emptyServiceTypes, true);
		assertEquals(0,list.size());
		String[] serviceTypes={"textmsg","subs"};
		list=impl.retrieveDataUsageSummary(ban, phoneNumber, fromDate, toDate, serviceTypes, isPostpaid);
		assertEquals(2,list.size());
		for(DataUsageSummaryInfo dinf:list){
			assertEquals("subs", dinf.getServiceType());
			assertEquals(2, dinf.getCount());
			break;
		}
		list=impl.retrieveDataUsageSummary(ban, phoneNumber, fromDate, toDate, serviceTypes, true);
		assertEquals(0,list.size());
	}
	*/
	@Test
	public void testRetrieveDepositHistory()throws ApplicationException{
		int ban=81;
		String subscriber="9057160005";
		List<DepositHistoryInfo> list= new ArrayList<DepositHistoryInfo>();
		list = impl.retrieveDepositHistory(ban, subscriber);
		assertEquals(1,list.size());
		for(DepositHistoryInfo depHisinf : list){
			assertEquals(200,depHisinf.getDepositPaidAmount(),0);
			assertEquals(200,depHisinf.getChargesAmount(),0);
		}
		list = impl.retrieveDepositHistory(121, "1212");
		assertEquals(0,list.size());
		list = impl.retrieveDepositHistory(121, null);
		assertEquals(0,list.size());

	}
	@Test
	public void testRetrieveTalkGroupsBySubscriber()throws ApplicationException{
		String subscriber="M000000203";
		List<TalkGroupInfo> list = new ArrayList<TalkGroupInfo>();
		list=impl.retrieveTalkGroupsBySubscriber(subscriber);
		assertEquals(1,list.size());
		for(TalkGroupInfo talkGrInf:list){
			assertEquals(84,talkGrInf.getOwnerBanId());
			assertEquals(905,talkGrInf.getFleetIdentity().getUrbanId());
		}
		list=impl.retrieveTalkGroupsBySubscriber("");
		assertEquals(0,list.size());
		list=impl.retrieveTalkGroupsBySubscriber(null);
		assertEquals(0,list.size());
	}
	@Test
	public void testRetrieveInvoiceTaxInfo()throws ApplicationException{
		int ban=8;
		String subscriber="4033404108";
		int billSeqNo=52;
		InvoiceTaxInfo info = new InvoiceTaxInfo();
		ChargeTypeTax[] taxArray = new ChargeTypeTax[4];
		info = impl.retrieveInvoiceTaxInfo(ban, subscriber, billSeqNo);
		taxArray=info.getChargeTypeTaxes();
		assertEquals(3,info.getChargeTypeTaxes().length);
		assertEquals(0,taxArray[0].getRoamingTaxAmount(),0);
	}
	@Test
	public void testRetrieveLastMemo()throws ApplicationException{
		int ban=99999999;
		String subscriber=null;
		String memoType="CreditCheck";
		MemoInfo memoinfo = null;
		memoinfo = impl.retrieveLastMemo(ban, subscriber, memoType);
		assertEquals(null,memoinfo);
		memoType="AUTH";
		memoinfo = impl.retrieveLastMemo(ban, subscriber, memoType);
		assertEquals("ROLE MANAGEMENT TOOL ASSIGNED - [9057160531: AA]",memoinfo.getText());
		subscriber="9057160531";
		memoType="AOFR";
		memoinfo = impl.retrieveLastMemo(ban, subscriber, memoType);
		assertEquals("Subscriber activated with dealer code/salesrep=1100030729/25KP, handset model type=null and price plan=PXTAL30",memoinfo.getText());
	}

	@Test
	public void testGetCountForRepairID()throws ApplicationException{
		assertEquals(918,impl.getCountForRepairID("DUMMY0"));
		assertEquals(4,impl.getCountForRepairID("123123"));
		assertEquals(0,impl.getCountForRepairID("12"));
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveHandsetChangeHistory()throws ApplicationException{
		int ban=20007953;
		String subscriberID="4168940137";
		Date from = new Date(2003-1900,07,12);
		Date to = new Date(2003-1900,07,12);
		List<HandsetChangeHistoryInfo> list= new ArrayList<HandsetChangeHistoryInfo>();
		list = impl.retrieveHandsetChangeHistory(ban, subscriberID, from, to);
		assertEquals(3,list.size());
		for(HandsetChangeHistoryInfo handset:list){
			assertEquals("24701409885",handset.getNewSerialNumber());
			assertEquals("24701409887",handset.getOldSerialNumber());
			break;
		}
	}

	@Test
	public void testRetrievePartiallyReservedSubscriberListByBan() throws ApplicationException{

		int ban=70104723;
		int maximum=100;
		List<String> subscriberList=(List<String>) impl.retrievePartiallyReservedSubscriberListByBan(ban, maximum);
		assertEquals(12,subscriberList.size());
		for(String list:subscriberList){
			assertEquals("6471551974",list);
			break;
		}
	}

	@Test
	public void testRetrievePortedSubscriberListByBAN() throws ApplicationException{

		int ban=20001552;
		int listLength=100;
		Collection<SubscriberInfo> subscriberInfoList=impl.retrievePortedSubscriberListByBAN(ban, listLength);
		assertEquals(5,subscriberInfoList.size());
		for(SubscriberInfo list:subscriberInfoList){
			assertEquals("SUS",list.getActivityCode());
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveResourceChangeHistory() throws ApplicationException{

		int ban=20007215;
		String subscriberID="M000000484";
		String type="H";
		Date from=new Date((2000-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		List<ResourceChangeHistoryInfo> rchiList=Arrays.asList(impl.retrieveResourceChangeHistory(ban, subscriberID, type, from, to));
		assertEquals(1,rchiList.size());
		for(ResourceChangeHistoryInfo list:rchiList){
			assertEquals("R",list.getStatus());
			assertEquals("API   ",list.getApplicationID());
			break;
		}
	}

	@Test
	public void testRetrieveLightWeightSubscriberListByBAN() throws ApplicationException{

		int banId=70103921;
		int listLength=100;
		boolean isIDEN=false;
		boolean includeCancelled=false;
		List<LightWeightSubscriberInfo> lwsiList=(List<LightWeightSubscriberInfo>) impl.retrieveLightWeightSubscriberListByBAN(banId, isIDEN, listLength, includeCancelled);
		assertEquals(1,lwsiList.size());
		for(LightWeightSubscriberInfo list:lwsiList){
			assertEquals("ALBATROS",list.getFirstName());
			assertEquals("C",list.getProductType());
			break;
		}
	}

	@Test
	public void testRetrieveLastAssociatedSubscriptionId() throws ApplicationException{

		String imsi="302220999950762";
		String subscriptionId=impl.retrieveLastAssociatedSubscriptionId(imsi);
		assertEquals("1023761",subscriptionId);

	}

	@Test
	public void testRetrieveHotlineIndicator() throws ApplicationException{

		String subscriberId = "7807183927";
		assertTrue(impl.retrieveHotlineIndicator(subscriberId));
	}

	@Test
	public void testGetPortProtectionIndicator() throws ApplicationException{

		int ban = 55673955;
		String subscriberId = "4164160074";
		String phoneNumber = "4164160074";
		String status = "A";
		String subscriptionId=impl.getPortProtectionIndicator(ban, subscriberId, phoneNumber, status);
		assertEquals("Y",subscriptionId);

	}

	@Test
	public void testRetrieveSubscriptionPreference() throws ApplicationException{

		long subscriptionId=12255794;
		int preferenceTopicId=1;
		SubscriptionPreferenceInfo spi=impl.retrieveSubscriptionPreference(subscriptionId, preferenceTopicId);
		assertEquals(1,spi.getPreferenceTopicId());
		assertEquals("N",spi.getPreferenceValueTxt());
	}

	@Test
	public void testRetrieveAvailableCellularPhoneNumbersByRanges() throws ApplicationException, TelusAPIException{

		LineRangeInfo[] lineRanges=new LineRangeInfo[1];
		lineRanges[0]=new LineRangeInfo();
		lineRanges[0].setStart(0000);
		lineRanges[0].setEnd(9999);

		NumberRangeInfo[] numberRanges=new NumberRangeInfo[2];
		numberRanges[0]=new NumberRangeInfo();
		numberRanges[0].setNXX(717);
		numberRanges[0].setNPA(306);
		numberRanges[0].setLineRanges(lineRanges);
		numberRanges[1]=new NumberRangeInfo();
		numberRanges[1].setNXX(974);
		numberRanges[1].setNPA(306);
		numberRanges[1].setLineRanges(lineRanges);

		NumberGroupInfo numberGroupInfo=new NumberGroupInfo();
		numberGroupInfo.setCode("SAS");
		numberGroupInfo.setProvinceCode("ON");
		numberGroupInfo.setNumberLocation("TLS");
		numberGroupInfo.setNumberRanges(numberRanges);

		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setNumberGroup(numberGroupInfo);
		String startFromPhoneNumber = "0";
		String searchPattern = "*";
		boolean asian = true;
		int maxNumber = 100;

		//System.out.println(((NumberGroupInfo)phoneNumberReservation.getNumberGroup()).getNumberRanges().length);

		List<String> phoneNumberList=Arrays.asList(impl.retrieveAvailableCellularPhoneNumbersByRanges(phoneNumberReservation, startFromPhoneNumber, searchPattern, asian, maxNumber));
		assertEquals(maxNumber,phoneNumberList.size());
		for(String list:phoneNumberList){
			assertEquals("3067176000",list);
			break;
		}
	}

	@Test
	public void testRetrieveSubscriberIDByPhoneNumber() throws ApplicationException{

		int ban=0;
		String phoneNumber="416-416-0074";
		String s=impl.retrieveSubscriberIDByPhoneNumber(ban, phoneNumber);
		assertEquals("4164160074",s);
	}

	@Test
	public void testRetrieveSubscriptionRole()throws ApplicationException{
		SubscriptionRoleInfo subscriptionRoleInfo=null;
		subscriptionRoleInfo=impl.retrieveSubscriptionRole("4033501530");
		assertEquals("ES",subscriptionRoleInfo.getCode());
		assertEquals(null,subscriptionRoleInfo.getDealerCode());
	}
	@Test
	public void testRetrieveSubscriberListByBanAndTalkGroup()throws ApplicationException{
		Collection<SubscriberInfo> collection = null;
		collection=impl.retrieveSubscriberListByBanAndTalkGroup(84, 905, 131077, 1, 1);
		assertEquals(1,collection.size());
		for(SubscriberInfo subInf:collection){
			assertEquals("I",subInf.getProductType());
			assertEquals('A',subInf.getStatus());
			assertEquals("0000000014",subInf.getDealerCode());
		}
	}
	@Test
	public void testRetrieveSubscriberListByBanAndFleet()throws ApplicationException{
		Collection<SubscriberInfo> collection = null;
		collection=impl.retrieveSubscriberListByBanAndFleet(84, 905, 131077, 8);
		assertEquals(8,collection.size());
		for(SubscriberInfo subInf:collection){
			assertEquals("I",subInf.getProductType());
			assertEquals('A',subInf.getStatus());
			assertEquals("0000000014",subInf.getDealerCode());
			break;
		}
	}	
	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveSubscriberHistory()throws ApplicationException{
		Collection<SubscriberHistoryInfo> collection = null;
		collection=impl.retrieveSubscriberHistory(8, "4033404108", new Date(2005-1900,02,31), new Date(2005-1900,02,31));
		assertEquals(1,collection.size());
		for(SubscriberHistoryInfo subInf:collection){
			assertEquals('A',subInf.getStatus());
			assertEquals("CRQ ",subInf.getActivityReasonCode());
			assertEquals(0,subInf.getPreviousBanId());
			break;
		}
	}	

	@Test
	public void testRetrieveSubscriberPhoneNumbers()throws ApplicationException{
		List<String> list =new ArrayList<String>();
		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', 10);
		assertEquals(3,list.size());
		for(String str:list){
			assertEquals("4033404108",str);
			break;
		}

		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', 5);
		assertEquals(5,list.size());
		for(String str:list){
			assertEquals("4033404108",str);
			break;
		}

		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', 0);
		assertEquals(0,list.size());

		list=impl.retrieveSubscriberPhonenumbers('A', 'X', 'R', 'O', 20);
		assertEquals(0,list.size());

		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O',"S", 10);
		assertEquals(10,list.size());
		for(String str:list){
			assertEquals("4033404108",str);
			break;
		}
		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O',"S", 20);
		assertEquals(20,list.size());
		for(String str:list){
			assertEquals("4033404108",str);
			break;
		}

		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O',"S", 0);
		assertEquals(0,list.size());

		list=impl.retrieveSubscriberPhonenumbers('A', 'X', 'R', 'O', "S",20);
		assertEquals(0,list.size());
	}
	@Test
	public void testRetrieveEquipmentSubscribers() throws ApplicationException{

		String serialNumber = "17905762942";
		boolean active=false;
		EquipmentSubscriberInfo[] esiList=impl.retrieveEquipmentSubscribers(serialNumber, active);
		for(EquipmentSubscriberInfo esi:esiList){
			assertEquals("4168940045", esi.getPhoneNumber());
			break;
		}

	}

	@Test
	public void testRetrieveHSPASubscriberListByIMSI() throws ApplicationException{
		Collection<SubscriberInfo> collection=new ArrayList<SubscriberInfo>();

		collection=impl.retrieveHSPASubscriberListByIMSI("214030000050002", true);
		for (SubscriberInfo x:collection){
			assertEquals(70104822, x.getBanId());
			assertEquals("3063063141", x.getSubscriberId());
		}

		collection=impl.retrieveHSPASubscriberListByIMSI("214030000050012", true);
		for (SubscriberInfo x:collection){
			assertEquals(70104724, x.getBanId());
			assertEquals("9057160958", x.getSubscriberId());
			assertEquals("100000000000000000", x.getSerialNumber());
			break;
		}
	}

	@Test
	public void testRetrieveSubscriberByPhoneNumber()throws ApplicationException{
		SubscriberInfo subInfo = null;
		String phoneNumber="7807186535";
		subInfo = impl.retrieveSubscriberByPhoneNumber(phoneNumber);
		System.out.println(subInfo);
	}
	@Test
	public void testRetrieveSubscriberListBySerialNumber()throws ApplicationException{
		Collection<SubscriberInfo> collection=new ArrayList<SubscriberInfo>();
		collection =impl.retrieveSubscriberListBySerialNumber("100000000000000000", true);
		assertEquals(51,collection.size());
		collection =impl.retrieveSubscriberListBySerialNumber("300000000100225", false);
		assertEquals(0,collection.size());
	}
	
	@Test
	public void testIsPortRestricted() throws ApplicationException{
		int ban=20070098;
		String subscriberId = "9057160034";
		String phoneNumber = "9057160034";
		String status = "C";
		
		boolean portRestricted=impl.isPortRestricted(ban, subscriberId, phoneNumber, status);
		assertFalse(portRestricted);

	}
	
	@Test
	public void testRetrieveSubscriberListBySerialNumber1()throws ApplicationException{
		Collection<SubscriberInfo> collection=new ArrayList<SubscriberInfo>();
		collection =impl.retrieveSubscriberListBySerialNumber("300000000100225");
		assertEquals(0,collection.size());
	}
	
	@Test
	public void testCheckSubscriberPortOutEligibility()throws ApplicationException{
		String phoneNumber = "9056309337";
		String ndpInd = "A";
		PortOutEligibilityInfo poe=impl.checkSubscriberPortOutEligibility(phoneNumber, ndpInd);
		assertFalse(poe.isTransferBlocking());
	}
	
	@Test
	public void testRetrieveSubscriberByPhoneNumber1() throws SystemException, ApplicationException{
		SubscriberInfo subInfo = null;
		int ban=70103921;
		String phoneNumber="9057160845";
		subInfo = impl.retrieveSubscriberByPhoneNumber(ban,phoneNumber);
		assertEquals("RSP",subInfo.getActivityCode());
		assertEquals("VAR ",subInfo.getActivityReasonCode());
		assertEquals("C",subInfo.getProductType().trim());
		
	}
	
	@Test
	public void testRetrieveSubscriberListByBAN() throws SystemException, ApplicationException{
		Collection<SubscriberInfo> subInfo = null;
		int ban=70103921;
		int listLength=100;
		subInfo = impl.retrieveSubscriberListByBAN(ban, listLength);
		for(SubscriberInfo x:subInfo){
			assertEquals("VAR ",x.getActivityReasonCode());
			assertEquals("C",x.getProductType().trim());
			break;
		}
		
	}
	
	@Test
	public void testRetrieveSubscriberListByBAN1() throws SystemException, ApplicationException{
		Collection<SubscriberInfo> subInfo = null;
		int ban=70103921;
		int listLength=100;
		boolean includeCancelled=false;
		subInfo = impl.retrieveSubscriberListByBAN(ban, listLength, includeCancelled);
		for(SubscriberInfo x:subInfo){
			assertEquals("VAR ",x.getActivityReasonCode());
			assertEquals("C",x.getProductType().trim());
			break;
		}
	}
	
	@Test
	public void testRetrieveSubscriberListByImsi()throws ApplicationException{
		Collection<SubscriberInfo> subInfo = null;
		String imsi = "302220999918616";
		boolean includeCancelled = false;
		subInfo = impl.retrieveSubscriberListByImsi(imsi, includeCancelled);
		for(SubscriberInfo x:subInfo){
			assertEquals("CMER",x.getActivityReasonCode());
			assertEquals("C",x.getProductType().trim());
			break;
		}
	}
	
	@Test
	public void testRetrieveSubscriberListByPhoneNumbers() throws ApplicationException{
		String[] ary={"4033501530","4033404108"};	
		Collection<SubscriberInfo> collection = impl.retrieveSubscriberListByPhoneNumbers(ary,true);
		assertEquals(1,collection.size());
		for(SubscriberInfo inf:collection){
			assertEquals("RCL",inf.getActivityCode());
			assertEquals("CRQ ",inf.getActivityReasonCode());
			assertEquals("C",inf.getProductType().trim());
		}
		collection=null;
		try{
		collection = impl.retrieveSubscriberListByPhoneNumbers(new String[0],true);
		fail("Exception Expected");
		}catch(Exception e){
			assertEquals(null,collection);	
		}
	}
	
	@Test
	public void testRetrieveSubscriberListByBanAndPhoneNumber() throws ApplicationException{
		Collection<SubscriberInfo> collection = null;
		collection = impl.retrieveSubscriberListByPhoneNumber("4033404108",1,false);
		assertEquals(1,collection.size());
		for(SubscriberInfo inf:collection){
			assertEquals("RCL",inf.getActivityCode());
			assertEquals("CRQ ",inf.getActivityReasonCode());
			assertEquals("C",inf.getProductType().trim());
		}
		collection = impl.retrieveSubscriberListByPhoneNumber("4033501530",1,false);
		assertEquals(0,collection.size());
	}
	
	@Test
	public void testRetrieveVoiceUsageSummary() throws ApplicationException{
		String subscriberId="4033501530";
		String featureCode="ABC";
		int banId=11234;
		VoiceUsageSummaryInfo vsi=impl.retrieveVoiceUsageSummary(banId, subscriberId, featureCode);
		assertEquals(null,vsi);//no test data available
	}
	
	@Test
	public void testGetSubscriptionId() throws ApplicationException {
		assertEquals(5203630, impl.getSubscriptionId(8, "4164010950", "A"));
	}
	
	//4161742650
	//defect - 4161644474
	//defect#2 - 7782054244
	//defect#3 - 4160605118
	@Test
	public void testRetrieveFeaturesForPrepaidSubscriber2(){
		String phoneNumber="4160704114";

		try {
			 System.out.println("Calling");
			 ServiceAgreementInfo[] serviceAgreementInfos = impl.retrieveFeaturesForPrepaidSubscriber(phoneNumber);
			 for(ServiceAgreementInfo o: serviceAgreementInfos){
				 System.out.println("Feature:");
				 System.out.println("" + o.toString());
			 }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
