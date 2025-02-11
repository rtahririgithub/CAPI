/*
 *  Copyright (c) 2010 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.cmb.jws.mapper.ContractChangeMapper;
import com.telus.cmb.jws.mapper.FeatureParameterChangeMapper;
import com.telus.cmb.jws.mapper.PricePlanChangeMapper;
import com.telus.cmb.jws.mapper.ServiceChangeMapper;
import com.telus.cmb.jws.mapper.SubscriberStatusChangeMapper;
import com.telus.eas.subscriber.info.ContractChangeHistoryInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberHistoryInfo;
import com.telus.cmb.jws.ContractChangeHistoryList;
import com.telus.cmb.jws.ContractChangeHistoryResult;
import com.telus.cmb.jws.FeatureParameterHistoryList;
import com.telus.cmb.jws.FeatureParameterHistoryResult;
import com.telus.cmb.jws.GetContractChangeHistory;
import com.telus.cmb.jws.GetContractChangeHistoryResponse;
import com.telus.cmb.jws.GetFeatureParameterHistoryByParameterName;
import com.telus.cmb.jws.GetFeatureParameterHistoryByParameterNameResponse;
import com.telus.cmb.jws.GetPricePlanChangeHistory;
import com.telus.cmb.jws.GetPricePlanChangeHistoryResponse;
import com.telus.cmb.jws.GetServiceChangeHistory;
import com.telus.cmb.jws.GetServiceChangeHistoryResponse;
import com.telus.cmb.jws.GetSubscriberStatusHistory;
import com.telus.cmb.jws.GetSubscriberStatusHistoryResponse;
import com.telus.cmb.jws.PricePlanChangeHistoryList;
import com.telus.cmb.jws.PricePlanChangeHistoryResult;
import com.telus.cmb.jws.ServiceChangeHistoryList;
import com.telus.cmb.jws.ServiceChangeHistoryResult;
import com.telus.cmb.jws.SubscriberStatusHistoryList;
import com.telus.cmb.jws.SubscriberStatusHistoryResult;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingStats;

@SchemaValidation(handler = com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(portName = "SubscriberInformationHistoryServicePort", serviceName = "SubscriberInformationHistoryService_v1_0", 
			targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/SubscriberInformationHistoryService_1", 
			wsdlLocation = "/wsdls/SubscriberInformationHistoryService_v1_0.wsdl", 
			endpointInterface = "com.telus.cmb.jws.SubscriberInformationHistoryServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class SubscriberInformationHistoryService_10 extends BaseServiceV2 implements SubscriberInformationHistoryServicePort {

	protected static final int MAX_OCCUR = 1000;

	public SubscriberInformationHistoryService_10() {
		super(new SubscriberInformationHistoryExceptionTranslator());
	}

	/*
	 * The current XML2WSDL XSLT doesn't support version 2 of the ping operation. We need the simpler v1 operation instead.
	 *
	 * @Override
	@ServiceBusinessOperation(errorCode = "CMB_CHS_0011", errorMessage = "Ping error")
	public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse ping(com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping parameters) throws ServiceException {
		PingResponse pingResponse = new PingResponse();
		PingStats pingStats = new PingStats();
		pingStats.setServiceName("SubscriberInfoHistoryService v1.0");
		pingResponse.setPingStats(pingStats);
		return pingResponse;
	}*/

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_CHS_0011", errorMessage = "Ping error")
	public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse ping(com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping parameters) throws PolicyException, ServiceException {
		com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse pingResponse = new com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse();
		pingResponse.setVersion("SubscriberInfoHistoryService v1.0");
		return pingResponse;
	}
	

	@ServiceBusinessOperation(errorCode = "CMB_CHS_0001", errorMessage = "Customer price plan change history retrieval error")
	@Override
	public GetPricePlanChangeHistoryResponse getPricePlanChangeHistory(final GetPricePlanChangeHistory parameters) throws ServiceException {

		String ban = parameters.getBillingAccountNumber();
		final Date fromDate = parameters.getFromDate();
		final Date toDate = parameters.getToDate();
		final int banInt = new Integer(ban).intValue();

		return execute(new ServiceInvocationCallback<GetPricePlanChangeHistoryResponse>() {
			@Override
			public GetPricePlanChangeHistoryResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				GetPricePlanChangeHistoryResponse ret = new GetPricePlanChangeHistoryResponse();
				if (context.getSchemaValidationException() != null) {
					return schemaValidationErrorToResponse(ret, context.getSchemaValidationException());
				}

				PricePlanChangeHistoryInfo[] historyInfos = getSubscriberLifecycleHelper(context).retrievePricePlanChangeHistory(banInt, parameters.getSubscriberNumber(), fromDate, toDate);

				PricePlanChangeHistoryInfo[] historyInfos1000;
				if (historyInfos.length > MAX_OCCUR) {
					// return first MAX_OCCUR elements
					historyInfos1000 = new PricePlanChangeHistoryInfo[MAX_OCCUR];
					System.arraycopy(historyInfos, 0, historyInfos1000, 0, MAX_OCCUR);
				} else {
					historyInfos1000 = historyInfos;
				}

				

				PricePlanChangeHistoryResult pricePlanChangeHistoryResult = new PricePlanChangeHistoryResult();
				pricePlanChangeHistoryResult.setBillingAccountNumber(Integer.toString(banInt));
				pricePlanChangeHistoryResult.setSubscriberNumber(parameters.getSubscriberNumber());
				pricePlanChangeHistoryResult.setHistoryList(mapPricePlanChangeHistoryInfoList(historyInfos1000));

				ret.setPricePlanChangeHistory(pricePlanChangeHistoryResult);

				return ret;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_CHS_0002", errorMessage = "Customer service change history retrieval error")
	@Override
	public GetServiceChangeHistoryResponse getServiceChangeHistory(final GetServiceChangeHistory parameters) throws ServiceException {

		final String ban = parameters.getBillingAccountNumber();
		final Date fromDate = parameters.getFromDate();
		final Date toDate = parameters.getToDate();
		final int banInt = new Integer(ban).intValue();
		final boolean includeAllServiceTypes = parameters.isIncludeAllServicesInd();
		final String subscriberNumber = parameters.getSubscriberNumber();

		return execute(new ServiceInvocationCallback<GetServiceChangeHistoryResponse>() {
			@Override
			public GetServiceChangeHistoryResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				GetServiceChangeHistoryResponse ret = new GetServiceChangeHistoryResponse();
				if (context.getSchemaValidationException() != null) {
					return schemaValidationErrorToResponse(ret, context.getSchemaValidationException());
				}

				ServiceChangeHistoryInfo[] serviceChangeHistoryArray = getSubscriberLifecycleHelper(context).retrieveServiceChangeHistory(banInt, subscriberNumber, fromDate, toDate, includeAllServiceTypes);

				ServiceChangeHistoryInfo[] serviceChangeHistoryInfoListForResponse;
				if (serviceChangeHistoryArray.length > MAX_OCCUR) {
					// return first MAX_OCCUR elements
					serviceChangeHistoryInfoListForResponse = new ServiceChangeHistoryInfo[MAX_OCCUR];
					System.arraycopy(serviceChangeHistoryArray, 0, serviceChangeHistoryArray, 0, MAX_OCCUR);
				} else {
					serviceChangeHistoryInfoListForResponse = serviceChangeHistoryArray;
				}

				ServiceChangeHistoryResult serviceChangeHistoryResult = new ServiceChangeHistoryResult();
				serviceChangeHistoryResult.setServiceChangeHistoryList(mapServiceChangeHistoryInfoList(serviceChangeHistoryInfoListForResponse));
				serviceChangeHistoryResult.setBillingAccountNumber(ban);
				serviceChangeHistoryResult.setSubscriberNumber(subscriberNumber);
				ret.setServiceChangeHistory(serviceChangeHistoryResult);
				return ret;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_CHS_0003", errorMessage = "Customer contract change history retrieval error")
	@Override
	public GetContractChangeHistoryResponse getContractChangeHistory(final GetContractChangeHistory parameters) throws ServiceException {
		String ban = parameters.getBillingAccountNumber();
		final Date fromDate = parameters.getFromDate();
		final Date toDate = parameters.getToDate();
		final int banInt = new Integer(ban).intValue();

		return execute(new ServiceInvocationCallback<GetContractChangeHistoryResponse>() {
			@Override
			public GetContractChangeHistoryResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				GetContractChangeHistoryResponse ret = new GetContractChangeHistoryResponse();
				if (context.getSchemaValidationException() != null) {
					return schemaValidationErrorToResponse(ret, context.getSchemaValidationException());
				}

				ContractChangeHistoryInfo[] contractChangeHistoryInfo = getSubscriberLifecycleHelper(context).retrieveContractChangeHistory(banInt, parameters.getSubscriberNumber(), fromDate, toDate);

				ContractChangeHistoryInfo[] contractChangeHistoryInfo1000;
				if (contractChangeHistoryInfo.length > MAX_OCCUR) {
					// return first MAX_OCCUR elements
					contractChangeHistoryInfo1000 = new ContractChangeHistoryInfo[MAX_OCCUR];
					System.arraycopy(contractChangeHistoryInfo, 0, contractChangeHistoryInfo1000, 0, MAX_OCCUR);
				} else {
					contractChangeHistoryInfo1000 = contractChangeHistoryInfo;
				}

				ContractChangeHistoryResult contractChangeHistoryResult = new ContractChangeHistoryResult();
				contractChangeHistoryResult.setHistoryList(mapContractChangeHistoryInfoList(contractChangeHistoryInfo1000));
				contractChangeHistoryResult.setBillingAccountNumber(parameters.getBillingAccountNumber());
				contractChangeHistoryResult.setSubscriberNumber(parameters.getSubscriberNumber());
				ret.setContractChangeHistory(contractChangeHistoryResult);
				return ret;
			}
		});

	}


	@ServiceBusinessOperation(errorCode = "CMB_CHS_0004", errorMessage = "Customer subscrirber change history retrieval error")
	@Override
	public GetSubscriberStatusHistoryResponse getSubscriberStatusChangeHistory(final GetSubscriberStatusHistory parameters) throws ServiceException {
		String ban = parameters.getBillingAccountNumber();
		final Date fromDate = parameters.getFromDate();
		final Date toDate = parameters.getToDate();
		final int banInt = new Integer(ban).intValue();

		return execute(new ServiceInvocationCallback<GetSubscriberStatusHistoryResponse>() {
			@Override
			public GetSubscriberStatusHistoryResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				GetSubscriberStatusHistoryResponse ret = new GetSubscriberStatusHistoryResponse();
				if (context.getSchemaValidationException() != null) {
					return schemaValidationErrorToResponse(ret, context.getSchemaValidationException());
				}

				List<SubscriberHistoryInfo> subscriberStatusChangeHistoryInfo = getSubscriberLifecycleHelper(context).retrieveSubscriberHistory(banInt, parameters.getSubscriberNumber(), fromDate,
						toDate);

				if (subscriberStatusChangeHistoryInfo.size() > MAX_OCCUR) {
					// returns first MAX_OCCUR elements
					subscriberStatusChangeHistoryInfo = subscriberStatusChangeHistoryInfo.subList(0, MAX_OCCUR - 1);
				}

				SubscriberStatusHistoryResult subscriberStatusHistoryResult = new SubscriberStatusHistoryResult();
				subscriberStatusHistoryResult.setHistoryList(mapSubscriberStatusChangeHistoryInfoList(subscriberStatusChangeHistoryInfo));
				subscriberStatusHistoryResult.setBillingAccountNumber(Integer.toString(banInt));
				subscriberStatusHistoryResult.setSubscriberNumber(parameters.getSubscriberNumber());
				ret.setSubscriberStatusHistory(subscriberStatusHistoryResult);
				return ret;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_CHS_0005", errorMessage = "Customer feature parameter history retrieval error")
	@Override
	public GetFeatureParameterHistoryByParameterNameResponse getFeatureParameterHistoryByParameterName(final GetFeatureParameterHistoryByParameterName parameters) throws ServiceException {
		String ban = parameters.getBillingAccountNumber();
		final Date fromDate = parameters.getFromDate();
		final Date toDate = parameters.getToDate();
		final int banInt = new Integer(ban).intValue();

		final String[] parameterNames = parameters.getParameterNameList().getParameterNames().toArray(new String[parameters.getParameterNameList().getParameterNames().size()]);
		return execute(new ServiceInvocationCallback<GetFeatureParameterHistoryByParameterNameResponse>() {
			@Override
			public GetFeatureParameterHistoryByParameterNameResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				GetFeatureParameterHistoryByParameterNameResponse ret = new GetFeatureParameterHistoryByParameterNameResponse();
				if (context.getSchemaValidationException() != null) {
					return schemaValidationErrorToResponse(ret, context.getSchemaValidationException());
				}

				FeatureParameterHistoryInfo[] featureParameterChangeHistoryInfo = getSubscriberLifecycleHelper(context).retrieveFeatureParameterHistory(banInt, parameters.getSubscriberNumber(),
						parameters.getProductType(), parameterNames, fromDate, toDate);
				FeatureParameterHistoryInfo[] featureParameterChangeHistoryInfo1000;
				if (featureParameterChangeHistoryInfo.length > MAX_OCCUR) {
					// return first MAX_OCCUR elements
					featureParameterChangeHistoryInfo1000 = new FeatureParameterHistoryInfo[MAX_OCCUR];
					System.arraycopy(featureParameterChangeHistoryInfo, 0, featureParameterChangeHistoryInfo1000, 0, MAX_OCCUR);
				} else {
					featureParameterChangeHistoryInfo1000 = featureParameterChangeHistoryInfo;
				}

				FeatureParameterHistoryResult featureParameterHistoryResult = new FeatureParameterHistoryResult();
				featureParameterHistoryResult.setBillingAccountNumber(Integer.toString(banInt));
				featureParameterHistoryResult.setSubscriberNumber(parameters.getSubscriberNumber());
				featureParameterHistoryResult.setProductType(parameters.getProductType());
				featureParameterHistoryResult.setHistoryList(mapFeatureParameterInfoList(featureParameterChangeHistoryInfo1000));
				ret.setFeatureParameterChangeHistory(featureParameterHistoryResult);
				return ret;
			}
		});
	}

	private PricePlanChangeHistoryList mapPricePlanChangeHistoryInfoList(PricePlanChangeHistoryInfo[] historyInfos) {

		PricePlanChangeHistoryList pricePlanChangeHistoryList = new PricePlanChangeHistoryList();
		PricePlanChangeMapper pricePlanStatusChangeMapper = PricePlanChangeMapper.getInstance();
		pricePlanChangeHistoryList.getPricePlanChangeHistory().addAll(pricePlanStatusChangeMapper.mapToSchema(historyInfos));

		return pricePlanChangeHistoryList;
	}

	private ServiceChangeHistoryList mapServiceChangeHistoryInfoList(ServiceChangeHistoryInfo[] historyInfos) {

		ServiceChangeHistoryList serviceChangeHistoryList = new ServiceChangeHistoryList();
		ServiceChangeMapper serviceChangeMapper = ServiceChangeMapper.getInstance();
		serviceChangeHistoryList.getServiceChangeHistory().addAll(serviceChangeMapper.mapToSchema(historyInfos));

		return serviceChangeHistoryList;
	}

	private SubscriberStatusHistoryList mapSubscriberStatusChangeHistoryInfoList(List<SubscriberHistoryInfo> historyInfos) {

		SubscriberStatusHistoryList subscriberStatusHistoryList = new SubscriberStatusHistoryList();
		SubscriberStatusChangeMapper statusChangeMapper = SubscriberStatusChangeMapper.getInstance();
		subscriberStatusHistoryList.getSubscriberHistoryDetail().addAll(statusChangeMapper.mapToSchema(historyInfos));

		return subscriberStatusHistoryList;
	}

	private FeatureParameterHistoryList mapFeatureParameterInfoList(FeatureParameterHistoryInfo[] featureParameterChangeHistoryInfo) {

		FeatureParameterHistoryList featureParameterHistoryList = new FeatureParameterHistoryList();
		FeatureParameterChangeMapper featureParameterChangeMapper = FeatureParameterChangeMapper.getInstance();
		featureParameterHistoryList.getFeatureParameterHistory().addAll(featureParameterChangeMapper.mapToSchema(featureParameterChangeHistoryInfo));
		return featureParameterHistoryList;
	}

	private ContractChangeHistoryList mapContractChangeHistoryInfoList(ContractChangeHistoryInfo[] historyInfos) {

		ContractChangeHistoryList contract = new ContractChangeHistoryList();
		ContractChangeMapper contractChangeMapper = ContractChangeMapper.getInstance();
		contract.getContractChangeHistory().addAll(contractChangeMapper.mapToSchema(historyInfos));

		return contract;
	}
	
	private <T extends ResponseMessage> T schemaValidationErrorToResponse (T response, Exception exception) {
		response.setErrorCode(SCHEMA_VALIDATION_ERROR_CODE);
		response.setMessageType("ERROR");
		Message msg = new Message();
		msg.setMessage(exception.getMessage());
		response.getMessageList().add(msg);
		
		return response;
	}
}
