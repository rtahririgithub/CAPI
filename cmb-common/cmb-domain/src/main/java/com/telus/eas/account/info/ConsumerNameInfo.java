/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;

public class ConsumerNameInfo extends Info implements ConsumerName {

	 static final long serialVersionUID = 1L;

	private String title;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String generation;
	private String additionalLine;
	private String nameFormat = ConsumerName.NAME_FORMAT_PRIVATE;
	
	public ConsumerNameInfo() {
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = toUpperCase(title);
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = toUpperCase(firstName);
	}
	
	public String getMiddleInitial() {
		return middleInitial;
	}
	
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = toUpperCase(middleInitial);
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = toUpperCase(lastName);
	}
	
	public String getGeneration(){
		return generation;
	}
	
	public void setGeneration(String generation){
		this.generation = generation;
	}
	
	public String getAdditionalLine(){
		return additionalLine;
	}
	
	public void setAdditionalLine(String additionalLine){
		this.additionalLine = additionalLine;
	}
	
	public String getFullName() {
		return firstName +
		Info.nullToString(" ", middleInitial) +
		Info.nullToString(" ", lastName);
	}
	
	public String getNameFormat() {
		return nameFormat;
	}
	
	public void setNameFormat(String nameFormat) {
		this.nameFormat = nameFormat;
	}
	
	public void copyFrom(ConsumerNameInfo o) {
		setTitle(o.title);
		setFirstName(o.firstName);
		setMiddleInitial(o.middleInitial);
		setLastName(o.lastName);
		setGeneration(o.generation);
		setAdditionalLine(o.additionalLine);
		setNameFormat(o.nameFormat);
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		s.append("ConsumerNameInfo:{\n");
		s.append("    title=[").append(title).append("]\n");
		s.append("    firstName=[").append(firstName).append("]\n");
		s.append("    middleInitial=[").append(middleInitial).append("]\n");
		s.append("    lastName=[").append(lastName).append("]\n");
		s.append("    generation=[").append(generation).append("]\n");
		s.append("    additionalLine=[").append(additionalLine).append("]\n");
		s.append("    nameFormat=[").append(nameFormat).append("]\n");
		s.append("}");
		
		return s.toString();
	}
	
}




