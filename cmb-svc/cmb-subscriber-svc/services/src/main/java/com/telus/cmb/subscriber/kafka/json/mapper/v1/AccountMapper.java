package com.telus.cmb.subscriber.kafka.json.mapper.v1;

import java.util.Calendar;
import java.util.Date;
import com.telus.cmb.common.kafka.subscriber_v1.Account;
import com.telus.eas.account.info.AccountInfo;

public class AccountMapper {
	public static Account mapAccountData(AccountInfo source) {
		Account account = new Account();
		account.setBanId(source.getBanId());
		account.setBrandId(source.getBrandId());
		account.setAccountType(String.valueOf(source.getAccountType()));
		account.setAccountSubType(String.valueOf(source.getAccountSubType()));
		account.setEmail(source.getEmail());
		account.setLanguage(source.getLanguage());
		account.setBanSegment(source.getBanSegment());
		account.setBanSubSegment(source.getBanSubSegment());
		account.setBillCycleCode(source.getBillCycle());
		account.setBillCycleStartDate(getBillCycleCloseDate(source.getBillCycleCloseDay(), new Date()));
		return account;
	}

	private static Date getBillCycleCloseDate(int billCycleCloseDay, Date today) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MONTH, 1);

		if (billCycleCloseDay > cal.getActualMaximum(Calendar.DATE)) {
			billCycleCloseDay = cal.getActualMaximum(Calendar.DATE);
		}
		cal.set(Calendar.DAY_OF_MONTH, billCycleCloseDay);
		return cal.getTime();
	}

}
