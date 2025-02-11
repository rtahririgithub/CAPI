package com.telus.api.rules;

import java.util.List;

import com.telus.api.BaseTest;
import com.telus.api.account.Account;
import com.telus.api.account.PoolingPricePlanSubscriberCount;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.reference.PoolingGroup;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.provider.account.TMAccount;
import com.telus.provider.rules.Rule;

public class TestPoolingSubscriberCounts extends BaseTest {
	
    static {
        //setuplocalHost();
        //setupD3();
    	//setupSMARTDESKTOPJ();
    	setupINTECA_QA();
    }

    public TestPoolingSubscriberCounts(String name) throws Throwable {
        super(name);
    }

    public void _testPoolingCountsD3() throws Throwable {
    	
    	//Part 1 check Provider functions for various scenarios
    	//TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(1004400);	//D3 for MP
    	//TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(1004650);	//D3 for DP - IDEN
//    	TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(20007202);	//D3 for DP - PCS
      	TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(1003458);	//D3 for DP - PCS
    	
//    	PricePlanSubscriberCount[] test = account.getLDMinutePoolingEnabledPricePlanSubscriberCounts();
//    	PoolingPricePlanSubscriberCount count = account.getPoolingEnabledPricePlanSubscriberCount(4, false);
//    	PoolingPricePlanSubscriberCount countA = account.getPoolingEnabledPricePlanSubscriberCount(2, false);
//    	PoolingPricePlanSubscriberCount countB = account.getPoolingEnabledPricePlanSubscriberCount(1, false);
////    	PoolingPricePlanSubscriberCount[]counts2 = account.getPoolingEnabledPricePlanSubscriberCount(false);
//    	PoolingPricePlanSubscriberCount[]counts3 = account.getPoolingEnabledPricePlanSubscriberCount(true);
//    	PricePlanSubscriberCount[]counts4 = account.getDollarPoolingPricePlanSubscriberCount(false);
      	PoolingPricePlanSubscriberCount[] counts = account.getZeroMinutePoolingEnabledPricePlanSubscriberCount(true);
      	PoolingPricePlanSubscriberCount count = account.getZeroMinutePoolingEnabledPricePlanSubscriberCount(1, true);
    	
    	//Part 2
//    	String[] blah = {"1XNVPN250", "SDOC1", "SWWSS"};
//    	String[] blah2 = {"H", "O"};
//    	PoolingPricePlanSubscriberCount[] info = provider.getAccountHelperEJB().retrievePoolingPricePlanSubscriberCounts(55028432, 4); //returns array of 1 element
//    	PricePlanSubscriberCountInfo[] info2 = provider.getAccountHelperEJB().retrieveShareablePricePlanSubscriberCount(3411446); //returns array of 1 element
//    	ServiceSubscriberCount[] info3 = provider.getAccountHelperEJB().retrieveServiceSubscriberCounts(20829377, blah, false); //returns array of 1 element
//    	ServiceSubscriberCount[] info3a = provider.getAccountHelperEJB().retrieveServiceSubscriberCounts(1003458, blah, false); //returns array of 0 element
//    	PricePlanSubscriberCount[] info4 = provider.getAccountHelperEJB().retrieveMinutePoolingEnabledPricePlanSubscriberCounts(1003458, blah2);//returns array of 2 elements
//    	PricePlanSubscriberCount[] info9 = provider.getAccountHelperEJB().retrieveDollarPoolingPricePlanSubscriberCounts(1004650, "I");
//    	PricePlanSubscriberCount[] info9a = provider.getAccountHelperEJB().retrieveDollarPoolingPricePlanSubscriberCounts(20008131, "C");
//    	PricePlanSubscriberCount[] info9b = provider.getAccountHelperEJB().retrieveDollarPoolingPricePlanSubscriberCounts(20007202, "C");
//    	PricePlanSubscriberCount[] info9c = provider.getAccountHelperEJB().retrieveDollarPoolingPricePlanSubscriberCounts(20008205, "C");
//    	PricePlanSubscriberCount[] info9d = provider.getAccountHelperEJB().retrieveDollarPoolingPricePlanSubscriberCounts(20007848, "C");
//    	PricePlanSubscriberCount[] info9e = provider.getAccountHelperEJB().retrieveDollarPoolingPricePlanSubscriberCounts(70020495, "C");
//    	PricePlanSubscriberCount[] info9f = provider.getAccountHelperEJB().retrieveDollarPoolingPricePlanSubscriberCounts(1005503, "C");
    	
    	System.out.println("Done!");
    	
    }

    
    public void testPoolingCountsSIT() throws Throwable {
    	
    	TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(55028432);	//SIT

    	PricePlanSubscriberCount[] test = account.getLDMinutePoolingEnabledPricePlanSubscriberCounts();
    	PoolingPricePlanSubscriberCount count = account.getPoolingEnabledPricePlanSubscriberCount(4, false);
    	PoolingPricePlanSubscriberCount countA = account.getPoolingEnabledPricePlanSubscriberCount(2, false);
    	PoolingPricePlanSubscriberCount countB = account.getPoolingEnabledPricePlanSubscriberCount(4, true);
//    	PoolingPricePlanSubscriberCount[]counts2 = account.getPoolingEnabledPricePlanSubscriberCount(false);
    	PoolingPricePlanSubscriberCount[]counts3 = account.getPoolingEnabledPricePlanSubscriberCount(true);
    	PricePlanSubscriberCount[]counts4 = account.getDollarPoolingPricePlanSubscriberCount(false);
    	
    	//Part 2
    	String[] blah = {"SLD25RFPM", "SDOC1", "SWWSS", "S911"};
    	String[] blah2 = {"H", "O"};
    	//PoolingPricePlanSubscriberCount[] info = provider.getAccountHelperEJB().retrievePoolingPricePlanSubscriberCounts(55028432, 4); //returns array of 1 element
    	List info2 = provider.getAccountInformationHelper().retrieveShareablePricePlanSubscriberCount(3411446); //returns array of 1 element
    	List info3 = provider.getAccountInformationHelper().retrieveServiceSubscriberCounts(14331, blah, false); //returns array of 0 element
    	List info3a = provider.getAccountInformationHelper().retrieveServiceSubscriberCounts(1431, blah, true); //returns array of 1 element
    	List info4 = provider.getAccountInformationHelper().retrieveMinutePoolingEnabledPricePlanSubscriberCounts(55028432, blah2);//returns array of 3 elements
    	List info9 = provider.getAccountInformationHelper().retrieveDollarPoolingPricePlanSubscriberCounts(1004650, "I");//returns array of 0 elements
    	
    	System.out.println("Done!");
    	
    }

    
    public void _testPoolingCountsQA() throws Throwable {
    	
    	//Part 1 check Provider functions for various scenarios
    	TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(12474);	
    	
    	
    	
    	PricePlanSubscriberCount[] test = account.getLDMinutePoolingEnabledPricePlanSubscriberCounts();
    	PoolingPricePlanSubscriberCount count = account.getPoolingEnabledPricePlanSubscriberCount(4, false);
    	PoolingPricePlanSubscriberCount countA = account.getPoolingEnabledPricePlanSubscriberCount(2, false);
    	PoolingPricePlanSubscriberCount countB = account.getPoolingEnabledPricePlanSubscriberCount(1, false);
    	PoolingPricePlanSubscriberCount[]counts3 = account.getPoolingEnabledPricePlanSubscriberCount(true);
    	PricePlanSubscriberCount[]counts4 = account.getDollarPoolingPricePlanSubscriberCount(false);
    	PoolingPricePlanSubscriberCount[]counts5 = account.getZeroMinutePoolingEnabledPricePlanSubscriberCount(true);
//    	PoolingPricePlanSubscriberCount counts6 = account.getZeroMinutePoolingEnabledPricePlanSubscriberCount(1,true);
//    	PoolingPricePlanSubscriberCount counts7 = account.getZeroMinutePoolingEnabledPricePlanSubscriberCount(1,false);
    	
//    	for (int i = 0; i<counts3.length; i++)
//    		System.out.println("Pool Group ID: " + counts3[i].getPoolingGroupId());
    	//Part 2
    	String[] blah = {"M36P     ", "AT20D2   ", "TPR1     ","PCMS40B4 "};
    	String[] blah2 = {"H", "O"};
    	List info = provider.getAccountInformationHelper().retrievePoolingPricePlanSubscriberCounts(12474, 4); //returns array of 1 element
    	List info2 = provider.getAccountInformationHelper().retrieveShareablePricePlanSubscriberCount(70616685); //returns array of 1 element
    	List info3 = provider.getAccountInformationHelper().retrieveServiceSubscriberCounts(70616685, blah, false); //returns array of 1 element
    	List info3a = provider.getAccountInformationHelper().retrieveServiceSubscriberCounts(12474, blah, false); //returns array of 0 element
    	List info4 = provider.getAccountInformationHelper().retrieveMinutePoolingEnabledPricePlanSubscriberCounts(12474, blah2);//returns array of 2 elements
    	List info9 = provider.getAccountInformationHelper().retrieveDollarPoolingPricePlanSubscriberCounts(12474, "I");
    	List info9a = provider.getAccountInformationHelper().retrieveDollarPoolingPricePlanSubscriberCounts(70616685, "C");
    	List info9b = provider.getAccountInformationHelper().retrieveDollarPoolingPricePlanSubscriberCounts(70616678, "C");
    	List info9c = provider.getAccountInformationHelper().retrieveDollarPoolingPricePlanSubscriberCounts(12474, "C");
    	List info9d = provider.getAccountInformationHelper().retrieveDollarPoolingPricePlanSubscriberCounts(70616684, "C");
    	
    	System.out.println("Done!");
    	
    }
    

    public void _testPoolingCountsQA_Defect() throws Throwable {
    	
    	//Part 1 check Provider functions for various scenarios
    	TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70548701);	//D3 for MP

    	
       	PoolingPricePlanSubscriberCount poolCount_AT = account.getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.AIRTIME, true);
   
    	PoolingPricePlanSubscriberCount poolCount_LD = account.getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.LONG_DISTANCE, true);
    	PoolingPricePlanSubscriberCount poolCounts_RAT = account.getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.ROAMING_AIRTIME, true);
    	PoolingPricePlanSubscriberCount poolCounts_RLD = account.getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.ROAMING_LONG_DISTANCE, true);
    	
    	PoolingPricePlanSubscriberCount[]poolCount_ALL = account.getPoolingEnabledPricePlanSubscriberCount(false);
    	   	
    	
       	PoolingPricePlanSubscriberCount poolCount_AT2 = account.getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.AIRTIME, true);
        
    	PoolingPricePlanSubscriberCount poolCount_LD2 = account.getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.LONG_DISTANCE, true);
    	
    	PoolingPricePlanSubscriberCount[]poolCount_ALL2 = account.getPoolingEnabledPricePlanSubscriberCount(false);
    	System.out.println("Done!");
    	
    }
    
    public void _testBLAH() throws Throwable {
        Account account0 = api.getAccountManager().findAccountByBAN(
        		2088);
       

//           PoolingPricePlanSubscriberCount ppp = ( (PostpaidAccount) account0).
//                getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.AIRTIME, true);
//            ppp = ( (PostpaidAccount) account0).
//                getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.LONG_DISTANCE, true);
//            ppp = ( (PostpaidAccount) account0).
//                getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.
//                ROAMING_AIRTIME, true);
//            ppp = ( (PostpaidAccount) account0).
//                getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.
//                ROAMING_LONG_DISTANCE, true);
//
//
//            PoolingPricePlanSubscriberCount[] ppps = ( (PostpaidAccount) account0).
//                getPoolingEnabledPricePlanSubscriberCount(false);

//      It shows group, 1, 4, 7, 8 all have subscribers.  ONLY THIS WORKS
//
//      >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            PoolingPricePlanSubscriberCount ppp = ( (PostpaidAccount) account0).
                getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.AIRTIME, true);
            ppp = ( (PostpaidAccount) account0).
                getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.LONG_DISTANCE, true);

          PoolingPricePlanSubscriberCount[] ppps = ( (PostpaidAccount) account0).
                getPoolingEnabledPricePlanSubscriberCount(false);

//      It shows only group, 1, 4 all have subscribers
//
//      >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//            PoolingPricePlanSubscriberCount[]  ppps = ( (PostpaidAccount) account0).
//                getPoolingEnabledPricePlanSubscriberCount(true);
//
//
//            PoolingPricePlanSubscriberCount[]  ppps1 = ( (PostpaidAccount) account0).
//                getPoolingEnabledPricePlanSubscriberCount(false);
    }

    
    public void _testBlah2() throws Throwable {

        Account account0 = api.getAccountManager().findAccountByBAN(
        		70616680);


        PoolingPricePlanSubscriberCount[]  ppps = ( (PostpaidAccount) account0).
            getPoolingEnabledPricePlanSubscriberCount(true);
        
         for (int i=0; i< ppps.length; i++)  {


           System.out.println("ppps[i].getPoolingGroupId() " + ppps[i].getPoolingGroupId());
         PricePlanSubscriberCount[]   ppsc = ppps[i].getPricePlanSubscriberCount();
           for (int j=0; j<ppsc.length; j++) {
         System.out.println(">>>>>>>>>>>>>>>>>ppps[i].getPoolingGroupId() " + ppps[i].getPoolingGroupId());
         System.out.println("ppsc[j].getPricePlanCode() " + ppsc[j].getPricePlanCode());
             String [] ssssub = ppsc[j].getActiveSubscribers();
             for (int k=0; k<ssssub.length; k++) {
         System.out.println("ppsc[j].getActiveSubscribers().toString() " + ssssub[k]);
         System.out.println("<<<<<<<<<<<<<<<<<<<,ppps[i].getPoolingGroupId() " + ppps[i].getPoolingGroupId());
             }
           }
         }
         

         ppps = ( (PostpaidAccount) account0).
            getPoolingEnabledPricePlanSubscriberCount(false);

        for (int i = 0; i < ppps.length; i++) {

          System.out.println("ppps[i].getPoolingGroupId() " +
                             ppps[i].getPoolingGroupId());
          PricePlanSubscriberCount[] ppsc = ppps[i].getPricePlanSubscriberCount();
          for (int j = 0; j < ppsc.length; j++) {
            System.out.println(">>>>>>>>>>>>>>>>>ppps[i].getPoolingGroupId() " +
                               ppps[i].getPoolingGroupId());
            System.out.println("ppsc[j].getPricePlanCode() " +
                               ppsc[j].getPricePlanCode());
            String[] ssssub = ppsc[j].getActiveSubscribers();
            for (int k = 0; k < ssssub.length; k++) {
              System.out.println("ppsc[j].getActiveSubscribers().toString() " +
                                 ssssub[k]);
              System.out.println(
                  "<<<<<<<<<<<<<<<<<<<,ppps[i].getPoolingGroupId() " +
                  ppps[i].getPoolingGroupId());
            }
          }
        }


        if (true) {
          return;
        }

    }
    
    
//    public void testBlah3() throws Throwable {
//    	
//    	PCSPostpaidConsumerAccount pCSPostpaidConsumerAccount = newPCSPostpaidConsumerAccount(true);
//    	Subscriber subscriber = newPCSSubscriber(pCSPostpaidConsumerAccount, null,
//    			"PTCAE250", 36, true);
//    	
//    	AvailablePhoneNumber[] numbers = subscriber.findAvailablePhoneNumbers(api.getAccountManager().newPhoneNumberReservation(), 10);
//    	System.out.println("Done!");
//    }
    
    public void _testPoolingCounts() throws Throwable {
    	
    	//PT!48
    	TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(70616680); 
    	PricePlanSubscriberCount[] test = account.getLDMinutePoolingEnabledPricePlanSubscriberCounts();
    	PoolingPricePlanSubscriberCount[] poolingSubs = account.getPoolingEnabledPricePlanSubscriberCount(true);
    	PricePlanSubscriberCount[] dollarSubs = account.getDollarPoolingPricePlanSubscriberCount(false);
    	System.out.println("dollarSubs count"+dollarSubs.length);
    	  	   	
    	System.out.println("Done!");
    	
    }

    
    public void _testRuleRetrieval() throws Throwable {
    	
    	Rule[] rules = provider.getRulesProcessor0().getRulesByCategory(1);
    	
    	System.out.print("done");
    }
    
}

