package com.telus.cmb.unit.test.subscriber.svc.facade;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mock;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.ContractService;
import com.telus.api.account.SubscriberDataSharingDetail.DataSharingDetail;
import com.telus.api.account.SubscriberDataSharingDetail.DataSharingSoc;
import com.telus.api.account.SubscriberDataSharingDetail.NonDataSharingRegularSoc;
import com.telus.api.reference.ServiceDataSharingGroup;
import com.telus.api.reference.ServiceSummary;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.DataSharingSocTransferInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionMSCResultInfo;
import com.telus.eas.utility.info.BrandInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class SLFDataSharingUnitTestHelper extends SLFBaseUnitTestHelper {

	@Mock protected SubscriberInfo activeSubscriber;
	@Mock protected SubscriberInfo suspendedSubscriber;
	@Mock protected SubscriberInfo reservedSubscriber;
	@Mock protected SubscriberInfo cancelledSubscriber;
	@Mock protected SubscriberInfo koodoSubscriber;
	
	@Mock protected AccountInfo openAccount;
	@Mock protected AccountInfo suspendedAccount;
	@Mock protected AccountInfo tentativeAccount;
	@Mock protected AccountInfo cancelledAccount;
	@Mock protected AccountInfo closedAccount;
	@Mock protected AccountInfo outOfScopeAccount;
	
	@Mock protected SubscriberContractInfo subscriberContract;
	
	@Mock protected ServiceInfo regularService;
	@Mock protected ServiceInfo contributorServiceGroup1;
	@Mock protected ServiceInfo contributorServiceGroup2;
	@Mock protected ServiceInfo accessorServiceGroup1;
	@Mock protected ServiceInfo accessorServiceGroup2;
	
	@Mock protected PricePlanInfo regularPricePlan;
	@Mock protected PricePlanInfo dataSharingPricePlanContributorGroup1;
	@Mock protected PricePlanInfo dataSharingPricePlanContributorGroup2;
	@Mock protected PricePlanInfo dataSharingPricePlanAccessorGroup1;
	@Mock protected PricePlanInfo dataSharingPricePlanAccessorGroup2;
	
	@Mock protected ServiceDataSharingGroup contributorDataSharingGroup1;
	@Mock protected ServiceDataSharingGroup contributorDataSharingGroup2;
	@Mock protected ServiceDataSharingGroup accessorDataSharingGroup1;
	@Mock protected ServiceDataSharingGroup accessorDataSharingGroup2;
	
	@Mock protected SubscriberInfo subContributor1Group1;
	@Mock protected SubscriberInfo subContributor2Group1;
	@Mock protected SubscriberInfo subContributor1Group2;	
	@Mock protected SubscriberInfo subAccessor1Group1;
	@Mock protected SubscriberInfo subAccessor2Group1;
	@Mock protected SubscriberInfo subAccessor3Group1;
	@Mock protected SubscriberInfo subAccessor4Group1;
	@Mock protected SubscriberInfo subAccessor5Group1;
	@Mock protected SubscriberInfo subAccessor6Group1;
	@Mock protected SubscriberInfo subAccessor1Group2;
	
	@Mock protected SubscriberDataSharingDetailInfo contributor1Group1;
	@Mock protected SubscriberDataSharingDetailInfo contributor2Group1;
	@Mock protected SubscriberDataSharingDetailInfo contributor1Group2;	
	@Mock protected SubscriberDataSharingDetailInfo accessor1Group1;
	@Mock protected SubscriberDataSharingDetailInfo accessor2Group1;
	@Mock protected SubscriberDataSharingDetailInfo accessor3Group1;
	@Mock protected SubscriberDataSharingDetailInfo accessor4Group1;
	@Mock protected SubscriberDataSharingDetailInfo accessor5Group1;
	@Mock protected SubscriberDataSharingDetailInfo accessor6Group1;
	@Mock protected SubscriberDataSharingDetailInfo accessor1Group2;
	
	@Mock protected DataSharingDetail contributorSoc1Group1;
	@Mock protected DataSharingDetail contributorSoc1Group2;
	@Mock protected DataSharingDetail contributorSoc2Group1;
	@Mock protected DataSharingDetail contributorSoc2Group2;
	@Mock protected DataSharingDetail contributorNonMSCSocGroup1;
	@Mock protected DataSharingDetail contributorNonMSCSocGroup2;
	@Mock protected DataSharingDetail accessorSoc1Group1;
	@Mock protected DataSharingDetail accessorSoc1Group2;
	@Mock protected DataSharingDetail accessorSoc2Group1;
	@Mock protected DataSharingDetail accessorSoc2Group2;
	@Mock protected DataSharingDetail accessorSocNonMSCSocGroup1;
	@Mock protected DataSharingDetail accessorSocNonMSCSocGroup2;
	@Mock protected DataSharingDetail contributorPricePlanGroup1;
	@Mock protected DataSharingDetail accessorPricePlanGroup1;	
	
	@Mock protected DataSharingSoc contributorSoc1;
	@Mock protected DataSharingSoc contributorSoc2;
	@Mock protected DataSharingSoc contributorNonMSCSoc;
	@Mock protected DataSharingSoc accessorSoc1;
	@Mock protected DataSharingSoc accessorSoc2;
	@Mock protected DataSharingSoc accessorSocNonMSCSoc;
	@Mock protected DataSharingSoc contributorPricePlan;
	@Mock protected DataSharingSoc accessorPricePlan;
	@Mock protected NonDataSharingRegularSoc regularSoc1;
	@Mock protected NonDataSharingRegularSoc regularSoc2;
	@Mock protected NonDataSharingRegularSoc regularNonMSCSoc;
	
	protected static final String DATA_SHARING_GROUP_1 = "CAD_DATA";
	protected static final String DATA_SHARING_GROUP_2 = "US_DATA";
	protected static final String DATA_SHARING_GROUP_3 = "CAD_DATA2013";
	protected static final String CONTRIBUTOR1_GROUP1_ID = "4161111111";
	protected static final String CONTRIBUTOR2_GROUP1_ID = "4162222222";
	protected static final String CONTRIBUTOR1_GROUP2_ID = "4163333333";
	protected static final String ACCESSOR1_GROUP1_ID = "4164444444";
	protected static final String ACCESSOR2_GROUP1_ID = "4165555555";
	protected static final String ACCESSOR3_GROUP1_ID = "4166666666";
	protected static final String ACCESSOR4_GROUP1_ID = "4166667777";
	protected static final String ACCESSOR5_GROUP1_ID = "4166668888";
	protected static final String ACCESSOR6_GROUP1_ID = "4166669999";
	protected static final String ACCESSOR1_GROUP2_ID = "4167777777";
	protected static final String MIXED_CONTRIBUTOR1_ID = "4168888888";
	protected static final String MIXED_CONTRIBUTOR2_ID = "4169999999";
	protected static final String MIXED_ACCESSOR1_ID = "4167654321";
	protected static final String MIXED_ACCESSOR2_ID = "4161234567";
	protected static final String ACCT_SUB_TYPE_IR = "IR";
	protected static final String ACCT_SUB_TYPE_BC = "BC";
	protected static final Date FAR_INTO_THE_FUTURE = getDate(2063, 4, 5);
	protected static final Date BLAST_FROM_THE_PAST = getDate(1999, 12, 31);
	protected static final Date PI_DAY = getDate(2015, 3, 14);
	protected static final Date LEAFS_WIN_CUP = getDate(1967, 5, 2);
	protected static final Date OLYMPICS_JAPAN = getDate(2020, 7, 24);
	protected static final Date LEAPPING_YEARS = getDate(2016, 2, 29);
	protected static final Date TODAY = Calendar.getInstance().getTime();
	protected static final String CONTRIBUTOR_SOC_1 = "CONTRIBR1";
	protected static final String CONTRIBUTOR_SOC_2 = "CONTRIBR2";
	protected static final String CONTRIBUTOR_SOC_NONMSC = "CONTRIBR3";
	protected static final String ACCESSOR_SOC_1 = "ACCESSOR1";
	protected static final String ACCESSOR_SOC_2 = "ACCESSOR2";
	protected static final String ACCESSOR_SOC_NONMSC = "ACCESSOR3";
	protected static final String REGULAR_SOC_1 = "REGULAR01";
	protected static final String REGULAR_SOC_2 = "REGULAR02";
	protected static final String REGULAR_SOC_NONMSC = "REGULAR03";
	protected static final String PRICEPLAN_CONTR_SOC_1 = "PRICPLNC1";
	protected static final String PRICEPLAN_ACCSS_SOC_1 = "PRICPLNA1";
	
	protected Map<String, List<String>> pricing_data_sharing_groups = new HashMap<String, List<String>>();
	protected SubscriptionMSCResultInfo maintainedMSCResult = new SubscriptionMSCResultInfo();
	protected SubscriptionMSCResultInfo brokenMSCResult = new SubscriptionMSCResultInfo();
	
	protected ServiceInfo[] noDataSharingServices;
	protected ServiceInfo[] multipleContributingServices;
	protected ServiceInfo[] accessingDataSharingService;		
	protected ServiceInfo[] contributingDataSharingService;
	protected ServiceInfo[] usDataSharingService;
	
	private List<String> mscFamilyTypes = Arrays.asList(ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE, ServiceSummary.FAMILY_TYPE_CODE_DATA_DOLLAR_POOLING);
	private List<String> nonMSCFamilyTypes = Arrays.asList(ServiceSummary.FAMILY_TYPE_CODE_NON_MSC_SERVICE, ServiceSummary.FAMILY_TYPE_CODE_PPS_ADDON);
	private List<String> nonMSCFamilyType = Arrays.asList(ServiceSummary.FAMILY_TYPE_CODE_NON_MSC_SERVICE);		
		
	public void setupEJBs() throws ApplicationException, TelusException {
		pricing_data_sharing_groups.put(ACCT_SUB_TYPE_IR, Arrays.asList(DATA_SHARING_GROUP_1, DATA_SHARING_GROUP_3));
		pricing_data_sharing_groups.put(ACCT_SUB_TYPE_BC, Arrays.asList(DATA_SHARING_GROUP_2));
		when(referenceDataFacade.getDataSharingPricingGroups()).thenReturn(pricing_data_sharing_groups);
		when(ejbController.getEjb(AccountInformationHelper.class)).thenReturn(accountInformationHelper);
		when(ejbController.getEjb(ReferenceDataFacade.class)).thenReturn(referenceDataFacade);		
	}
	
	public void setupSubscriberInfo() {
		when(activeSubscriber.getStatus()).thenReturn(SubscriberInfo.STATUS_ACTIVE);
		when(suspendedSubscriber.getStatus()).thenReturn(SubscriberInfo.STATUS_SUSPENDED);
		when(reservedSubscriber.getStatus()).thenReturn(SubscriberInfo.STATUS_RESERVED);
		when(cancelledSubscriber.getStatus()).thenReturn(SubscriberInfo.STATUS_CANCELED);
		when(koodoSubscriber.getStatus()).thenReturn(SubscriberInfo.STATUS_ACTIVE);
		when(activeSubscriber.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
		when(suspendedSubscriber.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
		when(reservedSubscriber.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
		when(cancelledSubscriber.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
		when(koodoSubscriber.getBrandId()).thenReturn(BrandInfo.BRAND_ID_KOODO);		
	}
	
	public void setupAccountInfo() {
		when(outOfScopeAccount.getStatus()).thenReturn(AccountInfo.STATUS_OPEN);
		when(openAccount.getStatus()).thenReturn(AccountInfo.STATUS_OPEN);
		when(suspendedAccount.getStatus()).thenReturn(AccountInfo.STATUS_SUSPENDED);
		when(tentativeAccount.getStatus()).thenReturn(AccountInfo.STATUS_TENTATIVE);
		when(cancelledAccount.getStatus()).thenReturn(AccountInfo.STATUS_CANCELED);
		when(closedAccount.getStatus()).thenReturn(AccountInfo.STATUS_CLOSED);
		
		when(outOfScopeAccount.getAccountType()).thenReturn(AccountInfo.ACCOUNT_TYPE_CONSUMER);
		when(outOfScopeAccount.getAccountSubType()).thenReturn(AccountInfo.ACCOUNT_SUBTYPE_IDEN_INDIVIDUAL);		
		when(openAccount.getAccountType()).thenReturn(AccountInfo.ACCOUNT_TYPE_CONSUMER);
		when(openAccount.getAccountSubType()).thenReturn(AccountInfo.ACCOUNT_SUBTYPE_IDEN_ENTERPRISE);
		when(suspendedAccount.getAccountType()).thenReturn(AccountInfo.ACCOUNT_TYPE_CONSUMER);
		when(suspendedAccount.getAccountSubType()).thenReturn(AccountInfo.ACCOUNT_SUBTYPE_IDEN_ENTERPRISE);
	}
	
	public void setupSubscriberContractInfo() throws TelusAPIException {
		setupPricePlans();
		setupServices();
		noDataSharingServices = new ServiceInfo[]{regularService};
		multipleContributingServices = new ServiceInfo[]{contributorServiceGroup1, contributorServiceGroup2};
		accessingDataSharingService = new ServiceInfo[]{accessorServiceGroup1};		
		contributingDataSharingService = new ServiceInfo[]{contributorServiceGroup1};
		usDataSharingService = new ServiceInfo[]{contributorServiceGroup2};
		ServiceAgreementInfo dummyContractService = new ServiceAgreementInfo();
		dummyContractService.setServiceCode("TEST");
		when(subscriberContract.getServices()).thenReturn(new ContractService[]{dummyContractService});
		
	}
	
	private void setupServices() throws TelusAPIException {
		setupServiceDataSharingGroups();
		when(regularService.getDataSharingGroups()).thenReturn(new ServiceDataSharingGroup[0]);		
		when(contributorServiceGroup1.getDataSharingGroups()).thenReturn(new ServiceDataSharingGroup[]{contributorDataSharingGroup1});
		when(contributorServiceGroup2.getDataSharingGroups()).thenReturn(new ServiceDataSharingGroup[]{contributorDataSharingGroup2});
		when(accessorServiceGroup1.getDataSharingGroups()).thenReturn(new ServiceDataSharingGroup[]{accessorDataSharingGroup1});
		when(accessorServiceGroup2.getDataSharingGroups()).thenReturn(new ServiceDataSharingGroup[]{accessorDataSharingGroup2});
	}
	
	private void setupPricePlans() throws TelusAPIException {
		when(regularPricePlan.getDataSharingGroups()).thenReturn(new ServiceDataSharingGroup[0]);		
		when(dataSharingPricePlanContributorGroup1.getDataSharingGroups()).thenReturn(new ServiceDataSharingGroup[]{contributorDataSharingGroup1});
		when(dataSharingPricePlanContributorGroup2.getDataSharingGroups()).thenReturn(new ServiceDataSharingGroup[]{contributorDataSharingGroup2});
		when(dataSharingPricePlanAccessorGroup1.getDataSharingGroups()).thenReturn(new ServiceDataSharingGroup[]{accessorDataSharingGroup1});
		when(dataSharingPricePlanAccessorGroup2.getDataSharingGroups()).thenReturn(new ServiceDataSharingGroup[]{accessorDataSharingGroup2});
	}
	
	private void setupServiceDataSharingGroups() {
		when(contributorDataSharingGroup1.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_1);	
		when(contributorDataSharingGroup1.isContributing()).thenReturn(true);
		
		when(contributorDataSharingGroup2.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_2);	
		when(contributorDataSharingGroup2.isContributing()).thenReturn(true);
		
		when(accessorDataSharingGroup1.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_1);	
		when(accessorDataSharingGroup1.isContributing()).thenReturn(false);
		
		when(accessorDataSharingGroup2.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_2);	
		when(accessorDataSharingGroup2.isContributing()).thenReturn(false);
	}
	
	public void setupSubscriptionMSCResult() {
		maintainedMSCResult.setReturnCode(SubscriptionMSCResultInfo.RETURN_CODE_MSC_NOT_BROKEN);
		brokenMSCResult.setReturnCode(SubscriptionMSCResultInfo.RETURN_CODE_MSC_BROKEN);
	}
	
	public void setupSubscriberDataSharingDetailInfo() {
		setupDataSharingSocs();
		setupRegularSocs();
		
		setupContributor1Group1();
		setupContributor2Group1();
		setupAccessor1Group1();
		setupAccessor2Group1();
		setupAccessor3Group1();
		setupAccessor4Group1();
		setupAccessor5Group1();
		setupAccessor6Group1();
	}
	
	/**
	 * <pre>
	 * Contributor 1 Group 1 Details
	 * PricePlan:
	 *      $50 - not data sharing
	 * Non-data sharing services:
	 *      $25 - regularSoc1
	 *      $15 - regularSoc2
	 * Data sharing services:
	 *   GROUP1
	 *      $10 - contributingSoc1
	 * Contract End Date: 
	 *      TODAY
	 * </pre>
	 * @return
	 */
	private void setupContributor1Group1() {
		when(contributor1Group1.getSubscriberId()).thenReturn(CONTRIBUTOR1_GROUP1_ID);
		when(contributor1Group1.getSubscriptionId()).thenReturn(Long.valueOf(CONTRIBUTOR1_GROUP1_ID));
		when(contributor1Group1.getContractEndDate()).thenReturn(TODAY);		
		when(contributor1Group1.getPricePlanRecurringCharge()).thenReturn(50.0);
		when(contributor1Group1.getNonDataSharingRegularSocList()).thenReturn(Arrays.asList(regularSoc1, regularSoc2));
		when(contributor1Group1.getDataSharingInfoList()).thenReturn(Arrays.asList(contributorSoc1Group1));
		when(subContributor1Group1.getSubscriberId()).thenReturn(CONTRIBUTOR1_GROUP1_ID);
		when(subContributor1Group1.getStartServiceDate()).thenReturn(TODAY);
		when(subContributor1Group1.getStatus()).thenReturn(SubscriberInfo.STATUS_ACTIVE);
		when(subContributor1Group1.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
	}
	
	/**
	 * <pre>
	 * Contributor 2 Group 1 Details
	 * PricePlan:
	 *      $60 - PRICPLNC1, data sharing GROUP1
	 * Non-data sharing services:
	 *      $15 - regularSoc2
	 *      $30 - regularNonMSCSoc
	 * Data sharing services:
	 *   GROUP2
	 *      $5  - accessingSoc2
	 * Contract End Date: 
	 *      April 5, 2063
	 * </pre>
	 * @return
	 */
	private void setupContributor2Group1() {
		when(contributor2Group1.getSubscriberId()).thenReturn(CONTRIBUTOR2_GROUP1_ID);
		when(contributor2Group1.getSubscriptionId()).thenReturn(Long.valueOf(CONTRIBUTOR2_GROUP1_ID));
		when(contributor2Group1.getContractEndDate()).thenReturn(FAR_INTO_THE_FUTURE);
		when(contributor2Group1.getPricePlanCode()).thenReturn(PRICEPLAN_CONTR_SOC_1);
		when(contributor2Group1.getPricePlanRecurringCharge()).thenReturn(60.0);
		when(contributor2Group1.getPricePlanDSGroupCode()).thenReturn(DATA_SHARING_GROUP_1);
		when(contributor2Group1.isPricePlanContributingInd()).thenReturn(true);
		when(contributor2Group1.getNonDataSharingRegularSocList()).thenReturn(Arrays.asList(regularSoc2, regularNonMSCSoc));
		when(contributor2Group1.getDataSharingInfoList()).thenReturn(Arrays.asList(contributorPricePlanGroup1, accessorSoc2Group2));
		when(subContributor2Group1.getSubscriberId()).thenReturn(CONTRIBUTOR2_GROUP1_ID);
		when(subContributor2Group1.getStartServiceDate()).thenReturn(FAR_INTO_THE_FUTURE);
		when(subContributor2Group1.getStatus()).thenReturn(SubscriberInfo.STATUS_ACTIVE);
		when(subContributor2Group1.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
	}
	
	/**
	 * <pre>
	 * Accessor 1 Group 1 Details
	 * PricePlan:
	 *      $60 - non-data sharing 
	 * Non-data sharing services:
	 *      $25 - regularSoc1
	 * Data sharing services:
	 *   GROUP1
	 *      $0  - accessingSoc1
	 * Contract End Date: 
	 *      February 29, 2016
	 * </pre>
	 * @return
	 */
	private void setupAccessor1Group1() {
		when(accessor1Group1.getSubscriberId()).thenReturn(ACCESSOR1_GROUP1_ID);
		when(accessor1Group1.getSubscriptionId()).thenReturn(Long.valueOf(ACCESSOR1_GROUP1_ID));
		when(accessor1Group1.getContractEndDate()).thenReturn(LEAPPING_YEARS);		
		when(accessor1Group1.getPricePlanRecurringCharge()).thenReturn(60.0);
		when(accessor1Group1.getNonDataSharingRegularSocList()).thenReturn(Arrays.asList(regularSoc1));
		when(accessor1Group1.getDataSharingInfoList()).thenReturn(Arrays.asList(accessorSoc1Group1));
		when(subAccessor1Group1.getSubscriberId()).thenReturn(ACCESSOR1_GROUP1_ID);
		when(subAccessor1Group1.getStartServiceDate()).thenReturn(LEAPPING_YEARS);
		when(subAccessor1Group1.getStatus()).thenReturn(SubscriberInfo.STATUS_ACTIVE);
		when(subAccessor1Group1.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
	}
	
	/**
	 * <pre>
	 * Accessor 2 Group 1 Details
	 * PricePlan:
	 *      $30 - non-data sharing 
	 * Non-data sharing services:
	 *      $15 - regularSoc2
	 * Data sharing services:
	 *   GROUP1
	 *      $2  - accessingSoc2
	 * Contract End Date: 
	 *      July 24, 2020
	 * </pre>
	 * @return
	 */
	private void setupAccessor2Group1() {
		when(accessor2Group1.getSubscriberId()).thenReturn(ACCESSOR2_GROUP1_ID);
		when(accessor2Group1.getSubscriptionId()).thenReturn(Long.valueOf(ACCESSOR2_GROUP1_ID));
		when(accessor2Group1.getContractEndDate()).thenReturn(OLYMPICS_JAPAN);		
		when(accessor2Group1.getPricePlanRecurringCharge()).thenReturn(30.0);
		when(accessor2Group1.getNonDataSharingRegularSocList()).thenReturn(Arrays.asList(regularSoc2));
		when(accessor2Group1.getDataSharingInfoList()).thenReturn(Arrays.asList(accessorSoc2Group1));
		when(subAccessor2Group1.getSubscriberId()).thenReturn(ACCESSOR2_GROUP1_ID);
		when(subAccessor2Group1.getStartServiceDate()).thenReturn(OLYMPICS_JAPAN);
		when(subAccessor2Group1.getStatus()).thenReturn(SubscriberInfo.STATUS_ACTIVE);
		when(subAccessor2Group1.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
	}
	
	/**
	 * <pre>
	 * Accessor 3 Group 1 Details
	 * PricePlan:
	 *      $55 - non-data sharing 
	 * Non-data sharing services:
	 *      $15 - regularSoc2
	 * Data sharing services:
	 *   GROUP1
	 *      $2  - accessingSoc2
	 * Contract End Date: 
	 *      no contract
	 * Start Service Date:
	 *      December 31, 1999     
	 * </pre>
	 * @return
	 */
	private void setupAccessor3Group1() {
		when(accessor3Group1.getSubscriberId()).thenReturn(ACCESSOR3_GROUP1_ID);
		when(accessor3Group1.getSubscriptionId()).thenReturn(Long.valueOf(ACCESSOR3_GROUP1_ID));	
		when(accessor3Group1.getContractEndDate()).thenReturn(null);					
		when(accessor3Group1.getPricePlanRecurringCharge()).thenReturn(55.0);
		when(accessor3Group1.getNonDataSharingRegularSocList()).thenReturn(Arrays.asList(regularSoc2));
		when(accessor3Group1.getDataSharingInfoList()).thenReturn(Arrays.asList(accessorSoc2Group1));
		when(subAccessor3Group1.getSubscriberId()).thenReturn(ACCESSOR3_GROUP1_ID);
		when(subAccessor3Group1.getStartServiceDate()).thenReturn(BLAST_FROM_THE_PAST);
		when(subAccessor3Group1.getStatus()).thenReturn(SubscriberInfo.STATUS_ACTIVE);
		when(subAccessor3Group1.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
	}
	
	/**
	 * <pre>
	 * Accessor 4 Group 1 Details
	 * PricePlan:
	 *      $55 - non-data sharing 
	 * Non-data sharing services:
	 *      $15 - regularSoc2
	 * Data sharing services:
	 *   GROUP1
	 *      $0  - accessingSoc1
	 * Contract End Date: 
	 *      no contract
	 * Start Service Date:
	 *      April 5, 2063     
	 * </pre>
	 * @return
	 */
	private void setupAccessor4Group1() {
		when(accessor4Group1.getSubscriberId()).thenReturn(ACCESSOR4_GROUP1_ID);
		when(accessor4Group1.getSubscriptionId()).thenReturn(Long.valueOf(ACCESSOR4_GROUP1_ID));		
		when(accessor4Group1.getContractEndDate()).thenReturn(null);								
		when(accessor4Group1.getPricePlanRecurringCharge()).thenReturn(55.0);
		when(accessor4Group1.getNonDataSharingRegularSocList()).thenReturn(Arrays.asList(regularSoc2));
		when(accessor4Group1.getDataSharingInfoList()).thenReturn(Arrays.asList(accessorSoc1Group1));
		when(subAccessor4Group1.getSubscriberId()).thenReturn(ACCESSOR4_GROUP1_ID);
		when(subAccessor4Group1.getStartServiceDate()).thenReturn(FAR_INTO_THE_FUTURE);
		when(subAccessor4Group1.getStatus()).thenReturn(SubscriberInfo.STATUS_ACTIVE);
		when(subAccessor4Group1.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
	}
	
	/**
	 * <pre>
	 * Accessor 5 Group 1 Details
	 * PricePlan:
	 *      $50 - PRICPLNA1, data sharing GROUP1
	 * Non-data sharing services:
	 *      $15 - regularSoc2
	 * Contract End Date: 
	 *     TODAY           
	 * </pre>
	 * @return
	 */
	private void setupAccessor5Group1() {
		when(accessor5Group1.getSubscriberId()).thenReturn(ACCESSOR5_GROUP1_ID);
		when(accessor5Group1.getSubscriptionId()).thenReturn(Long.valueOf(ACCESSOR5_GROUP1_ID));	
		when(accessor5Group1.getContractEndDate()).thenReturn(TODAY);										
		when(accessor5Group1.getPricePlanRecurringCharge()).thenReturn(55.0);
		when(accessor5Group1.getPricePlanDSGroupCode()).thenReturn(DATA_SHARING_GROUP_1);
		when(accessor5Group1.isPricePlanContributingInd()).thenReturn(false);
		when(accessor5Group1.getNonDataSharingRegularSocList()).thenReturn(Arrays.asList(regularSoc2));
		when(accessor5Group1.getDataSharingInfoList()).thenReturn(Arrays.asList(accessorPricePlanGroup1));
		when(subAccessor5Group1.getSubscriberId()).thenReturn(ACCESSOR5_GROUP1_ID);
		when(subAccessor5Group1.getStartServiceDate()).thenReturn(TODAY);
		when(subAccessor5Group1.getStatus()).thenReturn(SubscriberInfo.STATUS_ACTIVE);
		when(subAccessor5Group1.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
	}
	
	/**
	 * <pre>
	 * Accessor 6 Group 1 Details
	 * Status: 
	 *      Suspended!
	 * PricePlan:
	 *      $30 - non-data sharing 
	 * Non-data sharing services:
	 *      $15 - regularSoc2
	 * Data sharing services:
	 *   GROUP1
	 *      $2  - accessingSoc2
	 * Contract End Date: 
	 *      July 24, 2020
	 * </pre>
	 * @return
	 */
	private void setupAccessor6Group1() {
		when(accessor6Group1.getSubscriberId()).thenReturn(ACCESSOR6_GROUP1_ID);
		when(accessor6Group1.getSubscriptionId()).thenReturn(Long.valueOf(ACCESSOR6_GROUP1_ID));
		when(accessor6Group1.getContractEndDate()).thenReturn(OLYMPICS_JAPAN);		
		when(accessor6Group1.getPricePlanRecurringCharge()).thenReturn(30.0);
		when(accessor6Group1.getNonDataSharingRegularSocList()).thenReturn(Arrays.asList(regularSoc2));
		when(accessor6Group1.getDataSharingInfoList()).thenReturn(Arrays.asList(accessorSoc2Group1));
		when(subAccessor6Group1.getSubscriberId()).thenReturn(ACCESSOR6_GROUP1_ID);
		when(subAccessor6Group1.getStartServiceDate()).thenReturn(OLYMPICS_JAPAN);
		when(subAccessor6Group1.getStatus()).thenReturn(SubscriberInfo.STATUS_SUSPENDED);
		when(subAccessor6Group1.getBrandId()).thenReturn(BrandInfo.BRAND_ID_TELUS);
	}
	
	private void setupRegularSocs() {
		when(regularSoc1.getSocCode()).thenReturn(REGULAR_SOC_1);
		when(regularSoc1.getSocRecurringCharge()).thenReturn(25.0);		
				
		when(regularSoc2.getSocCode()).thenReturn(REGULAR_SOC_2);
		when(regularSoc2.getSocRecurringCharge()).thenReturn(15.0);		
		
		when(regularNonMSCSoc.getSocCode()).thenReturn(REGULAR_SOC_NONMSC);
		when(regularNonMSCSoc.getSocRecurringCharge()).thenReturn(30.0);
		when(regularNonMSCSoc.getFamilyTypes()).thenReturn(nonMSCFamilyType);		
	}
	
	private void setupDataSharingSocs() {
		when(contributorSoc1.getDataSharingSocCode()).thenReturn(CONTRIBUTOR_SOC_1);
		when(contributorSoc1.getDataSharingSocDescription()).thenReturn(CONTRIBUTOR_SOC_1);
		when(contributorSoc1.getDataSharingSpentAmt()).thenReturn(10.00);
		when(contributorSoc1.getContributingInd()).thenReturn(true);
		when(contributorSoc1.getFamilyTypes()).thenReturn(null);
		when(contributorSoc1Group1.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_1);
		when(contributorSoc1Group1.getDataSharingSocList()).thenReturn(Arrays.asList(contributorSoc1));
		when(contributorSoc1Group2.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_2);
		when(contributorSoc1Group2.getDataSharingSocList()).thenReturn(Arrays.asList(contributorSoc1));

		when(contributorSoc2.getDataSharingSocCode()).thenReturn(CONTRIBUTOR_SOC_2);
		when(contributorSoc2.getDataSharingSocDescription()).thenReturn(CONTRIBUTOR_SOC_2);
		when(contributorSoc2.getDataSharingSpentAmt()).thenReturn(25.00);
		when(contributorSoc2.getContributingInd()).thenReturn(true);
		when(contributorSoc2.getFamilyTypes()).thenReturn(mscFamilyTypes);
		when(contributorSoc2Group1.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_1);
		when(contributorSoc2Group1.getDataSharingSocList()).thenReturn(Arrays.asList(contributorSoc2));
		when(contributorSoc2Group2.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_2);
		when(contributorSoc2Group2.getDataSharingSocList()).thenReturn(Arrays.asList(contributorSoc2));

		when(contributorNonMSCSoc.getDataSharingSocCode()).thenReturn(CONTRIBUTOR_SOC_NONMSC);
		when(contributorNonMSCSoc.getDataSharingSocDescription()).thenReturn(CONTRIBUTOR_SOC_NONMSC);
		when(contributorNonMSCSoc.getDataSharingSpentAmt()).thenReturn(25.00);
		when(contributorNonMSCSoc.getContributingInd()).thenReturn(true);
		when(contributorNonMSCSoc.getFamilyTypes()).thenReturn(nonMSCFamilyTypes);
		when(contributorNonMSCSocGroup1.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_1);
		when(contributorNonMSCSocGroup1.getDataSharingSocList()).thenReturn(Arrays.asList(contributorNonMSCSoc));
		when(contributorNonMSCSocGroup2.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_2);
		when(contributorNonMSCSocGroup2.getDataSharingSocList()).thenReturn(Arrays.asList(contributorNonMSCSoc));

		when(accessorSoc1.getDataSharingSocCode()).thenReturn(ACCESSOR_SOC_1);
		when(accessorSoc1.getDataSharingSocDescription()).thenReturn(ACCESSOR_SOC_1);
		when(accessorSoc1.getDataSharingSpentAmt()).thenReturn(0.00);
		when(accessorSoc1.getContributingInd()).thenReturn(false);
		when(accessorSoc1.getFamilyTypes()).thenReturn(null);
		when(accessorSoc1Group1.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_1);
		when(accessorSoc1Group1.getDataSharingSocList()).thenReturn(Arrays.asList(accessorSoc1));
		when(accessorSoc1Group2.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_2);
		when(accessorSoc1Group2.getDataSharingSocList()).thenReturn(Arrays.asList(accessorSoc1));

		when(accessorSoc2.getDataSharingSocCode()).thenReturn(ACCESSOR_SOC_2);
		when(accessorSoc2.getDataSharingSocDescription()).thenReturn(ACCESSOR_SOC_2);
		when(accessorSoc2.getDataSharingSpentAmt()).thenReturn(5.00);
		when(accessorSoc2.getContributingInd()).thenReturn(false);
		when(accessorSoc2.getFamilyTypes()).thenReturn(mscFamilyTypes);
		when(accessorSoc2Group1.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_1);
		when(accessorSoc2Group1.getDataSharingSocList()).thenReturn(Arrays.asList(accessorSoc2));
		when(accessorSoc2Group2.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_2);
		when(accessorSoc2Group2.getDataSharingSocList()).thenReturn(Arrays.asList(accessorSoc2));

		when(accessorSocNonMSCSoc.getDataSharingSocCode()).thenReturn(ACCESSOR_SOC_NONMSC);
		when(accessorSocNonMSCSoc.getDataSharingSocDescription()).thenReturn(ACCESSOR_SOC_NONMSC);
		when(accessorSocNonMSCSoc.getDataSharingSpentAmt()).thenReturn(5.00);
		when(accessorSocNonMSCSoc.getContributingInd()).thenReturn(false);
		when(accessorSocNonMSCSoc.getFamilyTypes()).thenReturn(nonMSCFamilyType);
		when(accessorSocNonMSCSocGroup1.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_1);
		when(accessorSocNonMSCSocGroup1.getDataSharingSocList()).thenReturn(Arrays.asList(accessorSocNonMSCSoc));
		when(accessorSocNonMSCSocGroup2.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_2);
		when(accessorSocNonMSCSocGroup2.getDataSharingSocList()).thenReturn(Arrays.asList(accessorSocNonMSCSoc));
		
		when(contributorPricePlan.getDataSharingSocCode()).thenReturn(PRICEPLAN_CONTR_SOC_1);
		when(contributorPricePlan.getDataSharingSocDescription()).thenReturn(PRICEPLAN_CONTR_SOC_1);
		when(contributorPricePlan.getDataSharingSpentAmt()).thenReturn(60.00);
		when(contributorPricePlan.getContributingInd()).thenReturn(true);
		when(contributorPricePlan.getFamilyTypes()).thenReturn(null);
		when(contributorPricePlanGroup1.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_1);
		when(contributorPricePlanGroup1.getDataSharingSocList()).thenReturn(Arrays.asList(contributorPricePlan));
		
		when(accessorPricePlan.getDataSharingSocCode()).thenReturn(CONTRIBUTOR_SOC_1);
		when(accessorPricePlan.getDataSharingSocDescription()).thenReturn(CONTRIBUTOR_SOC_1);
		when(accessorPricePlan.getDataSharingSpentAmt()).thenReturn(50.00);
		when(accessorPricePlan.getContributingInd()).thenReturn(false);
		when(accessorPricePlan.getFamilyTypes()).thenReturn(null);
		when(accessorPricePlanGroup1.getDataSharingGroupCode()).thenReturn(DATA_SHARING_GROUP_1);
		when(accessorPricePlanGroup1.getDataSharingSocList()).thenReturn(Arrays.asList(accessorPricePlan));
	}
	
	protected void verifyDataSharingSocTransferInfo(DataSharingSocTransferInfo socTransferInfo, String candidateSubscriberId, String candidateSocCode,
			String candidateSocDescription, String contributorSocCode, String contributorSocDescription) {
		assertThat(socTransferInfo.getCandidateSubscriberId(), equalTo(candidateSubscriberId));		
		assertThat(socTransferInfo.getCandidateSocCode(), equalTo(candidateSocCode));
		assertThat(socTransferInfo.getCandidateSocDescription(), equalTo(candidateSocDescription));
		assertThat(socTransferInfo.getContributorSocCode(), equalTo(contributorSocCode));
		assertThat(socTransferInfo.getContributorSocDescription(), equalTo(contributorSocDescription));
	}
	
	protected static String[] getAnyStringArray() {
		return anyListOf(String.class).toArray(new String[0]);
	}
		
	private static Date getDate(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month + 1);
		c.set(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}
	
}
