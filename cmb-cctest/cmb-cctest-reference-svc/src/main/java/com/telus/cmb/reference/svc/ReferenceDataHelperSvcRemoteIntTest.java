package com.telus.cmb.reference.svc;

import org.junit.Before;
import org.junit.Test;

import com.telus.api.TelusAPIException;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.util.RemoteBeanProxyFactory;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;


public class ReferenceDataHelperSvcRemoteIntTest extends ReferenceDataHelperSvcIntTest{

	@Before
	public void init() {
		
		//String url ="t3://wldv103umgenutilsvc:20152"; 
		String url ="t3://localhost:7001"; 
//		String url ="t3://sn25257:30152"; 
		//String url ="t3://wlpt148umgenutilsvc.tmi.telus.com:30152"; 
		
		System.out.println( "ReferenceDataHeler URL: " + url );
		service = (ReferenceDataHelper) RemoteBeanProxyFactory.createProxy(
				ReferenceDataHelper.class, 
					"ReferenceDataHelper#com.telus.cmb.reference.svc.impl.ReferenceDataHelperHome", 
					url);
	}

	@Test
	public void testRetrieveWPSFeaturesList()  {
		try {
			
			System.out.println("Starting retrieveWPSFeaturesList()...");
			
			Object [] result = service.retrieveWPSFeaturesList();
			System.out.println("Retrieved [" + result.length + "] features.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	@Test
	public void testRetrieveRegularServices()  {
		
		
		long start = System.currentTimeMillis();

		try {
			
			System.out.println("Starting retrieveRegularServices()...");
			
			ServiceInfo [] result = service.retrieveRegularServices();
			System.out.println("Retrieved [" + result.length + "] services in [" + (System.currentTimeMillis() - start) + "] msec.");
			
		} catch (Exception e) {
			System.out.println("Exception [" + e.getMessage()+ "] after [" + (System.currentTimeMillis() - start) + "] msec.");
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testRetrievePricePlan() throws Exception {
		PricePlanInfo info = service.retrievePricePlan("");
		writeResults("retrievePricePlan(String)", info, true);
	}
	
	@Test
	public void testRetrievePricePlan2() throws Exception {
		PricePlanInfo info = service.retrievePricePlan(null, "ABC", "ON", 'I', 'R', 1);
		writeResults("retrievePricePlan(String)", info, true);
	}
	
	@Test
	public void testRetrieveServiceGroupRelation()throws Exception {
		
		
			System.out.println("Start testRetrieveServiceGroupRelation");
			
			String[] result=service.retrieveServiceGroupRelation("FPUEWQ   ");
			System.out.println("OUTPUT : "+result.length);
					
			System.out.println("[");
			for(int i=0;i<result.length;i++){
				System.out.print(result[i]+" ");
			}
			System.out.println("]");
		
		System.out.println("End testRetrieveServiceGroupRelation");
	}
	
	
	@Test
	public void testRetrieveServiceTerm() throws Exception{
		System.out.println("testRetrieveServiceTerm  start");
		try{
			
			String serviceCode="SAIR2T1  ";
			ServiceTermDto seTermDto= service.retrieveServiceTerm(serviceCode);
			System.out.println("OUTPUT : "+seTermDto.toString());
			
		} catch (TelusException e) {
			e.printStackTrace();
		}
		System.out.println("testRetrieveServiceTerm  End");
		
	}
	
	@Test
	public void testRetrievePricePlanList() throws TelusAPIException {
		
		String productType = "C";
		String province = "ON";
		char accountType = 'B';
		char accountSubType = 'A';
		String networkType = NetworkType.NETWORK_TYPE_ALL;
		String equipmentType = "P";
		int brandId = Brand.BRAND_ID_ALL;
		
		findPricePlansTbs(productType, province, accountType, accountSubType, brandId, equipmentType, networkType);
	}
	private void findPricePlansTbs(String productType, String province, char accountType, char accountSubType, int brandId,	String equipmentType, String networkType) throws TelusAPIException {
		
		boolean currentOnly = true;
		boolean activationOnly  = true;

		StringBuffer sb  = new StringBuffer("Search price plan criteria: \n");
		sb.append("productType [").append( productType ).append("]\n")
		.append("province [").append(province).append("]\n")
		.append("accountType [").append(accountType).append("/").append(accountSubType).append("]\n")
		.append("brand [").append(brandId).append("]\n")
		.append("networkType [").append(networkType).append("]\n")
		.append("equipmentType [").append(equipmentType).append("]\n")
		.append("currentOnly [ " ) .append( currentOnly ).append("]\n")
		.append("activationOnly [ " ) .append( activationOnly ).append("]\n")
		;
		
		System.out.println( sb );
		
		PricePlanSummary[] pricePlans =service.retrievePricePlanList(productType, equipmentType, province, accountType, accountSubType, brandId, currentOnly, activationOnly, null, null, networkType);
		writeResults("retrievePricePlanList(inlineSQL)", pricePlans, true);
		
		pricePlans = service.retrievePricePlanListByAccType(productType, province, accountType, equipmentType, brandId, currentOnly, activationOnly, networkType);
		writeResults("retrievePricePlanList(package)", pricePlans, true);
		


	}
	
	
}
