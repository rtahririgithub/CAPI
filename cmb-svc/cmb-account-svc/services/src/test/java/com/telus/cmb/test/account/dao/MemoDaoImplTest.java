/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.test.account.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.account.informationhelper.dao.MemoDao;
import com.telus.eas.account.info.MemoCriteriaInfo;
import com.telus.eas.framework.info.MemoInfo;

@ContextConfiguration(locations="classpath:com/telus/cmb/test/account/dao/test-context-dao.xml")
public class MemoDaoImplTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");	
		
	}
	
	@Autowired
	private MemoDao dao;
	
	@Test
	public void retrieveMemos() throws Exception {
		//subscriberId = 70757229 , Ban = 70757229, Status =  A
		try {
			MemoCriteriaInfo memoCriteria = new MemoCriteriaInfo();
			memoCriteria.setBanId(70864244);
			//memoCriteria.setSubscriberIds(new String[]{ "4161495797","4161495829","4161495538"});
			//memoCriteria.setTypes(new String[]{"0002","1040","0003","HCDC","CPUI","2011"});
			//memoCriteria.setManualText("Bill Credit");
			memoCriteria.setManualText("Reserved");	
			List<MemoInfo> info = dao.retrieveMemos(memoCriteria);
			for (MemoInfo memoInfo : info) {
				System.out.println("ban = "+memoInfo.getBanId()+"\tmemo type = "+memoInfo.getMemoType()+"\tMemoId"+memoInfo.getMemoId()+"\tmemo date"+memoInfo.getDate()+"\tSystemText"+memoInfo.getSystemText());
				System.out.println("memo text = "+memoInfo.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void retrieveLastMemo() throws Exception {
		try {
			int ban = 70864244;
			String memoType = "0002";
			//String memoType = "CreditCheck";

			MemoInfo memoInfo = dao.retrieveLastMemo(ban, memoType);
			System.out.println("ban = "+memoInfo.getBanId()+"\tmemo type = "+memoInfo.getMemoType()+"\tMemoId"+memoInfo.getMemoId()+"\tmemo date"+memoInfo.getDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void retrieveMemosWithCount() throws Exception {
		try {
			int ban = 70864244;
			int count = 3;
			List<MemoInfo> info = dao.retrieveMemos(ban, count);
			for (MemoInfo memoInfo : info) {
				System.out.println("ban = "+memoInfo.getBanId()+"\tmemo type = "+memoInfo.getMemoType()+"\tMemoId"+memoInfo.getMemoId()+"\tmemo date"+memoInfo.getDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}