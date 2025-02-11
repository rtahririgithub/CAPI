package com.telus.cmb.unit.test.subscriber.svc.facade;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.util.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.reference.SeatType;
import com.telus.api.resource.Resource;
import com.telus.cmb.common.dao.provisioning.RequestParamNameConstants;
import com.telus.cmb.common.dao.provisioning.WirelessProvisioningServiceRequestFactory;
import com.telus.cmb.subscriber.utilities.BusinessConnectUtil;
import com.telus.cmb.unit.test.subscriber.svc.facade.ServiceEditionConstants.ServiceEdition;
import com.telus.eas.utility.info.ProvisioningRequestInfo;

public class SLFBusinessConnectUnitTest extends SLFBusinessConnectUnitTestHelper {

	@Before
	public void startup() throws ApplicationException, TelusAPIException {
		MockitoAnnotations.initMocks(this);		
		when(starterSeatData.getSeatType()).thenReturn(SeatType.SEAT_TYPE_STARTER);
		when(officeSeatData.getSeatType()).thenReturn(SeatType.SEAT_TYPE_OFFICE);
		setupAccount();
		setupVOIPAccount();
		setupSubscriber();		
		setupResources();
		setupServices();
		setupServiceEditions();
	}
		
	@SuppressWarnings("unchecked")
	@Test
	public void test_create_primary_starter_seat_subscriber_with_no_toll_free() throws ApplicationException {	
		when(starterSeatData.getResources()).thenReturn(new Resource[]{primaryResource, otherResource});
		ProvisioningRequestInfo provisioningRequest = WirelessProvisioningServiceRequestFactory.createAccountAddRequest(bcAccount, bcSubscriber, bcSubscriber.getStartServiceDate(), bcAddress);
		Map<String, String> requestParams = provisioningRequest.getRequestParams();
		assertNull(requestParams.get(RequestParamNameConstants.MAIN_VOIP_PHONE_NUMBER));
		assertThat(requestParams.get(RequestParamNameConstants.VOIP_PHONE_NUMBER_LIST), equalTo(primaryResourceNumber));
		assertThat(requestParams.get(RequestParamNameConstants.VOIP_PHONE_TYPE_LIST), equalTo(RequestParamNameConstants.VOIP_PHONE_TYPE_VOICEFAX));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_create_primary_starter_seat_subscriber_with_one_toll_free() throws ApplicationException {	
		when(starterSeatData.getResources()).thenReturn(new Resource[]{primaryResource, tollFreeResource1});
		ProvisioningRequestInfo provisioningRequest = WirelessProvisioningServiceRequestFactory.createAccountAddRequest(bcAccount, bcSubscriber, bcSubscriber.getStartServiceDate(), bcAddress);
		Map<String, String> requestParams = provisioningRequest.getRequestParams();
		assertThat(requestParams.get(RequestParamNameConstants.MAIN_VOIP_PHONE_NUMBER), equalTo(tollFreeResource1Number));		
		assertThat(requestParams.get(RequestParamNameConstants.VOIP_PHONE_NUMBER_LIST), equalTo(primaryResourceNumber));
		assertThat(requestParams.get(RequestParamNameConstants.VOIP_PHONE_TYPE_LIST), equalTo(RequestParamNameConstants.VOIP_PHONE_TYPE_VOICEFAX));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_create_primary_starter_seat_subscriber_with_multiple_toll_free() throws ApplicationException {
		when(starterSeatData.getResources()).thenReturn(new Resource[]{primaryResource, tollFreeResource2, additionalVoipResource, tollFreeResource1, otherResource});
		List<String> voipNumberList = new ArrayList<String>();
		List<String> voipTypeList = new ArrayList<String>();
		voipNumberList.add(primaryResourceNumber);
		voipTypeList.add(RequestParamNameConstants.VOIP_PHONE_TYPE_VOICEFAX);
		voipNumberList.add(additionalVoipResourceNumber);
		voipTypeList.add(RequestParamNameConstants.VOIP_PHONE_TYPE_VOICEFAX);
		voipNumberList.add(tollFreeResource1Number);
		voipTypeList.add(RequestParamNameConstants.VOIP_PHONE_TYPE_VOICEFAX);
		ProvisioningRequestInfo provisioningRequest = WirelessProvisioningServiceRequestFactory.createAccountAddRequest(bcAccount, bcSubscriber, bcSubscriber.getStartServiceDate(), bcAddress);
		Map<String, String> requestParams = provisioningRequest.getRequestParams();
		assertThat(requestParams.get(RequestParamNameConstants.MAIN_VOIP_PHONE_NUMBER), equalTo(tollFreeResource2Number));
		assertThat(requestParams.get(RequestParamNameConstants.VOIP_PHONE_NUMBER_LIST), equalTo(StringUtils.collectionToCommaDelimitedString(voipNumberList)));
		assertThat(requestParams.get(RequestParamNameConstants.VOIP_PHONE_TYPE_LIST), equalTo(StringUtils.collectionToCommaDelimitedString(voipTypeList)));
	}
	
	@Test
	public void test_isBusinessConnectPrimaryStarterSeatActivation_happy_path() {
		assertThat(BusinessConnectUtil.isBusinessConnectPrimaryStarterSeatActivation(bcSubscriber, bcAccount), equalTo(true));
	}

	@Test
	public void test_isBusinessConnectPrimaryStarterSeatActivation_with_reserved_subscribers() {
		when(bcAccount.getReservedSubscribersCount()).thenReturn(1);
		assertThat(BusinessConnectUtil.isBusinessConnectPrimaryStarterSeatActivation(bcSubscriber, bcAccount), equalTo(false));
	}

	@Test
	public void test_isBusinessConnectPrimaryStarterSeatActivation_with_active_subscribers() {
		when(bcAccount.getReservedSubscribersCount()).thenReturn(100);
		assertThat(BusinessConnectUtil.isBusinessConnectPrimaryStarterSeatActivation(bcSubscriber, bcAccount), equalTo(false));
	}

	@Test
	public void test_isBusinessConnectPrimaryStarterSeatActivation_with_active_account() {
		when(bcAccount.getStatus()).thenReturn(Account.STATUS_OPEN);
		assertThat(BusinessConnectUtil.isBusinessConnectPrimaryStarterSeatActivation(bcSubscriber, bcAccount), equalTo(false));
	}

	@Test
	public void test_isBusinessConnectPrimaryStarterSeatActivation_with_office_seat() {
		when(bcSubscriber.getSeatData()).thenReturn(officeSeatData);
		assertThat(BusinessConnectUtil.isBusinessConnectPrimaryStarterSeatActivation(bcSubscriber, bcAccount), equalTo(false));
	}

	@Test
	public void test_isBusinessConnectPrimaryStarterSeatActivation_non_bc_subscriber() {
		when(bcSubscriber.getSeatData()).thenReturn(null);
		assertThat(BusinessConnectUtil.isBusinessConnectPrimaryStarterSeatActivation(bcSubscriber, bcAccount), equalTo(false));
	}

	@Test
	public void test_isBusinessConnectPrimaryStarterSeat_matching_subscription_ids() throws ApplicationException {
		when(voipSupplementaryServiceDao.getPrimaryStarterSeatSubscriptionId(anyInt())).thenReturn(String.valueOf(subscriptionId1));
		assertThat(BusinessConnectUtil.isBusinessConnectPrimaryStarterSeat(bcSubscriber, voipSupplementaryServiceDao), equalTo(true));
	}

	@Test
	public void test_isBusinessConnectPrimaryStarterSeat_subscription_ids_dont_match() throws ApplicationException {
		when(voipSupplementaryServiceDao.getPrimaryStarterSeatSubscriptionId(anyInt())).thenReturn(String.valueOf(subscriptionId2));
		assertThat(BusinessConnectUtil.isBusinessConnectPrimaryStarterSeat(bcSubscriber, voipSupplementaryServiceDao), equalTo(false));
	}

	@Test
	public void test_isBusinessConnectPrimaryStarterSeat_not_bc_subscriber() throws ApplicationException {
		when(bcSubscriber.getSeatData()).thenReturn(null);
		assertThat(BusinessConnectUtil.isBusinessConnectPrimaryStarterSeat(bcSubscriber, voipSupplementaryServiceDao), equalTo(false));
	}

	@Test
	public void test_isBusinessConnectPrimaryStarterSeat_exception_path() throws ApplicationException {
		expectedException.expect(ApplicationException.class);
		when(voipSupplementaryServiceDao.getPrimaryStarterSeatSubscriptionId(anyInt())).thenThrow(new ApplicationException("", "", ""));
		BusinessConnectUtil.isBusinessConnectPrimaryStarterSeat(bcSubscriber, voipSupplementaryServiceDao);
	}
	
	@Test
	public void test_getActivityDateFromPricePlan_regular() {		
		when(subscriberContractInfo.getPricePlan0()).thenReturn(enterpriseNationalPricePlan);
		Date currentTimestamp = Calendar.getInstance().getTime();
		assertThat(BusinessConnectUtil.getActivityDateFromPricePlan(subscriberContractInfo, currentTimestamp), equalTo(currentTimestamp));
	}

	@Test
	public void test_getActivityDateFromPricePlan_future_dated() {
		Calendar c = Calendar.getInstance();
		Date currentTimestamp = c.getTime();
		c.add(Calendar.YEAR, 1);
		Date futureTimestamp = c.getTime();	
		when(subscriberContractInfo.getPricePlan0()).thenReturn(enterpriseNationalPricePlan);
		when(enterpriseNationalPricePlan.getEffectiveDate()).thenReturn(futureTimestamp);		
		assertThat(BusinessConnectUtil.getActivityDateFromPricePlan(subscriberContractInfo, currentTimestamp), equalTo(futureTimestamp));
	}
	
	@Test
	public void test_getVOIPServiceEditionFromPricePlan_Quebec_standard() throws ApplicationException {		
		when(subscriberContractInfo.getPricePlan0()).thenReturn(standardQuebecPricePlan);
		assertThat(BusinessConnectUtil.getVOIPServiceEditionFromPricePlan(subscriberContractInfo, serviceEditions), equalTo(ServiceEdition.QUEBEC_STANDARD.getCode()));
	}
	
	@Test
	public void test_getVOIPServiceEditionFromPricePlan_national_standard() throws ApplicationException {		
		when(subscriberContractInfo.getPricePlan0()).thenReturn(standardNationalPricePlan);
		assertThat(BusinessConnectUtil.getVOIPServiceEditionFromPricePlan(subscriberContractInfo, serviceEditions), equalTo(ServiceEdition.NATIONAL_STANDARD.getCode()));
	}
		
	@Test
	public void test_getVOIPServiceEditionFromPricePlan_Quebec_premium() throws ApplicationException {		
		when(subscriberContractInfo.getPricePlan0()).thenReturn(premiumQuebecPricePlan);
		assertThat(BusinessConnectUtil.getVOIPServiceEditionFromPricePlan(subscriberContractInfo, serviceEditions), equalTo(ServiceEdition.QUEBEC_PREMIUM.getCode()));
	}
	
	@Test
	public void test_getVOIPServiceEditionFromPricePlan_national_premium() throws ApplicationException {		
		when(subscriberContractInfo.getPricePlan0()).thenReturn(premiumNationalPricePlan);
		assertThat(BusinessConnectUtil.getVOIPServiceEditionFromPricePlan(subscriberContractInfo, serviceEditions), equalTo(ServiceEdition.NATIONAL_PREMIUM.getCode()));
	}
	
	@Test
	public void test_getVOIPServiceEditionFromPricePlan_Quebec_enterprise() throws ApplicationException {		
		when(subscriberContractInfo.getPricePlan0()).thenReturn(enterpriseQuebecPricePlan);
		assertThat(BusinessConnectUtil.getVOIPServiceEditionFromPricePlan(subscriberContractInfo, serviceEditions), equalTo(ServiceEdition.QUEBEC_ENTERPRISE.getCode()));
	}
	
	@Test
	public void test_getVOIPServiceEditionFromPricePlan_national_enterprise() throws ApplicationException {		
		when(subscriberContractInfo.getPricePlan0()).thenReturn(enterpriseNationalPricePlan);
		assertThat(BusinessConnectUtil.getVOIPServiceEditionFromPricePlan(subscriberContractInfo, serviceEditions), equalTo(ServiceEdition.NATIONAL_ENTERPRISE.getCode()));
	}
	
	@Test
	public void test_getVOIPServiceEditionFromPricePlan_non_bc_price_plan() throws ApplicationException {
		expectedException.expect(ApplicationException.class);		
		when(subscriberContractInfo.getPricePlan0()).thenReturn(nonBusinessConnectPricePlan);
		assertThat(BusinessConnectUtil.getVOIPServiceEditionFromPricePlan(subscriberContractInfo, serviceEditions), equalTo(ServiceEdition.NATIONAL_ENTERPRISE.getCode()));
	}
	
	@Test
	public void test_validateServiceEditionChange_true() throws ApplicationException {			
		expectedException.expect(ApplicationException.class);	
		BusinessConnectUtil.validateServiceEditionChange(ServiceEdition.NATIONAL_STANDARD.getCode(), ServiceEdition.NATIONAL_STANDARD.getCode());
	}
	
	@Test
	public void test_validateServiceEditionChange_null() throws ApplicationException {	
		BusinessConnectUtil.validateServiceEditionChange(ServiceEdition.NATIONAL_STANDARD.getCode(), null);
	}
	
	@Test
	public void test_validateServiceEditionChange_false() throws ApplicationException {
		BusinessConnectUtil.validateServiceEditionChange(ServiceEdition.NATIONAL_STANDARD.getCode(), ServiceEdition.NATIONAL_PREMIUM.getCode());
	}	
	
	@Test
	public void test_isBusinessConnectStarterSeat() {
		assertThat(BusinessConnectUtil.isBusinessConnectStarterSeat(bcSubscriber), equalTo(true));
	}
	
	@Test
	public void test_isBusinessConnectPrimaryStarterSeat() throws ApplicationException {	
		assertThat(BusinessConnectUtil.isBusinessConnectPrimaryStarterSeat(bcSubscriber, voipAccount), equalTo(true));
	}
	
}