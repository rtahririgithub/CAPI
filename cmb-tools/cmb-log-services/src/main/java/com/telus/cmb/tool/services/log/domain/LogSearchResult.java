package com.telus.cmb.tool.services.log.domain;

import java.util.ArrayList;
import java.util.List;

public class LogSearchResult {

	private String filePath;
	private LogServerInfo logServer;
	private List<LineInfo> results;
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public LogServerInfo getLogServer() {
		return logServer;
	}

	public void setLogServer(LogServerInfo logServer) {
		this.logServer = logServer;
	}

	public List<LineInfo> getResults() {
		return results;
	}
	
	public void setResults(List<LineInfo> results) {
		this.results = results;
	}
	
	public void addResult(Long lineNumber, String lineContent) {
		if (this.results == null) {
			this.results = new ArrayList<LineInfo>();
		}
		LineInfo lineInfo = new LineInfo();
		lineInfo.setLineNumber(lineNumber);
		lineInfo.setLineContent(lineContent);
		this.results.add(lineInfo);
	}
	
}
