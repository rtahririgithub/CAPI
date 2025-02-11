/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.util;

import java.util.Collection;

import com.telus.api.TelusAPIException;
import com.telus.api.reference.Reference;


/**
 * <code>TMFutureReferenceData</code> represents ReferenceData that might
 * not yet be loaded.
 *
 */
public class TMFutureReferenceDataGroup extends TMReflexiveReferenceDataGroup {


	public TMFutureReferenceDataGroup(Class referenceClass, Object target, String methodName) throws Throwable {
		super(referenceClass, target, target.getClass().getMethod(methodName, ZERO_CLASSES));
	}

	public synchronized void reload() {
		loaded.setFalse();

		try {
			//-----------------------------------------------------
			// Call the retrieval method on the target object.
			//-----------------------------------------------------
			Object result = method.invoke(target, ZERO_OBJECTS);
			if(COLLECTION_CLASS.isAssignableFrom(result.getClass())) {
				setData((Collection)result);
			} else {
				setData((Reference[])result);
			}

			loaded.setTrue();
			exception = null;
		}catch (Throwable e) {
			Logger.debug0(e);
			exception = e;
		} finally {

		}
	}

	public Reference get(String code) throws TelusAPIException {
		waitForLoading();
		return super.get(code);
	}

	public Reference[] getAll() throws TelusAPIException {
		waitForLoading();
		return super.getAll();
	}

}




