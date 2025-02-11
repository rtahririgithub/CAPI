package com.telus.cmb.tool.services.log.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogFilePaths implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LogServerInfo logServer;
	private List<String> filepaths;

	public LogServerInfo getLogServer() {
		return logServer;
	}

	public void setLogServer(LogServerInfo logServer) {
		this.logServer = logServer;
	}

	public List<String> getFilepaths() {
		return filepaths;
	}

	public void setFilepaths(List<String> filepaths) {
		this.filepaths = filepaths;
	}

	public void addFilepath(String filepath) {
		if (filepaths == null) {
			filepaths = new ArrayList<String>();
		}
		filepaths.add(filepath);
	}

	public void addFilepaths(List<String> filepaths) {
		if (this.filepaths == null) {
			this.filepaths = new ArrayList<String>();
		}
		this.filepaths.addAll(filepaths);
	}
	
}
