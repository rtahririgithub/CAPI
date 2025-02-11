package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.FleetDao;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TalkGroupInfo;

public class FleetDaoImplIntTest extends BaseInformationHelperIntTest{
	
	@Autowired
	FleetDao dao;

	@Test
	public void testRetrieveFleetsByBan() {
		Collection<FleetInfo> fleets = dao.retrieveFleetsByBan(134);
		
		assertEquals(1, fleets.size());
		
		for (FleetInfo fleet : fleets) {
			assertEquals(0, fleet.getAssociatedAccountsCount());
		}
	}

	@Test
	public void testRetrieveAssociatedAccountsCount() {
		assertEquals(1, dao.retrieveAssociatedAccountsCount(905, 131073));
		assertEquals(0, dao.retrieveAssociatedAccountsCount(0, 0));
	}
	@Test
	public void testRetrieveAssociatedTalkGroupsCount() {
		FleetIdentityInfo flInf= new FleetIdentityInfo();
		flInf.setUrbanId(905);
		flInf.setFleetId(16389);
		assertEquals(3, dao.retrieveAssociatedTalkGroupsCount(flInf,20004220));
		flInf.setUrbanId(0);
		flInf.setFleetId(0);
		assertEquals(0, dao.retrieveAssociatedTalkGroupsCount(flInf, 20004220));
	}
	@Test
	public void tesretrieveAttachedSubscribersCountForTalkGroup(){
		assertEquals(2,dao.retrieveAttachedSubscribersCountForTalkGroup(905, 16389, 2, 20004220));
		assertEquals(1,dao.retrieveAttachedSubscribersCountForTalkGroup(905, 16389, 1, 20004220));
		assertEquals(0,dao.retrieveAttachedSubscribersCountForTalkGroup(905, 16389, 3, 20004220));
	}
	@Test
	public void testRetretrieveTalkGroupsByBan(){
		Collection<TalkGroupInfo> talkGroup = new ArrayList<TalkGroupInfo>();
		talkGroup = dao.retrieveTalkGroupsByBan(84);
		assertEquals(3,talkGroup.size());
		for(TalkGroupInfo talkInfo :talkGroup){
		assertEquals("ru",talkInfo.getName());
		assertEquals(905,talkInfo.getFleetIdentity().getUrbanId());
		assertEquals(131075,talkInfo.getFleetIdentity().getFleetId());
		break;
		}
	}


}
