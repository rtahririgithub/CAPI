package com.telus.cmb.common.dao.billnotification;

import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.BillMediumInfo;
import com.telus.eas.account.info.BillNotificationAddressInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface BillNotificationManagementDao {

	public BillMediumInfo getBillMediumInfo(int ban) throws ApplicationException;

	public List<BillNotificationAddressInfo> getBillNotificationAddressInfo(int ban) throws ApplicationException;

	public void processBillMedium(int ban, BillMediumInfo billMediumInfo) throws ApplicationException;

	public void processBillNotificationAddress(int ban, List<String> expireBillNotificationPhoneNumberList, List<String> expireBillNotificationEmailAddressList, List<String> registerBillNotificationPhoneNumberList,
			List<String> registerBillNotificationEmailAddressList) throws ApplicationException;

	public void expireBillNotificationAddress(int ban, List<String> billNotificationPhoneNumberList, List<String> billNotificationEmailAddressList) throws ApplicationException;

	public TestPointResultInfo test();

}
