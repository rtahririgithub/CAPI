package com.telus.cmb.subscriber.mapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.utility.info.SapccThresholdInfo;
import com.telus.eas.utility.info.SapccUpdateAccountPurchaseInfo;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.onlinechargingsubscriberaccountmgmtservicerequestresponse_v3.ChargingDetail;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.onlinechargingsubscriberaccountmgmtservicerequestresponse_v3.Threshold;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.onlinechargingsubscriberaccountmgmtservicerequestresponse_v3.UpdatePurchaseAmountResult;

/**
 * OcssamServiceMapper
 * 
 * @author R. Fong
 *
 */
public class OcssamServiceMapper {

	public static UpdatePurchaseAmountResultMapper UpdatePurchaseAmountResultMapper() {
		return UpdatePurchaseAmountResultMapper.getInstance();
	}

	public static class UpdatePurchaseAmountResultMapper extends AbstractSchemaMapper<UpdatePurchaseAmountResult, SapccUpdateAccountPurchaseInfo> {

		private static UpdatePurchaseAmountResultMapper INSTANCE = null;

		private UpdatePurchaseAmountResultMapper() {
			super(UpdatePurchaseAmountResult.class, SapccUpdateAccountPurchaseInfo.class);
		}

		protected synchronized static UpdatePurchaseAmountResultMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new UpdatePurchaseAmountResultMapper();
			}
			return INSTANCE;
		}

		@Override
		protected SapccUpdateAccountPurchaseInfo performDomainMapping(UpdatePurchaseAmountResult source, SapccUpdateAccountPurchaseInfo target) {

			target.setBreachedInd(source.isBreachedInd());
			target.setZone(source.getZoneCd());
			for (ChargingDetail detail : source.getChargingDetailList()) {
				if (StringUtils.equalsIgnoreCase(detail.getAmountCd(), "CURRENT CHARGED AMOUNT")) {
					target.setChargedAmount(detail.getAmountNum().doubleValue());
				}
				if (StringUtils.equalsIgnoreCase(detail.getAmountCd(), "CURRENT PURCHASE AMOUNT")) {
					target.setPurchaseAmount(detail.getAmountNum().doubleValue());
				}
				if (StringUtils.equalsIgnoreCase(detail.getAmountCd(), "LAST CONSENTED AMOUNT")) {
					target.setLastConsentAmount(detail.getAmountNum().doubleValue());
				}
			}
			List<SapccThresholdInfo> list = new ArrayList<SapccThresholdInfo>();
			for (Threshold threshold : source.getThresholdList()) {
				SapccThresholdInfo info = new SapccThresholdInfo();
				info.setThresholdType(threshold.getThresholdTypeCd());
				info.setThresholdLimitAmount(threshold.getThresholdValueNum().doubleValue());		
				list.add(info);
			}
			target.setThresholdList(list);
			
			return super.performDomainMapping(source, target);
		}

	}

}