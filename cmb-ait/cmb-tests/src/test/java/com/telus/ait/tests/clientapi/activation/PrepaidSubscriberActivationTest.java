package com.telus.ait.tests.clientapi.activation;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.junit.annotations.UseTestDataFrom;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.telus.ait.cmb.steps.db.ClientApiKbDbSteps;
import com.telus.ait.fwk.test.WebServiceBaseTest;
import com.telus.ait.integration.kb.info.AccountInfo;
import com.telus.ait.tests.clientapi.constants.DefaultValues;
import com.telus.ait.tests.clientapi.helpers.CommonHelper;
import com.telus.ait.tests.clientapi.helpers.SLCMSHelper;
import com.telus.ait.tests.clientapi.helpers.SMSHelper;
import com.telus.ait.tests.clientapi.helpers.WALMSHelper;
import com.telus.ait.tests.clientapi.stepgroups.ActivationStepGroups;
import com.telus.api.reference.ApplicationSummary;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReservePhoneNumberForSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReservePhoneNumberForSubscriberResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePrepaidAccount;

/**
 * Created by wcheong on 10/12/2015.
 */
@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value= "cmb/regression/activation/testdata-PrepaidConsumerSubscriberActivation-$DATADIR.csv")
@WithTagValuesOf({"epic: Regression", "test type: web service", "test style: structured (recommended)"})
public class PrepaidSubscriberActivationTest extends WebServiceBaseTest {
	
	@Steps
	private ActivationStepGroups activationSteps;	
	
	@Steps
	private ClientApiKbDbSteps kbSteps;
	
	private String subscriberId;
	private String phoneNumber;
    
	//CSV Input Variables	
	public String ban, banStatus, accountType, accountSubType, homeProvince; 																				//AccountInfo
	public String productType, equipmentType, phoneNumberPattern; 																							//NewSubscriberInfo	
	public String pricePlan, services, prepaidServices, callingCircleNumbers, marketAreaCode;																//Activation
    	
	@Before
	public void setUp() throws Exception {	
		setupAccount();
	}
	
	@Test
	public void testSubscriberActivationForPrepaidAccounts() throws Exception {
		reservePhoneNumber();
		activateSubscriber();
		cancelSubscriber();
	}
	
	@Test
	public void testReservePhoneNumberForPrepaidAccounts() throws Exception {		
		reservePhoneNumber();
		releasePhoneNumber();
	}

	@Step
	private void setupAccount() throws Exception {			
		if (CommonHelper.isEmptyOrWildcard(ban)) {
			if (CommonHelper.isEmptyOrWildcard(banStatus)) {
				String serialNumber = activationSteps.getAvailableUSimEquipment(DefaultValues.BRAND_ID_TELUS).getPrimaryEquipment().getSerialNumber();
				CreatePrepaidAccount createPrepaid = WALMSHelper.createDefaultPrepaidAccount(accountType, accountSubType, homeProvince, serialNumber);
				OriginatingUserType createPrepaidHeader = WALMSHelper.createDefaultAccountHeader();
				ban = activationSteps.createPrepaidAccount(createPrepaid, createPrepaidHeader);	
				banStatus = AccountInfo.BAN_STATUS_TENTATIVE;
			} else {
				ban = kbSteps.findAnyBan(banStatus, accountType, accountSubType);
			}
		}
	}
	
	@Step
	private void reservePhoneNumber() throws Exception {	   		
		ReservePhoneNumberForSubscriber reservePhoneNumber = SLCMSHelper.createDefaultReservePhoneNumber(ban, productType, phoneNumberPattern, activationSteps.getAvailableSimNumber());
		ReservePhoneNumberForSubscriberResponse reserveResponse = activationSteps.reservePhoneNumber(reservePhoneNumber, accountType, accountSubType, productType, equipmentType, marketAreaCode);
		phoneNumber = reserveResponse.getPhoneNumber();
		subscriberId = reserveResponse.getSubscriberId();
		activationSteps.verifyThatPhoneNumberIsReserved(phoneNumber);
	}

	@Step
	private void releasePhoneNumber() throws Exception {		
		activationSteps.releasePhoneNumber(SLCMSHelper.createReleasePhoneNumber(ban, subscriberId, productType, DefaultValues.DEALER_CODE, DefaultValues.SALES_REP_CODE, false, null));
		activationSteps.verifyThatPhoneNumberIsReleased(phoneNumber);
	}

	@Step
	private void activateSubscriber() throws Exception {
		activationSteps.activateSubscriber(SMSHelper.createDefaultActivatePrepaidSubscriber(ban, subscriberId, phoneNumber, marketAreaCode, productType, activationSteps.getLogicalDate(), 
				activationSteps.getAvailableUSimEquipment(DefaultValues.BRAND_ID_TELUS), pricePlan, services, prepaidServices, callingCircleNumbers));
		activationSteps.verifyThatSubscriberIsActive(phoneNumber);
		if (AccountInfo.BAN_STATUS_TENTATIVE.equals(banStatus)) {
			activationSteps.verifyThatBanStatus(ban, AccountInfo.BAN_STATUS_NEW);
			banStatus = AccountInfo.BAN_STATUS_OPEN;
		}
	}

	@Step
	private void cancelSubscriber() throws Exception {
		activationSteps.cancelSubscriber("18654", "apollo", ApplicationSummary.APP_SD, phoneNumber, "AIE", 'R');
	}
		
}
