package com.telus.cmb.account.mapping;

import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.cmb.common.mapping.EnterpriseCommonTypesV9Mapper;
import com.telus.eas.account.credit.info.CreditAssessmentInfo;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditmanagementsvcrequestresponse_v2.GetCreditWorthinessResponse;

/**
 * WirelessCreditManagementServiceMapper
 * 
 * @author R. Fong
 *
 */
public class WirelessCreditManagementServiceMapper {

	public static CreditWorthinessResponseMapper CreditWorthinessResponseMapper() {
		return CreditWorthinessResponseMapper.getInstance();
	}
	
	public static class CreditWorthinessResponseMapper extends AbstractSchemaMapper<GetCreditWorthinessResponse, CreditAssessmentInfo> {

		private static CreditWorthinessResponseMapper INSTANCE = null;

		private CreditWorthinessResponseMapper() {
			super(GetCreditWorthinessResponse.class, CreditAssessmentInfo.class);
		}

		protected synchronized static CreditWorthinessResponseMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditWorthinessResponseMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditAssessmentInfo performDomainMapping(GetCreditWorthinessResponse source, CreditAssessmentInfo target) {

			target.setCreditWorthiness(WirelessCreditTypesMapper.CreditWorthinessMapper().mapToDomain(source.getCreditWorthiness()));
			if (source.getAsessmentMessage() != null) {
				target.setAssessmentMessageList(EnterpriseCommonTypesV9Mapper.DescriptionMapper().mapToDomain(source.getAsessmentMessage().getDescription()));
			}
			target.setCreditWarningList(WirelessCreditTypesMapper.CreditWarningMapper().mapToDomain(source.getWarningHistoryList()));
			
			return super.performDomainMapping(source, target);
		}

	}

}