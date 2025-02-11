package com.telus.cmb.jws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.api.ApplicationException;
import com.telus.cmb.jws.mapper.SubscriberInformationServiceMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.AddressMapper;
import com.telus.cmb.jws.mapping.subscriber.information_types_20.SubscriberMapper_20;
import com.telus.cmb.jws.mapping.subscriber.information_types_20.SubscriptionRoleMapper;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.CallingCircleInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.NDPDirectionIndicator;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.PortOutEligibilityInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.Subscriber;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.SubscriberIdentifierList;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.SubscriberListByDataSharingGroup;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.SubscriberMemo;


@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(
		portName = "SubscriberInformationServicePort", 
		serviceName = "SubscriberInformationService_v2_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/SubscriberInformationService_2", 
		wsdlLocation = "/wsdls/SubscriberInformationService_v2_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.SubscriberInformationServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class SubscriberInformationService_20 extends BaseService implements SubscriberInformationServicePort {
	private static final int MAX_SUBSCRIBER_LIST = 1000; 
	
	public SubscriberInformationService_20(){
		super(new SubscriberInformationExceptionMapper());
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0001", errorMessage="Failed to create memo")
	public void createMemo(final String billingAccountNumber,final SubscriberMemo subscriberMemo) throws PolicyException,ServiceException {
		execute(new ServiceInvocationCallback<Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				MemoInfo memoInfo = getSubscriberMemoMapper().mapToDomain(subscriberMemo);
				memoInfo.setBanId(Integer.parseInt(billingAccountNumber));
				if ( Boolean.TRUE.equals( subscriberMemo.isAsyncInd()) ) {
					getAccountLifecycleFacade(context).asyncCreateMemo(memoInfo, context.getAccountLifeCycleFacadeSessionId() );
				} else {
					getAccountLifecycleManager(context).createMemo(memoInfo, context.getAccountLifeCycleManagerSessionId());
				}
				return null;
			}
		});
		
		
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0002", errorMessage="Failed to get subscriber identifier list by phone # and account #")
	public SubscriberIdentifierList getSubscriberIdentifierListByPhoneNumberAndAccountNumber(final String billingAccountNumber, final String phoneNumber)
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<SubscriberIdentifierList>() {

			@Override
			public SubscriberIdentifierList doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SubscriberIdentifierInfo sub = getSubscriberLifecycleHelper(context).retrieveSubscriberIdentifiersByPhoneNumber(Integer.valueOf(billingAccountNumber), phoneNumber);
				SubscriberIdentifierList subIdentifier = new SubscriberIdentifierList();
				subIdentifier.setSubscriberId(sub.getSubscriberId());
				subIdentifier.setSubscriptionId(sub.getSubscriptionId());
				return subIdentifier;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0003", errorMessage="Failed to get subscriber list by account #")
	public List<Subscriber> getSubscriberListByAccountNumber(final String billingAccountNumber) throws PolicyException,
			ServiceException {
		return execute (new ServiceInvocationCallback<List<Subscriber>>() {

			@Override
			public List<Subscriber> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				Collection<SubscriberInfo> subscriberList = getSubscriberLifecycleHelper(context).retrieveSubscriberListByBAN(Integer.valueOf(billingAccountNumber), MAX_SUBSCRIBER_LIST);
				return mapSubscriberList(subscriberList, context);
			}
			
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0004", errorMessage="Failed to get subscriber list by IMSI")
	public List<Subscriber> getSubscriberListByIMSI(final String imsi,final Boolean includeCancelledSubscribersInd) throws PolicyException,
			ServiceException {
		return execute(new ServiceInvocationCallback<List<Subscriber>>() {

			@Override
			public List<Subscriber> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				Collection<SubscriberInfo> subscriberList = getSubscriberLifecycleHelper(context).retrieveSubscriberListByImsi(imsi, includeCancelledSubscribersInd.booleanValue());
				return mapSubscriberList(subscriberList, context);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0005", errorMessage="Failed to get subscriber list by phone #")
	public List<Subscriber> getSubscriberListByPhoneNumber(final String phoneNumber)
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Subscriber>>() {

			@Override
			public List<Subscriber> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				Collection<SubscriberInfo> subscriberList = getSubscriberLifecycleHelper(context).retrieveSubscriberListByPhoneNumber(phoneNumber, MAX_SUBSCRIBER_LIST, true);
				return mapSubscriberList(subscriberList, context);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0006", errorMessage="Failed to set subscriber port in indicator")
	public void updatePortInSubscriber(final String phoneNumber)
			throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback<Object>(){
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
 				getSubscriberLifecycleManager(context).setSubscriberPortIndicator(phoneNumber, 
 						context.getSubscriberLifecycleManagerSessionId());
			return null;
			}
		});		
		
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0007", errorMessage="Failed to snap back phone number")
	public void updateSnapbackPhoneNumber(final String phoneNumber)
			throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback<Object>(){
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
					getSubscriberLifecycleManager(context).snapBack(phoneNumber,  
							context.getSubscriberLifecycleManagerSessionId());
				return null;
			}
		});
		
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0008", errorMessage="Failed to check subscriber port out eligibility")
	public PortOutEligibilityInfo checkPortOutEligibility(final String phoneNumber,	final NDPDirectionIndicator ndpDirectionIndicator)
			throws PolicyException, ServiceException {
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
	@ServiceBusinessOperation(errorCode="CMB_SIS_0009", errorMessage="Failed to Update Email Address")
	public void updateEmailAddress(final String ban,
								   final String subscriberNumber, 
								   final String emailAddress) throws PolicyException, ServiceException {
		execute (new ServiceInvocationCallback<Object>() {
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				getSubscriberLifecycleManager(context).updateEmailAddress(Integer.valueOf(ban), subscriberNumber, emailAddress, context.getSubscriberLifecycleManagerSessionId());
				return null;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0010", errorMessage="Failed to Get Email Address")
	public String getEmailAddress(final String ban,
								  final String subscriberNumber) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<String>() {
			@Override
			public String doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return getSubscriberLifecycleHelper(context).retrieveEmailAddress(Integer.valueOf(ban), subscriberNumber);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0011", errorMessage="Failed to Get Calling Circle Information")
	public CallingCircleInfo getCallingCircleInformation(final String ban, 
														 final String subscriberNumber,
														 final String productType, 
														 final String serviceCode, 
														 final String featureCode) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<CallingCircleInfo>() {
			@Override
			public CallingCircleInfo doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				CallingCircleParametersInfo callingCircleParametersInfo = getSubscriberLifecycleFacade(context).
						getCallingCircleInformation(Integer.valueOf(ban), subscriberNumber, serviceCode, featureCode, productType, context.getSubscriberLifecycleFacadeSessionId());	
				return SubscriberInformationServiceMapper.CallingCircleInformationMapper().mapToSchema(callingCircleParametersInfo);
			}
		});
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0012", errorMessage="Failed to get SubscriberListByDataSharingGroupList")
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
	@ServiceBusinessOperation(errorCode="CMB_SIS_0013", errorMessage="Failed to get SubscriberListBy FamilyGroupType")
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
	@ServiceBusinessOperation(errorCode="CMB_SIS_0014", errorMessage="Failed to get Subscriber By SubscriberID")
	public Subscriber getSubscriberBySubscriberId(final String subscriberId)
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Subscriber>() {

			@Override
			public Subscriber doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper(context).retrieveSubscriber(0,subscriberId);
				return mapSubscriber(subscriberInfo, context);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0015", errorMessage="Failed to get subscriber by phone number")
	public Subscriber getSubscriberByPhoneNumber(final String phoneNumber)
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Subscriber>() {

			@Override
			public Subscriber doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper(context).retrieveSubscriberByPhoneNumber(phoneNumber);
				return mapSubscriber(subscriberInfo, context);
			}
		});
	}

	private Subscriber mapSubscriber (SubscriberInfo subscriberInfo, ServiceInvocationContext context) throws ApplicationException, Exception {
		Subscriber wsSubscriber = SubscriberMapper_20.getInstance().mapToSchema(subscriberInfo);
		
		SubscriptionRoleInfo subscriptionRoleInfo = getSubscriberLifecycleHelper(context).retrieveSubscriptionRole(subscriberInfo.getPhoneNumber());
		wsSubscriber.setSubscriptionRole(SubscriptionRoleMapper.getInstance().mapToSchema(subscriptionRoleInfo));
		
		AddressInfo addressInfo = getSubscriberLifecycleHelper(context).retrieveSubscriberAddress(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());
		wsSubscriber.setAddress(AddressMapper.getInstance().mapToSchema(addressInfo));
		
		return wsSubscriber;
	}
	
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
	
	private SubscriberInformationServiceMapper.SubscriberPortOutEligibilityInfoMapper getSubscriberPortOutEligibilityInfoMapper() {
		return SubscriberInformationServiceMapper.SubscriberPortOutEligibilityInfoMapper();		
	}
	private SubscriberInformationServiceMapper.SubscriberMemoMapper getSubscriberMemoMapper() {
		return SubscriberInformationServiceMapper.SubscriberMemoMapper();
	}




}
