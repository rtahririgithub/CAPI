package com.telus.logFileReport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class GenerateAPIUsageReport{

	FileWriter fileWriter = null;
	BufferedWriter bufferWriter = null;
	FileInputStream fstream = null;
	File file;
	String FILTER_CRITERIA;
	static String SOURCE_PATH;
	static String CSV_RESULTS_PATH;
	static String EXCEL_RESULTS_PATH;

	private void readConfiguration() {
		// reading configurations from property file
		Properties pro = new Properties();
		try {
			file = new File("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/monitoringlogfiles.properties");
			pro.load(new FileInputStream(file));
			FILTER_CRITERIA = pro.getProperty("filtercriteria");
			SOURCE_PATH = pro.getProperty("sourcefilesPath");
			CSV_RESULTS_PATH = pro.getProperty("csvOutOutputPath");
			EXCEL_RESULTS_PATH = pro.getProperty("excelOutputPath");
			

		} catch (Exception e) {
			System.out.println("ERROR OCCRED WHILE READING THE LOGS PROPERTIES FILE"+ e.getStackTrace());

		}
	}

	public void processLogFiles(String path) throws IOException {
		try {
			
			DataInputStream dataIS = new DataInputStream(new FileInputStream(path));
			BufferedReader br = new BufferedReader(new InputStreamReader(dataIS));
			String[] filtercriteria = FILTER_CRITERIA.split(",");
			
			String strLine;

			// read the whole file line by line..
			
			while ((strLine = br.readLine()) != null) {
				// write to separate file if the current line matches with filter criteria..
				String filter = isCurrentLineMathesWithFilter(strLine, filtercriteria);
				if (filter != null) {
					String[] tokens = strLine.split(" | ");
					System.out.println("processing for Date .." + tokens[0] +" for fliter " +filter);
					writeToFile(strLine, tokens[0], filter);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String isCurrentLineMathesWithFilter(String cuurrentLine, String[] filtercriteria) {
		for (String filter : filtercriteria) {
			if (cuurrentLine.contains(filter)) {
				System.out.println("match line found for filter..." + filter);
				return filter;
			}

		}
		return null;
	}
	
	public void writeToFile(String currentLine, String date, String filter) {
//		String results_path = CSV_RESULTS_PATH + date + "-" + filter + ".csv";
		String results_path = CSV_RESULTS_PATH + "-" + filter + ".csv";
		try {
			FileWriter fwriter = new FileWriter(results_path, true);
			bufferWriter = new BufferedWriter(fwriter);
			bufferWriter.write(currentLine);
			bufferWriter.append('\n');
			bufferWriter.flush();

			// Date| StartTime| EndTime| GMTOffset| avgResponseTime| ExecutionCount| HostName | ApplicationId |getMethodSignature
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		try {

			GenerateAPIUsageReport extractAPIUsageReport = new GenerateAPIUsageReport();
			extractAPIUsageReport.readConfiguration();

			File directory = new File(SOURCE_PATH);
			File[] fileArray = directory.listFiles();

			for (File file : fileArray) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
				    System.out.println("processing file....... "+ file.getName() + files[i].getName());
				    extractAPIUsageReport.processLogFiles(files[i].getPath());

				}
			}
			
			
			
		} catch (Exception e) {
			System.out.println("ERROR OCCRED WHILE PROCESSING THE FILES ");
			e.printStackTrace();
		}
	}

}
