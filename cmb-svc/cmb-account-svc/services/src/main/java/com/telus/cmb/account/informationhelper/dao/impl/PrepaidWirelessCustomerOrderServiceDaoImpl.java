package com.telus.cmb.account.informationhelper.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.cmb.account.informationhelper.dao.PrepaidWirelessCustomerOrderServiceDao;
import com.telus.cmb.common.prepaid.PrepaidUtils;
import com.telus.cmb.common.prepaid.PrepaidWirelessCustomerOrderServiceClient;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.common.util.DateUtil;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.Create;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.CreateResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.GetCreditList;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.GetCreditListResponse;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.common_domain_types_3.CardNumberDisplay;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.common_domain_types_3.PaymentInstrumentType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.prepaidwirelessordertypes_v1.Credit;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.prepaidwirelessordertypes_v1.CustomerAccountProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.prepaidwirelessordertypes_v1.OrderDetail;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.prepaidwirelessordertypes_v1.PrepaidWirelessCustomerOrder;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.prepaidwirelessordertypes_v1.RechargePayment;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.prepaidwirelessordertypes_v1.SubscriberEquipment;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.prepaidwirelessordertypes_v1.SubscriptionProfileInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.prepaidwirelessordertypes_v1.Voucher;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourcetypes_v1.CDMAEquipment;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourcetypes_v1.SIMCard;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourcetypes_v1.SIMEquipment;

@Component
public class PrepaidWirelessCustomerOrderServiceDaoImpl implements PrepaidWirelessCustomerOrderServiceDao {
	
	private static final Logger LOGGER = Logger.getLogger(PrepaidWirelessCustomerOrderServiceDaoImpl.class);
	
	@Autowired
	private PrepaidWirelessCustomerOrderServiceClient pwcosWSClient;
	
	@Override
	public String validatePayAndTalkSubscriberActivation(String applicationId,
			String userId,
			PrepaidConsumerAccountInfo prepaidConsumerAccountInfo,
			AuditHeader auditHeader) throws ApplicationException {
		/*
		 * validateSubscriberActivation -> PrepaidWirelessCustomerOrderService.create
		 * accountID/BAN -> CustomerAccountProfile/customerID
		 * ActivationCode - VoucherList.VoucherPIN (first item in VoucherList as CAPI can provide the VoucherList.type)
		 * 				  - VoucherList.type could be one of the following (optional in Prepaid WS, CAPI do not need to provide such information):
		 * 					Activation Code= ACT; 
		 * 					Airtime Card= AIR; 
		 * 					Credit Card= CC; 
		 * 					Airtime and Activation Card= AAC; 
		 * 					ACTIVATION CREDIT= ACR
		 * CreditCard - rechargePaymentList.paymentInstrument while PaymentInstrumentType="CC"
		 * 		CreditCard.Status (was CardStatus.CARD_STATUS_OTHER) - N/A
		 * 		CreditCard.Type (was ccInfo.getType()) - N/A
		 * 		CreditCard.Token - paymentInstrument/cardNumber
		 * 		CreditCard.First6Digits - paymentInstrument/cardNumberDisplay/First6Digits
		 * 		CreditCard.Last4Digits - paymentInstrument/cardNumberDisplay/Last4Digits
		 * 		CreditCard.ExpiryDate - paymentInstrument/expiryMonth and paymentInstrument/expiryYear.
		 * 		CreditCard.CardHolder(was ccInfo.getHolderName()) - N/A
		 * ActivationCreditAmount - CreditList/amount (first item in CreditList) while creditType="ACR"
		 * SerialNumber - subscriberEquipment/simCard/serialNumber
		 * AssociatedHandsetIMEI - SubscriberEquipment/simEquipment/serialNumber
		 * subscriberGeoCode - subscriptionProfile/subscriptionProvinceCode 
		 * Pin - subscriptionProfile/subscriptionPIN
		 * ActivationType - orderDetail/orderType
		 * invocationType - orderDetail/orderInvocationType
		 * Source - orderDetail/orderSource
		 * UserId - orderDetail/orderAgent
		 * AvalonHeader - SOAPHeader/UserHeader
		 * 		AvalonHeader.CustomerId - UserHeader/custId 
		 * 		AvalonHeader.UserIpAddress - UserHeader/ipAddress
		 */
		
		String orderId = "";
		String ban = "";
		if (prepaidConsumerAccountInfo != null) {
			
			String subscriberGeoCode = "";
			String invocationType = PrepaidUtils.INVOCATION_TYPE_SOFT_VALIDATION;
			OriginatingUserType originatingUserType = PrepaidUtils.createOriginatingUserTypeObj();
			String cardNumber = "";
			CardNumberDisplay cardNumberDisplay = null;
			String expiryMonth = "";
			String expiryYear = "";
			
			// 'SOFT_VALIDATION': if ban has not been created yet
			// 'HARD_VALIDATION': if ban has been created
			boolean banHasBeenCreated = prepaidConsumerAccountInfo.getBanId() > 0;
			if (banHasBeenCreated) {
				ban = String.valueOf(prepaidConsumerAccountInfo.getBanId());
				CreditCardInfo ccInfo = prepaidConsumerAccountInfo.getActivationCreditCard0();
				if (ccInfo.hasToken()) {
					cardNumber = ccInfo.getToken();
					cardNumberDisplay = PrepaidUtils.createCardNumberDisplayObj(ccInfo.getLeadingDisplayDigits(), ccInfo.getTrailingDisplayDigits(), null);
				} else {
					if (LOGGER.isDebugEnabled()) 
						LOGGER.debug("CreditCardInfo Token is not available for BAN " + ban);
				}
				Date expiryDate = ccInfo.getExpiryDate();
				if (expiryDate != null) {
					expiryMonth = DateUtil.getTwoDigitMonth(expiryDate);
					expiryYear = DateUtil.getLastTwoDigitYear(expiryDate);
				} else {
					if (LOGGER.isDebugEnabled()) 
						LOGGER.debug("CreditCardInfo Expiry Date is not available for BAN " + ban);
				}
				subscriberGeoCode = AttributeTranslator.emptyFromNull(prepaidConsumerAccountInfo.getAddress0().getProvince());
				if (auditHeader != null) {
					originatingUserType = PrepaidUtils.createOriginatingUserTypeObj(auditHeader.getCustomerId(), auditHeader.getUserIPAddress());
				} else {
					if (LOGGER.isDebugEnabled()) 
						LOGGER.debug("AuditHeaer is null for BAN " + ban);
				}
				invocationType = PrepaidUtils.INVOCATION_TYPE_HARD_VALIDATION;
			}
			
			Create parameters = new Create();
			PrepaidWirelessCustomerOrder order = new PrepaidWirelessCustomerOrder();

			//CustomerAccountProfile
			order.setCustomerAccountProfile(createCustomerAccountProfileObj(ban));
			
			//VoucherList
			String activationCode = StringUtils.isNotBlank(prepaidConsumerAccountInfo.getActivationCode())? prepaidConsumerAccountInfo.getActivationCode():""; 
			order.getVoucherList().add(createVoucherObj(activationCode));
			
			//RechargePaymentList
			RechargePayment rechargePayment = createRechargePaymentObj();
			rechargePayment.setPaymentInstrument(PrepaidUtils.createPaymentInstrumentObj(PaymentInstrumentType.CC, null, cardNumber, cardNumberDisplay, expiryMonth, expiryYear));
			order.getRechargePaymentList().add(rechargePayment);
			
			//CreditList
			order.getCrediList().add(createCreidtObj(prepaidConsumerAccountInfo.getActivationCreditAmount(), PrepaidUtils.CREDIT_TYPE_ACR));
			
			//SubscriberEquipment
			SIMCard simCard = createSIMCardBySerialNumberObj(prepaidConsumerAccountInfo.getSerialNumber());
			SIMEquipment simEquipment = createSIMEquipmentObj(prepaidConsumerAccountInfo.getAssociatedHandsetIMEI());
			SubscriberEquipment subscriberEquipment = createSubscriberEquipmentObj(simCard, simEquipment, null);
			order.setSubscriberEquipment(subscriberEquipment);
			
			//SubscriptionProfile
			String pin = AttributeTranslator.emptyFromNull(prepaidConsumerAccountInfo.getPin());
			order.setSubscriptionProfile(createSubscriptionProfileInfoObj(subscriberGeoCode, pin));
			
			//OrderDetail
			String source = PrepaidUtils.getSourceFromAppId(applicationId);
			order.setOrderDetail(createOrderDetailObj(source, userId, ""+prepaidConsumerAccountInfo.getActivationType(), invocationType, null));
			
			//Log request
			logValidatePayAndTalkSubscriberActivationReq(ban, 
					expiryMonth,
					expiryYear,
					activationCode,
					cardNumber,
					cardNumberDisplay,
					prepaidConsumerAccountInfo,
					subscriberGeoCode,
					source,
					userId,
					invocationType,
					auditHeader);
			
			parameters.setPrepaidWirelessCustomerOrder(order);
			CreateResponse response = pwcosWSClient.create(parameters, originatingUserType);
			orderId = response.getOrderID();
			
		} else {
			if (LOGGER.isInfoEnabled()) 
				LOGGER.info("ValidatePayAndTalkSubscriberActivation prepaidConsumerAccountInfo is null.");
		}
		
		//Log response
		logValidatePayAndTalkSubscriberActivationRes(orderId, ban);
		
		return orderId;
		
	}
	
	private void logValidatePayAndTalkSubscriberActivationReq(String ban, 
			String expiryMonth,
			String expiryYear,
			String activationCode,
			String cardNumber,
			CardNumberDisplay cardNumberDisplay,
			PrepaidConsumerAccountInfo prepaidConsumerAccountInfo,
			String subscriberGeoCode,
			String source,
			String userId,
			String invocationType,
			AuditHeader auditHeader) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("ValidatePayAndTalkSubscriberActivation");
			sb.append(" | BAN:" + ban);
			sb.append(" | ExpiryMonth:" + expiryMonth);
			sb.append(" | ExpiryYear:" + expiryYear);
			sb.append(" | ActivationCode:" + activationCode);
			sb.append(" | CardNumber:" + cardNumber);
			if (cardNumberDisplay != null) sb.append(" | First6Digits:" + cardNumberDisplay.getFirst6Digits());
			if (cardNumberDisplay != null) sb.append(" | Last4Digits:" + cardNumberDisplay.getLast4Digits());
			sb.append(" | ActivationCreditAmount:" + prepaidConsumerAccountInfo.getActivationCreditAmount());
			sb.append(" | SerialNumber:" + prepaidConsumerAccountInfo.getSerialNumber());
			sb.append(" | IMEI:" + prepaidConsumerAccountInfo.getAssociatedHandsetIMEI());
			sb.append(" | SubscriberGeoCode:" + subscriberGeoCode);
			sb.append(" | Pin:" + prepaidConsumerAccountInfo.getPin());
			sb.append(" | Source:" + source);
			sb.append(" | UserId:" + userId);
			sb.append(" | ActivationType:" + prepaidConsumerAccountInfo.getActivationType());
			sb.append(" | InvocationType:" + invocationType);
			if (auditHeader!= null) sb.append(" | CustId:" + auditHeader.getCustomerId());
			if (auditHeader!= null) sb.append(" | IpAddress:" + auditHeader.getUserIPAddress());
			LOGGER.info(sb.toString());
		}
		
	}
	
	private void logValidatePayAndTalkSubscriberActivationRes(String orderId, String ban) {
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("ValidatePayAndTalkSubscriberActivation orderId: " + orderId + " for BAN: " + ban);
	}
	
	
	
	@Override
	public double getPrepaidActivationCredit(String applicationId,
			PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo) throws ApplicationException {
		/*
		 * getActivationCredit -> PrepaidWirelessCustomerOrderService.getCreditList
		 */
		double prepaidActivationCredit = 0;
		
		if (pPrepaidConsumerAccountInfo != null) {
			String activationSource = PrepaidUtils.getSourceFromAppId(applicationId);
			String customerID = "" + pPrepaidConsumerAccountInfo.getBanId();
			String subscriptionID = "";//phone number, it is optional and for future support only - April/2014 release Surepay Retirement
			String equipmentSerialNumber = pPrepaidConsumerAccountInfo.getSerialNumber();
			String activationType = "" + pPrepaidConsumerAccountInfo.getActivationType();
			String activationCode = AttributeTranslator.emptyFromNull(pPrepaidConsumerAccountInfo.getActivationCode());
			String imei = AttributeTranslator.emptyFromNull(pPrepaidConsumerAccountInfo.getAssociatedHandsetIMEI());
			
			//Log request
			logGetPrepaidActivationCreditReq(customerID,
					subscriptionID,
					equipmentSerialNumber,
					activationType,
					activationSource,
					activationCode,
					imei);
			
			prepaidActivationCredit = getPrepaidActivationCredit(customerID, 
					subscriptionID, 
					equipmentSerialNumber, 
					activationType, 
					activationSource, 
					activationCode, 
					imei);
			
			//Log response
			logGetPrepaidActivationCreditRes(prepaidActivationCredit, customerID);
			
		} else {
			LOGGER.info("PrepaidConsumerAccountInfo is null for GetPrepaidActivationCredit");
		}
		
		return prepaidActivationCredit;
	}
	
	private void logGetPrepaidActivationCreditReq(String customerID,
			String subscriptionID,
			String equipmentSerialNumber,
			String activationType,
			String activationSource,
			String activationCode,
			String imei) {
		if (LOGGER.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("GetPrepaidActivationCredit");
			sb.append(" | CustomerID: " + customerID);
			sb.append(" | SubscriptionID: " + subscriptionID);
			sb.append(" | EquipmentSerialNumber: " + equipmentSerialNumber);
			sb.append(" | ActivationType: " + activationType);
			sb.append(" | ActivationSource: " + activationSource);
			sb.append(" | ActivationCode: " + activationCode);
			sb.append(" | IMEI: " + imei);
			LOGGER.debug(sb.toString());
		}
	}

	private void logGetPrepaidActivationCreditRes(double prepaidActivationCredit, String customerID) {
		if (LOGGER.isDebugEnabled()) 
			LOGGER.debug("GetPrepaidActivationCredit for customerID " + customerID + ": " + prepaidActivationCredit);
	}
	
	
	@Override
	public double getPrepaidActivationCredit(String customerID, 
			String subscriptionID, 
			String equipmentSerialNumber, 
			String activationType, 
			String activationSource, 
			String activationCode, 
			String imei) throws ApplicationException {
		
		GetCreditList parameters = new GetCreditList();
		parameters.setActivationCode(activationCode);
		parameters.setActivationSource(activationSource);
		parameters.setActivationType(activationType);
		parameters.setCustomerID(customerID);
		parameters.setEquipmentSerialNumber(equipmentSerialNumber);
		parameters.setImei(imei);
		parameters.setSubscriptionID(subscriptionID);
		
		GetCreditListResponse response = pwcosWSClient.getCreditList(parameters); 
		List<Credit> credits = response.getCreditList(); 
		
		return getActivationCredit(credits);
	}
	
	private double getActivationCredit(List<Credit> credits) {
		double activationCredit = 0;
		if (credits != null && credits.size() > 0) {
			for (Credit credit:credits) {
				if (credit != null && PrepaidUtils.CREDIT_TYPE_ACR.equalsIgnoreCase(credit.getType())) {
					activationCredit = credit.getAmount();
				}
			}
		}
		return activationCredit;
	}

	
	
	@Override
	public TestPointResultInfo test() {
		return pwcosWSClient.test();
	}
	
	
	/*
	 * Helper Mapping methods 
	 */
	
	private RechargePayment createRechargePaymentObj() {
		RechargePayment result = new RechargePayment();
		return result;
	}
	
	private Voucher createVoucherObj(String voucherPIN) {
		return createVoucherObj(false, null, 0, voucherPIN);
	}
	
	private Voucher createVoucherObj(boolean linkedToOrderInd, String type, double amount, String voucherPIN) {
		Voucher result = new Voucher();
		result.setLinkedToOrderInd(linkedToOrderInd);
		if (StringUtils.isNotBlank(type))
			result.setType(type);
		if (amount != 0)
			result.setAmount(amount);
		if (StringUtils.isNotBlank(voucherPIN))
			result.setVoucherPIN(voucherPIN);
		return result;
	}
	
	private OrderDetail createOrderDetailObj(String orderSource, String orderAgent, String orderType, String orderInvocationType, String orderReasonID) {
		OrderDetail result = new OrderDetail();
		if (StringUtils.isNotEmpty(orderSource))
			result.setOrderSource(orderSource);
		if (StringUtils.isNotEmpty(orderAgent))
			result.setOrderAgent(orderAgent);
		if (StringUtils.isNotEmpty(orderType))
			result.setOrderType(orderType);
		if (StringUtils.isNotEmpty(orderInvocationType))
			result.setOrderInvocationType(orderInvocationType);
		if (StringUtils.isNotEmpty(orderReasonID))
			result.setOrderReasonID(orderReasonID);
		return result;
	}
	
	private Credit createCreidtObj(double amount, String type) {
		return createCreidtObj(false, type, amount, null, 0, null);
	}
	
	private Credit createCreidtObj(boolean likedToOrderInd, String type, double amount, String rateId, int expiryDaysAmount, String reasonCode) {
		Credit result = new Credit();
		result.setAmount(amount);
		result.setExpiryDaysAmount(expiryDaysAmount);
		if (StringUtils.isNotBlank(rateId))
			result.setRateId(rateId);
		if (StringUtils.isNotBlank(reasonCode))
			result.setReasonCode(reasonCode);
		if (StringUtils.isNotBlank(type))
			result.setType(type);
		
		return result;
	}
	
	private SubscriptionProfileInfo createSubscriptionProfileInfoObj(String provinceCode, String pin) {
		return createSubscriptionProfileInfoObj(null, provinceCode, pin, null);
	}
	
	private SubscriptionProfileInfo createSubscriptionProfileInfoObj(String Id, String provinceCode, String pin, String marketRateId) {
		SubscriptionProfileInfo result = new SubscriptionProfileInfo();
		result.setSubscriptionID(StringUtils.isNotBlank(Id)? Id:null);
		result.setSubscriptionProvinceCode(StringUtils.isNotBlank(provinceCode)? provinceCode:null);
		result.setSubscriptionPIN(StringUtils.isNotBlank(pin)? pin:null);
		result.setMarketRateID(StringUtils.isNotBlank(marketRateId)? marketRateId:null);
		return result;
	}
	
	private SIMEquipment createSIMEquipmentObj(String imei) throws ApplicationException {
		SIMEquipment result = new SIMEquipment();
		if (StringUtils.isNotBlank(imei)) {
			result.setSerialNumber(imei);
		}
		return result;
	}
	
	private SIMCard createSIMCardBySerialNumberObj(String serialNumber) {
		//set as serialNumber
		SIMCard result = createSIMCardObj(null, null, null, null, serialNumber, null, null, null, null);
		return result;
	}
	
	private SIMCard createSIMCardObj(String commonName, 
			String desc, 
			String objectId, 
			String version, 
			String serialNumber, 
			String versionNumber, 
			String type, 
			String imsi, 
			String msisdn) {
		//imsi and msisdn is a list, this api support only one item for now.
		SIMCard result = new SIMCard();
		result.setCommonName((StringUtils.isNotEmpty(commonName))? commonName:null);
		result.setDescription((StringUtils.isNotEmpty(desc))? desc:null);
		result.setObjectID((StringUtils.isNotEmpty(objectId))? objectId:null);
		result.setVersion((StringUtils.isNotEmpty(version))? version:null);
		result.setSerialNumber((StringUtils.isNotEmpty(serialNumber))? serialNumber:null);
		result.setVersionNumber((StringUtils.isNotEmpty(versionNumber))? versionNumber:null);
		result.setType((StringUtils.isNotEmpty(type))? type:null);
		if (StringUtils.isNotEmpty(imsi))
			result.getImsi().add(imsi);
		if (StringUtils.isNotEmpty(msisdn))
			result.getMsisdn().add(msisdn);
		return result;
	}
	
	private SubscriberEquipment createSubscriberEquipmentObj(SIMCard simCard, SIMEquipment simEquipment, CDMAEquipment cdmaEquipment) {
		SubscriberEquipment result = new SubscriberEquipment();
		if (simCard != null )
			result.setSimCard(simCard);
		if (simEquipment != null)
			result.setSimEquipment(simEquipment);
		if (cdmaEquipment != null) 
			result.setCDMAEquipment(cdmaEquipment);
		return result;
	}
	
	private CustomerAccountProfile createCustomerAccountProfileObj(String ban) {
		CustomerAccountProfile result = new CustomerAccountProfile();
		if (StringUtils.isNotBlank(ban))
			result.setCustomerID(ban);
		return result;
	}
	
	
	
	
}
