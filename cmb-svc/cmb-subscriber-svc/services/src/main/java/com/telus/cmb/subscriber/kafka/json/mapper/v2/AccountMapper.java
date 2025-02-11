package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import java.util.Date;

import com.telus.api.util.DateUtil;
import com.telus.cmb.common.kafka.subscriber_v2.BillDetail;
import com.telus.cmb.common.kafka.subscriber_v2.Account;
import com.telus.eas.account.info.AccountInfo;

public class AccountMapper {
	public static Account mapAccount(AccountInfo source) {
		Account account = new Account();
		account.setBan(source.getBanId());
		account.setBrandId(source.getBrandId());
		account.setAccountType(String.valueOf(source.getAccountType()));
		account.setAccountSubType(String.valueOf(source.getAccountSubType()));
		account.setStatus(String.valueOf(source.getStatus()));
		account.setSegmentCode(source.getBanSegment());
		account.setSubSegmentCode(source.getBanSubSegment());
		account.setEmail(source.getEmail());
		account.setLanguage(source.getLanguage());
		account.setBillDetail(mapBillDetail(source.getBillCycle(),source.getBillCycleCloseDay()));
		return account;
	}

	private static BillDetail mapBillDetail(int billCycleCode,int billCycleCloseDay){
		BillDetail billDetail = new BillDetail();
		billDetail.setBillCycleCode(billCycleCode);
		billDetail.setBillCycleCloseDate(DateUtil.calculateBillCycleCloseDate(new Date(), billCycleCloseDay));
		return billDetail;	
	}

}
