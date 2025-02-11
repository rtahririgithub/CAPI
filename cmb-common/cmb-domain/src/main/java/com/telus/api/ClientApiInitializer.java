package com.telus.api;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 18-Jan-2008
 */

public class ClientApiInitializer {
	
	private static String user = "18654";
	private static String password = "apollo";
	private static String applicationCode = "INITIALIZER";

	public static void main(String[] args) {
		
		Timer timer = new Timer(true);
		
		timer.scheduleAtFixedRate( new TimerTask() {
		
			public void run() {
				
				try {
					
					System.out.println("Initializing ClientAPI...");
					
					ClientAPI.getInstance(user, password, applicationCode);
					
					System.out.println("ClientAPI has been successfully initialized.");

				} catch (Exception e) {
					System.out.println("ClientAPI initialization failed: " + e.getMessage());
					e.printStackTrace();
				}
		
			}
		
		}, 10000, 300000);
	}
		
}
