package com.telus.cmb.account.informationhelper.dao.impl;
/*
 * Created by Inbaselvan Gandhi for WL10 Upgrade
 */
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.MemoDao;
import com.telus.eas.account.info.MemoCriteriaInfo;
import com.telus.eas.framework.info.MemoInfo;

public class MemoDaoImplIntTest extends BaseInformationHelperIntTest {

	@Autowired
	MemoDao dao;
	
	static {
		System.setProperty("getMemosByCriteria.method.rollback", "false");
	}

	@Test
	public void testRetrieveMemos() {
		Collection<MemoInfo> memo=dao.retrieveMemos(20007004, 500);
		assertEquals(306, memo.size());
		for (MemoInfo mi : memo) {
			assertEquals("0001", mi.getMemoType());
			//System.out.println(mi.getMemoType()); 
			break;
		}		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveMemosByCriteria(){

		MemoCriteriaInfo mcinfo=new MemoCriteriaInfo();
		mcinfo.setBanId(81);
//		mcinfo.setSubscriberId("9057160015");
		mcinfo.setType("0002");
		mcinfo.setManualText(null);
		mcinfo.setSystemText(null);
		//Year and Month adjustments as per Java util Date
		mcinfo.setDateFrom(new Date((2002-1900),(2-1),13));
		mcinfo.setDateTo(new Date((2005-1900),(3-1),31));
		mcinfo.setSearchLimit(10);
		Collection<MemoInfo> memoCriteria=dao.retrieveMemos(mcinfo);
//		assertEquals(1, memoCriteria.size());
		//System.out.println("-----+");
		for (MemoInfo mi : memoCriteria) {
			System.out.println(mi);
//			assertEquals("0002", mi.getMemoType());
			//System.out.println("-----"+mi.getMemoType()); 
			break;
		}
	}

	@Test
	public void testRetrieveLastMemo(){
		MemoInfo memoinfo=dao.retrieveLastMemo(20007181, "1040");
		//System.out.println(memoinfo.getBan()+"--"+memoinfo.getBanId()+"--"+memoinfo.getSubscriberId());
		assertEquals("9057160067", memoinfo.getSubscriberId());
	}
	
}
