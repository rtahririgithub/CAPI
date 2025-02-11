package com.telus.cmb.common.dao.provisioning;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClientV2;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.common.dao.soa.spring.SoaDefaultErrorHandler;
import com.telus.cmb.wsclient.ServiceException_v3;
import com.telus.cmb.wsclient.VoipLicensePurchaseServicePort;
import com.telus.tmi.xmlschema.srv.smo.activation.voiplicensepurchasesvcrequestresponse_v1.DeleteLicenseOnAccount;
import com.telus.tmi.xmlschema.srv.smo.activation.voiplicensepurchasesvcrequestresponse_v1.PurchaseLicense;
import com.telus.tmi.xmlschema.srv.smo.activation.voiplicensepurchasesvcrequestresponse_v1.SwitchCodeInfo;
import com.telus.tmi.xmlschema.srv.smo.activation.voiplicensepurchasesvcrequestresponse_v1.SwitchCodeInfoList;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;

public class VOIPLicensePurchaseServiceDaoImpl extends SoaBaseSvcClientV2 implements VOIPLicensePurchaseServiceDao {

	private static final Log logger = LogFactory.getLog(VOIPLicensePurchaseServiceDaoImpl.class);

	@Autowired
	public VoipLicensePurchaseServicePort voipLicensePurchaseService_v1_0_PortType;
	
	public VOIPLicensePurchaseServiceDaoImpl() {
		super(new SoaDefaultErrorHandler() {
			
			@Override
			protected SystemException getSystemException(ServiceException_v3 cause) {

				String errorCode = getErrorCode(cause);
				String errorMessage = getErrorMsg(cause);
				String messageId = getErrorMsgId(cause);

				return new SystemException(SystemCodes.CMB_VOIP_LICENSE_DAO,
						StringUtils.equalsIgnoreCase(errorCode, "400") ? ErrorCodes.NO_UNASSIGNED_LICENSE_ERROR : ErrorCodes.VOIP_LICENSE_CHANGE_ERROR,
						"ServiceException occurred while calling VOIPLicensePurchaseService: message ID [" + messageId + "]; errorCode [" + errorCode + "]; error message [" + errorMessage + "].",
						StringUtils.EMPTY, cause);
			}
			
		}, null);
	}

	@Override
	public void addLicenses(final int ban, final String subscriptionId, final List<String> switchCodeList) throws ApplicationException {
		
		execute(new SoaCallback<Object>() {
			
			@Override
			public String doCallback() throws Throwable {
				
				// Set request header
				PurchaseLicense request = new PurchaseLicense();
				request.setBillingAccountNum(String.valueOf(ban));
				request.setSubscriptionId(subscriptionId);
				request.setSwitchCodeInfoList(populateProvisioningSwitchCodeInfo(switchCodeList));
				logger.info(request.toString());
				
				// Send the request
				getResponseHandler().handleErrorResponse(voipLicensePurchaseService_v1_0_PortType.purchaseLicense(request), SystemCodes.CMB_VOIP_LICENSE_DAO);
				
				return null;
			}
		});
	}

	@Override
	public void removeLicenses(final int ban, final String subscriptionId, final List<String> switchCodeList) throws ApplicationException {
		
		execute(new SoaCallback<Object>() {
			
			@Override
			public String doCallback() throws Throwable {
				
				// Set request header
				DeleteLicenseOnAccount request = new DeleteLicenseOnAccount();
				request.setBillingAccountNum(String.valueOf(ban));
				request.setSubscriptionId(subscriptionId);
				request.setSwitchCodeInfoList(populateProvisioningSwitchCodeInfo(switchCodeList));
				logger.info(request.toString());
				
				// Send the request
				getResponseHandler().handleErrorResponse(voipLicensePurchaseService_v1_0_PortType.deleteLicenseOnAccount(request), SystemCodes.CMB_VOIP_LICENSE_DAO);
				
				return null;
			}
		});
	}

	// Convert subscriber switch codes to Provisioning format
	private SwitchCodeInfoList populateProvisioningSwitchCodeInfo(List<String> switchCodeList) {
		
		// Take unique switch code set from list to check the frequency
		Set<String> uniqueSwitchCodes = new HashSet<String>();
		uniqueSwitchCodes.addAll(switchCodeList);
		SwitchCodeInfoList list = new SwitchCodeInfoList();
		List<SwitchCodeInfo> switchCodeInfoList = new ArrayList<SwitchCodeInfo>();
		for (String switchCode : uniqueSwitchCodes) {
			SwitchCodeInfo info = new SwitchCodeInfo();
			info.setSwitchCode(switchCode);
			info.setQuantityNum(BigInteger.valueOf(Collections.frequency(switchCodeList, switchCode)));
			switchCodeInfoList.add(info);
		}
		list.setSwitchCodeInfo(switchCodeInfoList);
		
		return list;
	}
	
	@Override
	public String ping() throws ApplicationException {

		return execute(new SoaCallback<String>() {

			@Override
			public String doCallback() throws Exception {
				PingResponse pingResponse = voipLicensePurchaseService_v1_0_PortType.ping(new Ping());
				return pingResponse.getVersion();
			}
		});
	}

}