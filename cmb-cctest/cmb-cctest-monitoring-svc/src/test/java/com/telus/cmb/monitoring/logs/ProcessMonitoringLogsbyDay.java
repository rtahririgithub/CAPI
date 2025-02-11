package com.telus.cmb.monitoring.logs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
public class ProcessMonitoringLogsbyDay {
	
	 FileWriter fileWriter = null;
	 BufferedWriter bufferWriter = null;
	 FileInputStream fstream = null;
     File file;
     String APPLICATION_NAME;
     static String SOURCE_PATH;
     String RESULTS_PATH;
     static String TOTAL_NUM_FILES ;

	private void readConfiguration() {
		// reading configurations from property file
		Properties pro = new Properties();
		try {
			file = new File ("./src/test/com/telus/cmb/monitoring/logs/monitoringlogfiles.properties"); 
			System.out.println("PROPERTY FILE PATH"+file.getCanonicalPath());
			pro.load(new FileInputStream(file));
			APPLICATION_NAME = pro.getProperty("ApplicationName");
			SOURCE_PATH = pro.getProperty("SourcePath");
			RESULTS_PATH = pro.getProperty("DestinationPath");
			TOTAL_NUM_FILES = pro.getProperty("TotalNumOfFiles");
		} catch (Exception e) {
			System.out.println("ERROR OCCRED WHILE READING THE LOGS PROPERTIES FILE"+ e.getMessage());

		}
	}
	public void processLogFiles(String path) throws IOException {
		try {
			fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				System.out.println("s" + strLine);
				if (strLine.contains(APPLICATION_NAME)) {
					String[] tokens = strLine.split(" ");
					System.out.println("Tokens" + tokens[0]);
					writeToFile(strLine, tokens[0]);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	public void writeToFile(String line, String date) {
		String results_path= RESULTS_PATH + "/" + APPLICATION_NAME + " " + date+ ".txt";
			
		try {
			FileWriter fwriter = new FileWriter(results_path, true);
			bufferWriter = new BufferedWriter(fwriter);
			bufferWriter.write(line);
			bufferWriter.append('\n');
			bufferWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
		try {
			ProcessMonitoringLogsbyDay processMonitoringLogsbyDay = new ProcessMonitoringLogsbyDay();
			processMonitoringLogsbyDay.readConfiguration();
			int totalfiles = Integer.parseInt(TOTAL_NUM_FILES);
			for (int i = 0; i < totalfiles; i++) {
				String filepathappender = SOURCE_PATH+"/monitoring_output_rolling.txt" +".";
				filepathappender = filepathappender + i;
				processMonitoringLogsbyDay.processLogFiles(filepathappender);
				System.out.println(filepathappender + "\n");
				
			}
		} catch (Exception e) {
			System.out.println("ERROR OCCRED WHILE PROCESSING THE FILES ");
			e.printStackTrace();
		}
	}

}
