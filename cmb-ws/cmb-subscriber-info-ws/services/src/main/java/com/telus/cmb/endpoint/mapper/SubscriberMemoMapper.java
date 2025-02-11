package com.telus.cmb.endpoint.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberMemo;

public class SubscriberMemoMapper extends AbstractSchemaMapper<SubscriberMemo, MemoInfo> {

	public SubscriberMemoMapper() {
		super(SubscriberMemo.class, MemoInfo.class);
	}

	@Override
	protected MemoInfo performDomainMapping(SubscriberMemo source, MemoInfo target) {
		if (source.getMemoId() != null) {
			target.setMemoId(source.getMemoId().doubleValue());
		}
		if (source.getModifyDate() != null) {
			target.setModifyDate(source.getModifyDate());
		}
		if (source.getOperatorId() != null) {
			target.setOperatorId(Integer.parseInt(source.getOperatorId()));
		}
		target.setText(source.getMemoText());
		target.setDate(source.getCreationDate());
		target.setMemoType(source.getMemoType());
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
