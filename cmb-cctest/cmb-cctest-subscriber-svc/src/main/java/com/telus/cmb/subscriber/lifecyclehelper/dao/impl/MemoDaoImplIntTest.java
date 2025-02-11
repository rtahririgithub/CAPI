package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.framework.info.MemoInfo;

public class MemoDaoImplIntTest extends BaseLifecycleHelperIntTest{
	@Autowired
	MemoDaoImpl dao;

	@Test
	public void testRetrieveLastMemo(){
		int ban=99999999;
		String subscriber=null;
		String memoType="CreditCheck";
		MemoInfo memoinfo = null;
		memoinfo = dao.retrieveLastMemo(ban, subscriber, memoType);
		assertEquals(null,memoinfo);
		memoType="AUTH";
		memoinfo = dao.retrieveLastMemo(ban, subscriber, memoType);
		assertEquals("ROLE MANAGEMENT TOOL ASSIGNED - [9057160531: AA]",memoinfo.getText());
		subscriber="9057160531";
		memoType="AOFR";
		memoinfo = dao.retrieveLastMemo(ban, subscriber, memoType);
		assertEquals("Subscriber activated with dealer code/salesrep=1100030729/25KP, handset model type=null and price plan=PXTAL30",memoinfo.getText());
	}

}
