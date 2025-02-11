package com.telus.cmb.subscriber.lifecyclemanager.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyChar;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.rmi.RemoteException;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import com.telus.api.ApplicationException;
import com.telus.api.account.ServicesValidation;
import com.telus.api.reference.ServiceSummary;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.dao.amdocs.AmdocsSessionManager;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.dao.AddressDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.DepositDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.FleetDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewIdenPcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewPcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.ServiceAgreementDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.SubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.SubscriberDaoFactory;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateIdenPcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateIdenSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdatePagerSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdatePcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UsageDao;
import com.telus.eas.account.info.ActivationFeaturesPurchaseArrangementInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.AdditionalMsiSdnFtrInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.subscriber.info.UsageProfileListsSummaryInfo;
import com.telus.eas.utility.info.DiscountPlanInfo;
import com.telus.eas.utility.info.NumberGroupInfo;




public class SubscriberLifeCycleManagerImplTest {

	@Autowired
	@Mock
	SubscriberLifecycleManagerImpl mockedImpl;
	
//	@Mock
//	PrepaidDao mockedPrepaidDao;
	
	@Mock
	SubscriberDao mockedSubscriberDao;
	
	@Mock
	NewSubscriberDao mockedNewSubscriberDao;
	
	@Mock
	AddressDao mockedAddressDao;
	
	@Mock
	DepositDao mockedDepositDao;
	
	@Mock
	UsageDao mockedUsageDao;
	
	@Mock
	ServiceAgreementDao mockedServiceAgreementDao;
	
	@Mock
	SubscriberDaoFactory mockedSubscriberDaoFactory;
	
	@Mock
	UpdateSubscriberDao mockedUpdateSubscriberDao;
	
	@Mock
	ProductEquipmentHelper mockedProductEquipmentHelper;
	
	@Mock
	AccountLifecycleManager mockedAccountLifecycleManager;
	
	@Mock
	ReferenceDataHelper mockedReferenceDataHelper;
	
	@Mock
	SubscriberLifecycleHelper mockedSubscriberLifecycleHelper;
	
	@Mock
	UpdateIdenSubscriberDao mockedUpdateIdenSubscriberDao;
	
	@Mock
	UpdatePcsSubscriberDao mockedUpdatePcsSubscriberDao;
	
	@Mock
	FleetDao mockedFleetDao;

	@Mock
	UpdateIdenPcsSubscriberDao mockedUpdateIdenPcsSubscriberDao;
	
	@Mock
	NewIdenPcsSubscriberDao mockedNewIdenPcsSubscriberDao;
	
	@Mock
	NewPcsSubscriberDao mockedNewPcsSubscriberDao;
	
	@Mock
	UpdatePagerSubscriberDao mockedUpdatePagerSubscriberDao;
		
	@Mock
	AmdocsSessionManager mockedAmdocsSessionManager;
	
	@Mock
	MigrationRequestInfo migrationRequestInfo;
	
	@Mock
	PhoneNumberReservationInfo phoneNumberReservation;
	

	@Before
	public void setup() throws ApplicationException {
		MockitoAnnotations.initMocks(this);
		
		mockedImpl = new SubscriberLifecycleManagerImpl();

		// DAOs
		//mockedImpl.setPrepaidDao(mockedPrepaidDao);			
		mockedImpl.setAddressDao(mockedAddressDao);		
		mockedImpl.setSubscriberDao(mockedSubscriberDao);
		mockedImpl.setServiceAgreementDao(mockedServiceAgreementDao);
		mockedImpl.setDepositDao(mockedDepositDao);
		mockedImpl.setFleetDao(mockedFleetDao);
		mockedImpl.setFleetDao(mockedFleetDao);

		when(mockedSubscriberDaoFactory.getNewSubscriberDao(anyString())).thenReturn(mockedNewSubscriberDao);
		when(mockedSubscriberDaoFactory.getNewIdenPcsSubscriberDao(anyString())).thenReturn(mockedNewIdenPcsSubscriberDao);
		when(mockedSubscriberDaoFactory.getNewPcsSubscriberDao()).thenReturn(mockedNewPcsSubscriberDao);
		when(mockedSubscriberDaoFactory.getUpdateSubscriberDao(anyString())).thenReturn(mockedUpdateSubscriberDao);
		when(mockedSubscriberDaoFactory.getUpdateIdenSubscriberDao()).thenReturn(mockedUpdateIdenSubscriberDao);
		when(mockedSubscriberDaoFactory.getUpdatePcsSubscriberDao()).thenReturn(mockedUpdatePcsSubscriberDao);
		when(mockedSubscriberDaoFactory.getUpdateIdenSubscriberDao()).thenReturn(mockedUpdateIdenSubscriberDao);
		when(mockedSubscriberDaoFactory.getUpdatePcsSubscriberDao()).thenReturn(mockedUpdatePcsSubscriberDao);
		when(mockedSubscriberDaoFactory.getUpdateIdenPcsSubscriberDao(anyString())).thenReturn(mockedUpdateIdenPcsSubscriberDao);
		when(mockedSubscriberDaoFactory.getUpdatePagerSubscriberDao()).thenReturn(mockedUpdatePagerSubscriberDao);
		
		
		mockedImpl.setSubscriberDaoFactory(mockedSubscriberDaoFactory);
		mockedImpl.setAmdocsSessionManager(mockedAmdocsSessionManager);
	}	

//	@Test
//	public void testActivateFeatureCard()throws ApplicationException{		
//		String phoneNumber="3232223";
//		String userId="testUser";
//		ServiceAgreementInfo serAgInf = new ServiceAgreementInfo();
//		serAgInf.setServiceTermUnits("DAY");
//		serAgInf.setFeatureCard(true);
//		serAgInf.setServiceCode("1212");
//		mockedImpl.activateFeatureForPrepaidSubscriber(userId, phoneNumber, serAgInf);
//		
//		verify(mockedPrepaidDao).activateFeatureCard(phoneNumber, 1212, serAgInf.getServiceTerm(), userId);
//		
//		verify(mockedPrepaidDao).updateFeatureAutoRenewal(phoneNumber, 1212, false, ServiceSummary.AUTORENEW_NOT_DEFINED, userId);
//	}
//	@Test
//	public void testActivateFeatureForPrepaidSubscriber()throws ApplicationException{
//
//		String phoneNumber="3232223";
//		String userId="testUser";
//
//		ServiceAgreementInfo serAgInf = new ServiceAgreementInfo();
//		serAgInf.setServiceTermUnits("MTH");
//		serAgInf.setFeatureCard(false);
//		serAgInf.setServiceCode("1212");
//		serAgInf.setServiceTerm(3);
//		serAgInf.setEffectiveDate(new Date());		
//
//		mockedImpl.activateFeatureForPrepaidSubscriber(userId, phoneNumber, serAgInf);
//		
//		verify(mockedPrepaidDao).activateFeature(phoneNumber, 1212, serAgInf.getRecurringCharge(), 90, userId);
//		
//		verify(mockedPrepaidDao).updateFeatureAutoRenewal(phoneNumber, 1212, false, ServiceSummary.AUTORENEW_NOT_DEFINED, userId);		
//	}
//	
//	@Test
//	public void testDeactivateFeatureForPrepaidSubscriber()throws ApplicationException{
//		String phoneNumber="3232223";
//		String userId="testUser";
//		double charge=0;
//		ServiceAgreementInfo serAgInf = new ServiceAgreementInfo();
//		serAgInf.setServiceCode("2112");
//		mockedImpl.deactivateFeatureForPrepaidSubscriber(userId, phoneNumber, serAgInf);
//		
//		verify(mockedPrepaidDao).deactivateFeature(phoneNumber, 2112, charge, userId);
//	}
//
//	@Test
//	public void testChangeFeaturesForPrepaidSubscriber()throws ApplicationException{
//
//		String phoneNumber="3232223";
//		String userId="testUser";
//		double charge=0;
//		byte add= (byte)'I';
//		byte delete = (byte) 'R';
//		byte update = (byte) 'U';
//		ServiceAgreementInfo[] serAgInfArray= new ServiceAgreementInfo[3];
//		ServiceAgreementInfo serAgInf1 = new ServiceAgreementInfo();
//		serAgInf1.setServiceTermUnits("MTH");
//		serAgInf1.setFeatureCard(true);
//		serAgInf1.setWPS(true);
//		serAgInf1.setTransaction(add);
//		serAgInf1.setServiceCode("2112");
//		serAgInf1.setServiceTerm(3);
//
//		ServiceAgreementInfo serAgInf2 = new ServiceAgreementInfo();
//		serAgInf2.setWPS(true);
//		serAgInf2.setTransaction(update);
//		serAgInf2.setServiceCode("2112");
//		serAgInf2.setServiceTerm(3);
//
//		ServiceAgreementInfo serAgInf3 = new ServiceAgreementInfo();
//		serAgInf3.setWPS(true);
//		serAgInf3.setTransaction(delete);
//		serAgInf3.setServiceCode("2112");
//		serAgInf3.setServiceTerm(3);
//		
//		serAgInfArray[0]=serAgInf1;
//		serAgInfArray[1]=serAgInf2;
//		serAgInfArray[2]=serAgInf3;
//
//		
//		when(mockedSubscriberLifecycleHelper.retrieveFeaturesForPrepaidSubscriber(anyString())).thenReturn(new ServiceAgreementInfo[0]);
//		
//		SubscriberLifecycleManagerImpl watchedImpl = spy(mockedImpl);
//		
//		watchedImpl.changeFeaturesForPrepaidSubscriber(userId, phoneNumber, serAgInfArray);
//		
//		verify(watchedImpl).activateFeatureForPrepaidSubscriber(userId, phoneNumber, serAgInfArray[0]);	
//		
//		verify(watchedImpl).updateFeatureForPrepaidSubscriber(userId, phoneNumber, serAgInfArray[1]);
//		
//		verify(watchedImpl).deactivateFeatureForPrepaidSubscriber(userId, phoneNumber, serAgInfArray[2]);
//	}
//
//	@Test
//	public void testUpdatePrepaidSubscriberRateId()throws ApplicationException{
//		String phoneNumber="3232223";
//		String userId="testUser";
//		long rateId=112121211;
//		mockedImpl.updatePrepaidSubscriberRateId(phoneNumber, userId, rateId);
//		
//		
//		verify(mockedPrepaidDao).updateSubscriberRateId(phoneNumber, userId, rateId);
//	}
//
//	@Test
//	public void testUpdateSubscriberExpiryDate()throws ApplicationException{
//
//		String phoneNumber="3232223";
//		String userId="testUser";
//		Date expiryDate=new Date();
//		mockedImpl.updatePrepaidSubscriberExpiryDate(phoneNumber, userId, expiryDate);
//		
//		verify(mockedPrepaidDao).updateSubscriberExpiryDate(phoneNumber, userId, expiryDate);
//	}
//	@Test
//	public void testSaveActivationFeaturesPurchaseArrangement()throws ApplicationException{	
//		String userId="testUser";
//		SubscriberInfo subInf= new SubscriberInfo();
//		ActivationFeaturesPurchaseArrangementInfo[] actFeaPInf = new ActivationFeaturesPurchaseArrangementInfo[0];
//		mockedImpl.saveActivationFeaturesPurchaseArrangement(subInf, actFeaPInf, userId);
//		
//		verify(mockedPrepaidDao).saveActivationFeaturesPurchaseArrangement(any(Subscriber.class), eq(actFeaPInf), eq(userId));
//	}
//
//	@Test
//	public void testUpdatePrepaidCallingCircleParameters()throws ApplicationException{
//		String applicationId="testID";
//		byte action=(byte)'I';
//		String phoneNumber="3232223";
//		String userId="testUser";
//		ServiceAgreementInfo serAgInf= new ServiceAgreementInfo();
//		mockedImpl.updatePrepaidCallingCircleParameters(applicationId, userId, phoneNumber, serAgInf, action);
//		
//		verify(mockedPrepaidDao).updateCallingCircleParameters(applicationId, userId, phoneNumber, serAgInf, action);
//	}
//	@Test
//	public void testUpdatePrepaidSubscriberLanguage()throws ApplicationException{
//		String userId="testUser";
//		String prevLanguage="F";
//		String serialNo="12";
//		String banId="121212";
//		String MDN="dada";
//		String lang="E";
//		mockedImpl.updatePrepaidSubscriberLanguage(banId, MDN, serialNo, prevLanguage, lang, userId);
//		
//		verify(mockedPrepaidDao).updatePrepaidSubscriberLanguage(banId, MDN, serialNo, prevLanguage, lang, userId);
//	}

	@Test
	public void testSaveSubscriptionPreference() throws ApplicationException{
		int preferenceTopicId = 1;
		
		SubscriptionPreferenceInfo preferenceInfo = new SubscriptionPreferenceInfo();
		preferenceInfo.setPreferenceTopicId(preferenceTopicId);
		preferenceInfo.setSubscriptionId(12255794);
		preferenceInfo.setSubscriberPreferenceId(22);
		preferenceInfo.setSubscrPrefChoiceSeqNum(0);
		preferenceInfo.setPreferenceValueTxt("N");
		String user = "ABC";

		mockedImpl.saveSubscriptionPreference(preferenceInfo, user);
		
		verify(mockedSubscriberDao).saveSubscriptionPreference(preferenceInfo, user);
	}
	
	@Test
	public void testRetrieveDiscounts() throws ApplicationException {
		
		int ban = 0;
		String subscriberId = "subid";
		String sessionId = "sessionid";
		String productType = "321";
		
		mockedImpl.retrieveDiscounts(ban, subscriberId, productType, sessionId);
		
		verify(mockedSubscriberDao).retrieveDiscounts(ban, subscriberId, productType, sessionId);
	}
	
	@Test
	public void testRetrieveCancellationPenalty() throws ApplicationException {
		
		int ban = 10;
		String subscriberId = "321";
		String productType = "32111";
		String sessionId = "3jklfda";
		mockedImpl.retrieveCancellationPenalty(ban, subscriberId, productType, sessionId);
		
		verify(mockedSubscriberDao).retrieveCancellationPenalty(ban, subscriberId, productType, sessionId);
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersNewConv() throws ApplicationException {		
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType("321");
		phoneNumberReservation.setPhoneNumberPattern("");
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		phoneNumberReservation.setNumberGroup0(numberGroup);
		
		when(mockedNewSubscriberDao.retrieveAvailablePhoneNumbers(Mockito.anyInt(), Mockito.any(PhoneNumberReservationInfo.class), Mockito.anyInt(), anyString())).thenReturn(new String[0]);
		when(mockedSubscriberDaoFactory.getNewSubscriberDao(anyString())).thenReturn(mockedNewSubscriberDao);		
		
		mockedImpl.setSubscriberDaoFactory(mockedSubscriberDaoFactory);
		
		mockedImpl.retrieveAvailablePhoneNumbers(1, null, phoneNumberReservation, 1, null);
		
		verify(mockedSubscriberDaoFactory, Mockito.times(1)).getNewSubscriberDao(anyString());
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersUpdateConv() throws ApplicationException {
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType("321");
		phoneNumberReservation.setPhoneNumberPattern("");
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		phoneNumberReservation.setNumberGroup0(numberGroup);
		
		when(mockedUpdateSubscriberDao.retrieveAvailablePhoneNumbers(Mockito.anyInt(), Mockito.anyString(),
				Mockito.any(PhoneNumberReservationInfo.class), Mockito.anyInt(), anyString())).thenReturn(new String[0]);
		when(mockedSubscriberDaoFactory.getUpdateSubscriberDao(anyString())).thenReturn(mockedUpdateSubscriberDao);		
		
		mockedImpl.setSubscriberDaoFactory(mockedSubscriberDaoFactory);
		
		mockedImpl.retrieveAvailablePhoneNumbers(1, "subscriber_number", phoneNumberReservation, 1, null);
		
		verify(mockedSubscriberDaoFactory, Mockito.times(1)).getUpdateSubscriberDao(anyString());
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersTestReturningObject() throws ApplicationException {
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType("321");		
		phoneNumberReservation.setPhoneNumberPattern("");
		NumberGroupInfo numberGroup = new NumberGroupInfo();
		phoneNumberReservation.setNumberGroup(numberGroup);
		numberGroup.setNumberLocation("123");
		numberGroup.setCode("abc");
		
		when(mockedNewSubscriberDao.retrieveAvailablePhoneNumbers(Mockito.anyInt(), Mockito.any(PhoneNumberReservationInfo.class), Mockito.anyInt(), anyString())).thenReturn(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9"});
		when(mockedSubscriberDaoFactory.getNewSubscriberDao(anyString())).thenReturn(mockedNewSubscriberDao);		
		
		mockedImpl.setSubscriberDaoFactory(mockedSubscriberDaoFactory);
		
		AvailablePhoneNumberInfo[] availablePhoneNumberInfo = mockedImpl.retrieveAvailablePhoneNumbers(1, null, phoneNumberReservation, 1, null);
		
		verify(mockedSubscriberDaoFactory, Mockito.times(1)).getNewSubscriberDao(anyString());
		
		assertEquals(availablePhoneNumberInfo.length, 9);
		
		for (int i = 0; i < 9; i++) {
			assertEquals(availablePhoneNumberInfo[i].getNumberLocationCode(), "123");
			assertEquals(availablePhoneNumberInfo[i].getNumberGroup0(), numberGroup);
			assertEquals(availablePhoneNumberInfo[i].getPhoneNumber(), String.valueOf(i + 1));
		}
	}
	
	@Test
	public void testCreateSubscriberIden() throws ApplicationException, TelusException {
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setProductType(SubscriberInfo.PRODUCT_TYPE_IDEN);		
		SubscriberContractInfo subscriberContractInfo = new SubscriberContractInfo();		
		boolean activate = false;
		boolean overridePatternSearchFee = false;
		String activationFeeChargeCode = null;
		boolean dealerHasDeposit = false;
		boolean portedIn = false;
		ServicesValidation srvValidation = null;
		String portProcessType = null;
		int oldBanId = 0;
		String oldSubscriberId = null;
		String sessionId = null;
		
		EquipmentInfo equipmentInfo = new EquipmentInfo();
		equipmentInfo.setProductType(EquipmentInfo.PRODUCT_TYPE_PAGER);
		when(mockedProductEquipmentHelper.getEquipmentInfobySerialNo(Mockito.anyString())).thenReturn(equipmentInfo);
		when(mockedAmdocsSessionManager.getClientIdentity(anyString())).thenReturn(new ClientIdentity());
		when(mockedAccountLifecycleManager.openSession(anyString(), anyString(), anyString())).thenReturn("");
		
		mockedImpl.createSubscriber(subscriberInfo, subscriberContractInfo
				, activate, overridePatternSearchFee, activationFeeChargeCode
				, dealerHasDeposit, portedIn, srvValidation
				, portProcessType, oldBanId, oldSubscriberId, sessionId);
		
		Mockito.verify(mockedNewSubscriberDao, Mockito.times(1)).createSubscriber(Mockito.any(SubscriberInfo.class)
				, Mockito.any(SubscriberContractInfo.class), Mockito.anyBoolean()
				, Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any(ServicesValidation.class)
				, Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());
		
		Mockito.verify(mockedProductEquipmentHelper, Mockito.times(1)).getEquipmentInfobySerialNo(Mockito.anyString());
		
		Mockito.verify(mockedReferenceDataHelper, Mockito.times(1)).retrieveDiscountPlans(true
				, subscriberInfo.getPricePlan(), subscriberInfo.getMarketProvince()
				, equipmentInfo.getProductPromoTypeList(), equipmentInfo.isInitialActivation()
				, subscriberContractInfo.getCommitmentMonths());
		
		Mockito.verify(mockedAccountLifecycleManager, Mockito.times(1)).openSession(anyString(), anyString(), anyString());
		Mockito.verify(mockedAccountLifecycleManager, Mockito.times(1)).createMemo(Mockito.any(MemoInfo.class), anyString());
		Mockito.verify(mockedAccountLifecycleManager, Mockito.times(1)).applyChargeToAccountWithOverride(Mockito.any(ChargeInfo.class), anyString());
	}
	
	@Test
	public void testCreateSubscriberOther() throws ApplicationException, TelusException {
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setProductType(SubscriberInfo.PRODUCT_TYPE_PAGER);		
		SubscriberContractInfo subscriberContractInfo = new SubscriberContractInfo();		
		boolean activate = false;
		boolean overridePatternSearchFee = false;
		String activationFeeChargeCode = null;
		boolean dealerHasDeposit = false;
		boolean portedIn = false;
		ServicesValidation srvValidation = null;
		String portProcessType = null;
		int oldBanId = 0;
		String oldSubscriberId = null;
		String sessionId = null;
		
		EquipmentInfo equipmentInfo = new EquipmentInfo();
		equipmentInfo.setProductType(EquipmentInfo.PRODUCT_TYPE_PAGER);
		when(mockedProductEquipmentHelper.getEquipmentInfobySerialNo(Mockito.anyString())).thenReturn(equipmentInfo);
		when(mockedAmdocsSessionManager.getClientIdentity(anyString())).thenReturn(new ClientIdentity());
		when(mockedAccountLifecycleManager.openSession(anyString(), anyString(), anyString())).thenReturn("");
		
		mockedImpl.createSubscriber(subscriberInfo, subscriberContractInfo
				, activate, overridePatternSearchFee, activationFeeChargeCode
				, dealerHasDeposit, portedIn, srvValidation
				, portProcessType, oldBanId, oldSubscriberId, sessionId);
		
		Mockito.verify(mockedNewSubscriberDao, Mockito.times(1)).createSubscriber(Mockito.any(SubscriberInfo.class)
				, Mockito.any(SubscriberContractInfo.class), Mockito.anyBoolean()
				, Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.any(ServicesValidation.class)
				, Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());
		
		Mockito.verify(mockedProductEquipmentHelper, Mockito.times(1)).getEquipmentInfobySerialNo(Mockito.anyString());
		
		Mockito.verify(mockedReferenceDataHelper, Mockito.times(1)).retrieveDiscountPlans(true
				, subscriberInfo.getPricePlan(), subscriberInfo.getMarketProvince()
				, equipmentInfo.getProductPromoTypeList(), equipmentInfo.isInitialActivation()
				, subscriberContractInfo.getCommitmentMonths());
		
		Mockito.verify(mockedAccountLifecycleManager, Mockito.times(1)).openSession(anyString(), anyString(), anyString());
		Mockito.verify(mockedAccountLifecycleManager, Mockito.times(1)).createMemo(Mockito.any(MemoInfo.class), anyString());
		Mockito.verify(mockedAccountLifecycleManager, Mockito.times(1)).applyChargeToAccountWithOverride(Mockito.any(ChargeInfo.class), anyString());
	}
	
	
	@Test
	public void testUpdateAddress() throws ApplicationException{
				
		int ban=8;
		String subscriber="123";
		String productType="C";
		AddressInfo addressInfo=new AddressInfo();
		String sessionId="1234";
		
		mockedImpl.updateAddress(ban, subscriber, productType, addressInfo, sessionId);
		verify(mockedAddressDao).updateAddress(ban, subscriber, productType, addressInfo, sessionId);
		
		
	}
	
	@Test
	public void testChangePricePlan() throws ApplicationException{
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setPhoneNumber("1234");
		subscriberInfo.setPortType("I");
		subscriberInfo.setProductType("C");
	
		SubscriberContractInfo subscriberContractInfo=new SubscriberContractInfo();
		 PricePlanValidationInfo ppInfo=new PricePlanValidationInfo();
		 String dealerCode="dealerCode";
		 String salesRepCode="salesRepCode";
		 String sessionId="sessionId";
		
		mockedImpl.changePricePlan(subscriberInfo, subscriberContractInfo, dealerCode,salesRepCode, ppInfo, sessionId);
		
		verify(mockedServiceAgreementDao).changePricePlan(subscriberInfo, subscriberContractInfo, dealerCode, salesRepCode, ppInfo, sessionId);
		
	
	}
	
	
	@Test
	public void testChangeServiceAgreement() throws ApplicationException,RemoteException, TelusException{
		System.out.println("testChangeServiceAgreement Start");
		
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setPhoneNumber("1234");
		subscriberInfo.setPortType("O");
		subscriberInfo.setProductType("A");
		
		SubscriberContractInfo subscriberContractInfo=new SubscriberContractInfo();
		PricePlanValidationInfo pricePlanValidation=new PricePlanValidationInfo();
		NumberGroupInfo numberGroupInfo= new NumberGroupInfo();
		
		String dealerCode=null;
		 String salesRepCode=null;
		 String sessionId=null;
		 
		 when(mockedAmdocsSessionManager.getClientIdentity(anyString())).thenReturn(new ClientIdentity());
		 // Flow 1  
		 when(mockedReferenceDataHelper.retrieveNumberGroupByPhoneNumberProductType(anyString(), anyString())).thenReturn(numberGroupInfo);
		 mockedImpl.changeServiceAgreement(subscriberInfo, subscriberContractInfo,  
				dealerCode, salesRepCode, pricePlanValidation, sessionId);
		 verify(mockedReferenceDataHelper).retrieveNumberGroupByPhoneNumberProductType(anyString(), anyString());
		
		 // Flow 2
		 subscriberInfo.setPortType("I");
		 when(mockedReferenceDataHelper.retrieveNumberGroupByPortedInPhoneNumberProductType(anyString(), anyString())).thenReturn(numberGroupInfo);
		 mockedImpl.changeServiceAgreement(subscriberInfo, subscriberContractInfo,  
					dealerCode, salesRepCode, pricePlanValidation, sessionId);
		 
		 verify(mockedReferenceDataHelper).retrieveNumberGroupByPortedInPhoneNumberProductType(anyString(), anyString());
		 
		 verify(mockedServiceAgreementDao, Mockito.times(2)).changeServiceAgreement(any(SubscriberInfo.class), any(SubscriberContractInfo.class),  
				 any(NumberGroupInfo.class), anyString(), anyString(), any(PricePlanValidationInfo.class), anyString());
	}	
	
	
	@Test
	public void testDeleteFutureDatedPricePlan() throws ApplicationException{
		
		 String sessionId="sessionId";
		 int ban=0;
		 String subscriberId="123";
		 String productType="C";
		
		mockedImpl.deleteFutureDatedPricePlan(ban, subscriberId, productType, sessionId);
		verify(mockedServiceAgreementDao).deleteFutureDatedPricePlan(anyInt(), anyString(),anyString(), anyString());
		
	
	}
	
	@Test
	public void testRetrieveCallingCircleParameters() throws ApplicationException{
		 String sessionId=null;
		 int banId=0;
		 String subscriberNo="123";
		 String productType="C";
		 String soc=null;
		 String featureCode=null;

		mockedImpl.retrieveCallingCircleParameters(banId, subscriberNo, soc, featureCode, productType, sessionId);
		verify(mockedServiceAgreementDao).retrieveCallingCircleParameters(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString());
	}
	
	@Test
	public void testUpdateCommitment() throws ApplicationException, RemoteException{
		
		
		SubscriberInfo subscriberInfo=new SubscriberInfo();
		subscriberInfo.setDealerCode("aa");
		subscriberInfo.setSalesRepId("bb");
		
		CommitmentInfo pCommitmentInfo = new CommitmentInfo();
		String sessionId=null;
		
		mockedImpl.updateCommitment(subscriberInfo, pCommitmentInfo, subscriberInfo.getDealerCode(), 
				subscriberInfo.getSalesRepId(), sessionId);
		
		verify(mockedServiceAgreementDao).updateCommitment(any(SubscriberInfo.class), any(CommitmentInfo.class), anyString(), anyString(), anyString());
		
	}
	
	@Test
	public void testCreateDeposit() throws ApplicationException{
		SubscriberInfo subscriberInfo=new SubscriberInfo();
		String sessionId=null;
		double amount=100;
		
		mockedImpl.createDeposit(subscriberInfo, amount, "memotxt", sessionId);
		
		verify(mockedDepositDao).createDeposit(any(SubscriberInfo.class), anyDouble(), anyString(), anyString());
		
		
	}
	@Test
	public void testActivateReservedSubscriber() throws ApplicationException, TelusException{
		SubscriberInfo subscriberInfo=new SubscriberInfo();
		subscriberInfo.setSerialNumber("123");
		subscriberInfo.setMarketProvince("ON");
		subscriberInfo.setProductType("C");
		subscriberInfo.setPricePlan("PP");
		
		SubscriberContractInfo subscriberContractInfo=new SubscriberContractInfo();
		Date startServiceDate=new Date();
		String activityReasonCode="aa";
		ServicesValidation srvValidation=null;
		String portProcessType="";
		int oldBanId=0;
		String oldSubscriberId="";
		String sessionId=null;
		
		EquipmentInfo equipmentInfo = new EquipmentInfo();
		equipmentInfo.setProductPromoTypeList(new long[1]);
		equipmentInfo.setInitialActivation(true);
		
		 when(mockedAmdocsSessionManager.getClientIdentity(anyString())).thenReturn(new ClientIdentity());
		 when(mockedAccountLifecycleManager.openSession(anyString(), anyString(), anyString())).thenReturn("");
		 when(mockedProductEquipmentHelper.getEquipmentInfobySerialNo(subscriberInfo.getSerialNumber())).thenReturn(equipmentInfo);
		 when(mockedReferenceDataHelper.retrieveDiscountPlans(true, subscriberInfo.getPricePlan(),
				 subscriberInfo.getMarketProvince(), new long[0], true, 12)).thenReturn(new DiscountPlanInfo[2]); 
		
		mockedImpl.activateReservedSubscriber(subscriberInfo, subscriberContractInfo, 
				startServiceDate, activityReasonCode, srvValidation, portProcessType, oldBanId, oldSubscriberId, sessionId);
		
		verify(mockedSubscriberDao).activateReservedSubscriber(anyInt(), anyString(), any(Date.class), anyString(), 
				anyString(), anyString(), any(ServicesValidation.class), anyString(), anyInt(), anyString(), anyString(), anyString());
		verify(mockedProductEquipmentHelper, Mockito.times(1)).getEquipmentInfobySerialNo(Mockito.anyString());
		verify(mockedReferenceDataHelper).retrieveDiscountPlans(anyBoolean(), anyString(), anyString(), any(long[].class), anyBoolean(),anyInt());
	}
	
	@Test
	public void testCancelSubscriber() throws ApplicationException, RemoteException{
		SubscriberInfo subscriberInfo=new SubscriberInfo();
		String sessionId=null;
		mockedImpl.cancelSubscriber(subscriberInfo, new Date(), "pActivityReasonCode", "pDepositReturnMethod", "pWaiveReason",
				"pUserMemoText", false,sessionId);
		
		verify(mockedSubscriberDao).cancelSubscriber(any(SubscriberInfo.class), any(Date.class), anyString(), anyString(), anyString(), anyString(), anyBoolean(),anyString());
		
	}
	
	@Test
	public void testRefreshSwitch() throws ApplicationException{
		
		String sessionId=null;
		mockedImpl.refreshSwitch(8, "pSubscriberId", "pProductType", sessionId);
		verify(mockedSubscriberDao).refreshSwitch(anyInt(), anyString(), anyString(), anyString());
		
	}
	

	@Test
	public void testRestoreSuspendedSubscriber() throws ApplicationException{
		SubscriberInfo subscriberInfo=new SubscriberInfo();
		Date pActivityDate=new Date();
		String sessionId=null;
		mockedImpl.restoreSuspendedSubscriber(subscriberInfo, pActivityDate, "pActivityReasonCode", "pUserMemoText", false, sessionId);
		
		verify(mockedSubscriberDao).restoreSuspendedSubscriber(any(SubscriberInfo.class), any(Date.class), anyString(), anyString(), anyBoolean(), anyString());
	}
	
	@Test
	public void testResumeCancelledSubscriber() throws ApplicationException{
		SubscriberInfo subscriberInfo=new SubscriberInfo();
		String sessionId=null;
		
		mockedImpl.resumeCancelledSubscriber(subscriberInfo, "pActivityReasonCode", "pUserMemoText",
				true, "portProcessType", 99, "oldSubscriberId", sessionId);
		verify(mockedSubscriberDao).resumeCancelledSubscriber(any(SubscriberInfo.class),
				anyString(), anyString(), anyBoolean(), anyString(), anyInt(), anyString(), anyString());
	}
	
	
	@Test
	public void testRetrieveSubscribersByMemberIdentity() throws ApplicationException{
		
		String sessionId=null;
		int urbanId=0;
		int fleetId=0;
		int memberId=0;
		SubscriberInfo[] subscriber=new SubscriberInfo[1];
		SubscriberInfo subscriberInfo= new SubscriberInfo();
		subscriberInfo.setBanId(8);
		subscriberInfo.setSubscriberId("123");
		subscriber[0]=subscriberInfo;
		
		when(mockedSubscriberDao.retrieveSubscribersByMemberIdentity(urbanId, fleetId, memberId, sessionId)).thenReturn(subscriber);
		when(mockedSubscriberLifecycleHelper.retrieveSubscriber(8, "123")).thenReturn(subscriberInfo);
		
		mockedImpl.retrieveSubscribersByMemberIdentity(urbanId, fleetId, memberId, sessionId);
		
		verify(mockedSubscriberDao).retrieveSubscribersByMemberIdentity(anyInt(), anyInt(), anyInt(), anyString());
		verify(mockedSubscriberLifecycleHelper).retrieveSubscriber(anyInt(), anyString());
	}
	

	@Test
	public void testaddMemberIdentity() throws ApplicationException {
		IDENSubscriberInfo subscriberInfo = new IDENSubscriberInfo();
		SubscriberContractInfo subscriberContractInfo = new SubscriberContractInfo();
		String dealerCode = "";
		String salesRepCode = "";
		int urbanId = 0;
		int fleetId = 0;
		String memberId = "";
		boolean pricePlanChange = true;
		String sessionId = "123";
		mockedImpl.addMemberIdentity(subscriberInfo, subscriberContractInfo, dealerCode, salesRepCode
				, urbanId, fleetId, memberId, pricePlanChange, sessionId);
		
		verify(mockedFleetDao, Mockito.times(1)).addMemberIdentity(subscriberInfo, subscriberContractInfo, dealerCode
				, salesRepCode, urbanId, fleetId, memberId, pricePlanChange, sessionId);	
	}
	
	@Test
	public void testChangeMemberID() throws ApplicationException {
		IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
		String newMemberId = "123";
		String sessionId = "345";
		mockedImpl.changeMemberId(idenSubscriberInfo, newMemberId, sessionId);
		
		verify(mockedFleetDao).changeMemberId(idenSubscriberInfo, newMemberId, sessionId);
	}
	
	@Test
	public void testChangeMemberIdentity() throws ApplicationException {
		IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
		int newUrbanId = 1;
		int newFleetId = 2;
		String newMemberId = "321";
		String sessionId = "321";
		mockedImpl.changeMemberIdentity(idenSubscriberInfo, newUrbanId, newFleetId, newMemberId, sessionId);
		
		verify(mockedFleetDao).changeMemberIdentity(idenSubscriberInfo, newUrbanId, newFleetId, newMemberId, sessionId);
	}
	
	@Test
	public void testChangeTalkGroup() throws ApplicationException {
		IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
		TalkGroupInfo[] addedTalkGroups = new TalkGroupInfo[2];
		TalkGroupInfo[] removedTalkGroups = new TalkGroupInfo[3];
		String sessionId = "";
		mockedImpl.changeTalkGroups(idenSubscriberInfo, addedTalkGroups, removedTalkGroups, sessionId);
		
		verify(mockedFleetDao).changeTalkGroups(idenSubscriberInfo, addedTalkGroups, removedTalkGroups, sessionId);
	}
	
	@Test
	public void testGetAvailableMemberIds() throws ApplicationException {
		int urbanId = 0;
		int fleetId = 1;
		String memberIdPattern = "321";
		int max = 3;
		String sessionId = "32111";
		mockedImpl.getAvailableMemberIDs(urbanId, fleetId, memberIdPattern, max, sessionId);
		
		verify(mockedFleetDao).retrieveAvailableMemberIDs(urbanId, fleetId, memberIdPattern, max, sessionId);
	}
	
	@Test
	public void testGetAvailableMemberIDs() throws ApplicationException {
		int urbanId = 0;
		int fleetId = 1;
		String memberIdPattern = "321";
		int maxMemberIds = 10;
		String sessionId = "321321";
		mockedImpl.retrieveAvailableMemberIds(urbanId, fleetId, memberIdPattern, maxMemberIds, sessionId);
		
		verify(mockedFleetDao).retrieveAvailableMemberIds(urbanId, fleetId, memberIdPattern, maxMemberIds, sessionId);
	}
	
	@Test
	public void testRemoveMemberIdentity() throws ApplicationException {
		IDENSubscriberInfo subscriberInfo = new IDENSubscriberInfo();
		SubscriberContractInfo subscriberContractInfo = new SubscriberContractInfo();
		String dealerCode = "321321";
		String salesRepCode = "321321";
		boolean pricePlanChange = false;
		String sessionId = "321321321321";
		mockedImpl.removeMemberIdentity(subscriberInfo, subscriberContractInfo, dealerCode, salesRepCode, pricePlanChange, sessionId);
		
		verify(mockedFleetDao).removeMemberIdentity(subscriberInfo, subscriberContractInfo, dealerCode, salesRepCode, pricePlanChange, sessionId);
	}
	
	@Test
	public void testReserveMemberId() throws ApplicationException {
		IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
		idenSubscriberInfo.setPhoneNumber("4166688646");
		String sessionId = "321";
		mockedImpl.reserveMemberId(idenSubscriberInfo, sessionId);
		
		FleetIdentityInfo fleetIdentityInfo = new FleetIdentityInfo(416, 668);
		String wildCard = "8646";
		verify(mockedFleetDao).reserveMemberId(idenSubscriberInfo, fleetIdentityInfo, wildCard,true, sessionId);
	}
	
	@Test
	public void testReserveMemberIdFleetWildCard() throws ApplicationException {
		IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
		FleetIdentityInfo fleetIdentityInfo = new FleetIdentityInfo();
		String wildCard = "123";
		String sessionId = "321";
		mockedImpl.reserveMemberId(idenSubscriberInfo, fleetIdentityInfo, wildCard, sessionId);
		
		verify(mockedFleetDao).reserveMemberId(idenSubscriberInfo, fleetIdentityInfo, wildCard,true, sessionId);
	}
	
	@Test
	public void testRetrieveAvailableMemberIds() throws ApplicationException {
		int urbanId = 321;
		int fleetId = 123;
		String memberIdPattern = "321321";
		int maxMemberIds = 100;
		String sessionId = "321321";
		mockedImpl.retrieveAvailableMemberIds(urbanId, fleetId, memberIdPattern, maxMemberIds, sessionId);
		
		verify(mockedFleetDao).retrieveAvailableMemberIds(urbanId, fleetId, memberIdPattern, maxMemberIds, sessionId);
	}
	
	@Test
	public void testRetrieveTalkGroupsBySubscriber() throws ApplicationException {
		int ban = 100;
		String subscriberId = "321";
		String sessionId = "32121";
		mockedImpl.retrieveTalkGroupsBySubscriber(ban, subscriberId, sessionId);
		
		verify(mockedFleetDao).retrieveTalkGroupsBySubscriber(ban, subscriberId, sessionId);
	}
	
	@Test
	public void testResetVoicemailPassword() throws ApplicationException {
		int ban = 0;
		String subscriberId = "string";
		String productType = " string 2";
		String sessionId = "string 3";
		String[] stringArray = new String[]{"123"};
				
		when(mockedSubscriberLifecycleHelper.retrieveVoiceMailFeatureByPhoneNumber(anyString(), anyString())).thenReturn(stringArray);
		
		mockedImpl.resetVoiceMailPassword(ban, subscriberId, productType, sessionId);
		
		verify(mockedUpdateSubscriberDao, Mockito.times(0)).resetVoiceMailPassword(ban, subscriberId, stringArray, sessionId);
		
		stringArray = new String[]{"123", "123"};
		
		when(mockedSubscriberLifecycleHelper.retrieveVoiceMailFeatureByPhoneNumber(anyString(), anyString())).thenReturn(stringArray);
		
		mockedImpl.resetVoiceMailPassword(ban, subscriberId, productType, sessionId);
		
		verify(mockedUpdateSubscriberDao, Mockito.times(1)).resetVoiceMailPassword(ban, subscriberId, stringArray, sessionId);
	}
	
	@Test
	public void testDeleteMsisdnFeature() throws ApplicationException {
		AdditionalMsiSdnFtrInfo ftrInfo = new AdditionalMsiSdnFtrInfo();
		String sessionId = "321";
		mockedImpl.deleteMsisdnFeature(ftrInfo, sessionId);
		
		verify(mockedUpdateIdenSubscriberDao).deleteMsisdnFeature(ftrInfo, sessionId);
	}
	
	@Test
	public void testUpdateBirthdate() throws ApplicationException {
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		String sessionId = "321321";
		mockedImpl.updateBirthDate(subscriberInfo, sessionId);
		
		verify(mockedUpdatePcsSubscriberDao).updateBirthDate(subscriberInfo, sessionId);
	}
	
	@Test
	public void testChangePhoneNumber() throws ApplicationException {
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		AvailablePhoneNumberInfo newPhoneNumber = new AvailablePhoneNumberInfo();
		String reasonCode = "321";
		String sessionId = "321321";
		mockedImpl.changePhoneNumber(subscriberInfo, newPhoneNumber, reasonCode, null, null, sessionId);
		
		verify(mockedUpdateSubscriberDao).changePhoneNumber(subscriberInfo, newPhoneNumber, reasonCode, null, null, sessionId);
	}
	
	@Test
	public void testChangePhoneNumberDealerCodeSalesRepCode() throws ApplicationException {
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		AvailablePhoneNumberInfo newPhoneNumber = new AvailablePhoneNumberInfo();
		String reasonCode = "321321";
		String dealerCode = "32132122";
		String salesRepCode = "ddfd";
		String sessionId = "3211dldll";
		mockedImpl.changePhoneNumber(subscriberInfo, newPhoneNumber, reasonCode, dealerCode, salesRepCode, sessionId);
		
		verify(mockedUpdateSubscriberDao).changePhoneNumber(subscriberInfo, newPhoneNumber, reasonCode, dealerCode, salesRepCode, sessionId);
	}
	
	@Test
	public void testReleaseSubscriber() throws ApplicationException {
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		String sessionId = "321321";
		mockedImpl.releaseSubscriber(subscriberInfo, sessionId);
		
		verify(mockedNewSubscriberDao).releaseSubscriber(subscriberInfo, sessionId);
	}
	
	@Test
	public void testReserveLikePhoneNumber() throws ApplicationException {
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo(); 
		String sessionId = "321321";
		mockedImpl.reserveLikePhoneNumber(subscriberInfo, phoneNumberReservation, sessionId);
		
		verify(mockedNewSubscriberDao).reserveLikePhoneNumber(subscriberInfo, phoneNumberReservation, sessionId);
	}
	
	@Test
	public void testReservePhoneNumber() throws ApplicationException {
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		String sessionId = "321321";
		
		mockedImpl.reservePhoneNumber(subscriberInfo, phoneNumberReservation, false, sessionId);
		
		verify(mockedNewSubscriberDao).reservePhoneNumber(subscriberInfo, phoneNumberReservation, false, sessionId);
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersException() throws ApplicationException {
		int ban = 3210;
		String subscriberId = "";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo(); 
		int maxNumbers = 321;
		String sessionId = "321321";
		
		when(mockedNewSubscriberDao.retrieveAvailablePhoneNumbers(anyInt(), any(PhoneNumberReservationInfo.class), anyInt(), anyString())).thenReturn(new String[0]);
		when(mockedUpdateSubscriberDao.retrieveAvailablePhoneNumbers(anyInt(), anyString(), any(PhoneNumberReservationInfo.class), anyInt(), anyString())).thenReturn(new String[0]);
		
		try {
			mockedImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
			fail("Exception expected");
		} catch (ApplicationException ae) {
			assertEquals("phoneNumberReservation.getProductType() should not be null", ae.getErrorMessage());
		}
		
		phoneNumberReservation.setProductType("321321");
		try {
			mockedImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
			fail("Exception expected");
		} catch (ApplicationException ae) {
			assertEquals("phoneNumberReservation.getNumberGroup() should not be null", ae.getErrorMessage());
		}
		
		phoneNumberReservation.setNumberGroup(new NumberGroupInfo());
		try {
			mockedImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
			fail("Exception expected");
		} catch (ApplicationException ae) {
			assertEquals("PhoneNumberReservation.getPhoneNumberPattern() should not be null.", ae.getErrorMessage());
		}
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersUpdate() throws ApplicationException {
		int ban = 3210;
		String subscriberId = "";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo(); 
		int maxNumbers = 321;
		String sessionId = "321321";
		
		when(mockedNewSubscriberDao.retrieveAvailablePhoneNumbers(anyInt(), any(PhoneNumberReservationInfo.class), anyInt(), anyString())).thenReturn(new String[0]);
		when(mockedUpdateSubscriberDao.retrieveAvailablePhoneNumbers(anyInt(), anyString(), any(PhoneNumberReservationInfo.class), anyInt(), anyString())).thenReturn(new String[0]);
			
		
		phoneNumberReservation.setProductType("321321");		
		phoneNumberReservation.setNumberGroup(new NumberGroupInfo());		
		phoneNumberReservation.setPhoneNumberPattern("321321");		
		subscriberId = "321321";
		mockedImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);		
		verify(mockedNewSubscriberDao, Mockito.times(0)).retrieveAvailablePhoneNumbers(ban, phoneNumberReservation, maxNumbers, sessionId);
		verify(mockedUpdateSubscriberDao, Mockito.times(1)).retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersNew() throws ApplicationException {
		int ban = 3210;
		String subscriberId = "";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo(); 
		int maxNumbers = 321;
		String sessionId = "321321";
		
		when(mockedNewSubscriberDao.retrieveAvailablePhoneNumbers(anyInt(), any(PhoneNumberReservationInfo.class), anyInt(), anyString())).thenReturn(new String[0]);
		when(mockedUpdateSubscriberDao.retrieveAvailablePhoneNumbers(anyInt(), anyString(), any(PhoneNumberReservationInfo.class), anyInt(), anyString())).thenReturn(new String[0]);
				
		phoneNumberReservation.setProductType("321321");
		phoneNumberReservation.setNumberGroup(new NumberGroupInfo());		
		phoneNumberReservation.setPhoneNumberPattern("321321");
		
		mockedImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);		
		verify(mockedNewSubscriberDao, Mockito.times(1)).retrieveAvailablePhoneNumbers(ban, phoneNumberReservation, maxNumbers, sessionId);
		verify(mockedUpdateSubscriberDao, Mockito.times(0)).retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);		
	}
	
	@Test
	public void testAddMemberIdentity() throws ApplicationException{
		
		SubscriberContractInfo subscriberContractInfo=new SubscriberContractInfo();
		
		IDENSubscriberInfo idenSubscriberInfo= new IDENSubscriberInfo();
		idenSubscriberInfo.setDealerCode("dealer");
		idenSubscriberInfo.setSalesRepId("salesRep");
		
		int urbanId=0;
		int fleetId=0;
		String memberId="0";
		
		String sessionId=null;
	
		mockedImpl.addMemberIdentity(idenSubscriberInfo, subscriberContractInfo, idenSubscriberInfo.getDealerCode(), 
				idenSubscriberInfo.getSalesRepId(), urbanId, fleetId, memberId, false, sessionId);
		
		verify(mockedFleetDao).addMemberIdentity(any(IDENSubscriberInfo.class), any(SubscriberContractInfo.class), 
				anyString(), anyString(), anyInt(), anyInt(), anyString(), anyBoolean(), anyString());
		
	}
	
	@Test
	public void testChangeMemberId() throws ApplicationException{
		IDENSubscriberInfo idenSubscriberInfo= new IDENSubscriberInfo();
		String sessionId=null;
		String memberId="0";
		mockedImpl.changeMemberId(idenSubscriberInfo, memberId, sessionId);
		verify(mockedFleetDao).changeMemberId(any(IDENSubscriberInfo.class), anyString(), anyString());
	}

	
	@Test
	public void testChangeTalkGroups() throws ApplicationException{
		IDENSubscriberInfo idenSubscriberInfo= new IDENSubscriberInfo();
		String sessionId=null;
		TalkGroupInfo[] talkGroups=new TalkGroupInfo[1];
		
		mockedImpl.changeTalkGroups(idenSubscriberInfo, talkGroups, talkGroups, sessionId);
		verify(mockedFleetDao).changeTalkGroups(any(IDENSubscriberInfo.class), any(TalkGroupInfo[].class), any(TalkGroupInfo[].class), anyString());
	}
	


	@Test
	public void testResetVoiceMailPassword() throws ApplicationException{
		String[] voiceMailSocAndFeature=new String[]{"soc1","soc2"};
		String subscriberId="";
		String sessionId=null;
		
		when(mockedSubscriberLifecycleHelper.retrieveVoiceMailFeatureByPhoneNumber(anyString(), anyString())).thenReturn(voiceMailSocAndFeature);
		when(mockedSubscriberDaoFactory.getUpdateSubscriberDao(anyString())).thenReturn(mockedUpdateSubscriberDao);
		
		mockedImpl.resetVoiceMailPassword(8, subscriberId, "C", sessionId);
		
		verify(mockedSubscriberLifecycleHelper).retrieveVoiceMailFeatureByPhoneNumber(anyString(), anyString());
		verify(mockedSubscriberDaoFactory).getUpdateSubscriberDao(anyString());
		verify(mockedUpdateSubscriberDao).resetVoiceMailPassword(anyInt(), anyString(), any(String[].class), anyString());
	}
	

	@Test
	public void testUpdateBirthDate() throws ApplicationException{
		
		String sessionId=null;
		
		when(mockedSubscriberDaoFactory.getUpdatePcsSubscriberDao()).thenReturn(mockedUpdatePcsSubscriberDao);
		mockedImpl.updateBirthDate(new SubscriberInfo(), sessionId);
		
		verify(mockedUpdatePcsSubscriberDao).updateBirthDate(any(SubscriberInfo.class), anyString());
		verify(mockedSubscriberDaoFactory).getUpdatePcsSubscriberDao();
	}
	

	@Test
	public void testChangePhoneNumber1() throws ApplicationException{
		
		when(mockedSubscriberDaoFactory.getUpdateSubscriberDao(anyString())).thenReturn(mockedUpdateSubscriberDao);
		mockedImpl.changePhoneNumber(new SubscriberInfo(), new AvailablePhoneNumberInfo(), "reasonCode",
				"dealerCode", "salesRepCode", "sessionId");
		
		verify(mockedSubscriberDaoFactory).getUpdateSubscriberDao(anyString());
		verify(mockedUpdateSubscriberDao).changePhoneNumber(any(SubscriberInfo.class), any(AvailablePhoneNumberInfo.class), 
				anyString(), anyString(), anyString(), anyString());
	}
	
	@Test
	public void testMoveSubscriber_flow1() throws ApplicationException, TelusException{
		//Flow 1 
		when(mockedSubscriberDaoFactory.getUpdateSubscriberDao(anyString())).thenReturn(mockedUpdateSubscriberDao);
		mockedImpl.moveSubscriber(new SubscriberInfo(), 10, new Date(), false, 
				"activityReasonCode", "userMemoText", "dealerCode", "salesRepCode", "sessionId");
		
		verify(mockedSubscriberDaoFactory).getUpdateSubscriberDao(anyString());
		verify(mockedUpdateSubscriberDao).moveSubscriber(any(SubscriberInfo.class), anyInt(), any(Date.class), anyBoolean(),
				anyString(), anyString(), anyString(), anyString(),anyString());
		verify(mockedSubscriberDaoFactory).getUpdateSubscriberDao(anyString());

	}
	
	@Test
	public void testMoveSubscriber_flow2() throws ApplicationException, TelusException{

		//Flow 2
		when(mockedSubscriberDaoFactory.getUpdateSubscriberDao(anyString())).thenReturn(mockedUpdateIdenSubscriberDao);
		when(mockedReferenceDataHelper.retrieveNumberGroupByPhoneNumberProductType(anyString(), anyString())).thenReturn(new NumberGroupInfo());
		when(mockedReferenceDataHelper.retrieveFleetByFleetIdentity(any(FleetIdentityInfo.class))).thenReturn(new FleetInfo());
		
		mockedImpl.moveSubscriber(new IDENSubscriberInfo(), 10, new Date(), false, 
				"activityReasonCode", "userMemoText", "dealerCode", "salesRepCode", "sessionId");
		
		verify(mockedSubscriberDaoFactory).getUpdateSubscriberDao(anyString());
		verify(mockedReferenceDataHelper).retrieveNumberGroupByPhoneNumberProductType(anyString(), anyString());
//		verify(mockedReferenceDataHelper).retrieveFleetByFleetIdentity(any(FleetIdentityInfo.class));
		verify(mockedUpdateIdenSubscriberDao).moveSubscriber(any(SubscriberInfo.class), anyInt(), any(Date.class), anyBoolean(),
				anyString(), anyString(), anyString(), anyString(),any(FleetInfo.class),anyString());
	}
	

	
	@Test
	public void testRetrieveAvailablePhoneNumbers() throws ApplicationException{

		int ban=0;
		String subscriberId=null;
		int maxNumbers=0;
		String sessionId=null;
		String[] phNumbers= new String[]{"0123456789"};
		
		PhoneNumberReservationInfo phoneNumberReservation= new PhoneNumberReservationInfo();
		NumberGroupInfo numberGroupInfo=new NumberGroupInfo();
		numberGroupInfo.setNumberLocation("NoGroup");
		phoneNumberReservation.setNumberGroup0(numberGroupInfo);
		phoneNumberReservation.setProductType("A");
		phoneNumberReservation.setNumberGroup(numberGroupInfo);
		phoneNumberReservation.setPhoneNumberPattern("012");
		
		when(mockedSubscriberDaoFactory.getNewSubscriberDao(anyString())).thenReturn(mockedNewSubscriberDao);
		when(mockedNewSubscriberDao.retrieveAvailablePhoneNumbers(anyInt(), any(PhoneNumberReservationInfo.class), 
				anyInt(), anyString())).thenReturn(phNumbers);
		
		AvailablePhoneNumberInfo[] availablePhoneNumberInfos=mockedImpl.retrieveAvailablePhoneNumbers(ban,
				subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		
		assertEquals("NoGroup", availablePhoneNumberInfos[0].getNumberGroup0().getNumberLocation());
		assertEquals("0123456789",availablePhoneNumberInfos[0].getPhoneNumber());
		
		verify(mockedSubscriberDaoFactory).getNewSubscriberDao(anyString());
		verify(mockedNewSubscriberDao).retrieveAvailablePhoneNumbers(anyInt(), any(PhoneNumberReservationInfo.class), 
				anyInt(), anyString());
		
	}
	
	
	@Test
	public void testCancelAdditionalMsisdn() throws ApplicationException{

		when(mockedSubscriberDaoFactory.getUpdateIdenSubscriberDao()).thenReturn(mockedUpdateIdenSubscriberDao);
		mockedImpl.cancelAdditionalMsisdn(new AdditionalMsiSdnFtrInfo[0], "additionalMsisdn", "sessionId");
		
		verify(mockedSubscriberDaoFactory).getUpdateIdenSubscriberDao();
		verify(mockedUpdateIdenSubscriberDao).cancelAdditionalMsisdn(any(AdditionalMsiSdnFtrInfo[].class), anyString(), anyString());
		
	}
	
	@Test
	public void testCancelPortedInSubscriber() throws ApplicationException{
		SubscriberInfo subscriberInfo=new SubscriberInfo();
		when(mockedSubscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(anyString())).thenReturn(subscriberInfo);
		when(mockedSubscriberDaoFactory.getUpdateIdenPcsSubscriberDao(anyString())).thenReturn(mockedUpdateIdenPcsSubscriberDao);
		mockedImpl.cancelPortedInSubscriber(0, "0123456789", "deactivationReason", new Date(), "portOutInd", false, "sessionId");
		
		verify(mockedSubscriberLifecycleHelper).retrieveSubscriberByPhoneNumber(anyString());
		verify(mockedSubscriberDaoFactory).getUpdateIdenPcsSubscriberDao(anyString());
		verify(mockedUpdateIdenPcsSubscriberDao).cancelPortedInSubscriber(anyInt(), anyString(), anyString(), any(Date.class),
				anyString(), anyBoolean(), anyString());
		
	}
	
	
	@Test
	public void testChangeFaxNumber() throws ApplicationException{
		SubscriberInfo subscriber=new SubscriberInfo();
		subscriber.setBanId(1);
		subscriber.setSubscriberId("123");
		subscriber.setPortType("I");
		
		NumberGroupInfo numberGroupInfo=new NumberGroupInfo();
		numberGroupInfo.setNumberLocation("NoGroup");
		
		AvailablePhoneNumberInfo newFaxNumber=new AvailablePhoneNumberInfo();
		newFaxNumber.setPhoneNumber("0123456789");
		newFaxNumber.setNumberGroup(numberGroupInfo);
		String sessionId=null;
		
		when(mockedSubscriberDaoFactory.getUpdateIdenSubscriberDao()).thenReturn(mockedUpdateIdenSubscriberDao);
		mockedImpl.changeFaxNumber(subscriber, newFaxNumber, sessionId);
		
		verify(mockedSubscriberDaoFactory).getUpdateIdenSubscriberDao();
		verify(mockedUpdateIdenSubscriberDao).changeFaxNumber(anyInt(), anyString(), anyString(), 
				any(NumberGroupInfo.class), anyBoolean(), anyString());
	}
	
	@Test
	public void testChangeIMSI() throws ApplicationException{
		when(mockedSubscriberDaoFactory.getUpdateIdenSubscriberDao()).thenReturn(mockedUpdateIdenSubscriberDao);
		
		mockedImpl.changeIMSI(8, "subscriberId", "sessionId");
		verify(mockedSubscriberDaoFactory).getUpdateIdenSubscriberDao();
		verify(mockedUpdateIdenSubscriberDao).changeIMSI(anyInt(), anyString(), anyString());
	}
	
	@Test
	public void testChangeIP() throws ApplicationException{
		when(mockedSubscriberDaoFactory.getUpdateIdenSubscriberDao()).thenReturn(mockedUpdateIdenSubscriberDao);
		
		mockedImpl.changeIP(8, "subscriberId", "newIp", "newIpType", "newIpCorpCode", "sessionId");
		verify(mockedSubscriberDaoFactory).getUpdateIdenSubscriberDao();
		verify(mockedUpdateIdenSubscriberDao).changeIP(anyInt(), anyString(),anyString(), anyString(),anyString(), anyString());
	}
	
	@Test
	public void testMigrateSubscriber() throws ApplicationException{
		
		SubscriberInfo srcSubscriberInfo= new SubscriberInfo();
		SubscriberInfo newSubscriberInfo= new SubscriberInfo();
		Date activityDate= new Date();
		SubscriberContractInfo subscriberContractInfo= new SubscriberContractInfo();
		EquipmentInfo newPrimaryEquipmentInfo= new EquipmentInfo();
		EquipmentInfo[] newSecondaryEquipmentInfo= new EquipmentInfo[0];
		String sessionId = null;
//		Flow 1
		when(migrationRequestInfo.isM2P()).thenReturn(true);
		
//		Flow 2
//		when(migrationRequestInfo.isP2M()).thenReturn(true);
		when(mockedSubscriberDaoFactory.getNewIdenPcsSubscriberDao(anyString())).thenReturn(mockedNewIdenPcsSubscriberDao);
		mockedImpl.migrateSubscriber(srcSubscriberInfo, newSubscriberInfo, activityDate, 
				subscriberContractInfo, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo, migrationRequestInfo, sessionId);
		verify(mockedSubscriberDaoFactory).getNewIdenPcsSubscriberDao(anyString());
		verify(mockedNewIdenPcsSubscriberDao).migrateSubscriber(any(SubscriberInfo.class), any(SubscriberInfo.class),
				any(Date.class),any(SubscriberContractInfo.class), any(EquipmentInfo.class),any(EquipmentInfo[].class), 
				any(MigrationRequestInfo.class), anyString());
		
//		Flow 3
		when(migrationRequestInfo.isM2P()).thenReturn(false);
		when(migrationRequestInfo.isP2M()).thenReturn(false);
		when(mockedSubscriberDaoFactory.getUpdatePcsSubscriberDao()).thenReturn(mockedUpdatePcsSubscriberDao);

		mockedImpl.migrateSubscriber(srcSubscriberInfo, newSubscriberInfo, activityDate, 
				subscriberContractInfo, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo, migrationRequestInfo, sessionId);
		verify(mockedSubscriberDaoFactory).getUpdatePcsSubscriberDao();
		
		verify(mockedUpdatePcsSubscriberDao).migrateSubscriber(any(SubscriberInfo.class), anyInt(),
				any(Date.class),any(SubscriberContractInfo.class), any(EquipmentInfo.class),any(EquipmentInfo[].class), 
				any(MigrationRequestInfo.class), anyString());
	}
	
	@Test
	public void testPortChangeSubscriberNumber() throws ApplicationException{
		
		when(mockedSubscriberDaoFactory.getUpdateIdenPcsSubscriberDao(anyString())).thenReturn(mockedUpdateIdenPcsSubscriberDao);
		
		mockedImpl.portChangeSubscriberNumber(new SubscriberInfo(), new AvailablePhoneNumberInfo(), "reasonCode", "dealerCode", "salesRepCode",
				"portProcessType", 9, "oldSubscriberId", "sessionId");
		verify(mockedSubscriberDaoFactory).getUpdateIdenPcsSubscriberDao(anyString());
		verify(mockedUpdateIdenPcsSubscriberDao).portChangeSubscriberNumber(any(SubscriberInfo.class), any(AvailablePhoneNumberInfo.class),
				anyString(),anyString(), anyString(),anyString(), anyInt(), anyString(),anyString());
	}
	
	@Test
	public void testReleasePortedInSubscriber() throws ApplicationException{
		
		when(mockedSubscriberDaoFactory.getNewIdenPcsSubscriberDao(anyString())).thenReturn(mockedNewIdenPcsSubscriberDao);
		
		mockedImpl.releasePortedInSubscriber(new SubscriberInfo(), "sessionId");
		verify(mockedSubscriberDaoFactory).getNewIdenPcsSubscriberDao(anyString());
		verify(mockedNewIdenPcsSubscriberDao).releasePortedInSubscriber(any(SubscriberInfo.class),anyString());
	}
	
	@Test
	public void testReserveAdditionalPhoneNumber() throws ApplicationException{
		
		NumberGroupInfo numberGroup=new NumberGroupInfo();
		AvailablePhoneNumberInfo additionalPhoneNumber= new AvailablePhoneNumberInfo();
		additionalPhoneNumber.setPhoneNumber("0123456789");
		additionalPhoneNumber.setNumberGroup(numberGroup);
		
		when(mockedSubscriberDaoFactory.getUpdateIdenSubscriberDao()).thenReturn(mockedUpdateIdenSubscriberDao);
		
		mockedImpl.reserveAdditionalPhoneNumber(8, "subscriberId", additionalPhoneNumber, "sessionId");
		verify(mockedSubscriberDaoFactory).getUpdateIdenSubscriberDao();
		verify(mockedUpdateIdenSubscriberDao).reserveAdditionalPhoneNumber(anyInt(), anyString(), any(NumberGroupInfo.class),anyString(),anyString());
	}
	
	@Test
	public void testReservePortedInPhoneNumber() throws ApplicationException{
		SubscriberInfo subscriberInfo= new SubscriberInfo();
		subscriberInfo.setProductType("C");
	
		when(mockedSubscriberDaoFactory.getNewPcsSubscriberDao()).thenReturn(mockedNewPcsSubscriberDao);
		
		mockedImpl.reservePortedInPhoneNumber(subscriberInfo, phoneNumberReservation, true, "sessionId");
		verify(mockedSubscriberDaoFactory).getNewPcsSubscriberDao();
		verify(mockedNewPcsSubscriberDao).reservePortedInPhoneNumber(any(SubscriberInfo.class),any(PhoneNumberReservationInfo.class), anyBoolean(), anyString());
	}
	
	@Test
	public void testSearchSubscriberByAdditionalMsiSdn() throws ApplicationException{
		when(mockedSubscriberDaoFactory.getUpdateIdenSubscriberDao()).thenReturn(mockedUpdateIdenSubscriberDao);
		
		mockedImpl.searchSubscriberByAdditionalMsiSdn("additionalMsisdn", "sessionId");
		verify(mockedSubscriberDaoFactory).getUpdateIdenSubscriberDao();
		verify(mockedUpdateIdenSubscriberDao).searchSubscriberByAdditionalMsiSdn(anyString(),anyString());
	}
	
	@Test
	public void testSendTestPage() throws ApplicationException{
		when(mockedSubscriberDaoFactory.getUpdatePagerSubscriberDao()).thenReturn(mockedUpdatePagerSubscriberDao);
		
		mockedImpl.sendTestPage(8, "subscriberId", "sessionId");
		verify(mockedSubscriberDaoFactory).getUpdatePagerSubscriberDao();
		verify(mockedUpdatePagerSubscriberDao).sendTestPage(anyInt(), anyString(),anyString());
	}
	
	@Test
	public void testSetSubscriberPortIndicator1() throws ApplicationException, TelusException{
		SubscriberInfo subscriberInfo= new SubscriberInfo();
		subscriberInfo.setProductType("C");
		subscriberInfo.setSubscriberId("1234");
		
		when(mockedReferenceDataHelper.retrieveLogicalDate()).thenReturn(new Date());
		when(mockedSubscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(anyString())).thenReturn(subscriberInfo);
		when(mockedSubscriberDaoFactory.getUpdateIdenPcsSubscriberDao(anyString())).thenReturn(mockedUpdateIdenPcsSubscriberDao);
		
		mockedImpl.setSubscriberPortIndicator("0123456789", "sessionId");
		
		verify(mockedReferenceDataHelper).retrieveLogicalDate();
		verify(mockedSubscriberLifecycleHelper).retrieveSubscriberByPhoneNumber(anyString());
		verify(mockedSubscriberDaoFactory).getUpdateIdenPcsSubscriberDao(anyString());
		
		
	}
	
	@Test
	public void testSetSubscriberPortIndicator2() throws ApplicationException, TelusException{
		SubscriberInfo subscriberInfo= new SubscriberInfo();
		subscriberInfo.setProductType("C");
		subscriberInfo.setSubscriberId("1234");
		
		when(mockedSubscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(anyString())).thenReturn(subscriberInfo);
		when(mockedSubscriberDaoFactory.getUpdateIdenPcsSubscriberDao(anyString())).thenReturn(mockedUpdateIdenPcsSubscriberDao);
		
		mockedImpl.setSubscriberPortIndicator("0123456789", new Date(), "sessionId");
		
		verify(mockedSubscriberLifecycleHelper).retrieveSubscriberByPhoneNumber(anyString());
		verify(mockedSubscriberDaoFactory).getUpdateIdenPcsSubscriberDao(anyString());
	}
	
	@Test
	public void testSnapBack() throws ApplicationException, TelusException{
		
		when(mockedSubscriberDaoFactory.getUpdateIdenPcsSubscriberDao(anyString())).thenReturn(mockedUpdateIdenPcsSubscriberDao);
		mockedImpl.snapBack("0123456789", "sessionId");
		verify(mockedSubscriberDaoFactory).getUpdateIdenPcsSubscriberDao(anyString());
		verify(mockedUpdateIdenPcsSubscriberDao).setPortTypeToSnapback(anyString(),anyString());
	}
	
	@Test
	public void testSuspendPortedInSubscriber() throws ApplicationException, TelusException{
		SubscriberInfo subscriberInfo= new SubscriberInfo();
		subscriberInfo.setProductType("C");
		subscriberInfo.setSubscriberId("1234");
		
		when(mockedSubscriberDaoFactory.getUpdateIdenPcsSubscriberDao(anyString())).thenReturn(mockedUpdateIdenPcsSubscriberDao);
		when(mockedSubscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(anyString())).thenReturn(subscriberInfo);

		mockedImpl.suspendPortedInSubscriber(8, "0123456789", "deactivationReason", new Date(), "portOutInd", "sessionId");
		verify(mockedSubscriberDaoFactory).getUpdateIdenPcsSubscriberDao(anyString());
		verify(mockedSubscriberLifecycleHelper).retrieveSubscriberByPhoneNumber(anyString());
		verify(mockedUpdateIdenPcsSubscriberDao).suspendPortedInSubscriber(anyInt(), anyString(),anyString(),any(Date.class), anyString(),anyString());
	}
	
	
	
	@Test	
	public void testChangeFaxNumber_1() throws ApplicationException, TelusException{

		//create subscriber info object
		SubscriberInfo subscriber=new SubscriberInfo();
		subscriber.setBanId(1);
		subscriber.setSubscriberId("123");
		subscriber.setPortType("I");
		
		//create numbergroupinfo object
		NumberGroupInfo numberGroupInfo=new NumberGroupInfo();
		numberGroupInfo.setNumberLocation("NoGroup");
		
		subscriber.setNumberGroup(numberGroupInfo);

		String sessionId=null;
		
		String[] stringArray = new String[]{"123"};
		when(mockedSubscriberDaoFactory.getUpdateIdenSubscriberDao()).thenReturn(mockedUpdateIdenSubscriberDao);
		when(mockedReferenceDataHelper.retrieveNpaNxxForMsisdnReservation(anyString(),anyBoolean())).thenReturn(stringArray);
		
		when(mockedUpdateSubscriberDao.retrieveAvailablePhoneNumbers(anyInt(), anyString(), 
				any(PhoneNumberReservationInfo.class), anyInt(), anyString())).thenReturn(new String[]{"2345432345","2345432345"});

		mockedImpl.changeFaxNumber(subscriber, sessionId);
		
		verify(mockedSubscriberDaoFactory).getUpdateSubscriberDao(anyString());
		verify(mockedSubscriberDaoFactory).getUpdateIdenSubscriberDao();
		verify(mockedUpdateSubscriberDao).retrieveAvailablePhoneNumbers(anyInt(), anyString(), 
				any(PhoneNumberReservationInfo.class), anyInt(), anyString());
		verify(mockedUpdateIdenSubscriberDao).changeFaxNumber(anyInt(),anyString(), anyString(),
				any(NumberGroupInfo.class),anyBoolean(), anyString());

	}
	
	
	@Test	
	public void testChangeAdditionalPhoneNumbersForPortIn() throws ApplicationException, TelusException{
		
		//create subscriber info object
		SubscriberInfo subscriber=new SubscriberInfo();
		subscriber.setBanId(1);
		subscriber.setSubscriberId("123");
		subscriber.setPortType("I");
		
		String sessionId=null;
		
		when(mockedSubscriberDaoFactory.getUpdateIdenSubscriberDao()).thenReturn(mockedUpdateIdenSubscriberDao);
		
		NumberGroupInfo numberGroup=new NumberGroupInfo();
		when(mockedReferenceDataHelper.retrieveNumberGroupByPhoneNumberProductType(anyString(),anyString())).thenReturn(numberGroup);
		
		mockedImpl.changeAdditionalPhoneNumbersForPortIn(subscriber, sessionId);
		
		verify(mockedSubscriberDaoFactory).getUpdateIdenSubscriberDao();
		verify(mockedUpdateIdenSubscriberDao).changeAdditionalPhoneNumbers(anyInt(),anyString(), anyString(),
				any(NumberGroupInfo.class),anyBoolean(), anyString());
	
	}
	
	
	@Test	
	public void testChangeAdditionalPhoneNumbers() throws ApplicationException, TelusException{
		
		//create subscriber info object
		SubscriberInfo subscriber=new SubscriberInfo();
		subscriber.setBanId(1);
		subscriber.setSubscriberId("123");
		subscriber.setPortType("I");
		subscriber.setProductType("I");
		subscriber.setPhoneNumber("1234543454");
		
		String sessionId=null;
		
		when(mockedSubscriberDaoFactory.getUpdateIdenSubscriberDao()).thenReturn(mockedUpdateIdenSubscriberDao);
		
		NumberGroupInfo numberGroup=new NumberGroupInfo();		
		when(mockedReferenceDataHelper.retrieveNumberGroupByPhoneNumberProductType(anyString(),anyString())).thenReturn(numberGroup);	
		mockedImpl.changeAdditionalPhoneNumbers(subscriber, sessionId);
		
		verify(mockedSubscriberDaoFactory).getUpdateIdenSubscriberDao();
		verify(mockedUpdateIdenSubscriberDao).changeAdditionalPhoneNumbers(anyInt(),anyString(), anyString(),
				any(NumberGroupInfo.class),anyBoolean(), anyString());
		
	}
	
	
	@Test	
	public void testMoveSubscriber() throws ApplicationException, TelusException{
		
		SubscriberInfo subscriber=new IDENSubscriberInfo();
		subscriber.setBanId(1);
		subscriber.setSubscriberId("123");
		subscriber.setPortType("I");
		subscriber.setProductType("I");
		subscriber.setPhoneNumber("1234543454");
		
		String sessionId=null;
		
		when(mockedSubscriberDaoFactory.getUpdateSubscriberDao(anyString())).thenReturn(mockedUpdateIdenSubscriberDao);
		
		NumberGroupInfo numberGroup=new NumberGroupInfo();
		when(mockedReferenceDataHelper.retrieveNumberGroupByPhoneNumberProductType(anyString(),anyString())).thenReturn(numberGroup);
		
		mockedImpl.moveSubscriber(subscriber, 8, new Date(), true, "ABC", "Moving subscriber", sessionId);		
		verify(mockedSubscriberDaoFactory).getUpdateSubscriberDao(anyString());
		verify(mockedUpdateIdenSubscriberDao).moveSubscriber(any(SubscriberInfo.class), anyInt(), any(Date.class),
				anyBoolean(), anyString(), anyString(), anyString(), anyString(),any(FleetInfo.class), anyString());

	}
	
	
	@Test
	public void testSuspendSubscriber() throws ApplicationException{
		
		SubscriberInfo subscriberInfo=new IDENSubscriberInfo();	
		FleetIdentityInfo fleetIdentityInfo = new FleetIdentityInfo(416, 668);
		String wildCard = "8646";		
		when(mockedSubscriberDaoFactory.getSubscriberDao()).thenReturn(mockedSubscriberDao);
		mockedImpl.suspendSubscriber(subscriberInfo, new Date(), "s1", "s2", "s3");
		verify(mockedSubscriberDaoFactory).getSubscriberDao();
		verify(mockedSubscriberDao).suspendSubscriber(any(SubscriberInfo.class), any(Date.class), anyString(), anyString(), anyString());

	}

	
	@Test
	public void testUpdatePortRestriction() throws ApplicationException{

		int ban=8;
		String subscriberNo="232422";	
		when(mockedSubscriberDaoFactory.getSubscriberDao()).thenReturn(mockedSubscriberDao);
		mockedImpl.updatePortRestriction(ban, subscriberNo, true, "s");
		verify(mockedSubscriberDaoFactory).getSubscriberDao();
		verify(mockedSubscriberDao).updatePortRestriction(anyInt(), anyString(), anyBoolean(), anyString());
		
	}
	
	@Test
	public void testUpdateSubscriber() throws ApplicationException{
		
		SubscriberInfo subscriberInfo=new IDENSubscriberInfo();	
		int ban=8;
		String subscriberNo="232422";	
		when(mockedSubscriberDaoFactory.getSubscriberDao()).thenReturn(mockedSubscriberDao);
		mockedImpl.updateSubscriber(subscriberInfo, "s");
		verify(mockedSubscriberDaoFactory).getSubscriberDao();
		verify(mockedSubscriberDao).updateSubscriber(any(SubscriberInfo.class), anyString());
		
	}
	
	@Test
	public void testUpdateSubscriptionRole() throws ApplicationException{
		
		when(mockedSubscriberDaoFactory.getSubscriberDao()).thenReturn(mockedSubscriberDao);
		mockedImpl.updateSubscriptionRole(8, "String", "String", "String", "String", "String");
		verify(mockedSubscriberDaoFactory).getSubscriberDao();
		verify(mockedSubscriberDao).updateSubscriptionRole(anyInt(), anyString(),anyString(), anyString(), anyString(), anyString());
		
	}
	
	@Test
	public void testAdjustCall() throws ApplicationException{
		
		mockit.Deencapsulation.setField(mockedImpl, "usageDao",
				mockedUsageDao);
		mockedImpl.adjustCall(8, "String", "String", 9, new Date(), "String", 8.8, "String", "String", "String", "String");	
		verify(mockedUsageDao).adjustCall(anyInt(), anyString(),anyString(), anyInt(), 
				any(Date.class),anyString(), anyDouble(), anyString(), anyString(), anyString(), anyString());
	
	}
	
	@Test
	public void testGetUsageProfileListsSummary() throws ApplicationException{
		
		UsageProfileListsSummaryInfo result = null;
		mockit.Deencapsulation.setField(mockedImpl, "usageDao",
				mockedUsageDao);
		result = mockedImpl.getUsageProfileListsSummary(9, "String", 9, "String", "String");
		System.out.println(result);

	}
	
	
	@Test
	public void testRetrieveBilledCallsList() throws ApplicationException{
		
		mockit.Deencapsulation.setField(mockedImpl, "usageDao",
				mockedUsageDao);
		mockedImpl.retrieveBilledCallsList(8, "String", "String", 8, 'c', new Date(), new Date(), true,"String");
		verify(mockedUsageDao).retrieveBilledCallsList(anyInt(), anyString(),anyString(), anyInt(), 
				anyChar(),any(Date.class), any(Date.class), anyBoolean(), anyString());
		
	}
	
	@Test
	public void testRetrieveCallDetails() throws ApplicationException{ 
	
		mockit.Deencapsulation.setField(mockedImpl, "usageDao",
				mockedUsageDao);
		mockedImpl.retrieveCallDetails(8, "String", "String", 8, new Date(), "String", "String", "String");
		verify(mockedUsageDao).retrieveCallDetails(anyInt(), anyString(),anyString(), anyInt(), 
				any(Date.class),anyString(), anyString(), anyString());
	
	}
	
	
	@Test
	public void testRetrieveUnbilledCallsList() throws ApplicationException{ 
	
		mockit.Deencapsulation.setField(mockedImpl, "usageDao",
				mockedUsageDao);
		mockedImpl.retrieveUnbilledCallsList(8, "String", "String", "String");
		verify(mockedUsageDao).retrieveUnbilledCallsList(anyInt(),anyString(), anyString(), any(Date.class),any(Date.class),anyBoolean(), anyString());

	}

	
	@Test
	public void testRetrieveUnbilledCallsList_1() throws ApplicationException{ 
	
		mockit.Deencapsulation.setField(mockedImpl, "usageDao",
				mockedUsageDao);
		mockedImpl.retrieveUnbilledCallsList(8, "String", "String",new Date(),new Date(),false, "String");
		verify(mockedUsageDao).retrieveUnbilledCallsList(anyInt(),anyString(), anyString(), any(Date.class),any(Date.class),anyBoolean(), anyString());

	}
	
	
	@Test
	public void testChangeEquipment() throws ApplicationException, TelusException{ 
	
		SubscriberInfo subscriberInfo=new SubscriberInfo();		
		SubscriberContractInfo subscriberContractInfo=new SubscriberContractInfo();		
		EquipmentInfo oldPrimaryEquipmentInfo=new EquipmentInfo();	
		EquipmentInfo[] newSecondaryEquipmentInfo=new EquipmentInfo[1];
		String dealerCode=null;
		String salesRepCode=null;
		String requestorId=null;
		String swapType=null; 
		String sessionId=null;
		PricePlanValidationInfo pricePlanValidation=null;
		EquipmentInfo newPrimaryEquipmentInfo = Mockito.mock(EquipmentInfo.class);

		
		NumberGroupInfo numberGroup=new NumberGroupInfo();
		when(newPrimaryEquipmentInfo.isIDEN()).thenReturn(true);
		when(mockedSubscriberDaoFactory.getUpdateSubscriberDao(anyString())).thenReturn(mockedUpdateSubscriberDao);
		when(mockedReferenceDataHelper.retrieveNumberGroupByPhoneNumberProductType(anyString(),anyString())).thenReturn(numberGroup);
		
		mockedImpl.changeEquipment(subscriberInfo, oldPrimaryEquipmentInfo, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo,
				dealerCode, salesRepCode, requestorId, swapType, subscriberContractInfo, pricePlanValidation, sessionId);
		
		verify(mockedSubscriberDaoFactory).getUpdateSubscriberDao(anyString());
		verify(mockedUpdateSubscriberDao).changeSerialNumberAndMaybePricePlan(any(SubscriberInfo.class),any(EquipmentInfo.class), 
				any(EquipmentInfo[].class), any(SubscriberContractInfo.class),anyString(),anyString(),
				any(PricePlanValidationInfo.class),anyString());
		verify(mockedSubscriberDaoFactory).getUpdateIdenSubscriberDao();
		verify(mockedUpdateIdenSubscriberDao).changeIMSI(anyInt(),anyString(),anyString());

	}
	
	
	@Test
	public void testReserveMemberId_1() throws ApplicationException {
		
		IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
		idenSubscriberInfo.setPhoneNumber("4166688646");
		String sessionId = "321";
		FleetIdentityInfo fleetIdentityInfo = new FleetIdentityInfo(416, 668);
		String wildCard = "8646";
		mockedImpl.reserveMemberId(idenSubscriberInfo, fleetIdentityInfo, wildCard, sessionId);
		verify(mockedFleetDao).reserveMemberId(idenSubscriberInfo, fleetIdentityInfo, wildCard,true, sessionId);
	}

}
