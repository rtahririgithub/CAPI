package com.telus.cmb.unit.test.subscriber.svc.facade;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.cmb.common.svc.matchers.ApplicationExceptionMatcher;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.DataSharingSocTransferInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionMSCSpendingInfo;

public class SLFDataSharingUnitTest extends SLFDataSharingUnitTestHelper {
	
	@Before
	public void startup() throws ApplicationException, TelusAPIException {
		MockitoAnnotations.initMocks(this);
		setupEJBs();		
		setupSubscriberInfo();
		setupAccountInfo();
		setupSubscriberContractInfo();
		setupSubscriberDataSharingDetailInfo();
		setupSubscriptionMSCResult();
	}
	
	@Test
	public void test_validate_data_sharing_with_invalid_account() throws ApplicationException {	
		List<AccountInfo> invalidAccounts = Arrays.asList(closedAccount, cancelledAccount, tentativeAccount);
		List<SubscriberInfo> validSubscribers = Arrays.asList(activeSubscriber, suspendedSubscriber);
		for (AccountInfo invalidAccount : invalidAccounts) {
			for (SubscriberInfo validSubscriber : validSubscribers) {
				when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(invalidAccount);	
				when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(validSubscriber);	
				assertNull(subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID));
			}
		}
	}
	
	@Test
	public void test_validate_data_sharing_with_invalid_subscriber() throws ApplicationException {	
		List<AccountInfo> validAccounts = Arrays.asList(openAccount, suspendedAccount);
		List<SubscriberInfo> invalidSubscribers = Arrays.asList(reservedSubscriber, cancelledSubscriber);	
		for (AccountInfo validAccount : validAccounts) {
			for (SubscriberInfo invalidSubscriber : invalidSubscribers) {	
				when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(validAccount);	
				when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(invalidSubscriber);	
				assertNull(subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID));
			}
		}
	}
	
	@Test
	public void test_validate_data_sharing_leaving_subscriber_has_no_data_sharing() throws ApplicationException, TelusException {		
		List<AccountInfo> validAccounts = Arrays.asList(openAccount, suspendedAccount);
		List<SubscriberInfo> validSubscribers = Arrays.asList(activeSubscriber, suspendedSubscriber);
		for (AccountInfo validAccount : validAccounts) {
			for (SubscriberInfo validSubscriber : validSubscribers) {
				when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(validAccount);	
				when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(validSubscriber);
				doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(validSubscriber, validAccount);
				when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(noDataSharingServices);
				when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);				
				assertNull(subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID));
			}
		}
	}
	
	@Test
	public void test_validate_data_sharing_leaving_subscriber_is_accessor() throws ApplicationException, TelusException {		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(activeSubscriber);	
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(activeSubscriber, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(accessingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		assertNull(subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID));
	}
	
	@Test
	public void test_validate_data_sharing_leaving_subscriber_multiple_contributing_data_sharing_groups_exception() throws ApplicationException, TelusException {		
		expectedException.expect(ApplicationException.class);	
		expectedException.expect(ApplicationExceptionMatcher.hasErrorCode(ErrorCodes.MULTIPLE_CONTRIBUTING_DATA_SHARING_GROUPS));
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(activeSubscriber);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(activeSubscriber, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(multipleContributingServices);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);	
		subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);		
	}
	
	@Test
	public void test_validate_data_sharing_leaving_subscriber_account_type_out_of_scope() throws ApplicationException, TelusException {
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(activeSubscriber);
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(outOfScopeAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(activeSubscriber, outOfScopeAccount);	
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);	
		assertNull(subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID));		
	}
	
	@Test
	public void test_validate_data_sharing_leaving_subscriber_data_sharing_out_of_scope() throws ApplicationException, TelusException {
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(activeSubscriber);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(activeSubscriber, openAccount);	
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(usDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);		
		assertNull(subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID));		
	}
	
	@Test
	public void test_validate_data_sharing_leaving_pricing_group_reference_ejb_exception() throws ApplicationException, TelusException {		
		expectedException.expect(ApplicationException.class);	
		expectedException.expect(ApplicationExceptionMatcher.hasErrorCode(ErrorCodes.REFERENCE_DATA_ERROR));
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(activeSubscriber);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(activeSubscriber, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(noDataSharingServices);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(dataSharingPricePlanContributorGroup1);		
		when(referenceDataFacade.getDataSharingPricingGroups()).thenThrow(new TelusException("Reference EJB Down"));
		subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);		
	}
	
	@Test
	public void test_validate_data_sharing_leaving_regular_service_reference_ejb_exception() throws ApplicationException, TelusException {		
		expectedException.expect(ApplicationException.class);	
		expectedException.expect(ApplicationExceptionMatcher.hasErrorCode(ErrorCodes.REFERENCE_DATA_ERROR));
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(activeSubscriber);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(activeSubscriber, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenThrow(new TelusException("Reference EJB Down"));				
		subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);		
	}
	
	@Test
	public void test_validate_data_sharing_leaving_subscriber_brand_out_of_scope() throws ApplicationException, TelusException {
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(koodoSubscriber);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(koodoSubscriber, openAccount);	
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);		
		assertNull(subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID));		
	}
	
	@Test
	public void test_validate_data_sharing_leaving_subscriber_only_one_in_data_sharing_group() throws ApplicationException, TelusException {
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1};
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(activeSubscriber);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(activeSubscriber, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		assertNull(subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID));		
	}
	
	@Test
	public void test_validate_data_sharing_one_contributor_one_accessor() throws ApplicationException, TelusException {
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, accessor1Group1};
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subAccessor1Group1});
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);
		
		DataSharingSocTransferInfo socTransferInfo = subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
		verifyDataSharingSocTransferInfo(socTransferInfo, ACCESSOR1_GROUP1_ID, ACCESSOR_SOC_1, ACCESSOR_SOC_1, CONTRIBUTOR_SOC_1, CONTRIBUTOR_SOC_1);
	}
	
	@Test
	public void test_validate_data_sharing_one_contributor_one_non_contract_accessor() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, accessor3Group1};	
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subAccessor3Group1});
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);
		
		DataSharingSocTransferInfo socTransferInfo = subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
		verifyDataSharingSocTransferInfo(socTransferInfo, ACCESSOR3_GROUP1_ID, ACCESSOR_SOC_2, ACCESSOR_SOC_2, CONTRIBUTOR_SOC_1, CONTRIBUTOR_SOC_1);
	}
	
	@Test
	public void test_validate_data_sharing_one_contributor_two_accessors() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, accessor1Group1, accessor2Group1};
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subAccessor1Group1, subAccessor2Group1});
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);
		
		DataSharingSocTransferInfo socTransferInfo = subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
		verifyDataSharingSocTransferInfo(socTransferInfo, ACCESSOR2_GROUP1_ID, ACCESSOR_SOC_2, ACCESSOR_SOC_2, CONTRIBUTOR_SOC_1, CONTRIBUTOR_SOC_1);
	}
	
	@Test
	public void test_validate_data_sharing_one_contributor_two_accessors_ds_price_plan_exception() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {accessor1Group1, contributor2Group1, accessor2Group1};	
		expectedException.expect(ApplicationException.class);	
		expectedException.expect(ApplicationExceptionMatcher.hasErrorCode(ErrorCodes.CONTRIBUTING_SOC_IS_PRICE_PLAN));
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor2Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor2Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(noDataSharingServices);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(dataSharingPricePlanContributorGroup1);		
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR2_GROUP1_ID);
	}
	
	@Test
	public void test_validate_data_sharing_two_contributors_one_accessor_msc_unbroken() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, contributor2Group1, accessor1Group1};	
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(activeSubscriber);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(activeSubscriber, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(penaltyCalculationServiceDao.validateSubscriptionMSCList(anyListOf(SubscriptionMSCSpendingInfo.class))).thenReturn(maintainedMSCResult);
		assertNull(subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID));
	}
	
	@Test
	public void test_validate_data_sharing_two_contributors_one_accessor_msc_broken() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, contributor2Group1, accessor1Group1};	
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subContributor2Group1, subAccessor1Group1});
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(penaltyCalculationServiceDao.validateSubscriptionMSCList(anyListOf(SubscriptionMSCSpendingInfo.class))).thenReturn(brokenMSCResult);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);

		DataSharingSocTransferInfo socTransferInfo = subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
		verifyDataSharingSocTransferInfo(socTransferInfo, ACCESSOR1_GROUP1_ID, ACCESSOR_SOC_1, ACCESSOR_SOC_1, CONTRIBUTOR_SOC_1, CONTRIBUTOR_SOC_1);
	}
	
	@Test
	public void test_validate_data_sharing_two_contributors_two_accessors_msc_broken() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, contributor2Group1, accessor1Group1, accessor2Group1};	
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subContributor2Group1, subAccessor1Group1, subAccessor2Group1});
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(penaltyCalculationServiceDao.validateSubscriptionMSCList(anyListOf(SubscriptionMSCSpendingInfo.class))).thenReturn(brokenMSCResult);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);

		DataSharingSocTransferInfo socTransferInfo = subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
		verifyDataSharingSocTransferInfo(socTransferInfo, ACCESSOR2_GROUP1_ID, ACCESSOR_SOC_2, ACCESSOR_SOC_2, CONTRIBUTOR_SOC_1, CONTRIBUTOR_SOC_1);
	}
	
	@Test
	public void test_validate_data_sharing_two_contributors_two_month_to_month_accessors_msc_broken() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, contributor2Group1, accessor3Group1, accessor4Group1};	
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subContributor2Group1, subAccessor3Group1, subAccessor4Group1});
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(penaltyCalculationServiceDao.validateSubscriptionMSCList(anyListOf(SubscriptionMSCSpendingInfo.class))).thenReturn(brokenMSCResult);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);

		DataSharingSocTransferInfo socTransferInfo = subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
		verifyDataSharingSocTransferInfo(socTransferInfo, ACCESSOR4_GROUP1_ID, ACCESSOR_SOC_1, ACCESSOR_SOC_1, CONTRIBUTOR_SOC_1, CONTRIBUTOR_SOC_1);
	}
	
	@Test
	public void test_validate_data_sharing_two_contributors_mixed_accessors_msc_broken() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, contributor2Group1, accessor1Group1, accessor3Group1};	
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subContributor2Group1, subAccessor1Group1, subAccessor3Group1});
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(penaltyCalculationServiceDao.validateSubscriptionMSCList(anyListOf(SubscriptionMSCSpendingInfo.class))).thenReturn(brokenMSCResult);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);
		
		DataSharingSocTransferInfo socTransferInfo = subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
		verifyDataSharingSocTransferInfo(socTransferInfo, ACCESSOR1_GROUP1_ID, ACCESSOR_SOC_1, ACCESSOR_SOC_1, CONTRIBUTOR_SOC_1, CONTRIBUTOR_SOC_1);
	}
	
	@Test
	public void test_validate_data_sharing_two_contributors_no_accessors_msc_unbroken() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, contributor2Group1};	
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(activeSubscriber);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(activeSubscriber, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(penaltyCalculationServiceDao.validateSubscriptionMSCList(anyListOf(SubscriptionMSCSpendingInfo.class))).thenReturn(maintainedMSCResult);
		assertNull(subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID));
	}
	
	@Test
	public void test_validate_data_sharing_two_contributors_no_accessors_msc_broken_exception() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, contributor2Group1};	
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subContributor2Group1});
		expectedException.expect(ApplicationException.class);	
		expectedException.expect(ApplicationExceptionMatcher.hasErrorCode(ErrorCodes.NO_AVAILABLE_ACCESSORS));
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(penaltyCalculationServiceDao.validateSubscriptionMSCList(anyListOf(SubscriptionMSCSpendingInfo.class))).thenReturn(brokenMSCResult);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);
		subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
	}
	
	@Test
	public void test_validate_data_sharing_two_contributors_price_plan_accessor_and_soc_accessor_msc_broken() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, contributor2Group1, accessor2Group1, accessor5Group1};	
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subContributor2Group1, subAccessor2Group1, subAccessor5Group1});		
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(penaltyCalculationServiceDao.validateSubscriptionMSCList(anyListOf(SubscriptionMSCSpendingInfo.class))).thenReturn(brokenMSCResult);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);
		
		DataSharingSocTransferInfo socTransferInfo = subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
		verifyDataSharingSocTransferInfo(socTransferInfo, ACCESSOR2_GROUP1_ID, ACCESSOR_SOC_2, ACCESSOR_SOC_2, CONTRIBUTOR_SOC_1, CONTRIBUTOR_SOC_1);
	}
	
	@Test
	public void test_validate_data_sharing_two_contributors_price_plan_accessor_msc_broken_exception() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, contributor2Group1, accessor5Group1};	
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subContributor2Group1, subAccessor5Group1});
		expectedException.expect(ApplicationException.class);	
		expectedException.expect(ApplicationExceptionMatcher.hasErrorCode(ErrorCodes.NO_AVAILABLE_ACCESSORS));
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(penaltyCalculationServiceDao.validateSubscriptionMSCList(anyListOf(SubscriptionMSCSpendingInfo.class))).thenReturn(brokenMSCResult);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);
		subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
	}
	
	@Test
	public void test_validate_data_sharing_two_contributors_suspended_accessor_msc_broken_exception() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, contributor2Group1, accessor6Group1};	
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subContributor2Group1, subAccessor6Group1});
		expectedException.expect(ApplicationException.class);	
		expectedException.expect(ApplicationExceptionMatcher.hasErrorCode(ErrorCodes.NO_AVAILABLE_ACCESSORS));
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(penaltyCalculationServiceDao.validateSubscriptionMSCList(anyListOf(SubscriptionMSCSpendingInfo.class))).thenReturn(brokenMSCResult);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);
		subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
	}
	
	@Test
	public void test_validate_data_sharing_two_contributors_mixed_active_suspended_accessors_msc_broken() throws ApplicationException, TelusException {	
		SubscriberDataSharingDetailInfo[] dataSharingAccount = new SubscriberDataSharingDetailInfo[] {contributor1Group1, contributor2Group1, accessor1Group1, accessor6Group1};	
		Collection<SubscriberInfo> subscribers = Arrays.asList(new SubscriberInfo[]{subContributor1Group1, subContributor2Group1, subAccessor1Group1, subAccessor6Group1});
		
		when(subscriberLifecycleHelper.retrieveSubscriber(anyString())).thenReturn(subContributor1Group1);		
		when(accountInformationHelper.retrieveAccountByBan(anyInt(), Account.ACCOUNT_LOAD_ALL)).thenReturn(openAccount);
		doReturn(subscriberContract).when(subscriberLifecycleFacade).getServiceAgreement(subContributor1Group1, openAccount);
		when(referenceDataFacade.getRegularServices(getAnyStringArray())).thenReturn(contributingDataSharingService);
		when(referenceDataFacade.getPricePlan(anyString())).thenReturn(regularPricePlan);			
		when(accountInformationHelper.retrieveSubscriberDataSharingInfoList(anyInt(), getAnyStringArray())).thenReturn(dataSharingAccount);
		when(penaltyCalculationServiceDao.validateSubscriptionMSCList(anyListOf(SubscriptionMSCSpendingInfo.class))).thenReturn(brokenMSCResult);
		when(subscriberLifecycleHelper.retrieveSubscriberListByBAN(anyInt(), anyInt())).thenReturn(subscribers);
		
		DataSharingSocTransferInfo socTransferInfo = subscriberLifecycleFacade.validateDataSharingBeforeCancelSubscriber(CONTRIBUTOR1_GROUP1_ID);
		verifyDataSharingSocTransferInfo(socTransferInfo, ACCESSOR1_GROUP1_ID, ACCESSOR_SOC_1, ACCESSOR_SOC_1, CONTRIBUTOR_SOC_1, CONTRIBUTOR_SOC_1);
	}
	
}
