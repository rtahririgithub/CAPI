package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telus.api.ApplicationException;
import com.telus.api.account.PaymentCard;
import com.telus.cmb.account.lifecyclemanager.dao.PrepaidWirelessCustomerOrderServiceDao;
import com.telus.cmb.common.prepaid.PrepaidUtils;
import com.telus.cmb.common.prepaid.PrepaidWirelessCustomerOrderServiceClient;
import com.telus.cmb.common.util.DateUtil;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.PreRegisteredPrepaidCreditCardInfo;
import com.telus.eas.account.info.PrepaidCreditCardInfo;
import com.telus.eas.account.info.PrepaidDebitCardInfo;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.Create;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.CreateResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.Update;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.common_domain_types_3.CardNumberDisplay;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.common_domain_types_3.PaymentInstrument;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.common_domain_types_3.RechargeType;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscriber_types_3.AutomaticRechargeProfile;
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
	public void creditSubscriberMigration(String ban, 
			String esn, 
			String imei, 
			String phoneNumber, 
			String provinceCode, 
			String pin,
			double creditAmt, 
			String rateId, 
			int expiryDays, 
			String source, 
			String user, 
			String activationType, 
			String reasonCode) throws ApplicationException {
		/* 
		 * creditSubscriberMigration -> PrepaidWirelessCustomerOrderService.create
		 * accountId/BAN	- customerAccountProfile/customerID - String
		 * imei 			- subscriberEquipement/simEquipment/serialnumber
		 * esn 				- subscriberEquipement/simCard/serialNumber
		 * phoneNumber		- subscriberEquipement/simCard/msisdn (first one in list) - String
		 * provinceCode		- subscriptionProfile/subscriptionProvinceCode - String
		 * pin				- subscriptionProfile/subscriptionPIN - String
		 * creditAmt		- crediList/amount - double - (Note: Use "P2P" for credit type)
		 * rateID			- crediList/rateId - String
		 * expiryDays		- creditList/expiryDaysAmount - int
		 * source			- orderDetail/orderSource - String
		 * user				- orderDetail/orderAgent - String
		 * activationType	- orderDetail/orderType - String set to "P2P" always for creditSubscriberMigration
		 * reasonCode		- orderDetail/orderReasonID - String
		 */
		
		//Log request
		logCreditSubscriberMigrationReq(ban, 
				esn,
				imei,
				phoneNumber,
				provinceCode,
				pin,
				creditAmt,
				rateId,
				expiryDays,
				source,
				activationType,
				reasonCode);
		
		Create parameters = new Create();
		PrepaidWirelessCustomerOrder order = new PrepaidWirelessCustomerOrder();
		
		//CustomerAccountProfile
		order.setCustomerAccountProfile(createCustomerAccountProfileObj(ban));
		
		//SubscriberEquipment (CDMAEquipment, SIMCard, SIMEquipment)
		SubscriberEquipment subscriberEquipment = createSubscriberEquipmentObj(createSIMCardByPhoneNumberObj(esn, phoneNumber), createSIMEquipmentObj(imei), null); 
		order.setSubscriberEquipment(subscriberEquipment);
		
		//SubscriptionProfile
		order.setSubscriptionProfile(createSubscriptionProfileInfoObj(phoneNumber, provinceCode, pin));
		
		//CreditList
		order.getCrediList().add(createCreidtObj(creditAmt, PrepaidUtils.CREDIT_TYPE_P2P, rateId, expiryDays));
		
		//OrderDetail
		order.setOrderDetail(createOrderDetailObj(source, user, activationType, null, reasonCode));
		
		parameters.setPrepaidWirelessCustomerOrder(order);
		CreateResponse response = pwcosWSClient.create(parameters, PrepaidUtils.createOriginatingUserTypeObj());
		//response object is null.
		
		//Log response
		logCreditSubscriberMigrationReq(ban, phoneNumber);
	}
	
	private void logCreditSubscriberMigrationReq(String ban, 
			String esn,
			String imei,
			String phoneNumber,
			String provinceCode,
			String pin,
			double creditAmt,
			String rateId,
			int expiryDays,
			String source,
			String activationType,
			String reasonCode) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("CreditSubscriberMigration");
			sb.append(" | BAN: " + ban);
			sb.append(" | ESN: " + esn);
			sb.append(" | IMEI: " + imei);
			sb.append(" | PhoneNumber: " + phoneNumber);
			sb.append(" | ProvinceCode: " + provinceCode);
			sb.append(" | PIN: " + pin);
			sb.append(" | CreditAmt: " + creditAmt);
			sb.append(" | RateId: " + rateId);
			sb.append(" | ExpiryDays: " + expiryDays);
			sb.append(" | Source: " + source);
			sb.append(" | ActivationType: " + activationType);
			sb.append(" | ReasonCode: " + reasonCode);
			LOGGER.info(sb.toString());
		}
	}
	
	private void logCreditSubscriberMigrationReq(String ban,
			String phoneNumber) {
		if (LOGGER.isInfoEnabled()) 
			LOGGER.info("CreditSubscriberMigration for BAN " + ban + " and subscriber " + phoneNumber);
	}
	
	
	@Override
	public void saveActivationTopUpArrangement(String ban, 
			String esn, 
			String phoneNumber, 
			PaymentCard[] paymentCards) throws ApplicationException {
		/*
		 * getPrepaidApi().saveActivationTopUpArrangement(prepaidSubscriber, activationTopUpArrangement, user); - deprecated, no replacement
		 * getPrepaidApi().saveActivationTopUpArrangement(prepaidSubscriber,cards,user);
		 * 
		 * saveActivationTopUpArrangement -> PrepaidWirelessCustomerOrderService.update (Nagaraj) 
		 * BanId 									- CustomerAccountProfile/customerID (optional)
		 * MDN/Phone 								- subscriberEquipement/simCard/msisdn (optional) 
		 * serialNo(/ESN)							- subscriberEquipement/simCard/serialnumber (Mandatory) 
		 * 
		 * Card
		 * 		CreditCard (PreRegisteredPrepaidCreditCardInfo / PrepaidCreditCardInfo / CreditCardInfo)
		 * 											- rechargePayment/type: CREDITCARD
		 * 			PreRegistered					- if true (CC only), set registeredPaymentInstrument as in rechargePaymentList, if false, don't set registeredPaymentInstrument
		 * 			Token							- rechargePayment/PaymentInstrument/cardNumber
		 * 			Last4Digits						- rechargePayment/PaymentInstrument/CardNumberDisplay/maskedCardNumber (not last4Digits)
		 * 			ExpiryDate						- rechargePayment/PaymentInstrument/expiryMonth & expiryYear
		 * 			TopupAmount						- rechargePayment/amount
		 * 			ThreshHoldAmount				- automaticRechargeProfile/thresholdAmount
		 * 			RechargeAmount					- automaticRechargeProfile/rechargeAmount
		 * 											- automaticRechargeProfile/rechargeType: interval - RechargeAmount is not 0 & ThreshHoldAmount is 0
		 * 											- automaticRechargeProfile/rechargeType: threshold - RechargeAmount is not 0 & ThreshHoldAmount is not 0
		 * 											- automaticRechargeProfile/rechargeType: onetime - RechargeAmount is 0 & ThreshHoldAmount is 0, TopupAmount is not 0
		 * 		AirtimeCard
		 * 			CardNumber						- voucher/voucherPIN 
		 * 											- atcNumberAndPin: atcNumber.substring(atcNumber.length()-8, atcNumber.length())+cardInfo.getPIN())
		 * 											- import org.apache.commons.codec.binary.Base64;
		 * 											- Base64.encodeBase64(atcNumberAndPin.trim().getBytes()))
		 * 		DebitCard
		 * 											- rechargePayment/type: DEBITCARD
		 * 			TopupAmount						- rechargePayment/amount
		 * 
		 * Notes:
		 * - Use this sql (KB DB) before call this method: update prepaid_activation_temp set status_cd = 'p' where esn='8912230000000584548';
		 * - From the list of paymentCards, it credit card and debit card should not be available at the same time, or else Prepaid team will reject it, however, credit
		 *   card or debit card can coexist with the voucher/Airetime Card.
		 * - Prepaid team will get the ESN number from either one of the following
		 * - HSPA: subscriberEquipement/simCard/serialnumber
		 * - CDMA: subscriberEquipement/CDMAEquipment/serialnumber
		 * 
		 */

		//Log request
		logSaveActivationTopUpArrangementReq(ban, esn, phoneNumber, paymentCards);
		
		PrepaidWirelessCustomerOrder order = new PrepaidWirelessCustomerOrder();

		//CustomerAccountProfile
		order.setCustomerAccountProfile(createCustomerAccountProfileObj(ban));
		
		//SubscriberEquipment (CDMAEquipment, SIMCard, SIMEquipment)
		SIMCard simCard = createSIMCardByPhoneNumberObj(esn, phoneNumber);
		SubscriberEquipment subscriberEquipment = createSubscriberEquipmentObj(simCard, null, null);
		order.setSubscriberEquipment(subscriberEquipment);
		
		List<RechargePayment> rechargePaymentList = order.getRechargePaymentList();
		List<AutomaticRechargeProfile> automaticRechargeProfileList = order.getAutomaticRechargeProfileList();
		List<Voucher> voucherList = order.getVoucherList();
		order.setRegisteredPaymentInstrument(new PaymentInstrument());
		mapPaymentCards(ban, phoneNumber, paymentCards, rechargePaymentList, automaticRechargeProfileList, voucherList, order.getRegisteredPaymentInstrument());
		
		Update parameters = new Update();
		parameters.setPrepaidWirelessCustomerOrder(order);
		pwcosWSClient.update(parameters);
		
		//Log response
		LOGGER.info("SaveActivationTopUpArrangement successfully for ESN " + esn + " and Phone Number " + phoneNumber);
	}
	
	private void logSaveActivationTopUpArrangementReq(String ban, String esn, String phoneNumber, PaymentCard[] paymentCards) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("SaveActivationTopUpArrangement");
			sb.append(" | BAN: " + ban);
			sb.append(" | ESN: " + esn);
			sb.append(" | PhoneNumber: " + phoneNumber);
			if (paymentCards != null) {
				sb.append(" | Number of PaymentCards: " + paymentCards.length);
			}
			LOGGER.info(sb.toString());
		}
	}
	
	@Override
	public TestPointResultInfo test() {
		return pwcosWSClient.test();
	}
	
	
	/*
	 * Helper Mapping methods 
	 */
	
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
	
	private Credit createCreidtObj(double amount, String type, String rateId, int expiryDaysAmount) {
		return createCreidtObj(false, type, amount, rateId, expiryDaysAmount, null);
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
	
	private SubscriptionProfileInfo createSubscriptionProfileInfoObj(String id, String provinceCode, String pin) {
		return createSubscriptionProfileInfoObj(id, provinceCode, pin, null);
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
	
	private SIMCard createSIMCardByPhoneNumberObj(String esn, String phoneNumber) {
		//phone number being set as imsi
		SIMCard result = createSIMCardObj(null, null, null, null, esn, null, null, null, phoneNumber);
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
	
	private AutomaticRechargeProfile createAutomaticRechargeProfileObj(double topupAmount,
			double recharegeAmount,
			double thresholdAmount) {
		AutomaticRechargeProfile result = new AutomaticRechargeProfile();
		//only setting the amount if it is not negative
		if (recharegeAmount >= 0)
			result.setRechargeAmount(recharegeAmount);
		if (thresholdAmount >= 0) 
			result.setThresholdAmount(thresholdAmount);
		//setting recharegeType base on recharegeAmount, 
		if (recharegeAmount >= 0 && thresholdAmount == 0) {
			result.setRechargeType(RechargeType.INTERVAL);
		} else if (recharegeAmount >= 0 && thresholdAmount >= 0) {
			result.setRechargeType(RechargeType.THRESHOLD);
		} else if (recharegeAmount == 0 && thresholdAmount == 0 && topupAmount >= 0) {
			result.setRechargeType(RechargeType.ONETIME);
		} else {
			if (LOGGER.isInfoEnabled()) 
				LOGGER.info("Unknown automatic rechage type base on topupAmount: " + topupAmount 
					+ " recharegeAmount:" + recharegeAmount
					+ " thresholdAmount:" + thresholdAmount);
		}
		
		return result;
	}
	
	private void mapPaymentCards(String ban, 
			String phoneNumber, 
			PaymentCard[] paymentCards, 
			List<RechargePayment> rechargePaymentList, 
			List<AutomaticRechargeProfile> automaticRechargeProfileList, 
			List<Voucher> voucherList, 
			PaymentInstrument registeredPaymentInstrument) {
		for (int i = 0; i < paymentCards.length; i++) {
			if (paymentCards[i] instanceof PreRegisteredPrepaidCreditCardInfo) {
				mapPreRegisteredPrepaidCreditCard(ban, phoneNumber, (PreRegisteredPrepaidCreditCardInfo)paymentCards[i], 
						rechargePaymentList, automaticRechargeProfileList, registeredPaymentInstrument);
			} else if (paymentCards[i] instanceof PrepaidCreditCardInfo){
				mapPrepaidCreditCard(ban, phoneNumber, (PrepaidCreditCardInfo)paymentCards[i], rechargePaymentList);
			} else if (paymentCards[i] instanceof CreditCardInfo){
				mapCreditCard(ban, phoneNumber, (CreditCardInfo)paymentCards[i], rechargePaymentList);
			} else if (paymentCards[i] instanceof CardInfo){
				mapCard(ban, phoneNumber, (CardInfo)paymentCards[i], voucherList);
			} else if (paymentCards[i] instanceof PrepaidDebitCardInfo){
				mapPrepaidDebitCard(ban, phoneNumber, (PrepaidDebitCardInfo)paymentCards[i], rechargePaymentList);
			} else {
				if (LOGGER.isInfoEnabled()) 
					LOGGER.info("Unknown payment card for paymentCards number " + i + ".");
			}
		}
	}
	
	private void mapPreRegisteredPrepaidCreditCard(String ban,
			String phoneNumber, 
			PreRegisteredPrepaidCreditCardInfo card, 
			List<RechargePayment> rechargePaymentList, 
			List<AutomaticRechargeProfile> automaticRechargeProfileList, 
			PaymentInstrument registeredPaymentInstrument) {
		
		if (card != null) {
			//For rechargePaymentList
			//Reference on PrepaidWirelessCustomerOrderServiceDaoImpl.validatePayAndTalkSubscriberActivation
			if (card.hasToken()) {
				//register the CC as well - same as the one in rechargePayment
				updatePaymentInstrument(registeredPaymentInstrument, card.getToken(), card.getTrailingDisplayDigits(), card.getExpiryDate());
				
				RechargePayment rechargePayment = createCreditCardRechargePayment(registeredPaymentInstrument, card.getTopupAmount());
				rechargePaymentList.add(rechargePayment);
				
			} else {
				if (LOGGER.isInfoEnabled()) 
					LOGGER.info("CreditCardInfo Token is not available for BAN " + ban + " and phone number " + phoneNumber);
			}
			
			//For automaticRechargeProfileList
			AutomaticRechargeProfile automaticRechargeProfile = createAutomaticRechargeProfileObj(card.getTopupAmount(), card.getRechargeAmount(), card.getThresholdAmount());
			automaticRechargeProfileList.add(automaticRechargeProfile);
			
			//Log request
			logPreRegisteredPrepaidCreditCard(ban, phoneNumber, card.getTrailingDisplayDigits(), card.getTopupAmount(), card.getRechargeAmount(), card.getThresholdAmount());
			
		} else {
			if (LOGGER.isInfoEnabled()) 
				LOGGER.info("PreRegisteredPrepaidCreditCard is null for " + ban + " and phone number " + phoneNumber);
		}
	}
	
	private PaymentInstrument createPaymentInstrument(String cardNumber, String trailingDisplayDigits, Date expiryDate) {
		String expiryMonth = "";
		String expiryYear = "";
		CardNumberDisplay cardNumberDisplay = null;
		//Note: Prepaid team ask us to map the TrailingDisplayDigits to maskedCardNumber instead of the Last4Digits, 
		//pls reference to PrepaidWirelessCustomerOrderServiceDaoImpl.validatePayAndTalkSubscriberActivation in Account Information Helper EJB
		cardNumberDisplay = PrepaidUtils.createCardNumberDisplayObj(null, null, trailingDisplayDigits);
		expiryMonth = DateUtil.getTwoDigitMonth(expiryDate);
		expiryYear = DateUtil.getLastTwoDigitYear(expiryDate);
		PaymentInstrument paymentInstrument = PrepaidUtils.createPaymentInstrumentObj(null, null, cardNumber, cardNumberDisplay, expiryMonth, expiryYear); 
		
		return paymentInstrument;
	}

	private void updatePaymentInstrument(PaymentInstrument paymentInstrument, String cardNumber, String trailingDisplayDigits, Date expiryDate) {
		String expiryMonth = "";
		String expiryYear = "";
		CardNumberDisplay cardNumberDisplay = null;
		//Note: Prepaid team ask us to map the TrailingDisplayDigits to maskedCardNumber instead of the Last4Digits, 
		//pls reference to PrepaidWirelessCustomerOrderServiceDaoImpl.validatePayAndTalkSubscriberActivation in Account Information Helper EJB
		cardNumberDisplay = PrepaidUtils.createCardNumberDisplayObj(null, null, trailingDisplayDigits);
		expiryMonth = DateUtil.getTwoDigitMonth(expiryDate);
		expiryYear = DateUtil.getLastTwoDigitYear(expiryDate);
		updatePaymentInstrumentObj(paymentInstrument, cardNumber, cardNumberDisplay, expiryMonth, expiryYear); 
		
	}
	
	private void updatePaymentInstrumentObj(PaymentInstrument paymentInstrument,
			String cardNumber,
			CardNumberDisplay cardNumberDisplay,
			String expiryMonth,
			String expiryYear) {
		if (StringUtils.isNotBlank(cardNumber))
			paymentInstrument.setCardNumber(cardNumber);
		if (cardNumberDisplay != null)
			paymentInstrument.setCardNumberDisplay(cardNumberDisplay);
		if (StringUtils.isNotBlank(expiryMonth))
			paymentInstrument.setExpiryMonth(expiryMonth);
		if (StringUtils.isNotBlank(expiryYear))
			paymentInstrument.setExpiryYear(expiryYear);
	}
	
	private RechargePayment createCreditCardRechargePayment(PaymentInstrument paymentInstrument, double topupAmount) {
		RechargePayment rechargePayment = new RechargePayment();
		//amount is mandatory, set as 0 if the amount is less than 0.
		if (topupAmount<0)
			LOGGER.info("Creating RechargePayment for CreditCard with unexpected amount " + topupAmount + ", set as 0 instead.");
		rechargePayment.setAmount(topupAmount>0? topupAmount:0);
		rechargePayment.setType(PrepaidUtils.RECHARGEPAYMENT_TYPE_CREDITCARD);
		rechargePayment.setPaymentInstrument(paymentInstrument);
		return rechargePayment;
	}
	
	private void logPreRegisteredPrepaidCreditCard(String ban, String phoneNumber, String trailingDisplayDigits, double topupAmount, double rechargeAmount, double thresholdAmount) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("PreRegisteredPrepaidCreditCard");
			sb.append(" | BAN: " + ban);
			sb.append(" | PhoneNumber: " + phoneNumber);
			sb.append(" | TrailingDisplayDigits: " + trailingDisplayDigits);
			sb.append(" | TopupAmount: " + topupAmount);
			sb.append(" | RechargeAmount: " + rechargeAmount);
			sb.append(" | ThresholdAmount: " + thresholdAmount);
			LOGGER.info(sb.toString());
		}
	}
	
	private void mapPrepaidCreditCard(String ban,
			String phoneNumber, 
			PrepaidCreditCardInfo card, 
			List<RechargePayment> rechargePaymentList) {
		if (card.hasToken()) {
			PaymentInstrument paymentInstrument = createPaymentInstrument(card.getToken(), card.getTrailingDisplayDigits(), card.getExpiryDate());
			RechargePayment rechargePayment = createCreditCardRechargePayment(paymentInstrument, card.getTopupAmount());
			rechargePaymentList.add(rechargePayment);

			//Log request
			logPrepaidCreditCard(ban, phoneNumber, card.getTrailingDisplayDigits(), card.getTopupAmount());

		} else {
			if (LOGGER.isInfoEnabled())
				LOGGER.info("CreditCardInfo Token is not available for BAN " + ban + " and phone number " + phoneNumber);
		}
	}
	
	private void logPrepaidCreditCard(String ban, String phoneNumber, String trailingDisplayDigits, double topupAmount) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("PrepaidCreditCard");
			sb.append(" | BAN: " + ban);
			sb.append(" | PhoneNumber: " + phoneNumber);
			sb.append(" | TrailingDisplayDigits: " + trailingDisplayDigits);
			sb.append(" | TopupAmount: " + topupAmount);
			LOGGER.info(sb.toString());
		}
	}
	
	private void mapCreditCard(String ban,
			String phoneNumber, 
			CreditCardInfo card, 
			List<RechargePayment> rechargePaymentList) {
		if (card.hasToken()) {
			PaymentInstrument paymentInstrument = createPaymentInstrument(card.getToken(), card.getTrailingDisplayDigits(), card.getExpiryDate());
			RechargePayment rechargePayment = createCreditCardRechargePayment(paymentInstrument, -1);
			rechargePaymentList.add(rechargePayment);

			//Log request
			logCreditCard(ban, phoneNumber, card.getTrailingDisplayDigits());

		} else {
			if (LOGGER.isInfoEnabled()) 
				LOGGER.info("CreditCardInfo Token is not available for BAN " + ban + " and phone number " + phoneNumber);
		}
	}

	private void logCreditCard(String ban, String phoneNumber, String trailingDisplayDigits) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("CreditCard");
			sb.append(" | BAN: " + ban);
			sb.append(" | PhoneNumber: " + phoneNumber);
			sb.append(" | TrailingDisplayDigits: " + trailingDisplayDigits);
			LOGGER.info(sb.toString());
		}
	}
	
	private void mapCard(String ban, String phoneNumber, CardInfo card, List<Voucher> voucherList) {
		if (card != null) {
			String atcNumber = card.getSerialNumber();
			String atcNumberAndPin = atcNumber.substring(atcNumber.length()-8, atcNumber.length())+card.getPIN();
			Voucher voucher = new Voucher();
			String encodedVoucherPin = PrepaidUtils.encodeBase64String(atcNumberAndPin);
			voucher.setVoucherPIN(encodedVoucherPin);
			voucherList.add(voucher);
			
			if (LOGGER.isInfoEnabled()) 
				LOGGER.info("Card with VoucherPin for BAN " + ban + " and phone number " + phoneNumber);
		}
	}
	
	private void mapPrepaidDebitCard(String ban, String phoneNumber, PrepaidDebitCardInfo card, List<RechargePayment> rechargePaymentList) {
		if (card != null) {
			RechargePayment rechargePayment = new RechargePayment();
			rechargePayment.setType(PrepaidUtils.RECHARGEPAYMENT_TYPE_DEBITCARD);
			//amount is mandatory, set as 0 if the amount is less than 0.
			if (card.getTopupAmount()<0)
				LOGGER.info("Creating RechargePayment for DebitCard with unexpected amount " + card.getTopupAmount() + ", set as 0 instead.");
			rechargePayment.setAmount(card.getTopupAmount()>0? card.getTopupAmount():0);
			rechargePaymentList.add(rechargePayment);
			
			logPrepaidDebitCard(ban, phoneNumber, card.getTopupAmount());
		}
	}
	
	private void logPrepaidDebitCard(String ban, String phoneNumber, double topupAmount) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("PrepaidDebitCard");
			sb.append(" | BAN: " + ban);
			sb.append(" | PhoneNumber: " + phoneNumber);
			sb.append(" | TopupAmount: " + topupAmount);
			LOGGER.info(sb.toString());
		}
	}
	
	
	
}
