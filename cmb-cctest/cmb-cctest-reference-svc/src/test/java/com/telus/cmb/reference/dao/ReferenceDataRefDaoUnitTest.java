package com.telus.cmb.reference.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao.xml", "classpath:application-context-datasources-dv103.xml"})

public class ReferenceDataRefDaoUnitTest {

	@Autowired
	ReferenceDataRefDao dao;
	
	@Test
	public void testRetrievePricePlan() {
		
		PricePlanInfo info = dao.retrievePricePlan("PYCTX30B4"); 
		
		System.out.println(info.getAllNetworkTypes());
	}
	
	@Test
	public void testRetrieveServiceGroupRelation() {
		System.out.println("testRetrieveServiceGroupRelation  start");
		List<String> result=dao.retrieveServiceGroupRelation("FPUEWQ   ");
		System.out.println("OUTPUT : "+result.size());
		
	
		System.out.println("[");
		for(int i=0;i<result.size();i++){
			System.out.print(result.get(i)+" ");
			
		}
		System.out.println("]");
	System.out.println("testRetrieveServiceGroupRelation  end");
	}
	
	@Test
	public void testRetrieveServiceTerm() {
		System.out.println("testRetrieveServiceTerm  start");
		
		String serviceCode="SAIR2T1  ";
		ServiceTermDto seTermDto= dao.retrieveServiceTerm(serviceCode);
		System.out.println("OUTPUT : "+seTermDto.toString());
		
		System.out.println("testRetrieveServiceTerm  End");
		
	}
	
	@Test
	public void testRetrieveIncludedServices(){
		System.out.println("testRetrieveIncludedServices  Start");
//		String pricePlanCode="TPBSOC41";
		String pricePlanCode="TPBSOC45";
		ServiceInfo[] info= dao.retrieveIncludedServices(pricePlanCode);
		if(info!=null && info.length>0){
			System.out.println("info length :"+info.length);
			for(int i=0; i<info.length; i++){
				System.out.println(i+" BillCycleTreatmentCode : "+info[i].getBillCycleTreatmentCode());
			}
		}
		System.out.println("testRetrieveIncludedServices  End");
	}
	
	
	@Test
	public void testRetrievePricePlan1(){
		
		System.out.println("testRetrievePricePlan  Start");
		String pricePlanCode="TPBSOC41";
//		String pricePlanCode="TPBSOC45";
		PricePlanInfo info= dao.retrievePricePlan(pricePlanCode);
		System.out.println(" BillCycleTreatmentCode : "+info.getBillCycleTreatmentCode());
		System.out.println("testRetrievePricePlan  End");
	}
	
	
	@Test
	public void testRetrievePricePlanList(){
		
		System.out.println("testRetrievePricePlanList  Start");
				
		String equipmentType="D";
		String networkType="C";
		char accountType='I';
		String provinceCode="NS";
		String productType="C";
		int brandId=1;
		boolean currentPlansOnly=true;
		boolean availableForActivationOnly=true;
		
		Collection<PricePlanInfo> info= dao.retrievePricePlanList(productType, provinceCode, accountType, equipmentType, brandId, currentPlansOnly, availableForActivationOnly, networkType);
				
		Iterator<PricePlanInfo> iterator= info.iterator();
		while(iterator.hasNext()){
			PricePlanInfo pricePlanInfo= (PricePlanInfo) iterator.next();
			System.out.println("priceplan code : "+pricePlanInfo.getCode()+", BillCycleTreatmentCode : "+pricePlanInfo.getBillCycleTreatmentCode());
			System.out.println("Term :"+pricePlanInfo.getMaxTerm());
		}
		
		System.out.println("testRetrievePricePlanList  End");
		
	}
	
	
	@Test
	public void testRetrievePricePlanList1(){
		
		System.out.println("testRetrievePricePlanList  Start");
				
		String equipmentType="D";
		String networkType="C";
		char accountType='I';
		char accountSubType='R';
		String provinceCode="NS";
		String productType="C";
		int pBrandId=1;
		boolean currentPricePlansOnly=false;
		boolean availableForActivationOnly=false;
		long[] pProductPromoTypeList=null;
		boolean initialActivation=true;
		int term=0;
		String activityCode="";
		String activityReasonCode="";
		String seatType = null;
		
		Collection<PricePlanInfo> info= dao.retrievePricePlanList(productType, equipmentType, provinceCode, 
				accountType, accountSubType, pBrandId, pProductPromoTypeList, initialActivation, 
				currentPricePlansOnly, availableForActivationOnly, term, activityCode, activityReasonCode, 
				networkType, seatType, null, null, null);
				
		Iterator<PricePlanInfo> iterator= info.iterator();
		while(iterator.hasNext()){
			PricePlanInfo pricePlanInfo= (PricePlanInfo) iterator.next();
			System.out.println("BillCycleTreatmentCode : "+pricePlanInfo.getBillCycleTreatmentCode());
		}
		
		System.out.println("testRetrievePricePlanList  End");
		
	}
	
	
	@Test
	public void testRetrieveRegularServices(){
		
		System.out.println("testRetrieveRegularServices  Start");
		
		String featureCategory="";
		String productType="C";
		boolean available=true;
		boolean current=true;
		
		Collection<ServiceInfo> info= dao.retrieveRegularServices(featureCategory, productType, available, current);
				
		Iterator<ServiceInfo> iterator= info.iterator();
		while(iterator.hasNext()){
			ServiceInfo serviceInfo= (ServiceInfo) iterator.next();
			System.out.println("priceplan code : "+serviceInfo.getCode()+",BillCycleTreatmentCode : "+serviceInfo.getBillCycleTreatmentCode());
		}
		
		System.out.println("testRetrieveRegularServices  End");
	}
	
	
	@Test
	public void testRetrieveOptionalServices(){
		System.out.println("testRetrieveOptionalServices  Start");
		
		String pricePlanCode="FET105   ";
		String equipmentType="D";
		String networkType="C";
		char accountType='I';
		char accountSubType='R';
		String provinceCode="ON";
		
		ServiceInfo[] info= dao.retrieveOptionalServices(pricePlanCode, equipmentType, provinceCode, accountType, accountSubType, networkType);
				
		if(info!=null && info.length>0){
			for(int i=0; i<info.length; i++){
				System.out.println("Code: "+info[i].getCode()+", BillCycleTreatmentCode : "+info[i].getBillCycleTreatmentCode());
			}
		}
		
		System.out.println("testRetrieveOptionalServices  End");
	}
	
	@Test
	public void testRetrieveIncludedPromotions(){
		System.out.println("testRetrieveIncludedPromotions  Start");
		
		String pricePlanCode="TPBSOC42 ";
		String equipmentType="D";
		String networkType="C";
		String provinceCode="NS";
		int term=0;
		
		List<ServiceInfo> serviceInfoList= dao.retrieveIncludedPromotions(pricePlanCode, equipmentType, networkType, provinceCode, term);
		System.out.println("SIZE: "+serviceInfoList.size());
		for(int i=0; i <serviceInfoList.size();i++){
			ServiceInfo	serviceInfo=(ServiceInfo) serviceInfoList.get(i);
			System.out.println("BillCycleTreatmentCode : "+serviceInfo.getBillCycleTreatmentCode());
		}
		System.out.println("testRetrieveIncludedPromotions  End");
	}
	
	
	

	

	
	
	
	
	
	
}
