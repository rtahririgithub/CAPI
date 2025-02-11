package com.telus.provider.rules;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.telus.api.TelusAPIException;
import com.telus.api.rules.RulesException;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.eas.utility.info.ConditionInfo;
import com.telus.eas.utility.info.RuleInfo;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.Logger;

/**
 * Core rules processor implementation.  The public RulesProcessor interface is actually
 * a rules orchestration wrapper indirectly exposing methods from this class.  The core rules framework
 * exists here.
 * 
 * @author R. Fong
 * @version 1.0, 09-Jul-2008 
 **/
public class TMRulesProcessor {
	

	private final ReferenceDataHelper referenceDataHelper;	
	protected static Map rulesByCategory = new Hashtable();
	protected static Map ruleTemplatesByCategory = new Hashtable();
	protected static Date lastRefreshTime;
	protected static Map assessors = new Hashtable();
	protected static final long MINUTE = 1000L * 60L;
	
	public TMRulesProcessor(ReferenceDataHelper referenceDataHelper) throws Throwable {
	    this.referenceDataHelper = referenceDataHelper;
		initialize();
		periodicRefresh();
	}

	
	/**
	 * Basic implementation of initialization method.  This method populates the processor's rules,
	 * templates and assessors.
	 * 
	 * @throws TelusAPIException
	 */
	protected void initialize() throws TelusAPIException {
		loadRules();
		loadRuleAssessors();
		lastRefreshTime = new Date();
	}
		
	/**
	 * Refreshes the rules, templates and assessors caches.
	 * 
	 * @throws TelusAPIException
	 */
	protected void refresh() throws TelusAPIException {
		initialize();
	}
	
	private synchronized void periodicRefresh() {

		String threadName = "RulesCachePeriodicRefreshThread";
			
		int refreshStartTime = AppConfiguration.getRuleRefreshStartTime();
		int refreshIntervalValue = AppConfiguration.getRuleRefreshIntervalValue();					

 		
		if (refreshIntervalValue != -1) {
			// setup the calendar to refresh at 5:00am
			final Calendar nextRefresh = Calendar.getInstance();
			nextRefresh.set(Calendar.HOUR_OF_DAY, refreshStartTime);
			nextRefresh.set(Calendar.MINUTE, 0);
			nextRefresh.set(Calendar.SECOND, 0);
			if (System.currentTimeMillis() > nextRefresh.getTimeInMillis()) {
				nextRefresh.add(Calendar.HOUR, refreshIntervalValue);
			}
			Logger.debug(threadName + ": current time is [" + new Date(System.currentTimeMillis()) + "].");
			Logger.debug(threadName + ": next cache refresh will be after [" + nextRefresh.getTime() + "].");

			// create the refresh thread
			Thread thread = new Thread(threadName) {
			
				public void run() {
				
					try {
						int sleepInterval = AppConfiguration.getRuleRefreshSleepIntervalValue();

						Logger.debug(getName() + ": current time is [" + new Date(System.currentTimeMillis()) + "].");
						Logger.debug(getName() + ": next cache refresh will be after [" + nextRefresh.getTime() + "].");
						Logger.debug(getName() + ": sleeping until [" + new Date(System.currentTimeMillis() + MINUTE * sleepInterval) + "].");
						Thread.sleep(MINUTE * sleepInterval);

						while (true) {
							Logger.debug(getName() + ": woke up!");
							if (System.currentTimeMillis() > nextRefresh.getTimeInMillis()) {
								// it's time to refresh the cache
								Logger.debug(getName() + ": refreshing rules, templates and assessors...");
								refresh();
								Logger.debug(getName() + ": refresh complete.");
								// set the next refresh time
								int refreshIntervalValue = AppConfiguration.getRuleRefreshIntervalValue();
								if (refreshIntervalValue != -1) 
									nextRefresh.add(Calendar.HOUR, refreshIntervalValue);
							}						
							Logger.debug(getName() + ": current time is [" + new Date(System.currentTimeMillis()) + "].");
							Logger.debug(getName() + ": next cache refresh will be after [" + nextRefresh.getTime() + "].");
							Logger.debug(getName() + ": sleeping until [" + new Date(System.currentTimeMillis() + MINUTE * sleepInterval) + "].");							
							Thread.sleep(MINUTE * sleepInterval);
						}
					
					} catch (Throwable e) {
						Logger.warning(getName() + ": error occurred during periodic refresh.");
						Logger.warning(getName() + ": current time is [" + new Date(System.currentTimeMillis()) + "].");
						Logger.warning(getName() + ": next cache refresh will be after [" + nextRefresh.getTime() + "].");
						Logger.warning(e);
					}
				}
			};
		
			// start the refresh thread
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	/**
	 * Loads rules and rule templates into their respective caches.
	 * 
	 * @throws TelusAPIException
	 */
	protected synchronized void loadRules() throws TelusAPIException {

		Map rulesByType = null;
		Map rules = null;
		Rule rule = null;
		RuleTemplate template = null;
		int templateSz = 0;
		int ruleSz = 0;
		
		try {
			// retrieve the rules from the data repository
			RuleInfo[] infos = referenceDataHelper.retrieveRules();
			if (infos != null && infos.length > 0) {

				// only if the info are good then we do refresh
				rulesByCategory.clear();
				ruleTemplatesByCategory.clear();

				for (int i = 0; i < infos.length; i++) {
					
					switch (infos[i].getRole()) {				
					case RuleConstants.ROLE_TYPE_RULE:
						// create a new rule instance and add it to the rules cache
						rule = new Rule(infos[i]);
						
						// check if the HashMap for this category exists
						rulesByType = (Map)rulesByCategory.get(String.valueOf(rule.getCategory()));						
						if (rulesByType != null) {
							rules = (Map)rulesByType.get(String.valueOf(rule.getType()));
							if (rules != null) {
								rules.put(rule.getCode(), rule);
							} else {
								// we need to create a new HashMap for this type
								rules = new HashMap();
								rules.put(rule.getCode(), rule);
								rulesByType.put(String.valueOf(rule.getType()), rules);
							}
						} else {
							// we need to create new HashMaps for this category and type
							rulesByType = new HashMap();
							rules = new HashMap();
							rules.put(rule.getCode(), rule);
							rulesByType.put(String.valueOf(rule.getType()), rules);
							rulesByCategory.put(String.valueOf(rule.getCategory()), rulesByType);
						}
						
						ruleSz++;
						break;

					case RuleConstants.ROLE_TYPE_TEMPLATE:
						// create a new rule template instance and add it to the templates cache
						template = new RuleTemplate(infos[i]);
						
						// check if the HashMap for this category exists
						rulesByType = (Map)ruleTemplatesByCategory.get(String.valueOf(template.getCategory()));
						if (rulesByType != null) {
							rulesByType.put(String.valueOf(template.getType()), template);
						} else {
							// we need to create a new HashMap for this type
							rulesByType = new HashMap();
							rulesByType.put(String.valueOf(template.getType()), template);
							ruleTemplatesByCategory.put(String.valueOf(template.getCategory()), rulesByType);
						}
						
						templateSz++;
						break;
					}
				}			
			}
			
			Logger.debug("Loaded " + templateSz + " rule templates.");
			Logger.debug("Loaded " + ruleSz + " rules.");
			
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}
	
	/**
	 * Loads assessor classes into the assessor cache.
	 * 
	 * @throws RulesException
	 * @throws TelusAPIException
	 */
	protected synchronized void loadRuleAssessors() throws RulesException, TelusAPIException {

		Map templatesByType = null;
		List templates = new ArrayList();
		RuleTemplate template = null;
		int assessorSz = 0;

		assessors.clear();
		
		// retrieve the templates by category and add them to the vector
		Iterator i = ruleTemplatesByCategory.values().iterator();
		while (i.hasNext()) {
			templatesByType = (Map)i.next();
			templates.addAll(templatesByType.values());
		}
			
		// iterate over all the templates and cache each assessor instance
		Iterator j = templates.iterator();
		while (j.hasNext()) {
			template = (RuleTemplate)j.next();
			ConditionInfo[] conditions = template.getConditions();
			for (int k = 0; k < conditions.length; k++) {
				if (getAssessor(conditions[k].getCode()) == null) {
					cacheAssessor(conditions[k].getCode(), conditions[k].getName());
					assessorSz++;
				}
			}
		}
		
		Logger.debug("Instantiated " + assessorSz + " assessors.");
	}
	
	/**
	 * Creates an instance of an assessor and puts it into the assessor cache.
	 * 
	 * @param String conditionId
	 * @param String conditionName
	 * @throws RulesException
	 */
	protected synchronized void cacheAssessor(String conditionId, String conditionName) throws RulesException, TelusAPIException {

		String className = conditionName + RuleConstants.ASSESSOR_CLASS_SUFFIX;
		try {
			Class assessorClass = Class.forName(RuleConstants.PACKAGE_NAME_ASSESSOR + className);
	        Constructor constructor = assessorClass.getConstructor(new Class[] {String.class});
	        ConditionAssessor assessor = (ConditionAssessor)constructor.newInstance(new Object[] {conditionId});
			assessors.put(conditionId, assessor);
			
		} catch(ClassNotFoundException cnfe) {
			throw new RulesException("Assessor class " + className + " instantiation failed.", RulesException.REASON_ASSESSOR_CLASS_INSTANTIATION_FAILURE, 
					cnfe);			
		} catch(Throwable t) {
			throw new TelusAPIException(t);
		}
	}
	
	/**
	 * Returns the sorted (by ID) array of rules by category.
	 * 
	 * @param int category
	 * @return Rule[]
	 */
	public Rule[] getRulesByCategory(int category) {
		
		List rules = new ArrayList();
		Map map = null;
		
		Map rulesByType = (Map)rulesByCategory.get(String.valueOf(category));
		Iterator i = rulesByType.values().iterator();
		while (i.hasNext()) {
			map = (Map)i.next();
			rules.addAll(map.values());
		}
		
		Comparator comparator = new Comparators.RuleComparator(true);
		Collections.sort(rules, comparator);
		
		return (Rule[])rules.toArray(new Rule[rules.size()]);
	}
	
	/**
	 * Returns the sorted (by ID) array of rules by category and type.
	 * 
	 * @param int category
	 * @param int type
	 * @return Rule[]
	 */
	public Rule[] getRulesByCategoryAndType(int category, int type) {

		Map rulesByType = (Map)rulesByCategory.get(String.valueOf(category));
		Map rulesMap = (Map)rulesByType.get(String.valueOf(type));
		Rule[] rules = (Rule[])rulesMap.values().toArray(new Rule[rulesMap.size()]);
		
		Comparator comparator = new Comparators.RuleComparator(true);
		Arrays.sort(rules, comparator);

		return rules;
	}
	
	/**
	 * Returns the sorted (by ID) array of rule templates by category.
	 * 
	 * @param int category
	 * @return RuleTemplate[]
	 */
	public RuleTemplate[] getRuleTemplatesByCategory(int category) {

		Map templateMap = (Map)ruleTemplatesByCategory.get(String.valueOf(category));		
		RuleTemplate[] templates = (RuleTemplate[])templateMap.values().toArray(new RuleTemplate[templateMap.size()]);
		
		Comparator comparator = new Comparators.RuleComparator(true);
		Arrays.sort(templates, comparator);

		return templates;
	}

	/**
	 * Evaluate the conditions for the provided rule template using the provided working memory context. 
	 * Conditions will be evaluated in order of condition ID.
	 * 
	 * @param WorkingMemory workingMemory
	 * @return RuleTemplate
	 * @throws RulesException
	 * @throws TelusAPIException
	 */
	public RuleTemplate getTestRule(WorkingMemory workingMemory) throws RulesException, TelusAPIException {
		
		// get the template from working memory
		WorkingMemoryElement templateElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_RULE_TEMPLATE);
		RuleTemplate template = (RuleTemplate)templateElement.getElement();
		
		// if the template is null, this can't be a valid rule evaluation
		if (template == null)
			throw new RulesException("Invalid rule - template not found.", RulesException.REASON_INVALID_RULE);
		
		// create a test rule from the template and setup the contexts
		RuleTemplate testRule = template.getTestRule();
		ConditionInfo[] testConditions = testRule.getConditions();
		
		// sort the conditions based on ID
		Comparator comparator = new Comparators.ConditionComparator(true);
		Arrays.sort(testConditions, comparator);

		// iterate through the test conditions and assess each condition
		for (int i = 0; i < testConditions.length; i++) {
			ConditionAssessor assessor = getAssessor(testConditions[i].getCode());
			ConditionResult conditionResult = assessor.evaluate(workingMemory);
			
			// set the results of the assessment into the test condition
			switch (conditionResult.getConditionType()) {
			case RuleConstants.CONDITION_TYPE_BOOLEAN:
				testConditions[i].setValue(conditionResult.getTextResult());
				break;
				
			case RuleConstants.CONDITION_TYPE_NUMBER_RANGE:
				testConditions[i].setFromAmount(conditionResult.getAmountResult());
				break;
				
			case RuleConstants.CONDITION_TYPE_DATE_RANGE:
				testConditions[i].setFromDate(conditionResult.getDateResult());
				break;
				
			case RuleConstants.CONDITION_TYPE_TEXT:
				testConditions[i].setValue(conditionResult.getTextResult());
				break;
			
			default:
				// if we're here, the condition type is not applicable
				// don't set any values, just use the defaults
				break;
			}
		}
		// set the evaluated conditions into the test rule
		testRule.setConditions(testConditions);
		
		return testRule;
	}
	
	/**
	 * Processes the provided rule using the provided working memory. 
	 * 
	 * @param Rule rule
	 * @param WorkingMemory workingMemory
	 * @return boolean
	 * @throws RulesException
	 * @throws TelusAPIException
	 */
	public boolean processRule(Rule rule, WorkingMemory workingMemory) throws RulesException, TelusAPIException {
		// evaluate the rule against the test rule and if it matches, return true, otherwise false
		return rule.evaluate(getTestRule(workingMemory).getDelegate());
	}
	
	public Rule getRule(String category, String type, String id) {
		Map rulesByType = (Map)rulesByCategory.get(category);
		Map rules = (Map)rulesByType.get(type); 
		return (Rule)rules.get(id);
	}
	
	public RuleTemplate getRuleTemplate(String category, String type) {
		Map templates = (Map)ruleTemplatesByCategory.get(category); 
		return (RuleTemplate)templates.get(type);
	}
	
	public ConditionAssessor getAssessor(String id) {		
		return (ConditionAssessor)assessors.get(id);
	}

	/**
	 * Return the static Map rulesCategory 
	 * 
	 * @param None
	 * @return Map rulesByCategory
	 * @throws No exceptions thrown
	 */
	public static Map getRulesByCategory() {
		return rulesByCategory;
	}

	/**
	 * Return the static lastRefreshTime 
	 * 
	 * @param None
	 * @return Date lastRefreshTime
	 * @throws No exceptions thrown
	 */
	public static Date getLastRefreshTime() {
		return lastRefreshTime;
	}
}