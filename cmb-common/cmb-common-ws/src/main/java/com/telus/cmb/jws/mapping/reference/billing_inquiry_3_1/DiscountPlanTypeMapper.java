package com.telus.cmb.jws.mapping.reference.billing_inquiry_3_1;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.DiscountPlanInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.DiscountPlanType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.DiscountPlanType.BrandIdList;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.DiscountPlanType.DiscountGroupList;

public class DiscountPlanTypeMapper extends ReferenceMapper <DiscountPlanType, DiscountPlanInfo> {

	public DiscountPlanTypeMapper() {
		super(DiscountPlanType.class,DiscountPlanInfo.class);
	}
	
	@Override
	protected DiscountPlanType performSchemaMapping(DiscountPlanInfo source, DiscountPlanType target) {
		
		target.setAmount(source.getAmount());
		
		BrandIdList brandIdList = new BrandIdList();
		int[] discountBrandIds = source.getDiscountBrandIDs();
		
		if(discountBrandIds != null){
			for(int i=0; i<discountBrandIds.length; i++) {
				brandIdList.getBrandId().add(discountBrandIds[i]);
			}
			target.setBrandIdList(brandIdList);
		}
					
		DiscountGroupList discountGroupList = new DiscountGroupList();
		
		String[] discountGroups = source.getGroupCodes();
		if(discountGroups != null) {
			for(String discountGroup:source.getGroupCodes()){
				discountGroupList.getDiscountGroup().add(discountGroup);
			}
			target.setDiscountGroupList(discountGroupList);
		}
		
		target.setEffectiveDate(source.getEffectiveDate());
		target.setExpirationDate(source.getExpiration());
		target.setLevel(source.getLevel());
		target.setMonths(source.getMonths());
		target.setOfferExpirationDate(source.getOfferExpirationDate());
		target.setPercent(source.getPercent());
		target.setPricePlanInd(source.isPricePlanDiscount());
		target.setProductType(source.getProductType());
		
		return super.performSchemaMapping(source, target);
	}

}
