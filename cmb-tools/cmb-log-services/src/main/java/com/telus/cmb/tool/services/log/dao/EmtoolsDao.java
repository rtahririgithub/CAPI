package com.telus.cmb.tool.services.log.dao;

import java.util.List;

import com.telus.cmb.tool.services.log.config.domain.AppMap;
import com.telus.cmb.tool.services.log.domain.RAPiDRelease;

public interface EmtoolsDao {

	public List<AppMap> getAppMap(String appMapName);
	
	public List<RAPiDRelease> getActiveReleases();
	
	public List<String> getLdifpaths(int releaseId);
}
