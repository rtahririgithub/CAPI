package com.telus.cmb.unit.test.subscriber.svc.facade;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.mockito.Mock;

import com.telus.api.account.Account;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.SeatData;
import com.telus.api.account.Subscriber;
import com.telus.api.resource.Resource;
import com.telus.cmb.unit.test.subscriber.svc.facade.ServiceEditionConstants.ServiceEdition;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.VOIPAccountInfo;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceEditionInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class SLFBusinessConnectUnitTestHelper extends SLFBaseUnitTestHelper {

	@Mock protected AccountInfo bcAccount;
	@Mock protected SubscriberInfo bcSubscriber;
	@Mock protected AddressInfo bcAddress;
	@Mock protected SeatData starterSeatData;
	@Mock protected SeatData officeSeatData;
	@Mock protected ConsumerName contactName;
	@Mock protected SubscriberContractInfo subscriberContractInfo;
	@Mock protected ServiceAgreementInfo addEnterpriseNationalSoc;
	@Mock protected ServiceAgreementInfo removeEnterpriseNationalSoc;
	@Mock protected ServiceAgreementInfo addPremiumNationalSoc;	
	@Mock protected ServiceAgreementInfo addPremiumQuebecSoc;
	@Mock protected PricePlanInfo standardNationalPricePlan;
	@Mock protected PricePlanInfo standardQuebecPricePlan;
	@Mock protected PricePlanInfo premiumNationalPricePlan;
	@Mock protected PricePlanInfo premiumQuebecPricePlan;
	@Mock protected PricePlanInfo enterpriseNationalPricePlan;
	@Mock protected PricePlanInfo enterpriseQuebecPricePlan;
	@Mock protected PricePlanInfo nonBusinessConnectPricePlan;
	@Mock protected ServiceInfo enterpriseNationalSoc;
	@Mock protected ServiceInfo premiumNationalSoc;
	@Mock protected ServiceInfo premiumQuebecSoc;
	@Mock protected VOIPAccountInfo voipAccount;
	
	@Mock protected Resource primaryResource;
	@Mock protected Resource tollFreeResource1;
	@Mock protected Resource tollFreeResource2;
	@Mock protected Resource otherResource;
	@Mock protected Resource additionalVoipResource;
	
	protected String primaryResourceNumber = "1111111";
	protected String tollFreeResource1Number = "2222222";
	protected String tollFreeResource2Number = "3333333";
	protected String otherResourceNumber = "4444444";
	protected String additionalVoipResourceNumber = "5555555";
	protected long subscriptionId1 = 123456;
	protected long subscriptionId2 = 234567;
	protected int ban1 = 123456789;
	protected List<ServiceEditionInfo> serviceEditions = new ArrayList<ServiceEditionInfo>();
		
	public void setupAccount() {
		when(bcAccount.getStatus()).thenReturn(Account.STATUS_TENTATIVE);
		when(bcAccount.getActiveSubscribersCount()).thenReturn(0);
		when(bcAccount.getReservedSubscribersCount()).thenReturn(0);
		when(bcAccount.getContactName()).thenReturn(contactName);
	}
	
	public void setupSubscriber() {
		Calendar c = Calendar.getInstance();
		when(bcSubscriber.getStartServiceDate()).thenReturn(c.getTime());
		when(bcSubscriber.getBanId()).thenReturn(1);		
		when(bcSubscriber.getBrandId()).thenReturn(1);	
		when(bcSubscriber.getSeatData()).thenReturn(starterSeatData);
		when(bcSubscriber.getSubscriptionId()).thenReturn(subscriptionId1);		
	}
	
	public void setupServiceEditions() {
		ServiceEditionInfo nationalStandard = new ServiceEditionInfo();
		nationalStandard.setCode(ServiceEdition.NATIONAL_STANDARD.getCode());
		ServiceEditionInfo nationalPremium = new ServiceEditionInfo();
		nationalPremium.setCode(ServiceEdition.NATIONAL_PREMIUM.getCode());
		ServiceEditionInfo nationalEnterprise = new ServiceEditionInfo();
		nationalEnterprise.setCode(ServiceEdition.NATIONAL_ENTERPRISE.getCode());
		ServiceEditionInfo quebecStandard = new ServiceEditionInfo();
		quebecStandard.setCode(ServiceEdition.QUEBEC_STANDARD.getCode());
		ServiceEditionInfo quebecPremium = new ServiceEditionInfo();
		quebecPremium.setCode(ServiceEdition.QUEBEC_PREMIUM.getCode());
		ServiceEditionInfo quebecEnterprise = new ServiceEditionInfo();
		quebecEnterprise.setCode(ServiceEdition.QUEBEC_ENTERPRISE.getCode());
		serviceEditions.add(nationalStandard);
		serviceEditions.add(nationalPremium);
		serviceEditions.add(nationalEnterprise);
		serviceEditions.add(quebecStandard);
		serviceEditions.add(quebecPremium);
		serviceEditions.add(quebecEnterprise);
	}
	
	public void setupServices() {
		when(enterpriseNationalSoc.containsSwitchCode(anyString())).thenReturn(false);
		when(enterpriseNationalSoc.containsSwitchCode(ServiceEdition.NATIONAL_ENTERPRISE.getCode())).thenReturn(true);
		when(premiumQuebecSoc.containsSwitchCode(anyString())).thenReturn(false);
		when(premiumQuebecSoc.containsSwitchCode(ServiceEdition.QUEBEC_PREMIUM.getCode())).thenReturn(true);
		when(premiumNationalSoc.containsSwitchCode(anyString())).thenReturn(false);
		when(premiumNationalSoc.containsSwitchCode(ServiceEdition.NATIONAL_PREMIUM.getCode())).thenReturn(true);
		
		when(addEnterpriseNationalSoc.getService0()).thenReturn(enterpriseNationalSoc);		
		when(addEnterpriseNationalSoc.getTransaction()).thenReturn(BaseAgreementInfo.ADD);
		when(removeEnterpriseNationalSoc.getService0()).thenReturn(enterpriseNationalSoc);		
		when(removeEnterpriseNationalSoc.getTransaction()).thenReturn(BaseAgreementInfo.DELETE);
		when(addPremiumQuebecSoc.getService0()).thenReturn(premiumQuebecSoc);		
		when(addPremiumQuebecSoc.getTransaction()).thenReturn(BaseAgreementInfo.ADD);		
		when(addPremiumNationalSoc.getService0()).thenReturn(premiumNationalSoc);		
		when(addPremiumNationalSoc.getTransaction()).thenReturn(BaseAgreementInfo.ADD);
		
		when(standardNationalPricePlan.containsSwitchCode(anyString())).thenReturn(false);
		when(standardNationalPricePlan.containsSwitchCode(ServiceEdition.NATIONAL_STANDARD.getCode())).thenReturn(true);
		when(standardQuebecPricePlan.containsSwitchCode(anyString())).thenReturn(false);
		when(standardQuebecPricePlan.containsSwitchCode(ServiceEdition.QUEBEC_STANDARD.getCode())).thenReturn(true);
		when(premiumNationalPricePlan.containsSwitchCode(anyString())).thenReturn(false);
		when(premiumNationalPricePlan.containsSwitchCode(ServiceEdition.NATIONAL_PREMIUM.getCode())).thenReturn(true);
		when(premiumQuebecPricePlan.containsSwitchCode(anyString())).thenReturn(false);
		when(premiumQuebecPricePlan.containsSwitchCode(ServiceEdition.QUEBEC_PREMIUM.getCode())).thenReturn(true);
		when(enterpriseNationalPricePlan.containsSwitchCode(anyString())).thenReturn(false);
		when(enterpriseNationalPricePlan.containsSwitchCode(ServiceEdition.NATIONAL_ENTERPRISE.getCode())).thenReturn(true);
		when(enterpriseQuebecPricePlan.containsSwitchCode(anyString())).thenReturn(false);
		when(enterpriseQuebecPricePlan.containsSwitchCode(ServiceEdition.QUEBEC_ENTERPRISE.getCode())).thenReturn(true);
		when(nonBusinessConnectPricePlan.containsSwitchCode(anyString())).thenReturn(false);
	}
	
	public void setupResources() {
		when(primaryResource.getResourceType()).thenReturn(Subscriber.RESOURCE_TYPE_PRIMARY_VOIP);
		when(primaryResource.getResourceNumber()).thenReturn(primaryResourceNumber);

		when(tollFreeResource1.getResourceType()).thenReturn(Subscriber.RESOURCE_TYPE_TOLLFREE_VOIP);
		when(tollFreeResource1.getResourceNumber()).thenReturn(tollFreeResource1Number);
		
		when(tollFreeResource2.getResourceType()).thenReturn(Subscriber.RESOURCE_TYPE_TOLLFREE_VOIP);
		when(tollFreeResource2.getResourceNumber()).thenReturn(tollFreeResource2Number);
		
		when(otherResource.getResourceType()).thenReturn(Subscriber.RESOURCE_TYPE_PHONE_NUMBER);
		when(otherResource.getResourceNumber()).thenReturn(otherResourceNumber);
		
		when(additionalVoipResource.getResourceType()).thenReturn(Subscriber.RESOURCE_TYPE_ADDITIONAL_VOIP);
		when(additionalVoipResource.getResourceNumber()).thenReturn(additionalVoipResourceNumber);		
	}
	
	public void setupVOIPAccount() {
		when(voipAccount.getMainCompanyNumber()).thenReturn(tollFreeResource1Number);
		when(voipAccount.getOperatorSubscriptionId()).thenReturn(subscriptionId1);
		when(voipAccount.getServiceEditionCode()).thenReturn("TEST");
	}
		
}
