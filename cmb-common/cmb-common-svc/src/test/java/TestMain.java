import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.telus.framework.foundation.ClassPathResourceUtil;

/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */

/**
 * @author Pavel Simonovsky
 *
 */
public class TestMain {

	/**
	 * 
	 */
	public TestMain() {
	}

	public static void main(String[] args) {
		try {

			Properties props = PropertiesLoaderUtils.loadAllProperties("config-mapping.properties");
			System.out.println(props);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
