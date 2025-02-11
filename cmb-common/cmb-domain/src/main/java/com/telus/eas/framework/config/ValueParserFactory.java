package com.telus.eas.framework.config;

public class ValueParserFactory {
	public static ValueParser getStringValueParser() {
		return new ValueParser() {
			
			public Object parse(Object value) {
				return String.valueOf(value);
			}
		};
	}
}
