package com.telus.cmb.endpoint.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.CallingCircleInfo;

public class CallingCircleInformationMapper extends AbstractSchemaMapper<CallingCircleInfo, CallingCircleParametersInfo> {

	private CallingCirclePhoneNumberListInfoMapper callingCirclePhoneNumberMapper = new CallingCirclePhoneNumberListInfoMapper();

	public CallingCircleInformationMapper() {
		super(CallingCircleInfo.class, CallingCircleParametersInfo.class);
	}

	@Override
	protected CallingCircleParametersInfo performDomainMapping(CallingCircleInfo source, CallingCircleParametersInfo target) {
		target.setCallingCircleCurrentPhoneNumberList(callingCirclePhoneNumberMapper.mapToDomain(source.getCurrentPhoneNumberList()));
		target.setCallingCircleFuturePhoneNumberList(callingCirclePhoneNumberMapper.mapToDomain(source.getFuturePhoneNumberList()));
		return super.performDomainMapping(source, target);
	}

	@Override
	protected CallingCircleInfo performSchemaMapping(CallingCircleParametersInfo source, CallingCircleInfo target) {
		target.setCurrentPhoneNumberList(callingCirclePhoneNumberMapper.mapToSchema((CallingCirclePhoneListInfo) source.getCallingCircleCurrentPhoneNumberList()));
		target.setFuturePhoneNumberList(callingCirclePhoneNumberMapper.mapToSchema((CallingCirclePhoneListInfo) source.getCallingCircleFuturePhoneNumberList()));
		return super.performSchemaMapping(source, target);
	}
}
