package com.telus.cmb.account.mapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.BillMediumInfo;
import com.telus.eas.account.info.BillNotificationAddressInfo;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.EmailBillNotificationAddressInfo;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.GetBillMediumInfoResponse;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.PaperBillMediumInfo;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.PhoneBillNotificationAddressInfo;

public class BillNotificationMgmtServiceMapper {
	
	public static BillMediumInfoMapper BillMediumInfoMapper() {
		return BillMediumInfoMapper.getInstance();
	}
	
	public static PhoneBillNotificationAddressInfoMapper PhoneBillNotificationAddressInfoMapper() {
		return PhoneBillNotificationAddressInfoMapper.getInstance();
	}
	
	public static EmailBillNotificationAddressInfoMapper EmailBillNotificationAddressInfoMapper() {
		return EmailBillNotificationAddressInfoMapper.getInstance();
	}
	
	public static class BillMediumInfoMapper extends AbstractSchemaMapper<GetBillMediumInfoResponse, BillMediumInfo> {

		private static BillMediumInfoMapper INSTANCE = null;
		private static final Logger LOGGER = Logger.getLogger(BillMediumInfoMapper.class);

		public BillMediumInfoMapper() {
			super(GetBillMediumInfoResponse.class, BillMediumInfo.class);
		}

		protected synchronized static BillMediumInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new BillMediumInfoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected BillMediumInfo performDomainMapping(GetBillMediumInfoResponse source, BillMediumInfo target) {

			List<String> billTypeList = new ArrayList<String>();			

			if (CollectionUtils.isNotEmpty(source.getEbillBillMediumInfoList())) {
				billTypeList.add(BillMediumInfo.BILL_MEDIUM_EBILL);
			}

			for (PaperBillMediumInfo paperInfo : source.getPaperBillMediumInfoList()) {
				if (paperInfo.getPaperBillType().trim().equalsIgnoreCase(BillMediumInfo.BILL_MEDIUM_FULL_PAPER)) {
					billTypeList.add(BillMediumInfo.BILL_MEDIUM_FULL_PAPER);
				} else if (paperInfo.getPaperBillType().trim().equalsIgnoreCase(BillMediumInfo.BILL_MEDIUM_PAPER)) {
					billTypeList.add(BillMediumInfo.BILL_MEDIUM_PAPER);
				} else {
					LOGGER.info("Unrecognized paperBillType: " + paperInfo.getPaperBillType());
				}
			}

			target.setBillTypeList(billTypeList);
			
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class PhoneBillNotificationAddressInfoMapper extends AbstractSchemaMapper<PhoneBillNotificationAddressInfo, BillNotificationAddressInfo> {

		private static PhoneBillNotificationAddressInfoMapper INSTANCE = null;

		public PhoneBillNotificationAddressInfoMapper() {
			super(PhoneBillNotificationAddressInfo.class, BillNotificationAddressInfo.class);
		}

		protected synchronized static PhoneBillNotificationAddressInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new PhoneBillNotificationAddressInfoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected BillNotificationAddressInfo performDomainMapping(PhoneBillNotificationAddressInfo source, BillNotificationAddressInfo target) {
			target.setPhoneNumber(String.valueOf(source.getPhoneNumber()));
			target.setEffectiveDate(source.getEffectiveDate());
			target.setExpiryDate(source.getExpiryDate());			
			return super.performDomainMapping(source, target);
		}
	}

	
	public static class EmailBillNotificationAddressInfoMapper extends AbstractSchemaMapper<EmailBillNotificationAddressInfo, BillNotificationAddressInfo> {

		private static EmailBillNotificationAddressInfoMapper INSTANCE = null;

		public EmailBillNotificationAddressInfoMapper() {
			super(EmailBillNotificationAddressInfo.class, BillNotificationAddressInfo.class);
		}

		protected synchronized static EmailBillNotificationAddressInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new EmailBillNotificationAddressInfoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected BillNotificationAddressInfo performDomainMapping(EmailBillNotificationAddressInfo source, BillNotificationAddressInfo target) {
			target.setEmailAddress(source.getEmailAddress());
			target.setEffectiveDate(source.getEffectiveDate());
			target.setExpiryDate(source.getExpiryDate());			
			return super.performDomainMapping(source, target);
		}
	}
}