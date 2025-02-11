package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.EquipmentDao;

public class EquipmentDaoImplIntTest extends BaseInformationHelperIntTest{
	
	@Autowired
	EquipmentDao equipmentDao;

	@Test
	public void testGetUsimBySerialNumber() {
		assertEquals("8912230000000067934", equipmentDao.getUsimBySerialNumber("300000000100223"));
		assertNull(equipmentDao.getUsimBySerialNumber(""));
		assertNull(equipmentDao.getUsimBySerialNumber(null));
	}

	@Test
	public void testGetImsisByUsim() {
		String[] imsis = equipmentDao.getImsisByUsim("8912230000000067934");
		
		assertEquals(2, imsis.length);
		
		assertEquals(0, equipmentDao.getImsisByUsim("").length);
		assertEquals(0, equipmentDao.getImsisByUsim(null).length);
	}

}
