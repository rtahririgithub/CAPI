/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.eas.activitylog.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.telus.api.util.ToStringBuilder;


/**
 * @author Pavel Simonovsky
 *
 */
public class ActivityLoggingResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List recordLoggingResults = new ArrayList();
	private String applicationName = "N/A"; //for debugging purpose only.
	
	public class ActivityRecordLoggingResult implements Serializable {
		
		private static final long serialVersionUID = 1L;

		private String name = null;
		
		private String description = null;
		
		private ActivityLogRecordIdentifier identifier = null;
		
		private Throwable error = null;
		
		private ActivityRecordLoggingResult(String name, String description, ActivityLogRecordIdentifier identifier) {
			this(name, description);
			this.identifier = identifier;
		}
		
		private ActivityRecordLoggingResult(String name, String description, Throwable error) {
			this(name, description);
			this.error = error;
		}
		
		private ActivityRecordLoggingResult(String name, String description) {
			this.name = name;
			this.description = description;
		}

		/**
		 * @return the identifier
		 */
		public ActivityLogRecordIdentifier getIdentifier() {
			return identifier;
		}

		/**
		 * @return the error
		 */
		public Throwable getError() {
			return error;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		
		public String toString() {
			return ToStringBuilder.toString(this);
		}
		
	}
	
	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public void addActivityRecordLoggingResult(String name, String description, ActivityLogRecordIdentifier identifier) {
		recordLoggingResults.add( new ActivityRecordLoggingResult(name, description, identifier));
	}

	public void addActivityRecordLoggingResult(String name, String description, Throwable error) {
		recordLoggingResults.add( new ActivityRecordLoggingResult(name, description, error));
	}
	
	public Collection getActivityRecordLoggingResults() {
		return recordLoggingResults;
	}
	
	public String toString() {
		return ToStringBuilder.toString(this);
	}
	
}
