package com.telus.cmb.shakedown.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telus.eas.framework.info.TestPointResultInfo;

public class ShakedownHelper {
	static Logger log = Logger.getLogger(ShakedownHelper.class.getName());
	static BufferedWriter output;
	static File file = null;
	public static void displayResult(List<TestPointResultInfo> testResultList) throws IOException {
		if (testResultList != null) {
			for (TestPointResultInfo testResult : testResultList) {
				displayResult(testResult);
				
			}
		}
	}
	
	public static void displayResult(TestPointResultInfo testResult) {
		
			writetoErrorFile(testResult);
		StringBuffer s = new StringBuffer(128);
		s.append("Test Name=["+testResult.getTestPointName()+"] ");
		if (testResult.isPass()) {
			s.append("OK");
			log.info(s);
		}else {
			s.append("FAILED");
			log.debug(s);
		}
		s.append(" at node=["+testResult.getNodeDetail()+"].\n");
		if (testResult.getResultDetail() != null) {
			s.append(" ResultDetail=["+testResult.getResultDetail()+"]\n");
		}
		if (testResult.getExceptionDetail() != null) {
			s.append("Exception=["+testResult.getExceptionDetail()+"]\n\n");
		}
		if (testResult.isPass()) {
			System.out.println(s.toString());
		}else {
			System.err.println(s.toString());
			log.error(testResult.getExceptionDetail());
		}
	}
	
	public static void writetoErrorFile(TestPointResultInfo testResult) {
		try {
			if (file == null) {
				file = new File(readErrorFilePath("errorFileLocation") + "-"+ testResult.getDomainName().substring(0, 5) + ".txt");	
				output = new BufferedWriter(new FileWriter(file));
				output.append("");
			}
			if (!testResult.isPass()) {
			if(output==null)
		    output = new BufferedWriter(new FileWriter(file));
			
			output.append("Test Name=[" + testResult.getTestPointName() + "] ");
			if (testResult.isPass()) {
				output.append("OK");
			} else {
				output.append("FAILED");
			}
			output.append(" at node=[" + testResult.getNodeDetail() + "]. ");
			if (testResult.getResultDetail() != null) {
				output.append(" ResultDetail=[" + testResult.getResultDetail()	+ "]");				
			}
			if (testResult.getExceptionDetail() != null) {
				output.append("\nException=[" + testResult.getExceptionDetail()+ "]\n\n");				
			}
			if (!testResult.isPass()) {
				output.append(testResult.getExceptionDetail());
			}
			output.newLine();
			output.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				//output.close(); if i close this only one record is writing into file.
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String readErrorFilePath(String key) throws FileNotFoundException,IOException {	
		Properties pro = new Properties();
		File file = new File(".//" + "resources//filepath.properties");
		pro.load(new FileInputStream(file));
		return pro.getProperty("errorFileLocation");
	}

}
