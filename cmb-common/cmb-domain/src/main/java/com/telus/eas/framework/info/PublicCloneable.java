/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.framework.info;

import java.util.ArrayList;
import java.util.Date;

/**
 * <code></code>
 *
 *
 * @author  Dele Taylor
 * @version 1.0
 */
public interface PublicCloneable extends Cloneable {

  static final long serialVersionUID = 1L;

  public Object clone();


  public final static class Helper {

    public static ArrayList clone(ArrayList o) {
      if(o != null) {
        /*
        Class clazz = o.getClass();
        List copy = (List)clazz.newInstance();
        */

        ArrayList copy = new ArrayList(o.size());

        for(int i=0; i<o.size(); i++) {
          copy.add(clone(o.get(i)));
        }

        return copy;
      } else {
        return null;
      }
    }


    public static Object clone(Object[] o) {
      if(o != null) {
        // TODO: use deep copy for PublicCloneable
        // (and other well-known Cloneable) elements.
        return o.clone();
      } else {
        return null;
      }
    }

    public static Object clone(PublicCloneable[] o) {
      if(o != null) {
        Object[] copy = (Object[])java.lang.reflect.Array.newInstance(o.getClass().getComponentType(), o.length);

        for(int i=0; i<o.length; i++) {
          copy[i] = clone(o[i]);
        }

        return copy;
      } else {
        return null;
      }
    }

    public static Date clone(Date o) {
      if(o != null) {
        return (Date)o.clone();
      } else {
        return null;
      }
    }

    public static Object clone(Object o) {

      if(!(o instanceof PublicCloneable)) {
        //throw new CloneNotSupportedException(o + " does not implement com.telus.eas.utility.info.PublicCloneable");
        throw new InternalError(o + " does not implement com.telus.eas.framework.info.PublicCloneable");
      }

      return clone((PublicCloneable)o);
    }

    public static Object clone(PublicCloneable o) {
      if(o != null) {
        return o.clone();
      } else {
        return null;
      }
    }

	/**
	 * This method clone a HashMap object. Unlike the HashMap.clone() method's shallow clone, this method clone all it's value elements.
	 * This method does not clone the key elements. The value element has to implement PublicCloneable interface.
	 * 
	 * @param o the HashMap to be cloned
	 * @return cloned HashMap with all the value elements being cloned.
	 */
	public static java.util.HashMap clone(java.util.HashMap o) {
		java.util.HashMap copy = null; 
		if ( o!=null ) {
			copy =  new java.util.HashMap( o );
			java.util.Iterator keyIt = copy.keySet().iterator();
			while ( keyIt.hasNext() ) {
				Object key = keyIt.next(); 
				Object value = copy.get(key);
				copy.put(key, clone ( value ) );
			}
		}
		return copy;
	}
  }

}



