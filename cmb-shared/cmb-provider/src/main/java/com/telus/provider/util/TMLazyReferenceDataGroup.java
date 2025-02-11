/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.util;

import com.telus.api.*;
import com.telus.api.reference.*;
import com.telus.provider.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;


public class TMLazyReferenceDataGroup extends TMReflexiveReferenceDataGroup {

	public TMLazyReferenceDataGroup(Class referenceClass, Object target, String methodName) throws Throwable {
		super(referenceClass, target, target.getClass().getMethod(methodName, ONE_STRING_CLASS));
	}

	public synchronized void reload() {
		cache.clear();
	}

	public Reference[] getAll() throws TelusAPIException {
		throw new UnsupportedOperationException("method call is inappropriate, not all data will be available");
	}

	public Reference get(String code) throws TelusAPIException {
		Reference r = super.get(code);

		//-----------------------------------------------------------
		// we shouldn't go to the DB if the code exists in the cache,
		// even if the value is null.
		//-----------------------------------------------------------
		if(r == null && !super.contains(code)) {
			r = get0(code);
		}

		return r;
	}

	private Reference get0(String code) throws TelusAPIException {
		try {
			//-----------------------------------------------------
			// Call the retrieval method on the target object.
			//-----------------------------------------------------
			Object result = method.invoke(target, new Object[]{code});
			Class  resultClass = result.getClass();

			if(!REFERENCE_CLASS.isAssignableFrom(resultClass)) {
				throw new TelusAPIException(target.getClass().getName() + "." + method + ": expected a sublass of " + REFERENCE_CLASS + ", but found " + resultClass);
			}

			addData(code, (Reference)result, true);

			exception = null;

			return (Reference)result;
		}catch (Throwable e) {
			Logger.debug0(e);
			exception = e;
			return null;
		} finally {

		}
	}


}



