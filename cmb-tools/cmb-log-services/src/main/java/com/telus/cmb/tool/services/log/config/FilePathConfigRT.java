package com.telus.cmb.tool.services.log.config;

import java.util.List;
import java.util.Set;

import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;

public interface FilePathConfigRT {

	public List<LogFilePaths> getFilePaths(String envShortName, String appShortName, String componentName);

	public Set<LogServerInfo> getLogServers(String envShortName, String appShortName);

}
