/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.*;
import com.telus.api.config0.*;
import java.util.*;
import java.io.*;



public class ConfigurationInfo implements Configuration, Serializable {

   static final long serialVersionUID = 1L;

  private ConfigurationInfo parent;
  private int id;
  private String name;

  private final Map properties = new HashMap();


  public ConfigurationInfo() {
  }

  /**
   * Creates a child Configuration node.
   *
   */
  public ConfigurationInfo(ConfigurationInfo parent, int id, String name) {
    this.parent = parent;
    this.id = id;
    this.name = name;
  }

  /**
   * Creates a root (one with no parent) Configuration node.
   *
   */
  public ConfigurationInfo(int id, String name) {
    this(null, id, name);
  }


/*
  public ConfigurationInfo getParent0() {
    return parent;
  }

  public void setParent0(ConfigurationInfo parent) {
    this.parent = parent;
  }
*/

  public void copyFrom(ConfigurationInfo object) {
    this.id = object.id;
    this.name = object.name;

    this.properties.clear();
    object.getDefined(this.properties);
  }

  public ConfigurationInfo getParent0() {
    return parent;
  }

  public void setParent0(ConfigurationInfo parent) {
    this.parent = parent;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void put(String key, String value) {
    /*  --null is a valid value
    if(value == null) {
      value = "";
    }
    */
    properties.put(key, value);
  }

  /**
   *  Retrieves a property explicitly set (or overridden) at this level.
   *
   */
  public String getDefined(String key) {
    return (String)properties.get(key);
  }

  public boolean containsDefined(String key) {
    return properties.containsKey(key);
  }

  /**
   *  Retrieves a property explicitly set at this level or
   *  inherited from one of its ancestors.
   *
   */
  public String get(String key) {
    String value = getDefined(key);

    if(value == null && parent != null)
    {
        value = parent.get(key);
    }

    return value;
  }

  public boolean contains(String key) {
    boolean containsKey = containsDefined(key);

    if(!containsKey && parent != null)
    {
        containsKey = parent.contains(key);
    }

    return containsKey;
  }

  /**
   *  Retrieves all properties explicitly set (or overridden) at this level.
   *
   */
  public Map getDefined(Map map) {
    Iterator i = properties.keySet().iterator();
    while(i.hasNext()) {
      String key   = (String)i.next();
      Object value = properties.get(key);
      map.put(key, value);  // overrides old values.
    }

    return map;
  }

  /**
   *  Retrieves all properties explicitly set at this level and
   *  and at every ancestor.
   *
   */
  public Map get(Map map) {
    if(parent != null) {
      parent.get(map);
    }

    getDefined(map);

    return map;
  }

  public List getPath(List list) {
    if(parent != null) {
      parent.getPath(list);
    }

    list.add(name);

    return list;
  }

  public String[] getPath() {
    List list = getPath(new ArrayList(16));
    return (String[])list.toArray(new String[list.size()]);
  }

  public String[] appendToPath(String name) {
    List list = getPath(new ArrayList(16));

    list.add(name);

    return (String[])list.toArray(new String[list.size()]);
  }

  /**
   *  Retrieves all properties explicitly set (or overridden) at this level.
   *
   */
  public int getDefinedPropertyCount0() {
    return properties.size();
  }

  /**
   *  Retrieves all properties explicitly set at this level and
   *  and at every ancestor.
   *
   */
  public int getPropertyCount0() {
    int count = getDefinedPropertyCount0();

    if(parent != null) {
      count += parent.getPropertyCount0();
    }

    return count;
  }


  public void clearDefinedProperties() {
    properties.clear();
  }



  public void refreshProperties() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void saveProperties() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public Configuration newConfiguration(String name) throws TelusAPIException, DuplicateObjectException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public Configuration getParent() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public Configuration[] getChildren() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public Configuration getChild(String name) throws TelusAPIException, UnknownObjectException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

/*
  public int getChildCount() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }
*/

  public void removeProperty(String name) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void removeProperties(String[] name) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void removeAllProperties() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void remove() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void removeProperty0(String name) throws TelusAPIException {
    removeProperties0(new String[]{name});
  }

  public void removeProperties0(String[] name) throws TelusAPIException {
    for (int i=0; i<name.length; i++) {
      properties.remove(name[i]);
    }
  }

  public void removeAllProperties0() throws TelusAPIException {
    properties.clear();
  }

  /*
  public void removeChildren() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }
  */

  public void setProperty(String name, String value) throws TelusAPIException {
    put(name, value);
  }

  public void setProperty(String name, long value) throws TelusAPIException {
    put(name, String.valueOf(value));
  }

  public void setProperty(String name, double value) throws TelusAPIException {
    put(name, String.valueOf(value));
  }

  public void setProperty(String name, boolean value) throws TelusAPIException {
    put(name, String.valueOf(value));
  }

  public void setProperty(String name, Date value) throws TelusAPIException {
    put(name, value.toString());  // TODO: use DATE_FORMAT
  }

  public synchronized String[] getPropertyNames() {
    Map map = get(new HashMap());
    return (String[])map.keySet().toArray(new String[map.size()]);
  }

  public synchronized String[] getDefinedPropertyNames() {
    return (String[])properties.keySet().toArray(new String[properties.size()]);
  }



  public int getPropertyCount() {
    return getPropertyCount0();
  }

  public Map getProperties() {
    return getProperties(new HashMap());
  }

  public Map getProperties(Map map) {
    return get(map);
  }

  public boolean isNull(String name) throws UnknownObjectException {
    return getPropertyAsString(name) == null;
  }

  public String getPropertyAsString(String name) throws UnknownObjectException {
    String value = get(name);

    if(value == null && !contains(name)) {
      throw new UnknownObjectException(name);
    }

    return value;
  }

  public String[] getPropertyAsStringArray(String name) throws UnknownObjectException {
    String value = get(name);

    List list = new ArrayList(10);

    StringTokenizer t = new StringTokenizer(value);
    while(t.hasMoreTokens()){
      list.add(t.nextToken());
    }

    return (String[])list.toArray(new String[list.size()]);
  }


  public int getPropertyAsInt(String name) throws UnknownObjectException {
    return Integer.parseInt(getPropertyAsString(name));
  }

  public long getPropertyAsLong(String name) throws UnknownObjectException {
    return Long.parseLong(getPropertyAsString(name));
  }

  public double getPropertyAsDouble(String name) throws UnknownObjectException {
    return Double.parseDouble(getPropertyAsString(name));
  }

  public boolean getPropertyAsBoolean(String name) throws UnknownObjectException {
    return Boolean.valueOf(getPropertyAsString(name)).booleanValue();
  }

  public Date getPropertyAsDate(String name) throws UnknownObjectException {
    return new Date(getPropertyAsString(name));  // TODO: use DATE_FORMAT

  }

  public String getPropertyAsString(String name, String defaultValue) {
    String value = get(name);

    if(value == null) {
      value = defaultValue;
    }

    return value;
  }

  public String[] getPropertyAsStringArray(String name, String defaultValue) {
    if(defaultValue == null) {
      defaultValue = "";
    }

    String value = get(name);

    if(value == null) {
      value = defaultValue;
    }

    List list = new ArrayList(10);

    StringTokenizer t = new StringTokenizer(value);
    while(t.hasMoreTokens()){
      list.add(t.nextToken());
    }

    return (String[])list.toArray(new String[list.size()]);
  }

  public int getPropertyAsInt(String name, int defaultValue) {
    return Integer.parseInt(getPropertyAsString(name, String.valueOf(defaultValue)));
  }

  public long getPropertyAsLong(String name, long defaultValue) {
    return Long.parseLong(getPropertyAsString(name, String.valueOf(defaultValue)));
  }

  public double getPropertyAsDouble(String name, double defaultValue) {
    return Double.parseDouble(getPropertyAsString(name, String.valueOf(defaultValue)));
  }

  public boolean getPropertyAsBoolean(String name, boolean defaultValue) {
    return Boolean.valueOf(getPropertyAsString(name, String.valueOf(defaultValue))).booleanValue();
  }

  public Date getPropertyAsDate(String name, Date defaultValue) {
    Date value = new Date(getPropertyAsString(name, null));

    if(value == null) {
      value = defaultValue;
    }

    return value;
  }

  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("ConfigurationInfo:[\n");
      s.append("    parent=[").append(parent).append("]\n");
      s.append("    id=[").append(id).append("]\n");
      s.append("    name=[").append(name).append("]\n");

      Iterator i = properties.keySet().iterator();
      while(i.hasNext()) {
        String key = (String)i.next();
        s.append("    properties[").append(key).append("]=\""+ properties.get(key) +"\"\n");
      }

      s.append("]");

      return s.toString();
  }
}




