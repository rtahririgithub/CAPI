package com.telus.cmb.tool.services.log.domain.usage;

import java.util.List;

public class UsageResult implements Comparable<UsageResult> {

	private String operationName;
	private List<MonthlyUsage> monthlyUsageList;

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public List<MonthlyUsage> getMonthlyUsageList() {
		return monthlyUsageList;
	}

	public void setMonthlyUsageList(List<MonthlyUsage> monthlyUsageList) {
		this.monthlyUsageList = monthlyUsageList;
	}
	
	public String getOperationShortname() {
		if (this.operationName != null) {
			int endIdx = this.operationName.indexOf("(");
			int opStart = this.operationName.lastIndexOf(".", endIdx); 
			int clStart = this.operationName.lastIndexOf(".", opStart - 1) + 1;
			return this.operationName.substring(clStart, endIdx);
		}
		return "";
	}
	
	public String getOperationPackage() {
		if (this.operationName != null) {
			int firstSpIdx = this.operationName.indexOf(" ") + 1;
			int startIdx = this.operationName.indexOf(" ", firstSpIdx) + 1;			 
			int endIdx = this.operationName.indexOf(getOperationShortname()) - 1;
			return this.operationName.substring(startIdx, endIdx);
		}
		return "";
	}

	public String getOperationParameters() {
		if (this.operationName != null) {
			int startIdx = this.operationName.indexOf("(") + 1;
			int endIdx = this.operationName.indexOf(")", startIdx); 
			return simplifyClassNames(this.operationName.substring(startIdx, endIdx));
		}
		return "";
	}
	
	private String simplifyClassNames(String parameters) {
		
		String simple = "";
		String[] params = parameters.split(",");
		for (int i = 0; i < params.length; i++) {
			if (i > 0) {
				simple += ", ";
			}
			String trimmed = params[i].trim();
			if (trimmed.contains(".")) {
				simple += trimmed.substring(trimmed.lastIndexOf(".") + 1);
			} else {
				simple += trimmed;
			}
		}
		
		return simple;
	}

	@Override
	public int compareTo(UsageResult o) {
		return getOperationShortname().compareTo(o.getOperationShortname());
	}
	
}
