package com.telus.cmb.reference.svc;

import org.junit.BeforeClass;
import org.junit.Test;

import com.telus.api.util.RemoteBeanProxyFactory;
import com.telus.eas.message.info.ApplicationMessageInfo;


public class ApplicationMessageFacadeRemoteIntTest {

	static ApplicationMessageFacade facade;
	
	@BeforeClass
	public static void prepare() {
//		service = (ReferenceDataSvc) RemoteBeanProxyFactory.createProxy(
//				ReferenceDataSvc.class, 
//				"ReferenceDataSvc#com.telus.cmb.reference.svc.impl.ReferenceDataSvcHome", 
//				"t3://wldv103umgenutilsvc:20152");

		facade = (ApplicationMessageFacade) RemoteBeanProxyFactory.createProxy(
				ApplicationMessageFacade.class, 
				"ApplicationMessageFacade#com.telus.cmb.reference.svc.ApplicationMessageFacade", 
				"t3://sn25257:30152");
	
	}

	@Test
	public void getApplicationMessage() throws Throwable {
		String application = "SMARTDESKTOP";
		String audienceType = null;
		ApplicationMessageInfo info = facade.getApplicationMessage(application, audienceType, 1, 84);
		System.out.println(info);
	}
	
}
