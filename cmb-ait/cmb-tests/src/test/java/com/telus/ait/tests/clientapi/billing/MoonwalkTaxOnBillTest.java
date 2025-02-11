package com.telus.ait.tests.clientapi.billing;

import static net.thucydides.core.steps.StepData.withTestDataFrom;

import java.util.Date;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.junit.annotations.UseTestDataFrom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.telus.ait.fwk.test.WebServiceBaseTest;
import com.telus.ait.tests.clientapi.constants.DefaultValues;
import com.telus.ait.tests.clientapi.helpers.SLCMSHelper;
import com.telus.ait.tests.clientapi.helpers.SMSHelper;
import com.telus.ait.tests.clientapi.helpers.WALMSHelper;
import com.telus.ait.tests.clientapi.stepgroups.ActivationStepGroups;
import com.telus.ait.tests.clientapi.stepgroups.BillingStepGroups;
import com.telus.api.reference.ApplicationSummary;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReservePhoneNumberForSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReservePhoneNumberForSubscriberResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.EquipmentActivated;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePostpaidAccount;

/**
 * Created by wcheong on 10/12/2015.
 */
@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value= "cmb/common/testdata-ConsumerRegularSubscriber-$DATADIR.csv")
@WithTagValuesOf({"epic: Moonwalk", "test type: web service", "test style: structured (recommended)"})
public class MoonwalkTaxOnBillTest extends WebServiceBaseTest {
	
	@Steps
	private ActivationStepGroups activationSteps;
	
	@Steps
	private BillingStepGroups billingSteps; 
            
	private String cleanUpState = "NONE";
    private static final String CLEAN_UP_RELEASE = "RELEASE";
    private static final String CLEAN_UP_CANCEL = "CANCEL";
    private String ban, subscriberId, phoneNumber;			

	//CSV Input Variables	
    public String accountType, accountSubType, homeProvince, equipmentType, productType, marketAreaCode, phoneNumberPattern, pricePlan, services;				
    	
	@Test
	public void testMoonwalkTaxOnBill() throws Exception {					
		createAccount();	
		createSubscriber();
		activateSubscriber();
		withTestDataFrom("cmb/moonwalk/testdata-TaxOnBillApplyChargeAndAdjustment-$DATADIR.csv").run(billingSteps).applyChargesAndAdjustmentsWithTax(ban, subscriberId);
		cleanup();
	}
		
	@Step
	private void createAccount() throws Exception {			
		CreatePostpaidAccount createPostpaid = WALMSHelper.createDefaultPostpaidAccount(accountType, accountSubType, homeProvince);
		OriginatingUserType createPostpaidHeader = WALMSHelper.createDefaultAccountHeader();
		ban = activationSteps.createPostpaidAccount(createPostpaid, createPostpaidHeader);
	}
	
	@Step
	private void createSubscriber() throws Exception {	   	
		String simSerialNumber = activationSteps.getAvailableSimNumber();
		ReservePhoneNumberForSubscriber reservePhoneNumber = SLCMSHelper.createDefaultReservePhoneNumber(ban, productType, phoneNumberPattern, simSerialNumber);
		ReservePhoneNumberForSubscriberResponse reserveResponse = activationSteps.reservePhoneNumber(reservePhoneNumber, accountType, accountSubType, productType, equipmentType, marketAreaCode);
		phoneNumber = reserveResponse.getPhoneNumber();
		subscriberId = reserveResponse.getSubscriberId();
		cleanUpState = CLEAN_UP_RELEASE;
	}
	
	@Step
	private void activateSubscriber() throws Exception {
		Date startServiceDate = activationSteps.getLogicalDate();
		EquipmentActivated equipmentRequest = activationSteps.getAvailableUSimEquipment(DefaultValues.BRAND_ID_TELUS);
		activationSteps.activateSubscriber(SMSHelper.createDefaultActivateSubscriber(ban, subscriberId, phoneNumber, marketAreaCode, productType, startServiceDate, equipmentRequest, 
				pricePlan, services));
		activationSteps.verifyThatSubscriberIsActive(phoneNumber);	
		cleanUpState = CLEAN_UP_CANCEL;
	}

	@Step
	private void cleanup() {
		try {
			if (CLEAN_UP_CANCEL.equals(cleanUpState)) {
				activationSteps.cancelSubscriber("18654", "apollo", ApplicationSummary.APP_SD, phoneNumber, "AIE", 'R');
			} else if (CLEAN_UP_RELEASE.equals(cleanUpState)){
				activationSteps.releasePhoneNumber(SLCMSHelper.createDefaultReleasePhoneNumber(ban, subscriberId, productType));	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
