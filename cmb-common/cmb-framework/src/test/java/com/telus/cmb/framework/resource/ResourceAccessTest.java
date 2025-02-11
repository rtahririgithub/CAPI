package com.telus.cmb.framework.resource;

import org.testng.annotations.Test;

public class ResourceAccessTest {


	@Test
	public void test() {
		try {
			
			ResourceAccessTestClient client = new ResourceAccessTestClient();
			System.out.println(client.sayHello("aa", "bb"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
