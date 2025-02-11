/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.config1.info;

import com.telus.api.*;
import com.telus.api.config1.*;
import com.telus.eas.framework.info.*;
import java.util.*;
import java.io.*;

public class ConfigurationInfo extends Info implements Configuration, Serializable {

   static final long serialVersionUID = 1L;

  private final String[] path;
  private final Map properties;

  //=========================================================================================

  public ConfigurationInfo(String[] path, Map properties) {
    this.path = path;
    this.properties = properties;
  }

  //=========================================================================================

  public String getName() {
    return path[path.length - 1];
  }

  public String[] getPath() {
    return (String[])path.clone();
  }

  public int getPropertyCount() {
    return properties.size();
  }

  public Map getProperties() {
    return getProperties(new HashMap());
  }

  public Map getProperties(Map map) {
    map.putAll(properties);
    return map;
  }

  public boolean contains(String key) {
    return properties.containsKey(key);
  }

  public synchronized String[] getPropertyNames() {
    return (String[])properties.keySet().toArray(new String[properties.size()]);
  }

  public boolean isNull(String name) throws UnknownObjectException {
    return (getProperty(name) == null);
  }

  //=========================================================================================

  public Object getProperty(String name, boolean throwNullPointerException) throws UnknownObjectException {
    Object value = properties.get(name);
    if (value == null){
      if (!contains(name)) {
        throw new UnknownObjectException(name);
      } else if (throwNullPointerException) {
        throw new NullPointerException("the value for \"" + name + "\" is null");
      }
    }
    return value;
  }

  public Object getProperty(String name) throws UnknownObjectException {
    return getProperty(name, false);
  }

  //=========================================================================================

  public String getPropertyAsString(String name) throws UnknownObjectException {
    Object value = getProperty(name);
    if (value == null) {
      return null;
    }

    return value.toString();
  }

  public String[] getPropertyAsStringArray(String name) throws UnknownObjectException {
    Object value = getProperty(name);
    if (value == null) {
      return null;
    }

    if (value instanceof String[]) {
      return (String[])value;
    }

    return new String[]{value.toString()};
  }

  public int getPropertyAsInt(String name) throws UnknownObjectException {
    Object value = getProperty(name, true);

    if (value instanceof Number) {
      return ((Number)value).intValue();
    } else {
      return Integer.parseInt(getPropertyAsString(name));
    }
  }

  public long getPropertyAsLong(String name) throws UnknownObjectException {
    Object value = getProperty(name, true);

    if (value instanceof Number) {
      return ((Number)value).longValue();
    } else {
      return Long.parseLong(getPropertyAsString(name));
    }
  }

  public double getPropertyAsDouble(String name) throws UnknownObjectException {
    Object value = getProperty(name, true);

    if (value instanceof Number) {
      return ((Number)value).doubleValue();
    } else {
      return Double.parseDouble(getPropertyAsString(name));
    }
  }

  public boolean getPropertyAsBoolean(String name) throws UnknownObjectException {
    Object value = getProperty(name, true);

    if (value instanceof Boolean) {
      return ((Boolean)value).booleanValue();
    } else {
      return Boolean.valueOf(getPropertyAsString(name)).booleanValue();
    }
  }

  public Date getPropertyAsDate(String name) throws UnknownObjectException {
    Object value = getProperty(name);
    if (value == null) {
      return null;
    }

    if (value instanceof Date) {
      return (Date)value;
    } else {
      /** @todo use DATE_FORMAT */
      return new Date(getPropertyAsString(name));
    }
  }

  //=========================================================================================

  public String getPropertyAsString(String name, String defaultValue) {
    try {
      String value = getPropertyAsString(name);
      if (value == null) {
        value = defaultValue;
      }

      return value;
    } catch (Throwable e) {
      return defaultValue;
    }
  }

  public String[] getPropertyAsStringArray(String name, String defaultValue) {
    String[] defaultValueArray = null;
    if (defaultValue == null) {
      defaultValueArray = new String[0];
    } else {
      defaultValueArray = new String[1];
      defaultValueArray[0] = defaultValue;
    }

    try {
      String[] value = getPropertyAsStringArray(name);
      if (value == null) {
        return defaultValueArray;
      }
      return value;
    } catch (Throwable e) {
      return defaultValueArray;
    }
  }

  public int getPropertyAsInt(String name, int defaultValue) {
    try {
      return getPropertyAsInt(name);
    } catch (Throwable e) {
      return defaultValue;
    }
  }

  public long getPropertyAsLong(String name, long defaultValue) {
    try {
      return getPropertyAsLong(name);
    } catch (Throwable e) {
      return defaultValue;
    }
  }

  public double getPropertyAsDouble(String name, double defaultValue) {
    try {
      return getPropertyAsDouble(name);
    } catch (Throwable e) {
      return defaultValue;
    }
  }

  public boolean getPropertyAsBoolean(String name, boolean defaultValue) {
    try {
      return getPropertyAsBoolean(name);
    } catch (Throwable e) {
      return defaultValue;
    }
  }

  public Date getPropertyAsDate(String name, Date defaultValue) {
    try {
      Date returnValue = getPropertyAsDate(name);
      if (returnValue == null) {
        return defaultValue;
      } else {
        return returnValue;
      }
    } catch (Throwable e) {
      return defaultValue;
    }
  }

  //=========================================================================================

  public boolean addConfigurationChangeListener(ConfigurationChangeListener aListener) {
    throw new UnsupportedOperationException();
  }

  public boolean removeConfigurationChangeListener(ConfigurationChangeListener aListener) {
    throw new UnsupportedOperationException();
  }


}
