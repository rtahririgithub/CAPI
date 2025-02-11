package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.account.info.TalkGroupInfo;

public class FleetDaoImplIntTest extends BaseLifecycleHelperIntTest{

	@Autowired
	FleetDaoImpl dao;
	
	@Test
	public void testRetrieveTalkGroupsBySubscriber(){
		String subscriber="M000000203";
		List<TalkGroupInfo> list = new ArrayList<TalkGroupInfo>();
		list=dao.retrieveTalkGroupsBySubscriber(subscriber);
		assertEquals(1,list.size());
		for(TalkGroupInfo talkGrInf:list){
			assertEquals(84,talkGrInf.getOwnerBanId());
			assertEquals(905,talkGrInf.getFleetIdentity().getUrbanId());
		}
		list=dao.retrieveTalkGroupsBySubscriber("");
		assertEquals(0,list.size());
		list=dao.retrieveTalkGroupsBySubscriber(null);
		assertEquals(0,list.size());
	}
}
