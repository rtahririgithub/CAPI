/*
 *  Copyright (c) 2011 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.eas.framework.eligibility.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.EvaluationResult;

public class EvaluationRule {
	
	private String name = null;

	private List rules = new ArrayList();
	
	private EvaluationRule parent = null;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parent
	 */
	public EvaluationRule getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(EvaluationRule parent) {
		this.parent = parent;
	}

	public EvaluationResult evaluate(EligibilityCheckCriteria criteria) {

		EvaluationResult result = null;
		
		Iterator iter = rules.iterator();
		while (iter.hasNext()) {
			EvaluationRule rule = (EvaluationRule) iter.next();
			result = rule.evaluate(criteria);
			if (result != null) {
				break;
			}
		}
		
		return result;
	}
	
	public void addEvaluationRule(EvaluationRule rule) {
		rule.setParent(this);
		rules.add(rule);
	}
}
