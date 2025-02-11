package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v7.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v7.ResponseMessage;



public class ResponseErrorInfoMapper extends AbstractSchemaMapper<ResponseMessage, ServiceAirTimeAllocationInfo> {
	
	public ResponseErrorInfoMapper() {
		super(ResponseMessage.class, ServiceAirTimeAllocationInfo.class);
	}
	
		@Override
	protected ResponseMessage performSchemaMapping( ServiceAirTimeAllocationInfo source, ResponseMessage target) {
			target.setDateTimeStamp(source.getTimeStamp());
			target.setErrorCode(source.getErrorCode());
			target.getMessageList().addAll(new MessageMapper().mapToSchema(new ServiceAirTimeAllocationInfo[]{source}));
			return super.performSchemaMapping(source, target);
	}
		
		
		public static class MessageMapper extends AbstractSchemaMapper<Message, ServiceAirTimeAllocationInfo> {
			
			public MessageMapper() {
				super (Message.class, ServiceAirTimeAllocationInfo.class);
			}
			
			@Override
			protected Message performSchemaMapping(ServiceAirTimeAllocationInfo source , Message target){
				target.setLocale(source.getLocale());
				target.setMessage(source.getErrorMessage());
				return target;
			}	
		}


}
