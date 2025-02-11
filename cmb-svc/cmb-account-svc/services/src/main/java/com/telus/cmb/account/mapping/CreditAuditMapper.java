package com.telus.cmb.account.mapping;

import java.math.BigInteger;
import java.util.Arrays;

import com.telus.api.account.AuditHeader;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.CreditAuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.AuditInfo;

public class CreditAuditMapper {
	
	public static AuditHeaderMapper AuditHeaderMapper() {
		return AuditHeaderMapper.getInstance();
	}

	public static AuditInfoMapper AuditInfoMapper() {
		return AuditInfoMapper.getInstance();
	}
	
	public static CreditAuditInfoMapper CreditAuditInfoMapper() {
		return CreditAuditInfoMapper.getInstance();
	}

	public static class AuditHeaderMapper extends AbstractSchemaMapper<OriginatingUserType, AuditHeader> {

		private static AuditHeaderMapper INSTANCE = null;

		public AuditHeaderMapper() {
			super(OriginatingUserType.class, AuditHeader.class);
		}

		protected synchronized static AuditHeaderMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new AuditHeaderMapper();
			}
			return INSTANCE;
		}

		@Override
		protected OriginatingUserType performSchemaMapping(AuditHeader source, OriginatingUserType target) {

			target.setCustId(source.getCustomerId());
			target.setIpAddress(source.getUserIPAddress());
			AuditHeader.AppInfo[] appInfos = source.getAppInfos();
			if (appInfos != null) {
				OriginatingUserType.AppInfo[] appInfoList = new OriginatingUserType.AppInfo[appInfos.length];
				for (int i = 0; i < appInfos.length; i++) {
					appInfoList[i] = new OriginatingUserType.AppInfo();
					appInfoList[i].setApplicationId(BigInteger.valueOf(appInfos[i].getApplicationId()));
					appInfoList[i].setIpAddress(appInfos[i].getIPAddress());
					appInfoList[i].setUserId(appInfos[i].getUserId());
				}
				target.getAppInfo().addAll(Arrays.asList(appInfoList));
			}
			
			return super.performSchemaMapping(source, target);
		}
	}

	public static class AuditInfoMapper extends AbstractSchemaMapper<AuditInfo, com.telus.eas.transaction.info.AuditInfo> {

		private static AuditInfoMapper INSTANCE = null;

		public AuditInfoMapper() {
			super(AuditInfo.class, com.telus.eas.transaction.info.AuditInfo.class);
		}

		protected synchronized static AuditInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new AuditInfoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected AuditInfo performSchemaMapping(com.telus.eas.transaction.info.AuditInfo source, AuditInfo target) {

			target.setUserId(source.getUserId());
			target.setUserTypeCode(source.getUserTypeCode());
			target.setSalesRepresentativeId(source.getSalesRepId());
			target.setChannelOrganizationId(source.getChannelOrgId());
			target.setOutletId(source.getOutletId());
			target.setOriginatorApplicationId(source.getOriginatorAppId());
			target.setCorrelationId(source.getCorrelationId());
			if (source.getTimestamp() != null) {
				target.setTimestamp(source.getTimestamp());
			}
			
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class CreditAuditInfoMapper extends AbstractSchemaMapper<CreditAuditInfo, com.telus.eas.transaction.info.AuditInfo> {

		private static CreditAuditInfoMapper INSTANCE = null;

		public CreditAuditInfoMapper() {
			super(CreditAuditInfo.class, com.telus.eas.transaction.info.AuditInfo.class);
		}

		protected synchronized static CreditAuditInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditAuditInfoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditAuditInfo performSchemaMapping(com.telus.eas.transaction.info.AuditInfo source, CreditAuditInfo target) {
			
		    target.setOriginatorApplicationId(source.getOriginatorAppId());
		    target.setChannelOrganizationId(source.getChannelOrgId());
			target.setUserId(source.getUserId());		
			//target.setServiceConsumerApplicationId(0L);
				
			return super.performSchemaMapping(source, target);
		}
	}
	
}