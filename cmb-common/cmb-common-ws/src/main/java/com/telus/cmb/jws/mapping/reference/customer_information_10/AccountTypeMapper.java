/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.customer_information_10;

import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.schemas.eca.common_types_2_1.NameFormat;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customer_information_reference_types_1_0.AccountType;

/**
 * @author Pavel Simonovsky
 *
 */
public class AccountTypeMapper extends ReferenceMapper<AccountType, AccountTypeInfo> {

	public AccountTypeMapper() { 
		super(AccountType.class, AccountTypeInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected AccountType performSchemaMapping(AccountTypeInfo source, AccountType target) {
		
		target.setAccountSubTypeCode(Character.toString(source.getAccountSubType()));
		target.setAccountTypeCode(Character.toString(source.getAccountType()));
		target.setBillingNameFormat(NameFormat.fromValue(Character.toString(source.getBillingNameFormat())));
		target.setBrandId(source.getBrandId());
		target.setCode(source.getCode());
		target.setDefaultDealer(source.getDefaultDealer());
		target.setDefaultSalesRepCode(source.getDefaultSalesCode());
		target.setDescription(source.getDescription());
		target.setDescriptionFrench(source.getDescriptionFrench());
		target.setIsAutotel(source.isAutotel());
		target.setIsCorporate(source.isCorporate());
		target.setIsCorporateIDEN(source.isCorporateIDEN());
		target.setIsCorporatePCS(source.isCorporatePCS());
		target.setIsCorporatePrivateNetworkPlus(source.isCorporatePrivateNetworkPlus());
		target.setIsCorporateRegional(source.isCorporateRegional());
		target.setIsCorporateRegular(source.isCorporateRegular());
		target.setIsCreditCheckRequired(source.isCreditCheckRequired());
		target.setIsDuplicateBANCheck(source.isDuplicateBANCheck());
		target.setIsIDEN(source.isIDEN());
		target.setIsPager(source.isPager());
		target.setIsPCS(source.isPCS());
		target.setIsPCSPostpaidCorporateRegularAccount(source.isPCSPostpaidCorporateRegularAccount());
		target.setIsPostpaid(source.isPostpaid());
		target.setIsPostpaidBoxedConsumer(source.isPostpaidBoxedConsumer());
		target.setIsPostpaidBusinessDealer(source.isPostpaidBusinessDealer());
		target.setIsPostpaidBusinessOfficial(source.isPostpaidBusinessOfficial());
		target.setIsPostpaidBusinessPersonal(source.isPostpaidBusinessPersonal());
		target.setIsPostpaidBusinessRegular(source.isPostpaidBusinessRegular());
		target.setIsPostpaidConsumer(source.isPostpaidConsumer());
		target.setIsPostpaidCorporatePersonal(source.isPostpaidCorporatePersonal());
		target.setIsPostpaidCorporateRegular(source.isPostpaidCorporateRegular());
		target.setIsPostpaidEmployee(source.isPostpaidEmployee());
		target.setIsPostpaidOfficial(source.isPostpaidOfficial());
		target.setIsPrepaidConsumer(source.isPrepaidConsumer());
		target.setIsQuebectelPrepaidConsumer(source.isQuebectelPrepaidConsumer());
		target.setIsWesternPrepaidConsumer(source.isWesternPrepaidConsumer());
		
		return super.performSchemaMapping(source, target);
	}

}
