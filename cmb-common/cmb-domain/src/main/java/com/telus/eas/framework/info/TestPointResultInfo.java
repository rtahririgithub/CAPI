package com.telus.eas.framework.info;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Date;

public class TestPointResultInfo implements Serializable {

	private static final long serialVersionUID = -8957024612402951728L;
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
	public void setExceptionDetail(Throwable t) {
		Throwable throwableToPrint = t;
		if (t.getCause() != null) {
			throwableToPrint = t.getCause();
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		throwableToPrint.printStackTrace(ps);
		setExceptionDetail(os.toString());
//		setExceptionDetail(throwableToPrint.toString());
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
	public String toString() {
		StringBuffer s = new StringBuffer(128);
		s.append("Test Name=["+testPointName+"] ");
		s.append("nodeDetail=["+nodeDetail+"]\n");
		s.append("pass=["+pass+"]\n");
		s.append("resultDetail=["+resultDetail+"]\n");
		s.append("timestamp=["+timestamp+"]\n");
		s.append("exception=["+exceptionDetail+"]\n\n");
		return s.toString();
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	

}
