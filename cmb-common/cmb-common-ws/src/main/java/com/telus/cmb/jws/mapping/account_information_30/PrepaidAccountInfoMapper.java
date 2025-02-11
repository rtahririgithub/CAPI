package com.telus.cmb.jws.mapping.account_information_30;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.CreditCardMapper;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.AutoTopUp;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.PrepaidAccountInfo;



public class PrepaidAccountInfoMapper extends AbstractSchemaMapper<PrepaidAccountInfo, PrepaidConsumerAccountInfo> {

	public PrepaidAccountInfoMapper(){
		super(PrepaidAccountInfo.class, PrepaidConsumerAccountInfo.class);
	}


	@Override
	protected PrepaidAccountInfo performSchemaMapping(PrepaidConsumerAccountInfo source, PrepaidAccountInfo target) {
		target.setAirtimeRate(source.getAirtimeRate());
		AutoTopUp autoTopUp = new AutoTopUpMapper().mapToSchema(source.getAutoTopUp0());
		target.setAutoTopUp(autoTopUp);
		target.setBalance(source.getBalance());
		target.setBalanceExpiryDate(source.getBalanceExpiryDate());
		target.setBillingType(source.getBillingType());
		target.setLongDistanceRate(source.getLongDistanceRate());
		target.setMaximumBalanceCap(source.getMaximumBalanceCap());
		target.setMinimumBalanceDate(source.getMinimumBalanceDate());
		target.setOutstandingCharge(source.getOutstandingCharge());
		target.setTopUpCreditCard(new CreditCardMapper().mapToSchema(source.getTopUpCreditCard0()));

		return super.performSchemaMapping(source, target);
	}

}
