/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.eas.utility.info.ServiceExtendedInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.AccountSubType;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.ServiceExtendedInformation;


/**
 * @author Wilson Cheong
 *
 */
public class ServiceExtendedInfoMapper extends AbstractSchemaMapper<ServiceExtendedInformation, ServiceExtendedInfo> {

	public ServiceExtendedInfoMapper() {
		super(ServiceExtendedInformation.class, ServiceExtendedInfo.class);
	}
	
	private static final String ALL_PROVINCE_CODE = "ALL";
	private static final String CALGARY_CODE = "CAL";
	private static final List<ProvinceCode> ALL_PROVINCES = Arrays.asList(new ProvinceCode[]{
			ProvinceCode.AB, ProvinceCode.BC, ProvinceCode.MB, ProvinceCode.NB,	ProvinceCode.NF, 
			ProvinceCode.NS, ProvinceCode.NT, ProvinceCode.NU, ProvinceCode.ON, ProvinceCode.PE, 
			ProvinceCode.PQ, ProvinceCode.SK,	ProvinceCode.YT
	});
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected ServiceExtendedInformation performSchemaMapping(ServiceExtendedInfo source, ServiceExtendedInformation target) {
		//Copy over account type/subtypes
		List<AccountSubType> accountTypes = new ArrayList<AccountSubType>();
		if (source.getAccountTypes() != null) {
			for (AccountTypeInfo accountTypeInfo : source.getAccountTypes()) {
				if (accountTypeInfo != null) {
					AccountSubType accountType = new AccountSubType();
					accountType.setAccountType(String.valueOf(accountTypeInfo.getAccountType()));			
					accountType.setAccountSubType(String.valueOf(accountTypeInfo.getAccountSubType()));
					accountTypes.add(accountType);
				}
			}
		}
		target.getAccountTypeList().addAll(accountTypes);
						
		//Copy over province codes
		Set<ProvinceCode> provinceCodes = new HashSet<ProvinceCode>();
		if (source.getProvinceCodes() != null) {
			for (String provinceInfo : source.getProvinceCodes()) {
				if (provinceInfo != null) {
					if (CALGARY_CODE.equals(provinceInfo.trim())) {
						provinceCodes.add(ProvinceCode.AB);
					} else if (ALL_PROVINCE_CODE.equals(provinceInfo.trim())) {
						provinceCodes.addAll(ALL_PROVINCES);
						break;
					} else {
						try {
							provinceCodes.add(ProvinceCode.fromValue(provinceInfo.trim()));	
						} catch (IllegalArgumentException ex) {
							//Skipped province
						}	
					}
				}
			}
		}
		target.getProvinceCodeList().addAll(provinceCodes);	
		
		//Copy over socGroups
		if (source.getSocGroups() != null) {
			target.getSocGroupList().addAll(Arrays.asList(source.getSocGroups()));
		}

		//Copy over soc code
		target.setServiceCode(source.getCode());
		
		return super.performSchemaMapping(source, target);
	}

}
