/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.account.Account;
import com.telus.api.account.Subscriber;
import com.telus.api.portability.PRMReferenceData;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestSummary;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Province;
import com.telus.cmb.framework.resource.ResourceExecutionCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.PortRequestInformationDao;
import com.telus.cmb.subscriber.utilities.PortRequestFieldTrimmer;
import com.telus.cmb.wsclient.PortRequestInformationPort;
import com.telus.eas.portability.info.PRMReferenceDataInfo;
import com.telus.eas.portability.info.PortRequestAddressInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.portability.info.PortRequestNameInfo;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.CheckPortRequestStatus;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.CheckPortRequestStatusResponse;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.CompletePortRequestDataType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.GetCurrentPortRequests;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.GetCurrentPortRequestsResponse.RetrievePortRequestCollection;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.GetReferenceData;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.GetReferenceDataResponse;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.GetReferenceDataResponse.ReferenceDefinitionList;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.PortRequestStatusCheckRequestDataType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.PortRequestStatusCheckResponseDataType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.PreportValidationRequestDataBodyType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.PreportValidationRequestDataType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.ReferenceDefinitionDataType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.ReferenceDefinitionType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.RetrieveCurrentPortRequestDataType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.ValidatePortInRequest;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.ValidatePortInRequestResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Description;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.MultilingualCodeDescriptionList;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourceorderservicecommontypes_v2.AccountInfoType;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourceorderservicecommontypes_v2.BillingAddressType;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourceorderservicecommontypes_v2.HeaderType;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourceorderservicecommontypes_v2.PortTnType;

/**
 * @author Pavel Simonovsky
 *
 */
public class PortRequestInformationDaoImpl extends WnpLegacyClient implements PortRequestInformationDao {

	private static final String PRM_TRUE = "Y";

	private static final String PRM_FALSE = "N";
	
	private static final String PRM_DATE_FORMAT = "MMddyyyyhhmmss";
	
	@Autowired
	private PortRequestInformationPort port;

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclefacade.dao.PortRequestInformationDao#isValidPortInRequest(com.telus.eas.portability.info.PortRequestInfo, java.lang.String, java.lang.String, com.telus.api.reference.Province[])
	 */
	@Override
	public boolean isValidPortInRequest(final PortRequestInfo portRequest, final String applicationId, final String user, final Province[] provinces) throws ApplicationException {

		return execute( new ResourceExecutionCallback<Boolean>() {
			
			@Override
			public Boolean doInCallback() throws Exception {
				Subscriber subscriber = portRequest.getSubscriber();
				Account account = portRequest.getAccount();

				ValidatePortInRequest request = new ValidatePortInRequest();

				PreportValidationRequestDataType requestData = new PreportValidationRequestDataType();

				// set request header
				HeaderType requestDataHeader = new HeaderType();
				
				String appId = applicationId;
				if (appId.length() > 6) {
					appId = appId.substring(0,6);
				}
				
				requestDataHeader.setOriginatorName(appId);
				requestDataHeader.setRequestId(portRequest.getPortRequestId());
				requestDataHeader.setDestinationName("SMG");
				Calendar toDay = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat(PRM_DATE_FORMAT);
				String timeStamp = formatter.format(toDay.getTime());
				requestDataHeader.setHeaderTimestamp(timeStamp);

				requestData.setPortDataHeader(requestDataHeader);

				// prepare request body
				PreportValidationRequestDataBodyType requestDataBody = new PreportValidationRequestDataBodyType();

				// set product type
				if (portRequest.isPortInFromMVNE()) {
					requestDataBody.setProductTypeCd("INTR_R2T");
				} else {
					if (subscriber.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
						requestDataBody.setProductTypeCd("EXT_2I");
						
					} else if (subscriber.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
						if (portRequest.getEquipment().isHSPA()) {
							requestDataBody.setProductTypeCd("EXT_2H");
						} else {
							requestDataBody.setProductTypeCd("EXT_2C");
						}
					}
				}

				requestDataBody.setAgentAuthorizedInd(StringUtils.equalsIgnoreCase(portRequest.getAuthorizationIndicator(),"Y"));
				requestDataBody.setAgentAuthorizedDate(portRequest.getAgencyAuthorizationDate());
				//enforce the Authorization name not to exceed 60
				portRequest.setAgencyAuthorizationName(
						PortRequestFieldTrimmer.trucate( portRequest.getAgencyAuthorizationName(), PortRequestFieldTrimmer.AGENCY_NAME_LEN)
				);
				requestDataBody.setAgentAuthorizedName(portRequest.getAgencyAuthorizationName());

				if (account.isPostpaid()) {
					requestDataBody.setProductBillingMethodTypeCd("POSTPAID");
				} else if (account.isPrepaidConsumer()) {
					requestDataBody.setProductBillingMethodTypeCd("PREPAID");
				}

				requestDataBody.setInitiatorRepresentativeId(user);

				// set alternate contact number
				requestDataBody.setImplementContactTelephoneNumber(portRequest.getAlternateContactNumber());

				// set billing name & address
				BillingAddressType billAddress = new BillingAddressType();
				billAddress.setFirstName(portRequest.getPortRequestName().getFirstName());

				if (StringUtils.isNotEmpty(portRequest.getPortRequestName().getMiddleInitial())) {
					billAddress.setMiddleInitialTxt(portRequest.getPortRequestName().getMiddleInitial());
				}
				
				billAddress.setLastName(portRequest.getPortRequestName().getLastName());
				billAddress.setPrefixTxt(portRequest.getPortRequestName().getTitle());
				billAddress.setStreetDirectionTxt(portRequest.getPortRequestAddress().getStreetDirection());
				billAddress.setStreetName(portRequest.getPortRequestAddress().getStreetName());
				
				if (StringUtils.isNotEmpty(portRequest.getPortRequestAddress().getStreetNumber())) {
					billAddress.setStreetNum(portRequest.getPortRequestAddress().getStreetNumber());
				}
				
				billAddress.setSuffixTxt(portRequest.getPortRequestName().getGeneration());
				billAddress.setBusinessName(portRequest.getBusinessName());
				billAddress.setCityName(portRequest.getPortRequestAddress().getCity());
				billAddress.setCountryName(portRequest.getPortRequestAddress().getCountry());
				billAddress.setPostalCd(portRequest.getPortRequestAddress().getPostalCode());
				
				String province = portRequest.getPortRequestAddress().getProvince();
				String canadaPostCd = getCanadaPostCode(province, portRequest.getPortRequestAddress().getCountry(), provinces);
				
				billAddress.setProvinceCd(canadaPostCd);
				requestDataBody.setBillingAddress(billAddress);
				
				// set account info
				AccountInfoType accountInfo = new AccountInfoType();
				accountInfo.setAccountNumber(portRequest.getOSPAccountNumber());
				accountInfo.setPin(portRequest.getOSPPin());
				accountInfo.setEsn(portRequest.getOSPSerialNumber());
				requestDataBody.setAccountInfo(accountInfo);
				
				// set phone number
				PortTnType phoneNumber = new PortTnType();
				phoneNumber.setPhoneNumber(portRequest.getPhoneNumber());
				requestDataBody.setPortedTelephoneNumber(phoneNumber);
				
				requestDataBody.setRemarksTxt(portRequest.getRemarks());
				requestDataBody.setAutoActivateCd(portRequest.isAutoActivate() ? PRM_TRUE : PRM_FALSE);

				requestDataBody.setExpediteInd(StringUtils.equals(portRequest.getExpedite(), PRM_TRUE));

				if (PortInEligibility.PORT_DIRECTION_INDICATOR_WIRELINE_WIRELESS.equalsIgnoreCase(portRequest.getPortDirectionIndicator())) {
					// the following three fields only apply to for intermodal port.
					// (wireline to wireless)
					requestDataBody.setDigitalSubscriberLineCd(portRequest.getDslInd());
					if (portRequest.getDslLineNumber() != null) {
						requestDataBody.setDigitalSubscriberLineNumber(String.valueOf(portRequest.getDslLineNumber()));
					}
					requestDataBody.setEndUserMovingInd(portRequest.getEndUserMovingInd());
				}
				
				if (StringUtils.isNotEmpty(portRequest.getOldReseller())) {
					requestDataBody.setOldResellerName(portRequest.getOldReseller());
				}

				requestData.setPortDataBody(requestDataBody);

				PortRequestFieldTrimmer.newInstance(false).trim(requestData);
				requestData.getPortDataBody().getBillingAddress().setMiddleInitialTxt(
				        PortRequestFieldTrimmer.trucate( requestData.getPortDataBody().getBillingAddress().getMiddleInitialTxt(), PortRequestFieldTrimmer.BILL_MIDDLE_INIT_LEN )
				);

				// set request data
				request.setPreportValidationRequestData(requestData);

				ValidatePortInRequestResponse response = port.validatePortInRequest(request);
				
				assertResponse("WNP", response);
				
				return response.getPreportValidationResponseData().isPreportValidationInd();
			}
		}, "0001", "SUBS-SVC", "PRIS", "WNP");
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclefacade.dao.PortRequestInformationDao#getPortRequestStatus(java.lang.String, int)
	 */
	@Override
	public PortRequestSummary getPortRequestStatus(final String phoneNumber, final int brandId) throws ApplicationException {
		
		return execute( new ResourceExecutionCallback<PortRequestSummary>() {
			
			@Override
			public PortRequestSummary doInCallback() throws Exception {
				PortRequestInfo summary = null;
				CheckPortRequestStatus request = new CheckPortRequestStatus();

				PortRequestStatusCheckRequestDataType requestData = new PortRequestStatusCheckRequestDataType();
				requestData.setPhoneNumber(phoneNumber);
				requestData.setBrandId(String.valueOf(brandId));

				request.setPortRequestStatusCheckRequestData(requestData);

				CheckPortRequestStatusResponse response = port.checkPortRequestStatus(request);
				PortRequestStatusCheckResponseDataType statusCheck = response.getPortRequestStatusCheckResponseData();

				if (statusCheck != null) {
					summary = new PortRequestInfo();
					String banId = statusCheck.getBillingAccountInfo().getBillingAccountId();
					if (banId != null && !banId.trim().equals("")) {
						summary.setBanId(Integer.parseInt(banId));
					}
					summary.setBusinessName(statusCheck.getBillingAccountInfo().getBusinessName());
					summary.setCanBeActivate(statusCheck.isCanBeActivatedInd());
					summary.setCanBeCancel(statusCheck.isCanBeCancelledInd());
					summary.setCanBeModify(statusCheck.isCanBeModifiedInd());
					summary.setCanBeSubmit(statusCheck.isCanBeSubmittedInd());
					String creationDate = statusCheck.getCreationDate();
					String year = creationDate.substring(4, 8);
					String month = creationDate.substring(0, 2);
					String day = creationDate.substring(2, 4);
					summary.setCreationDate(java.sql.Date.valueOf(year + "-" + month + "-" + day));
					summary.setPhoneNumber(phoneNumber);
					summary.setPortRequestId(statusCheck.getRequestId());
					String status = statusCheck.getStatusCd();
					summary.setStatusCode(status);
					summary.setStatusReasonCode("");
					summary.setStatusCategory(getStatusCategory(status));
					if (statusCheck.getPortTypeCd().equals("PORT_IN"))
						summary.setType(PortRequest.TYPE_PORT_IN);
					else if (statusCheck.getPortTypeCd().equals("PORT_OUT"))
						summary.setType(PortRequest.TYPE_PORT_OUT);
					else if (statusCheck.getPortTypeCd().equals("PORT_WITHIN"))
						summary.setType(PortRequest.TYPE_PORT_WITHIN);
					summary.setIncomingBrandId(statusCheck.getTargetBrandId() != null ? Integer.parseInt(statusCheck.getTargetBrandId()) : Brand.BRAND_ID_NOT_APPLICABLE);
					summary.setOutgoingBrandId(statusCheck.getSourceBrandId() != null ? Integer.parseInt(statusCheck.getSourceBrandId()) : Brand.BRAND_ID_NOT_APPLICABLE);
				}
				
				return summary;			
			}
		}, "0002", "SUBS-SVC", "PRIS", "WNP");
	}
	
	@Override
	public Collection<PortRequestInfo> getCurrentPortRequestsByBan(final int banNumber, final Province[] provinces) throws ApplicationException {

		return execute( new ResourceExecutionCallback<Collection<PortRequestInfo>>() {
			
			@Override
			public Collection<PortRequestInfo> doInCallback() throws Exception {
				List<CompletePortRequestDataType> vec = new ArrayList<CompletePortRequestDataType>();

				RetrieveCurrentPortRequestDataType requestData = new RetrieveCurrentPortRequestDataType();

				requestData.setInputDataValueTxt(String.valueOf(banNumber));
				requestData.setInputTypeId(4L);
				
				GetCurrentPortRequests request = new GetCurrentPortRequests();
				request.setRetrieveCurrentPortRequestData(requestData);
				
				RetrievePortRequestCollection response = port.getCurrentPortRequests(request).getRetrievePortRequestCollection();
				
				List<CompletePortRequestDataType> allPrmPortRequests = response.getPortRequestList();

				for (CompletePortRequestDataType portRequestType : allPrmPortRequests) {
					// we only want port-in port requests
					if (portRequestType.getIcpRequestTypeCd().equals(PortRequestSummary.TYPE_PORT_IN)) {
						vec.add(portRequestType);
					}
				}

				List<PortRequestInfo> portRequests = new ArrayList<PortRequestInfo>();
				
				for (CompletePortRequestDataType portRequestType : vec) {
					
					PortRequestInfo pri = new PortRequestInfo();
					
					pri.setAgencyAuthorizationDate(portRequestType.getAgentAuthorizedDate());
					pri.setAgencyAuthorizationIndicator(portRequestType.isAgentAuthorizedInd() ? "Y" : "N");
					pri.setAgencyAuthorizationName(portRequestType.getAgentAuthorizedName());
					pri.setAlternateContactNumber(portRequestType.getAlternativeContactTelephoneNumber());
					pri.setAutoActivate(portRequestType.getAutoActivateCd() != null && portRequestType.getAutoActivateCd().equals("Y"));
					pri.setBanId(Integer.parseInt(portRequestType.getBillingAccountInfo().getBillingAccountId()));
					pri.setBusinessName(portRequestType.getBillingAddress().getBusinessName());
					pri.setCanBeActivate(portRequestType.isCanBeActivatedInd());
					pri.setCanBeCancel(portRequestType.isCanBeCancelledInd());
					pri.setCanBeModify(portRequestType.isCanBeModifiedInd());
					pri.setCanBeSubmit(portRequestType.isCanBeSubmittedInd());

					String dateSent = portRequestType.getSentDate();
					String year1 = dateSent.substring(4, 8);
					String month1 = dateSent.substring(0, 2);
					String day1 = dateSent.substring(2, 4);
					pri.setCreationDate(java.sql.Date.valueOf(year1 + "-" + month1 + "-" + day1));

					if (portRequestType.getDesiredDueDate() != null && !portRequestType.getDesiredDueDate().trim().equals("")) {
						String desiredDateTime = portRequestType.getDesiredDueDate().substring(0, 8);
						String year = desiredDateTime.substring(4, 8);
						String month = desiredDateTime.substring(0, 2);
						String day = desiredDateTime.substring(2, 4);
						java.sql.Date strToDate = java.sql.Date.valueOf(year + "-" + month + "-" + day);
						pri.setDesiredDateTime(strToDate);
					}

					pri.setOSPAccountNumber(portRequestType.getAccountInfo().getAccountNumber());
					pri.setOSPPin(portRequestType.getAccountInfo().getPin());
					pri.setOSPSerialNumber(portRequestType.getAccountInfo().getEsn());
					pri.setPhoneNumber(portRequestType.getPortedTelephoneNumber().get(0).getPortedTNInfo().getPhoneNumber());
					pri.setPortDirectionIndicator(portRequestType.getNumberPortabilityDirectionCd());
					PortRequestAddressInfo portRequestAddress = new PortRequestAddressInfo();
					portRequestAddress.setCity(portRequestType.getBillingAddress().getCityName());
					portRequestAddress.setCountry(portRequestType.getBillingAddress().getCountryName());
					portRequestAddress.setPostalCode(portRequestType.getBillingAddress().getPostalCd());
					String canadaPostCode = portRequestType.getBillingAddress().getProvinceCd();
					String province = getProvince(canadaPostCode, portRequestType.getBillingAddress().getCountryName(), provinces);
					portRequestAddress.setProvince(province);
					portRequestAddress.setStreetDirection(portRequestType.getBillingAddress().getStreetDirectionTxt());
					portRequestAddress.setStreetName(portRequestType.getBillingAddress().getStreetName());
					portRequestAddress.setStreetNumber(portRequestType.getBillingAddress().getStreetNum());
					pri.setPortRequestAddress(portRequestAddress);
					pri.setPortRequestId(portRequestType.getRequestId());
					PortRequestNameInfo portRequestName = new PortRequestNameInfo();
					portRequestName.setFirstName(portRequestType.getBillingAddress().getFirstName());
					portRequestName.setGeneration(portRequestType.getBillingAddress().getSuffixTxt());
					portRequestName.setLastName(portRequestType.getBillingAddress().getLastName());
					portRequestName.setMiddleInitial(portRequestType.getBillingAddress().getMiddleInitialTxt());
					portRequestName.setTitle(portRequestType.getBillingAddress().getPrefixTxt());
					pri.setPortRequestName(portRequestName);
					pri.setRemarks(portRequestType.getRemarksTxt());
					String status = portRequestType.getStatusCd();
					pri.setStatusCode(status);
					pri.setStatusCategory(getStatusCategory(status));
					pri.setStatusReasonCode("");
					pri.setType(portRequestType.getIcpRequestTypeCd());
					pri.setIncomingBrandId(portRequestType.getTargetBrandId() != null ? Integer.parseInt(portRequestType.getTargetBrandId()) : Brand.BRAND_ID_NOT_APPLICABLE);
					pri.setOutgoingBrandId(portRequestType.getSourceBrandId() != null ? Integer.parseInt(portRequestType.getSourceBrandId()) : Brand.BRAND_ID_NOT_APPLICABLE);

					pri.setExpedite(portRequestType.isExpediteInd() ? "Y" : "N");
					pri.setDslInd(portRequestType.getDigitalSubscriberLineCd());
					if (portRequestType.getPlatformCd() != null) {
						pri.setPlatformId(Integer.valueOf(portRequestType.getPlatformCd()));
					}
					Integer dslLineNumber = null;
					try {
						dslLineNumber = Integer.getInteger(portRequestType.getDigitalSubscriberLineNumber());
					} catch (Exception e) {
					}
					pri.setDslLineNumber(dslLineNumber);
					pri.setEndUserMovingInd(portRequestType.isEndUserMovingInd());
					pri.setOldReseller(portRequestType.getOldResellerName());
					
					portRequests.add(pri);
				}
				
				return portRequests;
			}
		}, "0003", "SUBS-SVC", "PRIS", "WNP");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclefacade.dao.PortRequestInformationDao#getCurrentPortRequestsByPhoneNumber(java.lang.String, int, com.telus.api.reference.Province[])
	 */
	@Override
	public Collection<PortRequestInfo> getCurrentPortRequestsByPhoneNumber(final String phoneNumber, final int brandId, final Province[] provinces) throws ApplicationException {
		
		return execute( new ResourceExecutionCallback<Collection<PortRequestInfo>>() {
			
			@Override
			public Collection<PortRequestInfo> doInCallback() throws Exception {
				List<CompletePortRequestDataType> vector = new Vector<CompletePortRequestDataType>();

				RetrieveCurrentPortRequestDataType requestData = new RetrieveCurrentPortRequestDataType();

				String npa = phoneNumber.substring(0, 3);
				String nxx = phoneNumber.substring(3, 6);
				String range = phoneNumber.substring(6, 10);
				requestData.setInputDataValueTxt(npa + "-" + nxx + "-" + range);
				requestData.setInputTypeId(3L);
				requestData.setBrandId(String.valueOf(brandId));

				GetCurrentPortRequests request = new GetCurrentPortRequests();
				request.setRetrieveCurrentPortRequestData(requestData);

				RetrievePortRequestCollection response = port.getCurrentPortRequests(request).getRetrievePortRequestCollection();

				List<CompletePortRequestDataType> allPrmPortRequests = response.getPortRequestList();

				for (CompletePortRequestDataType portRequest : allPrmPortRequests) {
					
					// never retrieve snap-back port requests - these will cause
					// null pointer exceptions during mapping!
					
					if (!portRequest.getIcpRequestTypeCd().equals(PortRequestSummary.TYPE_PORT_SNAP_BACK)) {
						vector.add(portRequest);
					}
				}

				List<PortRequestInfo> portRequests = new ArrayList<PortRequestInfo>();

				for (CompletePortRequestDataType portRequestType : vector) {

					PortRequestInfo pri = new PortRequestInfo();

					pri.setAgencyAuthorizationDate(portRequestType.getAgentAuthorizedDate());
					pri.setAgencyAuthorizationIndicator(portRequestType.isAgentAuthorizedInd() ? "Y" : "N");
					pri.setAgencyAuthorizationName(portRequestType.getAgentAuthorizedName());
					pri.setAlternateContactNumber(portRequestType.getAlternativeContactTelephoneNumber());
					pri.setAutoActivate(portRequestType.getAutoActivateCd().equals("Y"));
					pri.setBanId(portRequestType.getBillingAccountInfo().getBillingAccountId() != null ? Integer.parseInt(portRequestType.getBillingAccountInfo().getBillingAccountId()) : 0);
					pri.setBusinessName(portRequestType.getBillingAddress().getBusinessName());
					pri.setCanBeActivate(portRequestType.isCanBeActivatedInd());
					pri.setCanBeCancel(portRequestType.isCanBeCancelledInd());
					pri.setCanBeModify(portRequestType.isCanBeModifiedInd());
					pri.setCanBeSubmit(portRequestType.isCanBeSubmittedInd());

					String dateSent = portRequestType.getSentDate();
					String year1 = dateSent.substring(4, 8);
					String month1 = dateSent.substring(0, 2);
					String day1 = dateSent.substring(2, 4);
					pri.setCreationDate(java.sql.Date.valueOf(year1 + "-" + month1 + "-" + day1));

					if (portRequestType.getDesiredDueDate() != null && !portRequestType.getDesiredDueDate().trim().equals("")) {
						String desiredDateTime = portRequestType.getDesiredDueDate().substring(0, 8);
						String year = desiredDateTime.substring(4, 8);
						String month = desiredDateTime.substring(0, 2);
						String day = desiredDateTime.substring(2, 4);
						java.sql.Date strToDate = java.sql.Date.valueOf(year + "-" + month + "-" + day);
						pri.setDesiredDateTime(strToDate);
					}
					pri.setOSPAccountNumber(portRequestType.getAccountInfo().getAccountNumber());
					pri.setOSPPin(portRequestType.getAccountInfo().getPin());
					pri.setOSPSerialNumber(portRequestType.getAccountInfo().getEsn());
					pri.setPhoneNumber(phoneNumber);
					pri.setPortDirectionIndicator(portRequestType.getNumberPortabilityDirectionCd());
					PortRequestAddressInfo portRequestAddress = new PortRequestAddressInfo();
					portRequestAddress.setCity(portRequestType.getBillingAddress().getCityName());
					portRequestAddress.setCountry(portRequestType.getBillingAddress().getCountryName());
					portRequestAddress.setPostalCode(portRequestType.getBillingAddress().getPostalCd());
					String canadaPostCode = portRequestType.getBillingAddress().getProvinceCd();
					String province = getProvince(canadaPostCode, portRequestType.getBillingAddress().getCountryName(), provinces);
					portRequestAddress.setProvince(province);
					portRequestAddress.setStreetDirection(portRequestType.getBillingAddress().getStreetDirectionTxt());
					portRequestAddress.setStreetName(portRequestType.getBillingAddress().getStreetName());
					portRequestAddress.setStreetNumber(portRequestType.getBillingAddress().getStreetNum());
					pri.setPortRequestAddress(portRequestAddress);
					pri.setPortRequestId(portRequestType.getRequestId());
					PortRequestNameInfo portRequestName = new PortRequestNameInfo();
					portRequestName.setFirstName(portRequestType.getBillingAddress().getFirstName());
					portRequestName.setGeneration(portRequestType.getBillingAddress().getSuffixTxt());
					portRequestName.setLastName(portRequestType.getBillingAddress().getLastName());
					portRequestName.setMiddleInitial(portRequestType.getBillingAddress().getMiddleInitialTxt());
					portRequestName.setTitle(portRequestType.getBillingAddress().getPrefixTxt());
					pri.setPortRequestName(portRequestName);
					pri.setRemarks(portRequestType.getRemarksTxt());
					String status = portRequestType.getStatusCd();
					pri.setStatusCode(status);
					pri.setStatusCategory(getStatusCategory(status));
					pri.setStatusReasonCode("");
					pri.setType(portRequestType.getIcpRequestTypeCd());
					pri.setIncomingBrandId(portRequestType.getTargetBrandId() != null ? Integer.parseInt(portRequestType.getTargetBrandId()) : Brand.BRAND_ID_NOT_APPLICABLE);
					pri.setOutgoingBrandId(portRequestType.getSourceBrandId() != null ? Integer.parseInt(portRequestType.getSourceBrandId()) : Brand.BRAND_ID_NOT_APPLICABLE);

					pri.setExpedite(portRequestType.isExpediteInd() ? "Y" : "N");
					pri.setDslInd(portRequestType.getDigitalSubscriberLineCd());
					if (portRequestType.getPlatformCd() != null) {
						pri.setPlatformId(Integer.parseInt(portRequestType.getPlatformCd()));
					}
					Integer dslLineNumber = null;
					try {
						dslLineNumber = Integer.getInteger(portRequestType.getDigitalSubscriberLineNumber());
					} catch (Exception e) {
					}
					pri.setDslLineNumber(dslLineNumber);
					pri.setEndUserMovingInd(portRequestType.isEndUserMovingInd());
					pri.setOldReseller(portRequestType.getOldResellerName());

					portRequests.add(pri);
				}
				
				return portRequests;			
			}
		}, "0004", "SUBS-SVC", "PRIS", "WNP");
	}

	private String getStatusCategory(String statusCode) {
		String statuscategory = "";
		if (statusCode.equals("ST1"))
			statuscategory = PortRequestSummary.STATUS_CATEGORY_COMPLETED;
		else if (statusCode.equals("ST2") || statusCode.equals("ST3") || statusCode.equals("ST4") || statusCode.equals("ST8"))
			statuscategory = PortRequestSummary.STATUS_CATEGORY_IN_PROGRESS;
		else if (statusCode.equals("ST5") || statusCode.equals("ST6") || statusCode.equals("ST7"))
			statuscategory = PortRequestSummary.STATUS_CATEGORY_NOT_EXIST;
		else if (statusCode.equals("ST9"))
			statuscategory = PortRequestSummary.STATUS_CATEGORY_CANCELLED;
		return statuscategory;
	}

	private String getCanadaPostCode(String province, String country, Province[] provinces) {
		if (country == null || !country.equalsIgnoreCase("CAN"))
			return province;
		String canadaPostCode = "";
		for (int i = 0; i < provinces.length; i++) {
			if (provinces[i].getCode().equals(province) || provinces[i].getCanadaPostCode().equals(province)) {
				canadaPostCode = provinces[i].getCanadaPostCode();
				break;
			}
		}
		return canadaPostCode;
	}
	
	private String getProvince(String canadaPostCode, String country, Province[] provinces) {
		if (country == null || !country.equalsIgnoreCase("CAN")) {
			return canadaPostCode;
		}
		String province = "";
		for (int i = 0; i < provinces.length; i++) {
			if (provinces[i].getCanadaPostCode().equals(canadaPostCode) || provinces[i].getCode().equals(canadaPostCode)) {
				province = provinces[i].getCode();
				break;
			}
		}
		return province;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclefacade.dao.PortRequestInformationDao#getReferenceData(java.lang.String)
	 */
	@Override
	public Collection<PRMReferenceData> getReferenceData(final String category) throws ApplicationException {
		
		return execute( new ResourceExecutionCallback<Collection<PRMReferenceData>>() {
		
			@Override
			public Collection<PRMReferenceData> doInCallback() throws Exception {
				List<PRMReferenceData> result = new ArrayList<PRMReferenceData>();
				
				ReferenceDefinitionDataType referenceDefinitionDataType = new ReferenceDefinitionDataType();
				referenceDefinitionDataType.setCategoryId(category);
				
				GetReferenceData request = new GetReferenceData();
				request.setReferenceDefinitionData(referenceDefinitionDataType);
				
				GetReferenceDataResponse response = port.getPortReferenceData(request);
				
				ReferenceDefinitionList definitionList = response.getReferenceDefinitionList();
				if (definitionList != null) {
					for (ReferenceDefinitionType reference : definitionList.getReferenceDefinition()) {
						
						MultilingualCodeDescriptionList referenceData = reference.getReferenceData();
						
						PRMReferenceDataInfo prmReferenceDataInfo = new PRMReferenceDataInfo();
						prmReferenceDataInfo.setCategory(category);
						prmReferenceDataInfo.setCode((String) referenceData.getCode()); 
						prmReferenceDataInfo.setDescription(getDescriptionText(referenceData.getDescription(), "en"));
						prmReferenceDataInfo.setDescriptionFrench(getDescriptionText(referenceData.getDescription(), "fr"));

						result.add(prmReferenceDataInfo);
					}
				}
				
				return result;			
			}
		}, "0005", "SUBS-SVC", "PRIS", "WNP");
	}
	
	private String getDescriptionText(List<Description> descriptions, String locale) {
		for (Description description : descriptions) {
			if (description != null && StringUtils.equalsIgnoreCase(description.getLocale(), locale)) {
				return description.getDescriptionText();
			}
		}
		return null;
	}

}
