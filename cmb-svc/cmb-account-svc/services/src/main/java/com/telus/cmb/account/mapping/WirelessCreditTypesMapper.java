package com.telus.cmb.account.mapping;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.cmb.common.mapping.EnterpriseCommonTypesV9Mapper;
import com.telus.eas.account.credit.info.CreditBureauDocumentInfo;
import com.telus.eas.account.credit.info.CreditProgramInfo;
import com.telus.eas.account.credit.info.CreditWarningInfo;
import com.telus.eas.account.credit.info.CreditWorthinessInfo;
import com.telus.eas.account.credit.info.DevicePaymentPlanInfo;
import com.telus.eas.account.credit.info.DevicePaymentThresholdInfo;
import com.telus.eas.account.credit.info.SubscriberEligibilityInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.BaseCreditProgram;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.BaseDevicePaymentPlanAbstract;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.ByodDevicePaymentPlan;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.ClpCreditProgram;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.CreditBureauDocument;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.CreditWorthiness;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.DeclinedCreditProgram;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.DepositCreditProgram;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.DevicePaymentPlanThresholdList;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.FinancingDevicePaymentPlan;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.NoDeviceCreditProgram;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.ProductDeposit;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.SubscriberEligibilityResult;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.SubsidyDevicePaymentPlan;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.TabDevicePaymentPlan;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.WarningHistoryBase;

/**
 * WirelessCreditTypesMapper
 * 
 * @author R. Fong
 *
 */
public class WirelessCreditTypesMapper {

	public static CreditWorthinessMapper CreditWorthinessMapper() {
		return CreditWorthinessMapper.getInstance();
	}

	public static CreditProgramMapper CreditProgramMapper() {
		return CreditProgramMapper.getInstance();
	}

	public static CreditWarningMapper CreditWarningMapper() {
		return CreditWarningMapper.getInstance();
	}

	public static CreditBureauDocumentMapper CreditBureauDocumentMapper() {
		return CreditBureauDocumentMapper.getInstance();
	}

	public static SubscriberEligibilityMapper SubscriberEligibilityMapper() {
		return SubscriberEligibilityMapper.getInstance();
	}

	public static DevicePaymentPlanEligibilityMapper DevicePaymentPlanEligibilityMapper() {
		return DevicePaymentPlanEligibilityMapper.getInstance();
	}

	public static DevicePaymentThresholdMapper DevicePaymentThresholdMapper() {
		return DevicePaymentThresholdMapper.getInstance();
	}

	public static class CreditWorthinessMapper extends AbstractSchemaMapper<CreditWorthiness, CreditWorthinessInfo> {

		private static CreditWorthinessMapper INSTANCE = null;

		private CreditWorthinessMapper() {
			super(CreditWorthiness.class, CreditWorthinessInfo.class);
		}

		protected synchronized static CreditWorthinessMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditWorthinessMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditWorthinessInfo performDomainMapping(CreditWorthiness source, CreditWorthinessInfo target) {

			target.setCreditWorthinessID(source.getCreditWorthinessId() != null ? source.getCreditWorthinessId() : 0L);
			target.setCreditProfileID(source.getCreditProfileId() != null ? source.getCreditProfileId() : 0L);
			target.setCreditAssessmentID(source.getCreditAssessmentId() != null ? source.getCreditAssessmentId() : 0L);
			target.setPrimaryCreditScoreCode(source.getPrimaryCreditScoreCd());
			target.setPrimaryCreditScoreTypeCode(source.getPrimaryCreditScoreTypeCd());
			target.setBureauDecisionCode(EnterpriseCommonTypesV9Mapper.CodeDescriptionMapper().mapToDomain(source.getBureauDecisionCode()));
			target.setCreditProgram(CreditProgramMapper().mapToDomain(source.getCreditProgram()));

			return super.performDomainMapping(source, target);
		}

	}

	public static class CreditProgramMapper extends AbstractSchemaMapper<BaseCreditProgram, CreditProgramInfo> {

		private static CreditProgramMapper INSTANCE = null;

		private CreditProgramMapper() {
			super(BaseCreditProgram.class, CreditProgramInfo.class);
		}

		protected synchronized static CreditProgramMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditProgramMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditProgramInfo performDomainMapping(BaseCreditProgram source, CreditProgramInfo target) {

			target.setCreditProgramName(source.getCreditProgramName());
			target.setCreditClassCode(source.getCreditClassCd());
			target.setCreditClassDate(source.getCreditClassDate());
			target.setCreditDecisionCode(source.getCreditDecisionCd());
			target.setCreditDecisionDate(source.getCreditDecisionDate());
			target.setRiskLevelNumber(source.getRiskLevelNumber() != null ? source.getRiskLevelNumber() : 0);
			target.setRiskLevelDecisionCode(source.getRiskLevelDecisionCd());
			target.setRiskLevelDate(source.getRiskLevelDt());
			if (source instanceof ClpCreditProgram) {
				mapCreditLimitCreditProgram((ClpCreditProgram) source, target);
			} else if (source instanceof DepositCreditProgram) {
				mapDepositCreditProgram((DepositCreditProgram) source, target);
			} else if (source instanceof DeclinedCreditProgram) {
				mapDeclinedCreditProgram((DeclinedCreditProgram) source, target);
			} else if (source instanceof NoDeviceCreditProgram) {
				mapNoDeviceCreditProgram((NoDeviceCreditProgram) source, target);
			}

			return super.performDomainMapping(source, target);
		}

		private void mapCreditLimitCreditProgram(ClpCreditProgram source, CreditProgramInfo target) {
			target.setCreditProgramType(CreditProgramInfo.TYPE_CLP);
			target.setCLPContractTerm(source.getClpContractTermNum() != null ? source.getClpContractTermNum() : 0);
			target.setCLPCreditLimitAmount(source.getClpCreditLimitAmount() != null ? source.getClpCreditLimitAmount().doubleValue() : 0);
			target.setCLPRatePlanAmount(source.getClpRatePlanAmount() != null ? source.getClpRatePlanAmount().doubleValue() : 0);
		}

		private void mapDepositCreditProgram(DepositCreditProgram source, CreditProgramInfo target) {
			target.setCreditProgramType(CreditProgramInfo.TYPE_DEPOSIT);
			mapDeposits(source, target);
		}

		private void mapDeclinedCreditProgram(DeclinedCreditProgram source, CreditProgramInfo target) {
			target.setCreditProgramType(CreditProgramInfo.TYPE_DECLINED);
			mapDeposits(source, target);
		}

		private void mapNoDeviceCreditProgram(NoDeviceCreditProgram source, CreditProgramInfo target) {
			target.setCreditProgramType(CreditProgramInfo.TYPE_NDP);
			mapDeposits(source, target);
		}
		
		private void mapDeposits(DepositCreditProgram source, CreditProgramInfo target) {
			List<ProductDeposit> productDepositList = source.getAverageSecurityDepositList();
			target.setSecurityDepositAmount(CollectionUtils.isNotEmpty(productDepositList) && productDepositList.get(0) != null && productDepositList.get(0).getDepositAmt() != null
					? (productDepositList.get(0).getDepositAmt().doubleValue()) : 0);
		}

	}

	public static class CreditWarningMapper extends AbstractSchemaMapper<WarningHistoryBase, CreditWarningInfo> {

		private static CreditWarningMapper INSTANCE = null;

		private CreditWarningMapper() {
			super(WarningHistoryBase.class, CreditWarningInfo.class);
		}

		protected synchronized static CreditWarningMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditWarningMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditWarningInfo performDomainMapping(WarningHistoryBase source, CreditWarningInfo target) {

			target.setCategoryCode(source.getWarningCategoryCd());
			target.setTypeCode(source.getWarningTypeCd());
			target.setCode(source.getWarningCd());
			target.setItemTypeCode(source.getWarningItemTypeCd());
			target.setDetectionDate(source.getWarningDetectionDate());
			target.setStatusCode(source.getWarningStatusCd());

			return super.performDomainMapping(source, target);
		}

	}

	public static class CreditBureauDocumentMapper extends AbstractSchemaMapper<CreditBureauDocument, CreditBureauDocumentInfo> {

		private static CreditBureauDocumentMapper INSTANCE = null;

		private CreditBureauDocumentMapper() {
			super(CreditBureauDocument.class, CreditBureauDocumentInfo.class);
		}

		protected synchronized static CreditBureauDocumentMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditBureauDocumentMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditBureauDocumentInfo performDomainMapping(CreditBureauDocument source, CreditBureauDocumentInfo target) {

			target.setDocumentID(source.getDocumentId());
			target.setDocumentTypeCode(source.getDocumentTypeCd());
			target.setDocumentPathText(source.getDocumentPathTxt());
			if (source.getDocumentContentBinary() != null) {
				// ISO8859_1 is required to preserve French characters when doing decoding
				target.setDocument(new String(source.getDocumentContentBinary(), CreditBureauDocumentInfo.charsetForDecoding));
			}

			return super.performDomainMapping(source, target);
		}

	}

	public static class SubscriberEligibilityMapper extends AbstractSchemaMapper<SubscriberEligibilityResult, SubscriberEligibilityInfo> {

		private static SubscriberEligibilityMapper INSTANCE = null;

		private SubscriberEligibilityMapper() {
			super(SubscriberEligibilityResult.class, SubscriberEligibilityInfo.class);
		}

		protected synchronized static SubscriberEligibilityMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new SubscriberEligibilityMapper();
			}
			return INSTANCE;
		}

		@Override
		protected SubscriberEligibilityInfo performDomainMapping(SubscriberEligibilityResult source, SubscriberEligibilityInfo target) {

			target.setSubscriberOrdinalNumber(source.getSubscriberOrdinalNum());
			target.setSubscriberID(source.getSubscriberId());
			target.setSubscriptionID(Long.parseLong(StringUtils.defaultIfEmpty(source.getSubscriptionId(), "0")));
			target.setDevicePaymentPlanEligibilityList(DevicePaymentPlanEligibilityMapper().mapToDomain(source.getDevicePaymentPlanEligibilityResultList()));

			return super.performDomainMapping(source, target);
		}

	}

	public static class DevicePaymentPlanEligibilityMapper extends AbstractSchemaMapper<BaseDevicePaymentPlanAbstract, DevicePaymentPlanInfo> {

		private static DevicePaymentPlanEligibilityMapper INSTANCE = null;

		private DevicePaymentPlanEligibilityMapper() {
			super(BaseDevicePaymentPlanAbstract.class, DevicePaymentPlanInfo.class);
		}

		protected synchronized static DevicePaymentPlanEligibilityMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new DevicePaymentPlanEligibilityMapper();
			}
			return INSTANCE;
		}

		@Override
		protected DevicePaymentPlanInfo performDomainMapping(BaseDevicePaymentPlanAbstract source, DevicePaymentPlanInfo target) {

			target.setDevicePaymentPlanEligibilityInd(source.getDevicePaymentPlanEligibilityIndicator().isEligibilityInd());
			target.setDevicePaymentPlanEligibilityReasonList(source.getDevicePaymentPlanEligibilityIndicator().getReasonCodeList());
			target.setDevicePaymentPlanCode(source.getDevicePaymentPlanCd());
			if (source instanceof FinancingDevicePaymentPlan) {
				mapFinancingDevicePaymentPlan((FinancingDevicePaymentPlan) source, target);
			} else if (source instanceof TabDevicePaymentPlan) {
				mapTabDevicePaymentPlan((TabDevicePaymentPlan) source, target);
			} else if (source instanceof SubsidyDevicePaymentPlan) {
				mapSubsidyDevicePaymentPlan((SubsidyDevicePaymentPlan) source, target);
			} else if (source instanceof ByodDevicePaymentPlan) {
				mapBYODDevicePaymentPlan((ByodDevicePaymentPlan) source, target);
			}

			return super.performDomainMapping(source, target);
		}

		private void mapFinancingDevicePaymentPlan(FinancingDevicePaymentPlan source, DevicePaymentPlanInfo target) {
			target.setDevicePaymentPlanType(DevicePaymentPlanInfo.TYPE_FINANCIAL);
			target.setDevicePaymentPlanAmount(source.getDownpaymentAmount());
			target.setSecurityDepositAmount(source.getSecurityDepositAmount());
		}

		private void mapTabDevicePaymentPlan(TabDevicePaymentPlan source, DevicePaymentPlanInfo target) {
			target.setDevicePaymentPlanType(DevicePaymentPlanInfo.TYPE_TAB);
			target.setDevicePaymentPlanAmount(source.getTabAmount());
			target.setSecurityDepositAmount(source.getSecurityDepositAmount());
		}

		private void mapSubsidyDevicePaymentPlan(SubsidyDevicePaymentPlan source, DevicePaymentPlanInfo target) {
			target.setDevicePaymentPlanType(DevicePaymentPlanInfo.TYPE_SUBSIDY);
			target.setSecurityDepositAmount(source.getSecurityDepositAmount());
		}

		private void mapBYODDevicePaymentPlan(ByodDevicePaymentPlan source, DevicePaymentPlanInfo target) {
			target.setDevicePaymentPlanType(DevicePaymentPlanInfo.TYPE_BYOD);
			target.setSecurityDepositAmount(source.getSecurityDepositAmount());
		}

	}

	public static class DevicePaymentThresholdMapper extends AbstractSchemaMapper<DevicePaymentPlanThresholdList, DevicePaymentThresholdInfo> {

		private static DevicePaymentThresholdMapper INSTANCE = null;

		private DevicePaymentThresholdMapper() {
			super(DevicePaymentPlanThresholdList.class, DevicePaymentThresholdInfo.class);
		}

		protected synchronized static DevicePaymentThresholdMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new DevicePaymentThresholdMapper();
			}
			return INSTANCE;
		}

		@Override
		protected DevicePaymentThresholdInfo performDomainMapping(DevicePaymentPlanThresholdList source, DevicePaymentThresholdInfo target) {

			target.setDevicePaymentPlanCode(source.getDevicePaymentPlanCd());
			target.setNoSecurityDepositRequiredMaxNumber(source.getNoSecurityDepositRequiredMaxNumber());
			target.setNoDownPaymentRequiredMaxNumber(source.getNoDownPaymentRequiredMaxNumber());

			return super.performDomainMapping(source, target);
		}

	}

}