package com.telus.cmb.account.informationhelper.svc.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.aop.utilities.BANValue;
import com.telus.cmb.common.logging.Sensitive;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.Info;

public class SampleExceptionLogger {
	public void noParams() throws TelusException {
		throw new TelusException();
	}

	public void oneSimpleParam(int number) throws TelusException {
		throw new TelusException();
	}

	public void oneStringParam(String str) throws TelusException {
		throw new TelusException();
	}

	public void oneCollectionParam(Collection<?> collection) throws TelusException {
		throw new TelusException();
	}

	public void oneArrayParam(String[] stringArray) throws TelusException {
		throw new TelusException();
	}

	public void oneObjectParamNoToString(ComplexObjectNoToString obj) throws TelusException {
		throw new TelusException();
	}

	public void oneObjectParamToString(ComplexObjectToString obj) throws TelusException {
		throw new TelusException();
	}

	public void threeParams(int num, String str, ComplexObjectToString obj) throws TelusException {
		throw new TelusException();
	}

	public void threeParamsBAN(@BANValue int num, String str, ComplexObjectToString obj) throws TelusException, ApplicationException {
		throw new TelusException();
	}

	public void threeParamsSensitive1(@Sensitive int num, String str, ComplexObjectToString obj) throws TelusException, ApplicationException {
		throw new TelusException();
	}

	public void threeParamsSensitive2(int num,@Sensitive String str, ComplexObjectToString obj) throws TelusException, ApplicationException {
		throw new TelusException();
	}

	public void threeParamsSensitive3(int num,String str, @Sensitive ComplexObjectToString obj) throws TelusException, ApplicationException {
		throw new TelusException();
	}

	public void threeParamsBANSensitive1(@BANValue @Sensitive int num,String str, ComplexObjectToString obj) throws TelusException, ApplicationException {
		throw new TelusException();
	}

	public void threeParamsBANSensitive2(@BANValue int num,@Sensitive String str, ComplexObjectToString obj) throws TelusException, ApplicationException {
		throw new TelusException();
	}

	public void threeParamsBANSensitive3(@BANValue int num, String str, @Sensitive ComplexObjectToString obj) throws TelusException, ApplicationException {
		throw new TelusException();
	}
	public class ComplexObjectNoToString {
		String str;
		int integer;
		ArrayList<String> list;

		public ComplexObjectNoToString() {
			str = "abc";
			integer = 5;
			list = new ArrayList<String>();
			list.add("happy");
			list.add("day");
		}
	}

	public class ComplexObjectToString extends ComplexObjectNoToString {
		@Override
		public String toString() {

			return "." + str + "." + integer + "." + list;
		}
	}

	public void oneArrayInfoClassParam(Info[] infoClass) throws TelusException {
		throw new TelusException();
	}

	public void infoClassParam(Info infoClass) throws TelusException {
		throw new TelusException();
	}

}
