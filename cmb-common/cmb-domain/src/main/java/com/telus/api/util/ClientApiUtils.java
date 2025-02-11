package com.telus.api.util;

import java.util.Date;

import com.telus.api.account.ContractService;
import com.telus.eas.framework.info.Info;

/**
 * This class contains various convenient utilities to deal 
 * with cliantAPI domain objects. These utilities logically cannot be part of
 * any class.
 * 
 * @author x168277
 *
 */
public abstract class ClientApiUtils {
	
	/**
	 * Generates mapping key for in-memory contract services management.
	 * This key should allow multiple instance of the same service co-exist 
	 * in the same contract. The differentiator is service EffectiveDate.
	 * At the time of this developed an assumption that the same duplicate service is
	 * NOT allowed at the same day. However, if this changes in the future, please update
	 * this utility accordingly.
	 * The key is only used for in-memory map management.
	 * 
	 * @param contractService
	 * @return
	 */
	public static String getContractServiceMappingKey(ContractService contractService) {
		if(contractService == null) {
			return null;
		} 
		return getContractServiceMappingKey(Info.padService(contractService.getCode()), contractService.getEffectiveDate());
	}
	
	/**
	 * Generates mapping key for in-memory contract services management.
	 * This key should allow multiple instance of the same service co-exist 
	 * in the same contract. The differentiator is service EffectiveDate.
	 * At the time of this developed an assumption that the same duplicate service is
	 * NOT allowed at the same day. However, if this changes in the future, please update
	 * this utility accordingly.
	 * The key is only used for in-memory map management.
	 * 
	 * @param serviceCode
	 * @param serviceEffectiveDate
	 * @return
	 */
	public static String getContractServiceMappingKey(String serviceCode, Date serviceEffectiveDate) {
		if(serviceCode == null) {
			return null;
		}
		if(serviceEffectiveDate != null) {
			return  Info.padService(serviceCode) + DateUtil.clearTimePortion(serviceEffectiveDate).getTime();
		} else {
			return Info.padService(serviceCode);
		}
	}
}
