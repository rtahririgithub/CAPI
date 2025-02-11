package com.telus.cmb.account.informationhelper.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telus.api.ApplicationException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.PrepaidConsumerAccount;
import com.telus.cmb.account.informationhelper.dao.PrepaidSubscriberServiceDao;
import com.telus.cmb.common.prepaid.PrepaidSubscriberServiceClient;
import com.telus.cmb.common.prepaid.PrepaidUtils;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.prepaidsubscriberservice_v3.GetDetail;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.prepaidsubscriberservice_v3.GetDetailResponse;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.CostRate;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.Duration;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.Individual;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.Money;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.TimePeriod;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.AutomaticRechargeProfile;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.RateProfile;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.RechargeableRealTimeBalance;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.Subscriber;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.Subscription;


@Component
public class PrepaidSubscriberServiceDaoImpl implements PrepaidSubscriberServiceDao {
	
	private static final Logger LOGGER = Logger.getLogger(PrepaidSubscriberServiceDaoImpl.class);
	
	@Autowired
	private PrepaidSubscriberServiceClient pssWSClient;
	
	@Override
	public PrepaidConsumerAccountInfo retrieveAccountInfo(int pBan, String pPhoneNumber) throws ApplicationException {
		PrepaidConsumerAccountInfo accountInfo = new PrepaidConsumerAccountInfo();
		accountInfo.setBanId(pBan);
		this.retrieveAccountInfo(accountInfo, pPhoneNumber);
		return accountInfo;
	}
	
	@Override
	public void retrieveAccountInfo(PrepaidConsumerAccountInfo accountInfo, String pPhoneNumber) throws ApplicationException {
		if (accountInfo != null) {
			if (AccountSummary.STATUS_CLOSED != accountInfo.getStatus() && AccountSummary.STATUS_CANCELED != accountInfo.getStatus()) {
				//Do not call Prepaid Web Services to retrieve account if the status is Closed or Canceled
				try {
					Subscriber subscriber = retrieveAccountInfo(pPhoneNumber);
					mapToPrepaidConsumerAccountInfo(accountInfo, subscriber, pPhoneNumber);
				} catch (ApplicationException ae) {
					//Overlook all ApplicationException so upstream application can still continue the workflow
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("Overlook Prepaid Application Exception for subuscriber " + pPhoneNumber
								+ " with error code " + ae.getErrorCode() + " and error message " + ae.getErrorMessage());
					}
				}
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Prepaid Account with subuscriber " + pPhoneNumber + " was not retrieved with status " + accountInfo.getStatus());
				}
			}
		}
	}
	
	@Override
	public AutoTopUpInfo retrieveAutoTopUpInfo(int pBan, String pPhoneNumber) throws ApplicationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("retrieveAutoTopUpInfo with phone number " + pPhoneNumber);
		}
		Subscriber subscriber = retrieveAccountInfo(pPhoneNumber);
		RechargeableRealTimeBalance primaryBalance = getBalance(getBalanceList(subscriber), PrepaidUtils.BALANCE_TYPE_PRIMARY);
		AutoTopUpInfo autoTopUpInfo = createAutoTopUpInfoObj(primaryBalance, pBan, pPhoneNumber);
		
		return autoTopUpInfo;
	}
	
	private Subscriber retrieveAccountInfo(String phoneNumber) throws ApplicationException {
		//Log request
		logRetrieveAccountInfoReq(phoneNumber);
		
		GetDetail parameters = new GetDetail();
		parameters.setSubscriptionID(phoneNumber);
		GetDetailResponse response = pssWSClient.getDetail(parameters);
		Subscriber subscriber = response.getSubscriber();
		
		//Log response - refer to PrepaidSubscriberAccountMapper.mapToPrepaidConsumerAccountInfo
		return subscriber;
	}
	
	private void logRetrieveAccountInfoReq(String phoneNumber) {
		if (LOGGER.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("RetrieveAccountInfo");
			sb.append(" | PhoneNumber: " + phoneNumber);
			LOGGER.debug(sb.toString());
		}
	}
	

	
	@Override
	public TestPointResultInfo test() {
		return pssWSClient.test();
	}
	
	
	
	//Helper methods
	
	private void mapToPrepaidConsumerAccountInfo (PrepaidConsumerAccountInfo outAcountInfo, Subscriber inPrepaidSubscriber, String inPhoneNumber) {
		/*
		 * getPrepaidAccountInfo -> PrepaidSubscriberService.getDetails
		 * getBalance -> subscription.balances.balanceAmount (while balanceType as "primary")
		 * getReservedBalance -> subscription.balances.balanceAmount (while balanceType as "reserved")
		 * getBalanceExpiryDate -> subscription.balances.expiryDate.  (while balanceType as "primary")
		 * getLifecycleExpiryDate (set as MinimumBalanceDate) -> subscriber.individual.aliveDuring.endDateTime
		 * getBillingFrequency (set as BillingType, i.e. per "minute"/"second") -> check subscription.balances.rateProfiles. (while balanceType as "primary")
		 * getLocalRate -> check rateProfiles.rate.numerator.amount  when  rateProfiles.rateType='local'
		 * getLongDistanceRate -> check rateProfiles  when rateProfiles.rateType='ld'
		 * getOutstandingCharge -> subscription.balances.balanceAmount while Balance Type "oscharge"
		 * getMaxBalCap -> Use 300 (requested by Prepaid team).
		 * getSwipedAmount (WCoC field) -> subscription.balances.balanceAmount while Balance Type "inTrust"
		 * getInTrustBalanceExpiryDate (WCoC field) -> subscription.balances.expiryDate while Balance Type "inTrust"
		 * getThresholdCode -> subscription.balances.thresholdCode, check "primary" balance type
		 * getAutoRechargeDetail().getAmount() -> subscription.balances.automaticRechargeProfiles.rechargeAmount, 
		 * 		- rechargeType "interval" & "threshold" (if we have "threshold", we must have "interval" as well)
		 * getAutoRechargeDetail().getnextRechargeDate -> subscription.balances.automaticRechargeProfiles.nextRechargeDate (always the same for all rechargeType, pick up the one from interval type by default)
		 * getAutoRechargeDetail().getThresholdTopupIndicator -> check if subscription.balances.automaticRechargeProfiles has rechargeType is "threshold"
		 * getAutoRechargeDetail().getThresholdTopupAmount -> subscription.balances.automaticRechargeProfiles.rechargeAmount (while rechargeType is "threshold")
		 * 
		 * SuprePay retirement potential new fields (not implemented yet):
		 * getLocalRateUnit -> rateProfiles.rate.numerator.unit
		 * getLongDistanceRateUnit -> rateProfiles.rate.numerator.unit
		 * getBalanceUnit -> subscription.balances.balanceAmount.units
		 * getReservedBalanceUnit -> subscription.balances.balanceAmount.units
		 */
		

		//AccountInfo
		RechargeableRealTimeBalance primaryBalance = getBalance(getBalanceList(inPrepaidSubscriber), PrepaidUtils.BALANCE_TYPE_PRIMARY);
		RechargeableRealTimeBalance reservedBalance = getBalance(getBalanceList(inPrepaidSubscriber), PrepaidUtils.BALANCE_TYPE_RESERVED);
		RechargeableRealTimeBalance inTrustBalance = getBalance(getBalanceList(inPrepaidSubscriber), PrepaidUtils.BALANCE_TYPE_INTRUST);
		RechargeableRealTimeBalance outstandingChargeBalance = getBalance(getBalanceList(inPrepaidSubscriber), PrepaidUtils.BALANCE_TYPE_OSCHARGE);
		Individual individual = inPrepaidSubscriber.getIndividual();
		
		//PrimaryBalance
		RateProfile localRate = null;
		RateProfile ldRate = null;
		RateProfile usLdRate = null;
		AutoTopUpInfo autoTopUpInfo = null;
		if (primaryBalance != null) {
			if (primaryBalance.getBalanceAmount() != null)
				outAcountInfo.setBalance(primaryBalance.getBalanceAmount().getAmount());
			if (primaryBalance.getExpiryDate() != null)
				outAcountInfo.setBalanceExpiryDate(primaryBalance.getExpiryDate());
			outAcountInfo.setBalanceCapOrThresholdCode(StringUtils.isNotBlank(primaryBalance.getThresholdCode()) ? primaryBalance.getThresholdCode() : "");
			//Use 300 always (Prepaid team contact: Jack/Radha) - April/2014 release Surepay Retirement
			outAcountInfo.setMaximumBalanceCap(300);
			
			List<RateProfile> rateList = primaryBalance.getRateProfiles();
			//Local Rate
			localRate = getRateProfile(rateList, PrepaidUtils.RATE_PROFILE_TYPE_LOCAL);
			if (localRate != null) {
				Duration rateDenominator = getRateDenominator(localRate);
				String denominatorUnit = rateDenominator != null? rateDenominator.getUnits() : "";
				outAcountInfo.setBillingType(PrepaidUtils.BILLING_TYPE_MINUTE.equalsIgnoreCase(denominatorUnit)? PrepaidConsumerAccount.BILLING_TYPE_PER_MINUTE : PrepaidConsumerAccount.BILLING_TYPE_PER_SECOND);
				outAcountInfo.setAirtimeRate(getDollarAmount(localRate));
			}
			//Long Distance Rate
			ldRate = getRateProfile(rateList, PrepaidUtils.RATE_PROFILE_TYPE_LONG_DISTANCE);
			if (ldRate != null) {
				outAcountInfo.setLongDistanceRate(getDollarAmount(ldRate));
			}
			//US Long Distance Rate
			usLdRate = getRateProfile(rateList, PrepaidUtils.RATE_PROFILE_TYPE_US_LONG_DISTANCE);
			if (usLdRate != null) {
				outAcountInfo.setUSLongDistanceRate(getDollarAmount(usLdRate));
			}
			
		    //AutoTopUpInfo
			autoTopUpInfo = createAutoTopUpInfoObj(primaryBalance, outAcountInfo.getBanId(), inPhoneNumber);
		    outAcountInfo.setAutoTopUp(autoTopUpInfo);
		    outAcountInfo.setExistingAutoTopUp(autoTopUpInfo.isExistingAutoTopUp());

		}
		
		//Outstanding charge Balance
		if (outstandingChargeBalance != null && (outstandingChargeBalance.getBalanceAmount() != null))
			outAcountInfo.setOutstandingCharge(outstandingChargeBalance.getBalanceAmount().getAmount());
		
		//InTrustBalance
		if (inTrustBalance != null) {
			//WCoC fields
			if (inTrustBalance.getBalanceAmount() != null)
				outAcountInfo.setSwipedAmount(inTrustBalance.getBalanceAmount().getAmount());
			if (inTrustBalance.getExpiryDate() != null)
				outAcountInfo.setInTrustBalanceExpiryDate(inTrustBalance.getExpiryDate());
		}
		
		//ReservedBalance
		if (reservedBalance != null && (reservedBalance.getBalanceAmount() != null))
			outAcountInfo.setReservedBalance(reservedBalance.getBalanceAmount().getAmount());
		
		//Individual
		if (individual != null) {
			TimePeriod timePeriod  = individual.getAliveDuring();
			if (timePeriod != null && timePeriod.getEndDateTime() != null)
				outAcountInfo.setMinimumBalanceDate(timePeriod.getEndDateTime());
		}
		
		//log mapping
		logMapToPrepaidConsumerAccountInfo(inPhoneNumber, outAcountInfo);
		
	}
	
	private void logMapToPrepaidConsumerAccountInfo(String inPhoneNumber, PrepaidConsumerAccountInfo acountInfo) {
		if (LOGGER.isDebugEnabled() && acountInfo != null) {
			LOGGER.debug("MapToPrepaidConsumerAccountInfo for phone " + inPhoneNumber + ": " + acountInfo.toString());
		}
	}
	
	private AutoTopUpInfo createAutoTopUpInfoObj(RechargeableRealTimeBalance primaryBalance, int ban, String phoneNumber) {
		AutoTopUpInfo autoTopUpInfo = new AutoTopUpInfo();
	    AutomaticRechargeProfile intervalRecharge = getAutoRecharge(primaryBalance, PrepaidUtils.RECHARGE_TYPE_INTERVAL);
	    AutomaticRechargeProfile thresholdRecharge = getAutoRecharge(primaryBalance, PrepaidUtils.RECHARGE_TYPE_THRESHOLD);
	    if (intervalRecharge != null) {
	    	autoTopUpInfo.setBan(ban);
	    	autoTopUpInfo.setPhoneNumber(phoneNumber);
	    	autoTopUpInfo.setChargeAmount(intervalRecharge.getRechargeAmount());
	    	if (intervalRecharge.getNextRechargeDate() != null)
	    		autoTopUpInfo.setNextChargeDate(intervalRecharge.getNextRechargeDate());
	    	// add threshold top-up data
	    	if (thresholdRecharge != null) {
	    		autoTopUpInfo.setHasThresholdRecharge(true);
	    		autoTopUpInfo.setThresholdAmount(thresholdRecharge.getThresholdAmount());
	    	} else {
	    		autoTopUpInfo.setHasThresholdRecharge(false);
	    	}
	    	autoTopUpInfo.setIsExistingAutoTopUp(true);
	    } else {
	    	autoTopUpInfo.setBan(ban);
	    	autoTopUpInfo.setPhoneNumber(phoneNumber);
	    	autoTopUpInfo.setIsExistingAutoTopUp(false);
	    }
	    return autoTopUpInfo;
	}
	
	private AutomaticRechargeProfile getAutoRecharge(RechargeableRealTimeBalance balance, String type) {
		AutomaticRechargeProfile result = null;
		if (balance != null) {
			List<AutomaticRechargeProfile> automaticRechargeProfileList = balance.getAutomaticRechargeProfiles();
			for (AutomaticRechargeProfile autoRecharge:automaticRechargeProfileList) {
				if (autoRecharge != null && type.equalsIgnoreCase(autoRecharge.getRechargeType())) {
					result = autoRecharge;
					break;
				}
			}
		}
		return result;
	}
	
	private double getDollarAmount(RateProfile rateProfile) {
		double amount = 0;
		Money rate = getRateNumerator(rateProfile);
		if (rate != null) {
			//rate is always cent for rate profile
			if (PrepaidUtils.RATE_UNIT_CENT.equalsIgnoreCase(rate.getUnits())) {
				amount = rate.getAmount() / 100;
			} else {
				LOGGER.debug("Unexcpected rate units: " + rate.getUnits());
			}
		}
		return amount;
	}
	
	private Money getRateNumerator(RateProfile rateProfile) {
		Money result = null;
		if (rateProfile != null) {
			CostRate rate = rateProfile.getRate();
			if (rate != null) {
				result = rate.getNumerator();
			}
		}
		return result;
	}
	
	private Duration getRateDenominator(RateProfile rateProfile) {
		Duration result = null;
		if (rateProfile != null) {
			CostRate rate = rateProfile.getRate();
			if (rate != null) {
				result = rate.getDenominator();
			}
		}
		return result;
	}
	
	private RateProfile getRateProfile(List<RateProfile> rateList, String rateType) {
		RateProfile result = null;
		if (rateList != null) {
			for (RateProfile rate:rateList) {
				if (rate != null && rateType.equalsIgnoreCase(rate.getRateType())) {
					result = rate;
					break;
				}
			}
		}
		return result;
	}
	
	private List<RechargeableRealTimeBalance> getBalanceList(Subscriber inPrepaidSubscriber) {
		List<RechargeableRealTimeBalance> result = null;
		if (inPrepaidSubscriber != null) {
			Subscription subscription = inPrepaidSubscriber.getSubscription();
			if (subscription != null) {
				result = subscription.getBalances();
			}
		}
		return result;
	}
	
	private RechargeableRealTimeBalance getBalance(List<RechargeableRealTimeBalance> balanceList, String balanceType) {
		RechargeableRealTimeBalance result = null;
		if (balanceList != null) {
			for (RechargeableRealTimeBalance balance:balanceList) {
				if (balance != null && balanceType.equalsIgnoreCase(balance.getBalanceType())) {
					result = balance;
					break;
				}
			}
		}
		return result;
	}	
	
}
