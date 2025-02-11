package com.telus.cmsc.domain.artifact;

import com.telus.cmsc.domain.BaseEntity;


/**
 * @author Pavel Simonovsky	
 *
 */
public class Environment extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	private Integer environmentId;

	private String code;
	
	private String configCode;
	
	private String name;
	
	private String description;
	
	private String ldapUrl;
	
	private boolean flipperMember;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLdapUrl() {
		return ldapUrl;
	}
	
	public void setLdapUrl(String ldapUrl) {
		this.ldapUrl = ldapUrl;
	}

	public Integer getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public Integer getId() {
		return getEnvironmentId();
	}

	public boolean isFlipperMember() {
		return flipperMember;
	}

	public void setFlipperMember(boolean flipperMember) {
		this.flipperMember = flipperMember;
	}

	public String getConfigCode() {
		return configCode;
	}

	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}
	
}
