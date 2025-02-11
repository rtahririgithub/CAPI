package com.telus.cmb.jws;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.cmb.jws.mapping.AdjustmentReasonMapper;
import com.telus.cmb.jws.mapping.BillingCycleMapper;
import com.telus.cmb.jws.mapping.ChargeTypeMapper;
import com.telus.cmb.jws.mapping.PaymentMethodMapper;
import com.telus.cmb.jws.mapping.PaymentSourceTypeMapper;
import com.telus.cmb.jws.mapping.PrepaidRechargeDenominationMapper;
import com.telus.cmb.jws.mapping.TaxAmountMapper;
import com.telus.cmb.jws.mapping.TaxExemptionMapper;
import com.telus.cmb.jws.mapping.TaxPolicyMapper;
import com.telus.cmb.jws.mapping.reference.billing_inquiry_3_1.DiscountPlanTypeMapper;
import com.telus.eas.utility.info.TaxationPolicyInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.AdjustmentReason;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.BillingCycle;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.ChargeType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.PaymentSourceType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.PrepaidRechargeDenomination;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.RefPaymentMethod;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.TaxAmount;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.TaxExemption;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.TaxPolicyType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.DiscountPlanType;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode;

@WebService(
		portName = "BillingInquiryReferenceServicePort", 
		serviceName = "BillingInquiryReferenceService_v2_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/BillingInquiryMgmt/BillingInquiryReferenceService_2", 
		wsdlLocation = "/wsdls/BillingInquiryReferenceService_v2_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.BillingInquiryReferenceServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class BillingInquiryReferenceService_20 extends BaseService implements BillingInquiryReferenceServicePort {
	public BillingInquiryReferenceService_20() {
		super( new BillingInquiryReferenceExceptionTranslator());
	}
	
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0001")
	public List<RefPaymentMethod> getPaymentMethodList() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<RefPaymentMethod>>() {
			
			@Override
			public List<RefPaymentMethod> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PaymentMethodMapper().mapToSchema(getBillingInquiryReferenceFacade().getPaymentMethods());
			}
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_BIRS_0002")
	public List<PaymentSourceType> getPaymentSourceTypeList() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<PaymentSourceType>>() {
			
			@Override
			public List<PaymentSourceType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PaymentSourceTypeMapper().mapToSchema(getBillingInquiryReferenceFacade().getPaymentSourceTypes());
			}
		});
	}
	
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0003")
	public List<PrepaidRechargeDenomination> getPrepaidRechargeDenominationList() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<PrepaidRechargeDenomination>>() {
			
			@Override
			public List<PrepaidRechargeDenomination> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PrepaidRechargeDenominationMapper().mapToSchema(getBillingInquiryReferenceFacade().getPrepaidRechargeDenominations());
			}
		});
	}


	@ServiceBusinessOperation(errorCode="CMB_BIRS_0004")
	public List<AdjustmentReason> getAdjustmentReasonList() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<AdjustmentReason>>() {
			
			@Override
			public List<AdjustmentReason> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new AdjustmentReasonMapper().mapToSchema(getBillingInquiryReferenceFacade().getAdjustmentReasons());
			}
		});
	}


	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getAdjustmentReason(java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0005")
	public AdjustmentReason getAdjustmentReason(final String code) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<AdjustmentReason>() {
			
			@Override
			public AdjustmentReason doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new AdjustmentReasonMapper().mapToSchema(getBillingInquiryReferenceFacade().getAdjustmentReason(code));
			}
		});
	}

	
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0006")
	public List<ChargeType> getManualChargeTypeList() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<ChargeType>>() {
			
			@Override
			public List<ChargeType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ChargeTypeMapper().mapToSchema(getBillingInquiryReferenceFacade().getManualChargeTypes());
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getManualChargeType(java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0007")
	public ChargeType getManualChargeType(final String code) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<ChargeType>() {
			
			@Override
			public ChargeType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ChargeTypeMapper().mapToSchema(getBillingInquiryReferenceFacade().getManualChargeType(code));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getPaperBillChargeType(java.lang.Integer, com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.ProvinceCode, com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode, java.lang.String, java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0008")
	public ChargeType getPaperBillChargeType(final Integer brandId, final ProvinceCode provinceCode,
			final AccountTypeCode accountType, final String accountSubType, final String segment,
			final String invoiceSuppressionLevel)
	throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<ChargeType>() {
			
			@Override
			public ChargeType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ChargeTypeMapper().mapToSchema(getBillingInquiryReferenceFacade()
						.getPaperBillChargeType(brandId == null ? 0 : brandId.intValue(),
								provinceCode == null ? null : provinceCode.value(),
								accountType == null ? '\u0000' : accountType.value().toCharArray()[0],
								"".equals(accountSubType) ? '\u0000' : accountSubType.toCharArray()[0],
								"".equals(segment) ? null : segment,
								"".equals(invoiceSuppressionLevel) ? null : invoiceSuppressionLevel));
			}
		});
	}


	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getTaxRateList()
	 */
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0009")
	public List<TaxPolicyType> getTaxRateList()
			throws PolicyException, ServiceException {
		
		return execute( new ServiceInvocationCallback<List<TaxPolicyType>>() {
			
			@Override
			public List<TaxPolicyType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {	
				return new TaxPolicyMapper().mapToSchema(getBillingInquiryReferenceFacade().getTaxationPolicies());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getTaxRateListByProvince(com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode)
	 */
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0010")
	public TaxPolicyType getTaxRateListByProvince(final ProvinceCode provinceCode)
			throws PolicyException, ServiceException {
		
		assertValid("provinceCode", provinceCode, ProvinceCode.values());
		
		return execute( new ServiceInvocationCallback<TaxPolicyType>() {
			@Override
			public TaxPolicyType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {	
				return new TaxPolicyMapper().mapToSchema((TaxationPolicyInfo)getBillingInquiryReferenceFacade().getTaxationPolicy(provinceCode.value()));
			}
		});
	}


	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getTaxCalculationListByProvince(com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode, double, com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.TaxExemption)
	 */
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0011")
	public TaxAmount getTaxCalculationListByProvince(final ProvinceCode provinceCode,
			final double amount, final TaxExemption exemptionPolicy)
			throws PolicyException, ServiceException {
		
		assertValid("provinceCode", provinceCode, ProvinceCode.values());
		
		return execute( new ServiceInvocationCallback<TaxAmount>() {
			
			@Override
			public TaxAmount doInInvocationCallback(ServiceInvocationContext context) throws Throwable {	
				return new TaxAmountMapper().mapToSchema(getBillingInquiryReferenceFacade().getTaxCalculationListByProvince(provinceCode.value(), amount, new TaxExemptionMapper().mapToDomain(exemptionPolicy)));
			}
		});
	}


	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getBillingCycleList(java.lang.Object)
	 */
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0012")
	public List<BillingCycle> getBillingCycleList() throws PolicyException,
			ServiceException {
		return execute( new ServiceInvocationCallback<List<BillingCycle>>() {
			
			@Override
			public List<BillingCycle> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {	
				return new BillingCycleMapper().mapToSchema(getBillingInquiryReferenceFacade().getBillCycles());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getBillingCycle(int)
	 */
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0013")
	public BillingCycle getBillingCycle(final int billingCycleCode)
			throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<BillingCycle>() {
			
			@Override
			public BillingCycle doInInvocationCallback(ServiceInvocationContext context) throws Throwable {	
				return new BillingCycleMapper().mapToSchema(getBillingInquiryReferenceFacade().getBillCycle(Integer.toString(billingCycleCode)));
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.reference.ReferenceService#enumerateRuntimeResources(java.util.Map)
	 */
	protected Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("BillingInquiryReferenceFacade", getBillingInquiryReferenceFacade());
		return super.enumerateRuntimeResources(resources);
	}
}
