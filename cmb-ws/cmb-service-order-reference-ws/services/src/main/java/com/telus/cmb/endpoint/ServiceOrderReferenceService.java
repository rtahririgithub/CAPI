/*
 *  Copyright (c) 2020 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.telus.api.reference.Service;
import com.telus.cmb.endpoint.mapping.BrandMapper;
import com.telus.cmb.endpoint.mapping.CommitmentReasonMapper;
import com.telus.cmb.endpoint.mapping.DataSharingGroupMapper;
import com.telus.cmb.endpoint.mapping.FeatureMapper;
import com.telus.cmb.endpoint.mapping.PeriodMapper;
import com.telus.cmb.endpoint.mapping.PoolingGroupMapper;
import com.telus.cmb.endpoint.mapping.PricePlanMapper;
import com.telus.cmb.endpoint.mapping.SeatTypeMapper;
import com.telus.cmb.endpoint.mapping.ServiceAirTimeAllocationMapper;
import com.telus.cmb.endpoint.mapping.ServiceExclusionGroupsMapper;
import com.telus.cmb.endpoint.mapping.ServiceExtendedInfoMapper;
import com.telus.cmb.endpoint.mapping.ServiceFeatureClassificationMapper;
import com.telus.cmb.endpoint.mapping.ServiceMapper;
import com.telus.cmb.endpoint.mapping.ServicePolicyMapper;
import com.telus.cmb.endpoint.mapping.ServiceRelationMapper;
import com.telus.cmb.endpoint.mapping.ServiceTermMapper;
import com.telus.cmb.endpoint.mapping.SpecialNumberMapper;
import com.telus.cmb.endpoint.mapping.SpecialNumberRangeMapper;
import com.telus.cmb.endpoint.mapping.WCCServiceExtendedInfoMapper;
import com.telus.cmb.framework.endpoint.EndpointOperation;
import com.telus.cmb.framework.endpoint.EndpointProvider;
import com.telus.cmb.framework.endpoint.ObsoleteOperationException;
import com.telus.cmb.jws.ServiceHelpers;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.schema.*;
import com.telus.cmb.schema.GetServiceRelationResponseType.OriginalServiceCodeList;
import com.telus.cmb.schema.ServiceCodeAndExpiry.ServiceCodeRelationList;
import com.telus.eas.utility.info.MigrationTypeInfo;
import com.telus.eas.utility.info.OfferCriteriaInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.PricePlanSelectionCriteriaInfo;
import com.telus.eas.utility.info.ServiceAndRelationInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServiceRelationInfo;
import com.telus.eas.utility.info.ServiceSetInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.Description;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.MultilingualDescriptionList;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.Brand;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.CommitmentReason;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.OfferCriteria;

/**
 * @author Pavel Simonovsky
 *
 */
@WebService(portName = "ServiceOrderReferenceServicePort", serviceName = "ServiceOrderReferenceService_v5_4", targetNamespace = "http://telus.com/wsdl/SMO/OrderMgmt/ServiceOrderReferenceService_5", wsdlLocation = "/wsdls/ServiceOrderReferenceService_v5_4.wsdl", endpointInterface = "com.telus.cmb.endpoint.ServiceOrderReferencePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class ServiceOrderReferenceService extends EndpointProvider implements ServiceOrderReferencePort {

	@Autowired
	private ReferenceDataFacade facade;

	@Autowired
	private ReferenceDataHelper helper;

	@Override
	public Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("referenceHelper", helper);
		resources.put("referenceFacade", facade);
		return resources;
	}

	private Collection<String> getDistinctValues(Collection<String> entries) {
		Set<String> result = new HashSet<String>();
		if (CollectionUtils.isNotEmpty(entries)) {
			for (String entry : entries) {
				if (StringUtils.isNotEmpty(entry)) {
					result.add(entry);
				}
			}
		}
		return result;
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0001")
	public CheckServiceAssociationResponse checkServiceAssociation(CheckServiceAssociation params) throws EndpointException {
		CheckServiceAssociationResponseType response = new CheckServiceAssociationResponseType();
		response.setAssociatedServiceInd(facade.checkServiceAssociation(params.getServiceCode(), params.getPricePlanCode()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0002")
	public CheckServicePrivilegeResponse checkServicePrivilege(CheckServicePrivilege params) throws EndpointException {
		CheckServicePrivilegeResponseType response = new CheckServicePrivilegeResponseType();
		response.setServicePolicyList(new ServicePolicyMapper().mapToSchema(facade.checkServicePrivilege(params.getServiceCodeList().toArray(new String[0]), params.getBusinessRole().value(), params
				.getPrivilege().value())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0003")
	public GetBrandByBrandIdResponse getBrandByBrandId(GetBrandByBrandId params) throws EndpointException {
		Brand brand = new BrandMapper().mapToSchema(facade.getBrandByBrandId(params.getBrandId().intValue()));
		GetBrandByBrandIdResponseType response = new GetBrandByBrandIdResponseType();
		response.setBrand(brand);
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0004")
	public GetBrandListResponse getBrandList(GetBrandList params) throws EndpointException {
		GetBrandListResponseType response = new GetBrandListResponseType();
		response.setBrandList(new BrandMapper().mapToSchema(facade.getBrands()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0005")
	public GetCommitmentReasonResponse getCommitmentReason(GetCommitmentReason params) throws EndpointException {
		GetCommitmentReasonResponseType response = new GetCommitmentReasonResponseType();
		response.setCommitmentReason(new CommitmentReasonMapper().mapToSchema(facade.getCommitmentReason(params.getCommitmentReasonCode())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0006")
	public GetCommitmentReasonListResponse getCommitmentReasonList(GetCommitmentReasonList params) throws EndpointException {
		GetCommitmentReasonListResponseType response = new GetCommitmentReasonListResponseType();
		List<CommitmentReason> commitmentReasons = new CommitmentReasonMapper().mapToSchema(facade.getCommitmentReasons());
		response.setCommitmentReasonList(commitmentReasons);
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0007")
	public GetFeatureResponse getFeature(GetFeature params) throws EndpointException {
		GetFeatureResponseType response = new GetFeatureResponseType();
		response.setFeature(new FeatureMapper().mapToSchema(facade.getFeature(params.getFeatureCode())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0008")
	public GetFeatureListResponse getFeatureList(GetFeatureList params) throws EndpointException {
		GetFeatureListResponseType response = new GetFeatureListResponseType();
		response.setFeatureList(new FeatureMapper().mapToSchema(facade.getFeatures()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0009")
	public GetLogicalDateResponse getLogicalDate(GetLogicalDate params) throws EndpointException {
		GetLogicalDateResponseType response = new GetLogicalDateResponseType();
		response.setLogicalDate(facade.getLogicalDate());
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0010")
	public GetPoolingGroupResponse getPoolingGroup(GetPoolingGroup params) throws EndpointException {
		GetPoolingGroupResponseType response = new GetPoolingGroupResponseType();
		response.setPoolingGroup(new PoolingGroupMapper().mapToSchema(facade.getPoolingGroup(params.getPoolingGroupCode())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0011")
	public GetPoolingGroupListResponse getPoolingGroupList(GetPoolingGroupList params) throws EndpointException {
		GetPoolingGroupListResponseType response = new GetPoolingGroupListResponseType();
		response.setPoolingGroupList(new PoolingGroupMapper().mapToSchema(facade.getPoolingGroups()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0012")
	public GetPricePlanResponse getPricePlan(GetPricePlan params) throws EndpointException {
		GetPricePlanResponseType response = new GetPricePlanResponseType();
		response.setPricePlanSummary(new PricePlanMapper().mapToSchema(facade.getPricePlan(params.getPricePlanCode())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0013")
	public GetPricePlanByEquipmentAndAccountInfoResponse getPricePlanByEquipmentAndAccountInfo(GetPricePlanByEquipmentAndAccountInfo params) throws EndpointException {
		GetPricePlanByEquipmentAndAccountInfoResponseType response = new GetPricePlanByEquipmentAndAccountInfoResponseType();
		response.setPricePlan(new PricePlanMapper().mapToSchema(facade.getPricePlan(params.getProductType(), params.getPricePlanCode(), params.getEquipmentType(), params.getProvinceCode().value(),
				params.getAccountType(), params.getAccountSubType(), params.getBrandId().intValue())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0014")
	public GetPricePlanListResponse getPricePlanList(GetPricePlanList params) throws EndpointException {
		GetPricePlanListResponseType response = new GetPricePlanListResponseType();

		PricePlanSelectionCriteriaInfo criteria = ServiceHelpers.constructPricePlanSelectionCriteria(params.getProductType(), params.getEquipmentType(), params.getProvinceCode(),
				params.getAccountType(), params.getAccountSubType(), params.getBrandId().intValue(), params.getActivityCode(), params.isCurrentPlansOnlyInd(),
				params.isAvailableForActivationOnlyInd(), params.getActivityReasonCode(), params.getTerm(), params.getNetworkType(), params.isInitialActivationInd(), params.getProductPromoTypeList(),
				params.getSeatTypeCd());

		//Default value for includeFeaturesAndServicesInd is true
		Boolean includeFeaturesAndServicesInd = params.isIncludeFeaturesAndServicesInd();
		criteria.setIncludeFeaturesAndServices(includeFeaturesAndServicesInd == null || includeFeaturesAndServicesInd);

		//Only set socGroups if set (optional)
		List<String> socGroupList = params.getSocGroupList();
		if (socGroupList != null) {
			criteria.setSocGroups(socGroupList.toArray(new String[0]));
		}

		//Only set offerCriteria if systemId is not empty or null.
		
		OfferCriteria offerCriteria = params.getOfferCriteria();
		
		if (offerCriteria != null && StringUtils.isNotBlank(offerCriteria.getSystemId())) {
			OfferCriteriaInfo offerCriteriaInfo = new OfferCriteriaInfo();
			offerCriteriaInfo.setOfferId(offerCriteria.getOfferId());
			offerCriteriaInfo.setSystemId(offerCriteria.getSystemId());
			offerCriteriaInfo.setPerspectiveDate(offerCriteria.getPerspectiveDate());
			offerCriteriaInfo.setPromotionIdList(offerCriteria.getPromotionIdList());
			offerCriteriaInfo.setMscPaidIndicator(offerCriteria.isMSCPaidIndicator());
			criteria.setOfferCriteria(offerCriteriaInfo);
		}

		response.getPricePlanSummaryList().addAll(new PricePlanMapper().mapToSchema(facade.getPricePlans(criteria)));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0015")
	public GetPricePlanSummaryListByPricePlanCodeListResponse getPricePlanSummaryListByPricePlanCodeList(GetPricePlanSummaryListByPricePlanCodeList params) throws EndpointException {
		GetPricePlanSummaryListByPricePlanCodeListResponseType response = new GetPricePlanSummaryListByPricePlanCodeListResponseType();
		Collection<String> pricePlanCodes = getDistinctValues(params.getPricePlanCodeList());
		if (CollectionUtils.isNotEmpty(pricePlanCodes)) {
			response.getPricePlanSummaryList().addAll(new PricePlanMapper().mapToSchema(facade.getPricePlans(pricePlanCodes.toArray(new String[0]))));
		}
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0016")
	public GetRegularServiceResponse getRegularService(GetRegularService params) throws EndpointException {
		GetRegularServiceResponseType response = new GetRegularServiceResponseType();
		response.setService(new ServiceMapper().mapToSchema(facade.getRegularService(params.getRegularServiceCode())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0017")
	public GetRegularServiceListResponse getRegularServiceList(GetRegularServiceList params) throws EndpointException {
		GetRegularServiceListResponseType response = new GetRegularServiceListResponseType();
		response.setServiceList(new ServiceMapper().mapToSchema(facade.getRegularServices(params.getRegularServiceCodeList().toArray(new String[0]))));
		return respond(response);
	}

	@EndpointOperation(errorCode = "CMB_SORS_0018")
	@Override
	public GetServiceExclusionGroupListResponse getServiceExclusionGroupList(GetServiceExclusionGroupList params) throws EndpointException {
		GetServiceExclusionGroupListResponseType response = new GetServiceExclusionGroupListResponseType();
		response.setServiceExclusionGroupList(new ServiceExclusionGroupsMapper().mapToSchema(facade.getServiceExclusionGroups()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0019")
	public GetServiceListByFeatureCategoryResponse getServiceListByFeatureCategory(GetServiceListByFeatureCategory params) throws EndpointException {
		GetServiceListByFeatureCategoryResponseType response = new GetServiceListByFeatureCategoryResponseType();
		response.setServiceList(new ServiceMapper().mapToSchema(facade.getServicesByFeatureCategory(params.getFeatureCategory(), params.getProductType(), params.isCurrentInd())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0020")
	public GetPrepaidServiceResponse getPrepaidService(GetPrepaidService params) throws EndpointException {
		GetPrepaidServiceResponseType response = new GetPrepaidServiceResponseType();
		response.setService(new ServiceMapper().mapToSchema(facade.getWPSService(params.getServiceCode())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0021")
	public GetPrepaidServiceListResponse getPrepaidServiceList(GetPrepaidServiceList params) throws EndpointException {
		GetPrepaidServiceListResponseType response = new GetPrepaidServiceListResponseType();
		response.setServiceList(new ServiceMapper().mapToSchema(facade.getWPSServices()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0022")
	public GetServiceTermResponse getServiceTerm(GetServiceTerm params) throws EndpointException {
		GetServiceTermResponseType response = new GetServiceTermResponseType();
		response.setServiceTerm(new ServiceTermMapper().mapToSchema(facade.getServiceTerm(params.getServiceCode())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0023")
	public GetPrepaidServiceListByEquipmentAndNetworkTypeResponse getPrepaidServiceListByEquipmentAndNetworkType(GetPrepaidServiceListByEquipmentAndNetworkType params) throws EndpointException {
		GetPrepaidServiceListByEquipmentAndNetworkTypeResponseType response = new GetPrepaidServiceListByEquipmentAndNetworkTypeResponseType();
		response.setServiceList(new ServiceMapper().mapToSchema(facade.getPrepaidServiceListByEquipmentAndNetworkType(params.getEquipmentType(), params.getNetworkType())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0024")
	public GetDataSharingGroupListResponse getDataSharingGroupList(GetDataSharingGroupList params) throws EndpointException {
		GetDataSharingGroupListResponseType response = new GetDataSharingGroupListResponseType();
		response.setDataSharingGroupList(new DataSharingGroupMapper().mapToSchema(facade.getDataSharingGroups()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0025")
	public GetDataSharingGroupResponse getDataSharingGroup(GetDataSharingGroup params) throws EndpointException {
		GetDataSharingGroupResponseType response = new GetDataSharingGroupResponseType();
		response.setDataSharingGroup(new DataSharingGroupMapper().mapToSchema(facade.getDataSharingGroup(params.getDataSharingGroupCode())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0026")
	public GetAirTimeAllocationListResponse getAirTimeAllocationList(GetAirTimeAllocationList params) throws EndpointException {
		GetAirTimeAllocationListResponseType response = new GetAirTimeAllocationListResponseType();
		response.setAirTimeAllocationList(new ServiceAirTimeAllocationMapper().mapToSchema(facade.getVoiceAllocation(params.getServiceCodeList().toArray(new String[0]),
				params.getEffectiveRetrievalDate(), getSessionId(facade))));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0027")
	public GetCalculatedEffectiveAirTimeAllocationListResponse getCalculatedEffectiveAirTimeAllocationList(GetCalculatedEffectiveAirTimeAllocationList params) throws EndpointException {
		throw new ObsoleteOperationException();
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0028")
	public GetPeriodListResponse getPeriodList(GetPeriodList params) throws EndpointException {
		GetPeriodListResponseType response = new GetPeriodListResponseType();
		response.setPeriodList(new PeriodMapper().mapToSchema(facade.getServicePeriodInfo(params.getServiceCode())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0029")
	public GetServiceFeatureClassificationResponse getServiceFeatureClassification(GetServiceFeatureClassification params) throws EndpointException {
		GetServiceFeatureClassificationResponseType response = new GetServiceFeatureClassificationResponseType();
		response.setServiceFeatureClassification(new ServiceFeatureClassificationMapper().mapToSchema(facade.getServiceFeatureClassification(params.getClassificationCode())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0030")
	public GetSpecialNumberResponse getSpecialNumber(GetSpecialNumber params) throws EndpointException {
		GetSpecialNumberResponseType response = new GetSpecialNumberResponseType();
		response.setSpecialNumber(new SpecialNumberMapper().mapToSchema(facade.getSpecialNumber(params.getPhoneNumber())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0031")
	public GetSpecialNumberListResponse getSpecialNumberList(GetSpecialNumberList params) throws EndpointException {
		GetSpecialNumberListResponseType response = new GetSpecialNumberListResponseType();
		response.getSpecialNumberList().addAll(new SpecialNumberMapper().mapToSchema(facade.getSpecialNumbers()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0032")
	public GetSpecialNumberRangeResponse getSpecialNumberRange(GetSpecialNumberRange params) throws EndpointException {
		GetSpecialNumberRangeResponseType response = new GetSpecialNumberRangeResponseType();
		response.setSpecialNumberRange(new SpecialNumberRangeMapper().mapToSchema(facade.getSpecialNumberRange(params.getPhoneNumber())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0033")
	public GetSpecialNumberRangeListResponse getSpecialNumberRangeList(GetSpecialNumberRangeList params) throws EndpointException {
		GetSpecialNumberRangeListResponseType response = new GetSpecialNumberRangeListResponseType();
		response.getSpecialNumberRangeList().addAll(new SpecialNumberRangeMapper().mapToSchema(facade.getSpecialNumberRanges()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0034")
	public GetEquivalentServiceResponse getEquivalentService(GetEquivalentService params) throws EndpointException {
		GetEquivalentServiceResponseType response = new GetEquivalentServiceResponseType();
		String[] originalServiceCodeList = params.getOriginalServiceCodeList().toArray(new String[0]);
		@SuppressWarnings("unchecked")
		Map<String, ServiceInfo> serviceMap = facade.getEquivalentServiceByServiceCodeList(originalServiceCodeList, params.getDestinationServiceCodeList().toArray(new String[0]),
				params.getNetworkType());
		for (String originalServiceCode : originalServiceCodeList) {
			EquivalentServiceList equivalientService = new EquivalentServiceList();
			equivalientService.setOriginalServiceCode(originalServiceCode);
			ServiceInfo serviceInfo = serviceMap.get(originalServiceCode);
			if (serviceInfo != null) {
				equivalientService.setService(new ServiceMapper().mapToSchema(serviceInfo));
			}
			response.getEquivalentServiceList().add(equivalientService);
		}
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0035")
	public GetServiceRelationResponse getServiceRelation(GetServiceRelation params) throws EndpointException {
		GetServiceRelationResponseType response = new GetServiceRelationResponseType();
		@SuppressWarnings("unchecked")
		Map<String, List<ServiceAndRelationInfo>> serviceRelationMap = facade.getServiceAndRelationList(new ServiceRelationMapper().mapToDomain(params.getServiceRelationCodeList()).toArray(
				new ServiceRelationInfo[0]));
		for (String serviceCode : serviceRelationMap.keySet()) {
			OriginalServiceCodeList originalServiceCode = new OriginalServiceCodeList();
			originalServiceCode.setOriginalServiceCode(serviceCode);
			List<ServiceCodeRelationList> serviceCodeRelationList = originalServiceCode.getServiceCodeRelationList();
			for (ServiceAndRelationInfo serviceAndRelation : serviceRelationMap.get(serviceCode)) {
				ServiceCodeRelationList outputServiceAndRelation = new ServiceCodeRelationList();
				outputServiceAndRelation.setServiceRelationExpirationDate(serviceAndRelation.getServiceRelation().getExpirationDate());
				outputServiceAndRelation.setService(new ServiceMapper().mapToSchema(serviceAndRelation.getService()));
				serviceCodeRelationList.add(outputServiceAndRelation);
			}
			response.getOriginalServiceCodeList().add(originalServiceCode);
		}
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0036")
	public GetServiceListByServiceGroupResponse getServiceListByServiceGroup(GetServiceListByServiceGroup params) throws EndpointException {
		GetServiceListByServiceGroupResponseType response = new GetServiceListByServiceGroupResponseType();
		@SuppressWarnings("unchecked")
		Map<String, List<ServiceInfo>> serviceGroupMap = facade.getServiceListByGroupList(params.getServiceGroupCodeList().toArray(new String[0]));
		for (String serviceGroupCode : serviceGroupMap.keySet()) {
			ServiceGroupCodeList serviceGroup = new ServiceGroupCodeList();
			serviceGroup.setServiceGroupCode(serviceGroupCode);
			serviceGroup.getServiceList().addAll(new ServiceMapper().mapToSchema(serviceGroupMap.get(serviceGroupCode)));
			response.getServiceGroupCodeList().add(serviceGroup);
		}
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0037")
	public GetIncludedPromotionServiceResponse getIncludedPromotionService(GetIncludedPromotionService params) throws EndpointException {
		GetIncludedPromotionServiceResponseType response = new GetIncludedPromotionServiceResponseType();
		ServiceInfo[] promotionServices = facade.getIncludedPromotions(params.getPricePlanCode(), params.getEquipmentType(), params.getNetworkType(), params.getProvinceCode(),
				Integer.valueOf(params.getContractTerm()).intValue());
		ServiceCodeBusinessRolePriviledge serviceCodeBusinessAndPriviledgeCode = params.getServiceCodeBusinessAndPriviledgeCode();
		if (promotionServices.length > 0 && serviceCodeBusinessAndPriviledgeCode != null) {
			promotionServices = facade.filterByPrivilege(promotionServices, serviceCodeBusinessAndPriviledgeCode.getBusinessRoleCode().value(), serviceCodeBusinessAndPriviledgeCode.getPrivilegeCode()
					.value(), serviceCodeBusinessAndPriviledgeCode.isServiceAvailableInd());
		}
		response.getServiceList().addAll(new ServiceMapper().mapToSchema(promotionServices));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0038")
	public GetAlternateRecurringChargeResponse getAlternateRecurringCharge(GetAlternateRecurringCharge params) throws EndpointException {
		GetAlternateRecurringChargeResponseType response = new GetAlternateRecurringChargeResponseType();
		@SuppressWarnings("unchecked")
		Map<String, Double> chargeMap = facade.getAlternateRecurringCharge(params.getServiceCodeList().toArray(new String[0]), params.getProvinceCode(), params.getNpaNXX(), params.getCorporateId());
		for (String serviceCode : chargeMap.keySet()) {
			AlternateRecurringCharge altRecurringCharge = new AlternateRecurringCharge();
			altRecurringCharge.setServiceCode(serviceCode);
			altRecurringCharge.setAlternateRecurringCharge(chargeMap.get(serviceCode));
			response.getAlternateRecurringChargeList().add(altRecurringCharge);
		}
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0039")
	public GetPricePlanByEquipmentAndAccountByBusinessRoleResponse getPricePlanByEquipmentAndAccountByBusinessRole(GetPricePlanByEquipmentAndAccountByBusinessRole params) throws EndpointException {
		GetPricePlanByEquipmentAndAccountByBusinessRoleResponseType response = new GetPricePlanByEquipmentAndAccountByBusinessRoleResponseType();
		PricePlanInfo pricePlanInfo = facade.getPricePlan(params.getProductType(), params.getPricePlanCode(), params.getEquipmentType(), params.getProvinceCode().value(), params.getAccountType(),
				params.getAccountSubType(), params.getBrandId().intValue());
		ServiceCodeBusinessRolePriviledge serviceCodeBusinessAndPriviledgeCode = params.getServiceCodeBusinessAndPriviledgeCode();
		if (serviceCodeBusinessAndPriviledgeCode != null) {
			pricePlanInfo.setOptionalServices(facade.filterByPrivilege(pricePlanInfo.getOptionalServices0(), serviceCodeBusinessAndPriviledgeCode.getBusinessRoleCode().value(),
					serviceCodeBusinessAndPriviledgeCode.getPrivilegeCode().value(), serviceCodeBusinessAndPriviledgeCode.isServiceAvailableInd()));
		}
		response.setPricePlan(new PricePlanMapper().mapToSchema(pricePlanInfo));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0040")
	public GetPricePlanAndServiceCodeListByGroupCodeResponse getPricePlanAndServiceCodeListByGroupCode(GetPricePlanAndServiceCodeListByGroupCode params) throws EndpointException {
		GetPricePlanAndServiceCodeListByGroupCodeResponseType response = new GetPricePlanAndServiceCodeListByGroupCodeResponseType();
		@SuppressWarnings("unchecked")
		Map<String, List<String>> serviceCodeListMap = facade.getServiceCodeListByGroupList(params.getGroupCodeList().toArray(new String[0]));
		for (String pricePlanServiceCode : serviceCodeListMap.keySet()) {
			PricePlanServiceCodeList pricePlanServiceCodeList = new PricePlanServiceCodeList();
			pricePlanServiceCodeList.setGroupCode(pricePlanServiceCode);
			pricePlanServiceCodeList.getPricePlanServiceCodeList().addAll(serviceCodeListMap.get(pricePlanServiceCode));
			response.getPricePlanServiceCodeList().add(pricePlanServiceCodeList);
		}
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0041")
	public GetMigrationTypeListResponse getMigrationTypeList(GetMigrationTypeList params) throws EndpointException {
		GetMigrationTypeListResponseType response = new GetMigrationTypeListResponseType();
		MigrationTypeInfo[] migrationTypes = facade.getMigrationTypes();
		for (MigrationTypeInfo migrationTypeInfo : migrationTypes) {
			MigrationType migrationType1 = new MigrationType();
			migrationType1.setMigrationTypeCode(migrationTypeInfo.getCode());
			Description descriptionEn = new Description();
			descriptionEn.setLocale("EN");
			descriptionEn.setDescriptionText(migrationTypeInfo.getDescription());
			Description descriptionFr = new Description();
			descriptionFr.setLocale("FR");
			descriptionFr.setDescriptionText(migrationTypeInfo.getDescriptionFrench());
			MultilingualDescriptionList multiLan = new MultilingualDescriptionList();
			multiLan.getDescription().add(descriptionEn);
			multiLan.getDescription().add(descriptionFr);
			migrationType1.setMigrationTypeDesc(multiLan);
			response.getMigrationTypeList().add(migrationType1);
		}
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0042")
	public GetSeatTypeListResponse getSeatTypeList(GetSeatTypeList params) throws EndpointException {
		GetSeatTypeListResponseType response = new GetSeatTypeListResponseType();
		response.getSeatTypeList().addAll(new SeatTypeMapper().mapToSchema(facade.getSeatTypes()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0044")
	public GetAssociatedIncludedPromotionResponse getAssociatedIncludedPromotion(GetAssociatedIncludedPromotion params) throws EndpointException {
		GetAssociatedIncludedPromotionResponseType response = new GetAssociatedIncludedPromotionResponseType();
		response.setAssociatedIncludedPromoInd(facade.isAssociatedIncludedPromotion(params.getPricePlanCode(), Integer.valueOf(params.getTerm()).intValue(), params.getServiceCode()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0045")
	public GetOptionalServiceListResponse getOptionalServiceList(GetOptionalServiceList params) throws EndpointException {
		GetOptionalServiceListResponseType response = new GetOptionalServiceListResponseType();
		PricePlanInfo pricePlanInfo = facade.getPricePlan(params.getProductType(), params.getPricePlanCode(), params.getEquipmentType(), params.getProvinceCode().value(), params.getAccountType(),
				params.getAccountSubType(), params.getBrandId().intValue());
		response.getServiceList().addAll(new ServiceMapper().mapToSchema(pricePlanInfo.getOptionalServices0()));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0046")
	public GetServiceExtendedInfoResponse getServiceExtendedInfo(GetServiceExtendedInfo params) throws EndpointException {
		GetServiceExtendedInfoResponseType response = new GetServiceExtendedInfoResponseType();
		response.getServiceExtendedInformationList().addAll(new ServiceExtendedInfoMapper().mapToSchema(facade.getServiceExtendedInfo(params.getServiceCodeList().toArray(new String[0]))));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0047")
	public CheckPricePlanPrivilegeResponse checkPricePlanPrivilege(CheckPricePlanPrivilege params) throws EndpointException {
		CheckPricePlanPrivilegeResponseType response = new CheckPricePlanPrivilegeResponseType();
		response.setServicePolicyList(new ServicePolicyMapper().mapToSchema(facade.checkPricePlanPrivilege(params.getServiceCodeList().toArray(new String[0]), params.getBusinessRole().value(), params
				.getPrivilege().value())));
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0048")
	public GetMandatoryServiceListResponse getMandatoryServiceList(GetMandatoryServiceList params) throws EndpointException {
		GetMandatoryServiceListResponseType response = new GetMandatoryServiceListResponseType();
		ServiceSetInfo[] serviceSetInfo = facade.getMandatoryServices(params.getPricePlanCode(), params.getHandSetType(), params.getProductType(), params.getEquipmentType(), params.getProvinceCode()
				.value(), params.getAccountType(), params.getAccountSubType(), params.getBrandId());
		for (ServiceSetInfo serviceSetInfoObject : serviceSetInfo) {
			ArrayList<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
			for (Service service : serviceSetInfoObject.getServices()) {
				serviceInfos.add((ServiceInfo) service);
			}
			response.setServiceList(new ServiceMapper().mapToSchema(serviceInfos.toArray(new ServiceInfo[serviceInfos.size()])));
		}
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SORS_0049")
	public GetWccServiceExtendedInfoResponse getWccServiceExtendedInfo(GetWccServiceExtendedInfo params) throws EndpointException {
		GetWccServiceExtendedInfoResponseType response = new GetWccServiceExtendedInfoResponseType();
		response.setWccServiceExtendedInformationList(WCCServiceExtendedInfoMapper.getInstance().mapToSchema(facade.getWCCServiceExtendedInfo()));
		return respond(response);
	}

}
