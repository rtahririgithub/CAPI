package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.UsageChargeInfo;
import com.telus.eas.account.info.VoiceUsageServiceDirectionInfo;
import com.telus.eas.account.info.VoiceUsageServiceInfo;
import com.telus.eas.account.info.VoiceUsageServicePeriodInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.ratedairtimeusageinquirytypes_v1.AirtimeUsageChargeInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.ratedairtimeusageinquirytypes_v1.ChargeInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.ratedairtimeusageinquirytypes_v1.VoiceUsageService;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.ratedairtimeusageinquirytypes_v1.VoiceUsageServiceDirection;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.ratedairtimeusageinquirytypes_v1.VoiceUsageServicePeriod;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.ratedairtimeusageinquirytypes_v1.VoiceUsageSummary;

/**
 * @author Brandon Wen
 *
 */
public class WirelessRatedAirtimeUsageInquiryMapper {
	
	public static AirtimeUsageChargeInfoMapper AirtimeUsageChargeInfoMapper() {
		return AirtimeUsageChargeInfoMapper.getInstance();
	}
	
	public static UsageChargeInfoMapper UsageChargeInfoMapper() {
		return UsageChargeInfoMapper.getInstance();
	}

	/**
	 * AirtimeUsageChargeInfoMapper class
	 * @author Brandon Wen
	 *
	 */
	public static class AirtimeUsageChargeInfoMapper extends AbstractSchemaMapper<AirtimeUsageChargeInfo, com.telus.eas.account.info.AirtimeUsageChargeInfo> {
		private static AirtimeUsageChargeInfoMapper INSTANCE;
		
		private AirtimeUsageChargeInfoMapper() {
			super(AirtimeUsageChargeInfo.class, com.telus.eas.account.info.AirtimeUsageChargeInfo.class);
		}
		
		protected static synchronized AirtimeUsageChargeInfoMapper getInstance() {
			if (INSTANCE == null) {
				 INSTANCE = new AirtimeUsageChargeInfoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected AirtimeUsageChargeInfo performSchemaMapping(com.telus.eas.account.info.AirtimeUsageChargeInfo source, AirtimeUsageChargeInfo target) {
			target.setTotalChargeAmount(source.getTotalChargeAmount());
			for (Object usageChargeInfo : source.getChargeInfoList()) {
				target.getChargeInfo().add(UsageChargeInfoMapper().mapToSchema((UsageChargeInfo)usageChargeInfo));
			}
			return super.performSchemaMapping(source, target);
		}
		

		@SuppressWarnings("unchecked")
		@Override
		protected com.telus.eas.account.info.AirtimeUsageChargeInfo performDomainMapping(AirtimeUsageChargeInfo source, com.telus.eas.account.info.AirtimeUsageChargeInfo target) {
			target.setTotalChargeAmount(source.getTotalChargeAmount());
			for (ChargeInfo chargeInfo : source.getChargeInfo()) {
				target.getChargeInfoList().add(UsageChargeInfoMapper().mapToDomain(chargeInfo));
			}
			return super.performDomainMapping(source, target);
		}
	}
	
	/**
	 * UsageChargeInfoMapper class
	 * @author Brandon Wen
	 *
	 */
	public static class UsageChargeInfoMapper extends AbstractSchemaMapper<ChargeInfo, UsageChargeInfo> {
		private static UsageChargeInfoMapper INSTANCE;

		private UsageChargeInfoMapper() {
			super(ChargeInfo.class, UsageChargeInfo.class);
		}
		
		protected static synchronized UsageChargeInfoMapper getInstance() {
			if (INSTANCE == null) {
				 INSTANCE = new UsageChargeInfoMapper();
			}
			return INSTANCE;
		}
		
		@Override 
		protected ChargeInfo performSchemaMapping(UsageChargeInfo source, ChargeInfo target) {
			target.setChargeRecordType(source.getChargeRecordType());
			target.setChargeAmount(source.getChargeAmount());
			return super.performSchemaMapping(source, target);
		}
		
		
		@Override 
		protected UsageChargeInfo performDomainMapping(ChargeInfo source, UsageChargeInfo target) {
			target.setChargeRecordType(source.getChargeRecordType());
			target.setChargeAmount(source.getChargeAmount());
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class VoiceUsageSummaryMapper extends AbstractSchemaMapper<VoiceUsageSummary, VoiceUsageSummaryInfo> {
		private static VoiceUsageSummaryMapper instance = null;
		
		private VoiceUsageSummaryMapper() {
			super (VoiceUsageSummary.class, VoiceUsageSummaryInfo.class);
		}
		
		public static synchronized VoiceUsageSummaryMapper getInstance() {
			if (instance == null) {
				instance = new VoiceUsageSummaryMapper();
			}
			
			return instance;
		}

		@Override
		protected VoiceUsageSummary performSchemaMapping(VoiceUsageSummaryInfo source, VoiceUsageSummary target) {
			target.setServiceFeature(source.getFeatureCode());
			target.setSubscriberNumber(source.getPhoneNumber());
			target.setUnitOfMeasureCode(source.getUnitOfMeasureCode());
			target.getVoiceUsageService().addAll(VoiceUsageServiceMapper.getInstance().mapToSchema((VoiceUsageServiceInfo[]) source.getVoiceUsageServices()));
			return super.performSchemaMapping(source, target);
		}	
	}
	
	private static class VoiceUsageServiceMapper extends AbstractSchemaMapper<VoiceUsageService, VoiceUsageServiceInfo> {
		private static VoiceUsageServiceMapper instance = null;
		
		private VoiceUsageServiceMapper() {
			super (VoiceUsageService.class, VoiceUsageServiceInfo.class);
		}
		
		public static VoiceUsageServiceMapper getInstance() {
			if (instance == null) {
				instance = new VoiceUsageServiceMapper();
			}
			
			return instance;
		}

		@Override
		protected VoiceUsageService performSchemaMapping(VoiceUsageServiceInfo source, VoiceUsageService target) {
			target.setIncludedMinutesAllocationCode(source.getIMAllocationIndicator());
			target.setServiceCode(source.getServiceCode());
			target.getVoiceUsageServiceDirection().addAll(VoiceUsageServiceDirectionMapper.getInstance().mapToSchema((VoiceUsageServiceDirectionInfo[]) source.getVoiceUsageServiceDirections()));
			return super.performSchemaMapping(source, target);
		}
	}
	
	private static class VoiceUsageServiceDirectionMapper extends AbstractSchemaMapper<VoiceUsageServiceDirection, VoiceUsageServiceDirectionInfo> {
		private static VoiceUsageServiceDirectionMapper instance = null;
		
		private VoiceUsageServiceDirectionMapper() {
			super (VoiceUsageServiceDirection.class, VoiceUsageServiceDirectionInfo.class);
		}
		
		public static synchronized VoiceUsageServiceDirectionMapper getInstance() {
			if (instance == null) {
				instance = new VoiceUsageServiceDirectionMapper();
			}
			
			return instance;
		}

		@Override
		protected VoiceUsageServiceDirection performSchemaMapping(VoiceUsageServiceDirectionInfo source, VoiceUsageServiceDirection target) {
			target.setDirectionCode(source.getDirectionCode());
			target.setLastCallDate(source.getLastCallDate());
			target.getVoiceUsageServicePeriod().addAll(VoiceUsageServicePeriodMapper.getInstance().mapToSchema((VoiceUsageServicePeriodInfo[]) source.getVoiceUsageServicePeriods()));
			return super.performSchemaMapping(source, target);
		}
	}

	private static class VoiceUsageServicePeriodMapper extends AbstractSchemaMapper<VoiceUsageServicePeriod, VoiceUsageServicePeriodInfo> {
		private static VoiceUsageServicePeriodMapper instance = null;
		
		private VoiceUsageServicePeriodMapper() {
			super (VoiceUsageServicePeriod.class, VoiceUsageServicePeriodInfo.class);
		}
		
		public static synchronized VoiceUsageServicePeriodMapper getInstance() {
			if (instance == null) {
				instance = new VoiceUsageServicePeriodMapper();
			}
			
			return instance;
		}

		@Override
		protected VoiceUsageServicePeriod performSchemaMapping(VoiceUsageServicePeriodInfo source, VoiceUsageServicePeriod target) {
			target.setCalls(source.getCalls());
			target.setChargeable(source.getChargeable());
			target.setChargeAmount(source.getChargeAmount());
			target.setFree(source.getFree());
			target.setIncludedMinuteAmt(source.getIncluded());
			target.setIncludedUsedMinuteAmt(source.getIncludedUsed());
			target.setPeriodCode(source.getPeriodCode());
			target.setRemaining(source.getRemaining());
			target.setTotalUsedMinuteAmt(source.getTotalUsed());
			return super.performSchemaMapping(source, target);
		}


	}
}
