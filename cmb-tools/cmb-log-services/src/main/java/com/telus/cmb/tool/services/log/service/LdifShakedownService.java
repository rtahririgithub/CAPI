package com.telus.cmb.tool.services.log.service;

import java.util.List;

import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.domain.LdifSearchResult;
import com.telus.cmb.tool.services.log.domain.RAPiDRelease;

public interface LdifShakedownService {

	public List<LdifSearchResult> shakedown(Environment environment, int releaseId);
	
	public List<LdifSearchResult> shakedown(Environment environment, List<String> ldifPaths);
	
	public List<RAPiDRelease> getReleases();
}
