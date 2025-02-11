package com.telus.cmb.common.dao.util;

import java.util.ArrayList;
import java.util.Date;

import amdocs.APILink.datatypes.FtrVoiceAllocationInfo;
import amdocs.APILink.datatypes.ServiceVoiceAllocationInfo;
import amdocs.APILink.datatypes.SocVoiceAllocationInfo;

import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.utility.info.FeatureAirTimeAllocationInfo;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;

public class VoiceAirTimeMapping {
	
	private static final char KB_BYTE_FLAG_TRUE = 'Y';
	
	public static ServiceAirTimeAllocationInfo mapServiceVoiceAllocation( ServiceVoiceAllocationInfo amdocsInfo ) {
		
		ServiceAirTimeAllocationInfo serviceAirTimeInfo = new ServiceAirTimeAllocationInfo();
		SocVoiceAllocationInfo socInfo = amdocsInfo.socVoiceAllocationInfo;
		serviceAirTimeInfo.setServiceCode( socInfo.soc );
		serviceAirTimeInfo.setDescription( socInfo.socDescEng );
		serviceAirTimeInfo.setDescriptionFrench( socInfo.socDescFrench);
		serviceAirTimeInfo.setServiceType( AttributeTranslator.stringFrombyte(socInfo.serviceType) );
		serviceAirTimeInfo.setProductType( AttributeTranslator.stringFrombyte(socInfo.productType) );
		serviceAirTimeInfo.setBillCycleTreatmentCode( socInfo.bcicIndicator );
		serviceAirTimeInfo.setCoverageType( AttributeTranslator.stringFrombyte(socInfo.coverageType) );
		
		ArrayList<FeatureAirTimeAllocationInfo> list = new ArrayList<FeatureAirTimeAllocationInfo>();
		
		Date earliestEffectiveDate = new Date();
		Date earliestExpriationDate = null;
		boolean containPeriodBasedFeature = false;
		for ( FtrVoiceAllocationInfo ftrVoiceAllocation : amdocsInfo.ftrVoiceAllocationInfo ) {
			
			FeatureAirTimeAllocationInfo featureAirTimeInfo = mapFtrVoiceAllocationInfo(ftrVoiceAllocation);
			
			containPeriodBasedFeature |= featureAirTimeInfo.isPeriodBased();
			if ( ftrVoiceAllocation.ftrEffDate!=null && earliestEffectiveDate.after(ftrVoiceAllocation.ftrEffDate) ) {
				earliestEffectiveDate = ftrVoiceAllocation.ftrEffDate;
			}
			
			if ( ftrVoiceAllocation.ftrExpDate!=null) {
				if ( earliestExpriationDate ==null ) {
					earliestExpriationDate = ftrVoiceAllocation.ftrExpDate;
				} else if( earliestExpriationDate.after(ftrVoiceAllocation.ftrExpDate)) {
					earliestExpriationDate = ftrVoiceAllocation.ftrExpDate;
				}
			}
			
			list.add( featureAirTimeInfo );
		}
		
		serviceAirTimeInfo.setFeatureAirTimeAllocations(list.toArray( new FeatureAirTimeAllocationInfo[list.size()]) );
		serviceAirTimeInfo.setEffectiveDate(earliestEffectiveDate);
		serviceAirTimeInfo.setExpirationDate(earliestExpriationDate);
		serviceAirTimeInfo.setContainPeriodBasedFeature(containPeriodBasedFeature);
		
		return serviceAirTimeInfo;
		
	}
	
	private static FeatureAirTimeAllocationInfo  mapFtrVoiceAllocationInfo(FtrVoiceAllocationInfo ftrVoiceAllocation){
		FeatureAirTimeAllocationInfo featureAirTimeInfo = new FeatureAirTimeAllocationInfo();
		
		featureAirTimeInfo.setCode( ftrVoiceAllocation.featureCode );
		featureAirTimeInfo.setDescription(ftrVoiceAllocation.featureDesc );
		featureAirTimeInfo.setDescriptionFrench(ftrVoiceAllocation.featureDescF);
		featureAirTimeInfo.setCallingDirection(AttributeTranslator.stringFrombyte(ftrVoiceAllocation.actionDirectionCode));
		featureAirTimeInfo.setCallingRouteType(AttributeTranslator.stringFrombyte(ftrVoiceAllocation.callingRouteType));
		featureAirTimeInfo.setPeriodBased((ftrVoiceAllocation.ucPeriodInd==KB_BYTE_FLAG_TRUE));
		featureAirTimeInfo.setPeriodName(ftrVoiceAllocation.periodName );
		featureAirTimeInfo.setPeriodValueCode(ftrVoiceAllocation.periodValCd);
		featureAirTimeInfo.getClassification0().setCode( ftrVoiceAllocation.categoryCode );
		featureAirTimeInfo.setFeatureGroup(ftrVoiceAllocation.featureGroup);
		featureAirTimeInfo.setInclusiveQuantityType(ftrVoiceAllocation.incQtyType);
		featureAirTimeInfo.setUsageChargeDependCode( AttributeTranslator.stringFrombyte( ftrVoiceAllocation.ucUsgDependCode ) );
		featureAirTimeInfo.setTierLevelCode(ftrVoiceAllocation.tierLevelCd);
		featureAirTimeInfo.setFromQuantity(ftrVoiceAllocation.fromQuantity);
		featureAirTimeInfo.setToQuantity(ftrVoiceAllocation.toQuantity);
		featureAirTimeInfo.setPooling( ftrVoiceAllocation.poolingInd==KB_BYTE_FLAG_TRUE );
		featureAirTimeInfo.setSharing(ftrVoiceAllocation.sharingInd==KB_BYTE_FLAG_TRUE);
		featureAirTimeInfo.setMinimumCommitmentRequired(ftrVoiceAllocation.mcInd==KB_BYTE_FLAG_TRUE);
		featureAirTimeInfo.setParameterRequired( ftrVoiceAllocation.svcFtrParamInd==KB_BYTE_FLAG_TRUE);
		featureAirTimeInfo.setParameterName(ftrVoiceAllocation.paramName);
		featureAirTimeInfo.setParameterValue(ftrVoiceAllocation.paramVal);
		featureAirTimeInfo.setFreeMinuteType(AttributeTranslator.stringFrombyte( ftrVoiceAllocation.fmType ) );
		featureAirTimeInfo.setFreeInlcudedMinutes(ftrVoiceAllocation.fmImAllowed);
		featureAirTimeInfo.setUsageCharge(ftrVoiceAllocation.ucInfoInd==KB_BYTE_FLAG_TRUE );
		featureAirTimeInfo.setUsageCharge(ftrVoiceAllocation.ucRate);
		featureAirTimeInfo.setRecurringCharge(ftrVoiceAllocation.rcInfoInd==KB_BYTE_FLAG_TRUE);
		featureAirTimeInfo.setRecurringCharge(ftrVoiceAllocation.rcRate);
		featureAirTimeInfo.setOneTimeCharge(ftrVoiceAllocation.ocInfoInd==KB_BYTE_FLAG_TRUE);
		featureAirTimeInfo.setOneTimeCharge(ftrVoiceAllocation.ocRate);
		featureAirTimeInfo.setSwitchActionRequired(ftrVoiceAllocation.switchActNeeded==KB_BYTE_FLAG_TRUE);
		featureAirTimeInfo.setSwitchCode(ftrVoiceAllocation.switchCode);
		featureAirTimeInfo.setSwitchParameter(ftrVoiceAllocation.switchParam);
		
		return featureAirTimeInfo;
		
	}

}
