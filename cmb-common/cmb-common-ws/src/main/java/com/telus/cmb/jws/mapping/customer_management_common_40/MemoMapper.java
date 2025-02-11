/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.customer_management_common_40;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.Memo;


public class MemoMapper extends AbstractSchemaMapper<Memo, MemoInfo> {

	private static MemoMapper INSTANCE;
	
	private MemoMapper() {
		super(Memo.class, MemoInfo.class);
	}
	
	public static synchronized MemoMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MemoMapper();
		}
		return INSTANCE;
	}

	@Override
	protected Memo performSchemaMapping(MemoInfo source, Memo target) {
		
		target.setCreationDate(source.getDate());
		target.setMemoId(Double.valueOf(source.getMemoId()));
		target.setMemoText(source.getText());
		target.setMemoType(source.getMemoType());
		target.setModifyDate(source.getModifyDate());
		target.setOperatorId(String.valueOf(source.getOperatorId()));
		target.setSystemText(source.getSystemText());
		
		return super.performSchemaMapping(source, target);
	}

}