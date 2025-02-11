package com.telus.cmb.test.account.dao;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.api.account.Account;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.VoiceUsageService;
import com.telus.api.account.VoiceUsageServiceDirection;
import com.telus.api.account.VoiceUsageServicePeriod;
import com.telus.cmb.account.informationhelper.dao.AdjustmentDao.CreditInfoHolder;
import com.telus.cmb.account.informationhelper.dao.impl.AccountDaoImpl;
import com.telus.cmb.account.informationhelper.dao.impl.AdjustmentDaoImpl;
import com.telus.cmb.account.informationhelper.dao.impl.CollectionDaoImpl;
import com.telus.cmb.account.informationhelper.dao.impl.CreditCheckDaoImpl;
import com.telus.cmb.account.informationhelper.dao.impl.DepositDaoImpl;
import com.telus.cmb.account.informationhelper.dao.impl.FleetDaoImpl;
import com.telus.cmb.account.informationhelper.dao.impl.InvoiceDaoImpl;
//import com.telus.cmb.account.informationhelper.dao.impl.LetterDaoImpl;
import com.telus.cmb.account.informationhelper.dao.impl.MemoDaoImpl;
import com.telus.cmb.account.informationhelper.dao.impl.PaymentDaoImpl;
import com.telus.cmb.account.informationhelper.dao.impl.SubscriberDaoImpl;
import com.telus.cmb.account.informationhelper.dao.impl.UsageDaoImpl;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CollectionHistoryInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.DepositAssessedHistoryInfo;
import com.telus.eas.account.info.DepositHistoryInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
//import com.telus.eas.account.info.LMSLetterRequestInfo;
import com.telus.eas.account.info.MemoCriteriaInfo;
import com.telus.eas.account.info.PaymentActivityInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PaymentMethodChangeHistoryInfo;
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.RefundHistoryInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.ServiceSubscriberCountInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo.DataSharingResultInfo;
import com.telus.eas.account.info.SubscriberInvoiceDetailInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.MemoInfo;

/**
 * @author Emerson Cho
 *
 */
@Test
@ContextConfiguration(locations={"classpath:application-context-side-by-side-test.xml"})
@ActiveProfiles("standalone")
public class AccountDaoParallelTest extends AbstractTestNGSpringContextTests {

	@Autowired
	ApplicationContext context;
	private static final String KNOWBILITY_JDBC_TEMPLATE_O9 = "knowbilityJdbcTemplateO9";
	private static final String KNOWBILITY_JDBC_TEMPLATE_O12 = "knowbilityJdbcTemplateO12";	
	
	static {
		System.setProperty("weblogic.Name", "standalone");
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
	}

	@Test
	public void run_O9_tests() throws Exception {
		Writer writerO9 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("acccount-test-results-O9.txt"), "utf-8"));
		Writer writerO9_perf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("perf-account-test-results-O9.txt"), "utf-8"));			
		run_all_tests(KNOWBILITY_JDBC_TEMPLATE_O9, writerO9,writerO9_perf);
		writerO9.close();
		writerO9_perf.close();
	}
	
	@Test
	public void run_O12_tests() throws Exception {
		Writer writerO12 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("acccount-test-results-O12.txt"), "utf-8"));
		Writer writer012_perf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("perf-account-test-results-O12.txt"), "utf-8"));			
		run_all_tests(KNOWBILITY_JDBC_TEMPLATE_O12, writerO12,writer012_perf);		
		writerO12.close();
		writer012_perf.close();
	}	
	
	@Test
	public void run_all_tests(String template, Writer writer,Writer writer_perf) throws Exception {
		testRetrieveAccountByBan(template, writer,writer_perf);
		testRetrieveBilledCredits(template, writer,writer_perf);
		testRetrieveUnbilledCredits(template, writer,writer_perf);
		testRetrieveCreditByFollowUpId(template, writer,writer_perf);
		testRetrieveRelatedChargesForCredit(template, writer,writer_perf);
		testRetrieveRelatedCreditsForCharge(template, writer,writer_perf);
		testRetrievePendingChargeHistory(template, writer,writer_perf);		
		testRetrieveCollectionHistoryInfo(template, writer,writer_perf);	
		testRetrieveLastCreditCheckResultByBan(template, writer,writer_perf);
		testRetrieveDepositHistory(template, writer,writer_perf);
		testRetrieveDepositAssessedHistoryList(template, writer,writer_perf);	
		testRetrieveOriginalDepositAssessedHistoryList(template, writer,writer_perf);
		testRetrieveFleetsByBan(template, writer,writer_perf);
		testRetrieveSubscriberInvoiceDetails(template, writer,writer_perf);		
		testRetrieveBilledCharges(template, writer,writer_perf);
//		testRetrieveLetterRequests(template, writer,writer_perf);		
		testRetrieveMemos(template, writer,writer_perf);		
		testRetrieveMemosByCriteria(template, writer,writer_perf);	
		testRetrieveLastMemo(template, writer,writer_perf);		
		testRetrievePaymentHistory(template, writer,writer_perf);	
		testRetrievePaymentActivities(template, writer,writer_perf);
		testRetrievePaymentMethodChangeHistory(template, writer,writer_perf);	
		testRetrieveRefundHistory(template, writer,writer_perf);
		testRetrievePhoneNumbersForBAN(template, writer,writer_perf);	
		testRetrieveAttachedSubscribersCount(template, writer,writer_perf);	
		testRetrieveProductSubscriberLists(template, writer,writer_perf);	
		testRetrieveSubscriberIdsByStatus(template, writer,writer_perf);
		testRetrieveSubscriberPhoneNumbersByStatus(template, writer,writer_perf);	
		testIsFeatureCategoryExistOnSubscribers(template, writer,writer_perf);
		testRetrieveHotlinedSubscriberPhoneNumber(template, writer,writer_perf);
		testRetrievePCSNetworkCountByBan(template, writer,writer_perf);	
		testRetrievePoolingPricePlanSubscriberCounts(template, writer,writer_perf);		
		testRetrieveServiceSubscriberCounts(template, writer,writer_perf); //org.apache.commons.dbcp.PoolableConnection cannot be cast to oracle.jdbc.OracleConnection
		testRetrieveMinutePoolingEnabledPricePlanSubscriberCounts(template, writer,writer_perf); //java.sql.SQLException: Missing IN or OUT parameter at index:: 5
		testRetrieveMinutePoolingEnabledPricePlanSubscriberCountsCoverage(template, writer,writer_perf); //org.apache.commons.dbcp.PoolableConnection cannot be cast to oracle.jdbc.OracleConnection	
		testRetrievePricePlanSubscriberCountInfo(template, writer,writer_perf);		
		testRetrieveShareablePricePlanSubscriberCount(template, writer,writer_perf);
		testRetrieveSubscriberIdsByServiceFamily(template, writer,writer_perf);		
		testRetrieveSubscribersByDataSharingGroupCodes(template, writer,writer_perf); //org.apache.commons.dbcp.PoolableConnection cannot be cast to oracle.jdbc.OracleConnection
		testRetrieveSubscriberDataSharingInfoList(template, writer,writer_perf); //can't use hints with new envs	
		testRetrieveVoiceUsageSummary(template, writer,writer_perf);
	}		

	@Test
	public void testRetrieveAccountByBan(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveAccountByBan\n");		
		long startTime  = System.currentTimeMillis();
		AccountDaoImpl accountDao = new AccountDaoImpl();
		accountDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		AccountInfo ban = accountDao.retrieveAccountByBan(194587);
		writer.append(ban.toString());
		printExecutionTime(startTime, "RetrieveAccountByBan", writer_perf);
		writer.append("\nExit testRetrieveAccountByBan\n");			
	}	
	
	@Test
	public void testRetrieveBilledCredits(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveBilledCredits\n");
		long startTime  = System.currentTimeMillis();
		Date fromDate = new Date(0);
		Date toDate = new Date();
		AdjustmentDaoImpl adjustmentDao = new AdjustmentDaoImpl();
		adjustmentDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		CreditInfoHolder holder = adjustmentDao.retrieveBilledCredits(70426502, fromDate, toDate, Account.BILL_STATE_BILLED, "", "", '0', "", 10);
		ArrayList<CreditInfo> creditInfoList = (ArrayList<CreditInfo>) holder.getCreditInfo();
		for (CreditInfo creditInfo : creditInfoList) {
			writer.append(creditInfo.toString());
		}
		printExecutionTime(startTime, "RetrieveBilledCredits", writer_perf);
		writer.append("\nExit testRetrieveBilledCredits\n");			
	}
	
	@Test
	public void testRetrieveUnbilledCredits(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveUnbilledCredits\n");
		long startTime  = System.currentTimeMillis();
		Date fromDate = new Date(0);
		Date toDate = new Date();
		AdjustmentDaoImpl adjustmentDao = new AdjustmentDaoImpl();
		adjustmentDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		CreditInfoHolder holder = adjustmentDao.retrieveUnbilledCredits(70654987, fromDate, toDate, Account.BILL_STATE_UNBILLED, "", "", '0', "", 10);
		ArrayList<CreditInfo> creditInfoList = (ArrayList<CreditInfo>) holder.getCreditInfo();
		for (CreditInfo creditInfo : creditInfoList) {
			writer.append(creditInfo.toString());
		}
		printExecutionTime(startTime, "retrieveUnbilledCredits", writer_perf);
		writer.append("\nExit testRetrieveUnbilledCredits\n");		
	}
	
	@Test
	public void testRetrieveCreditByFollowUpId(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveCreditByFollowUpId\n");
		long startTime  = System.currentTimeMillis();
		AdjustmentDaoImpl adjustmentDao = new AdjustmentDaoImpl();
		adjustmentDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		ArrayList<CreditInfo> creditInfoList = (ArrayList<CreditInfo>) adjustmentDao.retrieveCreditByFollowUpId(7024999);
		for (CreditInfo creditInfo : creditInfoList) {
			writer.append(creditInfo.toString());
		}		
		printExecutionTime(startTime, "retrieveCreditByFollowUpId", writer_perf);
		writer.append("\nExit testRetrieveCreditByFollowUpId\n");		
	}

	@Test
	public void testRetrieveRelatedChargesForCredit(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveRelatedChargesForCredit\n");	
		long startTime  = System.currentTimeMillis();
		AdjustmentDaoImpl adjustmentDao = new AdjustmentDaoImpl();
		adjustmentDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		ArrayList<ChargeInfo> chargeInfoList = (ArrayList<ChargeInfo>) adjustmentDao.retrieveRelatedChargesForCredit(70782967, 2096650845);
		for (ChargeInfo chargeInfo : chargeInfoList) {
			writer.append(chargeInfo.toString());
		}	
		printExecutionTime(startTime, "retrieveRelatedChargesForCredit", writer_perf);
		writer.append("\nExit testRetrieveRelatedChargesForCredit\n");			
	}

	@Test
	public void testRetrieveRelatedCreditsForCharge(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveRelatedCreditsForCharge\n");		
		long startTime  = System.currentTimeMillis();
		AdjustmentDaoImpl adjustmentDao = new AdjustmentDaoImpl();
		adjustmentDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		ArrayList<CreditInfo> creditInfoList = (ArrayList<CreditInfo>) adjustmentDao.retrieveRelatedCreditsForCharge(70783450, 2096650817);
		for (CreditInfo creditInfo : creditInfoList) {
			writer.append(creditInfo.toString());
		}
		printExecutionTime(startTime, "retrieveRelatedCreditsForCharge", writer_perf);
		writer.append("\nExit testRetrieveRelatedCreditsForCharge\n");		
	}

	@Test
	public void testRetrievePendingChargeHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrievePendingChargeHistory\n");	
		long startTime  = System.currentTimeMillis();
		Date fromDate = new Date(0);
		Date toDate = new Date();
		char level = '*'; //CHARGE_LEVEL_ALL='*'; CHARGE_LEVEL_ACCOUNT = 'B'; CHARGE_LEVEL_SUBSCRIBER = 'C'; 
		
		AdjustmentDaoImpl adjustmentDao = new AdjustmentDaoImpl();
		adjustmentDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		SearchResultsInfo chargeInfoList = (SearchResultsInfo) adjustmentDao.retrievePendingChargeHistory(70800425, fromDate, toDate, level, null, 10);
		for (ChargeInfo chargeInfo : (ChargeInfo[]) chargeInfoList.getItems()) {
			writer.append(chargeInfo.toString());
		}
		printExecutionTime(startTime, "retrievePendingChargeHistory", writer_perf);
		writer.append("\nExit testRetrievePendingChargeHistory\n");			
	}

	@Test
	public void testRetrieveCollectionHistoryInfo(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveCollectionHistoryInfo\n");
		long startTime  = System.currentTimeMillis();
		Date fromDate = new Date(0);
		Date toDate = new Date();
		
		CollectionDaoImpl collectionDao = new CollectionDaoImpl();
		collectionDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		CollectionHistoryInfo[] collectionHistoryList = collectionDao.retrieveCollectionHistoryInfo(70629483, fromDate, toDate);
		for (CollectionHistoryInfo collectionHistory : collectionHistoryList) {
			writer.append(collectionHistory.toString());
		}
		printExecutionTime(startTime, "retrieveCollectionHistoryInfo", writer_perf);
		writer.append("\nExit testRetrieveCollectionHistoryInfo\n");			
	}

	@Test
	public void testRetrieveLastCreditCheckResultByBan(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveLastCreditCheckResultByBan\n");	
		long startTime  = System.currentTimeMillis();
		CreditCheckDaoImpl creditCheckDao = new CreditCheckDaoImpl();
		creditCheckDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		CreditCheckResultInfo creditCheckInfo = creditCheckDao.retrieveLastCreditCheckResultByBan(70800488, "C");
		writer.append(creditCheckInfo.toString());
		printExecutionTime(startTime, "retrieveLastCreditCheckResultByBan", writer_perf);
		writer.append("\nExit testRetrieveLastCreditCheckResultByBan\n");		
	}

	@Test
	public void testRetrieveDepositHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveDepositHistory\n");	
		long startTime  = System.currentTimeMillis();
		Date fromDate = new Date(0);
		Date toDate = new Date();		
		DepositDaoImpl depositDao = new DepositDaoImpl();
		depositDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<DepositHistoryInfo> depositList = depositDao.retrieveDepositHistory(44128455, fromDate, toDate);
		for (DepositHistoryInfo deposit : depositList) {
			writer.append(deposit.toString());
		}
		printExecutionTime(startTime, "retrieveDepositHistory", writer_perf);
		writer.append("\nExit testRetrieveDepositHistory\n");			
	}

	@Test
	public void testRetrieveDepositAssessedHistoryList(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveDepositAssessedHistoryList\n");		
		long startTime  = System.currentTimeMillis();
		DepositDaoImpl depositDao = new DepositDaoImpl();
		depositDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<DepositAssessedHistoryInfo> depositList = depositDao.retrieveDepositAssessedHistoryList(70778530);
		for (DepositAssessedHistoryInfo deposit : depositList) {
			writer.append(deposit.toString());
		}	
		printExecutionTime(startTime, "retrieveDepositAssessedHistoryList", writer_perf);
		writer.append("\nExit testRetrieveDepositAssessedHistoryList\n");		
	}

	@Test
	public void testRetrieveOriginalDepositAssessedHistoryList(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveOriginalDepositAssessedHistoryList\n");	
		long startTime  = System.currentTimeMillis();
		DepositDaoImpl depositDao = new DepositDaoImpl();
		depositDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<DepositAssessedHistoryInfo> depositList = depositDao.retrieveOriginalDepositAssessedHistoryList(70800490);
		for (DepositAssessedHistoryInfo deposit : depositList) {
			writer.append(deposit.toString());
		}	
		printExecutionTime(startTime, "retrieveOriginalDepositAssessedHistoryList", writer_perf);
		writer.append("\nExit testRetrieveOriginalDepositAssessedHistoryList\n");			
	}

	@Test
	public void testRetrieveFleetsByBan(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveFleetsByBan\n");		
		long startTime  = System.currentTimeMillis();
		FleetDaoImpl fleetDao = new FleetDaoImpl();
		fleetDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		List<FleetInfo> fleets = (List<FleetInfo>) fleetDao.retrieveFleetsByBan(70031952);
		for (FleetInfo fleet : fleets) {
			writer.append(fleet.toString());
		}	
		printExecutionTime(startTime, "retrieveFleetsByBan", writer_perf);
		writer.append("\nExit testRetrieveFleetsByBan\n");		
	}

	@Test
	public void testRetrieveSubscriberInvoiceDetails(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberInvoiceDetails\n");			
		long startTime  = System.currentTimeMillis();
		InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
		invoiceDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		List<SubscriberInvoiceDetailInfo> invoiceDetailList = invoiceDao.retrieveSubscriberInvoiceDetails(194587, 5);
		for (SubscriberInvoiceDetailInfo invoiceDetail : invoiceDetailList) {
			writer.append(invoiceDetail.toString());
		}
		printExecutionTime(startTime, "retrieveSubscriberInvoiceDetails", writer_perf);
		writer.append("\nExit testRetrieveSubscriberInvoiceDetails\n");			
	}

	@Test
	public void testRetrieveBilledCharges(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveBilledCharges\n");		
		long startTime  = System.currentTimeMillis();
		Date fromDate = new Date(0);
		Date toDate = new Date();
		InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
		invoiceDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		List<ChargeInfo> billedChargeList = invoiceDao.retrieveBilledCharges(70800890, 1, "4031652377", fromDate, toDate);	
		for (ChargeInfo chargeInfo : billedChargeList) {
			writer.append(chargeInfo.toString());
		}	
		printExecutionTime(startTime, "retrieveBilledCharges", writer_perf);
		writer.append("\nExit testRetrieveBilledCharges\n");				
	}

//	@Test
//	public void testRetrieveLetterRequests(String template, Writer writer,Writer writer_perf) throws Exception {
//		writer.append("\nEnter testRetrieveLetterRequests\n");		
//		long startTime  = System.currentTimeMillis();
//		Date fromDate = new Date(0);
//		Date toDate = new Date();
//		char level = '*'; //CHARGE_LEVEL_ALL='*'; CHARGE_LEVEL_ACCOUNT = 'B'; CHARGE_LEVEL_SUBSCRIBER = 'C'; 		
//		LetterDaoImpl letterDaoImpl = new LetterDaoImpl();
//		letterDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
//		SearchResultsInfo results = (SearchResultsInfo) letterDaoImpl.retrieveLetterRequests(70778530, fromDate, toDate, level, null, 10);
//		for (LMSLetterRequestInfo LMSLetter : (LMSLetterRequestInfo[])results.getItems()) {
//			writer.append(LMSLetter.toString());
//		}	
//		printExecutionTime(startTime, "retrieveLetterRequests", writer_perf);
//		writer.append("\nExit testRetrieveLetterRequests\n");				
//	}
	
	@Test
	public void testRetrieveMemos(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveMemos\n");		
		long startTime  = System.currentTimeMillis();
		MemoDaoImpl memoDao = new MemoDaoImpl();
		memoDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		List<MemoInfo> results = memoDao.retrieveMemos(70704156, 10);
		for (MemoInfo memoInfo : results) {
			writer.append(memoInfo.toString());
		}
		printExecutionTime(startTime, "retrieveMemos", writer_perf);
		writer.append("\nExit testRetrieveMemos\n");		
	}

	@Test
	public void testRetrieveMemosByCriteria(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveMemosByCriteria\n");			
		long startTime  = System.currentTimeMillis();
		MemoDaoImpl memoDao = new MemoDaoImpl();
		memoDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));	
		MemoCriteriaInfo criteria = new MemoCriteriaInfo();
		criteria.setBanId(70704156);		
		List<MemoInfo> results = memoDao.retrieveMemos(criteria);
		for (MemoInfo memoInfo : results) {
			writer.append(memoInfo.toString());
		}	
		printExecutionTime(startTime, "retrieveMemos", writer_perf);
		writer.append("\nExit testRetrieveMemosByCriteria\n");		
	}

	@Test
	public void testRetrieveLastMemo(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveLastMemo\n");	
		long startTime  = System.currentTimeMillis();
		MemoDaoImpl memoDao = new MemoDaoImpl();
		memoDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		MemoInfo result = memoDao.retrieveLastMemo(70704156, "HCDI");
		writer.append(result.toString());
		printExecutionTime(startTime, "retrieveLastMemo", writer_perf);
		writer.append("\nExit testRetrieveLastMemo\n");			
	}

	@Test
	public void testRetrievePaymentHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrievePaymentHistory\n");		
		long startTime  = System.currentTimeMillis();
		Date fromDate = new Date(0);
		Date toDate = new Date();
		PaymentDaoImpl paymentDao = new PaymentDaoImpl();
		paymentDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		List<PaymentHistoryInfo> results = paymentDao.retrievePaymentHistory(70800891, fromDate, toDate);
		for (PaymentHistoryInfo paymentHistoryInfo : results) {
			writer.append(paymentHistoryInfo.toString());
		}
		printExecutionTime(startTime, "retrievePaymentHistory", writer_perf);
		writer.append("\nExit testRetrievePaymentHistory\n");				
	}

	@Test
	public void testRetrievePaymentActivities(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrievePaymentActivities\n");			
		long startTime  = System.currentTimeMillis();
		PaymentDaoImpl paymentDao = new PaymentDaoImpl();		
		paymentDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		List<PaymentActivityInfo> results = paymentDao.retrievePaymentActivities(70800891, 89905203);
		for (PaymentActivityInfo paymentActivityInfo : results) {
			writer.append(paymentActivityInfo.toString());
		}		
		printExecutionTime(startTime, "retrievePaymentActivities", writer_perf);
		writer.append("\nExit testRetrievePaymentActivities\n");		
	}

	@Test
	public void testRetrievePaymentMethodChangeHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrievePaymentMethodChangeHistory\n");
		long startTime  = System.currentTimeMillis();
		Date fromDate = new Date(0);
		Date toDate = new Date();
		PaymentDaoImpl paymentDao = new PaymentDaoImpl();		
		paymentDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		List<PaymentMethodChangeHistoryInfo> results = paymentDao.retrievePaymentMethodChangeHistory(70799774, fromDate, toDate);
		for (PaymentMethodChangeHistoryInfo paymentMethodChangeHistoryInfo : results) {
			writer.append(paymentMethodChangeHistoryInfo.toString());
		}	
		printExecutionTime(startTime, "retrievePaymentMethodChangeHistory", writer_perf);
		writer.append("\nExit testRetrievePaymentMethodChangeHistory\n");		
	}

	@Test
	public void testRetrieveRefundHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveRefundHistory\n");			
		long startTime  = System.currentTimeMillis();
		Date fromDate = new Date(0);
		Date toDate = new Date();
		PaymentDaoImpl paymentDao = new PaymentDaoImpl();		
		paymentDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));	
		List<RefundHistoryInfo> results = paymentDao.retrieveRefundHistory(70783844, fromDate, toDate);	
		for (RefundHistoryInfo refundHistoryInfo : results) {
			writer.append(refundHistoryInfo.toString());
		}	
		printExecutionTime(startTime, "retrieveRefundHistory", writer_perf);
		writer.append("\nExit testRetrieveRefundHistory\n");			
	}

	@Test
	public void testRetrievePhoneNumbersForBAN(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrievePhoneNumbersForBAN\n");	
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		Map<String, String> results = subscriberDao.retrievePhoneNumbersForBAN(70779333);
		for (String key : results.keySet()) {
			writer.append(results.get(key).toString());
		}	
		printExecutionTime(startTime, "retrievePhoneNumbersForBAN", writer_perf);
		writer.append("\nExit testRetrievePhoneNumbersForBAN\n");			
	}

	@Test
	public void testRetrieveAttachedSubscribersCount(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveAttachedSubscribersCount\n");			
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		FleetIdentityInfo fleetIdentity = new FleetIdentityInfo(403, 131334);		
		int result = subscriberDao.retrieveAttachedSubscribersCount(403, fleetIdentity);		
		writer.append(String.valueOf(result));
		printExecutionTime(startTime, "retrieveAttachedSubscribersCount", writer_perf);
		writer.append("\nExit testRetrieveAttachedSubscribersCount\n");		
	}

	@Test
	public void testRetrieveProductSubscriberLists(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveProductSubscriberLists\n");		
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		List<ProductSubscriberListInfo> results = (List<ProductSubscriberListInfo>) subscriberDao.retrieveProductSubscriberLists(70800987);
		for (ProductSubscriberListInfo subscriberListInfo : results) {
			writer.append(subscriberListInfo.toString());
		}	
		printExecutionTime(startTime, "retrieveProductSubscriberLists", writer_perf);
		writer.append("\nExit testRetrieveProductSubscriberLists\n");			
	}

	@Test
	public void testRetrieveSubscriberIdsByStatus(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberIdsByStatus\n");	
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		List<String> results = subscriberDao.retrieveSubscriberIdsByStatus(70800987, 'A', 10);
		for (String subscriberId : results) {
			writer.append(subscriberId.toString());
		}	
		printExecutionTime(startTime, "retrieveSubscriberIdsByStatus", writer_perf);
		writer.append("\nExit testRetrieveSubscriberIdsByStatus\n");			
	}

	@Test
	public void testRetrieveSubscriberPhoneNumbersByStatus(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberPhoneNumbersByStatus\n");			
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		List<String> results = subscriberDao.retrieveSubscriberPhoneNumbersByStatus(70800987, 'A', 10);
		for (String subscriberId : results) {
			writer.append(subscriberId.toString());
		}	
		printExecutionTime(startTime, "retrieveSubscriberPhoneNumbersByStatus", writer_perf);
		writer.append("\nExit testRetrieveSubscriberPhoneNumbersByStatus\n");			
	}

	@Test
	public void testIsFeatureCategoryExistOnSubscribers(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testIsFeatureCategoryExistOnSubscribers\n");			
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		boolean result = subscriberDao.isFeatureCategoryExistOnSubscribers(70801031, "TM");
		writer.append(String.valueOf(result));
		printExecutionTime(startTime, "isFeatureCategoryExistOnSubscribers", writer_perf);
		writer.append("\nExit testIsFeatureCategoryExistOnSubscribers\n");		
	}

	@Test
	public void testRetrieveHotlinedSubscriberPhoneNumber(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveHotlinedSubscriberPhoneNumber\n");				
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		String result = subscriberDao.retrieveHotlinedSubscriberPhoneNumber(70798647);
		writer.append(result);
		printExecutionTime(startTime, "retrieveHotlinedSubscriberPhoneNumber", writer_perf);
		writer.append("\nExit testRetrieveHotlinedSubscriberPhoneNumber\n");			
	}

	@Test
	public void testRetrievePCSNetworkCountByBan(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrievePCSNetworkCountByBan\n");	
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		HashMap<String, Integer> results = subscriberDao.retrievePCSNetworkCountByBan(70801031);
		for (String key : results.keySet()) {
			writer.append("key: " + key);
			writer.append(", value: " + results.get(key).toString() + "\n");
		}
		printExecutionTime(startTime, "retrievePCSNetworkCountByBan", writer_perf);
		writer.append("\nExit testRetrievePCSNetworkCountByBan\n");		
	}

	@Test
	public void testRetrievePoolingPricePlanSubscriberCounts(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrievePoolingPricePlanSubscriberCounts\n");		
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		List<PoolingPricePlanSubscriberCountInfo> results = subscriberDao.retrievePoolingPricePlanSubscriberCounts(70570807, PoolingPricePlanSubscriberCountInfo.POOLING_GROUP_ALL, false);
		for (PoolingPricePlanSubscriberCountInfo poolingPricePlanSubscriberCountInfo : results) {
			for (PricePlanSubscriberCountInfo pricePlanSubscriberCount: (PricePlanSubscriberCountInfo[]) poolingPricePlanSubscriberCountInfo.getPricePlanSubscriberCount()) {
				writer.append(pricePlanSubscriberCount.toString());
			}
		}
		printExecutionTime(startTime, "retrievePoolingPricePlanSubscriberCounts", writer_perf);
		writer.append("\nExit testRetrievePoolingPricePlanSubscriberCounts\n");			
	}

	@Test
	public void testRetrieveServiceSubscriberCounts(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveServiceSubscriberCounts\n");		
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		String[] serviceCodes = { "S9110    " };
		List<ServiceSubscriberCount> results = subscriberDao.retrieveServiceSubscriberCounts(70800760, serviceCodes, true);
		for (ServiceSubscriberCount serviceSubscriberCount : results) {
			ServiceSubscriberCountInfo serviceSubscriberCountInfo = (ServiceSubscriberCountInfo) serviceSubscriberCount;
			writer.append(serviceSubscriberCountInfo.toString());
		}
		printExecutionTime(startTime, "retrieveServiceSubscriberCounts", writer_perf);
		writer.append("\nExit testRetrieveServiceSubscriberCounts\n");			
	}

	@Test
	public void testRetrieveMinutePoolingEnabledPricePlanSubscriberCounts(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveMinutePoolingEnabledPricePlanSubscriberCounts\n");			
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		List<PricePlanSubscriberCount> results = subscriberDao.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70698899, new String[]{"H","O"});
		for (PricePlanSubscriberCount pricePlanSubscriberCount : results) {
			PricePlanSubscriberCountInfo pricePlanSubscriberCountInfo = (PricePlanSubscriberCountInfo) pricePlanSubscriberCount;
			writer.append(pricePlanSubscriberCountInfo.toString());
		}
		printExecutionTime(startTime, "retrieveMinutePoolingEnabledPricePlanSubscriberCounts", writer_perf);
		writer.append("\nExit testRetrieveMinutePoolingEnabledPricePlanSubscriberCounts\n");			
	}

	@Test
	public void testRetrieveMinutePoolingEnabledPricePlanSubscriberCountsCoverage(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveMinutePoolingEnabledPricePlanSubscriberCountsCoverage\n");		
		long startTime  = System.currentTimeMillis();
		String[] coverageTypes = { "O", "H" };
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		List<PricePlanSubscriberCount> results = subscriberDao.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70779350, coverageTypes);
		for (PricePlanSubscriberCount pricePlanSubscriberCount : results) {
			PricePlanSubscriberCountInfo pricePlanSubscriberCountInfo = (PricePlanSubscriberCountInfo) pricePlanSubscriberCount;
			writer.append(pricePlanSubscriberCountInfo.toString());
		}
		printExecutionTime(startTime, "retrieveMinutePoolingEnabledPricePlanSubscriberCounts", writer_perf);
		writer.append("\nExit testRetrieveMinutePoolingEnabledPricePlanSubscriberCountsCoverage\n");			
	}

	@Test
	public void testRetrievePricePlanSubscriberCountInfo(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrievePricePlanSubscriberCountInfo\n");			
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		List<PricePlanSubscriberCountInfo> results = subscriberDao.retrievePricePlanSubscriberCountInfo(70579688, "C");
		for (PricePlanSubscriberCountInfo pricePlanSubscriberCount : results) {
			writer.append(pricePlanSubscriberCount.toString());
		}
		printExecutionTime(startTime, "retrievePricePlanSubscriberCountInfo", writer_perf);
		writer.append("\nExit testRetrievePricePlanSubscriberCountInfo\n");			
	}

	@Test
	public void testRetrieveShareablePricePlanSubscriberCount(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveShareablePricePlanSubscriberCount\n");	
		long startTime  = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		List<PricePlanSubscriberCountInfo> results = subscriberDao.retrieveShareablePricePlanSubscriberCount(70576908);
		for (PricePlanSubscriberCountInfo pricePlanSubscriberCount : results) {
			writer.append(pricePlanSubscriberCount.toString());
		}
		printExecutionTime(startTime, "retrieveShareablePricePlanSubscriberCount", writer_perf);
		writer.append("\nExit testRetrieveShareablePricePlanSubscriberCount\n");			
	}

	@Test
	public void testRetrieveSubscriberIdsByServiceFamily(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberIdsByServiceFamily\n");	
		long startTime  = System.currentTimeMillis();
		Date today = new Date();
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		String[] results = subscriberDao.retrieveSubscriberIdsByServiceFamily(70801031, "W", today);
		for (String result : results) {
			writer.append(result);
		}
		printExecutionTime(startTime, "retrieveSubscriberIdsByServiceFamily", writer_perf);
		writer.append("\nExit testRetrieveSubscriberIdsByServiceFamily\n");			
	}

	@Test
	public void testRetrieveSubscribersByDataSharingGroupCodes(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscribersByDataSharingGroupCodes\n");	
		long startTime  = System.currentTimeMillis();
		Date today = new Date();
		String[] dataSharingGroupCodes = { "CAD_DATA_2013" };
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));		
		SubscribersByDataSharingGroupResultInfo[] results = subscriberDao.retrieveSubscribersByDataSharingGroupCodes(70739201, dataSharingGroupCodes, today);
		for (SubscribersByDataSharingGroupResultInfo subscribersByDataSharingGroupResultInfo : results) {
			writer.append(subscribersByDataSharingGroupResultInfo.toString());
		}
		printExecutionTime(startTime, "retrieveSubscribersByDataSharingGroupCodes", writer_perf);
		writer.append("\nExit testRetrieveSubscribersByDataSharingGroupCodes\n");			
	}

	@Test
	public void testRetrieveSubscriberDataSharingInfoList(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberDataSharingInfoList\n");			
		long startTime  = System.currentTimeMillis();
		String[] dataSharingGroupCodes = { "CAD_DATA_2013" };
		SubscriberDaoImpl subscriberDao = new SubscriberDaoImpl();		
		subscriberDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));			
		List<DataSharingResultInfo> results = (List<DataSharingResultInfo>) subscriberDao.retrieveSubscriberDataSharingInfoList(70739201, dataSharingGroupCodes);
		for (DataSharingResultInfo dataSharingResultInfo : results) {
			writer.append(dataSharingResultInfo.toString());
		}
		printExecutionTime(startTime, "retrieveSubscriberDataSharingInfoList", writer_perf);
		writer.append("\nExit testRetrieveSubscriberDataSharingInfoList\n");			
	}
	
	private void printExecutionTime(long startTime,String methodName , Writer writer_perf) throws IOException{
		long totalExecutiontime = System.currentTimeMillis() - startTime;
		writer_perf.append(methodName +" total executionTime is , "+totalExecutiontime +"ms \n");
	}
	
	@Test
	public void testRetrieveVoiceUsageSummary(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveVoiceUsageSummary\n");				
		long startTime  = System.currentTimeMillis();
		UsageDaoImpl usageDaoImpl = new UsageDaoImpl();		
		usageDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<VoiceUsageSummaryInfo> results = (List<VoiceUsageSummaryInfo>) usageDaoImpl.retrieveVoiceUsageSummary(1885362, "STD");
		for (VoiceUsageSummaryInfo voiceUsageSummaryInfo : results) {
			for (VoiceUsageService voiceUsageService : voiceUsageSummaryInfo.getVoiceUsageServices()) {	
				writer.append(voiceUsageService.getUsageRecordTypeCode() + "\n");
				for (VoiceUsageServiceDirection voiceUsageServiceDirection : voiceUsageService.getVoiceUsageServiceDirections()) {	
					writer.append(voiceUsageServiceDirection.getDirectionCode());
					writer.append(String.valueOf(voiceUsageServiceDirection.getLastCallDate()));
					writer.append(String.valueOf(voiceUsageServiceDirection.getOddFreeAirCalls()));	
					writer.append(String.valueOf(voiceUsageServiceDirection.getOddSpecialCalls()));						
					for (VoiceUsageServicePeriod voiceUsageServicePeriod : voiceUsageServiceDirection.getVoiceUsageServicePeriods()) {	
						writer.append(voiceUsageServicePeriod.getPeriodCode());
						writer.append(String.valueOf(voiceUsageServicePeriod.getCalls()));
						writer.append(String.valueOf(voiceUsageServicePeriod.getChargeable()));	
						writer.append(String.valueOf(voiceUsageServicePeriod.getChargeAmount()));		
						writer.append(String.valueOf(voiceUsageServicePeriod.getFree()));
						writer.append(String.valueOf(voiceUsageServicePeriod.getIncluded()));	
						writer.append(String.valueOf(voiceUsageServicePeriod.getIncludedUsed()));	
						writer.append(String.valueOf(voiceUsageServicePeriod.getRemaining()));
						writer.append(String.valueOf(voiceUsageServicePeriod.getTotalUsed()));	
					}
				}
			}
		}
		printExecutionTime(startTime, "getVoiceUsageServicePeriods", writer_perf);
		writer.append("\nExit testRetrieveVoiceUsageSummary\n");			
	}	
}