package com.telus.cmb.jws.mapping;

import com.telus.eas.utility.info.FeatureAirTimeAllocationInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.FeatureAirTimeAllocation;

public class FeatureAirTimeAllocationMapper extends AbstractSchemaMapper<FeatureAirTimeAllocation, FeatureAirTimeAllocationInfo> {
	private static ServiceFeatureClassificationMapper classificationMapper = new ServiceFeatureClassificationMapper();
	private static PeriodMapper periodMapper = new PeriodMapper();

	public FeatureAirTimeAllocationMapper() {
		super(FeatureAirTimeAllocation.class, FeatureAirTimeAllocationInfo.class);
	}

	@Override
	protected FeatureAirTimeAllocation performSchemaMapping( FeatureAirTimeAllocationInfo source, FeatureAirTimeAllocation target) {
		target.setCallingDirectionCd(source.getCallingDirection());
		target.setCallingRouteTypeCd(source.getCallingRouteType());
		target.setClassification(classificationMapper.mapToSchema(source.getClassification0()));
		target.setCode(source.getCode());
		target.setDescription(source.getDescription());
		target.setDescriptionFrench(source.getDescriptionFrench());
		target.setFeatureGroup(source.getFeatureGroup());
		target.setFreeIncludedMinutesAmount(source.getFreeInlcudedMinutes());
		target.setFreeMinuteTypeCd(source.getFreeMinuteType());
		target.setFromQuantity(source.getFromQuantity());
		target.setInclusiveQtyType(source.getInclusiveQuantityType());
		target.setMinCommitmentInd(source.isMinimumCommitmentRequired());
		target.setOnetimeChargeInfoInd(source.isOneTimeCharge());
		target.setOnetimeChargeRate(source.getOneTimeCharge());
		if (source.isPeriodBased()) { 
			target.setPeriod( periodMapper.mapToSchema(source.getPeriod()));
		}
		target.setPeriodBasedInd(source.isPeriodBased());
		target.setPoolingInd(source.isPooling());
		target.setRecurringChargeInfoInd(source.isRecurringCharge());
		target.setRecurringChargeRate(source.getRecurringCharge());
		target.setServiceFeatureParamTypeCd(source.getParameterType());
		target.setSharingInd(source.isSharing());
		target.setTierLevelCd(source.getTierLevelCode());
		target.setToQuantity(source.getToQuantity());
		target.setUsageChargeInfoInd(source.isUsageCharge());
		target.setUsageChargeRate(source.getUsageCharge());
		target.setUsageChargeRatingCd(source.getUsageChargeDependCode());
		return super.performSchemaMapping(source, target);
	}

}
