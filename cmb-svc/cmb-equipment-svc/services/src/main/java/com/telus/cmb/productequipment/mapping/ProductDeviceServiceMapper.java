package com.telus.cmb.productequipment.mapping;

import java.util.List;

import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.cmb.common.mapping.EnterpriseCommonTypesV9Mapper;
import com.telus.eas.equipment.info.UsimProfileInfo;
import com.telus.eas.equipment.productdevice.info.CatalogueItemCategoryInfo;
import com.telus.eas.equipment.productdevice.info.CatalogueItemInfo;
import com.telus.eas.equipment.productdevice.info.ClassificationInfo;
import com.telus.eas.equipment.productdevice.info.FeatureInfo;
import com.telus.eas.equipment.productdevice.info.ProductCategoryInfo;
import com.telus.eas.equipment.productdevice.info.ProductHeaderInfo;
import com.telus.eas.equipment.productdevice.info.ProductInfo;
import com.telus.eas.equipment.productdevice.info.ProductTechnologyTypeInfo;
import com.telus.tmi.xmlschema.xsd.product.product.productdevicetypes_v4.CatalogueItem;
import com.telus.tmi.xmlschema.xsd.product.product.productdevicetypes_v4.CatalogueItemCategoryList;
import com.telus.tmi.xmlschema.xsd.product.product.productdevicetypes_v4.CatalogueItemCategoryType;
import com.telus.tmi.xmlschema.xsd.product.product.productdevicetypes_v4.Classification;
import com.telus.tmi.xmlschema.xsd.product.product.productdevicetypes_v4.Feature;
import com.telus.tmi.xmlschema.xsd.product.product.productdevicetypes_v4.Product;
import com.telus.tmi.xmlschema.xsd.product.product.productdevicetypes_v4.ProductCategory;
import com.telus.tmi.xmlschema.xsd.product.product.productdevicetypes_v4.ProductHeader;
import com.telus.tmi.xmlschema.xsd.product.product.productdevicetypes_v4.ProductTechnologyType;
import com.telus.tmi.xmlschema.xsd.product.product.productdevicetypes_v4.ProductUSIMProfile;
import com.telus.tmi.xmlschema.xsd.product.product.productdevicetypes_v4.UsimProfile;

/**
 * ProductDeviceServiceMapper
 * @author R. Fong
 *
 */
public class ProductDeviceServiceMapper {

	private static final String CATALOGUE_ITEM_CATEGORY_TYPE_CD_DEVICE_TYPE = "DEVICE_TYPE";
	
	public static ProductMapper ProductMapper() {
		return ProductMapper.getInstance();
	}

	public static ProductHeaderMapper ProductHeaderMapper() {
		return ProductHeaderMapper.getInstance();
	}
	
	public static ProductUSIMProfileMapper ProductUSIMProfileMapper() {
		return ProductUSIMProfileMapper.getInstance();
	}
	
	public static class ProductMapper extends AbstractSchemaMapper<Product, ProductInfo> {

		private static ProductMapper INSTANCE;
		
		private ProductMapper() {
			super(Product.class, ProductInfo.class);
		}

		protected static synchronized ProductMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ProductMapper();
			}
			return INSTANCE;
		}

		@Override
		protected ProductInfo performDomainMapping(Product source, ProductInfo target) {
			
			if (source.getProduct() != null) {
				target.setProduct(ProductHeaderMapper.getInstance().mapToDomain(source.getProduct()));
			}
			if (source.getFeatureList() != null) {
				target.setFeatureList(FeatureMapper.getInstance().mapToDomain(source.getFeatureList()));
			}
			target.setVendorName(source.getVendorName());
			target.setVendorNumber(source.getVendorNum());
			target.setEvdoCapableInd(source.isEvdoCapableInd());
			target.setGpsInd(source.isGpsInd());
			target.setLegacyInd(source.isLegacyInd());
			target.setMmsCapableInd(source.isMmsCapableInd());
			target.setMsBasedEnabledInd(source.isMsBasedEnabledInd());
			target.setPttEnabledInd(source.isPttEnabledInd());
			target.setSmsCapableInd(source.isSmsCapableInd());
			target.setTelephonyEnabledInd(source.isTelephonyEnabledInd());
			target.setVistoCapableInd(source.isVistoCapableInd());
			target.setWirelessWebEnabledInd(source.isWirelessWebEnabledInd());
			target.setAppleInd(source.isAppleInd());
			
			if (source.getProduct().getEsimEnabledCd() != null) {
				target.setSupportedSimType(source.getProduct().getEsimEnabledCd());
			} else {
				target.setSupportedSimType("physical");
			}
			
			CatalogueItemCategoryList catalogueItemCategoryList = source.getProduct().getCatalogueItemCategoryList();
			List<CatalogueItemCategoryType> catalogueItemCategoryTypes = null;
			
			if (catalogueItemCategoryList != null) {
				catalogueItemCategoryTypes = catalogueItemCategoryList.getCategory();
			}
			
			if (catalogueItemCategoryTypes != null && catalogueItemCategoryTypes.size() > 0) {
				for (CatalogueItemCategoryType catalogueItemCategoryType : catalogueItemCategoryTypes) {
					if (CATALOGUE_ITEM_CATEGORY_TYPE_CD_DEVICE_TYPE.equalsIgnoreCase(catalogueItemCategoryType.getCatalogueItemCategoryTypeCd())) {
						target.setDeviceType(catalogueItemCategoryType.getCatalogueItemCategoryCd());
						break;
					}
				}
			}
			
			if (source.getAttachedFeaturedSIMProfile() != null) {
				target.setSimProfileCode(source.getAttachedFeaturedSIMProfile().getAttachedSIMProfileCd());
			}
			
			return super.performDomainMapping(source, target);
		}
	}

	public static class ProductHeaderMapper extends AbstractSchemaMapper<ProductHeader, ProductHeaderInfo> {

		private static ProductHeaderMapper INSTANCE;
		
		private ProductHeaderMapper() {
			super(ProductHeader.class, ProductHeaderInfo.class);
		}

		protected static synchronized ProductHeaderMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ProductHeaderMapper();
			}
			return INSTANCE;
		}

		@Override
		protected ProductHeaderInfo performDomainMapping(ProductHeader source, ProductHeaderInfo target) {
			
			target.setCatalogueItem(CatalogueItemMapper.getInstance().mapToDomain(source.getCatalogueItem()));
			if (source.getCatalogueItemCategoryList() != null) {
				target.setCatalogueItemCategoryList(CatalogueItemCategoryMapper.getInstance().mapToDomain(source.getCatalogueItemCategoryList().getCategory()));
			}
			target.setProductID(source.getProductID());
			target.setProductCode(source.getProductCode());
			if (source.getProductName() != null) {
				target.setProductNameList(EnterpriseCommonTypesV9Mapper.NameMapper().mapToDomain(source.getProductName().getName()));
			}
			if (source.getProductDescription() != null) {
				target.setProductDescriptionList(EnterpriseCommonTypesV9Mapper.DescriptionMapper().mapToDomain(source.getProductDescription().getDescription()));
			}
			target.setUniversalProductCode(source.getUniversalProductCode());
			if (source.getProductEffective() != null) {
				target.setProductEffectiveDate(source.getProductEffective());
			}
			if (source.getProductExpirationDate() != null) {
				target.setProductExpirationDate(source.getProductExpirationDate());
			}
			if (source.getProductLoadDate() != null) {
				target.setProductLoadDate(source.getProductLoadDate());
			}
			if (source.getProductUpdateDate() != null) {
				target.setProductUpdateDate(source.getProductUpdateDate());
			}
			target.setSapMaterialNumber(source.getSapMaterialNum());
			target.setRestockableInd(source.isRestockableInd());
			target.setSwapableInd(source.isSwapableInd());
			target.setSaleableInd(source.isSaleableInd());
			target.setEquipModelInd(source.isEquipModelInd());
			target.setManufacturerProductCode(source.getManufacturerProductCode());
			target.setSerializedInd(source.isSerializedInd());
			target.setPrePostPaidCode(source.getPrepostPaidCd());
			if (source.getTechnologyType() != null) {
				target.setTechnologyType(ProductTechnologyTypeMapper.getInstance().mapToDomain(source.getTechnologyType()));
			}
			target.setProductTypeCode(source.getProductTypeCode());
			target.setEquipmentTypeCode(source.getEquipmentTypeCode());
			target.setSemsEquipmentGroupCode(source.getSemsEquipmentGroupCode());
			target.setBrandID(source.getBrandID());
			target.setBrandCode(source.getBrandCode());
			if (source.getClassification() != null) {
				target.setClassification(ClassificationMapper.getInstance().mapToDomain(source.getClassification()));
			}
			if (source.getCategory() != null) {
				target.setCategory(CategoryMapper.getInstance().mapToDomain(source.getCategory()));
			}			
			target.setStatusCode(source.getStatusCode());
			target.setIdenInd(source.isIdenInd());
			target.setCellularInd(source.isCellularInd());
			target.setCellularDigitalInd(source.isCellularDigitalInd());
			target.setAnalogInd(source.isAnalogInd());
			target.setPcsInd(source.isPcsInd());
			target.setPagerInd(source.isPagerInd());
			target.setOnexRTTInd(source.isOnexRTTInd());
			target.setOnexRTTCardInd(source.isOnexRTTCardInd());
			target.setHandsetInd(source.isHandsetInd());
			target.setSimCardInd(source.isSimCardInd());
			target.setRimInd(source.isRimInd());
			target.setCellularRIMInd(source.isCellularRIMInd());
			target.setIdenRIMInd(source.isIdenRIMInd());
			target.setRuimCardInd(source.isRuimCardInd());
			target.setAssetTagInd(source.isAssetTagInd());
			target.setDataCapableInd(source.isDataCapableInd());
			target.setDataCardInd(source.isDataCardInd());
			target.setDispatchEnabledInd(source.isDispatchEnabledInd());
			target.setMuleInd(source.isMuleInd());
			target.setPdaInd(source.isPdaInd());
			target.setWorldPhoneInd(source.isWorldPhoneInd());
			target.setDataModemInd(source.isDataModemInd());
			target.setDataVoiceModemInd(source.isDataVoiceModemInd());
			target.setUsimCardInd(source.isUsimCardInd());
			target.setHspaEquipmentInd(source.isHspaEquipmentInd());
			target.setManufacturerID(source.getManufacturerId());
			target.setManufacturerName(source.getManufacturerName());
			
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class ProductUSIMProfileMapper extends AbstractSchemaMapper<ProductUSIMProfile, UsimProfileInfo> {
		
		private static ProductUSIMProfileMapper INSTANCE;
		
		private ProductUSIMProfileMapper() {
			super(ProductUSIMProfile.class, UsimProfileInfo.class);
		}

		protected static synchronized ProductUSIMProfileMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ProductUSIMProfileMapper();
			}
			return INSTANCE;
		}

		@Override
		protected UsimProfileInfo performDomainMapping(ProductUSIMProfile source, UsimProfileInfo target) {
			if (source.getUsimProfile() != null) {
				target = UsimProfileMapper.getInstance().mapToDomain(source.getUsimProfile());
			}
			
			target.setProductId(source.getProductID());
			target.setSimType(source.getSimType());
			
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class UsimProfileMapper extends AbstractSchemaMapper<UsimProfile, UsimProfileInfo> {

		private static UsimProfileMapper INSTANCE;
		
		private UsimProfileMapper() {
			super(UsimProfile.class, UsimProfileInfo.class);
		}

		protected static synchronized UsimProfileMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new UsimProfileMapper();
			}
			return INSTANCE;
		}

		@Override
		protected UsimProfileInfo performDomainMapping(UsimProfile source, UsimProfileInfo target) {
			target.setProfileCode(source.getProfileCode());
			target.setProfileDescription(source.getDescription());
			target.setProfileId(String.valueOf(source.getProfileID()));
			target.setProfileVersion(source.getProfileVersion());
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class FeatureMapper extends AbstractSchemaMapper<Feature, FeatureInfo> {

		private static FeatureMapper INSTANCE;
		
		private FeatureMapper() {
			super(Feature.class, FeatureInfo.class);
		}

		protected static synchronized FeatureMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new FeatureMapper();
			}
			return INSTANCE;
		}

		@Override
		protected FeatureInfo performDomainMapping(Feature source, FeatureInfo target) {
			
			target.setFeatureID(source.getFeatureID());
			target.setFeatureCode(source.getFeatureCode());
			target.setFeatureGroupCode(source.getFeatureGroupCode());
			if (source.getFeatureDescription() != null) {
				target.setFeatureDescriptionList(EnterpriseCommonTypesV9Mapper.DescriptionMapper().mapToDomain(source.getFeatureDescription().getDescription()));
			}
			if (source.getFeatureComment() != null) {
				target.setFeatureCommentList(EnterpriseCommonTypesV9Mapper.DescriptionMapper().mapToDomain(source.getFeatureComment().getDescription()));
			}

			return super.performDomainMapping(source, target);
		}
	}
	
	public static class CatalogueItemMapper extends AbstractSchemaMapper<CatalogueItem, CatalogueItemInfo> {

		private static CatalogueItemMapper INSTANCE;
		
		private CatalogueItemMapper() {
			super(CatalogueItem.class, CatalogueItemInfo.class);
		}

		protected static synchronized CatalogueItemMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CatalogueItemMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CatalogueItemInfo performDomainMapping(CatalogueItem source, CatalogueItemInfo target) {
			
			target.setCatalogueItemID(source.getCatalogueItemID());
			target.setCatalogueItemTypeCode(source.getCatalogueItemTypeCode());
			if (source.getCatalogueItemEffectiveDate() != null) {
				target.setCatalogueItemEffectiveDate(source.getCatalogueItemEffectiveDate());
			}
			if (source.getCatalogueItemExpirationDate() != null) {
				target.setCatalogueItemExpirationDate(source.getCatalogueItemExpirationDate());
			}
	
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class CatalogueItemCategoryMapper extends AbstractSchemaMapper<CatalogueItemCategoryType, CatalogueItemCategoryInfo> {

		private static CatalogueItemCategoryMapper INSTANCE;
		
		private CatalogueItemCategoryMapper() {
			super(CatalogueItemCategoryType.class, CatalogueItemCategoryInfo.class);
		}

		protected static synchronized CatalogueItemCategoryMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CatalogueItemCategoryMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CatalogueItemCategoryInfo performDomainMapping(CatalogueItemCategoryType source, CatalogueItemCategoryInfo target) {
			
			target.setCatalogueItemCategoryCode(source.getCatalogueItemCategoryCd());
			target.setCatalogueItemCategoryTypeCode(source.getCatalogueItemCategoryTypeCd());
	
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class ProductTechnologyTypeMapper extends AbstractSchemaMapper<ProductTechnologyType, ProductTechnologyTypeInfo> {

		private static ProductTechnologyTypeMapper INSTANCE;
		
		private ProductTechnologyTypeMapper() {
			super(ProductTechnologyType.class, ProductTechnologyTypeInfo.class);
		}

		protected static synchronized ProductTechnologyTypeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ProductTechnologyTypeMapper();
			}
			return INSTANCE;
		}

		@Override
		protected ProductTechnologyTypeInfo performDomainMapping(ProductTechnologyType source, ProductTechnologyTypeInfo target) {
			
			target.setTechnologyType(source.getTechnologyType());
			if (source.getTechnologyTypeDescription() != null) {
				target.setTechnologyTypeDescriptionList(EnterpriseCommonTypesV9Mapper.DescriptionMapper().mapToDomain(source.getTechnologyTypeDescription().getDescription()));
			}
	
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class ClassificationMapper extends AbstractSchemaMapper<Classification, ClassificationInfo> {

		private static ClassificationMapper INSTANCE;
		
		private ClassificationMapper() {
			super(Classification.class, ClassificationInfo.class);
		}

		protected static synchronized ClassificationMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ClassificationMapper();
			}
			return INSTANCE;
		}

		@Override
		protected ClassificationInfo performDomainMapping(Classification source, ClassificationInfo target) {
			
			target.setClassificationID(source.getClassificationID());
			target.setClassificationCode(source.getCode());
			if (source.getDescription() != null) {
				target.setClassificationDescriptionList(EnterpriseCommonTypesV9Mapper.DescriptionMapper().mapToDomain(source.getDescription().getDescription()));
			}

			return super.performDomainMapping(source, target);
		}
	}
	
	public static class CategoryMapper extends AbstractSchemaMapper<ProductCategory, ProductCategoryInfo> {

		private static CategoryMapper INSTANCE;
		
		private CategoryMapper() {
			super(ProductCategory.class, ProductCategoryInfo.class);
		}

		protected static synchronized CategoryMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CategoryMapper();
			}
			return INSTANCE;
		}

		@Override
		protected ProductCategoryInfo performDomainMapping(ProductCategory source, ProductCategoryInfo target) {
			
			target.setCategoryID(source.getCategoryID());
			if (source.getCategoryDescription() != null) {
				target.setCategoryDescriptionList(EnterpriseCommonTypesV9Mapper.DescriptionMapper().mapToDomain(source.getCategoryDescription().getDescription()));
			}
	
			return super.performDomainMapping(source, target);
		}
	}

}