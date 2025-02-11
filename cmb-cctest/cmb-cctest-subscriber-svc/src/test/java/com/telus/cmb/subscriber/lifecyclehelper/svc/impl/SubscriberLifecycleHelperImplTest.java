package com.telus.cmb.subscriber.lifecyclehelper.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.cmb.common.aop.utilities.BanValidatorTestHelper;
import com.telus.cmb.subscriber.lifecyclehelper.dao.AccountDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.AddressDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.DepositDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.FleetDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.InvoiceDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.PrepaidDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.ProvisioningDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.ServiceAgreementDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.SubscriberDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.UsageDao;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.DepositHistoryInfo;
import com.telus.eas.account.info.InvoiceTaxInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.equipment.info.EquipmentSubscriberInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.ContractChangeHistoryInfo;
import com.telus.eas.subscriber.info.EquipmentChangeHistoryInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.HandsetChangeHistoryInfo;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.PrepaidCallHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidEventHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidPromotionDetailInfo;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.eas.subscriber.info.ProvisioningTransactionInfo;
import com.telus.eas.subscriber.info.ResourceChangeHistoryInfo;
import com.telus.eas.subscriber.info.ResourceInfo;
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

public class SubscriberLifecycleHelperImplTest {

	SubscriberLifecycleHelperImpl impl = new SubscriberLifecycleHelperImpl();

	private TestPrepaidDao testPrepaidDao;
	private TestProvisioningDao testProvisioningDao;
	private TestServiceAgreementDao testServiceAgreementDao;
	private TestSubscriberDao testSubscriberDao;
	private TestAccountDao testAccountDao;
	private TestAddressDao testAddressDao;
	private TestDepositDao testDepositDao;
	private TestInvoiceDao testInvoiceDao;
	private TestFleetDao testFleetDao;
	private TestUsageDao testUsageDao;
	private TestSubscriberEquipmentDao testSubscriberEquipmentDao;

	@Before
	public void setup() {
		testPrepaidDao = new TestPrepaidDao();
		impl.setPrepaidDao(testPrepaidDao);	

		testProvisioningDao = new TestProvisioningDao();
		impl.setProvisioningDao(testProvisioningDao);	

		testServiceAgreementDao=new TestServiceAgreementDao();
		impl.setServiceAgreementDao(testServiceAgreementDao);

		testSubscriberDao=new TestSubscriberDao();
		impl.setSubscriberDao(testSubscriberDao);

		testAccountDao=new TestAccountDao();
		impl.setAccountDao(testAccountDao);

		testAddressDao=new TestAddressDao();
		impl.setAddressDao(testAddressDao);

		testDepositDao = new TestDepositDao();
		impl.setDepositDao(testDepositDao);

		testInvoiceDao = new TestInvoiceDao();
		impl.setInvoiceDao(testInvoiceDao);

		testFleetDao = new TestFleetDao();
		impl.setFleetDao(testFleetDao);

		testUsageDao = new TestUsageDao();
		impl.setUsageDao(testUsageDao);

		testSubscriberEquipmentDao = new TestSubscriberEquipmentDao();
		impl.setSubscriberEquipmentDao(testSubscriberEquipmentDao);

	}
	private static class TestSubscriberEquipmentDao implements com.telus.cmb.subscriber.lifecyclehelper.dao.SubscriberEquipmentDao{
		String repairID;
		int ban;
		String subscriberID;
		public String serialNumber;
		private boolean active;
		String method=null;
		@Override
		public int getCountForRepairID(String repairID) {
			this.repairID=repairID;
			method="getCountForRepairID";
			return 0;
		}

		@Override
		public List<EquipmentChangeHistoryInfo> retrieveEquipmentChangeHistory(
				int ban, String subscriberID, Date from, Date to) {
			this.ban=ban;
			this.subscriberID=subscriberID;
			method="retrieveEquipmentChangeHistory";
			return null;
		}

		@Override
		public List<HandsetChangeHistoryInfo> retrieveHandsetChangeHistory(
				int ban, String subscriberID, Date from, Date to) {
			this.ban=ban;
			this.subscriberID=subscriberID;
			method="retrieveHandsetChangeHistory";
			return null;
		}

		@Override
		public Hashtable<String,String> getUSIMListByIMSIs(String[] IMISIs) {
			// TODO Auto-generated method stub
			method="getUSIMListByIMSIs";
			return null;
		}

		@Override
		public List<String> getIMSIsByUSIM(String uSIM_Id) {
			method="getIMSIsByUSIM";
			this.serialNumber=uSIM_Id;
			List<String> list=new ArrayList<String>();
			
			if(serialNumber=="noIMSIserialnumber")
				return list;
			else{
				list.add("ABC");
				list.add("DEF");
				return list;
			}
		}

		@Override
		public List<EquipmentSubscriberInfo> retrieveEquipmentSubscribers(
				String serialNumber, boolean active) {
			this.serialNumber=serialNumber;
			this.active=active;
			return new ArrayList<EquipmentSubscriberInfo>();
		}

		@Override
		public List<String> getIMSIsBySerialNumber(String serialNumber) {
			this.serialNumber=serialNumber;
			method="getIMSIsBySerialNumber";
			this.serialNumber=serialNumber;
			List<String> list=new ArrayList<String>();
			if (serialNumber=="111") list.add("IMSI");
			return list;
		}

	}
	
	private static class TestFleetDao implements FleetDao{
		private String subscriberId=null;
		@Override
		public List<TalkGroupInfo> retrieveTalkGroupsBySubscriber(
				String subscriberId) {
			this.subscriberId=subscriberId;
			return null;
		}

	}

	private static class  TestAccountDao implements AccountDao{
		String phoneNumber=null;
		@Override
		public SubscriberInfo retrieveBanForPartiallyReservedSub(
				String phoneNumber) {
			this.phoneNumber=phoneNumber;
			return null;
		}

		@Override
		public int retrieveBanIdByPhoneNumber(String phoneNumber) {
			return Integer.valueOf(phoneNumber);
		}
	}
	
	private static class TestAddressDao implements AddressDao{
		int ban;
		String	subscriber;
		@Override
		public AddressInfo retrieveSubscriberAddress(int ban, String subscriber) {
			this.ban=ban;
			this.subscriber=subscriber;
			return null;
		}
		

	}
	private static class TestDepositDao implements DepositDao{
		int ban;
		String	subscriber;
		@Override
		public List<DepositHistoryInfo> retrieveDepositHistory(int ban,
				String subscriber) {
			this.ban=ban;
			this.subscriber=subscriber;
			return null;
		}

		@Override
		public double retrievePaidSecurityDeposit(int banId,
				String subscriberNo, String productType) {
			this.ban=banId;
			this.subscriber=subscriberNo;
			return 0;
		}

	}

	private static class TestInvoiceDao implements InvoiceDao{
		String subscriberId=null;
		int billSeqNo;
		int ban;
		@Override
		public InvoiceTaxInfo retrieveInvoiceTaxInfo(int ban,
				String subscriberId, int billSeqNo) {
			this.subscriberId=subscriberId;
			this.ban=ban;
			this.billSeqNo=billSeqNo;
			return null;
		}

	}

	private static class TestProvisioningDao implements ProvisioningDao{
		String phoneNumber;
		int ban;
		@Override
		public List<ProvisioningTransactionInfo> retrieveProvisioningTransactions(
				int customerID, String subscriberID, Date from, Date toDate) {
			this.phoneNumber=subscriberID;
			this.ban=customerID;			
			return null;
		}

		@Override
		public String retrieveSubscriberProvisioningStatus(int ban,
				String phoneNumber) {
			this.phoneNumber=phoneNumber;
			this.ban=ban;
			return null;
		}

	}
	private static class TestServiceAgreementDao implements ServiceAgreementDao {

		public String subscriberNo;
		public String productType;
		public String subscriberID;
		public Date fromDate;
		public int ban;
		public boolean includeAllServices;
		public String phoneNumber;
		public String[] categoryCodes;

		@Override
		public List<CallingCirclePhoneListInfo> retrieveCallingCirclePhoneNumberListHistory(
				int banId, String subscriberNo, String productType, Date from,
				Date to) {
			this.subscriberNo=subscriberNo;
			this.productType=productType;
			return null;
		}

		@Override
		public List<ContractChangeHistoryInfo> retrieveContractChangeHistory(
				int ban, String subscriberID, Date from, Date to) {
			this.subscriberID=subscriberID;
			this.fromDate=from;
			return null;
		}

		@Override
		public List<FeatureParameterHistoryInfo> retrieveFeatureParameterHistory(
				int banId, String subscriberNo, String productType,
				String[] parameterNames, Date from, Date to) {
			this.subscriberNo=subscriberNo;
			this.productType=productType;
			return null;
		}

		@Override
		public List<String> retrieveMultiRingPhoneNumbers(String subscriberId) {
			this.subscriberID=subscriberId;
			return null;
		}

		@Override
		public List<PricePlanChangeHistoryInfo> retrievePricePlanChangeHistory(
				int ban, String subscriberID, Date from, Date to) {
			this.subscriberID=subscriberID;
			this.ban=ban;
			return null;
		}

		@Override
		public SubscriberContractInfo retrieveServiceAgreementBySubscriberID(
				String subscriberID) {
			SubscriberContractInfo sci=new SubscriberContractInfo();
			sci.setCommitmentMonths(12);
			sci.setCommitmentReasonCode("ABC");
			sci.setPricePlanDealerCode(subscriberID); //just for testing
			return sci;
		}

		@Override
		public List<ServiceChangeHistoryInfo> retrieveServiceChangeHistory(
				int ban, String subscriberID, Date from, Date to,
				boolean includeAllServices) {
			this.subscriberID=subscriberID;
			this.includeAllServices=includeAllServices;
			return null;
		}

		@Override
		public List<VendorServiceChangeHistoryInfo> retrieveVendorServiceChangeHistory(
				int ban, String subscriberId, String[] categoryCodes) {
			this.subscriberID=subscriberId;
			this.categoryCodes=categoryCodes;
			return null;
		}

		@Override
		public List<String> retrieveVoiceMailFeatureByPhoneNumber(
				String phoneNumber, String productType) {
			this.phoneNumber=phoneNumber;
			this.productType=productType;
			return null;
		}

		@Override
		public FeatureParameterHistoryInfo retrieveLastEffectiveFeatureParameter(
				int banId, String subscriberId, String productType,
				String serviceCode, String featureCode) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<FeatureParameterHistoryInfo> retrieveCallingCircleParametersByDate(
				int banId, String subscriberId, String productType,
				Date fromDate) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<String> retrieveCSCFeatureByPhoneNumber(String phoneNumber,
				String productType) {
			// TODO Auto-generated method stub
			return null;
		}


	}

	private static class TestPrepaidDao implements PrepaidDao{
		String phoneNumber=null;
		int featureId;
		String userID=null;
		String method=null;
		Date from;
		

		
		@Override
		public List<PrepaidCallHistoryInfo> retrievePrepaidCallHistory(
				String phoneNumber, Date from, Date to) {
			this.phoneNumber=phoneNumber;
			this.from=from;
			method="retrievePrepaidCallHistory1";
			return null;
		}

		@Override
		public List<PrepaidEventHistoryInfo> retrievePrepaidEventHistory(
				String phoneNumber, Date from, Date to) {
			this.phoneNumber=phoneNumber;
			this.from=from;
			method="retrievePrepaidEventHistory1";
			return null;
		}

		@Override
		public List<PrepaidEventHistoryInfo> retrievePrepaidEventHistory(
				String phoneNumber, Date from, Date to,
				PrepaidEventTypeInfo[] prepaidEventTypes) {
			this.phoneNumber=phoneNumber;
			this.from=from;
			method="retrievePrepaidEventHistory2";
			return null;
		}

		

		

	}

	private static class TestSubscriberDao implements SubscriberDao{

		public int ban;
		public String subscriberID;
		public String type;
		public boolean isIDEN;
		public boolean includeCancelled;
		public String imsi;
		public String status;
		public long subscriptionId;
		public int preferenceTopicId;
		public LineRangeInfo[] lineRanges;
		public NumberRangeInfo[] numberRanges;
		public boolean asian;
		public String phoneNumber;
		private String serialNumber;
		private String portIndicator;
		private boolean notEmpty;
		private int maxCount;
		private int urbanId;
		private int fleetId;
		private int talkGroupId;
		private String[] phoneNumbers;
		private char subscriberstatus;
		private char accountType;
		private String addressType;

		

		@Override
		public List<String> retrievePartiallyReservedSubscriberListByBan(
				int ban, int maximum) {
			this.ban=ban;
			return null;
		}

		@Override
		public Collection<SubscriberInfo> retrievePortedSubscriberListByBAN(
				int ban, int listLength) {
			this.ban=ban;
			return null;
		}
        @Override
        public String retrieveChangedSubscriber( int ban, String subscriberId, String productType, Date searchFromDate,  Date searchToDate) throws ApplicationException {
	        this.ban=ban;
	        this.subscriberID=subscriberId;
        	return null;
          }
		@Override
		public List<ResourceChangeHistoryInfo> retrieveResourceChangeHistory(
				int ban, String subscriberID, String type, Date from, Date to) {
			this.ban=ban;
			this.subscriberID=subscriberID;
			this.type=type;
			return new ArrayList<ResourceChangeHistoryInfo>();
		}

		@Override
		public List<LightWeightSubscriberInfo> retrieveLightWeightSubscriberListByBAN(
				int banId, boolean isIDEN, int listLength,
				boolean includeCancelled) {
			this.ban=banId;
			return null;
		}

		@Override
		public String retrieveLastAssociatedSubscriptionId(String imsi) {
			this.imsi=imsi;
			return null;
		}

		@Override
		public boolean retrieveHotlineIndicator(String subscriberId) {
			this.subscriberID=subscriberId;
			return true;
		}

		@Override
		public String getPortProtectionIndicator(int ban, String subscriberId,
				String phoneNumber, String status) {
			this.ban=ban;
			this.subscriberID=subscriberId;
			this.status=status;
			String indicator="N";
			if(subscriberId!=null)
				indicator="Y";
			this.portIndicator=indicator;
			return indicator;
		}

		@Override
		public SubscriptionPreferenceInfo retrieveSubscriptionPreference(
				long subscriptionId, int preferenceTopicId) {
			this.subscriptionId=subscriptionId;
			this.preferenceTopicId=preferenceTopicId;
			return null;
		}

		@Override
		public List<String> retrieveAvailableCellularPhoneNumbersByRanges(
				PhoneNumberReservationInfo phoneNumberReservation,
				String startFromPhoneNumber, String searchPattern,
				boolean asian, int maxNumber) throws ApplicationException {

			try {
				this.numberRanges=((NumberGroupInfo)phoneNumberReservation.getNumberGroup()).getNumberRanges();
			} catch (TelusAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.lineRanges=this.numberRanges[0].getLineRanges();
			this.asian=asian;
			return new ArrayList<String>();
		}

		@Override
		public boolean isPortRestricted(int ban, String subscriberId,
				String phoneNumber, String status) {
			this.ban=ban;
			this.phoneNumber=phoneNumber;
			this.status=status;
			return false;
		}

		@Override
		public SubscriberInfo retrieveSubscriberByPhoneNumber(String phoneNumber) {
			this.phoneNumber=phoneNumber;
			return new SubscriberInfo();
		}

		@Override
		public List<SubscriberHistoryInfo> retrieveSubscriberHistory(int ban,
				String subscriberID, Date from, Date to) {
			this.ban=ban;
			return null;
		}

		@Override
		public Collection<SubscriberInfo> retrieveSubscriberListByBanAndFleet(
				int ban, int urbanId, int fleetId, int listLength) {
			this.ban=ban;
			this.urbanId=urbanId;
			this.fleetId=fleetId;
			this.maxCount=listLength;
			return null;
		}

		@Override
		public Collection<SubscriberInfo> retrieveSubscriberListByBanAndTalkGroup(
				int ban, int urbanId, int fleetId, int talkGroupId,
				int listLength) {
			this.ban=ban;
			this.urbanId=urbanId;
			this.fleetId=fleetId;
			this.maxCount=listLength;
			this.talkGroupId=talkGroupId;
			return null;
		}

		@Override
		public SubscriptionRoleInfo retrieveSubscriptionRole(String phoneNumber) {
			this.phoneNumber=phoneNumber;
			return null;
		}

		@Override
		public List<String> retrieveSubscriberPhonenumbers(
				char subscriberStatus, char accountType, char accountSubType,
				char banStatus, int maximum) {
			this.subscriberstatus=subscriberStatus;
			this.accountType=accountType;
			return null;
		}

		@Override
		public List<String> retrieveSubscriberPhonenumbers(
				char subscriberStatus, char accountType, char accountSubType,
				char banStatus, String addressType, int maximum) {
			this.subscriberstatus=subscriberStatus;
			this.accountType=accountType;
			this.addressType=addressType;
			return null;
		}

		@Override
		public Collection<SubscriberInfo> retrieveSubscriberListBySerialNumber(
				String serialNumber, boolean includeCancelled) {
			this.serialNumber=serialNumber;
			this.includeCancelled=includeCancelled;
			List<SubscriberInfo> list=new ArrayList<SubscriberInfo>();
			list.add(new SubscriberInfo());
			return list;
		}

		

		@Override
		public Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumbers(
				String[] phoneNumbers, boolean includeCancelled) {
			this.phoneNumbers=phoneNumbers;
			List<SubscriberInfo> list=null;
			SubscriberInfo si=new SubscriberInfo();
			si.setStatus('A');
			si.setBanId(2222);
			
			if (phoneNumbers[0]!="555") {
				list=new ArrayList<SubscriberInfo>();
				list.add(si);
			}
			
			return list;
		}

		@Override
		public Collection<SubscriberInfo> retrieveHSPASubscriberListByIMSI(
				String IMSI, boolean includeCancelled)
				throws ApplicationException {
			this.imsi=IMSI;
			this.includeCancelled=includeCancelled;
			List<SubscriberInfo> list=new ArrayList<SubscriberInfo>();
			list.add(new SubscriberInfo());
			return list;
		}

		@Override
		public long retrieveSubscriptionId(int banId, String phoneNumber,
				String status) throws ApplicationException {
			this.ban = banId;
			this.phoneNumber = phoneNumber;
			this.status = status;
			return 0;
		}

		@Override
		public String retrieveEmailAddress(int ban, String subscriberNumber)
				throws ApplicationException {
			this.ban = ban;
			this.subscriberID = subscriberNumber;
			return null;
		}

		@Override
		public SubscriberIdentifierInfo retrieveSubscriberIdentifierBySubscriptionId(
				long subscriptionId) throws ApplicationException {
			return null;
		}
		
		@Override
		public SubscriberIdentifierInfo retrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber(
				int ban, String phoneNumber) throws ApplicationException {
			return null;
		}
		
		@Override
		public SubscriberInfo retrieveSubscriberByBANAndPhoneNumber (
				final int ban, final String phoneNumber) throws ApplicationException {
			return null;
		}
		
		@Override
		public Collection<SubscriberInfo> retrieveSubscriberListByBAN(
				int ban, int maximumCount, boolean includeCancelled) {
			return null;
		}
		
		@Override
		public Collection<SubscriberInfo> retrieveSubscriberListByBANAndSubscriberId(
				int ban, String subscriberId, boolean includeCancelled, int maximumCount) {
			return null;
		}
		
		@Override
		public Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumbers(
				String[] phoneNumbers, int maximum, boolean includeCancelled) throws ApplicationException {
			return null;
		}
		
		@Override
		public Collection<SubscriberInfo> retrieveSubscriberListBySubscriberID(
				String subscriberId, boolean includeCancelled, int maximumCount) {
			return null;
		}

		

		@Override
		public SubscriberInfo retrieveSubscriberBySeatResourceNumber(
				String resourceNumber) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		

		@Override
		public Collection<SubscriberInfo> retrieveSubscriberListBySeatResourceNumber(
				int ban, String seatResourceNumber, int listLength,
				boolean includeCancelled) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Collection<SubscriberInfo> retrieveSubscriberListBySeatResourceNumber(
				String seatResourceNumber, int listLength,
				boolean includeCancelled) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SubscriberInfo retrieveSubscriberByBanAndSeatResourceNumber(
				int ban, String seatResourceNumber) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<ResourceInfo> retrieveSeatResourceInfoByBanAndPhoneNumber(
				int ban, String phoneNumber, boolean includeCancelled)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SubscriberIdentifierInfo retrieveLatestSubscriberIdentifierInfoBySeatResourceNumber(
				int ban, String seatResourceNumber) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.subscriber.lifecyclehelper.dao.SubscriberDao#updateSubscriptionRole(java.lang.String, java.lang.String)
		 */
		@Override
		public int updateSubscriptionRole(String phoneNumber,
				String subscriptionRoleCd) throws ApplicationException {
			// TODO Auto-generated method stub
			return 0;
		}


	}
	
	private static class TestUsageDao implements UsageDao{

		private int ban;
		private String phoneNumber;
		private Date day;
		private String serviceType;
		private boolean isPostpaid;
		private String[] serviceTypes;
		private Date fromDate;
		private Date toDate;
		private String subscriberId;
		private String featureCode;
		private String method;

		

		@Override
		public VoiceUsageSummaryInfo retrieveVoiceUsageSummary(int banId,
				String subscriberId, String featureCode) {
			this.ban=banId;
			this.subscriberId=subscriberId;
			this.featureCode=featureCode;
			return null;
		}
		
	}
		
	
	@Test
	public void testRetrieveSubscriberAddress()throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveSubscriberAddress",
				int.class,String.class), new Object[]{null,""}, 0);
		
		int ban=1212;
		String subscriber="13232323";
		impl.retrieveSubscriberAddress(ban, subscriber);
		assertEquals(ban,testAddressDao.ban);
		assertEquals(subscriber,testAddressDao.subscriber);
	}
	@Test
	public void testRetrieveDepositHistory()throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveDepositHistory",
				int.class,String.class), new Object[]{null,""}, 0);
		
		int ban=1212;
		String subscriber="13232323";
		impl.retrieveDepositHistory(ban, subscriber);
		assertEquals(ban,testDepositDao.ban);
		assertEquals(subscriber,testDepositDao.subscriber);
	}
	@Test
	public void testRetrievePaidSecurityDeposit()throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrievePaidSecurityDeposit",
				int.class,String.class,String.class), new Object[]{null,"",""}, 0);
		
		int ban=1212;
		String subscriber="13232323";
		impl.retrievePaidSecurityDeposit(ban, subscriber,"");
		assertEquals(ban,testDepositDao.ban);
		assertEquals(subscriber,testDepositDao.subscriber);
	
	}
	
	@Test
	public void testGetCountForRepairID(){
		impl.setSubscriberEquipmentDao(testSubscriberEquipmentDao);
		impl.getCountForRepairID("testrepair");
		assertEquals("testrepair",testSubscriberEquipmentDao.repairID);
		assertEquals("getCountForRepairID",testSubscriberEquipmentDao.method);
	}
	@Test
	public void testRetrieveEquipmentChangeHistory() throws IllegalArgumentException, SecurityException, IllegalAccessException, 
	InvocationTargetException, NoSuchMethodException, ApplicationException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveEquipmentChangeHistory",
				int.class,String.class,Date.class,Date.class), new Object[]{null,"",new Date(),new Date()}, 0);
		
		int ban=10;
		String subscriberID="testSub";
		impl.retrieveEquipmentChangeHistory(ban, subscriberID, new Date(), new Date());
		assertEquals(ban,testSubscriberEquipmentDao.ban);
		assertEquals(subscriberID,testSubscriberEquipmentDao.subscriberID);
		assertEquals("retrieveEquipmentChangeHistory",testSubscriberEquipmentDao.method);
	}
	@Test
	public void testRetrieveHandsetChangeHistory() throws IllegalArgumentException, SecurityException, IllegalAccessException, 
	InvocationTargetException, NoSuchMethodException, ApplicationException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveHandsetChangeHistory",
				int.class,String.class,Date.class,Date.class), new Object[]{null,"",new Date(),new Date()}, 0);
		
		int ban=10;
		String subscriberID="testSub";
		impl.retrieveHandsetChangeHistory(ban, subscriberID, new Date(), new Date());
		assertEquals(ban,testSubscriberEquipmentDao.ban);
		assertEquals(subscriberID,testSubscriberEquipmentDao.subscriberID);
		assertEquals("retrieveHandsetChangeHistory",testSubscriberEquipmentDao.method);
	}	
	@Test
	public void testRetrieveTalkGroupsBySubscriber()throws ApplicationException{
		String subscriberID="testSub";
		impl.retrieveTalkGroupsBySubscriber(subscriberID);
		assertEquals(subscriberID,testFleetDao.subscriberId);
	}
	@Test
	public void testRetrieveBanForPartiallyReservedSub()throws ApplicationException{
		impl.retrieveBanForPartiallyReservedSub("SampleNum");
		assertEquals("SampleNum",testAccountDao.phoneNumber);
	}
	@Test
	public void retrieveBanIdByPhoneNumber()throws ApplicationException{
		assertEquals(1234567890,impl.retrieveBanIdByPhoneNumber("1234567890"));
	}
	
	@Test
	public void testRetrieveInvoiceTaxInfo()throws ApplicationException, IllegalArgumentException, SecurityException, 
	IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveInvoiceTaxInfo",
				int.class,String.class,int.class), new Object[]{null,"",1}, 0);
		
		int ban=1212;
		String subscriberId="testId";
		int billSeqNo=2312;
		impl.retrieveInvoiceTaxInfo(ban, subscriberId, billSeqNo);
		assertEquals(ban,testInvoiceDao.ban);
		assertEquals(billSeqNo,testInvoiceDao.billSeqNo);
		assertEquals(subscriberId, testInvoiceDao.subscriberId);
	}

	@Test
	public void testRetrievePrepaidEventHistory()throws ApplicationException{
		String phoneNumber="1212";
		Date from= new Date();
		Date to= new Date();
		PrepaidEventTypeInfo[] preInfAr=new PrepaidEventTypeInfo[0];
		impl.setPrepaidDao(testPrepaidDao);
		impl.retrievePrepaidEventHistory(phoneNumber, from, to, preInfAr);
		assertEquals(phoneNumber,this.testPrepaidDao.phoneNumber);
		assertEquals(from,this.testPrepaidDao.from);
		assertEquals("retrievePrepaidEventHistory2",this.testPrepaidDao.method);
		impl.retrievePrepaidEventHistory(phoneNumber, from, to);
		assertEquals(phoneNumber,this.testPrepaidDao.phoneNumber);
		assertEquals(from,this.testPrepaidDao.from);
		assertEquals("retrievePrepaidEventHistory1",this.testPrepaidDao.method);
		impl.retrievePrepaidCallHistory(phoneNumber, from, to);
		assertEquals(phoneNumber,this.testPrepaidDao.phoneNumber);
		assertEquals(from,this.testPrepaidDao.from);
		assertEquals("retrievePrepaidCallHistory1",this.testPrepaidDao.method);
	}
	@Test
	public void testRetrieveProvisioningTransactions()throws ApplicationException, IllegalArgumentException, SecurityException, 
	IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveProvisioningTransactions",
				int.class,String.class,Date.class,Date.class), new Object[]{1,"", new Date(),new Date()}, 0);
		
		String phoneNumber="322323232";
		int ban=12121;
		Date from= new Date();

		impl.setProvisioningDao(testProvisioningDao);
		impl.retrieveProvisioningTransactions(ban, phoneNumber, from, new Date());
		assertEquals(phoneNumber,testProvisioningDao.phoneNumber);
		assertEquals(ban,testProvisioningDao.ban);
	}
	@Test
	public void testRetrieveSubscriberProvisioningStatus()throws ApplicationException, IllegalArgumentException, SecurityException, 
	IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveSubscriberProvisioningStatus",
				int.class,String.class), new Object[]{null,""}, 0);
		
		String phoneNumber="322323232";
		int ban=12121;

		impl.setProvisioningDao(testProvisioningDao);
		impl.retrieveSubscriberProvisioningStatus(ban, phoneNumber);
		assertEquals(phoneNumber,testProvisioningDao.phoneNumber);
		assertEquals(ban,testProvisioningDao.ban);
	}
	@Test
	public void testRetrieveFeaturesForPrepaidSubscriber()throws ApplicationException{
		String phoneNumber="32322323";
		impl.setPrepaidDao(testPrepaidDao);
		impl.retrieveFeaturesForPrepaidSubscriber(phoneNumber);
		assertEquals(phoneNumber,this.testPrepaidDao.phoneNumber);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveCallingCirclePhoneNumberListHistory() throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveCallingCirclePhoneNumberListHistory",
				int.class,String.class,String.class,Date.class,Date.class), new Object[]{null," "," ",new Date(),new Date()}, 0);
		
		int banId=70103921;
		String subscriberNo="9057160845";
		String productType="C";
		Date from=new Date((2006-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		impl.retrieveCallingCirclePhoneNumberListHistory(banId, subscriberNo, productType, from, to);
		assertEquals(subscriberNo,testServiceAgreementDao.subscriberNo);
		assertEquals(productType,testServiceAgreementDao.productType);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveContractChangeHistory() throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveContractChangeHistory",
				int.class,String.class,Date.class,Date.class), new Object[]{null," ",new Date(),new Date()}, 0);
		
		int ban=70103921;
		String subscriberID="9057160845";
		Date from=new Date((2006-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		impl.retrieveContractChangeHistory(ban, subscriberID, from, to);
		assertEquals(subscriberID,testServiceAgreementDao.subscriberID);
		assertEquals(from,testServiceAgreementDao.fromDate);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveFeatureParameterHistory() throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveFeatureParameterHistory",
				int.class,String.class,String.class,String[].class,Date.class,Date.class), 
				new Object[]{null," "," ",new String[]{"","",""},new Date(),new Date()}, 0);
		
		int banId=20007348;
		String subscriberNo="7807183952";
		String productType="C";
		Date from=new Date((2000-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		String[] parameterNames={"DATE-OF-BIRTH","CALLHOMEFREE","CALLING-CIRCLE"};
		impl.retrieveFeatureParameterHistory(banId, subscriberNo, productType, parameterNames, from, to);
		assertEquals(subscriberNo,testServiceAgreementDao.subscriberNo);
		assertEquals(productType,testServiceAgreementDao.productType);
	}

	@Test
	public void testRetrieveMultiRingPhoneNumbers() throws ApplicationException{

		String subscriberId="4039990012";
		impl.retrieveMultiRingPhoneNumbers(subscriberId);
		assertEquals(subscriberId,testServiceAgreementDao.subscriberID);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePricePlanChangeHistory() throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrievePricePlanChangeHistory",
				int.class,String.class,Date.class,Date.class), new Object[]{null," ",new Date(),new Date()}, 0);

		int ban=20001552;
		String subscriberID="4162060035";
		Date from=new Date((2000-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		impl.retrievePricePlanChangeHistory(ban, subscriberID, from, to);
		assertEquals(subscriberID,testServiceAgreementDao.subscriberID);
		assertEquals(ban,testServiceAgreementDao.ban);
	}


	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveServiceChangeHistory() throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveServiceChangeHistory",
				int.class,String.class,Date.class,Date.class,boolean.class), new Object[]{null," ",new Date(),new Date(),false}, 0);
		
		int ban=70103921;
		String subscriberID="9057160845";
		Date from=new Date((2000-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		boolean includeAllServices = true;
		impl.retrieveServiceChangeHistory(ban, subscriberID, from, to, includeAllServices);
		assertEquals(subscriberID,testServiceAgreementDao.subscriberID);
		assertTrue(testServiceAgreementDao.includeAllServices);
		impl.retrieveServiceChangeHistory(ban, subscriberID, from, to);
		assertFalse(testServiceAgreementDao.includeAllServices);
	}


	@Test
	public void testRetrieveVoiceMailFeatureByPhoneNumber() throws ApplicationException{

		String phoneNumber = "4162060035";
		String productType = "C";
		impl.retrieveVoiceMailFeatureByPhoneNumber(phoneNumber, productType);
		assertEquals(phoneNumber,testServiceAgreementDao.phoneNumber);
		assertEquals(productType,testServiceAgreementDao.productType);
	}

	@Test
	public void testRetrieveServiceAgreementByPhoneNumber() throws ApplicationException{

		String phoneNumber = "4168940511";
		SubscriberContractInfo sci = impl.retrieveServiceAgreementByPhoneNumber(phoneNumber);
		assertEquals(12,sci.getCommitmentMonths());
		assertEquals("ABC",sci.getCommitmentReasonCode());
		assertEquals("123456",sci.getPricePlanDealerCode());
	}

	@Test
	public void testRetrieveVendorServiceChangeHistory() throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveVendorServiceChangeHistory",
				int.class,String.class,String[].class), new Object[]{null," ",new String[]{"",""}}, 0);
		
		int ban=20580920;
		String subscriberId="4168940511";
		String[] categoryCodes={"N51","CL","VM"};
		impl.retrieveVendorServiceChangeHistory(ban, subscriberId, categoryCodes);
		assertEquals(subscriberId,testServiceAgreementDao.subscriberID);
		assertEquals(categoryCodes[0],testServiceAgreementDao.categoryCodes[0]);
	}





	@Test
	public void testRetrievePartiallyReservedSubscriberListByBan() throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrievePartiallyReservedSubscriberListByBan",
				int.class,int.class), new Object[]{null,1}, 0);
		
		int ban=70104723;
		int maximum=100;
		impl.retrievePartiallyReservedSubscriberListByBan(ban, maximum);
		assertEquals(ban,testSubscriberDao.ban);
	}

	@Test
	public void testRetrievePortedSubscriberListByBAN() throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrievePortedSubscriberListByBAN",
				int.class,int.class), new Object[]{null,1}, 0);
		
		int ban=20001552;
		int listLength=100;
		impl.retrievePortedSubscriberListByBAN(ban, listLength);
		assertEquals(ban,testSubscriberDao.ban);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveResourceChangeHistory() throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveResourceChangeHistory",
				int.class,String.class,String.class,Date.class,Date.class), new Object[]{null,"","",null,null}, 0);
		
		int ban=20007215;
		String subscriberID="M000000484";
		String type="H";
		Date from=new Date((2000-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		impl.retrieveResourceChangeHistory(ban, subscriberID, type, from, to);
		assertEquals(subscriberID,testSubscriberDao.subscriberID);
		assertEquals(ban,testSubscriberDao.ban);
		assertEquals(type,testSubscriberDao.type);
	}

	@Test
	public void testRetrieveLightWeightSubscriberListByBAN() throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveLightWeightSubscriberListByBAN",
				int.class,boolean.class,int.class,boolean.class), new Object[]{null,false,1,false}, 0);
		
		int banId=70103921;
		int listLength=100;
		boolean isIDEN=false;
		boolean includeCancelled=false;
		impl.retrieveLightWeightSubscriberListByBAN(banId, isIDEN, listLength, includeCancelled);
		assertEquals(isIDEN,testSubscriberDao.isIDEN);
		assertEquals(banId,testSubscriberDao.ban);
		assertEquals(includeCancelled,testSubscriberDao.includeCancelled);
	}

	@Test
	public void testRetrieveLastAssociatedSubscriptionId() throws ApplicationException{

		String imsi="302220999950762";
		impl.retrieveLastAssociatedSubscriptionId(imsi);
		assertEquals(imsi,testSubscriberDao.imsi);

	}

	@Test
	public void testRetrieveHotlineIndicator() throws ApplicationException{

		String subscriberId = "7807183927";
		assertTrue(impl.retrieveHotlineIndicator(subscriberId));
	}

	@Test
	public void testGetPortProtectionIndicator() throws ApplicationException, 
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveLightWeightSubscriberListByBAN",
				int.class,boolean.class,int.class,boolean.class), new Object[]{null,false,1,false}, 0);
		
		int ban = 55673955;
		String subscriberId = "4164160074";
		String phoneNumber = "4164160074";
		String status = "A";
		impl.getPortProtectionIndicator(ban, subscriberId, phoneNumber, status);
		assertEquals(subscriberId,testSubscriberDao.subscriberID);
		assertEquals(ban,testSubscriberDao.ban);
		assertEquals(status,testSubscriberDao.status);

	}

	@Test
	public void testRetrieveSubscriptionPreference() throws ApplicationException{

		long subscriptionId=12255794;
		int preferenceTopicId=1;
		impl.retrieveSubscriptionPreference(subscriptionId, preferenceTopicId);
		assertEquals(subscriptionId,testSubscriberDao.subscriptionId);
		assertEquals(preferenceTopicId,testSubscriberDao.preferenceTopicId);
	}

	@Test
	public void testRetrieveAvailableCellularPhoneNumbersByRanges() throws ApplicationException, TelusAPIException{

		LineRangeInfo[] lineRanges=new LineRangeInfo[1];
		lineRanges[0]=new LineRangeInfo();
		lineRanges[0].setStart(0000);
		lineRanges[0].setEnd(9999);

		NumberRangeInfo[] numberRanges=new NumberRangeInfo[2];
		numberRanges[0]=new NumberRangeInfo();
		numberRanges[0].setNXX(260);
		numberRanges[0].setNPA(705);
		numberRanges[0].setLineRanges(lineRanges);
		numberRanges[1]=new NumberRangeInfo();
		numberRanges[1].setNXX(292);
		numberRanges[1].setNPA(519);
		numberRanges[1].setLineRanges(lineRanges);

		NumberGroupInfo numberGroupInfo=new NumberGroupInfo();
		numberGroupInfo.setCode("SSM");
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

		impl.retrieveAvailableCellularPhoneNumbersByRanges(phoneNumberReservation, startFromPhoneNumber, searchPattern, asian, maxNumber);
		assertEquals(lineRanges[0].getEnd(),testSubscriberDao.lineRanges[0].getEnd());
		assertEquals(numberRanges[1].getNPA(),testSubscriberDao.numberRanges[1].getNPA());
		assertEquals(asian,testSubscriberDao.asian);
	}

	@Test
	public void testRetrieveSubscriberIDByPhoneNumber() throws ApplicationException {
		
		int ban=0;
		String phoneNumber="416-416-0074";
		String s=impl.retrieveSubscriberIDByPhoneNumber(ban, phoneNumber);
		assertEquals(ban,testSubscriberDao.ban);
		assertEquals(phoneNumber,testSubscriberDao.phoneNumber);
		assertEquals("123456",s);
	}
	
	@Test
	public void testRetrieveHSPASubscriberListByIMSI() throws ApplicationException{
		String IMSI="123456";
		boolean includeCancelled=false;
		impl.retrieveHSPASubscriberListByIMSI(IMSI, includeCancelled);
		assertEquals(IMSI, testSubscriberDao.imsi);
		assertEquals(includeCancelled, testSubscriberDao.includeCancelled);
	}
	
	@Test
	public void testRetrieveEquipmentSubscribers() throws ApplicationException{
		
		String serialNumber = "IMSIserialnumber";
		boolean active=false;
		impl.retrieveEquipmentSubscribers(serialNumber, active);
		assertEquals(serialNumber, testSubscriberEquipmentDao.serialNumber);
		assertEquals("ABC", testSubscriberDao.imsi);
		assertTrue(testSubscriberDao.includeCancelled);
		
		serialNumber = "noIMSIserialnumber";
		active=false;
		impl.retrieveEquipmentSubscribers(serialNumber, active);
		assertEquals(serialNumber, testSubscriberEquipmentDao.serialNumber);
		assertFalse(testSubscriberEquipmentDao.active);
	}
	@Test
	public void testIsPortRestricted() throws ApplicationException{
		int ban=55673955;
		String subscriberId = null;
		String phoneNumber = null;
		String status = null;
		
		impl.isPortRestricted(ban, subscriberId, phoneNumber, status);
		assertEquals(ban, testSubscriberDao.ban);
		assertEquals(phoneNumber, testSubscriberDao.phoneNumber);
		assertEquals(status, testSubscriberDao.status);
	}
	
	@Test
	public void testRetrieveSubscriberByPhoneNumber()throws ApplicationException{
		String phoneNumber="4033404108";
		impl.retrieveSubscriberByPhoneNumber(phoneNumber);
		assertEquals("4033404108",testSubscriberDao.phoneNumber);
	}
	
	@Test
	public void testRetrieveSubscriptionRole()throws ApplicationException{
		String phoneNumber="4033404108";
		impl.retrieveSubscriptionRole(phoneNumber);
		assertEquals(phoneNumber,testSubscriberDao.phoneNumber);
	}
	
	@Test
	public void testRetrieveSubscriberListBySerialNumber()throws ApplicationException{
		impl.retrieveSubscriberListBySerialNumber("100000000000000000", true);
		assertEquals("100000000000000000",testSubscriberDao.serialNumber);
		impl.retrieveSubscriberListBySerialNumber("111", true);
		assertEquals("IMSI",testSubscriberDao.imsi);
		
	}
	
	@Test
	public void testRetrieveSubscriberListBySerialNumber1()throws ApplicationException{
		impl.retrieveSubscriberListBySerialNumber("100000000000000000");
		assertEquals("100000000000000000",testSubscriberDao.serialNumber);
		impl.retrieveSubscriberListBySerialNumber("111");
		assertEquals("IMSI",testSubscriberDao.imsi);
	}
	
	@Test
	public void testCheckSubscriberPortOutEligibility()throws ApplicationException{
		String phoneNumber = "4165545503";
		String ndpInd = null;
		
		try{
			impl.checkSubscriberPortOutEligibility(phoneNumber, ndpInd);
			fail("Exception Expected");
		}catch(Exception e){}
		
		ndpInd = "A";
		
		try{
			impl.checkSubscriberPortOutEligibility(phoneNumber, ndpInd);
			fail("Exception Expected");
		}catch(Exception e){}
		
		phoneNumber="222";
		impl.checkSubscriberPortOutEligibility(phoneNumber, ndpInd);
		assertEquals("N",testSubscriberDao.portIndicator);
		
		phoneNumber="333";
		impl.checkSubscriberPortOutEligibility(phoneNumber, ndpInd);
		assertEquals("Y",testSubscriberDao.portIndicator);
	}
	
	@Test
	public void testRetrieveSubscriberByPhoneNumber1() throws SystemException, ApplicationException{
		int ban=70103921;
		String phoneNumber="9057160845";
		
		try{
			impl.retrieveSubscriberByPhoneNumber(ban, phoneNumber);
		fail("Exception Expected");
		}catch(Exception e){
			assertTrue(testSubscriberDao.notEmpty);
		}
		
		ban=444;
		try{
			impl.retrieveSubscriberByPhoneNumber(ban, phoneNumber);
		fail("Exception Expected");
		}catch(Exception e){
			assertFalse(testSubscriberDao.notEmpty);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveSubscriberHistory()throws ApplicationException{
		impl.retrieveSubscriberHistory(8, "4033404108", new Date(2005-1900,02,31), new Date(2005-1900,02,31));
		assertEquals(8,testSubscriberDao.ban);
	}	
	
	@Test
	public void testRetrieveSubscriberListByBAN() throws SystemException, ApplicationException{
		int ban=70103921;
		int listLength=100;
		impl.retrieveSubscriberListByBAN(ban, listLength);
			assertEquals(ban,testSubscriberDao.ban);
			assertEquals(listLength,testSubscriberDao.maxCount);
	
	}
	
	@Test
	public void testRetrieveSubscriberListByBAN1() throws SystemException, ApplicationException{
		int ban=70103921;
		int listLength=100;
		boolean includeCancelled=false;
		impl.retrieveSubscriberListByBAN(ban, listLength, includeCancelled);
			assertEquals(ban,testSubscriberDao.ban);
			assertEquals(listLength,testSubscriberDao.maxCount);
			assertEquals(includeCancelled,testSubscriberDao.includeCancelled);
	}
	
	@Test
	public void testRetrieveSubscriberListByBanAndFleet()throws ApplicationException{
		impl.retrieveSubscriberListByBanAndFleet(84, 905, 131077, 8);
		assertEquals(84,testSubscriberDao.ban);
		assertEquals(905,testSubscriberDao.urbanId);
		assertEquals(131077,testSubscriberDao.fleetId);
		assertEquals(8,testSubscriberDao.maxCount);
	}	
	
	@Test
	public void testRetrieveSubscriberListByBanAndTalkGroup()throws ApplicationException{
		impl.retrieveSubscriberListByBanAndTalkGroup(84, 905, 131077, 1, 1);
		assertEquals(84,testSubscriberDao.ban);
		assertEquals(905,testSubscriberDao.urbanId);
		assertEquals(131077,testSubscriberDao.fleetId);
		assertEquals(1,testSubscriberDao.talkGroupId);
		assertEquals(1,testSubscriberDao.maxCount);
	}
	
	@Test
	public void testRetrieveSubscriberListByImsi()throws ApplicationException{
		String imsi = "302220999918616";
		boolean includeCancelled = false;
		impl.retrieveSubscriberListByImsi(imsi, includeCancelled);
		assertEquals(imsi, testSubscriberDao.imsi);
		assertEquals(includeCancelled, testSubscriberDao.includeCancelled);
	}
	
	@Test
	public void testRetrieveSubscriberListByPhoneNumbers() throws ApplicationException{
		String[] ary={"4033501530","4033404108"};	
		impl.retrieveSubscriberListByPhoneNumbers(ary,true);
		assertEquals(ary.length,testSubscriberDao.phoneNumbers.length);
		
		ary=new String[]{"555","4033404108"};	
		try{
			impl.retrieveSubscriberListByPhoneNumbers(ary,true);
			fail("Exception Expected");
		}catch(Exception e){
			assertEquals(ary.length,testSubscriberDao.phoneNumbers.length);
		}
				
	}
	@Test
	public void testRetrieveSubscriberListByBanAndPhoneNumber() throws ApplicationException{
		
		impl.retrieveSubscriberListByPhoneNumber("345345",100,true);
		assertEquals(0,testSubscriberDao.ban);
		
		try{
			impl.retrieveSubscriberListByPhoneNumber("666",100,true);
			fail("Exception Expected");
		}catch(Exception e){
			assertEquals(0,testSubscriberDao.ban);
		}
	}
	
	@Test
	public void testRetrieveSubscriberPhoneNumbers()throws ApplicationException{
		char subscriberStatus='A';
		char accountType='B';
		char accountSubType='C';
		char banStatus='D';
		int maximum =10;
		impl.retrieveSubscriberPhonenumbers(subscriberStatus, accountType, accountSubType, banStatus, maximum);
		assertEquals(subscriberStatus,testSubscriberDao.subscriberstatus);
		assertEquals(accountType,testSubscriberDao.accountType);
		
		String addressType="XYZ";
		impl.retrieveSubscriberPhonenumbers(subscriberStatus, accountType, accountSubType, banStatus, addressType, maximum);
		assertEquals(subscriberStatus,testSubscriberDao.subscriberstatus);
		assertEquals(accountType,testSubscriberDao.accountType);
		assertEquals(addressType,testSubscriberDao.addressType);
	}
	
	
	
	@Test
	public void testRetrieveVoiceUsageSummary() throws ApplicationException{
		String subscriberId="7807183952";
		String featureCode="STD";
		int banId=20007348;
		impl.retrieveVoiceUsageSummary(banId, subscriberId, featureCode);
		assertEquals(banId,testUsageDao.ban);
		assertEquals(featureCode,testUsageDao.featureCode);
	}
	
	@Test
	public void testRetrieveSubscriptionId() throws ApplicationException {
		int ban = 10;
		String phoneNumber = "321321";
		String status = "321321";
		impl.getSubscriptionId(ban, phoneNumber, status);
		
		assertEquals(ban, testSubscriberDao.ban);
		assertEquals(phoneNumber, testSubscriberDao.phoneNumber);
		assertEquals(status, testSubscriberDao.status);
	}
	
	@Test
	public void testretrieveEquipmentSubscribers() throws ApplicationException {
		String DUMMY_ESN_FOR_USIM = "100000000000000000";
		try {
			impl.retrieveEquipmentSubscribers(DUMMY_ESN_FOR_USIM, true);
			fail("Exception Expected");
		} catch (Exception e) {
			assertEquals("Cannot use dummy USIM number for retrieveEquipmentSubscribers",e.getMessage().trim());
		}
	}
}
