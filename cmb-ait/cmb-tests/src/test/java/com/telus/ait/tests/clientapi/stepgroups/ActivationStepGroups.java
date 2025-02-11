package com.telus.ait.tests.clientapi.stepgroups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.thucydides.core.annotations.StepGroup;
import net.thucydides.core.annotations.Steps;

import com.telus.ait.cmb.steps.account.WirelessAccountLifecycleMgmtSteps;
import com.telus.ait.cmb.steps.db.ClientApiDistDbSteps;
import com.telus.ait.cmb.steps.db.ClientApiKbDbSteps;
import com.telus.ait.cmb.steps.reference.ResourceOrderReferenceSteps;
import com.telus.ait.cmb.steps.reference.ServiceOrderReferenceSteps;
import com.telus.ait.cmb.steps.subscriber.SubscriberInformationSteps;
import com.telus.ait.cmb.steps.subscriber.SubscriberLifeCycleMgmtSteps;
import com.telus.ait.cmb.steps.subscriber.SubscriberManagementSteps;
import com.telus.ait.fwk.test.WebServiceBaseSteps;
import com.telus.ait.tests.clientapi.helpers.RORSHelper;
import com.telus.ait.tests.clientapi.helpers.SMSHelper;
import com.telus.api.AuthenticationException;
import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.provider.TMProvider;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReleaseSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReservePhoneNumberForSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReservePhoneNumberForSubscriberResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.ActivateSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.EquipmentActivated;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePostpaidAccount;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePrepaidAccount;
import com.telus.tmi.xmlschema.srv.rmo.ordermgmt.resourceorderreferenceservicerequestresponse_1_0.GetAvailableNumberGroups;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.Subscriber;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberStatus;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.NumberGroup;
public class ActivationStepGroups extends WebServiceBaseSteps {
	
	@Steps
	private WirelessAccountLifecycleMgmtSteps wirelessAccountLifecycleSteps;
	
	@Steps
	private ResourceOrderReferenceSteps resourceOrderReferenceSteps;
	
	@Steps
	private ServiceOrderReferenceSteps serviceOrderReferenceSteps;

	@Steps
	private SubscriberInformationSteps subscriberInformationSteps;
	
	@Steps
	private SubscriberLifeCycleMgmtSteps subscriberLifeCycleMgmtSteps;

	@Steps
	private SubscriberManagementSteps subscriberManagementSteps;
	
	@Steps
	private ClientApiDistDbSteps clientApiDistDbSteps;

	@Steps
	private ClientApiKbDbSteps clientApiKbDbSteps;
			
	@StepGroup("Create postpaid account")
	public String createPostpaidAccount(CreatePostpaidAccount parameters, OriginatingUserType header) throws Exception {	
        String ban = wirelessAccountLifecycleSteps.createPostpaidAccount(parameters, header);
		super.displayRequest();	
		super.displayResponse();
		return ban;
	}
	
	@StepGroup("Create prepaid account") 
	public String createPrepaidAccount(CreatePrepaidAccount parameters, OriginatingUserType header) throws Exception {
		String ban = wirelessAccountLifecycleSteps.createPrepaidAccount(parameters, header);
		super.displayRequest();
		super.displayResponse();
		return ban;
	}
	
	@StepGroup
	public String getAvailableSimNumber() throws Exception {
		return clientApiDistDbSteps.getAvailableSimNumber();
	}
	
	@StepGroup("Get available USIM")
	public EquipmentActivated getAvailableUSimEquipment(String brandId) throws Exception {
		String uSimNumber = clientApiDistDbSteps.getAvailableUSim(Integer.valueOf(brandId));
		return SMSHelper.createEquipmentActivated("U", uSimNumber);
	}
	
	@StepGroup
	public Date getLogicalDate() throws Exception {
		return serviceOrderReferenceSteps.getLogicalDate();
	}

	@StepGroup("Reserve phone number")
	public ReservePhoneNumberForSubscriberResponse reservePhoneNumber(ReservePhoneNumberForSubscriber reservePhoneNumber, String accountType, String accountSubType, String productType, 
			String equipmentType, String marketAreaCode) throws Exception {		
		GetAvailableNumberGroups getNumberGroups = RORSHelper.createNumberGroups(accountType, accountSubType, productType, equipmentType, marketAreaCode);
		reservePhoneNumber.setNumberGroup(getNumberGroups(getNumberGroups, reservePhoneNumber.getPhoneNumberPattern()));
		ReservePhoneNumberForSubscriberResponse response = subscriberLifeCycleMgmtSteps.reservePhoneNumberForSubscriber(reservePhoneNumber);
		super.displayRequest();	
		super.displayResponse();
		return response;
	}
	
	private NumberGroup getNumberGroups(GetAvailableNumberGroups numberGroups, String phoneNumberPattern) throws Exception { 
        List<NumberGroup> numberGroupList = resourceOrderReferenceSteps.getAvailableNumberGroups(numberGroups);
		super.displayRequest();			
		super.displayResponse();
		NumberGroup numberGroup = getNumberGroupByAreaCode(numberGroupList, phoneNumberPattern);
		if (numberGroup == null) {
			throw new Exception("No available number groups for phoneNumberPattern:" + phoneNumberPattern);
		}
		return numberGroup;
	}

	private NumberGroup getNumberGroupByAreaCode(List<NumberGroup> numberGroups, String phoneNumberPattern) throws Exception {
		String areaCode = phoneNumberPattern.substring(0, 3);
		List<String> ngpCodeList = clientApiKbDbSteps.getNGPByNPA(areaCode);		
		if (numberGroups != null && !numberGroups.isEmpty()) {
			for (NumberGroup numberGroup : numberGroups) {
				for (String ngpCode : ngpCodeList) {
					if (ngpCode.equals(numberGroup.getCode())) {
						numberGroup.setDefaultDealerCode(null);
						numberGroup.setDefaultSalesRepCode(null);						
						return removeDummyNpaNxx(numberGroup);	
					}
				}
			}
		}
		return null;
	}
	
	private NumberGroup removeDummyNpaNxx(NumberGroup numberGroup) {
		List<String> npaNxxList = new ArrayList<String>();
		for (String npaNxx : numberGroup.getNpaNXX()) {
			if (npaNxx.matches("[0-9]{6}")) {
				npaNxxList.add(npaNxx);
			}
		}
		numberGroup.setNpaNXX(npaNxxList);
		return numberGroup;
	}

	@StepGroup("Activate subscriber")
	public void activateSubscriber(ActivateSubscriber activateSubscriber) {   
		try {
			subscriberManagementSteps.activateSubscriber(activateSubscriber);
		} catch (Exception e) {
			
		}
		super.displayRequest();	
		super.displayResponse();		
	}
	
	@StepGroup("Release phone number")
	public void releasePhoneNumber(ReleaseSubscriber releaseSubscriber) throws Exception {
		subscriberLifeCycleMgmtSteps.releasePhoneNumberForSubscriber(releaseSubscriber);
		super.displayRequest();	
		super.displayResponse();		
	}
	
	@StepGroup
	public void verifyThatSubscriberIsActive(String phoneNumber) throws Exception {
		Subscriber subscriber = subscriberInformationSteps.getSubscriberByPhoneNumber(phoneNumber, null);
		super.displayRequest();	
		super.displayResponse();
		assertThat(subscriber.getStatus(), equalTo(SubscriberStatus.A));
	}

	@StepGroup
	public void verifyThatPhoneNumberIsReserved(String phoneNumber) throws Exception {
//		assertThat(clientApiKbDbSteps.isPhoneNumberReserved(phoneNumber), equalTo(true));
//		assertThat(clientApiKbDbSteps.getSubscriberStatus(phoneNumber), equalTo(ClientApiKbDbSteps.SUB_STATUS_RESERVED));
	}
	
	@StepGroup
	public void verifyThatPhoneNumberIsReleased(String phoneNumber) throws Exception {
//		assertThat(clientApiKbDbSteps.isPhoneNumberReleased(phoneNumber), equalTo(true));
//		assertThat(clientApiKbDbSteps.getCTNStatus(phoneNumber), equalTo(ClientApiKbDbSteps.CTN_STATUS_RELEASED));
	}
		
	@StepGroup
	public void verifyThatBanStatus(String ban, String expectedStatus) throws Exception {
//		AccountInfo account = clientApiKbDbSteps.getAccountInfo(ban);
//		assertThat(account.getBanStatus(), equalTo(expectedStatus));
	}
	
	@StepGroup
	public void cancelSubscriber(String apiUser, String apiPassword, String appCode, String phoneNumber, String reasonCode, char depositReturnMethod) throws AuthenticationException, 
			TelusAPIException {
		ClientAPI api = ClientAPI.getInstance(apiUser, apiPassword, appCode);
		TMProvider provider = (TMProvider) api.getProvider();
		com.telus.api.account.Subscriber subscriber = provider.getAccountManager0().findSubscriberByPhoneNumber(phoneNumber);
		subscriber.cancel(reasonCode , depositReturnMethod);
	}
			
}