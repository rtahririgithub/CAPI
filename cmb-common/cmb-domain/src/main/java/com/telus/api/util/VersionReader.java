package com.telus.api.util;

import java.io.InputStream;
import java.util.PropertyResourceBundle;

import com.telus.api.ClientAPI;

public class VersionReader {

	public static String readVersionFromClassPath(String path) {
		return readVersionFromClassPath(path, VersionReader.class);
	}
	
	public static String readVersionFromClassPath(String path, Class clazz) {
		
		String version = "unknown";
		
		try {
			
			InputStream is = clazz.getResourceAsStream(path);
			if (is != null) {
				version = new PropertyResourceBundle(is).getString("version.number");
			}
			
		} catch (Exception e) {
			System.err.println("Unable to retrieve module version from path [" + path + "]");
		}
		
		return version;
	}
	
	public static String getVersion(String resourceName) {
		
		String version = "unknown";
		
		try {
			
			InputStream is = ClientAPI.class.getResourceAsStream(resourceName);
			if (is != null) {
				
				PropertyResourceBundle bundle = new PropertyResourceBundle(is);
				String appVersion = bundle.getString("appVersion");
				String buildLabel = bundle.getString("buildLabel");
				if (buildLabel.equalsIgnoreCase("buildlabel")) {
					buildLabel = "Local";
				}
				version = appVersion + "-" + buildLabel;
			}
			
		} catch (Exception e) {
			System.err.println("Unable to retrieve module version: " + e.getMessage());
		}
		
		return version;
	}
}
