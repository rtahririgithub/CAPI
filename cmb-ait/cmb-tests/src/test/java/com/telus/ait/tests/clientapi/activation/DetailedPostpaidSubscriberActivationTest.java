package com.telus.ait.tests.clientapi.activation;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.junit.annotations.UseTestDataFrom;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.telus.ait.fwk.test.WebServiceBaseTest;
import com.telus.ait.tests.clientapi.helpers.CommonHelper;
import com.telus.ait.tests.clientapi.helpers.SLCMSHelper;
import com.telus.ait.tests.clientapi.helpers.SMSHelper;
import com.telus.ait.tests.clientapi.helpers.WALMSHelper;
import com.telus.ait.tests.clientapi.stepgroups.ActivationStepGroups;
import com.telus.api.reference.ApplicationSummary;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReservePhoneNumberForSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReservePhoneNumberForSubscriberResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.ActivateSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.ActivationSubscriberData;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.EquipmentActivated;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.ServicesValidation;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.BaseWirelessAccount;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePostpaidAccount;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.Address;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ConsumerName;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ActivationOption;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TransactionType;

/**
 * Created by wcheong on 10/12/2015.
 */
@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value= "cmb/regression/activation/testdata-PostpaidDetailedSubscriberActivation-$DATADIR.csv")
@WithTagValuesOf({"epic: Regression (Detailed)", "test type: web service", "test style: structured (recommended)"})
public class DetailedPostpaidSubscriberActivationTest extends WebServiceBaseTest {
	
	@Steps
	private ActivationStepGroups steps;		  
            
    //Some hardcoded values...
    private Boolean likeMatchInd = Boolean.FALSE;
    private Boolean asianFriendlyInd = Boolean.FALSE;
    private Boolean offlineReservationInd = Boolean.FALSE;
    private Boolean equipmentServiceMatchInd = Boolean.FALSE;
    private Boolean pricePlanServiceGroupingInd = Boolean.FALSE;
    private Boolean provinceServiceMatchInd = Boolean.FALSE;
    private Boolean dealerHasDepositInd = Boolean.FALSE; 
    private Boolean waiveSearchFeeInd = Boolean.FALSE; 
    private Boolean notificationSuppressionInd = Boolean.FALSE; 
    private ActivationOption activationOption = null;
    
	//CSV Input Variables	
    public String ban, subscriberId, phoneNumber;																											//Override values (non-new)
    public String dealerCode, salesRepCode;																													//UserServiceProfile
	public String auditUserId, auditSalesRepId, channelOrgId, outletId, originatorAppId; 																	//AuditInfo	
	public String custId, ipAddress, appId, userId, appIpAddress; 																							//Header 	    
	public String accountType, accountSubType, brandId, homeProvince, pin, email, language, contactPhone, homePhone; 										//BaseAccount			
	public String title, firstName, lastName, nameFormat; 																									//ConsumerName			
	public String addressType, city, provinceCode, postalCode, country, streetName; 																		//Address	
	public String sin, birthDate, birthDateFormat; 																											//PersonalCreditInfo	
	public String productType, equipmentType, phoneNumberPattern; 																							//SubscriberInfo	
	public String deviceEquipmentType, serialNumber;																										//Device
	public String activityReasonCode, activationFeeChargeCode, subscriptionRoleCode, reasonCode, contractTerm, pricePlan, services,  						//Activation
			startServiceDate, startServiceDateFormat, memoText, marketAreaCode;
    
	@Before
	public void setUp() throws Exception {	
    	setupAccount();	
	}
	
	@Test
	public void reserveAndReleasePhoneNumber() throws Exception {
		reservePhoneNumber();			
		steps.releasePhoneNumber(SLCMSHelper.createReleasePhoneNumber(ban, subscriberId, productType, dealerCode, salesRepCode, false, null));
		steps.verifyThatPhoneNumberIsReleased(phoneNumber);
	}
	
	@Test
	public void activateNewSubscriber() throws Exception {
		reservePhoneNumber();
		activateSubscriber();
		steps.cancelSubscriber("18654", "apollo", ApplicationSummary.APP_SD, phoneNumber, "AIE", 'R');		
	}

	private void setupAccount() throws Exception {		
		if (ban == null || CommonHelper.WILDCARD.equals(ban)) {
			ConsumerName consumerName = WALMSHelper.createConsumerName(title, firstName, lastName, nameFormat);
			Address address = WALMSHelper.createAddress(addressType, city, provinceCode, postalCode, country, streetName);
			PersonalCreditInformation personalCreditInfo = WALMSHelper.createPersonalCreditInfo(sin, birthDateFormat, birthDate);
			BaseWirelessAccount baseAccount = WALMSHelper.createBaseWirelessAccount(accountType, accountSubType, brandId, homeProvince, pin, email, language, dealerCode, salesRepCode, 
	    			contactPhone, homePhone, consumerName, address, personalCreditInfo);	
			CreatePostpaidAccount createPostpaid = WALMSHelper.createPostpaidAccountParameters(baseAccount, WALMSHelper.createAuditInfo(auditUserId, auditSalesRepId, channelOrgId, outletId, 
		    		originatorAppId));
			OriginatingUserType createPostpaidHeader = WALMSHelper.createAccountHeader(custId, ipAddress, appId, auditUserId, appIpAddress);
			ban = steps.createPostpaidAccount(createPostpaid, createPostpaidHeader);	
		}
	}
	
	private void reservePhoneNumber() throws Exception {	   	
		String simSerialNumber = steps.getAvailableSimNumber();
		ReservePhoneNumberForSubscriber reservePhoneNumber = SLCMSHelper.createReservePhoneNumber(ban, productType, phoneNumberPattern, dealerCode, salesRepCode, simSerialNumber, 
				likeMatchInd, asianFriendlyInd, offlineReservationInd, null, null);
		ReservePhoneNumberForSubscriberResponse reserveResponse = steps.reservePhoneNumber(reservePhoneNumber, accountType, accountSubType, productType, equipmentType, marketAreaCode);
		phoneNumber = reserveResponse.getPhoneNumber();
		subscriberId = reserveResponse.getSubscriberId();
		steps.verifyThatPhoneNumberIsReserved(phoneNumber);
	}
	
	private void activateSubscriber() throws Exception {
		ServicesValidation servicesValidation = SMSHelper.createServicesValidation(equipmentServiceMatchInd, pricePlanServiceGroupingInd, provinceServiceMatchInd);		
		ServiceAgreement serviceAgreement = SMSHelper.createServiceAgreement(
				SMSHelper.createCommitment(true, false, reasonCode, contractTerm, TransactionType.ADD.value(), SMSHelper.createTimePeriod(contractTerm)), 
				SMSHelper.createPricePlan(pricePlan, "P", TransactionType.ADD.value()), 
				SMSHelper.createContractServices(services, "R", TransactionType.ADD.value()));		
		ActivationSubscriberData subscriberData = SMSHelper.createSubscriberData(ban, subscriberId, phoneNumber, brandId, marketAreaCode, productType, title, firstName, lastName, nameFormat, 
				addressType, city, provinceCode, postalCode, country, streetName, email, language);	
		ActivateSubscriber request = SMSHelper.createActivateSubscriber(activityReasonCode, activationFeeChargeCode, memoText, subscriptionRoleCode, getStartServiceDate(), waiveSearchFeeInd, 
				dealerHasDepositInd, notificationSuppressionInd, activationOption, subscriberData, servicesValidation, serviceAgreement, getEquipmentRequest(), 
				SMSHelper.createUserServiceProfile(dealerCode, salesRepCode), SMSHelper.createServiceRequestHeader(appId, language), 
				SMSHelper.createAuditInfo(auditUserId, auditSalesRepId, channelOrgId, outletId, originatorAppId), null);			
		steps.activateSubscriber(request);
		steps.verifyThatSubscriberIsActive(phoneNumber);
		
	}

	private EquipmentActivated getEquipmentRequest() throws Exception {
		 if (serialNumber == null || CommonHelper.WILDCARD.equals(serialNumber)) {			 
			 return steps.getAvailableUSimEquipment(brandId);
		 } 
		 return SMSHelper.createEquipmentActivated(deviceEquipmentType, serialNumber);
	}

	private Date getStartServiceDate() throws Exception {
		try {
			return new SimpleDateFormat(startServiceDateFormat).parse(startServiceDate);
		} catch (Exception e) {
			return steps.getLogicalDate();
		}
	}
		
}
