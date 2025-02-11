package com.telus.api.reference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.provider.reference.TMPricePlan;
import com.telus.provider.reference.TMServiceSummary;

public class TestBillCycleTreatmentCode extends TestCase {
	
        
    public TestBillCycleTreatmentCode(String name) throws Throwable {
        super(name);
    }
	
    private ClientAPI api;
	
	private static final String ALT_PROPERTIES_FILEPATH = "..\\test.properties";
	
	public void setUp(){
		String propertiesFilePath = System.getProperty("propertiesFile");
		File file=null;
		InputStream in=null;
		
		if (propertiesFilePath != null)
			file = new File(propertiesFilePath);
		
		if (file == null) {
			in = this.getClass().getResourceAsStream(ALT_PROPERTIES_FILEPATH);
		}else {
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				in = this.getClass().getResourceAsStream(ALT_PROPERTIES_FILEPATH);
			}	
		}
		
        System.out.println("setup called");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
	
		//Added for the ant build script, its not the taking the ALT_PROPERTIES_FILEPATH and 'in' is null
		if(in==null){
			file = new File("./src/test/com/telus/api/test.properties");
			try {
				in= new FileInputStream(file);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} 
		}
		
		assertNotNull(in);
		try {
			initSystemPropertiesFromInputStream(in);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}finally {
			in = null;
			file = null;			
		}
		
		System.out.println("**** LDAP URL set to: " + System.getProperty("com.telusmobility.config.java.naming.provider.url"));
		System.out.println("**** PROVIDER URL set to: " + System.getProperty("com.telus.provider.providerURL"));
		System.out.println("**** HELPER URL set to: " + System.getProperty("cmb.services.ReferenceDataHelper.url"));
		System.out.println("**** FACADE URL set to: " + System.getProperty("cmb.services.ReferenceDataFacade.url"));
		
		try {			
			if (api == null) 
				api = ClientAPI.getInstance("18654", "apollo", "OLN");			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("setup completed");
	}
    
    public void tearDown(){
        System.out.println("tearDown called");
        api = null;
        System.out.println("tearDown completed");
    }
   
    private void initSystemPropertiesFromInputStream(InputStream in) throws Throwable {

    	Properties properties = new Properties();
    	properties.load(in);
    	System.setProperty("com.telusmobility.config.java.naming.provider.url", properties.getProperty("LDAP.URL", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration"));
		System.setProperty("com.telus.provider.providerURL", properties.getProperty("PROVIDER.URL", "t3://wlqaeaseca:8682"));
//		System.setProperty("cmb.services.ReferenceDataHelper.url", properties.getProperty("REF.DATA.HELPER.URL", "t3://localhost:7001"));
//		System.setProperty("cmb.services.ReferenceDataFacade.url", properties.getProperty("REF.DATA.FACADE.URL", "t3://localhost:7001"));
        
    }
    
    
    public void testRetrieveBillCycleTreatmentCode() {
    	System.out.println("Start testRetrieveServiceCodesByGroup ");
    	try {

    		PricePlanSelectionCriteria criteria = new PricePlanSelectionCriteria();
    		criteria.setEquipmentType("D");
    		criteria.setNetworkType("C");
    		criteria.setAccountType(new Character('I'));
    		criteria.setAccountSubType(new Character('R'));
    		criteria.setProvinceCode("NS");
    		criteria.setProductType("C");
    		criteria.setBrandId(new Integer(1));
    		criteria.setAvailableForActivationOnly(new Boolean(true));
    		criteria.setCurrentPlansOnly(new Boolean(true));
    		
    		PricePlanSummary[] pricePlans = api.getReferenceDataManager().findPricePlans(criteria);
    		for (int i = 0; i < pricePlans.length; i++) {
    			String  BillCycleTreatmentCode= ((TMServiceSummary)pricePlans[i]).getDelegate().getBillCycleTreatmentCode();
    			System.out.println("pricePlans code : "+pricePlans[i].getCode()+", BillCycleTreatmentCode :["+BillCycleTreatmentCode+"]");
    		}
    		
    		System.out.println("End testRetrieveServiceCodesByGroup ");
    	}catch (TelusAPIException e) {
    		e.printStackTrace();
		}
 
 }
    public void testRetrieveRegularServices() {
    	System.out.println("Start testRetrieveRegularServices ");
    	
    	String featureCategory=null;
		String productType=null;
		boolean current=false;
    	
    	try {
    		Service[] result=	api.getReferenceDataManager().getServicesByFeatureCategory(featureCategory, productType, current);
    		System.out.println("Length : "+result.length);
    		for(int i=0;i<result.length;i++){
				System.out.println("Code : "+result[i].getCode()+", BillCycleTreatmentCode : "+result[i].getBillCycleTreatmentCode());
			}
		} catch (TelusAPIException e) {
			e.printStackTrace();
		}
    	
    	System.out.println("End testRetrieveRegularServices ");
    	
    }

    public void testRetrieveIncludedServices() {
    	System.out.println("Start testRetrieveIncludedServices ");
    	String pricePlanCode="TPBSOC45";
    	try {
    		PricePlanSummary plan= 	api.getReferenceDataManager().getPricePlan(pricePlanCode);
			System.out.println("Code : "+plan.getCode()+", BillCycleTreatmentCode: "+plan.getBillCycleTreatmentCode());
		} catch (TelusAPIException e) {
			e.printStackTrace();
		}
    	
    	System.out.println("End testRetrieveIncludedServices ");
    }
    
    public void testRetrieveOptionalServices() {
    	try {
    		ReferenceDataManager rdm=	api.getReferenceDataManager();
				PricePlanSelectionCriteria criteria = new PricePlanSelectionCriteria();
				
				criteria.setEquipmentType("D");
	    		criteria.setNetworkType("C");
	    		criteria.setAccountType(new Character('I'));
	    		criteria.setAccountSubType(new Character('R'));
	    		criteria.setProvinceCode("NS");
	    		criteria.setProductType("C");
	    		criteria.setBrandId(new Integer(1));
	    		criteria.setAvailableForActivationOnly(new Boolean(true));
	    		criteria.setCurrentPlansOnly(new Boolean(true));
	    		
				PricePlanSummary[] pricePlans;
				pricePlans = rdm.findPricePlans(criteria);
				System.out.println("Test retrieveOptionalServices()");
				for (int i = 0; i < pricePlans.length; i++) {
					PricePlan pricePlan = rdm.getPricePlan(pricePlans[i].getCode(), "D","NS", 'I', 'R', 1);
					System.out.println("Code : "+pricePlan.getCode()+", BillCycleTreatmentCode : "+ pricePlan.getBillCycleTreatmentCode());
					
				}
				
				System.out.println("Test retrievePricePlan(pricPlanCode)");
				for (int i = 0; i < pricePlans.length; i++) {
					PricePlanSummary pricePlan = rdm.getPricePlan(pricePlans[i].getCode());
					System.out.println("Code : "+pricePlan.getCode()+", BillCycleTreatmentCode : "+ pricePlan.getBillCycleTreatmentCode());
					
				}
				
    	} catch (TelusAPIException e) {
    		e.printStackTrace();
    	}
    }
    
    public void testRetrievePricePlanList() {
    	System.out.println("Start  testRetrievePricePlanList ");
    PricePlanSummary[] plan;
    PricePlanSelectionCriteria crit = new PricePlanSelectionCriteria();
	
    try {
		plan = api.getReferenceDataManager().findPricePlans(crit);
		for (int i=0; i < plan.length; i++){
			System.out.println("Price Plan #" + i + " -> " + plan[i].getCode().trim() + "; BillCycleTreatmentCode = " + plan[i].getBillCycleTreatmentCode());
	    }
	} catch (TelusAPIException e) {
		e.printStackTrace();
	}
    
    System.out.println("End testRetrievePricePlanList ");
    }
    
    
    public void testRetrievePricePlanList1() throws Throwable {
	   	
    	System.out.println("***** START OF TEST RUN testRetrievePricePlanList1****");
    	
		String equipmentType="D";
		String networkType="C";
		char accountType='I';
		char accountSubType='R';
		String provinceCode="NS";
		String productType="C";
		int brandId=1;
		long[] pProductPromoTypeList=null;
		boolean initialActivation=true;
		
		PricePlanSummary[] plan = 
    		api.getReferenceDataManager().findPricePlans(productType, provinceCode, accountType, accountSubType,
				 equipmentType, pProductPromoTypeList, initialActivation, brandId,  networkType);
		PricePlanSummary pricePlanSummary=null;
		
    	for (int i=0; i < plan.length; i++){
    		//System.out.println("Price Plan #" + i + " -> " + plan[i].getCode().trim() + "; BillCycleTreatmentCode = " + plan[i].getBillCycleTreatmentCode());
    		 pricePlanSummary =api.getReferenceDataManager().getPricePlan( plan[i].getCode());
    	}
    	System.out.println("***** END OF TEST RUN testRetrievePricePlanList1****");
    } 
    
    public void testRetrieveIncludedPromotions(){
    	
    	String equipmentType="D";
		String networkType="C";
		char accountType='I';
		char accountSubType='R';
		String provinceCode="NS";
		String productType="C";
		int brandId=1;
		long[] pProductPromoTypeList=null;
		boolean initialActivation=true;
		
		try {
			PricePlanSummary[] plan = 
				api.getReferenceDataManager().findPricePlans(productType, provinceCode, accountType, accountSubType,
					 equipmentType, pProductPromoTypeList, initialActivation, brandId,  networkType);
			
			//plan[i].getI
		} catch (TelusAPIException e) {
			e.printStackTrace();
		}
    	
    	
    }
    
    public void testGetIncludedPromotions() throws Throwable {
    	System.out.println("***** START OF TEST RUN testGetIncludedPromotions****");
    	
		String equipmentType="D";
		String networkType="C";
		char accountType='I';
		char accountSubType='R';
		String provinceCode="NS";
		String productType="C";
		int brandId=1;
		long[] pProductPromoTypeList={0};
		boolean initialActivation=true;
		
		PricePlanSummary[] plan = 
    		api.getReferenceDataManager().findPricePlans(productType, provinceCode, accountType, accountSubType,
				 equipmentType, pProductPromoTypeList, initialActivation, brandId,  networkType);
		
		
    	for (int i=0; i < plan.length; i++){
    	   TMPricePlan pp=(TMPricePlan) api.getReferenceDataManager().getPricePlan(plan[i].getCode(), equipmentType, provinceCode, accountType, accountSubType);
    	   
    	   Service[]service = pp.getIncludedPromotions(networkType, equipmentType, provinceCode, 0);
    	   for (int k=0; k < service.length; k++){
	    		System.out.println("Included promotions for "+plan[i].getCode()+"is :"+service[k].getCode());
	    		System.out.println("BillCycleTreatmentCode : "+service[k].getBillCycleTreatmentCode());
	    	}
    	}
    	System.out.println("***** END OF TEST RUN testGetIncludedPromotions****");	
    
    }
}

   


