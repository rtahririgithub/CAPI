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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.cmb.jws.GetVendorServiceChangeHistoryBySOCs.SocList;
import com.telus.cmb.jws.mapper.ContractChangeMapper;
import com.telus.cmb.jws.mapper.FeatureParameterChangeMapper;
import com.telus.cmb.jws.mapper.PricePlanChangeMapper;
import com.telus.cmb.jws.mapper.ServiceChangeInfoMapper;
import com.telus.cmb.jws.mapper.SubscriberStatusChangeMapper;
import com.telus.eas.subscriber.info.ContractChangeHistoryInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberHistoryInfo;
import com.telus.eas.subscriber.info.VendorServiceChangeHistoryInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.VendorServiceInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

@SchemaValidation(handler = com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(portName = "SubscriberInformationHistoryServicePort", serviceName = "SubscriberInformationHistoryService_v1_1", 
			targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/SubscriberInformationHistoryService_1", 
			wsdlLocation = "/wsdls/SubscriberInformationHistoryService_v1_1.wsdl", 
			endpointInterface = "com.telus.cmb.jws.SubscriberInformationHistoryServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class SubscriberInformationHistoryService_11 extends BaseServiceV2 implements SubscriberInformationHistoryServicePort {

	protected static final int MAX_OCCUR = 1000;

	public SubscriberInformationHistoryService_11() {
		super(new SubscriberInformationHistoryExceptionTranslator());
	}

	/*
	 * Unfortunately the Xml2Wsdl XSLT in use right now does not support this more advanced ping operation. 
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_CHS_0011", errorMessage = "Ping error")
	public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse ping(com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping parameters) throws ServiceException {
		PingResponse pingResponse = new PingResponse();
		PingStats pingStats = new PingStats();
		pingStats.setServiceName("SubscriberInfoHistoryService v1.1");
		pingResponse.setPingStats(pingStats);
		return pingResponse;
	}*/

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_CHS_0011", errorMessage = "Ping error")
	public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse ping(Ping parameters) throws PolicyException, ServiceException {
		com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse pingResponse = new com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse();
		pingResponse.setVersion("SubscriberInfoHistoryService v1.1");
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

	@ServiceBusinessOperation(errorCode = "CMB_CHS_0006", errorMessage = "Customer vendor service change history retrieval error")
	@Override
	public GetVendorServiceChangeHistoryBySOCsResponse getVendorServiceChangeHistoryBySOCs(final GetVendorServiceChangeHistoryBySOCs parameters) 
			throws ServiceException {
		
		// Retrieve input parameters
		final String ban = parameters.getBillingAccountNumber();
		final int banInt = new Integer(ban).intValue();
		final String subscriberNumber = parameters.getSubscriberNumber();
		validateSocList(parameters.getSocList());
		final String soc = parameters.getSocList().getSoc().get(0); // only support one SOC input for now.

		return execute(new ServiceInvocationCallback<GetVendorServiceChangeHistoryBySOCsResponse>() {
			@Override
			public GetVendorServiceChangeHistoryBySOCsResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				GetVendorServiceChangeHistoryBySOCsResponse response = new GetVendorServiceChangeHistoryBySOCsResponse();
				if (context.getSchemaValidationException() != null) {
					return schemaValidationErrorToResponse(response, context.getSchemaValidationException());
				}
				
				VendorServiceChangeHistoryList vendorServiceChangeHistoryList = new VendorServiceChangeHistoryList();

				// Get Service from the input SOC code
				ServiceInfo service = getReferenceDataReferenceFacade().getRegularService(soc);
				String[] categoryCodes = service.getCategoryCodes();
				
				// Get category codes from Service
				List<String> vendorCategoryCodes = new ArrayList<String>();
				if (categoryCodes != null) {
					
					for (String categoryCode : categoryCodes) {
						VendorServiceInfo vendorService = getReferenceDataReferenceFacade().getVendorService(categoryCode);
						if (vendorService != null && vendorService.isRestrictionRequired() == true) {
							vendorCategoryCodes.add(vendorService.getCode());
						}
					}
				}
				
				// Get Vendor Service History from category codes
				if (vendorCategoryCodes != null && vendorCategoryCodes.size() > 0) {
					VendorServiceChangeHistoryInfo[] changeHistoryInfos = getSubscriberLifecycleHelper(context).retrieveVendorServiceChangeHistory(banInt, subscriberNumber, 
							vendorCategoryCodes.toArray(new String[vendorCategoryCodes.size()]));

					if (changeHistoryInfos != null) {
						for (VendorServiceChangeHistoryInfo changeHistoryInfo : changeHistoryInfos) {
							
							String vendorServiceCode = changeHistoryInfo.getVendorServiceCode();
							com.telus.api.account.ServiceChangeHistory[] changeHistoryArray = changeHistoryInfo.getPromoSOCs();
							
							// Cast the ServiceChangeHistory to ServiceChangeHistoryInfo
							ArrayList<ServiceChangeHistoryInfo> changeHistoryInfoArray = new ArrayList<ServiceChangeHistoryInfo>();
							if (changeHistoryArray != null) {
								for (com.telus.api.account.ServiceChangeHistory cHistory : changeHistoryArray) {
									changeHistoryInfoArray.add((ServiceChangeHistoryInfo)cHistory);
								}
							}
							
							// Set results into web service response
							VendorServiceChangeHistory vsch = new VendorServiceChangeHistory();
							vsch.setVendorServiceCode(vendorServiceCode);
							vsch.setServiceChangeHistoryList(mapServiceChangeHistoryInfoList(changeHistoryInfoArray.toArray(new ServiceChangeHistoryInfo[changeHistoryInfoArray.size()])));
							vendorServiceChangeHistoryList.getVendorServiceChangeHistoryList().add(vsch);
						}
					}
				}
				
				response.setVendorServiceChangeHistoryList(vendorServiceChangeHistoryList);
				return response;
			}
		});
		
	}
	
	private void validateSocList(SocList socList) {
		
		if (socList == null) {
			throw new ServicePolicyException(ServiceErrorCodes.ERROR_DESC_INVALID_INPUT, "Input SOC code list is empty.");
		}
		
		// So far only 1 SOC input is supported. Multiple SOC inputs could be implemented in the future by verifying whether vendor category codes
		// returned from different SOCs can be merged.
		int socCount = socList.getSoc().size();
		if (socCount == 0) {
			throw new ServicePolicyException(ServiceErrorCodes.ERROR_DESC_INVALID_INPUT, "Input SOC code is empty.");
		} else if (socCount > 1) {
			throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "Input of more than one SOC code is not yet supported.");
		} else if (socList.getSoc().get(0) == null || socList.getSoc().get(0).trim().length() == 0) {
			throw new ServicePolicyException(ServiceErrorCodes.ERROR_DESC_INVALID_INPUT, "Input SOC code is an empty String.");
		}
	}
	
	private PricePlanChangeHistoryList mapPricePlanChangeHistoryInfoList(PricePlanChangeHistoryInfo[] historyInfos) {

		PricePlanChangeHistoryList pricePlanChangeHistoryList = new PricePlanChangeHistoryList();
		PricePlanChangeMapper pricePlanStatusChangeMapper = PricePlanChangeMapper.getInstance();
		pricePlanChangeHistoryList.getPricePlanChangeHistory().addAll(pricePlanStatusChangeMapper.mapToSchema(historyInfos));

		return pricePlanChangeHistoryList;
	}
	
	private ServiceChangeHistoryList mapServiceChangeHistoryInfoList(ServiceChangeHistoryInfo[] historyInfos) {

		ServiceChangeHistoryList serviceChangeHistoryList = new ServiceChangeHistoryList();
		ServiceChangeInfoMapper serviceChangeInfoMapper = ServiceChangeInfoMapper.getInstance();
		serviceChangeHistoryList.getServiceChangeHistory().addAll(serviceChangeInfoMapper.mapToSchema(historyInfos));

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
