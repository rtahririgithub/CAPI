package com.telus.eas.equipment.productdevice.info;

import java.util.Date;
import java.util.List;

import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.MultilingualTextInfo;

public class ProductHeaderInfo extends Info {

	static final long serialVersionUID = 1L;

	private CatalogueItemInfo catalogueItem;
	private List<CatalogueItemCategoryInfo> catalogueItemCategoryList;
	private long productID;
	private String productCode;
	private List<MultilingualTextInfo> productNameList;
	private List<MultilingualTextInfo> productDescriptionList;
	private String universalProductCode;
	private Date productEffectiveDate;
	private Date productExpirationDate;
	private Date productLoadDate;
	private Date productUpdateDate;
	private String sapMaterialNumber;
	private boolean restockableInd;
	private boolean swapableInd;
	private boolean saleableInd;
	private boolean equipModelInd;
	private String manufacturerProductCode;
	private boolean serializedInd;
	private String prePostPaidCode;
	private ProductTechnologyTypeInfo technologyType;
	private String productTypeCode;
	private String equipmentTypeCode;
	private String semsEquipmentGroupCode;
	private long brandID;
	private String brandCode;
	private ClassificationInfo classification;
	private ProductCategoryInfo category;
	private String statusCode;
	private boolean idenInd;
	private boolean cellularInd;
	private boolean cellularDigitalInd;
	private boolean analogInd;
	private boolean pcsInd;
	private boolean pagerInd;
	private boolean onexRTTInd;
	private boolean onexRTTCardInd;
	private boolean handsetInd;
	private boolean simCardInd;
	private boolean rimInd;
	private boolean cellularRIMInd;
	private boolean idenRIMInd;
	private boolean ruimCardInd;
	private boolean assetTagInd;
	private boolean dataCapableInd;
	private boolean dataCardInd;
	private boolean dispatchEnabledInd;
	private boolean muleInd;
	private boolean pdaInd;
	private boolean worldPhoneInd;
	private boolean dataModemInd;
	private boolean dataVoiceModemInd;
	private boolean usimCardInd;
	private boolean hspaEquipmentInd;
	private long manufacturerID;
	private String manufacturerName;

	public ProductHeaderInfo() { }

	public CatalogueItemInfo getCatalogueItem() {
		return catalogueItem;
	}

	public void setCatalogueItem(CatalogueItemInfo catalogueItem) {
		this.catalogueItem = catalogueItem;
	}

	public List<CatalogueItemCategoryInfo> getCatalogueItemCategoryList() {
		return catalogueItemCategoryList;
	}

	public void setCatalogueItemCategoryList(List<CatalogueItemCategoryInfo> catalogueItemCategoryList) {
		this.catalogueItemCategoryList = catalogueItemCategoryList;
	}

	public long getProductID() {
		return productID;
	}

	public void setProductID(long productID) {
		this.productID = productID;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public List<MultilingualTextInfo> getProductNameList() {
		return productNameList;
	}

	public void setProductNameList(List<MultilingualTextInfo> productNameList) {
		this.productNameList = productNameList;
	}

	public List<MultilingualTextInfo> getProductDescriptionList() {
		return productDescriptionList;
	}

	public void setProductDescriptionList(List<MultilingualTextInfo> productDescriptionList) {
		this.productDescriptionList = productDescriptionList;
	}

	public String getUniversalProductCode() {
		return universalProductCode;
	}

	public void setUniversalProductCode(String universalProductCode) {
		this.universalProductCode = universalProductCode;
	}

	public Date getProductEffectiveDate() {
		return productEffectiveDate;
	}

	public void setProductEffectiveDate(Date productEffectiveDate) {
		this.productEffectiveDate = productEffectiveDate;
	}

	public Date getProductExpirationDate() {
		return productExpirationDate;
	}

	public void setProductExpirationDate(Date productExpirationDate) {
		this.productExpirationDate = productExpirationDate;
	}

	public Date getProductLoadDate() {
		return productLoadDate;
	}

	public void setProductLoadDate(Date productLoadDate) {
		this.productLoadDate = productLoadDate;
	}

	public Date getProductUpdateDate() {
		return productUpdateDate;
	}

	public void setProductUpdateDate(Date productUpdateDate) {
		this.productUpdateDate = productUpdateDate;
	}

	public String getSapMaterialNumber() {
		return sapMaterialNumber;
	}

	public void setSapMaterialNumber(String sapMaterialNumber) {
		this.sapMaterialNumber = sapMaterialNumber;
	}

	public boolean isRestockableInd() {
		return restockableInd;
	}

	public void setRestockableInd(boolean restockableInd) {
		this.restockableInd = restockableInd;
	}

	public boolean isSwapableInd() {
		return swapableInd;
	}

	public void setSwapableInd(boolean swapableInd) {
		this.swapableInd = swapableInd;
	}

	public boolean isSaleableInd() {
		return saleableInd;
	}

	public void setSaleableInd(boolean saleableInd) {
		this.saleableInd = saleableInd;
	}

	public boolean isEquipModelInd() {
		return equipModelInd;
	}

	public void setEquipModelInd(boolean equipModelInd) {
		this.equipModelInd = equipModelInd;
	}

	public String getManufacturerProductCode() {
		return manufacturerProductCode;
	}

	public void setManufacturerProductCode(String manufacturerProductCode) {
		this.manufacturerProductCode = manufacturerProductCode;
	}

	public boolean isSerializedInd() {
		return serializedInd;
	}

	public void setSerializedInd(boolean serializedInd) {
		this.serializedInd = serializedInd;
	}

	public String getPrePostPaidCode() {
		return prePostPaidCode;
	}

	public void setPrePostPaidCode(String prePostPaidCode) {
		this.prePostPaidCode = prePostPaidCode;
	}

	public ProductTechnologyTypeInfo getTechnologyType() {
		return technologyType;
	}

	public void setTechnologyType(ProductTechnologyTypeInfo technologyType) {
		this.technologyType = technologyType;
	}

	public String getProductTypeCode() {
		return productTypeCode;
	}

	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}

	public String getEquipmentTypeCode() {
		return equipmentTypeCode;
	}

	public void setEquipmentTypeCode(String equipmentTypeCode) {
		this.equipmentTypeCode = equipmentTypeCode;
	}

	public String getSemsEquipmentGroupCode() {
		return semsEquipmentGroupCode;
	}

	public void setSemsEquipmentGroupCode(String semsEquipmentGroupCode) {
		this.semsEquipmentGroupCode = semsEquipmentGroupCode;
	}

	public long getBrandID() {
		return brandID;
	}

	public void setBrandID(long brandID) {
		this.brandID = brandID;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public ClassificationInfo getClassification() {
		return classification;
	}

	public void setClassification(ClassificationInfo classification) {
		this.classification = classification;
	}

	public ProductCategoryInfo getCategory() {
		return category;
	}

	public void setCategory(ProductCategoryInfo category) {
		this.category = category;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isIdenInd() {
		return idenInd;
	}

	public void setIdenInd(boolean idenInd) {
		this.idenInd = idenInd;
	}

	public boolean isCellularInd() {
		return cellularInd;
	}

	public void setCellularInd(boolean cellularInd) {
		this.cellularInd = cellularInd;
	}

	public boolean isCellularDigitalInd() {
		return cellularDigitalInd;
	}

	public void setCellularDigitalInd(boolean cellularDigitalInd) {
		this.cellularDigitalInd = cellularDigitalInd;
	}

	public boolean isAnalogInd() {
		return analogInd;
	}

	public void setAnalogInd(boolean analogInd) {
		this.analogInd = analogInd;
	}

	public boolean isPcsInd() {
		return pcsInd;
	}

	public void setPcsInd(boolean pcsInd) {
		this.pcsInd = pcsInd;
	}

	public boolean isPagerInd() {
		return pagerInd;
	}

	public void setPagerInd(boolean pagerInd) {
		this.pagerInd = pagerInd;
	}

	public boolean isOnexRTTInd() {
		return onexRTTInd;
	}

	public void setOnexRTTInd(boolean onexRTTInd) {
		this.onexRTTInd = onexRTTInd;
	}

	public boolean isOnexRTTCardInd() {
		return onexRTTCardInd;
	}

	public void setOnexRTTCardInd(boolean onexRTTCardInd) {
		this.onexRTTCardInd = onexRTTCardInd;
	}

	public boolean isHandsetInd() {
		return handsetInd;
	}

	public void setHandsetInd(boolean handsetInd) {
		this.handsetInd = handsetInd;
	}

	public boolean isSimCardInd() {
		return simCardInd;
	}

	public void setSimCardInd(boolean simCardInd) {
		this.simCardInd = simCardInd;
	}

	public boolean isRimInd() {
		return rimInd;
	}

	public void setRimInd(boolean rimInd) {
		this.rimInd = rimInd;
	}

	public boolean isCellularRIMInd() {
		return cellularRIMInd;
	}

	public void setCellularRIMInd(boolean cellularRIMInd) {
		this.cellularRIMInd = cellularRIMInd;
	}

	public boolean isIdenRIMInd() {
		return idenRIMInd;
	}

	public void setIdenRIMInd(boolean idenRIMInd) {
		this.idenRIMInd = idenRIMInd;
	}

	public boolean isRuimCardInd() {
		return ruimCardInd;
	}

	public void setRuimCardInd(boolean ruimCardInd) {
		this.ruimCardInd = ruimCardInd;
	}

	public boolean isAssetTagInd() {
		return assetTagInd;
	}

	public void setAssetTagInd(boolean assetTagInd) {
		this.assetTagInd = assetTagInd;
	}

	public boolean isDataCapableInd() {
		return dataCapableInd;
	}

	public void setDataCapableInd(boolean dataCapableInd) {
		this.dataCapableInd = dataCapableInd;
	}

	public boolean isDataCardInd() {
		return dataCardInd;
	}

	public void setDataCardInd(boolean dataCardInd) {
		this.dataCardInd = dataCardInd;
	}

	public boolean isDispatchEnabledInd() {
		return dispatchEnabledInd;
	}

	public void setDispatchEnabledInd(boolean dispatchEnabledInd) {
		this.dispatchEnabledInd = dispatchEnabledInd;
	}

	public boolean isMuleInd() {
		return muleInd;
	}

	public void setMuleInd(boolean muleInd) {
		this.muleInd = muleInd;
	}

	public boolean isPdaInd() {
		return pdaInd;
	}

	public void setPdaInd(boolean pdaInd) {
		this.pdaInd = pdaInd;
	}

	public boolean isWorldPhoneInd() {
		return worldPhoneInd;
	}

	public void setWorldPhoneInd(boolean worldPhoneInd) {
		this.worldPhoneInd = worldPhoneInd;
	}

	public boolean isDataModemInd() {
		return dataModemInd;
	}

	public void setDataModemInd(boolean dataModemInd) {
		this.dataModemInd = dataModemInd;
	}

	public boolean isDataVoiceModemInd() {
		return dataVoiceModemInd;
	}

	public void setDataVoiceModemInd(boolean dataVoiceModemInd) {
		this.dataVoiceModemInd = dataVoiceModemInd;
	}

	public boolean isUsimCardInd() {
		return usimCardInd;
	}

	public void setUsimCardInd(boolean usimCardInd) {
		this.usimCardInd = usimCardInd;
	}

	public boolean isHspaEquipmentInd() {
		return hspaEquipmentInd;
	}

	public void setHspaEquipmentInd(boolean hspaEquipmentInd) {
		this.hspaEquipmentInd = hspaEquipmentInd;
	}

	public long getManufacturerID() {
		return manufacturerID;
	}

	public void setManufacturerID(long manufacturerID) {
		this.manufacturerID = manufacturerID;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();

		s.append("ProductHeaderInfo: {\n");
		s.append("    catalogueItem=[").append(getCatalogueItem()).append("]\n");
		s.append("    catalogueItemCategoryList=[\n");
		for (CatalogueItemCategoryInfo info : getCatalogueItemCategoryList()) {
			s.append(info).append("\n");
		}
		s.append("    ]\n");
		s.append("    productID=[").append(getProductID()).append("]\n");
		s.append("    productCode=[").append(getProductCode()).append("]\n");
		s.append("    productNameList=[\n");
		for (MultilingualTextInfo info : getProductNameList()) {
			s.append(info).append("\n");
		}
		s.append("    ]\n");
		s.append("    productDescriptionList=[\n");
		for (MultilingualTextInfo info : getProductDescriptionList()) {
			s.append(info).append("\n");
		}
		s.append("    ]\n");
		s.append("    universalProductCode=[").append(getUniversalProductCode()).append("]\n");
		s.append("    productEffectiveDate=[").append(getProductEffectiveDate()).append("]\n");
		s.append("    productExpirationDate=[").append(getProductExpirationDate()).append("]\n");
		s.append("    productLoadDate=[").append(getProductLoadDate()).append("]\n");
		s.append("    productUpdateDate=[").append(getProductUpdateDate()).append("]\n");
		s.append("    sapMaterialNumber=[").append(getSapMaterialNumber()).append("]\n");		
		s.append("    restockableInd=[").append(isRestockableInd()).append("]\n");
		s.append("    swapableInd=[").append(isSwapableInd()).append("]\n");
		s.append("    saleableInd=[").append(isSaleableInd()).append("]\n");
		s.append("    equipModelInd=[").append(isEquipModelInd()).append("]\n");
		s.append("    manufacturerProductCode=[").append(getManufacturerProductCode()).append("]\n");
		s.append("    serializedInd=[").append(isSerializedInd()).append("]\n");
		s.append("    prePostPaidCode=[").append(getPrePostPaidCode()).append("]\n");
		s.append("    technologyType=[").append(getTechnologyType()).append("]\n");
		s.append("    productTypeCode=[").append(getProductTypeCode()).append("]\n");
		s.append("    equipmentTypeCode=[").append(getEquipmentTypeCode()).append("]\n");
		s.append("    semsEquipmentGroupCode=[").append(getSemsEquipmentGroupCode()).append("]\n");
		s.append("    brandID=[").append(getBrandID()).append("]\n");		
		s.append("    brandCode=[").append(getBrandCode()).append("]\n");
		s.append("    classification=[").append(getClassification()).append("]\n");
		s.append("    category=[").append(getCategory()).append("]\n");
		s.append("    statusCode=[").append(getStatusCode()).append("]\n");
		s.append("    idenInd=[").append(isIdenInd()).append("]\n");
		s.append("    cellularInd=[").append(isCellularInd()).append("]\n");		
		s.append("    cellularDigitalInd=[").append(isCellularDigitalInd()).append("]\n");
		s.append("    analogInd=[").append(isAnalogInd()).append("]\n");
		s.append("    pcsInd=[").append(isPcsInd()).append("]\n");
		s.append("    pagerInd=[").append(isPagerInd()).append("]\n");
		s.append("    onexRTTInd=[").append(isOnexRTTInd()).append("]\n");
		s.append("    onexRTTCardInd=[").append(isOnexRTTCardInd()).append("]\n");		
		s.append("    handsetInd=[").append(isHandsetInd()).append("]\n");
		s.append("    simCardInd=[").append(isSimCardInd()).append("]\n");
		s.append("    rimInd=[").append(isRimInd()).append("]\n");
		s.append("    cellularRIMInd=[").append(isCellularRIMInd()).append("]\n");
		s.append("    idenRIMInd=[").append(isIdenRIMInd()).append("]\n");
		s.append("    ruimCardInd=[").append(isRuimCardInd()).append("]\n");
		s.append("    assetTagInd=[").append(isAssetTagInd()).append("]\n");
		s.append("    dataCapableInd=[").append(isDataCapableInd()).append("]\n");
		s.append("    dataCardInd=[").append(isDataCardInd()).append("]\n");
		s.append("    dispatchEnabledInd=[").append(isDispatchEnabledInd()).append("]\n");
		s.append("    muleInd=[").append(isMuleInd()).append("]\n");
		s.append("    pdaInd=[").append(isPdaInd()).append("]\n");		
		s.append("    worldPhoneInd=[").append(isWorldPhoneInd()).append("]\n");
		s.append("    dataModemInd=[").append(isDataModemInd()).append("]\n");
		s.append("    dataVoiceModemInd=[").append(isDataVoiceModemInd()).append("]\n");
		s.append("    usimCardInd=[").append(isUsimCardInd()).append("]\n");
		s.append("    hspaEquipmentInd=[").append(isHspaEquipmentInd()).append("]\n");
		s.append("    manufacturerID=[").append(getManufacturerID()).append("]\n");
		s.append("    manufacturerName=[").append(getManufacturerName()).append("]\n");
		s.append("}");

		return s.toString();
	}

	

}