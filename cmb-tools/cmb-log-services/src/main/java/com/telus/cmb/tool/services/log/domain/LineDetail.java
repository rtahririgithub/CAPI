package com.telus.cmb.tool.services.log.domain;

public class LineDetail {

	private String filePath;
	private LineInfo lineInfo;

	public LineDetail(String filePath, LineInfo lineInfo) {
		this.filePath = filePath;
		this.lineInfo = lineInfo;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public LineInfo getLineInfo() {
		return lineInfo;
	}

	public void setLineInfo(LineInfo lineInfo) {
		this.lineInfo = lineInfo;
	}

}
