/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;


/**
 *
 * <CODE>Version2</CODE> holds the Java representation of a version number for software articacts.
 * The number is in the form of 1.2.3.4 (major#.development#.qa#.production#).  This sceme allows
 * artifacts to be modified in QA and Production, while still having a unique number that is less
 * than it's counterpart in the preceding environment.
 *
 */
public final class Version2 {

  public static void main(String[] args) throws Throwable {
    System.err.println(Version2.getVersionFromFile("..\\..\\source\\java\\com\\telus\\api\\ClientAPI.version2"));
  }


  public static final String DEFAULT_PROPERTY = "version.number";

  public static Version2 getVersionFromClassPath(ClassLoader classLoader, String file) throws Throwable {
    return getVersionFromClassPath(classLoader, file, DEFAULT_PROPERTY);
  }

  public static Version2 getVersionFromClassPath(ClassLoader classLoader, String file, String property) throws Throwable {

    System.err.println("Version2: classLoader=[" + classLoader + "]");
    System.err.println("Version2: classLoader.getClass()=[" + classLoader.getClass() + "]");
    System.err.println("Version2: classLoader.hashCode()=[" + classLoader.hashCode() + "]");

    System.err.println("Version2: file=[" + file + "]");
    System.err.println("Version2: property=[" + property + "]");

    URL url = classLoader.getResource(file);

    System.err.println("Version2: url=[" + url + "]");

    if(url == null) {
      throw new FileNotFoundException("file doesn't exist in classpath or insufficient access privileges: [" + file + "]");
    }

    return getVersionFromInputStream(url.openStream(), property);
  }



  public static Version2 getVersionFromClassPath(String file) throws Throwable {
    return getVersionFromClassPath(file, DEFAULT_PROPERTY);
  }

  public static Version2 getVersionFromClassPath(String file, String property) throws Throwable {
    return getVersionFromClassPath(Version2.class.getClassLoader(), file, property);
  }

  public static Version2 getVersionFromFile(String file) throws Throwable {
    return getVersionFromFile(file, DEFAULT_PROPERTY);
  }

  public static Version2 getVersionFromFile(String path, String property) throws Throwable {
    System.err.println("Version2: path=[" + path + "]");
    System.err.println("Version2: property=[" + property + "]");

    path = path.replace('/', File.separatorChar);

    File file = new File(path);

    System.err.println("Version2: file=[" + file + "]");
    System.err.println("Version2: file.getAbsolutePath()=[" + file.getAbsolutePath() + "]");

    FileInputStream in = new FileInputStream(file);

    return getVersionFromInputStream(in, property);
  }

  public static Version2 getVersionFromURL(URL url) throws Throwable {
    return getVersionFromURL(url, DEFAULT_PROPERTY);
  }

  public static Version2 getVersionFromURL(URL url, String property) throws Throwable {
    System.err.println("Version2: url=[" + url + "]");
    System.err.println("Version2: property=[" + property + "]");

    return getVersionFromInputStream(url.openStream(), property);
  }



  private static Version2 getVersionFromInputStream(InputStream in, String property) throws Throwable {
    try {
      Properties properties = new Properties();
      properties.load(in);
      String s = properties.getProperty(property, "0.0.0.0");
      System.err.println("Version2: "+property+"=[" + s + "]");

      return new Version2(s);
    } finally {
      in.close();
    }
  }


  public static URL getClassURL(ClassLoader classLoader, String className) {

    System.err.println("Version2:  getClassURL:  classLoader=[" + classLoader + "]");
    System.err.println("Version2:  getClassURL:  classLoader.getClass()=[" + classLoader.getClass() + "]");
    System.err.println("Version2:  getClassURL:  classLoader.hashCode()=[" + classLoader.hashCode() + "]");
    System.err.println("Version2:  getClassURL:  className=[" + className + "]");

    String classFile = className.replace('.', '/') + ".class";

    System.err.println("Version2:  getClassURL:  classFile=[" + classFile + "]");

    URL url = classLoader.getResource(classFile);

    System.err.println("Version2:  getClassURL:  url=[" + url + "]");

    return url;
  }

  public static URL getClassURL(ClassLoader classLoader, Class clazz) {
    return getClassURL(classLoader, clazz.getName());
  }

  public static URL getClassURL(Class clazz) {
    return getClassURL(clazz.getClassLoader(), clazz.getName());
  }



  private static final MessageFormat VERSION_FORMAT = new MessageFormat("{0,number,integer}.{1,number,integer}.{2,number,integer}.{3,number,integer}");

  private int major;
  private int development;
  private int releaseMonth;
  private int releaseYear;

  public Version2(final String versionString) throws Throwable {

    try {
      System.err.println("Version2: versionString=[" + versionString + "]");

      Object[] numbers = VERSION_FORMAT.parse(versionString);

      major = ((Number)numbers[0]).intValue();
      development = ((Number)numbers[1]).intValue();
      releaseMonth = ((Number)numbers[2]).intValue();
      releaseYear = ((Number)numbers[3]).intValue();

      /*
      for (int i=0; i<numbers.length && i< 4; i++) {
        //System.err.println("  " + i + ". ["+numbers[i]+"]");
        System.err.println("  " + i + ". ["+numbers[i].getClass()+"]=["+numbers[i]+"]");
      }
      */
    }catch (Throwable e) {
      //System.err.println("expected a version string in the form 1.2.3.4, not [" + versionString + "]");
      throw new TelusAPIException("expected a version string in the form 1.2.3.4, not [" + versionString + "]", e);
    }

  }

  /*
  public void setMajor(int major) {
    this.major = major;
  }
  */

  public int getMajor() {
    return major;
  }

  public void incrementMajor() {
    major++;
  }

  public int getDevelopment() {
    return development;
  }

  public void incrementDevelopment() {
    development++;
  }


  public int getReleaseMonth() {
    return releaseMonth;
  }


  public void incrementVersion(String releasePropertyPath) {
	  ReleaseVersion rv = new ReleaseVersion (releasePropertyPath);
	  try {
		  rv.loadReleaseVersion();
	  }catch (Throwable t) {
		  System.out.println (t);
	  }
	  if (rv.year > this.releaseYear) {
		  major ++;
		  development = 0;
		  this.releaseMonth = rv.month;
		  this.releaseYear = rv.year;
	  }else {
		  development ++;
		  this.releaseMonth = rv.month;
		  this.releaseYear = rv.year;
	  }
  }
  /*
  public void setProduction(int production) {
    this.production = production;
  }
  */

  public int getReleaseYear() {
    return releaseYear;
  }

  public String toString() {
    return major + "." + development + "." + releaseMonth + "." + releaseYear;
  }

	private class ReleaseVersion {
		int month = 0;
		int year = 0;
		private String path;
		
		ReleaseVersion (String path) {
			this.path = path;
		}

		public void loadReleaseVersion() throws Throwable {
			File file = new File(path);
			FileInputStream in = null;
		    try {
				in = new FileInputStream(file);
		        Properties properties = new Properties();
		        properties.load(in);
		        String s = properties.getProperty("version.year", "0.0");
		        System.out.println("ReleaseVersion: version.year=[" + s + "]");

		        MessageFormat VERSION_FORMAT = new MessageFormat("{0,number,integer}.{1,number,integer}");
		        Object[] numbers = VERSION_FORMAT.parse(s);
		        month = ((Number)numbers[0]).intValue();
		        year = ((Number)numbers[1]).intValue();
		      } catch (Exception t) {
		    	  System.out.println("Error reading from release.properties: " + t);
		      }finally {
		    	  if (in != null)
		    		  in.close();
		      }
		}
		
		public String getVersion() {
			return month + "." + (year < 10 ? "0"+year : ""+year);
		}
		
	}
}




