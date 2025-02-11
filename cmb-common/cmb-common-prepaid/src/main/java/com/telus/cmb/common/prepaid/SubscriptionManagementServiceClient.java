package com.telus.cmb.common.prepaid;

import java.util.List;

import org.springframework.stereotype.Component;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.SubscriptionManagementServicePort;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.ChangeAutomaticRecharge;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.ChangeAutomaticRechargeResponse;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RechargeBalanceByPaymentInstrument;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RechargeBalanceByPaymentInstrumentResponse;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RechargeBalanceByRegisteredPaymentInstrument;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RechargeBalanceByRegisteredPaymentInstrumentResponse;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RechargeBalanceByVoucher;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RechargeBalanceByVoucherResponse;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RegisterAutomaticRecharge;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RegisterAutomaticRechargeResponse;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RegisterRechargePaymentInstrument;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.RegisterRechargePaymentInstrumentResponse;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.UpdateRechargePaymentInstrument;
import com.telus.schemas.srv.cmo.billingaccountmgmt.subscriptionmanagementservice_v3.UpdateRechargePaymentInstrumentResponse;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.commondomaintypes_v4.PaymentInstrument;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.AutomaticRechargeProfile;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

@Component
public class SubscriptionManagementServiceClient extends SoaBaseSvcClient {

	protected SubscriptionManagementServicePort port;

	public void setPort(SubscriptionManagementServicePort port) {
		this.port = port;
	}
	
	@Override
	public String ping() throws ApplicationException {
		return execute(new SoaCallback<String>() {
			@Override
			public String doCallback() throws Exception {
				return port.ping(new Ping()).getVersion();
			}
		});
	}
	
	public ChangeAutomaticRechargeResponse changeAutomaticRecharge(final String subscriptionID, 
			final String balanceType, 
			final List<AutomaticRechargeProfile> automaticRechargeProfiles) throws ApplicationException {
		return execute(new SoaCallback<ChangeAutomaticRechargeResponse>() {
			@Override
			public ChangeAutomaticRechargeResponse doCallback() throws Exception {
				ChangeAutomaticRecharge parameters = new ChangeAutomaticRecharge();
				parameters.setBalanceType(balanceType);
				parameters.setSubscriptionID(subscriptionID);
				parameters.getAutomaticRechargeProfiles().addAll(automaticRechargeProfiles);
				return port.changeAutomaticRecharge(parameters,new OriginatingUserType()); 
			}
		});
	}
	
	public UpdateRechargePaymentInstrumentResponse updateRechargePaymentInstrument(final String subscriptionID, final PaymentInstrument paymentInstrument) throws ApplicationException {
		return execute(new SoaCallback<UpdateRechargePaymentInstrumentResponse>() {
			@Override
			public UpdateRechargePaymentInstrumentResponse doCallback() throws Exception {
				UpdateRechargePaymentInstrument parameters = new UpdateRechargePaymentInstrument();
				parameters.setPaymentInstrument(paymentInstrument);
				parameters.setSubscriptionID(subscriptionID);
				return port.updateRechargePaymentInstrument(parameters,new OriginatingUserType());
			}
		});
	}
	
	public RegisterAutomaticRechargeResponse registerAutomaticRecharge(final String subscriptionID, 
			final String balanceType, 
			final List<AutomaticRechargeProfile> automaticRechargeProfiles, 
			final boolean immediateRecharge, 
			final String cvv) throws ApplicationException {
		return execute(new SoaCallback<RegisterAutomaticRechargeResponse>() {
			@Override
			public RegisterAutomaticRechargeResponse doCallback() throws Exception {
				RegisterAutomaticRecharge parameters = new RegisterAutomaticRecharge();
				parameters.setBalanceType(balanceType);
				parameters.setCvv(cvv);
				parameters.setImmediateRecharge(immediateRecharge);
				parameters.setSubscriptionID(subscriptionID);
				parameters.getAutomaticRechargeProfiles().addAll(automaticRechargeProfiles);
				return port.registerAutomaticRecharge(parameters,new OriginatingUserType()); 
			}
		});
	}

	public RechargeBalanceByVoucherResponse rechargeBalanceByVoucher(final String subscriptionID, 
			final String balanceType, 
			final String voucherPIN) throws ApplicationException {
		return execute(new SoaCallback<RechargeBalanceByVoucherResponse>() {
			@Override
			public RechargeBalanceByVoucherResponse doCallback() throws Exception {
				RechargeBalanceByVoucher parameters = new RechargeBalanceByVoucher();
				parameters.setBalanceType(balanceType);
				parameters.setSubscriptionID(subscriptionID);
				parameters.setVoucherPIN(voucherPIN);
				return port.rechargeBalanceByVoucher(parameters,new OriginatingUserType()); 
			}
		});
	}

	public RechargeBalanceByRegisteredPaymentInstrumentResponse rechargeBalanceByRegisteredPaymentInstrument(final String subscriptionID, 
			final double amount, 
			final String balanceType, 
			final String paymentInstrumentType, 
			final Boolean recurringCharge, 
			final String cvv) throws ApplicationException {
		return execute(new SoaCallback<RechargeBalanceByRegisteredPaymentInstrumentResponse>() {
			@Override
			public RechargeBalanceByRegisteredPaymentInstrumentResponse doCallback() throws Exception {
				RechargeBalanceByRegisteredPaymentInstrument parameters = new RechargeBalanceByRegisteredPaymentInstrument();
				parameters.setAmount(amount);
				parameters.setBalanceType(balanceType);
				parameters.setCvv(cvv);
				parameters.setPaymentInstrumentType(paymentInstrumentType);
				parameters.setRecurringCharge(recurringCharge);
				parameters.setSubscriptionID(subscriptionID);
				return port.rechargeBalanceByRegisteredPaymentInstrument(parameters,new OriginatingUserType());
			}
		});
	}
	
	public RechargeBalanceByPaymentInstrumentResponse rechargeBalanceByPaymentInstrument(final String subscriptionID, 
			final double amount, 
			final String balanceType, 
			final String paymentInstrumentType, 
			final String cvv, 
			final PaymentInstrument paymentInstrument) throws ApplicationException {
		return execute(new SoaCallback<RechargeBalanceByPaymentInstrumentResponse>() {
			@Override
			public RechargeBalanceByPaymentInstrumentResponse doCallback() throws Exception {
				RechargeBalanceByPaymentInstrument parameters = new RechargeBalanceByPaymentInstrument();
				parameters.setAmount(amount);
				parameters.setBalanceType(balanceType);
				parameters.setCvv(cvv);
				parameters.setPaymentInstrument(paymentInstrument);
				parameters.setPaymentInstrumentType(paymentInstrumentType);
				parameters.setSubscriptionID(subscriptionID);
				return port.rechargeBalanceByPaymentInstrument(parameters,new OriginatingUserType());
			}
		});
	}
	
	public RegisterRechargePaymentInstrumentResponse registerRechargePaymentInstrument(final RegisterRechargePaymentInstrument parameters, 
			final OriginatingUserType registerRechargePaymentInstrumentInSoapHdr) throws ApplicationException {
		return execute(new SoaCallback<RegisterRechargePaymentInstrumentResponse>() {
			@Override
			public RegisterRechargePaymentInstrumentResponse doCallback() throws Exception {
				return port.registerRechargePaymentInstrument(parameters, registerRechargePaymentInstrumentInSoapHdr);
			}
		});
	}
	
	
}
