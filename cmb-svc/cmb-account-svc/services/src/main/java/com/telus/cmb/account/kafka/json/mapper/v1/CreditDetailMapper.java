package com.telus.cmb.account.kafka.json.mapper.v1;

import com.telus.api.account.Credit;
import com.telus.cmb.common.kafka.account_v1_0.CreditDetail;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.framework.info.CreditInfo;

public class CreditDetailMapper extends AbstractSchemaMapper<CreditDetail, CreditInfo> {
	
	private static CreditDetailMapper INSTANCE = null;

	public CreditDetailMapper() {
		super(CreditDetail.class, CreditInfo.class);
	}

	public static synchronized CreditDetailMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CreditDetailMapper();
		}
		
		return INSTANCE;
	}
	
	@Override
	protected CreditDetail performSchemaMapping(CreditInfo source,CreditDetail target) {
		target.setBalanceImpactInd(source.isBalanceImpactFlag());
		target.setBypassAuthorizationInd(source.isBypassAuthorization());
		target.setCreditAmt(source.getAmount());
		target.setEffectiveDt(source.getEffectiveDate());
		target.setReasonCd(source.getReasonCode());
		target.setRecurringInd(source.isRecurring());
		target.setRecurringNum(source.getNumberOfRecurring());
		target.setTaxAmt(source.getGSTAmount());
	

		double taxAmount = 0.0;

		if (source.getTaxOption() != com.telus.eas.framework.info.CreditInfo.TAX_OPTION_NO_TAX) {
			taxAmount = source.getGSTAmount();
			if ((source.getPSTAmount() != 0) && (source.getTaxOption() != com.telus.eas.framework.info.CreditInfo.TAX_OPTION_GST_ONLY)) {
				taxAmount += source.getPSTAmount();
			}
			if ((source.getHSTAmount() != 0) && (source.getTaxOption() != com.telus.eas.framework.info.CreditInfo.TAX_OPTION_GST_ONLY)) {
				taxAmount += source.getHSTAmount();
			}
		}

		if (taxAmount > 0) {
			target.setTaxAmt(taxAmount);
		}

		target.setTaxOptionCd(translateTaxOption(source.getTaxOption()));
				
		return super.performSchemaMapping(source, target);
	}
	
	private static String translateTaxOption(char taxOption) {
		switch (taxOption) {
		case Credit.TAX_OPTION_ALL_TAXES:
			return "ALL_TAXES";
		case Credit.TAX_OPTION_GST_ONLY:
			return "GST_ONLY";
		case Credit.TAX_OPTION_NO_TAX:
			return "NO_TAX";
		default:
			return null;
		}
	}
}
