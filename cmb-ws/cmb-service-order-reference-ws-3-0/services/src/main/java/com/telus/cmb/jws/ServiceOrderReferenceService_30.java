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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.cmb.common.util.ArrayUtil;
import com.telus.cmb.jws.mapping.BrandMapper;
import com.telus.cmb.jws.mapping.CommitmentReasonMapper;
import com.telus.cmb.jws.mapping.FeatureMapper;
import com.telus.cmb.jws.mapping.PoolingGroupMapper;
import com.telus.cmb.jws.mapping.PricePlanMapper;
import com.telus.cmb.jws.mapping.ServiceExclusionGroupsMapper;
import com.telus.cmb.jws.mapping.ServiceMapper;
import com.telus.cmb.jws.mapping.ServicePolicyMapper;
import com.telus.cmb.jws.mapping.ServiceTermMapper;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BusinessRole;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Privilege;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.Brand;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.CommitmentReason;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.Feature;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.PoolingGroup;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.PricePlan;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.PricePlanSummary;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.Service;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.ServiceExclusionGroups;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.ServicePolicy;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.ServiceTerm;

/**
 * @author Tsz Chung Tong
 *
 */

@WebService(
		portName = "ServiceOrderReferenceServicePort", 
		serviceName = "ServiceOrderReferenceService_v3_0", 
		targetNamespace = "http://telus.com/wsdl/SMO/OrderMgmt/ServiceOrderReferenceService_3",
		wsdlLocation = "/wsdls/ServiceOrderReferenceService_v3_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.ServiceOrderReferenceServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class ServiceOrderReferenceService_30 extends BaseService implements ServiceOrderReferenceServicePort {

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#checkServiceAssociation(java.lang.String, java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0001")
	public boolean checkServiceAssociation(final String serviceCode, final String pricePlanCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<Boolean>() {

			@Override
			public Boolean doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return getServiceOrderReferenceFacade().checkServiceAssociation(serviceCode, pricePlanCode);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#checkServicePrivilege(java.util.List, com.telus.schemas.eca.common_types_2_1.BusinessRole, com.telus.schemas.eca.common_types_2_1.Privilege)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0002")
	public List<ServicePolicy> checkServicePrivilege(final List<String> serviceCodes, final BusinessRole businessRole, final Privilege privilege) throws PolicyException, ServiceException {
		
		assertValid("businessRole", businessRole, BusinessRole.values());
		assertValid("privilege", privilege, Privilege.values());
		
		return execute( new ServiceInvocationCallback<List<ServicePolicy>>() {
			
			@Override
			public List<ServicePolicy> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				return new ServicePolicyMapper().mapToSchema(getServiceOrderReferenceFacade().checkServicePrivilege(
						serviceCodes.toArray( new String[0]), businessRole.value(), privilege.value()));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getBrandByBrandId(int)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0003")
	public Brand getBrandByBrandId(final Long brandId) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<Brand>() {
			
			@Override
			public Brand doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new BrandMapper().mapToSchema(getServiceOrderReferenceFacade().getBrandByBrandId(brandId.intValue()));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getBrands()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0004")
	public List<Brand> getBrandList() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<Brand>>() {
			
			@Override
			public List<Brand> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new BrandMapper().mapToSchema(getServiceOrderReferenceFacade().getBrands());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getCommitmentReason(java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0005")
	public CommitmentReason getCommitmentReason(final String commitmentReasonCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<CommitmentReason>() {
			
			@Override
			public CommitmentReason doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new CommitmentReasonMapper().mapToSchema(getServiceOrderReferenceFacade().getCommitmentReason(commitmentReasonCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getCommitmentReasons()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0006")
	public List<CommitmentReason> getCommitmentReasonList() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<CommitmentReason>>() {
			
			@Override
			public List<CommitmentReason> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new CommitmentReasonMapper().mapToSchema(getServiceOrderReferenceFacade().getCommitmentReasons());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getFeature(java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0007")
	public Feature getFeature(final String featureCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<Feature>() {
			
			@Override
			public Feature doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new FeatureMapper().mapToSchema(getServiceOrderReferenceFacade().getFeature(featureCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getFeatures()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0008")
	public List<Feature> getFeatureList() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<Feature>>() {
			
			@Override
			public List<Feature> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new FeatureMapper().mapToSchema(getServiceOrderReferenceFacade().getFeatures());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getLogicalDate()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0009")
	public Date getLogicalDate() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<Date>() {
			
			@Override
			public Date doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return getServiceOrderReferenceFacade().getLogicalDate();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getPoolingGroup(java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0010")
	public PoolingGroup getPoolingGroup(final String poolingGroupCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<PoolingGroup>() {
			
			@Override
			public PoolingGroup doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PoolingGroupMapper().mapToSchema(getServiceOrderReferenceFacade().getPoolingGroup(poolingGroupCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getPoolingGroups()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0011")
	public List<PoolingGroup> getPoolingGroupList() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<PoolingGroup>>() {
			
			@Override
			public List<PoolingGroup> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PoolingGroupMapper().mapToSchema(getServiceOrderReferenceFacade().getPoolingGroups());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getPricePlan(java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0012")
	public PricePlanSummary getPricePlan(final String pricePlanCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<PricePlan>() {
			
			@Override
			public PricePlan doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PricePlanMapper().mapToSchema(getServiceOrderReferenceFacade().getPricePlan(pricePlanCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getPricePlanByEquipmentAndAccountInfo(java.lang.String, java.lang.String, java.lang.String, com.telus.schemas.eca.common_types_2_1.ProvinceCode, java.lang.String, java.lang.String, int)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0013")
	public PricePlan getPricePlanByEquipmentAndAccountInfo(			
			final String productType,
			final String pricePlanCode,
			final String equipmentType,
			final ProvinceCode provinceCode,
			final String accountType,
			final String accountSubType,
			final Long brandId) throws PolicyException, ServiceException {
		
		assertValid("provinceCode", provinceCode, ProvinceCode.values());

		return execute( new ServiceInvocationCallback<PricePlan>() {
			
			@Override
			public PricePlan doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PricePlanMapper().mapToSchema(getServiceOrderReferenceFacade().getPricePlan(productType, pricePlanCode, 
						equipmentType, provinceCode.value(), accountType, accountSubType, brandId.intValue()));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getPricePlans(java.lang.String, java.lang.String, com.telus.schemas.eca.common_types_2_1.ProvinceCode, java.lang.String, java.lang.String, int, java.lang.String, boolean, boolean, java.lang.String, int, java.lang.String, boolean, java.util.List)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0014")
	public List<PricePlanSummary> getPricePlanList(
			final String productType,
			final String equipmentType,
			final ProvinceCode provinceCode,
			final String accountType,
			final String accountSubType,
			final Long brandId,
			final String activityCode,
			final boolean currentPlansOnlyInd,
			final boolean availableForActivationOnlyInd,
			final String activityReasonCode,
			final String term,
			final String networkType,
			final boolean initialActivationInd,
			final List<Long> productPromoType) throws PolicyException, ServiceException {

		assertValid("provinceCode", provinceCode, ProvinceCode.values());

		return execute( new ServiceInvocationCallback<List<PricePlanSummary>>() {
			
			@Override
			public List<PricePlanSummary> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				List<PricePlan> pricePlans = new PricePlanMapper().mapToSchema(getServiceOrderReferenceFacade().getPricePlans(
						productType, equipmentType, provinceCode.value(), accountType, accountSubType, brandId.intValue(), 
						ArrayUtil.unboxLong(productPromoType), initialActivationInd, currentPlansOnlyInd, availableForActivationOnlyInd, 
						Integer.valueOf(term).intValue(), activityCode, activityReasonCode, networkType,null));
				
				return new ArrayList<PricePlanSummary>(pricePlans);
			}
		});
	}


	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getPricePlanSummariesByPricePlanCodes(java.util.List)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0015")
	public List<PricePlanSummary> getPricePlanSummaryListByPricePlanCodeList(final List<String> pricePlanCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<PricePlanSummary>>() {
			
			@Override
			public List<PricePlanSummary> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				List<PricePlan> pricePlans = new PricePlanMapper().mapToSchema(getServiceOrderReferenceFacade().getPricePlans(pricePlanCode.toArray( new String[0])));
				return new ArrayList<PricePlanSummary>(pricePlans);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getRegularService(java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0016")
	public Service getRegularService(final String regularServiceCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<Service>() {
			
			@Override
			public Service doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceMapper().mapToSchema(getServiceOrderReferenceFacade().getRegularService(regularServiceCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getRegularServices(java.util.List)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0017")
	public List<Service> getRegularServiceList(final List<String> regularServiceCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<Service>>() {
			
			@Override
			public List<Service> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceMapper().mapToSchema(getServiceOrderReferenceFacade().getRegularServices(regularServiceCode.toArray( new String[0])));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getServiceExclusionGroups()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0018")
	public List<ServiceExclusionGroups> getServiceExclusionGroupList() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<ServiceExclusionGroups>>() {
			
			@Override
			public List<ServiceExclusionGroups> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceExclusionGroupsMapper().mapToSchema(getServiceOrderReferenceFacade().getServiceExclusionGroups());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getServicesByFeatureCategory(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0019")
	public List<Service> getServiceListByFeatureCategory(final String featureCategory, final String productType, final boolean current) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<Service>>() {
			
			@Override
			public List<Service> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceMapper().mapToSchema(getServiceOrderReferenceFacade().getServicesByFeatureCategory(featureCategory, productType, current));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getWPSService(java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0020")
	public Service getPrepaidService(final String serviceCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<Service>() {
			
			@Override
			public Service doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceMapper().mapToSchema(getServiceOrderReferenceFacade().getWPSService(serviceCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getWPSServices()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0021")
	public List<Service> getPrepaidServiceList() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<Service>>() {
			
			@Override
			public List<Service> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceMapper().mapToSchema(getServiceOrderReferenceFacade().getWPSServices());
			}
		});
	}

	
	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getServiceTerm(java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0022")
	public ServiceTerm getServiceTerm(final String serviceCode) throws PolicyException, ServiceException {
		
		return execute( new ServiceInvocationCallback<ServiceTerm>() {
			
			@Override
			public ServiceTerm doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceTermMapper().mapToSchema(getServiceOrderReferenceFacade().getServiceTerm(serviceCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.ServiceOrderReferenceServicePortType#getPrepaidServiceListByEquipmentAndNetworkType(java.lang.String, java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SORS_0023")
	public List<Service> getPrepaidServiceListByEquipmentAndNetworkType(final String equipmentType, final String networkType) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<Service>>() {
			
			@Override
			public List<Service> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceMapper().mapToSchema(getServiceOrderReferenceFacade().getPrepaidServiceListByEquipmentAndNetworkType(equipmentType, networkType));
			}
		});
	}


	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.reference.ReferenceService#enumerateRuntimeResources(java.util.Map)
	 */
	@Override
	protected Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("ServiceOrderReferenceFacade", getServiceOrderReferenceFacade());
		return super.enumerateRuntimeResources(resources);
	}

}
