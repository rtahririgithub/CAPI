package com.telus.api.reference;

import com.telus.api.reference.Brand;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.BaseTest;

public class TestFindPricePlans_HolbornR1 extends BaseTest {
	
    static {
        //setuplocalHost();
    	//setupD1();
    	//setupSMARTDESKTOP_PT168();
    	setupEASECA_QA();
    }

    /* constants */
    public static final String PRODUCT_TYPE = "C";
    public static final String PROVINCE_CD = "ON";
    public static final char ACCOUNT_TYPE = 'I';
    public static final char ACCOUNT_SUB_TYPE = 'R';
    public static final String EQUIPMENT_TYPE = "D";
    public static final long[] PRODUCT_PROMO_TYPE_LIST = {0};
    public static final boolean INITIAL_ACTIVATION = true;
    public static final boolean CURRENT_PLANS_ONLY = true;
    public static final boolean AVAILABLE_FOR_ACTIVATION_ONLY = true;
    public static final String ACTIVITY_CODE = "";
    public static final String ACTIVITY_REASON_CODE = "";
    public static final int TERM = PricePlanSummary.CONTRACT_TERM_ALL;
    public static final int BRAND_ID = Brand.BRAND_ID_KOODO;
    public static final String NETWORK_TYPE = NetworkType.NETWORK_TYPE_ALL; //NetworkType.NETWORK_TYPE_HSPA;
    
    
    public TestFindPricePlans_HolbornR1(String name) throws Throwable {
        super(name);
    }

    
    public void testAll() throws Throwable{
    	_testFindPricePlan_003();
    }
    
   /*==================================================================
    * findPricePlan test cases
    * ================================================================= */   
    
    /* Test Con. ID: findPricePlan.001
     * Action: call TMReferenceDataManager.findPricePlans(String, String, String, char, char, boolean, boolean, String, String, int, String)  method 
     * Parameters: productType, equipmentType, provinceCode, accountType, accountSubType, currentPlansOnly, availableForActivationOnly, activityCode, 
     * 			   activityReasonCode, brandId, networkType
     * Expected Results: PricePlanSummary array of applicable Price Plans (Price Plan SOC has network type matching with networkType parameter). 
     * 					 NOTE: if 9 is passed in for the networkType parameter, validate that this method works as it did prior to Holborn R1 
     * 					 release changes
     */
    public void _testFindPricePlan_001() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN ****"); 
    	
    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(
    				PRODUCT_TYPE, 
    				EQUIPMENT_TYPE, 
    				PROVINCE_CD, 
    				ACCOUNT_TYPE, 
    				ACCOUNT_SUB_TYPE, 
    				CURRENT_PLANS_ONLY, 
    				AVAILABLE_FOR_ACTIVATION_ONLY, 
    				ACTIVITY_CODE, 
    				ACTIVITY_REASON_CODE, 
    				BRAND_ID, 
    				NETWORK_TYPE);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    	    	
    	System.out.println("***** END OF TEST RUN ****"); 
    }

    
    /* Test Con. ID: findPricePlan.002
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, String, int, boolean, boolean, String)  method 
     * Parameters: productType, provinceCode, accountType, equipmentType, brandId, currentPlansOnly, 
     * 			   availableForActivationOnly, networkType
     * Expected Results: PricePlanSummary array of applicable Price Plans (Price Plan SOC has network type matching with 
     * 					 networkType parameter). NOTE: if 9 is passed in for the networkType parameter, validate that this method 
     * 					 works as it did prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_002() throws Throwable {

    	System.out.println("***** START OF TEST RUN ****");
    	PricePlanSelectionCriteria crit = new PricePlanSelectionCriteria();
    	crit.setAccountType(new Character(ACCOUNT_TYPE));
    	crit.setProductType(PRODUCT_TYPE);
    	crit.setProvinceCode(PROVINCE_CD);
    	crit.setNetworkType(NETWORK_TYPE);
    	crit.setEquipmentType(EQUIPMENT_TYPE);
    	//crit.setActivityReasonCode(ACTIVITY_REASON_CODE);
    	crit.setBrandId(new Integer(BRAND_ID));
    	crit.setCurrentPlansOnly(new Boolean(CURRENT_PLANS_ONLY));
    	crit.setAvailableForActivationOnly(new Boolean(AVAILABLE_FOR_ACTIVATION_ONLY));
    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(crit);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim() + "; NetworkType = " 
    				+ plan[i].getNetworkType());

    	System.out.println("***** END OF TEST RUN ****");
    }    

    
    /* Test Con. ID: findPricePlan.003
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, char, String, long[], boolean, int, String) method 
     * Parameters: productType, provinceCode, accountType, accountSubType, equipmentType, productPromoTypeList, initialActivation, 
     * 			   brandId, networkType
     * Expected Results: PricePlanSummary array of applicable Price Plans (Price Plan SOC has network type matching with networkType 
     * 					 parameter). NOTE: if 9 is passed in for the networkType parameter, validate that this method works as it did 
     * 					 prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_003() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN ****");
    	
    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(
    				PRODUCT_TYPE, 
    				PROVINCE_CD, 
    				ACCOUNT_TYPE, 
    				ACCOUNT_SUB_TYPE, 
    				EQUIPMENT_TYPE, 
    				PRODUCT_PROMO_TYPE_LIST, 
    				INITIAL_ACTIVATION, 
    				BRAND_ID, 
    				NETWORK_TYPE);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    	    	
    	System.out.println("***** END OF TEST RUN ****");
    }    

    
    /* Test Con. ID: findPricePlan.004
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, char, String, int, int, String)  method 
     * Parameters: productType, provinceCode, accountType, accountSubType, equipmentType, brandId, term, networkType
     * Expected Results: PricePlanSummary array of applicable Price Plans (Price Plan SOC has network type matching with 
     * 					 networkType parameter). NOTE: if 9 is passed in for the networkType parameter, validate that this 
     * 					 method works as it did prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_004() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN ****");
    	PricePlanSelectionCriteria crit = new PricePlanSelectionCriteria();
    	crit.setAccountType(new Character(ACCOUNT_TYPE));
    	crit.setProductType(PRODUCT_TYPE);
    	crit.setProvinceCode(PROVINCE_CD);
    	crit.setNetworkType(NETWORK_TYPE);
    	crit.setEquipmentType(EQUIPMENT_TYPE);
    	//crit.setActivityReasonCode(ACTIVITY_REASON_CODE);
    	crit.setBrandId(new Integer(BRAND_ID));
    	//crit.setCurrentPlansOnly(new Boolean(CURRENT_PLANS_ONLY));
    	//crit.setAvailableForActivationOnly(new Boolean(AVAILABLE_FOR_ACTIVATION_ONLY));
    	crit.setTerm(new Integer(TERM));
    	crit.setAccountSubType(new Character(ACCOUNT_SUB_TYPE));
    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(crit);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    	    	
    	System.out.println("***** END OF TEST RUN ****");
    }
    
    
    /* Test Con. ID: findPricePlan.005
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, char, String, int, boolean, boolean, String)  method 
     * Parameters: productType, provinceCode, accountType, accountSubType, equipmentType, brandId, currentPlansOnly, 
     * 			   availableForActivationOnly, networkType
     * Expected Results: PricePlanSummary array of applicable Price Plans (Price Plan SOC has network type matching with networkType 
     * 					 parameter). NOTE: if 9 is passed in for the networkType parameter, validate that this method works as it did 
     * 					 prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_005() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN ****");
    	
    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(
    				PRODUCT_TYPE, 
    				PROVINCE_CD, 
    				ACCOUNT_TYPE, 
    				ACCOUNT_SUB_TYPE, 
    				EQUIPMENT_TYPE, 
    				BRAND_ID, 
    				CURRENT_PLANS_ONLY, 
    				AVAILABLE_FOR_ACTIVATION_ONLY, 
    				NETWORK_TYPE);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    	    
    	System.out.println("***** END OF TEST RUN ****");
    }
    
    
    /* Test Con. ID: findPricePlan.006
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, char, String, boolean, boolean, int, int, String)  method 
     * Parameters: productType, provinceCode, accountType, accountSubType, equipmentType, currentPlansOnly, availableForActivationOnly, 
     * 			   brandId, term, networkType 
     * Expected Results: PricePlanSummary array of applicable Price Plans (Price Plan SOC has network type matching with networkType 
     * 					 parameter). NOTE: if 9 is passed in for the networkType parameter, validate that this method works as it did 
     * 					 prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_006() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN ****");
    	
    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(
    				PRODUCT_TYPE, 
    				PROVINCE_CD, 
    				ACCOUNT_TYPE, 
    				ACCOUNT_SUB_TYPE, 
    				EQUIPMENT_TYPE, 
    				CURRENT_PLANS_ONLY, 
    				AVAILABLE_FOR_ACTIVATION_ONLY, 
    				BRAND_ID, 
    				TERM, 
    				NETWORK_TYPE);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    	
    	System.out.println("***** END OF TEST RUN ****");
    }
    
    
    /* Test Con. ID: findPricePlan.007
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, char, int, String, String)  method
     * Parameters: productType, provinceCode, accountType, accountSubType, brandId, equipmentType, networkType
     * Expected Results: PricePlanSummary array of applicable Price Plans (Price Plan SOC has network type matching with 
     * 					 networkType parameter). NOTE: if 9 is passed in for the networkType parameter, validate that this method 
     * 					 works as it did prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_007() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN ****");
    	
    	PricePlanSelectionCriteria crit = new PricePlanSelectionCriteria();
    	crit.setAccountType(new Character(ACCOUNT_TYPE));
    	crit.setProductType(PRODUCT_TYPE);
    	crit.setProvinceCode(PROVINCE_CD);
    	crit.setNetworkType(NETWORK_TYPE);
    	crit.setEquipmentType(EQUIPMENT_TYPE);
    	//crit.setActivityReasonCode(ACTIVITY_REASON_CODE);
    	crit.setBrandId(new Integer(BRAND_ID));
    	//crit.setCurrentPlansOnly(new Boolean(CURRENT_PLANS_ONLY));
    	//crit.setAvailableForActivationOnly(new Boolean(AVAILABLE_FOR_ACTIVATION_ONLY));
    	//crit.setTerm(new Integer(TERM));
    	//crit.setAccountSubType(new Character(ACCOUNT_SUB_TYPE));

    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(crit);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    	    	
    	System.out.println("***** END OF TEST RUN ****");
    }
    
    
    /* Test Con. ID: findPricePlan.008
     * Action: call TMReferenceDataManager.findPricePlans(String, String, String, char, char, boolean, boolean, String, String, int)  method 
     * Parameters: productType, equipmentType, provinceCode, accountType, accountSubType, currentPlansOnly, availableForActivationOnly, 
     * 			   activityCode, activityReasonCode, brandId
     * Expected Results: PricePlanSummary array of applicable Price Plans. NOTE: this method is deprecated and does not accept a networkType 
     * 			parameter. Validate that method works as it did prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_008() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN ****");
    	PricePlanSelectionCriteria crit = new PricePlanSelectionCriteria();
    	crit.setAccountType(new Character(ACCOUNT_TYPE));
    	crit.setProductType(PRODUCT_TYPE);
    	crit.setProvinceCode(PROVINCE_CD);
    	crit.setNetworkType(NETWORK_TYPE);
    	crit.setEquipmentType(EQUIPMENT_TYPE);
    	crit.setActivityReasonCode(ACTIVITY_REASON_CODE);
    	crit.setBrandId(new Integer(BRAND_ID));
    	crit.setCurrentPlansOnly(new Boolean(CURRENT_PLANS_ONLY));
    	crit.setAvailableForActivationOnly(new Boolean(AVAILABLE_FOR_ACTIVATION_ONLY));
    	//crit.setTerm(new Integer(TERM));
    	crit.setAccountSubType(new Character(ACCOUNT_SUB_TYPE));

    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(crit);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    	    	
    	System.out.println("***** END OF TEST RUN ****");
    }
    
    
    /* Test Con. ID: findPricePlan.009
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, String, int, boolean, boolean)  method 
     * Parameters: productType, provinceCode, accountType, accountSubType, equipmentType, productPromoTypeList, initialActivation, brandId
     * Expected Results: PricePlanSummary array of applicable Price Plans. NOTE: this method is deprecated and does not accept a 
     * 					 networkType parameter. Validate that method works as it did prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_009() throws Throwable {
	   	
    	System.out.println("***** START OF TEST RUN ****");
    	PricePlanSelectionCriteria crit = new PricePlanSelectionCriteria();
    	crit.setAccountType(new Character(ACCOUNT_TYPE));
    	crit.setProductType(PRODUCT_TYPE);
    	crit.setProvinceCode(PROVINCE_CD);
    	//crit.setNetworkType(NETWORK_TYPE);
    	crit.setEquipmentType(EQUIPMENT_TYPE);
    	//crit.setActivityReasonCode(ACTIVITY_REASON_CODE);
    	crit.setBrandId(new Integer(BRAND_ID));
    	crit.setCurrentPlansOnly(new Boolean(CURRENT_PLANS_ONLY));
    	crit.setAvailableForActivationOnly(new Boolean(AVAILABLE_FOR_ACTIVATION_ONLY));
    	//crit.setTerm(new Integer(TERM));
    	//crit.setAccountSubType(new Character(ACCOUNT_SUB_TYPE));

    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(crit);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim() + "; NetworkType = " 
    				+ plan[i].getNetworkType());
    	
    	System.out.println("***** END OF TEST RUN ****");
    } 
    
    
    /* Test Con. ID: findPricePlan.010
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, char, String, long[], boolean, int)  method 
     * Parameters: productType, provinceCode, accountType, accountSubType, equipmentType, productPromoTypeList, initialActivation, brandId
     * Expected Results: PricePlanSummary array of applicable Price Plans. NOTE: this method is deprecated and does not accept a 
     * 					 networkType parameter. Validate that method works as it did prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_010() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN ****");
    	PricePlanSelectionCriteria crit = new PricePlanSelectionCriteria();
    	crit.setAccountType(new Character(ACCOUNT_TYPE));
    	crit.setProductType(PRODUCT_TYPE);
    	crit.setProvinceCode(PROVINCE_CD);
    	//crit.setNetworkType(NETWORK_TYPE);
    	crit.setEquipmentType(EQUIPMENT_TYPE);
    	//crit.setActivityReasonCode(ACTIVITY_REASON_CODE);
    	crit.setBrandId(new Integer(BRAND_ID));
    	//crit.setCurrentPlansOnly(new Boolean(CURRENT_PLANS_ONLY));
    	//crit.setAvailableForActivationOnly(new Boolean(AVAILABLE_FOR_ACTIVATION_ONLY));
    	//crit.setTerm(new Integer(TERM));
    	crit.setAccountSubType(new Character(ACCOUNT_SUB_TYPE));
    	crit.setInitialActivation(new Boolean(INITIAL_ACTIVATION));
    	crit.setProductPromoTypes(new Long[]{new Long(0)});

    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(crit);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    	    	
    	System.out.println("***** END OF TEST RUN ****");
    }
   
    
    /* Test Con. ID: findPricePlan.011
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, char, String, int, int)  method 
     * Parameters: productType, provinceCode, accountType, accountSubType, equipmentType, brandId, term
     * Expected Results: PricePlanSummary array of applicable Price Plans. NOTE: this method is deprecated and does not accept 
     * 					 a networkType parameter. Validate that method works as it did prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_011() throws Throwable { 	   	
    	
    	System.out.println("***** START OF TEST RUN ****");
    	
    	PricePlanSelectionCriteria crit = new PricePlanSelectionCriteria();
    	crit.setAccountType(new Character(ACCOUNT_TYPE));
    	crit.setProductType(PRODUCT_TYPE);
    	crit.setProvinceCode(PROVINCE_CD);
    	//crit.setNetworkType(NETWORK_TYPE);
    	crit.setEquipmentType(EQUIPMENT_TYPE);
    	//crit.setActivityReasonCode(ACTIVITY_REASON_CODE);
    	crit.setBrandId(new Integer(BRAND_ID));
    	//crit.setCurrentPlansOnly(new Boolean(CURRENT_PLANS_ONLY));
    	//crit.setAvailableForActivationOnly(new Boolean(AVAILABLE_FOR_ACTIVATION_ONLY));
    	crit.setTerm(new Integer(TERM));
    	crit.setAccountSubType(new Character(ACCOUNT_SUB_TYPE));
    	//crit.setInitialActivation(new Boolean(INITIAL_ACTIVATION));
    	//crit.setProductPromoTypes(new Long[]{new Long(0)});

    	
    	PricePlanSummary[] plan =
    		provider.getReferenceDataManager().findPricePlans(crit);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    	    	
    	System.out.println("***** END OF TEST RUN ****");
    }
    
    
    /* Test Con. ID: findPricePlan.012
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, char, String, int, boolean, boolean)  method 
     * Parameters: productType, provinceCode, accountType, accountSubType, equipmentType, brandId, currentPlansOnly, 
     * 			   availableForActivationOnly
     * Expected Results: PricePlanSummary array of applicable Price Plans. NOTE: this method is deprecated and does not accept 
     * 					 a networkType parameter. Validate that method works as it did prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_012() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN ****");
    	
    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(
    				PRODUCT_TYPE, 
    				PROVINCE_CD, 
    				ACCOUNT_TYPE, 
    				ACCOUNT_SUB_TYPE, 
    				EQUIPMENT_TYPE, 
    				BRAND_ID, 
    				CURRENT_PLANS_ONLY, 
    				AVAILABLE_FOR_ACTIVATION_ONLY);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    	    	
    	System.out.println("***** END OF TEST RUN ****");
    }
    
    
    /* Test Con. ID: findPricePlan.013
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, char, String, boolean, boolean, int, int)  method 
     * Parameters: productType, provinceCode, accountType, accountSubType, equipmentType, currentPlansOnly, 
     * 			   availableForActivationOnly, brandId, term
     * Expected Results: PricePlanSummary array of applicable Price Plans. NOTE: this method is deprecated and does not accept a 
     * 					 networkType parameter. Validate that method works as it did prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_013() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN ****");
    	
    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(
    				PRODUCT_TYPE, 
    				PROVINCE_CD, 
    				ACCOUNT_TYPE, 
    				ACCOUNT_SUB_TYPE, 
    				EQUIPMENT_TYPE, 
    				CURRENT_PLANS_ONLY, 
    				AVAILABLE_FOR_ACTIVATION_ONLY, 
    				BRAND_ID, 
    				TERM);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    
    	System.out.println("***** END OF TEST RUN ****");
    }
    
    
    /* Test Con. ID: findPricePlan.014
     * Action: call TMReferenceDataManager.findPricePlans(String, String, char, char, int, String)  method 
     * Parameters: productType, provinceCode, accountType, accountSubType, brandId, equipmentType
     * Expected Results: PricePlanSummary array of applicable Price Plans. NOTE: this method is deprecated and does not 
     * 					 accept a networkType parameter. Validate that method works as it did prior to Holborn R1 release changes
     */
    public void _testFindPricePlan_014() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN ****");
    	
    	PricePlanSelectionCriteria crit = new PricePlanSelectionCriteria();
    	crit.setAccountType(new Character(ACCOUNT_TYPE));
    	crit.setProductType(PRODUCT_TYPE);
    	crit.setProvinceCode(PROVINCE_CD);
    	//crit.setNetworkType(NETWORK_TYPE);
    	crit.setEquipmentType(EQUIPMENT_TYPE);
    	//crit.setActivityReasonCode(ACTIVITY_REASON_CODE);
    	crit.setBrandId(new Integer(BRAND_ID));
    	//crit.setCurrentPlansOnly(new Boolean(CURRENT_PLANS_ONLY));
    	//crit.setAvailableForActivationOnly(new Boolean(AVAILABLE_FOR_ACTIVATION_ONLY));
    	//crit.setTerm(new Integer(TERM));
    	crit.setAccountSubType(new Character(ACCOUNT_SUB_TYPE));
    	//crit.setInitialActivation(new Boolean(INITIAL_ACTIVATION));
    	//crit.setProductPromoTypes(new Long[]{new Long(0)});

    	PricePlanSummary[] plan = 
    		provider.getReferenceDataManager().findPricePlans(crit);
    	
    	assertNotNull(plan);
    	
    	for (int i=0; i < plan.length; i++)
    		System.out.print("Price Plan #" + i + " -> " + plan[i].getCode().trim()
    				+ ": \n\t" + plan[i].toString());
    	    	
    	System.out.println("***** END OF TEST RUN ****");
    }
    
    
    public void _testNetworkTypes_001() throws Throwable {
    	
    	System.out.println("***** START OF TEST RUN ****");
    	
    	NetworkType[] networkTypes = provider.getReferenceDataManager().getNetworkTypes();
    	
    	assertNotNull(networkTypes);
    	
    	for (int i=0; i < networkTypes.length; i++)
    		System.out.print("Network Type #" + i + ": \n\t" + networkTypes[i].toString());
    	
    	System.out.println("***** END OF TEST RUN ****");
    }
    
}

