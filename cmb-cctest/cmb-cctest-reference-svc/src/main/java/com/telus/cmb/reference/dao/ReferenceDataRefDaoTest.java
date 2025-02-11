package com.telus.cmb.reference.dao;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.reference.NetworkType;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.utility.info.DiscountPlanInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;

//import com.telus.eas.utility.info.SocFamilyTypeInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-datasources-pt148.xml"})
public class ReferenceDataRefDaoTest {

	@Autowired
	ReferenceDataRefDao dao;
	
	
	
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration"); 
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}
	
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
		String pricePlanCode="PTKNA250";
		//String pricePlanCode="4PTLKBC";
		ServiceInfo[] info= dao.retrieveIncludedServices(pricePlanCode);
		if(info!=null && info.length>0){
			System.out.println("info length :"+info.length);
			for(int i=0; i<info.length; i++){
				ServiceInfo serviceInfo = info[i];
				System.out.println(i
						+ " soc[" + serviceInfo.getCode()+"] " + serviceInfo.getDescription() 
						+ "; brand: " + serviceInfo.getBrandId()
						+" billCycleTreatmentCode : "+serviceInfo.getBillCycleTreatmentCode()
						);
			}
		}
		System.out.println("testRetrieveIncludedServices  End");
	}
	
	
	@Test
	public void testRetrievePricePlan1(){
		
		System.out.println("testRetrievePricePlan  Start");
		String pricePlanCode="3PDATCOB2";
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
		
		
		Collection<PricePlanInfo> info= dao.retrievePricePlanList(productType, equipmentType, provinceCode, accountType, accountSubType, 
				pBrandId, currentPricePlansOnly, availableForActivationOnly, term, activityCode, activityReasonCode, networkType, null);
				
				
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
			System.out.println("priceplan code : "+serviceInfo.getCode() +
					"brand: " + serviceInfo.getBrandId() +
					" familyTypes " + Arrays.asList( serviceInfo.getFamilyTypes()) );
		}
		
		System.out.println("testRetrieveRegularServices  End");
	}
	
	
	@Test
	public void testRetrieveOptionalServices(){
		System.out.println("testRetrieveOptionalServices  Start");
		
		//String pricePlanCode="PCMS30B4"; String provinceCode="ON"; String equipmentType="D";
		String pricePlanCode="4PTLKBC"; String provinceCode="BC"; String equipmentType="D";
		String networkType=NetworkType.NETWORK_TYPE_ALL;
		char accountType='I';char accountSubType='R';
		
		
		ServiceInfo[] info= dao.retrieveOptionalServices(pricePlanCode, equipmentType, provinceCode, accountType, accountSubType, networkType);
				
		if(info!=null && info.length>0){
			for(int i=0; i<info.length; i++){
				ServiceInfo serviceInfo = info[i];
				System.out.println(i
						+ " SOC[" + serviceInfo.getCode()+"] " 
						+ serviceInfo.getServiceType() 
						+ " \"" + serviceInfo.getDescription() + "\"" 
						+ ", brand: " + serviceInfo.getBrandId()
						+", billCycleTreatmentCode : "+serviceInfo.getBillCycleTreatmentCode()
						);
			}
		}
		
		System.out.println("testRetrieveOptionalServices  End");
	}
	
	@Test
	public void testRetrieveIncludedPromotions(){
		System.out.println("testRetrieveIncludedPromotions  Start");
		
		//String pricePlanCode="PRFV80B4"; String provinceCode="ON"; int term=12;
		String pricePlanCode="4PTLKBC" ; String provinceCode="BC"; int term=0; 
		String equipmentType="D";
		String networkType=NetworkType.NETWORK_TYPE_ALL;
		
		
		List<ServiceInfo> serviceInfoList= dao.retrieveIncludedPromotions(pricePlanCode, equipmentType, networkType, provinceCode, term);
		System.out.println("SIZE: "+serviceInfoList.size());
		for(int i=0; i <serviceInfoList.size();i++){
			ServiceInfo	serviceInfo=(ServiceInfo) serviceInfoList.get(i);
			System.out.println(i
					+ " soc[" + serviceInfo.getCode()+"] " + serviceInfo.getDescription() 
					+ "; brand: " + serviceInfo.getBrandId()
					+" billCycleTreatmentCode : "+serviceInfo.getBillCycleTreatmentCode()
					);
		}
		System.out.println("testRetrieveIncludedPromotions  End");
	}

	@Test
	public void testretrieveDiscountPlans(){
		System.out.println("testretrieveDiscountPlans  Start");
		boolean current=false;
		String pricePlanCode="1XPDU";
		String provinceCode="ON";	
		long[] productPromoTypes = {99999999l};
		  
		boolean initialActivation=false;
		int term=36;
				
		DiscountPlanInfo[] dpinfo= dao.retrieveDiscountPlans(current,pricePlanCode,provinceCode,
									productPromoTypes,initialActivation,term);
		
		
		if(dpinfo!=null && dpinfo.length>0){
			System.out.println("info length :"+dpinfo.length);
		
			for(int i=0; i<dpinfo.length; i++){
				DiscountPlanInfo DiscountPlanInfo = dpinfo[i];
				System.out.println(i
						+ " soc[" + DiscountPlanInfo.getCode()+"] " 
						+ DiscountPlanInfo.getDescription() 
				);
			}
		
		}
         
		System.out.println("testretrieveDiscountPlans  End");
	}
	
	
	@Test
	public void testRetrievePricePlanListTest(){
		
		System.out.println("testRetrievePricePlanList  Start");
				
		String equipmentType="P";
		String networkType="H";
		char accountType='B';
		char accountSubType='F';
		String provinceCode="BC";
		String productType="C";
		int pBrandId=1;
		boolean currentPricePlansOnly=true;
		boolean availableForActivationOnly=false;
		long[] pProductPromoTypeList=null;
		boolean initialActivation=true;
		int term=0;
		String activityCode="SUS";
		String activityReasonCode="VAD ";
		String seatType = "MOBL";
		
		
		Collection<PricePlanInfo> info= dao.retrievePricePlanList(productType, equipmentType, provinceCode, accountType, accountSubType, 
				pBrandId,  pProductPromoTypeList, initialActivation,currentPricePlansOnly, availableForActivationOnly, term, activityCode, 
				activityReasonCode, networkType, seatType, null, null, null);
				
				
		Iterator<PricePlanInfo> iterator= info.iterator();
		while(iterator.hasNext()){
			PricePlanInfo pricePlanInfo= (PricePlanInfo) iterator.next();
			System.out.println("priceplan code : "+pricePlanInfo.getCode());
		}
		
		System.out.println("testRetrievePricePlanList  End");
		
	}
	
}
