package com.telus.cmb.endpoint.mapping;

import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getAccountMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getApplicationMessageMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getServiceAgreementMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getServiceWarningMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getSubscriberMapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.MigrateResponseDataType;
import com.telus.eas.subscriber.info.MigrationChangeInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;

public class MigrateResponseDataTypeMapper extends AbstractSchemaMapper<MigrateResponseDataType, MigrationChangeInfo> {

	private static MigrateResponseDataTypeMapper INSTANCE;

	private MigrateResponseDataTypeMapper() {
		super(MigrateResponseDataType.class, MigrationChangeInfo.class);
	}

	public static synchronized MigrateResponseDataTypeMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MigrateResponseDataTypeMapper();
		}
		return INSTANCE;
	}

	@Override
	protected MigrateResponseDataType performSchemaMapping(MigrationChangeInfo source, MigrateResponseDataType target) {
		
		target.setAccount(getAccountMapper().mapToSchema(source.getNewAccountInfo()));
		target.setNewSubscriber(getSubscriberMapper().mapToSchema(source.getNewSubscriberInfo()));
		target.setServiceAgreement(getServiceAgreementMapper().mapToSchema(source.getNewSubscriberContractInfo()));
		target.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(source.getApplicationMessageList()));
		target.getWarningList().addAll(getServiceWarningMapper().mapToSchema(source.getSystemWarningList()));
		
		return target;
	}
		
}