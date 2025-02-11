package com.telus.cmb.common.kafka;

public interface TransactionEventMapper {
	public Object mapToSchema (TransactionEventInfo transactionEventInfo);
}
