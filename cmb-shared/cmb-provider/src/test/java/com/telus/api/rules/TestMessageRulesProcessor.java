package com.telus.api.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.telus.api.BaseTest;
import com.telus.api.ClientAPI;
import com.telus.api.account.Account;
import com.telus.api.account.Contract;
import com.telus.api.account.PoolingPricePlanSubscriberCount;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.Subscriber;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.reference.PricePlan;
import com.telus.api.rules.message.MessageContext;
import com.telus.api.rules.message.MessageResult;
import com.telus.api.rules.message.MessageRulesProcessor;
import com.telus.api.rules.message.MessageSubscriberContext;
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.provider.account.TMAccount;
import com.telus.provider.rules.Rule;

public class TestMessageRulesProcessor extends BaseTest {
	
    static {
        //setuplocalHost();
        //setupD3();
    	//setupJ();
    	//setupSMARTDESKTOPQA();
    	setupEASECA_QA();
    }

    public TestMessageRulesProcessor(String name) throws Throwable {
        super(name);
    }
        
    public void _testPoolingCounts() throws Throwable {
    	
    	
    	TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(660416);	
    	
    	PricePlanSubscriberCount[] test = account.getLDMinutePoolingEnabledPricePlanSubscriberCounts();
    	PoolingPricePlanSubscriberCount count = account.getPoolingEnabledPricePlanSubscriberCount(4, false);
    	PoolingPricePlanSubscriberCount countA = account.getPoolingEnabledPricePlanSubscriberCount(2, false);
    	PoolingPricePlanSubscriberCount countB = account.getPoolingEnabledPricePlanSubscriberCount(4, true);
    	PoolingPricePlanSubscriberCount[]counts3 = account.getPoolingEnabledPricePlanSubscriberCount(true);
    	
    	//Part 2
    	
    	/* ************************************** D3 TEST DATA BLOCK BEGINS ******************************************
    	 * *
    	String[] blah = {"1XNVPN250", "SDOC1", "SWWSS"};
    	String[] blah2 = {"H", "O"};
    	PoolingPricePlanSubscriberCount[] info = provider.getAccountHelperEJB().retrievePoolingPricePlanSubscriberCounts(55028432, 4); //returns array of 1 element
    	PricePlanSubscriberCountInfo[] info2 = provider.getAccountHelperEJB().retrieveShareablePricePlanSubscriberCount(3411446); //returns array of 1 element
    	ServiceSubscriberCount[] info3 = provider.getAccountHelperEJB().retrieveServiceSubscriberCounts(20829377, blah, false); //returns array of 1 element
    	ServiceSubscriberCount[] info3a = provider.getAccountHelperEJB().retrieveServiceSubscriberCounts(1003458, blah, false); //returns array of 0 element
    	PricePlanSubscriberCount[] info4 = provider.getAccountHelperEJB().retrieveMinutePoolingEnabledPricePlanSubscriberCounts(1003458, blah2);//returns array of 2 elements
    	PricePlanSubscriberCount[] info9 = provider.getAccountHelperEJB().retrieveDollarPoolingPricePlanSubscriberCounts(1004650);

    	* ************************************** D3 TEST DATA BLOCK ENDS ********************************************
    	 * */
    	
    	/* ************************************** SIT TEST DATA BLOCK BEGINS ******************************************
    	 * */
    	String[] blah = {"SLD25RFPM", "SDOC1", "SWWSS", "S911"};
    	String[] blah2 = {"H", "O"};
    	List info = provider.getAccountInformationHelper().retrievePoolingPricePlanSubscriberCounts(660416, 4); //returns array of 1 element
    	List info2 = provider.getAccountInformationHelper().retrieveShareablePricePlanSubscriberCount(660416); //returns array of 1 element
    	List info3 = provider.getAccountInformationHelper().retrieveServiceSubscriberCounts(660416, blah, false); //returns array of 0 element
    	List info3a = provider.getAccountInformationHelper().retrieveServiceSubscriberCounts(660416, blah, true); //returns array of 1 element
    	List info4 = provider.getAccountInformationHelper().retrieveMinutePoolingEnabledPricePlanSubscriberCounts(660416, blah2);//returns array of 3 elements
    	//PricePlanSubscriberCount[] info9 = provider.getAccountHelperEJB().retrieveDollarPoolingPricePlanSubscriberCounts(1004650);//returns array of 0 elements

    	/* ************************************** SIT TEST DATA BLOCK ENDS ********************************************
    	 * */
    	
    	System.out.println("Done!");   	
    }
    
    
    public void _testRuleRetrieval() throws Throwable {
    	
    	Rule[] rules = provider.getRulesProcessor0().getRulesByCategory(1);    	
    	Rule rule = provider.getRulesProcessor0().getRule("2", "5", "552");
    	
    	System.out.print("done");
    }
    

    public void testPoolingRule_MP_CONMP() throws Throwable {
        
    	
/* SIT test data used by Kevin Smith - SD team */
   		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4033829195");
    	Account account = api.getAccountManager().findAccountByBAN(12474);
    	PricePlan newPricePlan = subscriber.getAvailablePricePlan("AT20D2   ");
/**/    	


    	Contract newContract = subscriber.newContract(newPricePlan, Subscriber.TERM_PRESERVE_COMMITMENT);

    	//newContract.addService("SNMBUS8");
    		      
    	ArrayList msgs = new ArrayList();
    	MessageRulesProcessor messageRulesProcessor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
    	MessageSubscriberContext messageSubscriberContext = messageRulesProcessor.newMessageSubscriberContext(subscriber, newContract);
    		  			
    	MessageContext messageContext = 
    		messageRulesProcessor.newMessageContext(Result.CATEGORY_MESSAGE_ALL,Context.TRANSACTION_TYPE_SERVICE_CHANGE, 
    				account,null,new MessageSubscriberContext[] {messageSubscriberContext}, false, 200);
    		  		 
    	MessageResult[] messageResults = (MessageResult[]) messageRulesProcessor.processRules(messageContext);
    		
    	for (int i = 0; i < messageResults.length; i ++) {
    		MessageResult messageResult = messageResults[i];
    		ApplicationMessage[] applicationMessages = messageResult.getResults();
    		for (int j = 0; j < applicationMessages.length; j ++) {
    			msgs.add(applicationMessages[j]);
    			System.err.println("Message " + applicationMessages[j].getText(Locale.ENGLISH));
    		}
    	}    	
    }
    
    public void _testPoolingRules() throws Throwable {
    
    	System.out.println("Getting instance of ClientAPI...");
    	String kbId = "12463";
    	String kbPwd = "toronto";
    	String appId = "SSERVE";
    	ClientAPI api = ClientAPI.getInstance(kbId, kbPwd, appId);

    	
/* Vlad B. test data */ 
   		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4033829195");
    	Account account = api.getAccountManager().findAccountByBAN(12474);
    	PricePlan newPricePlan = subscriber.getAvailablePricePlan("AT20D2   ");
/**/    	


    	Contract newContract = subscriber.newContract(newPricePlan, Subscriber.TERM_PRESERVE_COMMITMENT);

    	newContract.addService("SNMBUS8");
    		      
    	ArrayList msgs = new ArrayList();
    	MessageRulesProcessor messageRulesProcessor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
    	MessageSubscriberContext messageSubscriberContext = messageRulesProcessor.newMessageSubscriberContext(subscriber, newContract);
    		  			
    	MessageContext messageContext = 
    		messageRulesProcessor.newMessageContext(Result.CATEGORY_MESSAGE_ALL,Context.TRANSACTION_TYPE_SERVICE_CHANGE, 
    				account,null,new MessageSubscriberContext[] {messageSubscriberContext}, false, 200);
    		  		 
    	MessageResult[] messageResults = (MessageResult[]) messageRulesProcessor.processRules(messageContext);
    		
    	for (int i = 0; i < messageResults.length; i ++) {
    		MessageResult messageResult = messageResults[i];
    		ApplicationMessage[] applicationMessages = messageResult.getResults();
    		for (int j = 0; j < applicationMessages.length; j ++) {
    			msgs.add(applicationMessages[j]);
    			System.err.println("Message " + applicationMessages[j].getText(Locale.ENGLISH));
    		}
    	}    	
    }

    public void _testMobileAppRules() throws Throwable {
        
    	System.out.println("Getting instance of ClientAPI...");
    	String kbId = "12463";
    	String kbPwd = "toronto";
    	String appId = "SSERVE";
    	ClientAPI api = ClientAPI.getInstance(kbId, kbPwd, appId);

    	
    	/*****PT 148 test data ************************************************************************/
   		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4033829195");
    	Account account = api.getAccountManager().findAccountByBAN(12474);
    	PricePlan newPricePlan = subscriber.getAvailablePricePlan("AT20D2   ");
    	/********************************************************************************************/


    	Contract newContract = subscriber.newContract(newPricePlan, Subscriber.TERM_PRESERVE_COMMITMENT);
    	newContract.addService("SNMBUS8");	//Need to ensure subscriber has equipment with either 3, 'c', 'p'

    	ArrayList msgs = new ArrayList();
    	MessageRulesProcessor messageRulesProcessor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
    	MessageSubscriberContext messageSubscriberContext = messageRulesProcessor.newMessageSubscriberContext(subscriber, newContract);
    		  			
    	MessageContext messageContext = 
    		messageRulesProcessor.newMessageContext(Result.CATEGORY_MESSAGE_ALL,Context.TRANSACTION_TYPE_SERVICE_CHANGE, 
    				account,null,new MessageSubscriberContext[] {messageSubscriberContext}, false, 200);
    		  		 
    	MessageResult[] messageResults = (MessageResult[]) messageRulesProcessor.processRules(messageContext);
    		
    	for (int i = 0; i < messageResults.length; i ++) {
    		MessageResult messageResult = messageResults[i];
    		ApplicationMessage[] applicationMessages = messageResult.getResults();
    		for (int j = 0; j < applicationMessages.length; j ++) {
    			msgs.add(applicationMessages[j]);
    			System.err.println("Message " + applicationMessages[j].getText(Locale.ENGLISH));
    		}
    	}    	
    }

}
