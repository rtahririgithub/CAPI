package com.telus.provider.rules.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractService;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.Subscriber;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.reference.PoolingGroup;
import com.telus.api.reference.Service;
import com.telus.api.rules.Context;
import com.telus.api.rules.Result;
import com.telus.api.rules.RulesException;
import com.telus.api.rules.message.MessageContext;
import com.telus.api.rules.message.MessageRulesProcessor;
import com.telus.api.rules.message.MessageSubscriberContext;
import com.telus.eas.utility.info.ConditionInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.message.TMApplicationMessage;
import com.telus.provider.rules.Comparators;
import com.telus.provider.rules.Rule;
import com.telus.provider.rules.RuleConstants;
import com.telus.provider.rules.RuleTemplate;
import com.telus.provider.rules.TMRulesProcessor;
import com.telus.provider.rules.WorkingMemory;
import com.telus.provider.rules.WorkingMemoryElement;
import com.telus.provider.util.Logger;

/**
 * This class is really a wrapper class that manages the interaction between calling interfaces and the
 * RulesProcessor for processing messaging rules.  It's purpose is to provide the necessary context and 
 * perform the orchestration in order to return the expected MessageResult objects. 
 * 
 * @author R. Fong
 * @version 1.0, 09-Jul-2008 
 **/
public class TMMessageRulesProcessor extends BaseProvider implements MessageRulesProcessor {

	private static final long serialVersionUID = 1L;
	private TMRulesProcessor rulesProcessor = null;
	private static Map rulesByCategoryAndTypeAndPoolGroupId = new Hashtable();
	private static Date lastinitRulesByPoolGroupId = null;
	
	public TMMessageRulesProcessor(TMProvider provider) throws TelusAPIException {
		super(provider);
		rulesProcessor = provider.getRulesProcessor0();
		// Lazy refresh of the static Map rulesByCategoryAndTypeAndPoolGroupId
		// If the master has been changed, then rebuild it
		if (lastinitRulesByPoolGroupId == null ||
			lastinitRulesByPoolGroupId.before(TMRulesProcessor.getLastRefreshTime())) {
			initRulesByPoolGroupId();
		}
	}

	/**
	 * Factory method for getting new instances of MessageContext.
	 * 
	 * @param int category
	 * @param int transactionType
	 * @param Account sourceAccount
	 * @param Account targetAccount
	 * @param MessageSubscriberContext[] subscriberContexts
	 * @param boolean includeReservedSubscribers
	 * @param int banSizeThreshold 
	 * @return MessageContext 
	 * @throws RulesException
	 * @throws TelusAPIException
	 */
	public MessageContext newMessageContext(int category, int transactionType, Account sourceAccount, Account targetAccount, 
			MessageSubscriberContext[] subscriberContexts, boolean includeReservedSubscribers, int banSizeThreshold)
	throws RulesException, TelusAPIException {

		if (lastinitRulesByPoolGroupId == null ||
			lastinitRulesByPoolGroupId.before(TMRulesProcessor.getLastRefreshTime())) {
			initRulesByPoolGroupId();
		}
		
		TMMessageContext tmMessageContext = TMMessageContext.newMessageContext(category, transactionType, sourceAccount, targetAccount, 
				subscriberContexts, includeReservedSubscribers, banSizeThreshold);	
		// validate the MessageSubscriberContext array for the given transactionType
		for (int i = 0; i < subscriberContexts.length; i++) {
			// only transactions involving a price plan change (service change and migrate) can utilize the new contract object
			if (subscriberContexts[i].getNewContract() != null && 
					(transactionType != Context.TRANSACTION_TYPE_SERVICE_CHANGE && transactionType != Context.TRANSACTION_TYPE_MIGRATE)) {
				throw new RulesException("Subscriber context new contract object is not null.  Invalid subscriber context for the given transaction type.", 
						RulesException.REASON_INVALID_SUBCRIBER_CONTEXT_TRANSACTION_TYPE_COMBINATION);
			}
			// migration transactions require the new contract object
			if (subscriberContexts[i].getNewContract() == null && transactionType == Context.TRANSACTION_TYPE_MIGRATE) {
				throw new RulesException("Subscriber context new contract object is null.  Invalid subscriber context for the given transaction type.", 
						RulesException.REASON_INVALID_SUBCRIBER_CONTEXT_TRANSACTION_TYPE_COMBINATION);
			}
		}

		return tmMessageContext;
	}
	
	/**
	 * Factory method for getting new instances of MessageSubscriberContext.
	 * NOTE: The contract parameter is optional.  This is only required for transactions where a new contract is created, such as
	 * a price plan change or migration.  It is NOT required for service changes which modify the subscriber's existing contract.
	 * 
	 * @param Subscriber subscriber
	 * @param Contract contract 
	 * @return MessageSubscriberContext
	 * @throws RulesException
	 * @throws TelusAPIException
	 */
	public MessageSubscriberContext newMessageSubscriberContext(Subscriber subscriber, Contract contract)
	throws RulesException, TelusAPIException {
		return TMMessageSubscriberContext.newMessageSubscriberContext(subscriber, contract);
	}
	
	/**
	 * Method for processes messaging rules.
	 * 
	 * @param Context context
	 * @return Result[]
	 * @throws RulesException
	 * @throws TelusAPIException
	 */
	public Result[] processRules(Context context) throws RulesException, TelusAPIException {

		List list = new ArrayList();
		TMMessageContext messageContext = (TMMessageContext)context;

		if (messageContext.getCategory() == Result.CATEGORY_MESSAGE_ALL) {
			// set the category and then process messages for that category
			messageContext.setCategory(Result.CATEGORY_MESSAGE_POOLING);
			list.add(processRulesByCategory(messageContext));
			// set the category and then process messages for that category
			messageContext.setCategory(Result.CATEGORY_MESSAGE_MOBILE_APPLICATIONS);
			list.add(processRulesByCategory(messageContext));
			
		} else {
			list.add(processRulesByCategory(messageContext));
		}
		
		// create and return the array of Result objects for all categories	
		return (TMMessageResult[])list.toArray(new TMMessageResult[list.size()]);
	}
	
	// Orchestrates the processing of rule categories.
	private TMMessageResult processRulesByCategory(TMMessageContext messageContext) throws RulesException, TelusAPIException {	
		
		Map messagesBySubscriber = new HashMap();
		MessageSubscriberContext[] subContexts = messageContext.getSubscriberContexts();
		
		// setup the initial processing context information
		WorkingMemory workingMemory = initializeWorkingMemory(messageContext);
		
		// iterate through all the subscriber contexts
		for (int i = 0; i < subContexts.length; i++) {
			
			// store the messages per subscriber in a list
			List list = new ArrayList();
			
			// add the subscriber objects to the context
			workingMemory.putAttribute(RuleConstants.ATTRIBUTE_SUBSCRIBER, 
					new WorkingMemoryElement("Subscriber", subContexts[i].getSubscriber(), true));
			workingMemory.putAttribute(RuleConstants.ATTRIBUTE_CONTRACT, 
					new WorkingMemoryElement("Contract", subContexts[i].getNewContract(), true));
			
			switch (messageContext.getCategory()) {
			case Result.CATEGORY_MESSAGE_POOLING:
				// add the messages from the result to the list
				list.addAll(processPoolingRules(messageContext, (TMMessageSubscriberContext)subContexts[i], workingMemory));
				break;
			
			case Result.CATEGORY_MESSAGE_MOBILE_APPLICATIONS:
				// add the messages from the result to the list
				list.addAll(processMobileAppRules(messageContext, (TMMessageSubscriberContext)subContexts[i], workingMemory));
				break;
				
			default:
				throw new RulesException("Invalid category: [" + messageContext.getCategory() + "].", RulesException.REASON_INVALID_CATEGORY);
			}
			
			// package the results per subscriber
			messagesBySubscriber.put(subContexts[i].getSubscriber().getSubscriberId(), (TMApplicationMessage[])list.toArray(
					new TMApplicationMessage[list.size()]));
		}
		
		return new TMMessageResult(messageContext.getCategory(), messagesBySubscriber);
	}
	
	// Orchestrates the processing of rules specific to the transactions involving Minute Pooling services.
	private List processPoolingRules(TMMessageContext messageContext, TMMessageSubscriberContext subContext, WorkingMemory workingMemory)
	throws TelusAPIException {
		
		// store the message IDs in a Set to make sure no duplicates are added 
		Set messageIds = new HashSet();
		
		// get all of the applicable rule templates from the cache
		RuleTemplate[] templates = rulesProcessor.getRuleTemplatesByCategory(messageContext.getCategory());		
		
		// get the set of pooling groups
		PoolingGroup[] poolingGroups = provider.getReferenceDataManager().getPoolingGroups();

		// iterate through the templates and process all rules for each one
		for (int i = 0; i < templates.length; i++) {
			// additional context is required here - put the current rule template into the processing context
			// explicitly set the modified parameter to true, to indicate that this attribute has changed in the working memory
			workingMemory.putAttribute(RuleConstants.ATTRIBUTE_RULE_TEMPLATE, 
					new WorkingMemoryElement("RuleTemplate", templates[i], true));
			
			// get the set of rules to be evaluated based on category and template type
			Rule[] rules = rulesProcessor.getRulesByCategoryAndType(messageContext.getCategory(), templates[i].getType());

			// process the pooling contributor change rule separately - it has different processing requirements
			if (templates[i].getType() == RuleConstants.RULE_TYPE_MP_POOLING_CONTRIBUTOR_SERVICE_CHANGE) {
				// get the impacted services - note we only care about lost or gained services, not features
				Service[] servicesGained = getServicesGained(messageContext, subContext);
				Service[] servicesLost = getServicesLost(messageContext, subContext);

				// additional context is required here - put the service transition type into the processing context
				workingMemory.putAttribute(RuleConstants.ATTRIBUTE_SERVICE_TRANSITION, 
						new WorkingMemoryElement("String", RuleConstants.TRANSITION_SERVICE_ADD, true));	
				
				// loop through all the services gained
				for (int s = 0; s < servicesGained.length; s++) {
					// additional context is required here - put the current service into the processing context
					// explicitly set the modified parameter to true, to indicate that this attribute has changed in the working memory
					workingMemory.putAttribute(RuleConstants.ATTRIBUTE_SERVICE, 
							new WorkingMemoryElement("Service", servicesGained[s], true));

					// loop through each rule in the array
					for (int t = 0; t < rules.length; t++) {
						// process the rule against the test rule
						if (rulesProcessor.processRule(rules[t], workingMemory)) {
							// if the rule matches, add the message ID to the set
							messageIds.add(rules[t].getResult().getValue());
						}
						// reset the modified boolean for each attribute to false
						// we're doing this here since all conditions for the current template have been evaluated at least once
						// if working memory attributes haven't changed, we shouldn't have to re-evaluate these conditions
						workingMemory.setModified(false);
					}
				}

				// additional context is required here - put the service transition type into the processing context
				workingMemory.putAttribute(RuleConstants.ATTRIBUTE_SERVICE_TRANSITION, 
						new WorkingMemoryElement("String", RuleConstants.TRANSITION_SERVICE_REMOVE, true));	
				
				// loop through all the services lost
				for (int u = 0; u < servicesLost.length; u++) {
					// additional context is required here - put the current category code into the processing context
					// explicitly set the modified parameter to true, to indicate that this attribute has changed in the working memory
					workingMemory.putAttribute(RuleConstants.ATTRIBUTE_SERVICE, 
							new WorkingMemoryElement("Service", servicesLost[u], true));

					// loop through each rule in the array
					for (int v = 0; v < rules.length; v++) {
						// process the rule against the test rule
						if (rulesProcessor.processRule(rules[v], workingMemory)) {
							// if the rule matches, add the message ID to the set
							messageIds.add(rules[v].getResult().getValue());
						}
						// reset the modified boolean for each attribute to false
						// we're doing this here since all conditions for the current template have been evaluated at least once
						// if working memory attributes haven't changed, we shouldn't have to re-evaluate these conditions
						workingMemory.setModified(false);
					}
				}			
				
			} else {
				// for all complete TOWN / move / migrate add service or conflicting service rules, the transition is relative to the
				// target account - swap the source account for the target account in working memory
				if (messageContext.getTransactionType() == Context.TRANSACTION_TYPE_COMPLETE_TOWN || 
						((messageContext.getTransactionType() == Context.TRANSACTION_TYPE_MOVE || 
								messageContext.getTransactionType() == Context.TRANSACTION_TYPE_MIGRATE) &&	
								(templates[i].getType() == RuleConstants.RULE_TYPE_MP_ADD_SERVICE || 
										templates[i].getType() == RuleConstants.RULE_TYPE_MP_CONFLICTING_SERVICE))) {
					// put the target account into working memory if it isn't already there
					WorkingMemoryElement accountElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_ACCOUNT);
					Account account = (Account)accountElement.getElement();
					if (account.getBanId() != messageContext.getTargetAccount().getBanId()) {
						workingMemory.putAttribute(RuleConstants.ATTRIBUTE_ACCOUNT, 
								new WorkingMemoryElement("Account", messageContext.getTargetAccount(), true));
					}

				// for all other rules and transaction types, the transition is relative to the source account
				// we'll capture this in the else clause		
				} else {
					// otherwise, for all other rules make sure the source account is in working memory
					WorkingMemoryElement accountElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_ACCOUNT);
					Account account = (Account)accountElement.getElement();
					if (account.getBanId() != messageContext.getSourceAccount().getBanId()) {
						workingMemory.putAttribute(RuleConstants.ATTRIBUTE_ACCOUNT, 
								new WorkingMemoryElement("Account", messageContext.getSourceAccount(), true));
					}
				}
								
				// loop through all the pooling groups
				for (int j = 0; j < poolingGroups.length; j++) {
					// additional context is required here - put the current pooling group ID into the processing context
					// explicitly set the modified parameter to true, to indicate that this attribute has changed in the working memory
					workingMemory.putAttribute(RuleConstants.ATTRIBUTE_POOLING_GROUP_ID, 
							new WorkingMemoryElement("Integer", new Integer(poolingGroups[j].getPoolingGroupId()), true));

					// get the set of rules to be evaluated based on category, template type and pool group Id
					Rule[] rulesByPoolGroupId  =
						getRulesByCategoryAndTypeAndPoolGroupId(
							messageContext.getCategory(),
							templates[i].getType(),
							poolingGroups[j].getPoolingGroupId());

					// loop through each rule in the array
					for (int k = 0; k < rulesByPoolGroupId.length; k++) {
						// process the rule against the test rule
						if (rulesProcessor.processRule(rulesByPoolGroupId[k], workingMemory)) {
							// if the rule matches, add the message ID to the set
							messageIds.add(rulesByPoolGroupId[k].getResult().getValue());
						}
						// reset the modified boolean for each attribute to false
						// we're doing this here since all conditions for the current template have been evaluated at least once
						// if working memory attributes haven't changed, we shouldn't have to re-evaluate these conditions
						workingMemory.setModified(false);
					}
				}
			}
		}
		
		// get the application messages and return them
		return getMessages(messageIds);
	}
	
	// Orchestrates the processing of rules specific to the addition and removal of Mobile Application services.
	private List processMobileAppRules(TMMessageContext messageContext, TMMessageSubscriberContext subContext, WorkingMemory workingMemory)
	throws TelusAPIException {
		
		// store the message IDs in a Set to make sure no duplicates are added 
		Set messageIds = new HashSet();
		
		// get all of the applicable rule templates from the cache
		RuleTemplate[] templates = rulesProcessor.getRuleTemplatesByCategory(messageContext.getCategory());		

		// get the impacted services - note we only care about lost or gained services, not features
		Service[] servicesGained = getServicesGained(messageContext, subContext);
		Service[] servicesLost = getServicesLost(messageContext, subContext);
		
		// loop through the templates and process all rules for each one
		for (int i = 0; i < templates.length; i++) {
			// additional context is required here - put the current rule template into the processing context
			// explicitly set the modified parameter to true, to indicate that this attribute has changed in the working memory
			workingMemory.putAttribute(RuleConstants.ATTRIBUTE_RULE_TEMPLATE,  
					new WorkingMemoryElement("RuleTemplate", templates[i], true));
			
			// get the set of rules to be evaluated based on category and template type
			Rule[] rules = rulesProcessor.getRulesByCategoryAndType(messageContext.getCategory(), templates[i].getType());

			// additional context is required here - put the service transition type into the processing context
			workingMemory.putAttribute(RuleConstants.ATTRIBUTE_SERVICE_TRANSITION, 
					new WorkingMemoryElement("String", RuleConstants.TRANSITION_SERVICE_ADD, true));	
			
			// loop through all the services gained
			for (int s = 0; s < servicesGained.length; s++) {
				// get the category codes for each service
				String[] categoryCodes = servicesGained[s].getCategoryCodes();
				
				// loop through the category codes
				for (int j = 0; j < categoryCodes.length; j++) {
					// additional context is required here - put the current category code into the processing context
					// explicitly set the modified parameter to true, to indicate that this attribute has changed in the working memory
					workingMemory.putAttribute(RuleConstants.ATTRIBUTE_CATEGORY_CODE, 
							new WorkingMemoryElement("String", categoryCodes[j], true));

					// loop through each rule in the array
					for (int k = 0; k < rules.length; k++) {
						// process the rule against the test rule
						if (rulesProcessor.processRule(rules[k], workingMemory)) {
							// if the rule matches, add the message ID to the set
							messageIds.add(rules[k].getResult().getValue());
						}
						// reset the modified boolean for each attribute to false
						// we're doing this here since all conditions for the current template have been evaluated at least once
						// if working memory attributes haven't changed, we shouldn't have to re-evaluate these conditions
						workingMemory.setModified(false);
					}
				}
			}

			// additional context is required here - put the service transition type into the processing context
			workingMemory.putAttribute(RuleConstants.ATTRIBUTE_SERVICE_TRANSITION, 
					new WorkingMemoryElement("String", RuleConstants.TRANSITION_SERVICE_REMOVE, true));	
			
			// loop through all the services lost
			for (int t = 0; t < servicesLost.length; t++) {
				// get the category codes for each service
				String[] categoryCodes = servicesLost[t].getCategoryCodes();
				
				// loop through the category codes
				for (int l = 0; l < categoryCodes.length; l++) {
					// additional context is required here - put the current category code into the processing context
					// explicitly set the modified parameter to true, to indicate that this attribute has changed in the working memory
					workingMemory.putAttribute(RuleConstants.ATTRIBUTE_CATEGORY_CODE, 
							new WorkingMemoryElement("String", categoryCodes[l], true));

					// loop through each rule in the array
					for (int m = 0; m < rules.length; m++) {
						// process the rule against the test rule
						if (rulesProcessor.processRule(rules[m], workingMemory)) {
							// if the rule matches, add the message ID to the set
							messageIds.add(rules[m].getResult().getValue());
						}
						// reset the modified boolean for each attribute to false
						// we're doing this here since all conditions for the current template have been evaluated at least once
						// if working memory attributes haven't changed, we shouldn't have to re-evaluate these conditions
						workingMemory.setModified(false);
					}
				}
			}	
		}
		
		// get the application messages and return them
		return getMessages(messageIds);
	}
	
	// This method returns all services gained from this transaction.
	private Service[] getServicesGained(MessageContext messageContext, MessageSubscriberContext subContext)
	throws TelusAPIException {

		Subscriber subscriber = subContext.getSubscriber();
		Contract oldContract = subscriber.getContract();
		Contract newContract = subContext.getNewContract();
		Set set = new HashSet();
		
		// if the new contract is not null, then this is a price plan change - services gained are on the new contract
		if (newContract != null) {
			// add all services from the new contract
			ContractService[] servicesGained = newContract.getServices();
			for (int i = 0; i < servicesGained.length; i++) {
				if (!servicesGained[i].getService().isBoundService() && !servicesGained[i].getService().isPromotion()) {
					// if the service is not in the old contract's list of services, add it
					if (!isServiceInList(servicesGained[i], oldContract.getServices()))
						set.add(servicesGained[i].getService()); 
				}
			}
			
		} else {
			// otherwise, this is a service change - get the added services from the old contract
			ContractService[] addedServices = oldContract.getAddedServices();
			for (int j = 0; j < addedServices.length; j++) {
				if (!addedServices[j].getService().isBoundService() && !addedServices[j].getService().isPromotion())
					set.add(addedServices[j].getService());
			}
		}

		return (Service[])set.toArray(new Service[set.size()]);
	}
	
	// This method returns all services lost from this transaction.
	private Service[] getServicesLost(MessageContext messageContext, MessageSubscriberContext subContext)
	throws TelusAPIException {

		Subscriber subscriber = subContext.getSubscriber();
		Contract oldContract = subscriber.getContract();
		Contract newContract = subContext.getNewContract();
		Set set = new HashSet();
		
		// if the new contract is not null, then this is a price plan change - services lost are on the old contract
		if (newContract != null) {
			// add all services from the old contract
			ContractService[] servicesLost = oldContract.getServices();
			for (int i = 0; i < servicesLost.length; i++) {
				if (!servicesLost[i].getService().isBoundService() && !servicesLost[i].getService().isPromotion()) {
					// if the service is not in the new contract's list of services, add it
					if (!isServiceInList(servicesLost[i], newContract.getServices()))
						set.add(servicesLost[i].getService());
				}
			}
			
		} else {
			// otherwise, this is a service change - get the deleted services from the old contract
			ContractService[] deletedServices = oldContract.getDeletedServices();
			for (int j = 0; j < deletedServices.length; j++) {
				if (!deletedServices[j].getService().isBoundService() && !deletedServices[j].getService().isPromotion()) {
					set.add(deletedServices[j].getService());
				}
			}
		}

		return (Service[])set.toArray(new Service[set.size()]);
	}

	// Checks if a given service is in the provided array of services.
	private boolean isServiceInList(ContractService service, ContractService[] services) {
		
		for (int i = 0; i < services.length; i++) {
			if (services[i].getCode().equals(service.getCode()))
				return true;
		}

		return false;
	}
	
	// Creates and populates the working memory with the relevant attributes for messaging rules.
	private WorkingMemory initializeWorkingMemory(TMMessageContext context) throws TelusAPIException {
		
		WorkingMemory workingMemory = new WorkingMemory();

		// add the source account object
		workingMemory.putAttribute(RuleConstants.ATTRIBUTE_ACCOUNT, new WorkingMemoryElement("Account", context.getSourceAccount()));
		
		// add the static message context attributes
		workingMemory.putAttribute(RuleConstants.ATTRIBUTE_BAN_SIZE_THRESHOLD, 
				new WorkingMemoryElement("Integer", new Integer(context.getBanSizeThreshold())));
		workingMemory.putAttribute(RuleConstants.ATTRIBUTE_TRANSACTION_TYPE, 
				new WorkingMemoryElement("Integer", new Integer(context.getTransactionType())));
		workingMemory.putAttribute(RuleConstants.ATTRIBUTE_INCLUDE_RESERVED_SUBSCRIBERS, 
				new WorkingMemoryElement("Boolean", new Boolean(context.includeReservedSubscribers())));
		
		// add reference data
		workingMemory.putAttribute(RuleConstants.ATTRIBUTE_MINUTE_POOLING_CONTRIBUTOR_SERVICES, 
				new WorkingMemoryElement("Service[]", provider.getReferenceDataManager0().getMinutePoolingContributorServices()));
		workingMemory.putAttribute(RuleConstants.ATTRIBUTE_POOLING_GROUPS, 
				new WorkingMemoryElement("PoolingGroup[]", provider.getReferenceDataManager().getPoolingGroups()));


		// Initialize the price plan subscribers - both pooling enabled and zero minute enabled, for all pooling groups.
		// So that the API will not query the DB many times per pool group id.
		// If the caches are there already, these do nothing.
		try {
			if (context.getSourceAccount() != null && (context.getSourceAccount().getAllSubscriberCount() < context.getBanSizeThreshold())
					&& context.getSourceAccount() instanceof PostpaidAccount) {
				//For minute pooling-enabled subscriber counts
				((com.telus.provider.account.TMAccount)context.getSourceAccount()).getPoolingEnabledPricePlanSubscriberCount(false);

				//For zero-minute pooling-enabled subscriber counts
				((com.telus.provider.account.TMAccount)context.getSourceAccount()).getZeroMinutePoolingEnabledPricePlanSubscriberCount(false);
			}
			if (context.getTargetAccount() != null  && (context.getTargetAccount().getAllSubscriberCount() < context.getBanSizeThreshold()) 
					&& context.getTargetAccount() instanceof PostpaidAccount) {
				//For minute pooling-enabled subscriber counts
				((com.telus.provider.account.TMAccount)context.getTargetAccount()).getPoolingEnabledPricePlanSubscriberCount(false);

				//For zero-minute pooling-enabled subscriber counts
				((com.telus.provider.account.TMAccount)context.getTargetAccount()).getZeroMinutePoolingEnabledPricePlanSubscriberCount(false);
			}
		}
		catch (Throwable e) {
			Logger.warning(e);
		}

		return workingMemory;
	}
	
	// Retrieves and returns the list of messages from the provided message IDs contained in the set.
	private List getMessages(Set set) {
		
		List list = new ArrayList();
		
		// iterate through the set to retrieve and add messages to the list
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			ApplicationMessage message = provider.getApplicationMessageWithSourceCode(Long.parseLong((String)iterator.next()));
			list.add(message);
		}
		
		return list;
	}

	/**
	 * Return the static Map rulesCategory 
	 * 
	 * @param None
	 * @return Map rulesByCategory
	 * @throws No exceptions thrown
	 */
	private synchronized void initRulesByPoolGroupId() {
		Map map = TMRulesProcessor.getRulesByCategory();

		synchronized (map) {
			if (map.size() == 0) {
				Logger.debug("initRulesByPoolGroupId - Master Map is empty");
				return;
			}
			
			// empty Map first before refresh
			rulesByCategoryAndTypeAndPoolGroupId.clear();
			
			Map rulesByType = null;
			Map rulesByPoolGroupId = null;

			Rule rule = null;
			int ruleSz = 0;

			Set entries = map.entrySet();
			Iterator it = entries.iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();

				if (entry.getValue() instanceof HashMap) {
					Map ruleTypes = (HashMap) entry.getValue();
					Set entries2 = ruleTypes.entrySet();
					Iterator it2 = entries2.iterator();
					while (it2.hasNext()) {
						Map.Entry entry2 = (Map.Entry) it2.next();

						if (entry2.getValue() instanceof HashMap) {
							Map ruleCats = (HashMap) entry2.getValue();
							Set entries3 = ruleCats.entrySet();
							Iterator it3 = entries3.iterator();
							while (it3.hasNext()) {
								Map.Entry entry3 = (Map.Entry) it3.next();

								if (entry3.getValue() instanceof Rule) {

									String poolingGroupId = "-1"; 
									Rule r3 = (Rule) entry3.getValue();

									ConditionInfo[] ci3 = r3.getConditions();
									boolean foundPoolingGroupId = false;
									for (int j = 0; j < ci3.length; j++) {

										if (ci3[j].getId() == 1 &&
												ci3[j].getFromAmount() == 0.0 && //not support from to values for now
												ci3[j].getToAmount() == 0.0) {

											poolingGroupId = ci3[j].getValue();
											foundPoolingGroupId = true;
											break;
										}
									}

									if (!foundPoolingGroupId) {
										continue; // if the rule is not pooling group related, ignore
									}

									/** start to re-organize **/
									rule = new Rule(r3.getDelegate());
									// check if the HashMap for this category exists
									rulesByType = (Map) rulesByCategoryAndTypeAndPoolGroupId.get(
											String.valueOf(rule.getCategory()));
									if (rulesByType != null) {
										rulesByPoolGroupId = (Map) rulesByType.get(String.valueOf(rule.getType()));
										if (rulesByPoolGroupId != null) {
											Rule[] rules = (Rule[]) rulesByPoolGroupId.get(poolingGroupId);
											rulesByPoolGroupId.put(poolingGroupId, addRuleInfoElement(rules, rule));
										}	
										else {
											// we need to create a new HashMap for this type
											rulesByPoolGroupId = new HashMap();
											rulesByPoolGroupId.put(poolingGroupId, addRuleInfoElement(null, rule));
											rulesByType.put(String.valueOf(rule.getType()), rulesByPoolGroupId);
										}
									}
									else {
										// we need to create new HashMaps for this category and type
										rulesByType = new HashMap();
										rulesByPoolGroupId = new HashMap();

										rulesByPoolGroupId.put(poolingGroupId, addRuleInfoElement(null, rule));
										rulesByType.put(String.valueOf(rule.getType()), rulesByPoolGroupId);
										rulesByCategoryAndTypeAndPoolGroupId.put(String.valueOf(rule.getCategory()),
												rulesByType);
									}
									ruleSz++;
									/* end to re-organize */
								}
							}
						}
					}
				}
			}
			Logger.debug("initRulesByPoolGroupId - Done");
			lastinitRulesByPoolGroupId = new Date();
		}
	}

	/**
	 * Returns the sorted (by ID) array of rules by category, type and pool group Id.
	 * 
	 * @param int category
	 * @param int type
	 * @param int poolGroupId
	 * @return Rule[], it does not return null, but an empty array if not found.
	 */
	private static Rule[] getRulesByCategoryAndTypeAndPoolGroupId(int category, int type, int poolGroupId) {

		Map rulesByCategory = (Map) rulesByCategoryAndTypeAndPoolGroupId.get(String.valueOf(category));

		if (rulesByCategory != null) {
			Map rulesByType = (Map) rulesByCategory.get(String.valueOf(type));

			if (rulesByType != null) {
				Rule[] rules = (Rule[]) rulesByType.get(String.valueOf(poolGroupId));
				if (rules != null) {
					Comparator comparator = new Comparators.RuleComparator(true);
					Arrays.sort(rules, comparator);
					return rules;
				}
			}
		}
		return  new Rule[0];
	}

	/**
	 * Add one Rule to the Rule Array
	 * 
	 * @param Rule array
	 * @param one element of Rule
	 * @return Rule[]
	 */	
	private static Rule[] addRuleInfoElement(Rule[] arr, Rule rule) {
		if (arr == null) {
			arr = new Rule[0];
		}

		int length = arr.length;
		Rule[] copy = new Rule[length + 1];
		System.arraycopy(arr, 0, copy, 0, length);
		copy[length] = rule;
		return copy;
	}
	

}

