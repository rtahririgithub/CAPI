package com.telus.cmb.jws.mapping.billing_inquiry;

import java.util.List;

import com.telus.api.ClientAPI;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType.AppInfo;

public class AuditHeaderMapper extends AbstractSchemaMapper<OriginatingUserType, AuditHeaderInfo> {

	public AuditHeaderMapper(){
		super(OriginatingUserType.class, AuditHeaderInfo.class);
	}

	@Override
	protected AuditHeaderInfo performDomainMapping(OriginatingUserType source,
			AuditHeaderInfo target) {
		target.setCustomerId(source.getCustId());
		target.setUserIPAddress(source.getIpAddress());
		List<AppInfo> appInfoList = source.getAppInfo();
		for (AppInfo appInfo : appInfoList) {
			target.appendAppInfo (appInfo.getUserId(), appInfo.getApplicationId().intValue(), appInfo.getIpAddress());
		}				
		return super.performDomainMapping(source, target);
	}

	
}
