package com.telus.cmb.endpoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.endpoint.mapper.CallingCircleInformationMapper;
import com.telus.cmb.endpoint.mapper.SubscriberMemoMapper;
import com.telus.cmb.endpoint.mapper.SubscriberPortOutEligibilityInfoMapper;
import com.telus.cmb.endpoint.mapper.airtime.ServiceAirTimeAllocationMapper;
import com.telus.cmb.endpoint.mapper.airtime.ServiceIdentityMapper;
import com.telus.cmb.endpoint.mapper.datasharing.SubscriberDataSharingInfoMapper;
import com.telus.cmb.endpoint.mapper.subscriber.PhoneNumberSearchOptionMapper;
import com.telus.cmb.endpoint.mapper.subscriber.SeatResourceMapper;
import com.telus.cmb.endpoint.mapper.subscriber.SubscriberIdentifierMapper;
import com.telus.cmb.endpoint.mapper.subscriber.SubscriberListByDataSharingGroupMapper;
import com.telus.cmb.framework.endpoint.EndpointOperation;
import com.telus.cmb.framework.endpoint.EndpointProviderV2;
import com.telus.cmb.jws.ServiceErrorCodes;
import com.telus.cmb.jws.ServicePolicyException;
import com.telus.cmb.jws.mapping.customer_common.CustomerCommonMapper_30;
import com.telus.cmb.jws.mapping.subscriber.information_types_30.SubscriberMapper_30;
import com.telus.cmb.jws.mapping.subscriber.information_types_30.SubscriptionRoleMapper;
import com.telus.cmb.schema.CheckPortOutEligibility;
import com.telus.cmb.schema.CheckPortOutEligibilityResponse;
import com.telus.cmb.schema.CreateMemo;
import com.telus.cmb.schema.CreateMemoResponse;
import com.telus.cmb.schema.GetAirTimeAllocationList;
import com.telus.cmb.schema.GetAirTimeAllocationListResponse;
import com.telus.cmb.schema.GetCallingCircleInformation;
import com.telus.cmb.schema.GetCallingCircleInformationResponse;
import com.telus.cmb.schema.GetLatestSubscriberByPhoneNumber;
import com.telus.cmb.schema.GetLatestSubscriberByPhoneNumberResponse;
import com.telus.cmb.schema.GetSubscriberByPhoneNumber;
import com.telus.cmb.schema.GetSubscriberByPhoneNumberResponse;
import com.telus.cmb.schema.GetSubscriberBySubscriberId;
import com.telus.cmb.schema.GetSubscriberBySubscriberIdResponse;
import com.telus.cmb.schema.GetSubscriberBySubscriptionId;
import com.telus.cmb.schema.GetSubscriberBySubscriptionIdResponse;
import com.telus.cmb.schema.GetSubscriberDataSharingInfoList;
import com.telus.cmb.schema.GetSubscriberDataSharingInfoListResponse;
import com.telus.cmb.schema.GetSubscriberIdListByServiceGroupFamily;
import com.telus.cmb.schema.GetSubscriberIdListByServiceGroupFamilyResponse;
import com.telus.cmb.schema.GetSubscriberIdentifierByPhoneNumberAndAccountNumber;
import com.telus.cmb.schema.GetSubscriberIdentifierByPhoneNumberAndAccountNumberResponse;
import com.telus.cmb.schema.GetSubscriberIdentifierBySubscriptionId;
import com.telus.cmb.schema.GetSubscriberIdentifierBySubscriptionIdResponse;
import com.telus.cmb.schema.GetSubscriberIdentifierListByAccountNumber;
import com.telus.cmb.schema.GetSubscriberIdentifierListByAccountNumberResponse;
import com.telus.cmb.schema.GetSubscriberListByAccountNumber;
import com.telus.cmb.schema.GetSubscriberListByAccountNumberResponse;
import com.telus.cmb.schema.GetSubscriberListByDataSharingGroupList;
import com.telus.cmb.schema.GetSubscriberListByDataSharingGroupListResponse;
import com.telus.cmb.schema.GetSubscriberListByImsi;
import com.telus.cmb.schema.GetSubscriberListByImsiResponse;
import com.telus.cmb.schema.GetSubscriberListByPhoneNumber;
import com.telus.cmb.schema.GetSubscriberListByPhoneNumberResponse;
import com.telus.cmb.schema.IncludeStatusOption;
import com.telus.cmb.schema.PhoneNumberSearchOption;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.account.info.EnterpriseAddressInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.ServiceIdentityInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.ServiceIdentity;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.Subscriber;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberIdentifier;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberListByDataSharingGroup;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberMemo;

@WebService(portName = "SubscriberInformationServicePort", serviceName = "SubscriberInformationService_v4_0", targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/SubscriberInformationService_4", wsdlLocation = "/wsdls/SubscriberInformationService_v4_0.wsdl", endpointInterface = "com.telus.cmb.endpoint.SubscriberInformationServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class SubscriberInformationService extends EndpointProviderV2 implements SubscriberInformationServicePort {

	@Autowired
	private SubscriberLifecycleFacade subscriberFacade;

	@Autowired
	private SubscriberLifecycleManager subscriberManager;

	@Autowired
	private SubscriberLifecycleHelper subscriberHelper;

	@Autowired
	private AccountLifecycleFacade accountFacade;

	@Autowired
	private AccountLifecycleManager accountManager;

	@Autowired
	private AccountInformationHelper accountHelper;

	private static final int MAX_SUBSCRIBER_LIST = 1000;

	@Override
	public Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("subscriberFacade", subscriberFacade);
		resources.put("subscriberManager", subscriberManager);
		resources.put("subscriberHelper", subscriberHelper);
		resources.put("accountFacade", accountFacade);
		resources.put("accountManager", accountManager);
		resources.put("accountHelper", accountHelper);
		return resources;
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0001", errorMessage = "Failed to create memo")
	public CreateMemoResponse createMemo(CreateMemo parameters) throws EndpointException {
		SubscriberMemo subscriberMemo = parameters.getSubscriberMemo();
		MemoInfo memoInfo = new SubscriberMemoMapper().mapToDomain(subscriberMemo);
		memoInfo.setBanId(parameters.getBillingAccountNumber());
		if (Boolean.TRUE.equals(subscriberMemo.isAsyncInd())) {
			accountFacade.asyncCreateMemo(memoInfo, getSessionId(accountFacade));
		} else {
			accountManager.createMemo(memoInfo, getSessionId(accountManager));
		}
		return respond(new CreateMemoResponse());
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0002", errorMessage = "Failed to check subscriber port out eligibility")
	public CheckPortOutEligibilityResponse checkPortOutEligibility(CheckPortOutEligibility parameters) throws EndpointException {
		CheckPortOutEligibilityResponse response = new CheckPortOutEligibilityResponse();
		response.setPortOutEligibility(new SubscriberPortOutEligibilityInfoMapper().mapToSchema(subscriberHelper.checkSubscriberPortOutEligibility(parameters.getPhoneNumber(), parameters
				.getNdpDirectionIndicator().value())));
		return respond(response);
	}

	@Override
	@SuppressWarnings("unchecked")
	@EndpointOperation(errorCode = "CMB_SIS_0003", errorMessage = "getSubscriberListByAccountNumber failed ")
	public GetSubscriberListByAccountNumberResponse getSubscriberListByAccountNumber(GetSubscriberListByAccountNumber parameters) throws EndpointException {
		GetSubscriberListByAccountNumberResponse response = new GetSubscriberListByAccountNumberResponse();
		response.setSubscriberList(mapSubscriberList(subscriberHelper.retrieveSubscriberListByBAN(parameters.getBillingAccountNumber(), MAX_SUBSCRIBER_LIST, true)));
		return respond(response);
	}

	@Override
	@SuppressWarnings("unchecked")
	@EndpointOperation(errorCode = "CMB_SIS_004", errorMessage = "Failed to get subscriber list by phone number")
	public GetSubscriberListByPhoneNumberResponse getSubscriberListByPhoneNumber(GetSubscriberListByPhoneNumber parameters) throws EndpointException {
		GetSubscriberListByPhoneNumberResponse response = new GetSubscriberListByPhoneNumberResponse();
		response.setSubscriberList(mapSubscriberList(subscriberHelper.retrieveSubscriberListByPhoneNumber(parameters.getPhoneNumber(),
				getPhoneNumberSearchOption(parameters.getPhoneNumberSearchOption()), MAX_SUBSCRIBER_LIST, true)));
		return respond(response);
	}

	@Override
	@SuppressWarnings("unchecked")
	@EndpointOperation(errorCode = "CMB_SIS_0005", errorMessage = "Failed to get subscriber list by IMSI")
	public GetSubscriberListByImsiResponse getSubscriberListByImsi(GetSubscriberListByImsi parameters) throws EndpointException {
		GetSubscriberListByImsiResponse response = new GetSubscriberListByImsiResponse();
		boolean includeCancelled = parameters.isIncludeCancelledSubscribersInd() != null ? parameters.isIncludeCancelledSubscribersInd() : false;
		response.setSubscriberList(mapSubscriberList(subscriberHelper.retrieveSubscriberListByImsi(parameters.getImsi(), includeCancelled)));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0006", errorMessage = "Failed to get SubscriberListByDataSharingGroupList")
	public GetSubscriberListByDataSharingGroupListResponse getSubscriberListByDataSharingGroupList(GetSubscriberListByDataSharingGroupList parameters) throws EndpointException {
		GetSubscriberListByDataSharingGroupListResponse response = new GetSubscriberListByDataSharingGroupListResponse();
		List<SubscriberListByDataSharingGroup> subscriberList = new SubscriberListByDataSharingGroupMapper().mapToSchema(accountHelper.retrieveSubscribersByDataSharingGroupCodes(
				parameters.getBillingAccountNumber(), parameters.getDataSharingGroupCodeList().toArray(new String[0]), parameters.getEffectiveDate()));
		response.setSubscriberListByDataSharingGroupList(subscriberList);
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0007", errorMessage = "Failed to get SubscriberListBy FamilyGroupType")
	public GetSubscriberIdListByServiceGroupFamilyResponse getSubscriberIdListByServiceGroupFamily(GetSubscriberIdListByServiceGroupFamily parameters) throws EndpointException {
		GetSubscriberIdListByServiceGroupFamilyResponse response = new GetSubscriberIdListByServiceGroupFamilyResponse();
		response.setSubscriberIdList(Arrays.asList(accountHelper.retrieveSubscriberIdsByServiceFamily(parameters.getBillingAccountNumber(), parameters.getFamilyTypeCode(),
				parameters.getEffectiveDate())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0008", errorMessage = "Failed to get Subscriber By SubscriberID")
	public GetSubscriberBySubscriberIdResponse getSubscriberBySubscriberId(GetSubscriberBySubscriberId parameters) throws EndpointException {
		GetSubscriberBySubscriberIdResponse response = new GetSubscriberBySubscriberIdResponse();
		response.setSubscriber(mapSubscriber(subscriberHelper.retrieveSubscriber(0, parameters.getSubscriberId())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0009", errorMessage = "getSubscriberByPhoneNumber(phoneNumber,PhoneNumberSearchOption) failed")
	public GetSubscriberByPhoneNumberResponse getSubscriberByPhoneNumber(GetSubscriberByPhoneNumber parameters) throws EndpointException {
		GetSubscriberByPhoneNumberResponse response = new GetSubscriberByPhoneNumberResponse();
		response.setSubscriber(mapSubscriber(subscriberHelper.retrieveSubscriberByPhoneNumber(parameters.getPhoneNumber(), getPhoneNumberSearchOption(parameters.getPhoneNumberSearchOption()))));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0010", errorMessage = "getSubscriberBySubscriptionId failed")
	public GetSubscriberBySubscriptionIdResponse getSubscriberBySubscriptionId(GetSubscriberBySubscriptionId parameters) throws EndpointException {
		GetSubscriberBySubscriptionIdResponse response = new GetSubscriberBySubscriptionIdResponse();
		response.setSubscriber(mapSubscriber(subscriberHelper.retrieveLatestSubscriberBySubscriptionId(parameters.getSubscriptionId())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0011", errorMessage = "Failed to get latest subscriber by phone number")
	public GetLatestSubscriberByPhoneNumberResponse getLatestSubscriberByPhoneNumber(GetLatestSubscriberByPhoneNumber parameters) throws EndpointException {
		GetLatestSubscriberByPhoneNumberResponse response = new GetLatestSubscriberByPhoneNumberResponse();
		response.setSubscriber(mapSubscriber(subscriberHelper.retrieveLatestSubscriberByPhoneNumber(parameters.getPhoneNumber(), getPhoneNumberSearchOption(parameters.getPhoneNumberSearchOption()))));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0012", errorMessage = "getSubscriberIdentifierListByAccountNumber(ban,includeStatusOption) failed")
	public GetSubscriberIdentifierListByAccountNumberResponse getSubscriberIdentifierListByAccountNumber(GetSubscriberIdentifierListByAccountNumber parameters) throws EndpointException {
		GetSubscriberIdentifierListByAccountNumberResponse response = new GetSubscriberIdentifierListByAccountNumberResponse();
		ProductSubscriberListInfo[] productSubscriberListInfoArray = accountHelper.retrieveProductSubscriberLists(parameters.getBillingAccountNumber());
		if (productSubscriberListInfoArray.length == 0) {
			throw new ServicePolicyException(ServiceErrorCodes.ERROR_NO_DATA_FOR_BAN, ServiceErrorCodes.ERROR_DESC_NO_DATA_FOR_BAN);
		}
		List<SubscriberIdentifierInfo> domainSubIdInfoList = getSubscriberIdentifierListFromProductSubscriberList(parameters, parameters.getBillingAccountNumber(), productSubscriberListInfoArray);
		response.setSubscriberIdentifierList(new SubscriberIdentifierMapper().mapToSchema(domainSubIdInfoList));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0013", errorMessage = "getSubscriberIdentifierBySubscriptionId failed")
	public GetSubscriberIdentifierBySubscriptionIdResponse getSubscriberIdentifierBySubscriptionId(GetSubscriberIdentifierBySubscriptionId parameters) throws EndpointException {
		GetSubscriberIdentifierBySubscriptionIdResponse response = new GetSubscriberIdentifierBySubscriptionIdResponse();
		response.setSubscriberIdentifier(new SubscriberIdentifierMapper().mapToSchema(subscriberHelper.retrieveSubscriberIdentifierBySubscriptionId(parameters.getSubscriptionId())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0014", errorMessage = "getSubscriberIdentifierByPhoneNumberAndAccountNumber(ban,phoneNumber,phoneNumberSearchOption) failed")
	public GetSubscriberIdentifierByPhoneNumberAndAccountNumberResponse getSubscriberIdentifierByPhoneNumberAndAccountNumber(GetSubscriberIdentifierByPhoneNumberAndAccountNumber parameters)
			throws EndpointException {
		GetSubscriberIdentifierByPhoneNumberAndAccountNumberResponse response = new GetSubscriberIdentifierByPhoneNumberAndAccountNumberResponse();
		response.setSubscriberIdentifier(new SubscriberIdentifierMapper().mapToSchema(subscriberHelper.retrieveSubscriberIdentifiersByPhoneNumber(parameters.getBillingAccountNumber(),
				parameters.getPhoneNumber(), getPhoneNumberSearchOption(parameters.getPhoneNumberSearchOption()))));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0015", errorMessage = "Failed to Get Calling Circle Information")
	public GetCallingCircleInformationResponse getCallingCircleInformation(GetCallingCircleInformation parameters) throws EndpointException {
		GetCallingCircleInformationResponse response = new GetCallingCircleInformationResponse();
		CallingCircleParametersInfo callingCircleParametersInfo = subscriberFacade.getCallingCircleInformation(parameters.getBillingAccountNumber(), parameters.getSubscriberNumber(),
				parameters.getServiceCode(), parameters.getFeatureCode(), parameters.getProductType(), getSessionId(subscriberFacade));
		response.setCallingCircleInfo(new CallingCircleInformationMapper().mapToSchema(callingCircleParametersInfo));
		return respond(response);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@EndpointOperation(errorCode = "CMB_SIS_0016", errorMessage = "getAirTimeAllocationList failed")
	public GetAirTimeAllocationListResponse getAirTimeAllocationList(GetAirTimeAllocationList parameters) throws EndpointException {
		GetAirTimeAllocationListResponse response = new GetAirTimeAllocationListResponse();
		List<ServiceAirTimeAllocationInfo> serviceAirTimeAllocationInfo = subscriberFacade.getAirTimeAllocations(getSubscriberIdentity(parameters.getSubscriberIdentity()),
				parameters.getEffectiveDate(), getServiceIdentityList(parameters.getServiceIdentityList()), getSessionId(subscriberFacade));
		if (serviceAirTimeAllocationInfo == null) {
			throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNKNOWN, ServiceErrorCodes.ERROR_DESC_INPUT);
		}
		response.getAirTimeAllocationList().addAll(new ServiceAirTimeAllocationMapper().mapToSchema(serviceAirTimeAllocationInfo));		
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SIS_0017", errorMessage = "Failed to get Subscriber Data Sharing Info List")
	public GetSubscriberDataSharingInfoListResponse getSubscriberDataSharingInfoList(GetSubscriberDataSharingInfoList parameters) throws EndpointException {
		GetSubscriberDataSharingInfoListResponse response = new GetSubscriberDataSharingInfoListResponse();
		String[] dataSharingGroupCodes = null;
		if (parameters.getDataSharingGroupCodeList() != null) {
			dataSharingGroupCodes = parameters.getDataSharingGroupCodeList().toArray(new String[0]);
		}
		response.setSubscriberDataSharingInfoList(new SubscriberDataSharingInfoMapper().mapToSchema(accountHelper.retrieveSubscriberDataSharingInfoList(parameters.getBillingAccountNumber(),
				dataSharingGroupCodes)));
		return respond(response);
	}

	private PhoneNumberSearchOptionInfo getPhoneNumberSearchOption(PhoneNumberSearchOption phoneNumberSearchOption) {
		PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
		phoneNumberSearchOptionInfo.setSearchWirelessNumber(true);
		if (phoneNumberSearchOption != null) {
			phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionMapper().mapToDomain(phoneNumberSearchOption);
		}
		return phoneNumberSearchOptionInfo;
	}

	private List<ServiceIdentityInfo> getServiceIdentityList(List<ServiceIdentity> serviceIdentityList) {
		List<ServiceIdentityInfo> serviceIdentityInfoList = null;
		if (serviceIdentityList != null && !serviceIdentityList.isEmpty()) {
			serviceIdentityInfoList = new ServiceIdentityMapper().mapToDomain(serviceIdentityList);
		}
		return serviceIdentityInfoList;
	}

	private SubscriberIdentifierInfo getSubscriberIdentity(SubscriberIdentifier subscriberIdentity) {
		SubscriberIdentifierInfo subscriberIdentityInfo = null;
		if (subscriberIdentity != null) {
			subscriberIdentityInfo = new SubscriberIdentifierMapper().mapToDomain(subscriberIdentity);
		}
		return subscriberIdentityInfo;
	}

	private List<Subscriber> mapSubscriberList(Collection<SubscriberInfo> subscriberInfoList) throws ApplicationException {
		List<Subscriber> subscriberList = new ArrayList<Subscriber>();
		int count = 0;
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			if (count++ > MAX_SUBSCRIBER_LIST) {
				break;
			}
			subscriberList.add(mapSubscriber(subscriberInfo));
		}
		return subscriberList;
	}

	private Subscriber mapSubscriber(SubscriberInfo subscriberInfo) throws ApplicationException {
		Subscriber subscriber = SubscriberMapper_30.getInstance().mapToSchema(subscriberInfo);
		subscriber.setSubscriptionRole(SubscriptionRoleMapper.getInstance().mapToSchema(subscriberHelper.retrieveSubscriptionRole(subscriberInfo.getPhoneNumber())));
		EnterpriseAddressInfo enterpriseAddressInfo = new EnterpriseAddressInfo();
		enterpriseAddressInfo.translateAddress(subscriberHelper.retrieveSubscriberAddress(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId()));
		subscriber.setAddress(CustomerCommonMapper_30.AddressMapper().mapToSchema(enterpriseAddressInfo));
		mapSeatData(subscriberInfo, subscriber);
		return subscriber;
	}

	private void mapSeatData(SubscriberInfo subscriberInfo, Subscriber subscriber) {
		if (subscriberInfo.getSeatData() != null) {
			subscriber.setSeatGroupId(subscriberInfo.getSeatData().getSeatGroup());
			subscriber.setSeatTypeCd(subscriberInfo.getSeatData().getSeatType());
			if (subscriberInfo.getSeatData().getResources() != null) {
				subscriber.getSeatResourceList().addAll(new SeatResourceMapper().mapToSchema(subscriberInfo.getSeatData().getResources()));
			}
		}
	}

	private List<SubscriberIdentifierInfo> getSubscriberIdentifierListFromProductSubscriberList(GetSubscriberIdentifierListByAccountNumber parameters, Integer ban,
			ProductSubscriberListInfo[] productSubscriberListInfoArray) {
		List<com.telus.api.account.SubscriberIdentifier> domainSubIdList = new ArrayList<com.telus.api.account.SubscriberIdentifier>();
		IncludeStatusOption includeStatusOption = parameters.getIncludeStatusOption();
		for (ProductSubscriberListInfo info : productSubscriberListInfoArray) {
			List<com.telus.api.account.SubscriberIdentifier> activeSubIDs = Arrays.asList(info.getActiveSubscriberIdentifiers());
			List<com.telus.api.account.SubscriberIdentifier> cancelledSubIDs = Arrays.asList(info.getCancelledSubscriberIdentifiers());
			List<com.telus.api.account.SubscriberIdentifier> reservedSubIDs = Arrays.asList(info.getReservedSubscriberIdentifiers());
			List<com.telus.api.account.SubscriberIdentifier> suspendedSubIDs = Arrays.asList(info.getSuspendedSubscriberIdentifiers());

			if (includeStatusOption == null) {
				domainSubIdList.addAll(activeSubIDs);
			} else {
				if (includeStatusOption.isIncludeActiveInd()) {
					domainSubIdList.addAll(activeSubIDs);
				}
				if (includeStatusOption.isIncludeReservedInd()) {
					domainSubIdList.addAll(reservedSubIDs);
				}
				if (includeStatusOption.isIncludeSuspendedInd()) {
					domainSubIdList.addAll(suspendedSubIDs);
				}
				if (includeStatusOption.isIncludeCancelledInd()) {
					domainSubIdList.addAll(cancelledSubIDs);
				}
			}
		}

		/**
		 * 1. We cast to info object since retrieve method returned
		 * SubscriberIdentifier interface objects 2. We set ban here as this
		 * method has the ban but the domain objects do not
		 */
		List<SubscriberIdentifierInfo> domainSubIdInfoList = new ArrayList<SubscriberIdentifierInfo>();
		for (com.telus.api.account.SubscriberIdentifier subscriber : domainSubIdList) {
			SubscriberIdentifierInfo subIdInfo = (SubscriberIdentifierInfo) subscriber;
			subIdInfo.setBan(ban);
			domainSubIdInfoList.add(subIdInfo);
		}
		return domainSubIdInfoList;
	}

	// Deprecated methods from SIS v3.4
	// They don't belong in SIS, but haven't been migrated over to SMS or SLCMS
	/*
	@Deprecated
	@EndpointOperation(errorCode = "CMB_SIS_0005", errorMessage = "Failed to set subscriber port in indicator")
	public UpdatePortInSubscriberResponse updatePortInSubscriber(UpdatePortInSubscriber parameters) throws EndpointException {
		subscriberManager.setSubscriberPortIndicator(parameters.getPhoneNumber(), getSessionId(subscriberManager));
		return new UpdatePortInSubscriberResponse();
	}

	@Deprecated
	@EndpointOperation(errorCode = "CMB_SIS_0006", errorMessage = "Failed to snap back phone number")
	public UpdateSnapbackPhoneNumberResponse updateSnapbackPhoneNumber(UpdateSnapbackPhoneNumber parameters) throws EndpointException {
		subscriberManager.snapBack(parameters.getPhoneNumber(), getSessionId(subscriberManager));
		return new UpdateSnapbackPhoneNumberResponse();
	}

	@Deprecated
	@EndpointOperation(errorCode = "CMB_SIS_0008", errorMessage = "Failed to Update Email Address")
	public UpdateEmailAddressResponse updateEmailAddress(UpdateEmailAddress parameters) throws EndpointException {
		subscriberManager.updateEmailAddress(Integer.valueOf(parameters.getBillingAccountNumber()), parameters.getSubscriberNumber(), parameters.getEmailAddressTxt(), getSessionId(subscriberManager));
		return new UpdateEmailAddressResponse();
	}

	@Deprecated
	@EndpointOperation(errorCode = "CMB_SIS_0021", errorMessage = "updateSubscriberProfile(ban,subscriberId,consumerName,address,subscriberEmailTxt,subscriberLanguageCd) failed")
	public UpdateSubscriberProfileResponse updateSubscriberProfile(UpdateSubscriberProfile parameters) throws EndpointException {
		UpdateSubscriberProfileResponse response = new UpdateSubscriberProfileResponse();
		AddressInfo addressInfo = new AddressMapper().mapToDomain(parameters.getAddress());
		ConsumerNameInfo consumerNameInfo = new ConsumerNameMapper().mapToDomain(parameters.getConsumerName());
		SubscriberInfo subscriberInfo = subscriberFacade.updateSubscriberProfile(parameters.getBillingAccountNumber(), parameters.getSubscriberId(), parameters.isPrepaidInd(), consumerNameInfo,
				addressInfo, parameters.getSubscriberEmailTxt(), parameters.getSubscriberLanguageCd(), parameters.getInvoiceCallSortOrderCd(), parameters.getSubscriptionRoleCd(),
				getSessionId(subscriberFacade));
		response.setSubscriber(mapSubscriber(subscriberInfo));
		return respond(response);
	}
	*/

}
