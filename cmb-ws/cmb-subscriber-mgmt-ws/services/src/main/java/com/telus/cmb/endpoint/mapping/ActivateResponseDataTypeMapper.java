package com.telus.cmb.endpoint.mapping;

import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getApplicationMessageMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getServiceWarningMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getSubscriberMapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.ActivateResponseDataType;
import com.telus.eas.subscriber.info.ActivationChangeInfo;

public class ActivateResponseDataTypeMapper extends AbstractSchemaMapper<ActivateResponseDataType, ActivationChangeInfo> {

	private static ActivateResponseDataTypeMapper INSTANCE;

	private ActivateResponseDataTypeMapper() {
		super(ActivateResponseDataType.class, ActivationChangeInfo.class);
	}

	public static synchronized ActivateResponseDataTypeMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ActivateResponseDataTypeMapper();
		}
		return INSTANCE;
	}

	@Override
	protected ActivateResponseDataType performSchemaMapping(ActivationChangeInfo source, ActivateResponseDataType target) {
		
		target.setSubscriberInfo(getSubscriberMapper().mapToSchema(source.getCurrentSubscriberInfo()));
		target.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(source.getApplicationMessageList()));
		target.getWarningList().addAll(getServiceWarningMapper().mapToSchema(source.getSystemWarningList()));
		
		return target;
	}
		
}