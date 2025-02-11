package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.framework.info.MemoInfo;
//import com.telus.eas.portability.info.PortOutEligibilityInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v1.PortOutEligibilityInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v1.SubscriberMemo;

public class SubscriberInformationServiceMapper {

	public static SubscriberMemoMapper SubscriberMemoMapper() {
		return SubscriberMemoMapper.getInstance();
	}
	
	/**
	 * SubscriberMemoMapper
	 * @author tongts
	 *
	 */
	public static class SubscriberMemoMapper extends AbstractSchemaMapper<SubscriberMemo, MemoInfo>  {
		private static SubscriberMemoMapper INSTANCE;
		
		private SubscriberMemoMapper() {
			super (SubscriberMemo.class, MemoInfo.class);
		}
		
		protected static synchronized SubscriberMemoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new SubscriberMemoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected MemoInfo performDomainMapping(SubscriberMemo source, MemoInfo target) {
			target.setDate(source.getCreationDate());
			if (source.getMemoId() != null) {
				target.setMemoId(source.getMemoId().doubleValue());
			}
			target.setText(source.getMemoText());
			target.setMemoType(source.getMemoType());
			if (source.getModifyDate() != null) {
				target.setModifyDate(source.getModifyDate());
			}
			if (source.getOperatorId() != null) {
				target.setOperatorId(Integer.parseInt(source.getOperatorId()));
			}
			
			target.setProductType(source.getProductType());
			target.setSubscriberId(source.getSubscriberId());
			target.setSystemText(source.getSystemText());
			return super.performDomainMapping(source, target);
		}

		@Override
		protected SubscriberMemo performSchemaMapping(MemoInfo source, SubscriberMemo target) {
			target.setCreationDate(source.getDate());
			target.setMemoId(Double.valueOf(source.getMemoId()));
			target.setMemoText(source.getText());
			target.setMemoType(source.getMemoType());
			target.setModifyDate(source.getModifyDate());
			target.setOperatorId(String.valueOf(source.getOperatorId()));
			target.setProductType(source.getProductType());
			target.setSubscriberId(source.getSubscriberId());
			target.setSystemText(source.getSystemText());
			return super.performSchemaMapping(source, target);
		}

		
	}


	public static SubscriberPortOutEligibilityInfoMapper SubscriberPortOutEligibilityInfoMapper() {
		return SubscriberPortOutEligibilityInfoMapper.getInstance();
	}
	
	/**
	 * SubscriberPortOutEligibilityInfoMapper
	 * @author t873127
	 *
	 */
	public static class SubscriberPortOutEligibilityInfoMapper extends AbstractSchemaMapper<PortOutEligibilityInfo, com.telus.eas.portability.info.PortOutEligibilityInfo>  {
		private static SubscriberPortOutEligibilityInfoMapper INSTANCE;
		
		private SubscriberPortOutEligibilityInfoMapper() {
			super (PortOutEligibilityInfo.class, com.telus.eas.portability.info.PortOutEligibilityInfo.class);
		}
		
		protected static synchronized SubscriberPortOutEligibilityInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new SubscriberPortOutEligibilityInfoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected com.telus.eas.portability.info.PortOutEligibilityInfo performDomainMapping(PortOutEligibilityInfo source, com.telus.eas.portability.info.PortOutEligibilityInfo target) {
			target.setEligible(source.isEligibilityInd());			 
			target.setTransferBlocking(source.isTransferBlockingInd());			
			return super.performDomainMapping(source, target);
		}

		@Override
		protected PortOutEligibilityInfo performSchemaMapping(com.telus.eas.portability.info.PortOutEligibilityInfo source, PortOutEligibilityInfo target) {
			target.setEligibilityInd(source.isEligible()); 
			target.setTransferBlockingInd(source.isTransferBlocking());
			return super.performSchemaMapping(source, target);
		}

		
	}
}
