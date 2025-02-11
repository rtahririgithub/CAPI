package com.telus.cmb.subscriber.lifecyclehelper.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.ChargeTypeTax;
import com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.DepositHistoryInfo;
import com.telus.eas.account.info.InvoiceTaxInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.equipment.info.EquipmentSubscriberInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.portability.info.PortOutEligibilityInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.ContractChangeHistoryInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.HandsetChangeHistoryInfo;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.PrepaidCallHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidEventHistoryInfo;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.eas.subscriber.info.ProvisioningTransactionInfo;
import com.telus.eas.subscriber.info.ResourceChangeHistoryInfo;
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

public class SubscriberLifecycleHelperEJBIntTest {

	
	SubscriberLifecycleHelperRemote impl=null;
	//String url="t3://ln98556:31022"; //PT168
	String url="t3://ln99231:30022"; //PT148
	//String url="t3://localhost:7001";
	//String url="t3://lp97635:52025";
	//String url="t3://ln99244:43024"; //staging-b
	
	TestPointResultInfo testPointResultInfo ;

	@Before
	public void setup() throws Exception {
		javax.naming.Context context = new javax.naming.InitialContext(setEnvContext());
		getAccountLifecycleManagerRemote(context);		
	}

	private Hashtable<Object,Object> setEnvContext(){

		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, url);
		return env;
	}
	
	private void getAccountLifecycleManagerRemote(Context context) throws Exception{
		SubscriberLifecycleHelperHome subscriberInformationHelperHome = (SubscriberLifecycleHelperHome) context.lookup("SubscriberLifecycleHelper#com.telus.cmb.subscriber.lifecyclehelper.svc.impl.SubscriberLifecycleHelperHome");
		impl = (SubscriberLifecycleHelperRemote) subscriberInformationHelperHome.create();
	}
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePrepaidCallHistory()throws ApplicationException,RemoteException{
		List<PrepaidCallHistoryInfo> preHisList = new ArrayList<PrepaidCallHistoryInfo>();
		preHisList = impl.retrievePrepaidCallHistory("4162809905", getDateInput(2002,7,29),getDateInput(2011,9,28));
		assertEquals(2,preHisList.size());
		for(PrepaidCallHistoryInfo preCalHis :preHisList){
			assertEquals("9999050123",preCalHis.getCalledPhoneNumber());
			assertEquals(20.0,preCalHis.getStartBalance(),0);
			break;
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePrepaidEventHistory()throws ApplicationException,RemoteException{
		List<PrepaidEventHistoryInfo> preHisList = new ArrayList<PrepaidEventHistoryInfo>();
		preHisList = impl.retrievePrepaidEventHistory("4162809905", getDateInput(2002,7,29),getDateInput(2011,9,28));
		assertEquals(404,preHisList.size());
		for(PrepaidEventHistoryInfo preCalHis :preHisList){
			assertEquals(0.0,preCalHis.getAmount(),0);
			assertEquals("-14",preCalHis.getPrepaidEventTypeCode());
			break;
		}
		
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePrepaidEventHistory1()throws ApplicationException,RemoteException{
		PrepaidEventTypeInfo[] prepaidEventTypes = new PrepaidEventTypeInfo[1];
		PrepaidEventTypeInfo prepaidEventTypesob= new PrepaidEventTypeInfo();
		prepaidEventTypesob.setCode("-73");
		prepaidEventTypes[0]=prepaidEventTypesob;
		List<PrepaidEventHistoryInfo> preHisList = new ArrayList<PrepaidEventHistoryInfo>();
		preHisList = impl.retrievePrepaidEventHistory("4162809905", getDateInput(2002,7,29),getDateInput(2011,9,28),prepaidEventTypes);
		assertEquals(40,preHisList.size());
		for(PrepaidEventHistoryInfo preCalHis :preHisList){
			assertEquals(0,preCalHis.getAmount(),0);
			assertEquals("-73",preCalHis.getPrepaidEventTypeCode());
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveProvisioningTransactions()throws ApplicationException,RemoteException{
		//query for ProvisioningTransactions : select subscriber_no ,customer_id from srv_trx_repos 
		List<ProvisioningTransactionInfo> provTranList = new ArrayList<ProvisioningTransactionInfo>();
		provTranList = impl.retrieveProvisioningTransactions(70024088, "6478839964", getDateInput(2000,7,29),getDateInput(2011,9,28));
		assertEquals(3,provTranList.size());
		System.out.println("provTranList.size()()"+provTranList.size());
		for(ProvisioningTransactionInfo provIn:provTranList){
			assertEquals("71974",provIn.getTransactionNo());
			assertEquals("CS",provIn.getStatus());
			break;
		}
	}
	@Test
	public void testRetrieveSubscriberProvisioningStatus()throws ApplicationException,RemoteException{
		assertEquals("CS",impl.retrieveSubscriberProvisioningStatus(70024088, "6478839964"));
		assertEquals("CS",impl.retrieveSubscriberProvisioningStatus(70024358, "4162809925"));
		assertEquals(null,impl.retrieveSubscriberProvisioningStatus(1212, "12112"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveCallingCirclePhoneNumberListHistory() throws ApplicationException,RemoteException{

		int banId=70024088;
		String subscriberNo="6478839964";
		String productType="C";
		Date from=getDateInput(2000,7,29);
		Date to= getDateInput(2011,9,28);
		List<CallingCirclePhoneListInfo> list=Arrays.asList(impl.retrieveCallingCirclePhoneNumberListHistory(banId, subscriberNo, productType, from, to));
		for(CallingCirclePhoneListInfo ccpli:list){
			assertEquals(3,ccpli.getPhoneNumberList().length);
			assertEquals(new Date((2007-1900),(1-1),23),ccpli.getEffectiveDate());
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveContractChangeHistory() throws ApplicationException,RemoteException{

		int ban=70024358;
		String subscriberID="4162809925";
		Date from=getDateInput(2000,7,29);
		Date to= getDateInput(2011,9,28);
		List<ContractChangeHistoryInfo> list=Arrays.asList(impl.retrieveContractChangeHistory(ban, subscriberID, from, to));
		for(ContractChangeHistoryInfo cchi:list){
			assertEquals("ATME0PREPD",cchi.getDealerCode());
			assertEquals("PPD",cchi.getReasonCode());
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveFeatureParameterHistory() throws ApplicationException,RemoteException{

		int banId=70024358;
		String subscriberNo="4162809925";
		String productType="C";
		Date from=getDateInput(2000,7,29);
		Date to= getDateInput(2011,9,28);
		String[] parameterNames={"DATE-OF-BIRTH","CALLHOMEFREE","CALLING-CIRCLE"};
		List<FeatureParameterHistoryInfo> list=Arrays.asList(impl.retrieveFeatureParameterHistory(banId, subscriberNo, productType, parameterNames, from, to));
		for(FeatureParameterHistoryInfo fphi:list){
			assertEquals("FBC   ",fphi.getFeatureCode());
			assertEquals("1010",fphi.getParameterValue());
		}
	}

	@Test
	public void testRetrieveMultiRingPhoneNumbers() throws ApplicationException,RemoteException{

		String subscriberId="4162809925";
		List<String> list=Arrays.asList(impl.retrieveMultiRingPhoneNumbers(subscriberId));
		for(String s:list){
			assertEquals("4162809925",s);
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePricePlanChangeHistory() throws ApplicationException,RemoteException{

		int ban=70024358;
		String subscriberID="4162809925";
		Date from=getDateInput(2000,7,29);
		Date to= getDateInput(2011,9,28);
		List<PricePlanChangeHistoryInfo> list=Arrays.asList(impl.retrievePricePlanChangeHistory(ban, subscriberID, from, to));
		System.out.println(list.size());
		for(PricePlanChangeHistoryInfo ppchi:list){
			System.out.println(ppchi.getDealerCode());
			assertEquals("ATME0PREPD",ppchi.getDealerCode());
			assertEquals("20099",ppchi.getKnowbilityOperatorID());
			break;
		}
	}


	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveServiceChangeHistory() throws ApplicationException,RemoteException{

		int ban=70024358;
		String subscriberID="4162809925";
		Date from=getDateInput(2000,7,29);
		Date to= getDateInput(2011,9,28);
		boolean includeAllServices = false;
		List<ServiceChangeHistoryInfo> list=Arrays.asList(impl.retrieveServiceChangeHistory(ban, subscriberID, from, to, includeAllServices));
		for(ServiceChangeHistoryInfo schi:list){
			assertEquals("ATME0PREPD",schi.getDealerCode());
			assertEquals("CLHMAIRCB",schi.getServiceCode());
		}
	}


	@Test
	public void testRetrieveVoiceMailFeatureByPhoneNumber() throws ApplicationException,RemoteException{

		String phoneNumber = "4162809925";
		String productType = "C";
		List<String> list=Arrays.asList(impl.retrieveVoiceMailFeatureByPhoneNumber(phoneNumber, productType));
		System.out.println(list.size());
		for(String s:list){
			assertEquals("PPNTLKVM3",s);
			break;
		}
	}

	@Test
	public void testRetrieveServiceAgreementByPhoneNumber() throws ApplicationException,RemoteException{

		String phoneNumber = "4162809925";
		SubscriberContractInfo sci=impl.retrieveServiceAgreementByPhoneNumber(phoneNumber);
		assertEquals(5,sci.getServiceCount());
		assertEquals(26,sci.getFeatures(true).length);
		assertEquals(13,sci.getFeatures(false).length);
	}

	@Test
	public void testRetrieveVendorServiceChangeHistory() throws ApplicationException,RemoteException{

		int ban=70024358;
		String subscriberId="4162809925";
		String[] categoryCodes={"N51","CL","VM"};
		List<VendorServiceChangeHistoryInfo> list=Arrays.asList(impl.retrieveVendorServiceChangeHistory(ban, subscriberId, categoryCodes));
		System.out.println(list.size());
		for(VendorServiceChangeHistoryInfo vschi:list){
			assertEquals("A001000001",vschi.getVendorServiceCode());
			assertEquals(2,vschi.getPromoSOCs().length);
		}
	}
	@Test
	public void testRetrieveBanForPartiallyReservedSub()throws ApplicationException,RemoteException{
		SubscriberInfo subscriberInfo= new SubscriberInfo();
		subscriberInfo=impl.retrieveBanForPartiallyReservedSub("4162809925");
		assertEquals("4162809925",subscriberInfo.getSubscriberId());
		assertEquals('A',subscriberInfo.getStatus());
		assertEquals(70585961,subscriberInfo.getBanId());
		subscriberInfo=impl.retrieveBanForPartiallyReservedSub("1221");
		assertEquals(0,subscriberInfo.getBanId());

	}

	@Test
	public void testRetrieveBanIdByPhoneNumber()throws ApplicationException,RemoteException{
		assertEquals(44128611,impl.retrieveBanIdByPhoneNumber("4162809925"));
		assertEquals(44129574,impl.retrieveBanIdByPhoneNumber("7054303231"));
		assertEquals(0,impl.retrieveBanIdByPhoneNumber("2143321"));

	}
	@Test
	public void testRetrieveSubscriberAddress()throws ApplicationException,RemoteException{
		AddressInfo addressInfo= new AddressInfo();
		addressInfo= impl.retrieveSubscriberAddress(44128611,"4162809925");
		assertEquals("53 SOUTHVALE DR",addressInfo.getPrimaryLine());
		assertEquals(null,addressInfo.getRrIdentifier());
		addressInfo=impl.retrieveSubscriberAddress(121,"4162809925");
		assertEquals(null,addressInfo);
	}
	
	@Test
	public void testRetrieveDepositHistory()throws ApplicationException,RemoteException{
		int ban=196031;
		String subscriber="4038616724";
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
	public void testRetrieveTalkGroupsBySubscriber()throws ApplicationException,RemoteException{
		String subscriber="M000013003";
		List<TalkGroupInfo> list = new ArrayList<TalkGroupInfo>();
		list=impl.retrieveTalkGroupsBySubscriber(subscriber);
		assertEquals(1,list.size());
		for(TalkGroupInfo talkGrInf:list){
			assertEquals(6001076,talkGrInf.getOwnerBanId());
			assertEquals(905,talkGrInf.getFleetIdentity().getUrbanId());
		}
		list=impl.retrieveTalkGroupsBySubscriber("");
		assertEquals(0,list.size());
		list=impl.retrieveTalkGroupsBySubscriber(null);
		assertEquals(0,list.size());
	}
	@Test
	public void testRetrieveInvoiceTaxInfo()throws ApplicationException,RemoteException{
		int ban=44370;
		String subscriber="4036787744";
		int billSeqNo=58;
		InvoiceTaxInfo info = new InvoiceTaxInfo();
		ChargeTypeTax[] taxArray = new ChargeTypeTax[4];
		info = impl.retrieveInvoiceTaxInfo(ban, subscriber, billSeqNo);
		taxArray=info.getChargeTypeTaxes();
		assertEquals(2,info.getChargeTypeTaxes().length);
		assertEquals(0,taxArray[0].getRoamingTaxAmount(),0);
	}
	@Test
	public void testRetrieveLastMemo()throws ApplicationException,RemoteException{
		int ban=70351387;
		String subscriber=null;
		String memoType="CreditCheck";
		MemoInfo memoinfo = null;
		memoinfo = impl.retrieveLastMemo(ban, subscriber, memoType);
		assertEquals("A",memoinfo.getProductType());
		memoType="AUTH";
		memoinfo = impl.retrieveLastMemo(ban, subscriber, memoType);
		assertEquals("ROLE MANAGEMENT TOOL ASSIGNED - [9022120076: AA]",memoinfo.getText());
		subscriber="9022120076";
		memoType="AOFR";
		memoinfo = impl.retrieveLastMemo(ban, subscriber, memoType);
		assertEquals("Subscriber activated with dealer code/salesrep=1100027422/224F, handset model type=LG4600 and price plan=PE50SPARK",memoinfo.getText());
	}

	@Test
	public void testGetCountForRepairID()throws ApplicationException,RemoteException{
		assertEquals(9450,impl.getCountForRepairID("DUMMY0"));
		assertEquals(1,impl.getCountForRepairID("123123"));
		
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveHandsetChangeHistory()throws ApplicationException,RemoteException{
		int ban=70615514;
		String subscriberID="2502660468";
		Date from = getDateInput(2000, 1, 1);
		Date to = getDateInput(2012, 1, 1);
		List<HandsetChangeHistoryInfo> list= new ArrayList<HandsetChangeHistoryInfo>();
		list = impl.retrieveHandsetChangeHistory(ban, subscriberID, from, to);
		assertEquals(0,list.size());
		for(HandsetChangeHistoryInfo handset:list){
			assertEquals("24701409885",handset.getNewSerialNumber());
			assertEquals("24701409887",handset.getOldSerialNumber());
			break;
		}
	}

	@Test
	public void testRetrievePartiallyReservedSubscriberListByBan() throws ApplicationException,RemoteException{

		int ban=70020923;
		int maximum=100;
		List<String> subscriberList=(List<String>) impl.retrievePartiallyReservedSubscriberListByBan(ban, maximum);
		assertEquals(1,subscriberList.size());
		for(String list:subscriberList){
			assertEquals("6045565927",list);
			break;
		}
	}

	@Test
	public void testRetrievePortedSubscriberListByBAN() throws ApplicationException,RemoteException{

		int ban=70020923;
		int listLength=100;
		Collection<SubscriberInfo> subscriberInfoList=impl.retrievePortedSubscriberListByBAN(ban, listLength);
		assertEquals(0,subscriberInfoList.size());
		for(SubscriberInfo list:subscriberInfoList){
			assertEquals("SUS",list.getActivityCode());
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveResourceChangeHistory() throws ApplicationException,RemoteException{

		int ban=70136583;
		String subscriberID="M001850004";
		String type="H";
		Date from=getDateInput(2000, 1, 1);
		Date to= getDateInput(2012, 1, 1);
		List<ResourceChangeHistoryInfo> rchiList=Arrays.asList(impl.retrieveResourceChangeHistory(ban, subscriberID, type, from, to));
		assertEquals(3,rchiList.size());
		for(ResourceChangeHistoryInfo list:rchiList){
			assertEquals("A",list.getStatus());
			assertEquals("NOTAPI",list.getApplicationID());
			break;
		}
	}

	@Test
	public void testRetrieveLightWeightSubscriberListByBAN() throws ApplicationException,RemoteException{

		int banId=17605;
		int listLength=100;
		boolean isIDEN=false;
		boolean includeCancelled=false;
		List<LightWeightSubscriberInfo> lwsiList=(List<LightWeightSubscriberInfo>) impl.retrieveLightWeightSubscriberListByBAN(banId, isIDEN, listLength, includeCancelled);
		assertEquals(1,lwsiList.size());
		for(LightWeightSubscriberInfo list:lwsiList){
			assertEquals("Q",list.getFirstName());
			assertEquals("C",list.getProductType());
			break;
		}
	}

	@Test
	public void testRetrieveLastAssociatedSubscriptionId() throws ApplicationException,RemoteException{

		String imsi="302220999463678";
		String subscriptionId=impl.retrieveLastAssociatedSubscriptionId(imsi);
		assertEquals("8308232",subscriptionId);

	}

	@Test
	public void testRetrieveHotlineIndicator() throws ApplicationException,RemoteException{

		String subscriberId = "M001898104";
		assertFalse(impl.retrieveHotlineIndicator(subscriberId));
	}

	@Test
	public void testGetPortProtectionIndicator() throws ApplicationException,RemoteException{

		int ban = 313935;
		String subscriberId = "4037100322";
		String phoneNumber = "4037109656";
		String status = "A";
		String subscriptionId=impl.getPortProtectionIndicator(ban, subscriberId, phoneNumber, status);
		assertEquals(null,subscriptionId);

	}

	@Test
	public void testRetrieveSubscriptionPreference() throws ApplicationException,RemoteException{

		long subscriptionId=8295588;
		int preferenceTopicId=1;
		SubscriptionPreferenceInfo spi=impl.retrieveSubscriptionPreference(subscriptionId, preferenceTopicId);
		assertEquals(1,spi.getPreferenceTopicId());
		assertEquals("N",spi.getPreferenceValueTxt());
	}

	@Test
	public void testRetrieveAvailableCellularPhoneNumbersByRanges() throws ApplicationException,RemoteException, TelusAPIException{

		LineRangeInfo[] lineRanges=new LineRangeInfo[1];
		lineRanges[0]=new LineRangeInfo();
		lineRanges[0].setStart(41);
		lineRanges[0].setEnd(2);

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
		numberGroupInfo.setProvinceCode("BC");
		numberGroupInfo.setNumberLocation("TLS");
		numberGroupInfo.setNumberRanges(numberRanges);

		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setNumberGroup(numberGroupInfo);
		String startFromPhoneNumber = "41";
		String searchPattern = "*";
		boolean asian = true;
		int maxNumber = 0;

		//System.out.println(((NumberGroupInfo)phoneNumberReservation.getNumberGroup()).getNumberRanges().length);

		List<String> phoneNumberList=Arrays.asList(impl.retrieveAvailableCellularPhoneNumbersByRanges(phoneNumberReservation, startFromPhoneNumber, searchPattern, asian, maxNumber));
		assertEquals(maxNumber,phoneNumberList.size());
		for(String list:phoneNumberList){
			assertEquals("3067176000",list);
			break;
		}
	}

	@Test
	public void testRetrieveSubscriberIDByPhoneNumber() throws ApplicationException,RemoteException{

		int ban=2404;
		String phoneNumber="403-678-7744";
		String s=impl.retrieveSubscriberIDByPhoneNumber(ban, phoneNumber);
		assertEquals("40367877449",s);
	}

	@Test
	public void testRetrieveSubscriptionRole()throws ApplicationException,RemoteException{
		SubscriptionRoleInfo subscriptionRoleInfo=null;
		subscriptionRoleInfo=impl.retrieveSubscriptionRole("4037109998");
		assertEquals("AA",subscriptionRoleInfo.getCode());
		assertEquals(null,subscriptionRoleInfo.getDealerCode());
	}
	@Test
	public void testRetrieveSubscriberListByBanAndTalkGroup()throws ApplicationException,RemoteException{
		Collection<SubscriberInfo> collection = null;
		collection=impl.retrieveSubscriberListByBanAndTalkGroup(6000055, 905, 70, 1, 1);
		assertEquals(1,collection.size());
		for(SubscriberInfo subInf:collection){
			assertEquals("I",subInf.getProductType());
			assertEquals('A',subInf.getStatus());
			assertEquals("1100025800",subInf.getDealerCode());
		}
	}
	@Test
	public void testRetrieveSubscriberListByBanAndFleet()throws ApplicationException,RemoteException{
		Collection<SubscriberInfo> collection = null;
		collection=impl.retrieveSubscriberListByBanAndFleet(6000055, 905, 70, 1);
		assertEquals(1,collection.size());
		for(SubscriberInfo subInf:collection){
			assertEquals("I",subInf.getProductType());
			assertEquals('A',subInf.getStatus());
			assertEquals("1100025800",subInf.getDealerCode());
			break;
		}
	}	
	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveSubscriberHistory()throws ApplicationException,RemoteException{
		Collection<SubscriberHistoryInfo> collection = null;
		collection=impl.retrieveSubscriberHistory(194587, "4037109998", getDateInput(2000, 1, 1), getDateInput(2012, 1, 1));
		assertEquals(1,collection.size());
		for(SubscriberHistoryInfo subInf:collection){
			assertEquals('A',subInf.getStatus());
			assertEquals("CAPO",subInf.getActivityReasonCode());
			assertEquals(0,subInf.getPreviousBanId());
			break;
		}
	}	

	@Test
	public void testRetrieveSubscriberPhoneNumbers()throws ApplicationException,RemoteException{
		List<String> list =new ArrayList<String>();
		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', 10);
		assertEquals(10,list.size());
		for(String str:list){
			assertEquals("2047980182",str);
			break;
		}

		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', 5);
		assertEquals(5,list.size());
		for(String str:list){
			assertEquals("4033317837",str);
			break;
		}

		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O', 0);
		assertEquals(0,list.size());

		list=impl.retrieveSubscriberPhonenumbers('A', 'X', 'R', 'O', 20);
		assertEquals(0,list.size());

		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O',"S", 10);
		assertEquals(3,list.size());
		for(String str:list){
			assertEquals("4033317837",str);
			break;
		}
		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O',"S", 20);
		assertEquals(4,list.size());
		for(String str:list){
			assertEquals("4033317837",str);
			break;
		}

		list=impl.retrieveSubscriberPhonenumbers('A', 'I', 'R', 'O',"S", 0);
	     assertEquals(0,list.size());
	

		list=impl.retrieveSubscriberPhonenumbers('A', 'X', 'R', 'O', "S",20);
	assertEquals(0,list.size());
		
	}
	

	@Test
	public void testRetrieveHSPASubscriberListByIMSI() throws ApplicationException,RemoteException{
		Collection<SubscriberInfo> collection=new ArrayList<SubscriberInfo>();

		collection=impl.retrieveHSPASubscriberListByIMSI("302220999756791", true);
		for (SubscriberInfo x:collection){
			assertEquals(70567346, x.getBanId());
			assertEquals("7781752310", x.getSubscriberId());
		}

		collection=impl.retrieveHSPASubscriberListByIMSI("302220999756792", true);
		for (SubscriberInfo x:collection){
			assertEquals(70567348, x.getBanId());
			assertEquals("4184181266", x.getSubscriberId());
			assertEquals("100000000000000000", x.getSerialNumber());
			break;
		}
	}

	@Test
	public void testRetrieveSubscriberByPhoneNumber()throws ApplicationException,RemoteException{
		SubscriberInfo subInfo = null;
		String phoneNumber="2507091294";
		subInfo = impl.retrieveSubscriberByPhoneNumber(phoneNumber);
		System.out.println("port type"+subInfo.getPortType());
		System.out.println("Activity Code"+subInfo.getActivityCode());
		System.out.println("ActivityReasonCode Code"+subInfo.getActivityReasonCode());
		
		String phoneNumber1="2507139685";
		SubscriberInfo subInfo1 = null;
		subInfo1 = impl.retrieveSubscriberByPhoneNumber(phoneNumber1);
		System.out.println("porttype"+subInfo1.getPortType());
		System.out.println("Activity Code"+subInfo1.getActivityCode());
		System.out.println("ActivityReasonCode Code"+subInfo1.getActivityReasonCode());
	}
	@Test
	public void testRetrieveSubscriberListBySerialNumber()throws ApplicationException,RemoteException{
		Collection<SubscriberInfo> collection=new ArrayList<SubscriberInfo>();
		collection =impl.retrieveSubscriberListBySerialNumber("100000000000000000", true);
		assertEquals(12242,collection.size());
		collection =impl.retrieveSubscriberListBySerialNumber("300000000100225", false);
		assertEquals(0,collection.size());
	}
	
	@Test
	public void testIsPortRestricted() throws ApplicationException,RemoteException{
		int ban=70567348;
		String subscriberId = "4184181266";
		String phoneNumber = "4184181266";
		String status = "C";
		
		boolean portRestricted=impl.isPortRestricted(ban, subscriberId, phoneNumber, status);
		assertFalse(portRestricted);


	}
	
	@Test
	public void testRetrieveSubscriberListBySerialNumber1()throws ApplicationException,RemoteException{
		Collection<SubscriberInfo> collection=new ArrayList<SubscriberInfo>();
		collection =impl.retrieveSubscriberListBySerialNumber("06816771536",true);
		for (SubscriberInfo subscriberInfo : collection) {
			System.out.println(subscriberInfo.getSubscriberId());
		}
	}
	
	@Test
	public void testCheckSubscriberPortOutEligibility()throws ApplicationException,RemoteException{
		String phoneNumber = "2507091294";
		String ndpInd = "A";
		PortOutEligibilityInfo poe=impl.checkSubscriberPortOutEligibility(phoneNumber, ndpInd);
		assertFalse(poe.isTransferBlocking());

	}
	
	@Test
	public void testRetrieveSubscriberByPhoneNumber1() throws SystemException, ApplicationException,RemoteException{
		SubscriberInfo subInfo = null;
		int ban=26262530;
		String phoneNumber="6474568519";
		subInfo = impl.retrieveSubscriberByPhoneNumber(ban,phoneNumber);
		System.out.println(""+subInfo.toString());
//		assertEquals("NAC",subInfo.getActivityCode());
//		assertEquals("CA  ",subInfo.getActivityReasonCode());
//		assertEquals("C",subInfo.getProductType().trim());
		
	}
	
	@Test
	public void testRetrieveSubscriberListByBAN() throws SystemException, ApplicationException,RemoteException{
		Collection<SubscriberInfo> subInfo = null;
		int ban=70567348;
		int listLength=100;
		subInfo = impl.retrieveSubscriberListByBAN(ban, listLength);
		for(SubscriberInfo x:subInfo){
			assertEquals("CA  ",x.getActivityReasonCode());
			assertEquals("C",x.getProductType().trim());
			break;
		}
		
	}
	
	@Test
	public void testRetrieveSubscriberListByBAN1() throws SystemException, ApplicationException,RemoteException{
		Collection<SubscriberInfo> subInfo = null;
		int ban=70567348;
		int listLength=100;
		boolean includeCancelled=false;
		subInfo = impl.retrieveSubscriberListByBAN(ban, listLength, includeCancelled);
		for(SubscriberInfo x:subInfo){
			assertEquals("CA  ",x.getActivityReasonCode());
			assertEquals("C",x.getProductType().trim());
			break;
		}
	}
	
	@Test
	public void testRetrieveSubscriberListByImsi()throws ApplicationException,RemoteException{
		Collection<SubscriberInfo> subInfo = null;
		String imsi = "302220999756791";
		boolean includeCancelled = false;
		subInfo = impl.retrieveSubscriberListByImsi(imsi, includeCancelled);
		for(SubscriberInfo x:subInfo){
			assertEquals("CA  ",x.getActivityReasonCode());
			assertEquals("C",x.getProductType().trim());
			break;
		}
	}
	
	@Test
	public void testRetrieveSubscriberListByPhoneNumbers() throws ApplicationException,RemoteException{
		String[] ary={"7781752310","4184181266"};	
		Collection<SubscriberInfo> collection = impl.retrieveSubscriberListByPhoneNumbers(ary,true);
		assertEquals(2,collection.size());
		for(SubscriberInfo inf:collection){
			assertEquals("NAC",inf.getActivityCode());
			assertEquals("CA  ",inf.getActivityReasonCode());
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
	public void testRetrieveSubscriberListByBanAndPhoneNumber() throws ApplicationException,RemoteException{
		Collection<SubscriberInfo> collection = null;
		collection = impl.retrieveSubscriberListByPhoneNumber("7781752310",1,false);
		assertEquals(1,collection.size());
		for(SubscriberInfo inf:collection){
			assertEquals("NAC",inf.getActivityCode());
			assertEquals("CA  ",inf.getActivityReasonCode());
			assertEquals("C",inf.getProductType().trim());
		}
		collection = impl.retrieveSubscriberListByPhoneNumber("4033501530",1,false);
		assertEquals(0,collection.size());
	}
	
	@Test
	public void testRetrieveVoiceUsageSummary() throws ApplicationException,RemoteException{
		String subscriberId="4184181266";
		String featureCode="ABC";
		int banId=70567348;
		VoiceUsageSummaryInfo vsi=impl.retrieveVoiceUsageSummary(banId, subscriberId, featureCode);
		assertEquals(null,vsi);//no test data available
	}
	
	@Test
	public void testRetrievePhoneDirectory() throws Throwable {
		long TEST_SUBSCRIPTION_ID = 7019032;
		System.out.println("Entering Test method : testRetrievePhoneDirectory ");
		System.out.println("--------------------------------------------------");

		PhoneDirectoryEntry[] pdList = impl.getPhoneDirectory(TEST_SUBSCRIPTION_ID); 
		
		for(int i=0; i<pdList.length; i++) {
			System.out.println("Ph. Num. : " + pdList[i].getPhoneNumber());
			System.out.println("Nickname : " + pdList[i].getNickName());
			System.out.println("Effective  : " + pdList[i].getEffectiveDate());
			System.out.println("--------------------------------------------------");
		}

		System.out.println("Exiting Test method : testRetrievePhoneDirectory ");
		System.out.println("--------------------------------------------------");
	}

	@Test
	public void testUpdatePhoneDirectory() throws Throwable {
		long TEST_SUBSCRIPTION_ID = 7019032;
		System.out.println("Entering Test method : testUpdatePhoneDirectory ");
		System.out.println("--------------------------------------------------");

		PhoneDirectoryEntry pdEntry1 = new PhoneDirectoryEntry();
		pdEntry1.setPhoneNumber("5198702348");
		pdEntry1.setNickName("pt148 test");
		pdEntry1.setEffectiveDate(new Date());
		
		PhoneDirectoryEntry pdEntry2 = new PhoneDirectoryEntry();
		pdEntry2.setPhoneNumber("4165551234");
		pdEntry2.setNickName("pt148 test2");
		pdEntry2.setEffectiveDate(new Date());
		
		PhoneDirectoryEntry[] entries = new PhoneDirectoryEntry[] {pdEntry1, pdEntry2};

		impl.updatePhoneDirectory(TEST_SUBSCRIPTION_ID, entries); 

		System.out.println("Exiting Test method : testUpdatePhoneDirectory ");
		System.out.println("--------------------------------------------------");
	}
	@Test
	public void testRetrieveServiceAgreementBySubscriberID() throws ApplicationException,RemoteException{

		String subscriberId = "4162809925";
		SubscriberContractInfo sci=impl.retrieveServiceAgreementBySubscriberId(subscriberId);
		assertEquals(5,sci.getServiceCount());
		assertEquals(26,sci.getFeatures(true).length);
		assertEquals(13,sci.getFeatures(false).length);
	}

	@Test
	public void testRetrieveSubscriberIdentifiersByPhoneNumber() throws ApplicationException, RemoteException {
			
		SubscriberIdentifierInfo subscriberIdentifierInfo = impl.retrieveSubscriberIdentifiersByPhoneNumber(6376486,"4168945218");				
		assertEquals("4168945218", subscriberIdentifierInfo.getSubscriberId());
	}

	@Test
	public void testRetrieveEmailAddress() throws ApplicationException, RemoteException {			
	String emailAddress = impl.retrieveEmailAddress(6376486,"4168945218");
	System.out.println("EmailAddress"+emailAddress);
		
	}
	
	@Test
	public void testDatasources() throws ApplicationException, RemoteException {
		System.out.println("start testDatasources");
    testPointResultInfo = impl.test2EcmsDataSource();
	System.out.println("2ECMS TestPointResultInfo"+testPointResultInfo.toString()+"\n");
	testPointResultInfo = impl.test3EcmsDataSource();
	System.out.println("3ECMS TestPointResultInfo"+testPointResultInfo.toString()+"\n");
	testPointResultInfo = impl.testCodsDataSource();
	System.out.println("Cods TestPointResultInfo"+testPointResultInfo.toString()+"\n");
	testPointResultInfo = impl.testConeDataSource();
	System.out.println("Cone TestPointResultInfo"+testPointResultInfo.toString()+"\n");
	testPointResultInfo = impl.testDistDataSource();
	System.out.println("Dist TestPointResultInfo"+testPointResultInfo.toString()+"\n");
	testPointResultInfo = impl.testEasDataSource();
	System.out.println("Eas TestPointResultInfo"+testPointResultInfo.toString()+"\n");
	testPointResultInfo = impl.testEcpcsDataSource();
	System.out.println("Ecpcs TestPointResultInfo"+testPointResultInfo.toString()+"\n");
	testPointResultInfo = impl.testKnowbilityDataSource();
	System.out.println("Knowbility TestPointResultInfo"+testPointResultInfo.toString()+"\n");
	testPointResultInfo = impl.testServDataSource();
	System.out.println("Srv TestPointResultInfo"+testPointResultInfo.toString()+"\n");
	System.out.println("End testDatasources");
	}
	
	@Test
	public void testRetrieveSubscriberByPhonenumber()throws ApplicationException,RemoteException{
		//SubscriberInfo s= impl.retrieveSubscriberByBanAndPhoneNumber(70703289, "4160500197");
		AddressInfo addressInfo = impl.retrieveSubscriberAddress(70703289, "4160500197");
		System.out.println("address"+addressInfo);
	}

	
	@Test
	public void testRetrieveEquipmentSubscribers() throws ApplicationException,RemoteException{
		// HSPA equipmet subscriber
		String serialNumber = "8912239900000600736";
		boolean active=true;
		EquipmentSubscriberInfo[] esiList=impl.retrieveEquipmentSubscribers(serialNumber, active);
		for(EquipmentSubscriberInfo esi:esiList){
			assertEquals("7781652978", esi.getPhoneNumber());
			break;
		}
		
		//CDMA Equipemnt Subscriber
		
		String CdmaSerialNumber = "17600011912";
		boolean active1=true;
		EquipmentSubscriberInfo[] esiList1=impl.retrieveEquipmentSubscribers(CdmaSerialNumber, active1);
		for(EquipmentSubscriberInfo esi:esiList1){
			assertEquals("4161626449", esi.getPhoneNumber());
			break;
		}

	}
	
	@Test
	public void testRetrieveNonHSPAEquipmentSubscribers()
			throws ApplicationException, RemoteException {
		String serialNumber = "17600011912";
		boolean active = true;
		EquipmentSubscriberInfo[] equipmentSubscriberInfo = impl.retrieveNonHSPAEquipmentSubscribers(serialNumber, active);
		for (int i = 0; i < equipmentSubscriberInfo.length; i++) {
			System.out.println("equipmentSubscriberInfo"+equipmentSubscriberInfo[i].getPhoneNumber());
		}
	}


		@Test
		public void testRetrieveHSPAEquipmentSubscribers()
				throws ApplicationException, RemoteException {
			String serialNumber = "8912239900000600736";
			boolean active = true;
			String [] imsi = { "302221000423765" };
			EquipmentSubscriberInfo[] equipmentSubscriberInfo = impl.retrieveHSPAEquipmentSubscribers(serialNumber, active ,imsi);
			for (int i = 0; i < equipmentSubscriberInfo.length; i++) {
				System.out.println("equipmentSubscriberInfo"+equipmentSubscriberInfo[i].getPhoneNumber());
			}
	}
		
		//Business Connect Test Cases
		@Test
		public void testRetrieveSubscriberByPhoneNumberForBC()throws ApplicationException,RemoteException{
			SubscriberInfo subInfo = null;
			PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
			phoneNumberSearchOptionInfo.setSearchVOIP(true);
			String phoneNumber="5144181201";
			subInfo = impl.retrieveSubscriberByPhoneNumber(phoneNumber,phoneNumberSearchOptionInfo);
			System.out.println(""+subInfo);
			
			//assertEquals("OFFC",subInfo.getSeatData().getSeatType());
			//assertEquals("NARESH-ON-SC-Group1",subInfo.getSeatData().getSeatGroup());
			//assertEquals("V",subInfo.getSeatData().getResources()[0].getResourceType());
		}
		
		@Test
		public void testRetrieveSubscriberListByBanAndPhoneNumberForBC() throws ApplicationException,RemoteException{
			Collection<SubscriberInfo> collection = null;
			PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
			phoneNumberSearchOptionInfo.setSearchVOIP(true);
			collection = impl.retrieveSubscriberListByPhoneNumber("6471251999",phoneNumberSearchOptionInfo,1,false);
			assertEquals(1,collection.size());
			for(SubscriberInfo inf:collection){
				assertEquals("OFFC",inf.getSeatData().getSeatType());
				assertEquals("NARESH-ON-SC-Group1",inf.getSeatData().getSeatGroup());
				assertEquals("V",inf.getSeatData().getResources()[0].getResourceType());
			}
			//collection = impl.retrieveSubscriberListByPhoneNumber("4033501530",phoneNumberSearchOptionInfo,1,false);
			//assertEquals(0,collection.size());
		}
		
		@Test
		public void testRetrieveSubscriberByBanAndPhoneNumberForBC() throws ApplicationException,RemoteException{
			SubscriberInfo subscriber = null;
			PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
			phoneNumberSearchOptionInfo.setSearchWirelessNumber(true);
			phoneNumberSearchOptionInfo.setSearchVOIP(true);
			subscriber = impl.retrieveSubscriberByBanAndPhoneNumber(70702629,"5144181201",phoneNumberSearchOptionInfo);
			System.out.println(subscriber);
			
			//	assertEquals("OFFC",subscriber.getSeatData().getSeatType());
				//assertEquals("NARESH-ON-SC-Group1",subscriber.getSeatData().getSeatGroup());
				//assertEquals("V",subscriber.getSeatData().getResources()[0].getResourceType());
		}
		
		@Test
		public void testRetrieveSubscriberIdentifiersByPhoneNumberForBC() throws ApplicationException,RemoteException{
			SubscriberIdentifierInfo subscriber = null;
			PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
			phoneNumberSearchOptionInfo.setSearchWirelessNumber(true);
			phoneNumberSearchOptionInfo.setSearchVOIP(true);
			subscriber = impl.retrieveSubscriberIdentifiersByPhoneNumber(70701432,"6471251999",phoneNumberSearchOptionInfo);
				assertEquals("OFFC",subscriber.getSeatType());
				assertEquals("NARESH-ON-SC-Group1",subscriber.getSeatGroup());
		}
		

		@Test
		public void testretrieveSubscriberListByPhoneNumber() throws RemoteException, ApplicationException{
			SubscriberInfo subscriber = null;
			PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
			phoneNumberSearchOptionInfo.setSearchVOIP(true);
			Collection<SubscriberInfo> collection = impl.retrieveSubscriberListByPhoneNumber("5144182222", phoneNumberSearchOptionInfo, 1000, true);
			for(SubscriberInfo inf:collection){
				System.out.println(inf.getBanId());
			}
		}
}
