package com.telus.cmb.jws.mapping.subscriber.information_types_20;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.ConsumerNameMapper;
import com.telus.cmb.jws.mapping.subscriber.management.common_types_10.CommitmentMapper;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.MemberIdentityInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.Subscriber;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v2.SubscriberStatus;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.Language;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ProvinceCode;

public class SubscriberMapper_20 extends AbstractSchemaMapper<Subscriber, SubscriberInfo> {
	private static SubscriberMapper_20 INSTANCE = null;

	private SubscriberMapper_20() {
		super (Subscriber.class, SubscriberInfo.class);
	}
	
	public synchronized static SubscriberMapper_20 getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SubscriberMapper_20();
		}
		
		return INSTANCE;
	}

	@Override
	protected SubscriberInfo performDomainMapping(Subscriber source, SubscriberInfo target) {
		
		if (source.getImsi()!=null) {
			IDENSubscriberInfo idenSub = new IDENSubscriberInfo();
			idenSub.setIMSI(source.getImsi());
			idenSub.setIPAddress(source.getIpAddress());
			if(source.getMemberIdentity()!=null){
				MemberIdentityInfo identity = MemberIdentityMapper.getInstance().mapToDomain(source.getMemberIdentity());
				idenSub.getMemberIdentity0().copyFrom(identity);
				idenSub.getFleetIdentity().copyFrom(identity.getFleetIdentity0());
			}
			target = idenSub;
		}
		
		target.setBanId(Integer.parseInt( source.getBillingAccountNumber()));
		target.setBrandId(source.getBrandId());
		target.setCommitment(CommitmentMapper.getInstance().mapToDomain( source.getCommitment()) );
		if(source.getConsumerName()!=null){
			ConsumerNameInfo nameInfo = ConsumerNameMapper.getInstance().mapToDomain(source.getConsumerName());	
			if (nameInfo!=null) ((ConsumerNameInfo) target.getConsumerName()).copyFrom(nameInfo );
		}
		target.setCreateDate(source.getCreateDate());
		target.setDealerCode(source.getDealerCode());
		target.setHasDummyESN(source.isDummyESNInd());
		target.setEmailAddress(source.getEmailAddress());
		target.setSerialNumber(source.getEquipmentSerialNumber());
		target.setHotlined( Boolean.TRUE.equals(source.isHotlinedInd())? "Y" : "N");	
		if(source.getLanguage()!=null)	target.setLanguage(source.getLanguage().value());
		if(source.getMarketProvince()!=null)	target.setMarketProvince(source.getMarketProvince().value());
		target.setMigrationDate(source.getMigrationDate());
		target.setMigrationTypeCode(source.getMigrationTypeCode());
		target.setPhoneNumber(source.getPhoneNumber());
		target.setPortDate(source.getPortDate());
		target.setPortType(source.getPortType());
		target.setPricePlan(source.getPricePlanCode());
		target.setProductType(source.getProductType());
		target.setSalesRepId(source.getSalesRepCode());
		target.setSecurityDeposit(source.getSecurityDeposit());
		target.setStartServiceDate(source.getStartServiceDate());
		
		if(source.getStatus()!=null){
			String subStatus = source.getStatus().value();
			if (subStatus!=null && subStatus.length()>0)target.setStatus( subStatus.charAt(0));
		}
		
		target.setStatusDate(source.getStatusDate());
		target.setSubscriberId(source.getSubscriberId());
		target.setSubscriptionId(source.getSubscriptionId());
		if ( source.getUserValueRating()!=null && source.getUserValueRating().length()>0 ) target.setUserValueRating(source.getUserValueRating());
		
		return super.performDomainMapping(source, target);
	}

	@Override
	protected Subscriber performSchemaMapping(SubscriberInfo source, Subscriber target) {
		target.setBillingAccountNumber(String.valueOf(source.getBanId()));
		target.setBrandId(source.getBrandId());
		target.setCommitment(CommitmentMapper.getInstance().mapToSchema((CommitmentInfo) source.getCommitment()) );
		target.setConsumerName(ConsumerNameMapper.getInstance().mapToSchema((ConsumerNameInfo) source.getConsumerName()));
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
