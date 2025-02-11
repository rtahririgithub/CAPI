package com.telus.cmb.common.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.telus.cmb.common.validation.BanValidator;

public class BanValidatorTest {

	@Test
	public void testIsValidRange() {
		assertTrue(BanValidator.isValidRange(1));
		assertTrue(BanValidator.isValidRange(10));
		assertTrue(BanValidator.isValidRange(Integer.MAX_VALUE));
		
		assertFalse(BanValidator.isValidRange(0));
		assertFalse(BanValidator.isValidRange(-1));
		assertFalse(BanValidator.isValidRange(Integer.MIN_VALUE));
	}

}
