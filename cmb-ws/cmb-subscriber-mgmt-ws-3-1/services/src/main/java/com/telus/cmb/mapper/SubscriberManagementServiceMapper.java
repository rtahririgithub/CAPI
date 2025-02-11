package com.telus.cmb.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.ActivationOptionInfo;
import com.telus.eas.account.info.ActivationOptionTypeInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.account.info.ServicesValidationInfo;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.message.info.ApplicationMessageInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.CallingCircleCommitmentAttributeDataInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.FeatureChangeInfo;
import com.telus.eas.subscriber.info.PrepaidServicePropertyInfo;
import com.telus.eas.subscriber.info.PricePlanChangeInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceChangeInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.cmb.jws.ApplicationMessage;
import com.telus.cmb.jws.PortInEligibility;
import com.telus.cmb.jws.ServicesValidation;
import com.telus.cmb.jws.WarningBaseType;
import com.telus.cmb.jws.WarningType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ActivationOption;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.Equipment;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.EquipmentChangeRequest;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.AutoRenewType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.CallingCircleCommitmentAttributeData;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.Commitment;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractFeature;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractService;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.MultiRingPhoneNumberType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PhoneNumberList;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PrepaidPropertyListType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PricePlan;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PricePlanValidationOverrideIndicatorList;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TimePeriod;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TransactionType;



public class SubscriberManagementServiceMapper {
	public static ApplicationMessageMapper ApplicationMessageMapper() {
		return ApplicationMessageMapper.getInstance();
	}
	
	public static EquipmentChangeRequestMapper EquipmentChangeRequestMapper() {
		return EquipmentChangeRequestMapper.getInstance();
	}
	
	public static ServiceWarningMapper ServiceWarningMapper() {
		return ServiceWarningMapper.getInstance();
	}
	
	public static WarningBaseTypeMapper WarningBaseTypeMapper() {
		return WarningBaseTypeMapper.getInstance();
	}
	
	public static ServiceAgreementMapper ServiceAgreementMapper() {
		return ServiceAgreementMapper.getInstance();
	}
	
	public static PricePlanMapper PricePlanMapper() {
		return PricePlanMapper.getInstance();
	}
	
	public static ContractChangeInfoMapper ContractChangeInfoMapper() {
		return ContractChangeInfoMapper.getInstance();
	}

	public static CallingCircleCommitmentAttributeDataInfoMapper CallingCircleCommitmentAttributeDataInfoMapper() {
		return CallingCircleCommitmentAttributeDataInfoMapper.getInstance();
	}
	
	public static PortInEligibilityMapper PortInEligibilityMapper() {
		return PortInEligibilityMapper.getInstance();
	}
	
	public static ServicesValidationMapper ServicesValidationMapper() {
		return ServicesValidationMapper.getInstance();
	}
	
	public static ActivationOptionMapper ActivationOptionMapper() {
		return ActivationOptionMapper.getInstance();
	}
	
	public static class CallingCircleCommitmentAttributeDataInfoMapper extends AbstractSchemaMapper<CallingCircleCommitmentAttributeData, CallingCircleCommitmentAttributeDataInfo> {		
		private static CallingCircleCommitmentAttributeDataInfoMapper INSTANCE;
		
		protected CallingCircleCommitmentAttributeDataInfoMapper() {
			super (CallingCircleCommitmentAttributeData.class, CallingCircleCommitmentAttributeDataInfo.class);
		}
		
		protected synchronized static CallingCircleCommitmentAttributeDataInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CallingCircleCommitmentAttributeDataInfoMapper();
			}
			return INSTANCE;
		}
		

		@Override
		protected CallingCircleCommitmentAttributeDataInfo performDomainMapping(
				CallingCircleCommitmentAttributeData source, CallingCircleCommitmentAttributeDataInfo target) {
			target.setEffectiveDate(source.getEffectiveDate());
			target.setRemainingAllowedModifications(source.getRemainingModificationsAllowed());
			target.setTotalAllowedModifications(source.getTotalModificationsAllowed());
			target.setPrepaidModificationBlocked(source.isPrepaidModificationsBlockedInd());
			return super.performDomainMapping(source, target);
		}

		@Override
		protected CallingCircleCommitmentAttributeData performSchemaMapping(
				CallingCircleCommitmentAttributeDataInfo source, CallingCircleCommitmentAttributeData target) {
			target.setEffectiveDate(source.getEffectiveDate());
			target.setTotalModificationsAllowed(source.getTotalAllowedModifications());
			target.setRemainingModificationsAllowed(source.getRemainingAllowedModifications());
			target.setPrepaidModificationsBlockedInd(source.isPrepaidModificationBlocked());
			return super.performSchemaMapping(source, target);
		}
	}
	
	
	public static class ApplicationMessageMapper extends AbstractSchemaMapper<ApplicationMessage, ApplicationMessageInfo> {		
		private static ApplicationMessageMapper INSTANCE;
		
		protected ApplicationMessageMapper() {
			super (ApplicationMessage.class, ApplicationMessageInfo.class);
		}
		
		protected synchronized static ApplicationMessageMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ApplicationMessageMapper();
			}
			return INSTANCE;
		}
		

		@Override
		protected ApplicationMessageInfo performDomainMapping(
				ApplicationMessage source, ApplicationMessageInfo target) {
			// TODO Auto-generated method stub
			return super.performDomainMapping(source, target);
		}

		@Override
		protected ApplicationMessage performSchemaMapping(
				ApplicationMessageInfo source, ApplicationMessage target) {
			target.setApplicationId(Integer.valueOf(source.getApplicationId()));
			target.setAudienceTypeId(Integer.valueOf(source.getAudienceTypeId()));
			target.setBrandId(Integer.valueOf(source.getBrandId()));
			target.setMessageCode(source.getCode());
			target.setMessageId(source.getId());
			target.setMessageTypeId(source.getMessageTypeId());
			return super.performSchemaMapping(source, target);
		}
	}
		
	
	public static class EquipmentChangeRequestMapper extends AbstractSchemaMapper<EquipmentChangeRequest, EquipmentChangeRequestInfo> {
		private static EquipmentChangeRequestMapper INSTANCE;
		
		protected EquipmentChangeRequestMapper() {
			super(EquipmentChangeRequest.class, EquipmentChangeRequestInfo.class);
		}
		
		protected synchronized static EquipmentChangeRequestMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new EquipmentChangeRequestMapper();
			}
			return INSTANCE;
		}

		@Override
		protected EquipmentChangeRequestInfo performDomainMapping(EquipmentChangeRequest source, EquipmentChangeRequestInfo target) {
			if (source != null) {
				if (source.getDuplicateSerialNumbersIndicator() != null) {
					target.setAllowDuplicateSerialNumber(source.getDuplicateSerialNumbersIndicator().value().charAt(0));
				}
				
				if (source.getSwapType() != null) {
					target.setSwapType(source.getSwapType().value());
				}
				
				target.setRepairId(source.getRepairId());
				target.setRequestorId(source.getRequestorId());
				target.setPreserveDigitalServices(source.isPreserveDigitalServices());
				
				if (source.getPrimaryEquipment() != null) {
					target.setNewEquipmentSerialNumber(source.getPrimaryEquipment().getSerialNumber());
				}
				
				if (source.getAssociatedHandsetEquipment() != null) {
					target.setNewAssociatedHandsetSerialNumber(source.getAssociatedHandsetEquipment().getSerialNumber());
				}
				
				List<Equipment> equipmentList = source.getSecondaryEquipmentList();
				List<String> secondaryEquipmentSerialNumberList = new ArrayList<String> ();
				for (Equipment e : equipmentList) {
					secondaryEquipmentSerialNumberList.add(e.getSerialNumber());
				}
				target.setSecondaryEquipmentSerialNumberList(secondaryEquipmentSerialNumberList.toArray(new String[0]));
			}
			return super.performDomainMapping(source, target);
		}

		@Override
		protected EquipmentChangeRequest performSchemaMapping(EquipmentChangeRequestInfo source, EquipmentChangeRequest target) {
			if (source.getAssociatedHandset() != null) {
				Equipment equipment = new Equipment();
				equipment.setSerialNumber(source.getAssociatedHandset().getSerialNumber());
				equipment.setEquipmentType(source.getAssociatedHandset().getEquipmentType());
				target.setAssociatedHandsetEquipment(equipment);
			}
			if (source.getNewEquipment() != null) {
				Equipment equipment = new Equipment();
				equipment.setSerialNumber(source.getNewEquipment().getSerialNumber());
				equipment.setEquipmentType(source.getNewEquipment().getEquipmentType());
				target.setPrimaryEquipment(equipment);
			}
			
			// remaining field values are not mapped here. its only needed to process the equipment change request.
			return super.performSchemaMapping(source, target);
		}
	}
	
	
	public static class ServiceWarningMapper extends AbstractSchemaMapper<WarningType, WarningFaultInfo> {
		private static ServiceWarningMapper INSTANCE;
		
		protected ServiceWarningMapper() {
			super(WarningType.class, WarningFaultInfo.class);
		}
		
		protected synchronized static ServiceWarningMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ServiceWarningMapper();
			}
			return INSTANCE;
		}

		@Override
		protected WarningFaultInfo performDomainMapping(WarningType source, WarningFaultInfo target) {
			// TODO Auto-generated method stub
			return super.performDomainMapping(source, target);
		}

		@Override
		protected WarningType performSchemaMapping(WarningFaultInfo source, WarningType target) {
			target.setSystemCode(source.getSystemCode());
			target.setWarningType(source.getWarningType());
			target.setWarning(WarningBaseTypeMapper().mapToSchema(source));
			return super.performSchemaMapping(source, target);
		}
	
	}
	
	
	public static class WarningBaseTypeMapper extends AbstractSchemaMapper<WarningBaseType, WarningFaultInfo> {
		private static WarningBaseTypeMapper INSTANCE;
		
		protected WarningBaseTypeMapper() {
			super (WarningBaseType.class, WarningFaultInfo.class);
		}
		
		protected synchronized static WarningBaseTypeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new WarningBaseTypeMapper();
			}
			return INSTANCE;
		}

		@Override
		protected WarningBaseType performSchemaMapping(WarningFaultInfo source, WarningBaseType target) {
			target.setMessageId(source.getMessageId());
			target.setWarningCode(source.getErrorCode());
			target.setWarningMessage(source.getErrorMessage());
			return super.performSchemaMapping(source, target);
		}
	}
	
	
	
	public static class ServiceAgreementMapper extends AbstractSchemaMapper<ServiceAgreement, SubscriberContractInfo> {
		private static ServiceAgreementMapper INSTANCE;

		private ServiceAgreementMapper() {
			super (ServiceAgreement.class, SubscriberContractInfo.class);
		}
		
		public synchronized static ServiceAgreementMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ServiceAgreementMapper();
			}
			return INSTANCE;
		}
		
		@Override
		protected SubscriberContractInfo performDomainMapping( ServiceAgreement source,  SubscriberContractInfo target) {
			if(source.getCommitment()!=null){
				CommitmentInfo commitmentInfo=CommitmentMapper.getInstance().mapToDomain(source.getCommitment());
				target.setCommitmentEndDate(commitmentInfo.getEndDate());
				target.setCommitmentStartDate(commitmentInfo.getStartDate());
				target.setCommitmentReasonCode(commitmentInfo.getReasonCode());
				target.setCommitmentMonths(commitmentInfo.getMonths());
			}
			if(source.getPricePlan()!=null) {
				SubscriberContractInfo mappedSource = PricePlanMapper.getInstance().mapToDomain(source.getPricePlan());
				target.setPricePlan(mappedSource.getPricePlanCode());
				target.setEffectiveDate(mappedSource.getEffectiveDate());
				target.setExpiryDate(mappedSource.getExpiryDate());
				target.setPricePlanServiceType(mappedSource.getPricePlanServiceType());
				HashMap<String, ServiceFeatureInfo> features = new HashMap<String, ServiceFeatureInfo>();
				for(ServiceFeatureInfo feature : mappedSource.getFeatures0(false)){
					features.put(feature.getCode(), feature);
				}
				target.setFeatures(features);
			}
			
			if(!source.getService().isEmpty()){
				HashMap<String, ServiceAgreementInfo> services = new HashMap<String, ServiceAgreementInfo>();
				for(ContractService contractService: source.getService()){
					services.put(contractService.getCode(),ContractServiceMapper.getInstance().mapToDomain(contractService));
				}
				target.setServices(services);
			}
			
			if (source.getMultiRingPhoneNumberList() != null) {
				target.setMultiRingPhoneNumbers(source.getMultiRingPhoneNumberList().getMultiRingPhoneNumber().toArray(new String[0]));
			}
			if(source.getValidationOverrideIndicatorList()!=null){
				 PricePlanValidationInfo pricePlanValidationInfo =ServiceAgreementValidationMapper.getInstance().mapToDomain(source.getValidationOverrideIndicatorList());
				target.getPricePlanValidation0().setCurrentValidation(pricePlanValidationInfo.validateCurrent());
				target.getPricePlanValidation0().setPricePlanServiceGrouping(pricePlanValidationInfo.validatePricePlanServiceGrouping());
				target.getPricePlanValidation0().setEquipmentServiceMatch(pricePlanValidationInfo.validateEquipmentServiceMatch());
				target.getPricePlanValidation0().setProvinceServiceMatch(pricePlanValidationInfo.validateProvinceServiceMatch());
				target.getPricePlanValidation0().setForSaleValidation(pricePlanValidationInfo.validateForSale());
			}
						
			if (source.isCascadeShareableServiceChangeInd()!=null) {
				target.setCascadeShareableServiceChanges(source.isCascadeShareableServiceChangeInd().booleanValue());
			}
			return super.performDomainMapping(source, target);
		}


		@Override
		protected ServiceAgreement performSchemaMapping(SubscriberContractInfo source, ServiceAgreement target) {
			target.setCommitment(CommitmentMapper.getInstance().mapToSchema(source.getCommitment()));
			if (source.getMultiRingPhoneNumbers() != null) {
				MultiRingPhoneNumberType multiRingPhoneNumber = new MultiRingPhoneNumberType();
				multiRingPhoneNumber.getMultiRingPhoneNumber().addAll(Arrays.asList(source.getMultiRingPhoneNumbers()));			
				target.setMultiRingPhoneNumberList(multiRingPhoneNumber);
			}
			target.setPricePlan(PricePlanMapper().mapToSchema(source));
			ServiceAgreementInfo[] contractServices = source.getServices0(true, true);
			if (contractServices != null) {
				for (ServiceAgreementInfo cs : contractServices) {
					target.getService().add(ContractServiceMapper.getInstance().mapToSchema(cs));
				}
			}
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class CommitmentMapper extends AbstractSchemaMapper<Commitment, CommitmentInfo> {
	
		private static CommitmentMapper INSTANCE ;

		private CommitmentMapper() {
			super (Commitment.class, CommitmentInfo.class);
		}
		
		public synchronized static CommitmentMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CommitmentMapper();
			}
			
			return INSTANCE;
		}
		
		@Override
		protected CommitmentInfo performDomainMapping(Commitment source, CommitmentInfo target) {
			if (source != null) {
				if (source.getTimePeriod() != null) {
					if(source.getTimePeriod().getExpiryDate()!=null)
						target.setEndDate(source.getTimePeriod().getExpiryDate());
					if(source.getTimePeriod().getEffectiveDate()!=null)
						target.setStartDate(source.getTimePeriod().getEffectiveDate());
				}
				target.setMonths(Integer.valueOf(source.getContractTerm()));
				target.setReasonCode(source.getReasonCode());
				target.setModified(TransactionType.MODIFY.equals(source.getTransactionType().value()));
			}
			return super.performDomainMapping(source, target);
		}

		
		@Override
		protected Commitment performSchemaMapping(CommitmentInfo source, Commitment target) {
			target.setContractTerm(String.valueOf(source.getMonths()));
			target.setReasonCode(source.getReasonCode());
			target.setRenewalInd(false);
			TimePeriod timePeriod = new TimePeriod();
			timePeriod.setEffectiveDate(source.getStartDate());
			timePeriod.setExpiryDate(source.getEndDate());
			target.setTimePeriod(timePeriod);
			target.setTransactionType(TransactionType.NO_CHANGE);
			return super.performSchemaMapping(source, target);
		}
		
	}
	
	
	public static class PricePlanMapper extends AbstractSchemaMapper<PricePlan, SubscriberContractInfo> {
		private static PricePlanMapper INSTANCE;

		protected PricePlanMapper() {
			super (PricePlan.class, SubscriberContractInfo.class);
		}
		
		protected synchronized static PricePlanMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new PricePlanMapper();
			}
			
			return INSTANCE;
		}
		
		@Override
		protected SubscriberContractInfo performDomainMapping(PricePlan source, SubscriberContractInfo target) {
			target.setPricePlan(source.getCode());
			target.setTransaction(translateTransactionType(source.getTransactionType()));
			if(source.getTimePeriod()!=null){
				if(source.getTimePeriod().getEffectiveDate()!=null)
					target.setEffectiveDate(source.getTimePeriod().getEffectiveDate());
				if(source.getTimePeriod().getExpiryDate()!=null)
					target.setExpiryDate(source.getTimePeriod().getExpiryDate());
			}
			if(source.getServiceType()!=null)
				target.setPricePlanServiceType(source.getServiceType());
			
			if(!source.getFeature().isEmpty()){
				HashMap<String, ServiceFeatureInfo> features = new HashMap<String, ServiceFeatureInfo>();
				for(ContractFeature feature :source.getFeature()){
					features.put(feature.getCode(), ContractFeatureMapper.getInstance().mapToDomain(feature));
				}
				target.setFeatures(features);
			}
			
			return super.performDomainMapping(source, target);
		}

		@Override
		protected PricePlan performSchemaMapping(SubscriberContractInfo source, PricePlan target) {
			target.setCode(source.getCode());
//			target.setServiceType(source.getPricePlan0().getServiceType());
			if(source.getPricePlanServiceType()!=null && !source.getPricePlanServiceType().isEmpty())
				target.setServiceType(source.getPricePlanServiceType());
			TimePeriod timePeriod = new TimePeriod();
			timePeriod.setEffectiveDate(source.getEffectiveDate());
			timePeriod.setExpiryDate(source.getExpiryDate());
			target.setTimePeriod(timePeriod);
			target.setTransactionType(translateTransactionType(source.getTransaction()));			
			ServiceFeatureInfo[] features = source.getFeatures0(true);
			if (features != null) {
				for (ServiceFeatureInfo serviceFeature : features) {
					target.getFeature().add(ContractFeatureMapper.getInstance().mapToSchema(serviceFeature));
				}
			}
			
			return super.performSchemaMapping(source, target);
		}
	}
	
	
	public static class ContractChangeInfoMapper extends AbstractSchemaMapper<ServiceAgreement, ContractChangeInfo> {
		private static ContractChangeInfoMapper INSTANCE;
		
		protected ContractChangeInfoMapper() {
			super (ServiceAgreement.class, ContractChangeInfo.class);
		}
		
		protected synchronized static ContractChangeInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ContractChangeInfoMapper();
			}
			
			return INSTANCE;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ContractChangeInfo performDomainMapping(ServiceAgreement source, ContractChangeInfo target) {
			/** Set Commitment **/
			target.setNewCommitmentInfo(CommitmentInfoMapper.getInstance().mapToDomain(source.getCommitment()));
			if (source.getCommitment() != null) {
				target.setContractRenewalInd(source.getCommitment().isRenewalInd());
				target.setContractTerm(Integer.valueOf(source.getCommitment().getContractTerm()));
				target.setActivationInd(source.getCommitment().isActivationInd());
				target.setMigrationInd(source.getCommitment().isMigrationInd());
			}
			/** Set multi ring phone number list */
			if (source.getMultiRingPhoneNumberList() != null) {
				List<String> multiRingPhoneNumberList = source.getMultiRingPhoneNumberList().getMultiRingPhoneNumber();
				target.setMultiRingPhoneNumberList(multiRingPhoneNumberList.toArray(new String[0]));
			}
			/** set price plan info */
			if (source.getPricePlan() != null) {
				PricePlan pp = source.getPricePlan();
				PricePlanChangeInfo ppcInfo = new PricePlanChangeInfo();
				ppcInfo.setCode(pp.getCode());
				ppcInfo.setServiceType(pp.getServiceType());
				
				if (pp.getFeature() != null) {
					List<ContractFeature> featureList = pp.getFeature();
					List<FeatureChangeInfo> featureChangeInfoList = FeatureChangeInfoMapper.getInstance().mapToDomain(featureList);
					ppcInfo.setFeatureChangeInfoList(featureChangeInfoList.toArray(new FeatureChangeInfo[0]));
				}
				
				if (pp.getTimePeriod() != null) {
					ppcInfo.setEffectiveDate(pp.getTimePeriod().getEffectiveDate());
					ppcInfo.setExpiryDate(pp.getTimePeriod().getExpiryDate());
				}
				
				if (pp.getTransactionType() != null) {
					ppcInfo.setTransactionType(pp.getTransactionType().value());
					target.setPricePlanChangeInd(TransactionType.MODIFY.equals(pp.getTransactionType()));
				}
				target.setPricePlanChangeInfo(ppcInfo);
			}
			/** Set service agreement validation **/
			target.setPricePlanValidatioInfo(ServiceAgreementValidationMapper.getInstance().mapToDomain(source.getValidationOverrideIndicatorList()));
			
			/** set optional services **/
			if (source.getService() != null) {
				List<ContractService> contractServiceList = source.getService();
				List<ServiceChangeInfo> serviceChangeInfoList = new ArrayList<ServiceChangeInfo>();
				
				for (ContractService cs : contractServiceList) {
					ServiceChangeInfo serviceChangeInfo = new ServiceChangeInfo();
					ServiceAgreementInfo sa = new ServiceAgreementInfo();
					
					sa.setServiceCode(cs.getCode());
					serviceChangeInfo.setCode(Info.padTo(cs.getCode(), ' ', 9));
					
					if (cs.getTimePeriod() != null) {
						serviceChangeInfo.setEffectiveDate(cs.getTimePeriod().getEffectiveDate());
						serviceChangeInfo.setExpiryDate(cs.getTimePeriod().getExpiryDate());
						sa.setEffectiveDate(cs.getTimePeriod().getEffectiveDate());
						sa.setExpiryDate(cs.getTimePeriod().getExpiryDate());
					}
					
					if (cs.getTransactionType() != null) {
						serviceChangeInfo.setTransactionType(cs.getTransactionType().value());
						sa.setTransaction(translateTransactionType(cs.getTransactionType()));
					}
					serviceChangeInfo.setNewServiceAgreementInfo(sa);

					List<ContractFeature> contractFeatureList = cs.getFeature();
					List<FeatureChangeInfo> featureChangeInfoList = FeatureChangeInfoMapper.getInstance().mapToDomain(contractFeatureList);
					serviceChangeInfo.setFeatureChangeInfoList(featureChangeInfoList.toArray(new FeatureChangeInfo[0]));
					serviceChangeInfo.setPrepaidServicePropertyInfo(PrepaidServicePropertyInfoMapper.getInstance().mapToDomain(cs.getPrepaidPropertyList()));
					serviceChangeInfo.setServiceType(cs.getServiceType());
					serviceChangeInfoList.add(serviceChangeInfo);
				}
				target.setServiceChangeInfoList(serviceChangeInfoList.toArray(new ServiceChangeInfo[0]));
			}
		//target.setActivationInd(source.)
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class CommitmentInfoMapper extends AbstractSchemaMapper<Commitment, CommitmentInfo> {
		private static CommitmentInfoMapper INSTANCE;
		
		protected CommitmentInfoMapper() {
			super (Commitment.class, CommitmentInfo.class);
		}
		
		protected synchronized static CommitmentInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CommitmentInfoMapper();
			}
			
			return INSTANCE;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected CommitmentInfo performDomainMapping(Commitment source, CommitmentInfo target) {
			if (source != null) {
				if (source.getTimePeriod() != null) {
					target.setEndDate(source.getTimePeriod().getExpiryDate());
					target.setStartDate(source.getTimePeriod().getEffectiveDate());					
				}
				target.setMonths(Integer.parseInt(source.getContractTerm()));
				target.setReasonCode(source.getReasonCode());
				target.setModified(TransactionType.MODIFY.equals(source.getTransactionType().value()));
			}
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class ServiceAgreementValidationMapper extends AbstractSchemaMapper<PricePlanValidationOverrideIndicatorList, PricePlanValidationInfo> {
		private static ServiceAgreementValidationMapper INSTANCE;
		
		protected ServiceAgreementValidationMapper() {
			super (PricePlanValidationOverrideIndicatorList.class, PricePlanValidationInfo.class);
		}
		
		protected synchronized static ServiceAgreementValidationMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ServiceAgreementValidationMapper();
			}
			
			return INSTANCE;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected PricePlanValidationInfo performDomainMapping(PricePlanValidationOverrideIndicatorList source, PricePlanValidationInfo target) {
			if (source.isCurrentValidationInd() != null) {
				target.setCurrentValidation(source.isCurrentValidationInd().booleanValue());
			}
			if (source.isEquipmentServiceMatchInd() != null) {
				target.setEquipmentServiceMatch(source.isEquipmentServiceMatchInd().booleanValue());
			}
			if (source.isForSaleValidationInd() != null) {
				target.setForSaleValidation(source.isForSaleValidationInd().booleanValue());
			}
			if (source.isPricePlanServiceGroupingInd() != null) {
				target.setPricePlanServiceGrouping(source.isPricePlanServiceGroupingInd().booleanValue());
			}
			if (source.isProvinceServiceMatchInd() != null) {
				target.setProvinceServiceMatch(source.isProvinceServiceMatchInd().booleanValue());
			}
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class FeatureChangeInfoMapper extends AbstractSchemaMapper<ContractFeature, FeatureChangeInfo> {
		private static FeatureChangeInfoMapper INSTANCE;
		
		protected FeatureChangeInfoMapper() {
			super (ContractFeature.class, FeatureChangeInfo.class);
		}
		
		protected synchronized static FeatureChangeInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new FeatureChangeInfoMapper();
			}
			
			return INSTANCE;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected FeatureChangeInfo performDomainMapping(ContractFeature source, FeatureChangeInfo target) {
			target.setCode(source.getCode());
			target.setFeatureParameter(source.getFeatureParameter());
			
			if (source.getTimePeriod() != null) {
				target.setEffectiveDate(source.getTimePeriod().getEffectiveDate());
				target.setExpiryDate(source.getTimePeriod().getExpiryDate());
			}
			
			if (source.getTransactionType() != null) {
				target.setTransactionType(source.getTransactionType().value());
			}
			
			if(source.getCallingCirclePhoneNumberList()!=null ){
				List<String> phoneNumbers=source.getCallingCirclePhoneNumberList().getPhoneNumber();
				if(phoneNumbers!=null && !phoneNumbers.isEmpty()){
					target.setCallingCirclePhoneNumberList(phoneNumbers.toArray(new String[phoneNumbers.size()]));
				}
			}

			ServiceFeatureInfo newServiceFeatureInfo = new ServiceFeatureInfo();
			newServiceFeatureInfo.setFeatureCode(target.getCode());
			if(source.getCallingCircleCommitmentAttributeData()!=null ){
				newServiceFeatureInfo.setCallingCircleCommitmentAttributeData(
						CallingCircleCommitmentAttributeDataInfoMapper.getInstance().mapToDomain(source.getCallingCircleCommitmentAttributeData()));
			}
			
			target.setNewServiceFeatureInfo(newServiceFeatureInfo);
			
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class PrepaidServicePropertyInfoMapper extends AbstractSchemaMapper<PrepaidPropertyListType, PrepaidServicePropertyInfo> {
		private static PrepaidServicePropertyInfoMapper INSTANCE;
		
		protected PrepaidServicePropertyInfoMapper() {
			super (PrepaidPropertyListType.class, PrepaidServicePropertyInfo.class);
		}
		
		protected synchronized static PrepaidServicePropertyInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new PrepaidServicePropertyInfoMapper();
			}
			
			return INSTANCE;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected PrepaidServicePropertyInfo performDomainMapping(PrepaidPropertyListType source, PrepaidServicePropertyInfo target) {
			if (source.getAutoRenewPropertyList() != null) {
				if (source.getAutoRenewPropertyList().getRenewalFundSource() != null) {
					target.setAutoRenewalFundSource(source.getAutoRenewPropertyList().getRenewalFundSource().intValue());
				}
				target.setAutoRenewalInd(source.getAutoRenewPropertyList().isAutoRenewInd());
			}
			target.setPurchaseFundSource(source.getPurchaseFundSource());
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class ContractServiceMapper extends AbstractSchemaMapper<ContractService, ServiceAgreementInfo> {
		private static ContractServiceMapper INSTANCE;
		
		protected ContractServiceMapper() {
			super (ContractService.class, ServiceAgreementInfo.class);
		}
		
		protected synchronized static ContractServiceMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ContractServiceMapper();
			}
			
			return INSTANCE;
		}

		@Override
		protected ServiceAgreementInfo performDomainMapping(ContractService source, ServiceAgreementInfo target) {
			
			target.setServiceCode(source.getCode());
			target.setTransaction(translateTransactionType(source.getTransactionType()));
			if(source.getTimePeriod()!=null){
				if(source.getTimePeriod().getEffectiveDate()!=null)
					target.setEffectiveDate(source.getTimePeriod().getEffectiveDate());
				if(source.getTimePeriod().getExpiryDate()!=null)
					target.setExpiryDate(source.getTimePeriod().getExpiryDate());
			}
			if(source.getServiceType()!=null)
				target.setServiceType(source.getServiceType());
			
			if(!source.getFeature().isEmpty()){
				HashMap<String, ServiceFeatureInfo> features = new HashMap<String, ServiceFeatureInfo>();
				for(ContractFeature feature :source.getFeature()){
					features.put(feature.getCode(), ContractFeatureMapper.getInstance().mapToDomain(feature));
				}
				target.setFeatures(features);
			}
			if(source.getPrepaidPropertyList()!=null){
				PrepaidPropertyListType prepaidProperty =source.getPrepaidPropertyList();
				target.setWPS(prepaidProperty.isPrepaidInd());	
				target.setPurchaseFundSource(prepaidProperty.getPurchaseFundSource());
				target.setAutoRenew(prepaidProperty.getAutoRenewPropertyList().isAutoRenewInd());
				if(prepaidProperty.getAutoRenewPropertyList().getRenewalFundSource()!=null)
					target.setAutoRenewFundSource(prepaidProperty.getAutoRenewPropertyList().getRenewalFundSource());
			}
			
			return super.performDomainMapping(source, target);
		}
			
		@Override
		protected ContractService performSchemaMapping(ServiceAgreementInfo source, ContractService target) {
			target.setCode(source.getCode());
			if (source.isWPS()) {
				PrepaidPropertyListType prepaidProperty = new PrepaidPropertyListType();
				AutoRenewType autoRenew = new AutoRenewType();
				autoRenew.setAutoRenewInd(source.getAutoRenew());
				autoRenew.setRenewalFundSource(Integer.valueOf(source.getAutoRenewFundSource()));
				prepaidProperty.setAutoRenewPropertyList(autoRenew);
				prepaidProperty.setPrepaidInd(source.isWPS());
				prepaidProperty.setPurchaseFundSource(source.getPurchaseFundSource());
				target.setPrepaidPropertyList(prepaidProperty);
			}
			
			if(source.getServiceType()!=null && !source.getServiceType().isEmpty())
				target.setServiceType(source.getServiceType());
			
			TimePeriod timePeriod = new TimePeriod();
			timePeriod.setEffectiveDate(source.getEffectiveDate());
			timePeriod.setExpiryDate(source.getExpiryDate());
			target.setTimePeriod(timePeriod);
			target.setTransactionType(translateTransactionType(source.getTransaction()));
			ServiceFeatureInfo[] features = source.getFeatures0(true);
			if (features != null) {
				for (ServiceFeatureInfo serviceFeature : features) {
					target.getFeature().add(ContractFeatureMapper.getInstance().mapToSchema(serviceFeature));
				}
			}
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class ContractFeatureMapper extends AbstractSchemaMapper<ContractFeature, ServiceFeatureInfo> {
		private static ContractFeatureMapper INSTANCE;
		
		protected ContractFeatureMapper() {
			super (ContractFeature.class, ServiceFeatureInfo.class);
		}
		
		protected synchronized static ContractFeatureMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ContractFeatureMapper();
			}
			
			return INSTANCE;
		}

		
		@Override
		protected ServiceFeatureInfo performDomainMapping(ContractFeature source, ServiceFeatureInfo target) {
			target.setFeatureCode(source.getCode());
			target.setTransaction(translateTransactionType(source.getTransactionType()));
			
			if(source.getTimePeriod()!=null){
				if(source.getTimePeriod().getEffectiveDate()!=null)
					target.setEffectiveDate(source.getTimePeriod().getEffectiveDate());
				if(source.getTimePeriod().getExpiryDate()!=null)
					target.setExpiryDate(source.getTimePeriod().getExpiryDate());
			}
			if(source.getFeatureParameter()!=null)
				target.setParameter(source.getFeatureParameter());
			
			if(source.getCallingCirclePhoneNumberList()!=null){
				List<String> phoneNumbers=source.getCallingCirclePhoneNumberList().getPhoneNumber();
				target.setCallingCirclePhoneNumberList(phoneNumbers.toArray(new String[0]));
			}
			
			if(source.getCallingCircleCommitmentAttributeData()!=null){
				target.setCallingCircleCommitmentAttributeData(
						CallingCircleCommitmentAttributeDataInfoMapper.getInstance().mapToDomain(source.getCallingCircleCommitmentAttributeData()));
			}
			return super.performDomainMapping(source, target);
		}
		
		@Override
		protected ContractFeature performSchemaMapping(ServiceFeatureInfo source, ContractFeature target) {
			target.setCode(source.getCode());
			target.setFeatureParameter(source.getParameter());
			TimePeriod timePeriod = new TimePeriod();
			timePeriod.setEffectiveDate(source.getEffectiveDate());
			timePeriod.setExpiryDate(source.getExpiryDate());
			target.setTimePeriod(timePeriod);
			target.setTransactionType(translateTransactionType(source.getTransaction()));
			if(source.isCallingCircle()){
				target.setCallingCirclePhoneNumberList(new PhoneNumberList());
				for(String phoneNumber :source.getCallingCirclePhoneNumbersFromParam()){
				  target.getCallingCirclePhoneNumberList().getPhoneNumber().add(phoneNumber);
				}
				target.setCallingCircleCommitmentAttributeData(
						CallingCircleCommitmentAttributeDataInfoMapper.getInstance().mapToSchema(source.getCallingCircleCommitmentAttributeData0()));
			}
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class PortInEligibilityMapper extends AbstractSchemaMapper<PortInEligibility, PortInEligibilityInfo> {
		private static PortInEligibilityMapper INSTANCE;
		
		private PortInEligibilityMapper() {
			super (PortInEligibility.class, PortInEligibilityInfo.class);
		}
		
		protected static PortInEligibilityMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new PortInEligibilityMapper();
			}
			return INSTANCE;
		}
		
		@Override
		protected PortInEligibilityInfo performDomainMapping(PortInEligibility source, PortInEligibilityInfo target) {
			target.setCDMACoverage(source.isCDMACoverageInd());
			target.setCDMAPostpaidCoverage(source.isCDMAPostPaidCoverageInd());
			target.setCDMAPrepaidCoverage(source.isCDMAPrePaidCoverageInd());
			target.setCurrentServiceProvider(source.getCurrentServiceProviderCd());
			target.setHSPACoverage(source.isHSPACoverageInd());
			target.setHSPAPostpaidCoverage(source.isHSPAPostPaidCoverageInd());
			target.setHSPAPrepaidCoverage(source.isHSPAPrePaidCoverageInd());
			target.setIdenCoverage(source.isIDENCoverageInd());
			target.setOutgoingBrandId(Integer.parseInt(source.getOutgoingBrandCd()));
			target.setIncomingBrandId(Integer.parseInt(source.getIncommingBrandCd()));
			target.setPhoneNumber(source.getPhoneNumberCd());
			target.setPlatformId(Integer.parseInt(source.getPlatformId()));
			target.setPortDirectionIndicator(source.getPortDirectionCd());
			target.setPortVisibility(source.getPortVisibilityCd());
			return super.performDomainMapping(source, target);
		}
	}

	public static class ServicesValidationMapper extends AbstractSchemaMapper<ServicesValidation, ServicesValidationInfo> {
		private static ServicesValidationMapper INSTANCE;
		
		private ServicesValidationMapper() {
			super (ServicesValidation.class, ServicesValidationInfo.class);
		}
		
		protected static ServicesValidationMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ServicesValidationMapper();
			}
			return INSTANCE;
		}
		
		@Override
		protected ServicesValidationInfo performDomainMapping(ServicesValidation source, ServicesValidationInfo target) {
			target.setPricePlanServiceGrouping(source.isValidatePricePlanServiceGroupingInd());
			target.setProvinceServiceMatch(source.isValidateProvinceServiceMatchInd());
			target.setEquipmentServiceMatch(source.isValidateEquipmentServiceMatchInd());
			return super.performDomainMapping(source, target);
		}
	}

	public static class ActivationOptionMapper extends AbstractSchemaMapper<ActivationOption, ActivationOptionInfo> {
		private static ActivationOptionMapper INSTANCE;
		
		private ActivationOptionMapper() {
			super (ActivationOption.class, ActivationOptionInfo.class);
		}
		
		protected static ActivationOptionMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ActivationOptionMapper();
			}
			return INSTANCE;
		}
		
		@Override
		protected ActivationOptionInfo performDomainMapping(ActivationOption source, ActivationOptionInfo target) {
			target.setOptionType(new ActivationOptionTypeInfo(source.getActivationOptionName().value()));
			target.setDeposit(source.getDepositAmount() == null ? 0.0 : source.getDepositAmount());
			target.setCreditLimit(source.getCreditLimitAmount() == null ? 0.0 : source.getCreditLimitAmount());
			target.setCreditClass(source.getCreditClass());
			target.setMaxContractTerm(source.getMaxCLPContractTerm() == null ? 0 : source.getMaxCLPContractTerm());
			return super.performDomainMapping(source, target);
		}
	}
		
	private static TransactionType translateTransactionType(byte transactionType) {
		if (transactionType == BaseAgreementInfo.ADD) {
			return TransactionType.ADD;
		}else if (transactionType == BaseAgreementInfo.DELETE) {
			return TransactionType.REMOVE;
		}else if (transactionType == BaseAgreementInfo.NO_CHG) {
			return TransactionType.NO_CHANGE;
		}else if (transactionType == BaseAgreementInfo.UPDATE) {
			return TransactionType.MODIFY;
		}
		
		return null;
	}
	
	private static byte translateTransactionType (TransactionType transactionType) {
		if (TransactionType.ADD.equals(transactionType)) {
			return BaseAgreementInfo.ADD;
		}else if (TransactionType.MODIFY.equals(transactionType)) {
			return BaseAgreementInfo.UPDATE;
		} else if (TransactionType.NO_CHANGE.equals(transactionType)) {
			return BaseAgreementInfo.NO_CHG;
		}else if (TransactionType.REMOVE.equals(transactionType)) {
			return BaseAgreementInfo.DELETE;
		}
		
		return BaseAgreementInfo.NO_CHG;
	}

	public static void resetPricePlanTransactionType(
			ServiceAgreement serviceAgreement,
			ContractChangeInfo contractChangeInfo) {
		if ( serviceAgreement.getPricePlan()!=null ) {
			if (contractChangeInfo.isPricePlanChangeInd()) {
				serviceAgreement.getPricePlan().setTransactionType(TransactionType.MODIFY);
			} else {
				serviceAgreement.getPricePlan().setTransactionType(TransactionType.NO_CHANGE);
			}
		}
	}
	
}
