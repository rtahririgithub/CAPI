package com.telus.eas.utility.info;

public class LicenseInfo extends BusinessConnectServiceInfo {

	static final long serialVersionUID = 1L;

	private String sku;	

	public String getSKU() {
		return sku;
	}

	public void setSKU(String sku) {
		this.sku = sku;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("LicenseInfo: [\n");
		s.append("    code=[").append(code).append("]\n");
		s.append("    sku=[").append(sku).append("]\n");
		s.append("    businessName=[").append(businessName).append("]\n");
		s.append("    businessNameFrench=[").append(businessNameFrench).append("]\n");
		s.append("    description=[").append(description).append("]\n");
		s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
		s.append("    familyType=[").append(familyType).append("]\n");
		s.append("]");

		return s.toString();
	}

}