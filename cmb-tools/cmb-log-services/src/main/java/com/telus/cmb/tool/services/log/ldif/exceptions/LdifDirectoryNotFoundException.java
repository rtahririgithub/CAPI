package com.telus.cmb.tool.services.log.ldif.exceptions;

import com.telus.cmb.tool.services.log.domain.LdifSearchResult;

public class LdifDirectoryNotFoundException extends LdifException {

	private static final long serialVersionUID = 1L;

	public LdifDirectoryNotFoundException(LdifSearchResult result) {
		this.setResult(result);
	}
	
}
