package com.telus.provider.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class LdapProperties extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LdapProperties() {
		super();
	}

	public LdapProperties(Properties paramProperties) {
		super(paramProperties);
	}

	/**
	 * @Override
	 */
	public void load(InputStream inStream) throws IOException {
		super.load(inStream);
		Enumeration propertyNamesEnumeration = propertyNames();
		if (propertyNamesEnumeration != null) {
			while (propertyNamesEnumeration.hasMoreElements()) {
				String propertyName = (String) propertyNamesEnumeration.nextElement();
				String value = getProperty(propertyName);
				if (value != null) {
					String[] commaSeperatedValues = value.split(",");
					put(propertyName, commaSeperatedValues);
				}
			}
		}
	}
}
