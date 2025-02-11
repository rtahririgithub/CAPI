package com.telus.cmb.tool.services.log.domain.swagger;

public enum SwaggerFileEnum {

	APMRS_V1("AccountProfileMgmtRESTSvc 1.0", "AccountProfileMgmtRESTSvc_v1_0.yaml"),
	WPMRS_V1("WLSPaymentMgmtRESTSvc 1.0", "WLSPaymentMgmtRESTSvc_v1_0.yaml"),
	WPPMRS_V1("WLSPricePlanMgmtRESTSvc 1.0", "WLSPricePlanMgmtRESTSvc_v1_0.yaml"),
	WPIMRS_V1("WLSProductInventoryMgmtAPI 1.0", "WLSProductInventoryMgmtRESTSvc_v1_0.yaml"),
	WSAMRS_V1("WLSServiceAgreementMgmtRESTSvc 1.0", "WLSServiceAgreementMgmtRESTSvc_v1_0.yaml"),
	WSAFMRS_V1("WLSServiceAndFeatureMgmtRESTSvc 1.0", "WLSServiceAndFeatureMgmtRESTSvc_v1_0.yaml"),
	WSLMRS_V1("WLSSubscriberLifecycleMgmtRESTSvc 1.0", "WLSSubscriberLifecycleMgmtRESTSvc_v1_0.yaml"),
	WVMRS_V1("WLSVoicemailMgmtRESTSvc 1.0", "WLSVoicemailMgmtRESTSvc_v1_0.yaml");

	private String servicename;
	private String filename;

	private SwaggerFileEnum(String name, String filename) {
		this.servicename = name;
		this.filename = filename;
	}

	public String getName() {
		return name();
	}
	
	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
