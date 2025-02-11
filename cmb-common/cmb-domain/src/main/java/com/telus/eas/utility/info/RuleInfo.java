package com.telus.eas.utility.info;

public class RuleInfo extends ReferenceInfo {

	private static final long serialVersionUID = 1L;

	long id;
	String name;
	int type;
	int role;
	int category;
	ConditionInfo[] conditions;
	ResultInfo result;

	public String getCode() {
		return String.valueOf(id);
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}	

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRole() {
		return this.role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getCategory() {
		return this.category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public ConditionInfo[] getConditions() {
		return this.conditions;
	}

	public void setConditions(ConditionInfo[] conditions) {
		this.conditions = conditions;
	}

	public ResultInfo getResult() {
		return this.result;
	}

	public void setResultInfo(ResultInfo result) {
		this.result = result;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("RuleInfo:[\n");
		s.append(super.toString()).append("\n");
		s.append("    id=[").append(id).append("]\n");
		s.append("    name=[").append(name).append("]\n");
		s.append("    type=[").append(type).append("]\n");
		s.append("    role=[").append(role).append("]\n");
		s.append("    category=[").append(category).append("]\n");
		s.append(result.toString());
		for (int i = 0; i < conditions.length; i++) {
			s.append(conditions[i].toString());
		}
		s.append("]");

		return s.toString();
	}

	public boolean equals(Object obj) {
		if (obj != null && obj.getClass().equals(this.getClass())) {
			RuleInfo info = (RuleInfo) obj;

			return this.id == info.id;
		}
		return false;
	}

	public int hashCode() {
		return new Long(this.id).intValue();
	}
}