/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc.impl;

import java.sql.SQLException;

import net.sf.ehcache.CacheException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;

import com.telus.cmb.common.svc.SvcInvocationInterceptor;

/**
 * @author Michael Liao
 *
 */
public class ReferenceFacadeSvcInvocationInterceptor extends SvcInvocationInterceptor{

	@Override
	protected void logError (String methodFullName, Throwable t) {	
		if (t instanceof CacheException) {
			Throwable cause = t.getCause();
			if (cause instanceof UncategorizedSQLException) {
				SQLException sqlException = ((UncategorizedSQLException) cause).getSQLException();
				if (sqlException.getErrorCode() == 20201) { //do not log complete stack trace for price plan not found error
					return;
				}
			}
		}
		
		super.logError(methodFullName, t);
	}
	
	@Override
	protected Exception translateException(Throwable t) {
		if ( t instanceof CacheException) {
			return translateCacheException( (CacheException) t );
		}
		else {
			return super.translateException( t );
		}
	}
		
	private Exception translateCacheException( CacheException ex) {
		Throwable cacheErrorCause = ex.getCause();

		if ( cacheErrorCause !=null ) {
			if ( cacheErrorCause instanceof DataAccessException ) {
				return translateDataAccessException ( (DataAccessException) cacheErrorCause );
			}
			else {
				return translateThrowable( cacheErrorCause );
			}
		} else {
			return translateThrowable( ex );
		}
	}
	
}
