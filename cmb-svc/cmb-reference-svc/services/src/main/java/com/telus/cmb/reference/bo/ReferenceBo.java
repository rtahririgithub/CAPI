/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.bo;

import java.io.Serializable;

import com.telus.api.reference.Reference;

/**
 * @author Pavel Simonovsky
 *
 */
public abstract class ReferenceBo<T extends Reference> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private T delegate;
	
	/**
	 * 
	 */
	public ReferenceBo(T delegate) {
		this.delegate = delegate;
	}
	
	public T getDelegate() {
		return delegate;
	}
}
