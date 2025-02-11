package com.telus.cmb.endpoint.mapper;

import java.util.List;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.CallingCirclePhoneNumberListInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.PhoneNumberList;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TimePeriod;

public class CallingCirclePhoneNumberListInfoMapper extends AbstractSchemaMapper<CallingCirclePhoneNumberListInfo, CallingCirclePhoneListInfo> {

	public CallingCirclePhoneNumberListInfoMapper() {
		super(CallingCirclePhoneNumberListInfo.class, CallingCirclePhoneListInfo.class);
	}

	@Override
	protected CallingCirclePhoneListInfo performDomainMapping(CallingCirclePhoneNumberListInfo source, CallingCirclePhoneListInfo target) {
		target.setEffectiveDate(source.getTimePeriod().getEffectiveDate());
		target.setExpiryDate(source.getTimePeriod().getExpiryDate());

		List<String> phoneNumberList = source.getPhoneNumberList().getPhoneNumber();
		target.setPhoneNumberList((String[]) phoneNumberList.toArray(new String[phoneNumberList.size()]));
		return super.performDomainMapping(source, target);
	}

	@Override
	protected CallingCirclePhoneNumberListInfo performSchemaMapping(CallingCirclePhoneListInfo source, CallingCirclePhoneNumberListInfo target) {
		TimePeriod timePeriod = new TimePeriod();
		timePeriod.setEffectiveDate(source.getEffectiveDate());
		timePeriod.setExpiryDate(source.getExpiryDate());

		PhoneNumberList phoneNumberList = new PhoneNumberList();
		for (String phoneNumber : source.getPhoneNumberList()) {
			phoneNumberList.getPhoneNumber().add(phoneNumber);
		}

		target.setTimePeriod(timePeriod);
		target.setPhoneNumberList(phoneNumberList);
		return super.performSchemaMapping(source, target);
	}
}
