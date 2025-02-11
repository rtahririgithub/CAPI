package com.telus.cmb.test.productequipment.dao;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.productequipment.helper.dao.impl.EquipmentHelperDaoImpl;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.ProfileInfo;


/**
 * @author Emerson Cho
 *
 */
@Test
@ContextConfiguration(locations={"classpath:application-context-side-by-side-test.xml"})
@ActiveProfiles("standalone")
public class EquipmentDaoParallelTest extends AbstractTestNGSpringContextTests {

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
		Writer writerO9 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("equip-test-results-O9.txt"), "utf-8"));
		run_all_tests(KNOWBILITY_JDBC_TEMPLATE_O9, writerO9);
		writerO9.close();
	}
	
	@Test
	public void run_O12_tests() throws Exception {
		Writer writerO12 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("equip-test-results-O12.txt"), "utf-8"));
		run_all_tests(KNOWBILITY_JDBC_TEMPLATE_O12, writerO12);		
		writerO12.close();
	}		
	
	@Test
	public void run_all_tests(String template, Writer writer) throws Exception {
		//testGetSubscriberByEquipment(template, writer); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement
		//testGetSubscriberByIMSI(template, writer); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement
		testRetrievePagerEquipmentInfo(template, writer);
	}		

	@Test
	public void testGetSubscriberByEquipment(String template, Writer writer) throws Exception {
		writer.append("\nEnter testGetSubscriberByEquipment\n");		
		EquipmentHelperDaoImpl equipDao = new EquipmentHelperDaoImpl();
		equipDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		EquipmentInfo equipmentInfo = new EquipmentInfo();
		equipmentInfo.setSerialNumber("11902007571"); //456565656565656565, 17609328717
		EquipmentInfo returnEquipmentInfo = equipDao.getSubscriberByEquipment(equipmentInfo);
		writer.append(returnEquipmentInfo.toString());
		writer.append("\nExit testGetSubscriberByEquipment\n");		
	}	
	
	@Test
	public void testGetSubscriberByIMSI(String template, Writer writer) throws Exception {
		writer.append("\nEnter testGetSubscriberByIMSI\n");		
		EquipmentHelperDaoImpl equipDao = new EquipmentHelperDaoImpl();
		equipDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		EquipmentInfo equipmentInfo = new EquipmentInfo();
		ProfileInfo profile = new ProfileInfo();
		profile.setLocalIMSI("214030000755255"); //302220999951970
		equipmentInfo.setProfile(profile);
		EquipmentInfo returnEquipmentInfo = equipDao.getSubscriberByIMSI(equipmentInfo);
		writer.append(returnEquipmentInfo.toString());
		writer.append("\nExit testGetSubscriberByIMSI\n");	
	}	
	
	@Test
	public void testRetrievePagerEquipmentInfo(String template, Writer writer) throws Exception {
		writer.append("\nEnter testRetrievePagerEquipmentInfo\n");		
		EquipmentHelperDaoImpl equipDao = new EquipmentHelperDaoImpl();
		equipDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		EquipmentInfo returnEquipmentInfo = equipDao.retrievePagerEquipmentInfo("000100002456070");
		writer.append(returnEquipmentInfo.toString());
		writer.append("\nExit testRetrievePagerEquipmentInfo\n");
	}		
}