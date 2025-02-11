package com.telus.cmb.subscriber.lifecyclefacade.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.rmi.UnmarshalException;
import java.util.ArrayList;
import java.util.List;

import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.account.AccountSummary;
import com.telus.api.reference.ServiceSummary;
import com.telus.cmb.common.eligibility.rules.CallingCircleEligibilityEvaluationResult;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.ProvisioningTransactionDetailInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.provisioning.bssAdapter.common.ConnectionException;
import com.telus.provisioning.bssAdapter.common.ObjectNotFoundException;
import com.telus.provisioning.bssAdapter.common.OrderLineItemStatus;
import com.telus.provisioning.bssAdapter.ejb.ProvisioningOrderLookupRemote;

public class SubscriberLifeCycleFacadeImplTest{

	@Mocked(methods={"getProvisioningOrderLookup"})
	SubscriberLifecycleFacadeImpl lifeCycleFacade1;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void testRetrieveProvisioningTransactionDetails() throws Exception{
	
		final ProvisioningOrderLookupRemote remoteMock = Mockito.mock(ProvisioningOrderLookupRemote.class);
		ProvisioningTransactionDetailInfo[] ptdiArray=null;
	
		new NonStrictExpectations() {
			{
				invoke(lifeCycleFacade1, "getProvisioningOrderLookup"); returns(remoteMock);
			}
		};
	
		List<OrderLineItemStatus> list=new ArrayList<OrderLineItemStatus>();
		list.add(new OrderLineItemStatus());
		list.add(new OrderLineItemStatus());
		Mockito.when(remoteMock.getOrderLineItemStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(list);
	
		String subscriberId=null;
		String transactionNo=null;
		ptdiArray=lifeCycleFacade1.retrieveProvisioningTransactionDetails(subscriberId, transactionNo);
		assertEquals(2,ptdiArray.length);
		Mockito.reset(remoteMock);
	
		Mockito.when(remoteMock.getOrderLineItemStatus(Mockito.anyString(), Mockito.anyString())).thenThrow(new ObjectNotFoundException());
		ptdiArray=lifeCycleFacade1.retrieveProvisioningTransactionDetails("abc", "def");
		assertEquals(0,ptdiArray.length);
		Mockito.reset(remoteMock);
	
		Mockito.when(remoteMock.getOrderLineItemStatus(Mockito.anyString(), Mockito.anyString())).thenThrow(new UnmarshalException("xx"));
		ptdiArray=lifeCycleFacade1.retrieveProvisioningTransactionDetails("abc", "def");
		assertEquals(0,ptdiArray.length);
		Mockito.reset(remoteMock);
	
		try{
			Mockito.when(remoteMock.getOrderLineItemStatus(Mockito.anyString(), Mockito.anyString())).thenThrow(new ConnectionException(new Exception(), ""));
			lifeCycleFacade1.retrieveProvisioningTransactionDetails("abc", "def");
			fail("Exception Expected");
			Mockito.reset(remoteMock);
	
		}catch(Throwable t) {
			System.out.println("Handling Throwable");
			//t.printStackTrace();	// do not print to stderr; use during development, remove when done (comment by Canh)
			//Mockito.doNothing();  // incorrect use (comment by Canh)
		}
	
		try{
			Mockito.when(remoteMock.getOrderLineItemStatus(Mockito.anyString(), Mockito.anyString())).thenThrow(new NullPointerException());
			lifeCycleFacade1.retrieveProvisioningTransactionDetails("abc", "def");
			fail("Exception Expected");
			Mockito.reset(remoteMock);
	
		}catch(Throwable t) {
			System.out.println("Handling Throwable");
			//t.printStackTrace();	// do not print to stderr; use during development, remove when done (comment by Canh)
			//Mockito.doNothing();  // incorrect use (comment by Canh)
		}
	}

	@Test
	public void addBusinessAnywherePricePlansToAccount() {
		AccountInfo accountInfo = Mockito.mock(AccountInfo.class);
		ServiceInfo serviceInfo = Mockito.mock(ServiceInfo.class);
		
		Mockito.when(serviceInfo.getFamilyTypes()).thenReturn(new String[]{ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE});
		Mockito.when(serviceInfo.getCode()).thenReturn("DUMMYCODE");
			
		Mockito.when(accountInfo.getAccountType()).thenReturn(AccountSummary.ACCOUNT_TYPE_BUSINESS);
		Mockito.when(accountInfo.getAccountSubType()).thenReturn(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR);
		try {
			lifeCycleFacade1.testServiceAddToBusinessAnywhereAccount(accountInfo, serviceInfo);
		} catch (ApplicationException e) {
			fail("Business Anywhere ServiceInfo should be applicable to " + AccountSummary.ACCOUNT_TYPE_BUSINESS + "/" + AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR);
		}
		
		Mockito.when(accountInfo.getAccountType()).thenReturn(AccountSummary.ACCOUNT_TYPE_BUSINESS);
		Mockito.when(accountInfo.getAccountSubType()).thenReturn(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL);
		try {
			lifeCycleFacade1.testServiceAddToBusinessAnywhereAccount(accountInfo, serviceInfo);
		} catch (ApplicationException e) {
			fail("Business Anywhere ServiceInfo should be applicable to " + AccountSummary.ACCOUNT_TYPE_BUSINESS + "/" + AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL);
		}

		Mockito.when(accountInfo.getAccountType()).thenReturn(AccountSummary.ACCOUNT_TYPE_CORPORATE);
		Mockito.when(accountInfo.getAccountSubType()).thenReturn(AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE);
		try {
			lifeCycleFacade1.testServiceAddToBusinessAnywhereAccount(accountInfo, serviceInfo);
		} catch (ApplicationException e) {
			fail("Business Anywhere ServiceInfo should be applicable to " + AccountSummary.ACCOUNT_TYPE_CORPORATE + "/" + AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE);
		}					
	}
	
	@Test(expected=ApplicationException.class)
	public void addBusinessAnywherePricePlansToAccount1() throws ApplicationException {
		AccountInfo accountInfo = Mockito.mock(AccountInfo.class);
		ServiceInfo serviceInfo = Mockito.mock(ServiceInfo.class);
		
		Mockito.when(serviceInfo.getFamilyTypes()).thenReturn(new String[]{ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE});
		Mockito.when(serviceInfo.getCode()).thenReturn("DUMMYCODE");

		Mockito.when(accountInfo.getAccountType()).thenReturn(AccountSummary.ACCOUNT_TYPE_CONSUMER);
		Mockito.when(accountInfo.getAccountSubType()).thenReturn(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
		try {
			lifeCycleFacade1.testServiceAddToBusinessAnywhereAccount(accountInfo, serviceInfo);
		} catch (ApplicationException e) {
			assertEquals(ErrorCodes.ERROR_INCOMPATIBLE_SERVICE_ACCOUNT, e.getErrorCode());
			throw e;
		}			
		fail("Business Anywhere ServiceInfo should NOT be applicable to " + AccountSummary.ACCOUNT_TYPE_CONSUMER + "/" + AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
	}
	
	@Test
	public void addBusinessAnywherePricePlansToAccount2() throws ApplicationException {
		AccountInfo accountInfo = Mockito.mock(AccountInfo.class);
		ServiceInfo serviceInfo = Mockito.mock(ServiceInfo.class);
		
		Mockito.when(serviceInfo.getFamilyTypes()).thenReturn(new String[]{});
		Mockito.when(serviceInfo.getCode()).thenReturn("DUMMYCODE");

		Mockito.when(accountInfo.getAccountType()).thenReturn(AccountSummary.ACCOUNT_TYPE_CONSUMER);
		Mockito.when(accountInfo.getAccountSubType()).thenReturn(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
		try {
			lifeCycleFacade1.testServiceAddToBusinessAnywhereAccount(accountInfo, serviceInfo);
		} catch (ApplicationException e) {
			fail("Non-Business Anywhere ServiceInfo should be applicable to " + AccountSummary.ACCOUNT_TYPE_CONSUMER + "/" + AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
		}					
	}	
	
	@Test(expected=ApplicationException.class)
	public void addBusinessAnywherePricePlansToAccount3() throws ApplicationException {
		AccountInfo accountInfo = Mockito.mock(AccountInfo.class);
		PricePlanInfo pricePlanInfo = Mockito.mock(PricePlanInfo.class);
		
		Mockito.when(pricePlanInfo.getFamilyTypes()).thenReturn(new String[]{ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE});
		Mockito.when(pricePlanInfo.getCode()).thenReturn("DUMMYCODE");

		Mockito.when(accountInfo.getAccountType()).thenReturn(AccountSummary.ACCOUNT_TYPE_CONSUMER);
		Mockito.when(accountInfo.getAccountSubType()).thenReturn(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
		try {
			lifeCycleFacade1.testServiceAddToBusinessAnywhereAccount(accountInfo, pricePlanInfo);
		} catch (ApplicationException e) {
			assertEquals(ErrorCodes.ERROR_INCOMPATIBLE_PRICEPLAN_ACCOUNT, e.getErrorCode());
			throw e;
		}			
		fail("Business Anywhere PricePlanInfo should NOT be applicable to " + AccountSummary.ACCOUNT_TYPE_CONSUMER + "/" + AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
	}	

	@Test
	public void evaluateCallingCircleCommitmentAttributeData1() throws ApplicationException {
		SubscriberContractInfo contractInfo=Mockito.mock(SubscriberContractInfo.class);
		ServiceFeatureInfo[] contractFeatures = new ServiceFeatureInfo[0];
		Mockito.when(contractInfo.getNullCCCommitmentDataFeatures()).thenReturn(contractFeatures);
		
		assertEquals(0,contractFeatures.length);
		
	}
	
	@Test
	public void evaluateCallingCircleCommitmentAttributeData2(@Mocked("isCDRCallingCircleRollback") final AppConfiguration appConfigurationMock)
					throws ApplicationException {
		
		CallingCircleEligibilityEvaluationResult ccEligibility =new CallingCircleEligibilityEvaluationResult();
		ccEligibility.setCommitmentRulesInd(false); 
		new NonStrictExpectations() {
			{
				invoke(appConfigurationMock, "isCDRCallingCircleRollback"); returns(true);
			}
		};
		
		SubscriberContractInfo contractInfo=Mockito.mock(SubscriberContractInfo.class);
		ContractChangeInfo changeInfo=Mockito.mock(ContractChangeInfo.class);
		
		ServiceFeatureInfo[] contractFeatures = new ServiceFeatureInfo[1];
		contractFeatures[0]=new ServiceFeatureInfo();
		
		Mockito.when(contractInfo.getNullCCCommitmentDataFeatures()).thenReturn(contractFeatures);
		ccEligibility= lifeCycleFacade1.getCallingCircleEligibilityReuslt(changeInfo);
		
		assertEquals(true,ccEligibility.isCommitmentRulesInd());
		
	}
/*	
	@Test
	public void evaluateCallingCircleCommitmentAttributeData3(@Mocked("isCDRCallingCircleRollback") final AppConfiguration appConfigurationMock)
					throws ApplicationException {
		final CallingCircleEligibilityEvaluationResult ccEligibility1 =Mockito.mock(CallingCircleEligibilityEvaluationResult.class);
		final CallingCircleEligibilityEvaluationStrategy strategyMock=  CallingCircleEligibilityEvaluationStrategy.getInstance();
		
		new NonStrictExpectations() {
			{
				invoke(appConfigurationMock, "isCDRCallingCircleRollback"); returns(false);
			//	invoke(callingCircleEligibilityEvaluationStrategyMock, "getInstance");returns(strategyMock);
			//	invoke(strategyMock, "evaluate");returns(ccEligibility1);
			}
		};
		
		AccountInfo accountInfo = Mockito.mock(AccountInfo.class);
		Mockito.when(accountInfo.getAccountType()).thenReturn(AccountSummary.ACCOUNT_TYPE_CONSUMER);
		Mockito.when(accountInfo.getAccountSubType()).thenReturn(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
		Mockito.when(accountInfo.getBrandId()).thenReturn(1);
				
		ContractChangeInfo changeInfo= Mockito.mock(ContractChangeInfo.class);
		SubscriberInfo subInfo=new SubscriberInfo();
		Mockito.when(changeInfo.getCurrentSubscriberInfo()).thenReturn(subInfo);
		Mockito.when(changeInfo.getCurrentAccountInfo()).thenReturn(accountInfo);
		
		CallingCircleEligibilityEvaluationResult ccEligibility =new CallingCircleEligibilityEvaluationResult();
		ccEligibility.setCommitmentRulesInd(false);
		
		ccEligibility= lifeCycleFacade1.getCallingCircleEligibilityReuslt(changeInfo);
		
		assertEquals(true,ccEligibility.isCommitmentRulesInd());
		
	}
	*/
	@Test
	public void evaluateCallingCircleCommitmentAttributeData4(@Mocked("isCDRCallingCircleRollback") final AppConfiguration appConfigurationMock)
					throws ApplicationException {
		
		CallingCircleEligibilityEvaluationResult ccEligibility =new CallingCircleEligibilityEvaluationResult();
		ccEligibility.setCommitmentRulesInd(false); 
		ccEligibility.setCarryOverInd(false);
		new NonStrictExpectations() {
			{
				invoke(appConfigurationMock, "isCDRCallingCircleRollback"); returns(true);
			}
		};
		
		SubscriberContractInfo contractInfo=Mockito.mock(SubscriberContractInfo.class);
		ContractChangeInfo changeInfo=Mockito.mock(ContractChangeInfo.class);
		
		ServiceFeatureInfo[] contractFeatures = new ServiceFeatureInfo[1];
		contractFeatures[0]=new ServiceFeatureInfo();
		
		Mockito.when(contractInfo.getNullCCCommitmentDataFeatures()).thenReturn(contractFeatures);
		ccEligibility= lifeCycleFacade1.getCallingCircleEligibilityReuslt(changeInfo);
		
		assertEquals(true,ccEligibility.isCommitmentRulesInd());
		assertEquals(false,ccEligibility.isCarryOverInd());
		
	}
	
	
}
