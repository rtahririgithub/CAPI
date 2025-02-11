package com.telus.eas.portability.info;

import com.telus.api.portability.PortRequestName;
import com.telus.eas.framework.info.Info;

public class PortRequestNameInfo extends Info implements PortRequestName{
	private String title;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String generation;
	
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public String getFirstName(){
		return firstName;
	}
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	public String getMiddleInitial(){
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial){
		this.middleInitial = middleInitial;
	}
	public String getLastName(){
		return lastName;
	}
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	public String getGeneration(){
		return generation;
	}
	public void setGeneration(String generation){
		this.generation = generation;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("PortRequestNameInfo:[\n");

		s.append("    title=[").append(title).append("]\n");
		s.append("    firstName=[").append(firstName).append("]\n");
		s.append("    middleInitial=[").append(middleInitial).append("]\n");
		s.append("    lastName=[").append(lastName).append("]\n");
		s.append("    generation=[").append(generation).append("]\n");
		s.append("]");

		return s.toString();
	}		
}
