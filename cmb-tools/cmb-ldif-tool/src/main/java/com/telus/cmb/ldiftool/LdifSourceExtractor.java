package com.telus.cmb.ldiftool;

import java.util.List;

public interface LdifSourceExtractor {

	public List<String> getLdifBuildFiles(String environment, boolean saveLdifIndicator);
	
}
