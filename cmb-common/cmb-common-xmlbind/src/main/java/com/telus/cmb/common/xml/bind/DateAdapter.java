package com.telus.cmb.common.xml.bind;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateAdapter extends XmlAdapter<String, Date> {

	private static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd"); 
	
	@Override
	public String marshal(Date value) throws Exception {
		return (value == null ? null : DATE_FORMATTER.format(value));
	}

	@Override
	public Date unmarshal(String value) throws Exception {
		return (value == null ? null : DatatypeConverter.parseDate(value).getTime());
	}

}