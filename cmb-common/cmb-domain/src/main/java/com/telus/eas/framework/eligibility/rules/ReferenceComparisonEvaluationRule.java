package com.telus.eas.framework.eligibility.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;

public abstract class ReferenceComparisonEvaluationRule extends ConditionalEvaluationRule {

	public static final String OP_EQUAL = "equal";
	public static final String OP_LESS = "less";
	public static final String OP_GREATER = "greater";
	public static final String OP_GREATER_EQUAL = "greater-equal";
	public static final String OP_LESS_EQUAL = "less-equal";

	private Object referenceValue = null;
	
	private String operation = null;
	
	public ReferenceComparisonEvaluationRule() {
	}
	
	public ReferenceComparisonEvaluationRule(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the referenceValue
	 */
	public Object getReferenceValue() {
		return referenceValue;
	}

	/**
	 * @param referenceValue the referenceValue to set
	 */
	public void setReferenceValue(Object referenceValue) {
		this.referenceValue = referenceValue;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	protected abstract Object getEvaluationValue(EligibilityCheckCriteria criteria);

	/* (non-Javadoc)
	 * @see com.telus.provider.eligibility.interservice.impl.rules.ConditionalEvaluationRule#matchCondition(com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria)
	 */
	protected boolean matchCondition(EligibilityCheckCriteria criteria) {
		
		Object o1 = getEvaluationValue(criteria);
		Object o2 = getReferenceValue();
		
		if (o1 != null && o2 != null) {
			
			if (operation.equals(OP_EQUAL)) {
				return o1.equals(o2);
			} else {
				
				if (o1 instanceof Comparable && o2 instanceof Comparable) {
					
					Comparable c1 = (Comparable) o1;
					Comparable c2 = (Comparable) o2;
					
					int result = c1.compareTo(c2);
					
					if (operation.equals(OP_LESS)) {
						return result < 0;
					} else if (operation.equals(OP_GREATER)) {
						return result > 0;
					} else if (operation.equals(OP_LESS_EQUAL)) {
						return result <= 0;
					} else if (operation.equals(OP_GREATER_EQUAL)) {
						return result >= 0;
					} else {
						throw new IllegalArgumentException("Illegal comparison operation [" + operation + "]");
					}

				} else {
					throw new IllegalArgumentException("Objects must be instances of Comparable interface");
				}
			}
		}
		
		return o1 == null || o2 == null ? false : o1.equals(o2);
	}
	
	public String getOperation() {
		return operation;
	}

}
