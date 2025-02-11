package com.telus.api.account;

/**
 * @author Roman Tov
 * @version 1.0, 20-Jul-2006
 *
 * Type-safe enum for Activation Option Types.
 *
 */

public interface ActivationOptionType {
	
	String getName();

	public static final String DEPOSIT = "Deposit";
	public static final String CREDIT_LIMIT = "CreditLimit";
	public static final String CREDIT_LIMIT_AND_DEPOSIT = "CreditLimitAndDeposit";
	public static final String DIFFERENTIATED_CREDIT = "DifferentiatedCredit";
	public static final String NDP = "NDP";
	public static final String DECLINED = "DCL";
	
	/**
	 * Use by Dealer Activation Only to Increase the Deposit amount due to added World Phone Deposit.
	 * Use in the special case when Dealer kept the deposits.
	 */
	public static final String DEALER_DEPOSIT_CHANGE = "DealerDepositChange";

}