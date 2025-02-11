package com.telus.cmb.jws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper.ActivityResultMapper;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper.AuditInfoMapper;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper.BaseWirelessAccountMapper;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper.BusinessCreditIdentityMapper;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper.CreatePostpaidAccountResponseMapper;
import com.telus.cmb.jws.mapping.WirelessAccountLifecycleMgmtMapper.FollowUpMapper;
import com.telus.cmb.jws.mapping.billing_inquiry.AuditHeaderMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.CreditCardMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.MemoMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.PaymentArrangementEligibilityResponseInfo;
import com.telus.eas.account.info.PaymentNotificationResponseInfo;
import com.telus.eas.account.info.PostpaidAccountCreationResponseInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.transaction.info.ActivityResultInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.cmb.jws.CreatePostpaidAccount;
import com.telus.cmb.jws.CreatePostpaidAccountResponse;
import com.telus.cmb.jws.CreatePrepaidAccount;
import com.telus.cmb.jws.CreatePrepaidAccountResponse;
import com.telus.cmb.jws.PaymentArrangementEligibilityResponseType;
import com.telus.cmb.jws.PaymentArrangementResponseType;
import com.telus.cmb.jws.PaymentNotificationResponseType;
import com.telus.cmb.jws.RestoreAccountFromPayment;
import com.telus.cmb.jws.RestoreAccountFromPaymentResponse;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BusinessCreditIdentity;

@WebService(portName = "WirelessAccountLifecycleMgmtServicePort", serviceName = "WirelessAccountLifecycleMgmtService_v1_3", targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/WirelessAccountLifecycleMgmtService_1", wsdlLocation = "/wsdls/WirelessAccountLifecycleMgmtService_v1_3.wsdl", endpointInterface = "com.telus.cmb.jws.WirelessAccountLifecycleMgmtServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@SchemaValidation(handler = com.telus.cmb.jws.ServiceSchemaValidator.class)
public class WirelessAccountLifecycleMgmtService_13 extends BaseService implements WirelessAccountLifecycleMgmtServicePort {

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_WALM_0001", errorMessage = "Create Postpaid Account")
	public CreatePostpaidAccountResponse createPostpaidAccount(final CreatePostpaidAccount parameters, final OriginatingUserType createPostpaidAccountInSoapHdr) throws PolicyException,
			ServiceException {

		return execute(new ServiceInvocationCallback<CreatePostpaidAccountResponse>() {

			@Override
			public CreatePostpaidAccountResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				if (createPostpaidAccountInSoapHdr == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
				}

				AuditHeaderMapper auditHeaderMapper = new AuditHeaderMapper();
				AuditHeaderInfo auditHeaderInfo = auditHeaderMapper.mapToDomain(createPostpaidAccountInSoapHdr);

				AccountInfo accountInfo = getBaseWirelessAccountMapper().mapToDomain(parameters.getBaseAccountRequest().getBaseAccount());

				BusinessCreditIdentityInfo[] creditIdentityInfos = null;
				if (parameters.getBaseAccountRequest().getBusinessCreditIdentityList() != null && parameters.getBaseAccountRequest().getBusinessCreditIdentityList().getItem().size() > 0) {
					List<BusinessCreditIdentityInfo> businessCreditIdentityList = new ArrayList<BusinessCreditIdentityInfo>();

					List<BusinessCreditIdentity> sourceList = parameters.getBaseAccountRequest().getBusinessCreditIdentityList().getItem();
					if (sourceList != null && sourceList.size() > 0) {
						for (int i = 0; i < sourceList.size(); i++) {
							businessCreditIdentityList.add(getBusinessCreditIdentityMapper().mapToDomain(sourceList.get(i)));
						}
					}
					creditIdentityInfos = businessCreditIdentityList.toArray(new BusinessCreditIdentityInfo[businessCreditIdentityList.size()]);
				}
				BusinessCreditIdentityInfo selectedBusinessCreditIdentity = getBusinessCreditIdentityMapper().mapToDomain(parameters.getBaseAccountRequest().getSelectedBusinessCreditIdentity());
				AuditInfo auditInfo = getAuditInfoMapper().mapToDomain(parameters.getBaseAccountRequest().getAuditInfo());

				setTelusTidToMessageContext(auditInfo);

				String customerId = null;
				if (parameters.getBaseAccountRequest().getCompassCustomerId() != null)
					customerId = parameters.getBaseAccountRequest().getCompassCustomerId().toString();

				PostpaidAccountCreationResponseInfo postpaidAccountCreationResponse = getAccountLifecycleFacade(context).createPostpaidAccount(accountInfo, customerId, creditIdentityInfos,
						selectedBusinessCreditIdentity, auditHeaderInfo, auditInfo, "SQ1ZJ3DGERi8Vgw3gfXzKoAkumKZszUf+6/iq4wRwZs=");

				return getCreatePostpaidAccountResponseMapper().mapToSchema(postpaidAccountCreationResponse);
			}
		});
	}

	private void setTelusTidToMessageContext(AuditInfo auditInfo) {
		// grab the telusTID put into message context
		if (auditInfo != null && "1".equals(auditInfo.getUserTypeCode())) {
			super.setTelusTidToMessageContext(auditInfo.getUserId());
		}
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_WALM_0002", errorMessage = "Create Prepaid Account")
	public CreatePrepaidAccountResponse createPrepaidAccount(final CreatePrepaidAccount parameters, final OriginatingUserType createPrepaidAccountInSoapHdr) throws PolicyException, ServiceException {

		return execute(new ServiceInvocationCallback<CreatePrepaidAccountResponse>() {

			@Override
			public CreatePrepaidAccountResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				if (createPrepaidAccountInSoapHdr == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
				}
				AuditHeaderMapper auditHeaderMapper = new AuditHeaderMapper();
				AuditHeaderInfo auditHeaderInfo = auditHeaderMapper.mapToDomain(createPrepaidAccountInSoapHdr);

				AccountInfo accountInfo = getBaseWirelessAccountMapper().mapToDomain(parameters.getPrepaidAccounttRequest().getBaseAccount());
				if (parameters.getPrepaidAccounttRequest().getCompassCustomerId() != null)
					((PrepaidConsumerAccountInfo) accountInfo).setCustomerId(parameters.getPrepaidAccounttRequest().getCompassCustomerId().intValue());

				((PrepaidConsumerAccountInfo) accountInfo).setSerialNumber(parameters.getPrepaidAccounttRequest().getSerialNumber());

				if (parameters.getPrepaidAccounttRequest().getHandsetIMEI() != null)
					((PrepaidConsumerAccountInfo) accountInfo).setAssociatedHandsetIMEI(parameters.getPrepaidAccounttRequest().getHandsetIMEI());

				if (parameters.getPrepaidAccounttRequest().getActivationType() != null)
					((PrepaidConsumerAccountInfo) accountInfo).setActivationType(Integer.valueOf(parameters.getPrepaidAccounttRequest().getActivationType()));

				((PrepaidConsumerAccountInfo) accountInfo).setActivationCode(parameters.getPrepaidAccounttRequest().getActivationCode());

				if (parameters.getPrepaidAccounttRequest().getActivationAmount() != null)
					((PrepaidConsumerAccountInfo) accountInfo).setActivationCreditAmount(parameters.getPrepaidAccounttRequest().getActivationAmount());

				if (parameters.getPrepaidAccounttRequest().getBirthDate() != null)
					((PrepaidConsumerAccountInfo) accountInfo).setBirthDate(parameters.getPrepaidAccounttRequest().getBirthDate());

				CreditCardInfo creditCardInfo = new CreditCardMapper().mapToDomain(parameters.getPrepaidAccounttRequest().getActivationCreditCard());
				if (creditCardInfo != null)
					((PrepaidConsumerAccountInfo) accountInfo).getActivationCreditCard0().copyFrom(creditCardInfo);

				CreditCardInfo topUpCreditCardInfo = new CreditCardMapper().mapToDomain(parameters.getPrepaidAccounttRequest().getTopUpCreditCard());
				if (topUpCreditCardInfo != null)
					((PrepaidConsumerAccountInfo) accountInfo).getTopUpCreditCard0().copyFrom(topUpCreditCardInfo);

				AuditInfo auditInfo = getAuditInfoMapper().mapToDomain(parameters.getPrepaidAccounttRequest().getAuditInfo());

				setTelusTidToMessageContext(auditInfo);

				String customerId = null;
				if (parameters.getPrepaidAccounttRequest().getCompassCustomerId() != null)
					customerId = parameters.getPrepaidAccounttRequest().getCompassCustomerId().toString();

				String businessRole = null;
				if (parameters.getPrepaidAccounttRequest().getBusinessRole() != null)
					businessRole = parameters.getPrepaidAccounttRequest().getBusinessRole().value();

				int ban = getAccountLifecycleFacade(context).createPrepaidAccount(accountInfo, customerId, businessRole, auditHeaderInfo, auditInfo, context.getAccountLifeCycleFacadeSessionId());

				CreatePrepaidAccountResponse createPrepaidAccountResponse = new CreatePrepaidAccountResponse();
				createPrepaidAccountResponse.setBillingAccountNumber(String.valueOf(ban));

				return createPrepaidAccountResponse;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_WALM_0003", errorMessage = "Restore Account from Payment")
	public RestoreAccountFromPaymentResponse restoreAccountFromPayment(final RestoreAccountFromPayment parameters, final OriginatingUserType restoreAccountFromPaymentInSoapHdr)
			throws PolicyException, ServiceException {
		throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is not yet implemented.");
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_WALM_0004", errorMessage = "Apply payment notification")
	public PaymentNotificationResponseType applyPaymentNotification(final String billingAccountNumber, final String paymentAmount,
			final com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo auditInfo, final Boolean notificationSuppressionInd) throws PolicyException, ServiceException {
		throw new ServicePolicyException (ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is no longer supported in this web service - please use WLSPaymentMgmtSvc.");
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_WALM_0005", errorMessage = "Payment arrangement eligibility check")
	public PaymentArrangementEligibilityResponseType checkPaymentArrangementEligibility(final String billingAccountNumber,
			final com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo auditInfo, final Boolean notificationSuppressionInd) throws PolicyException, ServiceException {
		throw new ServicePolicyException (ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is no longer supported in this web service - please use WLSPaymentMgmtSvc.");
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_WALM_0006", errorMessage = "Apply payment arrangement")
	public PaymentArrangementResponseType applyPaymentArrangement(final String paymentAmount, final PaymentArrangementEligibilityResponseType paymentArrangementEligibilityResponse,
			final com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo auditInfo, final Boolean notificationSuppressionInd) throws PolicyException, ServiceException {
		throw new ServicePolicyException (ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is no longer supported in this web service - please use WLSPaymentMgmtSvc.");
	}

	private BaseWirelessAccountMapper getBaseWirelessAccountMapper() {
		return WirelessAccountLifecycleMgmtMapper.BaseWirelessAccountMapper();
	}

	private BusinessCreditIdentityMapper getBusinessCreditIdentityMapper() {
		return WirelessAccountLifecycleMgmtMapper.BusinessCreditIdentityMapper();
	}

	private AuditInfoMapper getAuditInfoMapper() {
		return WirelessAccountLifecycleMgmtMapper.AuditInfoMapper();
	}

	private CreatePostpaidAccountResponseMapper getCreatePostpaidAccountResponseMapper() {
		return WirelessAccountLifecycleMgmtMapper.CreatePostpaidAccountResponseMapper();
	}

	private MemoMapper getMemoMapper_40() {
		return MemoMapper.getInstance();
	}

	private FollowUpMapper getFollowUpMapper() {
		return WirelessAccountLifecycleMgmtMapper.FollowUpMapper();
	}

	private ActivityResultMapper getActivityResultMapper() {
		return WirelessAccountLifecycleMgmtMapper.ActivityResultMapper();
	}

}
