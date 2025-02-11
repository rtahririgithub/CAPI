package com.telus.provider.rules;

import java.util.Comparator;

import com.telus.eas.utility.info.ConditionInfo;

public interface Comparators {

	/**
	 * This class implements the Comparator interface for comparing two rules based on their
	 * respective IDs.
	 */
	public class RuleComparator implements Comparator {

		private boolean isAsc = true;

		public RuleComparator(boolean isAsc) {
			this.isAsc = isAsc;
		}

		public int compare(Object o1, Object o2) {
			
			RuleTemplate rule1 = (RuleTemplate)o1;
			RuleTemplate rule2 = (RuleTemplate)o2;
			
			Long long1 = new Long(rule1.getId());
			Long long2 = new Long(rule2.getId());
			
			if (isAsc)
				return long1.compareTo(long2);

			return long2.compareTo(long1);
		}
	}
	
	/**
	 * This class implements the Comparator interface for comparing two conditions based on their
	 * respective IDs.
	 */
	public class ConditionComparator implements Comparator {

		private boolean isAsc = true;

		public ConditionComparator(boolean isAsc) {
			this.isAsc = isAsc;
		}

		public int compare(Object o1, Object o2) {
			
			ConditionInfo condition1 = (ConditionInfo)o1;
			ConditionInfo condition2 = (ConditionInfo)o2;
			
			Long long1 = new Long(condition1.getId());
			Long long2 = new Long(condition2.getId());
			
			if (isAsc)
				return long1.compareTo(long2);

			return long2.compareTo(long1);
		}
	}
}
