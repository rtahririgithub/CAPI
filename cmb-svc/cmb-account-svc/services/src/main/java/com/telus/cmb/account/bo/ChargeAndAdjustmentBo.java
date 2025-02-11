package com.telus.cmb.account.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.api.account.Account;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.ChargeType;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.framework.info.ChargeAdjustmentCodeInfo;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;

public class ChargeAndAdjustmentBo {
	private int ban;
	private String subscriberId;
	private String phoneNumber;
	private Date searchFromDate;
	private Date searchToDate;
	private String adjustmentMemoText;
	private String productType;
	private boolean bypassAuthorizationInd;
	private boolean overrideThresholdInd;
	private ChargeInfo[] charges = null;
	private Map<String,ChargeAdjustmentInfo> resultMap =  new HashMap<String,ChargeAdjustmentInfo>(); 
	private ChargeAdjustmentCodeInfo chargeAdjustmentCodeInfo = null;
	private AccountInformationHelper accountInformationHelper;
	private AccountLifecycleManager accountLifecycleManager;
	private String newSubscriberId = null;
	private boolean subscriberChangeChecked = false;
	
	public ChargeAndAdjustmentBo (AccountLifecycleManager accountLifecycleManager, AccountInformationHelper accountInformationHelper) {
		this.accountLifecycleManager = accountLifecycleManager;
		this.accountInformationHelper = accountInformationHelper;
	}
		
	public void initialize(ChargeAdjustmentCodeInfo chargeAdjustmentInfo) {
		this.chargeAdjustmentCodeInfo = chargeAdjustmentInfo;
		this.charges = null;
	}
	
	public void clear() {
		this.accountLifecycleManager = null;
		this.accountInformationHelper = null;
	}
	
	public void retrieveCharges() throws ApplicationException {
		retrieveCharges(subscriberId);
	}
	
	private void retrieveCharges(String actualSubscriberId) throws ApplicationException {
		charges = accountInformationHelper.retrieveCharges(ban, new String[]{chargeAdjustmentCodeInfo.getChargeCode()}, Account.BILL_STATE_ALL, ChargeType.CHARGE_LEVEL_SUBSCRIBER, actualSubscriberId, searchFromDate, searchToDate,2);
	}
	
	public void performCTNChangeSearchIfNecessary() throws ApplicationException {
		if (charges != null && charges.length == 0) {
			if (Subscriber.PRODUCT_TYPE_IDEN.equals(productType) == false) {
				performSubscriberChangeSearch();
				if (newSubscriberId != null) {
					retrieveCharges(newSubscriberId);
				}
			}
		}
	}
	
	public void applyChargesAndAdjustments(String accountLifecycleManagerSessionId) throws ApplicationException {
		String chargeCode = chargeAdjustmentCodeInfo.getChargeCode();
		if (charges != null) {
			if (charges.length == 0) {
				resultMap.put(chargeCode, createChargeAdjustmentError("Charge not found"));
			}else if (charges.length == 1 ) {
				searchAndApplyAdjustment(charges[0], accountLifecycleManagerSessionId);
			}else {
				resultMap.put(chargeCode, createChargeAdjustmentError("Multiple charges found for Charge ="+chargeCode+ " BAN ="+ban+" Subscriber ="+subscriberId));
			}
		}
	}
	
	private ChargeAdjustmentInfo createChargeAdjustmentError (String errorMessage) {
		ChargeAdjustmentInfo info = new ChargeAdjustmentInfo();
		info.setChargeCode(chargeAdjustmentCodeInfo.getChargeCode());
		info.setAdjustmentReasonCode(chargeAdjustmentCodeInfo.getAdjustmentReasonCode());
		info.setErrorCode(errorMessage);
		info.setErrorMessage(errorMessage);
		return info;
	}
	
	private ChargeAdjustmentInfo createChargeAdjustmentError (Throwable t) {
		ChargeAdjustmentInfo info = new ChargeAdjustmentInfo();
		info.setBan(ban);
		info.setChargeCode(chargeAdjustmentCodeInfo.getChargeCode());
		info.setAdjustmentReasonCode(chargeAdjustmentCodeInfo.getAdjustmentReasonCode());
		if (t instanceof ApplicationException) {
			ApplicationException ae = (ApplicationException) t;
			info.setErrorCode(ae.getErrorCode());
			info.setErrorMessage(ae.getErrorMessage());
//			info.setExceptionTrace(ae.getStackTraceAsString());
		}else if (t instanceof SystemException) {
			SystemException se = (SystemException) t;
			info.setErrorCode(se.getErrorCode());
			info.setErrorMessage(se.getErrorMessage());
//			info.setExceptionTrace(se.getStackTraceAsString());
		}else {
			info.setErrorCode("");
			info.setErrorMessage(t.getMessage());
		}
			
		return info;
	}
	
	private void performSubscriberChangeSearch() throws ApplicationException{
		if (AppConfiguration.isCTNChangeSearchEnabledForHandsetReturn() && subscriberChangeChecked == false) {
			newSubscriberId = accountInformationHelper.retrieveChangedSubscriber(ban, subscriberId, productType, searchFromDate, searchToDate);
			
		}
		
		subscriberChangeChecked = true;
	}
	
	@SuppressWarnings("unchecked")
	private void searchAndApplyAdjustment(ChargeInfo chargeInfo, String sessionId) throws ApplicationException {
		double chargeAmount = chargeInfo.getAmount();
		String adjustmentReasonCode = chargeAdjustmentCodeInfo.getAdjustmentReasonCode();
		String actualSubscriberId = (newSubscriberId != null ? newSubscriberId : subscriberId);
		ChargeAdjustmentInfo chargeAdjustmentInfo;
//		SearchResultsInfo searchResultInfo =  accountInformationHelper.retrieveCredits(ban, searchFromDate, searchToDate, Account.BILL_STATE_ALL, "", adjustmentReasonCode, ChargeType.CHARGE_LEVEL_SUBSCRIBER, actualSubscriberId, 2);
//		List<CreditInfo> adjustmentList = 	Arrays.asList((CreditInfo[])searchResultInfo.getItems());
		List<Double> adjustmentList = accountInformationHelper.retrieveAdjustedAmounts(ban, adjustmentReasonCode, actualSubscriberId, searchFromDate, searchToDate);
		if (adjustmentList.size() == 0) {
			chargeAdjustmentInfo = doFullAdjustmentOnCharge(chargeInfo, sessionId);
		} else if (adjustmentList.size() == 1) {
			double previouslyAdjustedAmount = adjustmentList.get(0).doubleValue();
//			double previouslyAdjustedAmount = adjustmentList.get(0).getAmount();
			if (wasPartialAdjustedPreviously(chargeAmount, previouslyAdjustedAmount)) {
				chargeAdjustmentInfo = adjustUsingAvailableAdjustmentRoom(chargeInfo, previouslyAdjustedAmount, sessionId);
			} else {
				chargeAdjustmentInfo = applyChargeAndAdjustment(previouslyAdjustedAmount, chargeAmount, chargeInfo.getText(), sessionId);
			}
		} else {
			chargeAdjustmentInfo = createChargeAdjustmentError("Multiple Adjustments found for adjustmentReasonCode = " + adjustmentReasonCode + " BAN =" + ban + " Subscriber ="
					+ subscriberId);
		}
		
		resultMap.put(chargeAdjustmentCodeInfo.getChargeCode(), chargeAdjustmentInfo);
	}
	
	private boolean wasPartialAdjustedPreviously (double chargeAmount, double adjustmentAmount) {
		return adjustmentAmount < chargeAmount;
	}
	
	private double availableAdjustmentRoom (double chargeAmount, double adjustmentAmount) {
		return chargeAmount - adjustmentAmount;
	}
	
	private ChargeAdjustmentInfo doFullAdjustmentOnCharge (ChargeInfo chargeInfo, String sessionId) {
		return doAdjustmentOnChargeOnly(chargeInfo, chargeInfo.getAmount(), sessionId);
	}
	
	private ChargeAdjustmentInfo adjustUsingAvailableAdjustmentRoom (ChargeInfo chargeInfo, double adjustmentAmount, String sessionId) {
		adjustmentAmount = availableAdjustmentRoom(chargeInfo.getAmount(), adjustmentAmount);
		return doAdjustmentOnChargeOnly(chargeInfo, adjustmentAmount, sessionId);
	}
	
	private ChargeAdjustmentInfo doAdjustmentOnChargeOnly(ChargeInfo chargeInfo, double adjustmentAmount, String sessionId)  {
		double adjustmentId;
		String adjustmentReasonCode = chargeAdjustmentCodeInfo.getAdjustmentReasonCode();

		try {
			if (overrideThresholdInd) {
				adjustmentId = accountLifecycleManager.adjustChargeWithOverride(chargeInfo, adjustmentAmount, adjustmentReasonCode, adjustmentMemoText, sessionId);
			} else {
				adjustmentId = accountLifecycleManager.adjustCharge(chargeInfo, adjustmentAmount, adjustmentReasonCode, adjustmentMemoText, sessionId);
			}
		} catch (Throwable t) {
			return createChargeAdjustmentError(t);
		}
		
		
		return createChargeAdjustmentInfoForAdjustment(adjustmentAmount, adjustmentId); 
	}
	
	@SuppressWarnings("unchecked")
	private ChargeAdjustmentInfo applyChargeAndAdjustment(double chargeAmount, double adjustmentAmount, String chargeMemoText, String accountLifecycleManagerSessionId) {
		ChargeAdjustmentInfo chargeAdjustmentInfo = createChargeAdjustmentInfoForChargeAndAdjustment(chargeAmount, adjustmentAmount, chargeMemoText);
		List<ChargeAdjustmentInfo> applyChargeAdjustmentList = new ArrayList<ChargeAdjustmentInfo>();
		applyChargeAdjustmentList.add(chargeAdjustmentInfo);

		try {
			List<ChargeAdjustmentInfo> chargeAdjustmentInfoList = accountLifecycleManager.applyChargesAndAdjustmentsToAccountForSubscriber(applyChargeAdjustmentList, accountLifecycleManagerSessionId);
			ChargeAdjustmentInfo info = chargeAdjustmentInfoList.get(0);

			if (info.isChargeApplied()) {
				chargeAdjustmentInfo.setChargeSequenceNumber(info.getChargeSequenceNumber());
				chargeAdjustmentInfo.setAdjustmentId(info.getAdjustmentId());
				return chargeAdjustmentInfo;
			} else {
				return info;
			}
		} catch (Throwable t) {
			return createChargeAdjustmentError(t);
		}
	}
	
	private ChargeAdjustmentInfo createChargeAdjustmentInfoForAdjustment (double adjustmentAmount, double adjustmentId){
		ChargeAdjustmentInfo info = new ChargeAdjustmentInfo();

		info.setBan(ban);
		info.setSubscriberId(subscriberId);
		info.setChargeCode(chargeAdjustmentCodeInfo.getChargeCode());
		info.setChargeEffectiveDate(new Date());
		info.setAdjustmentAmount(adjustmentAmount);
		info.setAdjustmentReasonCode(chargeAdjustmentCodeInfo.getAdjustmentReasonCode());
		info.setAdjustmentMemoText(adjustmentMemoText);
		info.setAdjustmentId(adjustmentId);
		info.setBypassAuthorization(bypassAuthorizationInd);
		info.setAuthorizedToCreateFollowUp(true);
		info.setProductType(productType);
		return info;
	}

	private ChargeAdjustmentInfo createChargeAdjustmentInfoForChargeAndAdjustment (double chargeAmount, double adjustmentAmount, String chargeMemoText){
		ChargeAdjustmentInfo info = new ChargeAdjustmentInfo();

		info.setBan(ban);
		info.setSubscriberId(subscriberId);
		info.setChargeCode(chargeAdjustmentCodeInfo.getChargeCode());
		info.setChargeEffectiveDate(new Date());
		info.setAdjustmentAmount(adjustmentAmount);
		info.setChargeAmount(chargeAmount);
		info.setAdjustmentReasonCode(chargeAdjustmentCodeInfo.getAdjustmentReasonCode());
		info.setAdjustmentMemoText(adjustmentMemoText);
		info.setChargeMemoText(chargeMemoText);
		info.setBypassAuthorization(bypassAuthorizationInd);
		info.setAuthorizedToCreateFollowUp(true);
		info.setProductType(productType);
		return info;
	}

	public int getBan() {
		return ban;
	}

	public void setBan(int ban) {
		this.ban = ban;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getSearchFromDate() {
		return searchFromDate;
	}

	public void setSearchFromDate(Date searchFromDate) {
		this.searchFromDate = searchFromDate;
	}

	public Date getSearchToDate() {
		return searchToDate;
	}

	public void setSearchToDate(Date searchToDate) {
		this.searchToDate = searchToDate;
	}

	public String getAdjustmentMemoText() {
		return adjustmentMemoText;
	}

	public void setAdjustmentMemoText(String adjustmentMemoText) {
		this.adjustmentMemoText = adjustmentMemoText;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public boolean isBypassAuthorizationInd() {
		return bypassAuthorizationInd;
	}

	public void setBypassAuthorizationInd(boolean bypassAuthorizationInd) {
		this.bypassAuthorizationInd = bypassAuthorizationInd;
	}

	public boolean isOverrideThresholdInd() {
		return overrideThresholdInd;
	}

	public void setOverrideThresholdInd(boolean overrideThresholdInd) {
		this.overrideThresholdInd = overrideThresholdInd;
	}

	public Map<String, ChargeAdjustmentInfo> getResultMap() {
		return resultMap;
	}

	public ChargeAdjustmentCodeInfo getChargeAdjustmentCodeInfo() {
		return chargeAdjustmentCodeInfo;
	}

	public void setChargeAdjustmentCodeInfo(ChargeAdjustmentCodeInfo chargeAdjustmentCodeInfo) {
		this.chargeAdjustmentCodeInfo = chargeAdjustmentCodeInfo;
	}

	
}
