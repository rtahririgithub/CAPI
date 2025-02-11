package com.telus.cmb.tool.services.log.ldif.exceptions;

import com.telus.cmb.tool.services.log.domain.LdifSearchResult;

public class MultipleLdifMatchException extends LdifException {

	private static final long serialVersionUID = 1L;

	public MultipleLdifMatchException(LdifSearchResult result) {
		this.setResult(result);
	}
	
}
