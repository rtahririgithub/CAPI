package com.telus.cmb.reference.svc;

import org.junit.BeforeClass;
import org.junit.Test;

import com.telus.api.account.AccountSummary;
import com.telus.api.account.Subscriber;
import com.telus.api.util.RemoteBeanProxyFactory;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.utility.info.LanguageInfo;
import com.telus.eas.utility.info.MigrationTypeInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.PricePlanSelectionCriteriaInfo;
import com.telus.eas.utility.info.SeatTypeInfo;

public class ReferenceDataFacadeRemoteTest {

	static ReferenceDataFacade facade;
	
	@BeforeClass
	public static void prepare() {
		
//		service = (ReferenceDataSvc) RemoteBeanProxyFactory.createProxy(
//				ReferenceDataSvc.class, 
//				"ReferenceDataSvc#com.telus.cmb.reference.svc.impl.ReferenceDataSvcHome", 
//				"t3://wldv103umgenutilsvc:20152");

//		facade = (ReferenceDataFacade) RemoteBeanProxyFactory.createProxy(
//				ReferenceDataFacade.class, 
//				"ReferenceDataFacade#com.telus.cmb.reference.svc.impl.ReferenceDataFacadeHome", 
//				"t3://um-generalutilities-pt148.tmi.telus.com:30152");
		
		facade = (ReferenceDataFacade) RemoteBeanProxyFactory.createProxy(ReferenceDataFacade.class, 
				"ReferenceDataFacade#com.telus.cmb.reference.svc.impl.ReferenceDataFacadeHome", 
				"t3://localhost:7001");	
	}

	@Test
	public void getAccountTypes() throws Exception {
		System.out.println(facade.getAccountTypes());
	}
	
	@Test
	public void getLanguages() throws Exception {
		System.out.println("Testing getLanguages() method...");
		for (LanguageInfo language : facade.getLanguages()) {
			System.out.println("Code = " + language.getCode());
			System.out.println("Description = " + language.getDescription());
			System.out.println("French Description = " + language.getDescriptionFrench());
		}
		System.out.println("End testing getLanguages() method...");
	}
	
	@Test
	public void getMigrationTypes() throws Exception {
		System.out.println("Testing getMigrationTypes() method...");
		for (MigrationTypeInfo type : facade.getMigrationTypes()) {
			System.out.println("Code = " + type.getCode());
			System.out.println("Description = " + type.getDescription());
			System.out.println("French Description = " + type.getDescriptionFrench());
		}
		System.out.println("End testing getMigrationTypes() method...");
	}
	
	@Test
	public void getSeatTypes() throws Exception {
		System.out.println("Testing getSeatTypes() method...");
		for (SeatTypeInfo seat : facade.getSeatTypes()) {
			System.out.println("Code = " + seat.getCode());
			System.out.println("Description = " + seat.getDescription());
			System.out.println("French Description = " + seat.getDescriptionFrench());
		}
		System.out.println("End testing getSeatTypes() method...");
	}
	
	@Test
	public void getServiceCodesByGroup() throws Exception {
		System.out.println("Testing getServiceCodesByGroup() method...");
		String serviceGroupCode = "FPUEWQ";
		String[] result = facade.getServiceCodesByGroup(serviceGroupCode);
		System.out.println("Result length :"+result.length);
		System.out.println("End testing getServiceCodesByGroup() method...");
	}
	
	@Test
	public void getServiceTerm() throws Exception  {
		System.out.println("Testing getServiceTerm() method...");
		String serviceCode="SAIR2T1";		
		ServiceTermDto result = facade.getServiceTerm(serviceCode);
		System.out.println("RESULT : " + result.toString());
		System.out.println("End testing getServiceTerm() method...");
	}
	
	@Test
	public void getPricePlans() throws Exception  {
		System.out.println("Testing getPricePlans(PricePlanSelectionCriteriaInfo criteriaInfo) method...");
		PricePlanSelectionCriteriaInfo criteriaInfo = new PricePlanSelectionCriteriaInfo();
		criteriaInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_CONSUMER);
		criteriaInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
		criteriaInfo.setProductType(Subscriber.PRODUCT_TYPE_PCS);
		criteriaInfo.setProvinceCode("ON");
		criteriaInfo.setCurrentPlansOnly(false);
		criteriaInfo.setAvailableForActivationOnly(false);
		for (PricePlanInfo plan : facade.getPricePlans(criteriaInfo)) {
			System.out.println("Code = " + plan.getCode());
			System.out.println("Description = " + plan.getDescription());
			System.out.println("French Description = " + plan.getDescriptionFrench());
		}
		System.out.println("End testing getPricePlans() method...");
	}

	@Test
	public void testGetRegularService() throws Exception {
		System.out.println(facade.getRegularService("SWAF0T7"));
	}

}	