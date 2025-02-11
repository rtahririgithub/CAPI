package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClientV2;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.common.dao.soa.spring.SoaDefaultErrorHandler;
import com.telus.cmb.subscriber.lifecyclefacade.dao.OcssamServiceDao;
import com.telus.cmb.wsclient.OnlineChargingSubscriberAccountMgmtServicePort;
import com.telus.cmb.wsclient.ServiceException_v3;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.onlinechargingsubscriberaccountmgmtservicerequestresponse_v3.UpdateAccountPurchaseAmount;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse;

public class OcssamServiceDaoImpl extends SoaBaseSvcClientV2 implements OcssamServiceDao {

	@Autowired
	private OnlineChargingSubscriberAccountMgmtServicePort ocssamsPort;
	
	public OcssamServiceDaoImpl() {
		super(new SoaDefaultErrorHandler() {
			
			@Override
			protected SystemException getSystemException(ServiceException_v3 cause) {
				
				String errorCode = getErrorCode(cause);
				String errorMessage = getErrorMsg(cause);
				String messageId = getErrorMsgId(cause);

				return new SystemException(SystemCodes.CMB_OCSSAMS_DAO, ErrorCodes.OCS_GENERAL_ERROR, "ServiceException occurred while calling OCSSAM service: message ID [" + messageId + "]; errorCode [" + errorCode
						+ "]; error message [" + errorMessage + "].", StringUtils.EMPTY, cause);
			}
			
		}, new OcssamServiceSoaResponseHandler(OcssamServiceDaoImpl.class.getName()));
	}

	public void updateAccountPurchaseAmount(final int ban, final String subscriberId, final String phoneNumber, final double domesticAmount, final double roamingAmount,
			final String actionCode, final String originatingApplicationId) throws ApplicationException {

		execute(new SoaCallback<Object>() {

			@Override
			public Object doCallback() throws Throwable {

				UpdateAccountPurchaseAmount request = new UpdateAccountPurchaseAmount();
				request.setBillingAccountNum(ban);
				request.setSubscriberId(subscriberId);
				request.setPhoneNum(Long.valueOf(phoneNumber));
				request.setDomesticPurchaseAmount(new BigDecimal(domesticAmount));
				request.setRoamingPurchaseAmount(new BigDecimal(roamingAmount));
				request.setActionCd(actionCode);
				request.setInitiatingApplicationCd(originatingApplicationId);

				getResponseHandler().handleErrorResponse(ocssamsPort.updateAccountPurchaseAmount(request), SystemCodes.CMB_OCSSAMS_DAO);
				
				return null;
			}

		});
	}

	@Override
	public String ping() throws ApplicationException {

		return execute(new SoaCallback<String>() {

			public String doCallback() throws Exception {
				PingResponse pingResponse = ocssamsPort.ping(new Ping());
				return ToStringBuilder.reflectionToString(pingResponse.getPingStats());
			}

		});
	}

}