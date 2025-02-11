/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.config0;

import java.util.Date;
import java.util.Map;

import com.telus.api.DuplicateObjectException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.config0.Configuration;
import com.telus.eas.config.info.ConfigurationInfo;
import com.telus.provider.TMProvider;


public class TMConfiguration  implements Configuration {
	 
  private final TMConfigurationManager configurationManager;
  protected final TMProvider provider;

  /**
   * @link aggregation
   */
  private final TMConfiguration parent;

  /**
   * @link aggregation
   */
  private final ConfigurationInfo delegate;

  public TMConfiguration(TMConfigurationManager configurationManager, TMConfiguration parent, ConfigurationInfo delegate, TMProvider provider) {
    super();
    this.configurationManager = configurationManager;
    this.delegate = delegate;
    this.parent = parent;
    this.provider=provider;
  }


  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public int getId() {
    return delegate.getId();
  }

  public String getName() {
    return delegate.getName();
  }

  public String[] getPath() {
    return delegate.getPath();
  }

  public String[] getPropertyNames() {
    return delegate.getPropertyNames();
  }

  public String[] getDefinedPropertyNames() {
    return delegate.getDefinedPropertyNames();
  }

  public int getPropertyCount() {
    return delegate.getPropertyCount();
  }

  public Map getProperties() {
    return delegate.getProperties();
  }

  public Map getProperties(Map map) {
    return delegate.getProperties(map);
  }

  public boolean contains(String key) {
    return delegate.contains(key);
  }

  public boolean containsDefined(String key) {
    return delegate.containsDefined(key);
  }


  public boolean isNull(String name) throws UnknownObjectException {
    return delegate.isNull(name);
  }

  public String getPropertyAsString(String name) throws UnknownObjectException {
    return delegate.getPropertyAsString(name);
  }

  public String[] getPropertyAsStringArray(String name) throws UnknownObjectException {
    return delegate.getPropertyAsStringArray(name);
  }

  public int getPropertyAsInt(String name) throws UnknownObjectException {
    return delegate.getPropertyAsInt(name);
  }

  public long getPropertyAsLong(String name) throws UnknownObjectException {
    return delegate.getPropertyAsLong(name);
  }

  public double getPropertyAsDouble(String name) throws UnknownObjectException {
    return delegate.getPropertyAsDouble(name);
  }

  public boolean getPropertyAsBoolean(String name) throws UnknownObjectException {
    return delegate.getPropertyAsBoolean(name);
  }

  public Date getPropertyAsDate(String name) throws UnknownObjectException {
    return delegate.getPropertyAsDate(name);
  }

  public String getPropertyAsString(String name, String defaultValue) {
    return delegate.getPropertyAsString(name, defaultValue);
  }

  public String[] getPropertyAsStringArray(String name, String defaultValue) {
    return delegate.getPropertyAsStringArray(name, defaultValue);
  }

  public int getPropertyAsInt(String name, int defaultValue) {
    return delegate.getPropertyAsInt(name, defaultValue);
  }

  public long getPropertyAsLong(String name, long defaultValue) {
    return delegate.getPropertyAsLong(name, defaultValue);
  }

  public double getPropertyAsDouble(String name, double defaultValue) {
    return delegate.getPropertyAsDouble(name, defaultValue);
  }

  public boolean getPropertyAsBoolean(String name, boolean defaultValue) {
    return delegate.getPropertyAsBoolean(name, defaultValue);
  }

  public Date getPropertyAsDate(String name, Date defaultValue) {
    return delegate.getPropertyAsDate(name, defaultValue);
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }


  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------
  public ConfigurationInfo getDelegate() {
    return delegate;
  }

  public void refreshDefinedProperties() throws TelusAPIException {
    try {
    	delegate.clearDefinedProperties();
        ConfigurationInfo copy = provider.getConfigurationManagerNew().loadProperties(delegate);
        delegate.copyFrom(copy);
    } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
  }

  public void refreshProperties() throws TelusAPIException {
    if(parent != null) {
      parent.refreshProperties();
    }
    refreshDefinedProperties();
  }

  public void saveProperty(String name) throws TelusAPIException {
    // TODO: if this call fails, the info will be out of sync with the database.
    String value = getPropertyAsString(name);
    configurationManager.addProperty(this, name, value);
  }

  public void setProperty(String name, String value) throws TelusAPIException {
    delegate.setProperty(name, value);
    saveProperty(name);
  }

  public void setProperty(String name, long value) throws TelusAPIException {
    delegate.setProperty(name, value);
    saveProperty(name);
  }

  public void setProperty(String name, double value) throws TelusAPIException {
    delegate.setProperty(name, value);
    saveProperty(name);
  }

  public void setProperty(String name, boolean value) throws TelusAPIException {
    delegate.setProperty(name, value);
    saveProperty(name);
  }

  public void setProperty(String name, Date value) throws TelusAPIException {
    delegate.setProperty(name, value);
    saveProperty(name);
  }

  /*
  public void saveProperties() throws TelusAPIException {
    throw new RuntimeException("TODO");
  }
  */

  public Configuration newConfiguration(String name) throws TelusAPIException, DuplicateObjectException {
    return newConfiguration0(name);
  }

  public TMConfiguration newConfiguration0(String name) throws TelusAPIException, DuplicateObjectException {
    return configurationManager.newConfiguration0(this, name);
  }

  public Configuration getParent() throws TelusAPIException {
    return getParent0();
  }

  public TMConfiguration getParent0() throws TelusAPIException {
    return parent;
  }

  public Configuration[] getChildren() throws TelusAPIException {
    return getChildren0();
  }

  public TMConfiguration[] getChildren0() throws TelusAPIException {
    return configurationManager.getChildren0(this);
  }

  public Configuration getChild(String name) throws TelusAPIException, UnknownObjectException {
    return getChild0(name);
  }

  public TMConfiguration getChild0(String name) throws TelusAPIException, UnknownObjectException {
    return configurationManager.getConfiguration0(delegate.appendToPath(name));
  }

  /*
  public int getChildCount() throws TelusAPIException {
    throw new RuntimeException("TODO");
  }
  */

  public void removeProperty(String name) throws TelusAPIException {
    configurationManager.removeProperty(this, name);
    delegate.removeProperty0(name);
  }

  public void removeProperties(String[] name) throws TelusAPIException {
    configurationManager.removeProperties(this, name);
    delegate.removeProperties0(name);
  }

  public void removeAllProperties() throws TelusAPIException {
    configurationManager.removeProperties(this, false);
    delegate.removeAllProperties0();
  }

  public void remove() throws TelusAPIException {
    configurationManager.removeConfiguration(this);
  }

  /*
  public void removeChildren() throws TelusAPIException {
    throw new RuntimeException("TODO");
  }
  */

}



