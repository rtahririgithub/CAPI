package com.telus.cmb.subscriber.lifecyclemanager.task;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.DiscountPlan;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.subscriber.bo.SubscriberBo;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.cmb.subscriber.utilities.BaseChangeContext;
import com.telus.cmb.subscriber.utilities.contract.ContractUtilities;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.BaseChangeInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.DiscountPlanInfo;

public abstract class SubscriberTask extends BaseChangeContext<BaseChangeInfo>{


	private final static Logger LOGGER = Logger.getLogger(SubscriberTask.class);
	private static final String MEMO_TYPE_HANDSET_ACTIVATION = "AOFR";
	private static final String MEMO_TYPE_CONTRACT_TERM_ACCEPTANCE = "CTAC";
	private static final String FUTURE_PATTERN_SEARCH_FEE_CHARGE_CODE = "PNUM1";
	private static final String PATTERN_SEARCH_FEE_CHARGE_CODE = "PNUM";
	private SubscriberInfo subscriberInfo;
	private SubscriberContractInfo subscriberContractInfo;
	private EquipmentInfo equipmentInfo;
	private boolean activate;
	private boolean overridePatternSearchFee;
	private String activationFeeChargeCode;
	private Date startServiceDate;
	
	public SubscriberTask(BaseChangeInfo changeInfo) throws SystemException, ApplicationException {
		super(changeInfo);
	}
	
	abstract public void execute() throws ApplicationException;
	
	@Override
	public void initialize() throws SystemException, ApplicationException {
		BaseChangeInfo changeInfo = getChangeInfo();
		
		if (changeInfo.getCurrentSubscriberInfo() != null) {
			currentSubscriber = new SubscriberBo(changeInfo.getCurrentSubscriberInfo(), this);
		}
		
		subscriberInfo = changeInfo.getCurrentSubscriberInfo();
		subscriberContractInfo = changeInfo.getCurrentContractInfo();
		equipmentInfo = changeInfo.getCurrentEquipmentInfo();
	}



	protected EquipmentInfo getEquipmentInfo() throws ApplicationException {
		if (equipmentInfo == null) {
			equipmentInfo = getCurrentEquipment().getDelegate();
		}

		return equipmentInfo;
	}
	
	protected DiscountPlanInfo[] getPromotionalDiscounts() throws ApplicationException {	
		DiscountPlanInfo[] promotionalDiscounts;
		EquipmentInfo equipmentInfo = getEquipmentInfo();
		
		try {
		promotionalDiscounts = getRefDataHelper().retrieveDiscountPlans(true
					, subscriberInfo.getPricePlan(), subscriberInfo.getMarketProvince()
					, equipmentInfo.getProductPromoTypeList(), equipmentInfo.isInitialActivation()
					, subscriberContractInfo.getCommitmentMonths());
		}catch (Throwable t) {
			ApplicationException rootException = new ApplicationException(SystemCodes.CMB_SLM_EJB, t.getMessage(), "", t);
			throw rootException;
		}
		
		
		return promotionalDiscounts;
	}
	
	
	public void createHandsetActivationMemo() throws ApplicationException {
		EquipmentInfo equipmentInfo = getEquipmentInfo();
		
		String handsetModelType = equipmentInfo.getProductType().equals(Equipment.PRODUCT_TYPE_PAGER) ? equipmentInfo.getEquipmentModel() == null ? equipmentInfo.getProductName() : equipmentInfo.getEquipmentModel() : equipmentInfo.getEquipmentModel();
		StringBuilder memoText = new StringBuilder("Subscriber activated with dealer code/salesrep=").append(subscriberInfo.getDealerCode()).append("/").append(subscriberInfo.getSalesRepId())
			.append(", handset model type=").append(handsetModelType)
			.append(" and price plan=").append(subscriberInfo.getPricePlan());
		
		memoText.append( ContractUtilities.genContractCallingCircleListMemoText(subscriberContractInfo) );
		

		MemoInfo memoInfo = new MemoInfo(subscriberInfo.getBanId()
					, MEMO_TYPE_HANDSET_ACTIVATION
					, subscriberInfo.getSubscriberId()
					, subscriberInfo.getProductType(), memoText.toString());
		getAccountLifecycleFacade().asyncCreateMemo(memoInfo, getAccountLifecycleFacadeSessionId());
	}
	
	public void createContractTermAcceptanceMemo() throws ApplicationException {	
		if (subscriberContractInfo != null && subscriberContractInfo.getCommitmentMonths() > 0) {
			MemoInfo memoInfo = new MemoInfo(subscriberInfo.getBanId(), MEMO_TYPE_CONTRACT_TERM_ACCEPTANCE, subscriberInfo.getSubscriberId(), subscriberInfo.getProductType(),
					"Client has accepted contract terms and conditions");

			getAccountLifecycleFacade().asyncCreateMemo(memoInfo, getAccountLifecycleFacadeSessionId());
		}
	}
	
	public void applyPatternFeeCharge() throws ApplicationException {
		LOGGER.debug("overridePatternSearchFee=["+overridePatternSearchFee+"]. subscriberId=["+subscriberInfo.getSubscriberId()+"]");
		
		if (!overridePatternSearchFee){
			ChargeInfo charge = new ChargeInfo();
			if (isFutureDated(startServiceDate) || !activate) {
				charge.setChargeCode(FUTURE_PATTERN_SEARCH_FEE_CHARGE_CODE);
			} else {
				charge.setChargeCode(PATTERN_SEARCH_FEE_CHARGE_CODE);
				charge.setSubscriberId(subscriberInfo.getSubscriberId());
				charge.setProductType(subscriberInfo.getProductType());
			}
			charge.setBan(subscriberInfo.getBanId());
			charge.setText("");
			LOGGER.debug(charge);

			// subscriber level charge cannot be applied to subscriber in 'reserve' status
			getAccountLifecycleManager().applyChargeToAccountWithOverride(charge, getAccountLifecycleManagerSessionId());
		}
	}
	
	public void applyActivationFeeCharge() throws ApplicationException {
		if (activationFeeChargeCode != null && !activationFeeChargeCode.equals("")){
			ChargeInfo charge = new ChargeInfo();
			charge.setBan(subscriberInfo.getBanId());
			charge.setSubscriberId(subscriberInfo.getSubscriberId());
			charge.setProductType(subscriberInfo.getProductType());
			charge.setChargeCode(activationFeeChargeCode);
			charge.setText("");
			LOGGER.debug("activationFeeChargeCode=["+activationFeeChargeCode+"]."+charge);

			try {
				getAccountLifecycleManager().applyChargeToAccountWithOverride(charge, getAccountLifecycleManagerSessionId());
			} catch(Throwable t) {
				LOGGER.warn("An Exception happend when applying activation fee charge - " + t.getMessage());
				throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.CREATE_SUBSCRIBER_ERROR_FEES_DISCOUNTS
						, "SubscriberManager createSubscriber() created the subscriber but did not complete the following steps: " +
						"Fee application, Discount application"
						, "", t);
			}
		}else {
			if (subscriberInfo != null) {
				LOGGER.debug("no activationFeeChargeCode for "+subscriberInfo.getSubscriberId());
			}
		}
	}
	
	public void applyPromotionalDiscount() throws ApplicationException {
		DiscountPlanInfo[] promotionalDiscounts = getPromotionalDiscounts();
		boolean isForFutureDatedActivation = isFutureDated(subscriberInfo.getStartServiceDate());
		boolean applyDiscounts = (!isForFutureDatedActivation) || (isForFutureDatedActivation && (!AppConfiguration.isFdDiscountRollback()));
		
		
		if (promotionalDiscounts != null && promotionalDiscounts.length > 0 && activate && applyDiscounts){ // && isFutureDatedTransaction == false) {
			for (int i = 0; i < promotionalDiscounts.length; i++) {
				if ( !isDiscountHasTheSameBrand (subscriberInfo, promotionalDiscounts[i])) {
					continue;
				}

				DiscountInfo discountInfo = new DiscountInfo();
				discountInfo.setBan(subscriberInfo.getBanId());
				discountInfo.setProductType(subscriberInfo.getProductType());
				discountInfo.setDiscountCode(promotionalDiscounts[i].getCode());
				discountInfo.setForFutureSubscriberActivation(isForFutureDatedActivation);
				//discountInfo.setEffectiveDate(promotionalDiscounts[i].getEffectiveDate());
				discountInfo.setExpiryDate(promotionalDiscounts[i].getOfferExpirationDate());
				Date subStartServiceDate = subscriberInfo.getStartServiceDate();
				if(subStartServiceDate != null){ //following won't work if it's future dated. However it may work if start service date is a past date.
					discountInfo.setEffectiveDate(subStartServiceDate);

					if(promotionalDiscounts[i].getOfferExpirationDate() != null){
						long timeDifference = (subStartServiceDate.getTime() - getSystemDate().getTime());
						Calendar cal = Calendar.getInstance();

						cal.setTimeInMillis(promotionalDiscounts[i].getOfferExpirationDate().getTime() + timeDifference);
						discountInfo.setExpiryDate(cal.getTime());    
					}


				}                        
				if (promotionalDiscounts[i].getLevel().equals(DiscountPlan.DISCOUNT_PLAN_LEVEL_SUBSCRIBER)) {
					discountInfo.setSubscriberId(subscriberInfo.getSubscriberId());
				}

				if(promotionalDiscounts[i].getMonths() > 0){
					discountInfo.setExpiryDate(null);  
				}

				getAccountLifecycleManager().applyDiscountToAccount(discountInfo, getAccountLifecycleManagerSessionId());
			}
		}
	}
	
	private boolean isDiscountHasTheSameBrand (SubscriberInfo subscriberInfo, DiscountPlanInfo promotionalDiscount){
		int subBrandId = subscriberInfo.getBrandId();
		boolean found = false;

		if (subBrandId == 0) {
			found = true;
		} else {
			int[] discountBrandIDs =  promotionalDiscount.getDiscountBrandIDs();
			if (discountBrandIDs == null || discountBrandIDs.length == 0 || 
					(discountBrandIDs.length == 1 && discountBrandIDs[0] == 0 ))
				found = true;
			else {
				for (int j = 0; j < discountBrandIDs.length; j++) {
					if (discountBrandIDs[j] == subBrandId){
						found = true;
						break;
					}
				}
			}
		}

		return found;
	}
	
	protected boolean isFutureDated(Date dateToCheck) throws ApplicationException {
		if (dateToCheck != null) {
			return DateUtil.isAfter(dateToCheck, getLogicalDate());
		}
		
		return false;
	}

	public boolean isActivate() {
		return activate;
	}

	public void setActivate(boolean activate) {
		this.activate = activate;
	}

	public boolean isOverridePatternSearchFee() {
		return overridePatternSearchFee;
	}

	public void setOverridePatternSearchFee(boolean overridePatternSearchFee) {
		this.overridePatternSearchFee = overridePatternSearchFee;
	}

	public String getActivationFeeChargeCode() {
		return activationFeeChargeCode;
	}

	public void setActivationFeeChargeCode(String activationFeeChargeCode) {
		this.activationFeeChargeCode = activationFeeChargeCode;
	}

	public Date getStartServiceDate() {
		return startServiceDate;
	}

	public void setStartServiceDate(Date startServiceDate) {
		this.startServiceDate = startServiceDate;
	}
	
	
}
