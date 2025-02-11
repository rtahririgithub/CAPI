package com.telus.cmb.unit.test.subscriber.svc.facade;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public interface ServiceEditionConstants {

	// Provisioning service edition constants and enumerator
	public static final String SERVICE_EDITION_STANDARD = "STANDARD";
	public static final String SERVICE_EDITION_PREMIUM = "PREMIUM";
	public static final String SERVICE_EDITION_ENTERPRISE = "ENTERPRISE";
	
	public enum ServiceEdition {
	
		NATIONAL_STANDARD ("BCPSTD", SERVICE_EDITION_STANDARD, "National Standard Edition", false),
		NATIONAL_PREMIUM ("BCPPRM", SERVICE_EDITION_PREMIUM, "National Premium Edition", false),
		NATIONAL_ENTERPRISE ("BCPEXC", SERVICE_EDITION_ENTERPRISE, "National Enterprise Edition", false),
		QUEBEC_STANDARD ("BCPSQC", SERVICE_EDITION_STANDARD, "Quebec Standard Edition", true),		
		QUEBEC_PREMIUM ("BCPPQC", SERVICE_EDITION_PREMIUM, "Quebec Premium Edition", true),		
		QUEBEC_ENTERPRISE ("BCPEQC", SERVICE_EDITION_ENTERPRISE, "Quebec Enterprise Edition", true);
		
		private String code;
		private String edition;
		private String description;
		private boolean quebec;
		
		private static List<String> standardList = new ArrayList<String>();
		private static List<String> premiumList = new ArrayList<String>();
		private static List<String> enterpriseList = new ArrayList<String>();
		private static List<String> quebecList = new ArrayList<String>();

		private ServiceEdition(String code, String edition, String description, boolean quebec) {
			this.code = code;
			this.edition = edition;
			this.description = description;
			this.quebec = quebec;
		}
		
		static {
			for (ServiceEdition edition : ServiceEdition.values()) {
				if (StringUtils.equals(edition.getEdition(), SERVICE_EDITION_STANDARD)) {
					standardList.add(edition.getCode());
				}
				if (StringUtils.equals(edition.getEdition(), SERVICE_EDITION_PREMIUM)) {
					premiumList.add(edition.getCode());
				}
				if (StringUtils.equals(edition.getEdition(), SERVICE_EDITION_ENTERPRISE)) {
					enterpriseList.add(edition.getCode());
				}
				if (edition.isQuebec()) {
					quebecList.add(edition.getCode());
				}
			}
		}

		public String getCode() {
			return code;
		}
		
		public String getEdition() {
			return edition;
		}

		public String getDescription() {
			return description;
		}
		
		public boolean isQuebec() {
			return quebec;
		}
		
		public static boolean isStandard(String code) {
			return standardList.contains(code);
		}
		
		public static boolean isPremium(String code) {
			return premiumList.contains(code);
		}
		
		public static boolean isEnterprise(String code) {
			return enterpriseList.contains(code);
		}
		
		public static boolean isQuebec(String code) {
			return quebecList.contains(code);
		}
		
	}
	
}