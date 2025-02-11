package com.telus.api.rules;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import com.telus.api.BaseTest;
import com.telus.api.ClientAPI;
import com.telus.api.account.Account;
import com.telus.api.account.Contract;
import com.telus.api.account.IDENAccount;
import com.telus.api.account.PCSAccount;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.PoolingPricePlanSubscriberCount;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.Subscriber;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PoolingGroup;
import com.telus.api.reference.PricePlan;
import com.telus.api.rules.message.MessageContext;
import com.telus.api.rules.message.MessageResult;
import com.telus.api.rules.message.MessageRulesProcessor;
import com.telus.api.rules.message.MessageSubscriberContext;
import com.telus.provider.account.TMIDENSubscriber;
import com.telus.provider.account.TMPCSSubscriber;
import com.telus.provider.account.TMSubscriber;

/**
 * @author Rich Fong
 */
public class TestRulesAPI extends BaseTest {
	
	private static ClientAPI api;
	
	static {	    
	    //setuplocalHost();
		setupSMARTDESKTOP_QA();
		//setupSMARTDESKTOP_PT168();

		try {
			api = ClientAPI.getInstance("18654", "apollo", ApplicationSummary.APP_SD);
			System.out.println("API instance created.");
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public TestRulesAPI(String name) throws Throwable{
		super(name);
	}
		
	public void _testRetrieveAccount() {

		try {
			Account account = api.getAccountManager().findAccountByBAN(70616680);
			assertNotNull(account);
			
			System.out.println("  account name    : " + account.getFullName());
			System.out.println("  province        : " + account.getHomeProvince());
			
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void _testRetrieveSubscriber() {
		
		try {
			Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4037109998");
			assertNotNull(subscriber);
			
			System.out.println("  subscriber firstname  : " + subscriber.getConsumerName().getFirstName());
			System.out.println("  subscriber lastname   : " + subscriber.getConsumerName().getLastName());
			System.out.println("  subscriber ID         : " + subscriber.getSubscriberId());
			System.out.println("  status                : " + subscriber.getStatus());
			System.out.println("  status date           : " + subscriber.getStatusDate());
			System.out.println("  product type          : " + subscriber.getProductType());
			System.out.println("  hotlined	            : " + subscriber.isHotlined());
			
			 if (((TMSubscriber)subscriber).getPortType() != null) {
				 System.out.println("  port type            : " + ((TMSubscriber)subscriber).getPortType());
			 }
			
			 System.out.println(subscriber.getNumberGroup().toString());
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void _testProcessActivationRules() {
		
		try {
			int ban = 197806;  
		
			Account account = api.getAccountManager().findAccountByBAN(ban);
			assertNotNull(account);
			System.out.println(account.toString());

			Subscriber subscriber = createSubscriber(account);
			assertNotNull(subscriber);
			System.out.println(subscriber.toString());
			
			PricePlan pricePlan = subscriber.getAvailablePricePlan("ABCTK30D3");  //defect test

			assertNotNull(pricePlan);
			System.out.println(pricePlan.toString());
			
			subscriber.newContract(pricePlan, Subscriber.TERM_3_YEARS);
			assertNotNull(subscriber.getContract());
			//subscriber.getContract().addService("MSMPPG3");
			System.out.println(subscriber.getContract().toString());

			PhoneNumberReservation phoneNumberReservation = reservePhoneNumber(subscriber);
			subscriber.reservePhoneNumber(phoneNumberReservation);
			
			MessageRulesProcessor processor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
			assertNotNull(processor);
			
			MessageSubscriberContext[] subContexts = {processor.newMessageSubscriberContext(subscriber, null)};
			MessageContext msgContext = processor.newMessageContext(Result.CATEGORY_MESSAGE_ALL, Context.TRANSACTION_TYPE_ACTIVATION, 
					account, null, subContexts, false, 100);
			
			System.out.println("Testing activation rules...");
			
			MessageResult[] results = (MessageResult[])processor.processRules(msgContext);
			assertNotNull(results);			
			for (int i = 0; i < results.length; i++) {
				ApplicationMessage[] messages = results[i].getResults();
				System.out.println("Result category: [" + results[i].getCategory() + "]\n");
				for (int j = 0; j < messages.length; j++) {
					System.out.println(messages[j].getType());
					System.out.println("ID: [" + messages[j].getId() + "]");
					System.out.println("Code: [" + messages[j].getCode() + "]");
					System.out.println("Text: [" + messages[j].getText(Locale.ENGLISH) + "]\n");
				}
			}			
			
			System.out.println("Finished!");
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void _testProcessMultipleActivationRules() {
		
		try {
			int ban = 197806;  
		
			Account account = api.getAccountManager().findAccountByBAN(ban);
			assertNotNull(account);

			Subscriber subscriber1 = createSubscriber(account);
			assertNotNull(subscriber1);			
			PricePlan pricePlan = subscriber1.getAvailablePricePlan("ABCTK30D3"); 
			assertNotNull(pricePlan);			
			subscriber1.newContract(pricePlan, Subscriber.TERM_3_YEARS);
			assertNotNull(subscriber1.getContract());
			PhoneNumberReservation phoneNumberReservation1 = reservePhoneNumber(subscriber1);
			subscriber1.reservePhoneNumber(phoneNumberReservation1);
			
			Subscriber subscriber2 = createSubscriber(account);
			assertNotNull(subscriber2);			
			PricePlan pricePlan2 = subscriber2.getAvailablePricePlan("ABCTK30D3"); 
			assertNotNull(pricePlan);
			subscriber2.newContract(pricePlan2, Subscriber.TERM_3_YEARS);
			assertNotNull(subscriber1.getContract());
			PhoneNumberReservation phoneNumberReservation2 = reservePhoneNumber(subscriber2);
			subscriber2.reservePhoneNumber(phoneNumberReservation2);
			
			MessageRulesProcessor processor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
			assertNotNull(processor);
			
			MessageSubscriberContext subContext1 = processor.newMessageSubscriberContext(subscriber1, null);
			MessageSubscriberContext subContext2 = processor.newMessageSubscriberContext(subscriber2, null);
			MessageSubscriberContext[] subContexts = {subContext1, subContext2};
			
			MessageContext msgContext = processor.newMessageContext(Result.CATEGORY_MESSAGE_ALL, Context.TRANSACTION_TYPE_ACTIVATION, 
					account, null, subContexts, false, 100);
			
			System.out.println("Testing activation rules...");
			
			MessageResult[] results = (MessageResult[])processor.processRules(msgContext);
			assertNotNull(results);			
			for (int i = 0; i < results.length; i++) {
				System.out.println("Result category: [" + results[i].getCategory() + "]\n");
				String[] subscriberIds = results[i].getSubscriberIds();
				for (int j = 0; j < subscriberIds.length; j++) {
					System.out.println("Subscriber ID: [" + subscriberIds[j] + "]\n");
					ApplicationMessage[] messages = results[i].getResultsBySubscriberId(subscriberIds[j]);
					for (int k = 0; k < messages.length; k++) {
						System.out.println(messages[k].getType());
						System.out.println("ID: [" + messages[k].getId() + "]");
						System.out.println("Code: [" + messages[k].getCode() + "]");
						System.out.println("Text: [" + messages[k].getText(Locale.ENGLISH) + "]\n");
					}
				}
			}			
			
			System.out.println("Finished!");
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void _testProcessPricePlanChangeRules() {
		
		try {
			String subscriberNo = "4035408463"; 
			int ban = 197806; 
			Account account = api.getAccountManager().findAccountByBAN(ban);
			assertNotNull(account);
			System.out.println(account.toString());
			
			Subscriber subscriber = account.getSubscriberByPhoneNumber(subscriberNo);
			assertNotNull(subscriber);
			System.out.println(subscriber.toString());
			
			//PricePlan pricePlan = subscriber.getAvailablePricePlan("PSHARE10");  // FNP100C(dollar pooling) PXTALK40  MCS70MC
			PricePlan pricePlan = subscriber.getAvailablePricePlan("FRE3");  // FNP100C(dollar pooling) PXTALK40  MCS70MC
			assertNotNull(pricePlan);
			System.out.println(pricePlan.toString());
			
		    Contract newContract = subscriber.newContract(pricePlan, Subscriber.TERM_PRESERVE_COMMITMENT);
			assertNotNull(newContract);
//			newContract.addService("MSMPPG4");
//			newContract.addService("MSMPPG7");
			System.out.println(newContract.toString());
			
			MessageRulesProcessor processor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
			assertNotNull(processor);
			
			MessageSubscriberContext[] subContexts = {processor.newMessageSubscriberContext(subscriber, newContract)};
			MessageContext msgContext = processor.newMessageContext(Result.CATEGORY_MESSAGE_ALL, Context.TRANSACTION_TYPE_SERVICE_CHANGE, 
					account, null, subContexts, false, 100);
			
			System.out.println("Testing price plan change rules...");
			
			MessageResult[] results = (MessageResult[])processor.processRules(msgContext);
			assertNotNull(results);			
			for (int i = 0; i < results.length; i++) {
				ApplicationMessage[] messages = results[i].getResults();
				System.out.println("Result category: [" + results[i].getCategory() + "]\n");
				for (int j = 0; j < messages.length; j++) {
					System.out.println(messages[j].getType());
					System.out.println("ID: [" + messages[j].getId() + "]");
					System.out.println("Code: [" + messages[j].getCode() + "]");
					System.out.println("Text: [" + messages[j].getText(Locale.ENGLISH) + "]\n");
				}
			}			
			
			System.out.println("Finished!");
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void testProcessServiceChangeRules() {
		
		try {
			String subscriberNo = "4035408463";
			int ban = 197806;
		
			Account account = api.getAccountManager().findAccountByBAN(ban);
			assertNotNull(account);
			System.out.println(account.toString());
			
			Subscriber subscriber = account.getSubscriberByPhoneNumber(subscriberNo);
			assertNotNull(subscriber);
			System.out.println(subscriber.toString());
					
			assertNotNull(subscriber.getContract());
		    subscriber.getContract().addService("FRE4");
			
			System.out.println(subscriber.getContract().toString());
			
			MessageRulesProcessor processor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
			assertNotNull(processor);
			
			MessageSubscriberContext[] subContexts = {processor.newMessageSubscriberContext(subscriber, null)};
			MessageContext msgContext = processor.newMessageContext(Result.CATEGORY_MESSAGE_ALL, Context.TRANSACTION_TYPE_SERVICE_CHANGE, 
					account, null, subContexts, false, 100);
			
			System.out.println("Testing service change rules...");
			
			MessageResult[] results = (MessageResult[])processor.processRules(msgContext);
			assertNotNull(results);			
			for (int i = 0; i < results.length; i++) {
				ApplicationMessage[] messages = results[i].getResults();
				System.out.println("Result category: [" + results[i].getCategory() + "]\n");
				for (int j = 0; j < messages.length; j++) {
					System.out.println(messages[j].getType());
					System.out.println("ID: [" + messages[j].getId() + "]");
					System.out.println("Code: [" + messages[j].getCode() + "]");
					System.out.println("Text: [" + messages[j].getText(Locale.ENGLISH) + "]\n");
				}
			}			
			
			System.out.println("Finished!");
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}

	public void _testProcessMoveRules() {
		
		try {
			
			String subscriberNo = "7807198318";
			int ban1 = 292007;
			int ban2 = 6005936;
		
			Account account1 = api.getAccountManager().findAccountByBAN(ban1);
			assertNotNull(account1);
			System.out.println(account1.toString());
			
			Subscriber subscriber = account1.getSubscriber(subscriberNo);
			assertNotNull(subscriber);
			System.out.println(subscriber.toString());
					
			Account account2 = api.getAccountManager().findAccountByBAN(ban2);
			assertNotNull(account2);
			System.out.println(account2.toString());
			
			MessageRulesProcessor processor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
			assertNotNull(processor);
			
			MessageSubscriberContext[] subContexts = {processor.newMessageSubscriberContext(subscriber, null)};
			MessageContext msgContext = processor.newMessageContext(Result.CATEGORY_MESSAGE_ALL, Context.TRANSACTION_TYPE_MOVE, 
					account1, account2, subContexts, false, 100);
			
			System.out.println("Testing move rules...");
			
			MessageResult[] results = (MessageResult[])processor.processRules(msgContext);
			assertNotNull(results);			
			for (int i = 0; i < results.length; i++) {
				ApplicationMessage[] messages = results[i].getResults();
				System.out.println("Result category: [" + results[i].getCategory() + "]\n");
				for (int j = 0; j < messages.length; j++) {
					System.out.println(messages[j].getType());
					System.out.println("ID: [" + messages[j].getId() + "]");
					System.out.println("Code: [" + messages[j].getCode() + "]");
					System.out.println("Text: [" + messages[j].getText(Locale.ENGLISH) + "]\n");
				}
			}			
			
			System.out.println("Finished!");
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void _testProcessMigrateRules() {
		
		try {
			
			String subscriberNo = "7781760305";
			int ban1 = 70616718;
			int ban2 = 6005936;
		
			Account account1 = api.getAccountManager().findAccountByBAN(ban1);
			assertNotNull(account1);
			System.out.println(account1.toString());
			
			Subscriber subscriber = account1.getSubscriber(subscriberNo);
			assertNotNull(subscriber);
			System.out.println(subscriber.toString());
			
			PricePlan pricePlan = subscriber.getAvailablePricePlan("PBUSTKCFN");  // FNP100C(dollar pooling) PXTALK40  MCS70MC  PPNTLKVM3  PSHARE10
			assertNotNull(pricePlan);
			System.out.println(pricePlan.toString());
			
		    Contract newContract = subscriber.newContract(pricePlan, Subscriber.TERM_PRESERVE_COMMITMENT);
			assertNotNull(newContract);
			System.out.println(newContract.toString());
					
			Account account2 = api.getAccountManager().findAccountByBAN(ban2);
			assertNotNull(account2);
			System.out.println(account2.toString());
			
			MessageRulesProcessor processor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
			assertNotNull(processor);
			
			MessageSubscriberContext[] subContexts = {processor.newMessageSubscriberContext(subscriber, subscriber.getContract())};
			MessageContext msgContext = processor.newMessageContext(Result.CATEGORY_MESSAGE_ALL, Context.TRANSACTION_TYPE_MIGRATE, 
					account1, account2, subContexts, false, 100);
			
			System.out.println("Testing migrate rules...");
			
			MessageResult[] results = (MessageResult[])processor.processRules(msgContext);
			assertNotNull(results);			
			for (int i = 0; i < results.length; i++) {
				ApplicationMessage[] messages = results[i].getResults();
				System.out.println("Result category: [" + results[i].getCategory() + "]\n");
				for (int j = 0; j < messages.length; j++) {
					System.out.println(messages[j].getType());
					System.out.println("ID: [" + messages[j].getId() + "]");
					System.out.println("Code: [" + messages[j].getCode() + "]");
					System.out.println("Text: [" + messages[j].getText(Locale.ENGLISH) + "]\n");
				}
			}			
			
			System.out.println("Finished!");
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void _testProcessStatusChangeRules() {
		
		try {
			
			String subscriberNo = "4161752898";
			int ban = 70616711;
		
			Account account = api.getAccountManager().findAccountByBAN(ban);
			assertNotNull(account);
			System.out.println(account.toString());
			
			Subscriber subscriber = account.getSubscriberByPhoneNumber(subscriberNo);
			assertNotNull(subscriber);
			System.out.println(subscriber.toString());
			
			MessageRulesProcessor processor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
			assertNotNull(processor);
			
			MessageSubscriberContext[] subContexts = {processor.newMessageSubscriberContext(subscriber, null)};
			// *** Note *** Context formerly known as Context.TRANSACTION_TYPE_STATUS_CHANGE.  
			//              Use either TRANSCATION_TYPE_RESTORE_RESUME or TRANSACTION_TYPE_SUSPEND_CANCEL 
			//              for the context (depending on which transaction you are testing against
			MessageContext msgContext = processor.newMessageContext(Result.CATEGORY_MESSAGE_ALL, Context.TRANSACTION_TYPE_RESTORE_RESUME, 
					account, null, subContexts, false, 100);
			
			System.out.println("Testing status change rules...");
			
			MessageResult[] results = (MessageResult[])processor.processRules(msgContext);
			assertNotNull(results);			
			for (int i = 0; i < results.length; i++) {
				ApplicationMessage[] messages = results[i].getResults();
				System.out.println("Result category: [" + results[i].getCategory() + "]\n");
				for (int j = 0; j < messages.length; j++) {
					System.out.println(messages[j].getType());
					System.out.println("ID: [" + messages[j].getId() + "]");
					System.out.println("Code: [" + messages[j].getCode() + "]");
					System.out.println("Text: [" + messages[j].getText(Locale.ENGLISH) + "]\n");
				}
			}			
			
			System.out.println("Finished!");
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void _testProcessInitiateTOWNRules() {
		
		try {
		
			String subscriberNo = "4161705195";
			int ban1 = 70616704;
			int ban2 = 70616683;
		
			Account account1 = api.getAccountManager().findAccountByBAN(ban1);
			assertNotNull(account1);
			System.out.println(account1.toString());
			
			Subscriber subscriber = account1.getSubscriberByPhoneNumber(subscriberNo);
			assertNotNull(subscriber);
			System.out.println(subscriber.toString());
			
			Account account2 = api.getAccountManager().findAccountByBAN(ban2);
			assertNotNull(account2);
			System.out.println(account2.toString());
			
			MessageRulesProcessor processor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
			assertNotNull(processor);
			
			MessageSubscriberContext[] subContexts = {processor.newMessageSubscriberContext(subscriber, null)};
			MessageContext msgContext = processor.newMessageContext(Result.CATEGORY_MESSAGE_ALL, Context.TRANSACTION_TYPE_INITIATE_TOWN, 
					account1, null, subContexts, false, 100);
			
			System.out.println("Testing initiate TOWN rules...");
			
			MessageResult[] results = (MessageResult[])processor.processRules(msgContext);
			assertNotNull(results);			
			for (int i = 0; i < results.length; i++) {
				ApplicationMessage[] messages = results[i].getResults();
				System.out.println("Result category: [" + results[i].getCategory() + "]\n");
				for (int j = 0; j < messages.length; j++) {
					System.out.println(messages[j].getType());
					System.out.println("ID: [" + messages[j].getId() + "]");
					System.out.println("Code: [" + messages[j].getCode() + "]");
					System.out.println("Text: [" + messages[j].getText(Locale.ENGLISH) + "]\n");
				}
			}			
			
			System.out.println("Finished!");
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void _testProcessCompleteTOWNRules() {
		
		try {
		
			String subscriberNo = "4161705195";
			int ban1 = 70616704;
			int ban2 = 70616683;
		
			Account account1 = api.getAccountManager().findAccountByBAN(ban1);
			assertNotNull(account1);
			System.out.println(account1.toString());
			
			Subscriber subscriber = account1.getSubscriber(subscriberNo);
			assertNotNull(subscriber);
			System.out.println(subscriber.toString());
			
			Account account2 = api.getAccountManager().findAccountByBAN(ban2);
			assertNotNull(account2);
			System.out.println(account2.toString());
			
			MessageRulesProcessor processor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
			assertNotNull(processor);
			
			MessageSubscriberContext[] subContexts = {processor.newMessageSubscriberContext(subscriber, null)};
			MessageContext msgContext = processor.newMessageContext(Result.CATEGORY_MESSAGE_ALL, Context.TRANSACTION_TYPE_COMPLETE_TOWN, 
					account1, account2, subContexts, false, 100);
			
			System.out.println("Testing complete TOWN rules...");
			
			MessageResult[] results = (MessageResult[])processor.processRules(msgContext);
			assertNotNull(results);			
			for (int i = 0; i < results.length; i++) {
				ApplicationMessage[] messages = results[i].getResults();
				System.out.println("Result category: [" + results[i].getCategory() + "]\n");
				for (int j = 0; j < messages.length; j++) {
					System.out.println(messages[j].getType());
					System.out.println("ID: [" + messages[j].getId() + "]");
					System.out.println("Code: [" + messages[j].getCode() + "]");
					System.out.println("Text: [" + messages[j].getText(Locale.ENGLISH) + "]\n");
				}
			}			
			
			System.out.println("Finished!");
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void _testProcessMobileAppsServiceChangeRules() {
		
		try {
			String subscriberNo = "7781760305";
			int ban = 70616718;
		
			Account account = api.getAccountManager().findAccountByBAN(ban);
			assertNotNull(account);
			System.out.println(account.toString());
			
			Subscriber subscriber = account.getSubscriber(subscriberNo);
			assertNotNull(subscriber);
			System.out.println(subscriber.toString());
					
			assertNotNull(subscriber.getContract());
		    subscriber.getContract().addService("SMBAPNO  ");
			System.out.println(subscriber.getContract().toString());
			
			MessageRulesProcessor processor = (MessageRulesProcessor)api.getRulesProcessor(RulesProcessor.PROCESSOR_ID_MESSAGE);
			assertNotNull(processor);
			
			MessageSubscriberContext[] subContexts = {processor.newMessageSubscriberContext(subscriber, null)};
			MessageContext msgContext = processor.newMessageContext(Result.CATEGORY_MESSAGE_ALL, Context.TRANSACTION_TYPE_SERVICE_CHANGE, 
					account, null, subContexts, false, 100);
			
			System.out.println("Testing MAP service change rules...");
			
			MessageResult[] results = (MessageResult[])processor.processRules(msgContext);
			assertNotNull(results);			
			for (int i = 0; i < results.length; i++) {
				ApplicationMessage[] messages = results[i].getResults();
				System.out.println("Result category: [" + results[i].getCategory() + "]\n");
				for (int j = 0; j < messages.length; j++) {
					System.out.println(messages[j].getType());
					System.out.println("ID: [" + messages[j].getId() + "]");
					System.out.println("Code: [" + messages[j].getCode() + "]");
					System.out.println("Text: [" + messages[j].getText(Locale.ENGLISH) + "]\n");
				}
			}			
			
			System.out.println("Finished!");
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void _testCheckPoolingSubscriber() {
		
		try {
			String subscriberNo = "7781760305"; 			
			Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(subscriberNo);
			assertNotNull(subscriber);
			System.out.println(subscriber.toString());
			
			PoolingGroup[] poolingGroups = api.getReferenceDataManager().getPoolingGroups();
			
			System.out.println("Testing subscriber pooling status...");
			
			for (int i = 0; i < poolingGroups.length; i++) {
				subscriber.getContract().isPoolingEnabled(poolingGroups[i].getPoolingGroupId());
				System.out.println("Subscriber isPoolingEnabled(" + 
						poolingGroups[i].getPoolingGroupId() + ") = [" + subscriber.getContract().isPoolingEnabled(poolingGroups[i].getPoolingGroupId()) + "]");
			}

			System.out.println("Testing subscriber shareable status...");
			System.out.println("Subscriber isShareable = [" + subscriber.getContract().isShareable() + "]");
			
			System.out.println("Testing subscriber dollar pooling status...");
			System.out.println("Subscriber isDollarPooling = [" + subscriber.getContract().isDollarPooling() + "]");
			
			System.out.println("Finished!");
			 
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	public void _testPricePlanSubscriberCounts() {
		
		try {
			int ban = 70616718;  
						
			PostpaidAccount account = (PostpaidAccount)api.getAccountManager().findAccountByBAN(ban);
			assertNotNull(account);
			System.out.println("Retrieved BAN = [" + account.getBanId() + "]");
			System.out.println("active subscriber count [" + account.getActiveSubscribersCount() + "]");
			String[] subscriberNumbers = account.getActiveSubscriberPhoneNumbers(5);
			for (int i = 0; i < subscriberNumbers.length; i++) {
				System.out.println("subscriber number [" + subscriberNumbers[i] + "]");
			}

			System.out.println("Retrieving all pooling price plan subscriber counts...");
			PoolingPricePlanSubscriberCount[] poolingCounts = account.getPoolingEnabledPricePlanSubscriberCount(true);
			
			ArrayList poolingList = new ArrayList();
			for (int h = 0; h < (poolingCounts != null ? poolingCounts.length : 0); h++) {
				poolingList.clear();
				System.out.println("Pooling group = [" + poolingCounts[h].getPoolingGroupId() + "]");
				PricePlanSubscriberCount[] poolingPricePlanSubscriberCount = poolingCounts[h].getPricePlanSubscriberCount();
				if (poolingPricePlanSubscriberCount != null) {
					System.out.println("Retrieved minute pooling price plan subscriber counts [" + poolingPricePlanSubscriberCount.length + "]");
					for (int i = 0; i < poolingPricePlanSubscriberCount.length; i++) {
						poolingList.add(poolingPricePlanSubscriberCount[i]);
					}
				}
				printPricePlanSubscriberCount(poolingList, account);
			}
			
			System.out.println("Finished!");
			
			PoolingGroup[] poolingGroups = api.getReferenceDataManager().getPoolingGroups();
			
			System.out.println("Retrieving inidividual pooling price plan subscriber counts...");	
			for (int j = 0; j < poolingGroups.length; j++) {
				System.out.println("Pooling group = [" + poolingGroups[j].getPoolingGroupId() + "]");
				poolingList.clear();
				PoolingPricePlanSubscriberCount poolingCount = account.getPoolingEnabledPricePlanSubscriberCount(poolingGroups[j].getPoolingGroupId(), false);
				if (poolingCount != null) {
					PricePlanSubscriberCount[] poolingPricePlanSubscriberCount = poolingCount.getPricePlanSubscriberCount();
					if (poolingPricePlanSubscriberCount != null) {
						System.out.println("Retrieved minute pooling price plan subscriber counts [" + poolingPricePlanSubscriberCount.length + "]");
						for (int i = 0; i < poolingPricePlanSubscriberCount.length; i++) {
							poolingList.add(poolingPricePlanSubscriberCount[i]);
						}
					}
					printPricePlanSubscriberCount(poolingList, account);
					
				} else {
					System.out.println("No pooling subscribers for pooling group [" + poolingGroups[j].getPoolingGroupId() + "]");	
				}
			}
			
			System.out.println("Finished!");
			
			System.out.println("Retrieving zero-minute pooling price plan subscriber counts...");	
			for (int j = 0; j < poolingGroups.length; j++) {
				System.out.println("Pooling group = [" + poolingGroups[j].getPoolingGroupId() + "]");
				poolingList.clear();
				PoolingPricePlanSubscriberCount poolingCount = account.getZeroMinutePoolingEnabledPricePlanSubscriberCount(poolingGroups[j].getPoolingGroupId(), false);
				if (poolingCount != null) {
					PricePlanSubscriberCount[] poolingPricePlanSubscriberCount = poolingCount.getPricePlanSubscriberCount();
					if (poolingPricePlanSubscriberCount != null) {
						System.out.println("Retrieved zero-minute pooling price plan subscriber counts [" + poolingPricePlanSubscriberCount.length + "]");
						for (int i = 0; i < poolingPricePlanSubscriberCount.length; i++) {
							poolingList.add(poolingPricePlanSubscriberCount[i]);
						}
					}
					printPricePlanSubscriberCount(poolingList, account);
					
				} else {
					System.out.println("No zero-minute pooling subscribers for pooling group [" + poolingGroups[j].getPoolingGroupId() + "]");	
				}
			}
			
			System.out.println("Finished!");
			
			System.out.println("Retrieving dollar pooling price plan subscriber counts...");	
			for (int j = 0; j < poolingGroups.length; j++) {
				System.out.println("Pooling group = [" + poolingGroups[j].getPoolingGroupId() + "]");
				poolingList.clear();
				PricePlanSubscriberCount[] dollarPoolingCounts = account.getDollarPoolingPricePlanSubscriberCount(false);
				if (dollarPoolingCounts != null) {
					System.out.println("Retrieved dollar pooling price plan subscriber counts [" + dollarPoolingCounts.length + "]");
					for (int i = 0; i < dollarPoolingCounts.length; i++) {
						poolingList.add(dollarPoolingCounts[i]);
					}
					printPricePlanSubscriberCount(poolingList, account);

				} else {
					System.out.println("No dollar pooling subscribers for pooling group [" + poolingGroups[j].getPoolingGroupId() + "]");	
				}
			}
			
			System.out.println("Finished!");

		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}
	
	private void printPricePlanSubscriberCount(ArrayList list, Account account) {
		
		try {
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {			
				PricePlanSubscriberCount currentPricePlantSubscriberCount = (PricePlanSubscriberCount)iterator.next();
				System.out.println("Price plan = [" + currentPricePlantSubscriberCount.getPricePlanCode() + "]");
				System.out.println("Active subscriber count = " + currentPricePlantSubscriberCount.getActiveSubscribers().length);
				for (int i = 0; i < (currentPricePlantSubscriberCount.getActiveSubscribers() != null ? currentPricePlantSubscriberCount.getActiveSubscribers().length : 0); i++) {
					String phoneNumber = account.getPhoneNumberBySubscriberID(currentPricePlantSubscriberCount.getActiveSubscribers()[i]);	
					System.out.println("subscriber ID: [" + currentPricePlantSubscriberCount.getActiveSubscribers()[i] + "]");
					System.out.println("subscriber phone number: [" + phoneNumber + "]");
				}
				System.out.println("Suspended subscriber count = " + currentPricePlantSubscriberCount.getSuspendedSubscribers().length);			
				for (int i = 0; i < (currentPricePlantSubscriberCount.getSuspendedSubscribers() != null ? currentPricePlantSubscriberCount.getSuspendedSubscribers().length : 0); i++) {
					String phoneNumber = account.getPhoneNumberBySubscriberID(currentPricePlantSubscriberCount.getSuspendedSubscribers()[i]);	
					System.out.println("subscriber ID: [" + currentPricePlantSubscriberCount.getSuspendedSubscribers()[i] + "]");
					System.out.println("subscriber phone number: [" + phoneNumber + "]");
				}
				System.out.println("Cancelled subscriber count = " + currentPricePlantSubscriberCount.getCanceledSubscribers().length);							
				for (int i = 0; i < (currentPricePlantSubscriberCount.getCanceledSubscribers() != null ? currentPricePlantSubscriberCount.getCanceledSubscribers().length : 0); i++) {
					String phoneNumber = account.getPhoneNumberBySubscriberID(currentPricePlantSubscriberCount.getCanceledSubscribers()[i]);	
					System.out.println("subscriber ID: [" + currentPricePlantSubscriberCount.getCanceledSubscribers()[i] + "]");
					System.out.println("subscriber phone number: [" + phoneNumber + "]");
				}
			}	
		} catch (Throwable t) {
			t.printStackTrace();
			fail();
		}
	}

	
	private Subscriber createSubscriber(Account account) throws Throwable {
		
		String[] serialNumbers = getSerialNumbers(Subscriber.PRODUCT_TYPE_PCS, false, 1);
	    TMPCSSubscriber subscriber = (TMPCSSubscriber)((PCSAccount)account).newPCSSubscriber(serialNumbers[0], false, null);
	    subscriber.setFirstName("Dele");
	    subscriber.setLastName("Taylor");
	    subscriber.setLanguage("EN");
	    subscriber.setBirthDate(new Date(1960 - 1900, 01 - 1, 01));
	    subscriber.setEmailAddress("dele.taylor@telusmobility.com");
	    subscriber.setActivityReasonCode("CMER");
	    subscriber.getDelegate().setMarketProvince("NS");
		assertNotNull(subscriber);
		System.out.println(subscriber.toString());
		
		return subscriber;
	}
	
	private Subscriber createIdenSubscriber(Account account) throws Throwable {
		
		String[] serialNumbers = getSerialNumbers(Subscriber.PRODUCT_TYPE_IDEN, false, 1);
		TMIDENSubscriber subscriber = (TMIDENSubscriber)((IDENAccount)account).newIDENSubscriber(serialNumbers[0], false, null);
	    subscriber.setFirstName("Dele");
	    subscriber.setLastName("Taylor");
	    subscriber.setLanguage("EN");
	    subscriber.setBirthDate(new Date(1960 - 1900, 01 - 1, 01));
	    subscriber.setEmailAddress("dele.taylor@telusmobility.com");
	    subscriber.setActivityReasonCode("CMER");
	    subscriber.getDelegate().setMarketProvince("PQ");
		assertNotNull(subscriber);
		System.out.println(subscriber.toString());
		
		return subscriber;
	}
	
	private PhoneNumberReservation reservePhoneNumber(Subscriber subscriber) throws Throwable {

		NumberGroup[] numberGroups = api.getReferenceDataManager().getNumberGroups(
				subscriber.getAccount().getAccountType(),
				subscriber.getAccount().getAccountSubType(),
				subscriber.getProductType(),
				String.valueOf(subscriber.getEquipment().getEquipmentType())
		);
		NumberGroup numberGroup = null;
		for(int i = 0; i < numberGroups.length; i++) {
			if("NS".equals(numberGroups[i].getProvinceCode())) {
				numberGroup = numberGroups[i];
				break;
			}
		}
		PhoneNumberReservation phoneNumberReservation = api.getAccountManager().newPhoneNumberReservation();
		phoneNumberReservation.clear();
		phoneNumberReservation.setNumberGroup(numberGroup);
	    phoneNumberReservation.setAsian(false);
	    phoneNumberReservation.setLikeMatch(false);
	    phoneNumberReservation.setWaiveSearchFee(true);
	    phoneNumberReservation.setPhoneNumberPattern("**********");
	    assertNotNull(phoneNumberReservation);
		System.out.println("Phone number reservation: [" + phoneNumberReservation.toString() + "]");
		
		return phoneNumberReservation;
	}
}
