package com.telus.eas.utility.info;

import java.util.Arrays;

public class ServiceExtendedInfo extends ReferenceInfo {

	private static final long serialVersionUID = 1L;

	AccountTypeInfo[] accountTypes;
	String[] provinceCodes;
	String[] socGroups;
	
	public AccountTypeInfo[] getAccountTypes() {
		return accountTypes;
	}


	public void setAccountTypes(AccountTypeInfo[] accountTypes) {
		this.accountTypes = accountTypes;
	}


	public String[] getProvinceCodes() {
		return provinceCodes;
	}


	public void setProvinceCodes(String[] provinceCodes) {
		this.provinceCodes = provinceCodes;
	}


	public String[] getSocGroups() {
		return socGroups;
	}


	public void setSocGroups(String[] planGroups) {
		this.socGroups = planGroups;
	}


	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("ServiceExtendedInfo [")		
		.append("provinceCodes=")
		.append(provinceCodes != null ? 
				Arrays.asList(provinceCodes) : null)
		.append(", socGroups=")
		.append(socGroups != null ? 
				Arrays.asList(socGroups) : null)		
		.append(", accountTypeInfo=");
		buffer.append("{");
		for (int i = 0; i < accountTypes.length; i++) {
			AccountTypeInfo accountType = accountTypes[i];
			buffer.append("accountType=[")
				.append(accountType.getAccountType())
				.append("], accountSubType=[")
				.append(accountType.getAccountSubType())
				.append("], ");
		}
		buffer.append("}");		
		buffer.append("]");
		return buffer.toString();
	}

}