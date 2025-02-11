package com.telus.cmb.common.dao.billnotification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.BillNotificationManagementPort;
import com.telus.eas.account.info.BillMediumInfo;
import com.telus.eas.account.info.BillNotificationAddressInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.ActivateBillMediumSet;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.DeactivateBillMediumSet;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.EbillBillMediumRequest;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.ExpireBillNotificationAddress;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.GetBillMediumInfo;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.GetBillNotificationAddressInfo;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.GetBillNotificationAddressInfoResponse;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.PaperBillMediumRequest;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.ProcessBillMedium;
import com.telus.tmi.xmlschema.srv.cmo.billformatmgmt.billnotificationmanagementsvcrequestresponse_v4.ProcessBillNotificationAddress;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

public class BillNotificationManagementDaoImpl extends SoaBaseSvcClient implements BillNotificationManagementDao {
	@Autowired
	public BillNotificationManagementPort billNotificationManagementPort;

	private static final Logger LOGGER = Logger.getLogger(BillNotificationManagementDaoImpl.class);

	public void setBillNotificationManagementServicePortType(BillNotificationManagementPort port) {
		this.billNotificationManagementPort = port;
	}

	@Override
	public String ping() throws ApplicationException {
		return execute(new SoaCallback<String>() {
			@Override
			public String doCallback() throws Exception {
				Ping parameters = new Ping();
				return billNotificationManagementPort.ping(parameters).getVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo test() {
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName("Bill Notification Management Service v4.0 ping");

		try {
			String pingResult = ping();
			resultInfo.setResultDetail(pingResult);
			resultInfo.setPass(true);
		} catch (Throwable t) {
			resultInfo.setExceptionDetail(t);
			resultInfo.setPass(false);
		}

		return resultInfo;
	}

	@Override
	public BillMediumInfo getBillMediumInfo(final int ban) throws ApplicationException {

		LOGGER.debug("BNMS getBillMediumInfo request - ban: " + ban);

		return execute(new SoaCallback<BillMediumInfo>() {

			@Override
			public BillMediumInfo doCallback() throws Throwable {

				// Prepare the request data
				GetBillMediumInfo request = new GetBillMediumInfo();
				request.setAccountNumber(ban);
				request.setEffectiveIndicator(Boolean.TRUE);
				
				// Map the response data
				BillMediumInfo billMediumInfo = BillNotificationMgmtServiceMapper.BillMediumInfoMapper().mapToDomain(billNotificationManagementPort.getBillMediumInfo(request));
				LOGGER.debug("BNMS getBillMediumInfo response: " + billMediumInfo);
				
				return billMediumInfo;
			}
		});
	}

	@Override
	public List<BillNotificationAddressInfo> getBillNotificationAddressInfo(final int ban) throws ApplicationException {

		LOGGER.debug("BNMS getBillNotificationInfo request - ban: " + ban);

		return execute(new SoaCallback<List<BillNotificationAddressInfo>>() {

			@Override
			public List<BillNotificationAddressInfo> doCallback() throws Throwable {

				// Prepare the request data
				GetBillNotificationAddressInfo request = new GetBillNotificationAddressInfo();
				request.setAccountNumber(ban);
				request.setEffectiveIndicator(Boolean.TRUE);

				// Map the response data
				GetBillNotificationAddressInfoResponse response = billNotificationManagementPort.getBillNotificationAddressInfo(request);
				List<BillNotificationAddressInfo> addressInfoList = new ArrayList<BillNotificationAddressInfo>();
				if (response != null) {
					addressInfoList.addAll(BillNotificationMgmtServiceMapper.PhoneBillNotificationAddressInfoMapper().mapToDomain(response.getPhoneBillNotificationAddressInfoList()));
					addressInfoList.addAll(BillNotificationMgmtServiceMapper.EmailBillNotificationAddressInfoMapper().mapToDomain(response.getEmailBillNotificationAddressInfoList()));
				}								
				LOGGER.debug("getBillNotificationInfo response: " + addressInfoList);
				
				return addressInfoList;
			}
		});
	}

	@Override
	public void processBillMedium(final int ban, final BillMediumInfo billMediumInfo) throws ApplicationException {

		execute(new SoaCallback<Object>() {

			@Override
			public Object doCallback() throws Throwable {

				// Set the ban
				ProcessBillMedium request = new ProcessBillMedium();
				request.setAccountNumber(ban);

				ActivateBillMediumSet activateBillMediumSet = new ActivateBillMediumSet();
				DeactivateBillMediumSet deactivateBillMediumSet = new DeactivateBillMediumSet();

				// Activate bill medium
				for (String addedBillType : billMediumInfo.getAddedBillTypeList()) {
					if (addedBillType.equalsIgnoreCase(BillMediumInfo.BILL_MEDIUM_FULL_PAPER) || addedBillType.trim().equalsIgnoreCase(BillMediumInfo.BILL_MEDIUM_PAPER)) {
						PaperBillMediumRequest paperBillMediumRequest = new PaperBillMediumRequest();
						paperBillMediumRequest.setType(addedBillType);
						activateBillMediumSet.setPaperBillMediumRequest(paperBillMediumRequest);
						request.setActivateBillMedium(activateBillMediumSet);
					} else if (addedBillType.equalsIgnoreCase(BillMediumInfo.BILL_MEDIUM_EBILL)) {
						EbillBillMediumRequest ebillBillMediumRequest = new EbillBillMediumRequest();
						ebillBillMediumRequest.setType(addedBillType);
						activateBillMediumSet.setEbillBillMediumRequest(ebillBillMediumRequest);
						request.setActivateBillMedium(activateBillMediumSet);
					}
				}

				// Deactivate bill medium
				if (CollectionUtils.isNotEmpty(billMediumInfo.getRemovedBillTypeList())) {
					deactivateBillMediumSet.setBillMediumTypeList(billMediumInfo.getRemovedBillTypeList());
					request.setDeactivateBillMedium(deactivateBillMediumSet);
				}

				return billNotificationManagementPort.processBillMedium(request);
			}
		});
	}
	
	@Override
	public void processBillNotificationAddress(final int ban, final List<String> expireBillNotificationPhoneNumberList, final List<String> expireBillNotificationEmailAddressList,
			final List<String> registerBillNotificationPhoneNumberList, final List<String> registerBillNotificationEmailAddressList) throws ApplicationException {

		execute(new SoaCallback<Object>() {
			
			@Override
			public Object doCallback() throws Throwable {

				ProcessBillNotificationAddress request = new ProcessBillNotificationAddress();
				
				// Set the ban
				request.setAccountNumber(ban);				
				request.setExpireBillNotificationAddressSet(BillNotificationMgmtServiceMapper.createExpireBillNotificationAddressSet(expireBillNotificationPhoneNumberList,
						expireBillNotificationEmailAddressList));
				request.setRegisterBillNotificationAddressSet(BillNotificationMgmtServiceMapper.createRegisterBillNotificationAddressSet(registerBillNotificationPhoneNumberList,
						registerBillNotificationEmailAddressList));

				// If we're not making any changes, let's skip calling the downstream service entirely
				if (request.getExpireBillNotificationAddressSet() == null && request.getRegisterBillNotificationAddressSet() == null) {
					LOGGER.info("processBillNotificationAddress invoked with no changes, skipping call to BNMSv4.");
					return null;
				}
				
				return billNotificationManagementPort.processBillNotificationAddress(request);
			}
		});
	}

	@Override
	public void expireBillNotificationAddress(final int ban, final List<String> billNotificationPhoneNumberList, final List<String> billNotificationEmailAddressList) throws ApplicationException {

		execute(new SoaCallback<Object>() {
			@Override
			public Object doCallback() throws Throwable {

				ExpireBillNotificationAddress request = new ExpireBillNotificationAddress();
				request.setAccountNumber(ban);
				for (String phoneNumber : billNotificationPhoneNumberList) {
					request.getPhoneNumberList().add(Long.parseLong(phoneNumber));
				}
				if (CollectionUtils.isNotEmpty(billNotificationEmailAddressList)) {
					request.setEmailAddressList(billNotificationEmailAddressList);
				}
				
				return billNotificationManagementPort.expireBillNotificationAddress(request);
			}
		});
	}
}

