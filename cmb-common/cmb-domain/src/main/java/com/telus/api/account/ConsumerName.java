/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

/**
 * <CODE>ConsumerName</CODE>
 *
 */
public interface ConsumerName {
	
	// Amdocs name format constants
	public static final String NAME_FORMAT_PRIVATE = "P";
	public static final String NAME_FORMAT_BUSINESS = "D";
	
	String getTitle();
	
	void setTitle(String title);
	
	String getFirstName();
	
	void setFirstName(String firstName);
	
	String getMiddleInitial();
	
	void setMiddleInitial(String middleInitial);
	
	String getLastName();
	
	void setLastName(String lastName);
	
	String getGeneration();
	
	void setGeneration(String generation);
	
	String getAdditionalLine();
	
	void setAdditionalLine(String additionalLine);
	
	void setNameFormat(String nameFormat);
	
	String getNameFormat();
	
}


