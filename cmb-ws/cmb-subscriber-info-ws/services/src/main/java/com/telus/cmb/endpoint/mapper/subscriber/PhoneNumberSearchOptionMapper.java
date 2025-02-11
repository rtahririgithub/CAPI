package com.telus.cmb.endpoint.mapper.subscriber;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.PhoneNumberSearchOption;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;

public class PhoneNumberSearchOptionMapper extends AbstractSchemaMapper<PhoneNumberSearchOption, PhoneNumberSearchOptionInfo> {

	public PhoneNumberSearchOptionMapper() {
		super(PhoneNumberSearchOption.class, PhoneNumberSearchOptionInfo.class);
	}

	protected PhoneNumberSearchOptionInfo performDomainMapping(PhoneNumberSearchOption source, PhoneNumberSearchOptionInfo target) {
		boolean searchWirelessInd = source.isSearchWirelessInd() != null ? source.isSearchWirelessInd() : false;
		boolean searchVoipInd = source.isSearchVoipInd() != null ? source.isSearchVoipInd() : false;
		boolean searchTollFreeInd = source.isSearchTollFreeInd() != null ? source.isSearchTollFreeInd() : false;
		target.setSearchWirelessNumber(searchWirelessInd);
		target.setSearchVOIP(searchVoipInd);
		target.setSearchTollFree(searchTollFreeInd);
		return super.performDomainMapping(source, target);
	}
}
