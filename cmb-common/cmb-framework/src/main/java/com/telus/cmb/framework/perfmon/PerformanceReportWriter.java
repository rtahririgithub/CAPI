/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.perfmon;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Pavel Simonovsky
 *
 */
public class PerformanceReportWriter {
	
	private static final Log logger = LogFactory.getLog(PerformanceReportWriter.class);

	private String directoryName;
	
	private String baseName;

	private String extension;

	private String timestampPattern;
	
	private PerformanceMonitor monitor;
	
	private int maxHistoryFiles = 1;
	

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private boolean header = false;
	private static final String headerDetails ="TimeLogged|StartTime|EndTime|GMTOffset|AverageInvocationTime|TotalNumberOfInvocations|HostName|ApplicationId|MethodSignature|"+
												"FirstInvocationTime|LastInvocationTime|MinimumInvocationTime|MaximumInvocationTime|TotalInvocationTime|NumberOfSuccesfullInvocations|NumberOfFailureInvocations \n";

	/**
	 * @return the directoryName
	 */
	public String getDirectoryName() {
		return directoryName;
	}

	/**
	 * @param directoryName the directoryName to set
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	/**
	 * @return the baseName
	 */
	public String getBaseName() {
		return baseName;
	}

	/**
	 * @param baseName the baseName to set
	 */
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	/**
	 * @return the timestampPattern
	 */
	public String getTimestampPattern() {
		return timestampPattern;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @param timestampPattern the timestampPattern to set
	 */
	public void setTimestampPattern(String timestampPattern) {
		this.timestampPattern = timestampPattern;
	}

	/**
	 * @return the monitor
	 */
	public PerformanceMonitor getMonitor() {
		return monitor;
	}

	/**
	 * @param monitor the monitor to set
	 */
	public void setMonitor(PerformanceMonitor monitor) {
		this.monitor = monitor;
	}
	
	/**
	 * @return the maxHistoryFiles
	 */
	public int getMaxHistoryFiles() {
		return maxHistoryFiles;
	}

	/**
	 * @param maxHistoryFiles the maxHistoryFiles to set
	 */
	public void setMaxHistoryFiles(int maxHistoryFiles) {
		this.maxHistoryFiles = maxHistoryFiles;
	}
	

	public void writeReport() {
		
		logger.debug("Generating performance report...");
		
		try {
			
			String fileName = baseName + sdf.format(new Date()) + "." + extension;

			File directory = new File(getDirectoryName());
			if (!directory.exists() && !directory.mkdirs()) {
				logger.error("Unable to access report output directory: " + directory.getAbsolutePath());
				return;
			}
			
			createReportFile(directory, fileName);
			
			cleanupHistory(directory);
			
		} catch (Exception e) {
			logger.error("Error writing report: " + e.getMessage(), e);
		}
	}
	
	private void createReportFile(File directory, String fileName) throws IOException {
		Calendar calendar = Calendar.getInstance();
	    
		if(calendar.get(Calendar.HOUR_OF_DAY)  == 0 && calendar.get(Calendar.MINUTE)  == 0){
			calendar.add(Calendar.DATE, -1);
			fileName = baseName + sdf.format(calendar.getTime()) + "." + extension;
		}
		
		File file = new File(directory, fileName);
		
		if(!file.exists())
			header = true;
		logger.debug("Creating report file: " + file.getAbsolutePath());
		FileWriter writer = new FileWriter(file,true);
		if(header){
			writer.write(headerDetails);
			header = false;
		}
		writer.write(monitor.getTextReport());
		monitor.reset();
		writer.close();
	}
	
	private void cleanupHistory(File directory) throws IOException {
		File[] files = directory.listFiles( new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(baseName);
			}
		});
		
		if (files.length > maxHistoryFiles) {
			Arrays.sort(files, new Comparator<File>() {
				
				@Override
				public int compare(File f1, File f2) {
					return (int) (f1.lastModified() - f2.lastModified());
				}
			});
			
			int endIdx = files.length - maxHistoryFiles;
			
			for (int idx = 0; idx < endIdx; idx++) {
				logger.debug("Deleting history file: " + files[idx]);
				files[idx].delete();
			}
		}
	}
}
