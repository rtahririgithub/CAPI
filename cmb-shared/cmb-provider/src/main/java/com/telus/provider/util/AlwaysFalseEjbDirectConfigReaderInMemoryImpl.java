package com.telus.provider.util;

public class AlwaysFalseEjbDirectConfigReaderInMemoryImpl implements
		EjbDirectConfigReader {

	public boolean useNewEjb() {		
		return false;
	}

}
