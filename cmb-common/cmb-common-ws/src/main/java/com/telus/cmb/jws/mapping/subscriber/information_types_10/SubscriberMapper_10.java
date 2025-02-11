package com.telus.cmb.jws.mapping.subscriber.information_types_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.ConsumerNameMapper;
import com.telus.cmb.jws.mapping.subscriber.management.common_types_10.CommitmentMapper;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v1.Subscriber;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v1.SubscriberStatus;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Language;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode;

public class SubscriberMapper_10 extends AbstractSchemaMapper<Subscriber, SubscriberInfo> {
	private static SubscriberMapper_10 INSTANCE = null;

	private SubscriberMapper_10() {
		super (Subscriber.class, SubscriberInfo.class);
	}
	
	public synchronized static SubscriberMapper_10 getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SubscriberMapper_10();
		}
		
		return INSTANCE;
	}

	@Override
	protected SubscriberInfo performDomainMapping(Subscriber source, SubscriberInfo target) {
		
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
		target.setUserValueRating(source.getUserValueRating());
		
		if (source.isIDEN()) {
			IDENSubscriberInfo idenInfo = (IDENSubscriberInfo) source;
			target.setImsi(idenInfo.getIMSI());
			target.setIpAddress(idenInfo.getIPAddress());
			target.setMemberIdentity(MemberIdentityMapper.getInstance().mapToSchema(idenInfo.getMemberIdentity0()));
		}
		
		return super.performSchemaMapping(source, target);
	}
	
	
}
