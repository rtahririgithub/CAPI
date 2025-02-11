package com.telus.cmb.tool.services.log.ldif.exceptions;

import com.telus.cmb.tool.services.log.domain.LdifSearchResult;

public class LdifException extends Exception {

	private static final long serialVersionUID = 1L;
	private LdifSearchResult result;

	public LdifSearchResult getResult() {
		return result;
	}

	public void setResult(LdifSearchResult result) {
		this.result = result;
	}
}
