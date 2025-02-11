package com.telus.cmb.jws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.cmb.common.util.ArrayUtil;
import com.telus.cmb.jws.GetServiceRelationResponse.OriginalServiceCodeList;
import com.telus.cmb.jws.ServiceCodeAndExpiry.ServiceCodeRelationList;
import com.telus.cmb.jws.mapping.BrandMapper;
import com.telus.cmb.jws.mapping.CommitmentReasonMapper;
import com.telus.cmb.jws.mapping.DataSharingGroupMapper;
import com.telus.cmb.jws.mapping.FeatureMapper;
import com.telus.cmb.jws.mapping.PeriodMapper;
import com.telus.cmb.jws.mapping.PoolingGroupMapper;
import com.telus.cmb.jws.mapping.PricePlanMapper;
import com.telus.cmb.jws.mapping.ServiceAirTimeAllocationMapper;
import com.telus.cmb.jws.mapping.ServiceExclusionGroupsMapper;
import com.telus.cmb.jws.mapping.ServiceFeatureClassificationMapper;
import com.telus.cmb.jws.mapping.ServiceMapper;
import com.telus.cmb.jws.mapping.ServicePolicyMapper;
import com.telus.cmb.jws.mapping.ServiceRelationMapper;
import com.telus.cmb.jws.mapping.ServiceTermMapper;
import com.telus.cmb.jws.mapping.SpecialNumberMapper;
import com.telus.cmb.jws.mapping.SpecialNumberRangeMapper;
import com.telus.eas.utility.info.DataSharingGroupInfo;
import com.telus.eas.utility.info.MigrationTypeInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
import com.telus.eas.utility.info.ServiceAndRelationInfo;
import com.telus.eas.utility.info.ServiceFeatureClassificationInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServicePeriodInfo;
import com.telus.eas.utility.info.ServiceRelationInfo;
import com.telus.eas.utility.info.SpecialNumberInfo;
import com.telus.eas.utility.info.SpecialNumberRangeInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BusinessRole;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.Privilege;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.Description;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.MultilingualDescriptionList;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.Brand;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.CommitmentReason;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.DataSharingGroup;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.Feature;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.Period;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.PoolingGroup;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.PricePlan;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.PricePlanSummary;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.Service;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.ServiceAirTimeAllocation;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.ServiceExclusionGroups;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.ServiceFeatureClassification;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.ServicePolicy;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.ServiceTerm;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.SpecialNumber;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.SpecialNumberRange;

@WebService(
		portName = "ServiceOrderReferenceServicePort", 
		serviceName = "ServiceOrderReferenceService_v4_4", 
		targetNamespace = "http://telus.com/wsdl/SMO/OrderMgmt/ServiceOrderReferenceService_4",
		wsdlLocation = "/wsdls/ServiceOrderReferenceService_v4_4.wsdl", 
		endpointInterface = "com.telus.cmb.jws.ServiceOrderReferenceServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class ServiceOrderReferenceService_44 extends BaseService implements ServiceOrderReferenceServicePort {

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#checkServiceAssociation(java.lang.String, java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0001")
	public boolean checkServiceAssociation(final String serviceCode, final String pricePlanCode) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Boolean>() {

			@Override
			public Boolean doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return getServiceOrderReferenceFacade().checkServiceAssociation(serviceCode, pricePlanCode);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#checkServicePrivilege(java.util.List, com.telus.schemas.eca.common_types_2_1.BusinessRole, com.telus.schemas.eca.common_types_2_1.Privilege)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0002")
	public List<ServicePolicy> checkServicePrivilege(final List<String> serviceCodes, final BusinessRole businessRole, final Privilege privilege) throws PolicyException, ServiceException {
		
		assertValid("businessRole", businessRole, BusinessRole.values());
		assertValid("privilege", privilege, Privilege.values());
		
		return execute(new ServiceInvocationCallback<List<ServicePolicy>>() {
			
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
	@ServiceBusinessOperation(errorCode="CMB_SORS_0003")
	public Brand getBrandByBrandId(final Long brandId) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Brand>() {
			
			@Override
			public Brand doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new BrandMapper().mapToSchema(getServiceOrderReferenceFacade().getBrandByBrandId(brandId.intValue()));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getBrands()
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0004")
	public List<Brand> getBrandList() throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Brand>>() {
			
			@Override
			public List<Brand> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new BrandMapper().mapToSchema(getServiceOrderReferenceFacade().getBrands());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getCommitmentReason(java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0005")
	public CommitmentReason getCommitmentReason(final String commitmentReasonCode) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<CommitmentReason>() {
			
			@Override
			public CommitmentReason doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new CommitmentReasonMapper().mapToSchema(getServiceOrderReferenceFacade().getCommitmentReason(commitmentReasonCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getCommitmentReasons()
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0006")
	public List<CommitmentReason> getCommitmentReasonList() throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<CommitmentReason>>() {
			
			@Override
			public List<CommitmentReason> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new CommitmentReasonMapper().mapToSchema(getServiceOrderReferenceFacade().getCommitmentReasons());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getFeature(java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0007")
	public Feature getFeature(final String featureCode) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Feature>() {
			
			@Override
			public Feature doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new FeatureMapper().mapToSchema(getServiceOrderReferenceFacade().getFeature(featureCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getFeatures()
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0008")
	public List<Feature> getFeatureList() throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Feature>>() {
			
			@Override
			public List<Feature> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new FeatureMapper().mapToSchema(getServiceOrderReferenceFacade().getFeatures());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getLogicalDate()
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0009")
	public Date getLogicalDate() throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Date>() {
			
			@Override
			public Date doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return getServiceOrderReferenceFacade().getLogicalDate();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getPoolingGroup(java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0010")
	public PoolingGroup getPoolingGroup(final String poolingGroupCode) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<PoolingGroup>() {
			
			@Override
			public PoolingGroup doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PoolingGroupMapper().mapToSchema(getServiceOrderReferenceFacade().getPoolingGroup(poolingGroupCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getPoolingGroups()
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0011")
	public List<PoolingGroup> getPoolingGroupList() throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<PoolingGroup>>() {
			
			@Override
			public List<PoolingGroup> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PoolingGroupMapper().mapToSchema(getServiceOrderReferenceFacade().getPoolingGroups());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getPricePlan(java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0012")
	public PricePlanSummary getPricePlan(final String pricePlanCode) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<PricePlan>() {
			
			@Override
			public PricePlan doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new PricePlanMapper().mapToSchema(getServiceOrderReferenceFacade().getPricePlan(pricePlanCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getPricePlanByEquipmentAndAccountInfo(java.lang.String, java.lang.String, java.lang.String, com.telus.schemas.eca.common_types_2_1.ProvinceCode, java.lang.String, java.lang.String, int)
	 */
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

		return execute(new ServiceInvocationCallback<PricePlan>() {
			
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
	@ServiceBusinessOperation(errorCode="CMB_SORS_0014")
	public List<PricePlanSummary> getPricePlanList(
			final  String productType,
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
		
		return execute(new ServiceInvocationCallback<List<PricePlanSummary>>() {
			
			@Override
			public List<PricePlanSummary> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				List<PricePlan> pricePlans = new PricePlanMapper().mapToSchema(getServiceOrderReferenceFacade().getPricePlans(
						productType, equipmentType, provinceCode.value(), accountType, accountSubType, brandId.intValue(), 
						ArrayUtil.unboxLong(productPromoType), initialActivationInd, currentPlansOnlyInd, availableForActivationOnlyInd, 
						Integer.valueOf(term).intValue(), activityCode, activityReasonCode, networkType, null));
				
				return new ArrayList<PricePlanSummary>(pricePlans);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getPricePlanSummariesByPricePlanCodes(java.util.List)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0015")
	public List<PricePlanSummary> getPricePlanSummaryListByPricePlanCodeList(final List<String> pricePlanCode) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<PricePlanSummary>>() {
			
			@Override
			public List<PricePlanSummary> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				List<String> newPricePlanCodeList = removeEmptyAndDuplicate(pricePlanCode);
				if (newPricePlanCodeList != null && newPricePlanCodeList.isEmpty() == false) {
					List<PricePlan> pricePlans = new PricePlanMapper().mapToSchema(getServiceOrderReferenceFacade().getPricePlans(newPricePlanCodeList.toArray( new String[0])));
					return new ArrayList<PricePlanSummary>(pricePlans);
				}else {
					return new ArrayList<PricePlanSummary>();
				}
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getRegularService(java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0016")
	public Service getRegularService(final String regularServiceCode) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Service>() {
			
			@Override
			public Service doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceMapper().mapToSchema(getServiceOrderReferenceFacade().getRegularService(regularServiceCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getRegularServices(java.util.List)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0017")
	public List<Service> getRegularServiceList(final List<String> regularServiceCode) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Service>>() {
			
			@Override
			public List<Service> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceMapper().mapToSchema(getServiceOrderReferenceFacade().getRegularServices(regularServiceCode.toArray( new String[0])));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getServiceExclusionGroups()
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0018")
	public List<ServiceExclusionGroups> getServiceExclusionGroupList() throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<ServiceExclusionGroups>>() {
			
			@Override
			public List<ServiceExclusionGroups> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceExclusionGroupsMapper().mapToSchema(getServiceOrderReferenceFacade().getServiceExclusionGroups());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getServicesByFeatureCategory(java.lang.String, java.lang.String, boolean)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0019")
	public List<Service> getServiceListByFeatureCategory(final String featureCategory, final String productType, final boolean current) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Service>>() {
			
			@Override
			public List<Service> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceMapper().mapToSchema(getServiceOrderReferenceFacade().getServicesByFeatureCategory(featureCategory, productType, current));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getWPSService(java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0020")
	public Service getPrepaidService(final String serviceCode) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Service>() {
			
			@Override
			public Service doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceMapper().mapToSchema(getServiceOrderReferenceFacade().getWPSService(serviceCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getWPSServices()
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0021")
	public List<Service> getPrepaidServiceList() throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Service>>() {
			
			@Override
			public List<Service> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceMapper().mapToSchema(getServiceOrderReferenceFacade().getWPSServices());
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.telus.wsdl.smo.ordermgmt.serviceorderreferenceservice_2_0.ServiceOrderReferenceServicePortType#getServiceTerm(java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0022")
	public ServiceTerm getServiceTerm(final String serviceCode) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ServiceTerm>() {
			
			@Override
			public ServiceTerm doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new ServiceTermMapper().mapToSchema(getServiceOrderReferenceFacade().getServiceTerm(serviceCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.ServiceOrderReferenceServicePortType#getPrepaidServiceListByEquipmentAndNetworkType(java.lang.String, java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0023")
	public List<Service> getPrepaidServiceListByEquipmentAndNetworkType(final String equipmentType, final String networkType) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Service>>() {
			
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
	protected Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("ServiceOrderReferenceFacade", getServiceOrderReferenceFacade());
		resources.put("ReferenceDataFacade", getReferenceDataReferenceFacade());
		return super.enumerateRuntimeResources(resources);
	}
	
	/*
	 * Returns a list of all possible data sharing groups configured in the system.
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0024")
	public List<DataSharingGroup> getDataSharingGroupList()
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<DataSharingGroup>>() {
			@Override
			public List<DataSharingGroup> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new DataSharingGroupMapper().mapToSchema(getReferenceDataReferenceFacade().getDataSharingGroups());
			}
		});
	}

	/*
	 * Returns a specific data sharing group given the code.  If there is no matching
	 * data sharing group code, this method will throw a PolicyException.
	 *
	 * @param code - data sharing group code
	 * @throws PolicyException - when no data sharing group matches the input code
	 */
	@ServiceBusinessOperation(errorCode="CMB_SORS_0025")
	public DataSharingGroup getDataSharingGroup(final String dataSharingGroupCode)
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<DataSharingGroup>() {
			@Override
			public DataSharingGroup doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				DataSharingGroupInfo dataSharingGroupInfo = getReferenceDataReferenceFacade().getDataSharingGroup(dataSharingGroupCode);
				if (dataSharingGroupInfo == null)
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNKNOWN, ServiceErrorCodes.ERROR_DESC_INPUT+"["+dataSharingGroupCode+"]");
				return new DataSharingGroupMapper().mapToSchema(dataSharingGroupInfo);				
			}
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0026")
	public List<ServiceAirTimeAllocation> getAirTimeAllocationList(	final List<String> serviceCode, final Date effectiveRetrievalDate) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<ServiceAirTimeAllocation>>() {
			@Override
			public List<ServiceAirTimeAllocation> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				ServiceAirTimeAllocationInfo[] result = getServiceOrderReferenceFacade().getVoiceAllocation(serviceCode.toArray(new String[0]), effectiveRetrievalDate, context.getSessionId(getServiceOrderReferenceFacade()));
				if (result == null)
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNKNOWN, ServiceErrorCodes.ERROR_DESC_INPUT+"["+serviceCode+"]");
				return new ServiceAirTimeAllocationMapper().mapToSchema(result);				
			}
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0027")
	public List<ServiceAirTimeAllocation> getCalculatedEffectiveAirTimeAllocationList(final List<String> serviceCode, final Date effectiveDate)	throws PolicyException, ServiceException {
		/*return execute( new ServiceInvocationCallback<List<ServiceAirTimeAllocation>>() {
			@Override
			public List<ServiceAirTimeAllocation> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				ServiceAirTimeAllocationInfo[] result = getServiceOrderReferenceFacade().getCalculatedEffectedVoiceAllocation( serviceCode.toArray(new String[0]), effectiveDate, context.getSessionId(getServiceOrderReferenceFacade()));
				if (result == null)
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNKNOWN, ServiceErrorCodes.ERROR_DESC_INPUT+"["+serviceCode+"]");
				return new ServiceAirTimeAllocationMapper().mapToSchema(result);				
			}
		});*/
		throw new ServicePolicyException(ServiceErrorCodes.ERROR_OPERATION_NO_LONGER_IN_SERVICE, ServiceErrorCodes.ERROR__DESC_OPERATION_NO_LONGER_IN_SERVICE);
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0028")
	public List<Period> getPeriodList(final String serviceCode)throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Period>>() {
			@Override
			public List<Period> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				ServicePeriodInfo[] periods = getServiceOrderReferenceFacade().getServicePeriodInfo(serviceCode);
				if (periods == null)
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNKNOWN, ServiceErrorCodes.ERROR_DESC_INPUT+"["+serviceCode+"]");
				return new PeriodMapper().mapToSchema(periods);				
			}
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0029")
	public ServiceFeatureClassification getServiceFeatureClassification(final String classificationCode)	throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<ServiceFeatureClassification>() {
			@Override
			public ServiceFeatureClassification doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				ServiceFeatureClassificationInfo classification = getServiceOrderReferenceFacade().getServiceFeatureClassification(classificationCode);
				if (classification == null)
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNKNOWN, ServiceErrorCodes.ERROR_DESC_INPUT+"["+classificationCode+"]");
				return new ServiceFeatureClassificationMapper().mapToSchema(classification);				
			}
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0030")
	public SpecialNumber getSpecialNumber(final String phoneNumber)throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<SpecialNumber>() {
			@Override
			public SpecialNumber doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SpecialNumberInfo specialNumberInfo = getServiceOrderReferenceFacade().getSpecialNumber(phoneNumber);
				if (specialNumberInfo == null)
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNKNOWN, "["+phoneNumber+"] " + ServiceErrorCodes.ERROR_DESC_INVALID_INPUT);
				return new SpecialNumberMapper().mapToSchema(specialNumberInfo);				
			}
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0031")
	public List<SpecialNumber> getSpecialNumberList() throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<SpecialNumber>>() {
			@Override
			public List<SpecialNumber> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				List<SpecialNumber> specialNumbers=  new ArrayList<SpecialNumber>(); 
				SpecialNumberInfo[] numberInfos = getServiceOrderReferenceFacade().getSpecialNumbers();
				if (numberInfos != null && numberInfos.length > 0) {
					for (SpecialNumberInfo numberInfo: numberInfos) {
						specialNumbers.add(new SpecialNumberMapper().mapToSchema(numberInfo));
					}
				}
				return specialNumbers;				
			}
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0032")
	public SpecialNumberRange getSpecialNumberRange(final String phoneNumber)throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<SpecialNumberRange>() {
			@Override
			public SpecialNumberRange doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SpecialNumberRangeInfo specialNumberRangeInfo = getServiceOrderReferenceFacade().getSpecialNumberRange(phoneNumber);
				if(specialNumberRangeInfo == null)
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNKNOWN, "["+phoneNumber+"] "+ServiceErrorCodes.ERROR_DESC_INVALID_INPUT);
				return new SpecialNumberRangeMapper().mapToSchema(specialNumberRangeInfo);				
			}
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0033")
	public List<SpecialNumberRange> getSpecialNumberRangeList()throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<SpecialNumberRange>>() {
			@Override
			public List<SpecialNumberRange> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				List<SpecialNumberRange> specialNumberRanges=  new ArrayList<SpecialNumberRange>(); 
				SpecialNumberRangeInfo[] rangeInfos = getServiceOrderReferenceFacade().getSpecialNumberRanges();
				if (rangeInfos != null && rangeInfos.length > 0) {
					for (SpecialNumberRangeInfo rangeInfo: rangeInfos) {
						specialNumberRanges.add(new SpecialNumberRangeMapper().mapToSchema(rangeInfo));
					}
				}
				return specialNumberRanges;				
			}
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0034")
	public List<EquivalentServiceList> getEquivalentService(final AuditInfo auditInfo, final List<String> originalServiceCodeList, final List<String> destinationServiceCodeList, final String networkType) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<EquivalentServiceList>>() {
			@Override
			public List<EquivalentServiceList> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				List<EquivalentServiceList> equivalentList = new ArrayList<EquivalentServiceList> ();
				@SuppressWarnings("unchecked")
				Map<String, ServiceInfo> serviceMap = getServiceOrderReferenceFacade().getEquivalentServiceByServiceCodeList(originalServiceCodeList.toArray(new String[0]), destinationServiceCodeList.toArray(new String[0]), networkType);
				for (String originalServiceCode : originalServiceCodeList) {
					EquivalentServiceList equivalientService = new EquivalentServiceList();
					equivalientService.setOriginalServiceCode(originalServiceCode);
					ServiceInfo serviceInfo = serviceMap.get(originalServiceCode);
					if (serviceInfo != null) {
						equivalientService.setService(new ServiceMapper().mapToSchema(serviceInfo));
					}
					equivalentList.add(equivalientService);
				}
				return equivalentList;
			}
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0035")
	public List<OriginalServiceCodeList> getServiceRelation(final AuditInfo auditInfo, final List<ServiceRelationCode> serviceRelationCodeList) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<OriginalServiceCodeList>>() {
			@Override
			public List<OriginalServiceCodeList> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				List<OriginalServiceCodeList> output = new ArrayList<OriginalServiceCodeList>();
				List<ServiceRelationInfo> serviceRelations = new ServiceRelationMapper().mapToDomain(serviceRelationCodeList);
				@SuppressWarnings("unchecked")
				Map<String, List<ServiceAndRelationInfo>> serviceRelationMap = getServiceOrderReferenceFacade().getServiceAndRelationList(serviceRelations.toArray(new ServiceRelationInfo[0]));
				for (String serviceCode : serviceRelationMap.keySet()) {
					OriginalServiceCodeList outputItem = new OriginalServiceCodeList();
					outputItem.setOriginalServiceCode(serviceCode);
					List<ServiceCodeRelationList> outputItemList = outputItem.getServiceCodeRelationList();
					for (ServiceAndRelationInfo serviceAndRelation : serviceRelationMap.get(serviceCode)) {
						ServiceCodeRelationList outputServiceAndRelation = new ServiceCodeRelationList();
						outputServiceAndRelation.setServiceRelationExpirationDate(serviceAndRelation.getServiceRelation().getExpirationDate());
						outputServiceAndRelation.setService(new ServiceMapper().mapToSchema(serviceAndRelation.getService()));
						outputItemList.add(outputServiceAndRelation);
					}
					output.add(outputItem);
				}
				return output;
			}
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0036")
	public List<ServiceGroupCodeList> getServiceListByServiceGroup(final AuditInfo auditInfo, final List<String> serviceGroupCodeList) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<ServiceGroupCodeList>>() {
			@Override
			public List<ServiceGroupCodeList> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				List<ServiceGroupCodeList> output = new ArrayList<ServiceGroupCodeList> ();
				@SuppressWarnings("unchecked")
				Map<String, List<ServiceInfo>> serviceGroupMap = getServiceOrderReferenceFacade().getServiceListByGroupList(serviceGroupCodeList.toArray(new String[0]));
				for (String serviceGroupCode : serviceGroupMap.keySet()) {
					ServiceGroupCodeList serviceGroup = new ServiceGroupCodeList();
					serviceGroup.setServiceGroupCode(serviceGroupCode);
					serviceGroup.getServiceList().addAll(new ServiceMapper().mapToSchema(serviceGroupMap.get(serviceGroupCode)));
					output.add(serviceGroup);
				}
				return output;
			}
		});
	}
	
	@ServiceBusinessOperation(errorCode="CMB_SORS_0037")
	public List<Service> getIncludedPromotionService(final AuditInfo auditInfo, final String pricePlanCode, final String equipmentType, final String networkType,
			final String provinceCode, final String contractTerm, final ServiceCodeBusinessRolePriviledge serviceCodeBusinessAndPriviledgeCode) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Service>>() {
			@Override
			public List<Service> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				ServiceInfo[] promotionServices = getReferenceDataHelper(context).retrieveIncludedPromotions(pricePlanCode, equipmentType, networkType, provinceCode,	Integer.valueOf(contractTerm).intValue());
				if (promotionServices.length > 0 && serviceCodeBusinessAndPriviledgeCode != null) {
					promotionServices = getReferenceDataReferenceFacade().filterByPrivilege(promotionServices, 
						serviceCodeBusinessAndPriviledgeCode.getBusinessRoleCode().value(),
						serviceCodeBusinessAndPriviledgeCode.getPrivilegeCode().value(),
						serviceCodeBusinessAndPriviledgeCode.isServiceAvailableInd());
				}
				return new ServiceMapper().mapToSchema(promotionServices);
			}
		});
	}
	
	@ServiceBusinessOperation(errorCode="CMB_SORS_0038")
	public List<AlternateRecurringCharge> getAlternateRecurringCharge(final AuditInfo auditInfo, final List<String> serviceCodeList, final String provinceCode, final String npaNxx, final String corporateId)
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<AlternateRecurringCharge>>() {
			@Override
			public List<AlternateRecurringCharge> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				List<AlternateRecurringCharge> output = new ArrayList<AlternateRecurringCharge>();
				@SuppressWarnings("unchecked")
				Map<String, Double> chargeMap = getServiceOrderReferenceFacade().getAlternateRecurringCharge(serviceCodeList.toArray(new String[0]), provinceCode, npaNxx, corporateId);
				for (String serviceCode : chargeMap.keySet()) {
					AlternateRecurringCharge outputItem = new AlternateRecurringCharge();
					outputItem.setServiceCode(serviceCode);
					outputItem.setAlternateRecurringCharge(chargeMap.get(serviceCode));
					output.add(outputItem);
				}
				return output;
			}
		});
	}
	
	@ServiceBusinessOperation(errorCode="CMB_SORS_0039")
	public PricePlan getPricePlanByEquipmentAndAccountByBusinessRole(			
			final AuditInfo auditInfo,
			final String productType,
			final String pricePlanCode,
			final String equipmentType,
			final ProvinceCode provinceCode,
			final String accountType,
			final String accountSubType,
			final Long brandId,
			final ServiceCodeBusinessRolePriviledge serviceCodeBusinessAndPriviledgeCode)
			throws PolicyException, ServiceException {
		
		assertValid("provinceCode", provinceCode, ProvinceCode.values());

		return execute(new ServiceInvocationCallback<PricePlan>() {
			@Override
			public PricePlan doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				PricePlanInfo pricePlan = getServiceOrderReferenceFacade().getPricePlan(productType, pricePlanCode, equipmentType, provinceCode.value(), accountType, accountSubType,	brandId.intValue());
				if (serviceCodeBusinessAndPriviledgeCode != null) {
					pricePlan.setOptionalServices(getReferenceDataReferenceFacade().filterByPrivilege(pricePlan.getOptionalServices0(), 
						serviceCodeBusinessAndPriviledgeCode.getBusinessRoleCode().value(),
						serviceCodeBusinessAndPriviledgeCode.getPrivilegeCode().value(),
						serviceCodeBusinessAndPriviledgeCode.isServiceAvailableInd()));
				}
				return new PricePlanMapper().mapToSchema(pricePlan);
			}
		});
	}
	
	@ServiceBusinessOperation(errorCode="CMB_SORS_0040")
	public List<PricePlanServiceCodeList> getPricePlanAndServiceCodeListByGroupCode(
			final List<String> groupCodeList) throws PolicyException,
			ServiceException {
		return execute(new ServiceInvocationCallback<List<PricePlanServiceCodeList>>() {
			@Override
			public List<PricePlanServiceCodeList> doInInvocationCallback(
					ServiceInvocationContext context) throws Throwable {

				List<PricePlanServiceCodeList> output = new ArrayList<PricePlanServiceCodeList>();
				
				@SuppressWarnings("unchecked")
				Map<String, List<String>> serviceCodeListMap = getServiceOrderReferenceFacade().getServiceCodeListByGroupList(groupCodeList.toArray(new String[0]));
				
				for (String pricePlanServiceCode : serviceCodeListMap.keySet()) {
					PricePlanServiceCodeList pricePlanServiceCodeList = new PricePlanServiceCodeList();
					pricePlanServiceCodeList.setGroupCode(pricePlanServiceCode);
					pricePlanServiceCodeList.getPricePlanServiceCodeList().addAll(serviceCodeListMap.get(pricePlanServiceCode));
					output.add(pricePlanServiceCodeList);
				}
				
				return output;
			}
			
		});
	}

	@ServiceBusinessOperation(errorCode="CMB_SORS_0041")
	public List<MigrationType> getMigrationTypeList() throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<MigrationType>>() {
			@Override
			public List<MigrationType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				MigrationTypeInfo[] migrationTypes = getReferenceDataFacade(context).getMigrationTypes();
				List<MigrationType> migrationList = new ArrayList<MigrationType>();
				for (MigrationTypeInfo migrationTypeInfo : migrationTypes) {
					MigrationType migrationType1 = new MigrationType();
					migrationType1.setMigrationTypeCode(migrationTypeInfo.getCode());
					Description descriptionEn = new Description();
					Description descriptionFr = new Description();
					descriptionEn.setLocale("EN");
					descriptionEn.setDescriptionText(migrationTypeInfo.getDescription());
					descriptionFr.setLocale("FR");
					descriptionFr.setDescriptionText(migrationTypeInfo.getDescriptionFrench());
					MultilingualDescriptionList multiLan = new MultilingualDescriptionList();
					multiLan.getDescription().add(descriptionEn);
					multiLan.getDescription().add(descriptionFr);
					migrationType1.setMigrationTypeDesc(multiLan);
					migrationList.add(migrationType1);
				}
				
				return migrationList;
			}
		});
	}
	
	private List<String> removeEmptyAndDuplicate(List<String> list) {
		if (list != null) {
			List<String> newList = new ArrayList<String>();
			for (String str : list) {
				if (str != null && str.trim().isEmpty() == false && newList.contains(str) == false) {
					newList.add(str);
				}
			}
			
			return newList;
		}
		
		return null;
	}

}
