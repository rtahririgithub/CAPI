package com.telus.cmb.tool.services.log.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import com.telus.cmb.tool.services.log.domain.DateRangeFilter;
import com.telus.cmb.tool.services.log.domain.LineInfo;
import com.telus.cmb.tool.services.log.domain.ListFileResult;
import com.telus.cmb.tool.services.log.domain.LogSearchResult;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;

public interface LogFileService {

	public List<ListFileResult> getFilenames(List<String> filePaths, LogServerInfo logServer, String username, String password, String extension) throws Exception;

	public List<LogSearchResult> getSearchResults(List<String> filePaths, LogServerInfo logServer, String username, String password, Set<String> searchCriteria, DateRangeFilter dateRangeFilter,
			int resultLimit) throws Exception;

	public List<LogSearchResult> getSearchResultsForTask(List<String> filePaths, LogServerInfo logServer, String username, String password, Set<String> searchCriteria, DateRangeFilter dateRangeFilter,
			int resultLimit, Set<String> searchExcludeList) throws Exception;

	public boolean isCredentialValid(String host, String username, String password);

	public boolean doesFolderExist(LogServerInfo logServer, String username, String password, String filepath) throws Exception;

	public List<LineInfo> readLogFile(LogServerInfo logServer, String username, String password, String file, int lineNumber, int linesBefore, int linesAfter) throws Exception;
	
	public List<String> readLogFile(LogServerInfo logServer, String username, String password, String file) throws Exception;

	public void downloadFile(LogServerInfo logServer, String username, String password, String file, OutputStream outputStream) throws Exception;
}
