package com.telus.eas.utility.info;

public class ServiceEditionInfo extends BusinessConnectServiceInfo {

	static final long serialVersionUID = 1L;

	private String type;
	private String locale;
	private int rank;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("ServiceEditionInfo: [\n");
		s.append("    code=[").append(code).append("]\n");
		s.append("    type=[").append(type).append("]\n");
		s.append("    businessName=[").append(businessName).append("]\n");
		s.append("    businessNameFrench=[").append(businessNameFrench).append("]\n");
		s.append("    description=[").append(description).append("]\n");
		s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
		s.append("    familyType=[").append(familyType).append("]\n");
		s.append("    locale=[").append(locale).append("]\n");
		s.append("    rank=[").append(rank).append("]\n");
		s.append("]");

		return s.toString();
	}

}