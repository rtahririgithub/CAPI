package com.telus.cmb.reference.svc;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;


public class ReferenceDataServiceControllerTest {

	public static void main(String[] args) {
		try {
			
			Hashtable<String, String> environment = new Hashtable<String, String>();
			environment.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			environment.put(Context.PROVIDER_URL, "t3://localhost:7001");
			
			InitialContext context = new InitialContext(environment);
			
			ReferenceServiceController controller = (ReferenceServiceController) context.lookup("ReferenceServiceController#com.telus.cmb.reference.svc.ReferenceServiceController");
			
			controller.clearCache();
			
//			controller.restartCache();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
