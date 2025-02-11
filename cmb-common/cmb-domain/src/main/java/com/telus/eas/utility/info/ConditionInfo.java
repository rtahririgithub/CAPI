package com.telus.eas.utility.info;

import java.util.Date;

public class ConditionInfo extends ReferenceInfo {
	
	private static final long serialVersionUID = 1L;
    
	private long id;
	private String name;
	private String value;
	private int conditionType;
	private double fromAmount;
	private double toAmount;
	private Date fromDate;
	private Date toDate;
	 
	public String getCode() {
		return String.valueOf(id);
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	 
	public String getName() {
		return name;
	}
	 
	public void setName(String name) {
		this.name = name;
	}
	
	public int getConditionType() {
		return conditionType;
	}
	
	public void setConditionType(int type) {
		this.conditionType = type;
	}
	 
	public String getValue() {
		return value;
	}

	public void setValue(String newValue) {
		this.value = newValue;
	}	
	
	public double getFromAmount() {
		return fromAmount;
	}
	
	public void setFromAmount(double amount) {
		this.fromAmount = amount;
	}
	
	public double getToAmount() {
		return toAmount;
	}
	
	public void setToAmount(double amount) {
		this.toAmount = amount;
	}
	 
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date date) {
		this.fromDate = date;
	}
	
	public Date getToDate() {
		return toDate;
	}
	 
	public void setToDate(Date date) {
		this.toDate = date;
	}
	    
    public String toString() {
    	
        StringBuffer s = new StringBuffer(128);
        
        s.append("ConditionInfo:[\n");
        s.append(super.toString()).append("\n");
        s.append("    id=[").append(id).append("]\n");
        s.append("    name=[").append(name).append("]\n");
        s.append("    conditionType=[").append(conditionType).append("]\n");
        s.append("    value=[").append(value).append("]\n");
        s.append("    fromAmount=[").append(fromAmount).append("]\n");
        s.append("    toAmount=[").append(toAmount).append("]\n"); 
        s.append("    fromDate=[").append(fromDate).append("]\n");
        s.append("    toDate=[").append(toDate).append("]\n"); 
        s.append("]");

        return s.toString();
    }
    
}
