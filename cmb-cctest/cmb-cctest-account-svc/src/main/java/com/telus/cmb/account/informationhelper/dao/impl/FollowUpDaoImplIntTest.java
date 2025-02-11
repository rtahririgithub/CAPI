package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.FollowUpDao;
import com.telus.eas.account.info.FollowUpStatisticsInfo;
import com.telus.eas.account.info.FollowUpTextInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.utility.info.FollowUpCriteriaInfo;

public class FollowUpDaoImplIntTest extends BaseInformationHelperIntTest{

	@Autowired
	FollowUpDao dao;
	
	@Test
	public void testretrieveFollowUpAdditionalText(){
		Collection<FollowUpTextInfo> followUpTextInfo=dao.retrieveFollowUpAdditionalText(20007401, 11545271);
		assertEquals(5, followUpTextInfo.size());
		for (FollowUpTextInfo fti : followUpTextInfo) {
			assertEquals("12114",fti.getOperatorId());
			break;
		}	
	}
	
	@Test
	public void testretrieveFollowUpHistory(){
		Collection<FollowUpInfo> followUpInfo=dao.retrieveFollowUpHistory(22405200);
		assertEquals(5, followUpInfo.size());
		for (FollowUpInfo fi : followUpInfo) {
			assertEquals("901781  ",fi.getAssignedToWorkPositionId());
			assertEquals(10000283, fi.getBanId());
			assertEquals("ADJT", fi.getFollowUpType());
			break;
		}	
		
	}
	
	@Test
	public void testretrieveFollowUpInfoByBanFollowUpID(){
		FollowUpInfo followInfo=dao.retrieveFollowUpInfoByBanFollowUpID(20007401, 11545271);
		assertEquals("7807183956", followInfo.getSubscriberId());
	}
	
	@Test
	public void testretrieveFollowUpsByCriteria(){
		
		FollowUpCriteriaInfo fci=new FollowUpCriteriaInfo();
		fci.setWorkPositionId("RS_C0020");
		Collection<FollowUpInfo> followUpInfo=dao.retrieveFollowUps(fci);
		assertEquals(31, followUpInfo.size());
		for (FollowUpInfo fi : followUpInfo) {
			assertEquals("12857",fi.getOpenedBy());
			break;
		}	
	}
	
	@Test
	public void testretrieveFollowUps(){
		Collection<FollowUpInfo> followUpInfo=dao.retrieveFollowUps(70103567, 75);
		assertEquals(75, followUpInfo.size());
		for (FollowUpInfo fi : followUpInfo) {
			assertEquals("RS_C0020",fi.getAssignedToWorkPositionId());
			assertEquals(22405927,fi.getFollowUpId());
			break;
		}	
		
		Collection<FollowUpInfo> followUpInfo1=dao.retrieveFollowUps(70103567, -1);
		assertEquals(87, followUpInfo1.size());
		for (FollowUpInfo fi : followUpInfo1) {
			assertEquals("RS_C0020",fi.getAssignedToWorkPositionId());
			assertEquals(22405605,fi.getFollowUpId());
			break;
		}	
	}
	
	@Test
	public void testretrieveFollowUpStatistics(){
		FollowUpStatisticsInfo followUpStats=dao.retrieveFollowUpStatistics(20579111);
		assertTrue(followUpStats.hasOpenFollowUps());
		assertFalse(followUpStats.hasDueFollowUps());
	}
	
	@Test
	public void testretrieveLastFollowUpIDByBanFollowUpType(){
		int followUpId=dao.retrieveLastFollowUpIDByBanFollowUpType(10000283, "ADJT");
		assertEquals(22405202,followUpId);
	}
}
