/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.billing_enquiry_1_1;

import com.telus.cmb.jws.mapping.reference.billing_enquiry_1_1.BillAdjustmentMapper;
import com.telus.eas.utility.info.AdjustmentReasonInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_reference_types_1_1.AdjustmentReason;

/**
 * @author Gregory Bragg
 *
 */
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
