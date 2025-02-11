package com.telus.api.contract;

import com.telus.api.BrandNotSupportedException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.CallingCircleParameters;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PricePlan;
import com.telus.provider.account.TMContractFeature;
import com.telus.provider.account.TMSubscriber;
import com.telus.api.BaseTest;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;

public class TestContractManagement_HolbornR1 extends BaseTest {
	
    static {
        //setuplocalHost();
    	//setupD1();
     	//setupSMARTDESKTOP_PT168();
    	setupEASECA_QA();
    }

    /* Constants */
    private static final boolean NEW_CONTRACT = true;
    private static final boolean RENEW_CONTRACT = true;

    
    /* Global public members */
    public String HSPA_SUBSCRIBER;
    public String PCS_SUBSCRIBER;
    public String IDEN_SUBSCRIBER;
           
    public String MATCHING_IDEN_PRICE_PLAN_CODE;
    public String MATCHING_PCS_PRICE_PLAN_CODE;
    public String MATCHING_HSPA_PRICE_PLAN_CODE;
    public String NON_MATCHING_IDEN_PRICE_PLAN_CODE;
    public String NON_MATCHING_PCS_PRICE_PLAN_CODE;
    public String NON_MATCHING_HSPA_PRICE_PLAN_CODE;
    public String PCS_SERVICE_CODE;
    public String IDEN_SERVICE_CODE;
    public String HSPA_SERVICE_CODE;

    public String PCS_EQUIPMENT;
    public String IDEN_EQUIPMENT;
    public String HSPA_EQUIPMENT;    
    
    /* Global private members */
    private static Subscriber subscriber;
    
    
    public TestContractManagement_HolbornR1(String name) throws Throwable {
        super(name);
    }

    
    public void testAll() throws Throwable{

    	setupData("qa");
    	_testNewContract_001();
    	_testNewContract_002();
    	_testNewContract_003();
    	_testNewContract_004();
    	_testNewContract_005();
    	_testNewContract_006();
    	_testNewContract_007();
    	_testNewContract_008();
    	_testNewContract_009();
    	_testNewContract_010();
    	_testNewContract_011();
    	_testNewContract_012();
    	_testNewContract_013();
    	_testNewContract_014();
    	_testNewContract_015();
    	_testNewContract_016();
    	
    	_testRenewContract_001();
    	_testRenewContract_002();
    	_testRenewContract_003();
    	_testRenewContract_004();
    	_testRenewContract_005();
    	_testRenewContract_006();
    	_testRenewContract_007();
    	_testRenewContract_008();
    	_testRenewContract_009();
    	_testRenewContract_010();
    	_testRenewContract_011();
    	_testRenewContract_012();
    	_testRenewContract_013();
    	_testRenewContract_014();
    	_testRenewContract_015();
    	_testRenewContract_016();
    	_testRenewContract_017();
    	_testRenewContract_018();

    	_testSaveContract_001();
    	_testSaveContract_002();
    	_testSaveContract_003();
    	_testSaveContract_004();

    }
    
    
    
    /*==================================================================
     * New Contract test cases
     * ================================================================= */   
     
     /* Test Con. ID: newContract.001
      * Action: call TMSubscriber.newContract(PricePlan, int) method for a subscriber with Non-HSPA equipment and PricePlan object 
      * 		containing matching services only
      * Parameters: PricePlan pricePlan (with matching services only), int term
      * Expected Results: Contract Object returned with the same result as expected prior to Holborn release
      */
     public void _testNewContract_001() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.001 ****"); 
    	newOrRenewContract(PCS_SUBSCRIBER, MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, NEW_CONTRACT);
    	System.out.println("***** END OF TEST RUN newContract.001 ****"); 
     }

     
     /* Test Con. ID: newContract.002
      * Action: call TMSubscriber.newContract(PricePlan, int) method for a subscriber with Non-HSPA equipment and PricePlan object containing 
      * 		non-matching services (defined as included/optional SOCs with different NetworkType than subscriber's equipment NetworkType) 
      * Parameters: PricePlan pricePlan (containing non-matching services), int term 
      * Expected Results: Contract Object returned with all non-matching services removed
      */
     public void _testNewContract_002() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.002 ****"); 
    	newOrRenewContract(PCS_SUBSCRIBER, NON_MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, NEW_CONTRACT);
    	System.out.println("***** END OF TEST RUN newContract.002 ****"); 
     }
     
     
     /* Test Con. ID: newContract.003
      * Action: call TMSubscriber.newContract(PricePlan, int) method for a subscriber with HSPA equipment and PricePlan object containing 
      * 		matching services only
      * Parameters: PricePlan pricePlan (with matching services only), int term
      * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType
      */
     public void _testNewContract_003() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.003 ****"); 
    	newOrRenewContract(HSPA_SUBSCRIBER, MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, NEW_CONTRACT);
    	System.out.println("***** END OF TEST RUN newContract.003 ****"); 
     }
     
     
     /* Test Con. ID: newContract.004
      * Action: call TMSubscriber.newContract(PricePlan, int) method for a subscriber with HSPA equipment and PricePlan object containing 
      * 		non-matching services (defined as included/optional SOCs with different NetworkType than subscriber's equipment NetworkType) 
      * Parameters: PricePlan pricePlan (containing non-matching services), int term 
      * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType and with all non-matching 
      * 				  services removed
      */
     public void _testNewContract_004() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.004 ****"); 
    	newOrRenewContract(HSPA_SUBSCRIBER, NON_MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, NEW_CONTRACT);    	
    	System.out.println("***** END OF TEST RUN newContract.004 ****"); 
     }

     
     /* Test Con. ID: newContract.005
      * Action: call TMSubscriber.newContract(PricePlan, int, boolean) method for a subscriber with Non-HSPA equipment and PricePlan object 
      * 		containing matching services only
      * Parameters: PricePlan pricePlan (with matching services only), int term, boolean dispatchOnly
      * Expected Results: Contract Object returned with the same result as expected prior to Holborn release
      */
     public void _testNewContract_005() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.005 ****"); 
    	
    	//PCS => dispatchOnly parameter always set to false
    	System.out.println("\t***** PCS Scenario (dispatchOnly set to false)  ****");
    	newOrRenewContract(PCS_SUBSCRIBER, MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, NEW_CONTRACT);  //dispatch is set to true for IDEN only    	
    	
    	//IDEN => dispatchOnly parameter can be set to true
    	System.out.println("\t***** IDEN Scenario (dispatchOnly set to true)  ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, true, NEW_CONTRACT);  //dispatch is set to true for IDEN only    	

    	System.out.println("***** END OF TEST RUN newContract.005 ****"); 
     }

     
     /* Test Con. ID: newContract.006
      * Action: call TMSubscriber.newContract(PricePlan, int, boolean) method for a subscriber with Non-HSPA equipment and PricePlan object containing 
      * 		non-matching services (defined as included/optional SOCs with different NetworkType than subscriber's equipment NetworkType) 
      * Parameters: PricePlan pricePlan (containing non-matching services), int term, boolean dispatchOnly
      * Expected Results: Contract Object returned with all non-matching services removed
      */
     public void _testNewContract_006() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.006 ****"); 

    	//PCS => dispatchOnly parameter always set to false
    	System.out.println("\t***** PCS Scenario (dispatchOnly set to false)  ****");
    	newOrRenewContract(PCS_SUBSCRIBER, NON_MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, NEW_CONTRACT);  //dispatch is set to true for IDEN only

    	//IDEN => dispatchOnly parameter can be set to true
    	System.out.println("\t***** IDEN Scenario (dispatchOnly set to true)  ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, NON_MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, true, NEW_CONTRACT);  //dispatch is set to true for IDEN only
    	
    	System.out.println("***** END OF TEST RUN newContract.006 ****"); 
     }

     
     /* Test Con. ID: newContract.007
      * Action: call TMSubscriber.newContract(PricePlan, int, boolean) method for a subscriber with HSPA equipment and PricePlan object containing 
      * 		matching services only
      * Parameters: PricePlan pricePlan (with matching services only), int term, boolean dispatchOnly
      * Expected Results: PricePlan pricePlan (with matching services only), int term, boolean dispatchOnly
      */
     public void _testNewContract_007() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.007 ****"); 
    	newOrRenewContract(HSPA_SUBSCRIBER, MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, NEW_CONTRACT);  //dispatch is set to true for IDEN only
    	System.out.println("***** END OF TEST RUN newContract.007 ****"); 
     }

     
     /* Test Con. ID: newContract.008
      * Action: call TMSubscriber.newContract(PricePlan, int, boolean) method for a subscriber with HSPA equipment and PricePlan object containing 
      * 		non-matching services (defined as included/optional SOCs with different NetworkType than subscriber's equipment NetworkType) 
      * Parameters: PricePlan pricePlan (containing non-matching services), int term, boolean dispatchOnly
      * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType and with all non-matching services 
      * 				  removed
      */
     public void _testNewContract_008() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.008 ****"); 
    	newOrRenewContract(HSPA_SUBSCRIBER, NON_MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, NEW_CONTRACT);  //dispatch is set to true for IDEN only  	
    	System.out.println("***** END OF TEST RUN newContract.008 ****"); 
     }

     
     /* Test Con. ID: newContract.009
      * Action: call TMSubscriber.newContract(PricePlan, int, boolean, EquipmentChangeRequest) method for a subscriber with Non-HSPA equipment 
      * 		and PricePlan object containing matching services only
      * Parameters: PricePlan pricePlan (with matching services only), int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest
      * Expected Results: Contract Object returned with the same result as expected prior to Holborn release
      */
     public void _testNewContract_009() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.009 ****"); 

    	//PCS => dispatchOnly parameter always set to false
    	System.out.println("\t***** PCS Scenario (dispatchOnly set to false)  ****");   	
    	newOrRenewContract(PCS_SUBSCRIBER, MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, PCS_EQUIPMENT, NEW_CONTRACT); //dispatch is set to true for IDEN only

    	//IDEN => dispatchOnly parameter can be set to true   	
    	System.out.println("\t***** IDEN Scenario (dispatchOnly set to true)  ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, true, IDEN_EQUIPMENT, NEW_CONTRACT); //dispatch is set to true for IDEN only

    	System.out.println("***** END OF TEST RUN newContract.009 ****"); 
     }
    
          
     /* Test Con. ID: newContract.010
      * Action: call TMSubscriber.newContract(PricePlan, int, boolean, EquipmentChangeRequest) method for a subscriber with Non-HSPA equipment 
      * 		and PricePlan object containing non-matching services (defined as included/optional SOCs with different NetworkType than 
      * 		subscriber's equipment NetworkType) 
      * Parameters: PricePlan pricePlan (containing non-matching services), int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest
      * Expected Results: Contract Object returned with all non-matching services removed
      */
     public void _testNewContract_010() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.010 ****"); 
    	
    	//PCS => dispatchOnly parameter always set to false
    	System.out.println("\t***** PCS Scenario (dispatchOnly set to false)  ****");   	
    	newOrRenewContract(PCS_SUBSCRIBER, NON_MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, PCS_EQUIPMENT, NEW_CONTRACT); //dispatch is set to true for IDEN only

    	//IDEN => dispatchOnly parameter can be set to true   	
    	System.out.println("\t***** IDEN Scenario (dispatchOnly set to true)  ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, NON_MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, true, IDEN_EQUIPMENT, NEW_CONTRACT); //dispatch is set to true for IDEN only
    	
    	System.out.println("***** END OF TEST RUN newContract.010 ****"); 
     }

     
     /* Test Con. ID: newContract.011
      * Action: call TMSubscriber.newContract(PricePlan, int, boolean, EquipmentChangeRequest) method for a subscriber with HSPA equipment and 
      * 		PricePlan object containing matching services only
      * Parameters: PricePlan pricePlan (with matching services only), int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest
      * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType
      */
     public void _testNewContract_011() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.011 ****"); 
    	newOrRenewContract(HSPA_SUBSCRIBER, MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, HSPA_EQUIPMENT, NEW_CONTRACT); //dispatch is set to true for IDEN only
    	System.out.println("***** END OF TEST RUN newContract.011 ****"); 
     }

     
     /* Test Con. ID: newContract.012
      * Action: call TMSubscriber.newContract(PricePlan, int, boolean, EquipmentChangeRequest) method for a subscriber with HSPA equipment and 
      * 		PricePlan object containing non-matching services (defined as included/optional SOCs with different NetworkType than 
      * 		subscriber's equipment NetworkType) 
      * Parameters: PricePlan pricePlan (containing non-matching services), int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest
      * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType and with all non-matching services 
      * 				  removed
      */
     public void _testNewContract_012() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.012 ****"); 
    	newOrRenewContract(HSPA_SUBSCRIBER, NON_MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, HSPA_EQUIPMENT, NEW_CONTRACT); //dispatch is set to true for IDEN only  	
    	System.out.println("***** END OF TEST RUN newContract.012 ****"); 
     }

     
     /* Test Con. ID: newContract.013
      * Action: call TMSubscriber.newContract(PricePlan, int, EquipmentChangeRequest) method for a subscriber with Non-HSPA equipment and 
      * 		PricePlan object containing matching services only
      * Parameters: PricePlan pricePlan (with matching services only), int term, EquipmentChangeRequest equipmentChangeRequest
      * Expected Results: Contract Object returned with the same result as expected prior to Holborn release
      */
     public void _testNewContract_013() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.013 ****"); 
    	
    	//PCS
    	System.out.println("\t***** PCS Scenario ****");   	
    	newOrRenewContract(PCS_SUBSCRIBER, MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, PCS_EQUIPMENT, NEW_CONTRACT);

    	//IDEN    	
    	System.out.println("\t***** IDEN Scenario ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, IDEN_EQUIPMENT, NEW_CONTRACT);

    	System.out.println("***** END OF TEST RUN newContract.013 ****"); 
     }

     
     /* Test Con. ID: newContract.014
      * Action: call TMSubscriber.newContract(PricePlan, int, EquipmentChangeRequest) method for a subscriber with Non-HSPA equipment and 
      * 		PricePlan object containing non-matching services (defined as included/optional SOCs with different NetworkType than subscriber's 
      * 		equipment NetworkType) 
      * Parameters: PricePlan pricePlan (containing non-matching services), int term, EquipmentChangeRequest equipmentChangeRequest
      * Expected Results: Contract Object returned with all non-matching services removed
      */
     public void _testNewContract_014() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.014 ****"); 
    	
    	//PCS
    	System.out.println("\t***** PCS Scenario ****");   	
    	newOrRenewContract(PCS_SUBSCRIBER, NON_MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, PCS_EQUIPMENT, NEW_CONTRACT);

    	//IDEN    	
    	System.out.println("\t***** IDEN Scenario ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, NON_MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, IDEN_EQUIPMENT, NEW_CONTRACT);

    	System.out.println("***** END OF TEST RUN newContract.014 ****"); 
     }

     
     /* Test Con. ID: newContract.015
      * Action: call TMSubscriber.newContract(PricePlan, int, EquipmentChangeRequest) method for a subscriber with HSPA equipment and PricePlan 
      * 		object containing matching services only
      * Parameters: PricePlan pricePlan (with matching services only), int term, EquipmentChangeRequest equipmentChangeRequest
      * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType
      */
     public void _testNewContract_015() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.015 ****"); 
    	newOrRenewContract(HSPA_SUBSCRIBER, MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, HSPA_EQUIPMENT, NEW_CONTRACT);
    	System.out.println("***** END OF TEST RUN newContract.015 ****"); 
     }

     
     /* Test Con. ID: newContract.016
      * Action: call TMSubscriber.newContract(PricePlan, int, EquipmentChangeRequest) method for a subscriber with HSPA equipment and PricePlan 
      * 		object containing non-matching services (defined as included/optional SOCs with different NetworkType than subscriber's equipment 
      * 		NetworkType) 
      * Parameters: PricePlan pricePlan (containing non-matching services), int term, EquipmentChangeRequest equipmentChangeRequest
      * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType and with all non-matching services 
      * 				  removed
      */
     public void _testNewContract_016() throws Throwable {
     
    	System.out.println("***** START OF TEST RUN newContract.016 ****"); 
    	newOrRenewContract(HSPA_SUBSCRIBER, NON_MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, HSPA_EQUIPMENT, NEW_CONTRACT);
    	System.out.println("***** END OF TEST RUN newContract.016 ****"); 
     }
     

     
     
     /*==================================================================
      * Renew Contract test cases
      * ================================================================= */   
      
      /* Test Con. ID: renewContract.001
       * Action: call TMSubscriber.renewContract(int) method for a subscriber with Non-HSPA equipment 
       * Parameters: int term
       * Expected Results: Contract Object returned with the same result as expected prior to Holborn release
       */
      public void _testRenewContract_001() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.001 ****"); 
     	renewContract(PCS_SUBSCRIBER, Subscriber.TERM_3_YEARS);
     	System.out.println("***** END OF TEST RUN renewContract.001 ****"); 
      }
     
      
      /* Test Con. ID: renewContract.002
       * Action: call TMSubscriber.renewContract(int) method for a subscriber with HSPA equipment 
       * Parameters: int term
       * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType
       */
      public void _testRenewContract_002() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.002 ****"); 
     	renewContract(HSPA_SUBSCRIBER, Subscriber.TERM_3_YEARS);
     	System.out.println("***** END OF TEST RUN renewContract.002 ****"); 
      }
   
      
      /* Test Con. ID: renewContract.003
       * Action: call TMSubscriber.renewContract(PricePlan, int) method for a subscriber with Non-HSPA equipment and PricePlan object 
       * 		 containing matching services only 
       * Parameters: PricePlan pricePlan (with matching services only), int term
       * Expected Results: Contract Object returned with the same result as expected prior to Holborn release
       */
      public void _testRenewContract_003() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.003 ****"); 
     	newOrRenewContract(PCS_SUBSCRIBER, MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, RENEW_CONTRACT);
     	System.out.println("***** END OF TEST RUN renewContract.003 ****"); 
      }

      
      /* Test Con. ID: renewContract.004
       * Action: call TMSubscriber.renewContract(PricePlan, int) method for a subscriber with Non-HSPA equipment and PricePlan object 
       * 		 containing non-matching services (defined as included/optional SOCs with different NetworkType than subscriber's equipment 
       * 		 NetworkType)  
       * Parameters: PricePlan pricePlan (containing non-matching services), int term
       * Expected Results: Contract Object returned with all non-matching services removed
       */
      public void _testRenewContract_004() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.004 ****"); 
     	newOrRenewContract(PCS_SUBSCRIBER, NON_MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, RENEW_CONTRACT);
     	System.out.println("***** END OF TEST RUN renewContract.004 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.005
       * Action: call TMSubscriber.renewContract(PricePlan, int) method for a subscriber with HSPA equipment and PricePlan object containing 
       * 		 matching services only
       * Parameters: PricePlan pricePlan (with matching services only), int term
       * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType
       */
      public void _testRenewContract_005() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.005 ****"); 
     	newOrRenewContract(HSPA_SUBSCRIBER, MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, RENEW_CONTRACT);
     	System.out.println("***** END OF TEST RUN renewContract.005 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.006
       * Action: call TMSubscriber.renewContract(PricePlan, int) method for a subscriber with HSPA equipment and PricePlan object containing 
       * 		 non-matching services (defined as included/optional SOCs with different NetworkType than subscriber's equipment NetworkType) 
       * Parameters: PricePlan pricePlan (containing non-matching services), int term
       * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType and with all non-matching 
       * 				   services removed
       */
      public void _testRenewContract_006() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.006 ****"); 
     	newOrRenewContract(HSPA_SUBSCRIBER, NON_MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, RENEW_CONTRACT);
     	System.out.println("***** END OF TEST RUN renewContract.006 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.007
       * Action: call TMSubscriber.renewContract(PricePlan, int, boolean) method for a subscriber with Non-HSPA equipment and PricePlan 
       * 		 object containing matching services only
       * Parameters: PricePlan pricePlan (with matching services only), int term, boolean dispatchOnly
       * Expected Results: Contract Object returned with the same result as expected prior to Holborn release
       */
      public void _testRenewContract_007() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.007 ****"); 
     	
     	//PCS => dispatchOnly parameter always set to false
    	System.out.println("\t***** PCS Scenario (dispatchOnly set to false)  ****");
     	newOrRenewContract(PCS_SUBSCRIBER, MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, RENEW_CONTRACT);
     	
     	//IDEN => dispatchOnly parameter can be set to true
    	System.out.println("\t***** IDEN Scenario (dispatchOnly set to true)  ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, true, RENEW_CONTRACT);
    	
     	System.out.println("***** END OF TEST RUN renewContract.007 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.008
       * Action: call TMSubscriber.renewContract(PricePlan, int, boolean) method for a subscriber with Non-HSPA equipment and PricePlan 
       * 		 object containing non-matching services (defined as included/optional SOCs with different NetworkType than subscriber's 
       * 		 equipment NetworkType) 
       * Parameters: PricePlan pricePlan (containing non-matching services), int term, boolean dispatchOnly
       * Expected Results: Contract Object returned with all non-matching services removed
       */
      public void _testRenewContract_008() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.008 ****"); 
     	
     	//PCS => dispatchOnly parameter always set to false
    	System.out.println("\t***** PCS Scenario (dispatchOnly set to false)  ****");
     	newOrRenewContract(PCS_SUBSCRIBER, NON_MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, RENEW_CONTRACT);
     	
     	//IDEN => dispatchOnly parameter can be set to true
    	System.out.println("\t***** IDEN Scenario (dispatchOnly set to true)  ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, NON_MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, true, RENEW_CONTRACT);
    	
     	System.out.println("***** END OF TEST RUN renewContract.008 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.009
       * Action: call TMSubscriber.renewContract(PricePlan, int, boolean) method for a subscriber with HSPA equipment and PricePlan object 
       * 			  containing matching services only
       * Parameters: PricePlan pricePlan (with matching services only), int term, boolean dispatchOnly
       * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType
       */
      public void _testRenewContract_009() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.009 ****");      	
     	newOrRenewContract(HSPA_SUBSCRIBER, MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, RENEW_CONTRACT);   	
     	System.out.println("***** END OF TEST RUN renewContract.009 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.010
       * Action: call TMSubscriber.renewContract(PricePlan, int, boolean) method for a subscriber with HSPA equipment and PricePlan object 
       * 		 containing non-matching services (defined as included/optional SOCs with different NetworkType than subscriber's equipment 
       * 		 NetworkType) 
       * Parameters: PricePlan pricePlan (containing non-matching services), int term, boolean dispatchOnly
       * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType and with all non-matching 
       * 				   services removed
       */
      public void _testRenewContract_010() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.010 ****");      	
     	newOrRenewContract(HSPA_SUBSCRIBER, NON_MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, RENEW_CONTRACT);   	
     	System.out.println("***** END OF TEST RUN renewContract.010 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.011
       * Action: call TMSubscriber.renewContract(PricePlan, int, boolean, EquipmentChangeRequest) method for a subscriber with Non-HSPA 
       * 		 equipment and PricePlan object containing matching services only
       * Parameters: PricePlan pricePlan (with matching services only), int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest
       * Expected Results: Contract Object returned with the same result as expected prior to Holborn release
       */
      public void _testRenewContract_011() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.011 ****");      	
     	
     	//PCS => dispatchOnly parameter always set to false
    	System.out.println("\t***** PCS Scenario (dispatchOnly set to false)  ****");
     	newOrRenewContract(PCS_SUBSCRIBER, MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, PCS_EQUIPMENT, RENEW_CONTRACT);
     	
     	//IDEN => dispatchOnly parameter can be set to true
    	System.out.println("\t***** IDEN Scenario (dispatchOnly set to true)  ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, true, PCS_EQUIPMENT, RENEW_CONTRACT);   	
     	
    	System.out.println("***** END OF TEST RUN renewContract.011 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.012
       * Action: call TMSubscriber.renewContract(PricePlan, int, boolean, EquipmentChangeRequest) method for a subscriber with Non-HSPA 
       * 		 equipment and PricePlan object containing non-matching services (defined as included/optional SOCs with different NetworkType 
       * 		 than subscriber's equipment NetworkType) 
       * Parameters: PricePlan pricePlan (containing non-matching services), int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest
       * Expected Results: Contract Object returned with all non-matching services removed
       */
      public void _testRenewContract_012() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.012 ****");      	
     	
     	//PCS => dispatchOnly parameter always set to false
    	System.out.println("\t***** PCS Scenario (dispatchOnly set to false)  ****");
     	newOrRenewContract(PCS_SUBSCRIBER, NON_MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, PCS_EQUIPMENT, RENEW_CONTRACT);
     	
     	//IDEN => dispatchOnly parameter can be set to true
    	System.out.println("\t***** IDEN Scenario (dispatchOnly set to true)  ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, NON_MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, true, PCS_EQUIPMENT, RENEW_CONTRACT);   	
     	
    	System.out.println("***** END OF TEST RUN renewContract.012 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.013
       * Action: call TMSubscriber.renewContract(PricePlan, int, boolean, EquipmentChangeRequest) method for a subscriber with HSPA equipment 
       * 		 and PricePlan object containing matching services only
       * Parameters: PricePlan pricePlan (with matching services only), int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest
       * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType
       */
      public void _testRenewContract_013() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.013 ****");      	
     	newOrRenewContract(HSPA_SUBSCRIBER, MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, HSPA_EQUIPMENT, RENEW_CONTRACT);   	
     	System.out.println("***** END OF TEST RUN renewContract.013 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.014
       * Action: call TMSubscriber.renewContract(PricePlan, int, boolean, EquipmentChangeRequest) method for a subscriber with HSPA equipment 
       * 		 and PricePlan object containing non-matching services (defined as included/optional SOCs with different NetworkType than 
       * 		 subscriber's equipment NetworkType) 
       * Parameters: PricePlan pricePlan (containing non-matching services), int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest
       * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType and with all non-matching 
       * 				   services removed
       */
      public void _testRenewContract_014() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.014 ****");      	
     	newOrRenewContract(HSPA_SUBSCRIBER, NON_MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, false, HSPA_EQUIPMENT, RENEW_CONTRACT);   	
     	System.out.println("***** END OF TEST RUN renewContract.014 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.015
       * Action: call TMSubscriber.renewContract(PricePlan, int, EquipmentChangeRequest) method for a subscriber with Non-HSPA equipment and 
       * 		 PricePlan object containing matching services only
       * Parameters: PricePlan pricePlan (with matching services only), int term, EquipmentChangeRequest equipmentChangeRequest
       * Expected Results: Contract Object returned with the same result as expected prior to Holborn release
       */
      public void _testRenewContract_015() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.015 ****");      	
     	
     	//PCS => dispatchOnly parameter always set to false
    	System.out.println("\t***** PCS Scenario (dispatchOnly set to false)  ****");
     	newOrRenewContract(PCS_SUBSCRIBER, MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, PCS_EQUIPMENT, RENEW_CONTRACT);
     	
     	//IDEN => dispatchOnly parameter can be set to true
    	System.out.println("\t***** IDEN Scenario (dispatchOnly set to true)  ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, PCS_EQUIPMENT, RENEW_CONTRACT);   	
     	
    	System.out.println("***** END OF TEST RUN renewContract.015 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.016
       * Action: call TMSubscriber.renewContract(PricePlan, int, EquipmentChangeRequest) method for a subscriber with Non-HSPA equipment and 
       * 		 PricePlan object containing non-matching services (defined as included/optional SOCs with different NetworkType than 
       * 		 subscriber's equipment NetworkType) 
       * Parameters: PricePlan pricePlan (containing non-matching services), int term, EquipmentChangeRequest equipmentChangeRequest
       * Expected Results: Contract Object returned with all non-matching services removed
       */
      public void _testRenewContract_016() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.016 ****");      	
     	
     	//PCS => dispatchOnly parameter always set to false
    	System.out.println("\t***** PCS Scenario (dispatchOnly set to false)  ****");
     	newOrRenewContract(PCS_SUBSCRIBER, NON_MATCHING_PCS_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, PCS_EQUIPMENT, RENEW_CONTRACT);
     	
     	//IDEN => dispatchOnly parameter can be set to true
    	System.out.println("\t***** IDEN Scenario (dispatchOnly set to true)  ****");
    	newOrRenewContract(IDEN_SUBSCRIBER, NON_MATCHING_IDEN_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, PCS_EQUIPMENT, RENEW_CONTRACT);   	
     	
    	System.out.println("***** END OF TEST RUN renewContract.016 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.017
       * Action: call TMSubscriber.renewContract(PricePlan, int, EquipmentChangeRequest) method for a subscriber with HSPA equipment and 
       * 		 PricePlan object containing matching services only
       * Parameters: PricePlan pricePlan (with matching services only), int term, EquipmentChangeRequest equipmentChangeRequest 
       * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType
       */
      public void _testRenewContract_017() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.017 ****");      	
     	newOrRenewContract(HSPA_SUBSCRIBER, MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, HSPA_EQUIPMENT, RENEW_CONTRACT);   	
     	System.out.println("***** END OF TEST RUN renewContract.017 ****"); 
      }
      
      
      /* Test Con. ID: renewContract.018
       * Action: call TMSubscriber.renewContract(PricePlan, int, EquipmentChangeRequest) method for a subscriber with HSPA equipment and 
       * 		 PricePlan object containing non-matching services (defined as included/optional SOCs with different NetworkType than 
       * 		 subscriber's equipment NetworkType) 
       * Parameters: PricePlan pricePlan (containing non-matching services), int term, EquipmentChangeRequest equipmentChangeRequest
       * Expected Results: Contract Object returned with no SOCs removed based on non-matching EquipmentType and with all non-matching 
       * 				   services removed
       */
      public void _testRenewContract_018() throws Throwable {
      
     	System.out.println("***** START OF TEST RUN renewContract.018 ****");      	
     	newOrRenewContract(HSPA_SUBSCRIBER, NON_MATCHING_HSPA_PRICE_PLAN_CODE, Subscriber.TERM_3_YEARS, HSPA_EQUIPMENT, RENEW_CONTRACT);   	
     	System.out.println("***** END OF TEST RUN renewContract.018 ****"); 
      }
      
     
      
      
   /*==================================================================
    * Contract Save test cases
    * ================================================================= */   
    
    /* Test Con. ID: saveContract.001
     * Action: call TMContract.save(String, String) method for a subscriber with Non-HSPA equipment and contract containing matching services only 
     * Parameters: dealerCode, salesRepCode
     * Expected Results: Contract saved with the same result as expected prior to Holborn release
     */
    public void _testSaveContract_001() throws Throwable {
    	   	
    	System.out.println("***** START OF TEST RUN saveContract.001 ****"); 
    	
     	//PCS
    	saveContract(PCS_SUBSCRIBER, PCS_SERVICE_CODE);
        
    	//MIKE
//    	saveContract(IDEN_SUBSCRIBER, IDEN_SERVICE_CODE);
        
    	System.out.println("***** END OF TEST RUN saveContract.001 ****"); 
    }


     /* Test Con. ID: saveContract.002
      * Action: call TMContract.save(String, String) method for a subscriber with Non-HSPA equipment and contract containing non-matching 
      * 		services (defined as included/optional SOCs with different NetworkType than subscriber's equipment NetworkType)  
      * Parameters: dealerCode, salesRepCode
      * Expected Results: Contract saved with all non-matching services removed
      */
     public void _testSaveContract_002() throws Throwable {
     	   	
     	System.out.println("***** START OF TEST RUN saveContract.002 ****"); 
     	
      	//PCS
     	saveContract(PCS_SUBSCRIBER, HSPA_SERVICE_CODE);
         
     	//MIKE
//     	saveContract(IDEN_SUBSCRIBER, IDEN_SERVICE_CODE);
         
     	System.out.println("***** END OF TEST RUN saveContract.002 ****"); 
     }

     
      /* Test Con. ID: saveContract.003
       * Action: call TMContract.save(String, String) method for a subscriber with HSPA equipment and contract containing matching services only  
       * Parameters: dealerCode, salesRepCode
       * Expected Results: Contract saved with no SOCs removed based on non-matching EquipmentType
       */
      public void _testSaveContract_003() {
      	   	
      	System.out.println("***** START OF TEST RUN saveContract.003 ****"); 
      	
       	//PCS
      	saveContract(HSPA_SUBSCRIBER, HSPA_SERVICE_CODE);
          
      	//MIKE
//      	saveContract(IDEN_SUBSCRIBER, IDEN_SERVICE_CODE);
          
      	System.out.println("***** END OF TEST RUN saveContract.003 ****"); 
      }

      
      /* Test Con. ID: saveContract.004
       * Action: call TMContract.save(String, String) method for a subscriber with HSPA equipment and contract containing non-matching 
       * 		 services (defined as included/optional SOCs with different NetworkType than subscriber's equipment NetworkType)   
       * Parameters: dealerCode, salesRepCode
       * Expected Results: Contract saved with no SOCs removed based on non-matching EquipmentType and with all non-matching services removed
       */
      public void _testSaveContract_004() throws Throwable {
      	   	
      	System.out.println("***** START OF TEST RUN saveContract.004 ****"); 
      	
       	//PCS
      	saveContract(HSPA_SUBSCRIBER, PCS_SERVICE_CODE);
          
      	//MIKE
//      	saveContract(IDEN_SUBSCRIBER, IDEN_SERVICE_CODE);
          
      	System.out.println("***** END OF TEST RUN saveContract.004 ****"); 
      }


      
    private void newOrRenewContract(String subscriberNum, String planCode, int term, boolean newContract) throws Throwable {
     	
    	subscriber = api.getAccountManager().findSubscriberByPhoneNumber(subscriberNum);
    	assertNotNull(subscriber);
    	
    	PricePlan plan = subscriber.getAvailablePricePlan(planCode);
    	assertNotNull(plan);
    	
    	Contract contract;
    	if (newContract)
    		contract = subscriber.newContract(plan, term);
    	else
    		contract = subscriber.renewContract(plan, term);
    	
    	printContract(contract, ((TMSubscriber)subscriber).getEquipment0().getNetworkType());    	 
     }

    
    private void newOrRenewContract(String subscriberNum, String planCode, int term, boolean dispatchOnly, boolean newContract) throws Throwable {
     	
    	subscriber = api.getAccountManager().findSubscriberByPhoneNumber(subscriberNum);
    	assertNotNull(subscriber);
    	
    	PricePlan plan = subscriber.getAvailablePricePlan(planCode);
    	assertNotNull(plan);
    	
    	Contract contract;
    	if (newContract)
    		contract = ((TMSubscriber)subscriber).newContract(plan, term, dispatchOnly);  //dispatch is set to true for IDEN only    	
    	else 
    		contract = ((TMSubscriber)subscriber).renewContract(plan, term, dispatchOnly);  //dispatch is set to true for IDEN only
    	
    	printContract(contract, ((TMSubscriber)subscriber).getEquipment0().getNetworkType());
    }
    
    
    private void newOrRenewContract(String subscriberNum, String planCode, int term, boolean dispatchOnly, String equipmentID, boolean newContract) throws Throwable {
    
    	subscriber = api.getAccountManager().findSubscriberByPhoneNumber(subscriberNum);
    	assertNotNull(subscriber);
    	
    	PricePlan plan = subscriber.getAvailablePricePlan(planCode);
    	assertNotNull(plan);
    	
    	Equipment equipment = api.getEquipmentManager().getEquipment(equipmentID);
    	assertNotNull(equipment);
    	
    	EquipmentChangeRequest equipmentChangeRequest = subscriber.newEquipmentChangeRequest(equipment, 
    			subscriber.getAccount().getDealerCode(), 
    			subscriber.getAccount().getSalesRepCode(), 
    			provider.getUser(), 
    			null, 
    			Subscriber.SWAP_TYPE_REPLACEMENT, false);
    	assertNotNull(equipmentChangeRequest);

    	Contract contract;
    	if (newContract)
    		contract = ((TMSubscriber)subscriber).newContract(plan, term, dispatchOnly, equipmentChangeRequest);  //dispatch is set to true for IDEN only
    	else
    		contract = ((TMSubscriber)subscriber).renewContract(plan, term, dispatchOnly, equipmentChangeRequest);  //dispatch is set to true for IDEN only
    	
    	printContract(contract, ((TMSubscriber)subscriber).getEquipment0().getNetworkType());    	
     }
 
    
    private void newOrRenewContract(String subscriberNum, String planCode, int term, String equipmentID, boolean newContract) throws Throwable {
        
    	subscriber = api.getAccountManager().findSubscriberByPhoneNumber(subscriberNum);
    	assertNotNull(subscriber);
    	
    	PricePlan plan = subscriber.getAvailablePricePlan(planCode);
    	assertNotNull(plan);
    	
    	Equipment equipment = api.getEquipmentManager().getEquipment(equipmentID);
    	assertNotNull(equipment);
    	
    	EquipmentChangeRequest equipmentChangeRequest = subscriber.newEquipmentChangeRequest(equipment, 
    			subscriber.getAccount().getDealerCode(), 
    			subscriber.getAccount().getSalesRepCode(), 
    			provider.getUser(), 
    			null, 
    			Subscriber.SWAP_TYPE_REPLACEMENT, false);
    	assertNotNull(equipmentChangeRequest);
    	
    	Contract contract;
    	if (newContract)
    		contract = ((TMSubscriber)subscriber).newContract(plan, term, equipmentChangeRequest);  //dispatch is set to true for IDEN only
    	else
    		contract = ((TMSubscriber)subscriber).renewContract(plan, term, equipmentChangeRequest);  //dispatch is set to true for IDEN only
    	
    	printContract(contract, ((TMSubscriber)subscriber).getEquipment0().getNetworkType());    	
     }

    
    private void renewContract(String subscriberNum, int term) throws Throwable {
     	
    	subscriber = api.getAccountManager().findSubscriberByPhoneNumber(subscriberNum);
    	assertNotNull(subscriber);
    	
    	Contract contract = subscriber.renewContract(term);
    	
    	printContract(contract, ((TMSubscriber)subscriber).getEquipment0().getNetworkType());    	 
     }
    
   
    private void saveContract(String subscriberNum, String socCode) {

    	try {
    		subscriber = api.getAccountManager().findSubscriberByPhoneNumber(subscriberNum);
    		assertNotNull(subscriber);
    	
    		System.out.println("Contract state at beginning of method: \n\t");
    		printContract(subscriber.getContract(), ((TMSubscriber)subscriber).getEquipment0().getNetworkType());
    	
    		removeOptionalServices();
    	
    		System.out.println("\n\nContract state at after removal of all optional services: \n\t");
    		printContract(subscriber.getContract(), ((TMSubscriber)subscriber).getEquipment0().getNetworkType());
        
    		subscriber.getContract().addService(socCode);
    		subscriber.getContract().save();        
    		
    		ContractService[] contractServices = subscriber.getContract().getOptionalServices();
        	for (int i = 0; i < contractServices.length; i++) {
        		assertEquals(contractServices[i].getCode().trim(), socCode);
        	}   		
    	
    		System.out.println("\n\nContract state at end of method: \n\t");
    		printContract(subscriber.getContract(), ((TMSubscriber)subscriber).getEquipment0().getNetworkType());
    	} catch (Throwable t) {
    		System.out.println("Error encountered in saveContract(): [");
    		t.printStackTrace();
    		System.out.println("]");
    	}

    }   

    
    private void removeOptionalServices() throws Throwable {
    	ContractService[] contractServices = subscriber.getContract().getOptionalServices();

    	for (int i = 0; i < contractServices.length; i++) {
    		subscriber.getContract().removeService(contractServices[i].getCode());
    	}
    	subscriber.getContract().save();	
    }
    
    
    private void printContract(Contract c, String subscriberNetworkType) {
  
    	//Ensure contract was provided
    	assertNotNull(c);
    	
    	ContractService[] inclSOCs = c.getIncludedServices();
    	ContractService[] optSOCs = c.getOptionalServices();
    	String socNetworkType;

    	try {
    		System.out.println("PricePlan on contract is: [" + c.getPricePlan().getCode() + "]");
    	
    		System.out.println("\tIncluded SOCs: [");
    		for(int i =0; i < inclSOCs.length; i++) { 
    			socNetworkType = inclSOCs[i].getService().getNetworkType();
    			System.out.println("\tSOC #" + i + "- " + inclSOCs[i].getCode() + 
    						   " -> NetworkType = " + socNetworkType);
    		
    			//Check to ensure SOC is valid for contract (i.e. SOC networkType must match subscribers 
    			//equipment networkType
    			if (!(subscriberNetworkType.equals(NetworkType.NETWORK_TYPE_ALL) || 
    					socNetworkType.equals(NetworkType.NETWORK_TYPE_ALL)))
    				assertEquals(subscriberNetworkType, socNetworkType);
    		}
    		System.out.println("\t]");
    	
    	
    		System.out.println("\tOptional SOCs: [");
    		for(int j =0; j < optSOCs.length; j++) { 
    			socNetworkType = optSOCs[j].getService().getNetworkType();    	
    			System.out.println("\tSOC #" + j + "- " + optSOCs[j].getCode()+ 
    					" -> NetworkType = " + optSOCs[j].getService().getNetworkType());
    		
    			//Check to ensure SOC is valid for contract (i.e. SOC networkType must match subscribers 
    			//equipment networkType
    			if (!(subscriberNetworkType.equals(NetworkType.NETWORK_TYPE_ALL) || 
    					socNetworkType.equals(NetworkType.NETWORK_TYPE_ALL)))
    				assertEquals(subscriberNetworkType, socNetworkType);
    		}
    		System.out.println("\t]");
    	
    		System.out.println("\nEquipmentChangeRequest on contract is: ");
    		if (c.getEquipmentChangeRequest() != null)
    			System.out.println("\tserial no --> " + c.getEquipmentChangeRequest().getNewEquipment().getSerialNumber());
    		else
    			System.out.println("\tserial no --> N/A");
    	} catch (TelusAPIException t) {
    		System.out.println("Error encountered in printContract(): [");
    		t.printStackTrace();
    		System.out.println("]");
    	}
    }
    
    
    private void changeSubEquipment(String phoneNumber, String equipment) throws Throwable {
    	String USIM = "";
    	
    	Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
    	assertNotNull(subscriber);
    	
    	// HSPA SIMs used up: 8912230000000066266
    	Equipment newE = api.getEquipmentManager().getEquipment(equipment);
    	assertNotNull(newE);
    	
    	subscriber.changeEquipment(newE, "99999", "00003", "18654", null, "replacement");
    }

    private void setupData(String env) {
    	
    	if (env.equalsIgnoreCase("d1")) {
    	    HSPA_SUBSCRIBER = "9057160569";
    	    PCS_SUBSCRIBER = "9057160566";
    	    IDEN_SUBSCRIBER = "5148200007";
    	           
    	    MATCHING_IDEN_PRICE_PLAN_CODE = "MCWK50NC";
    	    MATCHING_PCS_PRICE_PLAN_CODE = "PTCAE250";
    	    MATCHING_HSPA_PRICE_PLAN_CODE = "PTCAE250"; 		//needs to be set properly once env data is set up
    	    NON_MATCHING_IDEN_PRICE_PLAN_CODE = "MCWK50NC";	//needs to be set properly once env data is set up
    	    NON_MATCHING_PCS_PRICE_PLAN_CODE = "PTCAE250";	//needs to be set properly once env data is set up
    	    NON_MATCHING_HSPA_PRICE_PLAN_CODE = "PTCAE250";	//needs to be set properly once env data is set up
    	    PCS_SERVICE_CODE = "S6VM10";
    	    IDEN_SERVICE_CODE = "MSLD15OM";
    	    HSPA_SERVICE_CODE = "SCFBLK";
    	    
    	    PCS_EQUIPMENT = "270113177701549998";
    	    IDEN_EQUIPMENT = "21101035069";
    	    HSPA_EQUIPMENT = "8912230000000066274"; 
    	    
    	} else if (env.equalsIgnoreCase("d2")) {
    		
    	} else if (env.equalsIgnoreCase("d3")) {
    		
    	} else if (env.equalsIgnoreCase("sit")) {
    		
    	} else if (env.equalsIgnoreCase("qa")) {
    		
    		HSPA_SUBSCRIBER = "4161752304";
    	    PCS_SUBSCRIBER = "4033180402"; 
    	    IDEN_SUBSCRIBER = "4161705110"; //ban 70616292
    	           
    	    MATCHING_IDEN_PRICE_PLAN_CODE = "PYC40TB  ";
    	    MATCHING_PCS_PRICE_PLAN_CODE = "PBS100MP";
    	    MATCHING_HSPA_PRICE_PLAN_CODE = "PYC40TB  "; 		
    	    NON_MATCHING_IDEN_PRICE_PLAN_CODE = "PBS100MP";		//needs to be set properly once env data is set up
    	    NON_MATCHING_PCS_PRICE_PLAN_CODE = "MCWK50NC";		//needs to be set properly once env data is set up
    	    NON_MATCHING_HSPA_PRICE_PLAN_CODE = "PBS100MP";		//needs to be set properly once env data is set up
    	    PCS_SERVICE_CODE = "S6VM10";
    	    IDEN_SERVICE_CODE = "MSLD15OM";
    	    HSPA_SERVICE_CODE = "SCFBLK";

    	    PCS_EQUIPMENT = "15603173578";
    	    IDEN_EQUIPMENT = "000500000623110";
    	    HSPA_EQUIPMENT = "8912230000000096412"; 
    		
    	} else if (env.equalsIgnoreCase("pt168")) {
    	    HSPA_SUBSCRIBER = "6472144889";
    	    PCS_SUBSCRIBER = "9057577325";
    	    IDEN_SUBSCRIBER = "9054243973";
    	           
    	    MATCHING_IDEN_PRICE_PLAN_CODE = "PYC40TB  ";
    	    MATCHING_PCS_PRICE_PLAN_CODE = "PTCAE250";
    	    MATCHING_HSPA_PRICE_PLAN_CODE = "PYC40TB  "; 		
    	    NON_MATCHING_IDEN_PRICE_PLAN_CODE = "MCWK50NC";		//needs to be set properly once env data is set up
    	    NON_MATCHING_PCS_PRICE_PLAN_CODE = "PTCAE250";		//needs to be set properly once env data is set up
    	    NON_MATCHING_HSPA_PRICE_PLAN_CODE = "PTCAE250";		//needs to be set properly once env data is set up
    	    PCS_SERVICE_CODE = "S6VM10";
    	    IDEN_SERVICE_CODE = "MSLD15OM";
    	    HSPA_SERVICE_CODE = "SCFBLK";

    	    PCS_EQUIPMENT = "13000002322";
    	    IDEN_EQUIPMENT = "000100000010583";
    	    HSPA_EQUIPMENT = "8912230000000096412"; 
    	    
    	} else if (env.equalsIgnoreCase("stag")) {
    		
    	} else if (env.equalsIgnoreCase("csi")) {
    		
    	} else if (env.equalsIgnoreCase("prod")) {
    		
    	} else { //default to D1 settings
    		HSPA_SUBSCRIBER = "9057160569";
    	    PCS_SUBSCRIBER = "9057160566";
    	    IDEN_SUBSCRIBER = "5148200007";
    	           
    	    MATCHING_IDEN_PRICE_PLAN_CODE = "MCWK50NC";
    	    MATCHING_PCS_PRICE_PLAN_CODE = "PTCAE250";
    	    MATCHING_HSPA_PRICE_PLAN_CODE = "PTCAE250"; 		//needs to be set properly once env data is set up
    	    NON_MATCHING_IDEN_PRICE_PLAN_CODE = "MCWK50NC";	//needs to be set properly once env data is set up
    	    NON_MATCHING_PCS_PRICE_PLAN_CODE = "PTCAE250";	//needs to be set properly once env data is set up
    	    NON_MATCHING_HSPA_PRICE_PLAN_CODE = "PTCAE250";	//needs to be set properly once env data is set up
    	    PCS_SERVICE_CODE = "S6VM10";
    	    IDEN_SERVICE_CODE = "MSLD15OM";
    	    HSPA_SERVICE_CODE = "SCFBLK";

    	    PCS_EQUIPMENT = "270113177701549998";
    	    IDEN_EQUIPMENT = "21101035069";
    	    HSPA_EQUIPMENT = "8912230000000066274"; 
    	        		
    	}
    }
    
    public void testRetrieveCallingCircleNumbers() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException{
    	Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("7056061776");
//    	ContractFeature[] contract = subscriber.getContract().getFeatures();
//    	for(int i=0;i<contract.length;i++){
//    		System.out.println("Inside for");
//    		ContractFeature con = contract[i];
//    		if(con.getFeature().isCallingCircle()){
//        		System.out.println("Inside if");
//
//    			CallingCircleParameters par= con.getCallingCircleParameters();
//    	    	System.out.println("11---"+par.getCallingCircleCurrentPhoneNumberList());
//
//    		}
//    	}
    	
    	RatedFeatureInfo related = new RatedFeatureInfo();
    	related.setCode("CCAIR");
    	related.setDescription("Favourite Numbers - Free # Loc");
    	related.setDescriptionFrench("No. préférés - # Locaux");
    	related.setParameterRequired(true);
    	related.setDuplFeatureAllowed(true);
    	related.setAdditionalNumberRequired(false);
    	related.setWirelessWeb(false);
    	related.setTelephony(true);
    	related.setDispatch(false);
    	related.setSwitchCode("CLCRCL");
    	related.setCategoryCode("CC");
    	related.setRecurringCharge(0.0);
    	related.setUsageCharge(0.0);
    	related.setMinutePoolingContributor(false);
    	related.setPrepaidCallingCircle(false);
    	related.setWPS(false);
    	
    	ServiceFeatureInfo serFeatureInfo = new ServiceFeatureInfo(related);
    	serFeatureInfo.featureParam = "CALLING-CIRCLE=@";
    	 	serFeatureInfo.getParentCode();
 //   	serFeatureInfo.setParent(parent)
    	BaseAgreementInfo base = new BaseAgreementInfo();
  	
    	TMContractFeature obj = new TMContractFeature(provider,serFeatureInfo,subscriber);
    	CallingCircleParameters calling = obj.getCallingCircleParameters();
    	System.out.println("11---"+calling.getCallingCircleCurrentPhoneNumberList());

    }
}



