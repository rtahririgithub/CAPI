package com.telus.cmb.jws.mapping;

import com.telus.eas.utility.info.AdjustmentReasonInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.AdjustmentReason;

public class AdjustmentReasonMapper extends BillAdjustmentMapper<AdjustmentReason, AdjustmentReasonInfo> {

	public AdjustmentReasonMapper() {
		super(AdjustmentReason.class, AdjustmentReasonInfo.class);
	}
	
	@Override
	protected AdjustmentReason performSchemaMapping(AdjustmentReasonInfo source, AdjustmentReason target) {

		target.setAdjustmentActivityCode(source.getAdjustmentActivityCode());
		target.setAdjustmentCategory(source.getAdjustmentCategory());
		target.setAdjustmentLevelCode(source.getAdjustmentLevelCode());
		target.setAdjustmentTaxIndicator(source.getAdjustmentTaxIndicator());
		target.setExpiryDate(source.getExpiryDate());
		target.setFrequency(source.getFrequency());
		target.setMaxNumberOfRecurringCredits(source.getMaxNumberOfRecurringCredits());
		target.setRecurringInd(source.isRecurring());

		return super.performSchemaMapping(source, target);
	}
}
