package com.telus.provider.rules.message;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.rules.Context;
import com.telus.api.rules.RulesException;
import com.telus.api.rules.message.MessageContext;
import com.telus.api.rules.message.MessageSubscriberContext;
import com.telus.provider.util.AppConfiguration;

/**
 * @author R. Fong
 * @version 1.0, 09-Jul-2008 
 **/
public class TMMessageContext implements MessageContext {

	private int category;
	private int transactionType;
	private int banSizeThreshold = MessageContext.USE_MAX_BAN_SIZE_THRESHOLD;
	private int maxBanSizeThreshold = 200;
	private boolean includeReservedSubscribers = false;
	private Account sourceAccount;
	private Account targetAccount;
	private MessageSubscriberContext[] subscriberContexts;

	public TMMessageContext(int category, int transactionType, Account sourceAccount, Account targetAccount, 
			MessageSubscriberContext[] subscriberContexts, boolean includeReservedSubscribers, int banSizeThreshold) {
		this.category = category;
		this.transactionType = transactionType;
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
		this.subscriberContexts = subscriberContexts;
		this.includeReservedSubscribers = includeReservedSubscribers;
		this.maxBanSizeThreshold = AppConfiguration.getMaxBanSizeThreshold();

		if (banSizeThreshold < maxBanSizeThreshold) {
			this.banSizeThreshold = banSizeThreshold;
		}
	}

	public static final TMMessageContext newMessageContext(int category, int transactionType, Account sourceAccount, 
			Account targetAccount, MessageSubscriberContext[] subscriberContexts, boolean includeReservedSubscribers,
			int banSizeThreshold) 
	throws RulesException, TelusAPIException {

		if (sourceAccount == null)
			throw new RulesException("Invalid parameter - source account object is null.  Source account is mandatory for all transactions.", RulesException.REASON_INVALID_ACCOUNT);
		if (targetAccount == null && (transactionType == Context.TRANSACTION_TYPE_MOVE || 
				transactionType == Context.TRANSACTION_TYPE_MIGRATE || 
				transactionType == Context.TRANSACTION_TYPE_COMPLETE_TOWN))
			throw new RulesException("Invalid parameter - target account object is null.  Target account cannot be null for move, complete TOWN and migrate transactions.", RulesException.REASON_INVALID_ACCOUNT);
		if (targetAccount != null && (transactionType == Context.TRANSACTION_TYPE_ACTIVATION || 
				transactionType == Context.TRANSACTION_TYPE_SERVICE_CHANGE || transactionType == Context.TRANSACTION_TYPE_INITIATE_TOWN ||
				transactionType == Context.TRANSACTION_TYPE_RESTORE_RESUME || transactionType == Context.TRANSACTION_TYPE_SUSPEND_CANCEL))
			throw new RulesException("Invalid parameter - target account object is not null.  Target account is an invalid parameter for activation, service change, initiate TOWN, suspend / cancel and resume transactions.", RulesException.REASON_INVALID_ACCOUNT);
		if (subscriberContexts == null || subscriberContexts.length == 0)
			throw new RulesException("Invalid parameter - subscriber context array is null or empty.", RulesException.REASON_INVALID_SUBSCRIBER_CONTEXT);

		return new TMMessageContext(category, transactionType, sourceAccount, targetAccount, subscriberContexts, 
				includeReservedSubscribers, banSizeThreshold);
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public int getBanSizeThreshold() {
		return (banSizeThreshold != MessageContext.USE_MAX_BAN_SIZE_THRESHOLD && 
				banSizeThreshold <= maxBanSizeThreshold) ? banSizeThreshold : maxBanSizeThreshold;
	}

	public void setBanSizeThreshold(int banSizeThreshold) {
		this.banSizeThreshold = banSizeThreshold;
	}

	public boolean includeReservedSubscribers() {
		return includeReservedSubscribers;
	}

	public void setIncludeReservedSubscribers(boolean includeReservedSubscribers) {
		this.includeReservedSubscribers = includeReservedSubscribers;
	}

	public Account getSourceAccount() {
		return sourceAccount;
	}

	public Account getTargetAccount() {
		return targetAccount;
	}

	public MessageSubscriberContext[] getSubscriberContexts() {
		return subscriberContexts;
	}

	public int getMaxBanSizeThreshold() {
		return maxBanSizeThreshold;
	}
}
