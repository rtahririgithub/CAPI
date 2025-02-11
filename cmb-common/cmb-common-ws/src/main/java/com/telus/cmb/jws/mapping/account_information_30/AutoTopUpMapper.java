
package com.telus.cmb.jws.mapping.account_information_30;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.AutoTopUp;



public class AutoTopUpMapper extends AbstractSchemaMapper<AutoTopUp, AutoTopUpInfo> {

	public AutoTopUpMapper(){
		super(AutoTopUp.class, AutoTopUpInfo.class);
	}

	@Override
	protected AutoTopUp performSchemaMapping(AutoTopUpInfo source, AutoTopUp target) {
		target.setChargeAmount(source.getChargeAmount());
		target.setHasThresholdRecharge(source.hasThresholdRecharge());
		target.setNextChargeDate(source.getNextChargeDate());
		target.setThresholdAmount(source.getThresholdAmount());

		return super.performSchemaMapping(source, target);
	}
}
