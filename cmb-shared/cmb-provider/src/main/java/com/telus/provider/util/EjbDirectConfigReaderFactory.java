package com.telus.provider.util;

public class EjbDirectConfigReaderFactory {
	
	private static EjbDirectConfigReader accountInformationHelperDirectConfigReader 
		= new AlwaysTrueEjbDirectConfigReaderInMemoryImpl();
	
	public static EjbDirectConfigReader getAccountInformationHelperDirectConfigReader() {
		return accountInformationHelperDirectConfigReader;
	}

	public static void setAccountInformationHelperDirectConfigReader(EjbDirectConfigReader accountInformationHelperDirectConfigReader) {
		EjbDirectConfigReaderFactory.accountInformationHelperDirectConfigReader = accountInformationHelperDirectConfigReader;
	}
	
}
