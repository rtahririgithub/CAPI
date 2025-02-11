package com.telus.cmb.account.kafka.json.mapper.v1;

import java.util.Date;
import com.telus.api.util.DateUtil;
import com.telus.cmb.common.kafka.account_v1_0.Account;
import com.telus.cmb.common.kafka.account_v1_0.BillDetail;
import com.telus.eas.account.info.AccountInfo;

public class AccountMapper {
	public static Account mapAccountData(AccountInfo source) {
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
		return account;
	}

	public static BillDetail mapBillDetail(int billCycleCode,int billCycleCloseDay){
		BillDetail billDetail = new BillDetail();
		billDetail.setBillCycleCode(billCycleCode);
		billDetail.setBillCycleCloseDate(DateUtil.calculateBillCycleCloseDate(new Date(), billCycleCloseDay));
		return billDetail;	
	}

}
