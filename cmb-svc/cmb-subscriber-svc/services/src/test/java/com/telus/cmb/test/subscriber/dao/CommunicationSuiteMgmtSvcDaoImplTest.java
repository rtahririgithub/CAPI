package com.telus.cmb.test.subscriber.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.telus.cmb.subscriber.lifecyclefacade.dao.CommunicationSuiteMgmtSvcDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.CommSuiteMgmtRestSvcDao;

@ContextConfiguration(locations="classpath:com/telus/cmb/test/subscriber/dao/test-context-dao.xml")
public class CommunicationSuiteMgmtSvcDaoImplTest extends AbstractTestNGSpringContextTests {
	
	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
	}
	
	@Autowired
	private CommunicationSuiteMgmtSvcDao dao;
	
	@Autowired
	private CommSuiteMgmtRestSvcDao restDao;

	@Test
	public void ping() throws Exception {
		try {

			System.out.println(restDao.test());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
