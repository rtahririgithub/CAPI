package com.telus.eas.utility.info;

public class ResultInfo extends ReferenceInfo {
	
    private static final long serialVersionUID = 1L;
    
    String value;
    String name;
    String description;
    long resultType;
	 
    public String getValue() {
    	return this.value;
    }	
	 
    public void setValue(String val) {
    	this.value = val;
    }
	 	 
    public String getName() {
    	return this.name;
    }
	
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getDescription() {
    	return this.description;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
	 
    public long getResultType() {
    	return this.resultType;
    }
    
    public void setResultType(long type) {
    	this.resultType = type;
    }
    
    public String toString() {
    	
        StringBuffer s = new StringBuffer(128);

        s.append("ResultInfo:[\n");
        s.append(super.toString()).append("\n");
        s.append("    value=[").append(value).append("]\n");
        s.append("    name=[").append(name).append("]\n");
        s.append("    description=[").append(description).append("]\n");
        s.append("    resultType=[").append(resultType).append("]\n"); 
        s.append("]");

        return s.toString();
    }
    
}
