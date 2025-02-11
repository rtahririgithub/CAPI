package com.telus.cmb.reference.svc;

import org.junit.Before;
import org.junit.Test;

import com.telus.api.util.RemoteBeanProxyFactory;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.framework.exception.TelusException;


public class ReferenceDataHelperSvcRemoteTest extends ReferenceDataHelperSvcTest{

	@Before
	public void init() {
		service = (ReferenceDataHelper) RemoteBeanProxyFactory.createProxy(
				ReferenceDataHelper.class, 
					"ReferenceDataHelper#com.telus.cmb.reference.svc.impl.ReferenceDataHelperHome", 
					"t3://um-generalutilities-pt148.tmi.telus.com:30152");
//				"t3://sn25617:13301");
//				"t3://wlpt148umgenutilsvc.tmi.telus.com:30152");
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
	
//	@Test
//	public void testRetrieveRegularServices()  {
//		
//		
//		long start = System.currentTimeMillis();
//
//		try {
//			
//			System.out.println("Starting retrieveRegularServices()...");
//			
//			ServiceInfo [] result = service.retrieveRegularServices();
//			System.out.println("Retrieved [" + result.length + "] services in [" + (System.currentTimeMillis() - start) + "] msec.");
//			
//		} catch (Exception e) {
//			System.out.println("Exception [" + e.getMessage()+ "] after [" + (System.currentTimeMillis() - start) + "] msec.");
//			e.printStackTrace();
//		}
//	}
	
	
//	@Test
//	public void testRetrievePricePlan() throws Exception {
//		PricePlanInfo info = service.retrievePricePlan("PTLK75CF");
//		String [] types = info.getAllNetworkTypes();
//		for (String type : types) {
//			System.out.println(type);
//		}
//		writeResults("retrievePricePlan(String)", service.retrievePricePlan("PTLK75CF"), true);
//	}
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
	
}
