package com.telus.provider.util;

public class AlwaysTrueEjbDirectConfigReaderInMemoryImpl implements EjbDirectConfigReader{

	public boolean useNewEjb() {
		return true;
	}
	

}
