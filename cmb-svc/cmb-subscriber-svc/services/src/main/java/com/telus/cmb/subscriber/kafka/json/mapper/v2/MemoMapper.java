package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import org.apache.commons.lang.StringUtils;
import com.telus.cmb.common.kafka.subscriber_v2.Memo;
import com.telus.cmb.common.kafka.TransactionEventInfo;

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
