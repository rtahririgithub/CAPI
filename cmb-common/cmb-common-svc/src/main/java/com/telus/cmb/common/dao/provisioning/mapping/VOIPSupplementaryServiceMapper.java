package com.telus.cmb.common.dao.provisioning.mapping;

import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.VOIPAccountInfo;
import com.telus.tmi.xmlschema.srv.smo.activation.voipsupplementaryservicerequestresponse_v1.GetAccountInfoResponse;

public class VOIPSupplementaryServiceMapper {

	public static VOIPAccountInfoMapper VOIPAccountMapper() {
		return VOIPAccountInfoMapper.getInstance();
	}
	
	/**
	 * VOIPAccountInfoMapper
	 * @author R. Fong
	 *
	 */
	public static class VOIPAccountInfoMapper extends AbstractSchemaMapper<GetAccountInfoResponse, VOIPAccountInfo> {

		private static VOIPAccountInfoMapper INSTANCE;
		
		private VOIPAccountInfoMapper() {
			super(GetAccountInfoResponse.class, VOIPAccountInfo.class);
		}

		protected static synchronized VOIPAccountInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new VOIPAccountInfoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected VOIPAccountInfo performDomainMapping(GetAccountInfoResponse source, VOIPAccountInfo target) {
			
			if (source.getMainNum() != null) {
				target.setMainCompanyNumber(source.getMainNum());
			}
			if (source.getPricePlanCd() != null) {
				target.setServiceEditionCode(source.getPricePlanCd());
			}
			if (source.getServicePlanId() != null) {
				target.setServicePlanId(source.getServicePlanId());
			}
			if (source.getStatusCd() != null) {
				target.setStatusCode(source.getStatusCd());
			}
			if (source.getTelephoneNum() != null) {
				target.setPhoneNumber(source.getTelephoneNum());
			}
			
			return super.performDomainMapping(source, target);
		}
	}

}