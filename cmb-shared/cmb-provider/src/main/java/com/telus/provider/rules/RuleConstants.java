package com.telus.provider.rules;

public class RuleConstants {
	
	// rule type constants
	public static final int RULE_TYPE_MP_ADD_SERVICE = 1;
	public static final int RULE_TYPE_MP_REMOVE_SERVICE = 2;
	public static final int RULE_TYPE_MP_CONFLICTING_SERVICE = 3;
	public static final int RULE_TYPE_MP_BILL_CYCLE_CHANGE = 4;
	public static final int RULE_TYPE_MAP_SERVICE_CHANGE = 5;
	public static final int RULE_TYPE_MP_POOLING_CONTRIBUTOR_SERVICE_CHANGE = 6;
	
	// rule role type constants
	public static final int ROLE_TYPE_TEST_RULE = -1;
	public static final int ROLE_TYPE_TEMPLATE = 1;
	public static final int ROLE_TYPE_RULE = 2;
	
	// condition type constants
	public static final int CONDITION_TYPE_NOT_APPLICABLE = -1;
	public static final int CONDITION_TYPE_TEXT = 1;
	public static final int CONDITION_TYPE_BOOLEAN = 2;
	public static final int CONDITION_TYPE_DATE_RANGE = 3;
	public static final int CONDITION_TYPE_NUMBER_RANGE = 4;	
	
	// condition type boolean value constants
	public static final String CONDITION_VALUE_BOOLEAN_TRUE = Boolean.toString(true);
	public static final String CONDITION_VALUE_BOOLEAN_FALSE = Boolean.toString(false);
	
	// working memory attribute type constants
	public static final String ATTRIBUTE_ACCOUNT = "account";
	public static final String ATTRIBUTE_SUBSCRIBER = "subscriber";
	public static final String ATTRIBUTE_CONTRACT = "contract";
	public static final String ATTRIBUTE_OLD_CONTRACT = "oldContract";
	public static final String ATTRIBUTE_BAN_SIZE_THRESHOLD = "banSizeThreshold";
	public static final String ATTRIBUTE_TRANSACTION_TYPE = "transactionType";
	public static final String ATTRIBUTE_POOLING_GROUP_ID = "poolingGroupId";
	public static final String ATTRIBUTE_RULE_TEMPLATE = "ruleTemplate";
	public static final String ATTRIBUTE_RULE = "rule";
	public static final String ATTRIBUTE_INCLUDE_RESERVED_SUBSCRIBERS = "includeReservedSubscribers";
	public static final String ATTRIBUTE_SERVICE_TRANSITION = "serviceTransition";
	public static final String ATTRIBUTE_CATEGORY_CODE = "categoryCode";
	public static final String ATTRIBUTE_MINUTE_POOLING_CONTRIBUTOR_SERVICES = "minutePoolingContributorServices";
	public static final String ATTRIBUTE_SERVICE = "service";
	public static final String ATTRIBUTE_SUBCONTEXT_POOLING_SUBS = "subContextIsPoolingSubs";
	public static final String ATTRIBUTE_SUBCONTEXT_SHAREABLE_OR_DOLLAR_POOLING_SUBS = "subContextShareableOrDollarPoolingSubs";
	public static final String ATTRIBUTE_POOLING_GROUPS = "poolingGroups";
	
	// assessor constants
	public static final String PACKAGE_NAME_ASSESSOR = "com.telus.provider.rules.assessors.";
	public static final String ASSESSOR_CLASS_SUFFIX = "Assessor";
	
	// pooling transition constants
	public static final String TRANSITION_NONPOOLING_TO_POOLING = "N2P";
	public static final String TRANSITION_POOLING_TO_POOLING = "P2P";
	public static final String TRANSITION_POOLING_TO_NONPOOLING = "P2N";
	public static final String TRANSITION_NONPOOLING_TO_NONPOOLING = "N2N";

	// service change transition constants
	public static final String TRANSITION_SERVICE_ADD = "ADD";
	public static final String TRANSITION_SERVICE_REMOVE = "REMOVE";
	public static final String TRANSITION_SERVICE_NO_CHANGE = "NO_CHANGE";
	
	// activity reason codes
	public static final String ACTIVITY_REASON_SUSPEND_LOST = "LST";
	public static final String ACTIVITY_REASON_SUSPEND_STOLEN = "STL";
	public static final String ACTIVITY_REASON_SUSPEND_VACATION = "VAD";
}
