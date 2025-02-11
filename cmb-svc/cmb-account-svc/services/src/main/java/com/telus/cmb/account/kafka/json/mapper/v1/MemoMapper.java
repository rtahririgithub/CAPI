package com.telus.cmb.account.kafka.json.mapper.v1;

import org.apache.commons.lang.StringUtils;
import com.telus.cmb.common.kafka.TransactionEventInfo;
import com.telus.cmb.common.kafka.account_v1_0.Memo;

public class MemoMapper {
	
	public static Memo mapMemo(TransactionEventInfo eventInfo) {
		if (StringUtils.isNotEmpty(eventInfo.getUserMemoText())) {
			Memo memo = new Memo();
			memo.setMemoTxt(eventInfo.getUserMemoText());
			return memo;
		} else {
			return null;
		}
	}
	
}
