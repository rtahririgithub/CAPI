package com.telus.cmb.jws.mapper;

import java.util.List;

import weblogic.wsee.util.StringUtil;


import com.telus.api.account.SubscribersByDataSharingGroupResult.DataSharingSubscribers;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.customer_management_common_50.CustomerManagementCommonMapper;
import com.telus.cmb.jws.mapping.subscriber.information_types_30.MemberIdentityMapper;
import com.telus.cmb.jws.mapping.subscriber.management.common_types_30.CommitmentMapper;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.ServiceIdentityInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.ResourceInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.cmb.jws.PhoneNumberSearchOption;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.CallingCircleInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.CallingCirclePhoneNumberListInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.PhoneNumberList;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.PortOutEligibilityInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SeatResource;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.ServiceIdentity;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberIdentifier;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberListByDataSharingGroup;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberStatus;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberListByDataSharingGroup.DataSharingSubscriberList;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberMemo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.NameFormat;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.Address;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ConsumerName;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.Language;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TimePeriod;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.Subscriber;

public class SubscriberInformationServiceMapper {

	public static SubscriberMemoMapper SubscriberMemoMapper() {
		return SubscriberMemoMapper.getInstance();
	}
	public static ServiceIdentityMapper ServiceIdentityMapper() {
		return ServiceIdentityMapper.getInstance();
	}
	public static SubscriberIdentifierMapper SubscriberIdentifierMapper() {
		return SubscriberIdentifierMapper.getInstance();
	}
	public static PhoneNumberSearchOptionMapper PhoneNumberSearchOptionMapper() {
		return PhoneNumberSearchOptionMapper.getInstance();
	}

	public static ConsumerNameMapper ConsumerNameMapper() {
		return ConsumerNameMapper.getInstance();
	}

	public static AddressMapper AddressMapper() {
		return AddressMapper.getInstance();
	}

	public static SubscriberMapper SubscriberMapper() {
		return SubscriberMapper.getInstance();
	}
	public static SeatResourceMapper SeatResourceMapper() {
		return SeatResourceMapper.getInstance();
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
	
	public static class ServiceIdentityMapper extends AbstractSchemaMapper<ServiceIdentity, ServiceIdentityInfo> {
		private static ServiceIdentityMapper INSTANCE ;

		public ServiceIdentityMapper() {
			super (ServiceIdentity.class, ServiceIdentityInfo.class);
		}
		
		protected static synchronized ServiceIdentityMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ServiceIdentityMapper();
			}
			
			return INSTANCE;
		}

		@Override
		protected ServiceIdentityInfo performDomainMapping(ServiceIdentity source,ServiceIdentityInfo target) {
			target.setServiceCode(source.getServiceCode());
			if(source.getEffectiveDate()!=null)
				target.setEffectiveDate(source.getEffectiveDate());
			return super.performDomainMapping(source, target);
		}
			
	}
	
	public static class SubscriberIdentifierMapper extends AbstractSchemaMapper<SubscriberIdentifier, SubscriberIdentifierInfo> {
		private static SubscriberIdentifierMapper INSTANCE ;

		public SubscriberIdentifierMapper() {
			super (SubscriberIdentifier.class, SubscriberIdentifierInfo.class);
		}
		
		protected static synchronized SubscriberIdentifierMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new SubscriberIdentifierMapper();
			}
			
			return INSTANCE;
		}

		@Override
		protected SubscriberIdentifierInfo performDomainMapping(SubscriberIdentifier source,SubscriberIdentifierInfo target) 
		{
			target.setSubscriberId( source.getSubscriberId());
			if (source.getSubscriptionId() != null) {
				target.setSubscriptionId(source.getSubscriptionId());
			}
			target.setBan( Integer.parseInt( source.getBillingAccountNumber()));
			target.setPhoneNumber( source.getPhoneNumber());
			target.setSeatGroup( source.getSeatGroupId());
			target.setSeatType( source.getSeatTypeCd());
			return super.performDomainMapping( source, target);
		}
		
		@Override
		protected SubscriberIdentifier performSchemaMapping( SubscriberIdentifierInfo source, SubscriberIdentifier target ) 
		{
			target.setSubscriberId( source.getSubscriberId());
			target.setSubscriptionId( source.getSubscriptionId()) ;
			target.setBillingAccountNumber(Integer.toString(source.getBan()));  
			target.setPhoneNumber( source.getPhoneNumber());
			target.setSeatGroupId( source.getSeatGroup());
			target.setSeatTypeCd( source.getSeatType()); 
			return super.performSchemaMapping( source, target );
		}
	}
				
	public static class PhoneNumberSearchOptionMapper extends
			AbstractSchemaMapper<PhoneNumberSearchOption, PhoneNumberSearchOptionInfo> {
		private static PhoneNumberSearchOptionMapper INSTANCE;

		public PhoneNumberSearchOptionMapper() {
			super(PhoneNumberSearchOption.class,PhoneNumberSearchOptionInfo.class);
		}

		protected static synchronized PhoneNumberSearchOptionMapper getInstance() {
			if (INSTANCE == null)
				INSTANCE = new PhoneNumberSearchOptionMapper();
			return INSTANCE;
		}

		protected PhoneNumberSearchOptionInfo performDomainMapping(PhoneNumberSearchOption source,PhoneNumberSearchOptionInfo target) {
			boolean searchWirelessInd = source.isSearchWirelessInd()!=null ? source.isSearchWirelessInd() : false;
			boolean searchVoipInd = source.isSearchVoipInd()!=null ? source.isSearchVoipInd() : false;
			boolean searchTollFreeInd = source.isSearchTollFreeInd()!=null ? source.isSearchTollFreeInd() : false;
			target.setSearchWirelessNumber(searchWirelessInd);
			target.setSearchVOIP(searchVoipInd);
			target.setSearchTollFree(searchTollFreeInd);
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class SeatResourceMapper extends AbstractSchemaMapper<SeatResource, ResourceInfo> {
		private static SeatResourceMapper INSTANCE;

		public SeatResourceMapper() {
			super(SeatResource.class, ResourceInfo.class);
		}

		public static synchronized SeatResourceMapper getInstance() {
			if (INSTANCE == null)
				INSTANCE = new SeatResourceMapper();

			return INSTANCE;
		}

		protected SeatResource performSchemaMapping(ResourceInfo source,SeatResource target) {
			target.setResourceNumber(source.getResourceNumber());
			target.setResourceTypeCd(source.getResourceType());
			target.setResourceStatus(source.getResourceStatus());

			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class ConsumerNameMapper extends AbstractSchemaMapper<ConsumerName,ConsumerNameInfo> {

		private static ConsumerNameMapper INSTANCE;

		public ConsumerNameMapper() {
			super(ConsumerName.class,ConsumerNameInfo.class);
		}

		protected static synchronized ConsumerNameMapper getInstance() {
			if (INSTANCE == null)
				INSTANCE = new ConsumerNameMapper();
			return INSTANCE;
		}

		@Override
		protected ConsumerNameInfo performDomainMapping(ConsumerName source,ConsumerNameInfo target) {

				target.setAdditionalLine(source.getAdditionalLine());
				target.setFirstName(source.getFirstName());
				target.setGeneration(source.getGeneration());
				target.setLastName(source.getLastName());
				target.setMiddleInitial(source.getMiddleInitial());
				if(source.getNameFormat() != null)
				target.setNameFormat(source.getNameFormat().value());				
				target.setTitle(source.getTitle());

			return super.performDomainMapping(source, target);

		}
	}

	public static class AddressMapper extends AbstractSchemaMapper<Address,AddressInfo> {

		private static AddressMapper INSTANCE;

		public AddressMapper() {
			super(Address.class, AddressInfo.class);
		}

		protected static synchronized AddressMapper getInstance() {
			if (INSTANCE == null)
				INSTANCE = new AddressMapper();
			return INSTANCE;
		}

		@Override
		protected AddressInfo performDomainMapping(Address source, AddressInfo target) {

				if (source.getAddressType() != null) {
					target.setAddressType(source.getAddressType().value());
				}

				target.setAttention(source.getAttention());
				target.setCity(source.getCity());
				target.setCountry(source.getCountry());			
				target.setPoBox(source.getPoBox());
				target.setPostalCode(source.getPostalCode());
				target.setPrimaryLine(source.getPrimaryLine());
				target.setSecondaryLine(source.getSecondaryLine());
				if (source.getProvince() != null) {
					target.setProvince(source.getProvince().value());
					target.setProvince0(source.getProvince().value());
				}
				target.setRuralCompartment(source.getRuralCompartment());
				target.setRuralDeliveryType(source.getRuralDeliveryType());
				target.setRuralGroup(source.getRuralGroup());
				target.setRuralLocation(source.getRuralLocation());
				target.setRuralNumber(source.getRuralNumber());
				target.setRuralQualifier(source.getRuralQualifier());
				target.setRuralSite(source.getRuralSite());
				target.setRuralType(source.getRuralType());			
				target.setStreetDirection(source.getStreetDirection());
				target.setStreetName(source.getStreetName());			
				target.setStreetType(source.getStreetType());
				target.setUnit(source.getUnit());
				target.setUnitType(source.getUnitType());
				target.setZipGeoCode(source.getZipGeoCode());

				if((!StringUtil.isEmpty(source.getCivicNo()) && !StringUtil.isEmpty(source.getStreetNumber()))||
						(!StringUtil.isEmpty(source.getCivicNo()) && StringUtil.isEmpty(source.getStreetNumber()))){
					target.setCivicNo(source.getCivicNo());
				} else if (StringUtil.isEmpty(source.getCivicNo()) && !StringUtil.isEmpty(source.getStreetNumber())){
					target.setCivicNo(source.getStreetNumber());
				} else {
					target.setCivicNo(null);
				}

				if((!StringUtil.isEmpty(source.getCivicNoSuffix()) && !StringUtil.isEmpty(source.getStreetNumberSuffix()))||
						(!StringUtil.isEmpty(source.getCivicNoSuffix()) && StringUtil.isEmpty(source.getStreetNumberSuffix()))){
					target.setCivicNoSuffix(source.getCivicNoSuffix());
				} else if (StringUtil.isEmpty(source.getCivicNoSuffix()) && !StringUtil.isEmpty(source.getStreetNumberSuffix())){
					target.setCivicNoSuffix(source.getStreetNumberSuffix());
				} else {
					target.setCivicNoSuffix(null);
				}

				target.setForeignState(source.getForeignState());

			
			return super.performDomainMapping(source, target);

		}

		protected Address performSchemaMapping(AddressInfo source,Address target) {

			return super.performSchemaMapping(source, target);
		}

	}

	public static class SubscriberMapper extends AbstractSchemaMapper<Subscriber,SubscriberInfo> {

		private static SubscriberMapper INSTANCE;

		public SubscriberMapper() {
			super(Subscriber.class,SubscriberInfo.class);
		}

		protected static synchronized SubscriberMapper getInstance() {
			if (INSTANCE == null)
				INSTANCE = new SubscriberMapper();
			return INSTANCE;
		}

		@Override
		protected Subscriber performSchemaMapping(SubscriberInfo source, Subscriber target) {
			target.setActivityCd(source.getActivityCode());
			target.setActivityReasonCd(source.getActivityReasonCode());
			target.setBillingAccountNumber(String.valueOf(source.getBanId()));
			target.setBrandId(source.getBrandId());
			target.setCommitment(CommitmentMapper.getInstance().mapToSchema((CommitmentInfo) source.getCommitment()) );
			target.setConsumerName(CustomerManagementCommonMapper.ConsumerNameMapper().mapToSchema((ConsumerNameInfo) source.getConsumerName()));
			target.setCreateDate(source.getCreateDate());
			target.setDealerCode(source.getDealerCode());
			target.setDummyESNInd(source.hasDummyESN());
			target.setEmailAddress(source.getEmailAddress());
			target.setEquipmentSerialNumber(source.getSerialNumber());
			target.setHotlinedInd(source.isHotlined());
			target.setLanguage(toEnum(source.getLanguage(), Language.class));
			target.setMarketProvince(toEnum(source.getMarketProvince(), ProvinceCode.class));
			target.setMigrationDate(source.getMigrationDate());
			target.setMigrationTypeCode(source.getMigrationTypeCode());
			target.setPhoneNumber(source.getPhoneNumber());
			target.setPortDate(source.getPortDate());
			target.setPortType(source.getPortType());
			target.setPricePlanCode(source.getPricePlan());
			target.setProductType(source.getProductType());
			target.setSalesRepCode(source.getSalesRepId());
			target.setSecurityDeposit(source.getRequestedSecurityDeposit());
			target.setStartServiceDate(source.getStartServiceDate());
			target.setStatus(toEnum(String.valueOf(source.getStatus()), SubscriberStatus.class));
			target.setStatusDate(source.getStatusDate());
			target.setSubscriberId(source.getSubscriberId());
			target.setSubscriptionId(source.getSubscriptionId());
			if ( source.getUserValueRating()!=null && source.getUserValueRating().length()>0 ) target.setUserValueRating(source.getUserValueRating());

			if (source.isIDEN()) {
				IDENSubscriberInfo idenInfo = (IDENSubscriberInfo) source;
				target.setImsi(idenInfo.getIMSI());
				target.setIpAddress(idenInfo.getIPAddress());
				target.setMemberIdentity(MemberIdentityMapper.getInstance().mapToSchema(idenInfo.getMemberIdentity0()));
			}

			return super.performSchemaMapping(source, target);

		}
	}

}
