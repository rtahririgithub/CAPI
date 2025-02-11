/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.account.svc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;
import com.telus.eas.framework.info.MemoInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@Test
@ContextConfiguration(locations = "classpath:application-context-test.xml")
//@ActiveProfiles("standalone")
//@ActiveProfiles({"remote", "prod"})
//@ActiveProfiles({ "remote", "local" })
@ActiveProfiles({ "remote", "dv103" })
public class AccountLifecycleManagerTest extends AbstractTestNGSpringContextTests {

	static {
		System.setProperty("weblogic.Name", "standalone");
	}

	@Autowired
	private AccountLifecycleManager manager;

	@Test
	public void openSession() throws Exception {
		String sessionId = manager.openSession("18654", "apollo", "SMARTDESKTOP");
		System.out.println(sessionId);
	}

	@Test
	public void getUserRole() throws Exception {
		String sessionId = manager.openSession("75454", "telus008", "SMARTDESKTOP");
		System.out.println(sessionId);
		String role = manager.getUserProfileID(sessionId);
		System.out.println(role);
	}

	@Test
	public void createMemo() throws Exception {

		String sessionId = manager.openSession("18654", "apollo", "SMARTDESKTOP");

		MemoInfo memo = new MemoInfo();

		memo.setBanId(18078654);
		memo.setDate(new Date());
		memo.setMemoType("FYI");
		memo.setProductType("C");
		memo.setText("CMB memo test");
		memo.setModifyDate(new Date());
		memo.setOperatorId(18654);
		memo.setMemoId(111);

		manager.createMemo(memo, sessionId);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void applyChargeWithTax() throws Exception {
		String sessionId = manager.openSession("18654", "apollo", "SMARTDESKTOP");

		List<ChargeAdjustmentInfo> chargeAdjInfoList = new ArrayList<ChargeAdjustmentInfo>();
		ChargeAdjustmentInfo chargeAdjInfo = new ChargeAdjustmentInfo();
		chargeAdjInfo.setProductType("C");
		chargeAdjInfo.setChargeAmount(5000);
		chargeAdjInfo.setChargeCode("RDCTAX");
		chargeAdjInfo.setAdjustmentAmount(5000);
		chargeAdjInfo.setAdjustmentReasonCode("TAXACR");
		chargeAdjInfo.setBan(22751764);
		chargeAdjInfo.setSubscriberId("5872282456");
		chargeAdjInfo.setChargeEffectiveDate(Calendar.getInstance().getTime());
		chargeAdjInfo.setChargeMemoText("WCTEST");
		chargeAdjInfoList.add(chargeAdjInfo);

		String taxationProvinceCode = "ON";
		boolean waiveTaxInd = false;

		List<ChargeAdjustmentInfo> result = manager.applyChargesAndAdjustmentsToAccountForSubscriberWithTax(chargeAdjInfoList, taxationProvinceCode, waiveTaxInd, sessionId);

		for (ChargeAdjustmentInfo charge : result) {
			System.out.println(charge.getErrorMessage());
			System.out.println(String.format("%.0f", charge.getChargeSequenceNumber()));
			System.out.println(String.format("%.0f", charge.getAdjustmentId()));
		}
	}

	@Test
	public void updateEmailAddress() throws Exception {
		System.out.println("start updateEmailAddress ");
		int ban = 26083990;
		String email = "Annie.Tarquini@telus.com";
		String sessionId = manager.openSession("18654", "apollo", "SMARTDESKTOP");
		manager.updateEmailAddress(ban, email, sessionId);
		System.out.println("end updateEmailAddress ");
	}

	@Test
	public void retrieveAmdocsCreditCheckResultByBan() throws Exception {

		System.out.println("Start retrieveAmdocsCreditCheckResultByBan...");

		String sessionId = manager.openSession("18654", "apollo", "SMARTDESKTOP");
		CreditCheckResultInfo info = manager.retrieveAmdocsCreditCheckResultByBan(16689227, sessionId); // 70788418 70780702 70779652
		System.out.println(info.toString());

		System.out.println("End retrieveAmdocsCreditCheckResultByBan.");
	}
	
	@Test
	public void retrieveKBCreditCheckResultByBan() throws Exception {

		System.out.println("Start retrieveKBCreditCheckResultByBan...");

		String sessionId = manager.openSession("18654", "apollo", "SMARTDESKTOP");
		CreditCheckResultInfo info = manager.retrieveKBCreditCheckResultByBan(16689227, "C", sessionId); // 70788418 70780702 70779652 70788464
		System.out.println(info.toString());

		System.out.println("End retrieveKBCreditCheckResultByBan.");
	}

}