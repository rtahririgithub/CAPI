package com.telus.ait.fwk.util;

import org.apache.commons.lang3.LocaleUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Locale;

public class LocaleAdapter extends XmlAdapter<String, Locale> {

	@Override
	public Locale unmarshal(String localeStr) throws Exception {
		return LocaleUtils.toLocale(localeStr);
	}

	@Override
	public String marshal(Locale locale) throws Exception {
		return locale.toString();
	}

}
