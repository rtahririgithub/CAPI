package com.telus.cmb.account.kafka.json.mapper.v1;

import com.telus.cmb.common.kafka.account_v1_0.BankAccount;
import com.telus.cmb.common.kafka.account_v1_0.CreditCard;
import com.telus.cmb.common.kafka.account_v1_0.PaymentDetail;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.PaymentInfo;

public class PaymentDetailMapper extends AbstractSchemaMapper<PaymentDetail, PaymentInfo> {

	private static PaymentDetailMapper INSTANCE = null;

	public PaymentDetailMapper() {
		super(PaymentDetail.class, PaymentInfo.class);
	}

	public static synchronized PaymentDetailMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PaymentDetailMapper();
		}

		return INSTANCE;
	}

	@Override
	protected PaymentDetail performSchemaMapping(PaymentInfo source,PaymentDetail target) {

		target.setPaymentAmt(source.getAmount());

		if (source.getCreditCardInfo().getAuthorizationCode() != null) {
			target.setCcAuthCd(source.getCreditCardInfo().getAuthorizationCode());
			CreditCard creditCard = new CreditCard();
			creditCard.setLeadingDisplayDigits(source.getCreditCardInfo().getLeadingDisplayDigits());
			creditCard.setTrailingDisplayDigits(source.getCreditCardInfo().getTrailingDisplayDigits());
			target.setCreditCard(creditCard);
		} else if (source.getChequeInfo().getBankAccount0().getBankAccountNumber() != null) {
			BankAccount bankAccount = new BankAccount();
			bankAccount.setBankAccountNumber(source.getChequeInfo().getBankAccount0().getBankAccountNumber());
			target.setBankAccount(bankAccount);
		}
		
		return super.performSchemaMapping(source, target);
	}

}
