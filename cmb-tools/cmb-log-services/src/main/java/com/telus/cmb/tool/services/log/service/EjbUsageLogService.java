package com.telus.cmb.tool.services.log.service;

import java.io.File;

public interface EjbUsageLogService {

	public File updateEjbUsageLogFiles(String key, String user, String password);
	
	public void archiveOldFolders(int numOfMonths);
}
