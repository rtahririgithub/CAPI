/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.cmb.jws.mapping.reference.billing_enquiry_1_1.AdjustmentReasonMapper;
import com.telus.cmb.jws.mapping.reference.billing_enquiry_1_1.ChargeTypeMapper;
import com.telus.cmb.jws.mapping.reference.billing_enquiry_1_1.PaymentMethodMapper;
import com.telus.cmb.jws.mapping.reference.billing_enquiry_1_1.PaymentSourceTypeMapper;
import com.telus.cmb.jws.mapping.reference.billing_enquiry_1_1.PrepaidRechargeDenominationMapper;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_reference_types_1_1.AdjustmentReason;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_reference_types_1_1.ChargeType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_reference_types_1_1.PaymentSourceType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_reference_types_1_1.PrepaidRechargeDenomination;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_reference_types_1_1.RefPaymentMethod;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode;

/**
 * @author Pavel Simonovsky (ver 1.0)
 * @author Gregory Bragg (ver 1.1)
 */

@WebService(
		portName = "BillingInquiryReferenceServicePort", 
		serviceName = "BillingInquiryReferenceService_v1_1", 
		targetNamespace = "http://telus.com/wsdl/CMO/BillingInquiryMgmt/BillingInquiryReferenceService_1", 
		wsdlLocation = "/wsdls/BillingInquiryReferenceService_v1_1.wsdl", 
		endpointInterface = "com.telus.cmb.jws.BillingInquiryReferenceServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class BillingInquiryReferenceService_1_1 extends BaseService implements BillingInquiryReferenceServicePort {

	public BillingInquiryReferenceService_1_1() {
		super( new BillingInquiryReferenceExceptionTranslator());
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getPaymentMethods()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0001")
	public List<RefPaymentMethod> getPaymentMethods() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<RefPaymentMethod>>() {
			
			@Override
			public List<RefPaymentMethod> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PaymentMethodMapper().mapToSchema(getBillingInquiryReferenceFacade().getPaymentMethods());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getPaymentSourceTypes()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0002")
	public List<PaymentSourceType> getPaymentSourceTypes() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<PaymentSourceType>>() {
			
			@Override
			public List<PaymentSourceType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PaymentSourceTypeMapper().mapToSchema(getBillingInquiryReferenceFacade().getPaymentSourceTypes());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getPrepaidRechargeDenominations()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0003")
	public List<PrepaidRechargeDenomination> getPrepaidRechargeDenominations() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<PrepaidRechargeDenomination>>() {
			
			@Override
			public List<PrepaidRechargeDenomination> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PrepaidRechargeDenominationMapper().mapToSchema(getBillingInquiryReferenceFacade().getPrepaidRechargeDenominations());
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getAdjustmentReasons()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0004")
	public List<AdjustmentReason> getAdjustmentReasons() throws PolicyException, ServiceException {
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
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0005")
	public AdjustmentReason getAdjustmentReason(final String code) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<AdjustmentReason>() {
			
			@Override
			public AdjustmentReason doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new AdjustmentReasonMapper().mapToSchema(getBillingInquiryReferenceFacade().getAdjustmentReason(code));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingInquiryReferenceServicePortType#getManualChargeTypes()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BIRS_0006")
	public List<ChargeType> getManualChargeTypes() throws PolicyException, ServiceException {
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
	@Override
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
	@Override
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

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.reference.ReferenceService#enumerateRuntimeResources(java.util.Map)
	 */
	@Override
	protected Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("BillingInquiryReferenceFacade", getBillingInquiryReferenceFacade());
		return super.enumerateRuntimeResources(resources);
	}

}
