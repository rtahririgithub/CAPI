package com.telus.cmb.jws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.api.ApplicationException;
import com.telus.api.resource.Resource;
import com.telus.cmb.jws.mapper.ServiceAirTimeAllocationMapper;
import com.telus.cmb.jws.mapper.SubscriberInformationServiceMapper;
import com.telus.cmb.jws.mapping.customer_common.CustomerCommonMapper_30;
import com.telus.cmb.jws.mapping.subscriber.information_types_30.SubscriberMapper_30;
import com.telus.cmb.jws.mapping.subscriber.information_types_30.SubscriptionRoleMapper;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.EnterpriseAddressInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.ServiceIdentityInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.ResourceInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
import com.telus.cmb.jws.IncludeStatusOption;
import com.telus.cmb.jws.PhoneNumberSearchOption;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.CallingCircleInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.NDPDirectionIndicator;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.PortOutEligibilityInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SeatResource;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.ServiceAirTimeAllocation;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.ServiceIdentity;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.Subscriber;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberIdentifier;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberListByDataSharingGroup;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberMemo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.AuditInfo;


//@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)

@WebService(
		portName = "SubscriberInformationServicePort", 
		serviceName = "SubscriberInformationService_v3_2", 
		targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/SubscriberInformationService_3", 
		wsdlLocation = "/wsdls/SubscriberInformationService_v3_2.wsdl", 
		endpointInterface = "com.telus.cmb.jws.SubscriberInformationServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class SubscriberInformationService_32 extends BaseService implements SubscriberInformationServicePort {
	private static final int MAX_SUBSCRIBER_LIST = 1000;

	public SubscriberInformationService_32() {
		super(new SubscriberInformationExceptionMapper());
	}

	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SIS_0001", errorMessage = "Failed to create memo")
	public void createMemo(final String billingAccountNumber,final SubscriberMemo subscriberMemo) throws PolicyException,ServiceException {
		execute(new ServiceInvocationCallback<Object>() {
			@Override
			public Object doInInvocationCallback(
					ServiceInvocationContext context) throws Throwable {
				MemoInfo memoInfo = getSubscriberMemoMapper().mapToDomain(subscriberMemo);
				memoInfo.setBanId(Integer.parseInt(billingAccountNumber));
				if (Boolean.TRUE.equals(subscriberMemo.isAsyncInd())) {
					getAccountLifecycleFacade(context).asyncCreateMemo(memoInfo,context.getAccountLifeCycleFacadeSessionId());
				} else {
					getAccountLifecycleManager(context).createMemo(memoInfo,context.getAccountLifeCycleManagerSessionId());
				}
				return null;
			}
		});

	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0002", errorMessage="getSubscriberListByAccountNumber failed ")
	public List<Subscriber> getSubscriberListByAccountNumber(final String billingAccountNumber) throws PolicyException,ServiceException {
		return execute(new ServiceInvocationCallback<List<Subscriber>>() {
			@Override
			public List<Subscriber> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				int ban = Integer.valueOf(billingAccountNumber);
				@SuppressWarnings("unchecked")
				Collection<SubscriberInfo> subscriberList = getSubscriberLifecycleHelper(context).retrieveSubscriberListByBAN(ban,MAX_SUBSCRIBER_LIST);
				return mapSubscriberList(subscriberList, context);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_003", errorMessage="Failed to get subscriber list by phone number")
	public List<Subscriber> getSubscriberListByPhoneNumber( final String phoneNumber, final PhoneNumberSearchOption phoneNumberSearchOption ) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<List<Subscriber>>() 
		{
			@Override
			public List<Subscriber> doInInvocationCallback(
					ServiceInvocationContext context) throws Throwable {
				PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo;
				if (phoneNumberSearchOption == null) {
					phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
					phoneNumberSearchOptionInfo.setSearchWirelessNumber(true);
				}
				else{
					phoneNumberSearchOptionInfo = getPhoneNumberSearchOptionMapper().mapToDomain( phoneNumberSearchOption );
				}
				
				@SuppressWarnings("unchecked")
				Collection<SubscriberInfo> subscriberList = getSubscriberLifecycleHelper(context).retrieveSubscriberListByPhoneNumber(phoneNumber, 
						phoneNumberSearchOptionInfo, MAX_SUBSCRIBER_LIST,true);
				return mapSubscriberList(subscriberList, context);
			}
		});
	}

	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0004", errorMessage="Failed to get subscriber list by IMSI")
	public List<Subscriber> getSubscriberListByIMSI(final String imsi, final Boolean includeCancelledSubscribersInd) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Subscriber>>() {

			@Override
			public List<Subscriber> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				@SuppressWarnings("unchecked")
				Collection<SubscriberInfo> subscriberList = getSubscriberLifecycleHelper(context).retrieveSubscriberListByImsi(imsi, includeCancelledSubscribersInd.booleanValue());
				return mapSubscriberList(subscriberList, context);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0005", errorMessage="Failed to set subscriber port in indicator")
	public void updatePortInSubscriber(final String phoneNumber) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback<Object>(){
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
 				getSubscriberLifecycleManager(context).setSubscriberPortIndicator(phoneNumber, context.getSubscriberLifecycleManagerSessionId());
			return null;
			}
		});	
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0006", errorMessage="Failed to snap back phone number")
	public void updateSnapbackPhoneNumber(final String phoneNumber) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback<Object>(){
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
					getSubscriberLifecycleManager(context).snapBack(phoneNumber, context.getSubscriberLifecycleManagerSessionId());
				return null;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0007", errorMessage="Failed to check subscriber port out eligibility")
	public PortOutEligibilityInfo checkPortOutEligibility(final String phoneNumber, final NDPDirectionIndicator ndpDirectionIndicator) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<PortOutEligibilityInfo>(){
			@Override
			public PortOutEligibilityInfo doInInvocationCallback(ServiceInvocationContext context) throws Throwable {  
 					com.telus.eas.portability.info.PortOutEligibilityInfo portOutEligibInfo = 
							getSubscriberLifecycleHelper(context).checkSubscriberPortOutEligibility(phoneNumber, ndpDirectionIndicator.value());				
					return getSubscriberPortOutEligibilityInfoMapper().mapToSchema(portOutEligibInfo);
			}
		});	
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0008", errorMessage="Failed to Update Email Address")
	public void updateEmailAddress(final String billingAccountNumber, final String subscriberNumber, final String emailAddress, final Boolean notificationSuppressionInd,final AuditInfo auditInfo) throws PolicyException, ServiceException {		
		execute (new ServiceInvocationCallback<Object>() {
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				getSubscriberLifecycleManager(context).updateEmailAddress(Integer.valueOf(billingAccountNumber), subscriberNumber, emailAddress, context.getSubscriberLifecycleManagerSessionId());
				return null;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0009", errorMessage="Failed to Get Email Address")
	public String getEmailAddress(final String billingAccountNumber, final String subscriberNumber, final Boolean notificationSuppressionInd) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<String>() {
			@Override
			public String doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return getSubscriberLifecycleHelper(context).retrieveEmailAddress(Integer.valueOf(billingAccountNumber), subscriberNumber);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0010", errorMessage="Failed to Get Calling Circle Information")
	public CallingCircleInfo getCallingCircleInformation(final String billingAccountNumber, final String subscriberNumber, final String productType, final String serviceCode, final String featureCode) throws PolicyException,
			ServiceException {
		return execute (new ServiceInvocationCallback<CallingCircleInfo>() {
			@Override
			public CallingCircleInfo doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				CallingCircleParametersInfo callingCircleParametersInfo = getSubscriberLifecycleFacade(context).
						getCallingCircleInformation(Integer.valueOf(billingAccountNumber), subscriberNumber, serviceCode, featureCode, productType, context.getSubscriberLifecycleFacadeSessionId());	
				return SubscriberInformationServiceMapper.CallingCircleInformationMapper().mapToSchema(callingCircleParametersInfo);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0011", errorMessage="Failed to get SubscriberListByDataSharingGroupList")
	public List<SubscriberListByDataSharingGroup> getSubscriberListByDataSharingGroupList(final String billingAccountNumber, final List<String> dataSharingGroupCodeList, final Date effectiveDate)
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<SubscriberListByDataSharingGroup>>() {
			@Override
			public List<SubscriberListByDataSharingGroup> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return SubscriberInformationServiceMapper.SubscriberListByDataSharingGroupMapper().mapToSchema(getAccountInformationHelper(context).retrieveSubscribersByDataSharingGroupCodes(Integer.valueOf(billingAccountNumber), dataSharingGroupCodeList.toArray(new String[dataSharingGroupCodeList.size()]), effectiveDate));
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0012", errorMessage="Failed to get SubscriberListBy FamilyGroupType")
	public List<String> getSubscriberIdListByServiceGroupFamily(final String billingAccountNumber, final String familyTypeCode, final Date effectiveDate) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<String>>() {
			@Override
			public List<String> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
			String[] subsribers = getAccountInformationHelper(context).retrieveSubscriberIdsByServiceFamily(Integer.valueOf(billingAccountNumber), familyTypeCode, effectiveDate);
			return Arrays.asList(subsribers);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0013", errorMessage="Failed to get Subscriber By SubscriberID")
	public Subscriber getSubscriberBySubscriberId(final String subscriberId) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Subscriber>() {

			@Override
			public Subscriber doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper(context).retrieveSubscriber(0,subscriberId);
				return mapSubscriber(subscriberInfo, context);
			}
		});
	}

	
	@ServiceBusinessOperation(errorCode="CMB_SIS_0014", errorMessage="Failed to get latest subscriber by phone number")
	public Subscriber getLatestSubscriberByPhoneNumber(final String phoneNumber) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Subscriber>() {

			@Override
			public Subscriber doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper(context).retrieveLatestSubscriberByPhoneNumber(phoneNumber);
				return mapSubscriber(subscriberInfo, context);
			}
		});
	}


	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0015", errorMessage="getSubscriberIdentifierBySubscriptionId failed")
	public SubscriberIdentifier getSubscriberIdentifierBySubscriptionId(final long subscriptionId) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<SubscriberIdentifier>() {

			@Override
			public SubscriberIdentifier doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SubscriberIdentifierInfo subscriberIdentifierInfo = getSubscriberLifecycleHelper(context).retrieveSubscriberIdentifierBySubscriptionId(subscriptionId);
				return getSubscriberIdentifierMapper().mapToSchema( subscriberIdentifierInfo );
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0016", errorMessage="getSubscriberBySubscriptionId failed")
	public Subscriber getSubscriberBySubscriptionId(final long subscriptionId) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Subscriber>() {

			@Override
			public Subscriber doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper(context).retrieveLatestSubscriberBySubscriptionId(subscriptionId);
				return mapSubscriber(subscriberInfo, context);
			}
		});
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0017", errorMessage="getAirTimeAllocationList failed")
	public List<ServiceAirTimeAllocation> getAirTimeAllocationList(final List<ServiceIdentity> serviceIdentityList, 
			final Date effectiveDate, final SubscriberIdentifier subscriberIdentity) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<ServiceAirTimeAllocation>>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public List<ServiceAirTimeAllocation> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				SubscriberIdentifierInfo subscriberIdentityInfo= null;
				List<ServiceIdentityInfo> serviceIdentityInfoList=null;
				
				if(subscriberIdentity!=null)
					subscriberIdentityInfo=	getSubscriberIdentifierMapper().mapToDomain(subscriberIdentity);
				
				if(serviceIdentityList!=null && !serviceIdentityList.isEmpty())
					serviceIdentityInfoList=getServiceIdentityMapper().mapToDomain(serviceIdentityList);
				
				List<ServiceAirTimeAllocationInfo> serviceAirTimeAllocationInfo = getSubscriberLifecycleFacade(context).
						getAirTimeAllocations(subscriberIdentityInfo, 
								effectiveDate, serviceIdentityInfoList, 
								context.getSubscriberLifecycleFacadeSessionId());
				if(serviceAirTimeAllocationInfo == null)
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNKNOWN, ServiceErrorCodes.ERROR_DESC_INPUT);
				
				return new ServiceAirTimeAllocationMapper().mapToSchema(serviceAirTimeAllocationInfo);
				
			}
		});
	}
	
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0018", errorMessage="getSubscriberByPhoneNumber(phoneNumber,PhoneNumberSearchOption) failed")
	public Subscriber getSubscriberByPhoneNumber( final String phoneNumber, final PhoneNumberSearchOption phoneNumberSearchOption )
			throws PolicyException, ServiceException 
	{
		return execute( new ServiceInvocationCallback<Subscriber>() 
		{
			public Subscriber doInInvocationCallback(ServiceInvocationContext context) throws Throwable 
			{
				PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo;
				if (phoneNumberSearchOption == null) {
					phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
					phoneNumberSearchOptionInfo.setSearchWirelessNumber(true);
				}
				else{
					phoneNumberSearchOptionInfo = getPhoneNumberSearchOptionMapper().mapToDomain( phoneNumberSearchOption );
				}
				
				SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper(context).retrieveSubscriberByPhoneNumber( phoneNumber, phoneNumberSearchOptionInfo);
				return mapSubscriber(subscriberInfo, context);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0019", errorMessage="getLatestSubscriberByPhoneNumber(phoneNumber,phoneNumberSearchOption) failed")
	public Subscriber getLatestSubscriberByPhoneNumber( final String phoneNumber, final PhoneNumberSearchOption phoneNumberSearchOption )
		throws PolicyException, ServiceException 
	{
		return execute( new ServiceInvocationCallback<Subscriber>() 
		{
			@Override
			public Subscriber doInInvocationCallback(ServiceInvocationContext context) throws Throwable 
			{
				PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo;
				
				if (phoneNumberSearchOption == null) {
					phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
					phoneNumberSearchOptionInfo.setSearchWirelessNumber(true);
				}
				else{
					phoneNumberSearchOptionInfo = getPhoneNumberSearchOptionMapper().mapToDomain( phoneNumberSearchOption );
				}
				SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper(context).retrieveLatestSubscriberByPhoneNumber( phoneNumber,  phoneNumberSearchOptionInfo );
				return mapSubscriber( subscriberInfo, context );
			}
		});
	}
	
	private SubscriberInformationServiceMapper.ServiceIdentityMapper getServiceIdentityMapper() {
		return SubscriberInformationServiceMapper.ServiceIdentityMapper();		
	}
	
	private SubscriberInformationServiceMapper.SubscriberIdentifierMapper getSubscriberIdentifierMapper() {
		return SubscriberInformationServiceMapper.SubscriberIdentifierMapper();		
	}
	
	private SubscriberInformationServiceMapper.SubscriberPortOutEligibilityInfoMapper getSubscriberPortOutEligibilityInfoMapper() {
		return SubscriberInformationServiceMapper.SubscriberPortOutEligibilityInfoMapper();		
	}
	
	private SubscriberInformationServiceMapper.SubscriberMemoMapper getSubscriberMemoMapper() {
		return SubscriberInformationServiceMapper.SubscriberMemoMapper();
	}
	
	private SubscriberInformationServiceMapper.PhoneNumberSearchOptionMapper getPhoneNumberSearchOptionMapper() {
		return SubscriberInformationServiceMapper.PhoneNumberSearchOptionMapper();
	}
	
	private SubscriberInformationServiceMapper.SeatResourceMapper getSeatResourceMapper() {
		return SubscriberInformationServiceMapper.SeatResourceMapper();
	}

	private Subscriber mapSubscriber (SubscriberInfo subscriberInfo, ServiceInvocationContext context) throws ApplicationException, Exception {
		Subscriber wsSubscriber = SubscriberMapper_30.getInstance().mapToSchema(subscriberInfo);
		
		SubscriptionRoleInfo subscriptionRoleInfo = getSubscriberLifecycleHelper(context).retrieveSubscriptionRole(subscriberInfo.getPhoneNumber());
		wsSubscriber.setSubscriptionRole(SubscriptionRoleMapper.getInstance().mapToSchema(subscriptionRoleInfo));
		
		AddressInfo addressInfo = getSubscriberLifecycleHelper(context).retrieveSubscriberAddress(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());
		EnterpriseAddressInfo enterpriseAddressInfo = new EnterpriseAddressInfo();
		enterpriseAddressInfo.translateAddress(addressInfo);
		wsSubscriber.setAddress(CustomerCommonMapper_30.AddressMapper().mapToSchema(enterpriseAddressInfo));
		
		if (subscriberInfo.getSeatData() != null) {
			wsSubscriber.setSeatGroupId(subscriberInfo.getSeatData().getSeatGroup());
			wsSubscriber.setSeatTypeCd(subscriberInfo.getSeatData().getSeatType());
			Resource[] resources = subscriberInfo.getSeatData().getResources();
			List<ResourceInfo> resourceInfo = new ArrayList<ResourceInfo>();
			for (int i = 0; i < resources.length; i++) {
				ResourceInfo resource = new ResourceInfo();
				resource.setResourceNumber(resources[i].getResourceNumber());
				resource.setResourceType(resources[i].getResourceType());
				resource.setResourceStatus(resources[i].getResourceStatus());
				resourceInfo.add(resource);
			}
			List<SeatResource> seatResources =  getSeatResourceMapper().mapToSchema(resourceInfo);
			wsSubscriber.getSeatResourceList().addAll(seatResources);
		}
		
		return wsSubscriber;
	}
	
	/** TODO re-factor candidate: i is never incremented so the test of it is invalid code-MP */
	private List<Subscriber> mapSubscriberList (Collection<SubscriberInfo> subscriberInfoList, ServiceInvocationContext context) throws ApplicationException, Exception {
		int i = 0;
		List<Subscriber> respSubscriberList = new ArrayList<Subscriber>();
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			if (i >= MAX_SUBSCRIBER_LIST) {
				break;
			}
			respSubscriberList.add(mapSubscriber (subscriberInfo, context));
		}
		
		return respSubscriberList;
	}


	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0020", errorMessage="getSubscriberIdentifierByPhoneNumberAndAccountNumber(ban,phoneNumber,phoneNumberSearchOption) failed")
	public SubscriberIdentifier getSubscriberIdentifierByPhoneNumberAndAccountNumber(
			final String billingAccountNumber, final String phoneNumber,
			final PhoneNumberSearchOption phoneNumberSearchOption)
			throws PolicyException, ServiceException 
	{ 
		return execute( new ServiceInvocationCallback<SubscriberIdentifier>() 
		{
			public SubscriberIdentifier doInInvocationCallback(ServiceInvocationContext context) throws Throwable 
			{
				int ban = billingAccountNumber !=null ? Integer.valueOf( billingAccountNumber ):0; 
				PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo;
				if (phoneNumberSearchOption == null) {
					phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
					phoneNumberSearchOptionInfo.setSearchWirelessNumber(true);
				}
				else{
					phoneNumberSearchOptionInfo = getPhoneNumberSearchOptionMapper().mapToDomain( phoneNumberSearchOption );
				}
				SubscriberIdentifierInfo subIdInfo = getSubscriberLifecycleHelper(context).retrieveSubscriberIdentifiersByPhoneNumber( ban, phoneNumber, phoneNumberSearchOptionInfo );
				return getSubscriberIdentifierMapper().mapToSchema( subIdInfo );
			}			
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SIS_0021", errorMessage = "getSubscriberIdentifierListByAccountNumber(ban,includeStatusOption) failed")
	public List<SubscriberIdentifier> getSubscriberIdentifierListByAccountNumber(final String billingAccountNumber,final IncludeStatusOption includeStatusOption) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<SubscriberIdentifier>>() {
			public List<SubscriberIdentifier> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				Integer ban = Integer.valueOf( billingAccountNumber );
				ProductSubscriberListInfo[] productSubscriberListInfoArray = getAccountInformationHelper(context).retrieveProductSubscriberLists(ban);

				if( productSubscriberListInfoArray.length == 0 )
					throw new ServicePolicyException( ServiceErrorCodes.ERROR_NO_DATA_FOR_BAN, ServiceErrorCodes.ERROR_DESC_NO_DATA_FOR_BAN );

				List<com.telus.api.account.SubscriberIdentifier> domainSubIdList = new ArrayList<com.telus.api.account.SubscriberIdentifier>();
				for( ProductSubscriberListInfo info: productSubscriberListInfoArray )
				{
					List<com.telus.api.account.SubscriberIdentifier> activeSubIDs    = Arrays.asList( info.getActiveSubscriberIdentifiers() );
					List<com.telus.api.account.SubscriberIdentifier> cancelledSubIDs = Arrays.asList( info.getCancelledSubscriberIdentifiers() );
					List<com.telus.api.account.SubscriberIdentifier> reservedSubIDs  = Arrays.asList( info.getReservedSubscriberIdentifiers() );
					List<com.telus.api.account.SubscriberIdentifier> suspendedSubIDs = Arrays.asList( info.getSuspendedSubscriberIdentifiers() );

					if (includeStatusOption == null) {
						domainSubIdList.addAll(activeSubIDs);
					} else {
						if (includeStatusOption.isIncludeActiveInd())
							domainSubIdList.addAll(activeSubIDs);
						if (includeStatusOption.isIncludeReservedInd())
							domainSubIdList.addAll(reservedSubIDs);
						if (includeStatusOption.isIncludeSuspendedInd())
							domainSubIdList.addAll(suspendedSubIDs);
						if (includeStatusOption.isIncludeCancelledInd())
							domainSubIdList.addAll(cancelledSubIDs);
					}
				} 
				
				
				/**
				 * 1. We cast to info object since retrieve method returned SubscriberIdentifier interface objects
				 * 2. We set ban here as this method has the ban but the domain objects do not
				 */
				List<SubscriberIdentifierInfo> domainSubIdInfoList = new ArrayList<SubscriberIdentifierInfo>();
				for( com.telus.api.account.SubscriberIdentifier subscriber: domainSubIdList )
				{
					SubscriberIdentifierInfo subIdInfo = (SubscriberIdentifierInfo) subscriber;
					subIdInfo.setBan(ban);
					domainSubIdInfoList.add( subIdInfo);
				}
				
				return getSubscriberIdentifierMapper().mapToSchema( domainSubIdInfoList );
			} 
		}); 
	}

}
