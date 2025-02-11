/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

/**
 * Loades an ANT BuildNumber file from the classpath.
 *
 * @see <a href="http://jakarta.apache.org/ant/manual/CoreTasks/buildnumber.html">Ant BuildNumber Task</a>
 */
public final class Version {

  private static final DecimalFormat DECIMAL_FORMAT   =  new DecimalFormat("####0.00");

  private final String file;
  private final int buildNumber;
  private final double version;
  private final String versionString;

  public Version(String file) throws Throwable {
    this.file = file;
    URL url = getClass().getClassLoader().getResource(file);

    if(url == null) {
      throw new FileNotFoundException("file doesn't exist in classpath or insufficient privileges: [" + file + "]");
    }

    System.err.println("loading [" + url + "]");

    InputStream in = url.openStream();
    try {
      Properties properties = new Properties();
      properties.load(in);
      String s = properties.getProperty("build.number", "0");
      System.err.println("    build.number=[" + s + "]");
      buildNumber   = Integer.parseInt(s);
      version       = buildNumber / 100.0;
      versionString = DECIMAL_FORMAT.format(version);
    } finally {
      in.close();
    }
  }

  public String getFile() {
    return file;
  }

  public int getBuildNumber() {
    return buildNumber;
  }

  public double getVersion() {
    return version;
  }

  public String getVersionString() {
    return versionString;
  }

  public String toString() {
    return getVersionString();
  }

  public static String getVersionFromClassPath(ClassLoader classLoader, String path ) {
	  return getVersionFromClassPath( classLoader, path, "version.number");
  }
  
  public static String getVersionFromClassPath(ClassLoader classLoader, String path, String property ) {
	  InputStream stream = null;
	  String version="unknown";
      try{
    	  System.err.println("BuildVersion: classLoader=[" + classLoader + "]");
    	  System.err.println("BuildVersion: classLoader.getClass()=[" + classLoader.getClass() + "]");
    	  System.err.println("BuildVersion: classLoader.hashCode()=[" + classLoader.hashCode() + "]");
    	  
    	  System.err.println("BuildVersion: file=[" + path + "]");
    	  System.err.println("BuildVersion: property=[" + property + "]");
    	  
    	  URL url = classLoader.getResource(path);
    	  System.err.println("BuildVersion: url=[" + url + "]");
    	  if(url == null) {
    		  System.err.println( "file doesn't exist in classpath or insufficient access privileges: [" + path + "]");
    	  }    
    	  else {
	    	  stream = url.openStream();
	    	  ResourceBundle versionBundle = new PropertyResourceBundle(stream);
	    	  version = versionBundle.getString(property);
    	  }
      } catch(Throwable t){
    	  System.err.print(t);
    	  t.printStackTrace();
      }finally {
    	   if (stream != null)  try {stream.close();} catch (IOException e) {}
      }
 	  System.err.println("BuildVersion: url=[" + version + "]");
      return version;
  }
  
}




