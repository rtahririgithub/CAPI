/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.config0;

import com.telus.api.*;
import java.util.*;

public interface Configuration
{

    int getId();
    String getName();
    String[] getPath();

    String[] getPropertyNames();
    String[] getDefinedPropertyNames();
    int getPropertyCount();
    Map getProperties();
    Map getProperties(Map map);

    boolean contains(String key);
    boolean containsDefined(String key);


    boolean isNull(String name) throws UnknownObjectException;
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

    /*
    void setProperty(String name, String value) throws TelusAPIException;
    void setProperty(String name, long value) throws TelusAPIException;
    void setProperty(String name, double value) throws TelusAPIException;
    void setProperty(String name, boolean value) throws TelusAPIException;
    void setProperty(String name, Date value) throws TelusAPIException;

    void removeProperty(String name) throws TelusAPIException;
    void removeProperties(String[] name) throws TelusAPIException;
    void removeAllProperties() throws TelusAPIException;
    */

    Configuration getParent() throws TelusAPIException;


    void refreshProperties() throws TelusAPIException;

    Configuration[] getChildren() throws TelusAPIException;
    Configuration getChild(String name) throws TelusAPIException, UnknownObjectException;

    /*
    void remove() throws TelusAPIException;

    Configuration newConfiguration(String name) throws TelusAPIException, DuplicateObjectException;
    */

    /*
    //void saveProperties() throws TelusAPIException;
    //int getChildCount() throws TelusAPIException;

    void removeChildren() throws TelusAPIException;
    */


}


