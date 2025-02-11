package com.telus.cmb.subscriber.utilities.activation;

import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;
import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.DiscountPlan;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.svc.identityprofile.IdentityProfileRegistrationOrigin;
import com.telus.cmb.common.svc.identityprofile.IdentityProfileService;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.kafka.SubscriberEventPublisher;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.cmb.subscriber.utilities.contract.ContractUtilities;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.ProductEnterpriseDataInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.DiscountPlanInfo;

public class ActivationPostTask{
	
	private final static Logger LOGGER = Logger.getLogger(ActivationPostTask.class);
	
	private static final String MEMO_TYPE_HANDSET_ACTIVATION = "AOFR";
	private static final String MEMO_TYPE_CONTRACT_TERM_ACCEPTANCE = "CTAC";
	private static final String FUTURE_PATTERN_SEARCH_FEE_CHARGE_CODE = "PNUM1";
	private static final String PATTERN_SEARCH_FEE_CHARGE_CODE = "PNUM";

	private SubscriberInfo subscriberInfo;
	private SubscriberContractInfo contractInfoBeforeActivation;
	private SubscriberContractInfo contractInfoAfterActivation;

	
	private boolean portedIn;
	private String portProcessType;
	private EquipmentInfo equipmentInfo;
	private String activationFeeChargeCode;
	private String sessionId;
	private boolean overridePatternSearchFee;
	private boolean isFutureDated = false;
	private boolean activate;
	private AccountInfo lwAccount = null;
	private SubscriberInfo subscriberInfoAfterActivation;
	private AuditInfo auditInfo = null;
	private static ProductEquipmentHelper equipmentHelper = null;
	private static AccountLifecycleFacade accountFacade = null;
	private static AccountLifecycleManager accountManager = null;
	private static AccountInformationHelper accountHelper = null;
	private static SubscriberLifecycleHelper subscriberHelper = null;
	private static SubscriberLifecycleFacade subscriberFacade = null;
	private static ReferenceDataFacade refFacade = null;
	private static ReferenceDataHelper refHelper = null;
	private SubscriberEventPublisher subscriberEventPublisher= null;
	private IdentityProfileService identityProfileService= null;
	
	public ActivationPostTask(SubscriberEventPublisher subscriberEventPublisher,IdentityProfileService identityProfileService) {
		this.subscriberEventPublisher = subscriberEventPublisher;
		this.identityProfileService = identityProfileService;
	}
	

	public void initialize(SubscriberInfo subscriberInfo,SubscriberContractInfo subscriberContractInfo,boolean portedIn,String portProcessType ,
			String activationFeeChargeCode,boolean overridePatternSearchFee,boolean activate,AuditInfo auditInfo,String sessionId) throws ApplicationException {
		this.subscriberInfo = subscriberInfo;
		this.contractInfoBeforeActivation = subscriberContractInfo;
		this.portedIn=portedIn;
		this.portProcessType=portProcessType;
		this.equipmentInfo= subscriberInfo.getEquipment0();
		this.activationFeeChargeCode=activationFeeChargeCode;
		this.overridePatternSearchFee = overridePatternSearchFee;
		try {
			this.isFutureDated = (subscriberInfo.getStartServiceDate() != null && DateUtil.isAfter(subscriberInfo.getStartServiceDate(), getReferenceDataFacade().getLogicalDate()));
		} catch (Throwable t) {
			LOGGER.error("An Exception happend when retrieving the logical date for subscriberId [ "+ subscriberInfo.getSubscriberId() +" ]", t);
		}
		this.activate = activate;
		this.auditInfo = auditInfo;
		this.sessionId = sessionId;
		// Retrieve the lwAccount and activated subscriberInfo( to map subscriptionId and sub status after KB activation )
		lwAccount = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
		subscriberInfoAfterActivation = getSubscriberLifecycleHelper().retrieveSubscriberByBanAndPhoneNumber(subscriberInfo.getBanId(),subscriberInfo.getPhoneNumber());
		// set the subscriber status and subscriptionId to the current  subscriber ( before activation)  to return in web service response as a temporary work around
		// we don't want to replace complete subscriber with latest subscriber as we would need before activation subscriber data for post tasks mapping.
		subscriberInfo.setStatus(subscriberInfoAfterActivation.getStatus());
		subscriberInfo.setSubscriptionId(subscriberInfoAfterActivation.getSubscriptionId());
		contractInfoAfterActivation = getSubscriberLifecycleHelper().retrieveServiceAgreementByPhoneNumber(subscriberInfo.getPhoneNumber());
		if (equipmentInfo == null) {
			equipmentInfo = getProductEquipmentHelper().getEquipmentInfobySerialNo(subscriberInfoAfterActivation.getSerialNumber());
		}
		subscriberInfoAfterActivation.setEquipment(equipmentInfo);
	}
	
	
	

	public void apply(){
		
		LOGGER.info("begin activationPostTask apply , ban =  [ "+subscriberInfo.getBanId() +" ] , subscriberId = [ "+subscriberInfo.getSubscriberId() +" ]" );
		// publish activation event into kafka
		publishSubscriberActivationEvent();
		//register consumer profile
		registerConsumerProfile();
		//insert Customer ODS
		insertProductInstance();
		// create handsetActivationMemo
		createHandsetActivationMemo();
		// create contractTermAcceptanceMemo
		createContractTermAcceptanceMemo();
		// Note : we have identified that these two charges are not applicable in production today and should be removed when we refactor activation code in new cis stack.
		applyPatternFeeCharge();
		
		applyActivationFeeCharge();
		// apply promotional discount(s)
		applyPromotionalDiscount();
		LOGGER.info("end activationPostTask apply , ban =  [ "+subscriberInfo.getBanId() +" ] , subscriberId = [ "+subscriberInfo.getSubscriberId() +" ]" );

	}

	
	public void applyReservedSubscriberPostTasks(){
		LOGGER.info("begin reservedSubscriber activation post tasks for ban =  [ "+subscriberInfo.getBanId() +" ] , subscriberId = [ "+subscriberInfo.getSubscriberId() +" ]" );
		//register consumer profile
		registerConsumerProfile();
		//update Customer ODS
		updateProductInstance();	
		LOGGER.info("end reservedSubscriber activation post tasks for ban =  [ "+subscriberInfo.getBanId() +" ] , subscriberId = [ "+subscriberInfo.getSubscriberId() +" ]" );

	}
	
	private void publishSubscriberActivationEvent() {
		// publish kafka subscriber activation event
		try {
			if (activate) {
				subscriberEventPublisher.publishSubscriberActivationEvent(lwAccount, subscriberInfoAfterActivation,contractInfoBeforeActivation,
						portedIn, portProcessType, auditInfo,false);
		}
		}catch (Throwable t) {
			// post tasks should not cause hard stop for activation flow subscriber is already actiavted in KB DB.
			LOGGER.error("An Exception happend for publishSubscriberActivationEvent kafka call , ban = [ "+lwAccount.getBanId() +" ] , subscriberId =  [ "+ subscriberInfo.getSubscriberId() + " ]", t);
		}
	}
	
	
	private void insertProductInstance(){
		try {
			getSubscriberLifecycleFacade().asyncInsertProductInstance(lwAccount,subscriberInfoAfterActivation,equipmentInfo,contractInfoAfterActivation,ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_ACTIVATION,sessionId);
		} catch (Throwable t) {
			// post tasks should not cause hard stop for activation flow subscriber is already actiavted in KB DB.
			LOGGER.error("An Exception happend for asyncInsertProductInstance cods call , ban = [ "+lwAccount.getBanId() +" ] , subscriberId =  [ "+ subscriberInfo.getSubscriberId() + " ]", t);
		}
	}
	
	
	private void updateProductInstance()  {
		try {
			getSubscriberLifecycleFacade().asyncUpdateProductInstance(lwAccount.getBanId(),subscriberInfoAfterActivation,equipmentInfo,contractInfoAfterActivation,ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_ACTIVATION,sessionId);
		} catch (Throwable t) {
			// post tasks should not cause hard stop for activation flow subscriber is already actiavted in KB DB.
			LOGGER.error("An Exception happend for asyncUpdateProductInstance cods call , ban = [ "+lwAccount.getBanId() +" ] , subscriberId =  [ "+ subscriberInfo.getSubscriberId() + " ]", t);
		}
	}
	
	//Register the consumer profile
	private void registerConsumerProfile()  {
		try{
		identityProfileService.registerConsumerProfile(subscriberInfo, lwAccount, IdentityProfileRegistrationOrigin.SUBSCRIBER_ACTIVATION);
		} catch (Throwable t) {
		// post tasks should not cause hard stop for activation flow as subscriber is already actiavted in KB DB.
		 LOGGER.error("An Exception occurred for registerConsumerProfile ,  ban = [ "+lwAccount.getBanId() +" ] , subscriberId =  [ "+ subscriberInfo.getSubscriberId() + " ]", t);
		}
	}
	
	private void createHandsetActivationMemo()  {
		try{
		String handsetModelType = equipmentInfo.getProductType().equals(Equipment.PRODUCT_TYPE_PAGER) ? 
				equipmentInfo.getEquipmentModel() == null ? equipmentInfo.getProductName(): equipmentInfo.getEquipmentModel() : equipmentInfo.getEquipmentModel();
		StringBuilder memoText = new StringBuilder("Subscriber activated with dealer code/salesrep=").append(subscriberInfo.getDealerCode()).append("/").append(subscriberInfo.getSalesRepId()).
				append(", handset model type=").append(handsetModelType).append(" and price plan=").append(subscriberInfo.getPricePlan());

		memoText.append(ContractUtilities.genContractCallingCircleListMemoText(contractInfoBeforeActivation));
		MemoInfo memoInfo = new MemoInfo(subscriberInfo.getBanId(),MEMO_TYPE_HANDSET_ACTIVATION, subscriberInfo.getSubscriberId(),subscriberInfo.getProductType(), memoText.toString());

		getAccountLifecycleFacade().asyncCreateMemo(memoInfo, sessionId);
	} catch (Throwable t) {
		// post tasks should not cause hard stop for activation flow as subscriber is already actiavted in KB DB.
			LOGGER.error("An Exception occurred for async createHandsetActivationMemo call , ban = [ "+lwAccount.getBanId() +" ] , subscriberId =  [ "+ subscriberInfo.getSubscriberId() + " ]", t);
		}
	}

	
	private void createContractTermAcceptanceMemo() {
		try {
			if (contractInfoBeforeActivation != null && contractInfoBeforeActivation.getCommitmentMonths() > 0) {
				MemoInfo memoInfo = new MemoInfo(subscriberInfo.getBanId(),MEMO_TYPE_CONTRACT_TERM_ACCEPTANCE,subscriberInfo.getSubscriberId(),subscriberInfo.getProductType(),"Client has accepted contract terms and conditions");
			getAccountLifecycleFacade().asyncCreateMemo(memoInfo, sessionId);
			}
		} catch (Throwable t) {
			// post tasks should not cause hard stop for activation flow as subscriber is already actiavted in KB DB.
				LOGGER.error("An Exception happend for async createContractTermAcceptanceMemo, ban  = [ "+lwAccount.getBanId() +" ] , subscriberId =  [ "+ subscriberInfo.getSubscriberId() + " ]", t);
			}
	}
	
	private void applyPatternFeeCharge() {
		LOGGER.debug("overridePatternSearchFee=["+overridePatternSearchFee+"]. subscriberId=["+subscriberInfo.getSubscriberId()+"]");
		try {
			if (!overridePatternSearchFee) {
				ChargeInfo charge = new ChargeInfo();

			if (isFutureDated || !activate) {
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
			getAccountLifecycleManager().applyChargeToAccountWithOverride(charge, sessionId);
			} else {
				LOGGER.info("overridePatternSearchFee is not applicable for subscriberId=["+subscriberInfo.getSubscriberId()+"]");
			}
		} catch (Throwable t) {
			// post tasks should not cause hard stop for activation flow as subscriber is already actiavted in KB DB.
			LOGGER.error("An Exception occurred for applyPatternFeeCharge , ban = [ "+lwAccount.getBanId() +" ] , subscriberId =  [ "+ subscriberInfo.getSubscriberId() + " ]", t);
		}
	}
	
	private void applyActivationFeeCharge() {
		if (activationFeeChargeCode != null && !activationFeeChargeCode.equals("")) {
			ChargeInfo charge = new ChargeInfo();
			charge.setBan(subscriberInfo.getBanId());
			charge.setSubscriberId(subscriberInfo.getSubscriberId());
			charge.setProductType(subscriberInfo.getProductType());
			charge.setChargeCode(activationFeeChargeCode);
			charge.setText("");
			LOGGER.debug("activationFeeChargeCode=[" + activationFeeChargeCode+ "]." + charge);

			try {
				getAccountLifecycleManager().applyChargeToAccountWithOverride(charge, sessionId);
			} catch (Throwable t) {
				LOGGER.error("An Exception occurred for applyActivationFeeCharge , ban = [ "+lwAccount.getBanId() +" ] , subscriberId =  [ "+ subscriberInfo.getSubscriberId() + " ]", t);
			}
		} else {
			LOGGER.info("applyActivationFeeCharge empty for , ban = [ "+lwAccount.getBanId() +" ] , subscriberId =  [ "+ subscriberInfo.getSubscriberId() + " ]");
		}
	}

	public void applyPromotionalDiscount(){
		try {
			DiscountPlanInfo[] promotionalDiscounts = getPromotionalDiscounts();

			boolean applyDiscounts = (!isFutureDated) || (isFutureDated && (!AppConfiguration.isFdDiscountRollback()));
			if (promotionalDiscounts != null && promotionalDiscounts.length > 0 && activate && applyDiscounts) {
				for (int i = 0; i < promotionalDiscounts.length; i++) {
					if (!isDiscountHasTheSameBrand(subscriberInfo,promotionalDiscounts[i])) {
						continue;
					}

				DiscountInfo discountInfo = new DiscountInfo();
				discountInfo.setBan(subscriberInfo.getBanId());
				discountInfo.setProductType(subscriberInfo.getProductType());
				discountInfo.setDiscountCode(promotionalDiscounts[i].getCode());
				discountInfo.setForFutureSubscriberActivation(isFutureDated);
				//discountInfo.setEffectiveDate(promotionalDiscounts[i].getEffectiveDate());
				discountInfo.setExpiryDate(promotionalDiscounts[i].getOfferExpirationDate());
				Date subStartServiceDate = subscriberInfo.getStartServiceDate();
				if(subStartServiceDate != null){ //following won't work if it's future dated. However it may work if start service date is a past date.
						discountInfo.setEffectiveDate(subStartServiceDate);
						if (promotionalDiscounts[i].getOfferExpirationDate() != null) {
							long timeDifference = (subStartServiceDate.getTime() - getReferenceDataHelper().retrieveSystemDate().getTime());
							Calendar cal = Calendar.getInstance();
							cal.setTimeInMillis(promotionalDiscounts[i].getOfferExpirationDate().getTime()+ timeDifference);
							discountInfo.setExpiryDate(cal.getTime());
						}
				}                        
				
				if (promotionalDiscounts[i].getLevel().equals(DiscountPlan.DISCOUNT_PLAN_LEVEL_SUBSCRIBER)) {
					discountInfo.setSubscriberId(subscriberInfo.getSubscriberId());
				}

				if(promotionalDiscounts[i].getMonths() > 0){
					discountInfo.setExpiryDate(null);  
				}

				getAccountLifecycleManager().applyDiscountToAccount(discountInfo, sessionId);
			}
			} else{
				LOGGER.info("promotionalDiscounts is empty or not applicable for , ban = [ "+lwAccount.getBanId() +" ] , subscriberId =  [ "+ subscriberInfo.getSubscriberId() + " ]");

			}
		} catch (Throwable t) {
			// post tasks should not cause hard stop for activation flow as subscriber is already actiavted in KB DB.
			LOGGER.error("An Exception occurred for applyPromotionalDiscount , ban = [ "+lwAccount.getBanId() +" ] , subscriberId =  [ "+ subscriberInfo.getSubscriberId() + " ]", t);
		}
		
	}
	
	private DiscountPlanInfo[] getPromotionalDiscounts() throws ApplicationException {
		DiscountPlanInfo[] promotionalDiscounts = null;
		try {
			promotionalDiscounts = getReferenceDataHelper().retrieveDiscountPlans(true, subscriberInfo.getPricePlan(),subscriberInfo.getMarketProvince(),
					equipmentInfo.getProductPromoTypeList(),equipmentInfo.isInitialActivation(),contractInfoBeforeActivation.getCommitmentMonths());
		} catch (Throwable t) {
			LOGGER.error("An Exception happend when retrieving DiscountPlans for subscriberId [ "+ subscriberInfo.getSubscriberId() + " ]", t);
			ApplicationException rootException = new ApplicationException(SystemCodes.CMB_SLM_EJB, t.getMessage(), "", t);
			throw rootException;
		}

		return promotionalDiscounts;
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
	
	
	private static ProductEquipmentHelper getProductEquipmentHelper() {
		if (equipmentHelper == null) {
			equipmentHelper = EJBUtil.getHelperProxy(ProductEquipmentHelper.class,EJBUtil.TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_HELPER);
		}
		return equipmentHelper;
	}
	
	private static AccountLifecycleFacade getAccountLifecycleFacade() {
		if (accountFacade == null) {
			accountFacade = EJBUtil.getHelperProxy(AccountLifecycleFacade.class,EJBUtil.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_FACADE);
		}
		return accountFacade;
	}

	
	private static AccountInformationHelper getAccountInformationHelper() {
		if (accountHelper == null) {
			accountHelper = EJBUtil.getHelperProxy(AccountInformationHelper.class,EJBUtil.TELUS_CMBSERVICE_ACCOUNT_INFORMATION_HELPER);
		}
		return accountHelper;
	}
	
	private static AccountLifecycleManager getAccountLifecycleManager() {
		if (accountManager == null) {
			accountManager = EJBUtil.getHelperProxy(AccountLifecycleManager.class,EJBUtil.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER);
		}
		return accountManager;
	}

	private static SubscriberLifecycleHelper getSubscriberLifecycleHelper() {
		if (subscriberHelper == null) {
			subscriberHelper = EJBUtil.getHelperProxy(SubscriberLifecycleHelper.class,EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER);
		}
		return subscriberHelper;
	}
	
	
	private static SubscriberLifecycleFacade getSubscriberLifecycleFacade() {
		if (subscriberFacade == null) {
			subscriberFacade = EJBUtil.getHelperProxy(SubscriberLifecycleFacade.class,EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_FACADE);
		}
		return subscriberFacade;
	}
	
	private static ReferenceDataFacade getReferenceDataFacade() {
		if (refFacade == null) {
			refFacade = EJBUtil.getHelperProxy(ReferenceDataFacade.class,EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE);
		}
		return refFacade;
	}

	private static ReferenceDataHelper getReferenceDataHelper() {
		if (refHelper == null) {
			refHelper = EJBUtil.getHelperProxy(ReferenceDataHelper.class,EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_HELPER);
		}
		return refHelper;
	}

}
