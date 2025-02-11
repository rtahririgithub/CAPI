package com.telus.cmb.jws.mapper;

import java.util.List;

import com.telus.api.account.SubscribersByDataSharingGroupResult.DataSharingSubscribers;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo.DataSharingSubscriberInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
//import com.telus.eas.portability.info.PortOutEligibilityInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.CallingCircleInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.CallingCirclePhoneNumberListInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.PhoneNumberList;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.PortOutEligibilityInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.SubscriberListByDataSharingGroup;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.SubscriberListByDataSharingGroup.DataSharingSubscriberList;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.SubscriberMemo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.TimePeriod;

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

	
	public static CallingCircleInformationMapper CallingCircleInformationMapper() {
		return CallingCircleInformationMapper.getInstance();
	}
	
	/**
	 * CallingCircleInformationMapper
	 * @author Brandon Wen
	 *
	 */
	public static class CallingCircleInformationMapper extends AbstractSchemaMapper<CallingCircleInfo, CallingCircleParametersInfo>  {
		private static CallingCircleInformationMapper INSTANCE;
		
		private CallingCircleInformationMapper() {
			super(CallingCircleInfo.class, CallingCircleParametersInfo.class);
		}
		
		protected static synchronized CallingCircleInformationMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CallingCircleInformationMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CallingCircleParametersInfo performDomainMapping(CallingCircleInfo source, CallingCircleParametersInfo target) {
			target.setCallingCircleCurrentPhoneNumberList(
				CallingCirclePhoneNumberListInfoMapper().mapToDomain(source.getCurrentPhoneNumberList()));
			
			target.setCallingCircleFuturePhoneNumberList(
				CallingCirclePhoneNumberListInfoMapper().mapToDomain(source.getFuturePhoneNumberList()));
			
			return super.performDomainMapping(source, target);
		}

		@Override
		protected CallingCircleInfo performSchemaMapping(CallingCircleParametersInfo source, CallingCircleInfo target) {
			target.setCurrentPhoneNumberList(
				CallingCirclePhoneNumberListInfoMapper().mapToSchema((CallingCirclePhoneListInfo)source.getCallingCircleCurrentPhoneNumberList()));
			
			target.setFuturePhoneNumberList(
				CallingCirclePhoneNumberListInfoMapper().mapToSchema((CallingCirclePhoneListInfo)source.getCallingCircleFuturePhoneNumberList()));

			return super.performSchemaMapping(source, target);
		}
	}
	

	public static CallingCirclePhoneNumberListInfoMapper CallingCirclePhoneNumberListInfoMapper() {
		return CallingCirclePhoneNumberListInfoMapper.getInstance();
	}
	
	/**
	 * CallingCircleInformationMapper
	 * @author Brandon Wen
	 *
	 */
	public static class CallingCirclePhoneNumberListInfoMapper extends AbstractSchemaMapper<CallingCirclePhoneNumberListInfo, CallingCirclePhoneListInfo>  {
		private static CallingCirclePhoneNumberListInfoMapper INSTANCE;
		
		private CallingCirclePhoneNumberListInfoMapper() {
			super(CallingCirclePhoneNumberListInfo.class, CallingCirclePhoneListInfo.class);
		}
		
		protected static synchronized CallingCirclePhoneNumberListInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CallingCirclePhoneNumberListInfoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CallingCirclePhoneListInfo performDomainMapping(CallingCirclePhoneNumberListInfo source, CallingCirclePhoneListInfo target) {
			target.setEffectiveDate(source.getTimePeriod().getEffectiveDate());
			target.setExpiryDate(source.getTimePeriod().getExpiryDate());
			
			List<String> phoneNumberList = source.getPhoneNumberList().getPhoneNumber();
			target.setPhoneNumberList((String[])phoneNumberList.toArray(new String[phoneNumberList.size()]));
			return super.performDomainMapping(source, target);
		}

		@Override
		protected CallingCirclePhoneNumberListInfo performSchemaMapping(CallingCirclePhoneListInfo source, CallingCirclePhoneNumberListInfo target) {
			TimePeriod timePeriod = new TimePeriod();
			timePeriod.setEffectiveDate(source.getEffectiveDate());
			timePeriod.setExpiryDate(source.getExpiryDate());

			PhoneNumberList phoneNumberList = new PhoneNumberList();
			for (String phoneNumber : source.getPhoneNumberList()) {
				phoneNumberList.getPhoneNumber().add(phoneNumber);
			}
			
			target.setTimePeriod(timePeriod);
			target.setPhoneNumberList(phoneNumberList);
			return super.performSchemaMapping(source, target);
		}
	}

	public static SubscriberListByDataSharingGroupMapper SubscriberListByDataSharingGroupMapper() {
		return SubscriberListByDataSharingGroupMapper.getInstance();
	}

	
	public static class SubscriberListByDataSharingGroupMapper extends AbstractSchemaMapper<SubscriberListByDataSharingGroup ,SubscribersByDataSharingGroupResultInfo>{
		private static SubscriberListByDataSharingGroupMapper INSTANCE;
		public SubscriberListByDataSharingGroupMapper(){
			super(SubscriberListByDataSharingGroup.class, SubscribersByDataSharingGroupResultInfo.class);
		}
		
		protected static synchronized SubscriberListByDataSharingGroupMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new SubscriberListByDataSharingGroupMapper();
			}
			return INSTANCE;
		}
		
		@Override
		protected SubscriberListByDataSharingGroup performSchemaMapping(SubscribersByDataSharingGroupResultInfo source , SubscriberListByDataSharingGroup target){
			target.setDataSharingGroupCode(source.getDataSharingGroupCode());
			target.getDataSharingSubscriberList().addAll(DataSharingSubscriberListMapper().mapToSchema( source.getDataSharingSubscribers()));
			return target;
		}
		
	}
	
	public static DataSharingSubscriberListMapper DataSharingSubscriberListMapper() {
		return DataSharingSubscriberListMapper.getInstance();
	}


	public static class DataSharingSubscriberListMapper extends AbstractSchemaMapper<DataSharingSubscriberList ,DataSharingSubscribers>{
		private static DataSharingSubscriberListMapper INSTANCE;
		public DataSharingSubscriberListMapper(){
			super(DataSharingSubscriberList.class, DataSharingSubscribers.class);
		}
		
		protected static synchronized DataSharingSubscriberListMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new DataSharingSubscriberListMapper();
			}
			return INSTANCE;
		}
		
		@Override
		protected DataSharingSubscriberList performSchemaMapping(DataSharingSubscribers source , DataSharingSubscriberList target){
			target.setContributingInd(source.isContributing());
			target.setSubscriberId(source.getSubscriberId());
			return target;
		}
		
	}

	
}
