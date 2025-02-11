package com.telus.ait.tests.clientapi.wome;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.WithTagValuesOf;
import net.thucydides.junit.annotations.UseTestDataFrom;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.telus.ait.fwk.test.WebServiceBaseTest;
import com.telus.ait.tests.clientapi.stepgroups.ActivationStepGroups;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.ServicesValidation;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePostpaidAccount;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ActivationOption;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.AuditInfo;

/**
 * Created by wcheong on 10/12/2015.
 */
@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value= "cmb/testdata-SubscriberActivations.csv")
@WithTagValuesOf({"epic: Regression", "test type: web service", "test style: structured (recommended)"})
public class WOMESubscriberDataSharingTests extends WebServiceBaseTest {
	
	@Steps
	private ActivationStepGroups steps;
	
	//Some hardcoded values...
    private Boolean likeMatchInd = Boolean.FALSE;
    private Boolean asianFriendlyInd = Boolean.FALSE;
    private Boolean portInReserveRequiredInd = Boolean.FALSE;
    private Boolean offlineReservationInd = Boolean.FALSE;
    private Boolean cancelPortInInd = Boolean.FALSE; 
    private Boolean dealerHasDepositInd = Boolean.FALSE; 
    private Boolean waiveSearchFeeInd = Boolean.FALSE; 
    private Boolean notificationSuppressionInd = Boolean.FALSE; 
    private Boolean activationInd = Boolean.TRUE;
    private Boolean renewalInd = Boolean.FALSE;
    private Boolean equipmentServiceMatchInd = Boolean.FALSE;
    private Boolean pricePlanServiceGroupingInd = Boolean.FALSE;
    private Boolean provinceServiceMatchInd = Boolean.FALSE;
    private Date startServiceDate = Calendar.getInstance().getTime();
    private String memoText = "Subscriber Activation Automated Testcase from Client API";
    private ActivationOption activationOption = null;
    private com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.PortInEligibility slcmsPortInEligibility = null;
    private com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.PortInEligibility smsPortInEligibility = null;
    	
    private CreatePostpaidAccount createPostpaidAccountParameters;
    private OriginatingUserType createPostpaidAccountHeader;
    private UserServiceProfile userServiceProfile;
    private ServicesValidation servicesValidation;
    private ServiceAgreement serviceAgreement;
    private ServiceRequestHeader serviceRequestHeader;
    private AuditInfo auditInfo;
    
	@Before
	public void setUp() throws ParseException {
//		createPostpaidAccountHeader = WALMSHelper.createPostpaidAccountHeader(custId, ipAddress, appId, auditUserId, appIpAddress);
//		createPostpaidAccountParameters = WALMSHelper.createPostpaidAccountParameters(accountType, accountSubType, brandId, homeProvince, pin, email, language, dealerCode, salesRepCode, 
//				contactPhone, homePhone, title, firstName, lastName, nameFormat, addressType, city, provinceCode, postalCode, country, streetName, sin, birthDate, birthDateFormat, auditUserId, 
//				auditSalesRepId, channelOrgId, outletId, originatorAppId);
//		userServiceProfile = SLCMSHelper.createUserServiceProfile(dealerCode, salesRepCode);		
//		servicesValidation = SMSHelper.createServicesValidation(equipmentServiceMatchInd, pricePlanServiceGroupingInd, provinceServiceMatchInd);
//		serviceAgreement = SMSHelper.createServiceAgreement(activationInd, renewalInd, activityReasonCode, contractTerm, TransactionType.ADD.value(), pricePlan, TransactionType.ADD.value(), 
//				"P", services, "R", TransactionType.ADD);
//		serviceRequestHeader = SMSHelper.createServiceRequestHeader(appId, language);
//		auditInfo = SMSHelper.createAuditInfo(auditUserId, auditSalesRepId, channelOrgId, outletId, originatorAppId);
	}

	@Test
	public void activateNewSubscribers() throws Exception {
//		steps.setUpTestForEnvironment(environment());
//		String ban = steps.createPostpaidAccount(createPostpaidAccountParameters, createPostpaidAccountHeader); 			
//		NumberGroup numberGroup = steps.getNumberGroups(accountType, accountSubType, productType, equipmentType, provinceCode, phoneNumberPattern);
//		ReservePhoneNumberResponse reserveResponse = steps.reservePhoneNumber(ban, productType, phoneNumberPattern, likeMatchInd, asianFriendlyInd, portInReserveRequiredInd, 
//				slcmsPortInEligibility, offlineReservationInd, numberGroup, userServiceProfile);
//		String phoneNumber = reserveResponse.phoneNumber;
//		String subscriberId = reserveResponse.subscriberId;
//		ActivationSubscriberData subscriberData = SMSHelper.createSubscriberData(ban, subscriberId, phoneNumber, brandId, provinceCode, productType, title, firstName, lastName, nameFormat, 
//				addressType, city, provinceCode, postalCode, country, streetName, email, language);
//		try {
////			steps.activateSubscriber(userServiceProfile, activationOption, dealerHasDepositInd, activityReasonCode, startServiceDate, activationFeeChargeCode, waiveSearchFeeInd, memoText, 
////					subscriberData, servicesValidation, serviceAgreement, smsPortInEligibility, subscriberId, serviceRequestHeader, auditInfo, notificationSuppressionInd);
////			steps.verifyThatSubscriberIsActive(phoneNumber);
//		} finally {
//			steps.releasePhoneNumber(ban, subscriberId, ban, cancelPortInInd, phoneNumber, subscriberId, slcmsPortInEligibility);
//		}
	}
	
	//CSV Input Variables
	//Header
	public String custId;
	public String ipAddress;
	public String appId;
	public String userId;
	public String appIpAddress;    
		    	
	//BaseAccount
	public String accountType;
	public String accountSubType;
	public String brandId;
	public String homeProvince;
	public String pin;
	public String email;
	public String language;
	public String dealerCode;
	public String salesRepCode;
	public String contactPhone;
	public String homePhone;
		
	//ConsumerName
	public String title;
	public String firstName;
	public String lastName;
	public String nameFormat;
		
	//Address
	public String addressType;
	public String city;
	public String provinceCode;
	public String postalCode;
	public String country;
	public String streetName;
	
	//PersonalCreditInfo
	public String sin;
	public String birthDate;
	public String birthDateFormat;
	
	//AuditInfo
	public String auditUserId;
	public String auditSalesRepId;
	public String channelOrgId;
	public String outletId;
	public String originatorAppId;
		
	//SubscriberInfo
	public String productType;
	public String equipmentType;
	public String phoneNumberPattern;
		
	//Activation
	public String activityReasonCode;
	public String activationFeeChargeCode;
	public String subscriptionRoleCode;
	public String reasonCode;
	public String contractTerm;
	public String pricePlan;
	public String services;
		
}
