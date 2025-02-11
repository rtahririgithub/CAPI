package com.telus.cmb.reference.dao;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.utility.info.ActivityTypeInfo;
import com.telus.eas.utility.info.DiscountPlanInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;



/**
 * @author Emerson Cho
 *
 */
@Test
@ContextConfiguration(locations={"classpath:application-context-side-by-side-test.xml"})
@ActiveProfiles("standalone")
public class ReferenceDaoParallelTest extends AbstractTestNGSpringContextTests {

	@Autowired
	ApplicationContext context;
	private static final String KNOWBILITY_JDBC_TEMPLATE_O9 = "knowbilityJdbcTemplateO9";
	private static final String KNOWBILITY_JDBC_TEMPLATE_O12 = "knowbilityJdbcTemplateO12";	
	private static final String REF_JDBC_TEMPLATE_O9 = "refJdbcTemplateO9";
	private static final String REF_JDBC_TEMPLATE_O12 = "refJdbcTemplateO12";
	private static final String EXTO_JDBC_TEMPLATE_O9 = "extoJdbcTemplateO9";
	private static final String EXTO_JDBC_TEMPLATE_O12 = "extoJdbcTemplateO12";	
	
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
		Writer writerO9 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ref-test-results-O9.txt"), "utf-8"));		
		run_ref_tests(REF_JDBC_TEMPLATE_O9, writerO9);
		run_exto_tests(EXTO_JDBC_TEMPLATE_O9, writerO9);	
		run_kb_tests(KNOWBILITY_JDBC_TEMPLATE_O9, writerO9);			
		writerO9.close();
	}
	
	@Test
	public void run_O12_tests() throws Exception {
		Writer writerO12 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ref-test-results-O12.txt"), "utf-8"));		
		run_ref_tests(REF_JDBC_TEMPLATE_O12, writerO12);	
		run_exto_tests(EXTO_JDBC_TEMPLATE_O12, writerO12);
		run_kb_tests(KNOWBILITY_JDBC_TEMPLATE_O12, writerO12);		
		writerO12.close();
	}	

	@Test
	public void run_ref_tests(String template, Writer writer) throws Exception {
		testGetReferenceData(template, writer);
		testIsPortOutAllowed(template, writer);
		//testRetrieveDiscountPlans(template, writer); //org.apache.commons.dbcp.PoolableConnection cannot be cast to oracle.jdbc.OracleConnection
		testRetrieveIncludedPromotions(template, writer);
		//testRetrievePricePlan(template, writer); // ORA-06508: PL/SQL: could not find program unit being called
		//testRetrieveIncludedServices(template, writer); // ORA-06508: PL/SQL: could not find program unit being called
		testRetrieveOptionalServices(template, writer);
		testRetrievePricePlanList(template, writer);
	}		

	@Test
	public void run_exto_tests(String template, Writer writer) throws Exception {
		testRetrieveNpaNxxForMsisdnReservation(template, writer);
	}	
	
	@Test	
	public void run_kb_tests(String template, Writer writer) throws Exception {
		testRetrieveFleetByFleetIdentity(template, writer);
		testRetrieveAssociatedAccountsCount(template, writer);
		testRetrieveFleetsByFleetType(template, writer);
		testRetrieveTalkGroupsByFleetIdentity(template, writer);
		testRetrieveNpaNxxForMsisdnReservationKb(template, writer);
	}	
	
	@Test
	public void testGetReferenceData(String template, Writer writer) throws Exception {
		writer.append("\nEnter testGetReferenceData\n");			
		ReferenceDataRefDao refDao = new ReferenceDataRefDao();
		refDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<ActivityTypeInfo> activityTypeList = (List<ActivityTypeInfo>) refDao.retrieveActivityTypes();
		for(ActivityTypeInfo activityTypeInfo: activityTypeList) {
			writer.append(activityTypeInfo.toString());
		}
		writer.append("\nExit testGetReferenceData\n");			
	}	
	
	@Test
	public void testIsPortOutAllowed(String template, Writer writer) throws Exception {
		writer.append("\nEnter testIsPortOutAllowed\n");		
		ReferenceDataRefDao refDao = new ReferenceDataRefDao();
		refDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		boolean portOutIndicator = refDao.isPortOutAllowed("S", "SUS", "VAD");
		writer.append(String.valueOf(portOutIndicator));
		writer.append("\nExit testIsPortOutAllowed\n");			
	}
	
	@Test
	public void testRetrieveDiscountPlans(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrieveDiscountPlans\n");		
		ReferenceDataRefDao refDao = new ReferenceDataRefDao();
		refDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		DiscountPlanInfo[] discountPlanInfo = refDao.retrieveDiscountPlans(false, "M20FD", "ALL", new long[] {0}, true, 0);
		for(DiscountPlanInfo discountPlan: discountPlanInfo) {
			writer.append(discountPlan.toString());
		}
		writer.append("\nExit testRetrieveDiscountPlans\n");			
	}
	
	@Test
	public void testRetrieveNpaNxxForMsisdnReservation(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrieveNpaNxxForMsisdnReservation\n");		
		ReferenceDataRefDao refDao = new ReferenceDataRefDao();
		refDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		Collection<String> npaNxxList = refDao.retrieveNpaNxxForMsisdnReservation("2042010000", true);
		for(String npaNxx: npaNxxList) {
			writer.append(npaNxx);
		}
		npaNxxList = refDao.retrieveNpaNxxForMsisdnReservation("2042000000", false);
		for(String npaNxx: npaNxxList) {
			writer.append(npaNxx);
		}		
		writer.append("\nExit testRetrieveNpaNxxForMsisdnReservation\n");			
	}	
	
	@Test
	public void testRetrieveIncludedPromotions(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrieveIncludedPromotions\n");			
		ReferenceDataRefDao refDao = new ReferenceDataRefDao();
		refDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<ServiceInfo> serviceList = refDao.retrieveIncludedPromotions("SSK911", "9", "9", "ON", 12);
		for(ServiceInfo service: serviceList) {
			writer.append(service.toString());
		}	
		writer.append("\nExit testRetrieveIncludedPromotions\n");			
	}	
	
	@Test
	public void testRetrievePricePlan(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrievePricePlan\n");			
		ReferenceDataRefDao refDao = new ReferenceDataRefDao();
		refDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		PricePlanInfo pricePlan = refDao.retrievePricePlan("3P100M28");
		writer.append(pricePlan.toString());
		writer.append("\nExit testRetrievePricePlan\n");			
	}	
	
	@Test
	public void testRetrieveIncludedServices(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrieveIncludedServices\n");			
		ReferenceDataRefDao refDao = new ReferenceDataRefDao();
		refDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		ServiceInfo[] serviceList = refDao.retrieveIncludedServices("3P100M28");
		for(ServiceInfo service: serviceList) {
			writer.append(service.toString());
		}	
		writer.append("\nExit testRetrieveIncludedServices\n");		
	}	
	
	@Test
	public void testRetrieveOptionalServices(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrieveOptionalServices\n");		
		ReferenceDataRefDao refDao = new ReferenceDataRefDao();
		refDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		ServiceInfo[] serviceList = refDao.retrieveOptionalServices("SSK911", "9", "ON", 'I', 'R', "9");
		for(ServiceInfo service: serviceList) {
			writer.append(service.toString());
		}	
		writer.append("\nExit testRetrieveOptionalServices\n");			
	}	
	
	@Test
	public void testRetrievePricePlanList(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrievePricePlanList\n");		
		ReferenceDataRefDao refDao = new ReferenceDataRefDao();
		refDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<PricePlanInfo> pricePlanList = (List<PricePlanInfo>) refDao.retrievePricePlanList("C", "ON", 'I', "9", 1, true, false, "9");
			
		for(PricePlanInfo plan: pricePlanList) {
			writer.append(plan.toString());
		}	
		writer.append("\nExit testRetrievePricePlanList\n");			
	}	
	
	@Test
	public void testRetrieveFleetByFleetIdentity(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrieveFleetByFleetIdentity\n");		
		ReferenceDataKnowbilityDao kbDao = new ReferenceDataKnowbilityDao();
		FleetIdentityInfo fleetIdentity = new FleetIdentityInfo(403, 165);
		kbDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		FleetInfo fleetInfo = kbDao.retrieveFleetByFleetIdentity(fleetIdentity);
		writer.append(fleetInfo.toString());
		writer.append("\nExit testRetrieveFleetByFleetIdentity\n");			
	}	
	
	@Test
	public void testRetrieveAssociatedAccountsCount(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrieveAssociatedAccountsCount\n");		
		ReferenceDataKnowbilityDao kbDao = new ReferenceDataKnowbilityDao();
		kbDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		int accountCount = kbDao.retrieveAssociatedAccountsCount(403, 165);
		writer.append(String.valueOf(accountCount));
		writer.append("\nExit testRetrieveAssociatedAccountsCount\n");			
	}	
	
	@Test
	public void testRetrieveFleetsByFleetType(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrieveFleetsByFleetType\n");			
		ReferenceDataKnowbilityDao kbDao = new ReferenceDataKnowbilityDao();
		kbDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		Collection<FleetInfo> fleetList = kbDao.retrieveFleetsByFleetType('S');
		for(FleetInfo fleet: fleetList) {
			writer.append(fleet.toString());
		}	
		writer.append("\nExit testRetrieveFleetsByFleetType\n");			
	}	
	
	@Test
	public void testRetrieveTalkGroupsByFleetIdentity(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrieveTalkGroupsByFleetIdentity\n");			
		ReferenceDataKnowbilityDao kbDao = new ReferenceDataKnowbilityDao();
		FleetIdentityInfo fleetIdentity = new FleetIdentityInfo(403, 131101);
		kbDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		Collection<TalkGroupInfo> talkGroupList = kbDao.retrieveTalkGroupsByFleetIdentity(fleetIdentity);
		for(TalkGroupInfo talkGroupInfo: talkGroupList) {
			writer.append(talkGroupInfo.toString());
		}	
		writer.append("\nExit testRetrieveTalkGroupsByFleetIdentity\n");			
	}		
	
	@Test
	public void testRetrieveNpaNxxForMsisdnReservationKb(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrieveNpaNxxForMsisdnReservationKb\n");		
		ReferenceDataKnowbilityDao kbDao = new ReferenceDataKnowbilityDao();
		kbDao.setJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		Collection<String> npaNxxList = kbDao.retrieveNpaNxxForMsisdnReservation("2042010000", true);
		for(String npaNxx: npaNxxList) {
			writer.append(npaNxx);
		}
		npaNxxList = kbDao.retrieveNpaNxxForMsisdnReservation("2042000000", false);
		for(String npaNxx: npaNxxList) {
			writer.append(npaNxx);
		}	
		writer.append("\nExit testRetrieveNpaNxxForMsisdnReservationKb\n");		
	}		
}