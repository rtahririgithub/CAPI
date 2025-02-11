package com.telus.cmb.subscriber.mapping;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.MemberIdentityInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.HSPASubscriberInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.tmi.xmlschema.xsd.product.productinstance.customer_product_instance_sub_domain_v3.ProductInstance;
import com.telus.tmi.xmlschema.xsd.product.productinstance.customer_product_instance_sub_domain_v3.ProductParameter;
import com.telus.tmi.xmlschema.xsd.product.productinstance.customer_product_instance_sub_domain_v3.ProductResource;

public class ProductDataMgmtMapper {
	private static final Long KB_MASTER_SOURCE_ID = Long.valueOf(130L);
	private static ReferenceDataFacade refFacade = null;
	private static final Log logger = LogFactory.getLog(ProductDataMgmtMapper.class);
	
	private static ReferenceDataFacade getReferenceDataFacade() {
		if (refFacade == null) {
			refFacade = EJBUtil.getHelperProxy(ReferenceDataFacade.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE);
		}
		
		return refFacade;
	}

	public static class ProductResourceMapper {
		public List<ProductResource> mapDomainToSchema(SubscriberInfo subscriber, SubscriberContractInfo contract, EquipmentInfo equipment) {
			ProductResource[] productResources;
			
			if(subscriber.isIDEN()) {
				productResources = new ProductResource[4];
			}else if (subscriber.isHSPA()){
				productResources = new ProductResource[3];
			}else {
				productResources = new ProductResource[2];
			}
			
			ProductResourceTNMapper tnMapper = new ProductResourceTNMapper();
			productResources[0] = tnMapper.mapToSchema(subscriber);
			
			ProductResourceEquipmentMapper eqMapper = new ProductResourceEquipmentMapper();
			productResources[1] = eqMapper.mapToSchema(equipment);
			productResources[1].setEffectiveStartDate(subscriber.getStartServiceDate());
			
			if (subscriber.isHSPA() || subscriber.isIDEN()) {
				ProductResourceIMSIMapper imsiMapper = new ProductResourceIMSIMapper();
				productResources[2] = imsiMapper.mapToSchema(subscriber);
			}
			
			if (subscriber.isIDEN()) {
				ProductResourceFaxMapper faxMapper = new ProductResourceFaxMapper();
				productResources[3] = faxMapper.mapToSchema(subscriber, contract);
			}
			
			return Arrays.asList(productResources);
		}
	}
	public static class ProductResourceTNMapper extends AbstractSchemaMapper<ProductResource, SubscriberInfo> {

		public ProductResourceTNMapper() {
			super(ProductResource.class, SubscriberInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ProductResource performSchemaMapping(SubscriberInfo source, ProductResource target) {
			target.setMasterSourceId(KB_MASTER_SOURCE_ID.longValue());
			target.setResourceTypeCode("TN");
			target.setResourceValueId(source.getPhoneNumber());
			target.setProductResourceName("Telephone Number");
			target.setEffectiveStartDate(source.getStartServiceDate());
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class ProductResourceEquipmentMapper extends AbstractSchemaMapper<ProductResource, EquipmentInfo> {
		public ProductResourceEquipmentMapper() {
			super (ProductResource.class, EquipmentInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ProductResource performSchemaMapping(EquipmentInfo source, ProductResource target) {
			String equipmentGroupType = null;
			
			target.setMasterSourceId(KB_MASTER_SOURCE_ID.longValue());
			try {
				equipmentGroupType = getReferenceDataFacade().getEquipmentGroupTypeBySEMSEquipmentGroupType(source.getEquipmentGroup());
			} catch (TelusException e) {
				throw new SystemException(SystemCodes.CMB_SLF_EJB, e.getMessage(), "", e);
			}
			target.setResourceTypeCode(equipmentGroupType);
			target.setResourceValueId(source.getSerialNumber());
			target.setProductResourceName("Resource ID - " + equipmentGroupType);
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class ProductResourceIMSIMapper extends AbstractSchemaMapper<ProductResource, SubscriberInfo> {

		public ProductResourceIMSIMapper() {
			super(ProductResource.class, SubscriberInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ProductResource performSchemaMapping(SubscriberInfo source, ProductResource target) {
			target.setMasterSourceId(KB_MASTER_SOURCE_ID.longValue());
			target.setResourceTypeCode("IMSI");
			if (source instanceof HSPASubscriberInfo) {
				target.setResourceValueId(((HSPASubscriberInfo) source).getHspaImsi());
			}else if (source instanceof IDENSubscriberInfo) {
				target.setResourceValueId(((IDENSubscriberInfo) source).getIMSI());
			}
			target.setProductResourceName("Resource ID - IMSI");
			target.setEffectiveStartDate(source.getStartServiceDate());
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class ProductResourceFaxMapper {	
		public ProductResource mapToSchema(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo) {
			
			ProductResource productResource = new ProductResource();
			String faxNumber = subscriberInfo.getFaxNumber();
			
			// based on TMSubscriber.getFaxNumber()
			if ( (faxNumber == null || faxNumber.equals("")) && subscriberInfo.isIDEN()) {
				ServiceFeatureInfo[] contractFeatures = subscriberContractInfo.getFeatures0(false, true);
				for (ServiceFeatureInfo contractFeature : contractFeatures) {
					if ("MFAXM".equals(contractFeature.getCode().trim())) {
						faxNumber = contractFeature.getAdditionalNumber();
						break;
					}
				}
			}
			
			productResource.setResourceValueId(faxNumber);
			productResource.setMasterSourceId(KB_MASTER_SOURCE_ID.longValue());
			productResource.setResourceTypeCode("FAX");
			productResource.setProductResourceName("Resource ID - FAX");
			productResource.setEffectiveStartDate(subscriberInfo.getStartServiceDate());
			
			return productResource;
		}
	}
	
	public static class ProductResourceUFMIMapper extends AbstractSchemaMapper<ProductResource, SubscriberInfo> {
		public ProductResourceUFMIMapper() {
			super (ProductResource.class, SubscriberInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ProductResource performSchemaMapping(SubscriberInfo source, ProductResource target) {
			target.setMasterSourceId(KB_MASTER_SOURCE_ID.longValue());
			target.setResourceTypeCode("UFMI");
			if (source instanceof IDENSubscriberInfo) {
				MemberIdentityInfo memberIdentity = ((IDENSubscriberInfo) source).getMemberIdentity0();
				FleetIdentityInfo fleetIdentity = memberIdentity.getFleetIdentity0();
				String resourceValudId = fleetIdentity.getUrbanId() + fleetIdentity.getFleetId() + memberIdentity.getMemberId();
				target.setResourceValueId(resourceValudId);
			}
			target.setProductResourceName("Resource ID - UFMI");
			target.setEffectiveStartDate(source.getStartServiceDate());
			return super.performSchemaMapping(source, target);
		}	
	}

	public static class ProductResourceIPAddressMapper extends AbstractSchemaMapper<ProductResource, SubscriberInfo> {
		public ProductResourceIPAddressMapper() {
			super (ProductResource.class, SubscriberInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ProductResource performSchemaMapping(SubscriberInfo source, ProductResource target) {
			target.setMasterSourceId(KB_MASTER_SOURCE_ID.longValue());
			target.setResourceTypeCode("IP Address");
			if (source instanceof IDENSubscriberInfo) {
				target.setResourceValueId(((IDENSubscriberInfo) source).getIPAddress());
			}
			target.setProductResourceName("Resource ID - IP Address");
			target.setEffectiveStartDate(source.getStartServiceDate());
			return super.performSchemaMapping(source, target);
		}	
	}

	public static class ProductParameterMapper {
		public List<ProductParameter> mapDomainToSchema(SubscriberInfo source) {
			List<ProductParameter> productParameterList = new ArrayList<ProductParameter>();
			ProductParameter commitmentTerm = mapCommitmentTermDomainToSchema(source);
			ProductParameter commitmentStartDate = mapCommitmentStartDateToSchema(source);
			
			if (commitmentTerm != null) {
				productParameterList.add(commitmentTerm);
			}
			
			if (commitmentStartDate != null) {
				productParameterList.add(commitmentStartDate);
			}
			
			
			return productParameterList;
		}
		
		private ProductParameter mapCommitmentTermDomainToSchema(SubscriberInfo source) {
			ProductParameter productParameter = new ProductParameter();
			productParameter.setMasterSourceId(KB_MASTER_SOURCE_ID.longValue());
			productParameter.setParameterName("Commitment period");
			productParameter.setParameterValue(String.valueOf(source.getCommitment().getMonths()));

			return productParameter;
		}
		
		private ProductParameter mapCommitmentStartDateToSchema(SubscriberInfo source) {
			if (source.getCommitment().getStartDate() != null) {
				ProductParameter productParameter = new ProductParameter();
				productParameter.setMasterSourceId(KB_MASTER_SOURCE_ID.longValue());
				productParameter.setParameterName("Commitment start date");
				productParameter.setParameterValue(source.getCommitment().getStartDate().toString());

				return productParameter;
			}
			
			return null;
		}
	}
	
	public static class ProductCustomerSubscriptionMapper extends AbstractSchemaMapper<ProductInstance, ServiceAgreementInfo>{ 
		public ProductCustomerSubscriptionMapper() {
			super (ProductInstance.class, ServiceAgreementInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ProductInstance performSchemaMapping(ServiceAgreementInfo source, ProductInstance target) {
			if (source.isWPS() == false) {
			target.setMasterSourceId(Integer.valueOf((KB_MASTER_SOURCE_ID.intValue())));
			target.setKeyId(source.getCode().trim()+"-"+source.getServiceSequenceNo()+"-"+source.getServiceVersionNo());
			try {
				target.setProductTypeCode(getReferenceDataFacade().getSubscriptionTypeByKBServiceType(source.getServiceType()));
			} catch (TelusException e) {
				throw new SystemException(SystemCodes.CMB_SLF_EJB, e.getMessage(), "", e);
			}
			target.setProductCode(source.getCode().trim());
			target.setProductCodeSourceSystemId(KB_MASTER_SOURCE_ID);
			target.setEffectiveStartDate(source.getEffectiveDate());
			}
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class ProductInstanceMapper {
		public ProductInstance mapDomainToSchema (SubscriberInfo subscriber, EquipmentInfo equipment, SubscriberContractInfo contract) throws ApplicationException {
			String strBan = String.valueOf(subscriber.getBanId());
			ProductInstance productInstance = new ProductInstance();
			productInstance.setBillingAccountNumber(strBan);
			productInstance.setMasterSourceId(Integer.valueOf((KB_MASTER_SOURCE_ID.intValue())));
			productInstance.setBillingMasterSourceId(KB_MASTER_SOURCE_ID);
			productInstance.setProductCodeSourceSystemId(KB_MASTER_SOURCE_ID);
			productInstance.setKeyId(subscriber.getSubscriptionId() == 0 ? "" : String.valueOf(subscriber.getSubscriptionId()));
			productInstance.setProductTypeCode("C");
			Date startServiceDate = subscriber.getStartServiceDate();
			productInstance.setActivationDate(startServiceDate);
			try {
				productInstance.setStatusCode(getReferenceDataFacade().getServiceInstanceStatusByKBSubscriberStatus(String.valueOf(subscriber.getStatus())));
			} catch (TelusException e) {
				throw new SystemException(SystemCodes.CMB_SLF_EJB, e.getMessage(), "", e);
			}

			productInstance.setStatusDate(startServiceDate);
			productInstance.setStatusUpdateDate(subscriber.getStatusDate());
			productInstance.setPrimaryServiceResourceTypeCode("TN");
			productInstance.setPrimaryServiceResourceValue(subscriber.getPhoneNumber());
			productInstance.setPicStatusCode("T");
			productInstance.setBrandId(BigInteger.valueOf(subscriber.getBrandId()));
			productInstance.setSubscriberNumber(subscriber.getSubscriberId());
			
			ProductParameterMapper productParameterMapper = new ProductParameterMapper();
			List<ProductParameter> productParameters = productParameterMapper.mapDomainToSchema(subscriber);
			productInstance.setProductParameterList(productParameters);
			
			ProductResourceMapper productResourceMapper = new ProductResourceMapper();
			productInstance.setProductResourceList(productResourceMapper.mapDomainToSchema(subscriber, contract, equipment));
			
			if (contract != null) {
				/** Map price plan info **/
				ProductInstance ppProductInstance = new ProductInstance();
				ppProductInstance.setMasterSourceId(Integer.valueOf((KB_MASTER_SOURCE_ID.intValue())));
				
				if (contract.getPricePlanCode() == null) {
					logger.error("Price plan code is null for subscriber [" + subscriber.getPhoneNumber() +"]");
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Price plan code is null.", "");
				}
				ppProductInstance.setKeyId(contract.getPricePlanCode().trim() + "-" + contract.getServiceSequenceNo() + "-" + contract.getServiceVersionNo());
				try {
					ppProductInstance.setProductTypeCode(getReferenceDataFacade().getSubscriptionTypeByKBServiceType(contract.getPricePlanServiceType()));
				} catch (TelusException e) {
					throw new SystemException(SystemCodes.CMB_SLF_EJB, e.getMessage(), "", e);
				}
				ppProductInstance.setProductCode(contract.getPricePlanCode().trim());
				ppProductInstance.setProductCodeSourceSystemId(KB_MASTER_SOURCE_ID);
				ppProductInstance.setEffectiveStartDate(contract.getEffectiveDate());
				/** End map price plan info **/

				ProductCustomerSubscriptionMapper pcsMapper = new ProductCustomerSubscriptionMapper();
				List<ProductInstance> productInstanceList = new ArrayList<ProductInstance>();
				productInstanceList.add(ppProductInstance);

				ServiceAgreementInfo[] services = contract.getServices0(false);
				ArrayList<ServiceAgreementInfo> filteredServicesList = new ArrayList<ServiceAgreementInfo>();
				for (ServiceAgreementInfo service : services) {
					if (service.isWPS() == false) { // Prepaid Optional services
													// which are not stored in
													// KB should not be mapped
						filteredServicesList.add(service);
					}
				}
				productInstanceList.addAll(pcsMapper.mapToSchema(filteredServicesList)); // map subscriber services
				for (ProductInstance pi : productInstanceList) {
					pi.setBillingMasterSourceId(KB_MASTER_SOURCE_ID);
					pi.setBillingAccountNumber(strBan);
				}
				productInstance.setComponentList(productInstanceList);
			}else {
				logger.info("Contract is null for subscriber ["+subscriber.getPhoneNumber()+"]");
			}
			
			return productInstance;
		}
	}
}
