package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.HardwarePurchaseAccountServiceDao;
import com.telus.cmb.subscriber.mapping.HardwarePurchaseAccountServiceMapper;
import com.telus.cmb.wsclient.HardwarePurchaseAccountServicePort;
import com.telus.eas.hpa.info.RewardAccountInfo;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.hardwarepurchaseaccountservicerequestresponse_v1.CloseRewardAccount;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.hardwarepurchaseaccountservicerequestresponse_v1.FetchRewardAccount;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.hardwarepurchaseaccountservicerequestresponse_v1.RestoreRewardAccount;
import com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.hardwarepurchaseaccountservicerequestresponse_v1.RewardAccountRequestType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

public class HardwarePurchaseAccountServiceDaoImpl extends SoaBaseSvcClient implements HardwarePurchaseAccountServiceDao {

	@Autowired
	private HardwarePurchaseAccountServicePort port;

	@Override
	public void closeRewardAccount(final int billingAccountNumber, final String phoneNumber, final long subscriptionId) throws ApplicationException {

		execute(new SoaCallback<Object>() {

			@Override
			public Object doCallback() throws Throwable {

				CloseRewardAccount request = new CloseRewardAccount();
				request.setBillingAccountNumber(String.valueOf(billingAccountNumber));
				request.setPhoneNumber(phoneNumber);
				// Subscription ID is mapped here as requested by the HPA team to resolve CRDB lookup issues on their side. There is a small chance that a '0' value may be
				// supplied and this will cause problems for HPA, so we will validate first before including it in the request.
				if (subscriptionId != 0L) {
					request.setSubscriptionId(BigInteger.valueOf(subscriptionId));
				}

				port.closeRewardAccount(request);

				return null;
			}

		});
	}

	@Override
	public void restoreRewardAccount(final int billingAccountNumber, final String phoneNumber, final long subscriptionId) throws ApplicationException {

		execute(new SoaCallback<Object>() {

			@Override
			public Object doCallback() throws Throwable {

				RestoreRewardAccount request = new RestoreRewardAccount();
				request.setBillingAccountNumber(String.valueOf(billingAccountNumber));
				request.setPhoneNumber(phoneNumber);
				// Subscription ID is mapped here as requested by the HPA team to resolve CRDB lookup issues on their side. There is a small chance that a '0' value may be
				// supplied and this will cause problems for HPA, so we will validate first before including it in the request.
				if (subscriptionId != 0L) {
					request.setSubscriptionId(BigInteger.valueOf(subscriptionId));
				}

				port.restoreRewardAccount(request);

				return null;
			};
		});
	}
	
	@Override
	public List<RewardAccountInfo> getRewardAccount(final int billingAccountNumber, final String phoneNumber, final long subscriptionId) throws ApplicationException {

		return execute(new SoaCallback<List<RewardAccountInfo>>() {

			@Override
			public List<RewardAccountInfo> doCallback() throws Throwable {

				FetchRewardAccount request = new FetchRewardAccount();
				RewardAccountRequestType rewardAccountequest = new RewardAccountRequestType();
				rewardAccountequest.setBillingAccountNumber(String.valueOf(billingAccountNumber));
				rewardAccountequest.setPhoneNumber(phoneNumber);
				// Subscription ID is mapped here as requested by the HPA team to resolve CRDB lookup issues on their side. There is a small chance that a '0' value may be
				// supplied and this will cause problems for HPA, so we will validate first before including it in the request.
				if (subscriptionId != 0L) {
					rewardAccountequest.setSubscriptionId(BigInteger.valueOf(subscriptionId));
				}
				request.setFetchRewardAccountRequestObj(rewardAccountequest);

				return HardwarePurchaseAccountServiceMapper.RewardAccountMapper().mapToDomain(port.fetchRewardAccount(request).getRewardAccountList());
			};
		});
	}

	@Override
	public String ping() throws ApplicationException {
		
		return execute(new SoaCallback<String>() {

			@Override
			public String doCallback() throws Throwable {
				return port.ping(new Ping()).getVersion();
			}
		});
	}

}