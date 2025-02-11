package com.telus.cmb.account.utilities;


import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.telus.cmb.account.informationhelper.svc.impl.SampleExceptionLogger;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.MemoInfo;

public class ExceptionParamLoggerTest {
	SampleExceptionLogger sample;

	@Before
	public void setUp() throws Exception {
		sample = new SampleExceptionLogger();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=TelusException.class)
	public void testNoParam() throws TelusException {
		sample.noParams();
	}

	@Test(expected=TelusException.class)
	public void testSingleSimpleParam() throws TelusException {
		sample.oneSimpleParam(5);
	}

	@Test(expected=TelusException.class)
	public void testOneStringParam() throws TelusException {
		sample.oneStringParam("hello");
	}

	@Test(expected=TelusException.class)
	public void testOneCollectionParam() throws TelusException {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Hello");
		arrayList.add("World");
		sample.oneCollectionParam(arrayList);
	}

	@Test(expected=TelusException.class)
	public void testOneArrayParam() throws TelusException {
		sample.oneArrayParam(new String[] {"Hello", "World"});
	}

	@Test(expected=TelusException.class)
	public void testOneArrayInfoClassParam() throws TelusException {
		MemoInfo[] memoInfo = {new MemoInfo(1, "abc", "def", "ghi", "jkl"),
				new MemoInfo(2, "abc", "def", "ghi", "jkl")};
		sample.oneArrayInfoClassParam(memoInfo);
	}

	@Test(expected=TelusException.class)
	public void testInfoClassParam() throws TelusException {
		MemoInfo memoInfo = new MemoInfo(1, "abc", "def", "ghi", "jkl");
		sample.infoClassParam(memoInfo);
	}

	@Test(expected=TelusException.class)
	public void testInfo2ClassParam() throws TelusException {
		AccountInfo[] accountInfo = {new AccountInfo('A', 'B'),
				new AccountInfo('C', 'D')};
		sample.oneArrayInfoClassParam(accountInfo);
	}



	@Test(expected=TelusException.class)
	public void testOneObjectParamNoToString() throws TelusException {
		SampleExceptionLogger.ComplexObjectNoToString obj = sample.new ComplexObjectNoToString();
		sample.oneObjectParamNoToString(obj);
	}

	@Test(expected=TelusException.class)
	public void testOneObjectParamToString() throws TelusException {
		SampleExceptionLogger.ComplexObjectToString obj = sample.new ComplexObjectToString();
		sample.oneObjectParamToString(obj);
	}

	@Test(expected=TelusException.class)
	public void testThreeParams() throws TelusException {
		SampleExceptionLogger.ComplexObjectToString obj = sample.new ComplexObjectToString();
		sample.threeParams(5, "Hello", obj);
	}

	@Test
	public void testSensitive() throws TelusException {
		SampleExceptionLogger.ComplexObjectToString obj = sample.new ComplexObjectToString();
		try {
			sample.threeParamsSensitive1(5, "Hello", obj);
			fail("No exception thrown");
		} catch (Exception e) {
		}

		try {
			sample.threeParamsSensitive2(5, "Hello", obj);
			fail("No exception thrown");
		} catch (Exception e) {
		}

		try {
			sample.threeParamsSensitive3(5, "Hello", obj);
			fail("No exception thrown");
		} catch (Exception e) {
		}

	}

	@Test
	public void testBANSensitive() {
		SampleExceptionLogger.ComplexObjectToString obj = sample.new ComplexObjectToString();
		try {
			sample.threeParamsBAN(-1, "Hello", obj);
			fail("No exception thrown");
		} catch (Exception e) {
		}

		try {
			sample.threeParamsBANSensitive1(5, "Hello", obj);
			fail("No exception thrown");
		} catch (Exception e) {
		}

		try {
			sample.threeParamsBANSensitive2(5, "Hello", obj);
			fail("No exception thrown");
		} catch (Exception e) {
		}

		try {
			sample.threeParamsBANSensitive3(5, "Hello", obj);
			fail("No exception thrown");
		} catch (Exception e) {
		}

		try {
			sample.threeParamsBANSensitive3(5, "Hello", null);
			fail("No exception thrown");
		} catch (Exception e) {
		}

		try {
			sample.threeParamsBANSensitive3(5, null, obj);
			fail("No exception thrown");
		} catch (Exception e) {
		}
	}


}
