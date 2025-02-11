package com.telus.ait.fwk.util;

public class Utils {
	
	protected static final String SYS_PROP_TDD = "thucydides.data.dir";
	protected static final String SYS_PROP_ENV = "environment";
	protected static final String DEFAULT_SYS_ENV = "dv103";
	
    public static String getQueryParamValueFromUrl(String url, String paramName) {
        String[] partsAroundParamName = url.split(paramName+"=");
        String[] partsAfterParamName = partsAroundParamName[1].split("&");
        return partsAfterParamName[0];
    }

    public static String getEnvironment() {
    	if (System.getProperty(SYS_PROP_TDD) != null) {
    		String environment = System.getProperty(SYS_PROP_TDD);
    		System.setProperty(SYS_PROP_ENV, environment);
    		return environment;
    	} else if (System.getProperty(SYS_PROP_ENV) != null) {
    		String environment = System.getProperty(SYS_PROP_ENV);
    		System.setProperty(SYS_PROP_TDD, environment);
        	return SYS_PROP_ENV;
        } else {
        	return DEFAULT_SYS_ENV;
        }
    }
    
}
