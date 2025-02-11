package com.telus.eas.account.info;

import com.telus.api.account.Memo;
import com.telus.eas.framework.info.MemoInfo;

import junit.framework.TestCase;

public class SubscriberEligibilitySupportingInfoTest  extends TestCase{
	SubscriberEligibilitySupportingInfo sesi;
	Memo[] memoList;
	double totalPaymentAmount;
	double totalDepositHeld;
	
	public void setUp() throws Exception{
		super.setUp();
		sesi=new SubscriberEligibilitySupportingInfo();
		memoList = new Memo[1];
		memoList[0] = new MemoInfo();
		memoList[0].setMemoType("x");
		totalPaymentAmount = 303.03;
		totalDepositHeld =	123.45;
	}
	
	public void testInfoObject(){
		sesi.setMemoList(memoList);
		sesi.setTotalDepositHeld(totalDepositHeld);
		sesi.setTotalPaymentAmount(totalPaymentAmount);
		
		assertEquals(memoList,sesi.getMemoList());
		assertEquals(totalPaymentAmount,sesi.getTotalPaymentAmount(),0);
		assertEquals(totalDepositHeld,sesi.getTotalDepositHeld(),0);
	}

}
