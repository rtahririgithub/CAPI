package com.telus.cmb.tool.services.log.domain;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.tool.services.log.config.domain.AppMap;
import com.telus.cmb.tool.services.log.config.domain.LogServer;

public class LogServerInfo {

	private LogServer delegate;
	
	public LogServerInfo(LogServer logServer) {
		this.delegate = logServer;
	}

	public String getName() {
		return delegate.getName();
	}

	public String getShortname() {
		return delegate.getShortname();
	}

	public String getHost() {
		return delegate.getHost();
	}

	public boolean isProduction() {
		return delegate.isProduction();
	}

	public boolean usesWindowsLogin() {
		return StringUtils.equals("W", delegate.getPasswordType());
	}

	public boolean usesUnixLogin() {
		return StringUtils.equals("U", delegate.getPasswordType());
	}

	public boolean isLaird() {
		return StringUtils.startsWithIgnoreCase(delegate.getName(), "LAIRD");
	}

	public boolean isQidc() {
		return StringUtils.startsWithIgnoreCase(delegate.getName(), "QIDC");
	}

	public boolean isKidc() {
		return StringUtils.startsWithIgnoreCase(delegate.getName(), "KIDC");
	}

	public boolean isAppMapped(AppMap appMap) {
		return isDomainMapped(appMap.getDomain()) || isHostMapped(appMap.getHost());
	}
	
	public boolean isDomainMapped(String domain) {
		if (CollectionUtils.isNotEmpty(delegate.getMappedDomains())) {
			return delegate.getMappedDomains().contains(domain);
		}
		return false;
	}
	
	public boolean isHostMapped(String host) {
		if (CollectionUtils.isNotEmpty(delegate.getMappedHosts())) {
			return delegate.getMappedHosts().contains(host);
		}
		return false;		
	}
	
}
