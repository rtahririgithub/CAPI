package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import com.telus.cmb.common.kafka.subscriber_v2.Commitment;
import com.telus.eas.subscriber.info.CommitmentInfo;

public class CommitmentMapper {

	public static Commitment mapCommitment(CommitmentInfo commitmentInfo) {
	
		if (commitmentInfo == null) {
			return null;
		}
		
		Commitment commitment = new Commitment();
		commitment.setMonths(commitmentInfo.getMonths());
		commitment.setEffectiveDate(commitmentInfo.getStartDate());
		commitment.setExpiryDate(commitmentInfo.getEndDate());
		commitment.setReasonCode(commitmentInfo.getReasonCode());
		return commitment;
	}
}
