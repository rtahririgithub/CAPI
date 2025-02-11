package com.telus.cmb.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.subscriber.management.common_types_10.CommitmentMapper;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.message.info.ApplicationMessageInfo;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
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
import com.telus.cmb.jws.WarningBaseType;
import com.telus.cmb.jws.WarningType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v1.Equipment;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v1.EquipmentChangeRequest;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v1.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.AutoRenewType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.Commitment;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.ContractFeature;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.ContractService;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.MultiRingPhoneNumberType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.PrepaidPropertyListType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.PricePlan;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.PricePlanValidationOverrideIndicatorList;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.TimePeriod;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.TransactionType;

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
	
	/**
	 * ApplicationMessageMapper
	 * @author tongts
	 *
	 */
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
		
	/**
	 * EquipmentChangeRequestMapper
	 * @author tongts
	 *
	 */
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
			return super.performSchemaMapping(source, target);
		}
	}
	
	/**
	 * ServiceWarningMapper
	 * @author tongts
	 *
	 */
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
	
	/**
	 * WarningBaseTypeMapper
	 * @author tongts
	 *
	 */
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
	
	/**
	 * ServiceAgreementMapper
	 * 
	 * @author tongts
	 *
	 */
	public static class ServiceAgreementMapper extends AbstractSchemaMapper<ServiceAgreement, SubscriberContractInfo> {
		private static ServiceAgreementMapper INSTANCE;

		protected ServiceAgreementMapper() {
			super (ServiceAgreement.class, SubscriberContractInfo.class);
		}
		
		protected synchronized static ServiceAgreementMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ServiceAgreementMapper();
			}
			
			return INSTANCE;
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
					target.getServiceList().add(ContractServiceMapper.getInstance().mapToSchema(cs));
				}
			}
			return super.performSchemaMapping(source, target);
		}
	}
	
	/**
	 * PricePlanMapper
	 * @author tongts
	 *
	 */
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
		protected PricePlan performSchemaMapping(SubscriberContractInfo source, PricePlan target) {
			target.setCode(source.getCode());
//			target.setServiceType(source.getPricePlan0().getServiceType());
			target.setServiceType(source.getPricePlanServiceType());
			TimePeriod timePeriod = new TimePeriod();
			timePeriod.setEffectiveDate(source.getEffectiveDate());
			timePeriod.setExpiryDate(source.getExpiryDate());
			target.setTimePeriod(timePeriod);
			target.setTransactionType(translateTransactionType(source.getTransaction()));			
			ServiceFeatureInfo[] features = source.getFeatures0(true);
			if (features != null) {
				for (ServiceFeatureInfo serviceFeature : features) {
					target.getFeatureList().add(ContractFeatureMapper.getInstance().mapToSchema(serviceFeature));
				}
			}
			
			return super.performSchemaMapping(source, target);
		}
	}
	
	/**
	 * ContractChangeInfoMapper
	 * @author tongts
	 *
	 */
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
				
				if (pp.getFeatureList() != null) {
					List<ContractFeature> featureList = pp.getFeatureList();
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
			if (source.getServiceList() != null) {
				List<ContractService> contractServiceList = source.getServiceList();
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

					List<ContractFeature> contractFeatureList = cs.getFeatureList();
					List<FeatureChangeInfo> featureChangeInfoList = FeatureChangeInfoMapper.getInstance().mapToDomain(contractFeatureList);
					serviceChangeInfo.setFeatureChangeInfoList(featureChangeInfoList.toArray(new FeatureChangeInfo[0]));
					serviceChangeInfo.setPrepaidServicePropertyInfo(PrepaidServicePropertyInfoMapper.getInstance().mapToDomain(cs.getPrepaidPropertyList()));
					serviceChangeInfo.setServiceType(cs.getServiceType());
					serviceChangeInfoList.add(serviceChangeInfo);
				}
				target.setServiceChangeInfoList(serviceChangeInfoList.toArray(new ServiceChangeInfo[0]));
			}
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
			target.setFeatureParameter(source.getFeatureParameter());
			
			ServiceFeatureInfo newServiceFeatureInfo = new ServiceFeatureInfo();
			if (source.getTimePeriod() != null) {
				newServiceFeatureInfo.setEffectiveDate(source.getTimePeriod().getEffectiveDate());
				newServiceFeatureInfo.setExpiryDate(source.getTimePeriod().getExpiryDate());
			}
			newServiceFeatureInfo.setFeatureCode(source.getCode());
			if (source.getTransactionType() != null) {
				newServiceFeatureInfo.setTransaction((byte) source.getTransactionType().value().charAt(0));
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

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
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
			target.setServiceType(source.getServiceType());
			TimePeriod timePeriod = new TimePeriod();
			timePeriod.setEffectiveDate(source.getEffectiveDate());
			timePeriod.setExpiryDate(source.getExpiryDate());
			target.setTimePeriod(timePeriod);
			target.setTransactionType(translateTransactionType(source.getTransaction()));
			ServiceFeatureInfo[] features = source.getFeatures0(true);
			if (features != null) {
				for (ServiceFeatureInfo serviceFeature : features) {
					target.getFeatureList().add(ContractFeatureMapper.getInstance().mapToSchema(serviceFeature));
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

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ContractFeature performSchemaMapping(ServiceFeatureInfo source, ContractFeature target) {
			target.setCode(source.getCode());
			target.setFeatureParameter(source.getParameter());
			TimePeriod timePeriod = new TimePeriod();
			timePeriod.setEffectiveDate(source.getEffectiveDate());
			timePeriod.setExpiryDate(source.getExpiryDate());
			target.setTimePeriod(timePeriod);
			target.setTransactionType(translateTransactionType(source.getTransaction()));
			return super.performSchemaMapping(source, target);
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
	
}
