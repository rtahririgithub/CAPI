package com.telus.eas.equipment.info;

import java.sql.Timestamp;

import com.telus.eas.framework.info.Info;

public class CiPromotionInfo extends Info {

	private Long catalogueItemPromotionId;
	private Long promotionId;
	private Timestamp effectiveTs;
	private Timestamp expirationTs;
	private String promotionTypeDes;
	private String promotionTypeDesFrench;
	
	public Long getCatalogueItemPromotionId() {
		return catalogueItemPromotionId;
	}
	public void setCatalogueItemPromotionId(Long catalogueItemPromotionId) {
		this.catalogueItemPromotionId = catalogueItemPromotionId;
	}
	public Timestamp getEffectiveTs() {
		return effectiveTs;
	}
	public void setEffectiveTs(Timestamp effectiveTs) {
		this.effectiveTs = effectiveTs;
	}
	public Timestamp getExpirationTs() {
		return expirationTs;
	}
	public void setExpirationTs(Timestamp expirationTs) {
		this.expirationTs = expirationTs;
	}
	public Long getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}
	public String getPromotionTypeDes() {
		return promotionTypeDes;
	}
	public void setPromotionTypeDes(String promotionTypeDes) {
		this.promotionTypeDes = promotionTypeDes;
	}
	public String getPromotionTypeDesFrench() {
		return promotionTypeDesFrench;
	}
	public void setPromotionTypeDesFrench(String promotionTypeDesFrench) {
		this.promotionTypeDesFrench = promotionTypeDesFrench;
	}
	
	
}
