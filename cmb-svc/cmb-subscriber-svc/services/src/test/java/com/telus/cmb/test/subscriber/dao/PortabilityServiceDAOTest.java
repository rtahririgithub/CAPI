package com.telus.cmb.test.subscriber.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.subscriber.lifecyclefacade.dao.PortabilityServiceDao;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.eas.portability.info.LocalServiceProviderInfo;

@ContextConfiguration(locations="classpath:com/telus/cmb/test/subscriber/dao/test-context-dao.xml")
public class PortabilityServiceDAOTest  extends AbstractTestNGSpringContextTests{

	
static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
  @Autowired
  private PortabilityServiceDao dao;
  
  @Test
   public void getLocalServiceProvider()  throws Exception {
	  try {  
	  LocalServiceProviderInfo info = dao.getLocalServiceProvider("4161744425");
	  boolean isPortActivity = false;
		String localServiceProviderId = info.getLocalServiceProviderId();
		if (localServiceProviderId != null && !localServiceProviderId.equals("null")){
			System.out.println("LocalServiceProvider for PhoneNumber  4160717248 \n"+info.toString());
			System.out.println(AppConfiguration.getLocalServiceProviderIDTELUS()); 
			if(!(localServiceProviderId.equals(AppConfiguration.getLocalServiceProviderIDTELUS()))){
				isPortActivity =true;
			}
		}
		
	  }catch(Exception e){
		  e.printStackTrace();
	  }
	  
  }
}
