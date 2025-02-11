package com.telus.cmb.test.subscriber.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.api.portability.PortInEligibility;
import com.telus.cmb.subscriber.lifecyclefacade.dao.EligibilityCheckRequestDao;
import com.telus.eas.portability.info.PortInEligibilityInfo;

@ContextConfiguration(locations="classpath:com/telus/cmb/test/subscriber/dao/test-context-dao.xml")
public class EligibilityCheckRequestDaoTest extends AbstractTestNGSpringContextTests {
	
	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
	}
	
	@Autowired
	private EligibilityCheckRequestDao dao;

	@Test
	public void checkPortInEligibility() throws Exception {
		try {

			PortInEligibilityInfo result = dao.checkPortInEligibility("5871901868", PortInEligibility.PORT_VISIBILITY_TYPE_EXTERNAL_2C, 1); //("0000000000", "INT_2I", 1);
			System.out.println(result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
