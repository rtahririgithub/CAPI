package com.telus.cmb.account.kafka.json.mapper.v1;

import com.telus.cmb.common.kafka.account_v1_0.FollowUpDetail;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.FollowUpUpdateInfo;

public class FollowUpDetailMapper extends AbstractSchemaMapper<FollowUpDetail, FollowUpUpdateInfo> {

	private static FollowUpDetailMapper INSTANCE = null;

	public FollowUpDetailMapper() {
		super(FollowUpDetail.class, FollowUpUpdateInfo.class);
	}

	public static synchronized FollowUpDetailMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FollowUpDetailMapper();
		}

		return INSTANCE;
	}

	@Override
	protected FollowUpDetail performSchemaMapping(FollowUpUpdateInfo source,FollowUpDetail target) {
		target.setFollowUpId(source.getFollowUpId());
		target.setFollowUpType(source.getFollowUpType());

		if (source.isFollowUpApprovalForChargeAdj()) {
			target.setFollowUpReason(FollowUpUpdateInfo.FOLLOW_REASON_ADJUST_BAN.substring(5));
		} else if (source.isFollowUpApprovalForManualCredit()) {
			target.setFollowUpReason(FollowUpUpdateInfo.FOLLOW_REASON_ADJUST_CHARGE.substring(5));
		}

		return super.performSchemaMapping(source, target);
	}

}
