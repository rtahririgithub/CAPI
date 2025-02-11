package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.dao.SubscriptionManagementServiceDao;
import com.telus.cmb.common.logging.Sensitive;
import com.telus.cmb.common.prepaid.PrepaidUtils;
import com.telus.cmb.common.prepaid.SubscriptionManagementServiceClient;
import com.telus.cmb.common.util.DateUtil;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RechargeBalanceByPaymentInstrumentResponse;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RechargeBalanceByRegisteredPaymentInstrumentResponse;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RechargeBalanceByVoucherResponse;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RegisterAutomaticRechargeResponse;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RegisterRechargePaymentInstrument;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.commondomaintypes_v4.CardNumberDisplay;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.commondomaintypes_v4.PaymentInstrument;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.AutomaticRechargeProfile;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.RechargeableRealTimeBalance;


@Component
public class SubscriptionManagementServiceDaoImpl implements SubscriptionManagementServiceDao {
	
	private static final Logger LOGGER = Logger.getLogger(SubscriptionManagementServiceDaoImpl.class);

	@Autowired
	private SubscriptionManagementServiceClient smsWSClient;

	@Override
	public void removeTopupCreditCard(String phoneNumber) throws ApplicationException {
		/*
		 * removeSubscriberCreditCard -> SubscriptionManagementService.updateRechargePaymentInstrument
		 * MDN												- subscriptionID
		 * 
		 * Notes:
		 * paymentInstrument/paymentInstrumentType			- set as "CC" always
		 * paymentInstrument/expiryMonth					- set as "00" always
		 * paymentInstrument/expiryYear						- set as "00" always
		 */
		
		//Log Request
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("RemoveTopupCreditCard | PhoneNumber:" + phoneNumber);
		}
		
		smsWSClient.updateRechargePaymentInstrument(phoneNumber, createRemoveCreditCardPaymentInstrumentObj());
		
		//Log response
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("RemoveTopupCreditCard successfully for " + phoneNumber);
		}
	}
	
	@Override
	public void registerTopupCreditCard(String phoneNumber, @Sensitive CreditCardInfo creditCard) throws ApplicationException {
		
		//Log request
		logTopupCreditCardReq("RegisterTopupCreditCard", 
				phoneNumber, 
				creditCard.getType(),
				creditCard.getLeadingDisplayDigits(), 
				creditCard.getTrailingDisplayDigits(), 
				creditCard.getExpiryDate());
		
		RegisterRechargePaymentInstrument parameters = new RegisterRechargePaymentInstrument();
		parameters.setSubscriptionID(phoneNumber);
		PaymentInstrument paymentInstrument = createPaymentInstrumentObj("CC", 
				creditCard.getType(),
				creditCard.getToken(),
				creditCard.getLeadingDisplayDigits(), 
				creditCard.getTrailingDisplayDigits(), 
				null, 
				creditCard.getExpiryDate());
		parameters.setPaymentInstrument(paymentInstrument);
		
		smsWSClient.registerRechargePaymentInstrument(parameters, PrepaidUtils.createOriginatingUserType());
		
		//Log response
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("RegisterTopupCreditCard successfully for " + phoneNumber);
		}

	}

	@Override
	public void updateTopupCreditCard(String phoneNumber, @Sensitive CreditCardInfo creditCard) throws ApplicationException {
		/*
		 * updateSubscriberCreditCard -> SubscriptionManagementService.updateRechargePaymentInstrument
		 * 
		 * BAN 						- N/A
		 * MDN/phoneNumber			- subscriptionID
		 * serialNo					- N/A
		 * CreditCard
		 * 		Token				- paymentInstrument/cardNumber
		 * 		Last4Digits			- paymentInstrument/cardNumberDisplay/last4Digits
		 * 		First6Digits		- paymentInstrument/cardNumberDisplay/first6Digits
		 * 		Type				- paymentInstrument/cardType
		 * 		ExpiryDate			- paymentInstrument/expiryMonth & paymentInstrument/expiryYear.
		 * user						- N/A
		 * 
		 * Notes: 
		 * - getPrepaidApi().updateSubscriberCreditCard support updating the expiry date only.
		 * - set paymentInstrument/paymentInstrumentType as "CC" for Creditcard.
		 * 		- In general paymentInstrumentType can be CC/DC/CCA/DCA
		 * 		- CC: Creditcard
		 * 		- DC: Debitcard
		 * 		- CCA: Creditcard topup at the time of activation
		 * 		- DCA: Debitcard topup at the the activation
		 * - Need to validate expiryMonth to be not "0"/"00" (e.g. large than 0. January is "01". It might be valid for expiryYear to be "00").
		 * - If both expiryMonth and expiryYear are zero, it is for removing the CC.
		 * 
		 */
		
		//Log request
		logUpdateTopupCreditCardReq("UpdateTopupCreditCard (Expiry Date only)", 
				phoneNumber, 
				creditCard.getLeadingDisplayDigits(), 
				creditCard.getTrailingDisplayDigits(), 
				creditCard.getExpiryDate());
		
		PaymentInstrument paymentInstrument = createPaymentInstrumentObj("CC", 
				creditCard.getType(),
				creditCard.getToken(), 
				creditCard.getLeadingDisplayDigits(), 
				creditCard.getTrailingDisplayDigits(), 
				null, 
				creditCard.getExpiryDate());
		smsWSClient.updateRechargePaymentInstrument(phoneNumber, paymentInstrument);
		
		//Log response
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("UpdateTopupCreditCard successfully for " + phoneNumber);
		}
	}
	
	private void logUpdateTopupCreditCardReq(String method, String phoneNumber, String ccFirst6Digits, String ccLast4Digits, Date ccExpiryDate) {
		logTopupCreditCardReq(method, phoneNumber, null, ccFirst6Digits, ccLast4Digits, ccExpiryDate);
	}
	
	private void logTopupCreditCardReq(String method, String phoneNumber, String ccType, String ccFirst6Digits, String ccLast4Digits, Date ccExpiryDate) {
		if (LOGGER.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append(method);
			sb.append(" | PhoneNumber:" + phoneNumber);
			sb.append(" | CardType:" + ccType);
			sb.append(" | First6Digits:" + ccFirst6Digits);
			sb.append(" | Last4Digits:" + ccLast4Digits);
			sb.append(" | ExpiryDate:" + ccExpiryDate.toString());
			LOGGER.debug(sb.toString());
		}
	}
	
	@Override
	public void updateAutoTopUp(String phoneNumber, 
			AutoTopUpInfo autoTopUpInfo, 
			boolean existingAutoTopUp, 
			boolean existingThresholdRecharge) throws ApplicationException {
		/* 
		 * deleteThresholdRecharge 		-> SubscriptionManagementService.changeAutomaticRecharge (amount as "0") 
		 * deleteAutoRecharge 			-> SubscriptionManagementService.changeAutomaticRecharge (amount as "0")
		 * updateAutoRecharge			-> SubscriptionManagementService.changeAutomaticRecharge
		 * updateThresholdRecharge		-> SubscriptionManagementService.changeAutomaticRecharge
		 * 
		 * phoneNumber 					- subscriptionID
		 * userId 						- N/A
		 * 
		 * Notes:
		 * balanceType					- always "primary"
		 * recahrgeType					- interval / threshold (interval can exist without threshold, threshold can't exist without interval).
		 * rechargeInstrumentType 		- always "CC" for changeAutomaticRecharge (updating / deleting AutoTopup).
		 * 
		 * Deleting AutoTopup by calling SubscriptionManagementService.changeAutomaticRecharge:
		 * - To delete interval / threshold AutoTopu, set the amount as "0".
		 * - Prepaid WS will delete the threshold while deleting the interval.
		 * - Prepaid WS will NOT delete the interval while deleting threshold only.
		 *
		 * 
		 * insertThresholdRecharge		-> SubscriptionManagementService.registerAutomaticRecharge
		 * insertAutoRecharge			-> SubscriptionManagementService.registerAutomaticRecharge
		 * 
		 * Notes:
		 * Mapping refers to SubscriptionManagementService.changeAutomaticRecharge.
		 * insertThresholdRecharge can't exist without insertAutoRecharge.
		 * immediateRecharge			- always "false", as getPrepaidApi().insertAutoRecharge & getPrepaidApi().insertThresholdRecharge do not perform immediate recharge.
		 * 
		 * 
		 * Need to test the following test cases:
		 * 								- Updating AutoTopup (interval with/without threshold)
		 * 								- Updating AutoTopup interval while removing threshold
		 * 								- Registering AutoTopup (interval with/without threshold, immediateRecharge as false)
		 * 
		 */
		
		
		boolean updateOrDeleteInterval = false;
		boolean updateOrDeleteThreshold = false;
		boolean registerInterval = false;
		boolean registerThreshold = false;
		//Valid AutoTopup amounts for both interval and threshold are: 10,25,50
		double intervalAmount = 0;
		double thresholdAmount = 0;
		int removeAutoTopupAmount = 0; //The Interval/Threshold will be removed if the value is 0
		
		if (autoTopUpInfo != null) {
			//setting the existingAutoTopUp and existingThresholdRecharge to true will remove the existing one before inserting the one in autoTopUpInfo
			LOGGER.debug(" amount:" + autoTopUpInfo.getChargeAmount());
			LOGGER.debug(" hasThresholdRecharge:" + autoTopUpInfo.hasThresholdRecharge());
			LOGGER.debug(" thresholdAmount:" + autoTopUpInfo.getThresholdAmount());
			
			if (existingAutoTopUp) {
				//existingAutoTopUp is true, need to update the interval / threshold
				if (autoTopUpInfo.getChargeAmount() <= 0) {
					//delete both interval & threshold 
					//Note: By default, Prepaid WS delete the threshold while deleting the interval
					//delete interval if existingAutoTopUp is true and getChargeAmount is negative
					updateOrDeleteInterval = true;
					if (existingThresholdRecharge) {
						//delete threshold if existingThresholdRecharge is true
						updateOrDeleteThreshold = true;
						thresholdAmount = removeAutoTopupAmount;
					}
					intervalAmount = removeAutoTopupAmount;
				} else {
					//update interval if existingAutoTopUp is true and getChargeAmount is positive
					updateOrDeleteInterval = true;
					intervalAmount = autoTopUpInfo.getChargeAmount();
					
					//check for threshold - delete/update/register
					if (existingThresholdRecharge) {
						if (!autoTopUpInfo.hasThresholdRecharge()) {
							//delete threshold
							updateOrDeleteThreshold = true;
							thresholdAmount = removeAutoTopupAmount;
						} else {
							//update threshold
							updateOrDeleteThreshold = true;
							thresholdAmount = autoTopUpInfo.getThresholdAmount();
						}
					} else if (autoTopUpInfo.hasThresholdRecharge()) {
						//register new threshold (need to set the interval as well)
						registerThreshold = true;
						registerInterval = true;
						thresholdAmount = autoTopUpInfo.getThresholdAmount();
					}
				}
			
			} else {
				//existingAutoTopUp is false, need to register the interval / threshold
				if (autoTopUpInfo.getChargeAmount() <= 0) {
					LOGGER.debug("ExistingAutoTopUp is true yet ChargeAmount is negative for " + phoneNumber);
				} else {
					//register new interval if existingAutoTopUp is false (threshold as well if autoTopUpInfo.hasThresholdRecharge() is true)
					registerInterval = true;
					intervalAmount = autoTopUpInfo.getChargeAmount();
					if (autoTopUpInfo.hasThresholdRecharge()) {
						registerThreshold = true;
						thresholdAmount = autoTopUpInfo.getThresholdAmount();
					}
				}
			}
			
			//Log request
			logUpdateAutoTopUpReq(phoneNumber, 
					existingAutoTopUp,
					existingThresholdRecharge,
					autoTopUpInfo.hasThresholdRecharge(),
					updateOrDeleteInterval, 
					updateOrDeleteThreshold,
					registerInterval,
					registerThreshold,
					autoTopUpInfo.getChargeAmount(),
					autoTopUpInfo.getThresholdAmount(),
					intervalAmount, 
					thresholdAmount);
			
			updateAutoTopUp(phoneNumber, 
					updateOrDeleteInterval, 
					updateOrDeleteThreshold,
					registerInterval,
					registerThreshold,
					intervalAmount, 
					thresholdAmount);
			
			//Log response
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("UpdateAutoTopUp successfully for " + phoneNumber);
			}
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("AutoTopUpInfo is NULL for UpdateAutoTopUp with phone number " + phoneNumber + ".");
			}
		}
	}
	
	private void logUpdateAutoTopUpReq(String phoneNumber, 
			boolean existingAutoTopUp,
			boolean existingThresholdRecharge,
			boolean hasThresholdRecharge,
			boolean updateOrDeleteInterval, 
			boolean updateOrDeleteThreshold, 
			boolean registerInterval, 
			boolean registerThreshold,
			double atutoTopupChargeAmount,
			double atutoTopupThresholdAmount,
			double intervalAmount, 
			double thresholdAmount) {
		if (LOGGER.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("UpdateAutoTopUp");
			sb.append(" | PhoneNumber:" + phoneNumber);
			sb.append(" | ExistingAutoTopUp:" + existingAutoTopUp);
			sb.append(" | AtutoTopupChargeAmount:" + atutoTopupChargeAmount);
			sb.append(" | AtutoTopupThresholdAmount:" + atutoTopupThresholdAmount);
			sb.append(" | ExistingThresholdRecharge:" + existingThresholdRecharge);
			sb.append(" | HasThresholdRecharge:" + hasThresholdRecharge);
			sb.append(" | RegisterInterval:" + registerInterval);
			sb.append(" | UpdateOrDeleteInterval:" + updateOrDeleteInterval);
			sb.append(" | IntervalAmount:" + intervalAmount);
			sb.append(" | RegisterThreshold:" + registerThreshold);
			sb.append(" | UpdateOrDeleteThreshold:" + updateOrDeleteThreshold);
			sb.append(" | ThresholdAmount:" + thresholdAmount);
			LOGGER.debug(sb.toString());
		}
	}

	private void updateAutoTopUp(String phoneNumber, 
			boolean updateOrDeleteInterval, 
			boolean updateOrDeleteThreshold, 
			boolean registerInterval, 
			boolean registerThreshold,
			double intervalAmount, 
			double thresholdAmount) throws ApplicationException {
		
		if (registerInterval || registerThreshold) {
			//call SubscriptionManagementService.registerAutomaticRecharge
			List<AutomaticRechargeProfile> automaticRechargeProfiles = createAutomaticRechargeProfilesObj(registerInterval, 
					registerThreshold, 
					intervalAmount, 
					thresholdAmount);
			
			//Log request
			logUpdateAutoTopUpReq("UpdateAutoTopUp (RegisterAutomaticRecharge)", phoneNumber, automaticRechargeProfiles);
			
			//getPrepaidApi().insertAutoRecharge & getPrepaidApi().insertThresholdRecharge do not perform immediate recharge
			boolean immediateRecharge = false;
			RegisterAutomaticRechargeResponse response = smsWSClient.registerAutomaticRecharge(phoneNumber, PrepaidUtils.PRIMARY_TYPE, automaticRechargeProfiles, immediateRecharge, null);
			
			//Log response
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("UpdateAutoTopUp (RegisterAutomaticRecharge) successfully for " + phoneNumber);
			}
			logUpdateAutoTopUpRegisterRes("UpdateAutoTopUp (RegisterAutomaticRecharge)", phoneNumber, response);
			
		} else {
			//call SubscriptionManagementService.changeAutomaticRecharge only if not registering interval or threshold
			if (updateOrDeleteInterval || updateOrDeleteThreshold) {
				List<AutomaticRechargeProfile> automaticRechargeProfiles = createAutomaticRechargeProfilesObj(updateOrDeleteInterval, 
						updateOrDeleteThreshold, 
						intervalAmount, 
						thresholdAmount);
				
				//Log request
				logUpdateAutoTopUpReq("UpdateAutoTopUp (ChangeAutomaticRecharge)", phoneNumber, automaticRechargeProfiles);
				
				smsWSClient.changeAutomaticRecharge(phoneNumber, PrepaidUtils.PRIMARY_TYPE, automaticRechargeProfiles);
				
				//Log response
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("UpdateAutoTopUp (ChangeAutomaticRecharge) successfully for " + phoneNumber);
				}
			}
		}
	}
	
	private void logUpdateAutoTopUpReq(String methodName, String phoneNumber, List<AutomaticRechargeProfile> automaticRechargeProfiles) {
		if (LOGGER.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append(methodName);
			sb.append(" | PhoneNumber: " + phoneNumber);
			sb.append(getAutomaticRechargeProfilesString(automaticRechargeProfiles));
			LOGGER.debug(sb.toString());
		}
	}
	
	private void logUpdateAutoTopUpRegisterRes(String methodName, String phoneNumber, RegisterAutomaticRechargeResponse response) {
		if (LOGGER.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append(methodName);
			sb.append(" | PhoneNumber: " + phoneNumber);
			if (response != null && response.getBalance() != null) {
				RechargeableRealTimeBalance balance = response.getBalance();
				if (balance != null) {
					sb.append(" | BalanceType: " + balance.getBalanceType());
					sb.append(" | BalanceAmount: " + balance.getBalanceAmount());
					List<AutomaticRechargeProfile> autoRecharges = balance.getAutomaticRechargeProfiles();
					for (AutomaticRechargeProfile autoRecharge: autoRecharges) {
						sb.append(" | RechargeType: " + autoRecharge.getRechargeType());
						sb.append(" | RechargeAmount: " + autoRecharge.getRechargeAmount());
						sb.append(" | ThresholdAmount: " + autoRecharge.getThresholdAmount());
					}
				}
			}
			LOGGER.debug(sb.toString());
		}
	}
	
	private String getAutomaticRechargeProfilesString(List<AutomaticRechargeProfile> automaticRechargeProfiles) {
		StringBuffer sb = new StringBuffer();
		if (automaticRechargeProfiles != null && automaticRechargeProfiles.size() > 0) {
			for (AutomaticRechargeProfile automaticRechargeProfile:automaticRechargeProfiles) {
				sb.append(" | RechargeInstrumentType:" + automaticRechargeProfile.getRechargeInstrumentType());
				sb.append(" | RechargeAmount:" + automaticRechargeProfile.getRechargeAmount());
				sb.append(" | RechargeInstrumentType:" + automaticRechargeProfile.getRechargeInstrumentType());
				sb.append(" | ThresholdAmount:" + automaticRechargeProfile.getThresholdAmount());
			}
		}
		return sb.toString();
	}
	
	
	public void applyTopUp(String phoneNumber, String voucherPin) throws ApplicationException {
		/*
		 * applyTopupWithAirtimecard -> SubscriptionManagementService.rechargeBalanceByVoucher
		 * phoneNumber 		- rechargeBalanceByVoucher/subscriptionID
		 * voucherPIN 		- rechargeBalanceByVoucher/voucherPIN
		 * 
		 * Notes: balanceType always be "primary"
		 */
		
		//Log Request
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ApplyTopUp | PhoneNumber:" + phoneNumber + " | VoucherPin:" + voucherPin);
		}
		
		RechargeBalanceByVoucherResponse response = smsWSClient.rechargeBalanceByVoucher(phoneNumber, PrepaidUtils.PRIMARY_TYPE, voucherPin);
		
		//Log response
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ApplyTopUp successfully for " + phoneNumber 
				+ " with voucher amount " + response.getVoucherAmount() 
				+ " and balance " + PrepaidUtils.getRealTimeBalanceAmountForSmsService(response.getBalance()));
		}
		
	}
	
	@Override
	public String applyTopUpWithCreditCard(String phoneNumber, double amount) throws ApplicationException {
		/*
		 * applyTopupWithCreditCard -> SubscriptionManagementService.rechargeBalanceByRegisteredPaymentInstrument
		 * phoneNumber 		- rechargeBalanceByRegisteredPaymentInstrument/subscriptionID
		 * amount 			- rechargeBalanceByRegisteredPaymentInstrument/amount
		 * 
		 * Notes:
		 * balanceType always be "primary"
		 * paymentInstrumentType always be "CC"
		 * cvv is optional & N/A now
		 * 
		 */
		
		//Log Request
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ApplyTopUpWithCreditCard | PhoneNumber:" + phoneNumber + " | Amount:" + amount);
		}
		
		RechargeBalanceByRegisteredPaymentInstrumentResponse response = smsWSClient.rechargeBalanceByRegisteredPaymentInstrument(phoneNumber, 
				amount, 
				PrepaidUtils.PRIMARY_TYPE, 
				PrepaidUtils.INSTRUMENT_TYPE_CREDITCARD, 
				null, null);
		
		//Log response
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ApplyTopUpWithCreditCard successfully for " + phoneNumber 
				+ " with reference number " + response.getReferenceNumber()
				+ " and balance " + PrepaidUtils.getRealTimeBalanceAmountForSmsService(response.getBalance()));
		}
		
		return StringUtils.isBlank(response.getReferenceNumber())? "" : response.getReferenceNumber();
	}
	
	@Override
	public String applyTopUpWithDebitCard(String phoneNumber, double amount) throws ApplicationException {
		/*
		 * applyTopupWithBankCard -> SubscriptionManagementService.rechargeBalanceByPaymentInstrument
		 * phoneNumber 					- rechargeBalanceByPaymentInstrument/subscriptionID
		 * amount 						- rechargeBalanceByPaymentInstrument/amount
		 * 
		 * Notes:
		 * balanceType always be "primary"
		 * paymentInstrumentType always be "DC"
		 * Prepaid WS rechargeBalanceByPaymentInstrument support onetime topup with non registered Creditcard but CAPI don't support this as of April/2014 release Surepay Retirement
		 */
		
		//Log Request
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ApplyTopUpWithDebitCard | PhoneNumber:" + phoneNumber + " | Amount:" + amount);
		}
		
		RechargeBalanceByPaymentInstrumentResponse response = smsWSClient.rechargeBalanceByPaymentInstrument(phoneNumber, 
				amount, 
				PrepaidUtils.PRIMARY_TYPE, 
				PrepaidUtils.INSTRUMENT_TYPE_DEBITCARD, 
				null, null);
		
		//Log response
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ApplyTopUpWithDebitCard successfully for " + phoneNumber 
				+ " with Reference Number " + response.getReferenceNumber()
				+ " and balance " + PrepaidUtils.getRealTimeBalanceAmountForSmsService(response.getBalance()));
		}
		
		return StringUtils.isBlank(response.getReferenceNumber())? "" : response.getReferenceNumber();
	}
	
	
	@Override
	public TestPointResultInfo test() {
		return smsWSClient.test();
	}
	
	//Helper methods
	
	private PaymentInstrument createRemoveCreditCardPaymentInstrumentObj() {
		PaymentInstrument result = new PaymentInstrument();
		result.setPaymentInstrumentType(PrepaidUtils.INSTRUMENT_TYPE_CREDITCARD);
		result.setExpiryMonth("00");
		result.setExpiryYear("00");
		return result;
	}
	
	private List<AutomaticRechargeProfile> createAutomaticRechargeProfilesObj(boolean createInterval, 
			boolean createThreshold, 
			double intervalAmount, 
			double thresholdAmount) {
		ArrayList<AutomaticRechargeProfile> result = new ArrayList<AutomaticRechargeProfile>();
		if (createInterval) {
			//interval
			AutomaticRechargeProfile interval = createAutomaticRechargeProfileObj(PrepaidUtils.RECHARGE_TYPE_INTERVAL, 
					intervalAmount, 
					PrepaidUtils.INSTRUMENT_TYPE_CREDITCARD,
					thresholdAmount);
			result.add(interval);
		}
		if (createThreshold) {
			//threshold
			AutomaticRechargeProfile threshold = createAutomaticRechargeProfileObj(PrepaidUtils.RECHARGE_TYPE_THRESHOLD, 
					intervalAmount, 
					PrepaidUtils.INSTRUMENT_TYPE_CREDITCARD,
					thresholdAmount);
			result.add(threshold);
		}
		return result;
		
	}
	
	private AutomaticRechargeProfile createAutomaticRechargeProfileObj(String rechargeType, 
			double rechargeAmount, 
			String rechargeInstrumentType, 
			double thresholdAmount) {
		AutomaticRechargeProfile result = new AutomaticRechargeProfile();
		if (StringUtils.isNotBlank(rechargeType)) {
			result.setRechargeType(rechargeType);
		}
		if (StringUtils.isNotBlank(rechargeInstrumentType)) {
			result.setRechargeInstrumentType(rechargeInstrumentType);
		}
		//CAPI need to set this rechargeAmount for both RECHARGE_TYPE_INTERVAL & RECHARGE_TYPE_THRESHOLD
		result.setRechargeAmount(rechargeAmount);
		if (PrepaidUtils.RECHARGE_TYPE_THRESHOLD.equals(rechargeType)) {
			result.setThresholdAmount(thresholdAmount);
			//if removing the threshold, overriding rechargeAmount to be "0" (while rechargeType is RECHARGE_TYPE_THRESHOLD)
			if (thresholdAmount == 0) {
				result.setRechargeAmount(thresholdAmount);
			}
		}
		return result;
	}
	
	private PaymentInstrument createPaymentInstrumentObj(String paymentInstrumentType, 
			String cardType, 
			String cardNumber,
			String first6Digits, 
			String last4Digits, 
			String maskedCardNumber, 
			Date expiryDate) {
		PaymentInstrument result = new PaymentInstrument();
		if (StringUtils.isNotBlank(paymentInstrumentType)) {
			result.setPaymentInstrumentType(paymentInstrumentType);
		}
		if (StringUtils.isNotBlank(cardType)) {
			result.setCardType(cardType);
		}
		if (StringUtils.isNotBlank(cardNumber)) {
			result.setCardNumber(cardNumber);
		}
		result.setCardNumberDisplay(createCardNumberDisplayObj(first6Digits, last4Digits, maskedCardNumber));
		if (expiryDate != null) {
			//both month and year must be in two digits format
			result.setExpiryMonth(DateUtil.getTwoDigitMonth(expiryDate));
			result.setExpiryYear(DateUtil.getLastTwoDigitYear(expiryDate));
		}
		return result;
	}
	
	private CardNumberDisplay createCardNumberDisplayObj(String first6Digits, String last4Digits, String maskedCardNumber) {
		CardNumberDisplay result = new CardNumberDisplay();
		if (StringUtils.isNotBlank(first6Digits)) {
			result.setFirst6Digits(first6Digits);
		}
		if (StringUtils.isNotBlank(last4Digits)) {
			result.setLast4Digits(last4Digits);
		}
		if (StringUtils.isNotBlank(maskedCardNumber)) {
			result.setMaskedCardNumber(maskedCardNumber);
		}
		return result;
	}
	

}
