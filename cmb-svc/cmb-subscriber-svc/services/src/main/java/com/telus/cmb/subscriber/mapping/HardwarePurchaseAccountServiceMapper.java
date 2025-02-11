package com.telus.cmb.subscriber.mapping;

import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.hpa.info.OfferInstanceInfo;
import com.telus.eas.hpa.info.OfferTypeInfo;
import com.telus.eas.hpa.info.RewardAccountInfo;
import com.telus.eas.hpa.info.RewardTransactionTypeInfo;
import com.telus.eas.hpa.info.TransactionCrossReferenceTypeInfo;
import com.telus.eas.utility.info.MultilingualTextInfo;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.hardwarepurchaseaccountservicerequestresponse_v1.OfferInstance;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.hardwarepurchaseaccountservicerequestresponse_v1.OfferType;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.hardwarepurchaseaccountservicerequestresponse_v1.RewardAccountType;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.hardwarepurchaseaccountservicerequestresponse_v1.RewardTransactionType;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.hardwarepurchaseaccountservicerequestresponse_v1.TransactionCrossReferenceType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Description;

/**
 * HardwarePurchaseAccountServiceMapper
 * @author R. Fong
 *
 */
public class HardwarePurchaseAccountServiceMapper {

	public static RewardAccountMapper RewardAccountMapper() {
		return RewardAccountMapper.getInstance();
	}

	public static class RewardAccountMapper extends AbstractSchemaMapper<RewardAccountType, RewardAccountInfo> {

		private static RewardAccountMapper INSTANCE;
		
		private RewardAccountMapper() {
			super(RewardAccountType.class, RewardAccountInfo.class);
		}

		protected static synchronized RewardAccountMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new RewardAccountMapper();
			}
			return INSTANCE;
		}

		@Override
		protected RewardAccountInfo performDomainMapping(RewardAccountType source, RewardAccountInfo target) {

			target.setBan(Integer.valueOf(source.getBillingAccountNumber()));
			target.setOfferCode(source.getOfferCode());
			target.setApplicationID(source.getApplicationId());
			target.setSubscriptionID(source.getSubscriptionId() != null ? source.getSubscriptionId().longValue() : 0L);
			target.setRewardAccountID(source.getRewardAccountId() != null ? source.getRewardAccountId().longValue() : 0L);
			target.setAccountBalance(source.getAccountBalance() != null ? source.getAccountBalance() : 0);
			target.setPhoneNumber(source.getPhoneNumber());
			target.setStatusCode(source.getStatusCode());
			target.setStatusDescription(source.getStatusDescription());
			if (source.getActivationDate() != null) {
				target.setActivationDate(source.getActivationDate());
			}
			if (source.getAccountDetail() != null) {
				target.setAccountDetail(OfferInstanceMapper.getInstance().mapToDomain(source.getAccountDetail()));
			}
			if (source.getRewardTransactionList() != null) {
				target.setRewardTransactionList(RewardTransactionTypeMapper.getInstance().mapToDomain(source.getRewardTransactionList()));
			}
			
			return super.performDomainMapping(source, target);
		}
	}

	public static class RewardTransactionTypeMapper extends AbstractSchemaMapper<RewardTransactionType, RewardTransactionTypeInfo> {

		private static RewardTransactionTypeMapper INSTANCE;
		
		private RewardTransactionTypeMapper() {
			super(RewardTransactionType.class, RewardTransactionTypeInfo.class);
		}

		protected static synchronized RewardTransactionTypeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new RewardTransactionTypeMapper();
			}
			return INSTANCE;
		}

		@Override
		protected RewardTransactionTypeInfo performDomainMapping(RewardTransactionType source, RewardTransactionTypeInfo target) {
			
			target.setApplicationID(source.getApplicationId());
			target.setTransactionType(source.getTransactionType());
			target.setReasonCode(source.getReasonCode());
			target.setTransactionAmount(source.getTransactionAmount());
			target.setAccountBalance(source.getAccountBalance() != null ? source.getAccountBalance() : 0);
			target.setTransactionID(source.getTransactionId() != null ? source.getTransactionId().intValue() : 0);
			target.setRewardAccountID(source.getRewardAccountId() != null ? source.getRewardAccountId().intValue() : 0);
			if (source.getTransactionDate() != null) {
				target.setTransactionDate(source.getTransactionDate());
			}
			target.setCatalogueItemID(source.getCatalogueItemId());
			target.setOfferCode(source.getOfferCode());
			target.setCreditClass(source.getCreditClass());
			if (source.getReasonCodeDescription() != null) {
				target.setReasonCodeDescriptionList(DescriptionMapper.getInstance().mapToDomain(source.getReasonCodeDescription().getDescription()));
			}
			if (source.getTransactionTypeDescription() != null) {
				target.setTransactionTypeDescriptionList(DescriptionMapper.getInstance().mapToDomain(source.getTransactionTypeDescription().getDescription()));
			}
			if (source.getCrossReference() != null) {
				target.setCrossReference(TransactionCrossReferenceTypeMapper.getInstance().mapToDomain(source.getCrossReference()));
			}
			
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class OfferInstanceMapper extends AbstractSchemaMapper<OfferInstance, OfferInstanceInfo> {

		private static OfferInstanceMapper INSTANCE;
		
		private OfferInstanceMapper() {
			super(OfferInstance.class, OfferInstanceInfo.class);
		}

		protected static synchronized OfferInstanceMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new OfferInstanceMapper();
			}
			return INSTANCE;
		}

		@Override
		protected OfferInstanceInfo performDomainMapping(OfferInstance source, OfferInstanceInfo target) {
			
			target.setOfferCode(source.getOfferCode());
			if (source.getDescription() != null) {
				target.setDescriptionList(DescriptionMapper.getInstance().mapToDomain(source.getDescription().getDescription()));
			}
			target.setLowerBalanceLimit(source.getLowerBalanceLimit());
			target.setUpperBalanceLimit(source.getUpperBalanceLimit());
			target.setMinRedeemableAmount(source.getMinRedeemableAmount());
			target.setMaxRedeemableAmount(source.getMaxRedeemableAmount());
			target.setPricePlanGroupCode(source.getPricePlanGroupCode());
			target.setAccrualRate(source.getAccrualRate());
			target.setDiscountAmount(source.getDiscountAmount());
			target.setDiscountPercentage(source.getDiscountPercentage());
			target.setDiscountCode(source.getDiscountCode());
			target.setChargeAmount(source.getChargeAmount());
			target.setChargeCode(source.getChargeCode());
			target.setMinPricePlanCost(source.getMinPricePlanCost());
			target.setChargePromoCode(source.getChargePromoCode());
			target.setChargePromoExpiryDate(source.getChargePromoExpiryDate());
			target.setChargePromotionMonthCount(source.getChargePromotionMonthCount() != null ? source.getChargePromotionMonthCount().intValue() : 0);
			target.setProductDiscountAmount(source.getProductDiscountAmount() != null ? source.getProductDiscountAmount() : 0);
			target.setOfferTypeCode(source.getOfferTypeCode());
			target.setLastBillChargeAmount(source.getLastBillChargeAmount() != null ? source.getLastBillChargeAmount() : 0);
			target.setRecoveryMonthsCount(source.getRecoveryMonthsCount() != null ? source.getRecoveryMonthsCount().intValue() : 0);
			target.setPricePlanHighTierIndicator(source.isPricePlanHighTierIndicator() != null ? source.isPricePlanHighTierIndicator() : false);
			if (source.getRedeemedOffer() != null) {
				target.setRedeemedOffer(OfferTypeMapper.getInstance().mapToDomain(source.getRedeemedOffer()));
			}

			return super.performDomainMapping(source, target);
		}
	}
	
	public static class OfferTypeMapper extends AbstractSchemaMapper<OfferType, OfferTypeInfo> {

		private static OfferTypeMapper INSTANCE;
		
		private OfferTypeMapper() {
			super(OfferType.class, OfferTypeInfo.class);
		}

		protected static synchronized OfferTypeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new OfferTypeMapper();
			}
			return INSTANCE;
		}

		@Override
		protected OfferTypeInfo performDomainMapping(OfferType source, OfferTypeInfo target) {
			
			target.setOfferID(source.getOfferId());
			target.setOfferSourceSystemID(source.getOfferSourceSystemId() != null ? source.getOfferSourceSystemId() : 0L);
			if (source.getOfferRedemptionTimestamp() != null) {
				target.setOfferRedemptionDate(source.getOfferRedemptionTimestamp());
			}
	
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class TransactionCrossReferenceTypeMapper extends AbstractSchemaMapper<TransactionCrossReferenceType, TransactionCrossReferenceTypeInfo> {

		private static TransactionCrossReferenceTypeMapper INSTANCE;
		
		private TransactionCrossReferenceTypeMapper() {
			super(TransactionCrossReferenceType.class, TransactionCrossReferenceTypeInfo.class);
		}

		protected static synchronized TransactionCrossReferenceTypeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new TransactionCrossReferenceTypeMapper();
			}
			return INSTANCE;
		}

		@Override
		protected TransactionCrossReferenceTypeInfo performDomainMapping(TransactionCrossReferenceType source, TransactionCrossReferenceTypeInfo target) {
			
			target.setApplicationCrossReferenceWorkID(source.getApplicationCrossReferenceWorkId() != null ? source.getApplicationCrossReferenceWorkId().intValue() : 0);
			target.setRewardTransactionID(source.getRewardTransactionId() != null ? source.getRewardTransactionId().intValue() : 0);
			target.setServiceRequestHeaderID(source.getServiceRequestHeaderId() != null ? source.getServiceRequestHeaderId().intValue() : 0);
			target.setApplicationID(source.getApplicationId() != null ? source.getApplicationId().intValue() : 0);
			target.setTransactionID(source.getTransactionId() != null ? source.getTransactionId().intValue() : 0);
			target.setTransactionItemID(source.getTransactionItemId() != null ? source.getTransactionItemId().intValue() : 0);
			target.setTeaTransactionTypeID(source.getTeaTransactionTypeId());
			target.setOrganisationTransactionNumber(source.getOrganisationTransactionNo());
			target.setCreateUserID(source.getCreateUserId());
			if (source.getServiceRequestTransactionTs() != null) {
				target.setServiceRequestTransactionDate(source.getServiceRequestTransactionTs());
			}
			if (source.getTransactionTs() != null) {
				target.setTransactionDate(source.getTransactionTs());
			}
	
			return super.performDomainMapping(source, target);
		}
	}
		
	public static class DescriptionMapper extends AbstractSchemaMapper<Description, MultilingualTextInfo> {

		private static DescriptionMapper INSTANCE;
		
		private DescriptionMapper() {
			super(Description.class, MultilingualTextInfo.class);
		}

		protected static synchronized DescriptionMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new DescriptionMapper();
			}
			return INSTANCE;
		}

		@Override
		protected MultilingualTextInfo performDomainMapping(Description source, MultilingualTextInfo target) {
			
			target.setLocale(source.getLocale());
			target.setText(source.getDescriptionText());
	
			return super.performDomainMapping(source, target);
		}
	}

}