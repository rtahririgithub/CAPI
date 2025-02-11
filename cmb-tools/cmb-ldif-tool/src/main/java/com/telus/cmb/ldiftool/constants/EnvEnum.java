package com.telus.cmb.ldiftool.constants;

public enum EnvEnum {

	DV103("dv103", null, "ldapread-d3.tmi.telus.com", false, null, null), 
	PT140("pt140", "it03", "ldaps://ldapread-qa2.tmi.telus.com:636", false, "armxnonpr", "uid=armxnonpr,ou=administrators,o=telusconfiguration"), 
	PT148("pt148", "it02", "ldaps://ldapread-qa.tmi.telus.com:636", false, "armxnonpr", "uid=armxnonpr,ou=administrators,o=telusconfiguration"), 
	PT168("pt168", "it01", "ldaps://ldapread-pt168.tmi.telus.com:636", false, "armxnonpr", "uid=armxnonpr,ou=administrators,o=telusconfiguration"), 
	ST101A("st101a", "it04", "ldaps://ldapread-st101a.tmi.telus.com:636/", true, "armxnonpr", "uid=armxnonpr,ou=administrators,o=telusconfiguration_a"),
	ST101B("st101b", "it04", "ldaps://ldapread-st101b.tmi.telus.com:636/", true, "armxnonpr", "uid=armxnonpr,ou=administrators,o=telusconfiguration_b"),
	//ST101A("st101a", "it04", "ldaps://ldapread-st101a.tmi.telus.com:636/", true, "TelusAdminST101A", "uid=telusAdmin,ou=administrators,o=telusconfiguration_a"), 
	//ST101B("st101b", "it04", "ldaps://ldapread-st101b.tmi.telus.com:636/", true, "TelusAdminST101B", "uid=telusAdmin,ou=administrators,o=telusconfiguration_b"), 
	PRA("pra", null, "ldaps://ldapread-pra.tmi.telus.com:636", true, "armxpr", "uid=armxpr,ou=administrators,o=telusconfiguration_a"), 
	PRB("prb", null, "ldaps://ldapread-prb.tmi.telus.com:636", true, "armxpr", "uid=armxpr,ou=administrators,o=telusconfiguration_b");
	
	private String fullName;
	private String aliasName;
	private String ldapUrl;
	private String credential;
	private String principle;
	private boolean flipperEnabled;
	
	EnvEnum(String name, String aliasName, String ldapUrl, boolean flipperEnabled, String credential, String principle) {
		this.fullName = name;
		this.aliasName = aliasName;
		this.ldapUrl = ldapUrl;
		this.flipperEnabled = flipperEnabled;
		this.credential = credential;
		this.principle = principle;
	}
	
	public String getFullName() {
		return fullName;
	}

	public String getLdapUrl() {
		return ldapUrl;
	}
	
	public boolean isFlipperEnabled() {
		return flipperEnabled;
	}
	
	public String getCredential()
    {
        return credential;
    }

    public String getPrinciple()
    {
        return principle;
    }

    public String getAliasName()
    {
        return aliasName;
    }

    public String getName() {
		return flipperEnabled ? fullName.substring(0, fullName.length() - 1) : fullName;
	}
	
	public String getFlipperSide() {
		return flipperEnabled ? fullName.substring(fullName.length() - 1) : "";
	}
	
}
