package com.telus.cmb.account.kafka.json.mapper.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.telus.cmb.common.kafka.account_v1_0.BankAccount;
import com.telus.cmb.common.kafka.account_v1_0.CreditCard;
import com.telus.cmb.common.kafka.account_v1_0.PaymentMethod;
import com.telus.cmb.common.kafka.account_v1_0.PaymentType;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.cmb.common.kafka.TransactionType;

public class PaymentMethodMapper {

	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");

	public static PaymentMethod mapPaymentMethod(PaymentMethodInfo paymentMethodInfo){
		PaymentMethod paymentMethod = new PaymentMethod();
		
		// set transaction type
		paymentMethod.setTransactionType(getPaymentMethodTransactionType(paymentMethodInfo.getTransactionType()));
	
		// set new and old payment types
	
		paymentMethod.setNewPaymentType(mapNewPaymentType(paymentMethodInfo));
		paymentMethod.setOldPaymentType(mapOldPaymentType(paymentMethodInfo));
		

		return paymentMethod;
	}

	
	private static PaymentType mapNewPaymentType(PaymentMethodInfo paymentMethodInfo) {

		PaymentType newPaymentType = new PaymentType();

		// map newPaymentType & old payment types
		if (com.telus.eas.account.info.PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT.equals(paymentMethodInfo.getPaymentMethod())) {
			// R -> BD (new) , CC -> BD (Update) , BD -> BD ( Update)
			BankAccount bankAccount = new BankAccount();
			bankAccount.setBankAccountNumber(paymentMethodInfo.getCheque0().getBankAccount0().getBankAccountNumber());
			newPaymentType.setBankAccount(bankAccount);
		} else if (com.telus.eas.account.info.PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD.equals(paymentMethodInfo.getPaymentMethod())) {
			// R -> CC ( New) , BD -> CC (Update) ,CC-> CC (Update)
			CreditCard creditCard = new CreditCard();
			creditCard.setTrailingDisplayDigits(paymentMethodInfo.getCreditCard().getTrailingDisplayDigits());
			creditCard.setLeadingDisplayDigits(paymentMethodInfo.getCreditCard().getLeadingDisplayDigits());
			newPaymentType.setCreditCard(creditCard);
		} else {
			// CC -> R (Remove), BD -> R ( Remove) newPaymentType is null here which is PAYMENT_METHOD_REGULAR , old payment should be either credit or debit
			return null;
		}
		
		return newPaymentType;
	}
	
	private static PaymentType mapOldPaymentType(PaymentMethodInfo paymentMethodInfo) {

		PaymentType oldPaymentType = new PaymentType();

		if (com.telus.eas.account.info.PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT.equals(paymentMethodInfo.getOldPaymentMethod())) {
			BankAccount bankAccount = new BankAccount();
			bankAccount.setBankAccountNumber(paymentMethodInfo.getOldPaymentMethodCardNumber());
			oldPaymentType.setBankAccount(bankAccount);
		} else if (com.telus.eas.account.info.PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD.equals(paymentMethodInfo.getOldPaymentMethod())) {
			CreditCard creditCard = new CreditCard();
			creditCard.setTrailingDisplayDigits(paymentMethodInfo.getOldPaymentMethodCardNumber());
			oldPaymentType.setCreditCard(creditCard);
		} else {
			// Do nothing as old payment method is "REGULAR"
			return null;
		}
		return oldPaymentType;
	}
	
	private static String getPaymentMethodTransactionType(char transactionType) {
		switch (transactionType) {
		case PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_NEW:
			return TransactionType.ADD.getValue();

		case PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_UPDATE:
			return TransactionType.MODIFY.getValue();

		case PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_REMOVAL:
			return TransactionType.REMOVE.getValue();

		default:
			logger.info("unknown payment method transaction type ["+ transactionType+"]");
			return TransactionType.NO_CHG.getValue();
		}
	}
}
