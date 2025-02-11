package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.account.Account;
import com.telus.api.account.BasePrepaidAccount;
import com.telus.api.account.PostpaidBusinessRegularAccount;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.api.account.Subscriber;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.Province;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.cmb.framework.resource.ResourceExecutionCallback;
import com.telus.cmb.framework.resource.ResourceInvocationCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.CreatePortInRequestDao;
import com.telus.cmb.subscriber.utilities.PortRequestFieldTrimmer;
import com.telus.cmb.wsclient.wlnp.crtpirs.CreatePortInRequest;
import com.telus.cmb.wsclient.wlnp.crtpirs.CreatePortInRequestPort;
import com.telus.cmb.wsclient.wlnp.crtpirs.PortInRequestData;
import com.telus.cmb.wsclient.wlnp.crtpirs.PortInRequestDataBodyType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesAGAUTHType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesAccountInfoType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesBANInfoType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesBillingAddressType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesHeaderType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesPortTNType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesProductBillingMethodType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesSubscriberInfoType;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public class CreatePortInRequestDaoImpl extends WnpLegacyClient implements CreatePortInRequestDao {

	private final Logger logger = Logger.getLogger(CreatePortInRequestDaoImpl.class);

	private static final String DEFAULT_LANAGUAGE = "E";
	private static final String PRM_TRUE = "Y";
	private static final String PRM_FALSE = "N";
	private static final String PRM_DATE_FORMAT = "MMddyyyyhhmmss";

	@Autowired
	public CreatePortInRequestPort port;

	@Override
	public String createPortInRequest(final SubscriberInfo subscriber, final String portProcessType, final int incomingBrandId, final int outgoingBrandId,
			final String sourceNetwork, final String targetNetwork, final String applicationId, final String user, final Province[] provinces, final Brand[] brands,
			final PortRequestInfo portReq) throws ApplicationException {

		return execute( new ResourceExecutionCallback<String>() {
			
			@Override
			public String doInCallback() throws Exception {
				PortInRequestData requestData = new PortInRequestData();

				PortRequest portRequest = portReq;
				if (portRequest == null) {
					logger.error ("portReq is null");
					if (subscriber != null) {
						logger.error("Subscriber=["+subscriber.getSubscriberId()+"]");
					}else {
						logger.error("no info about subscriber");
					}
					return null;
				}
					

				// prepare request header
				TypesHeaderType requestDataHeader = new TypesHeaderType();
				String appId = applicationId;
				if (appId.length() > 6)
					appId = appId.substring(0, 6);
				requestDataHeader.setOriginator(appId);

				requestDataHeader.setDestination("SMG");
				Calendar toDay = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat(PRM_DATE_FORMAT);
				String timeStamp = formatter.format(toDay.getTime());
				requestDataHeader.setTimestamp(timeStamp);

				// set request header
				requestData.setHeader(requestDataHeader);

				// prepare request data body

				PortInRequestDataBodyType requestDataBody = new PortInRequestDataBodyType();

				// set account info
				TypesAccountInfoType acctInfo = new TypesAccountInfoType();
				if (portRequest.getOSPAccountNumber() != null)
					acctInfo.setAcctNumber(portRequest.getOSPAccountNumber());
				if (portRequest.getOSPSerialNumber() != null)
					acctInfo.setESN(portRequest.getOSPSerialNumber());
				if (portRequest.getOSPPin() != null)
					acctInfo.setPIN(portRequest.getOSPPin());
				requestDataBody.setAcctInfo(acctInfo);

				// set authorization info
				requestDataBody.setAgencyAuth(TypesAGAUTHType.fromValue(portRequest.getAuthorizationIndicator()));
				Calendar agencyAuthDate = Calendar.getInstance();
				agencyAuthDate.setTime(portRequest.getAgencyAuthorizationDate());
				requestDataBody.setAgencyAuthDate(agencyAuthDate.getTime());

				// enforce the Authorization name not to exceed 60
				portRequest.setAgencyAuthorizationName(PortRequestFieldTrimmer.trucate(portRequest.getAgencyAuthorizationName(), PortRequestFieldTrimmer.AGENCY_NAME_LEN));
				requestDataBody.setAgencyAuthName(portRequest.getAgencyAuthorizationName());

				// set auto activate
				requestDataBody.setAutoActivate(portRequest.isAutoActivate() ? PRM_TRUE : PRM_FALSE);

				// set billing address
				TypesBillingAddressType billAddress = new TypesBillingAddressType();
				if (portRequest.getPortRequestName().getFirstName() != null)
					billAddress.setBillFirstName(portRequest.getPortRequestName().getFirstName());
				if (portRequest.getPortRequestName().getMiddleInitial() != null && !portRequest.getPortRequestName().getMiddleInitial().trim().equals(""))
					billAddress.setBillMiddleInit(portRequest.getPortRequestName().getMiddleInitial());
				if (portRequest.getPortRequestName().getLastName() != null)
					billAddress.setBillLastName(portRequest.getPortRequestName().getLastName());
				if (portRequest.getPortRequestName().getTitle() != null)
					billAddress.setBillPrefix(portRequest.getPortRequestName().getTitle());
				if (portRequest.getPortRequestAddress().getStreetDirection() != null)
					billAddress.setBillStDir(portRequest.getPortRequestAddress().getStreetDirection());
				if (portRequest.getPortRequestAddress().getStreetName() != null)
					billAddress.setBillStName(portRequest.getPortRequestAddress().getStreetName());
				if (portRequest.getPortRequestAddress().getStreetNumber() != null && !portRequest.getPortRequestAddress().getStreetNumber().trim().equals(""))
					billAddress.setBillStNum(portRequest.getPortRequestAddress().getStreetNumber());
				if (portRequest.getPortRequestName().getGeneration() != null)
					billAddress.setBillSuffix(portRequest.getPortRequestName().getGeneration());
				if (portRequest.getBusinessName() != null)
					billAddress.setBusName(portRequest.getBusinessName());
				if (portRequest.getPortRequestAddress().getCity() != null)
					billAddress.setCity(portRequest.getPortRequestAddress().getCity());
				if (portRequest.getPortRequestAddress().getCountry() != null)
					billAddress.setCountry(portRequest.getPortRequestAddress().getCountry());
				if (portRequest.getPortRequestAddress().getPostalCode() != null)
					billAddress.setPostalCode(portRequest.getPortRequestAddress().getPostalCode());
				String province = portRequest.getPortRequestAddress().getProvince();
				String canadaPostCode = getCanadaPostCode(province, portRequest.getPortRequestAddress().getCountry(), provinces);
				billAddress.setProvince(canadaPostCode);
				requestDataBody.setBillingAddress(billAddress);

				// set desired due date/time
				if (portRequest.getDesiredDateTime() != null) {
					Calendar desiredDueDateTime = Calendar.getInstance();
					desiredDueDateTime.setTime(portRequest.getDesiredDateTime());
					requestDataBody.setDesiredDueDateTime(desiredDueDateTime.getTime());
				}

				requestDataBody.setInitiatorRepresentative(user);
				TypesPortTNType phNumberType = new TypesPortTNType();
				String phoneNumber = portRequest.getPhoneNumber();
				String npa = phoneNumber.substring(0, 3);
				String nxx = phoneNumber.substring(3, 6);
				String range = phoneNumber.substring(6, 10);
				phNumberType.setPhoneNumber(npa + "-" + nxx + "-" + range);
				requestDataBody.setPortTN(phNumberType);
				Account acc = portReq.getAccount();
				if (acc.isPostpaid())
					requestDataBody.setProductBillingMethodType(TypesProductBillingMethodType.POSTPAID);
				else if (acc.isPrepaidConsumer())
					requestDataBody.setProductBillingMethodType(TypesProductBillingMethodType.PREPAID);

				// mike <-> pcs migrations
				if (portProcessType.equals(PortInEligibility.PORT_PROCESS_MIGRATION)) {
					if (subscriber.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
						if (NetworkType.NETWORK_TYPE_HSPA.equals(sourceNetwork)) {
							requestDataBody.setProductType("P2M_H2I");
						} else {
							requestDataBody.setProductType("P2M_C2I");
						}
					} else if (subscriber.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
						if (NetworkType.NETWORK_TYPE_HSPA.equals(targetNetwork)) {
							requestDataBody.setProductType("M2P_I2H");
						} else {
							requestDataBody.setProductType("M2P_I2C");
						}
					}
					// inter-brand ports
				} else if (portProcessType.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
					if (ReferenceDataManager.Helper.validateBrandId(incomingBrandId, brands) && ReferenceDataManager.Helper.validateBrandId(outgoingBrandId, brands)
							&& (outgoingBrandId != incomingBrandId)) {
						if (subscriber.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
							requestDataBody.setProductType("INTR_M2M");
						} else if (subscriber.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
							requestDataBody.setProductType("INTR_P2P");
						}
					} else {
						throw new PortRequestException("Invalid inter-brand port request: incoming brand ID [" + incomingBrandId + "] matches outgoing brand ID ["
								+ outgoingBrandId + "]");
					}
				} else if (portProcessType.equals(PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT)) {
					requestDataBody.setProductType("INTR_R2T");
				} else { // inter-carrier ports are the default
					if (subscriber.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
						requestDataBody.setProductType("EXT_2I");
					else if (subscriber.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
						if (portReq.getEquipment().isHSPA()) {
							requestDataBody.setProductType("EXT_2H");
						} else {
							requestDataBody.setProductType("EXT_2C");
						}
					}
				}

				if (portRequest.getRemarks() != null && !portRequest.getRemarks().trim().equals(""))
					requestDataBody.setRemarks(portRequest.getRemarks());

				// set BAN info
				TypesBANInfoType banInfo = new TypesBANInfoType();
				banInfo.setBANId(String.valueOf(acc.getBanId()));
				banInfo.setBANSubType(String.valueOf(acc.getAccountSubType()));
				banInfo.setBANType(String.valueOf(acc.getAccountType()));
				if (ReferenceDataManager.Helper.validateBrandId(outgoingBrandId, brands))
					banInfo.setCurrentBrand(String.valueOf(outgoingBrandId));
				if (ReferenceDataManager.Helper.validateBrandId(incomingBrandId, brands))
					banInfo.setTargetBrand(String.valueOf(incomingBrandId));
				if (acc instanceof PostpaidConsumerAccount || acc instanceof BasePrepaidAccount) {
					banInfo.setFirstName(portRequest.getPortRequestName().getFirstName());
					banInfo.setLastName(portRequest.getPortRequestName().getLastName());
				} else if (acc instanceof PostpaidBusinessRegularAccount) {
					String businessName = ((PostpaidBusinessRegularAccount) acc).getLegalBusinessName();
					banInfo.setBusinessName(businessName);
				}
				banInfo.setPostalCode(acc.getAddress().getPostalCode());
				canadaPostCode = getCanadaPostCode(acc.getAddress().getProvince(), acc.getAddress().getCountry(), provinces);
				banInfo.setProvince(canadaPostCode);
				requestDataBody.setTelusBAN(banInfo);

				// set subscriber info

				TypesSubscriberInfoType subscriberInfoType = new TypesSubscriberInfoType();
				if (subscriber.getLanguage() != null && subscriber.getLanguage().trim().length() > 0) {
					String prefLang = subscriber.getLanguage().substring(0, 1);
					if (prefLang.equals("M"))
						prefLang = "MA";
					if (prefLang.equals("C"))
						prefLang = "CA";
					subscriberInfoType.setLanguagePref(prefLang);
				} else {
					subscriberInfoType.setLanguagePref(DEFAULT_LANAGUAGE);
				}
				subscriberInfoType.setPostalCode(acc.getAddress().getPostalCode());
				canadaPostCode = getCanadaPostCode(acc.getAddress().getProvince(), acc.getAddress().getCountry(), provinces);
				subscriberInfoType.setProvince(canadaPostCode);
				String alternateContactNumber = portRequest.getAlternateContactNumber();
				if (alternateContactNumber != null && !alternateContactNumber.equals("")) {
					if (alternateContactNumber.length() == 10) {
						String npa1 = alternateContactNumber.substring(0, 3);
						String nxx1 = alternateContactNumber.substring(3, 6);
						String range1 = alternateContactNumber.substring(6, 10);
						subscriberInfoType.setAlternateContactNumber(npa1 + "-" + nxx1 + "-" + range1);
					} else if (alternateContactNumber.length() == 12 && alternateContactNumber.substring(3, 4).equals("-") && alternateContactNumber.substring(7, 8).equals("-")) {
						subscriberInfoType.setAlternateContactNumber(alternateContactNumber);
					}
				}
				requestDataBody.setTelusSubscriber(subscriberInfoType);

				if (portRequest.getExpedite() != null) {
					requestDataBody.setExpedited(portRequest.getExpedite());
				}

				if (PortInEligibility.PORT_DIRECTION_INDICATOR_WIRELINE_WIRELESS.equalsIgnoreCase(portRequest.getPortDirectionIndicator())) {
					// the following three fields only apply to for
					// intermodal port. (wireline to wireless)
					if (portRequest.getDslInd() != null)
						requestDataBody.setDSL(portRequest.getDslInd());
					if (portRequest.getDslLineNumber() != null) {
						requestDataBody.setDSLLNUM(portRequest.getDslLineNumber().intValue());
					}
					if (portRequest.getEndUserMovingInd())
						requestDataBody.setEUMI("Y");
					else
						requestDataBody.setEUMI("N");
				}

				if (portRequest.getOldReseller() != null && portRequest.getOldReseller().trim().length() > 0)
					requestDataBody.setOldResellerName(portRequest.getOldReseller());

				requestData.setHeader(requestDataHeader);
				requestData.setPortInRequestBody(requestDataBody);
				boolean isTruncate = portProcessType.equals(PortInEligibility.PORT_PROCESS_MIGRATION);
				PortRequestFieldTrimmer.newInstance(isTruncate).trim(requestData);
				if (!isTruncate) {
				    requestData.getPortInRequestBody().getBillingAddress().setBillMiddleInit( 
				        PortRequestFieldTrimmer.trucate( requestData.getPortInRequestBody().getBillingAddress().getBillMiddleInit(),
				                                         PortRequestFieldTrimmer.BILL_MIDDLE_INIT_LEN )
				        );
				}
				
				logger.debug("request: " + requestData);

				CreatePortInRequest portInRequest = new CreatePortInRequest();
				portInRequest.setPortInRequestData(requestData);

				String portInRequestResult =  port.createPortInRequest(portInRequest).getCreatePortInRequestResult();
				logger.debug ("portInRequestResult=["+portInRequestResult+"]");
				
				return portInRequestResult;
			}
		}, "0001", "SUBS-SVC", "PRIS", "CRT-PIRS");
	}
	
	private String getCanadaPostCode(String province, String country, Province[] provinces) throws ApplicationException {
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

	public String ping() throws ApplicationException {

		invoke( new ResourceInvocationCallback() {
			
			@Override
			public void doInCallback() throws Exception {
				CreatePortInRequest portInRequest = new CreatePortInRequest();
				portInRequest.setPortInRequestData( new PortInRequestData());
				port.createPortInRequest(portInRequest).getCreatePortInRequestResult();	
			}
		}, "0002", "SUBS-SVC", "PRIS", "CRT-PIRS");
		
		return "OK";
	}

}
