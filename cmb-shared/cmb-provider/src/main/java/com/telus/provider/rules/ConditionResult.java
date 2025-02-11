package com.telus.provider.rules;

import java.util.Date;

public class ConditionResult {

	private int conditionType;
	private String textResult;
	private Date dateResult;
	private double amountResult;
	
	public final static ConditionResult DEFAULT = new ConditionResult(RuleConstants.CONDITION_TYPE_NOT_APPLICABLE, "Default result.");
	
	public ConditionResult(int conditionType, String result) {
		this.conditionType = conditionType;
		this.textResult = result;
	}
	
	public ConditionResult(int conditionType, Date result) {
		this.conditionType = conditionType;
		this.dateResult = result;
	}
	
	public ConditionResult(int conditionType, double result) {
		this.conditionType = conditionType;
		this.amountResult = result;
	}

	public double getAmountResult() {
		return amountResult;
	}

	public int getConditionType() {
		return conditionType;
	}

	public Date getDateResult() {
		return dateResult;
	}

	public String getTextResult() {
		return textResult;
	}

}
