package com.telus.cmb.jws.mapper;

import java.util.List;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.cmb.jws.SaveBusinessCreditCheckResult.BusinessCreditIdentityList;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BusinessCreditIdentity;


public class WirelessAccountManagementServiceMapper {
	
	public static BusinessCreditIdentityListMapper BusinessCreditIdentityListMapper() {
		return BusinessCreditIdentityListMapper.getInstance();
	}
	
	public static class BusinessCreditIdentityListMapper  extends AbstractSchemaMapper<BusinessCreditIdentityList, BusinessCreditIdentityInfo[]> {

		public BusinessCreditIdentityListMapper(){
			super(BusinessCreditIdentityList.class, BusinessCreditIdentityInfo[].class);
		}

		private static BusinessCreditIdentityListMapper INSTANCE;
		

		protected static synchronized BusinessCreditIdentityListMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new BusinessCreditIdentityListMapper();
			}
			return INSTANCE;
		}
		
		@Override
		protected BusinessCreditIdentityInfo[] performDomainMapping(BusinessCreditIdentityList source, BusinessCreditIdentityInfo[] target) {

			List<BusinessCreditIdentity> bciList = source.getItem();
			
			for(int i=0;i<=bciList.size();i++){
				target[i] = new BusinessCreditIdentityInfo();
				target[i].setCompanyName(bciList.get(i).getCompanyName());
				target[i].setMarketAccount(bciList.get(i).getMarketAccount());
			}
		      
			return super.performDomainMapping(source, target);
		}

		@Override
		protected BusinessCreditIdentityList performSchemaMapping(BusinessCreditIdentityInfo[] source, BusinessCreditIdentityList target) {
			
			for(int i=0;i<=source.length;i++){
				BusinessCreditIdentity bci = new BusinessCreditIdentity();
				bci.setCompanyName(source[0].getCompanyName());
				bci.setMarketAccount(source[0].getMarketAccount());
				target.getItem().add(bci);
			}
			
			return super.performSchemaMapping(source, target);
		}
	}
	
}
