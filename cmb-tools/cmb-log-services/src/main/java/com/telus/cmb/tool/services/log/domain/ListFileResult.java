package com.telus.cmb.tool.services.log.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListFileResult {

	private DateFormat listLMD = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS Z");

	private LogServerInfo logServer;
	private String folder;
	private List<FileInfo> fileInfoList = new ArrayList<FileInfo>();

	public LogServerInfo getLogServer() {
		return logServer;
	}

	public void setLogServer(LogServerInfo logServer) {
		this.logServer = logServer;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public List<FileInfo> getFileInfoList() {
		return fileInfoList;
	}

	public void setFileInfoList(List<FileInfo> fileInfoList) {
		this.fileInfoList = fileInfoList;
	}

	public void addListResult(String result) throws ParseException {		
		String[] resultParts = result.split("\\s+");
		FileInfo fileInfo = new FileInfo();
		fileInfo.setPermissions(resultParts[0]);
		fileInfo.setFileSize(resultParts[4]);
		String date = resultParts[5];
		String time = resultParts[6].substring(0, resultParts[6].indexOf(".") + 4);
		String timezone = resultParts[7];
		fileInfo.setLastModifiedTime(listLMD.parse(date + " " + time + " " + timezone));
		String filepath = resultParts[8];
		fileInfo.setFileName(filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length()));
		fileInfoList.add(fileInfo);
	}

}
