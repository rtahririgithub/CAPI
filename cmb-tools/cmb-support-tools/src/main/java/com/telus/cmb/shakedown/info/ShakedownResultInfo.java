package com.telus.cmb.shakedown.info;

import java.io.Serializable;
import java.util.Date;

public class ShakedownResultInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String testPointName; 
	private String exceptionDetail;
	private boolean pass;
	private Date timestamp;
	private String nodeDetail;
	private String resultDetail;
	private String domainName;
	
	public String getTestPointName() {
		return testPointName;
	}
	public void setTestPointName(String testPointName) {
		this.testPointName = testPointName;
	}
	public String getExceptionDetail() {
		return exceptionDetail;
	}
	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}
	public boolean isPass() {
		return pass;
	}
	public void setPass(boolean pass) {
		this.pass = pass;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getNodeDetail() {
		return nodeDetail;
	}
	public void setNodeDetail(String nodeDetail) {
		this.nodeDetail = nodeDetail;
	}
	public String getResultDetail() {
		return resultDetail;
	}
	public void setResultDetail(String resultDetail) {
		this.resultDetail = resultDetail;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String toString() {
		StringBuffer s = new StringBuffer("{ShakedownResultInfo : ");
		s.append("testPointName=["+testPointName+"]");
		s.append("nodeDetail=["+nodeDetail+"]");
		s.append("timestamp=["+timestamp+"]");
		s.append("pass=["+pass+"]");
		if (pass)
			s.append("resultDetail=["+resultDetail+"]");
		else 
			s.append("exception=["+exceptionDetail+"]}");
		return s.toString();
	}

}
