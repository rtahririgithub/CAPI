package com.telus.api.contract;

import org.junit.Test;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.PrepaidConsumerAccount;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSet;
import com.telus.provider.account.TMContract;
import com.telus.provider.account.TMSubscriber;
import com.telus.api.BaseTest;

public class TestContractManagement_VVM extends BaseTest {
	
    static {
        //setuplocalHost();
    	//setupD1();
     	//setupSMARTDESKTOP_PT168();
    	setupINTECA_CSI();
    }


    
    /* Global private members */
    private static Subscriber subscriber;
    private static String SOC_VVM = "SIVVMO";
    
    public TestContractManagement_VVM(String name) throws Throwable {
        super(name);
    }

    
    public void testAll() throws Throwable{
    	String subNo_withVM = "6472144889";
    	String subNo_withoutVM = "4161752304";
    	
    	testSaveContractWithVVM(subNo_withVM);
     }
    
 
      
    public void testSaveContractWithVVM(String subscriberNum) {

    	try {
    		subscriber = api.getAccountManager().findSubscriberByPhoneNumber(subscriberNum);
    		assertNotNull(subscriber);
    	   		        
    		subscriber.getContract().addService(SOC_VVM);
    		subscriber.getContract().save();        
    		
    		ContractService[] contractServices = subscriber.getContract().getOptionalServices();
        	for (int i = 0; i < contractServices.length; i++) {
        		assertEquals(contractServices[i].getCode().trim(), SOC_VVM);
        	}   		
    	
    		System.out.println("\n\nContract state at end of method: \n\t");
    	
    	} catch (Throwable t) {
    		System.out.println("Error encountered in saveContract(): [");
    		t.printStackTrace();
    		System.out.println("]");
    	}

    }   
    
    
    public void testMandatoryServiceSets() {

    	try {
    		subscriber = api.getAccountManager().findSubscriberByPhoneNumber("6471258083");
    		assertNotNull(subscriber);
    		PricePlanSummary pricePlanSummary = api.getReferenceDataManager().getPricePlan("PVC50NAT");
    		System.out.println(pricePlanSummary.getCode() + " " + pricePlanSummary.getDescription() + " " + pricePlanSummary.getBrandId());
    		System.out.println("P" + " " + subscriber.getMarketProvince() + " " + "I" + " " + "R");
    		PricePlan pricePlan = pricePlanSummary.getPricePlan("P", subscriber.getMarketProvince(),  'I', 'R');
    		 ServiceSet[]  serviceSets = pricePlan.getMandatoryServiceSets("P", "C");
    		 
    		 for(ServiceSet set:serviceSets){
    			 System.out.println ("Name of Set " + set.getDescription() + "");
    			 for(Service service: set.getServices()){
    				 System.out.println ("Name of service " + service + ""); 
    			 }
    		 }
    	
    	
    	} catch (Throwable t) {
    		System.out.println("Error encountered in saveContract(): [");
    		t.printStackTrace();
    		System.out.println("]");
    	}

    }   
    
    public void testConflictInclude(){
    	try {
    		subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4160501145");
    		assertNotNull(subscriber);
    		PricePlanSummary pricePlanSummary = api.getReferenceDataManager().getPricePlan("XPBZSH27");
    		System.out.println(pricePlanSummary.getCode() + " " + pricePlanSummary.getDescription() + " " + pricePlanSummary.getBrandId());
    		System.out.println("P" + " " + subscriber.getMarketProvince() + " " + "I" + " " + "R");
    		Service service = api.getReferenceDataManager().getRegularService("SBEW6AOM");
    		TMContract contract =  (TMContract) subscriber.getContract();
            ContractService[] includedServices = contract.getIncludedServices();
            
    		for(ContractService includedService: includedServices){
    			System.out.println("included  Service before adding [" + includedService+ "]");
    		}
    		subscriber.getContract().addService(service);
 
       		for(ContractService includedService: includedServices){
    			System.out.println("included  Service before adding [" + includedService+ "]");
    		}
 
       		
    		ContractService[] deletedServices = contract.getDeletedServices();
    		for(ContractService contractservice: deletedServices){
    			System.out.println("Deleted Service [" + contractservice+ "]");
    		}
    		
    		ContractService[] addServices = contract.getAddedServices();
    		for(ContractService addService: addServices){
    			System.out.println("Added Service [" + addService+ "]");
    		}
    		
    		ContractFeature[] addFeatures = contract.getDeletedFeatures();
    		for(ContractFeature contractFeature: addFeatures){
    			System.out.println("Add Features  [" + contractFeature+ "]");
    		}
    		
    		ContractFeature[] removedFeatures = contract.getDeletedFeatures();
    		for(ContractFeature removedFeature: removedFeatures){
    			System.out.println("Remove Feature [" + removedFeature+ "]");
    		}
    		
    		ContractFeature[] changedFeatures = contract.getChangedFeatures();
    		for(ContractFeature changedFeature: changedFeatures){
    			System.out.println("Changed Feature [" + changedFeature+ "]");
    		}
    		
    		
    		ContractService[] ChangedService = contract.getChangedServices();
            
    		for(ContractService changedService: ChangedService){
    			System.out.println("changed Service  Service [" + changedService+ "]");
    		}
    		
    		contract.save();
    		contract = (TMContract)subscriber.getContract();
    		
    	} catch (Throwable t) {
    		System.out.println("Error encountered in saveContract(): [");
    		t.printStackTrace();
    		System.out.println("]");
    	}
    	
    }
    
    
   
    public void testChangeAccountNameForSubscriber() throws TelusAPIException {
       

       try {
          Subscriber subscriber =  api.getAccountManager().findSubscriberByPhoneNumber("4160715847");

          Account account = api.getAccountManager().findAccountByBAN(subscriber.getBanId()); 
          PrepaidConsumerAccount acc = (PrepaidConsumerAccount) account;
          ConsumerName consumerName = acc.getName();
          System.out.println(consumerName);
     
          ConsumerName[] names = account.getAuthorizedNames();
        
          if(names.length == 0){
        	  names = new ConsumerName[1];
        	  names[0] = consumerName;
          }
          for(ConsumerName name: names){
        	  System.out.println(name);
        	  name.setLastName("Somasundaram" + name.getLastName());
          }
          account.saveAuthorizedNames(names);
          account.refresh();

         ConsumerName[] authorizedNames = account.getAuthorizedNames();
         for(ConsumerName name: authorizedNames){
       	  System.out.println(name);
         }

       } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       }
    }
}



