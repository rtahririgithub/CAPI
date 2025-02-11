package com.telus.cmb.common.aop.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Assert;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorIds;
import com.telus.api.SystemCodes;

public class BanValidatorTestHelper {
	public static void executeBanRangeTest(Object o, Method m, Object[] arguments, int banPosition) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		try {
			arguments[banPosition] = -1;
			m.invoke(o, arguments);
			fail("Exception expected.");
		} catch (InvocationTargetException e) {
			assertTrue(e.getCause() instanceof ApplicationException);
			ApplicationException ae = (ApplicationException) e.getCause();
			assertEquals(SystemCodes.CMB_EJB, ae.getSystemCode());
		}

		try {
			arguments[banPosition] = 0;
			m.invoke(o, arguments);
			fail("Exception expected.");
		} catch (InvocationTargetException e) {
			assertTrue(e.getCause() instanceof ApplicationException);
			ApplicationException ae = (ApplicationException) e.getCause();
			assertEquals(SystemCodes.CMB_EJB, ae.getSystemCode());
		}

		try {
			arguments[banPosition] = 1;
			m.invoke(o, arguments);
		} catch (Exception e) {
			if (e.getCause() instanceof ApplicationException) {
				ApplicationException ae = (ApplicationException) e.getCause();
				Assert.assertNotSame(ErrorIds.BAN_NOT_FOUND, ae.getErrorCode());
			}
		}

	}
}
