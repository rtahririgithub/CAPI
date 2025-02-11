package com.telus.cmb.tool.services.log.service;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telus.cmb.tool.services.log.connector.JSchConnector;
import com.telus.cmb.tool.services.log.domain.DateRangeFilter;
import com.telus.cmb.tool.services.log.domain.LineInfo;
import com.telus.cmb.tool.services.log.domain.ListFileResult;
import com.telus.cmb.tool.services.log.domain.LogSearchResult;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;
import com.telus.cmb.tool.services.log.utils.ConnectorUtil;

@Service
public class LogFileServiceImpl implements LogFileService {

	private Logger logger = Logger.getLogger(LogFileServiceImpl.class);
	private static final String EXISTS = "exists";

	@Autowired
	JSchConnector jSchConnector;

	@Override
	public List<ListFileResult> getFilenames(List<String> filePaths, LogServerInfo logServer, String username, String password, String extension) throws Exception {

		Map<String, ListFileResult> listResultMap = new LinkedHashMap<String, ListFileResult>();
		String[] results = jSchConnector.executeCommand(logServer.getHost(), username, password, ConnectorUtil.createListCommand(filePaths, extension));
		for (String result : results) {
			if (StringUtils.isNotEmpty(result)) {
				String filepath = result.split("\\s+")[8];
				String folder = filepath.substring(0, filepath.lastIndexOf("/"));
				ListFileResult fileResult = listResultMap.get(folder);
				if (fileResult == null) {
					fileResult = new ListFileResult();
					fileResult.setLogServer(logServer);
					fileResult.setFolder(folder);
				}
				fileResult.addListResult(result);
				listResultMap.put(folder, fileResult);
			}
		}
		return new ArrayList<ListFileResult>(listResultMap.values());
	}

	@Override
	public List<LogSearchResult> getSearchResults(List<String> filePaths, LogServerInfo logServer, String username, String password, Set<String> searchCriteria, DateRangeFilter dateRangeFilter,
			int resultLimit) throws Exception {

		Map<String, Long> startLineFilterMap = null;
		Map<String, Long> endLineFilterMap = null;
		List<String> logFilePaths = filePaths;
		if (dateRangeFilter != null) {
			String startDateRegex = new SimpleDateFormat(dateRangeFilter.getDateFormat()).format(dateRangeFilter.getStartDate());
			String endDateRegex = new SimpleDateFormat(dateRangeFilter.getDateFormat()).format(dateRangeFilter.getTrueEndDate());
			startLineFilterMap = getFileLineNumbersByDate(filePaths, logServer, username, password, startDateRegex);
			endLineFilterMap = getFileLineNumbersByDate(filePaths, logServer, username, password, endDateRegex);
		}

		return searchUsingCriteria(logFilePaths, logServer, username, password, searchCriteria, resultLimit, false, startLineFilterMap, endLineFilterMap, dateRangeFilter);
	}

	@Override
	public List<LogSearchResult> getSearchResultsForTask(List<String> filePaths, LogServerInfo logServer, String username, String password, Set<String> searchCriteria, DateRangeFilter dateRangeFilter,
			int resultLimit, Set<String> searchExcludeList) throws Exception {
		return search(filePaths, logServer, username, password, searchCriteria, dateRangeFilter, resultLimit, searchExcludeList, true);
	}
	
	private List<LogSearchResult> search(List<String> filePaths, LogServerInfo logServer, String username, String password, Set<String> searchCriteria, DateRangeFilter dateRangeFilter,
			int resultLimit, Set<String> searchExcludeList, boolean isTask) throws Exception {

		Map<String, Long> startLineFilterMap = null;
		Map<String, Long> endLineFilterMap = null;
		if (dateRangeFilter != null) {
			String startDateRegex = new SimpleDateFormat(dateRangeFilter.getDateFormat()).format(dateRangeFilter.getStartDate());
			String endDateRegex = new SimpleDateFormat(dateRangeFilter.getDateFormat()).format(dateRangeFilter.getTrueEndDate());
			startLineFilterMap = getFileLineNumbersByDate(filePaths, logServer, username, password, startDateRegex);
			endLineFilterMap = getFileLineNumbersByDate(filePaths, logServer, username, password, endDateRegex);
		}
		String searchCommand = ConnectorUtil.createCommandToSearchCriteria(filePaths, searchCriteria, dateRangeFilter, resultLimit, isTask);
		String[] results = jSchConnector.executeCommand(logServer.getHost(), username, password, searchCommand);
		
		return parseResults(results, logServer, resultLimit, startLineFilterMap, endLineFilterMap, searchExcludeList);
	}

	private List<LogSearchResult> parseResults(String[] results, LogServerInfo logServer, int resultLimit, Map<String, Long> startLineFilterMap, Map<String, Long> endLineFilterMap,
			Set<String> searchExcludeList) {

		Map<String, LogSearchResult> logSeachResultMap = new LinkedHashMap<String, LogSearchResult>();
		if (ConnectorUtil.hasResults(results)) {
			for (String result : results) {
				if (resultContainsExcludedString(result, searchExcludeList)) {
					continue;
				}
				String[] line = ConnectorUtil.escapeSpecialCharacters(result).split(":", 3);
				if (line.length > 1) {
					String filePath = line[0];
					Long startLineNumber = startLineFilterMap == null ? new Long(0) : startLineFilterMap.get(filePath);
					Long endLineNumber = endLineFilterMap == null ? new Long(Long.MAX_VALUE) : endLineFilterMap.get(filePath);
					Long lineNumber = Long.parseLong(line[1]);
					if (isLineWithinStartAndEndFilter(startLineNumber, endLineNumber, lineNumber)) {
						LogSearchResult logSearchResult = logSeachResultMap.get(filePath);
						if (logSearchResult == null) {
							logSearchResult = new LogSearchResult();
						}
						if (line.length > 2 && (logSearchResult.getResults() == null || logSearchResult.getResults().size() <= resultLimit)) {
							logSearchResult.setFilePath(filePath);
							logSearchResult.setLogServer(logServer);
							logSearchResult.addResult(Long.parseLong(line[1]), line[2]);
							logSeachResultMap.put(filePath, logSearchResult);
						}
					}
				} else {
					if (result.startsWith("Binary file")) {
						// We can skip this line, it's expected
					} else {
						logger.info("Search result is not empty, but doesn't contain ':'...");
						logger.info("-->" + result);
					}
				}
			}
		}

		return new ArrayList<LogSearchResult>(logSeachResultMap.values());
	}
	
	private boolean resultContainsExcludedString(String result, Set<String> searchExcludeList) {
		if (CollectionUtils.isNotEmpty(searchExcludeList)) {
			for (String searchExclusion : searchExcludeList) {
				if (StringUtils.containsIgnoreCase(result, searchExclusion)) {
					return true;
				}
			}
		}
		return false;
	}
	
	//TODO: Why did I create a duplicate method here?
	private List<LogSearchResult> searchUsingCriteria(List<String> filePaths, LogServerInfo logServer, String username, String password, Set<String> searchCriteria, int resultLimit,
			boolean isTask, Map<String, Long> startLineFilterMap, Map<String, Long> endLineFilterMap, DateRangeFilter dateRangeFilter) throws Exception {

		Map<String, LogSearchResult> logSeachResultMap = new LinkedHashMap<String, LogSearchResult>();
		String searchCommand = ConnectorUtil.createCommandToSearchCriteria(filePaths, searchCriteria, dateRangeFilter, resultLimit, isTask);
		String[] results = jSchConnector.executeCommand(logServer.getHost(), username, password, searchCommand);

		if (ConnectorUtil.hasResults(results)) {
			for (String result : results) {
				String[] line = ConnectorUtil.escapeSpecialCharacters(result).split(":", 3);
				if (line.length > 1) {
					String filePath = line[0];
					Long startLineNumber = startLineFilterMap == null ? new Long(0) : startLineFilterMap.get(filePath);
					Long endLineNumber = endLineFilterMap == null ? new Long(Long.MAX_VALUE) : endLineFilterMap.get(filePath);
					Long lineNumber = Long.parseLong(line[1]);
					if (isLineWithinStartAndEndFilter(startLineNumber, endLineNumber, lineNumber)) {
						LogSearchResult logSearchResult = logSeachResultMap.get(filePath);
						if (logSearchResult == null) {
							logSearchResult = new LogSearchResult();
						}
						if (line.length > 2 && (logSearchResult.getResults() == null || logSearchResult.getResults().size() <= resultLimit)) {
							logSearchResult.setFilePath(filePath);
							logSearchResult.setLogServer(logServer);
							logSearchResult.addResult(Long.parseLong(line[1]), line[2]);
							logSeachResultMap.put(filePath, logSearchResult);
						}
					}
				} else {
					if (result.startsWith("Binary file")) {
						// We can skip this line, it's expected
					} else {
						logger.info("Search result is not empty, but doesn't contain ':'...");
						logger.info("-->" + result);
					}
				}
			}
		}

		return new ArrayList<LogSearchResult>(logSeachResultMap.values());
	}

	private boolean isLineWithinStartAndEndFilter(Long startLineNumber, Long endLineNumber, long lineNumber) {
		
		if (startLineNumber != null) {
			if (endLineNumber != null) {
				return startLineNumber.longValue() <= lineNumber && endLineNumber.longValue() >= lineNumber;
			} else {
				return startLineNumber.longValue() <= lineNumber;
			}
		} else {
			if (endLineNumber != null) {
				return endLineNumber.longValue() >= lineNumber;
			}
		}
		
		return false;
	}

	private Map<String, Long> getFileLineNumbersByDate(List<String> filePaths, LogServerInfo logServer, String username, String password, String dateRegex) throws Exception {

		Map<String, Long> fileLineNumberMap = new LinkedHashMap<String, Long>();		
		String[] startLineResults = jSchConnector.executeCommand(logServer.getHost(), username, password, ConnectorUtil.createCommandToFilterOnDate(filePaths, dateRegex));		
		if (ConnectorUtil.hasResults(startLineResults)) {
			for (String startLineResult : startLineResults) {
				String[] logFiles = ConnectorUtil.escapeSpecialCharacters(startLineResult).split(":", 3);
				if (logFiles.length > 1) {
					fileLineNumberMap.put(logFiles[0], Long.valueOf(logFiles[1]));
				} else {
					if (startLineResult.startsWith("Binary file")) {
						// We can skip this line, it's expected
					} else {
						logger.info("Line number result is not empty, but doesn't contain ':'...");
						logger.info("-->" + startLineResult);
					}
				}
			}
		}
		
		return fileLineNumberMap.isEmpty() ? null : fileLineNumberMap;
	}

	@Override
	public List<LineInfo> readLogFile(LogServerInfo logServer, String username, String password, String file, int lineNumber, int linesBefore, int linesAfter) throws Exception {

		List<LineInfo> fileResult = new ArrayList<LineInfo>();
		String wordCountResult = jSchConnector.executeCommand(logServer.getHost(), username, password, ConnectorUtil.getCommandForWordCount(file))[0];
		if (!wordCountResult.matches("[0-9]+")) {
			throw new Exception("File doesn't exist");
		}

		int max = Integer.parseInt(wordCountResult);
		int lineStart = (lineNumber - linesBefore < 0) ? 1 : lineNumber - linesBefore;
		int lineEnd = (lineNumber + linesAfter > max) ? max : lineNumber + linesAfter;
		String sedCommand = ConnectorUtil.getCommandToReadFile(lineStart, lineEnd, file);
		String[] results = jSchConnector.executeCommand(logServer.getHost(), username, password, sedCommand);
		if (ConnectorUtil.hasResults(results)) {
			for (int i = 0; i < results.length; i++) {
				LineInfo lineInfo = new LineInfo();
				lineInfo.setLineContent(results[i]);
				lineInfo.setLineNumber(new Long(lineStart + i));
				fileResult.add(lineInfo);
			}
		}

		return fileResult;
	}
	
	public List<String> readLogFile(LogServerInfo logServer, String username, String password, String file) throws Exception {		
		return jSchConnector.readFile(logServer.getHost(), username, password, file);
	}

	@Override
	public boolean isCredentialValid(String host, String username, String password) {
		return jSchConnector.validateConnection(host, username, password);
	}

	/**
	 * Used in the test case to validate if folders actually exist
	 * 
	 * @param logServer
	 * @param username
	 * @param password
	 * @param filepath
	 * @return
	 * @throws Exception
	 */
	public boolean doesFolderExist(LogServerInfo logServer, String username, String password, String filePath) throws Exception {
		String folderPath = StringUtils.substring(filePath, 0, filePath.lastIndexOf("/"));
		String command = folderPath.endsWith("*") ? getCommandForFolderCheckWithWildCards(folderPath) : getCommandForFolderCheck(folderPath);

		return StringUtils.equals(EXISTS, jSchConnector.executeCommand(logServer.getHost(), username, password, command)[0]);
	}

	private String getCommandForFolderCheck(String folderPath) {
		return "if [ -d " + folderPath + " ]; then echo " + EXISTS + "; fi;";
	}

	private String getCommandForFolderCheckWithWildCards(String folderPath) {
		String folderPathRoot = StringUtils.substring(folderPath, 0, folderPath.lastIndexOf("/"));
		String folderPrefix = StringUtils.substring(folderPath, folderPath.lastIndexOf("/") + 1, folderPath.length());

		return "if [ $(find " + folderPathRoot + " -type d -name " + folderPrefix + " | wc -l ) != 0 ] ; then echo " + EXISTS + "; fi;";
	}

	@Override
	public void downloadFile(LogServerInfo logServer, String username, String password, String file, OutputStream outputStream) throws Exception {
		jSchConnector.sftp(logServer.getHost(), username, password, file, outputStream);
	}

}
