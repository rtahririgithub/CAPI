/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.productequipment.svc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.api.reference.Brand;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.eas.equipment.info.EquipmentInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@ContextConfiguration(locations="classpath:application-context-test.xml")
//@ActiveProfiles({"standalone"})
@ActiveProfiles({"remote", "pt140"})
public class ProductEquipmentHelperTest extends AbstractTestNGSpringContextTests {

	static {
		System.setProperty("weblogic.Name", "standalone");
	}
	
	@Autowired
	private ProductEquipmentHelper helper;

	@Test
	public void getEquipmentInfobySerialNo() throws Exception {
		EquipmentInfo info = helper.getEquipmentInfobySerialNo("8912239900003026707"); // 8912239900003026707 8912240624189407419
		System.out.println(info);
	}
	
	@Test
	public void getEquipmentInfobySerialNumber() throws Exception {
		EquipmentInfo info = helper.getEquipmentInfobySerialNumber("8912239900003334747"); // 8912239900003169929 8912239900003334747 8912239900003169929 8912239900003026707 8912239900003354810 8912239900000239998 8912230000374392165 8912240624189407419 8912239900003007962 8912239900002951780 8912239900003004647 8912239900003032648    
		System.out.println(info);
	}
	
	@Test
	public void getEquipmentInfobyPhoneNo() throws Exception {
		EquipmentInfo info = helper.getEquipmentInfobyPhoneNo("7781559908");
		System.out.println(info);
	}
	
	@Test
	public void getMuleBySIM() throws Exception {
		EquipmentInfo info = helper.getMuleBySIM("000810232929310");
		System.out.println(info);
	}
	
	@Test
	public void getAssociatedHandsetbyUSIMID() throws Exception {
		EquipmentInfo info = helper.getAssociatedHandsetByUSIMID("8912230000000066142");
		System.out.println(info);
	}
	
	@Test
	public void retrieveVirtualEquipment() throws Exception {
		EquipmentInfo info = helper.retrieveVirtualEquipment("8912240624189407419", "");
		System.out.println(info);
	}
	
	@Test
	public void koodo_pre2post_brand_validation() throws Exception {
		EquipmentInfo info = helper.getEquipmentInfobySerialNumber("8912250000002000089"); // 8912240624189407419
		System.out.println(info);
		System.out.println("getBrandIds(): " + Arrays.toString(info.getBrandIds()) + ".");
		System.out.println("getBrandIds0(): " + Arrays.toString(info.getBrandIds0()) + ".");
		System.out.println("isValidForBrand() [1] = [" + info.isValidForBrand(Brand.BRAND_ID_TELUS) + "].");
		System.out.println("isValidForBrand() [2] = [" + info.isValidForBrand(Brand.BRAND_ID_AMPD) + "].");
		System.out.println("isValidForBrand() [3] = [" + info.isValidForBrand(Brand.BRAND_ID_KOODO) + "].");
		System.out.println("isValidForBrand() [4] = [" + info.isValidForBrand(Brand.BRAND_ID_CLEARNET) + "].");
		System.out.println("isValidForBrand() [-1] = [" + info.isValidForBrand(Brand.BRAND_ID_NOT_APPLICABLE) + "].");
		System.out.println("isValidForBrand() [0] = [" + info.isValidForBrand(0) + "].");
		System.out.println("isValidForBrand0() [1] = [" + info.isValidForBrand0(Brand.BRAND_ID_TELUS) + "].");
		System.out.println("isValidForBrand0() [2] = [" + info.isValidForBrand0(Brand.BRAND_ID_AMPD) + "].");
		System.out.println("isValidForBrand0() [3] = [" + info.isValidForBrand0(Brand.BRAND_ID_KOODO) + "].");
		System.out.println("isValidForBrand0() [4] = [" + info.isValidForBrand0(Brand.BRAND_ID_CLEARNET) + "].");
		System.out.println("isValidForBrand0() [-1] = [" + info.isValidForBrand0(Brand.BRAND_ID_NOT_APPLICABLE) + "].");
		System.out.println("isValidForBrand0() [0] = [" + info.isValidForBrand0(0) + "].");
	}
	
}