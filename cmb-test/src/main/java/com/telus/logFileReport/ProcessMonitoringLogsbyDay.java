package com.telus.logFileReport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
public class ProcessMonitoringLogsbyDay {

	FileWriter fileWriter = null;
	BufferedWriter bufferWriter = null;
	FileInputStream fstream = null;
	File file;
	String APPLICATION_NAME;
	String filter;
	static String SOURCE_PATH;
	String RESULTS_PATH;
	Map<String,String> methodAndSPNames = new HashMap<String,String>();

	private void readConfiguration() {
		// reading configurations from property file
		Properties pro = new Properties();
		try {
			file = new File("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/subscriberejb_monitoringlogfiles.properties");
			//file = new File("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/referenceejb_monitoringlogfiles.properties");
			//file = new File("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/accountejb_monitoringlogfiles.properties");
			System.out.println("PROPERTY FILE PATH" + file.getCanonicalPath());
			pro.load(new FileInputStream(file));
			SOURCE_PATH = pro.getProperty("SourcePath");
			RESULTS_PATH = pro.getProperty("SourcePath");
			filter = pro.getProperty("filter1");
		} catch (Exception e) {
			System.out.println("ERROR OCCRED WHILE READING THE LOGS PROPERTIES FILE"+ e.getMessage());
		}
	}
	
	private void readStoredProcedureAndMethodNames() throws IOException{
		//fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/subscriberejb_methods.properties");
		//fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/special_perf_methods.properties");
		fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/special_perf_methods.properties");

		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null) {
			String[] values = strLine.trim().split(",");
			if(values.length!=2){
				 System.out.println("readStoredProcedureAndMethodNames error , incorrect method and sp name provided");
					return ;
			}
			methodAndSPNames.put(values[0], values[1]);
		}
	}
	
	public void processLogFiles(String path) throws IOException {
		try {
			fstream = new FileInputStream(path);
			String[] pathValues = path.split(":?\\\\");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
			for (String key: methodAndSPNames.keySet()) {
				if (strLine.contains(key)) {
						System.out.println("filter matches with line " + strLine);
						String[] tokens = strLine.split(" ");
						System.out.println("Tokens" + tokens[0]);
						writeToFile(strLine, key,pathValues[pathValues.length-2]);
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	public void writeToFile(String line,String fileName,String hostNameAndNodeName) {
		String results_path = RESULTS_PATH + "/" + hostNameAndNodeName + "/" + fileName +".txt";
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
			processMonitoringLogsbyDay.readStoredProcedureAndMethodNames();
			File folder = new File(SOURCE_PATH);
			File[] listOfSubFolders = folder.listFiles();
			for (int i = 0; i < listOfSubFolders.length; i++) {
				File[] listOfFiles = listOfSubFolders[i].listFiles();
				for (int j = 0; j < listOfFiles.length; j++) {
					if (listOfFiles[j].isFile() && listOfFiles[j].getName().endsWith("2018-09-12.txt") && listOfFiles[j].getName().contains("perfstats")) {
						System.out.println("current processing file"+ listOfFiles[j].getPath());
						processMonitoringLogsbyDay.processLogFiles(listOfFiles[j].getPath());
					}
				}
			}

		} catch (Exception e) {
			System.out.println("ERROR OCCRED WHILE PROCESSING THE FILES ");
			e.printStackTrace();
		}
	}

}
