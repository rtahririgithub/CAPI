/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.config1;

import com.telus.api.*;
import java.util.*;

/**
 * This class acts as a proxy to the class of the same name in the standard
 * Configuration, please see its documentation for more information .
 *
 */
public interface Configuration {

  String getName();

  String[] getPath();

  String[] getPropertyNames();
  int getPropertyCount();
  Map getProperties();
  Map getProperties(Map map);

  boolean contains(String key);
  boolean isNull(String name) throws UnknownObjectException;

  Object getProperty(String name) throws UnknownObjectException;

  String getPropertyAsString(String name) throws UnknownObjectException;
  String[] getPropertyAsStringArray(String name) throws UnknownObjectException;
  int getPropertyAsInt(String name) throws UnknownObjectException;
  long getPropertyAsLong(String name) throws UnknownObjectException;
  double getPropertyAsDouble(String name) throws UnknownObjectException;
  boolean getPropertyAsBoolean(String name) throws UnknownObjectException;
  Date getPropertyAsDate(String name) throws UnknownObjectException;

  String getPropertyAsString(String name, String defaultValue);
  String[] getPropertyAsStringArray(String name, String defaultValue);
  int getPropertyAsInt(String name, int defaultValue);
  long getPropertyAsLong(String name, long defaultValue);
  double getPropertyAsDouble(String name, double defaultValue);
  boolean getPropertyAsBoolean(String name, boolean defaultValue);
  Date getPropertyAsDate(String name, Date defaultValue);

  boolean addConfigurationChangeListener(ConfigurationChangeListener aListener);
  boolean removeConfigurationChangeListener(ConfigurationChangeListener aListener);

}



